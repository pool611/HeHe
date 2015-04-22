package com.joker;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author 李颜翎
 * @说明 把分类栏的内容抽取成一个类，使得内容便于传递
 */
public class JokerIndexContainer {
	public TextView mJokes1NewestTextView;// 分类栏“最新”文本视图
	public TextView mJokes3WordTextView;// 分类栏“纯文”文本视图
	public TextView mJokes2BestTextView;// 分类栏“最赞”文本视图
	public TextView mJokes4AudioTextView;// 分类栏“语音”文本视图
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
