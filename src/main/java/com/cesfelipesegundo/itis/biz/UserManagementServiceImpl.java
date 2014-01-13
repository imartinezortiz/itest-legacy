package com.cesfelipesegundo.itis.biz;

import com.cesfelipesegundo.itis.biz.api.UserManagementService;
import com.cesfelipesegundo.itis.dao.api.ConectionsDAO;
import com.cesfelipesegundo.itis.dao.api.UserDAO;
import com.cesfelipesegundo.itis.model.User;

public class UserManagementServiceImpl extends BaseService implements UserManagementService {

	private UserDAO userDAO;
	private ConectionsDAO conectionsDAO;
	
	
	
	public ConectionsDAO getConectionsDAO() {
		return conectionsDAO;
	}


	public void setConectionsDAO(ConectionsDAO conectionsDAO) {
		this.conectionsDAO = conectionsDAO;
	}


	public UserDAO getUserDAO() {
		return userDAO;
	}


	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public User getUser(String userName) {
		return userDAO.getUser(userName);
	}
	
	public void addNewConection(User user, String ip){
		conectionsDAO.addNewConection(user,ip);
	}
	
}
