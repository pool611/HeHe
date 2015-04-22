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
 * @author:王亚伟 权限管理
 * @说明：这是BOSS用户管理界面的Activity
 */
public class JokerAuthorityActivity extends Activity {

	private Button set_admin;   //进入申请管理员界面按钮
	private Button unset_admin;	//进入撤销管理员界面按钮
	private ListView listview;
	private MyJson myJson=new MyJson();
	private ArrayList<UserInfo> appleList=new ArrayList<UserInfo>();	//申请管理员列表
	private ArrayList<UserInfo> cancleList=new ArrayList<UserInfo>();	//撤销管理员列表
	private MyApplyAdminAdapter myAppleAdminAdapter;
	private MyCancleAdminAdapter myCancleAdminAdapter;
	private Context ctx;
	private TextView show;	//界面名
	private ImageView login_close;
	private int statu=0;  //进入不同界面标志，0表示进入申请管理界面，1表示进入撤销管理员界面
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lay_joker_authority);
		ctx=JokerAuthorityActivity.this;
		listview=(ListView)findViewById(R.id.adminlist);
		show=(TextView) findViewById(R.id.show);
		adapter(statu);
		//设置与取消管理员按钮的注册、监听
		set_admin = (Button) findViewById(R.id.setAdmin);
		unset_admin = (Button) findViewById(R.id.unsetAdmin);
		set_admin.setOnClickListener(new buttonlistener());
		unset_admin.setOnClickListener(new buttonlistener());
		
		//退出键
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
			//申请管理员界面设置
			myAppleAdminAdapter = new MyApplyAdminAdapter(ctx, appleList);
			listview.setAdapter(myAppleAdminAdapter);
			show.setText("申请管理员");
			ThreadPoolUtils.execute(new HttpGetThread(hand, Model.SHOWADMINLIST));
		}else if(statu==1){
			//撤销管理员界面设置
			myCancleAdminAdapter = new MyCancleAdminAdapter(ctx, cancleList);
			listview.setAdapter(myCancleAdminAdapter);
			show.setText("撤销管理员");
			ThreadPoolUtils.execute(new HttpGetThread(hand, Model.SHOWADMINLIST));
		}
	}
	
	public class buttonlistener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.setAdmin:
				appleList.removeAll(appleList);		//清除当前列表
				adapter(statu=0);
				break;

			case R.id.unsetAdmin:
				cancleList.removeAll(cancleList);	//清除当前列表
				adapter(statu=1);
				break;
			}
		}
	}
	/*
	 * @author王亚伟  接收子线程连接服务器返回的数据msg
	 */
	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(ctx, "找不到地址", 1).show();
			} else if (msg.what == 100) {
				Toast.makeText(ctx, "传输失败", 1).show();
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null) {
					List<UserInfo> newList = myJson.getUserList(result);
					if (newList!=null) {
						int i=0;
						for (UserInfo info : newList) {
							if (newList.get(i++).getApplyforadmin()==1) {
								appleList.add(info);	//取申请管理员列表
							}else cancleList.add(info);      //取撤销管理员列表
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

