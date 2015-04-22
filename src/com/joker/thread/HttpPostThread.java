package com.joker.thread;

import android.os.Handler;
import android.os.Message;

import com.joker.net.MyPost;

/*
 * author֣��
 * ����Post������߳�
 */

public class HttpPostThread implements Runnable {

	private Handler hand;
	private String url;
	private String value;
	private String img = "";
	private String radio = "";
	private MyPost myGet = new MyPost();
	/*
	 * �ϴ�ͼ��
	 */
	public HttpPostThread(Handler hand, String endParamerse, String value,
			String img) {
		this.hand = hand;
		// ƴ�ӷ��ʷ����������ĵ�ַ
		url = endParamerse;
		this.value = value;
		this.img = img;
	}
	/*
	 * �ϴ��ı�
	 */
	public HttpPostThread(Handler hand, String endParamerse, String value) {
		this.hand = hand;
		// ƴ�ӷ��ʷ����������ĵ�ַ
		url = endParamerse;
		this.value = value;
	}
	/*
	 * �ϴ�ͼƬ�ı���Ƶ
	 */
	public HttpPostThread(Handler hand, String endParamerse, String value, String img, String radio) {
		this.hand = hand;
		// ƴ�ӷ��ʷ����������ĵ�ַ
		url = endParamerse;
		this.value = value;
		this.img = img;
		this.radio = radio;
	}
	@Override
	public void run() {
		// ��ȡ���ǻص���UI��message
		Message msg = hand.obtainMessage();
		String result = null;
		if (img.equalsIgnoreCase("")&&radio.equalsIgnoreCase("")) {
			result = myGet.doPost(url, value);
		}else if(radio.equalsIgnoreCase("")&&!img.isEmpty()){
			result = myGet.doPost(url, img, value, "");
		}else if(img.equalsIgnoreCase("")&&!radio.isEmpty()){
			result = myGet.doPost(url, "", value, radio);
		}else{
			result = myGet.doPost(url, img, value, radio);
		}
		msg.what = 200;
		msg.obj = result;
		// ����UI������Ϣ��������
		hand.sendMessage(msg);
	}

}
