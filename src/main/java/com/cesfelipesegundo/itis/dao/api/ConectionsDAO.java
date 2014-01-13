package com.cesfelipesegundo.itis.dao.api;

import java.util.Date;
import java.util.List;

import com.cesfelipesegundo.itis.model.Conection;
import com.cesfelipesegundo.itis.model.User;

public interface ConectionsDAO extends DAO{

	/**
	 * This method returns the last 100 connections
	 * 
	 * @return List<Conection>
	 * */
	public List<Conection> get100LastConections();

	/**
	 * Add a new conection into database
	 * @param user : The user who has conected
	 * @param ip : The user's ip
	 * @return
	 * */
	public void addNewConection(User user, String ip);

	/**
	 * Return a filtered list of conections
	 * @param idConection filtro por id de conexión
	 * @param userNameConection filtro por el nombre de usuario de la conexión
	 * @param date1 filtro para fechas mayores o iguales que esta
	 * @param date2 filtro para fechas menores o iguales que esta
	 * @return List<Conection>
	 * */
	public List<Conection> runFilterAndSearch100Conections(Long idConection,
			String userNameConection, Date date1, Date date2);

	/**
	 * Return a list with the 5 last conections
	 * @param id
	 * @return List<Conection>
	 * */
	public List<Conection> show5LastConections(Long id);
}
