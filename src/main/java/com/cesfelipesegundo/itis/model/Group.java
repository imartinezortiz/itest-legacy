package com.cesfelipesegundo.itis.model;

/**
 * Clase que representa un grupo de una asignatura
 * @author chema
 *
 */
public class Group {
	
	private Long id;			// Id del grupo
	private String name;		// Nombre o letra del grupo
	private Course course;	    // Asignatura a la que pertenece el grupo
	private String year;		// Academic year
	private Institution institution;
	/**
	 * Type of student.
	 */
	private Integer studentType;	// 1: regular, 2: kid, other (should be 0): generic
	
	
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
		boolean result = o instanceof Group;
		
		if(result){
			Group g = (Group)o;
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
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public Integer getStudentType() {
		if (studentType == null) return new Integer(0);
		return studentType;
	}
	public void setStudentType(Integer studentType) {
		this.studentType = studentType;
	}
	public String getStudentRole() {
		if (studentType == null) return "ANY";
		switch (studentType) {
		case 1: return "LEARNER";
		case 2: return "KID";
		default: return "ANY";
		}
	}

}
