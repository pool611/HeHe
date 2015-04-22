package com.joker.graphs;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gen.joker.R;
import com.joker.info.JokeInfo;
import com.joker.net.ThreadPoolUtils;
import com.joker.thread.HttpGetThread;
import com.joker.utils.MyJson;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 这里是统计用户比例的图表
 * 给出用户人数和管理员人数
 * 已经完成数据加载
 * @author 李颜翎
 */
public class JokerStatisticsUserProportion extends Activity {

	// 颜色设置
	private static int[] COLORS = new int[] { Color.GREEN, Color.BLUE,
		Color.MAGENTA, Color.CYAN };
	// 数据
	private CategorySeries mSeries = new CategorySeries("");
	// 渲染器
	private DefaultRenderer mRenderer = new DefaultRenderer();// 饼状图的主要描绘器
	// 视图
	private GraphicalView mChartView;
	private LinearLayout chartlayout;
	private ImageView login_close;

	@Override
	protected void onRestoreInstanceState(Bundle savedState) {
		super.onRestoreInstanceState(savedState);
		mSeries = (CategorySeries) savedState.getSerializable("current_series");
		mRenderer = (DefaultRenderer) savedState
				.getSerializable("current_renderer");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("current_series", mSeries);
		outState.putSerializable("current_renderer", mRenderer);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置布局
		setContentView(R.layout.lay_statistics_user_proportion);
		//访问数据库获取数据
		ThreadPoolUtils.execute(new HttpGetThread(handler, "statistics.php?action=userproportion"));
		// 设置渲染器
		setRenderer();
		// 设置颜色
		setColor();
		//退出键
		login_close = (ImageView) findViewById(R.id.loginClose);
		login_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	// 设置渲染器属性
	private void setRenderer() {
		mRenderer.setChartTitle("用户比例统计结果");// 设置图表标题，默认居中顶部显示
		mRenderer.setChartTitleTextSize(50);
		mRenderer.setBackgroundColor(Color.BLACK);
		mRenderer.setLabelsColor(Color.WHITE);

		mRenderer.setLabelsTextSize(40);// 图表标记文字的大小
		//		mRenderer.setLabelsColor(Color.WHITE);// 饼图上标记文字的颜色

		mRenderer.setPanEnabled(false);// 设置是否可以平移
		mRenderer.setDisplayValues(true);// 是否显示值
		mRenderer.setClickEnabled(true);// 设置是否可以被点击
		mRenderer.setLegendTextSize(40);// 图表左下角表注文字的大小

		mRenderer.setZoomButtonsVisible(true);// 设置图表大小调整按钮
		mRenderer.setStartAngle(180);// 设置为水平开始
		mRenderer.setDisplayValues(true);// 显示数据
	}


	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(getApplicationContext(), "找不到地址", 1).show();
			} else if (msg.what == 100) {
				Toast.makeText(getApplicationContext(), "传输失败", 1).show();
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null) {
					System.out.println("放数据");
					try {
						JSONArray jsoncounts = new JSONArray(result);
						JSONObject jsontemp = jsoncounts.getJSONObject(0);
						int userc = jsontemp.getInt("user");
						int adminc = jsontemp.getInt("adminuser");

						mSeries = new CategorySeries("");
						mSeries.add("用户", userc);
						mSeries.add("管理员", adminc);
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}
			initChart();
		};
	};

	// 给渲染器设置颜色
	private void setColor() {
		System.out.println("设置颜色");
		SimpleSeriesRenderer r;
		r = new SimpleSeriesRenderer();
		r.setColor(Color.BLUE);
		mRenderer.addSeriesRenderer(r);
		r = new SimpleSeriesRenderer();
		r.setColor(Color.GREEN);
		mRenderer.addSeriesRenderer(r);
	}

	// 获得饼状图的单击事件并进行高亮显示

	protected void initChart() {
		super.onResume();
		if (mChartView == null) {
			chartlayout = (LinearLayout) findViewById(R.id.lay_statistics_chart_user_proportion);
			// 获得图表视图
			System.out.println(mSeries.getItemCount());
			mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);

			mRenderer.setClickEnabled(true);
			mChartView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					SeriesSelection seriesSelection = mChartView
							.getCurrentSeriesAndPoint();
					if (seriesSelection == null) {
						Toast.makeText(JokerStatisticsUserProportion.this,
								"No chart element selected", Toast.LENGTH_SHORT)
								.show();
					} else {
						for (int i = 0; i < mSeries.getItemCount(); i++) {
							mRenderer.getSeriesRendererAt(i).setHighlighted(
									i == seriesSelection.getPointIndex());
						}
						mChartView.repaint();
						Toast.makeText(
								JokerStatisticsUserProportion.this,
								"Chart data point index "
										+ seriesSelection.getPointIndex()
										+ " selected" + " point value="
										+ seriesSelection.getValue(),
										Toast.LENGTH_SHORT).show();
					}
				}
			});
			chartlayout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));

		} else {
			mChartView.repaint();
		}
	}

}
