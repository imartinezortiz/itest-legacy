package com.cesfelipesegundo.itis.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ServletContextAware;

import com.cesfelipesegundo.itis.biz.api.AdminManagementService;
import com.cesfelipesegundo.itis.biz.api.TutorManagementService;
import com.cesfelipesegundo.itis.model.Course;
import com.cesfelipesegundo.itis.model.ExamTest;
import com.cesfelipesegundo.itis.model.GroupDetails;
import com.cesfelipesegundo.itis.model.Institution;
import com.cesfelipesegundo.itis.model.InstitutionStats;
import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.model.comparators.GroupNameComparator;
import com.cesfelipesegundo.itis.model.comparators.UserNameComparator;
import com.cesfelipesegundo.itis.web.Constants;

import es.itest.engine.course.business.entity.Group;
import es.itest.engine.test.business.entity.TestSessionTemplate;

public class AdminGroupManagementController  implements ServletContextAware {
	
	private static final Log log = LogFactory.getLog(AdminGroupManagementController.class);
	
	/**
	 * Services needed to manage requests from admin
	 */
	private AdminManagementService adminManagementService;
    private TutorManagementService tutorManagementService;
    
    /**
     * Group being managed (added or edited) by the admin
     */
    private Group currentGroup;
    
    private ServletContext servletContext;

	public AdminManagementService getAdminManagementService() {
		return adminManagementService;
	}

	public void setAdminManagementService(
			AdminManagementService adminManagementService) {
		this.adminManagementService = adminManagementService;
	}

	public Group getCurrentGroup() {
		return currentGroup;
	}

	public void setCurrentGroup(Group currentGroup) {
		this.currentGroup = currentGroup;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public TutorManagementService getTutorManagementService() {
		return tutorManagementService;
	}

	public void setTutorManagementService(
			TutorManagementService tutorManagementService) {
		this.tutorManagementService = tutorManagementService;
	}
	
	/* ---------------------- Group Management (Ajax) ------------------------ */
	
	public List<User> getRegisteredStudents() {
		List<User> result = tutorManagementService.getStudents(currentGroup);
		if (result == null) {
			return new ArrayList<User>();
		}
		else
			return result;
	}
	
	public List<User> getAssignedTutors() {
		List<User> result = adminManagementService.getTutors(currentGroup);
		if (result == null) {
			return new ArrayList<User>();
		}
		else
			return result;
	}

	public List<User> getUnregisteredStudents() {
		List<User> result = tutorManagementService.getStudentsNotRegistered(currentGroup);
		if (result == null) {
			return new ArrayList<User>();
		}
		else
			return result;
	}
	
	public List<User> getUnassignedTutors() {
		List<User> result = adminManagementService.getUnassignedTutors(currentGroup);
		if (result == null) {
			return new ArrayList<User>();
		}
		else
			return result;
	}
	
	public Group addGroup(String name, String year, String courseId, String studentType) {
		log.debug("añadiendo grupo "+name+" ("+year+") de tipo "+studentType+" a asignatura "+courseId);
		currentGroup.setName(name);
		currentGroup.setYear(year);
		currentGroup.setCourse(adminManagementService.getCourse(new Long(courseId)));
		if (studentType.equals("learner"))
			currentGroup.setStudentType(new Integer(1));
		else if (studentType.equals("kid"))
			currentGroup.setStudentType(new Integer(2));
		else
			currentGroup.setStudentType(new Integer(0));
		adminManagementService.saveGroup(currentGroup);
		return currentGroup;
	}
	
	public boolean saveGroup(String groupid, String name, String year, String studentType) {
		log.debug("salvando grupo "+groupid+" - "+name+" ("+year+") de tipo "+studentType);
		currentGroup.setName(name);
		currentGroup.setYear(year);
		if (studentType.equals("learner"))
			currentGroup.setStudentType(new Integer(1));
		else if (studentType.equals("kid"))
			currentGroup.setStudentType(new Integer(2));
		else
			currentGroup.setStudentType(new Integer(0));
		adminManagementService.saveGroup(currentGroup);
		return true;
	}
	
	public User assignTutor(String tutorid) {
		log.debug("asignando profesor "+tutorid+" a grupo "+currentGroup.getId());
		User tutor = tutorManagementService.getUserData(new Long(tutorid));
		adminManagementService.assignTutor(tutor, currentGroup);
		return tutor;
	}
	
	public User registerLearner(String learnerid) {
		log.debug("registrando alumno "+learnerid+" a grupo "+currentGroup.getId());
		User learner = tutorManagementService.getUserData(new Long(learnerid));
		tutorManagementService.registerStudent(learner, currentGroup);
		return learner;
	}

	public User unAssignTutor(String tutorid) {
		log.debug("desasignando profesor "+tutorid+" a grupo "+currentGroup.getId());
		User tutor = tutorManagementService.getUserData(new Long(tutorid));
		adminManagementService.unAssignTutor(tutor, currentGroup);
		return tutor;
	}
	
	public User unRegisterLearner(String learnerid) {
		log.debug("desregistrando alumno "+learnerid+" a grupo "+currentGroup.getId());
		User learner = tutorManagementService.getUserData(new Long(learnerid));
		tutorManagementService.unRegisterStudent(learner, currentGroup);
		return learner;
	}
	
	/**
	 * Deletes a group in AJAX
	 * @param idGroup id of the Group to be deleted
	 */
	public void deleteGroup (String idGroup) {
		// We delete the group from the database
		try {
			Long id = new Long(idGroup);
			Group group = new Group();
			group.setId(id);
			adminManagementService.deleteGroup(group);
		}
		catch (Exception e) {
			log.info("No se ha podido borrar el grupo: "+e.getMessage());
		}
		
	} // deleteGroup
	
	
	/**
	 * Unassings a tutor to a group in AJAX
	 * @param idTutor id of the tutor to be unassigned
	 * @param idGroup id of the Group
	 */
	public void unAssignTutorCourseView (String idTutor,String idGroup) {
		// We unassign the tutor from the group
		try {
			Long id = new Long(idGroup);
			Group group = new Group();
			group.setId(id);
			User tutor = tutorManagementService.getUserData(new Long(idTutor));
			adminManagementService.unAssignTutor(tutor, group);
		}
		catch (Exception e) {
			log.info("No se ha podido desasignar el tutor "+idTutor+" del grupo "+idGroup+" : "+e.getMessage());
		}
		
	} // unAssignTutorCourseView
	
	/**
	 * Unassings a user to a group in AJAX
	 * @param idUser id of the tutor to be unassigned
	 * @param idGroup id of the Group
	 */
	public void unAssignUserGroup(String idUser,String idGroup){
		// We unassign the tutor from the group
		try {
			Long id = new Long(idGroup);
			Group group = new Group();
			group.setId(id);
			User user = tutorManagementService.getUserData(new Long(idUser));
			if(user.getRole().equalsIgnoreCase(Constants.TUTOR) || user.getRole().equalsIgnoreCase(Constants.TUTORAV)){
				adminManagementService.unAssignTutor(user, group);
			}else{
				tutorManagementService.unRegisterStudent(user, group);
			}
		}
		catch (Exception e) {
			log.info("No se ha podido desasignar el usuario "+idUser+" del grupo "+idGroup+" : "+e.getMessage());
		}
	}
	
	/** 
	 * Returns all groups for this course
	 * @param idCourse id of this course
	 * @param idInstitution id of this institution
	 * @return the groups that there are in this course
	 * */
	public List<Group> getGroups(long idCourse, long idInstitution){
		Course course = adminManagementService.getCourse(idCourse);
		return adminManagementService.getGroups(course,idInstitution);
	}

	/** 
	 * Returns all groups for this institution
	 * @param idInstitution id of this institution
	 * @return the groups that there are in this course
	 * */
	public List<Group> getGroupsByInstitution(long idInstitution){
		Institution institution = adminManagementService.getInstitution(idInstitution);
		List<Group> result = adminManagementService.getGroups(institution);
		return result;
	}
	
	public InstitutionStats showStatsByInstitution(long idInstitution, String year, long idCourse){
		return adminManagementService.getInstitutionStats(idInstitution,year,idCourse);
	}
	
	public List<Group> getFilteredGroups(String id, String group, String course, String year){
		Map<String,Object> map = new HashMap<String,Object>();
		if(id!=null && id.equalsIgnoreCase("")){
			map.put("id", null);
		}else{
			map.put("id", "%"+id+"%");
		}
		if(group!=null && group.equalsIgnoreCase("")){
			map.put("group", null);	
		}else{
			map.put("group", "%"+group+"%");
		}
		if(course!=null && course.equalsIgnoreCase("")){
			map.put("course", null);
		}else{
			map.put("course", "%"+course+"%");
		}
		if(year != null && year.equalsIgnoreCase("")){
			map.put("year", null);
		}else{
			map.put("year", "%"+year+"%");
		}
		return adminManagementService.getFilteredGroups(map);
	}
	
	public List<Group> getUserInfoGroups(long id){
		return adminManagementService.getUserInfoGroups(id);
	}
	
	public List<GroupDetails> showGroupDetails(long idInstitution, String year, long idCourse){
		return adminManagementService.getGroupDetails(idInstitution,year,idCourse);
	}
	
	public List<Group> getGroupsByCourse(String courseId){
		long idCourse;
		try{
			idCourse = Long.parseLong(courseId);
		}catch(Exception e){
			idCourse = -1;
		}
		if(idCourse!=-1){
			Course course = adminManagementService.getCourse(idCourse);
			List<Group> groups = adminManagementService.getGroups(course);
			return groups;
		}
		return null;
	}
	
	public List<TestSessionTemplate> getExamsByGroup(String groupId){
		long idGroup;
		try{
			idGroup = Long.parseLong(groupId);
		}catch(Exception e){
			idGroup = -1;
		}
		if(idGroup!=-1){
			Group group = tutorManagementService.getGroup(idGroup);
			List<TestSessionTemplate> exams = tutorManagementService.getGroupConfigExams(group, "title");
			return exams;
		}
		return null;
	}
	
	public Integer getLearnersNumber(String groupId){
		long idGroup;
		try{
			idGroup = Long.parseLong(groupId);
		}catch(Exception e){
			idGroup = -1;
		}
		if(idGroup!=-1){
			Group group = tutorManagementService.getGroup(idGroup);
			int learnersNumber = tutorManagementService.getStudents(group).size();
			return learnersNumber;
		}
		return 0;
	}
}
