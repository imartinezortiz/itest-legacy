package com.cesfelipesegundo.itis.model;

import java.util.Date;

/**
 * Class to model an answer made by a student in order to solve an exam.
 * @author José Luis Risco Martín
 *
 */
public class AnswerExam {
	public static Byte MARKED = 1;
	public static Byte NOT_MARKED = 0;
	
	/** Answer identifier. */
    private Long id;

    /** Exam identifier. */
    private Long idExam;

    /** Student identifier. */
    private Long idStudent;

    /** Question identifier. */
    private Long idQuestion;
    
    /** Answer (from BD) identifier. */
    private Long idAnswer;

    /** If the answer has been marked or not. */
    private Byte marked;

    /** Grade of the answer. */
    private Double grade;

    /** Time in which the answer was sent. */
    private Date answerTime;

	public Date getAnswerTime() {
		return answerTime;
	}

	public void setAnswerTime(Date answerTime) {
		this.answerTime = answerTime;
	}

	public Double getGrade() {
		return grade;
	}

	public void setGrade(Double grade) {
		this.grade = grade;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdAnswer() {
		return idAnswer;
	}

	public void setIdAnswer(Long idAnswer) {
		this.idAnswer = idAnswer;
	}

	public Long getIdExam() {
		return idExam;
	}

	public void setIdExam(Long idExam) {
		this.idExam = idExam;
	}

	public Long getIdQuestion() {
		return idQuestion;
	}

	public void setIdQuestion(Long idQuestion) {
		this.idQuestion = idQuestion;
	}

	public Long getIdStudent() {
		return idStudent;
	}

	public void setIdStudent(Long idStudent) {
		this.idStudent = idStudent;
	}

	public Byte getMarked() {
		return marked;
	}

	public void setMarked(Byte marked) {
		this.marked = marked;
	}
	
	public boolean iguales(AnswerExam ob){
		if(this.idExam!=ob.idExam)
			return false;
		if(this.idStudent!=ob.idStudent)
			return false;
		if(this.idQuestion!=ob.idQuestion)
			return false;
		if(this.idAnswer!=ob.idAnswer)
			return false;
		if(this.grade!=ob.grade)
			return false;
		if(this.answerTime!=ob.answerTime)
			return false;
		if(this.marked!=ob.marked)
			return false;
		
		return true;
	}


}