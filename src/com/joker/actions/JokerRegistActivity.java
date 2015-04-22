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
 * @author:������ ע��
 * @˵���������û�ע���Activity
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
	 * �ж��û����������ȷ��
	 */
	private boolean checkUsername(String string) {							
		int stringLength = string.getBytes().length;
		if (stringLength<2 && stringLength>0){
			Toast.makeText(getApplicationContext(), "�û�������,����һ�����ֻ������ַ�", 1).show();
			return false;
		}else if (stringLength>20){
			Toast.makeText(getApplicationContext(), "�û�������,����������ֻ�20���ַ�", 1).show();
			return false;
		}

		//�������пո�
		Pattern ppp = Pattern.compile("[\\s]+");
		Matcher m1 = ppp.matcher(string);
		boolean b1 = false;
		while(m1.find()){
			b1 = true;
		}
		if (b1){
			Toast.makeText(getApplicationContext(), "�û����������пո�", 1).show();
			return false;
		}
		
		//�û����в������������ַ�
		String regx=" |!|��|@|��|#|��|(\\$)|��|%|��|(\\^)|����|(\\&)|��|(\\*)|��|(\\()|��|(\\))|��|_|����|(\\+)|��|(\\|)|��";		
		string = string.trim();
		Pattern p = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(string);
		if (m.find()){
			Toast.makeText(getApplicationContext(), "�û����������������ַ�", 1).show();
			return false;
		}
		//�û�����Ӣ����ĸ�����ֻ������
		String sss = "[a-zA-Z\\d\u4e00-\u9fa5]{1,20}";
		if (string.replaceAll(sss, "").length()==0){
			return true;
		}
		return false;
	}		
	
	/*
	 * ���������������ж�
	 */
	private boolean checkPassword(String string) {
		int stringLength = string.getBytes().length;
		if (stringLength<6){
			Toast.makeText(getApplicationContext(), "������̣�����6���ַ�", 1).show();
			return false;
		}
		if (stringLength>20){
			Toast.makeText(getApplicationContext(), "�������������20���ַ�", 1).show();
			return false;
		}
		//�û����в������������ַ�
		String regx=" |!|��|@|��|#|��|(\\$)|��|%|��|(\\^)|����|(\\&)|��|(\\*)|��|(\\()|��|(\\))|��|_|����|(\\+)|��|(\\|)|��";		
		string = string.trim();
		Pattern p = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(string);
		if (m.find()){
			Toast.makeText(getApplicationContext(), "���벻�����������ַ�", 1).show();
			return false;
		}
		//����Ϊ��ĸ������
		String sss = "[a-zA-Z\\d]{6,20}"; 
		if (string.replaceAll(sss, "").length()==0){
			return true;
		}
		return false;

	}
	/*
	 * @author:֣��
	 * 
	 * @˵��������ע�ᰴť�
	 */
	public class registbuttonlistener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if(!isnet.IsConnect()){
				Toast.makeText(JokerRegistActivity.this, "������������", Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(regist_username.getText())) {
				Toast.makeText(getApplicationContext(), "����д�û���",
						Toast.LENGTH_SHORT).show();
				return;
			}else if(!checkUsername(regist_username.getText().toString())){
				//Toast.makeText(getApplicationContext(), "�û������������֣�Ӣ�ĺͺ������", 1).show();
				return;
			}
			if (TextUtils.isEmpty(regist_password.getText())) {
				Toast.makeText(getApplicationContext(), "����д����",
						Toast.LENGTH_SHORT).show();
				return;
			}else if(!checkPassword(regist_password.getText().toString())){
				//Toast.makeText(getApplicationContext(), "�������������֣�Ӣ�����", 1).show();
				return;
			}
			reginstet();
			regist.setEnabled(false);
			regist.setText("ע����...");
		}
	}

	/*
	 * @author ֣��
	 * 
	 * @ע��
	 */
	private void reginstet() {
		url = Model.REGISTET;
		value = "{\"name\":\"" + regist_username.getText().toString()
				+ "\",\"password\":\"" + regist_password.getText().toString()
				+ "\"}";
		ThreadPoolUtils.execute(new HttpPostThread(handler, url, value));
	}

	/*
	 * @author ֣��
	 * 
	 * @˵��:handler���ڽ������̷߳���������Ϣ
	 */
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(JokerRegistActivity.this, "����ʧ�ܣ�����������", 1)
				.show();
			} else if (msg.what == 100) {
				Toast.makeText(JokerRegistActivity.this, "����������Ӧ", 1).show();
			} else if (msg.what == 200) {
				if (msg.obj.toString().equals("ok")) {
					Toast.makeText(getApplicationContext(), "ע��ɹ�",
							Toast.LENGTH_SHORT).show();
					finish();
					Intent intent = new Intent();
					intent.setClass(JokerRegistActivity.this,JokerLoginActivity.class);
					JokerRegistActivity.this.startActivity(intent);
				} else if (msg.obj.toString().equals("no")) {
					Toast.makeText(getApplicationContext(), "���û��Ѵ���",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "ע��ʧ��",
							Toast.LENGTH_SHORT).show();
				}
			}
			regist.setEnabled(true);
			regist.setText("ע��");
		}

	};
}
