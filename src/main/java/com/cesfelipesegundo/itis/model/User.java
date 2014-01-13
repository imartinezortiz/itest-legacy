package com.cesfelipesegundo.itis.model;

/**
 * Clase que representa un usuario de iTest
 * @author chema
 *
 */
public class User {
	/** Identificador del usuario (columna idusu de tabla usuarios) */
	private Long id;
	/** Nombre del usuario (columna usuario de tabla usuarios) */
	private String userName;		// Nombre de usuario en la aplicación
	/** Apellidos del usuario (columna apes de tabla usuarios) */
	private String surname;
	/** Nombre real del usuario (columna nombre de la tabla usuarios) */
	private String name;		// Nombre real del usuario
	/** User's ROLE: allow the difference between LEARNER and KID (both are learners) */
	private String role;
	/** User accesss password */
	private String passwd;
	/** Email address */
	private String email;
	/** Personal ID number */
	private String persId;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getPersId() {
		return persId;
	}
	public void setPersId(String persId) {
		this.persId = persId;
	}

}
