package com.cesfelipesegundo.itis.dao;


import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cesfelipesegundo.itis.dao.api.ConfigExamDAO;
import com.cesfelipesegundo.itis.model.ConfigExam;
import com.cesfelipesegundo.itis.model.ConfigExamSubject;
import com.cesfelipesegundo.itis.model.ExamQuestion;
import com.cesfelipesegundo.itis.model.Group;
import com.cesfelipesegundo.itis.model.User;

public class ConfigExamDAOImpl extends SqlMapClientDaoSupport implements ConfigExamDAO {
	/**
	 * Constructor
	 *
	 */
	public ConfigExamDAOImpl() {
		super();
	}
	
	
	public void delete(ConfigExam exam) {
		/*int rows =*/ super.getSqlMapClientTemplate().delete("ConfigExam.deleteConfigExam", exam.getId());
	}

	public List<ConfigExam> getGroupConfigExams(Group group, String orderBy) {
		String orderClause = "";
		if(orderBy.equals("title")) orderClause = "ORDER BY examenes.titulo ASC";
		else if(orderBy.equals("min")) orderClause = "ORDER BY examenes.duracion ASC";
		else if(orderBy.equals("sdate")) orderClause = "ORDER BY examenes.fecha_ini ASC";
		else if(orderBy.equals("edate")) orderClause = "ORDER BY examenes.fecha_fin ASC";
		else if(orderBy.equals("revsdate")) orderClause = "ORDER BY examenes.fecha_ini_rev ASC";
		else if(orderBy.equals("revedate")) orderClause = "ORDER BY examenes.fecha_fin_rev ASC";
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("groupId",group.getId());
		parameters.put("orderBy",orderClause);
		List<ConfigExam> exams = super.getSqlMapClientTemplate().queryForList("ConfigExam.selectConfigExamsByGroupId", parameters);
		
		Date fechaActual = new Date(System.currentTimeMillis());
		
		for(ConfigExam exam : exams) {
			// JOSELE: Testing purposes:
			// Subjects associated to the theme:
			List<ConfigExamSubject> exsubjects = super.getSqlMapClientTemplate().queryForList("ConfigExam.selectConfigExamSubjectsByExamId", exam.getId());
			exam.setSubjects(exsubjects);
		}
		return exams;
	}
	
	public List<ConfigExam> getGroupConfigExams(Group group) {
		return this.getGroupConfigExams(group, "title");
	}

	public void updateReview(ConfigExam exam) {
		if(!exam.isActiveReview()){
			exam.setEndDateRevision(new Date(0));
		}
		/*int rows =*/ super.getSqlMapClientTemplate().update("ConfigExam.updateConfigExamReviewFlag", exam);		
	}
	
	public void update(ConfigExam exam) {
		/*int rows =*/ super.getSqlMapClientTemplate().update("ConfigExam.updateConfigExam", exam);
	}

	public void save(ConfigExam exam) {
		Long newKey = (Long)super.getSqlMapClientTemplate().insert("ConfigExam.insertConfigExam", exam);
		exam.setId(newKey);
	}
	
    public void update(ConfigExam exam, ConfigExamSubject examSubject) {
    	examSubject.setCexam(exam);
		/*int rows =*/ super.getSqlMapClientTemplate().update("ConfigExam.updateConfigExamSubject", examSubject);		    	
    }
	
	public void save(ConfigExam exam, ConfigExamSubject examSubject) {
		if(examSubject.getId()!=null) this.update(exam, examSubject);
		else {
			examSubject.setCexam(exam);
			Long newKey = (Long)super.getSqlMapClientTemplate().insert("ConfigExam.insertConfigExamSubject", examSubject);
			examSubject.setId(newKey);
		}
	}
	
	public void delete(ConfigExam exam, ConfigExamSubject subject) {
		/*int rows =*/ super.getSqlMapClientTemplate().delete("ConfigExam.deleteConfigExamSubject", subject.getId());
	}


	public ConfigExam getConfigExamFromId(ConfigExam exfromdb) {
		// Take the config exam using the id
		exfromdb = (ConfigExam) super.getSqlMapClientTemplate().queryForObject("ConfigExam.selectConfigExam", exfromdb.getId());
		// // JOSELE: This is not needed, since the resultmap fills the subjects attribute.
		// Testing purposes:
		// Subjects associated to the theme:
		List<ConfigExamSubject> exsubjects = super.getSqlMapClientTemplate().queryForList("ConfigExam.selectConfigExamSubjectsByExamId", exfromdb.getId());
		exfromdb.setSubjects(exsubjects);
		int questions = 0;
		for(int i=0;i<exfromdb.getSubjects().size();i++){
			questions +=exfromdb.getSubjects().get(i).getQuestionsNumber();
		}
		exfromdb.setQuestionNumber(questions);
		return exfromdb;
	}
}
