package com.cesfelipesegundo.itis.model;

/**
 * Class to model an answered question data in an exam
 * @author José Luis Risco Martín
 *
 */
public class AnsweredQuestionData {
		
	/** Answered question identifier */
    private Long id;
    
    /** Exam where this question appears */
	private Long idexam;
	
	/** Alum associated to the exam */
	private Long idalu;

    /** Title of the question */
    private String title;
    
    /** Text of the statement of the question */
    private String text;
    
    /** The type of question (test or fill question)*/
    private int questionType;
    	
    /** Text of the subject of the question */
    private String subject;

    // Chema added this field
    /**  comment of the question, needed for questions with mmedia file and no text */
    private String comment;	
    
    /** Sum of the grades of the answers associated to the question in the exam associated */
    // ALBERTO: not necessary with attributes answered and answeredWithSuccess;
    private double answersGradeSum;
    
    /** maxGradePerQuestion from the exam (idexam, idalu) where the question appears */
    // JOSELE: changed from int to double
    //  ALBERTO: not necessary with attributes answered and answeredWithSuccess;
    private double maxGradePerQuestion;
    
    /** indicates if the question has been answered or not  */
    private boolean answered;
    
    /** indicates if the question has been successfully answered or not  */
    private boolean answeredWithSuccess;
    
    /** indicates if the learner marked the confidence level*/
    private boolean confidenceLevelMarked;
    
    /** indicates if the confidence level is active for the question in this exam*/
    private boolean confidenceLevelActive;

    
   
	public int getQuestionType() {
		return questionType;
	}

	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}

	public boolean isConfidenceLevelActive() {
		return confidenceLevelActive;
	}

	public void setConfidenceLevelActive(boolean confidenceLevelActive) {
		this.confidenceLevelActive = confidenceLevelActive;
	}

	public boolean isConfidenceLevelMarked() {
		return confidenceLevelMarked;
	}

	public void setConfidenceLevelMarked(boolean confidenceLevelMarked) {
		this.confidenceLevelMarked = confidenceLevelMarked;
	}

	public boolean isAnswered() {
		return answered;
	}

	public void setAnswered(boolean answered) {
		this.answered = answered;
	}

	public double getAnswersGradeSum() {
		return answersGradeSum;
	}

	public void setAnswersGradeSum(double answersGradeSum) {
		this.answersGradeSum = answersGradeSum;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdexam() {
		return idexam;
	}

	public void setIdexam(Long idexam) {
		this.idexam = idexam;
	}

	
	public double getMaxGradePerQuestion() {
		return maxGradePerQuestion;
	}

	public void setMaxGradePerQuestion(double maxGradePerQuestion) {
		this.maxGradePerQuestion = maxGradePerQuestion;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getIdalu() {
		return idalu;
	}

	public void setIdalu(Long idalu) {
		this.idalu = idalu;
	}

	public boolean isAnsweredWithSuccess() {
		return answeredWithSuccess;
	}

	public void setAnsweredWithSuccess(boolean answeredWithSuccess) {
		this.answeredWithSuccess = answeredWithSuccess;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}