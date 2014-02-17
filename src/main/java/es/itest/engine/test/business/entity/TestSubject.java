package es.itest.engine.test.business.entity;

import java.util.List;

/**
 * Class for the themes that conform the syllabus of a course. The themes are shared
 * by all the groups of a course.
 * @author Alberto
 *
 */
public class TestSubject {

	private Long id;
	private String subject;	// Nombre del tema
	private int questionsNumber;
	private int answersxQuestionNumber;
	private int maxDifficulty, minDifficulty;
	private int sort;
	private int questionsType;
	
	
	
	public int getQuestionsType() {
		return questionsType;
	}
	public void setQuestionsType(int questionsType) {
		this.questionsType = questionsType;
	}
	private List<Item> questions;	// Lista de preguntas
	
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
	
	public List<Item> getQuestions() {
		return questions;
	}
	public void setQuestions(List<Item> questions) {
		this.questions = questions;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
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
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}

	
	
	
}
