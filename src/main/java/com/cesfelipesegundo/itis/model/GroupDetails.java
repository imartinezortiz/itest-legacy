package com.cesfelipesegundo.itis.model;

import java.util.List;

public class GroupDetails {

	private Long idGroup;
	private String centerName;
	private String subjectName;
	private String groupName;
	private String year;
	//numero de preguntas
	private int numQuestion;
	//numero de temas
	private int numTheme;
	private List<String> teachers;
	
	
	public List<String> getTeachers() {
		return teachers;
	}
	public void setTeachers(List<String> teachers) {
		this.teachers = teachers;
	}
	public Long getIdGroup() {
		return idGroup;
	}
	public void setIdGroup(Long idGroup) {
		this.idGroup = idGroup;
	}
	public String getCenterName() {
		return centerName;
	}
	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public int getNumQuestion() {
		return numQuestion;
	}
	public void setNumQuestion(int numQuestion) {
		this.numQuestion = numQuestion;
	}
	public int getNumTheme() {
		return numTheme;
	}
	public void setNumTheme(int numTheme) {
		this.numTheme = numTheme;
	}
	
	
}
