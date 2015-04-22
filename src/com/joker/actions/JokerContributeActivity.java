package com.joker.actions;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import com.gen.joker.*;
import com.joker.model.Model;
import com.joker.net.IsNet;
import com.joker.net.ThreadPoolUtils;
import com.joker.thread.HttpPostThread;
import com.joker.utils.Base64Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * @author:李颜翎 审稿
 * @说明：这是登录用户投稿对应的的Activity
 */
public class JokerContributeActivity extends Activity {

	private ImageView close, contribute, album, radiobutton,p_image;
	private EditText neirongEdit;
	private String img = "";// 图片压缩后数据
	private String radio = "";// 音频压缩后数据
	private String Picpath = null;// 图片本地路径
	private String voicefile = null;// 音频本地路径
	private IsNet isnet;
	private long time_start = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lay_joker_contribute);
		isnet = new IsNet(this);
		initView();
	}

	/*
	 * @author郑轩 初始化
	 */
	private void initView() {
		// 获取关闭按钮id
		close = (ImageView) findViewById(R.id.close);
		MyOnclickListener mOnclickListener = new MyOnclickListener();
		RadioListener radioListener = new RadioListener();
		// 发表按钮
		contribute = (ImageView) findViewById(R.id.UpLoadEdit);
		// 图片按钮
		album = (ImageView) findViewById(R.id.album);
		p_image = (ImageView) findViewById(R.id.p_image);
		radiobutton = (ImageView) findViewById(R.id.radio);
		neirongEdit = (EditText) findViewById(R.id.neirongEdit);
		close.setOnClickListener(mOnclickListener);
		contribute.setOnClickListener(mOnclickListener);
		radiobutton.setOnTouchListener(radioListener);
		album.setOnClickListener(mOnclickListener);
	}

	/*
	 * @author郑轩 关闭，发布，插入图片按钮事件监听
	 */
	private class MyOnclickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			int ID = v.getId();
			switch (ID) {
			case R.id.close:
				JokerContributeActivity.this.finish();
				break;
			case R.id.UpLoadEdit:
				if(!isnet.IsConnect()){
					Toast.makeText(JokerContributeActivity.this, "请先连接网络", Toast.LENGTH_SHORT).show();
				}else if (Model.MYUSERINFO != null) {// 判断用户有无登陆
					sendJoke();
				} else {
					Intent intent = new Intent(JokerContributeActivity.this,
							JokerLoginActivity.class);
					startActivity(intent);
					Toast.makeText(JokerContributeActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.album:
				// 这个是调用android内置的intent，来过滤图片文件 ，同时也可以过滤其他的
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_PICK);
				intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//设置intent使得在4.4及以上版本不出现图片选择器
				startActivityForResult(intent, 1);
				break;
			}
		}
	}

	private class RadioListener implements OnTouchListener {

		MediaRecorder recorder = null;
		String filename = "";

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			long time_end=0;
			if (event.getAction() == 0) {
				time_start=System.currentTimeMillis();
				Toast.makeText(JokerContributeActivity.this, "开始录音啦~", 1000)
				.show();
				filename = new Date().getTime() + ".amr";
				recorder = new MediaRecorder();
				recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
				recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				recorder.setOutputFile("/sdcard/" + filename);
				try {
					recorder.prepare();
				} catch (Exception e) {
					Toast.makeText(JokerContributeActivity.this, "again", 1000)
					.show();
				}
				recorder.start();// 按下按钮开始录音
			} else if (event.getAction() == 1) {
				time_end=System.currentTimeMillis();
				if(time_end-time_start<2000){
					Toast.makeText(JokerContributeActivity.this, "至少要2秒呦~", Toast.LENGTH_SHORT).show();
					radiobutton.setEnabled(false);
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							recorder.stop();
							radiobutton.setEnabled(true);
						}
					}, 1000);
					voicefile = "/sdcard/" + filename;
					new File(voicefile).delete();

				}
				else{
					radiobutton.setImageResource(R.drawable.icon_radio_finished);
					recorder.stop();
					recorder.reset();// 放开按钮录音结束
					voicefile = "/sdcard/" + filename;
					
					try {
						radio = Base64Util.encodeBase64File(voicefile);
					} catch (Exception e) {
						e.printStackTrace();
					}
					Toast.makeText(JokerContributeActivity.this, "插入音频成功", Toast.LENGTH_SHORT).show();
					new File(voicefile).delete();
				}
			}
			return true;
		}
	}

	/*
	 * @author郑轩 发布笑话
	 */
	private void sendJoke() {
		if (neirongEdit.getText().toString().equals("")) {
			Toast.makeText(JokerContributeActivity.this, "请先填写笑话文字再提交",
					Toast.LENGTH_SHORT).show();
			return;
		}else{
			int neirongLength = neirongEdit.getText().toString().getBytes().length;
			if (neirongLength<6){
				//笑话内容过短时给出提示
				Toast.makeText(JokerContributeActivity.this, "最少要2个字呦~", Toast.LENGTH_SHORT).show();
				return;
			}else if(neirongLength>400){
				//笑话内容过长时给出提示
				Toast.makeText(JokerContributeActivity.this, "笑话内容过长", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		int uid = Model.MYUSERINFO.getUserID();// 用户ID
		String xvalue = neirongEdit.getText().toString();// 笑话内容
		String ximg = "";// 笑话图片
		String xradio = "";// 笑话音频
		if (!img.equalsIgnoreCase("")) {
			ximg = System.currentTimeMillis() + ".png";// 笑话图片
		}
		if (!radio.equalsIgnoreCase("")) {
			xradio = System.currentTimeMillis() + ".war";// 笑话音频
		}
		String Json = "{\"uid\":\"" + uid + "\"," + "\"radio\":\"" + xradio
				+ "\"," + "\"ximg\":\"" + ximg + "\"," + "\"value\":\""
				+ xvalue + "\"}";
		ThreadPoolUtils.execute(new HttpPostThread(handler, Model.CONTRIBUTE,
				Json, img, radio));
		JokerContributeActivity.this.finish();
	}

	/*
	 * @author郑轩 接收子线程连接服务器返回的数据msg
	 */
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null && result.equals("ok")) {
					Toast.makeText(JokerContributeActivity.this, "笑话发送成功", 1000)
					.show();
					JokerContributeActivity.this.finish();
				}
			}
		};
	};

	/*
	 * @author郑轩 接收用户决定添加图片，选择图片后的返回值
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			/**
			 * 当选择的图片不为空的话，在获取到图片的途径
			 */
			Uri uri = data.getData();
			// Log.e(TAG, "uri = " + uri);
			try {
				String[] pojo = { MediaStore.Images.Media.DATA };

				Cursor cursor = managedQuery(uri, pojo, null, null, null);
				if (cursor != null) {
					ContentResolver cr = this.getContentResolver();
					int colunm_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					String path = cursor.getString(colunm_index);
					/*
					 * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，你选择的文件就不一定是图片了，
					 * 这样的话，我们判断文件的后缀名 如果是图片格式的话，那么才可以
					 */
					if (path.endsWith("jpg") || path.endsWith("png")) {
						Toast.makeText(JokerContributeActivity.this, "添加图片成功",
								1000).show();
						//album.setBackgroundResource(R.drawable.icon_radio_finished);
						Picpath = path;
						img = Base64Util.encodeBase64File(Picpath);

						DisplayMetrics dm = getResources().getDisplayMetrics();
						int w_screen = dm.widthPixels;
						int h_screen = dm.heightPixels;//获取屏幕的长，宽
						
						// 压缩方法同ImageLoader,计算压缩比例
						BitmapFactory.Options o = new BitmapFactory.Options();
                        o.inJustDecodeBounds = true;
                        FileInputStream stream1=new FileInputStream(Picpath);
                        BitmapFactory.decodeStream(stream1,null,o);
                        stream1.close();

						 int w = o.outWidth;
						 int h = o.outHeight;
						//
						// float hh =h_screen;//这里设置高度为200f
						 float ww = w_screen;//这里设置宽度为480f
						// //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
						 int be = 1;//be=1表示不缩放
						 if ( w > ww) {//如果宽度大的话根据宽度固定大小缩放,w>h&&w>>ww就要求宽大于高，现在的条件只要宽度大于屏幕就压缩
						 be = (int) (o.outWidth / ww);
						 }
//						 else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
//						 be = (int) (o.outHeight / hh);
//						 }
						 if (be <= 0)
						 be = 1;
						 o.inSampleSize = be;//计算缩放比例
						 BitmapFactory.Options o2 = new BitmapFactory.Options();
	                        o2.inSampleSize=be;
	                    FileInputStream stream2=new FileInputStream(Picpath);
						
						Bitmap bitmap=BitmapFactory.decodeStream(stream2,null,o2);
						stream2.close();
						p_image.setVisibility(View.VISIBLE);
						p_image.setAdjustViewBounds(true);// 取消图片上下方
						p_image.setImageBitmap(bitmap);//显示图片
					} else {
						alert();
					}
				} else {
					alert();
				}

			} catch (Exception e) {
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/*
	 * @author郑轩 提示：若用户选择的非图片，提示重新选择
	 */
	private void alert() {
		Dialog dialog = new AlertDialog.Builder(this).setTitle("提示")
				.setMessage("您选择的不是有效的图片")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Picpath = null;
					}
				}).create();
		dialog.show();
	}
}
