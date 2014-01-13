package com.cesfelipesegundo.itis.model;

import java.util.ArrayList;

/**
 * Stats for a learner's result in his/her exams
 * @author ruben
 *
 */
public class LearnerStats {

		//private User learner;
	
		private Long id;
	
		private String surname;
		private String name;
		private String username;
	
		private int examsNumber;
		private int passedNumber;
		private int failedNumber;
		private double gradeMax;
		private double gradeMin;
		private double gradeAverage;
		private double gradeMedian;
		private double gradeStandardDeviation;
		
		private ArrayList<Double> gradeList;
		
		
		public ArrayList<Double> getGradeList() {
			return gradeList;
		}
		public void setGradeList(ArrayList<Double> gradeList) {
			this.gradeList = gradeList;
		}
		public int getExamsNumber() {
			return examsNumber;
		}
		public void setExamsNumber(int examsNumber) {
			this.examsNumber = examsNumber;
		}
		public int getFailedNumber() {
			return failedNumber;
		}
		public void setFailedNumber(int failedNumber) {
			this.failedNumber = failedNumber;
		}
		public double getGradeAverage() {
			return gradeAverage;
		}
		public void setGradeAverage(double gradeAverage) {
			this.gradeAverage = gradeAverage;
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
		public int getPassedNumber() {
			return passedNumber;
		}
		public void setPassedNumber(int passedNumber) {
			this.passedNumber = passedNumber;
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
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		
		public boolean equals(Object o)
		{
			LearnerStats learnerStats = (LearnerStats) o; 
			return learnerStats.id.equals(this.id);
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		
}
