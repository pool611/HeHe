package com.joker.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.joker.info.CommentsInfo;
import com.joker.info.JokeInfo;
import com.joker.info.UserInfo;

public class MyJson {
	/*
	 * @author 郑轩 参数value : 服务器返回的Json格式数据 解析JSON数据获取笑话 返回List<JokeInfo>
	 */
	public List<JokeInfo> getJokeList(String value) {
		List<JokeInfo> jokelist = new ArrayList<JokeInfo>();
		try {
			JSONArray joke = new JSONArray(value);
			for (int i = 0; i < joke.length(); i++) {
				JokeInfo jokeinfo = new JokeInfo();
				JSONObject joketemp = joke.getJSONObject(i);
				jokeinfo.setJokeID(joketemp.getInt("VID"));
				jokeinfo.setUsernameID(joketemp.getInt("UID"));
				jokeinfo.setImage(joketemp.getString("IMG"));
				jokeinfo.setRadio(joketemp.getString("RADIO"));
				jokeinfo.setText(joketemp.getString("TEXT"));
				jokeinfo.setLike(joketemp.getInt("LIKE"));
				jokeinfo.setShare(joketemp.getInt("SHARE"));
				jokeinfo.setCommentstimes(joketemp.getInt("COMMENTSTIMES"));
				jokeinfo.setTime(joketemp.getInt("TIME"));
				jokeinfo.setIschecked(joketemp.getInt("ISCHECKED"));
				jokeinfo.setUsername(joketemp.getString("NAME"));
				jokeinfo.setHead(joketemp.getString("HEAD"));
				jokelist.add(jokeinfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jokelist;
	}

	/*
	 * @author郑轩 参数value : 服务器返回的Json格式数据 解析JSON数据获取评论 返回List<CommentsInfo>
	 */
	public List<CommentsInfo> getCommentsInfo(String value) {
		List<CommentsInfo> commentlist = new ArrayList<CommentsInfo>();
		try {
			JSONArray comments = new JSONArray(value);
			for (int i = 0; i < comments.length(); i++) {
				CommentsInfo commentsinfo = new CommentsInfo();
				JSONObject commenttemp = comments.getJSONObject(i);
				commentsinfo.setCommentsID(commenttemp.getInt("CID"));
				commentsinfo.setJokeID(commenttemp.getInt("VID"));
				commentsinfo.setUserID(commenttemp.getInt("UID"));
				commentsinfo.setTime(commenttemp.getInt("TIME"));
				commentsinfo.setComment(commenttemp.getString("VALUE"));
				commentsinfo.setUsername(commenttemp.getString("NAME"));
				commentlist.add(commentsinfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return commentlist;
	}

	/*
	 * @author郑轩 参数value : 服务器返回的Json格式数据 解析JSON数据获取用户列表 返回List<UserInfo>
	 */
	public List<UserInfo> getUserList(String value) {
		List<UserInfo> userinfolist = new ArrayList<UserInfo>();
		
		try {
			JSONArray userlist = new JSONArray(value);
			for (int i = 0; i < userlist.length(); i++) {
				UserInfo userinfo = new UserInfo();
				JSONObject userinfotemp = userlist.getJSONObject(i);
				userinfo.setUserID(userinfotemp.getInt("UID"));
				userinfo.setUsername(userinfotemp.getString("NAME"));
				userinfo.setPassword(userinfotemp.getString("PASSWORD"));
				userinfo.setAge(userinfotemp.getInt("AGE"));
				userinfo.setHobbies(userinfotemp.getString("HOBBIES"));
				userinfo.setPlace(userinfotemp.getString("PLACE"));
				userinfo.setScore(userinfotemp.getInt("SCORE"));
				userinfo.setHead(userinfotemp.getString("HEAD"));
				userinfo.setSex(userinfotemp.getInt("SEX"));
				userinfo.setEmail(userinfotemp.getString("EMAIL"));
				userinfo.setStatus(userinfotemp.getInt("STATUS"));
				userinfo.setAudiorole(userinfotemp.getString("AUDIOROLE"));
				userinfo.setAudiospeed(userinfotemp.getInt("AUDIOSPEED"));
				userinfo.setApplyforadmin(userinfotemp.getInt("APPLYFORADMIN"));
				userinfolist.add(userinfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return userinfolist;
	}
	
	/*
	 * @author郑轩 参数value : 服务器返回的Json格式数据 解析JSON数据获取用户基本信息 返回UserInfo
	 */
	public UserInfo getUserInfo(String value) {
		UserInfo userinfo = new UserInfo();
		try {
			JSONArray user = new JSONArray(value);
			JSONObject usertemp = user.getJSONObject(0);
			userinfo.setUserID(usertemp.getInt("UID"));
			userinfo.setUsername(usertemp.getString("NAME"));
			userinfo.setPassword(usertemp.getString("PASSWORD"));
			userinfo.setAge(usertemp.getInt("AGE"));
			userinfo.setHobbies(usertemp.getString("HOBBIES"));
			userinfo.setPlace(usertemp.getString("PLACE"));
			userinfo.setScore(usertemp.getInt("SCORE"));
			userinfo.setHead(usertemp.getString("HEAD"));
			userinfo.setSex(usertemp.getInt("SEX"));
			userinfo.setEmail(usertemp.getString("EMAIL"));
			userinfo.setStatus(usertemp.getInt("STATUS"));
			userinfo.setAudiorole(usertemp.getString("AUDIOROLE"));
			userinfo.setAudiospeed(usertemp.getInt("AUDIOSPEED"));
			userinfo.setApplyforadmin(usertemp.getInt("APPLYFORADMIN"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return userinfo;
	}
}
