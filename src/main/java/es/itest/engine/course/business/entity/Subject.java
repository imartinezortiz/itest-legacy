package es.itest.engine.course.business.entity;

/**
 * Class that models the themes related to a course and its questions
 * Different from the subjects for the exams (see TemplateExamSubject)
 * @author J.M. Colmenar
 *
 */
public class Subject {
	private Long id;
	private String subject;			// Title of the theme
	private Integer order;				// Order to be sorted
	// The following attribute is not stored into the database
	private Short usedInExam;		// Flag to avoid deleting themes used in exam
	// Group
//	private Group group;			// Group related to this subject
	// Questions of this subject
	private Long numQuestions;		
	
	public Short getUsedInExam() {
		return usedInExam;
	}
	public void setUsedInExam(Short usedInExamQuestion) {
		this.usedInExam = usedInExamQuestion;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	public Long getNumQuestions() {
		return numQuestions;
	}
	public void setNumQuestions(Long numQuestions) {
		this.numQuestions = numQuestions;
	}
}
