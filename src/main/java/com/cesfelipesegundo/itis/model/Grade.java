package com.cesfelipesegundo.itis.model;

import java.util.Date;

/**
 * Class to model the student grades.
 * @author José Luis Risco Martín.
 *
 */
public class Grade {
	/** grade identifier. */
    private Long id;
    
    /** student identifier. */
    private Long idStudent;
    
    /** exam identifier. */
    private Long idExam;
    
    /** ip from where the exam was solved. */
    private String ip;
    
    /** grade. */
    private Double grade;
    
    /** Starting date of the exam. */
    private Date begin;
    
    /** Ending date of the exam. */
    private Date end;
    
    /** Time spent to solve the exam. */
    private Integer time;
    /** the max. grade for the exam*/
    private Double maxGrade;
    
    
    
    
	public Double getMaxGrade() {
		return maxGrade;
	}

	public void setMaxGrade(Double maxGrade) {
		this.maxGrade = maxGrade;
	}

	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Double getGrade() {
		return grade;
	}

	public void setGrade(Double grade) {
		this.grade = grade;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdExam() {
		return idExam;
	}

	public void setIdExam(Long idExam) {
		this.idExam = idExam;
	}

	public Long getIdStudent() {
		return idStudent;
	}

	public void setIdStudent(Long idStudent) {
		this.idStudent = idStudent;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}
	
	public Date getDuration() {
		return new Date(end.getTime() - begin.getTime());
	}
}