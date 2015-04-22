package com.joker.actions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.joker.JokerMainActivity;
import com.joker.info.UserInfo;
import com.joker.model.Model;
import com.joker.net.IsNet;
import com.joker.net.ThreadPoolUtils;
import com.joker.thread.HttpPostThread;
import com.joker.utils.MyJson;
import com.gen.joker.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/*
 * @author:������ ��½
 * @˵���������û���½��Activity
 */
public class JokerLoginActivity extends Activity {
	private EditText login_username;
	private EditText login_password;
	private CheckBox autologin;
	private CheckBox research;
	private ImageView login_close;
	private Button login;
	private Button researchpassword;
	private TextView login_to_regist;
	public String url =null;
	private String value=null;
	private IsNet isnet;
	private SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lay_joker_login);
		isnet = new IsNet(this);
		sharedPreferences = this.getSharedPreferences("userInfo",Context.MODE_WORLD_READABLE);
		login_username = (EditText)findViewById(R.id.id_login_username);
		login_password = (EditText)findViewById(R.id.id_login_password);
		login_close = (ImageView) findViewById(R.id.loginClose);
		autologin = (CheckBox) findViewById(R.id.autologin);
		research = (CheckBox) findViewById(R.id.research_password);
		login = (Button)findViewById(R.id.id_action_login);
		researchpassword= (Button)findViewById(R.id.research_password_button);
		login_to_regist = (TextView) findViewById(R.id.login_to_regist);
		researchpassword.setOnClickListener(new researchpasswordlistener());
		login.setOnClickListener(new loginbuttonlistener());
		login_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		login_to_regist.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(JokerLoginActivity.this,JokerRegistActivity.class);
				JokerLoginActivity.this.startActivity(intent);
			}
		});
		research.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					autologin.setEnabled(false);
					login.setVisibility(View.GONE);
					login_password.setHint("�����û��󶨵�����");
					Drawable drawable= getResources().getDrawable(R.drawable.joker_icon_message);
					login_password.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
					researchpassword.setVisibility(View.VISIBLE);
					login_password.setInputType(InputType.TYPE_CLASS_TEXT);
				}else{
					autologin.setEnabled(true);
					login.setVisibility(View.VISIBLE);
					login_password.setHint("����������");
					Drawable drawable= getResources().getDrawable(R.drawable.joker_icon_key);
					login_password.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
					researchpassword.setVisibility(View.GONE);
					login_password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
			}
		});
	}
	/*
	 * @author:֣��
	 * @˵���������һ����밴ť�
	 */
	public class researchpasswordlistener implements OnClickListener{
		@Override
		public void onClick(View v) {
			url = Model.RESEARCHPASSWORD;
			value = "{\"name\":\"" + login_username.getText().toString() + "\",\"email\":\""
					+ login_password.getText().toString() + "\"}";
			ThreadPoolUtils.execute(new HttpPostThread(handler_researchpassword, url, value));
		}
	}
	/*
	 * @author:֣��
	 * @˵����������¼��ť�
	 */
	public class loginbuttonlistener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(!isnet.IsConnect()){
				Toast.makeText(JokerLoginActivity.this, "������������", Toast.LENGTH_SHORT).show();
				return;
			}
			if(TextUtils.isEmpty(login_username.getText())){
				Toast.makeText(getApplicationContext(), "����д�û���",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if(TextUtils.isEmpty(login_password.getText())){
				Toast.makeText(getApplicationContext(), "����д����",
						Toast.LENGTH_SHORT).show();
				return;
			}
			login();
			login.setEnabled(false);
			login.setText("��¼��...");
		}
	}
	/*
	 * @author֣��
	 * ��½����
	 */
	private void login() {
		url = Model.LOGIN;
		value = "{\"name\":\"" + login_username.getText().toString() + "\",\"password\":\""
				+ login_password.getText().toString() + "\"}";
		ThreadPoolUtils.execute(new HttpPostThread(handler, url, value));
	}
	/*
	 * @author ֣��
	 * @˵��:handler���ڽ������̷߳���������Ϣ
	 */
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(JokerLoginActivity.this, "����ʧ�ܣ�����������", 1).show();
			} else if (msg.what == 100) {
				Toast.makeText(JokerLoginActivity.this, "����������Ӧ", 1).show();
			} else if (msg.what == 200) {
				String result = msg.obj.toString();
				if(result.equals("nopass")){
					Toast.makeText(getApplicationContext(), "�������",
							Toast.LENGTH_SHORT).show();
				}else if(result.equals("nouser")){
					Toast.makeText(getApplicationContext(), "�û�������",
							Toast.LENGTH_SHORT).show();
				}else{
					if(result!=null){
						Toast.makeText(getApplicationContext(), "��½�ɹ�",
								Toast.LENGTH_SHORT).show();
						Model.MYUSERINFO = new MyJson().getUserInfo(result);//����ѵ�¼
						if(autologin.isChecked()){
							Editor editor = sharedPreferences.edit(); 
							editor.putString("userName", login_username.getText().toString());
				            editor.putString("passWord", login_password.getText().toString());
				            editor.putBoolean("AUTO_ISCHECK", true);
				            editor.commit();
						}
						JokerLoginActivity.this.finish(); //�ᴥ��onDestroy();
					}
				}
			}
			login.setEnabled(true);
			login.setText("��¼");
		}
	};
	
	Handler handler_researchpassword = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(JokerLoginActivity.this, "����ʧ�ܣ�����������", 1).show();
			} else if (msg.what == 100) {
				Toast.makeText(JokerLoginActivity.this, "����������Ӧ", 1).show();
			} else if (msg.what == 200) {
				String result = msg.obj.toString();
				if(result.equals("no")){
					Toast.makeText(getApplicationContext(), "�û����������Ӧ����",Toast.LENGTH_SHORT).show();
				}else if(result.equals("email_false")){
					Toast.makeText(getApplicationContext(), "���䷢��ʧ��",Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getApplicationContext(), "�ѷ����ʼ����뵽���������в鿴",Toast.LENGTH_SHORT).show();
				}
			}
		}
	};
}
