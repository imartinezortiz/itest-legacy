package com.cesfelipesegundo.itis.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cesfelipesegundo.itis.dao.api.SubjectDAO;
import com.cesfelipesegundo.itis.model.ConfigExam;
import com.cesfelipesegundo.itis.model.ConfigExamSubject;
import com.cesfelipesegundo.itis.model.Group;
import com.cesfelipesegundo.itis.model.Subject;
import com.cesfelipesegundo.itis.model.TemplateExamQuestion;


public class SubjectDAOImpl extends SqlMapClientDaoSupport implements SubjectDAO{

	/**
	 * Constructor
	 *
	 */
	public SubjectDAOImpl() {
		super();
	}
	
	public Subject getSubject(Long id) {
		Subject sbj = new Subject();
		sbj = (Subject)super.getSqlMapClientTemplate().queryForObject("Subject.getSubject", id);
		return sbj;
	}

	public void deleteSubject(Subject theme) {
		super.getSqlMapClientTemplate().delete("Subject.deleteSubject", theme);
	}

	public Subject saveSubject(Subject theme) {
		if(theme.getId() == null) {
			// New theme has to be created.
			Long newKey = (Long)super.getSqlMapClientTemplate().insert("Subject.insertSubject", theme);
			theme.setId(newKey);
		} else {
			// Current theme data have to be updated.
			super.getSqlMapClientTemplate().update("Subject.updateSubject", theme);
		}
		return theme;
	}

	public List<Subject> getSubjectsMinInfo(Group g) {
		//	Theme list
		List<Subject> subjects = new ArrayList<Subject>();
		subjects = super.getSqlMapClientTemplate().queryForList("Subject.getSubjectsByGroupId", g.getId());
		// Number of questions for each theme:
		for(Subject sbj : subjects) {
			Long numQ = (Long) super.getSqlMapClientTemplate().queryForObject("Subject.getNumQuestionsBySubjectId", sbj.getId());
			sbj.setNumQuestions(numQ);			
		}	
		return subjects;
	}
	
	public List<Subject> getSubjectsOrderedAsc(Long groupId){
		List<Subject> subjects;
		subjects = super.getSqlMapClientTemplate().queryForList("Subject.getSubjectsByGroupIdOrderByOrdenAsc",groupId);
		return subjects;
	}
	
	public List<TemplateExamQuestion> getQuestionsBySubjectAndGroupId(Long subjectId,Long groupId){
		List<TemplateExamQuestion> questions;
		//Declaring a map containing parameters for query
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("subjectId",subjectId);
		parameters.put("groupId",groupId);
		
		questions = super.getSqlMapClientTemplate().queryForList("Subject.getQuestionsBySubjectAndGroupId",parameters);
		return questions;
	}
	
	public long getQuestionsNumber(Long subjectId, Long questionType, Long groupId, Long difficulty) {
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("subjectId",subjectId);
		if(questionType!=null)
			parameters.put("questionType", questionType);
		parameters.put("groupId",groupId);
		parameters.put("difficulty", difficulty);
		return ((Long)super.getSqlMapClientTemplate().queryForObject("Subject.getQuestionsNumber", parameters));
	}
	
	public long getAnswerMinNumber(Long subjectId, Long questionType, Long groupId, Long difficulty) {
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("subjectId",subjectId);
		parameters.put("groupId",groupId);
		parameters.put("difficulty", difficulty);
		if(questionType!=null)
			parameters.put("questionType", questionType);
		Long answers = ((Long)super.getSqlMapClientTemplate().queryForObject("Subject.getAnswerMinNumber", parameters));
		if(answers == null){
			return 0;
		}
		return answers;
	}

	public Long getTotalQuestion(Long idTheme, Long questionType, Long idGroup, Long difficulty) {
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("subjectId",idTheme);
		parameters.put("groupId",idGroup);
		parameters.put("difficulty", difficulty);
		if(questionType!=null)
			parameters.put("questionType", questionType);
		Long questions = ((Long)super.getSqlMapClientTemplate().queryForObject("Subject.getTotalQuestionLow", parameters));
		if(questions == null){
			return new Long(0);
		}
		return questions;
	}

	public boolean isThemeRepeat(String themeText, long groupId) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("tema", themeText);
		map.put("grupo", groupId);
		return (Boolean) super.getSqlMapClientTemplate().queryForObject("Subject.isThemeRepeat", map);
	}
	
	public Subject getSubjectByQuestionId(Long id){
		return (Subject) super.getSqlMapClientTemplate().queryForObject("Subject.getSubjectByQuestionId",id);
	}
}
