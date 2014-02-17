package com.cesfelipesegundo.itis.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cesfelipesegundo.itis.dao.api.InstitutionDAO;
import com.cesfelipesegundo.itis.model.Institution;
import com.cesfelipesegundo.itis.model.InstitutionStats;
import com.cesfelipesegundo.itis.model.InstitutionStudies;

import es.itest.engine.course.business.entity.Subject;



public class InstitutionDAOImpl extends SqlMapClientDaoSupport implements InstitutionDAO{

	/**
	 * Constructor
	 *
	 */
	public InstitutionDAOImpl() {
		super();
	}
	
	public List<Institution> getInstitutions()
	{
		List<Institution> institutions = new ArrayList<Institution>();
		
		institutions = super.getSqlMapClientTemplate().queryForList("Institution.getInstitutions");
		
		return institutions;
	}
	
	public Institution getInstitution(Long id) {
		Institution inst = new Institution();
		inst = (Institution)super.getSqlMapClientTemplate().queryForObject("Institution.getInstitution", id);
		return inst;
	}
	
	
	public void deleteInstitution(Institution institution) {
		super.getSqlMapClientTemplate().delete("Institution.deleteInstitution", institution);
	}
	
	
	public Institution saveInstitution(Institution institution, InstitutionStudies studies) {
	
		if(institution.getId() == null) {
			// New institution has to be created.
			Long newKey = (Long)super.getSqlMapClientTemplate().insert("Institution.insertInstitution", institution);
			institution.setId(newKey);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("Centro", institution.getId());
			for(int i=0;i<studies.getStudies().size();i++){
				String estudio = "Estudio";
				map.put(estudio, studies.getStudies().get(i));
				super.getSqlMapClientTemplate().insert("Institution.insertStudy", map);
				map.remove("Estudio");
			}
		} else {
			// Current institution data have to be updated.
			super.getSqlMapClientTemplate().update("Institution.updateInstitution", institution);
			super.getSqlMapClientTemplate().delete("Institution.deleteStudiesInstitution",institution.getId());
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("Centro", institution.getId());
			for(int i=0;i<studies.getStudies().size();i++){
				String estudio = "Estudio";
				map.put(estudio, studies.getStudies().get(i));
				super.getSqlMapClientTemplate().insert("Institution.insertStudy", map);
				map.remove("Estudio");
			}
		}
		
		return institution;
	}

	public InstitutionStats getInstitutionStats(long idInstitution,
			String year, long idCourse) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		if(idInstitution ==-1 || idInstitution == 0){
			map.put("idInstitution", null);
		}else{
			map.put("idInstitution", idInstitution);
		}
		if(idCourse==-1 || idCourse ==0){
			map.put("idCourse", null);
		}else{
			map.put("idCourse", idCourse);
		}
		if(year.equalsIgnoreCase("-1")||year.equals("")){
			map.put("year", null);
		}else{
			map.put("year", year);
		}
		InstitutionStats stats = new InstitutionStats();
		stats.setTeachers(((List<Integer>) super.getSqlMapClientTemplate().queryForList("Institution.getTeachersByInstitutionStats", map)).size());
		stats.setStudents(((List<Integer>) super.getSqlMapClientTemplate().queryForList("Institution.getStudentsByInstitutionStats", map)).size());
		stats.setSubjects(((List<Integer>) super.getSqlMapClientTemplate().queryForList("Institution.getSubjectsByInstitutionStats", map)).size());
		stats.setGroups(((List<Integer>) super.getSqlMapClientTemplate().queryForList("Institution.getGroupsByInstitutionStats", map)).size());
		stats.setConfigExams(((List<Integer>) super.getSqlMapClientTemplate().queryForList("Institution.getExamsByInstitutionStats", map)).size());
		stats.setAllTeachers((Integer) super.getSqlMapClientTemplate().queryForObject("User.getAllTutors"));
		stats.setAllStudents((Integer) super.getSqlMapClientTemplate().queryForObject("User.getAllLearners"));
		stats.setAllSubjects((Integer) super.getSqlMapClientTemplate().queryForObject("Subject.getAllSubjectsNumber"));
		stats.setAllGroups((Integer) super.getSqlMapClientTemplate().queryForObject("Group.getAllGroups"));
		stats.setAllConfigExams((Integer) super.getSqlMapClientTemplate().queryForObject("ConfigExam.getAllConfigExams"));
		return stats;
	}

	public InstitutionStudies getInstitutionStudies(long idInstitution) {
		InstitutionStudies studies = new InstitutionStudies();
		studies.setStudies(super.getSqlMapClientTemplate().queryForList("Institution.getStudiesList", idInstitution));
		return studies;
	}

	public List<Institution> getInstitutionsFiltered(Map<String, Object> map) {
		List<Institution> institutions = super.getSqlMapClientTemplate().queryForList("Institution.getInstitutionFiltered", map);
		for(int i=0;i<institutions.size();i++){
			institutions.get(i).setStudies(getInstitutionStudies(institutions.get(i).getId()));
		}
		return institutions;
	}

	public List<String> getAllCertifications() {
		return super.getSqlMapClientTemplate().queryForList("Institution.getAllCertifications");
	}

	public List<Institution> getInstitutionsWidthPublicQuestions() {
		List<Institution> institutions = new ArrayList<Institution>();
		
		institutions = super.getSqlMapClientTemplate().queryForList("Institution.getInstitutionsWidthPublicQuestions");
		
		return institutions;
	}
	
	public Institution getInstitutionByUserId(Long id) {
		List<Institution> institutionsList = super.getSqlMapClientTemplate().queryForList("Institution.getInstitutionByUserId",id);
		if(institutionsList.size()>0){
			return institutionsList.get(0);
		}else
			return null;
	}
}
