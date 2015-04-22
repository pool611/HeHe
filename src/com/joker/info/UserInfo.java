package com.joker.info;

import java.io.Serializable;

/*
 * @author 郑轩
 * 用户表信息
 */
public class UserInfo implements Serializable {
	private int userID;
	private String username;
	private String password;
	private int age;
	private String hobbies;
	private String place;
	private int score;
	private String time;
	private String head;
	private int sex;
	private String email;
	private int status;
	private String audiorole;
	private int audiospeed;
	private int applyforadmin;

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHobbies() {
		return hobbies;
	}

	public void setHobbies(String hobbies) {
		this.hobbies = hobbies;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAudiorole() {
		return audiorole;
	}

	public void setAudiorole(String audiorole) {
		this.audiorole = audiorole;
	}

	public int getAudiospeed() {
		return audiospeed;
	}

	public void setAudiospeed(int audiospeed) {
		this.audiospeed = audiospeed;
	}

	public int getApplyforadmin() {
		return applyforadmin;
	}

	public void setApplyforadmin(int applyforadmin) {
		this.applyforadmin = applyforadmin;
	}
}
