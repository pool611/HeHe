package com.joker.myview;


import com.gen.joker.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

//带头部刷新条的listview
public class MyListView extends ListView implements OnScrollListener {

	private static final String TAG = "listview";
	private final static int RELEASE_To_REFRESH = 0;//释放刷新
	private final static int PULL_To_REFRESH = 1;//正处下拉刷新
	private final static int REFRESHING = 2;//正在刷新
	private final static int DONE = 3;//默认状态
	private final static int LOADING = 4;
	private final static int RATIO = 3;

	private LayoutInflater inflater;//用于动态加载布局
	private LinearLayout headView;//下拉刷新布局
	private TextView tipsTextview;//下拉刷新文字
	private ImageView arrowImageView;//下拉箭头图片
	private ProgressBar progressBar;//加载圆圈
	private RotateAnimation animation;//箭头翻转动画
	private RotateAnimation reverseAnimation;//箭头转回动画
	private boolean isRecored;
	private int headContentHeight;//下拉刷新头部内容高度
	private int startY;//手指触摸起始Y轴长度
	private int firstItemIndex;//第一个项目索引
	private int state;//ListView状态
	private boolean isBack;//用于判断箭头翻转
	private OnRefreshListener refreshListener;//用于刷新数据接口回调
	private boolean isRefreshable;//是否可以刷新

	public MyListView(Context context) {
		super(context);
		init(context);
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	private void init(Context context) {
		setCacheColorHint(0x00000000);//去除ListView的拖动背景色
		inflater = LayoutInflater.from(context);//获取实例
		headView = (LinearLayout) inflater.inflate(R.layout.lay_jokes_header, null);//获取下拉刷新布局
		arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);//获取下拉刷新布局的箭头图像
		arrowImageView.setMinimumWidth(70);//设置最小宽度为70
		arrowImageView.setMinimumHeight(50);//设置最小高度为50
		progressBar = (ProgressBar) headView// 获取下拉刷新布局的圆形进度条
				.findViewById(R.id.head_progressBar);
		tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);//获取下拉刷新提示语实例
		measureView(headView);//测量视图，只有执行完该方法才能获得其视图宽高
		headContentHeight = headView.getMeasuredHeight();//获取视图高度
		headView.setPadding(0, -1 * headContentHeight, 0, 0);//设置padding
		headView.invalidate();//刷新view，使修改结果生效
		addHeaderView(headView, this, false);//为ListView添加头部下拉刷新布局
		setOnScrollListener(this);//设置滑动监听
		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);//设置箭头翻转动画
		animation.setInterpolator(new LinearInterpolator());//设置动画以匀速的速率改变
		animation.setDuration(250);//设置动画持续时间
		animation.setFillAfter(true);//当动画执行完毕后，view对象保留在终止位置
		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);//设置箭头转回动画
		reverseAnimation.setInterpolator(new LinearInterpolator());//设置动画以匀速的速率改变
		reverseAnimation.setDuration(200);//设置动画持续时间
		reverseAnimation.setFillAfter(true);//当动画执行完毕后，view对象保留在终止位置
		state = DONE;
		isRefreshable = false;
	}

	public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2,
			int arg3) {
		// Log.d("MyListView", "firstVisiableItem" + firstVisiableItem);
		// Log.d("MyListView","visibleItemCount"+arg2);
		// Log.d("MyListView","totalItemCount"+arg3);
		firstItemIndex = firstVisiableItem;//获取项目索引
	}

	public void onScrollStateChanged(AbsListView arg0, int arg1) {
	}

	public boolean onTouchEvent(MotionEvent event) {

		if (isRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (firstItemIndex == 0 && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
				}
				break;

			case MotionEvent.ACTION_UP:

				if (state != REFRESHING && state != LOADING) {
					if (state == DONE) {

					}
					if (state == PULL_To_REFRESH) {
						state = DONE;
						changeHeaderViewByState();
					}
					if (state == RELEASE_To_REFRESH) {
						state = REFRESHING;
						changeHeaderViewByState();
						onRefresh();

					}
				}

				isRecored = false;
				isBack = false;

				break;

			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();

				if (!isRecored && firstItemIndex == 0) {

					isRecored = true;
					startY = tempY;
				}
				if (state != REFRESHING && isRecored && state != LOADING) {
					if (state == RELEASE_To_REFRESH) {
						setSelection(0);
						if (((tempY - startY) / RATIO < headContentHeight)
								&& (tempY - startY) > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						} else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
						}
					}

					if (state == PULL_To_REFRESH) {

						setSelection(0);

						if ((tempY - startY) / RATIO >= headContentHeight) {
							state = RELEASE_To_REFRESH;
							isBack = true;
							changeHeaderViewByState();

						}

						else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();

						}
					}

					if (state == DONE) {
						if (tempY - startY > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
					}

					if (state == PULL_To_REFRESH) {
						headView.setPadding(0, -1 * headContentHeight
								+ (tempY - startY) / RATIO, 0, 0);

					}

					if (state == RELEASE_To_REFRESH) {
						headView.setPadding(0, (tempY - startY) / RATIO
								- headContentHeight, 0, 0);
					}

				}

				break;
			}
		}

		return super.onTouchEvent(event);
	}

	private void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_To_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			// lastUpdatedTextView.setVisibility(View.VISIBLE);

			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);

			tipsTextview.setText("释放立即刷新...");

			break;
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			// lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);

			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);
			}
			tipsTextview.setText("下拉刷新...");

			break;

		case REFRESHING:

			headView.setPadding(0, 0, 0, 0);

			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setText("拼命加载中...");
			// lastUpdatedTextView.setVisibility(View.VISIBLE);

			break;
		case DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);
			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setImageResource(R.drawable.arrow);
			tipsTextview.setText("下拉刷新");
			// lastUpdatedTextView.setVisibility(View.VISIBLE);
			break;
		}
	}

	public void setonRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}

	public interface OnRefreshListener {
		public void onRefresh();
	}

	public void onRefreshComplete() {
		state = DONE;
		changeHeaderViewByState();
	}

	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}

	private void measureView(View child) {

		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

}
