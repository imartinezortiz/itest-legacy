package com.cesfelipesegundo.itis.model;

public class CourseStats {
	
	private int groups;
	private int ss;
	private int ap;
	private int nt;
	private int sb;
	// total de los estudiantes presentados a examen.
	private int numExams;
	// total de los estudiantes de ese grupo
	private int totalStudentByGroup;
	// media de estudiantes presentados por examen
	
	public double getAvgStudents() {
		try{
			double num = (ss+ap+nt+sb);
			return num/((double)numExams);
		}catch(Exception e){
			return 0;
		}
	}
	public int getGroups() {
		return groups;
	}
	public void setGroups(int groups) {
		this.groups = groups;
	}
	public int getSs() {
		return ss;
	}
	public void setSs(int ss) {
		this.ss = ss;
	}
	public int getAp() {
		return ap;
	}
	public void setAp(int ap) {
		this.ap = ap;
	}
	public int getNt() {
		return nt;
	}
	public void setNt(int nt) {
		this.nt = nt;
	}
	public int getSb() {
		return sb;
	}
	public void setSb(int sb) {
		this.sb = sb;
	}
	public int getTotalStudent() {
		return ss+ap+nt+sb;
	}
	
	public double getPercentageSs(){
		try{
			return (ss*100)/(ss+ap+nt+sb);
		}catch(Exception e){
			return 0;
		}
	}
	
	public double getPercentageAp(){
		try{
			return (ap*100)/(ss+ap+nt+sb);
		}catch(Exception e){
			return 0;
		}
	}
	
	public double getPercentageNt(){
		try{
			return (nt*100)/(ss+ap+nt+sb);
		}catch(Exception e){
			return 0;
		}
	}

	public double getPercentageSb(){
		try{
			return (sb*100)/(ss+ap+nt+sb);
		}catch(Exception e){
			return 0;
		}
	}
	public int getNumExams() {
		return numExams;
	}
	public void setNumExams(int numExams) {
		this.numExams = numExams;
	}
	public int getTotalStudentByGroup() {
		return totalStudentByGroup;
	}
	public void setTotalStudentByGroup(int totalStudentByGroup) {
		this.totalStudentByGroup = totalStudentByGroup;
	}
	
	
}
