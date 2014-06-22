package es.itest.engine.test.business.entity;

import java.util.Date;
import java.util.List;


/**
 * Añade, a los datos básicos de un examen, el listado de preguntas y respuestas
 * @author chema
 *
 */
public class Test extends TestDetails {

	private int visibility;				// Visibility for the questions uses to make the exam
	private int questionDistribution;	// Distribution of the questions: continuous or by questions 
	private int duration;				// Duration
	private double maxGrade;				// Maximun grade for the exam
	private List<TestSubject> subjects;	// Lista de temas
	private boolean activeReview;
	private Date startDate;
	private Date endDate;
	
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
	
	/** Min question grade*/
	private double minQuestionGrade;	
	
	/** Confidence level*/
	private boolean confidenceLevel;
	
	
	
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

	public List<TestSubject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<TestSubject> subjects) {
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
	
}
