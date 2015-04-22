package com.joker.adapter;

import java.util.List;

import com.gen.joker.R;
import com.joker.info.UserInfo;
import com.joker.model.Model;
import com.joker.net.ThreadPoolUtils;
import com.joker.thread.HttpPostThread;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyApplyAdminAdapter extends BaseAdapter {

	private List<UserInfo> list;
	private Context ctx;
	private String Json;
	private String text;	//管理员信息

	public MyApplyAdminAdapter(Context ctx, List<UserInfo> list) {
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
			hold.applyadmininfo = (TextView) convertView.findViewById(R.id.userInfo);
			hold.agreeButton=(Button) convertView.findViewById(R.id.manger);
			hold.cancleButton=(Button) convertView.findViewById(R.id.manger1);
			convertView.setTag(hold);
		} else {
			hold = (Holder) convertView.getTag();
		}
		hold.applyadmininfo.setText(list.get(position).getUsername());
		//不同意申请管理员
		hold.cancleButton.setGravity(Gravity.CENTER);
		hold.cancleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//不同意申请管理员上传“取消”状态到服务器
				Json = "{\"do\":\"" + "rejectadmin" + "\"," + "\"adminname\":\""
						+ list.get(position).getUsername() + "\"}";
				ThreadPoolUtils.execute(new HttpPostThread(handler,
						Model.ADMIN, Json));
				text=list.get(position).getUsername();
				//移除当前列，并更新列表
				list.remove(position).getUsername();
				notifyDataSetChanged();
			}
		});
		//同意申请管理员设置
		hold.agreeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//同意申请管理，上传“同意”状态到数据库
				Json = "{\"do\":\"" + "setadmin" + "\"," + "\"adminname\":\""
						+ list.get(position).getUsername() + "\"}";
				ThreadPoolUtils.execute(new HttpPostThread(handler,
						Model.ADMIN, Json));
				text=list.get(position).getUsername();
				//移除当前列，并跟新列表
				list.remove(position).getUsername();
				notifyDataSetChanged();
			}
		});
		return convertView;
	}


	static class Holder {
		TextView applyadmininfo;
		Button	agreeButton;
		Button	cancleButton;
	}
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null && result.equals("setadmin_ok")) {
					Toast.makeText(ctx,
							"设置  "+ text  +"  管理员成功",Toast.LENGTH_SHORT).show();
				} else if (result != null && result.equals("rejectadmin_ok")){
					Toast.makeText(ctx, "不同意设置  "+ text  +"  为管理员",Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(ctx, "修改失败",Toast.LENGTH_SHORT).show();
				}
			}
		};
	};
}
