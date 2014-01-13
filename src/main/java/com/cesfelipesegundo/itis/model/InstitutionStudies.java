package com.cesfelipesegundo.itis.model;

import java.util.ArrayList;
import java.util.List;

public class InstitutionStudies {

	private List<String> studies;
	
	public InstitutionStudies(){
		studies = new ArrayList<String>();
	}
	public void addStudy(String study){
		studies.add(study);
	}
	public List<String> getStudies() {
		return studies;
	}
	public void setStudies(List<String> studies) {
		this.studies = studies;
	}
	public boolean isInfantil(){
		for(int i=0;i<studies.size();i++){
			if(studies.get(i).equalsIgnoreCase("Infantil")){
				return true;
			}
		}
		return false;
	}
	public boolean isPrimary(){
		for(int i=0;i<studies.size();i++){
			if(studies.get(i).equalsIgnoreCase("Primaria")){
				return true;
			}
		}
		return false;
	}
	public boolean isSecundary(){
		for(int i=0;i<studies.size();i++){
			if(studies.get(i).equalsIgnoreCase("Secundaria")){
				return true;
			}
		}
		return false;
	}
	public boolean isVocationalTraining(){
		for(int i=0;i<studies.size();i++){
			if(studies.get(i).equalsIgnoreCase("Ciclo Formativo")){
				return true;
			}
		}
		return false;
	}
	public boolean isHightSchool(){
		for(int i=0;i<studies.size();i++){
			if(studies.get(i).equalsIgnoreCase("Bachillerato")){
				return true;
			}
		}
		return false;
	}
	public boolean isUniversity(){
		for(int i=0;i<studies.size();i++){
			if(studies.get(i).equalsIgnoreCase("Universidad")){
				return true;
			}
		}
		return false;
	}
	
}
