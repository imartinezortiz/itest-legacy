package com.cesfelipesegundo.itis.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ServletContextAware;

import com.cesfelipesegundo.itis.biz.api.AdminManagementService;
import com.cesfelipesegundo.itis.biz.api.TutorManagementService;
import com.cesfelipesegundo.itis.model.Course;
import com.cesfelipesegundo.itis.model.CourseStats;
import com.cesfelipesegundo.itis.web.GroupWithTutors;
import com.cesfelipesegundo.itis.web.comparators.GroupWithTutorsCompareGroups;
import com.cesfelipesegundo.itis.web.comparators.GroupWithTutorsCompareInstitutions;
import com.cesfelipesegundo.itis.web.comparators.GroupWithTutorsCompareYears;

import es.itest.engine.course.business.entity.Group;

/**
 * Delegate class. It manages the main operations related to an course
 * @author gonzalo
 *
 */
//Gonzalo was here
public class AdminCourseManagementController  implements ServletContextAware {
	
	private static final Log log = LogFactory.getLog(AdminCourseManagementController.class);
	
	/**
	 * Services needed to manage requests from admin
	 */
	private AdminManagementService adminManagementService;
    private TutorManagementService tutorManagementService;
    
    /**
     * Course being managed (added or edited) by the admin
     */
    private Course currentCourse;
    
    private ServletContext servletContext;

	public AdminManagementService getAdminManagementService() {
		return adminManagementService;
	}

	public void setAdminManagementService(
			AdminManagementService adminManagementService) {
		this.adminManagementService = adminManagementService;
	}

	public Course getCurrentGroup() {
		return currentCourse;
	}

	public void setCurrentGroup(Course currentGroup) {
		this.currentCourse = currentGroup;
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
	
	/* ---------------------- Course Management (Ajax) ------------------------ */
	
	/**
	 * Obtains a the list of groups related to the actual course and the tutors assigned to those courses 
	 * @param idCourse the ID of the actual course
	 * @return List of GroupWithTutors with the groups of the course and the tutors of those groups
	 */
	public List<GroupWithTutors> getGroupsWithTutors(String idCourse){
		List<GroupWithTutors> result = new ArrayList<GroupWithTutors>();
		Long id = new Long(idCourse);
		currentCourse = adminManagementService.getCourse(id);

		List<Group> groupsList = adminManagementService.getGroups(currentCourse);
		Iterator<Group> groupIterator = groupsList.iterator();
		GroupWithTutors aux;
		Group group;
		while (groupIterator.hasNext()){
			group = groupIterator.next();
			aux = new GroupWithTutors(group);
			aux.setTutorList(adminManagementService.getTutors(group));
			result.add(aux);
		}	
		return result;
	}// getGroupsWithTutors
	
	/**
	 * Returns an ordered list of Groups of the current course
	 * @param idCourse the course's ID to obtain the groups
	 * @param order the order of the retured list, could be groups or institutions, in normal o reverse
	 * @return an ordered list of groups of the course
	 */
	public List<GroupWithTutors> getOrderedGroupsWithTutors(String idCourse,String order){
		
		List<GroupWithTutors> result = getGroupsWithTutors(idCourse);
		if (result != null){
			if (order.toLowerCase().contains("group")){
				GroupWithTutorsCompareGroups cG = new GroupWithTutorsCompareGroups();
				if (order.toLowerCase().contains("reverse")){
					Collections.sort(result,Collections.reverseOrder(cG));
				}else {
					Collections.sort(result,cG);
				}
			}else if (order.toLowerCase().contains("institution")){
				GroupWithTutorsCompareInstitutions cI = new GroupWithTutorsCompareInstitutions();
				if (order.toLowerCase().contains("reverse")){
					Collections.sort(result,Collections.reverseOrder(cI));
				}else{
					Collections.sort(result,cI);
				}
			}else if (order.toLowerCase().contains("year")){
				GroupWithTutorsCompareYears cY = new GroupWithTutorsCompareYears();
				if (order.toLowerCase().contains("reverse")){
					Collections.sort(result,Collections.reverseOrder(cY));
				}else{
					Collections.sort(result,cY);
				}
			}else{
				Collections.sort(result,new GroupWithTutorsCompareInstitutions());
			}
		}
		return result;
	}// getOrderedGroupsWithTutors
	
	
	public Course isCodeAlreadyInUse(String code){
		Course course = adminManagementService.getCourseByCode(code);
		if(currentCourse!=null){
			if(currentCourse.getCode().equalsIgnoreCase(code)){
				return null;
			}
		}
		return course;
	}
	
	public List<Course> getCourses(long institutionId){
		return adminManagementService.getCourses(institutionId);
	}
	
	public CourseStats getCourseStats(long institution, long course, String year){
		CourseStats stats = adminManagementService.getCourseStats(institution,course,year);
		return stats;
	}
	
	public List<Course> getCourseByInstitutionAndGroup(long idInstitution, String year){
		return adminManagementService.getCourseByInstitutionAndGroup(idInstitution,year);
	}
	
	public List<Course> filterByCourse(String codigo, String nombre, String curso, String estudios){
		List<Course> courseList = adminManagementService.getCoursesFiltered(codigo,nombre,curso,estudios);
		return courseList;
	}
}
