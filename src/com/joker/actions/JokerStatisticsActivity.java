package com.joker.actions;

import com.gen.joker.*;
import com.joker.graphs.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;


/*
 * @author:李颜翎 权限管理
 * @说明：这是BOSS后台统计对应的Activity
 */
public class JokerStatisticsActivity extends Activity {
	private Button btnStatisticsUserRegiet;//注册信息统计
	private Button btnStatisticsJokePost;//发布信息统计
	private Button btnStatisticsUserProportion;//用户比例统计
	private ImageView imvStatisticsClose;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lay_joker_statistics);
		btnStatisticsJokePost=(Button) findViewById(R.id.id_statistics_joke_post);
		btnStatisticsUserRegiet=(Button) findViewById(R.id.id_statistics_user_regist);
		btnStatisticsUserProportion=(Button) findViewById(R.id.id_statistics_user_proportion);
		imvStatisticsClose=(ImageView) findViewById(R.id.statisticsClose);
		initListener();
	}


	private void initListener() {
		//关闭统计页面
		imvStatisticsClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		//笑话发布情况统计监听
		btnStatisticsJokePost.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(JokerStatisticsActivity.this, JokerStatisticsJokePost.class);
				  startActivity(intent);
			}
		});
		
		//用户注册情况监听
		btnStatisticsUserRegiet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				System.out.println("查看注册情况统计");
				Intent intent = new Intent(JokerStatisticsActivity.this, JokerStatisticsUserRegist.class);
				  startActivity(intent);
			}
		});
		
		//用户比例统计监听
		btnStatisticsUserProportion.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				System.out.println("查看用户比例统计");
				Intent intent = new Intent(JokerStatisticsActivity.this, JokerStatisticsUserProportion.class);
				  startActivity(intent);
			}
		});
		
	}

}
