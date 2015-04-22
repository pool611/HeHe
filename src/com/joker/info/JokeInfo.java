package com.joker.info;

import java.io.Serializable;

/*
 * authorÖ£Ðù
 * Ð¦»°ÐÅÏ¢
 */
public class JokeInfo implements Serializable {
	private int jokeID;
	private int usernameID;
	private String username;
	private String image;
	private String radio;
	private String text;
	private int like;
	private int share;
	private int commentstimes;
	private int time;
	private int ischecked;
	private String head;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public int getJokeID() {
		return jokeID;
	}

	public void setJokeID(int jokeID) {
		this.jokeID = jokeID;
	}

	public int getUsernameID() {
		return usernameID;
	}

	public void setUsernameID(int usernameID) {
		this.usernameID = usernameID;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getRadio() {
		return radio;
	}

	public void setRadio(String radio) {
		this.radio = radio;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getLike() {
		return like;
	}

	public void setLike(int like) {
		this.like = like;
	}

	public int getShare() {
		return share;
	}

	public void setShare(int share) {
		this.share = share;
	}

	public int getCommentstimes() {
		return commentstimes;
	}

	public void setCommentstimes(int commentstimes) {
		this.commentstimes = commentstimes;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getIschecked() {
		return ischecked;
	}

	public void setIschecked(int ischecked) {
		this.ischecked = ischecked;
	}

}
