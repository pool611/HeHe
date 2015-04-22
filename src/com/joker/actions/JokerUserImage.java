package com.joker.actions;
/*
 * @author:王亚伟 
 * @说明：更换头像的Activity
 * */
import com.gen.joker.R;
import com.joker.model.Model;
import com.joker.net.ThreadPoolUtils;
import com.joker.thread.HttpPostThread;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class JokerUserImage extends Activity {
	private Context ctx;
	private ImageView login_close;
	private ImageView headimage1;
	private ImageView headimage2;
	private ImageView headimage3;
	private ImageView headimage4;
	private ImageView headimage5;
	private ImageView headimage6;
	private ImageView headimage7;
	private ImageView headimage8;
	private ImageView headimage9;
	private String value = null;
	private String userHeadImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_head_image);
		ctx=JokerUserImage.this;
		//退出键
		login_close = (ImageView) findViewById(R.id.loginClose);
		login_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		//获取默认头像
		getImage();
		//设置监听事件
		setImage();

	}
	private void getImage(){
		headimage1=(ImageView) findViewById(R.id.user_head_image1);
		headimage2=(ImageView) findViewById(R.id.user_head_image2);
		headimage3=(ImageView) findViewById(R.id.user_head_image3);
		headimage4=(ImageView) findViewById(R.id.user_head_image4);
		headimage5=(ImageView) findViewById(R.id.user_head_image5);
		headimage6=(ImageView) findViewById(R.id.user_head_image6);
		headimage7=(ImageView) findViewById(R.id.user_head_image7);
		headimage8=(ImageView) findViewById(R.id.user_head_image8);
		headimage9=(ImageView) findViewById(R.id.user_head_image9);
	}
	private void setImage(){
		headimage1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userHeadImage="headimage1";
				//设置本地
				Model.MYUSERINFO.setHead("headimage1");
				// 上传到数据库
				value = "{\"uid\":\""
						+ Model.MYUSERINFO.getUserID()
						+ "\",\"head\":\"" + userHeadImage
						+ "\"}";
				ThreadPoolUtils.execute(new HttpPostThread(
						handler, Model.HEADIMAGE, value));
				Toast.makeText(ctx, "更换头像成功", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		headimage2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userHeadImage="headimage2";
				//设置本地
				Model.MYUSERINFO.setHead("headimage2");
				// 上传到数据库
				value = "{\"uid\":\""
						+ Model.MYUSERINFO.getUserID()
						+ "\",\"head\":\"" + userHeadImage
						+ "\"}";
				ThreadPoolUtils.execute(new HttpPostThread(
						handler, Model.HEADIMAGE, value));
				Toast.makeText(ctx, "更换头像成功", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		headimage3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userHeadImage="headimage3";
				//设置本地
				Model.MYUSERINFO.setHead("headimage3");
				// 上传到数据库
				value = "{\"uid\":\""
						+ Model.MYUSERINFO.getUserID()
						+ "\",\"head\":\"" + userHeadImage
						+ "\"}";
				ThreadPoolUtils.execute(new HttpPostThread(
						handler, Model.HEADIMAGE, value));
				Toast.makeText(ctx, "更换头像成功", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		headimage4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userHeadImage="headimage4";
				//设置本地
				Model.MYUSERINFO.setHead("headimage4");
				// 上传到数据库
				value = "{\"uid\":\""
						+ Model.MYUSERINFO.getUserID()
						+ "\",\"head\":\"" + userHeadImage
						+ "\"}";
				ThreadPoolUtils.execute(new HttpPostThread(
						handler, Model.HEADIMAGE, value));
				Toast.makeText(ctx, "更换头像成功", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		headimage5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userHeadImage="headimage5";
				//设置本地
				Model.MYUSERINFO.setHead("headimage5");
				// 上传到数据库
				value = "{\"uid\":\""
						+ Model.MYUSERINFO.getUserID()
						+ "\",\"head\":\"" + userHeadImage
						+ "\"}";
				ThreadPoolUtils.execute(new HttpPostThread(
						handler, Model.HEADIMAGE, value));
				Toast.makeText(ctx, "更换头像成功", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		headimage6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userHeadImage="headimage6";
				//设置本地
				Model.MYUSERINFO.setHead("headimage6");
				// 上传到数据库
				value = "{\"uid\":\""
						+ Model.MYUSERINFO.getUserID()
						+ "\",\"head\":\"" + userHeadImage
						+ "\"}";
				ThreadPoolUtils.execute(new HttpPostThread(
						handler, Model.HEADIMAGE, value));
				Toast.makeText(ctx, "更换头像成功", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		headimage7.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userHeadImage="headimage7";
				//设置本地
				Model.MYUSERINFO.setHead("headimage7");
				// 上传到数据库
				value = "{\"uid\":\""
						+ Model.MYUSERINFO.getUserID()
						+ "\",\"head\":\"" + userHeadImage
						+ "\"}";
				ThreadPoolUtils.execute(new HttpPostThread(
						handler, Model.HEADIMAGE, value));
				Toast.makeText(ctx, "更换头像成功", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		headimage8.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userHeadImage="headimage8";
				//设置本地
				Model.MYUSERINFO.setHead("headimage8");
				// 上传到数据库
				value = "{\"uid\":\""
						+ Model.MYUSERINFO.getUserID()
						+ "\",\"head\":\"" + userHeadImage
						+ "\"}";
				ThreadPoolUtils.execute(new HttpPostThread(
						handler, Model.HEADIMAGE, value));
				Toast.makeText(ctx, "更换头像成功", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		headimage9.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userHeadImage="headimage9";
				//设置本地
				Model.MYUSERINFO.setHead("headimage9");
				// 上传到数据库
				value = "{\"uid\":\""
						+ Model.MYUSERINFO.getUserID()
						+ "\",\"head\":\"" + userHeadImage
						+ "\"}";
				ThreadPoolUtils.execute(new HttpPostThread(
						handler, Model.HEADIMAGE, value));
				Toast.makeText(ctx, "更换头像成功", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(getBaseContext(), "请求失败，服务器故障", 1).show();
			} else if (msg.what == 100) {
				Toast.makeText(getBaseContext(), "服务器无响应", 1).show();
			} else if (msg.what == 200) {
				String result = msg.obj.toString();
				System.out.println(result);
			}
		}
	};
}
