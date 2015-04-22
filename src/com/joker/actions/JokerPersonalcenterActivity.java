package com.joker.actions;

import java.util.ArrayList;
import java.util.List;

import com.gen.joker.R;
import com.joker.adapter.MyListAdapter;
import com.joker.info.JokeInfo;
import com.joker.info.UserInfo;
import com.joker.model.Model;
import com.joker.myview.MyListView;
import com.joker.myview.MyListView.OnRefreshListener;
import com.joker.net.IsNet;
import com.joker.net.ThreadPoolUtils;
import com.joker.thread.HttpGetThread;
import com.joker.thread.HttpPostThread;
import com.joker.utils.MyJson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
//
public class JokerPersonalcenterActivity extends Activity {

	//set variables
	private View mPopViewAction;
	private boolean flag=true;
	private boolean listBottemFlag=true;
	private boolean loadflag=false;
	private int start=0;
	private int number=5;
	private UserInfo priInfo=null;
	public String url = null;
	private String value = null;
	private Context ctx;
	private ImageView userBacking;
	private MyListView myListView;
	private TextView userName;
	private TextView userAge;
	private TextView userHobbies;
	private TextView userAddress;
	private TextView userGrade;
	private TextView userJokes;
	private TextView userBrief;
	private TextView userEmail;
	private boolean userFlag=true;
	private EditText userEmailRevise;
	private EditText userAddressRivese;
	private EditText userAgeRevise;
	private EditText userHobbiesRevise;
	private Button userInfoButton1;
	private Button userInfoButton2;
	private IsNet userInfoisnet;
	private LinearLayout userInfoBrief;
	private LinearLayout userInfoJoker;
	private IsNet isnet;
	private MyListAdapter mAdapter  ;
	private List<JokeInfo> list = new ArrayList<JokeInfo>();
	private Button ListBottem;
	private TextView NoNetText;
	private LinearLayout load_progressBar;
	private LinearLayout userInfoSexRevise;
	private RadioGroup userInfoSexRadioGroup;
	private RadioButton userInfoSexButtonMale;
	private RadioButton userInfoSexButtonFemale;
	private TextView userInfoGoverner;
	private int sexIntNumber=2;
	private TextView userInfoAgeSex;
	private ImageView userInfoMenu;
	private PopupWindow mPopWindowAction;
	private Button userInfoReviseButtonTest;
	private Button userInfoGovernerApplyButtonTest;
	private LinearLayout userBriefLayout;
	private LinearLayout userJokesLayout;
	private ImageView userimage;

	//main method
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lay_joker_personalcenter);//load layout.set a intent variable.get bundle from intent
		priInfo=Model.MYUSERINFO;//get info from bundle
		initView();
		setUserInfo();
	}

	//initialize the layout 
	private void initView() {
		//
		userName=(TextView) findViewById(R.id.userName);
		userEmail=(TextView) findViewById(R.id.useremail);
		userAddress=(TextView) findViewById(R.id.useraddress);
		userAge=(TextView) findViewById(R.id.userage);
		userHobbies=(TextView) findViewById(R.id.userhobbies);
		userGrade=(TextView) findViewById(R.id.usergrade);
		userBrief=(TextView) findViewById(R.id.userBrief);
		userBriefLayout=(LinearLayout) findViewById(R.id.userBrieflayout1);
		userJokes=(TextView) findViewById(R.id.userJokes);
		userJokesLayout=(LinearLayout) findViewById(R.id.userJokeslayout1);
		//		userRevise=(ImageView) findViewById(R.id.userRevise);
		userBacking=(ImageView) findViewById(R.id.userBacking);
		userEmailRevise=(EditText) findViewById(R.id.useremailrevise);
		userHobbiesRevise=(EditText) findViewById(R.id.userhobbiesrevise);
		userAgeRevise=(EditText) findViewById(R.id.useragerevise);
		userAddressRivese=(EditText) findViewById(R.id.useraddressrevise);
		userInfoButton1=(Button) findViewById(R.id.button1);
		userInfoBrief=(LinearLayout) findViewById(R.id.userBrieflayout);
		userInfoSexRevise=(LinearLayout) findViewById(R.id.sexRevise);
		userInfoJoker=(LinearLayout) findViewById(R.id.UserJokeLayout);
		myListView=(MyListView) findViewById(R.id.Detail_List);
		load_progressBar=(LinearLayout) findViewById(R.id.Detail__progressBar);
		NoNetText = (TextView) findViewById(R.id.NoNetText1);
		NoNetText.setVisibility(View.GONE);
		userInfoSexRadioGroup=(RadioGroup) findViewById(R.id.sexButton);
		userInfoSexButtonMale=(RadioButton) findViewById(R.id.sexButtonMale);
		userInfoSexButtonFemale=(RadioButton) findViewById(R.id.sexButtonFemale);
		userInfoGoverner=(TextView) findViewById(R.id.userGoverner);
		userInfoAgeSex=(TextView) findViewById(R.id.useragesex);
		userInfoMenu=(ImageView) findViewById(R.id.userReviseTest);
		userInfoButton2=(Button) findViewById(R.id.button2);
		userimage=(ImageView) findViewById(R.id.userhead);
		userimage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(JokerPersonalcenterActivity.this, JokerUserImage.class);
				startActivity(intent);
			}
		});

		//����Ц��
		listview();
		loadmenu();
		//���ü����¼�
		UserOnClick userclick=new UserOnClick();
		userBrief.setOnClickListener(userclick);
		userBacking.setOnClickListener(userclick);
		userJokes.setOnClickListener(userclick);
	}


	@Override
	protected void onResume() {
		super.onResume();
		if(Model.MYUSERINFO.getHead().equals("headimage1")){
			userimage.setImageResource(R.drawable.user_head_image1);
		}else if(Model.MYUSERINFO.getHead().equals("headimage2")){
			userimage.setImageResource(R.drawable.user_head_image2);
		}else if(Model.MYUSERINFO.getHead().equals("headimage3")){
			userimage.setImageResource(R.drawable.user_head_image3);
		}else if(Model.MYUSERINFO.getHead().equals("headimage4")){
			userimage.setImageResource(R.drawable.user_head_image4);
		}else if(Model.MYUSERINFO.getHead().equals("headimage5")){
			userimage.setImageResource(R.drawable.user_head_image5);
		}else if(Model.MYUSERINFO.getHead().equals("headimage6")){
			userimage.setImageResource(R.drawable.user_head_image6);
		}else if(Model.MYUSERINFO.getHead().equals("headimage7")){
			userimage.setImageResource(R.drawable.user_head_image7);
		}else if(Model.MYUSERINFO.getHead().equals("headimage8")){
			userimage.setImageResource(R.drawable.user_head_image8);
		}else if(Model.MYUSERINFO.getHead().equals("headimage9")){
			userimage.setImageResource(R.drawable.user_head_image9);
		}else {
			userimage.setImageResource(R.drawable.user_head_image5);
		}
		
	}

	private void loadmenu() {
		// TODO Auto-generated method stub
		initMainButton();
		userInfoMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPopupWindow(userInfoMenu);
			}
		});
		userInfoReviseButtonTest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				reviseUserInfo();
			}
		});
		userInfoGovernerApplyButtonTest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(priInfo.getApplyforadmin()!=1){
					uploadapplynumber();
				}else{
					userInfoGovernerApplyButtonTest.setText("���������");
					mPopWindowAction.dismiss();
				}
			}
		});
	}

	protected void uploadapplynumber() {
		// TODO Auto-generated method stub
		mPopWindowAction.dismiss();
		url = Model.APPLYFORADMIN;
		value = "{\"uid\":\"" + priInfo.getUserID()+ "\"}";
		ThreadPoolUtils.execute(new HttpPostThread(handlerforadmin, url, value));
	}

	protected void initMainButton() {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPopViewAction = layoutInflater.inflate(R.layout.lay_joker_personalcenter_manage, null);
		// ����һ��PopuWidow����
		mPopWindowAction = new PopupWindow(mPopViewAction,
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		userInfoReviseButtonTest = (Button) mPopViewAction
				.findViewById(R.id.userInfoReviseButton);
		userInfoGovernerApplyButtonTest = (Button) mPopViewAction
				.findViewById(R.id.userInfoGovernerButton);
		if(priInfo.getStatus()==1||priInfo.getStatus()==2){
			userInfoGovernerApplyButtonTest.setVisibility(View.GONE);
		}
		if(priInfo.getApplyforadmin()==1){
			userInfoGovernerApplyButtonTest.setText("���������");
		}

	}
	private void showPopupWindow(View parent) {
		// ���popwindow���㡣ʹ��ۼ� ��Ҫ������˵���ؼ����¼��ͱ���Ҫ���ô˷���
		mPopWindowAction.setFocusable(true);
		// ����������������ʧ
		mPopWindowAction.setOutsideTouchable(true);
		// ���ñ����������Ϊ�˵��������Back��Ҳ��ʹ����ʧ�����Ҳ�����Ӱ����ı���
		mPopWindowAction.setBackgroundDrawable(new BitmapDrawable());
		// ���ò˵���ʾ��λ��
		mPopWindowAction.showAsDropDown(parent, Gravity.CENTER, 0);
	}

	//UserOnClicklistener
	class UserOnClick implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int clickId=v.getId();
			switch(clickId){
			case R.id.userBacking:
				finish();
				break;
			case R.id.userBrief:
				userFlag = true;
				initCont(userFlag);
				break;
			case R.id.userJokes:
				userFlag = false;
				initCont(userFlag);
				break;
			}
		}
	}

	//layout of brief and jokes 
	private void initCont(boolean userFlag) {
		System.out.println(userFlag);
		if(userFlag){
			userBriefLayout.setBackgroundResource(R.color._green);
			userJokesLayout.setBackgroundResource(R.color._white);
			userInfoBrief.setVisibility(View.VISIBLE);
			userInfoJoker.setVisibility(View.GONE);
		}
		else{
			userJokesLayout.setBackgroundResource(R.color._green);
			userBriefLayout.setBackgroundResource(R.color._white);
			userInfoBrief.setVisibility(View.GONE);
			userInfoJoker.setVisibility(View.VISIBLE);
			myListView.setVisibility(View.VISIBLE);

		}
	}

	//revise user's information
	private void reviseUserInfo() {
		// TODO Auto-generated method stub
		mPopWindowAction.dismiss();
		userAge.setVisibility(View.GONE);
		userAgeRevise.setVisibility(View.VISIBLE);
		userAddress.setVisibility(View.GONE);
		userAddressRivese.setVisibility(View.VISIBLE);
		userHobbies.setVisibility(View.GONE);
		userHobbiesRevise.setVisibility(View.VISIBLE);
		userEmail.setVisibility(View.GONE);
		userEmailRevise.setVisibility(View.VISIBLE);
		userInfoButton1.setVisibility(View.VISIBLE);
		userInfoButton2.setVisibility(View.VISIBLE);
		userInfoSexRevise.setVisibility(View.VISIBLE);
		//
		if(!priInfo.getHobbies().equals("null")){
			userHobbiesRevise.setText(priInfo.getHobbies());
		}
		if(!priInfo.getPlace().equals("null")){
			userAddressRivese.setText(priInfo.getPlace());
		}
		if(!priInfo.getEmail().equals("null")){
			userEmailRevise.setText(priInfo.getEmail());
		}
		if(priInfo.getAge()!=0){
			userAgeRevise.setText(priInfo.getAge()+"");
		}
		if(priInfo.getSex()==1){
			userInfoSexButtonMale.setChecked(true);
		}
		if(priInfo.getSex()==2){
			userInfoSexButtonFemale.setChecked(true);
		}
		userInfoSexRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListenerImp());
		//check states of net
		userInfoisnet = new IsNet(this);
		//set button-listener
		userInfoButton1.setOnClickListener(new revisebuttonlistener());
		userInfoButton2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resetUserInfo();
			}
		});
	}

	//�Ա��ж�
	private class OnCheckedChangeListenerImp implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			if(JokerPersonalcenterActivity.this.userInfoSexButtonMale.getId()==checkedId){
				sexIntNumber=1;
			}
			else if(JokerPersonalcenterActivity.this.userInfoSexButtonFemale.getId()==checkedId){
				sexIntNumber=2;
			}
		}
	}
	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(ctx, "�Ҳ�����ַ", 1).show();
				listBottemFlag = true;
			} else if (msg.what == 100) {
				Toast.makeText(ctx, "����ʧ��", 1).show();
				listBottemFlag = true;
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null) {
					List<JokeInfo> newList =new MyJson().getJokeList(result);
					if (newList != null) {
						if (newList.size() == number) {
							ListBottem.setVisibility(View.VISIBLE);
						} else if (newList.size() == 0) {
							ListBottem.setVisibility(View.GONE);
							Toast.makeText(ctx, "�Ѿ�û����...", 1).show();
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
							System.out.println("what's wrong");
					}
				}
				load_progressBar.setVisibility(View.GONE);
				myListView.onRefreshComplete();
				mAdapter.notifyDataSetChanged();
				loadflag = true;
			}
		};
	};


	/**
	 *���û���Ϣ����У��
	 */
	/**
	 *���û��������У��
	 */
	private boolean checkUserAge(String string) {							
		int stringLength = string.getBytes().length;
		if(stringLength == 0){
			Toast.makeText(getApplicationContext(), "�û����䲻��Ϊ��", 1).show();
			return false;
		}
		String regex = "[0-9]{1,2}"; 
		if (string.matches(regex)){
			return true;
		}else{
			Toast.makeText(getApplicationContext(), "����ȷ��������(0-99)", 1).show();
			return false;
		}
	}		

	/*
	 * ��������û����ý���У��
	 */
	private boolean checkUserHobbies(String string) {
		int stringLength = string.getBytes().length;
		if (string.equals("") || string == null){
			Toast.makeText(getApplicationContext(), "���ò���Ϊ��", 1).show();
			return false;
		}
		if (stringLength>100){
			Toast.makeText(getApplicationContext(), "���ù���", 1).show();
			return false;
		}	
		return true;
	}

	/*
	 * ��������û���ַ����У��
	 */
	private boolean checkUserAddress(String string) {
		int stringLength = string.getBytes().length;
		if (string.equals("") || string == null){
			Toast.makeText(getApplicationContext(), "�û���ַ����Ϊ��", 1).show();
			return false;
		}
		if (stringLength>100){
			Toast.makeText(getApplicationContext(), "�û���ַ����", 1).show();
			return false;
		}
		return true;
	}		

	/*
	 * ��������û���������ж�
	 */
	private boolean checkUserEmail(String string) {
		if (string.equals("")){
			Toast.makeText(getApplicationContext(), "���䲻��Ϊ��", 1).show();
			return false;
		}	
		String regex="[a-zA-Z0-9_\\-\\.]+@(sina|qq|163|139|126|sohu|189|aliyun|outlook|Gmail)+(\\.(com|cn|org|edu|hk))";
		if (string.matches(regex)){		
			return true;
		}	
		Toast.makeText(getApplicationContext(), "�����ʽ����ȷ", 1).show();
		return false;
	}

	//revise button-listener
	public class revisebuttonlistener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if(!userInfoisnet.IsConnect()){
				Toast.makeText(JokerPersonalcenterActivity.this, "������������", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!checkUserAge(userAgeRevise.getText().toString())) {
				return;
			}
			if (!checkUserAddress(userAddressRivese.getText().toString())) {
				return;
			}
			if (!checkUserHobbies(userHobbiesRevise.getText().toString())) {
				return;
			}
			if (!checkUserEmail(userEmailRevise.getText().toString())) {
				return;
			}
			Model.MYUSERINFO.setAge(Integer.parseInt(userAgeRevise.getText().toString()));
			Model.MYUSERINFO.setPlace(userAddressRivese.getText().toString());
			Model.MYUSERINFO.setHobbies(userHobbiesRevise.getText().toString());
			Model.MYUSERINFO.setEmail(userEmailRevise.getText().toString());
			Model.MYUSERINFO.setSex(sexIntNumber);
			userinfoUpload();
		}
	}

	//upload user-information 
	private void userinfoUpload() {
		url = Model.EDITUSERINFO;
		value = "{\"uid\":\"" + priInfo.getUserID()
				+ "\",\"place\":\"" + userAddressRivese.getText().toString()
				+ "\",\"age\":\"" + userAgeRevise.getText().toString()
				+ "\",\"hobbies\":\"" + userHobbiesRevise.getText().toString()
				+ "\",\"email\":\"" + userEmailRevise.getText().toString()
				+ "\",\"sex\":\"" +sexIntNumber
				+ "\"}";
		ThreadPoolUtils.execute(new HttpPostThread(handler, url, value));
	}

	//receive info of upload
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(JokerPersonalcenterActivity.this, "����ʧ�ܣ�����������", 1)
				.show();
				resetUserInfo();
			} 
			else if (msg.what == 100) {
				Toast.makeText(JokerPersonalcenterActivity.this, "����������Ӧ", 1).show();
				resetUserInfo();
			} 
			else if (msg.what == 200) {
				String result = msg.obj.toString();
				if(result.equals("no")){
					Toast.makeText(getApplicationContext(), "�޸Ĳ��ɹ�",
							Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(getApplicationContext(), "�޸ĳɹ�",
							Toast.LENGTH_SHORT).show();
					priInfo=Model.MYUSERINFO;
					resetUserInfo();
				}
			}
		}
	};
	Handler handlerforadmin = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(JokerPersonalcenterActivity.this, "����ʧ�ܣ�����������", 1)
				.show();
			} 
			else if (msg.what == 100) {
				Toast.makeText(JokerPersonalcenterActivity.this, "����������Ӧ", 1).show();
			} 
			else if (msg.what == 200) {
				String result = msg.obj.toString();
				if(result.equals("no")){
					Toast.makeText(getApplicationContext(), "�������⣬����δ�ɹ�",
							Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(getApplicationContext(), "�ѷ�������",
							Toast.LENGTH_SHORT).show();
					priInfo.setApplyforadmin(1);
					userInfoGovernerApplyButtonTest.setText("���������");
				}
			}
		}
	};

	//set texts' information
	private void setUserInfo() {
		// TODO Auto-generated method stub
		userName.setText(priInfo.getUsername());
		if(priInfo.getAge()!=0){
			userAge.setText(""+priInfo.getAge());
			if(priInfo.getSex()==1){
				userInfoAgeSex.setBackgroundResource(R.drawable.joker_icon_male);
			}
			else{
				userInfoAgeSex.setBackgroundResource(R.drawable.joker_icon_female);
			}
		}
		if(!priInfo.getHobbies().equals("null")){
			userHobbies.setText(priInfo.getHobbies());
		}
		if(!priInfo.getPlace().equals("null")){
			userAddress.setText(priInfo.getPlace());
		}
		if(!priInfo.getEmail().equals("null")){
			userEmail.setText(priInfo.getEmail());
		}

		userGrade.setText(priInfo.getScore()+"");

		if(priInfo.getStatus()==2||priInfo.getStatus()==1){
			userInfoGoverner.setText("����Ա ");
		}else {
			userInfoGoverner.setText("��ͨ�û� ");
		}
	}

	//reset text's information
	private void resetUserInfo() {
		// TODO Auto-generated method stub
		if(priInfo.getAge()!=0){
			userAge.setText(""+priInfo.getAge());
			if(priInfo.getSex()==1){
				userInfoAgeSex.setBackgroundResource(R.drawable.joker_icon_male);
			}
			else{
				userInfoAgeSex.setBackgroundResource(R.drawable.joker_icon_female);
			}
		}
		if(!priInfo.getHobbies().equals("null")){
			userHobbies.setText(priInfo.getHobbies());
		}
		if(!priInfo.getPlace().equals("null")){
			userAddress.setText(priInfo.getPlace());
		}
		if(!priInfo.getEmail().equals("null")){
			userEmail.setText(priInfo.getEmail());
		}

		userGrade.setText(""+priInfo.getScore());

		if(priInfo.getStatus()==2||priInfo.getStatus()==1){
			userInfoGoverner.setText("����Ա ");
		}else {
			userInfoGoverner.setText("��ͨ�û� ");
		}
		//set visibility of texts
		userAge.setVisibility(View.VISIBLE);
		userAgeRevise.setVisibility(View.GONE);
		userAddress.setVisibility(View.VISIBLE);
		userAddressRivese.setVisibility(View.GONE);
		userHobbies.setVisibility(View.VISIBLE);
		userHobbiesRevise.setVisibility(View.GONE);
		userEmail.setVisibility(View.VISIBLE);
		userEmailRevise.setVisibility(View.GONE);
		userInfoButton1.setVisibility(View.GONE);
		userInfoButton2.setVisibility(View.GONE);
		userInfoSexRevise.setVisibility(View.GONE);
	}
	//load jokers\

	private void listview() {
		// TODO Auto-generated method stub
		ctx=JokerPersonalcenterActivity.this;
		url = "showmyjokeslist.php";
		ThreadPoolUtils.execute(new HttpGetThread(hand, url + "?start="
				+ start + "&num="+number +"&uid="+Model.MYUSERINFO.getUserID()));
		start += number;
		mAdapter = new MyListAdapter(ctx, list);
		isnet = new IsNet(ctx);
		ListBottem = new Button(ctx);
		ListBottem.setText("������ظ���");
		ListBottem.setBackgroundResource(R.drawable.mybutton_green);
		ListBottem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!isnet.IsConnect()){
					Toast.makeText(ctx, "���ȴ�����", 1).show();
					return;
				}
				if (flag && listBottemFlag) {
					url = "showmyjokeslist.php";
					ThreadPoolUtils.execute(new HttpGetThread(hand, url+"?start="+start+"&num="+number+"&uid="+Model.MYUSERINFO.getUserID()));
					start +=number;
					listBottemFlag = false;
				} else if (!listBottemFlag)
					Toast.makeText(ctx, "���������Ժ�...", 1).show();
			}
		});
		myListView.addFooterView(ListBottem, null, false);
		ListBottem.setVisibility(View.GONE);
		myListView.setAdapter(mAdapter);
		//δ�������ּ���
		if(!isnet.IsConnect()){
			load_progressBar.setVisibility(View.GONE);
			NoNetText.setVisibility(View.VISIBLE);
			return;
		}
		NoNetText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//System.out.println("no net text");
				if(isnet.IsConnect()){
					//System.out.println("into net");
					Toast.makeText(JokerPersonalcenterActivity.this, "nihao", 1).show();
					url = Model.NEWJOKE;
					start = 0;
					ThreadPoolUtils.execute(new HttpGetThread(hand, url+"?start="+start+"&num="+number+"&uid="+Model.MYUSERINFO.getUserID()));
					start +=number;
					NoNetText.setVisibility(View.GONE);
					load_progressBar.setVisibility(View.VISIBLE);
				}

			}
		});
		myListView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				if(!isnet.IsConnect()){
					Toast.makeText(ctx, "���ȴ�����", Toast.LENGTH_SHORT).show();
					load_progressBar.setVisibility(View.GONE);
					myListView.onRefreshComplete();
					mAdapter.notifyDataSetChanged();
					loadflag = true;
					return;
				}
				if (loadflag == true) {
					url = "showmyjokeslist.php";
					ListBottem.setVisibility(View.GONE);
					start = 0;
					ThreadPoolUtils.execute(new HttpGetThread(hand, url+"?start="+start+"&num="+number+"&uid="+Model.MYUSERINFO.getUserID()));
					start +=number;
					loadflag = false;
				} else {
					Toast.makeText(ctx, "����ˢ�£������ظ�ˢ��", 1).show();
				}
			}
		});
	}
	//
}









