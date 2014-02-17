package com.cesfelipesegundo.itis.dao;


import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cesfelipesegundo.itis.dao.api.ConfigExamDAO;
import com.cesfelipesegundo.itis.model.User;

import es.itest.engine.course.business.entity.Group;
import es.itest.engine.course.business.entity.TestSessionTemplateSubject;
import es.itest.engine.test.business.entity.ItemSession;
import es.itest.engine.test.business.entity.TestSessionTemplate;

public class ConfigExamDAOImpl extends SqlMapClientDaoSupport implements ConfigExamDAO {
	/**
	 * Constructor
	 *
	 */
	public ConfigExamDAOImpl() {
		super();
	}
	
	
	public void delete(TestSessionTemplate exam) {
		/*int rows =*/ super.getSqlMapClientTemplate().delete("ConfigExam.deleteConfigExam", exam.getId());
	}

	public List<TestSessionTemplate> getGroupConfigExams(Group group, String orderBy) {
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
		List<TestSessionTemplate> exams = super.getSqlMapClientTemplate().queryForList("ConfigExam.selectConfigExamsByGroupId", parameters);
		
		Date fechaActual = new Date(System.currentTimeMillis());
		
		for(TestSessionTemplate exam : exams) {
			// JOSELE: Testing purposes:
			// Subjects associated to the theme:
			List<TestSessionTemplateSubject> exsubjects = super.getSqlMapClientTemplate().queryForList("ConfigExam.selectConfigExamSubjectsByExamId", exam.getId());
			exam.setSubjects(exsubjects);
		}
		return exams;
	}
	
	public List<TestSessionTemplate> getGroupConfigExams(Group group) {
		return this.getGroupConfigExams(group, "title");
	}

	public void updateReview(TestSessionTemplate exam) {
		if(!exam.isActiveReview()){
			exam.setEndDateRevision(new Date(0));
		}
		/*int rows =*/ super.getSqlMapClientTemplate().update("ConfigExam.updateConfigExamReviewFlag", exam);		
	}
	
	public void update(TestSessionTemplate exam) {
		/*int rows =*/ super.getSqlMapClientTemplate().update("ConfigExam.updateConfigExam", exam);
	}

	public void save(TestSessionTemplate exam) {
		Long newKey = (Long)super.getSqlMapClientTemplate().insert("ConfigExam.insertConfigExam", exam);
		exam.setId(newKey);
	}
	
    public void update(TestSessionTemplate exam, TestSessionTemplateSubject examSubject) {
    	examSubject.setCexam(exam);
		/*int rows =*/ super.getSqlMapClientTemplate().update("ConfigExam.updateConfigExamSubject", examSubject);		    	
    }
	
	public void save(TestSessionTemplate exam, TestSessionTemplateSubject examSubject) {
		if(examSubject.getId()!=null) this.update(exam, examSubject);
		else {
			examSubject.setCexam(exam);
			Long newKey = (Long)super.getSqlMapClientTemplate().insert("ConfigExam.insertConfigExamSubject", examSubject);
			examSubject.setId(newKey);
		}
	}
	
	public void delete(TestSessionTemplate exam, TestSessionTemplateSubject subject) {
		/*int rows =*/ super.getSqlMapClientTemplate().delete("ConfigExam.deleteConfigExamSubject", subject.getId());
	}


	public TestSessionTemplate getTestSessionTemplate(long testSessionTemplateId) {
		// Take the config exam using the id
		TestSessionTemplate testSessionTemplate = (TestSessionTemplate) super.getSqlMapClientTemplate().queryForObject("ConfigExam.selectConfigExam", testSessionTemplateId);
		// // JOSELE: This is not needed, since the resultmap fills the subjects attribute.
		// Testing purposes:
		// Subjects associated to the theme:
		List<TestSessionTemplateSubject> exsubjects = super.getSqlMapClientTemplate().queryForList("ConfigExam.selectConfigExamSubjectsByExamId", testSessionTemplateId);
		testSessionTemplate.setSubjects(exsubjects);
		int questions = 0;
		for(int i=0;i<testSessionTemplate.getSubjects().size();i++){
			questions +=testSessionTemplate.getSubjects().get(i).getQuestionsNumber();
		}
		testSessionTemplate.setQuestionNumber(questions);
		return testSessionTemplate;
	}
}
