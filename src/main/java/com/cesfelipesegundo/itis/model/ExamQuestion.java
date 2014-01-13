package com.cesfelipesegundo.itis.model;

import java.util.List;

/**
 * Representa cada una de las preguntas que van a ser mostradas en un examen para un alumno
 * @author chema
 *
 */
public class ExamQuestion {

	private Long id;		// Id de la pregunta
	private String title;	// Question title
	private String text;	// Enunciado
	private Long difficulty;	// Difficulty of the question
	private String comment;		// Comment
	private List<MediaElem> mmedia;	// Lista de elementos multimedia ordenador por su campo de orden
	private List<ExamAnswer> answers;	// Lista de respuestas
	private List<MediaElem> mmediaComment;
	private double questionGrade; // Obtained grade for the question
	private int numCorrectAnswers;
	private boolean marked; //el nivel de confianza
	private int type;
	private String learnerFillAnswer;
	
	
	public String getLearnerFillAnswer() {
		return learnerFillAnswer;
	}
	public void setLearnerFillAnswer(String learnerFillAnswer) {
		this.learnerFillAnswer = learnerFillAnswer;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getNumCorrectAnswers() {
		return numCorrectAnswers;
	}
	public void setNumCorrectAnswers(int numCorrectAnswers) {
		this.numCorrectAnswers = numCorrectAnswers;
	}
	public List<ExamAnswer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<ExamAnswer> answers) {
		this.answers = answers;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<MediaElem> getMmedia() {
		return mmedia;
	}
	public void setMmedia(List<MediaElem> mmedia) {
		this.mmedia = mmedia;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Long getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(Long difficulty) {
		this.difficulty = difficulty;
	}
	public double getQuestionGrade() {
		return questionGrade;
	}
	public void setQuestionGrade(double questionGrade) {
		this.questionGrade = questionGrade;
	}

	//	 Return the text of the question splitted into paragraphs
	public String[] getTextParagraphs() {
		try{
			return (text.trim()).split("\n");
		}catch(Exception e){
			return null;
		}
	}
	
	//	 Return the text of the question comment splitted into paragraphs
	public String[] getCommentParagraphs() {
		try{
			return (comment.trim()).split("\n");
		}catch(Exception e){
			return null;
		}
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<MediaElem> getMmediaComment() {
		return mmediaComment;
	}
	public void setMmediaComment(List<MediaElem> mmediaComment) {
		this.mmediaComment = mmediaComment;
	}
	public boolean isMarked() {
		return marked;
	}
	public void setMarked(boolean marked) {
		this.marked = marked;
	}
	public int getCorrectAnswers(){
		int numAnswers = 0;
		for(int i=0;i<answers.size();i++){
			ExamAnswer ea = this.answers.get(i);
			if(ea.getSolution()==1){
				numAnswers++;
			}
		}
		return numAnswers;
	}
	public int getMarkedAnswer(){
		int marked = 0;
		for(int i=0;i<answers.size();i++){
			ExamAnswer ea = answers.get(i);
			if(ea.getMarked()){
				marked++;
			}
		}
		return marked;
	}
	
	
}
