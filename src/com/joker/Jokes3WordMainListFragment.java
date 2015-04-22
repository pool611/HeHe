package com.joker;

import java.util.ArrayList;
import java.util.List;

import com.gen.joker.R;
import com.joker.actions.JokerSendCommentActivity;
import com.joker.adapter.MyListAdapter;
import com.joker.info.JokeInfo;
import com.joker.model.Model;
import com.joker.myview.MyListView;
import com.joker.myview.MyListView.OnRefreshListener;
import com.joker.net.IsNet;
import com.joker.net.ThreadPoolUtils;
import com.joker.thread.HttpGetThread;
import com.joker.utils.MyJson;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Jokes3WordMainListFragment extends Fragment {

	/*
	 * @author：李颜翎
	 * 
	 * @说明：此处载入“纯文”排序页面，注意父类为Fragment （非 Javadoc）
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	private View view;
	private Context ctx;
	private MyListView myListView;
	private LinearLayout mLinearLayout, load_progressBar;
	private TextView HomeNoValue, NoNetText;
	private MyListAdapter mAdapter = null;
	private List<JokeInfo> list = new ArrayList<JokeInfo>();
	private Button ListBottem = null;
	private String url = null;
	private boolean flag = true;
	private boolean loadflag = false;
	private boolean listBottemFlag = true;
	private int start = 0;
	private int number = 20;//每次取的条数
	private IsNet isnet;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.lay_jokes_page3_word, container,false);
		ctx = view.getContext();
		myListView = new MyListView(ctx);
		isnet = new IsNet(ctx);
		initView();
		//注册Item监听事件
		myListView.setOnItemClickListener(listener);
		return view;
	}

	private void initView() {
		load_progressBar = (LinearLayout) view
				.findViewById(R.id.load_progressBar);
		mLinearLayout = (LinearLayout) view.findViewById(R.id.HomeGroup);
		myListView.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		myListView.setDivider(null);
		mLinearLayout.addView(myListView);
		HomeNoValue = (TextView) view.findViewById(R.id.HomeNoValue);
		NoNetText = (TextView) view.findViewById(R.id.NoNetText);
		HomeNoValue.setVisibility(View.GONE);
		NoNetText.setVisibility(View.GONE);
		mAdapter = new MyListAdapter(ctx, list);
		ListBottem = new Button(ctx);
		ListBottem.setText("点击加载更多");
		ListBottem.setBackgroundResource(R.drawable.mybutton_green);
		ListBottem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!isnet.IsConnect()){
					Toast.makeText(ctx, "请先打开网络", 1).show();
					return;
				}
				if (flag && listBottemFlag) {
					url = Model.TEXTJOKE;
					ThreadPoolUtils.execute(new HttpGetThread(hand, url+"?start="+start+"&num="+number));
					start +=number;
					listBottemFlag = false;
				} else if (!listBottemFlag)
					Toast.makeText(ctx, "加载中请稍候...", 1).show();
			}
		});
		myListView.addFooterView(ListBottem, null, false);
		ListBottem.setVisibility(View.GONE);
		myListView.setAdapter(mAdapter);
		//myListView.setOnItemClickListener(new MainListOnItemClickListener());
		//未联网文字监听
		NoNetText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//System.out.println("no net text");
				if(isnet.IsConnect()){
					//System.out.println("into net");
					url = Model.TEXTJOKE;
					start = 0;
					ThreadPoolUtils.execute(new HttpGetThread(hand, url+"?start="+start+"&num="+number));
					start +=number;
					NoNetText.setVisibility(View.GONE);
					load_progressBar.setVisibility(View.VISIBLE);
				}
			}
		});
		HomeNoValue.setVisibility(View.GONE);
		myListView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				if(!isnet.IsConnect()){
					Toast.makeText(ctx, "请先打开网络", Toast.LENGTH_SHORT).show();
					mLinearLayout.setVisibility(View.VISIBLE);
					load_progressBar.setVisibility(View.GONE);
					myListView.onRefreshComplete();
					mAdapter.notifyDataSetChanged();
					loadflag = true;
					return;
				}
				if (loadflag == true) {
					url = Model.TEXTJOKE;
					ListBottem.setVisibility(View.GONE);
					start = 0;
					ThreadPoolUtils.execute(new HttpGetThread(hand, url+"?start="+start+"&num="+number));
					start +=number;
					loadflag = false;
				} else {
					Toast.makeText(ctx, "正在刷新，请勿重复刷新", 1).show();
				}
			}
		});
		if(!isnet.IsConnect()){
			load_progressBar.setVisibility(View.GONE);
			NoNetText.setVisibility(View.VISIBLE);
			return;
		}
		url = Model.TEXTJOKE;
		ThreadPoolUtils.execute(new HttpGetThread(hand, url + "?start="
				+ start + "&num="+number));
		start += number;
	}

	private OnItemClickListener	listener =new OnItemClickListener()
	{
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			JokeInfo jokeinfo = (JokeInfo)arg0.getAdapter().getItem(arg2);//Adapter中取出数据
			if(!isnet.IsConnect()){
				Toast.makeText(ctx, "请先连接网络", Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intent = new Intent(ctx, JokerSendCommentActivity.class);
			Bundle bund = new Bundle();
			bund.putSerializable("JokeInfo", list.get(arg2-1));
			intent.putExtra("value", bund);
			ctx.startActivity(intent);
		}
	};
	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(ctx, "找不到地址", 1).show();
				listBottemFlag = true;
			} else if (msg.what == 100) {
				Toast.makeText(ctx, "传输失败", 1).show();
				listBottemFlag = true;
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null) {
					List<JokeInfo> newList =new MyJson().getJokeList(result);
					if (newList != null) {
						if (newList.size() == number) {
							ListBottem.setVisibility(View.VISIBLE);
						} else if (newList.size() == 0) {
							if (list.size() == 0)
								HomeNoValue.setVisibility(View.VISIBLE);
							ListBottem.setVisibility(View.GONE);
							Toast.makeText(ctx, "已经没有了...", 1).show();
						} else {
							ListBottem.setVisibility(View.GONE);
						}
						if (!loadflag) {
							list.removeAll(list);
						}
						for (JokeInfo info : newList) {
							list.add(info);
						}
						listBottemFlag = true;
					} else {
						if (list.size() == 0)
							HomeNoValue.setVisibility(View.VISIBLE);
					}
				}
				mLinearLayout.setVisibility(View.VISIBLE);
				load_progressBar.setVisibility(View.GONE);
				myListView.onRefreshComplete();
				mAdapter.notifyDataSetChanged();
				loadflag = true;
			}
		};
	};
}
