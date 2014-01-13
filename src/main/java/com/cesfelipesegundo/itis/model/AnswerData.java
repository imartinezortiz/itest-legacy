package com.cesfelipesegundo.itis.model;

public class AnswerData {

	private Long id;
	private Long questionId;
	private String text;
	//preguntas tipo test
	private int marcada;
	//preguntas a rellenar
	private String resp;
	private int tipoPreg;
	private int solution;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Long getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}
	public int getMarcada() {
		return marcada;
	}
	public void setMarcada(int marcada) {
		this.marcada = marcada;
	}
	public String getResp() {
		return resp;
	}
	public void setResp(String resp) {
		this.resp = resp;
	}
	public int getTipoPreg() {
		return tipoPreg;
	}
	public void setTipoPreg(int tipoPreg) {
		this.tipoPreg = tipoPreg;
	}
	public int getSolution() {
		return solution;
	}
	public void setSolution(int solution) {
		this.solution = solution;
	}
	
	
	
}
