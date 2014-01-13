package com.cesfelipesegundo.itis.model;

/**
 * Class to model a calif data for an alum for an exam
 * @author José Luis Risco Martín
 *
 */
public class CalifData {
		
	    
    /** Exam related with this calif */
	private Long idexam;
	
	/** Alum related with this calif */
	private Long idalu;
	
	/** Title of the exan related (config_exams) */
	private String title;

	/* Surname, name and username of the alum (usuarios) */
    private String surname, name, username;
    
    /** Grade obtained in the exam */
    private double grade;
    
    /** MaxGrade of the exam related (config_exams) */
    private int maxGrade;
    
    /** Duration ot the exam related (config_exams) */
    private int duration;
    
    /** Time of the exam related (califs) */
    private int time;
    
       
/*
    public CalifData(Long idexam, Long idalu,
    		String title, String surname, String name,
    		double grade, int maxGrade, int duration, int time)
    {
    	this.idexam = idexam;
    	this.idalu = idalu;
    	this.title = title;
    	this.surname = surname;
    	this.name = name;
    	this.grade = grade;
    	this.maxGrade = maxGrade;
    	this.duration = duration;
    	this.time = time;
    	
    }
*/  
    
	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	public Long getIdalu() {
		return idalu;
	}

	public void setIdalu(Long idalu) {
		this.idalu = idalu;
	}

	public Long getIdexam() {
		return idexam;
	}

	public void setIdexam(Long idexam) {
		this.idexam = idexam;
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

	public int getMaxGrade() {
		return maxGrade;
	}

	public void setMaxGrade(int maxGrade) {
		this.maxGrade = maxGrade;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	
}