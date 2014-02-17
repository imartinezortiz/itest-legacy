package com.cesfelipesegundo.itis.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cesfelipesegundo.itis.dao.api.TemplateGradeDAO;
import com.cesfelipesegundo.itis.model.Grade;
import com.cesfelipesegundo.itis.model.QueryGrade;
import com.cesfelipesegundo.itis.model.User;

import es.itest.engine.course.business.entity.TestSessionTemplateSubject;
import es.itest.engine.test.business.entity.TestSession;
import es.itest.engine.test.business.entity.TestSessionGrade;
import es.itest.engine.test.business.entity.TestSessionTemplate;

public class TemplateGradeDAOImpl extends SqlMapClientDaoSupport implements TemplateGradeDAO {

	public List<TestSessionGrade> find(QueryGrade query) {
		
		// This map will be the criteria for the SQL
		Map<String,Object> map = new HashMap<String,Object>();
		if(query.getTypeOrder() == null){
			query.setTypeOrder("ASC");
		}
		map.put("begin", query.getBegin());
		map.put("end", query.getEnd());
		map.put("exam", query.getExam());
		map.put("grade", query.getGrade());
		map.put("group", query.getGroup());
		map.put("learner", query.getLearner());
		
		// The order field requires a special coding.
		QueryGrade.OrderBy order = query.getOrder();
		if(order!=null) {
			if(order.equals(QueryGrade.OrderBy.ENDDATE)) map.put("preferredOrder", "califs.fecha_fin "+query.getTypeOrder());
			else if(order.equals(QueryGrade.OrderBy.EXAMTITLE)) map.put("preferredOrder", "examenes.titulo "+query.getTypeOrder());
			else if(order.equals(QueryGrade.OrderBy.GRADE)) map.put("preferredOrder", "califs.nota "+query.getTypeOrder());
			else if(order.equals(QueryGrade.OrderBy.STARTDATE)) map.put("preferredOrder", "califs.fecha_ini "+query.getTypeOrder());
			else if(order.equals(QueryGrade.OrderBy.STUDENT)) map.put("preferredOrder", "usuarios.apes "+query.getTypeOrder());
			else if(order.equals(QueryGrade.OrderBy.TIME)) map.put("preferredOrder", "diferencia "+query.getTypeOrder());
			else if(order.equals(QueryGrade.OrderBy.MAXGRADE)) map.put("preferredOrder", "examenes.nota_max "+query.getTypeOrder());
		}
		map.put("time", query.getTime());

		// Pages: 
		// I guess firstResult is always given.
		// If maxResultCount <= 0 LIMIT (MySQL statement) is not applied.  
		map.put("firstResult", Integer.valueOf(query.getFirstResult()));
		if ((query.getMaxResultCount() != null) && (query.getMaxResultCount()>0)) 
			map.put("maxResultCount", Integer.valueOf(query.getMaxResultCount()));
		
		List<TestSessionGrade> grades = super.getSqlMapClientTemplate().queryForList("Grade.selectTemplateGradesByCriteria", map);
		
		// Fill the User and ConfigExam objects
		for(TestSessionGrade grade : grades) {
			User user = (User)super.getSqlMapClientTemplate().queryForObject("User.getUserById", grade.getLearner().getId());
			grade.setLearner(user);
			
			TestSessionTemplate configExam = (TestSessionTemplate)super.getSqlMapClientTemplate().queryForObject("ConfigExam.selectConfigExam", grade.getExam().getId());
			// Subjects associated to the theme:
			List<TestSessionTemplateSubject> subjects = super.getSqlMapClientTemplate().queryForList("ConfigExam.selectConfigExamSubjectsByExamId", configExam.getId());
			configExam.setSubjects(subjects);
			grade.setExam(configExam);
		}
		
		return grades;
	}
	
	public boolean checkGrade(Long idLearner,Long idExam,Double finalGrade){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("idLearner", idLearner);
		map.put("idExam", idExam);
		map.put("finalGrade", finalGrade);
		
		List<TestSessionGrade> list = super.getSqlMapClientTemplate().queryForList("Grade.checkGrade",map);
		return list != null;
	}

	public Grade getGradeByIdExam(Long idexam, Long iduser) {
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("examen", idexam);
		map.put("alumno", iduser);
		return (Grade) super.getSqlMapClientTemplate().queryForObject("Grade.selectGradeByExamId",map);
	}
	
	public List<Grade> getAlreadyDoneGradeByGroup(long iduser, long idgroup){
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("user", iduser);
		map.put("group", idgroup);
		return (List<Grade>) super.getSqlMapClientTemplate().queryForList("Grade.selectGradeByGroupAndUser",map);
	}

	public List<Grade> getGradesByUser(String userName) {
		return (List<Grade>) super.getSqlMapClientTemplate().queryForList("Grade.getGradesByUser",userName);
	}
}
