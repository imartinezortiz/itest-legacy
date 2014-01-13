package com.cesfelipesegundo.itis.model;

import java.util.Collections;
import java.util.List;




/**
 * Representa cada una de las preguntas que van a ser mostradas en un examen para un alumno
 * @author chema
 *
 */
public class TemplateExamQuestion {

	private Long id;		// Id de la pregunta
	/** Title of the question */
	private String title;
	private String text;	// Enunciado
	private List<MediaElem> mmedia;	// Lista de elementos multimedia ordenador por su campo de orden
	private List<TemplateExamAnswer> answers;	// Lista de respuestas
	private List<MediaElem> mmediaComment;
	private int numCorrectAnswers;
	
	/**
	 * Grupo al que está asociada la pregunta
	 */
	private Group group;
	private Subject subject;	// Related theme	
	private int difficulty;
	private int visibility;
	private int active;
	private String comment;
	private int type;
	private Boolean usedInExam;
	
	public TemplateExamQuestion(){
		
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	public int getVisibility() {
		return visibility;
	}
	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}
	public List<TemplateExamAnswer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<TemplateExamAnswer> answers) {
		this.answers = answers;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<MediaElem> getMmedia() {
		// Orders the list:
		if (mmedia != null) Collections.sort(mmedia,new MediaElemComparator());
		return mmedia;
	}
	public void setMmedia(List<MediaElem> mmedia) {
		this.mmedia = mmedia;
	}
	
	public List<MediaElem> getMmediaComment() {
		if (mmediaComment != null) Collections.sort(mmediaComment,new MediaElemComparator());
		return mmediaComment;
	}

	public void setMmediaComment(List<MediaElem> mmediaComment) {
		this.mmediaComment = mmediaComment;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	public Subject getSubject() {
		return subject;
	}
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	public int getNumCorrectAnswers() {
		return numCorrectAnswers;
	}
	public void setNumCorrectAnswers(int numCorrectAnswers) {
		this.numCorrectAnswers = numCorrectAnswers;
	}

	public Boolean getUsedInExam() {
		return usedInExam;
	}

	public void setUsedInExam(Boolean usedInExam) {
		this.usedInExam = usedInExam;
	}
	
	//	 Return the text of the question splitted into paragraphs
	public String[] getTextParagraphs() {
		if(text!=null)
			return (text.trim()).split("\n");
		else
			return null;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	//	 Return the text of the question comment splitted into paragraphs
	public String[] getCommentParagraphs() {
		if(comment!=null)
			return (comment.trim()).split("\n");
		else
			return null;
	}
	
	public int getSize(){
		if(mmedia!=null)
			return mmedia.size();
		else
			return 0;
	}
	
	public int getCommentSize(){
		if(mmediaComment!=null)
			return mmediaComment.size();
		else
			return 0;
	}
}
