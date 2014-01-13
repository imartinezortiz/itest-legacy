package com.cesfelipesegundo.itis.model;

/**
 * Class for the configuration of a theme that belongs to a exam configuration
 * 
 * @author Alberto, Chema
 *
 */
public class ConfigExamSubject {

	private Long id;
	private ConfigExam cexam;	// Exam configuration related 
	private Subject subject;	// Related subject
	private int questionsNumber;
	private int answersxQuestionNumber;
	private int maxDifficulty, minDifficulty;
	private int questionType;
		
	
	public int getQuestionType() {
		return questionType;
	}
	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}
	public int getAnswersxQuestionNumber() {
		return answersxQuestionNumber;
	}
	public void setAnswersxQuestionNumber(int answersxQuestionNumber) {
		this.answersxQuestionNumber = answersxQuestionNumber;
	}
	public int getMaxDifficulty() {
		return maxDifficulty;
	}
	public void setMaxDifficulty(int maxDifficulty) {
		this.maxDifficulty = maxDifficulty;
	}
	public int getMinDifficulty() {
		return minDifficulty;
	}
	public void setMinDifficulty(int minDifficulty) {
		this.minDifficulty = minDifficulty;
	}
	public int getQuestionsNumber() {
		return questionsNumber;
	}
	public void setQuestionsNumber(int questionsNumber) {
		this.questionsNumber = questionsNumber;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ConfigExam getCexam() {
		return cexam;
	}
	public void setCexam(ConfigExam cexam) {
		this.cexam = cexam;
	}
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	public Subject getSubject() {
		return subject;
	}
		
	
	
}
