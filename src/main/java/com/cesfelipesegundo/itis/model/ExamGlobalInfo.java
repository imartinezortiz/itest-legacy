package com.cesfelipesegundo.itis.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Representa la información que se ofrece de cada examen en el interfaz de administrador
 * cuando se muestran los próximos examenes.
 * 
 * @author Ezequiel Yuste Montero
 * */
public class ExamGlobalInfo {
	private Long examId;
	private String examTitle;
	private Date startDate;
	private Date endDate;
	private String nameGroup;
	private String subject;
	private String center;
	private String teacher;
	private List<String> teachers = new ArrayList<String>();
	private String academicYear;
	
	
	public Long getExamId() {
		return examId;
	}
	public void setExamId(Long examId) {
		this.examId = examId;
	}
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
		this.teachers.add(teacher);
	}
	public List<String> getTeachers() {
		return teachers;
	}
	public void setTeachers(List<String> teachers) {
		this.teachers = teachers;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getNameGroup() {
		return nameGroup;
	}
	public void setNameGroup(String nameGroup) {
		this.nameGroup = nameGroup;
	}
	public String getCenter() {
		return center;
	}
	public void setCenter(String center) {
		this.center = center;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getExamTitle() {
		return examTitle;
	}
	public void setExamTitle(String examTitle) {
		this.examTitle = examTitle;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getAcademicYear() {
		return academicYear;
	}
	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}

}
