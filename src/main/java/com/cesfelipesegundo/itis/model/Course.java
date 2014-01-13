package com.cesfelipesegundo.itis.model;

/**
 * Representa una asignatura
 * @author chema
 *
 */
public class Course {
	
	private Long id;			// Id de la asignatura
	private String name;
	private String code;
	private String studies;
	private String comments;
	private String level;
	private int numGroups;
	
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getStudies() {
		return studies;
	}
	public void setStudies(String studies) {
		this.studies = studies;
	}
	public int getNumGroups() {
		return numGroups;
	}
	public void setNumGroups(int numGroups) {
		this.numGroups = numGroups;
	} 
}
