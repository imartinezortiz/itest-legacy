package com.cesfelipesegundo.itis.model;

import java.util.Date;
import java.util.List;


/**
 * Añade, a los datos básicos de un examen, la configuración de un examen
 * @author chema
 *
 */
public class ConfigExam extends BasicDataExam {

	private int visibility;				// Visibility for the questions uses to make the exam
	private int questionDistribution;	// Distribution of the questions: continuous or by questions 
	private int duration;				// Duration
	private double maxGrade;				// Maximun grade for the exam
	private List<ConfigExamSubject> subjects;	// Lista de temas
	private boolean activeReview;
	private Date startDate;
	private Date endDate;
	private Date startDateRevision;
	private Date endDateRevision;
	private int weight;				// Weight of the exam into the final grade
	private int questionNumber;
	private boolean customized;
	
	
	
	public boolean isCustomized() {
		return customized;
	}

	public void setCustomized(boolean customized) {
		this.customized = customized;
	}

	public ConfigExam(){
		
	}
	
	public ConfigExam(ConfigExam exam) {
		super();
		this.setGroup(exam.getGroup());
		this.setTitle(exam.getTitle());
		this.setId(exam.getId());
		this.visibility = exam.visibility;
		this.questionDistribution = exam.questionDistribution;
		this.duration = exam.duration;
		this.maxGrade = exam.maxGrade;
		this.subjects = exam.subjects;
		this.activeReview = exam.activeReview;
		this.startDate = exam.startDate;
		this.endDate = exam.endDate;
		this.startDateRevision = exam.startDateRevision;
		this.endDateRevision = exam.endDateRevision;
		this.weight = exam.weight;
		this.questionNumber = exam.questionNumber;
		this.partialCorrection = exam.partialCorrection;
		this.showNumCorrectAnswers = exam.showNumCorrectAnswers;
		this.penQuestionFailed = exam.penQuestionFailed;
		this.penQuestionNotAnswered = exam.penQuestionNotAnswered;
		this.penAnswerFailed = exam.penAnswerFailed;
		this.penConfidenceLevel = exam.penConfidenceLevel;
		this.rewardConfidenceLevel = exam.rewardConfidenceLevel;
		this.minQuestionGrade = exam.minQuestionGrade;
		this.published = exam.published;
		this.confidenceLevel = exam.confidenceLevel;
		this.customized = exam.customized;
	}

	/** Sets partial correction by answers*/
	private boolean partialCorrection;
	
	/** Show number of correct answers*/
	private boolean showNumCorrectAnswers;
	
	/** Penalty by question failed*/
	private double penQuestionFailed;
	
	/** Penalty by question not answered*/
	private double penQuestionNotAnswered;
	
	/** Penalty by answer failed*/
	private double penAnswerFailed;
	
	/** Penalty by confidence level*/
	private double penConfidenceLevel;
	
	/** Reward by confidence level*/
	private double rewardConfidenceLevel;
	
	/** Min question grade*/
	private double minQuestionGrade;
	
	/** Exam is published*/
	private boolean published;
	
	/** Exam is active confidence level*/
	private boolean confidenceLevel;
	
	
	
	public boolean isConfidenceLevel() {
		return confidenceLevel;
	}

	public void setConfidenceLevel(boolean confidenceLevel) {
		this.confidenceLevel = confidenceLevel;
	}

	public List<ConfigExamSubject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<ConfigExamSubject> subjects) {
		this.subjects = subjects;
	}

	public int getQuestionDistribution() {
		return questionDistribution;
	}

	public void setQuestionDistribution(int questionDistribution) {
		this.questionDistribution = questionDistribution;
	}

	public int getVisibility() {
		return visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public double getMaxGrade() {
		return maxGrade;
	}

	public void setMaxGrade(double maxGrade) {
		this.maxGrade = maxGrade;
	}

	public boolean isActiveReview() {
		return activeReview;
	}

	public void setActiveReview(boolean activeReview) {
		this.activeReview = activeReview;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public boolean isPartialCorrection() {
		return partialCorrection;
	}

	public void setPartialCorrection(boolean partialCorrection) {
		this.partialCorrection = partialCorrection;
	}

	public boolean isShowNumCorrectAnswers() {
		return showNumCorrectAnswers;
	}

	public void setShowNumCorrectAnswers(boolean showNumCorrectAnswers) {
		this.showNumCorrectAnswers = showNumCorrectAnswers;
	}

	public double getPenQuestionFailed() {
		return penQuestionFailed;
	}

	public void setPenQuestionFailed(double penQuestionFailed) {
		this.penQuestionFailed = penQuestionFailed;
	}

	public double getPenQuestionNotAnswered() {
		return penQuestionNotAnswered;
	}

	public void setPenQuestionNotAnswered(double penQuestionNotAnswered) {
		this.penQuestionNotAnswered = penQuestionNotAnswered;
	}

	public double getPenAnswerFailed() {
		return penAnswerFailed;
	}

	public void setPenAnswerFailed(double penAnswerFailed) {
		this.penAnswerFailed = penAnswerFailed;
	}

	public double getMinQuestionGrade() {
		return minQuestionGrade;
	}

	public void setMinQuestionGrade(double minQuestionGrade) {
		this.minQuestionGrade = minQuestionGrade;
	}

	public Date getStartDateRevision() {
		return startDateRevision;
	}

	public void setStartDateRevision(Date startDateRevision) {
		this.startDateRevision = startDateRevision;
	}

	public Date getEndDateRevision() {
		return endDateRevision;
	}

	public void setEndDateRevision(Date endDateRevision) {
		this.endDateRevision = endDateRevision;
	}

	public int getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}

	public double getPenConfidenceLevel() {
		return penConfidenceLevel;
	}

	public void setPenConfidenceLevel(double penConfidenceLevel) {
		this.penConfidenceLevel = penConfidenceLevel;
	}

	public double getRewardConfidenceLevel() {
		return rewardConfidenceLevel;
	}

	public void setRewardConfidenceLevel(double rewardConfidenceLevel) {
		this.rewardConfidenceLevel = rewardConfidenceLevel;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}
	
}
