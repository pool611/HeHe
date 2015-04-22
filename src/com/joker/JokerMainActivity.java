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
	 * @author:������ user_identity .
	 * @˵����������־�û���� null�οͣ�0�û���1����Ա��2BOSS
	 * @�������ں����Ĺ�������Ҫ���û�����ݺ͸�����Ϣ�����ڱ�������ʼ��ʱӦ�ôӱ�����ȡ�û��������Ϣ��
	 * @ע�⣺�ο͡��û��͹���Ա��ѡ��˵�������һ���ģ�����Ĳ���Ȩ�����û�����ݾ���
	 */

	// ������ҳ����
	private ViewPager mViewPager;
	private List<Fragment> mDatas;
	private FragmentViewPagerAdapter adapter;

	// ��������ָʾ��
	private JokerIndexContainer mTitleView;
	// ������ͼƬ��ť
	private ImageButton mJokesMainMore;// �����������ࡱͼƬ��ť
	private ImageButton mJokesMainCheck;// ����������ˡ�ͼƬ��ť
	private ImageButton mJokesMainContribute;// ��������Ͷ�塱��ť
	private ImageButton mVoiceSetting; // ���������������á���ť

	// ���������ఴť����������ͼ
	private View mPopViewAction;
	private View mPopViewVoice;
	private PopupWindow mPopWindowAction;//����ѡ��
	private PopupWindow mPopWindowVoice;//��������
	// ��������
	private Button mVoiceRole;
	private Button mVoiceSpeed;
	private int mVoiceRolePosition = 0;// ��ȡ���ݿ��ɫ
	private String mVoiceRoleName = "xiaoyan";	// ����
	private String mVoiceRoleSpeed = 50 + "";	// ����
	// ����ѡ���� �İ�ť��
	private Button mJokesPopRegistButton;
	private Button mJokesPopLoginButton;
	private Button mJokesPopPersonalcenterButton;
	private Button mJokesPopAuthorityButton;
	private Button mJokesPopStatisticsButton;
	private Button mJokesPopPushnewsButton;
	private Button mJokesPopLogoutButton;
	private Button mJokesPopExitButton;
	// �������
	private IsNet isnet;
	// ���ư������˳�
	private static boolean isExit = false;
	// ��תintent
	final Intent mMainIntent = new Intent();
	public String url = null;
	private String value = null;
	//�Զ���¼ʹ��
	private SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ����û�б�����
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ��ʼ���������
		isnet = new IsNet(this);
		//�Զ���¼ʹ��
		sharedPreferences = this.getSharedPreferences("userInfo",Context.MODE_WORLD_READABLE);
		// ������ҳ�沼���ļ�
		setContentView(R.layout.joker_main);
		//������
		mTitleView = new JokerIndexContainer(this);
		// ��ʼ��ָʾ��
		initTabline();
		// ��ʼ����ͼ
		initView();
		// ��ʼ��PopWindow
		initMainButton();
		// ���ּ�����
		initMainListener();
		initClassifyListener();
		// �������ð�ť
		initAudio();
		// �������ü�����
		initAudioListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mPopWindowAction.dismiss();
		updateMenu();
	}

	/**
	 * @author:������
	 * @˵������ʼ��ָʾ�������ÿ��Ϊ��Ļ��4��֮1
	 */
	private void initTabline() {
		// �ҵ�ָʾ���ļ�
		mTitleView.mTabline = (ImageView) findViewById(R.id.id_iv_tabline);
		// ����ָʾ��ռ�ķ�֮һ��Ļ
		Display display = getWindow().getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		mTitleView.mScreen1_4 = outMetrics.widthPixels / 4;
		// ʹ�õ���GroupView��LayoutParams
		LayoutParams lp = mTitleView.mTabline.getLayoutParams();
		lp.width = mTitleView.mScreen1_4;
		mTitleView.mTabline.setLayoutParams(lp);
	}

	/**
	 * @author��������
	 * @˵������ʼ��Ц���б���ҳ�����������������Լ���Ӧ�ļ�����������
	 */
	private void initView() {
		// �˴���������Ļ�ĳ�ʼ��
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		mTitleView.mJokes1NewestTextView = (TextView) findViewById(R.id.id_tv_jokes_newest);
		mTitleView.mJokes3WordTextView = (TextView) findViewById(R.id.id_tv_jokes_word);
		mTitleView.mJokes2BestTextView = (TextView) findViewById(R.id.id_tv_jokes_best);
		mTitleView.mJokes4AudioTextView = (TextView) findViewById(R.id.id_tv_jokes_audio);
		// ���ĸ���ҳ��ͼ���뵽������
		mDatas = new ArrayList<Fragment>();
		Jokes1NewestMainListFragment tab01 = new Jokes1NewestMainListFragment();
		Jokes2BestMainListFragment tab02 = new Jokes2BestMainListFragment();
		Jokes3WordMainListFragment tab03 = new Jokes3WordMainListFragment();
		Jokes4AudioMainListFragment tab04 = new Jokes4AudioMainListFragment();

		mDatas.add(tab01);
		mDatas.add(tab02);
		mDatas.add(tab03);
		mDatas.add(tab04);

		// ����������
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
	 * @author ������
	 * @˵�� �����������ϵĸ��������¼�
	 */
	private void initMainListener() {
		/**
		 * @author�������� ����pop������
		 * @˵�����˴���ʼ����poowindow���ݵļ�����
		 */
		mJokesMainMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPopupWindow(mJokesMainMore);
			}
		});
		// ���
		mJokesMainCheck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!isnet.IsConnect()) {
					Toast.makeText(JokerMainActivity.this, "������������",
							Toast.LENGTH_SHORT).show();
					return;
				}
				mMainIntent.setClass(JokerMainActivity.this,
						JokerCheckActivity.class);
				JokerMainActivity.this.startActivity(mMainIntent);
			}
		});
		// Ͷ��
		mJokesMainContribute.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mMainIntent.setClass(JokerMainActivity.this,
						JokerContributeActivity.class);
				JokerMainActivity.this.startActivity(mMainIntent);
			}
		});
		// �˸���ť�ֱ����ü�����
		// ע�����ü�����
		mJokesPopRegistButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mMainIntent.setClass(JokerMainActivity.this,
						JokerRegistActivity.class);
				JokerMainActivity.this.startActivity(mMainIntent);
			}
		});
		// ��½���ü�����
		mJokesPopLoginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mMainIntent.setClass(JokerMainActivity.this,
						JokerLoginActivity.class);
				JokerMainActivity.this.startActivityForResult(mMainIntent, 0);
			}
		});
		// �û������������ü�����
		mJokesPopPersonalcenterButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mMainIntent.setClass(JokerMainActivity.this,
						JokerPersonalcenterActivity.class);
				JokerMainActivity.this.startActivity(mMainIntent);
			}
		});
		// Ȩ�޹������ü�����
		mJokesPopAuthorityButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mMainIntent.setClass(JokerMainActivity.this,
						JokerAuthorityActivity.class);
				JokerMainActivity.this.startActivity(mMainIntent);
			}
		});
		// ��̨ͳ�����ü�����
		mJokesPopStatisticsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mMainIntent.setClass(JokerMainActivity.this,
						JokerStatisticsActivity.class);
				JokerMainActivity.this.startActivity(mMainIntent);
			}
		});
		// ��Ϣ����
		mJokesPopPushnewsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mMainIntent.setClass(JokerMainActivity.this,
						JokerPushnewsActivity.class);
				JokerMainActivity.this.startActivity(mMainIntent);
			}
		});
		// ע��
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
		// �˳�
		mJokesPopExitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				System.exit(0);
			}
		});
	}

	/**
	 * @author ������
	 * @˵�� ��ʼ����������ť�ļ�����
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
	 * @author��֣�� PopWindow
	 * @author�������� ��������
	 * @˵������ʼ���Զ���������İ�ť���ҳ�ʼ��
	 */
	private void initMainButton() {
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPopViewAction = layoutInflater.inflate(R.layout.joker_main_morepop_manage, null);
		// ����һ��PopuWidow����
		mPopWindowAction = new PopupWindow(mPopViewAction,
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		// ���popwindow�İ˸���ť
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

		// ����Զ��������������ͼƬ��ť
		mJokesMainMore = (ImageButton) findViewById(R.id.id_iv_main_more);
		mJokesMainCheck = (ImageButton) findViewById(R.id.id_iv_main_check);
		mJokesMainContribute = (ImageButton) findViewById(R.id.id_iv_main_contribute);

	}

	/**
	 * @author��֣�� ������
	 * @˵������ʼ��popwindow����ʾ���󽫳�ʼ���Ƴ���ִֻ����ʾpopwindow�Ĳ�����
	 */
	@SuppressWarnings("deprecation")
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
	 * @auther:����ΰ
	 * @˵������ʼ���Զ���������İ�ť���ҳ�ʼ��
	 */
	private void initAudio() {
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPopViewVoice = layoutInflater.inflate(R.layout.joker_main_morepop_audio, null);
		// ����һ��PopuWidow1����
		mPopWindowVoice = new PopupWindow(mPopViewVoice,
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		// ��ȡpopWindow1��������ť
		mVoiceRole = (Button) mPopViewVoice.findViewById(R.id.id_main_pop_role);
		mVoiceSpeed = (Button) mPopViewVoice.findViewById(R.id.id_main_pop_speed);
		// mSize=(Button)mPopView1.findViewById(R.id.id_main_pop_audiosize);

		// ����Զ���������Ĳ������ð�ť
		mVoiceSetting = (ImageButton) findViewById(R.id.id_iv_main_radiomore);
	}
	
	
	/**
	 * @author������ΰ ����pop������
	 * @˵�����˴���ʼ����poowindow1���ݵļ�����
	 */
	private void initAudioListener() {
		mVoiceSetting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Model.MYUSERINFO == null) {
					Toast.makeText(getApplicationContext(), "��½�����ʹ������ģ��",
							Toast.LENGTH_SHORT).show();
				} else {
					showPopupWindow1(mVoiceSetting);
				}
			}
		});

		mVoiceRole.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				System.out.println("����˲����˰�ť");
				selectRole();
			}
		});

		mVoiceSpeed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				System.out.println("����˲������ٰ�ť");
				selectVolume();
			}
		});
	}

	private void showPopupWindow1(View parent) {
		// ���popwindow1���㡣ʹ��ۼ� ��Ҫ������˵���ؼ����¼��ͱ���Ҫ���ô˷���
		mPopWindowVoice.setFocusable(true);
		// ����������������ʧ
		mPopWindowVoice.setOutsideTouchable(true);
		// ���ñ����������Ϊ�˵��������Back��Ҳ��ʹ����ʧ�����Ҳ�����Ӱ����ı���
		mPopWindowVoice.setBackgroundDrawable(new BitmapDrawable());
		// ���ò˵���ʾ��λ��
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
				.setTitle("��ѡ������")
				.setIcon(android.R.drawable.ic_dialog_info)
				// ���÷�����ɫ�������Ի��򣬽���ѡ��
				.setSingleChoiceItems(
						new String[] { "����(����Ů��)", "����(��������)", "С÷(����)",
								"С��(̨����ͨ��)", "С��(�Ĵ���)", "Сܿ(������)", "С��", "Сǿ ",
								"СӨ", "С��", "��", "����", "��ɪ��", "����(ֻ��Ӣ��)",
								"����(ֻ��Ӣ��)", "С��", "С��", "С��" }, mVoiceRolePosition,
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
								dialog.dismiss(); // ȡ���Ի���
								// ���÷�����
								Model.MYUSERINFO.setAudiorole(mVoiceRoleName);
								// �ϴ������ݿ�
								url = "editroleandspeed.php";
								value = "{\"uid\":\""
										+ Model.MYUSERINFO.getUserID()
										+ "\",\"audiorole\":\"" + mVoiceRoleName
										+ "\",\"audiospeed\":\""
										+ Integer.parseInt(mVoiceRoleSpeed) + "\"}";
								ThreadPoolUtils.execute(new HttpPostThread(
										handler, url, value));
								System.out.println("��ʼ�ϴ�������");
								Toast.makeText(getBaseContext(), "���÷����˳ɹ���",
										Toast.LENGTH_SHORT).show();
							}
						}).setNegativeButton("ȡ��", null).show();
	}

	private void selectVolume() {
		// ���������Ի���
		final EditText inputServer = new EditText(this);
		inputServer.setFocusable(true);
		// ��������Ĭ��ֵ
		inputServer.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				inputServer.setText("");
			}
		});
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("��������");
		builder.setIcon(android.R.drawable.ic_dialog_dialer);
		inputServer.setText("��ǰ��������" + Model.MYUSERINFO.getAudiospeed());
		builder.setView(inputServer).setNegativeButton(
				getString(android.R.string.cancel), null);
		builder.setPositiveButton(getString(android.R.string.ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						mVoiceRoleSpeed = inputServer.getText().toString();
						if (mVoiceRoleSpeed.matches("[0-9]+")) {
							if (Integer.parseInt(mVoiceRoleSpeed) > 100) {
								Toast.makeText(getApplicationContext(),
										"����̫�죬�ѳ���������ܷ�Χ��������С��100����ֵ",
										Toast.LENGTH_SHORT).show();
							} else {
								System.out.println("����Ϊ" + mVoiceRoleSpeed);
								// ��ʼ�ϴ�
								Model.MYUSERINFO.setAudiospeed(Integer
										.parseInt(mVoiceRoleSpeed));
								// �ϴ������ݿ�
								url = "editroleandspeed.php";
								value = "{\"uid\":\""
										+ Model.MYUSERINFO.getUserID()
										+ "\",\"audiorole\":\"" + mVoiceRoleName
										+ "\",\"audiospeed\":\""
										+ Integer.parseInt(mVoiceRoleSpeed) + "\"}";
								ThreadPoolUtils.execute(new HttpPostThread(
										handler, url, value));
								Toast.makeText(getBaseContext(), "�������ٳɹ���",
										Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(getApplicationContext(),
									"������0-100֮�������������������", Toast.LENGTH_SHORT)
									.show();
						}
					}
				});
		builder.show();
	}

	/**
	 * @author ֣�� ����˫���˳�
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
			Toast.makeText(getApplicationContext(), "�ٰ�һ�κ��˼��˳�����",
					Toast.LENGTH_SHORT).show();
			// ����handler�ӳٷ��͸���״̬��Ϣ
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
				Toast.makeText(getBaseContext(), "����ʧ�ܣ�����������", 1).show();
			} else if (msg.what == 100) {
				Toast.makeText(getBaseContext(), "����������Ӧ", 1).show();
			} else if (msg.what == 200) {
				String result = msg.obj.toString();
				System.out.println(result);
			}
		}
	};
}
