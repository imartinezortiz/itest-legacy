package es.itest.engine.test.business.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import es.itest.engine.course.business.entity.Subject;
import es.itest.engine.course.business.entity.TestSessionTemplateSubject;


/**
 * Añade, a los datos básicos de un examen, el listado de preguntas y respuestas
 * @author chema
 *
 */
public class TestSession extends TestDetails {

	private int duration;
	private double maxGrade;
	private List<ItemSession> questions;	// Lista de preguntas con contenido multimedia y respuestas asociadas
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

	public List<ItemSession> getQuestions() {
		return questions;
	}
	
	public void setQuestions(List<ItemSession> questions) {
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
	
	//
	// Business methods
	//
	
	public BigDecimal grade() {
		long startingDate = 0;
		long finalDate = 0;
		long maxDate = 0;
		int durationExam = 0;
		//If reviewing an exam, is not needed to calculate dates again
		if (!isReview) {
			//Calculate and check exam duration, starting and ending date
			startingDate = currentExam.getStartingDate();
			//Real ending date
			finalDate = System.currentTimeMillis();
			//Max ending date possible (Starting date + Pre-configured exam duration)
			maxDate = currentExam.getStartingDate() + (currentExam.getDuration()*60000);
			durationExam = 0;
			//If real ending date < max ending date possible => NO correction needed
			if (finalDate < maxDate)
				durationExam = (int) Math.round((finalDate - startingDate) / 60000.0); // in minutes
			//Else, correction to pre-configured exam duration and max ending date possible
			else {
				durationExam = currentExam.getDuration();
				finalDate = maxDate;
				examLog.info("El examen "+currentExam.getId()+" del alumno "+id+"ha registrado un tiempo de realizacion mayor que el maximo previsto. Se ha procedido a su correcion");
			}
			//End date calculation
		}
		double grade = 0.0;
		
		TestSessionTemplate c = new TestSessionTemplate();
		c.setId(currentExam.getId());
		c = testSessionTemplateDAO.getTestSessionTemplate(currentExam.getId());
		
		int numberOfQuestions = 0;
		//Obtain the total number of questions
		for(TestSessionTemplateSubject subjects : c.getSubjects()){
			numberOfQuestions+=subjects.getQuestionsNumber();
		}
		//Obtain the maximun exam's grade and maximun grade per question
		double maxGrade = currentExam.getMaxGrade();
		double maxGradePerQuestion = maxGrade
				/  numberOfQuestions;
		List<ItemSession> questions = currentExam.getQuestions();
		if(checkConfigExam){
			for (ItemSession question : questions) {
				boolean deleteQuestion = true;
				/*
				 * Recorremos la lista de temas para buscar a que tema pertenece esta pregunta*/
				for(TestSessionTemplateSubject subject : c.getSubjects()){
					Subject theme = subjectDAO.getSubjectByQuestionId(question.getId());
					if(subject.getSubject().getId().equals(theme.getId()) && subject.getQuestionType()==question.getType()){
						long qdif = question.getDifficulty();
						int min = subject.getMinDifficulty();
						int max = subject.getMaxDifficulty();
						if((min==qdif)||(max==qdif)){
							if(subject.getQuestionsNumber()>0){
								subject.setQuestionsNumber(subject.getQuestionsNumber()-1);
								deleteQuestion = false;
								break;
							}
						}
					}
				}
				if(deleteQuestion){
					examDAO.deleteQuestionFromExam(currentExam.getId(), question.getId());
				}
			}
		}
		//For each question
		for (ItemSession question : questions) {
			double questionGrade = gradeQuestion(question,id,currentExam,maxGradePerQuestion,updateDatabase);			
			question.setQuestionGrade(questionGrade);
			grade += questionGrade;
		}
		if(updateDatabase){
			boolean checked = false;
			int retries = 0;
			do{
				//If reviewing, no date update needed!!
				if (!isReview)
					basicDataExamDAO.updateCalif(id, currentExam.getId(), finalDate, grade, durationExam);
				else
					basicDataExamDAO.updateCalif(id, currentExam.getId(), grade);
				
				checked = templateGradeDAO.checkGrade(id, currentExam.getId(), grade);
				if (!checked){
					++retries;
					examLog.error("No se ha podido almacenar correctamente la calificacion del examen "+currentExam.getId()+" del alumno "+id+". Se procede al reintento "+retries);
				}
			} while (!checked);
		}
		
		return new Double(grade);		
	}
	
}
