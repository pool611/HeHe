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
	private List<Fragment> fragments; // ÿ��Fragment��Ӧһ��Page
	private FragmentManager fragmentManager;
	private ViewPager viewPager; // viewPager����
	private int currentPageIndex = 0; // ��ǰpage�������л�֮ǰ��

	// ������������
	private JokerIndexContainer titleview;
	// private ImageView mTabline;

	private int mCurrentPageIndex = 0;
	// private int mScreen1_4;

	private OnExtraPageChangeListener onExtraPageChangeListener; // ViewPager�л�ҳ��ʱ�Ķ��⹦����ӽӿ�

	/**
	 * page�л����⹦�ܽӿ�
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
		container.removeView(fragments.get(position).getView()); // �Ƴ�viewpager����֮���page����
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Fragment fragment = fragments.get(position);
		if (!fragment.isAdded()) { // ���fragment��û��added
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.add(fragment, fragment.getClass().getSimpleName());
			ft.commit();
			/**
			 * ����FragmentTransaction.commit()�����ύFragmentTransaction�����
			 * ���ڽ��̵����߳��У����첽�ķ�ʽ��ִ�С� �����Ҫ����ִ������ȴ��еĲ�������Ҫ�������������ֻ�������߳��е��ã���
			 * Ҫע����ǣ����еĻص�����ص���Ϊ��������������б�ִ����ɣ����Ҫ��ϸȷ����������ĵ���λ�á�
			 */
			fragmentManager.executePendingTransactions();
		}

		if (fragment.getView().getParent() == null) {
			container.addView(fragment.getView()); // Ϊviewpager���Ӳ���
		}

		return fragment.getView();
	}

	/**
	 * ��ǰpage�������л�֮ǰ��
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
	 * ����ҳ���л����⹦�ܼ�����
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

		if (null != onExtraPageChangeListener) { // ��������˶��⹦�ܽӿ�
			onExtraPageChangeListener.onExtraPageScrolled(position,
					positionOffset, positionOffsetPx);
		}

		LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) titleview.mTabline
				.getLayoutParams();

		// ����ָʾ����LeftMarginʹ�俴��������ָ����
		if (mCurrentPageIndex == 0 && position == 0) {
			// 0->1 ��������
			lp.leftMargin = (int) (mCurrentPageIndex * titleview.mScreen1_4 + positionOffset
					* titleview.mScreen1_4);
		} else if (mCurrentPageIndex == 1 && position == 0) {
			// 1->0 ���ϼ�С
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
		fragments.get(currentPageIndex).onPause(); // �����л�ǰFargment��onPause()
		// fragments.get(currentPageIndex).onStop(); // �����л�ǰFargment��onStop()
		if (fragments.get(i).isAdded()) {
			// fragments.get(i).onStart(); // �����л���Fargment��onStart()
			fragments.get(i).onResume(); // �����л���Fargment��onResume()
		}
		currentPageIndex = i;

		if (null != onExtraPageChangeListener) { // ��������˶��⹦�ܽӿ�
			onExtraPageChangeListener.onExtraPageSelected(i);
		}

		/**
		 * �����������ı���ɫ
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
		if (null != onExtraPageChangeListener) { // ��������˶��⹦�ܽӿ�
			onExtraPageChangeListener.onExtraPageScrollStateChanged(i);
		}
	}

	/**
	 * @author ������ �����������ı���ɫȫ���ָ��ɺ�ɫ
	 */
	protected void resetTextView() {
		titleview.mJokes1NewestTextView.setTextColor(Color.BLACK);
		titleview.mJokes3WordTextView.setTextColor(Color.BLACK);
		titleview.mJokes2BestTextView.setTextColor(Color.BLACK);
		titleview.mJokes4AudioTextView.setTextColor(Color.BLACK);
	}

	/**
	 * @author ������
	 * 
	 * @param textview
	 *            Ҫ������ɫ���ı���ͼ
	 * @param colorid
	 *            ���ı���ͼ������ɫ
	 */
	private void setTextColor(TextView textview, int colorid) {
		textview.setTextColor(colorid);
	}

}
