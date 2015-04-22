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

	private EditText commentEditText; //�������ݱ༭��
	private Button sendComment; //�������۰�ť
	private TextView comment_txt; //Ц���ı�����
	private TextView Detail_Up_text; //���޴���
	private ImageView imageView,detail_Radio,detail_Up_image; //Ц��ͼƬ,��Ƶ����
	private TextView Detail_NoCommentText; //���û�����ʱ��ʾ����Ϣ
	private TextView Detail_UserName; //��ʾЦ�����ߵ���Ϣ
	private ImageLoader imageLoader; //����Ц��ͼƬ
	private JokerDetailsListView detail_List; //��ʾ�����б����
	private Button ListButton = null; //�������۵İ�ť
	private LinearLayout Joker_Detail__progressBar;
	private LinearLayout Detail_Share, Detail_up; //��������
	private MediaPlayer player;

	private MyJson myJson;
	private JokeInfo info = null;
	private List<CommentsInfo> list;
	private MyCommentListAdapter myCommentListAdapter;
	private String text; //��������
	private int vid, uid; //Ц��id,�û�id
	private String userName;
	public String url = Model.SENDCOMMENT; //�ύ����Url
	public String url1 = Model.SHOWCOMMENTS; //�������ȡ���۵�Url
	private String url2 = "showdetailjoke.php";


	private boolean flag = true;
	private boolean getOrputFlag = true; //�ж��ǳ�ʼ�����ݻ����ύ���ݵı�ʶ��
	private boolean buttonFlag = true; //�������۰�ťֻ��һ���¼���Ӧ�ı�Ƿ�
	private boolean listBottemFlag = true;
	private boolean upFlag = true;
	private int mStart = 0; //���ۼ��س�ʼid��
	private int num = 10; //ÿ�μ�����������
	private int commentsTime;
	private IsNet isnet;
	private ImageView login_close;  
	private LinearLayout Comments;
	private String role;  //��ɫ
	private String pitch; //����
	private int audiolocation;
	private SpeechSynthesizer mySynthesizer;
	private ImageView userimage;
	private LayoutParams para; //ͷ��ߴ�

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
		//�˳���
		login_close = (ImageView) findViewById(R.id.loginClose);
		login_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		/*
        	��ʹ������ƽ̨�ϴ�Ӧ�ð���ʱ����Զ�����һ��appid 
        	Ӧ��ʹ��������Ӧ��appid�������ύ��û��ʹ�ô���������
		 */
		//������ʼ������ʹ��Ӧ��ʹ��ʱ��Ҫ��ʼ��һ�ξͺã����û���������10111��ʼ��ʧ��
		SpeechUtility.createUtility(getApplicationContext(), "appid=54367c25");
		//���������ϳɹؼ��࣬����һ�����Ժϳ�
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


		//����Ц������
		userName = info.getUsername();
		Detail_UserName.setText(userName);

		//��ʼ��Ц�����ı���Ϣ
		comment_txt.setText(info.getText());

		//��ʼ�����޴���
		Detail_Up_text.setText(info.getLike()+"");

		//���Ц���д���ͼƬ����ʾ����������
		if (info.getImage().equalsIgnoreCase("")){
			imageView.setVisibility(View.GONE);
		}else{
			imageView.setVisibility(View.VISIBLE);
			imageLoader = new ImageLoader(this);
			imageLoader.DisplayImage(Model.HTTPSTOR+info.getImage(), imageView);
		}


		myCommentListAdapter = new MyCommentListAdapter(this, list);
		ListButton = new Button(JokerSendCommentActivity.this); //�������۰�ť
		ListButton.setText("������ظ���");
		ListButton.setBackgroundColor(Color.rgb(255, 255, 255));
		ListButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag && listBottemFlag) {
					getOrputFlag = true;
					updatelist(mStart,num);
					listBottemFlag = false;
				} else if (!listBottemFlag)
					Toast.makeText(JokerSendCommentActivity.this, "���ڼ�����...", 1).show();
			}
		});

		detail_List.addFooterView(ListButton, null, false);
		ListButton.setVisibility(View.GONE);	

		//�¼�����
		Detail_up.setOnClickListener(JokerSendCommentActivity.this);
		sendComment.setOnClickListener(JokerSendCommentActivity.this);
		Detail_Share.setOnClickListener(JokerSendCommentActivity.this);

		//��Ƶ����
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

		//������Ƶ
		if(info.getRadio().equals("")){
			detail_Radio.setVisibility(View.GONE);
		}else{
			detail_Radio.setVisibility(View.VISIBLE);
			detail_Radio.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(!isnet.IsConnect()){
						Toast.makeText(getApplicationContext(), "������������", Toast.LENGTH_SHORT).show();
						return;
					}

					if (Model.audioplay==false) {  //�жϺϳ������Ƿ��ڲ���
						if(Model.player.isPlaying()){   //�ж������Ƿ��ڲ���
							if (info.getJokeID() == Model.palyjokeid) {  //�ж��Ƿ��ڲ���ͬһ��Ц��
								Model.player.stop();    //ֹͣ����
								Model.player.reset();
								Toast.makeText(getApplicationContext(), "ֹͣ�������ţ�", Toast.LENGTH_SHORT)
								.show();
								Model.palyjokeid = -1;
							}else {
								audioplay();
							}
						} else {
							audioplay();
						}
					} else {
						Toast.makeText(getApplicationContext(), "��ֹͣ�������ţ�", Toast.LENGTH_SHORT)
						.show();
					}
				}
			});
		}


		Comments.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg) {		
				//author������ΰ	
				//���ܣ��ı��ϳ����������Ų�ͬ����
				if (Model.player.isPlaying()==false) { //�ж������Ƿ��ڲ���
					if (mySynthesizer.isSpeaking()) { //�жϺϳ������Ƿ��ڲ���
						if (info.getJokeID() == Model.jokeid) {   //�ж��Ƿ��ںϳ�ͬһ��Ц��
							mySynthesizer.destroy();    //��������ı���ֹͣ���ű�Ц��
							Model.audioplay=false;
							Toast.makeText(getApplicationContext(), "ֹͣ�ϳ��������ţ�", Toast.LENGTH_SHORT)
							.show();
							Model.jokeid = -1;
						} else {
							if (Model.MYUSERINFO == null) {
								play("xiaoyan", 50 + ""); //��ʼ�ϳ���������

							} else {
								role=Model.MYUSERINFO.getAudiorole();	//ȡ����role
								pitch=Model.MYUSERINFO.getAudiospeed()+"";	//ȡ����pitch
								System.out.println("��ȡ����������");
								play(role, pitch);
							}
						}
					} else {
						if (Model.MYUSERINFO == null) {
							play("xiaoyan", 50 + ""); //��ʼ���źϳ�����

						} else {
							role=Model.MYUSERINFO.getAudiorole();	//ȡ����role
							pitch=Model.MYUSERINFO.getAudiospeed()+"";	//ȡ����pitch
							System.out.println("��ȡ����������");
							play(role, pitch);
						}
					}
				} else {
					Toast.makeText(getApplicationContext(), "��ֹͣ�������ţ�", Toast.LENGTH_SHORT)
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
		//����
		case R.id.Detail_Up:
			if(!isnet.IsConnect()){
				Toast.makeText(this, "���ȴ�����", Toast.LENGTH_SHORT).show();
				return;
			}
			if (Model.MYUSERINFO != null){
				if (upFlag){ //�û��״ε�����ް�ť�Ų�ѯ���ݿ⿴��Ц���Ƿ��ѱ����û�����
					up();
					upFlag = false;
				}else {
					Toast.makeText(JokerSendCommentActivity.this, "���Ѿ��������", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(JokerSendCommentActivity.this, "��½����ܵ���", 1).show();
				Intent intent = new Intent();
				intent.setClass(JokerSendCommentActivity.this, JokerLoginActivity.class);
				startActivity(intent);
			}
			break;
			//����
		case R.id.Detail_Share:
			String imageUrl = Model.HTTPSTOR+info.getImage();
			JokerShare as = new JokerShare(JokerSendCommentActivity.this,info.getText(),imageUrl);
			as.show();
			break;
			//����
		case R.id.comment_Btn:
			if(!isnet.IsConnect()){
				Toast.makeText(this, "������������", Toast.LENGTH_SHORT).show();
				break;
			}
			sendComment();
			break;
		default:
			break;
		}		
	}

	//ʵ�ֵ��޵ķ���
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
					Toast.makeText(JokerSendCommentActivity.this, "���Ѿ��������", Toast.LENGTH_SHORT).show();
				}
			}
		};
		ThreadPoolUtils.execute(new HttpGetThread(handler1, url)); //�����ݿ��в�ѯЦ���Ƿ��޹�	
	}

	//�����������ݵķ���
	private void sendComment() {
		if(Model.MYUSERINFO != null){
			//����������Ϣ���������ݣ������û�id��Ц��id
			text = commentEditText.getText().toString();
			int text_length = text.getBytes().length;
			uid = Model.MYUSERINFO.getUserID();	
			String Json = "{\"text\":\"" + text + "\"," + "\"vid\":\"" + vid + "\"," + "\"uid\":\"" + uid + "\"}";	
			//��������Ϊ��ʱ��ֹ�ύ����
			if(null == text || ("").equals(text)){
				Toast.makeText(getApplicationContext(), "����д��������", 1).show();
				return;
			}
			//�������ݹ���ʱ������ʧ��
			if (text_length > 80){
				Toast.makeText(getApplicationContext(), "�������ݲ�Ҫ����80��Ŷ~", 1).show();
				return;
			}
			//���û����������ӵ������б���
			CommentsInfo commentsInfo = new CommentsInfo();
			commentsInfo.setUsername(Model.MYUSERINFO.getUsername());
			commentsInfo.setCommentsID(mStart);
			commentsInfo.setJokeID(vid);
			commentsInfo.setUserID(uid);
			commentsInfo.setComment(text);
			list.add(commentsInfo);
			myCommentListAdapter.notifyDataSetInvalidated();
			commentEditText.setText("");
			//���۰�ťֻ��һ����Ӧ
			if(buttonFlag){ 
				ThreadPoolUtils.execute(new HttpPostThread(senthandler, Model.SENDCOMMENT, Json));
				updatelist(mStart,num);
				buttonFlag = false;
			}			
		}else{
			Toast.makeText(getApplicationContext(), "��½���������", 1000).show();
			Intent intent = new Intent();
			intent.setClass(JokerSendCommentActivity.this, JokerLoginActivity.class);
			startActivity(intent);
		}		    
	}

	private void audioplay(){
		Model.player.stop();    //ֹͣ����
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
		Toast.makeText(getApplicationContext(), "������ʼ����",
				Toast.LENGTH_SHORT).show();
	}

	private void play(String role,String pitch) {
		//���÷�����
		mySynthesizer.setParameter(SpeechConstant.VOICE_NAME,role);  
		//��������
		mySynthesizer.setParameter(SpeechConstant.PITCH,pitch);	
		//��������
		mySynthesizer.setParameter(SpeechConstant.VOLUME,"50");	
		//��ʼ����
		mySynthesizer.startSpeaking(info.getText(),mTtsListener);  
		Model.jokeid=info.getJokeID();
		Model.audioplay=true;
		Toast.makeText(getApplicationContext(), "��ʼ����Ц����������~",
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
		//���Ž������ص���������
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
			Model.audioplay=false;	//�ϳɽ���,�ϳɲ��ű�־����false
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
					commentsTime = jokeinfo.getCommentstimes(); //ÿ�ν�������ҳ�涼Ҫ��ȡ���µ����۴���
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
				Toast.makeText(JokerSendCommentActivity.this, "����ʧ�ܣ�����������", 1).show();
				listBottemFlag = true;
			} else if (msg.what == 100) {
				Toast.makeText(JokerSendCommentActivity.this, "����������Ӧ", 1).show();
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
							Toast.makeText(JokerSendCommentActivity.this, "�������...", 1000).show();
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
						//���û�����ʱ��������ʾ��Ϣ
						Detail_NoCommentText.setVisibility(View.VISIBLE);
					}
					Joker_Detail__progressBar.setVisibility(View.GONE);
					myCommentListAdapter.notifyDataSetChanged();
					getOrputFlag = false;
				}else if(result!=null && getOrputFlag== false){
					//���۳ɹ������ۿ��ÿ�
					commentEditText.setText("");
					detail_List.setVisibility(View.VISIBLE);
					ListButton.setVisibility(View.GONE);
					ListButton.setClickable(false);
					if (mStart == 0){
						//������Ϊ��һ������ʱ�����Ӽ��ذ�ť��ͬʱ���ء���Ц���������ۡ�����Ϣ
						Detail_NoCommentText.setVisibility(View.GONE);
					}
					buttonFlag = true;  //�����û����ж������
				}
			}
		};
	};
}
