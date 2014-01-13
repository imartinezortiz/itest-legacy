package com.cesfelipesegundo.itis.biz.api;

import com.cesfelipesegundo.itis.model.User;

public interface UserManagementService {
	public User getUser(String userName);	
	public void addNewConection(User user, String ip);
}