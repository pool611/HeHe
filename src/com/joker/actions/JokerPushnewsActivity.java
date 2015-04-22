package com.joker.actions;

import com.gen.joker.*;
import com.joker.JokerMainActivity;
import com.joker.model.Model;
import com.joker.net.ThreadPoolUtils;
import com.joker.thread.HttpPostThread;
import com.joker.utils.MyJson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class JokerPushnewsActivity extends Activity {
	private EditText pushtext;
	private EditText pushtitle;
	private Button submit;
	private ImageView pushnewsClose;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lay_joker_pushnews);
		pushtext = (EditText) findViewById(R.id.PustText);
		pushtitle = (EditText) findViewById(R.id.PushTitle);
		submit = (Button) findViewById(R.id.submit);
		pushnewsClose=(ImageView) findViewById(R.id.pushnewsClose);
		pushnewsClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			finish();	
			}
		});
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String value = "{\"pushtitle\":\"" + pushtitle.getText().toString() + "\",\"pushtext\":\""
						+ pushtext.getText().toString() + "\"}";
				ThreadPoolUtils.execute(new HttpPostThread(handler, Model.PUSH, value));
				
			}
		});
	}
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			//String result = msg.obj.toString();
			Toast.makeText(getApplicationContext(), "ÍÆËÍ³É¹¦", Toast.LENGTH_SHORT).show();
			pushtext.setText("");
			pushtitle.setText("");
		}
    };
}
