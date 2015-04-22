package com.joker.adapter;

import java.io.IOException;
import java.util.List;


import com.gen.joker.R;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.joker.actions.JokerLoginActivity;
import com.joker.actions.JokerSendCommentActivity;
import com.joker.info.JokeInfo;
import com.joker.model.Model;
import com.joker.net.IsNet;
import com.joker.net.ThreadPoolUtils;
import com.joker.thread.HttpGetThread;
import com.joker.utils.ImageLoader;
import com.joker.utils.JokerShare;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.view.ViewGroup.LayoutParams;

public class MyListAdapter extends BaseAdapter {

	private List<JokeInfo> list;
	private Context ctx;
	public ImageLoader imageLoader;

	private IsNet isnet;  //网络判断标志
	private SpeechSynthesizer mySynthesizer;
	private String role;  //角色
	private String pitch; //语速
	private int location; //所在位置
	private int audiolocation;
	private String text="   语音";
	private LayoutParams para; //头像尺寸


	public MyListAdapter(Context ctx, List<JokeInfo> list) {
		this.list = list;
		this.ctx = ctx;
		imageLoader=new ImageLoader(ctx);
		isnet = new IsNet(ctx);
		/*
                                  在使用语音平台上传应用包的时候会自定生成一个appid 
                                  应该使用与包相对应的appid在申请提交后没有使用次数的限制
		 */
		//语音初始化，在使用应用使用时需要初始化一次就好，如果没有这句会出现10111初始化失败
		SpeechUtility.createUtility(ctx, "appid=54367c25");
		//处理语音合成关键类，创建一个语言合成
		mySynthesizer = SpeechSynthesizer.createSynthesizer(ctx, myInitListener);
	}

	private InitListener myInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			Log.d("mySynthesiezer:", "InitListener init() code = " + code);
		}
	};

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		final Holder hold;
		if (arg1 == null) {
			hold = new Holder();
			arg1 = View.inflate(ctx, R.layout.lay_joker_item, null);
			hold.UserName = (TextView) arg1.findViewById(R.id.Item_UserName);
			hold.radio = (ImageView) arg1.findViewById(R.id.Item_Radio);
			hold.MainText = (TextView) arg1.findViewById(R.id.Item_MainText);
			hold.Img = (ImageView) arg1.findViewById(R.id.Item_MainImg);
			hold.Up = (LinearLayout) arg1.findViewById(R.id.Item_Up);
			hold.Up_Img = (ImageView) arg1.findViewById(R.id.Item_Up_img);
			hold.Img_status = (ImageView) arg1.findViewById(R.id.Item_joke_status);
			hold.Up_text = (TextView) arg1.findViewById(R.id.Item_Up_text);
			hold.Comments = (LinearLayout) arg1.findViewById(R.id.Item_Comments);
			hold.Comment_Text= (TextView) arg1.findViewById(R.id.Item_Yuyin_text);
			hold.Yuyin_Text = (TextView) arg1.findViewById(R.id.Item_Comments_text);
			hold.Share = (LinearLayout) arg1.findViewById(R.id.Item_Share);
			hold.Share_Img = (ImageView) arg1.findViewById(R.id.Item_Share_img);
			hold.userimage=(ImageView) arg1.findViewById(R.id.userimage);
			arg1.setTag(hold);
		} else {
			hold = (Holder) arg1.getTag();
		}

		hold.UserName.setText(list.get(arg0).getUsername());
		hold.MainText.setText(list.get(arg0).getText());

		hold.Up_text.setText(list.get(arg0).getLike()+"");
		hold.Up_Img.setImageResource(R.drawable.icon_img_like_disable);
		if (list.get(arg0).getCommentstimes()!=0) {
			hold.Comment_Text.setText(list.get(arg0).getCommentstimes() + "");
		}
		if(list.get(arg0).getIschecked()==0){
			hold.Img_status.setImageResource(R.drawable.checking);
			hold.Img_status.setVisibility(View.VISIBLE);
		}else if(list.get(arg0).getIschecked()==2){
			hold.Img_status.setImageResource(R.drawable.nopass);
			hold.Img_status.setVisibility(View.VISIBLE);
		}else{
			hold.Img_status.setVisibility(View.GONE);
		}

		//处理头像
		if (list.get(arg0).getHead().equals("headimage1")) {
			hold.userimage.setImageResource(R.drawable.user_head_image1);
		}else if (list.get(arg0).getHead().equals("headimage2")) {
			hold.userimage.setImageResource(R.drawable.user_head_image2);
		}else if (list.get(arg0).getHead().equals("headimage3")) {
			hold.userimage.setImageResource(R.drawable.user_head_image3);
		}else if (list.get(arg0).getHead().equals("headimage4")) {
			hold.userimage.setImageResource(R.drawable.user_head_image4);
		}else if (list.get(arg0).getHead().equals("headimage5")) {
			hold.userimage.setImageResource(R.drawable.user_head_image5);
		}else if (list.get(arg0).getHead().equals("headimage6")) {
			hold.userimage.setImageResource(R.drawable.user_head_image6);
		}else if (list.get(arg0).getHead().equals("headimage7")) {
			hold.userimage.setImageResource(R.drawable.user_head_image7);
		}else if (list.get(arg0).getHead().equals("headimage8")) {
			hold.userimage.setImageResource(R.drawable.user_head_image8);
		}else if (list.get(arg0).getHead().equals("headimage9")) {
			hold.userimage.setImageResource(R.drawable.user_head_image9);
		}else {
			//设置默认
			hold.userimage.setImageResource(R.drawable.user_head_image5);
		}
		para = hold.userimage.getLayoutParams();
		para.height = 100;
		para.width = 100;
		hold.userimage.setLayoutParams(para);
		

		//显示图片
		if(list.get(arg0).getImage().equalsIgnoreCase("")){
			hold.Img.setVisibility(View.GONE);
		}else{
			imageLoader.DisplayImage(Model.HTTPSTOR + list.get(arg0).getImage(), hold.Img);
			hold.Img.setVisibility(View.VISIBLE);
		}

		//处理音频
		if(list.get(arg0).getRadio().equals("")){
			hold.radio.setVisibility(View.GONE);
		}else{
			//监听语音有没播放完
			Model.player.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					Model.anim.stop();
				}
			});
			hold.radio.setVisibility(View.VISIBLE);
			hold.radio.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					audiolocation=arg0;
					if(!isnet.IsConnect()){
						Toast.makeText(ctx, "请先连接网络", Toast.LENGTH_SHORT).show();
						return;
					}

					if (Model.audioplay==false) {  //判断合成语音是否在播放
						if(Model.player.isPlaying()){   //判断语音是否在播放
							if (list.get(arg0).getJokeID() == Model.palyjokeid) {  //判断是否在播放同一个笑话
								Model.anim.stop();
								Model.player.stop();    //停止播放
								Model.player.reset();
								//Toast.makeText(ctx, "停止语音播放！", Toast.LENGTH_SHORT).show();
								Model.palyjokeid = -1;
							}else {
								Toast.makeText(ctx, "语音开始播放",Toast.LENGTH_LONG).show();
								//播放gif图片
								Model.anim.stop();
								Object ob = hold.radio.getBackground();
								Model.anim = (AnimationDrawable) ob;
								Model.anim.stop();
								Model.anim.start();
								audioplay();
							}
						} else {
							Toast.makeText(ctx, "语音开始播放",Toast.LENGTH_LONG).show();
							//播放gif图片
							Object ob = hold.radio.getBackground();
							Model.anim = (AnimationDrawable) ob;
							Model.anim.stop();
							Model.anim.start();
							audioplay();
						}
					} else {
						Toast.makeText(ctx, "先停止语音播放！", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}

		hold.Up.setOnClickListener(new View.OnClickListener() {
			private boolean upFlag = true;
			public void onClick(View v) {
				if (Model.MYUSERINFO != null){	
					int vid = list.get(arg0).getJokeID();
					int uid = Model.MYUSERINFO.getUserID();
					String url = Model.LIKE + "?vid=" + vid + "&uid=" + uid;
					if(!isnet.IsConnect()){
						Toast.makeText(ctx, "请先打开网络", Toast.LENGTH_SHORT).show();
						return;
					}
					if (upFlag){  //用户首次点击点赞按钮才查询数据库看该笑话是否已被该用户点赞
						Handler handler1 = new Handler() {
							public void handleMessage(android.os.Message msg) {
								super.handleMessage(msg);
								String result = msg.obj.toString();
								if (result.equals("ok")) {
									int num;
									num = list.get(arg0).getLike() + 1;
									hold.Up_text.setText(num + "");
									hold.Up_Img.setImageResource(R.drawable.icon_img_like_enable);
									list.get(arg0).setLike(num);
								}else {
									Toast.makeText(ctx, "您已经点过赞了", Toast.LENGTH_SHORT).show();
								}
							}
						};
						ThreadPoolUtils.execute(new HttpGetThread(handler1, url)); //到数据库中查询笑话是否被赞过
						upFlag = false;
					}
					else {
						Toast.makeText(ctx, "您已经点过赞了", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(ctx, "登陆后才能点赞", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.setClass(ctx, JokerLoginActivity.class);
					ctx.startActivity(intent);
				}
			}
		});
		hold.Share.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg) {
				String imageUrl = Model.HTTPSTOR+list.get(arg0).getImage();
				JokerShare as = new JokerShare(ctx,list.get(arg0).getText(),imageUrl);
				as.show();
			}
		});

		hold.Comments.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg) {		
				//author：王亚伟	
				//功能：文本合成语音，播放不同语音
				location=arg0;      
				if (Model.player.isPlaying()==false) { //判断语音是否在播放
					if (mySynthesizer.isSpeaking()) { //判断合成语音是否在播放
						if (list.get(arg0).getJokeID() == Model.jokeid) {   //判断是否在合成同一个笑话
							mySynthesizer.destroy();    //点击播放文本，停止播放本笑话
							Model.audioplay=false;
							Toast.makeText(ctx, "停止合成语音播放！", Toast.LENGTH_SHORT)
							.show();
							Model.jokeid = -1;
						} else {
							if (Model.MYUSERINFO == null) {
								play("xiaoyan", 50 + ""); //开始合成其他语音

							} else {
								role=Model.MYUSERINFO.getAudiorole();	//取本地role
								pitch=Model.MYUSERINFO.getAudiospeed()+"";	//取本地pitch
								System.out.println("获取服务器数据");
								play(role, pitch);
							}
						}
					} else {
						if (Model.MYUSERINFO == null) {
							play("xiaoyan", 50 + ""); //开始播放合成语音

						} else {
							role=Model.MYUSERINFO.getAudiorole();	//取本地role
							pitch=Model.MYUSERINFO.getAudiospeed()+"";	//取本地pitch
							System.out.println("获取服务器数据");
							play(role, pitch);
						}
					}
				} else {
					Toast.makeText(ctx, "先停止语音播放！", Toast.LENGTH_SHORT)
					.show();
				}
			}
		});

		return arg1;
	}

	private void audioplay(){

		Model.player.stop();    //停止播放
		Model.player.reset();
		String path = Model.HTTPSTOR
				+ list.get(audiolocation).getRadio();
		try {
			Model.player.setDataSource(path);
			Model.player.prepare();
			Model.player.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Model.palyjokeid = list.get(audiolocation).getJokeID();
		Toast.makeText(ctx, "语音已经播放",Toast.LENGTH_SHORT).show();
	}
	private void play(String role,String pitch) {
		//设置发音人
		mySynthesizer.setParameter(SpeechConstant.VOICE_NAME,role);  
		//设置语速
		mySynthesizer.setParameter(SpeechConstant.PITCH,pitch);	
		//设置音量
		mySynthesizer.setParameter(SpeechConstant.VOLUME,"50");	
		//开始播放
		mySynthesizer.startSpeaking(list.get(location).getText(),mTtsListener);  
		Model.jokeid=list.get(location).getJokeID();
		Model.audioplay=true;
		Toast.makeText(ctx, "开始播放笑话，请欣赏~",
				Toast.LENGTH_SHORT).show();
	}

	private SynthesizerListener mTtsListener = new SynthesizerListener() {
		@Override
		public void onSpeakBegin() {
		} 
		@Override
		public void onSpeakPaused() {

		}
		@Override
		public void onSpeakResumed() {
		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos,
				String info) {
		}
		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
		}
		//播放结束、回调错误类型
		@Override
		public void onCompleted(SpeechError error) {
			if(error!=null)
			{
				Log.d("mySynthesiezer complete code:", error.getErrorCode()+"");
			}
			else
			{
				Log.d("mySynthesiezer complete code:", "0");
			}
			Model.audioplay=false;	//合成结束,合成播放标志设置false
		}
		@Override
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
			// TODO Auto-generated method stub

		}
	};

	static class Holder {
		TextView UserName;
		ImageView radio;
		TextView MainText;
		ImageView Img;
		ImageView Img_status;
		LinearLayout Up;
		ImageView Up_Img;
		TextView Up_text;
		LinearLayout Comments;
		TextView Yuyin_Text;
		LinearLayout Share;
		ImageView Share_Img;
		TextView Comment_Text;
		ImageView userimage;
	}
}
