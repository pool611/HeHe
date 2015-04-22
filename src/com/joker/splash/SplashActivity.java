package com.joker.splash;

import com.gen.joker.R;
import com.gen.joker.R.id;
import com.gen.joker.R.layout;
import com.gen.joker.R.menu;
import com.joker.JokerMainActivity;
import com.joker.actions.JokerLoginActivity;
import com.joker.model.Model;
import com.joker.net.IsNet;
import com.joker.net.ThreadPoolUtils;
import com.joker.thread.HttpPostThread;
import com.joker.utils.MyJson;
import com.sina.push.PushManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

public class SplashActivity extends Activity {
	//数据存储方式，用于自动登录
	private SharedPreferences sharedPreferences;
	private String login_username;
	private String login_password;
	private String url;
	private String value;
	private Handler handler = new Handler(); 
	private IsNet isnet;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		isnet = new IsNet(this);
		//检测自动登录设置
		sharedPreferences = this.getSharedPreferences("userInfo",Context.MODE_WORLD_READABLE);
		if (sharedPreferences.getBoolean("AUTO_ISCHECK", false)&&isnet.IsConnect()) {  
			login_username = sharedPreferences.getString("userName", "");
			login_password = sharedPreferences.getString("passWord", "");
			url = Model.LOGIN;
			value = "{\"name\":\"" + login_username + "\",\"password\":\""
					+ login_password + "\"}";
			ThreadPoolUtils.execute(new HttpPostThread(handler_login, url, value));
		}else{
			handler.postDelayed(new Runnable()  
    		{  
    			@Override  
    			public void run()  
    			{  

    				PushManager manager = PushManager.getInstance(getApplicationContext());
    				manager.openChannel("20946","100","100");
    				Intent intent = new Intent(SplashActivity.this, JokerMainActivity.class);  
    				startActivity(intent);
    				finish();

    			}  
    		}, 3000);
		}
		
	}
	
	Handler handler_login = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(getBaseContext(), "请求失败，服务器故障", 1).show();
			} else if (msg.what == 100) {
				Toast.makeText(getBaseContext(), "服务器无响应", 1).show();
			} else if (msg.what == 200) {
				String result = msg.obj.toString();
				Model.MYUSERINFO = new MyJson().getUserInfo(result);//标记已登录
	            handler.postDelayed(new Runnable()  
	    		{  
	    			@Override  
	    			public void run()  
	    			{  

	    				PushManager manager = PushManager.getInstance(getApplicationContext());
	    				manager.openChannel("20946","100","100");
	    				Intent intent = new Intent(SplashActivity.this, JokerMainActivity.class);  
	    				startActivity(intent);
	    				finish();

	    			}  
	    		}, 3000);
			}
		}
	};
}
