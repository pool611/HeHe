package com.joker.actions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.gen.joker.R;
import com.joker.info.JokeInfo;
import com.joker.model.Model;
import com.joker.net.IsNet;
import com.joker.net.ThreadPoolUtils;
import com.joker.thread.HttpGetThread;
import com.joker.thread.HttpPostThread;
import com.joker.utils.ImageLoader;
import com.joker.utils.MyJson;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



/*
 * @author:李颜翎 审稿
 * @说明：这是管理员审核新发布的稿件的Activity
 */
public class JokerCheckActivity extends Activity {
	private ImageButton radiobutton; 
	private TextView joketext;
	private ImageView jokeimage;
	private ImageView checkClose;
	private Button ok;
	private Button no;
	public List<JokeInfo> joke = null;
	public ImageLoader imageLoader = new ImageLoader(this);
	public int jokeid = -1;
	public int uid = -1;
	private IsNet isnet;
	//private String value;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lay_joker_check);
		radiobutton = (ImageButton) findViewById(R.id.Check_Radio);
		jokeimage = (ImageView) findViewById(R.id.Check_MainImg);
		joketext = (TextView) findViewById(R.id.Check_MainText);
		ok = (Button) findViewById(R.id.Check_ok);
		no = (Button) findViewById(R.id.Check_no);
		checkClose=(ImageView) findViewById(R.id.checkClose);
		isnet = new IsNet(this);
		checkClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		ThreadPoolUtils.execute(new HttpGetThread(show_handler, Model.CHECKSHOW));
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//value = "{\"act\":\"pass\",\"vid\":\""+ jokeid+"\"}";
				if(!isnet.IsConnect()){
					Toast.makeText(JokerCheckActivity.this, "请先连接网络", Toast.LENGTH_SHORT).show();
					return;
				}
				ThreadPoolUtils.execute(new HttpGetThread(show_handler, Model.CHECKJOKE+"?vid="+jokeid+"&act=pass&uid="+uid));
				ThreadPoolUtils.execute(new HttpGetThread(show_handler, Model.CHECKSHOW+"?vid="+jokeid));
				ok.setEnabled(false);
				no.setEnabled(false);
			}
		});
		no.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//value = "{\"act\":\"nopass\",\"vid\":\""+ jokeid+"\"}";
				if(!isnet.IsConnect()){
					Toast.makeText(JokerCheckActivity.this, "请先连接网络", Toast.LENGTH_SHORT).show();
					return;
				}
				ThreadPoolUtils.execute(new HttpGetThread(show_handler, Model.CHECKJOKE+"?vid="+jokeid+"&act=nopass&uid="+uid));
				ThreadPoolUtils.execute(new HttpGetThread(show_handler, Model.CHECKSHOW+"?vid="+jokeid));
				ok.setEnabled(false);
				no.setEnabled(false);
			}
		});
	}
	
	Handler show_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
				String result = msg.obj.toString();
				if(result.equalsIgnoreCase("none")){
					joketext.setText("已全部审核完毕");
					radiobutton.setVisibility(View.GONE);
					jokeimage.setVisibility(View.GONE);
					ok.setEnabled(false);
					no.setEnabled(false);
				}else if(result.equalsIgnoreCase("ok")){
				
				}else
				{
					joke =new MyJson().getJokeList("["+result+"]");
					joketext.setText(joke.get(0).getText());
					jokeid = joke.get(0).getJokeID();
					uid= joke.get(0).getUsernameID();
					if(joke.get(0).getRadio().equals("")){
						radiobutton.setVisibility(View.GONE);
					}else{
						radiobutton.setVisibility(View.VISIBLE);
						radiobutton.setOnClickListener(new View.OnClickListener() {
							MediaPlayer player;
							@Override
							public void onClick(View v) {
								if(player==null){
									player = new MediaPlayer();
								}else{
									player.reset();
								}
								if(!player.isPlaying()){
									String path = Model.HTTPSTOR+joke.get(0).getRadio();
									try {
										player.setDataSource(path);
										player.prepare();
										player.start();
									} catch (IllegalArgumentException e) {
										e.printStackTrace();
									} catch (SecurityException e) {
										e.printStackTrace();
									} catch (IllegalStateException e) {
										e.printStackTrace();
									} catch (IOException e) {
										e.printStackTrace();
									}	
								}else{
									player.stop();
								}
							}
						});
					}
					if(joke.get(0).getImage().equals("")){
						jokeimage.setVisibility(View.GONE);
					}else{
						jokeimage.setVisibility(View.VISIBLE);
						imageLoader.DisplayImage(Model.HTTPSTOR + joke.get(0).getImage(), jokeimage);
					}
					ok.setEnabled(true);
					no.setEnabled(true);
				}
		}
    };
}
