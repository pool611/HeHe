package com.joker;

import java.util.ArrayList;
import java.util.List;

import com.gen.joker.R;
import com.joker.actions.JokerAuthorityActivity;
import com.joker.actions.JokerCheckActivity;
import com.joker.actions.JokerContributeActivity;
import com.joker.actions.JokerLoginActivity;
import com.joker.actions.JokerPersonalcenterActivity;
import com.joker.actions.JokerPushnewsActivity;
import com.joker.actions.JokerRegistActivity;
import com.joker.actions.JokerStatisticsActivity;
import com.joker.adapter.FragmentViewPagerAdapter;
import com.joker.model.Model;
import com.joker.net.IsNet;
import com.joker.net.ThreadPoolUtils;
import com.joker.thread.HttpPostThread;
import com.joker.utils.MyJson;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;

import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class JokerMainActivity extends FragmentActivity {
	/**
	 * @author:李颜翎 user_identity .
	 * @说明：用来标志用户身份 null游客；0用户；1管理员；2BOSS
	 * @后续：在后续的过程中需要将用户的身份和个人信息保存在本机，初始化时应该从本机读取用户的身份信息。
	 * @注意：游客、用户和管理员的选项菜单内容是一样的，具体的操作权限由用户的身份决定
	 */

	// 分类子页容器
	private ViewPager mViewPager;
	private List<Fragment> mDatas;
	private FragmentViewPagerAdapter adapter;

	// 分类栏和指示器
	private JokerIndexContainer mTitleView;
	// 标题栏图片按钮
	private ImageButton mJokesMainMore;// 标题栏“更多”图片按钮
	private ImageButton mJokesMainCheck;// 标题栏“审核”图片按钮
	private ImageButton mJokesMainContribute;// 标题栏“投稿”按钮
	private ImageButton mVoiceSetting; // 标题栏“播放设置”按钮

	// 播音、更多按钮弹出窗口视图
	private View mPopViewAction;
	private View mPopViewVoice;
	private PopupWindow mPopWindowAction;//更多选项
	private PopupWindow mPopWindowVoice;//播音设置
	// 播音设置
	private Button mVoiceRole;
	private Button mVoiceSpeed;
	private int mVoiceRolePosition = 0;// 获取数据库角色
	private String mVoiceRoleName = "xiaoyan";	// 人物
	private String mVoiceRoleSpeed = 50 + "";	// 语速
	// 更多选项栏 的按钮们
	private Button mJokesPopRegistButton;
	private Button mJokesPopLoginButton;
	private Button mJokesPopPersonalcenterButton;
	private Button mJokesPopAuthorityButton;
	private Button mJokesPopStatisticsButton;
	private Button mJokesPopPushnewsButton;
	private Button mJokesPopLogoutButton;
	private Button mJokesPopExitButton;
	// 联网检测
	private IsNet isnet;
	// 控制按两下退出
	private static boolean isExit = false;
	// 跳转intent
	final Intent mMainIntent = new Intent();
	public String url = null;
	private String value = null;
	//自动登录使用
	private SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置没有标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 初始化联网检测
		isnet = new IsNet(this);
		//自动登录使用
		sharedPreferences = this.getSharedPreferences("userInfo",Context.MODE_WORLD_READABLE);
		// 导入主页面布局文件
		setContentView(R.layout.joker_main);
		//分类栏
		mTitleView = new JokerIndexContainer(this);
		// 初始化指示器
		initTabline();
		// 初始化视图
		initView();
		// 初始化PopWindow
		initMainButton();
		// 各种监听器
		initMainListener();
		initClassifyListener();
		// 播放设置按钮
		initAudio();
		// 播放设置监听器
		initAudioListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mPopWindowAction.dismiss();
		updateMenu();
	}

	/**
	 * @author:李颜翎
	 * @说明：初始化指示器，设置宽度为屏幕的4分之1
	 */
	private void initTabline() {
		// 找到指示器文件
		mTitleView.mTabline = (ImageView) findViewById(R.id.id_iv_tabline);
		// 设置指示器占四分之一屏幕
		Display display = getWindow().getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		mTitleView.mScreen1_4 = outMetrics.widthPixels / 4;
		// 使用的是GroupView的LayoutParams
		LayoutParams lp = mTitleView.mTabline.getLayoutParams();
		lp.width = mTitleView.mScreen1_4;
		mTitleView.mTabline.setLayoutParams(lp);
	}

	/**
	 * @author：李颜翎
	 * @说明：初始化笑话列表子页，并设置适配器，以及相应的监听器及处理
	 */
	private void initView() {
		// 此处设置子屏幕的初始化
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		mTitleView.mJokes1NewestTextView = (TextView) findViewById(R.id.id_tv_jokes_newest);
		mTitleView.mJokes3WordTextView = (TextView) findViewById(R.id.id_tv_jokes_word);
		mTitleView.mJokes2BestTextView = (TextView) findViewById(R.id.id_tv_jokes_best);
		mTitleView.mJokes4AudioTextView = (TextView) findViewById(R.id.id_tv_jokes_audio);
		// 将四个子页视图放入到数组中
		mDatas = new ArrayList<Fragment>();
		Jokes1NewestMainListFragment tab01 = new Jokes1NewestMainListFragment();
		Jokes2BestMainListFragment tab02 = new Jokes2BestMainListFragment();
		Jokes3WordMainListFragment tab03 = new Jokes3WordMainListFragment();
		Jokes4AudioMainListFragment tab04 = new Jokes4AudioMainListFragment();

		mDatas.add(tab01);
		mDatas.add(tab02);
		mDatas.add(tab03);
		mDatas.add(tab04);

		// 设置适配器
		adapter = new FragmentViewPagerAdapter(mTitleView,
				this.getSupportFragmentManager(), mViewPager, mDatas);
		adapter.setOnExtraPageChangeListener(new FragmentViewPagerAdapter.OnExtraPageChangeListener() {
			@Override
			public void onExtraPageSelected(int i) {
				System.out.println("onExtraPageSelected  " + i);
			}
		});

	}

	/**
	 * @author 李颜翎
	 * @说明 设置主界面上的各个监听事件
	 */
	private void initMainListener() {
		/**
		 * @author：李颜翎 设置pop监听器
		 * @说明：此处开始设置poowindow内容的监听器
		 */
		mJokesMainMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPopupWindow(mJokesMainMore);
			}
		});
		// 审查
		mJokesMainCheck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!isnet.IsConnect()) {
					Toast.makeText(JokerMainActivity.this, "请先连接网络",
							Toast.LENGTH_SHORT).show();
					return;
				}
				mMainIntent.setClass(JokerMainActivity.this,
						JokerCheckActivity.class);
				JokerMainActivity.this.startActivity(mMainIntent);
			}
		});
		// 投稿
		mJokesMainContribute.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mMainIntent.setClass(JokerMainActivity.this,
						JokerContributeActivity.class);
				JokerMainActivity.this.startActivity(mMainIntent);
			}
		});
		// 八个按钮分别设置监听器
		// 注册设置监听器
		mJokesPopRegistButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mMainIntent.setClass(JokerMainActivity.this,
						JokerRegistActivity.class);
				JokerMainActivity.this.startActivity(mMainIntent);
			}
		});
		// 登陆设置监听器
		mJokesPopLoginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mMainIntent.setClass(JokerMainActivity.this,
						JokerLoginActivity.class);
				JokerMainActivity.this.startActivityForResult(mMainIntent, 0);
			}
		});
		// 用户个人中心设置监听器
		mJokesPopPersonalcenterButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mMainIntent.setClass(JokerMainActivity.this,
						JokerPersonalcenterActivity.class);
				JokerMainActivity.this.startActivity(mMainIntent);
			}
		});
		// 权限管理设置监听器
		mJokesPopAuthorityButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mMainIntent.setClass(JokerMainActivity.this,
						JokerAuthorityActivity.class);
				JokerMainActivity.this.startActivity(mMainIntent);
			}
		});
		// 后台统计设置监听器
		mJokesPopStatisticsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mMainIntent.setClass(JokerMainActivity.this,
						JokerStatisticsActivity.class);
				JokerMainActivity.this.startActivity(mMainIntent);
			}
		});
		// 消息推送
		mJokesPopPushnewsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mMainIntent.setClass(JokerMainActivity.this,
						JokerPushnewsActivity.class);
				JokerMainActivity.this.startActivity(mMainIntent);
			}
		});
		// 注销
		mJokesPopLogoutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Model.MYUSERINFO = null;
				Editor editor = sharedPreferences.edit(); 
	            editor.putBoolean("AUTO_ISCHECK", false);
	            editor.commit();
				mPopWindowAction.dismiss();
				updateMenu();
			}
		});
		// 退出
		mJokesPopExitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				System.exit(0);
			}
		});
	}

	/**
	 * @author 李颜翎
	 * @说明 初始化分类栏按钮的监听器
	 */
	private void initClassifyListener() {
		mTitleView.mJokes1NewestTextView
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						mViewPager.setCurrentItem(0);
					}
				});

		mTitleView.mJokes2BestTextView
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						mViewPager.setCurrentItem(1);
					}
				});

		mTitleView.mJokes3WordTextView
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						mViewPager.setCurrentItem(2);
					}
				});

		mTitleView.mJokes4AudioTextView
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						mViewPager.setCurrentItem(3);
					}
				});
	}

	/**
	 * @author：郑轩 PopWindow
	 * @author：李颜翎 监听设置
	 * @说明：初始化自定义标题栏的按钮，且初始化
	 */
	private void initMainButton() {
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPopViewAction = layoutInflater.inflate(R.layout.joker_main_morepop_manage, null);
		// 创建一个PopuWidow对象
		mPopWindowAction = new PopupWindow(mPopViewAction,
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		// 获得popwindow的八个按钮
		mJokesPopRegistButton = (Button) mPopViewAction
				.findViewById(R.id.id_main_pop_regist);
		mJokesPopLoginButton = (Button) mPopViewAction
				.findViewById(R.id.id_main_pop_login);
		mJokesPopPersonalcenterButton = (Button) mPopViewAction
				.findViewById(R.id.id_main_pop_personalcenter);
		mJokesPopAuthorityButton = (Button) mPopViewAction
				.findViewById(R.id.id_main_pop_authority);
		mJokesPopStatisticsButton = (Button) mPopViewAction
				.findViewById(R.id.id_main_pop_statistics);
		mJokesPopPushnewsButton = (Button) mPopViewAction
				.findViewById(R.id.id_main_pop_pushnews);
		mJokesPopLogoutButton = (Button) mPopViewAction
				.findViewById(R.id.id_main_pop_logout);
		mJokesPopExitButton = (Button) mPopViewAction
				.findViewById(R.id.id_main_pop_exit);

		// 获得自定义标题栏的三个图片按钮
		mJokesMainMore = (ImageButton) findViewById(R.id.id_iv_main_more);
		mJokesMainCheck = (ImageButton) findViewById(R.id.id_iv_main_check);
		mJokesMainContribute = (ImageButton) findViewById(R.id.id_iv_main_contribute);

	}

	/**
	 * @author：郑轩 李颜翎
	 * @说明：初始化popwindow及显示。后将初始化移除，只执行显示popwindow的操作。
	 */
	@SuppressWarnings("deprecation")
	private void showPopupWindow(View parent) {
		// 获得popwindow焦点。使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
		mPopWindowAction.setFocusable(true);
		// 设置允许在外点击消失
		mPopWindowAction.setOutsideTouchable(true);
		// 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		mPopWindowAction.setBackgroundDrawable(new BitmapDrawable());
		// 设置菜单显示的位置
		mPopWindowAction.showAsDropDown(parent, Gravity.CENTER, 0);
	}

	private void updateMenu() {
		if (Model.MYUSERINFO == null) {
			mJokesMainCheck.setVisibility(View.GONE);
			mJokesMainContribute.setVisibility(View.VISIBLE);
			mJokesPopRegistButton.setVisibility(View.VISIBLE);
			mJokesPopLoginButton.setVisibility(View.VISIBLE);
			mJokesPopPersonalcenterButton.setVisibility(View.GONE);
			mJokesPopAuthorityButton.setVisibility(View.GONE);
			mJokesPopStatisticsButton.setVisibility(View.GONE);
			mJokesPopPushnewsButton.setVisibility(View.GONE);
			mJokesPopLogoutButton.setVisibility(View.GONE);
		} else if (Model.MYUSERINFO.getStatus() == 0) {
			mJokesMainCheck.setVisibility(View.GONE);
			mJokesMainContribute.setVisibility(View.VISIBLE);
			mJokesPopRegistButton.setVisibility(View.GONE);
			mJokesPopLoginButton.setVisibility(View.GONE);
			mJokesPopPersonalcenterButton.setVisibility(View.VISIBLE);
			mJokesPopAuthorityButton.setVisibility(View.GONE);
			mJokesPopStatisticsButton.setVisibility(View.GONE);
			mJokesPopPushnewsButton.setVisibility(View.GONE);
			mJokesPopLogoutButton.setVisibility(View.VISIBLE);
		} else if (Model.MYUSERINFO.getStatus() == 1) {
			mJokesMainCheck.setVisibility(View.VISIBLE);
			mJokesMainContribute.setVisibility(View.VISIBLE);
			mJokesPopRegistButton.setVisibility(View.GONE);
			mJokesPopLoginButton.setVisibility(View.GONE);
			mJokesPopPersonalcenterButton.setVisibility(View.VISIBLE);
			mJokesPopAuthorityButton.setVisibility(View.GONE);
			mJokesPopStatisticsButton.setVisibility(View.GONE);
			mJokesPopPushnewsButton.setVisibility(View.GONE);
			mJokesPopLogoutButton.setVisibility(View.VISIBLE);
		} else if (Model.MYUSERINFO.getStatus() == 2) {
			mJokesMainCheck.setVisibility(View.VISIBLE);
			mJokesMainContribute.setVisibility(View.VISIBLE);
			mJokesPopRegistButton.setVisibility(View.GONE);
			mJokesPopLoginButton.setVisibility(View.GONE);
			mJokesPopPersonalcenterButton.setVisibility(View.VISIBLE);
			mJokesPopAuthorityButton.setVisibility(View.VISIBLE);
			mJokesPopStatisticsButton.setVisibility(View.VISIBLE);
			mJokesPopPushnewsButton.setVisibility(View.VISIBLE);
			mJokesPopLogoutButton.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * @auther:王亚伟
	 * @说明：初始化自定义标题栏的按钮，且初始化
	 */
	private void initAudio() {
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPopViewVoice = layoutInflater.inflate(R.layout.joker_main_morepop_audio, null);
		// 创建一个PopuWidow1对象
		mPopWindowVoice = new PopupWindow(mPopViewVoice,
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		// 获取popWindow1的三个按钮
		mVoiceRole = (Button) mPopViewVoice.findViewById(R.id.id_main_pop_role);
		mVoiceSpeed = (Button) mPopViewVoice.findViewById(R.id.id_main_pop_speed);
		// mSize=(Button)mPopView1.findViewById(R.id.id_main_pop_audiosize);

		// 获得自定义标题栏的播放设置按钮
		mVoiceSetting = (ImageButton) findViewById(R.id.id_iv_main_radiomore);
	}
	
	
	/**
	 * @author：王亚伟 设置pop监听器
	 * @说明：此处开始设置poowindow1内容的监听器
	 */
	private void initAudioListener() {
		mVoiceSetting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Model.MYUSERINFO == null) {
					Toast.makeText(getApplicationContext(), "登陆后才能使用语音模块",
							Toast.LENGTH_SHORT).show();
				} else {
					showPopupWindow1(mVoiceSetting);
				}
			}
		});

		mVoiceRole.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				System.out.println("点击了播放人按钮");
				selectRole();
			}
		});

		mVoiceSpeed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				System.out.println("点击了播放语速按钮");
				selectVolume();
			}
		});
	}

	private void showPopupWindow1(View parent) {
		// 获得popwindow1焦点。使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
		mPopWindowVoice.setFocusable(true);
		// 设置允许在外点击消失
		mPopWindowVoice.setOutsideTouchable(true);
		// 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		mPopWindowVoice.setBackgroundDrawable(new BitmapDrawable());
		// 设置菜单显示的位置
		mPopWindowVoice.showAsDropDown(parent, Gravity.CENTER, 0);
	}

	private void selectRole() {
		if (Model.MYUSERINFO.getAudiorole().equalsIgnoreCase("xiaoyan")) {
			mVoiceRolePosition = 0;
		}
		if (Model.MYUSERINFO.getAudiorole().equalsIgnoreCase("xiaoyu")) {
			mVoiceRolePosition = 1;
		}
		if (Model.MYUSERINFO.getAudiorole().equalsIgnoreCase("vixm")) {
			mVoiceRolePosition = 2;
		}
		if (Model.MYUSERINFO.getAudiorole().equalsIgnoreCase("vixl")) {
			mVoiceRolePosition = 3;
		}
		if (Model.MYUSERINFO.getAudiorole().equalsIgnoreCase("vixr")) {
			mVoiceRolePosition = 4;
		}
		if (Model.MYUSERINFO.getAudiorole().equalsIgnoreCase("vixyun")) {
			mVoiceRolePosition = 5;
		}
		if (Model.MYUSERINFO.getAudiorole().equalsIgnoreCase("vixk")) {
			mVoiceRolePosition = 6;
		}
		if (Model.MYUSERINFO.getAudiorole().equalsIgnoreCase("vixqa")) {
			mVoiceRolePosition = 7;
		}
		if (Model.MYUSERINFO.getAudiorole().equalsIgnoreCase("vixying")) {
			mVoiceRolePosition = 8;
		}
		if (Model.MYUSERINFO.getAudiorole().equalsIgnoreCase("vixx")) {
			mVoiceRolePosition = 9;
		}
		if (Model.MYUSERINFO.getAudiorole().equalsIgnoreCase("vinn")) {
			mVoiceRolePosition = 10;
		}
		if (Model.MYUSERINFO.getAudiorole().equalsIgnoreCase("vils")) {
			mVoiceRolePosition = 11;
		}
		if (Model.MYUSERINFO.getAudiorole().equalsIgnoreCase("Catherine")) {
			mVoiceRolePosition = 12;
		}
		if (Model.MYUSERINFO.getAudiorole().equalsIgnoreCase("henry")) {
			mVoiceRolePosition = 13;
		}
		if (Model.MYUSERINFO.getAudiorole().equalsIgnoreCase("vimary")) {
			mVoiceRolePosition = 14;
		}
		if (Model.MYUSERINFO.getAudiorole().equalsIgnoreCase("vixy")) {
			mVoiceRolePosition = 15;
		}
		if (Model.MYUSERINFO.getAudiorole().equalsIgnoreCase("vixq")) {
			mVoiceRolePosition = 16;
		}
		if (Model.MYUSERINFO.getAudiorole().equalsIgnoreCase("vixf")) {
			mVoiceRolePosition = 17;
		}
		System.out.println(mVoiceRolePosition);
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog
				.setTitle("请选择发音人")
				.setIcon(android.R.drawable.ic_dialog_info)
				// 设置发音角色，弹出对话框，进行选择
				.setSingleChoiceItems(
						new String[] { "晓燕(青年女声)", "晓宇(青年男声)", "小梅(粤语)",
								"小莉(台湾普通话)", "小蓉(四川话)", "小芸(东北话)", "小坤", "小强 ",
								"小莹", "小新", "楠楠", "老孙", "凯瑟琳", "亨利(只读英文)",
								"玛丽(只读英文)", "小研", "小琪", "小峰" }, mVoiceRolePosition,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									mVoiceRoleName = "xiaoyan";
									break;
								case 1:
									mVoiceRoleName = "xiaoyu";
									break;
								case 2:
									mVoiceRoleName = "vixm";
									break;
								case 3:
									mVoiceRoleName = "vixl";
									break;
								case 4:
									mVoiceRoleName = "vixr";
									break;
								case 5:
									mVoiceRoleName = "vixyun";
									break;
								case 6:
									mVoiceRoleName = "vixk";
									break;
								case 7:
									mVoiceRoleName = "vixqa";
									break;
								case 8:
									mVoiceRoleName = "vixying";
									break;
								case 9:
									mVoiceRoleName = "vixx";
									break;
								case 10:
									mVoiceRoleName = "vinn";
									break;
								case 11:
									mVoiceRoleName = "vils";
									break;
								case 12:
									mVoiceRoleName = "Catherine";
									break;
								case 13:
									mVoiceRoleName = "henry";
									break;
								case 14:
									mVoiceRoleName = "vimary";
									break;
								case 15:
									mVoiceRoleName = "vixy";
									break;
								case 16:
									mVoiceRoleName = "vixq";
									break;
								case 17:
									mVoiceRoleName = "vixf";
									break;
								}
								dialog.dismiss(); // 取消对话框
								// 设置发音人
								Model.MYUSERINFO.setAudiorole(mVoiceRoleName);
								// 上传到数据库
								url = "editroleandspeed.php";
								value = "{\"uid\":\""
										+ Model.MYUSERINFO.getUserID()
										+ "\",\"audiorole\":\"" + mVoiceRoleName
										+ "\",\"audiospeed\":\""
										+ Integer.parseInt(mVoiceRoleSpeed) + "\"}";
								ThreadPoolUtils.execute(new HttpPostThread(
										handler, url, value));
								System.out.println("开始上传发音人");
								Toast.makeText(getBaseContext(), "设置发音人成功！",
										Toast.LENGTH_SHORT).show();
							}
						}).setNegativeButton("取消", null).show();
	}

	private void selectVolume() {
		// 设置输入框对话框
		final EditText inputServer = new EditText(this);
		inputServer.setFocusable(true);
		// 清除输入框默认值
		inputServer.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				inputServer.setText("");
			}
		});
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("设置语速");
		builder.setIcon(android.R.drawable.ic_dialog_dialer);
		inputServer.setText("当前设置语速" + Model.MYUSERINFO.getAudiospeed());
		builder.setView(inputServer).setNegativeButton(
				getString(android.R.string.cancel), null);
		builder.setPositiveButton(getString(android.R.string.ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						mVoiceRoleSpeed = inputServer.getText().toString();
						if (mVoiceRoleSpeed.matches("[0-9]+")) {
							if (Integer.parseInt(mVoiceRoleSpeed) > 100) {
								Toast.makeText(getApplicationContext(),
										"语速太快，已超出人类接受范围，请输入小于100的数值",
										Toast.LENGTH_SHORT).show();
							} else {
								System.out.println("语速为" + mVoiceRoleSpeed);
								// 开始上传
								Model.MYUSERINFO.setAudiospeed(Integer
										.parseInt(mVoiceRoleSpeed));
								// 上传到数据库
								url = "editroleandspeed.php";
								value = "{\"uid\":\""
										+ Model.MYUSERINFO.getUserID()
										+ "\",\"audiorole\":\"" + mVoiceRoleName
										+ "\",\"audiospeed\":\""
										+ Integer.parseInt(mVoiceRoleSpeed) + "\"}";
								ThreadPoolUtils.execute(new HttpPostThread(
										handler, url, value));
								Toast.makeText(getBaseContext(), "设置语速成功！",
										Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(getApplicationContext(),
									"语速是0-100之间的整数，请重新输入", Toast.LENGTH_SHORT)
									.show();
						}
					}
				});
		builder.show();
	}

	/**
	 * @author 郑轩 控制双击退出
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次后退键退出程序",
					Toast.LENGTH_SHORT).show();
			// 利用handler延迟发送更改状态信息
			mHandler.sendEmptyMessageDelayed(0, 1000);
		} else {
			System.exit(0);
		}
	}

	private static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(getBaseContext(), "请求失败，服务器故障", 1).show();
			} else if (msg.what == 100) {
				Toast.makeText(getBaseContext(), "服务器无响应", 1).show();
			} else if (msg.what == 200) {
				String result = msg.obj.toString();
				System.out.println(result);
			}
		}
	};
}
