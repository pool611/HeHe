package com.joker.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gen.joker.R;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.joker.adapter.MyCommentListAdapter;
import com.joker.info.CommentsInfo;
import com.joker.info.JokeInfo;
import com.joker.model.Model;
import com.joker.myview.JokerDetailsListView;
import com.joker.net.IsNet;
import com.joker.net.ThreadPoolUtils;
import com.joker.thread.HttpGetThread;
import com.joker.thread.HttpPostThread;
import com.joker.utils.ImageLoader;
import com.joker.utils.JokerShare;
import com.joker.utils.MyJson;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class JokerSendCommentActivity extends Activity implements OnClickListener{

	private EditText commentEditText; //评论内容编辑框
	private Button sendComment; //发送评论按钮
	private TextView comment_txt; //笑话文本内容
	private TextView Detail_Up_text; //被赞次数
	private ImageView imageView,detail_Radio,detail_Up_image; //笑话图片,音频播放
	private TextView Detail_NoCommentText; //无用户评论时显示的信息
	private TextView Detail_UserName; //显示笑话作者的信息
	private ImageLoader imageLoader; //加载笑话图片
	private JokerDetailsListView detail_List; //显示评论列表的类
	private Button ListButton = null; //加载评论的按钮
	private LinearLayout Joker_Detail__progressBar;
	private LinearLayout Detail_Share, Detail_up; //分享，点赞
	private MediaPlayer player;

	private MyJson myJson;
	private JokeInfo info = null;
	private List<CommentsInfo> list;
	private MyCommentListAdapter myCommentListAdapter;
	private String text; //评论内容
	private int vid, uid; //笑话id,用户id
	private String userName;
	public String url = Model.SENDCOMMENT; //提交评论Url
	public String url1 = Model.SHOWCOMMENTS; //向服务器取评论的Url
	private String url2 = "showdetailjoke.php";


	private boolean flag = true;
	private boolean getOrputFlag = true; //判断是初始化数据还是提交数据的标识符
	private boolean buttonFlag = true; //控制评论按钮只做一次事件响应的标记符
	private boolean listBottemFlag = true;
	private boolean upFlag = true;
	private int mStart = 0; //评论加载初始id号
	private int num = 10; //每次加载评论条数
	private int commentsTime;
	private IsNet isnet;
	private ImageView login_close;  
	private LinearLayout Comments;
	private String role;  //角色
	private String pitch; //语速
	private int audiolocation;
	private SpeechSynthesizer mySynthesizer;
	private ImageView userimage;
	private LayoutParams para; //头像尺寸

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lay_jokes_list_send_comment);
		isnet = new IsNet(this);
		Intent intent0 = getIntent();
		Bundle bundle = intent0.getBundleExtra("value");
		info = (JokeInfo)bundle.getSerializable("JokeInfo");
		initView();
		vid = info.getJokeID();
		url2 = "showdetailjoke.php" + "?vid=" + vid;
		ThreadPoolUtils.execute(new HttpGetThread(handler1, url2));
		detail_List.setAdapter(myCommentListAdapter);
		updatelist(mStart,num);	
		//退出键
		login_close = (ImageView) findViewById(R.id.loginClose);
		login_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		/*
        	在使用语音平台上传应用包的时候会自定生成一个appid 
        	应该使用与包相对应的appid在申请提交后没有使用次数的限制
		 */
		//语音初始化，在使用应用使用时需要初始化一次就好，如果没有这句会出现10111初始化失败
		SpeechUtility.createUtility(getApplicationContext(), "appid=54367c25");
		//处理语音合成关键类，创建一个语言合成
		mySynthesizer = SpeechSynthesizer.createSynthesizer(getApplicationContext(), myInitListener);
		
		
		userimage=(ImageView) findViewById(R.id.userimage);
		if(info.getHead().equals("headimage1")){
			userimage.setImageResource(R.drawable.user_head_image1);
		}else if(info.getHead().equals("headimage2")){
			userimage.setImageResource(R.drawable.user_head_image2);
		}else if(info.getHead().equals("headimage3")){
			userimage.setImageResource(R.drawable.user_head_image3);
		}else if(info.getHead().equals("headimage4")){
			userimage.setImageResource(R.drawable.user_head_image4);
		}else if(info.getHead().equals("headimage5")){
			userimage.setImageResource(R.drawable.user_head_image5);
		}else if(info.getHead().equals("headimage6")){
			userimage.setImageResource(R.drawable.user_head_image6);
		}else if(info.getHead().equals("headimage7")){
			userimage.setImageResource(R.drawable.user_head_image7);
		}else if(info.getHead().equals("headimage8")){
			userimage.setImageResource(R.drawable.user_head_image8);
		}else if(info.getHead().equals("headimage9")){
			userimage.setImageResource(R.drawable.user_head_image9);
		}else {
			userimage.setImageResource(R.drawable.user_head_image5);
		}
		para = userimage.getLayoutParams();
		para.height = 100;
		para.width = 100;
		userimage.setLayoutParams(para);
	}  
	
	private InitListener myInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			Log.d("mySynthesiezer:", "InitListener init() code = " + code);
		}
	};

	private void initView() {	
		myJson = new MyJson();
		list = new ArrayList<CommentsInfo>();
		commentEditText = (EditText)this.findViewById(R.id.write_CommentText);
		sendComment = (Button)this.findViewById(R.id.comment_Btn);
		comment_txt = (TextView)this.findViewById(R.id.DetailText);	
		imageView = (ImageView)this.findViewById(R.id.DetailImg);
		detail_List = (JokerDetailsListView)this.findViewById(R.id.Detail_List1);
		Joker_Detail__progressBar = (LinearLayout) findViewById(R.id.Detail__progressBar1);
		Detail_NoCommentText = (TextView) findViewById(R.id.Detail_NoCommentsText);
		Detail_UserName = (TextView)findViewById(R.id.Detail_UserName);
		Detail_Up_text = (TextView)findViewById(R.id.Detail_Up_text);
		detail_Up_image = (ImageView) findViewById(R.id.Detail_Up_img);
		Detail_Share = (LinearLayout)findViewById(R.id.Detail_Share);
		Detail_up = (LinearLayout)findViewById(R.id.Detail_Up);
		detail_Radio = (ImageView)findViewById(R.id.Detail_Radio);
		Comments = (LinearLayout) findViewById(R.id.Item_Comments);


		//设置笑话作者
		userName = info.getUsername();
		Detail_UserName.setText(userName);

		//初始化笑话的文本信息
		comment_txt.setText(info.getText());

		//初始化被赞次数
		Detail_Up_text.setText(info.getLike()+"");

		//如果笑话中存在图片则显示，否则隐藏
		if (info.getImage().equalsIgnoreCase("")){
			imageView.setVisibility(View.GONE);
		}else{
			imageView.setVisibility(View.VISIBLE);
			imageLoader = new ImageLoader(this);
			imageLoader.DisplayImage(Model.HTTPSTOR+info.getImage(), imageView);
		}


		myCommentListAdapter = new MyCommentListAdapter(this, list);
		ListButton = new Button(JokerSendCommentActivity.this); //加载评论按钮
		ListButton.setText("点击加载更多");
		ListButton.setBackgroundColor(Color.rgb(255, 255, 255));
		ListButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag && listBottemFlag) {
					getOrputFlag = true;
					updatelist(mStart,num);
					listBottemFlag = false;
				} else if (!listBottemFlag)
					Toast.makeText(JokerSendCommentActivity.this, "正在加载中...", 1).show();
			}
		});

		detail_List.addFooterView(ListButton, null, false);
		ListButton.setVisibility(View.GONE);	

		//事件监听
		Detail_up.setOnClickListener(JokerSendCommentActivity.this);
		sendComment.setOnClickListener(JokerSendCommentActivity.this);
		Detail_Share.setOnClickListener(JokerSendCommentActivity.this);

		//音频播放
		if(info.getRadio().equals("")){
			detail_Radio.setVisibility(View.GONE);
		}else{
			detail_Radio.setVisibility(View.VISIBLE);
			detail_Radio.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(player==null){
						player = new MediaPlayer();
					}else if(vid != info.getJokeID()){
						player.stop();
						player.reset();
					}else{
						if(player.isPlaying()){
							player.reset();
							return;
						}else
							player.reset();
					}
					if(!player.isPlaying()){
						String path = Model.HTTPSTOR+info.getRadio();
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
						vid = info.getJokeID();
					}else{
						player.stop();
					}
				}
			});
		}

		//处理音频
		if(info.getRadio().equals("")){
			detail_Radio.setVisibility(View.GONE);
		}else{
			detail_Radio.setVisibility(View.VISIBLE);
			detail_Radio.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(!isnet.IsConnect()){
						Toast.makeText(getApplicationContext(), "请先连接网络", Toast.LENGTH_SHORT).show();
						return;
					}

					if (Model.audioplay==false) {  //判断合成语音是否在播放
						if(Model.player.isPlaying()){   //判断语音是否在播放
							if (info.getJokeID() == Model.palyjokeid) {  //判断是否在播放同一个笑话
								Model.player.stop();    //停止播放
								Model.player.reset();
								Toast.makeText(getApplicationContext(), "停止语音播放！", Toast.LENGTH_SHORT)
								.show();
								Model.palyjokeid = -1;
							}else {
								audioplay();
							}
						} else {
							audioplay();
						}
					} else {
						Toast.makeText(getApplicationContext(), "先停止语音播放！", Toast.LENGTH_SHORT)
						.show();
					}
				}
			});
		}


		Comments.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg) {		
				//author：王亚伟	
				//功能：文本合成语音，播放不同语音
				if (Model.player.isPlaying()==false) { //判断语音是否在播放
					if (mySynthesizer.isSpeaking()) { //判断合成语音是否在播放
						if (info.getJokeID() == Model.jokeid) {   //判断是否在合成同一个笑话
							mySynthesizer.destroy();    //点击播放文本，停止播放本笑话
							Model.audioplay=false;
							Toast.makeText(getApplicationContext(), "停止合成语音播放！", Toast.LENGTH_SHORT)
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
					Toast.makeText(getApplicationContext(), "先停止语音播放！", Toast.LENGTH_SHORT)
					.show();
				}
			}
		});

	}

	private void updatelist(int mStart, int num) {
		url = Model.SHOWCOMMENTS + "?vid=" + vid + "&start=" + mStart + "&num=" + num;
		ThreadPoolUtils.execute(new HttpGetThread(handler, url));
	}


	@Override
	public void onClick(View v) {
		int mID = v.getId();
		switch(mID){
		//点赞
		case R.id.Detail_Up:
			if(!isnet.IsConnect()){
				Toast.makeText(this, "请先打开网络", Toast.LENGTH_SHORT).show();
				return;
			}
			if (Model.MYUSERINFO != null){
				if (upFlag){ //用户首次点击点赞按钮才查询数据库看该笑话是否已被该用户点赞
					up();
					upFlag = false;
				}else {
					Toast.makeText(JokerSendCommentActivity.this, "您已经点过赞了", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(JokerSendCommentActivity.this, "登陆后才能点赞", 1).show();
				Intent intent = new Intent();
				intent.setClass(JokerSendCommentActivity.this, JokerLoginActivity.class);
				startActivity(intent);
			}
			break;
			//分享
		case R.id.Detail_Share:
			String imageUrl = Model.HTTPSTOR+info.getImage();
			JokerShare as = new JokerShare(JokerSendCommentActivity.this,info.getText(),imageUrl);
			as.show();
			break;
			//评论
		case R.id.comment_Btn:
			if(!isnet.IsConnect()){
				Toast.makeText(this, "请先连接网络", Toast.LENGTH_SHORT).show();
				break;
			}
			sendComment();
			break;
		default:
			break;
		}		
	}

	//实现点赞的方法
	private void up(){	
		int vid = info.getJokeID();
		int uid = Model.MYUSERINFO.getUserID();
		String url = Model.LIKE + "?vid=" + vid + "&uid=" + uid;
		Handler handler1 = new Handler() {
			public void handleMessage(android.os.Message msg) {
				super.handleMessage(msg);
				String result = msg.obj.toString();
				if (result.equals("ok")) {
					int num;
					num = info.getLike() + 1;
					Detail_Up_text.setText(num + "");
					detail_Up_image.setImageResource(R.drawable.icon_img_like_enable);
					info.setLike(num);
				}else {
					Toast.makeText(JokerSendCommentActivity.this, "您已经点过赞了", Toast.LENGTH_SHORT).show();
				}
			}
		};
		ThreadPoolUtils.execute(new HttpGetThread(handler1, url)); //到数据库中查询笑话是否被赞过	
	}

	//发送评论内容的方法
	private void sendComment() {
		if(Model.MYUSERINFO != null){
			//设置评论信息：评论内容，评论用户id，笑话id
			text = commentEditText.getText().toString();
			int text_length = text.getBytes().length;
			uid = Model.MYUSERINFO.getUserID();	
			String Json = "{\"text\":\"" + text + "\"," + "\"vid\":\"" + vid + "\"," + "\"uid\":\"" + uid + "\"}";	
			//评论内容为空时禁止提交数据
			if(null == text || ("").equals(text)){
				Toast.makeText(getApplicationContext(), "请填写评论内容", 1).show();
				return;
			}
			//评论内容过多时，评论失败
			if (text_length > 80){
				Toast.makeText(getApplicationContext(), "评论内容不要超过80字哦~", 1).show();
				return;
			}
			//将用户的评论增加到评论列表中
			CommentsInfo commentsInfo = new CommentsInfo();
			commentsInfo.setUsername(Model.MYUSERINFO.getUsername());
			commentsInfo.setCommentsID(mStart);
			commentsInfo.setJokeID(vid);
			commentsInfo.setUserID(uid);
			commentsInfo.setComment(text);
			list.add(commentsInfo);
			myCommentListAdapter.notifyDataSetInvalidated();
			commentEditText.setText("");
			//评论按钮只做一次响应
			if(buttonFlag){ 
				ThreadPoolUtils.execute(new HttpPostThread(senthandler, Model.SENDCOMMENT, Json));
				updatelist(mStart,num);
				buttonFlag = false;
			}			
		}else{
			Toast.makeText(getApplicationContext(), "登陆后才能评论", 1000).show();
			Intent intent = new Intent();
			intent.setClass(JokerSendCommentActivity.this, JokerLoginActivity.class);
			startActivity(intent);
		}		    
	}

	private void audioplay(){
		Model.player.stop();    //停止播放
		Model.player.reset();
		String path = Model.HTTPSTOR
				+ info.getRadio();
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
		Model.palyjokeid = info.getJokeID();
		Toast.makeText(getApplicationContext(), "语音开始播放",
				Toast.LENGTH_SHORT).show();
	}

	private void play(String role,String pitch) {
		//设置发音人
		mySynthesizer.setParameter(SpeechConstant.VOICE_NAME,role);  
		//设置语速
		mySynthesizer.setParameter(SpeechConstant.PITCH,pitch);	
		//设置音量
		mySynthesizer.setParameter(SpeechConstant.VOLUME,"50");	
		//开始播放
		mySynthesizer.startSpeaking(info.getText(),mTtsListener);  
		Model.jokeid=info.getJokeID();
		Model.audioplay=true;
		Toast.makeText(getApplicationContext(), "开始播放笑话，请欣赏~",
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

	Handler handler1 = new Handler(){
		public void handleMessage(android.os.Message msg){
			super.handleMessage(msg);
			if(msg.what == 200){
				JSONArray joke;
				try {
					joke = new JSONArray(msg.obj.toString());
					JokeInfo jokeinfo = new JokeInfo();
					JSONObject joketemp = joke.getJSONObject(0);
					jokeinfo.setCommentstimes(joketemp.getInt("COMMENTSTIMES"));
					commentsTime = jokeinfo.getCommentstimes(); //每次进入评论页面都要获取最新的评论次数
				} catch (JSONException e) {
					e.printStackTrace();
				}		
			}
		}
	};
	Handler senthandler = new Handler(){
		public void handleMessage(android.os.Message msg){
			super.handleMessage(msg);
			//			Toast.makeText(getBaseContext(),msg.obj.toString(), 1000).show();
		}
	};
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg){
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(JokerSendCommentActivity.this, "请求失败，服务器故障", 1).show();
				listBottemFlag = true;
			} else if (msg.what == 100) {
				Toast.makeText(JokerSendCommentActivity.this, "服务器无响应", 1).show();
				listBottemFlag = true;
			} else if (msg.what == 200) {
				String result = (String)msg.obj;
				if (result != null && getOrputFlag == true){
					List<CommentsInfo> newList = myJson.getCommentsInfo(result);
					if (newList != null) {
						if (newList.size() == num) {
							detail_List.setVisibility(View.VISIBLE);
							ListButton.setVisibility(View.VISIBLE);
							mStart += num;
						} else if (newList.size() == 0) {
							if (list.size() == 0)
								Detail_NoCommentText.setVisibility(View.VISIBLE);
							ListButton.setVisibility(View.GONE);
							Toast.makeText(JokerSendCommentActivity.this, "加载完毕...", 1000).show();
						} else {
							if(mStart+num>=commentsTime){
								mStart = commentsTime;
							}
							detail_List.setVisibility(View.VISIBLE);
							ListButton.setVisibility(View.GONE);
						}
						for (CommentsInfo commentsInfo : newList) {
							list.add(commentsInfo);
						}
						listBottemFlag = true;
					} else {
						//无用户评论时给出的提示信息
						Detail_NoCommentText.setVisibility(View.VISIBLE);
					}
					Joker_Detail__progressBar.setVisibility(View.GONE);
					myCommentListAdapter.notifyDataSetChanged();
					getOrputFlag = false;
				}else if(result!=null && getOrputFlag== false){
					//评论成功后将评论框置空
					commentEditText.setText("");
					detail_List.setVisibility(View.VISIBLE);
					ListButton.setVisibility(View.GONE);
					ListButton.setClickable(false);
					if (mStart == 0){
						//当评论为第一条评论时，增加加载按钮的同时隐藏“该笑话暂无评论”的信息
						Detail_NoCommentText.setVisibility(View.GONE);
					}
					buttonFlag = true;  //允许用户进行多次评论
				}
			}
		};
	};
}
