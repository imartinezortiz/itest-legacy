package com.cesfelipesegundo.itis.model;

import java.util.Date;

/**
 * This class represents a connection from the database
 * */
public class Conection {

	/**The id of a connection*/
	private Long id;
	/**The user who made the connection*/
	private User user;
	/***/
	private Date date;
	/**The user's ip*/
	private String ip;
	
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
