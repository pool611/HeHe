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
 * @author:������ Ȩ�޹���
 * @˵��������BOSS��̨ͳ�ƶ�Ӧ��Activity
 */
public class JokerStatisticsActivity extends Activity {
	private Button btnStatisticsUserRegiet;//ע����Ϣͳ��
	private Button btnStatisticsJokePost;//������Ϣͳ��
	private Button btnStatisticsUserProportion;//�û�����ͳ��
	private ImageView imvStatisticsClose;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
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
		//�ر�ͳ��ҳ��
		imvStatisticsClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		//Ц���������ͳ�Ƽ���
		btnStatisticsJokePost.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(JokerStatisticsActivity.this, JokerStatisticsJokePost.class);
				  startActivity(intent);
			}
		});
		
		//�û�ע���������
		btnStatisticsUserRegiet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				System.out.println("�鿴ע�����ͳ��");
				Intent intent = new Intent(JokerStatisticsActivity.this, JokerStatisticsUserRegist.class);
				  startActivity(intent);
			}
		});
		
		//�û�����ͳ�Ƽ���
		btnStatisticsUserProportion.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				System.out.println("�鿴�û�����ͳ��");
				Intent intent = new Intent(JokerStatisticsActivity.this, JokerStatisticsUserProportion.class);
				  startActivity(intent);
			}
		});
		
	}

}
