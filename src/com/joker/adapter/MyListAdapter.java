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

	private IsNet isnet;  //�����жϱ�־
	private SpeechSynthesizer mySynthesizer;
	private String role;  //��ɫ
	private String pitch; //����
	private int location; //����λ��
	private int audiolocation;
	private String text="   ����";
	private LayoutParams para; //ͷ��ߴ�


	public MyListAdapter(Context ctx, List<JokeInfo> list) {
		this.list = list;
		this.ctx = ctx;
		imageLoader=new ImageLoader(ctx);
		isnet = new IsNet(ctx);
		/*
                                  ��ʹ������ƽ̨�ϴ�Ӧ�ð���ʱ����Զ�����һ��appid 
                                  Ӧ��ʹ��������Ӧ��appid�������ύ��û��ʹ�ô���������
		 */
		//������ʼ������ʹ��Ӧ��ʹ��ʱ��Ҫ��ʼ��һ�ξͺã����û���������10111��ʼ��ʧ��
		SpeechUtility.createUtility(ctx, "appid=54367c25");
		//���������ϳɹؼ��࣬����һ�����Ժϳ�
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

		//����ͷ��
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
			//����Ĭ��
			hold.userimage.setImageResource(R.drawable.user_head_image5);
		}
		para = hold.userimage.getLayoutParams();
		para.height = 100;
		para.width = 100;
		hold.userimage.setLayoutParams(para);
		

		//��ʾͼƬ
		if(list.get(arg0).getImage().equalsIgnoreCase("")){
			hold.Img.setVisibility(View.GONE);
		}else{
			imageLoader.DisplayImage(Model.HTTPSTOR + list.get(arg0).getImage(), hold.Img);
			hold.Img.setVisibility(View.VISIBLE);
		}

		//������Ƶ
		if(list.get(arg0).getRadio().equals("")){
			hold.radio.setVisibility(View.GONE);
		}else{
			//����������û������
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
						Toast.makeText(ctx, "������������", Toast.LENGTH_SHORT).show();
						return;
					}

					if (Model.audioplay==false) {  //�жϺϳ������Ƿ��ڲ���
						if(Model.player.isPlaying()){   //�ж������Ƿ��ڲ���
							if (list.get(arg0).getJokeID() == Model.palyjokeid) {  //�ж��Ƿ��ڲ���ͬһ��Ц��
								Model.anim.stop();
								Model.player.stop();    //ֹͣ����
								Model.player.reset();
								//Toast.makeText(ctx, "ֹͣ�������ţ�", Toast.LENGTH_SHORT).show();
								Model.palyjokeid = -1;
							}else {
								Toast.makeText(ctx, "������ʼ����",Toast.LENGTH_LONG).show();
								//����gifͼƬ
								Model.anim.stop();
								Object ob = hold.radio.getBackground();
								Model.anim = (AnimationDrawable) ob;
								Model.anim.stop();
								Model.anim.start();
								audioplay();
							}
						} else {
							Toast.makeText(ctx, "������ʼ����",Toast.LENGTH_LONG).show();
							//����gifͼƬ
							Object ob = hold.radio.getBackground();
							Model.anim = (AnimationDrawable) ob;
							Model.anim.stop();
							Model.anim.start();
							audioplay();
						}
					} else {
						Toast.makeText(ctx, "��ֹͣ�������ţ�", Toast.LENGTH_SHORT).show();
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
						Toast.makeText(ctx, "���ȴ�����", Toast.LENGTH_SHORT).show();
						return;
					}
					if (upFlag){  //�û��״ε�����ް�ť�Ų�ѯ���ݿ⿴��Ц���Ƿ��ѱ����û�����
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
									Toast.makeText(ctx, "���Ѿ��������", Toast.LENGTH_SHORT).show();
								}
							}
						};
						ThreadPoolUtils.execute(new HttpGetThread(handler1, url)); //�����ݿ��в�ѯЦ���Ƿ��޹�
						upFlag = false;
					}
					else {
						Toast.makeText(ctx, "���Ѿ��������", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(ctx, "��½����ܵ���", Toast.LENGTH_SHORT).show();
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
				//author������ΰ	
				//���ܣ��ı��ϳ����������Ų�ͬ����
				location=arg0;      
				if (Model.player.isPlaying()==false) { //�ж������Ƿ��ڲ���
					if (mySynthesizer.isSpeaking()) { //�жϺϳ������Ƿ��ڲ���
						if (list.get(arg0).getJokeID() == Model.jokeid) {   //�ж��Ƿ��ںϳ�ͬһ��Ц��
							mySynthesizer.destroy();    //��������ı���ֹͣ���ű�Ц��
							Model.audioplay=false;
							Toast.makeText(ctx, "ֹͣ�ϳ��������ţ�", Toast.LENGTH_SHORT)
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
					Toast.makeText(ctx, "��ֹͣ�������ţ�", Toast.LENGTH_SHORT)
					.show();
				}
			}
		});

		return arg1;
	}

	private void audioplay(){

		Model.player.stop();    //ֹͣ����
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
		Toast.makeText(ctx, "�����Ѿ�����",Toast.LENGTH_SHORT).show();
	}
	private void play(String role,String pitch) {
		//���÷�����
		mySynthesizer.setParameter(SpeechConstant.VOICE_NAME,role);  
		//��������
		mySynthesizer.setParameter(SpeechConstant.PITCH,pitch);	
		//��������
		mySynthesizer.setParameter(SpeechConstant.VOLUME,"50");	
		//��ʼ����
		mySynthesizer.startSpeaking(list.get(location).getText(),mTtsListener);  
		Model.jokeid=list.get(location).getJokeID();
		Model.audioplay=true;
		Toast.makeText(ctx, "��ʼ����Ц����������~",
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
