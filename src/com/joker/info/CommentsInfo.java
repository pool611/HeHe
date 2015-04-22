package com.joker.info;
/*
 * @author郑轩
 * 评论表信息
 */
public class CommentsInfo {
	private int commentsID;
	private String comment;
	private int jokeID;
	private int userID;
	private String username;
	private int time;
	
	public int getCommentsID() {
		return commentsID;
	}
	
	public void setCommentsID(int commentsID) {
		this.commentsID = commentsID;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public int getJokeID() {
		return jokeID;
	}
	
	public void setJokeID(int jokeID) {
		this.jokeID = jokeID;
	}
	
	public int getUserID() {
		return userID;
	}
	
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public int getTime() {
		return time;
	}
	
	public void setTime(int time) {
		this.time = time;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
