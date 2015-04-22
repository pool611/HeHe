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

//��ͷ��ˢ������listview
public class MyListView extends ListView implements OnScrollListener {

	private static final String TAG = "listview";
	private final static int RELEASE_To_REFRESH = 0;//�ͷ�ˢ��
	private final static int PULL_To_REFRESH = 1;//��������ˢ��
	private final static int REFRESHING = 2;//����ˢ��
	private final static int DONE = 3;//Ĭ��״̬
	private final static int LOADING = 4;
	private final static int RATIO = 3;

	private LayoutInflater inflater;//���ڶ�̬���ز���
	private LinearLayout headView;//����ˢ�²���
	private TextView tipsTextview;//����ˢ������
	private ImageView arrowImageView;//������ͷͼƬ
	private ProgressBar progressBar;//����ԲȦ
	private RotateAnimation animation;//��ͷ��ת����
	private RotateAnimation reverseAnimation;//��ͷת�ض���
	private boolean isRecored;
	private int headContentHeight;//����ˢ��ͷ�����ݸ߶�
	private int startY;//��ָ������ʼY�᳤��
	private int firstItemIndex;//��һ����Ŀ����
	private int state;//ListView״̬
	private boolean isBack;//�����жϼ�ͷ��ת
	private OnRefreshListener refreshListener;//����ˢ�����ݽӿڻص�
	private boolean isRefreshable;//�Ƿ����ˢ��

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
		setCacheColorHint(0x00000000);//ȥ��ListView���϶�����ɫ
		inflater = LayoutInflater.from(context);//��ȡʵ��
		headView = (LinearLayout) inflater.inflate(R.layout.lay_jokes_header, null);//��ȡ����ˢ�²���
		arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);//��ȡ����ˢ�²��ֵļ�ͷͼ��
		arrowImageView.setMinimumWidth(70);//������С���Ϊ70
		arrowImageView.setMinimumHeight(50);//������С�߶�Ϊ50
		progressBar = (ProgressBar) headView// ��ȡ����ˢ�²��ֵ�Բ�ν�����
				.findViewById(R.id.head_progressBar);
		tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);//��ȡ����ˢ����ʾ��ʵ��
		measureView(headView);//������ͼ��ֻ��ִ����÷������ܻ������ͼ���
		headContentHeight = headView.getMeasuredHeight();//��ȡ��ͼ�߶�
		headView.setPadding(0, -1 * headContentHeight, 0, 0);//����padding
		headView.invalidate();//ˢ��view��ʹ�޸Ľ����Ч
		addHeaderView(headView, this, false);//ΪListView���ͷ������ˢ�²���
		setOnScrollListener(this);//���û�������
		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);//���ü�ͷ��ת����
		animation.setInterpolator(new LinearInterpolator());//���ö��������ٵ����ʸı�
		animation.setDuration(250);//���ö�������ʱ��
		animation.setFillAfter(true);//������ִ����Ϻ�view����������ֹλ��
		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);//���ü�ͷת�ض���
		reverseAnimation.setInterpolator(new LinearInterpolator());//���ö��������ٵ����ʸı�
		reverseAnimation.setDuration(200);//���ö�������ʱ��
		reverseAnimation.setFillAfter(true);//������ִ����Ϻ�view����������ֹλ��
		state = DONE;
		isRefreshable = false;
	}

	public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2,
			int arg3) {
		// Log.d("MyListView", "firstVisiableItem" + firstVisiableItem);
		// Log.d("MyListView","visibleItemCount"+arg2);
		// Log.d("MyListView","totalItemCount"+arg3);
		firstItemIndex = firstVisiableItem;//��ȡ��Ŀ����
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

			tipsTextview.setText("�ͷ�����ˢ��...");

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
			tipsTextview.setText("����ˢ��...");

			break;

		case REFRESHING:

			headView.setPadding(0, 0, 0, 0);

			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setText("ƴ��������...");
			// lastUpdatedTextView.setVisibility(View.VISIBLE);

			break;
		case DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);
			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setImageResource(R.drawable.arrow);
			tipsTextview.setText("����ˢ��");
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
