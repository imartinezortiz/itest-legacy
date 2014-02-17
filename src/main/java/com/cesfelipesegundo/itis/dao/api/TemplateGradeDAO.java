package com.cesfelipesegundo.itis.dao.api;

import java.util.List;

import com.cesfelipesegundo.itis.model.Grade;
import com.cesfelipesegundo.itis.model.QueryGrade;

import es.itest.engine.test.business.entity.TestSessionGrade;
import es.itest.engine.test.business.entity.TestSessionTemplate;

public interface TemplateGradeDAO {

	/**
	 * 
	 * Find grades usring a criteria.
	 * 
	 * @param query
	 * @return
	 */
	List<TestSessionGrade> find(QueryGrade query);
	
	/** Checks grade persistence for an exam and learner given
	 * 
	 * @param idLearner Learner who did the exam
	 * @param idExam Exam graded
	 * @param finalGrade Grade to check
	 * @return true if there was correct persistence
	 */
	public boolean checkGrade(Long idLearner,Long idExam,Double finalGrade);
	
	/**
	 * Return the grade of the current exam given
	 * @idexam Exam's id
	 * @return the grade of the exam
	 * */
	public Grade getGradeByIdExam(Long idexam, Long iduser);
	
	/**
	 * Return a list of grades to this user for this group
	 * @iduser  User's id
	 * @idgroup Group's id
	 * @return the grade list of the user to this group
	 * */
	public List<Grade>getAlreadyDoneGradeByGroup(long iduser, long idgroup);

	/**
	 * Return all of grades from the current user
	 * @param userName The user's name
	 * @return all of grades from the current user
	 * */
	public List<Grade> getGradesByUser(String userName);
}
