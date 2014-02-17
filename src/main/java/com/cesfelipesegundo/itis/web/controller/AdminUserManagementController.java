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
import com.cesfelipesegundo.itis.biz.api.LearnerManagementService;
import com.cesfelipesegundo.itis.biz.api.TutorManagementService;
import com.cesfelipesegundo.itis.biz.api.UserManagementService;
import com.cesfelipesegundo.itis.model.Institution;
import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.web.Constants;
import com.cesfelipesegundo.itis.model.comparators.CourseNameComparator;
import com.cesfelipesegundo.itis.model.comparators.GroupCourseComparator;
import com.cesfelipesegundo.itis.model.comparators.GroupInstitutionNameComparator;
import com.cesfelipesegundo.itis.model.comparators.GroupNameComparator;
import com.cesfelipesegundo.itis.model.comparators.GroupYearComparator;

import es.itest.engine.course.business.entity.Group;

public class AdminUserManagementController  implements ServletContextAware {
	
	private static final Log log = LogFactory.getLog(AdminUserManagementController.class);
	
	/**
	 * Services needed to manage requests from admin
	 */
	private AdminManagementService adminManagementService;
    private TutorManagementService tutorManagementService;
    private LearnerManagementService learnerManagementService;
    private UserManagementService usermanagementService;
    
    
    /**
     * Group being managed (added or edited) by the admin
     */
    private Institution currentInstitution;
    
    private ServletContext servletContext;

	public AdminManagementService getAdminManagementService() {
		return adminManagementService;
	}

	public void setAdminManagementService(
			AdminManagementService adminManagementService) {
		this.adminManagementService = adminManagementService;
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
	
	/* ---------------------- User Management (Ajax) ------------------------ */
	
	
	/**
	 * Save the information of a user. If the id is not provided means "new" user, and has to be
	 * registered in the current group. Otherwise, the information of a given user is changed.
	 * 
	 * The input parameters must be checked before invoking this method. 
	 * 
	 * @return true when no problems where detected. False when inserting and the username is used.
	 */
	public boolean saveUser (String stdId, String persId, String surname, String name,
									String email, String userName,String passwd, String role) {
		
		// New user object:
		User newUser = new User();
		// User data:
		if (!stdId.equals(new String(""))) {
			// Is not a new user
			newUser.setId(Long.valueOf(stdId));
		}
		newUser.setPersId(persId);
		newUser.setSurname(surname);
		newUser.setName(name);
		newUser.setEmail(email);
		newUser.setUserName(userName);
		if(!passwd.trim().equalsIgnoreCase("")){
			newUser.setPasswd(passwd);
		}
		if (role.equals("LEARNER")) {
			newUser.setRole(Constants.LEARNER);			
		} else {
			if (role.equals("KID")) {
				newUser.setRole(Constants.KID);
			} else {
				if(role.equals("TUTOR")){
					newUser.setRole(Constants.TUTOR);
				}else{
					newUser.setRole(Constants.TUTORAV);
				}
			}
		}
				
		// Log:
		log.debug("- Guardando usuario "+stdId);
		if(currentInstitution==null)
			currentInstitution = adminManagementService.getInstitutionByUserId(newUser.getId());		
		// Saving the user data in the current institution
		boolean res = adminManagementService.saveUser(newUser,currentInstitution);
		
		
		return res;
		
	} // saveUser	
	


	/**
	 * Checks if the userName is already under use 
	 * 
	 * @return true if the userName is free, false if is used
	 */
	public boolean checkNewUserName (String userName) {
		return tutorManagementService.userNameIsAvailable(userName);
	}

	public Institution getCurrentInstitution() {
		return currentInstitution;
	}

	public void setCurrentInstitution(Institution currentInstitution) {
		this.currentInstitution = currentInstitution;
	}	
	
	/**
	 * Obtains a the list of groups related to the actual user 
	 * @param idUser the ID of the user
	 * @return List of Group the groups of the course
	 */
	public List<Group> getGroups(String idUser){
		List<Group> result = new ArrayList<Group>();
		Long id = new Long(idUser);
		User u = tutorManagementService.getUserData(id);
		if (u.getRole().toLowerCase().equals("tutor") || u.getRole().toLowerCase().equals("tutorav")){
			result = tutorManagementService.getTeachedGroups(id);
		}else if (u.getRole().toLowerCase().equals("learner")){
			result = tutorManagementService.getMatriculatedGroups(id);
		}else if (u.getRole().toLowerCase().equals("kid")){
			result = tutorManagementService.getMatriculatedGroups(id);
		}
		return result;
	}// getGroups
	
	/**
	 * Returns an ordered list of Groups of the current user
	 * @param idUser the user's ID to obtain the groups
	 * @param order if the order of the retured list, could be groups, year, course or institutions, in normal o reverse
	 * @return an ordered list of groups of the course
	 */
	public List<Group> getOrderedGroups(String idUser,String order){

		List<Group> result = getGroups(idUser);
		if (result != null){
			if (order.toLowerCase().contains("group")){
				GroupNameComparator gN = new GroupNameComparator();
				if (order.toLowerCase().contains("reverse")){
					Collections.sort(result,Collections.reverseOrder(gN));
				}else {
					Collections.sort(result,gN);
				}
			}else if (order.toLowerCase().contains("institution")){
				GroupInstitutionNameComparator gIN = new GroupInstitutionNameComparator();
				if (order.toLowerCase().contains("reverse")){
					Collections.sort(result,Collections.reverseOrder(gIN));
				}else{
					Collections.sort(result,gIN);
				}
			}else if (order.toLowerCase().contains("course")){
				GroupCourseComparator gC = new GroupCourseComparator();
				if (order.toLowerCase().contains("reverse")){
					Collections.sort(result,Collections.reverseOrder(gC));
				}else{
					Collections.sort(result,gC);
				}
			}else if (order.toLowerCase().contains("year")){
				GroupYearComparator gY = new GroupYearComparator();
				if (order.toLowerCase().contains("reverse")){
					Collections.sort(result,Collections.reverseOrder(gY));
				}else{
					Collections.sort(result,gY);
				}
			}else{
				Collections.sort(result,new GroupInstitutionNameComparator());
			}
		}
		
		return result;
	}// getOrderedGroups
	
	public List<User> getFilteredUsers(String id, String dni, String name, String surname, 
			String userName, String typeUser){
		List<User> listUser = null;
		Map<String,Object> map = new HashMap<String,Object>();
		if(id==null||id.equalsIgnoreCase("")){
			map.put("id", null);
		}else{
			map.put("id", id);
		}
		if(dni==null||dni.equalsIgnoreCase("")){
			map.put("dni", null);
		}else{
			map.put("dni", "%"+dni+"%");
		}		
		if(name==null||name.equalsIgnoreCase("")){
			map.put("name", null);
		}else{
			map.put("name", "%"+name+"%");
		}
		if(surname==null||surname.equalsIgnoreCase("")){
			map.put("surname", null);
		}else{
			map.put("surname", "%"+surname+"%");
		}
		if(userName==null||userName.equalsIgnoreCase("")){
			map.put("userName", null);
		}else{
			map.put("userName", "%"+userName+"%");
		}
		if(typeUser==null||typeUser.equalsIgnoreCase("")){
			map.put("typeUser", null);
		}else{
			map.put("typeUser", typeUser);
		}
		
		listUser = adminManagementService.getFilteredUsers(map);
		
		return listUser;
	}
	
}
