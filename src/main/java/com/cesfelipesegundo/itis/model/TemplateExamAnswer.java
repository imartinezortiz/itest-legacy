package com.cesfelipesegundo.itis.model;

import java.util.Collections;
import java.util.List;

/**
 * Representa cada una de las respuestas que van a ser mostradas en cada pregunta de un examen para un alumno
 * @author chema
 *
 */
public class TemplateExamAnswer {
	
	private Long id;					// Id de la respuesta
	private String text;				// Texto de la respuesta
	private List<MediaElem> mmedia;		// Lista de elementos multimedia ordenador por su campo de orden
	private Boolean marked;		// Al iniciar el examen ninguna respuesta está marcada 

	private int active;
	private int solution;
	private int value;
	private TemplateExamQuestion question;
	
	private Boolean usedInExam;
	
	public TemplateExamAnswer(){
		usedInExam = null;
		marked = false;
	}
	
	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
	public int getSolution() {
		return solution;
	}
	public void setSolution(int solution) {
		this.solution = solution;
	}
	public Boolean getMarked() {
		return marked;
	}
	public void setMarked(Boolean marked) {
		this.marked = marked;
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
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public void setQuestion(TemplateExamQuestion question) {
		this.question = question;
	}
	public TemplateExamQuestion getQuestion(){
		return this.question;
	}

	public Boolean getUsedInExam() {
		return usedInExam;
	}

	public void setUsedInExam(Boolean usedInExam) {
		this.usedInExam = usedInExam;
	}
	
	//	 Return the text of the answer splitted into paragraphs
	public String[] getTextParagraphs() {
		if(text!=null)
			return (text.trim()).split("\n");
		else
			return null;
	}

	
}
