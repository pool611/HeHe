package com.joker.adapter;

import java.util.List;

import com.gen.joker.R;
import com.joker.info.CommentsInfo;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MyCommentListAdapter extends BaseAdapter {

	private List<com.joker.info.CommentsInfo> list;
	private Context ctx;

	public MyCommentListAdapter(Context ctx, List<CommentsInfo> list) {
		this.ctx = ctx;
		this.list = list;
	}

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
		Holder hold;
		if (arg1 == null) {
			hold = new Holder();
			arg1 = View.inflate(ctx, R.layout.lay_jokes_list_comment, null);
			hold.UserName = (TextView) arg1.findViewById(R.id.Detail_Item_UserName);
			hold.Comment_ID = (TextView) arg1.findViewById(R.id.Detail_Item_Num);
			hold.Comment_text = (TextView) arg1.findViewById(R.id.Detail_Item_Value);
			arg1.setTag(hold);
		} else {
			hold = (Holder) arg1.getTag();
		}
		hold.UserName.setText("  "+list.get(arg0).getUsername());
		hold.Comment_ID.setText((arg0 + 1)+"  ");
		hold.Comment_text.setText(list.get(arg0).getComment());
		
		return arg1;
	}

	static class Holder {
		TextView UserName;
		TextView Comment_ID;
		TextView Comment_text;
	}
}
