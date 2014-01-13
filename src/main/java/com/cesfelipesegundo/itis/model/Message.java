package com.cesfelipesegundo.itis.model;

public class Message {
	public enum MessageType {INFO, ERROR, WARN, FATAL};
	private String key;
	private MessageType type;
	private Object[] params;
	
	public Message(){
		
	}
	
	public Message(String key, MessageType type){
		this.key = key;
		this.type = type;
	}

	public Message(String key, Object[] params, MessageType type) {
		this(key, type);
		this.params=params;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}
	
	
}
