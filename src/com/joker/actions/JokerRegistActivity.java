package com.joker.actions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.joker.model.Model;
import com.joker.net.IsNet;
import com.joker.net.ThreadPoolUtils;
import com.joker.thread.HttpPostThread;
import com.gen.joker.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/*
 * @author:李颜翎 注册
 * @说明：这是用户注册的Activity
 */
public class JokerRegistActivity extends Activity {
	private EditText regist_username;
	private EditText regist_password;
	private ImageView regist_close;
	private Button regist;
	public String url = null;
	private String value = null;
	private IsNet isnet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lay_joker_regist);
		isnet = new IsNet(this);
		regist_username = (EditText) findViewById(R.id.id_regist_username);
		regist_password = (EditText) findViewById(R.id.id_regist_password);
		regist_close = (ImageView) findViewById(R.id.registClose);
		regist = (Button) findViewById(R.id.id_action_regist);
		regist.setOnClickListener(new registbuttonlistener());
		regist_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}


	/*
	 * 判断用户名输入的正确性
	 */
	private boolean checkUsername(String string) {							
		int stringLength = string.getBytes().length;
		if (stringLength<2 && stringLength>0){
			Toast.makeText(getApplicationContext(), "用户名过短,至少一个汉字或两个字符", 1).show();
			return false;
		}else if (stringLength>20){
			Toast.makeText(getApplicationContext(), "用户名过长,最多六个汉字或20个字符", 1).show();
			return false;
		}

		//不允许含有空格
		Pattern ppp = Pattern.compile("[\\s]+");
		Matcher m1 = ppp.matcher(string);
		boolean b1 = false;
		while(m1.find()){
			b1 = true;
		}
		if (b1){
			Toast.makeText(getApplicationContext(), "用户名不允许含有空格", 1).show();
			return false;
		}
		
		//用户名中不允许有特殊字符
		String regx=" |!|！|@|◎|#|＃|(\\$)|￥|%|％|(\\^)|……|(\\&)|※|(\\*)|×|(\\()|（|(\\))|）|_|——|(\\+)|＋|(\\|)|§";		
		string = string.trim();
		Pattern p = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(string);
		if (m.find()){
			Toast.makeText(getApplicationContext(), "用户名不允许含有特殊字符", 1).show();
			return false;
		}
		//用户名由英文字母，数字或汉字组成
		String sss = "[a-zA-Z\\d\u4e00-\u9fa5]{1,20}";
		if (string.replaceAll(sss, "").length()==0){
			return true;
		}
		return false;
	}		
	
	/*
	 * 对输入的密码进行判断
	 */
	private boolean checkPassword(String string) {
		int stringLength = string.getBytes().length;
		if (stringLength<6){
			Toast.makeText(getApplicationContext(), "密码过短，至少6个字符", 1).show();
			return false;
		}
		if (stringLength>20){
			Toast.makeText(getApplicationContext(), "密码过长，至多20个字符", 1).show();
			return false;
		}
		//用户名中不允许有特殊字符
		String regx=" |!|！|@|◎|#|＃|(\\$)|￥|%|％|(\\^)|……|(\\&)|※|(\\*)|×|(\\()|（|(\\))|）|_|——|(\\+)|＋|(\\|)|§";		
		string = string.trim();
		Pattern p = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(string);
		if (m.find()){
			Toast.makeText(getApplicationContext(), "密码不允许含有特殊字符", 1).show();
			return false;
		}
		//密码为字母，数字
		String sss = "[a-zA-Z\\d]{6,20}"; 
		if (string.replaceAll(sss, "").length()==0){
			return true;
		}
		return false;

	}
	/*
	 * @author:郑轩
	 * 
	 * @说明：监听注册按钮活动
	 */
	public class registbuttonlistener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if(!isnet.IsConnect()){
				Toast.makeText(JokerRegistActivity.this, "请先连接网络", Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(regist_username.getText())) {
				Toast.makeText(getApplicationContext(), "请填写用户名",
						Toast.LENGTH_SHORT).show();
				return;
			}else if(!checkUsername(regist_username.getText().toString())){
				//Toast.makeText(getApplicationContext(), "用户名允许由数字，英文和汉字组成", 1).show();
				return;
			}
			if (TextUtils.isEmpty(regist_password.getText())) {
				Toast.makeText(getApplicationContext(), "请填写密码",
						Toast.LENGTH_SHORT).show();
				return;
			}else if(!checkPassword(regist_password.getText().toString())){
				//Toast.makeText(getApplicationContext(), "密码允许由数字，英文组成", 1).show();
				return;
			}
			reginstet();
			regist.setEnabled(false);
			regist.setText("注册中...");
		}
	}

	/*
	 * @author 郑轩
	 * 
	 * @注册
	 */
	private void reginstet() {
		url = Model.REGISTET;
		value = "{\"name\":\"" + regist_username.getText().toString()
				+ "\",\"password\":\"" + regist_password.getText().toString()
				+ "\"}";
		ThreadPoolUtils.execute(new HttpPostThread(handler, url, value));
	}

	/*
	 * @author 郑轩
	 * 
	 * @说明:handler用于接收子线程发回来的消息
	 */
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(JokerRegistActivity.this, "请求失败，服务器故障", 1)
				.show();
			} else if (msg.what == 100) {
				Toast.makeText(JokerRegistActivity.this, "服务器无响应", 1).show();
			} else if (msg.what == 200) {
				if (msg.obj.toString().equals("ok")) {
					Toast.makeText(getApplicationContext(), "注册成功",
							Toast.LENGTH_SHORT).show();
					finish();
					Intent intent = new Intent();
					intent.setClass(JokerRegistActivity.this,JokerLoginActivity.class);
					JokerRegistActivity.this.startActivity(intent);
				} else if (msg.obj.toString().equals("no")) {
					Toast.makeText(getApplicationContext(), "该用户已存在",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "注册失败",
							Toast.LENGTH_SHORT).show();
				}
			}
			regist.setEnabled(true);
			regist.setText("注册");
		}

	};
}
