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
 * @author:������ ���
 * @˵�������ǵ�¼�û�Ͷ���Ӧ�ĵ�Activity
 */
public class JokerContributeActivity extends Activity {

	private ImageView close, contribute, album, radiobutton,p_image;
	private EditText neirongEdit;
	private String img = "";// ͼƬѹ��������
	private String radio = "";// ��Ƶѹ��������
	private String Picpath = null;// ͼƬ����·��
	private String voicefile = null;// ��Ƶ����·��
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
	 * @author֣�� ��ʼ��
	 */
	private void initView() {
		// ��ȡ�رհ�ťid
		close = (ImageView) findViewById(R.id.close);
		MyOnclickListener mOnclickListener = new MyOnclickListener();
		RadioListener radioListener = new RadioListener();
		// ����ť
		contribute = (ImageView) findViewById(R.id.UpLoadEdit);
		// ͼƬ��ť
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
	 * @author֣�� �رգ�����������ͼƬ��ť�¼�����
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
					Toast.makeText(JokerContributeActivity.this, "������������", Toast.LENGTH_SHORT).show();
				}else if (Model.MYUSERINFO != null) {// �ж��û����޵�½
					sendJoke();
				} else {
					Intent intent = new Intent(JokerContributeActivity.this,
							JokerLoginActivity.class);
					startActivity(intent);
					Toast.makeText(JokerContributeActivity.this, "���ȵ�¼", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.album:
				// ����ǵ���android���õ�intent��������ͼƬ�ļ� ��ͬʱҲ���Թ���������
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_PICK);
				intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//����intentʹ����4.4�����ϰ汾������ͼƬѡ����
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
				Toast.makeText(JokerContributeActivity.this, "��ʼ¼����~", 1000)
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
				recorder.start();// ���°�ť��ʼ¼��
			} else if (event.getAction() == 1) {
				time_end=System.currentTimeMillis();
				if(time_end-time_start<2000){
					Toast.makeText(JokerContributeActivity.this, "����Ҫ2����~", Toast.LENGTH_SHORT).show();
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
					recorder.reset();// �ſ���ť¼������
					voicefile = "/sdcard/" + filename;
					
					try {
						radio = Base64Util.encodeBase64File(voicefile);
					} catch (Exception e) {
						e.printStackTrace();
					}
					Toast.makeText(JokerContributeActivity.this, "������Ƶ�ɹ�", Toast.LENGTH_SHORT).show();
					new File(voicefile).delete();
				}
			}
			return true;
		}
	}

	/*
	 * @author֣�� ����Ц��
	 */
	private void sendJoke() {
		if (neirongEdit.getText().toString().equals("")) {
			Toast.makeText(JokerContributeActivity.this, "������дЦ���������ύ",
					Toast.LENGTH_SHORT).show();
			return;
		}else{
			int neirongLength = neirongEdit.getText().toString().getBytes().length;
			if (neirongLength<6){
				//Ц�����ݹ���ʱ������ʾ
				Toast.makeText(JokerContributeActivity.this, "����Ҫ2������~", Toast.LENGTH_SHORT).show();
				return;
			}else if(neirongLength>400){
				//Ц�����ݹ���ʱ������ʾ
				Toast.makeText(JokerContributeActivity.this, "Ц�����ݹ���", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		int uid = Model.MYUSERINFO.getUserID();// �û�ID
		String xvalue = neirongEdit.getText().toString();// Ц������
		String ximg = "";// Ц��ͼƬ
		String xradio = "";// Ц����Ƶ
		if (!img.equalsIgnoreCase("")) {
			ximg = System.currentTimeMillis() + ".png";// Ц��ͼƬ
		}
		if (!radio.equalsIgnoreCase("")) {
			xradio = System.currentTimeMillis() + ".war";// Ц����Ƶ
		}
		String Json = "{\"uid\":\"" + uid + "\"," + "\"radio\":\"" + xradio
				+ "\"," + "\"ximg\":\"" + ximg + "\"," + "\"value\":\""
				+ xvalue + "\"}";
		ThreadPoolUtils.execute(new HttpPostThread(handler, Model.CONTRIBUTE,
				Json, img, radio));
		JokerContributeActivity.this.finish();
	}

	/*
	 * @author֣�� �������߳����ӷ��������ص�����msg
	 */
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null && result.equals("ok")) {
					Toast.makeText(JokerContributeActivity.this, "Ц�����ͳɹ�", 1000)
					.show();
					JokerContributeActivity.this.finish();
				}
			}
		};
	};

	/*
	 * @author֣�� �����û��������ͼƬ��ѡ��ͼƬ��ķ���ֵ
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			/**
			 * ��ѡ���ͼƬ��Ϊ�յĻ����ڻ�ȡ��ͼƬ��;��
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
					 * ���������һ���ж���Ҫ��Ϊ�˵����������ѡ�񣬱��磺ʹ�õ��������ļ��������Ļ�����ѡ����ļ��Ͳ�һ����ͼƬ�ˣ�
					 * �����Ļ��������ж��ļ��ĺ�׺�� �����ͼƬ��ʽ�Ļ�����ô�ſ���
					 */
					if (path.endsWith("jpg") || path.endsWith("png")) {
						Toast.makeText(JokerContributeActivity.this, "���ͼƬ�ɹ�",
								1000).show();
						//album.setBackgroundResource(R.drawable.icon_radio_finished);
						Picpath = path;
						img = Base64Util.encodeBase64File(Picpath);

						DisplayMetrics dm = getResources().getDisplayMetrics();
						int w_screen = dm.widthPixels;
						int h_screen = dm.heightPixels;//��ȡ��Ļ�ĳ�����
						
						// ѹ������ͬImageLoader,����ѹ������
						BitmapFactory.Options o = new BitmapFactory.Options();
                        o.inJustDecodeBounds = true;
                        FileInputStream stream1=new FileInputStream(Picpath);
                        BitmapFactory.decodeStream(stream1,null,o);
                        stream1.close();

						 int w = o.outWidth;
						 int h = o.outHeight;
						//
						// float hh =h_screen;//�������ø߶�Ϊ200f
						 float ww = w_screen;//�������ÿ��Ϊ480f
						// //���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ�����ݽ��м��㼴��
						 int be = 1;//be=1��ʾ������
						 if ( w > ww) {//�����ȴ�Ļ����ݿ�ȹ̶���С����,w>h&&w>>ww��Ҫ�����ڸߣ����ڵ�����ֻҪ��ȴ�����Ļ��ѹ��
						 be = (int) (o.outWidth / ww);
						 }
//						 else if (w < h && h > hh) {//����߶ȸߵĻ����ݿ�ȹ̶���С����
//						 be = (int) (o.outHeight / hh);
//						 }
						 if (be <= 0)
						 be = 1;
						 o.inSampleSize = be;//�������ű���
						 BitmapFactory.Options o2 = new BitmapFactory.Options();
	                        o2.inSampleSize=be;
	                    FileInputStream stream2=new FileInputStream(Picpath);
						
						Bitmap bitmap=BitmapFactory.decodeStream(stream2,null,o2);
						stream2.close();
						p_image.setVisibility(View.VISIBLE);
						p_image.setAdjustViewBounds(true);// ȡ��ͼƬ���·�
						p_image.setImageBitmap(bitmap);//��ʾͼƬ
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
	 * @author֣�� ��ʾ�����û�ѡ��ķ�ͼƬ����ʾ����ѡ��
	 */
	private void alert() {
		Dialog dialog = new AlertDialog.Builder(this).setTitle("��ʾ")
				.setMessage("��ѡ��Ĳ�����Ч��ͼƬ")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Picpath = null;
					}
				}).create();
		dialog.show();
	}
}
