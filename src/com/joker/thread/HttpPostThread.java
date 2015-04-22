package com.joker.thread;

import android.os.Handler;
import android.os.Message;

import com.joker.net.MyPost;

/*
 * author郑轩
 * 网络Post请求的线程
 */

public class HttpPostThread implements Runnable {

	private Handler hand;
	private String url;
	private String value;
	private String img = "";
	private String radio = "";
	private MyPost myGet = new MyPost();
	/*
	 * 上传图文
	 */
	public HttpPostThread(Handler hand, String endParamerse, String value,
			String img) {
		this.hand = hand;
		// 拼接访问服务器完整的地址
		url = endParamerse;
		this.value = value;
		this.img = img;
	}
	/*
	 * 上传文本
	 */
	public HttpPostThread(Handler hand, String endParamerse, String value) {
		this.hand = hand;
		// 拼接访问服务器完整的地址
		url = endParamerse;
		this.value = value;
	}
	/*
	 * 上传图片文本音频
	 */
	public HttpPostThread(Handler hand, String endParamerse, String value, String img, String radio) {
		this.hand = hand;
		// 拼接访问服务器完整的地址
		url = endParamerse;
		this.value = value;
		this.img = img;
		this.radio = radio;
	}
	@Override
	public void run() {
		// 获取我们回调主UI的message
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
		// 给主UI发送消息传递数据
		hand.sendMessage(msg);
	}

}
