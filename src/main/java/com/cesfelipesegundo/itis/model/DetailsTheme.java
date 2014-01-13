package com.cesfelipesegundo.itis.model;

public class DetailsTheme {

	/**
	 * id Subject
	 */
	private Long id;
	
	/**
	 * Number of visible Questions with difficultyLow
	 */
	private Long difficultyLow;
	
	/**
	 * Number of Answers by Question with difficultyLow
	 */
	private Long answerDifficultyLow;
	
	/**
	 * Number of Questions with difficultyMedium
	 */
	private Long difficultyMedium;
	
	/**
	 * Number of Answers by Question with difficultyMedium
	 */
	private Long answerDifficultyMedium;
	
	/**
	 * Number of visible Questions with difficultyHigh
	 */
	private Long difficultyHigh;
	
	/**
	 * Number of visible Questions with difficultyHigh
	 */
	private Long answerDifficultyHigh;
	
	/**
	 * Number of all Questions with difficulty low
	 */
	private Long totalQuestionLow;
	
	/**
	 * Number of all Questions with difficulty medium
	 */
	private Long totalQuestionMedium;
	
	/**
	 * Number of all Questions with difficulty high
	 */
	private Long totalQuestionHigh;
	
	/**
	 * Number of visible Questions with difficulty low and type question fill
	 */
	private Long difficultyLowFill;
	
	/**
	 * Number of visible Questions with difficulty medium and type question fill
	 */
	private Long difficultyMediumFill;
	
	/**
	 * Number of visible Questions with difficulty high and type question fill
	 */
	private Long difficultyHighFill;
	
	

	public Long getId() {
		return id;
	}

	public Long getDifficultyLowFill() {
		return difficultyLowFill;
	}


	public void setDifficultyLowFill(Long difficultyLowFill) {
		this.difficultyLowFill = difficultyLowFill;
	}


	public Long getDifficultyMediumFill() {
		return difficultyMediumFill;
	}


	public void setDifficultyMediumFill(Long difficultyMediumFill) {
		this.difficultyMediumFill = difficultyMediumFill;
	}


	public Long getDifficultyHighFill() {
		return difficultyHighFill;
	}


	public void setDifficultyHighFill(Long difficultyHighFill) {
		this.difficultyHighFill = difficultyHighFill;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public Long getDifficultyLow() {
		return difficultyLow;
	}

	public void setDifficultyLow(Long difficultyLow) {
		this.difficultyLow = difficultyLow;
	}

	public Long getDifficultyMedium() {
		return difficultyMedium;
	}

	public void setDifficultyMedium(Long difficultyMedium) {
		this.difficultyMedium = difficultyMedium;
	}

	public Long getDifficultyHigh() {
		return difficultyHigh;
	}

	public void setDifficultyHigh(Long difficultyHigh) {
		this.difficultyHigh = difficultyHigh;
	}

	public Long getAnswerDifficultyLow() {
		return answerDifficultyLow;
	}

	public void setAnswerDifficultyLow(Long answerDifficultyLow) {
		this.answerDifficultyLow = answerDifficultyLow;
	}

	public Long getAnswerDifficultyMedium() {
		return answerDifficultyMedium;
	}

	public void setAnswerDifficultyMedium(Long answerDifficultyMedium) {
		this.answerDifficultyMedium = answerDifficultyMedium;
	}

	public Long getAnswerDifficultyHigh() {
		return answerDifficultyHigh;
	}

	public void setAnswerDifficultyHigh(Long answerDifficultyHigh) {
		this.answerDifficultyHigh = answerDifficultyHigh;
	}
	
	
	public Long getTotalQuestionLow() {
		return totalQuestionLow;
	}

	public void setTotalQuestionLow(Long totalQuestionLow) {
		this.totalQuestionLow = totalQuestionLow;
	}

	public Long getTotalQuestionMedium() {
		return totalQuestionMedium;
	}

	public void setTotalQuestionMedium(Long totalQuestionMedium) {
		this.totalQuestionMedium = totalQuestionMedium;
	}

	public Long getTotalQuestionHigh() {
		return totalQuestionHigh;
	}

	public void setTotalQuestionHigh(Long totalQuestionHigh) {
		this.totalQuestionHigh = totalQuestionHigh;
	}

	public String toString() {
		return answerDifficultyLow+", "+answerDifficultyMedium+", "+answerDifficultyHigh+", "+difficultyLow+", "+difficultyMedium+", "+difficultyHigh;
	}
	
	

	
	
	
}
