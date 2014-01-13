package com.cesfelipesegundo.itis.biz;

import java.util.Date;

import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.dao.api.RetrievePasswordDAO;
import com.cesfelipesegundo.itis.dao.api.UserDAO;

/**
 * Service that allows to perform operations shared by all kind of users.
 * 
 * @author J. M. Colmenar
 *
 */
public class CommonManagementService {

	private UserDAO userDAO;
	private RetrievePasswordDAO retrievePasswordDAO;

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	public RetrievePasswordDAO getRetrievePasswordDAO() {
		return retrievePasswordDAO;
	}

	public void setRetrievePasswordDAO(RetrievePasswordDAO retrievePasswordDAO) {
		this.retrievePasswordDAO = retrievePasswordDAO;
	}

	/**
	 * Checks if the string matches with the real password of the user.
	 * @param user			User object with all attributes filled in
	 * @param oldPasswd		String to compare with
	 * @return				True if oldPasswd matches with the user password
	 */
	public Boolean checkPassword(User user, String oldPasswd) {
		return userDAO.checkUserPasswd(user, oldPasswd);
	}

	/**
	 * Updates the password of the user in the database
	 * @param user			User object
	 * @param newPasswd1	New password
	 * @return				The user object with the updated password
	 */
	public User updatePassword(User user, String newPasswd1) {
		return userDAO.updatePassword(user, newPasswd1);
	}

	/**
	 * Gets a String token and checks that is in DB
	 * @param token 
	 * @return true if token is in DB, false if token isn't in DB
	 * */
	public boolean checkTokenRetrievePassord(String token) {
		return retrievePasswordDAO.checkTokenRetrievePassord(token);
	}

	/**
	 * Gets an user's id and a token and inserts it into table "recupera_pass"
	 * @param id the user's id
	 * @param token the token that will be insert
	 * @param fechaInsert the date when token has been inserted
	 * @param fechaCaducidad the date when token is no longer valid
	 * */
	public void insertToken(Long id, String token,Date fechaInsert, Date fechaCaducidad) {
		retrievePasswordDAO.insertToken(id,token,fechaInsert,fechaCaducidad);
	}

	/**
	 * Gets a token and searches the database
	 * @param token the token searched
	 * @return the user who has the token
	 * */
	public User getTokenUser(String token) {
		long id = retrievePasswordDAO.getTokenUserId(token);
		if(id==-1)
			return null;
		return userDAO.getUser(id);
	}

	/**
	 * Gets a token and searches the database
	 * @param token the token searched
	 * @return the date when token is no longer valid
	 * */
	public Date getTokenDateEnd(String token) {
		return retrievePasswordDAO.getTokenDateEnd(token);
	}

	/**
	 * Gets a token and searches the database
	 * @param token the token searched
	 * @return the date when token has been modified
	 * */
	public Date getTokenDateChange(String token) {
		return retrievePasswordDAO.getTokenDateChange(token);
	}

	/**
	 * Gets a token and a date and find the token in the database to update the change date
	 * @param token the token searched
	 * @param date the change date
	 * */
	public void updateTokenDateChange(String token, Date date) {
		retrievePasswordDAO.updateTokenDateChange(token,date);
	}

	/**
	 * Removes the row from the database where the row token is the same as the receiving method
	 * @param token
	 * */
	public void deleteTokenUser(String token) {
		retrievePasswordDAO.deleteTokenUser(token);
	}

}
