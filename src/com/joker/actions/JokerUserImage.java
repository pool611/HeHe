package com.joker.actions;
/*
 * @author:����ΰ 
 * @˵��������ͷ���Activity
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
		//�˳���
		login_close = (ImageView) findViewById(R.id.loginClose);
		login_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		//��ȡĬ��ͷ��
		getImage();
		//���ü����¼�
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
				//���ñ���
				Model.MYUSERINFO.setHead("headimage1");
				// �ϴ������ݿ�
				value = "{\"uid\":\""
						+ Model.MYUSERINFO.getUserID()
						+ "\",\"head\":\"" + userHeadImage
						+ "\"}";
				ThreadPoolUtils.execute(new HttpPostThread(
						handler, Model.HEADIMAGE, value));
				Toast.makeText(ctx, "����ͷ��ɹ�", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		headimage2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userHeadImage="headimage2";
				//���ñ���
				Model.MYUSERINFO.setHead("headimage2");
				// �ϴ������ݿ�
				value = "{\"uid\":\""
						+ Model.MYUSERINFO.getUserID()
						+ "\",\"head\":\"" + userHeadImage
						+ "\"}";
				ThreadPoolUtils.execute(new HttpPostThread(
						handler, Model.HEADIMAGE, value));
				Toast.makeText(ctx, "����ͷ��ɹ�", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		headimage3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userHeadImage="headimage3";
				//���ñ���
				Model.MYUSERINFO.setHead("headimage3");
				// �ϴ������ݿ�
				value = "{\"uid\":\""
						+ Model.MYUSERINFO.getUserID()
						+ "\",\"head\":\"" + userHeadImage
						+ "\"}";
				ThreadPoolUtils.execute(new HttpPostThread(
						handler, Model.HEADIMAGE, value));
				Toast.makeText(ctx, "����ͷ��ɹ�", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		headimage4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userHeadImage="headimage4";
				//���ñ���
				Model.MYUSERINFO.setHead("headimage4");
				// �ϴ������ݿ�
				value = "{\"uid\":\""
						+ Model.MYUSERINFO.getUserID()
						+ "\",\"head\":\"" + userHeadImage
						+ "\"}";
				ThreadPoolUtils.execute(new HttpPostThread(
						handler, Model.HEADIMAGE, value));
				Toast.makeText(ctx, "����ͷ��ɹ�", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		headimage5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userHeadImage="headimage5";
				//���ñ���
				Model.MYUSERINFO.setHead("headimage5");
				// �ϴ������ݿ�
				value = "{\"uid\":\""
						+ Model.MYUSERINFO.getUserID()
						+ "\",\"head\":\"" + userHeadImage
						+ "\"}";
				ThreadPoolUtils.execute(new HttpPostThread(
						handler, Model.HEADIMAGE, value));
				Toast.makeText(ctx, "����ͷ��ɹ�", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		headimage6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userHeadImage="headimage6";
				//���ñ���
				Model.MYUSERINFO.setHead("headimage6");
				// �ϴ������ݿ�
				value = "{\"uid\":\""
						+ Model.MYUSERINFO.getUserID()
						+ "\",\"head\":\"" + userHeadImage
						+ "\"}";
				ThreadPoolUtils.execute(new HttpPostThread(
						handler, Model.HEADIMAGE, value));
				Toast.makeText(ctx, "����ͷ��ɹ�", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		headimage7.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userHeadImage="headimage7";
				//���ñ���
				Model.MYUSERINFO.setHead("headimage7");
				// �ϴ������ݿ�
				value = "{\"uid\":\""
						+ Model.MYUSERINFO.getUserID()
						+ "\",\"head\":\"" + userHeadImage
						+ "\"}";
				ThreadPoolUtils.execute(new HttpPostThread(
						handler, Model.HEADIMAGE, value));
				Toast.makeText(ctx, "����ͷ��ɹ�", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		headimage8.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userHeadImage="headimage8";
				//���ñ���
				Model.MYUSERINFO.setHead("headimage8");
				// �ϴ������ݿ�
				value = "{\"uid\":\""
						+ Model.MYUSERINFO.getUserID()
						+ "\",\"head\":\"" + userHeadImage
						+ "\"}";
				ThreadPoolUtils.execute(new HttpPostThread(
						handler, Model.HEADIMAGE, value));
				Toast.makeText(ctx, "����ͷ��ɹ�", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		headimage9.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				userHeadImage="headimage9";
				//���ñ���
				Model.MYUSERINFO.setHead("headimage9");
				// �ϴ������ݿ�
				value = "{\"uid\":\""
						+ Model.MYUSERINFO.getUserID()
						+ "\",\"head\":\"" + userHeadImage
						+ "\"}";
				ThreadPoolUtils.execute(new HttpPostThread(
						handler, Model.HEADIMAGE, value));
				Toast.makeText(ctx, "����ͷ��ɹ�", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(getBaseContext(), "����ʧ�ܣ�����������", 1).show();
			} else if (msg.what == 100) {
				Toast.makeText(getBaseContext(), "����������Ӧ", 1).show();
			} else if (msg.what == 200) {
				String result = msg.obj.toString();
				System.out.println(result);
			}
		}
	};
}
