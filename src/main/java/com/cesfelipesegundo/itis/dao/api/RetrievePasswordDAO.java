package com.cesfelipesegundo.itis.dao.api;

import java.util.Date;

import com.cesfelipesegundo.itis.model.User;

public interface RetrievePasswordDAO extends DAO {

	/**
	 * Gets a String token and checks that is in DB
	 * @param token 
	 * @return true if token is in DB, false if token isn't in DB
	 * */
	public boolean checkTokenRetrievePassord(String token);

	/**
	 * Gets an user's id and a token and inserts it into table "recupera_pass"
	 * @param id the user's id
	 * @param token the token that will be insert
	 * @param fechaInsert the date when token has been inserted
	 * @param fechaCaducidad the date when token is no longer valid
	 * */
	public void insertToken(Long id, String token, Date fechaInsert,
			Date fechaCaducidad);

	/**
	 * Gets a token and searches the database
	 * @param token the token searched
	 * @return the user who has the token
	 * */
	public long getTokenUserId(String token);
	
	/**
	 * Gets a token and searches the database
	 * @param token the token searched
	 * @return the date when token is no longer valid
	 * */
	public Date getTokenDateEnd(String token);

	/**
	 * Gets a token and searches the database
	 * @param token the token searched
	 * @return the date when token has been modified
	 * */
	public Date getTokenDateChange(String token);

	/**
	 * Gets a token and a date and find the token in the database to update the change date
	 * @param token the token searched
	 * @param date the change date
	 * */
	public void updateTokenDateChange(String token, Date date);

	/**
	 * Removes the row from the database where the row token is the same as the receiving method
	 * @param token
	 * */
	public void deleteTokenUser(String token);

}
