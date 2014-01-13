package com.cesfelipesegundo.itis.model;

import java.util.Date;
import java.util.List;


/**
 * Añade, a los datos básicos de un examen, el listado de preguntas y respuestas
 * @author chema
 *
 */
public class Exam extends BasicDataExam {

	private int duration;
	private double maxGrade;
	private List<ExamQuestion> questions;	// Lista de preguntas con contenido multimedia y respuestas asociadas
	private double examGrade; 				// Obtained after the grade: just for revision
	private int questionsNumber;			// Numero de preguntas totales
	private long startingDate;
	private Date startingDateExam;
	
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
	
	/** Exam is active confidence level*/
	private boolean confidenceLevel;
	
	/** Min question grade*/
	private double minQuestionGrade;
	
	
	
	public Date getStartingDateExam() {
		return startingDateExam;
	}

	public void setStartingDateExam(Date startingDateExam) {
		this.startingDateExam = startingDateExam;
		this.startingDate = startingDateExam.getTime();
	}

	public boolean isConfidenceLevel() {
		return confidenceLevel;
	}

	public void setConfidenceLevel(boolean confidenceLevel) {
		this.confidenceLevel = confidenceLevel;
	}

	public boolean isShowNumCorrectAnswers() {
		return showNumCorrectAnswers;
	}

	public void setShowNumCorrectAnswers(boolean showNumCorrectAnswers) {
		this.showNumCorrectAnswers = showNumCorrectAnswers;
	}

	public boolean isPartialCorrection() {
		return partialCorrection;
	}

	public void setPartialCorrection(boolean partialCorrection) {
		this.partialCorrection = partialCorrection;
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

	public long getStartingDate() {
		return startingDate;
	
	}

	public void setStartingDate(long startingDate) {
		this.startingDate = startingDate;
	}

	public List<ExamQuestion> getQuestions() {
		return questions;
	}
	
	public void setQuestions(List<ExamQuestion> questions) {
		this.questions = questions;
		this.questionsNumber = questions.size();
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

	public double getExamGrade() {
		return examGrade;
	}

	public void setExamGrade(double examGrade) {
		this.examGrade = examGrade;
	}
	
	public int getQuestionsNumber() {
		return questionsNumber;
	}

	public void setQuestionsNumber(int questionsNumber) {
		this.questionsNumber = questionsNumber;
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
	
	
}
