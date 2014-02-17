package es.itest.engine.test.business.entity;

import java.util.Date;

import com.cesfelipesegundo.itis.model.User;

/**
 * Class to model the student grades using objects instead of identifiers.
 * @author Chema
 *
 */
public class TestSessionGrade {
	/** Grade identifier */
    private Long id;
    
    /** Student */
    private User learner;
    
    /** Exam */
    private TestSessionTemplate exam;
    
    /** ip from where the exam was solved. */
    private String ip;
    
    /** grade. */
    private Double grade;
    
    /** Starting date of the exam. */
    private Date begin;
    
    /** Ending date of the exam. */
    private Date end;
    
    /** Time spent to solve the exam. */
    private Integer time;
    
	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Double getGrade() {
		return grade;
	}

	public void setGrade(Double grade) {
		this.grade = grade;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getTime() {
		return time;
		//return new Integer((int)((end.getTime() - begin.getTime())/1000));
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public TestSessionTemplate getExam() {
		return exam;
	}

	public void setExam(TestSessionTemplate exam) {
		this.exam = exam;
	}

	public User getLearner() {
		return learner;
	}

	public void setLearner(User learner) {
		this.learner = learner;
	}
	
	public Date getDuration() {
		return new Date(end.getTime() - begin.getTime());
	}
}