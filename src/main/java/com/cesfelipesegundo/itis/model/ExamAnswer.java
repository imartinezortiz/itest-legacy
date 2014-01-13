package com.cesfelipesegundo.itis.model;

import java.util.List;

/**
 * Representa cada una de las respuestas que van a ser mostradas en cada pregunta de un examen para un alumno
 * @author chema
 *
 */
public class ExamAnswer {
	
	private Long id;					// Id de la respuesta
	private String text;				// Texto de la respuesta
	private List<MediaElem> mmedia;		// Lista de elementos multimedia ordenador por su campo de orden
	private Boolean marked = false;		// Al iniciar el examen ninguna respuesta está marcada 

	private int solution;	// Indica si la respuesta es solución o no
	private int value;		// Valor de la respuesta
	
	
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

	//	 Return the text of the answer splitted into paragraphs
	public String[] getTextParagraphs() {
		try{
			return (text.trim()).split("\n");
		}catch(Exception e){
			return null;
		}
	}
	
}
