package com.cesfelipesegundo.itis.model;

import java.util.Date;

/**
 * Allows the implementation of queries related to student grades
 * @author chema
 *
 */
public class QueryGrade {

	public enum OrderBy { STUDENT, EXAMTITLE, GRADE, STARTDATE, ENDDATE, TIME, MAXGRADE };

	/**
	 * Group
	 * <p>If <code>null</code> do not consider in the query.</p>
	 */
	private Long group;
	
	/**
	 * Student
	 * <p>If <code>null</code> do not consider in the query.</p>
	 */
	private Long learner;
	
	/**
	 * Exam configuration
	 * <p>If <code>null</code> do not consider in the query.</p> 
	 */
	private Long exam;
	

	/**
	 * Grade
	 * <p>If <code>null</code> do not consider in the query.</p> 
	 */
	private Double grade;
	
    /** 
     * Starting date of the exam
     * <p>If <code>null</code> do not consider in the query.</p>
     */
    private Date begin;
    
    /**
     * Ending date of the exam
     * <p>If <code>null</code> do not consider in the query.</p>
     */
    private Date end;
    
    /**
     * Time spent to solve the exam.
     * <p>If <code>null</code> do not consider in the query.</p>
     */
    private Integer time;
	
	
	/**
	 * Row position of the first result to be shown.
	 */
	private int firstResult;
	
	/**
	 * Max number of rows to be shown, starting at @see #firstResult 
	 */
	private Integer maxResultCount;
	
	/**
	 * Results order
	 */
	private OrderBy order;
	
	/**
	 * ASC or DESC
	 * */
	private String typeOrder;
	
	public QueryGrade(){
		order = OrderBy.STUDENT;
		firstResult = 0;
		maxResultCount = null;
	}

	public Long getExam() {
		return exam;
	}

	public void setExam(Long exam) {
		this.exam = exam;
	}

	public Long getGroup() {
		return group;
	}

	public void setGroup(Long group) {
		this.group = group;
	}

	public Long getLearner() {
		return learner;
	}

	public void setLearner(Long learner) {
		this.learner = learner;
	}

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


	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public int getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	public Integer getMaxResultCount() {
		return maxResultCount;
	}

	public void setMaxResultCount(Integer maxResultCount) {
		this.maxResultCount = maxResultCount;
	}

	public OrderBy getOrder() {
		return order;
	}

	public void setOrder(OrderBy order) {
		this.order = order;
	}

	public String getTypeOrder() {
		return typeOrder;
	}

	public void setTypeOrder(String typeOrder) {
		this.typeOrder = typeOrder;
	}

		
}
