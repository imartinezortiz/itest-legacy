package com.cesfelipesegundo.itis.model;

import java.util.ArrayList;

/**
 * Stats for an exam
 * @author ruben
 *
 */
public class ExamStats {
	
	//private TemplateExam exam;
	
	private Long id;
	
	private String title;
	private int duration;				// Duration
	private int maxGrade;
	private boolean customized;
	
	// about learners
	private int learnersNumber;	//matriculados
	private int doneNumber;		//presentados
	private int passNumber;		//aprobados
	
	// about grades
	private double gradeMax;
	private double gradeMin;
	private double gradeAverage;
	private double gradeMedian;
	private double gradeStandardDeviation;
	// about times
	private int timeMax;
	private int timeMin;
	private double timeAverage;
	private double timeMedian;
	private double timeStandardDeviation;
	
	private ArrayList<Double> gradeList;
	private ArrayList<Integer> timeList;
	


	public boolean isCustomized() {
		return customized;
	}



	public void setCustomized(boolean customized) {
		this.customized = customized;
	}



	public int getDoneNumber() {
		return doneNumber;
	}



	public void setDoneNumber(int doneNumber) {
		this.doneNumber = doneNumber;
	}



	public int getDuration() {
		return duration;
	}



	public void setDuration(int duration) {
		this.duration = duration;
	}



	public double getGradeAverage() {
		return gradeAverage;
	}



	public void setGradeAverage(double gradeAverage) {
		this.gradeAverage = gradeAverage;
	}



	public ArrayList<Double> getGradeList() {
		return gradeList;
	}



	public void setGradeList(ArrayList<Double> gradeList) {
		this.gradeList = gradeList;
	}



	public double getGradeMax() {
		return gradeMax;
	}



	public void setGradeMax(double gradeMax) {
		this.gradeMax = gradeMax;
	}



	public double getGradeMedian() {
		return gradeMedian;
	}



	public void setGradeMedian(double gradeMedian) {
		this.gradeMedian = gradeMedian;
	}



	public double getGradeMin() {
		return gradeMin;
	}



	public void setGradeMin(double gradeMin) {
		this.gradeMin = gradeMin;
	}



	public double getGradeStandardDeviation() {
		return gradeStandardDeviation;
	}



	public void setGradeStandardDeviation(double gradeStandardDeviation) {
		this.gradeStandardDeviation = gradeStandardDeviation;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public int getLearnersNumber() {
		return learnersNumber;
	}



	public void setLearnersNumber(int learnersNumber) {
		this.learnersNumber = learnersNumber;
	}



	public int getMaxGrade() {
		return maxGrade;
	}



	public void setMaxGrade(int maxGrade) {
		this.maxGrade = maxGrade;
	}



	public int getPassNumber() {
		return passNumber;
	}



	public void setPassNumber(int passNumber) {
		this.passNumber = passNumber;
	}



	public double getTimeAverage() {
		return timeAverage;
	}



	public void setTimeAverage(double timeAverage) {
		this.timeAverage = timeAverage;
	}



	public ArrayList<Integer> getTimeList() {
		return timeList;
	}



	public void setTimeList(ArrayList<Integer> timeList) {
		this.timeList = timeList;
	}



	public int getTimeMax() {
		return timeMax;
	}



	public void setTimeMax(int timeMax) {
		this.timeMax = timeMax;
	}



	public double getTimeMedian() {
		return timeMedian;
	}



	public void setTimeMedian(double timeMedian) {
		this.timeMedian = timeMedian;
	}



	public int getTimeMin() {
		return timeMin;
	}



	public void setTimeMin(int timeMin) {
		this.timeMin = timeMin;
	}



	public double getTimeStandardDeviation() {
		return timeStandardDeviation;
	}



	public void setTimeStandardDeviation(double timeStandardDeviation) {
		this.timeStandardDeviation = timeStandardDeviation;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public boolean equals(Object o)
	{
		ExamStats examStats = (ExamStats) o; 
		return examStats.id.equals(this.id);
	}
}
