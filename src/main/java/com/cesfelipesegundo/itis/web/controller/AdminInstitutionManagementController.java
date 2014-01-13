package com.cesfelipesegundo.itis.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import com.cesfelipesegundo.itis.biz.api.TutorManagementService;
import com.cesfelipesegundo.itis.biz.api.AdminManagementService;
import com.cesfelipesegundo.itis.model.Course;
import com.cesfelipesegundo.itis.model.Group;
import com.cesfelipesegundo.itis.model.Institution;
import com.cesfelipesegundo.itis.model.InstitutionStudies;
import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.model.comparators.CourseNameComparator;
import com.cesfelipesegundo.itis.model.comparators.GroupCourseComparator;
import com.cesfelipesegundo.itis.model.comparators.GroupNameComparator;
import com.cesfelipesegundo.itis.model.comparators.GroupYearComparator;
import com.cesfelipesegundo.itis.model.comparators.UserNameComparator;
import com.cesfelipesegundo.itis.model.comparators.UserPersIdComparator;
import com.cesfelipesegundo.itis.model.comparators.UserRoleComparator;
import com.cesfelipesegundo.itis.model.comparators.UserSurnameComparator;
import com.cesfelipesegundo.itis.model.comparators.UserUserNameComparator;
import com.cesfelipesegundo.itis.web.BreadCrumb;
import com.cesfelipesegundo.itis.web.Constants;

/**
 * Delegate class. It manages the main operations related to an institution
 * @author ruben
 *
 */
public class AdminInstitutionManagementController implements ServletContextAware{

	private static final Log log = LogFactory.getLog(TutorGroupManagementController.class);
	
	private ServletContext servletContext;
	
	/**
	 * Service needed to manage requests from admin
	 */
    private AdminManagementService adminManagementService;
    
    /**
	 * Service needed to manage some request that are also available for tutor
	 */
    private TutorManagementService tutorManagementService;
    
    /**
     * Group manager: AJAX operations on a group
     */
    private AdminGroupManagementController adminGroupManagementController;
	    
    /**
     * Institution being managed
     */
    private Institution currentInstitution;
    
    /**
     * User manager: AJAX operations
     */
	private AdminUserManagementController adminUserManagementController;
    
    /**
     * Getters and Setters
     */
    
	public AdminManagementService getAdminManagementService() {
		return adminManagementService;
	}

	public void setAdminManagementService(
			AdminManagementService adminManagementService) {
		this.adminManagementService = adminManagementService;
	}

	public Institution getCurrentInstitution() {
		return currentInstitution;
	}

	public void setCurrentInstitution(Institution currentInstitution) {
		this.currentInstitution = currentInstitution;
	}

	public TutorManagementService getTutorManagementService() {
		return tutorManagementService;
	}

	public void setTutorManagementService(
			TutorManagementService tutorManagementService) {
		this.tutorManagementService = tutorManagementService;
	}

	public AdminGroupManagementController getAdminGroupManagementController() {
		return adminGroupManagementController;
	}

	public void setAdminGroupManagementController(
			AdminGroupManagementController adminGroupManagementController) {
		this.adminGroupManagementController = adminGroupManagementController;
	}
    
    /* ********  Controller Methods ******** */
	
	/**
	 * Tutor's main interface for managing groups
	 * @param request
	 * @param response
	 * @return ModelAndView corresponding to the main interface to manage institutions
    */
	public ModelAndView indexInstitution (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("admin/index_institution");
		
		/* 
		 * Institution Id to be managed: when this method is invoked, the institution may have been changed
		 */ 
		if (request.getParameter("idinstitution") != null) {
			currentInstitution = adminManagementService.getInstitution(Long.valueOf(request.getParameter("idinstitution"))); 
		    log.debug("Institución seleccionada: "+currentInstitution.getId().toString());
		}
		List<String> certifications = adminManagementService.getAllCertifications();
		// Addition of the institution object:
		mav.addObject("institution",currentInstitution);
		
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		mav.addObject("studies",adminManagementService.getInstitutionStudies(currentInstitution.getId()));
		mav.addObject("certifications", certifications);
		
		return mav;
	} // indexInstitution
	
	/**
	 * Adds a new institution
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView addInstitution (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("admin/index_institution");
		
		// Create the new institution
		Institution institution = new Institution();
		InstitutionStudies studies = new InstitutionStudies();
		if(request.getParameter("Infantil")!=null)
			studies.addStudy("Infantil");
		if(request.getParameter("Primaria")!=null)
			studies.addStudy("Primaria");
		if(request.getParameter("Secundaria")!=null)
			studies.addStudy("Secundaria");
		if(request.getParameter("FormacionProfesional")!=null)
			studies.addStudy("Ciclo Formativo");
		if(request.getParameter("Bachillerato")!=null)
			studies.addStudy("Bachillerato");
		if(request.getParameter("Universidad")!=null)
			studies.addStudy("Universidad");
		if(request.getParameter("titulacion")!=null && !request.getParameter("titulacion").equalsIgnoreCase("-1"))
			institution.setCertification(request.getParameter("titulacion"));
		else
			institution.setCertification(request.getParameter("certification"));
		institution.setCode(request.getParameter("code"));
		institution.setName(request.getParameter("name"));
		institution.setAddress(request.getParameter("address"));
		institution.setCity(request.getParameter("city"));
		institution.setZipCode(request.getParameter("zip"));
		institution.setState(request.getParameter("area"));
		institution.setPhone(request.getParameter("phone"));
		institution.setFax(request.getParameter("fax"));
		institution.setEmail(request.getParameter("email"));
		institution.setWeb(request.getParameter("web"));
		institution.setContactPerson(request.getParameter("contactPerson"));
		institution.setContactPhone(request.getParameter("contactPhone"));
		
		//Save the institution into the database
		adminManagementService.saveInstitution(institution,studies);
		
		// Addition of the institution object:
		currentInstitution = institution;
		mav.addObject("institution",currentInstitution);
		mav.addObject("studies", studies);
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		mav.addObject("addedInstituion",true);
		return mav;
	} // addInstitution

	/**
	 * Saves the data of the current institution
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView saveInstitution (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("admin/index_institution");
		
		// Create the new institution
		currentInstitution.setCode(request.getParameter("code"));
		currentInstitution.setName(request.getParameter("name"));
		currentInstitution.setAddress(request.getParameter("address"));
		currentInstitution.setCity(request.getParameter("city"));
		currentInstitution.setZipCode(request.getParameter("zip"));
		currentInstitution.setState(request.getParameter("area"));
		currentInstitution.setPhone(request.getParameter("phone"));
		currentInstitution.setFax(request.getParameter("fax"));
		currentInstitution.setEmail(request.getParameter("email"));	
		currentInstitution.setWeb(request.getParameter("web"));					
		currentInstitution.setContactPerson(request.getParameter("contactPerson"));
		currentInstitution.setContactPhone(request.getParameter("contactPhone"));
		InstitutionStudies studies = new InstitutionStudies();
		if(request.getParameter("Infantil")!=null)
			studies.addStudy("Infantil");
		if(request.getParameter("Primaria")!=null)
			studies.addStudy("Primaria");
		if(request.getParameter("Secundaria")!=null)
			studies.addStudy("Secundaria");
		if(request.getParameter("FormacionProfesional")!=null)
			studies.addStudy("Ciclo Formativo");
		if(request.getParameter("Bachillerato")!=null)
			studies.addStudy("Bachillerato");
		if(request.getParameter("Universidad")!=null)
			studies.addStudy("Universidad");
		
		if(request.getParameter("titulacion")!=null && !request.getParameter("titulacion").equalsIgnoreCase("-1"))
			currentInstitution.setCertification(request.getParameter("titulacion"));
		else
			currentInstitution.setCertification(request.getParameter("certification"));
		
		//Save the institution into the database
		adminManagementService.saveInstitution(currentInstitution,studies);
		List<String> certifications = adminManagementService.getAllCertifications();
		// Addition of the institution object:
		mav.addObject("institution",currentInstitution);
		mav.addObject("studies", studies);
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		mav.addObject("certifications", certifications);
		mav.addObject("savedInstituion",true);
		return mav;
	} // addInstitution

	
    /**
	 * Shows a list of users
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView showUsersList (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("admin/users_list");
		
		// We have to obtain from the database the list of users
		// The method we have to call depends on the view
		String view = request.getParameter("view");
		List<User> userslist = null;
		if (view == null) {
			userslist = adminManagementService.getUsers(currentInstitution);
		} else if (view.equals("tutors")) {
			userslist = adminManagementService.getTutors(currentInstitution);
		} else if (view.equals("learners")) {
			userslist = adminManagementService.getLearners(currentInstitution);
		} else {
			userslist = adminManagementService.getUsers(currentInstitution);
		}
		mav.addObject("view", view);
		
		// Addition of the institution object:
		mav.addObject("institution",currentInstitution);

		// If there is a specified order, we sort the list properly
		String orderby = request.getParameter("orderby");
		if (orderby != null) {
			if (orderby.equals("persid"))
				Collections.sort(userslist,new UserPersIdComparator());
			else if (orderby.equals("surname"))
				Collections.sort(userslist,new UserSurnameComparator());
			else if (orderby.equals("name"))
				Collections.sort(userslist,new UserNameComparator());
			else if (orderby.equals("username"))
				Collections.sort(userslist,new UserUserNameComparator());
			else if (orderby.equals("role"))
				Collections.sort(userslist,new UserRoleComparator());
			mav.addObject("orderby",orderby);
		}

		if(request.getParameter("reverse")!=null && !request.getParameter("reverse").trim().equalsIgnoreCase("")){
			Collections.reverse(userslist);
		}else{
			mav.addObject("reverse", "reverse");
		}
		// Addtion of the users:
		mav.addObject("users",userslist);
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
				
		return mav;
	} // showUsersList
	
	public ModelAndView findUser(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("admin/search_user");
		String view = request.getParameter("view");
		mav.addObject("view", view);
		mav.addObject("institution",currentInstitution);
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		
		return mav;
	}
	
	public ModelAndView findGroup(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("admin/search_group");
		String view = request.getParameter("view");
		mav.addObject("view", view);
		mav.addObject("institution",currentInstitution);
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		
		return mav;
	}

    /**
	 * Shows a list of groups
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView showGroupsList (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("admin/groups_list");
		
		// We have to obtain from the database the list of users
		List<Group> groupslist = adminManagementService.getGroups(currentInstitution);
		
		// If there is a specified order, we sort the list properly
		String orderby = request.getParameter("orderby");
		if (orderby != null) {
			if (orderby.equals("course"))
				Collections.sort(groupslist,new GroupCourseComparator());
			else if (orderby.equals("name"))
				Collections.sort(groupslist,new GroupNameComparator());
			else if (orderby.equals("year"))
				Collections.sort(groupslist,new GroupYearComparator());
			mav.addObject("orderby",orderby);
		}
		// Reverse if asked
		String reverse = request.getParameter("reverse");
		if (reverse!=null) {
			Collections.reverse(groupslist);
			mav.addObject("reverse","yes");
		}
		
		// Addition of the institution object:
		mav.addObject("institution",currentInstitution);

		// Addtion of the groups:
		mav.addObject("groups",groupslist);
		
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
				
		return mav;
	} // showGroupsList
	
	/**
	 * Shows the template for the addition of a new user
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView newUser (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("admin/new_user");
		
		BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
		breadCrumb.addStep(currentInstitution.getName(),request.getContextPath()+"/admin/institution.itest?method=indexInstitution");
		breadCrumb.addBundleStep("textNewUser","");
		mav.addObject("breadCrumb",breadCrumb);
		
		// Addition of the institution object:
		mav.addObject("institution",currentInstitution);
		// Setting the institution
		adminUserManagementController.setCurrentInstitution(currentInstitution);

		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
				
		return mav;
	} // newUser
	
	/**
	 * Deletes an user
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView deleteUser (HttpServletRequest request, HttpServletResponse response) {
		// We have to obtain the user from the id:
		User user = null; 
		if (request.getParameter("iduser") != null) {
			user = tutorManagementService.getUserData(new Long(request.getParameter("iduser")));
			log.debug("Borrando usuario "+user.getId().toString());
		} else {
			log.error("ERROR borrando usuario");
		}
		// Now we delete the user
		adminManagementService.deleteUser(user);
		
		// And show the users list
		return showUsersList(request, response);
	} // deleteUser

	/**
	 * Shows the template for the addition of a new user
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView editUser (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("admin/new_user");
		// We have to obtain the user from the id:
		User userEditing = null; 
		if (request.getParameter("iduser") != null) {
			userEditing = tutorManagementService.getUserData(new Long(request.getParameter("iduser")));
			// Addition of the user object:
			mav.addObject("editUser",userEditing);
			log.debug("Editando usuario "+userEditing.getId().toString());
		} else {
			log.error("ERROR editando usuario");
		}
				
		BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
		if (currentInstitution != null)
			breadCrumb.addStep(currentInstitution.getName(),request.getContextPath()+"/admin/institution.itest?method=indexInstitution");
		breadCrumb.addBundleStep("textEditUser","");
		mav.addObject("breadCrumb",breadCrumb);
		
		// Addition of the institution object:
		if (currentInstitution != null) {
			mav.addObject("institution",currentInstitution);
			// Setting the institution
			adminUserManagementController.setCurrentInstitution(currentInstitution);
		}

		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
				
		return mav;
	} // editUser

	/**
	 * Shows the template for the addition of a new group
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView newGroup (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("admin/new_group");
		
		// Addition of the institution object:
		mav.addObject("institution",currentInstitution);
		
		// a new group
		Group group = new Group();
		group.setInstitution(currentInstitution);
		adminGroupManagementController.setCurrentGroup(group);

		// We have to obtain from the database the list of courses
		List<Course> courseslist = adminManagementService.getCoursesFiltered("", "", "", "");
		Collections.sort(courseslist, new CourseNameComparator());
		
		// Addtion of the courses:
		mav.addObject("courses",courseslist);
		
		// List of tutors, empty
		List<User> tutorslist = new ArrayList<User>();
		mav.addObject("users",tutorslist);
		
		// View: tutors
		mav.addObject("view","tutors");
		
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
				
		return mav;
	} // newGroup
	
	/**
	 * Shows the template for the edition of a group 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView editGroup (HttpServletRequest request, HttpServletResponse response) {
		// The same view as the new group one, but with the group precharged 
		
		// We have to obtain the group from the id:
		Group group = null; 
		if (request.getParameter("idgroup") != null) {
			group = tutorManagementService.getGroup(new Long(request.getParameter("idgroup")));
			log.debug("Editando asignatura "+group.getId().toString());
		} else {
			log.error("ERROR editando asignatura");
		}
		
		/* 
		 * Institution Id of the group being edited, called from new_course
		 */ 
		if (request.getParameter("idinstitution") != null) {
			currentInstitution = adminManagementService.getInstitution(Long.valueOf(request.getParameter("idinstitution"))); 
		    log.debug("Institución seleccionada: "+currentInstitution.getId().toString());
		}
		
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("admin/new_group");
		
		// Set the current group
		adminGroupManagementController.setCurrentGroup(group);
		
		// Addition of the group object:
		mav.addObject("group",group);
		
		// Addition of the institution object:
		mav.addObject("institution",currentInstitution);
		
		// Selecting the view: learners or tutors
		String view = request.getParameter("view");
		List<User> userslist = null;
		if (view == null || !view.equals("learners")) {
			view = "tutors";
			userslist = adminManagementService.getTutors(group);
		}
		else {
			userslist = tutorManagementService.getStudents(group);
		}
		mav.addObject("users",userslist);
		mav.addObject("view",view);

		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		
		return mav;
	} // editGroup

	/**
	 * Deletes a group
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView deleteGroup (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("admin/groups_list");
		
		// We delete the group from the database
		try {	
			Long id = new Long(request.getParameter("idgroup"));
			Group group = new Group();
			group.setId(id);
			adminManagementService.deleteGroup(group);
			mav.addObject("groupDeleted", true);
		}
		catch (Exception e) {
			log.info("No se ha podido borrar el grupo: "+e.getMessage());
		}
		
		// We have to obtain from the database the list of groups
		List<Group> groupslist = adminManagementService.getGroups(currentInstitution);
		
		// Addition of the institution object:
		mav.addObject("institution",currentInstitution);

		// Addtion of the groups:
		mav.addObject("groups",groupslist);
		
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		
		return mav;
		
	} // deleteGroup

	
	
	public AdminUserManagementController getAdminUserManagementController() {
		return adminUserManagementController;
	}

	public void setAdminUserManagementController(
			AdminUserManagementController adminUserManagementController) {
		this.adminUserManagementController = adminUserManagementController;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	public List<User> filterByUser(String dni, String name, String surname, String userName, String typeUser){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("Institution",currentInstitution.getId() );
		if(dni!=null && dni.equalsIgnoreCase("")){
			map.put("dni", null);
		}else{
			map.put("dni", "%"+dni+"%");
		}
		if(name!=null && name.equalsIgnoreCase("")){
			map.put("name", null);
		}else{
			map.put("name", "%"+name+"%");
		}
		if(surname!=null && surname.equalsIgnoreCase("")){
			map.put("surname", null);
		}else{
			map.put("surname", "%"+surname+"%");
		}
		if(userName!=null && userName.equalsIgnoreCase("")){
			map.put("userName", null);
		}else{
			map.put("userName", "%"+userName+"%");
		}
		if(typeUser!=null && typeUser.equalsIgnoreCase("")){
			map.put("typeUser", null);
		}else{
			map.put("typeUser", typeUser);
		}
		List<User> userlist = adminManagementService.getUsersByFilter(map);
		return userlist;
	}
	
	public List<Group> filterByGroup(String course, String group, String year){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("Institution",currentInstitution.getId() );
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
		List<Group> groupList = adminManagementService.getGroupsByFilter(map);
		return groupList;
	}

}
