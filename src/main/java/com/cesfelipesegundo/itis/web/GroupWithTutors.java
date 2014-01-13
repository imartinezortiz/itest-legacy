package com.cesfelipesegundo.itis.web;


import java.util.List;

import com.cesfelipesegundo.itis.model.Course;
import com.cesfelipesegundo.itis.model.Institution;
import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.model.Group;

/**
 * Clase que representa un grupo de una asignatura
 * @author Gonzalo
 *
 */
public class GroupWithTutors {
	
	private Long id;			// Id del grupo
	private String name;		// Nombre o letra del grupo
	private Course course;	    // Asignatura a la que pertenece el grupo
	private String year;		// Academic year
	private Institution institution;	// Institution
	private List<User> tutorList;	// List of the tutors related to this group;
	
	/**
	 * Method to create a GroupWithTutors from a Group
	 * @param toClone Group with the data of the new GroupWithTutors
	 */
	public GroupWithTutors(Group toClone){
		this.id = toClone.getId();
		this.name = toClone.getName();
		this.course =toClone.getCourse();
		this.year = toClone.getYear();
		this.institution = toClone.getInstitution();
	}
	
	
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public boolean equals(Object o){
		boolean result = o instanceof GroupWithTutors;
		
		if(result){
			GroupWithTutors g = (GroupWithTutors)o;
			result = g.id.equals(id);
		}
		
		return result;
	}
	
	public int hashCode(){
		int result = 23;
		
		if(id != null ){
			result += 32*id.hashCode();
		}
		
		if(name != null){
			result += 94*name.hashCode();
		}
		
		if(course != null){
			result += 78*course.hashCode();
		}
		
		return result;
	}
	
	public String toString(){
		return "Group: "+name+" id: "+id;
	}
	public Institution getInstitution() {
		return institution;
	}
	public void setInstitution(Institution institution) {
		this.institution = institution;
	}
	

	public List<User> getTutorList() {
		return tutorList;
	}
	public void setTutorList(List<User> tL) {
		this.tutorList = tL;
	}
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}

}
