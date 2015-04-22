package com.joker;

import java.util.List;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentViewPagerAdapter extends PagerAdapter implements
		ViewPager.OnPageChangeListener {

	private static final int CGREEN = Color.parseColor("#1abc9c");
	private List<Fragment> fragments; // 每个Fragment对应一个Page
	private FragmentManager fragmentManager;
	private ViewPager viewPager; // viewPager对象
	private int currentPageIndex = 0; // 当前page索引（切换之前）

	// 分类栏的内容
	private JokerIndexContainer titleview;
	// private ImageView mTabline;

	private int mCurrentPageIndex = 0;
	// private int mScreen1_4;

	private OnExtraPageChangeListener onExtraPageChangeListener; // ViewPager切换页面时的额外功能添加接口

	/**
	 * page切换额外功能接口
	 */
	static class OnExtraPageChangeListener {
		public void onExtraPageScrolled(int i, float v, int i2) {
		}

		public void onExtraPageSelected(int i) {
		}

		public void onExtraPageScrollStateChanged(int i) {
		}
	}

	public FragmentViewPagerAdapter(JokerIndexContainer titleview,
			FragmentManager fragmentManager, ViewPager viewPager,
			List<Fragment> fragments) {
		this.fragments = fragments;
		this.fragmentManager = fragmentManager;
		this.viewPager = viewPager;
		this.viewPager.setAdapter(this);
		this.viewPager.setOnPageChangeListener(this);
		this.titleview = titleview;

	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object o) {
		return view == o;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(fragments.get(position).getView()); // 移出viewpager两边之外的page布局
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Fragment fragment = fragments.get(position);
		if (!fragment.isAdded()) { // 如果fragment还没有added
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.add(fragment, fragment.getClass().getSimpleName());
			ft.commit();
			/**
			 * 在用FragmentTransaction.commit()方法提交FragmentTransaction对象后
			 * 会在进程的主线程中，用异步的方式来执行。 如果想要立即执行这个等待中的操作，就要调用这个方法（只能在主线程中调用）。
			 * 要注意的是，所有的回调和相关的行为都会在这个调用中被执行完成，因此要仔细确认这个方法的调用位置。
			 */
			fragmentManager.executePendingTransactions();
		}

		if (fragment.getView().getParent() == null) {
			container.addView(fragment.getView()); // 为viewpager增加布局
		}

		return fragment.getView();
	}

	/**
	 * 当前page索引（切换之前）
	 * 
	 * @return
	 */
	public int getCurrentPageIndex() {
		return currentPageIndex;
	}

	public OnExtraPageChangeListener getOnExtraPageChangeListener() {
		return onExtraPageChangeListener;
	}

	/**
	 * 设置页面切换额外功能监听器
	 * 
	 * @param onExtraPageChangeListener
	 */
	public void setOnExtraPageChangeListener(
			OnExtraPageChangeListener onExtraPageChangeListener) {
		this.onExtraPageChangeListener = onExtraPageChangeListener;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPx) {

		if (null != onExtraPageChangeListener) { // 如果设置了额外功能接口
			onExtraPageChangeListener.onExtraPageScrolled(position,
					positionOffset, positionOffsetPx);
		}

		LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) titleview.mTabline
				.getLayoutParams();

		// 设置指示器的LeftMargin使其看起来随手指滑动
		if (mCurrentPageIndex == 0 && position == 0) {
			// 0->1 不断增大
			lp.leftMargin = (int) (mCurrentPageIndex * titleview.mScreen1_4 + positionOffset
					* titleview.mScreen1_4);
		} else if (mCurrentPageIndex == 1 && position == 0) {
			// 1->0 不断减小
			lp.leftMargin = (int) (mCurrentPageIndex * titleview.mScreen1_4 + (positionOffset - 1)
					* titleview.mScreen1_4);
		} else if (mCurrentPageIndex == 1 && position == 1) {
			// 1->2
			lp.leftMargin = (int) (mCurrentPageIndex * titleview.mScreen1_4 + positionOffset
					* titleview.mScreen1_4);
		} else if (mCurrentPageIndex == 2 && position == 1) {
			// 2->1
			lp.leftMargin = (int) (mCurrentPageIndex * titleview.mScreen1_4 + (positionOffset - 1)
					* titleview.mScreen1_4);
		} else if (mCurrentPageIndex == 2 && position == 2) {
			// 2->3
			lp.leftMargin = (int) (mCurrentPageIndex * titleview.mScreen1_4 + positionOffset
					* titleview.mScreen1_4);
		} else if (mCurrentPageIndex == 3 && position == 2) {
			// 3->2
			lp.leftMargin = (int) (mCurrentPageIndex * titleview.mScreen1_4 + (positionOffset - 1)
					* titleview.mScreen1_4);
		}
		titleview.mTabline.setLayoutParams(lp);
	}

	@Override
	public void onPageSelected(int i) {
		fragments.get(currentPageIndex).onPause(); // 调用切换前Fargment的onPause()
		// fragments.get(currentPageIndex).onStop(); // 调用切换前Fargment的onStop()
		if (fragments.get(i).isAdded()) {
			// fragments.get(i).onStart(); // 调用切换后Fargment的onStart()
			fragments.get(i).onResume(); // 调用切换后Fargment的onResume()
		}
		currentPageIndex = i;

		if (null != onExtraPageChangeListener) { // 如果设置了额外功能接口
			onExtraPageChangeListener.onExtraPageSelected(i);
		}

		/**
		 * 调整标题栏文本颜色
		 */
		resetTextView();
		switch (i) {
		case 0: {
			setTextColor(titleview.mJokes1NewestTextView, CGREEN);
			break;
		}
		case 1: {
			setTextColor(titleview.mJokes2BestTextView, CGREEN);
			break;
		}
		case 2: {
			setTextColor(titleview.mJokes3WordTextView, CGREEN);
			break;
		}
		case 3: {
			setTextColor(titleview.mJokes4AudioTextView, CGREEN);
			break;
		}

		}
		mCurrentPageIndex = i;
	}

	@Override
	public void onPageScrollStateChanged(int i) {
		if (null != onExtraPageChangeListener) { // 如果设置了额外功能接口
			onExtraPageChangeListener.onExtraPageScrollStateChanged(i);
		}
	}

	/**
	 * @author 李颜翎 将分类栏的文本颜色全部恢复成黑色
	 */
	protected void resetTextView() {
		titleview.mJokes1NewestTextView.setTextColor(Color.BLACK);
		titleview.mJokes3WordTextView.setTextColor(Color.BLACK);
		titleview.mJokes2BestTextView.setTextColor(Color.BLACK);
		titleview.mJokes4AudioTextView.setTextColor(Color.BLACK);
	}

	/**
	 * @author 李颜翎
	 * 
	 * @param textview
	 *            要设置颜色的文本视图
	 * @param colorid
	 *            给文本视图设置颜色
	 */
	private void setTextColor(TextView textview, int colorid) {
		textview.setTextColor(colorid);
	}

}
