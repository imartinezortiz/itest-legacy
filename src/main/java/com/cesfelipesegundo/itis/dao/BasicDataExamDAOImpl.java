package com.cesfelipesegundo.itis.dao;


import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cesfelipesegundo.itis.dao.api.BasicDataExamDAO;
import com.cesfelipesegundo.itis.model.BasicDataExam;
import com.cesfelipesegundo.itis.model.ExamGlobalInfo;
import com.cesfelipesegundo.itis.model.Grade;

public class BasicDataExamDAOImpl extends SqlMapClientDaoSupport implements BasicDataExamDAO {
	/**
	 * Constructor
	 *
	 */
	public BasicDataExamDAOImpl() {
		super();
	}
	
	public List<BasicDataExam> getPendingExams(Long id) {
		List<BasicDataExam> list = super.getSqlMapClientTemplate().queryForList("BasicDataExam.getPendingExams", id);
		return list;
	}
	
	public List<BasicDataExam> getExamsForRevision(Long id) {
		List<BasicDataExam> list = super.getSqlMapClientTemplate().queryForList("BasicDataExam.getExamsForRevision", id);
		return list;
	}
	
	public void addNewCalif(Long idlearner, Long idexam, String ipAddress, Long startingDate) {
		Grade grade = new Grade();
		grade.setBegin(new Date(startingDate));
		grade.setEnd(new Date(0));
		grade.setGrade(new Double(0));
		grade.setIdExam(idexam);
		grade.setIdStudent(idlearner);
		grade.setIp(ipAddress);
		grade.setTime(0);
		this.addNewGrade(grade);
	}
	public void updateCalif(Long idLearner, Long idExam, Double finalGrade) {
		Grade grade = new Grade();
		grade.setIdStudent(idLearner);
		grade.setIdExam(idExam);
		grade.setGrade(finalGrade);
		updateGrade(grade);
	}
	public void updateCalif(Long idlearner, Long idexam, Long finalDate, Double finalGrade, Integer duration) {
		Grade grade = new Grade();
		grade.setIdStudent(idlearner);
		grade.setIdExam(idexam);
		grade.setEnd(new Date(finalDate));
		grade.setGrade(finalGrade);
		grade.setTime(duration);
		this.updateGrade(grade);
	}

	/**
	 * Adds a new grade to the database.
	 * @param grade grade to be added.
	 * @return The new key of the grade.
	 */
	private Long addNewGrade(Grade grade) {
		Object newKey = super.getSqlMapClientTemplate().insert("BasicDataExam.addNewGrade", grade);
		return (Long)newKey;
	}
	
	/**
	 * Updates a grade in the database by idStudent and idExam
	 * @param grade grade to be updated.
	 * @return affected rows.
	 */
	private int updateGrade(Grade grade) {
		int rows = getSqlMapClientTemplate().update("BasicDataExam.updateGrade", grade);
		return rows;
	}
	
	public List<BasicDataExam> getNextExams(long userId, long idGroup) {
		HashMap<String,Long> parameters = new HashMap<String,Long>();
		parameters.put("userId", userId);
		parameters.put("idGroup", idGroup);
		List<BasicDataExam> nextExams = (List<BasicDataExam>)super.getSqlMapClientTemplate().queryForList("BasicDataExam.getNextExamsByUser",parameters);
		return nextExams;
	}
	
}
