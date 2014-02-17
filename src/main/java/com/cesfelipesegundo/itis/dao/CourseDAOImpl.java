package com.cesfelipesegundo.itis.dao;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cesfelipesegundo.itis.dao.api.CourseDAO;
import com.cesfelipesegundo.itis.model.Course;
import com.cesfelipesegundo.itis.model.CourseStats;

import es.itest.engine.course.business.entity.Group;
import es.itest.engine.test.business.entity.Item;
import es.itest.engine.test.business.entity.ItemResponse;
import es.itest.engine.test.business.entity.MediaElem;
import es.itest.engine.test.business.entity.TestSubject;

public class CourseDAOImpl extends SqlMapClientDaoSupport implements CourseDAO {
	/**
	 * Constructor
	 *
	 */
	public CourseDAOImpl() {
		super();
	}
	
	
	/**
	 * Obtain the list of subjects (syllabus) of the group from the database.
	 * @param group object
	 * @return list of subjects.
	 */
	public List<TestSubject> getSubjects(Group group) {
		// Cojo la lista de temas ...
		List<TestSubject> subjects = super.getSqlMapClientTemplate().queryForList("TemplateExam.getTemplateSubjectByGroupId", group.getId());
		
		// A cada subject hay que añadirle:
		for(TestSubject subject : subjects) {
			
			// Lista de preguntas del tema correspondiente
			List<Item> questions = super.getSqlMapClientTemplate().queryForList("TemplateExam.getTemplateExamQuestion", subject);
			subject.setQuestions(questions);
			
			// Además, a cada question hay que añadirle:
			for(Item question : questions) {
				
				// Lista de los elementos multimedia
				List<MediaElem> questionMedia = super.getSqlMapClientTemplate().queryForList("TemplateExam.getQuestionMedia", question.getId());
				question.setMmedia(questionMedia);
				
				// Lista de respuestas a esta pregunta
				List<ItemResponse> answers = super.getSqlMapClientTemplate().queryForList("TemplateExam.getTemplateExamAnswer", question.getId());
				question.setAnswers(answers);
				
				// Y finalmente a cada respuesta hay que añadirle
				for(ItemResponse answer : answers) {
					// Lista de elementos multimedia
					List<MediaElem> answerMedia = super.getSqlMapClientTemplate().queryForList("TemplateExam.getAnswerMedia", answer.getId());
					answer.setMmedia(answerMedia);
				}
			}
		}
		return subjects;
	}


	public Course getCourse(Long id) {
		Course record = (Course)getSqlMapClientTemplate().queryForObject("Course.getCourseById", id);
		return record;
	}
	
	public Course getCourseByCode(String code){
		Course record = (Course)getSqlMapClientTemplate().queryForObject("Course.getCourseByCode", code);
		return record;
	}
	
	public Course saveCourse(Course course) {
		
		if(course.getId() == null) {
			// New Course has to be created.
			Long newKey = (Long)super.getSqlMapClientTemplate().insert("Course.insertCourse", course);
			course.setId(newKey);
		} else {
			// Current Course data have to be updated.
			super.getSqlMapClientTemplate().update("Course.updateCourse", course);
		}
		return course;
	}
	
	public void deleteCourse(Course course) {
		super.getSqlMapClientTemplate().delete("Course.deleteCourse", course);
	}
	
	public List<Course> getCourses() {
		List<Course> courses = new ArrayList<Course>();
		
		courses = super.getSqlMapClientTemplate().queryForList("Course.getCourses");
		
		return courses;	
	}


	public List<Course> getCourses(long institutionId) {
		List<Course> courses = super.getSqlMapClientTemplate().queryForList("Course.getCoursesByCenter",institutionId);
		return courses;
	}


	public CourseStats getCourseStats(long institution, long course, String year) {
		Map<String,Object> map = new HashMap<String,Object>();
		if(institution <=0){
			map.put("institution", null);
		}else{
			map.put("institution", institution);
		}
		if(course<=0){
			map.put("course", null);
		}else{
			map.put("course", course);
		}
		if(year.equalsIgnoreCase("-1")||year.equals("")){
			map.put("year", null);
		}else{
			map.put("year", year);
		}
		
		CourseStats stats = new CourseStats();
		stats.setGroups(((Integer) super.getSqlMapClientTemplate().queryForObject("Course.getGroupsByCourse",map)));
		map.put("valMin", -10);
		map.put("valMax", 4.999999999);
		stats.setSs(((Integer) super.getSqlMapClientTemplate().queryForObject("Course.getGroupCalifs",map)));
		map.remove("valMin");
		map.remove("valMax");
		map.put("valMin", 5);
		map.put("valMax", 6.999999999);
		stats.setAp(((Integer) super.getSqlMapClientTemplate().queryForObject("Course.getGroupCalifs",map)));
		map.remove("valMin");
		map.remove("valMax");
		map.put("valMin", 7);
		map.put("valMax", 8.999999999);
		stats.setNt(((Integer) super.getSqlMapClientTemplate().queryForObject("Course.getGroupCalifs",map)));
		map.remove("valMin");
		map.remove("valMax");
		map.put("valMin", 9);
		map.put("valMax", 10.00000001);
		stats.setSb(((Integer) super.getSqlMapClientTemplate().queryForObject("Course.getGroupCalifs",map)));
		/*
		 * Algunas notas tienen valores por debajo de 0 y por encima de 10
		 * */
		//stats.setTotalStudent(((Integer) super.getSqlMapClientTemplate().queryForObject("Course.getAllGroupCalifs",map)));
		map.remove("valMin");
		map.remove("valMax");
		map.put("valMin", -10);
		map.put("valMax", 10.00000001);
		stats.setTotalStudentByGroup(((Integer) super.getSqlMapClientTemplate().queryForObject("Course.getAllStudentGroup",map)));
		stats.setNumExams(((Integer) super.getSqlMapClientTemplate().queryForObject("Course.getNumExams",map)));

		return stats;
	}


	public List<Course> getCourseByInstitutionAndGroup(long idInstitution,
			String year) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("idInstitution", idInstitution);
		map.put("year", year);
		
		return super.getSqlMapClientTemplate().queryForList("Course.getCourseByInstitutionAndGroup",map);
	}


	public List<Course> getCoursesFiltered(String codigo, String nombre, String curso, String estudios) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("codigo", "%"+codigo+"%");
		map.put("nombre","%"+nombre+"%");
		if(curso!=null)
			map.put("curso", "%"+curso+"%");
		if(estudios!=null)
			map.put("estudios", "%"+estudios+"%");
		List<Course> asignaturas = super.getSqlMapClientTemplate().queryForList("Course.getCoursesFiltered",map);
		for(Course asignatura : asignaturas){
			List<Group> grupos = super.getSqlMapClientTemplate().queryForList("Group.getCourseGroups", asignatura.getId());
			asignatura.setNumGroups(grupos.size());
		}
		return asignaturas;
	}
	
}
