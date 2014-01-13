package com.cesfelipesegundo.itis.web;

public interface Constants {

	public static final String USER = "com.cesfelipesegundo.itis.web.user";
	
	// Learner Roles
	public static final String LEARNER = "LEARNER";
	public static final String KID = "KID";
	// Teacher Role
	public static final String TUTOR = "TUTOR";
	public static final String TUTORAV = "TUTORAV";
	
	// Useful constants
	public static final Short YES = 1;
	public static final Short NO = 0;

	/**
	 * Username for demostrations: concrete surname for all demo users
	 */
	public static final String USERDEMOSURNAME = "--DEMO--";
	
	/**
	 * Difficulty constants
	 */
	public static final Short LOW = 0;
	public static final Short MEDIUM = 1;
	public static final Short HIGH = 2;

	/**
	 * Visibility constants
	 */
	public static final Short GROUP = 0;
	public static final Short COURSE = 1;
	public static final Short PUBLIC = 2;

	/**
	 * User sorting constants. Match with the database fields
	 */
	public static final String PERSID = "usuarios.dni";
	public static final String SURNAME = "usuarios.apes";
	public static final String NAME = "usuarios.nombre";	
	public static final String USERNAME = "usuarios.usuario";
	
	/**
	 * Some Directories and related strings
	 */
	public static final String MMEDIAPATH = "common/mmedia/";
	public static final String MTEMPLATESPATH = "common/templates/";
	public static final String MMEDIAPREFIX = "med";
	
	/**
	 * Buffer size to read and write files 
	 */
	public static final Integer FILEBUFSIZE = 512;
	
}
