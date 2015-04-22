package com.joker.model;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;

import com.joker.info.UserInfo;

public class Model {
	/*
	 * author郑轩 保存用到的传输网址
	 */
	// 主页
	public static String HTTPURL = "http://cups.sinaapp.com/";
	// 数据仓库主页
	public static String HTTPSTOR = "http://cups-domain.stor.sinaapp.com/";
	// 注册用户页面
	public static String REGISTET = "regist.php";
	// 登陆页面
	public static String LOGIN = "login.php";
	// 保存登陆后用户信息
	public static UserInfo MYUSERINFO = null;
	// 添加笑话页面
	public static String CONTRIBUTE = "contribute.php";
	// 增减管理员
	public static String ADMIN = "authority.php";
	// 获取最新笑话
	public static String NEWJOKE = "shownewjokelist.php";
	// 获取最赞笑话
	public static String LIKEJOKE = "showlikejokelist.php";
	// 获取纯文笑话
	public static String TEXTJOKE = "showtextjokelist.php";
	// 获取音频笑话
	public static String RADIOJOKE = "showradiojokelist.php";
	// 显示评论列表
	public static String SHOWCOMMENTS = "showcomments.php";
	// 发布评论
	public static String SENDCOMMENT = "sendcomment.php";
	// 记录手机token，推送使用
	public static String PHONERECARD = "phonerecard.php";
	// 推送消息
	public static String PUSH = "push.php";
	// 显示待审核笑话
	public static String CHECKSHOW = "showcheckjoke.php";
	// 审核笑话
	public static String CHECKJOKE = "checkjokes.php";
	// 点赞
	public static String LIKE = "likejoke.php";
	//合成语音播放状态
	public static boolean audioplay=false;
	//人物and语速
	public static String AUDIOROLEANDSPEED = "editroleandspeed.php";
	//发送邮件找回密码
	public static String RESEARCHPASSWORD = "sendmail.php";
	//管理员权限显示列表
	public static String SHOWADMINLIST = "showauthoritylist.php";
	// 合成当时笑话ID
	public static int jokeid=-1;
	// 播放当时笑话ID 
	public static int palyjokeid=-1;
	// 语音播放
	public static MediaPlayer player=new MediaPlayer();
	//发送邮件找回密码
	public static String EDITUSERINFO = "edituserinfo.php";
	//管理员权限显示列表
	public static String APPLYFORADMIN = "applyforadmin.php";
	//播放语音动画
	public static AnimationDrawable anim = null;
	//上传头像
	public static String HEADIMAGE = "uploadhead.php";
}
