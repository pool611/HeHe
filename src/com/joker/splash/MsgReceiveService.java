package com.joker.splash;

import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.joker.JokerMainActivity;
import com.joker.actions.JokerLoginActivity;
import com.joker.model.Model;
import com.joker.net.ThreadPoolUtils;
import com.joker.thread.HttpPostThread;
import com.joker.utils.MyJson;
import com.sina.push.PushManager;
import com.sina.push.model.ActionResult;
import com.sina.push.receiver.PushMsgRecvService;
import com.sina.push.receiver.event.IEvent;
import com.sina.push.response.PushDataPacket;

public class MsgReceiveService extends PushMsgRecvService {
	//log信息，仅供测试使用
	private String log = new String();
	
	@Override
	public void onActionResult(IEvent<?> event) {
		// TODO Auto-generated method stub
		if (event.getType()==PushManager.ACTION_OPEN_CHANNEL) {
			
			//调用打开，切换接口的执行结果
			ActionResult result = (ActionResult)event.getPayload();
			log = result+"\n";
			//Toast.makeText(getApplicationContext(), log, Toast.LENGTH_LONG).show();
			//System.out.println(log);
			if (result.getResultCode() == 1) {
				//打开通道成功，可以正常接收Push和调用接口功能
				
			}
		}
	}

	@Override
	public void onPush(IEvent<?> event) {
		try {
			switch (event.getType()) {
			case PushManager.MSG_TYPE_MPS_PUSH_DATA:
				PushDataPacket packet = (PushDataPacket) event.getPayload();
				//to do something
				
				log = "received MPS push:[appid="+packet.getAppID()
				+",msgID="+packet.getMsgID()+",srcJson="+packet.getSrcJson()+"\n";
				//Toast.makeText(getApplicationContext(), "onPush data: " + packet.getSrcJson(), Toast.LENGTH_LONG).show();
				break;
			case PushManager.MSG_TYPE_GET_AID:
				
				String aid = (String) event.getPayload();
				ThreadPoolUtils.execute(new HttpPostThread(handler, Model.PHONERECARD, aid));
				//log = "received aid:["+aid+"]\n";
				//System.out.println(log);
				//Toast.makeText(getApplicationContext(), log, Toast.LENGTH_LONG).show();
			
				break;
			
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			//String result = msg.obj.toString();
			//发送手机标识到服务器后，返回不用处理
		}
    };
}
