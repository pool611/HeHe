package com.joker;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author ������
 * @˵�� �ѷ����������ݳ�ȡ��һ���࣬ʹ�����ݱ��ڴ���
 */
public class JokerIndexContainer {
	public TextView mJokes1NewestTextView;// �����������¡��ı���ͼ
	public TextView mJokes3WordTextView;// �����������ġ��ı���ͼ
	public TextView mJokes2BestTextView;// �����������ޡ��ı���ͼ
	public TextView mJokes4AudioTextView;// ���������������ı���ͼ
	public ImageView mTabline;
	public int mScreen1_4;

	JokerIndexContainer(Context ctx) {
		mJokes1NewestTextView = new TextView(ctx);
		mJokes3WordTextView = new TextView(ctx);
		mJokes2BestTextView = new TextView(ctx);
		mJokes4AudioTextView = new TextView(ctx);
		mTabline = new ImageView(ctx);
	}

}
