package com.joker.actions;

import java.util.ArrayList;
import java.util.List;

import com.gen.joker.R;
import com.joker.adapter.MyApplyAdminAdapter;
import com.joker.adapter.MyCancleAdminAdapter;
import com.joker.info.UserInfo;
import com.joker.model.Model;
import com.joker.net.ThreadPoolUtils;
import com.joker.thread.HttpGetThread;
import com.joker.utils.MyJson;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * @author:����ΰ Ȩ�޹���
 * @˵��������BOSS�û���������Activity
 */
public class JokerAuthorityActivity extends Activity {

	private Button set_admin;   //�����������Ա���水ť
	private Button unset_admin;	//���볷������Ա���水ť
	private ListView listview;
	private MyJson myJson=new MyJson();
	private ArrayList<UserInfo> appleList=new ArrayList<UserInfo>();	//�������Ա�б�
	private ArrayList<UserInfo> cancleList=new ArrayList<UserInfo>();	//��������Ա�б�
	private MyApplyAdminAdapter myAppleAdminAdapter;
	private MyCancleAdminAdapter myCancleAdminAdapter;
	private Context ctx;
	private TextView show;	//������
	private ImageView login_close;
	private int statu=0;  //���벻ͬ�����־��0��ʾ�������������棬1��ʾ���볷������Ա����
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lay_joker_authority);
		ctx=JokerAuthorityActivity.this;
		listview=(ListView)findViewById(R.id.adminlist);
		show=(TextView) findViewById(R.id.show);
		adapter(statu);
		//������ȡ������Ա��ť��ע�ᡢ����
		set_admin = (Button) findViewById(R.id.setAdmin);
		unset_admin = (Button) findViewById(R.id.unsetAdmin);
		set_admin.setOnClickListener(new buttonlistener());
		unset_admin.setOnClickListener(new buttonlistener());
		
		//�˳���
		login_close = (ImageView) findViewById(R.id.loginClose);
		login_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void adapter(int statu){
		if (statu==0) {
			//�������Ա��������
			myAppleAdminAdapter = new MyApplyAdminAdapter(ctx, appleList);
			listview.setAdapter(myAppleAdminAdapter);
			show.setText("�������Ա");
			ThreadPoolUtils.execute(new HttpGetThread(hand, Model.SHOWADMINLIST));
		}else if(statu==1){
			//��������Ա��������
			myCancleAdminAdapter = new MyCancleAdminAdapter(ctx, cancleList);
			listview.setAdapter(myCancleAdminAdapter);
			show.setText("��������Ա");
			ThreadPoolUtils.execute(new HttpGetThread(hand, Model.SHOWADMINLIST));
		}
	}
	
	public class buttonlistener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.setAdmin:
				appleList.removeAll(appleList);		//�����ǰ�б�
				adapter(statu=0);
				break;

			case R.id.unsetAdmin:
				cancleList.removeAll(cancleList);	//�����ǰ�б�
				adapter(statu=1);
				break;
			}
		}
	}
	/*
	 * @author����ΰ  �������߳����ӷ��������ص�����msg
	 */
	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(ctx, "�Ҳ�����ַ", 1).show();
			} else if (msg.what == 100) {
				Toast.makeText(ctx, "����ʧ��", 1).show();
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null) {
					List<UserInfo> newList = myJson.getUserList(result);
					if (newList!=null) {
						int i=0;
						for (UserInfo info : newList) {
							if (newList.get(i++).getApplyforadmin()==1) {
								appleList.add(info);	//ȡ�������Ա�б�
							}else cancleList.add(info);      //ȡ��������Ա�б�
						}
					}
					if (statu==0) {
						myAppleAdminAdapter.notifyDataSetChanged();	
					}else if(statu==1){
						myCancleAdminAdapter.notifyDataSetChanged();
					}
				}
			}
		}
	};
}

