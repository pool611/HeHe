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
 * ������ͳ���û�������ͼ��
 * �����û������͹���Ա����
 * �Ѿ�������ݼ���
 * @author ������
 */
public class JokerStatisticsUserProportion extends Activity {

	// ��ɫ����
	private static int[] COLORS = new int[] { Color.GREEN, Color.BLUE,
		Color.MAGENTA, Color.CYAN };
	// ����
	private CategorySeries mSeries = new CategorySeries("");
	// ��Ⱦ��
	private DefaultRenderer mRenderer = new DefaultRenderer();// ��״ͼ����Ҫ�����
	// ��ͼ
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
		// ���ò���
		setContentView(R.layout.lay_statistics_user_proportion);
		//�������ݿ��ȡ����
		ThreadPoolUtils.execute(new HttpGetThread(handler, "statistics.php?action=userproportion"));
		// ������Ⱦ��
		setRenderer();
		// ������ɫ
		setColor();
		//�˳���
		login_close = (ImageView) findViewById(R.id.loginClose);
		login_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	// ������Ⱦ������
	private void setRenderer() {
		mRenderer.setChartTitle("�û�����ͳ�ƽ��");// ����ͼ����⣬Ĭ�Ͼ��ж�����ʾ
		mRenderer.setChartTitleTextSize(50);
		mRenderer.setBackgroundColor(Color.BLACK);
		mRenderer.setLabelsColor(Color.WHITE);

		mRenderer.setLabelsTextSize(40);// ͼ�������ֵĴ�С
		//		mRenderer.setLabelsColor(Color.WHITE);// ��ͼ�ϱ�����ֵ���ɫ

		mRenderer.setPanEnabled(false);// �����Ƿ����ƽ��
		mRenderer.setDisplayValues(true);// �Ƿ���ʾֵ
		mRenderer.setClickEnabled(true);// �����Ƿ���Ա����
		mRenderer.setLegendTextSize(40);// ͼ�����½Ǳ�ע���ֵĴ�С

		mRenderer.setZoomButtonsVisible(true);// ����ͼ���С������ť
		mRenderer.setStartAngle(180);// ����Ϊˮƽ��ʼ
		mRenderer.setDisplayValues(true);// ��ʾ����
	}


	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(getApplicationContext(), "�Ҳ�����ַ", 1).show();
			} else if (msg.what == 100) {
				Toast.makeText(getApplicationContext(), "����ʧ��", 1).show();
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null) {
					System.out.println("������");
					try {
						JSONArray jsoncounts = new JSONArray(result);
						JSONObject jsontemp = jsoncounts.getJSONObject(0);
						int userc = jsontemp.getInt("user");
						int adminc = jsontemp.getInt("adminuser");

						mSeries = new CategorySeries("");
						mSeries.add("�û�", userc);
						mSeries.add("����Ա", adminc);
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}
			initChart();
		};
	};

	// ����Ⱦ��������ɫ
	private void setColor() {
		System.out.println("������ɫ");
		SimpleSeriesRenderer r;
		r = new SimpleSeriesRenderer();
		r.setColor(Color.BLUE);
		mRenderer.addSeriesRenderer(r);
		r = new SimpleSeriesRenderer();
		r.setColor(Color.GREEN);
		mRenderer.addSeriesRenderer(r);
	}

	// ��ñ�״ͼ�ĵ����¼������и�����ʾ

	protected void initChart() {
		super.onResume();
		if (mChartView == null) {
			chartlayout = (LinearLayout) findViewById(R.id.lay_statistics_chart_user_proportion);
			// ���ͼ����ͼ
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
