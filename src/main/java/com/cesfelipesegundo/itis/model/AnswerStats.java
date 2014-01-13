package com.cesfelipesegundo.itis.model;

public class AnswerStats {

	private Long idQuestion;
	private Long idResp;
	private String respText;
	private int appearances;
	private float percentage;
	private int markedNumber;
	private int solution;
	private int tipoPreg;
	
	public int getTipoPreg() {
		return tipoPreg;
	}
	public void setTipoPreg(int tipoPreg) {
		this.tipoPreg = tipoPreg;
	}
	public Long getIdQuestion() {
		return idQuestion;
	}
	public void setIdQuestion(Long idQuestion) {
		this.idQuestion = idQuestion;
	}
	public Long getIdResp() {
		return idResp;
	}
	public void setIdResp(Long idResp) {
		this.idResp = idResp;
	}
	public String getRespText() {
		return respText;
	}
	public void setRespText(String respText) {
		this.respText = respText;
	}
	public int getAppearances() {
		return appearances;
	}
	public void setAppearances(int appearances) {
		this.appearances = appearances;
	}
	public float getPercentage() {
		return percentage;
	}
	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}
	public int getMarkedNumber() {
		return markedNumber;
	}
	public void setMarkedNumber(int markedNumber) {
		this.markedNumber = markedNumber;
	}
	public int getSolution() {
		return solution;
	}
	public void setSolution(int solution) {
		this.solution = solution;
	}
	
	
}
