package com.cesfelipesegundo.itis.model;

public class CustomExamUser extends User{
	
	private boolean inExam;

	public boolean isInExam() {
		return inExam;
	}

	public void setInExam(boolean inExam) {
		this.inExam = inExam;
	}
	
	
}
