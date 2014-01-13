package com.cesfelipesegundo.itis.model;

import java.util.List;

/**
 * Stats for a question
 * @author ruben, J. M. Colmenar
 *
 */
public class QuestionStats {
	
	private Long id;
	
	private String title; // title of the question
	private String text; // statement of the question	
	private String subject;	// subject of the question
	private String comment;	// comment of the question, needed for questions with mmedia file and no text
	
	private int appeareances; // times has appeared in all exams of a group
	private int answers; // times has been answered in all exams of a group
	private int successes; // times has been succesfully answered in all exams of a group
	
	private int confidenceLevelAppeareances; //times has appeared with confidence level
	private int confidenceLevelAnswers;
	private int spectiveEasiness;
	private int trueEasiness;
	private int insecurityEasiness;
	
	private List<AnswerStats> answerStats;
	
	
	
	
	public List<AnswerStats> getAnswerStats() {
		return answerStats;
	}

	public void setAnswerStats(List<AnswerStats> answerStats) {
		this.answerStats = answerStats;
	}

	public int getConfidenceLevelAnswers() {
		return confidenceLevelAnswers;
	}

	public void setConfidenceLevelAnswers(int confidenceLevelAnswers) {
		this.confidenceLevelAnswers = confidenceLevelAnswers;
	}

	public int getInsecurityEasiness() {
		return insecurityEasiness;
	}

	public void setInsecurityEasiness(int insecurityEasiness) {
		this.insecurityEasiness = insecurityEasiness;
	}

	public int getConfidenceLevelAppeareances() {
		return confidenceLevelAppeareances;
	}

	public void setConfidenceLevelAppeareances(int confidenceLevelAppeareances) {
		this.confidenceLevelAppeareances = confidenceLevelAppeareances;
	}

	public int getSpectiveEasiness() {
		return spectiveEasiness;
	}

	public void setSpectiveEasiness(int spectiveEasiness) {
		this.spectiveEasiness = spectiveEasiness;
	}

	public int getTrueEasiness() {
		return trueEasiness;
	}

	public void setTrueEasiness(int trueEasiness) {
		this.trueEasiness = trueEasiness;
	}

	public QuestionStats() {}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getAppeareances() {
		return appeareances;
	}
	public void setAppeareances(int appeareances) {
		this.appeareances = appeareances;
	}
	
	public int getSuccesses() {
		return successes;
	}
	public void setSuccesses(int successes) {
		this.successes = successes;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public int getAnswers() {
		return answers;
	}
	public void setAnswers(int answers) {
		this.answers = answers;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public boolean equals(QuestionStats questionStat)
	{
		return questionStat.id.equals(this.id);
	}
	
	public boolean equals(Object o)
	{
		QuestionStats questionStats = (QuestionStats) o; 
		return questionStats.id.equals(this.id);
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
