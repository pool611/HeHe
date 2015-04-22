package com.joker.graphs;

/**
 * Copyright (C) 2009 - 2013 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.gen.joker.R;
import com.joker.net.ThreadPoolUtils;
import com.joker.thread.HttpGetThread;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 这个是固定的数据数组填充饼状图 用户注册信息统计，以年份为单位
 * 
 * @author Administrator
 * 
 */
public class JokerStatisticsJokePost extends Activity {

	// 装饰渲染器的标题和数据
	String[] titles;
	List<double[]> x;
	List<double[]> y;
	private static int count = 3;
	private int year;// 当前年份
	private int max_year = 2014;// 最大的年份
	private int min_year = 2011;// 最小的年份
	private int max_value;// 这是取得的12个月数据中最大的数据，用来设置图表中y轴的最大值

	// 图表的布局视图及布局控件
	private GraphicalView mChartView;
	private Button next;
	private Button last;
	private TextView tv_year;
	LinearLayout layout;

	// 设置渲染器的颜色
	private static int[] COLORS = new int[] { Color.GREEN, Color.BLUE,
		Color.MAGENTA, Color.CYAN };
	// 设置渲染器的样式
	private static PointStyle[] STYLES = new PointStyle[] { PointStyle.CIRCLE,
		PointStyle.DIAMOND, PointStyle.POINT, PointStyle.SQUARE,
		PointStyle.TRIANGLE };
	// 渲染器和它的的数据集
	private XYMultipleSeriesDataset mSeriesDataset = new XYMultipleSeriesDataset();// 折线图的数据集
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();// 折线图的主要描绘器
	private ImageView login_close;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 获得布局及各个控件
		setContentView(R.layout.lay_statistics_joke_post);
		last = (Button) findViewById(R.id.id_statistics_joke_post_last);
		next = (Button) findViewById(R.id.id_statistics_joke_post_next);
		tv_year = (TextView) findViewById(R.id.id_statistics_joke_post_year);

		layout = (LinearLayout) findViewById(R.id.lay_statistics_chart_joke_post);
		//监听内容是layout内的，所以只需一次性获得layout并设置监听即可
		setListener();

		/**
		 * 1、这里要取到maxyear和minyear 2、取12个月的注册数据 3、得到的数据放到series里面 4、初始化
		 */

		ThreadPoolUtils.execute(new HttpGetThread(handler1,
				"statistics.php?action=rangejokepostyear"));
		//退出键
		login_close = (ImageView) findViewById(R.id.loginClose);
		login_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	/**
	 * 初始化填充数据和视图内容
	 */
	private void initChart() {
		// 设置标题
		titles = new String[] {"已发布笑话数","未通过审核笑话数", "等待审核笑话数"};
		// 得到数据集
		mSeriesDataset = buildDataset(x, y);
		//设置风格
		setChartStyle();
		//获得图表视图
		mChartView = ChartFactory.getLineChartView(this, mSeriesDataset,
				mRenderer);
		mRenderer.setClickEnabled(true);
		//加载图表视图
		layout.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

	}
	//只有在获得按钮监听是才会清除数据
	private void clearData() {
		if (mChartView != null) {
			x.clear();
			y.clear();
			x = new ArrayList<double[]>();
			y = new ArrayList<double[]>();
			mSeriesDataset.clear();
			mSeriesDataset = new XYMultipleSeriesDataset();
			mRenderer = new XYMultipleSeriesRenderer();
		}
	}

	/**
	 * 给页面上的调整时间按钮设置监听器
	 */
	private void setListener() {
		// 设置+监听器
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (year == max_year) {
					System.out.println("最晚到" + max_year + "有信息");
					Toast.makeText(getApplicationContext(),
							"最晚到" + max_year + "有信息", Toast.LENGTH_SHORT)
							.show();
				} else {
					System.out.println("当前时间：" + year + "     点击+ 查询下一年的情况");
					clearData();
					year++;
					tv_year.setText(year + "");
					ThreadPoolUtils.execute(new HttpGetThread(handler2,
							"statistics.php?action=jokepostcount&year="
									+ year));
				}
			}
		});
		// 设置-监听器
		last.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (year == min_year) {
					System.out.println("最早到" + min_year + " 有信息");
					Toast.makeText(getApplicationContext(),
							"最早到" + min_year + " 有信息", Toast.LENGTH_SHORT)
							.show();
				} else {
					System.out.println("当前时间：" + year + "     点击- 查询上一年的情况");
					clearData();

					year--;
					tv_year.setText(year + "");
					ThreadPoolUtils.execute(new HttpGetThread(handler2,
							"statistics.php?action=jokepostcount&year="
									+ year));
				}
			}
		});
	}

	// 给出标题、xy轴数据 设置数据集
	protected XYMultipleSeriesDataset buildDataset(List xValues, List yValues) {

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int length = titles.length; // 有几条线
		System.out.println("一共有"+length+"条线");

		for (int i = 0; i < length; i++) {// 针对第i条线作如下操作
			XYSeries series = new XYSeries(titles[i]); // 根据每条线的名称创建
			double[] xV = (double[]) xValues.get(i); // 获取第i条线的数据
			double[] yV = (double[]) yValues.get(i);
			int seriesLength = xV.length; // 有几个点
			for (int k = 0; k < seriesLength; k++) // 每条线里有几个点
			{
				series.add(xV[k], yV[k]);
			}
			// 将获得的线数据放到数据集里面
			dataset.addSeries(series);
		}
		return dataset;
	}

	/**
	 * 渲染器设置
	 * 
	 * @param colors
	 * @param styles
	 * @param fill
	 * @return
	 */
	protected XYMultipleSeriesRenderer buildRenderer(int[] colors,
			PointStyle[] styles, boolean fill) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setLineWidth(2);
			r.setPointStyle(styles[i]);
			r.setFillPoints(fill);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}

	/**
	 * 设置颜色样式渲染器属性等
	 */
	protected void setChartStyle() {
		// 颜色设置和折点样式设置
		int[] colors = new int[count];
		PointStyle[] styles = new PointStyle[count];
		for (int i = 0; i < count; i++) {
			colors[i] = COLORS[i % COLORS.length];
			styles[i] = STYLES[i % STYLES.length];
		}
		// 渲染器属性设置
		mRenderer = buildRenderer(colors, styles, false);
		setChartSettings(mRenderer, "笑话发布统计", "Month", "JokeCounts",
				1, 12, 0, max_value, Color.LTGRAY, Color.LTGRAY);

		// 设置折点颜色填充
		int length = mRenderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			((XYSeriesRenderer) mRenderer.getSeriesRendererAt(i))
			.setFillPoints(true);
		}
	}

	/**
	 * 渲染器属性设置
	 * 
	 * @param renderer
	 * @param title
	 * @param xTitle
	 * @param yTitle
	 * @param xMin
	 * @param xMax
	 * @param yMin
	 * @param yMax
	 * @param axesColor
	 * @param labelsColor
	 */
	protected void setChartSettings(XYMultipleSeriesRenderer renderer,
			String title, String xTitle, String yTitle, double xMin,
			double xMax, double yMin, double yMax, int axesColor,
			int labelsColor) {
		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setAxisTitleTextSize(25);
		renderer.setChartTitleTextSize(25);
		renderer.setChartValuesTextSize(20);
		renderer.setLabelsTextSize(30);
		renderer.setLegendTextSize(20);
		renderer.setPointSize(10);
		renderer.setZoomEnabled(true);
		renderer.setZoomButtonsVisible(true);

		renderer.setXLabels(12);
		renderer.setYLabels(10);
		renderer.setShowGrid(true);
		renderer.setXLabelsAlign(Align.RIGHT);
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setZoomButtonsVisible(true);
		renderer.setPanLimits(new double[] { -10, 20, -10, 40 });
		renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });
		renderer.setPanEnabled(false);// 设置是否可以平移
		renderer.setMargins(new int[] { 20, 30, 100, 0 });

		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);
	}

	// 取最早和最晚用户注册时间的handler
	Handler handler1 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(getApplicationContext(), "找不到地址", 1).show();
			} else if (msg.what == 100) {
				Toast.makeText(getApplicationContext(), "传输失败", 1).show();
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null) {
					System.out.println("要获得注册的最早时间和最晚时间     从数据库返回的结果是："
							+ result);
					String[] arr = result.split(",");
					min_year = Integer.parseInt(arr[0]);
					max_year = Integer.parseInt(arr[1]);
					year = max_year;
					tv_year.setText(year + "");
					// 当前年份以最大的年份为准
					System.out.println("当前时间年份year=" + year);
					System.out.println("最小年份min_year=" + min_year
							+ "最大年份max_year=" + max_year);

					ThreadPoolUtils.execute(new HttpGetThread(handler2,
							"statistics.php?action=jokepostcount&year="
									+ year));

				}
			}
		};
	};

	// 取最早和最晚用户注册时间的handler
	Handler handler2 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(getApplicationContext(), "找不到地址", 1).show();
			} else if (msg.what == 100) {
				Toast.makeText(getApplicationContext(), "传输失败", 1).show();
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null) {
					System.out.println("要获得" + year
							+ "年每个月的用户注册信息     从数据库返回的结果是：" + result);
					try {
						JSONArray jsoncounts = new JSONArray(result);
						JSONObject jsontemp = jsoncounts.getJSONObject(0);
						int[] Mon1 = new int[12];
						System.out.println("取pass数据   1");
						Mon1[0] = jsontemp.getInt("January_pass");

						Mon1[1] = jsontemp.getInt("February_pass");
						Mon1[2] = jsontemp.getInt("March_pass");
						Mon1[3] = jsontemp.getInt("April_pass");
						Mon1[4] = jsontemp.getInt("May_pass");
						Mon1[5] = jsontemp.getInt("June_pass");
						Mon1[6] = jsontemp.getInt("July_pass");
						Mon1[7] = jsontemp.getInt("August_pass");
						Mon1[8] = jsontemp.getInt("September_pass");
						Mon1[9] = jsontemp.getInt("October_pass");
						Mon1[10] = jsontemp.getInt("November_pass");
						Mon1[11] = jsontemp.getInt("December_pass");
						int[] Mon2 = new int[12];
						Mon2[0] = jsontemp.getInt("January_uncheck");
						Mon2[1] = jsontemp.getInt("February_uncheck");
						Mon2[2] = jsontemp.getInt("March_uncheck");
						System.out.println("取数据2");
						Mon2[3] = jsontemp.getInt("April_uncheck");
						Mon2[4] = jsontemp.getInt("May_uncheck");
						Mon2[5] = jsontemp.getInt("June_uncheck");
						Mon2[6] = jsontemp.getInt("July_uncheck");
						Mon2[7] = jsontemp.getInt("August_uncheck");
						Mon2[8] = jsontemp.getInt("September_uncheck");
						Mon2[9] = jsontemp.getInt("October_uncheck");
						Mon2[10] = jsontemp.getInt("November_uncheck");
						Mon2[11] = jsontemp.getInt("December_uncheck");

						int[] Mon3 = new int[12];
						Mon3[0] = jsontemp.getInt("January_nopass");
						Mon3[1] = jsontemp.getInt("February_nopass");
						Mon3[2] = jsontemp.getInt("March_nopass");
						Mon3[3] = jsontemp.getInt("April_nopass");
						Mon3[4] = jsontemp.getInt("May_nopass");
						Mon3[5] = jsontemp.getInt("June_nopass");
						Mon3[6] = jsontemp.getInt("July_nopass");
						Mon3[7] = jsontemp.getInt("August_nopass");
						Mon3[8] = jsontemp.getInt("September_nopass");
						Mon3[9] = jsontemp.getInt("October_nopass");
						Mon3[10] = jsontemp.getInt("November_nopass");
						Mon3[11] = jsontemp.getInt("December_nopass");

						System.out.println("取数据3");
						System.out.println(year + "年 注册人数如下");
						max_value = 0;
						for(int i=0;i<12;i++)
						{
							if(Mon1[i]>max_value)
								max_value=Mon1[i];
							if(Mon2[i]>max_value)
								max_value=Mon2[i];
							if(Mon3[i]>max_value)
								max_value=Mon3[i];
						}

						System.out.println("获得的数值中最大值为：" + max_value);
						max_value *= 1.1;
						System.out.println("最大值*1.1=" + max_value);

						x = new ArrayList<double[]>();
						y = new ArrayList<double[]>();

						x.add(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
								12 });
						x.add(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
								12 });
						x.add(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
								12 });


						y.add(new double[] { Mon1[0], Mon1[1], Mon1[2], Mon1[3],
								Mon1[4], Mon1[5], Mon1[6], Mon1[7], Mon1[8], Mon1[9],
								Mon1[10], Mon1[11] });
						y.add(new double[] { Mon2[0], Mon2[1], Mon2[2], Mon2[3],
								Mon2[4], Mon2[5], Mon2[6], Mon2[7], Mon2[8], Mon2[9],
								Mon2[10], Mon2[11] });
						y.add(new double[] { Mon3[0], Mon3[1], Mon3[2], Mon3[3],
								Mon3[4], Mon3[5], Mon3[6], Mon3[7], Mon3[8], Mon3[9],
								Mon3[10], Mon3[11] });


						Toast.makeText(getApplicationContext(), year + "年",
								Toast.LENGTH_SHORT).show();

						layout.removeAllViews();

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}
			initChart();
		};
	};
}
