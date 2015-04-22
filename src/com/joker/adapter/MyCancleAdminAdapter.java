package com.joker.adapter;

import java.util.List;

import com.gen.joker.R;
import com.joker.info.UserInfo;
import com.joker.model.Model;
import com.joker.net.ThreadPoolUtils;
import com.joker.thread.HttpPostThread;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyCancleAdminAdapter extends BaseAdapter {

	private List<UserInfo> list;
	private Context ctx;
	private String Json;
	private String text;	//�û���Ϣ

	public MyCancleAdminAdapter(Context ctx, List<UserInfo> list) {
		this.list = list;
		this.ctx = ctx;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder hold;
		if (convertView == null) {
			hold = new Holder();
			convertView = View.inflate(ctx, R.layout.lay_joker_authority_adminlist, null);
			hold.cancleadmininfo = (TextView) convertView.findViewById(R.id.userInfo);
			hold.cancleeButton=(Button) convertView.findViewById(R.id.manger1);
			hold.sureButton=(Button) convertView.findViewById(R.id.manger);
			convertView.setTag(hold);
		} else {
			hold = (Holder) convertView.getTag();
		}
		hold.cancleadmininfo.setText(list.get(position).getUsername());
		hold.sureButton.setVisibility(View.GONE);	//ȡ��ͬ�ⰴť�ɼ�
		hold.cancleeButton.setText("����");		//���ò�ͬ�ⰴť��Ϊ��������
		hold.cancleeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//�ϴ���������״̬��������
				Json = "{\"do\":\"" + "unsetadmin" + "\"," + "\"adminname\":\""
						+ list.get(position).getUsername() + "\"}";
				ThreadPoolUtils.execute(new HttpPostThread(handler,
						Model.ADMIN, Json));
				text=list.get(position).getUsername();
				//�Ƴ���ǰ�У��������б�
				list.remove(position).getUsername();
				notifyDataSetChanged();
			}
		});

		return convertView;
	}


	static class Holder {
		TextView cancleadmininfo;
		Button	cancleeButton;
		Button sureButton;
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null && result.equals("ok")) {
					Toast.makeText(ctx,
						"ȡ��  "+text+"  ����Ա�ɹ�",Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(ctx, "�޸�ʧ�ܣ����Ժ�����",Toast.LENGTH_SHORT).show();
				}
			}
		};
	};
}
