package com.cesfelipesegundo.itis.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

import com.cesfelipesegundo.itis.biz.api.AdminManagementService;
import com.cesfelipesegundo.itis.biz.api.LearnerManagementService;
import com.cesfelipesegundo.itis.biz.api.TutorManagementService;
import com.cesfelipesegundo.itis.model.Conection;
import com.cesfelipesegundo.itis.model.CourseStats;
import com.cesfelipesegundo.itis.model.Exam;
import com.cesfelipesegundo.itis.model.ExamTest;
import com.cesfelipesegundo.itis.model.ExamGlobalInfo;
import com.cesfelipesegundo.itis.model.Grade;
import com.cesfelipesegundo.itis.model.Group;
import com.cesfelipesegundo.itis.model.Institution;
import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.model.Course;
import com.cesfelipesegundo.itis.model.comparators.ConectionsDateComparator;
import com.cesfelipesegundo.itis.model.comparators.ConectionsIdComparator;
import com.cesfelipesegundo.itis.model.comparators.ConectionsIpComparator;
import com.cesfelipesegundo.itis.model.comparators.ConectionsUserNameComparator;
import com.cesfelipesegundo.itis.model.comparators.CourseCodeComparator;
import com.cesfelipesegundo.itis.model.comparators.CourseLevelComparator;
import com.cesfelipesegundo.itis.model.comparators.CourseNameComparator;
import com.cesfelipesegundo.itis.model.comparators.CourseNumGroupsComparator;
import com.cesfelipesegundo.itis.model.comparators.CourseStudiesComparator;
import com.cesfelipesegundo.itis.model.comparators.ExamGlobalInfoCenterComparator;
import com.cesfelipesegundo.itis.model.comparators.ExamGlobalInfoEndDateComparator;
import com.cesfelipesegundo.itis.model.comparators.ExamGlobalInfoStartDateComparator;
import com.cesfelipesegundo.itis.model.comparators.ExamGlobalInfoSubjectComparator;
import com.cesfelipesegundo.itis.model.comparators.ExamGlobalInfoTitleComparator;
import com.cesfelipesegundo.itis.model.comparators.ExamStatsDoneNumberComparator;
import com.cesfelipesegundo.itis.model.comparators.ExamStatsDurationComparator;
import com.cesfelipesegundo.itis.model.comparators.ExamStatsFailedPercentageComparator;
import com.cesfelipesegundo.itis.model.comparators.ExamStatsGradeAverageComparator;
import com.cesfelipesegundo.itis.model.comparators.ExamStatsGradeMaxComparator;
import com.cesfelipesegundo.itis.model.comparators.ExamStatsGradeMedianComparator;
import com.cesfelipesegundo.itis.model.comparators.ExamStatsGradeMinComparator;
import com.cesfelipesegundo.itis.model.comparators.ExamStatsGradeStandardDeviationComparator;
import com.cesfelipesegundo.itis.model.comparators.ExamStatsLearnersNumberComparator;
import com.cesfelipesegundo.itis.model.comparators.ExamStatsMaxGradeComparator;
import com.cesfelipesegundo.itis.model.comparators.ExamStatsPassedPercentageComparator;
import com.cesfelipesegundo.itis.model.comparators.ExamStatsTimeAverageComparator;
import com.cesfelipesegundo.itis.model.comparators.ExamStatsTimeMaxComparator;
import com.cesfelipesegundo.itis.model.comparators.ExamStatsTimeMedianComparator;
import com.cesfelipesegundo.itis.model.comparators.ExamStatsTimeMinComparator;
import com.cesfelipesegundo.itis.model.comparators.ExamStatsTimeStandardDeviationComparator;
import com.cesfelipesegundo.itis.model.comparators.ExamStatsTitleComparator;
import com.cesfelipesegundo.itis.model.comparators.ExamStatsUndonePercentageComparator;
import com.cesfelipesegundo.itis.model.comparators.InstitutionAreaComparator;
import com.cesfelipesegundo.itis.model.comparators.InstitutionCertificationComparator;
import com.cesfelipesegundo.itis.model.comparators.InstitutionCityComparator;
import com.cesfelipesegundo.itis.model.comparators.InstitutionCodeComparator;
import com.cesfelipesegundo.itis.model.comparators.InstitutionNameComparator;
import com.cesfelipesegundo.itis.model.comparators.UserEmailComparator;
import com.cesfelipesegundo.itis.model.comparators.UserIdComparator;
import com.cesfelipesegundo.itis.model.comparators.UserNameComparator;
import com.cesfelipesegundo.itis.model.comparators.UserPersIdComparator;
import com.cesfelipesegundo.itis.model.comparators.UserRoleComparator;
import com.cesfelipesegundo.itis.model.comparators.UserSurnameComparator;
import com.cesfelipesegundo.itis.model.comparators.UserUserNameComparator;
import com.cesfelipesegundo.itis.web.BreadCrumb;
import com.cesfelipesegundo.itis.web.Constants;
import com.cesfelipesegundo.itis.web.servlets.HomeServlet;

public class AdminManagementController implements ServletContextAware{

	private static final Log log = LogFactory.getLog(HomeServlet.class);
	
	private TutorManagementService tutorManagementService;
	private LearnerManagementService learnerManagementService;
	
	private ServletContext servletContext;
	/**
	 * Service needed to manage requests from admin
	 */
    private AdminManagementService adminManagementService;
    
    
    /**
     * User's list
     * */
    private List<User> currentUserList;
    
    /**
     * User's list
     * */
    private List<ExamGlobalInfo> currentExamGlobalInfoList;
	
    /**
     * Getters and Setters
     */
    private List<Institution> currentInstituionList;
    
    /**
     * Conections' list
     * */
    private List<Conection> currentConectionList;
    
    private List<ExamTest> currentExamsTest;
    
    private AdminInstitutionManagementController adminInstitutionManagementController;
    private AdminUserManagementController adminUserManagementController;
    
    
    
    public AdminUserManagementController getAdminUserManagementController() {
		return adminUserManagementController;
	}

	public void setAdminUserManagementController(
			AdminUserManagementController adminUserManagementController) {
		this.adminUserManagementController = adminUserManagementController;
	}

	public AdminInstitutionManagementController getAdminInstitutionManagementController() {
		return adminInstitutionManagementController;
	}

	public void setAdminInstitutionManagementController(
			AdminInstitutionManagementController adminInstitutionManagementController) {
		this.adminInstitutionManagementController = adminInstitutionManagementController;
	}
	
    
    public List<ExamTest> getCurrentExamsTest() {
		return currentExamsTest;
	}

	public void setCurrentExamsTest(List<ExamTest> currentExamsTest) {
		this.currentExamsTest = currentExamsTest;
	}

	public TutorManagementService getTutorManagementService() {
		return tutorManagementService;
	}

	public void setTutorManagementService(
			TutorManagementService tutorManagementService) {
		this.tutorManagementService = tutorManagementService;
	}

	public LearnerManagementService getLearnerManagementService() {
		return learnerManagementService;
	}

	public void setLearnerManagementService(
			LearnerManagementService learnerManagementService) {
		this.learnerManagementService = learnerManagementService;
	}

	public List<Conection> getCurrentConectionList() {
		return currentConectionList;
	}

	public void setCurrentConectionList(List<Conection> currentConectionList) {
		this.currentConectionList = currentConectionList;
	}

	public List<Institution> getCurrentInstituionList() {
		return currentInstituionList;
	}

	public void setCurrentInstituionList(List<Institution> currentInstituionList) {
		this.currentInstituionList = currentInstituionList;
	}

	public List<User> getCurrentUserList() {
		return currentUserList;
	}

	public void setCurrentUserList(List<User> currentUserList) {
		this.currentUserList = currentUserList;
	}
	
    public List<ExamGlobalInfo> getCurrentExamGlobalInfoList() {
		return currentExamGlobalInfoList;
	}

	public void setCurrentExamGlobalInfoList(
			List<ExamGlobalInfo> currentPreviousExamList) {
		this.currentExamGlobalInfoList = currentPreviousExamList;
	}

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

    /**
	 * Shows a list of courses
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView showCoursesList (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("admin/courses_list");
		
		// We have to obtain from the database the list of courses
		List<Course> courseslist = adminManagementService.getCoursesFiltered("", "", "", "");
		
		// If there is a specified order, we sort the list properly
		String orderby = request.getParameter("orderby");
		if (orderby != null) {
			if (orderby.equals("code"))
				Collections.sort(courseslist,new CourseCodeComparator());
			else if (orderby.equals("name"))
				Collections.sort(courseslist,new CourseNameComparator());
			else if (orderby.equals("studies"))
				Collections.sort(courseslist,new CourseStudiesComparator());
			else if (orderby.equals("level"))
				Collections.sort(courseslist,new CourseLevelComparator());
			else if(orderby.equals("numGroups")){
				Collections.sort(courseslist,new CourseNumGroupsComparator());
			}
			mav.addObject("orderby",orderby);
		}
		// Reverse if asked
		String reverse = request.getParameter("reverse");
		if (reverse!=null) {
			Collections.reverse(courseslist);
			mav.addObject("reverse","yes");
		}
		
		// Addtion of the courses:
		mav.addObject("courses",courseslist);
		
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
				
		return mav;
	} // showCoursesList

    /**
	 * Shows a list of institutions
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView showInstitutionsList (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("admin/institutions_list");
		
		// We have to obtain from the database the list of institutions
		List<Institution> institutionslist = adminManagementService.getInstitutions();
		for(int i=0;i<institutionslist.size();i++){
			institutionslist.get(i).setStudies(adminManagementService.getInstitutionStudies(institutionslist.get(i).getId()));
		}
		currentInstituionList = institutionslist;
		// Addtion of the institutions:
		mav.addObject("institutions",institutionslist);
		
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
				
		return mav;
	} // showInstitutionsList
	
    /**
	 * Shows the template for the addition of a new institution
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView newInstitution (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("admin/new_institution");
		
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
				
		return mav;
	} // newInstitution

	public ModelAndView nextExams (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("admin/next_exams");
		currentExamGlobalInfoList =  adminManagementService.getNextExams();
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		mav.addObject("nextExams",currentExamGlobalInfoList);
		return mav;
	} // nextExams
	
	public ModelAndView activeExams (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("admin/active_exams");
		currentExamGlobalInfoList= adminManagementService.getActiveExams();
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		mav.addObject("activeExams", currentExamGlobalInfoList);
				
		return mav;
	} // nextExams
	
	public ModelAndView previousExams (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("admin/previous_exams");
		
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		mav.addObject("institutions",adminManagementService.getInstitutions());
				
		return mav;
	} // nextExams
	public ModelAndView searchUsers (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("admin/search_users");
		
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		mav.addObject("institutions",adminManagementService.getInstitutions());
				
		return mav;
	} // nextExams
	
	public ModelAndView goStatsBySubject(HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("admin/subject_stats");
		
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		
		//Hacer una consulta que devuelva todas las asignatruas
		List<Institution> list = adminManagementService.getInstitutions();
		Collections.sort(list,new InstitutionNameComparator());
		mav.addObject("institutions",list);
		
		
		return mav;
	} // goStatsBySubject
	
	public ModelAndView goStatsByCenter(HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("admin/center_stats");
		
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		//Hacer una consulta que devuelva todas las asignatruas
		List<Institution> list = adminManagementService.getInstitutions();
		Collections.sort(list,new InstitutionNameComparator());
		mav.addObject("institutions", list);
		
		
		return mav;
	} // goStatsByCenter
	
    /**
	 * Deletes an institution
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView deleteInstitution (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("admin/institutions_list");
		mav.addObject("deletedInstitution", true);
		// We delete the institution from the database
		try {	
			Long id = new Long(request.getParameter("idinstitution"));
			Institution institution = new Institution();
			institution.setId(id);
			adminManagementService.deleteInstitution(institution);
		}
		catch (Exception e) {
			log.info("No se ha podido borrar el centro: "+e.getMessage());
		}
		
		// We have to obtain from the database the list of institutions
		List<Institution> institutionslist = adminManagementService.getInstitutions();
		for(int i=0;i<institutionslist.size();i++){
			institutionslist.get(i).setStudies(adminManagementService.getInstitutionStudies(institutionslist.get(i).getId()));
		}
		// Addtion of the institutions:
		mav.addObject("institutions",institutionslist);
		
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
				
		return mav;
	} // showInstitutionsList

	/**
	 * Shows the template for the addition of a new course
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView newCourse (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("admin/new_course");
		
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
				
		return mav;
	} // newCourse
	
	/**
	 * Saves an existing course
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView saveCourse (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		
		boolean exist = true;
		// Get the course
		String id = request.getParameter("idcourse");
		Course course = null;
		Course course2 = adminManagementService.getCourseByCode(request.getParameter("code"));
		if (id == null || id.equals("")) {
			course = new Course();
			exist = false;
		}
		else {
			course = adminManagementService.getCourse(new Long(request.getParameter("idcourse")));
		}
		course.setCode(request.getParameter("code"));
		course.setName(request.getParameter("name"));
		course.setLevel(request.getParameter("level"));
		course.setStudies(request.getParameter("studies"));
		course.setComments(request.getParameter("comments"));		
		ModelAndView mav;
		// Save the institution into the database
		if(!exist){
			// si es distinto de null es porque ya hay una asignatura con ese código
			if(course2 == null){
				mav = new ModelAndView("admin/courses_list");
				course = adminManagementService.saveCourse(course);
			}else{
				mav = new ModelAndView("admin/new_course");
				mav.addObject("errorCourse",true);
			}
			
		}else{
			mav = new ModelAndView("admin/courses_list");
			course = adminManagementService.saveCourse(course);
		}
		// Addition of the course object:
		mav.addObject("course",course);
		
		// We have to obtain from the database the list of users
		List<Course> courseslist = adminManagementService.getCourses();
		
		// Addtion of the users:
		mav.addObject("courses",courseslist);

		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
				
		return mav;
	} // addCourse

	/**
	 * Shows the template for the edition of a course 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView editCourse (HttpServletRequest request, HttpServletResponse response) {
		// The same view as the new course one, but with the course precharged 
		
		// We have to obtain the course from the id:
		Course course = null; 
		if (request.getParameter("idcourse") != null) {
			course = adminManagementService.getCourse(new Long(request.getParameter("idcourse")));
			log.debug("Editando asignatura "+course.getId().toString());
		} else {
			log.error("ERROR editando asignatura");
		}
		
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("admin/new_course");
		
		// Addition of the course object:
		mav.addObject("course",course);

		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		
		return mav;
	}	// editCourse

    /**
	 * Deletes a course
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView deleteCourse (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("admin/courses_list");
		
		// We have to obtain the course from the id, and delete it from the database:
		Course course = null; 
		if (request.getParameter("idcourse") != null) {
			course = adminManagementService.getCourse(new Long(request.getParameter("idcourse")));
			log.debug("Borrando asignatura "+course.getId().toString());
			adminManagementService.deleteCourse(course);
			mav.addObject("deleted", true);
		} else {
			log.error("ERROR borrando asignatura");
		}
		
		// We have to obtain from the database the list of courses
		List<Course> courseslist = adminManagementService.getCourses();
		
		// Addtion of the courses:
		mav.addObject("courses",courseslist);
		
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
				
		return mav;
	} // deleteCourse

	
	public ModelAndView loadTests (HttpServletRequest request, HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute(Constants.USER);
		List<Course> courses = adminManagementService.getCourses();
		ModelAndView mav = new ModelAndView("admin/load_tests");
		mav.addObject("courses", courses);
		mav.addObject("user",user);
		return mav;
	}
	
	public ModelAndView generateTestExams(HttpServletRequest request, HttpServletResponse response){
		User user = (User) request.getSession().getAttribute(Constants.USER);
		String examId = request.getParameter("examId");
		String examsNumberString = request.getParameter("examsNumber");
		String groupId = request.getParameter("groupId");
		log.info("exam's id: "+examId+" exams number: "+examsNumberString);
		long idExam;
		long examsNumber;
		long idGroup;
		long start = 0;
		long end = 1;
		long beginExam;
		long endExam;
		float time;
		Group group = null;
		try{
			idGroup = Long.parseLong(groupId);
			idExam = Long.parseLong(examId);
			examsNumber = Long.parseLong(examsNumberString);
			group = adminManagementService.getGroup(idGroup);
		}catch(Exception e){
			idExam = -1;
			examsNumber = -1;
			idGroup = -1;
		}
		if(idExam!=-1 && examsNumber!=-1){
			List<User> users = adminManagementService.getLearnerByGroup(group);
			currentExamsTest = new ArrayList<ExamTest>();
			int userNumber = 0;
			User auxUser = null;
			start = System.currentTimeMillis();
			for(int i=0;i<examsNumber;i++){
				beginExam = System.currentTimeMillis();
				ExamTest ex4 = new ExamTest();
				if(userNumber<users.size())
					auxUser = users.get(userNumber);
				Exam ex = adminManagementService.getAlreadyDoneExam(auxUser, idExam);
				if(ex==null){
					try{
						ex = adminManagementService.getNewExam(auxUser, idExam ,request.getRemoteAddr());
						if(ex==null){
							ex = new Exam();
							ex.setId(idExam);
							ex.setTitle("null");
							ex.setQuestionsNumber(0);
							ex4.setExam(ex);
							ex4.setUser(auxUser);
							ex4.setFailed(true);
						}else{
							ex4.setExam(ex);
							ex4.setUser(auxUser);
							ex4.setFailed(false);
						}
						
						log.info("Generado examen con id: "+idExam+" para el alumno con id: "+auxUser.getId());
					}catch(Exception e){
						e.printStackTrace();
						ex4.setFailed(true);
						if(ex4.getExam()==null && ex!=null)
							ex4.setExam(ex);
						if(ex4.getUser()==null)
							ex4.setUser(auxUser);
					}
				}else{
					ex4.setExam(ex);
					ex4.setUser(auxUser);
					ex4.setFailed(false);
					log.info("El examen con id: "+idExam+" para el alumno con id: "+auxUser.getId()+"ya estaba generado");
				}
				userNumber++;
				endExam = System.currentTimeMillis();
				ex4.setTime(endExam-beginExam);
				currentExamsTest.add(ex4);
			}
			end = System.currentTimeMillis();
		}else{
			log.info("no es posible generar ningún examen");
		}
		time = end - start;
		
		ModelAndView mav = new ModelAndView("admin/exam_tests");
		mav.addObject("generatedExam",currentExamsTest);
		mav.addObject("user",user);
		mav.addObject("maxExams", examsNumber);
		mav.addObject("time", time);
		mav.addObject("group", group);
		mav.addObject("examid", idExam);
		return mav;
	}
	
	/**
	 * Shows the exam performed by a student
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView showStudentExam (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("tutor/exam_review");
		
		Long idStd = Long.valueOf(request.getParameter("alu"));
		Long idConfigExam = Long.valueOf(request.getParameter("exam"));
		
		if ((idStd != null) && (idConfigExam != null)) {
			// Obtention of the exam from the database.
			Exam ex = tutorManagementService.getStudentExam(idStd,idConfigExam);
			
			if (ex == null) {
				log.error("No existe examen "+request.getParameter("exam")+" o alumno "+request.getParameter("alu"));
			} else {
				log.debug("Examen "+request.getParameter("exam")+" de alumno "+request.getParameter("alu")+" recuperado.");
				// Adding the exam
				mav.addObject("exam",ex);
				request.getSession().setAttribute("examPreview", ex);
				// Data of the student, to be included at the header of the exam review.
				User user = tutorManagementService.getUserData(idStd);
				mav.addObject("user",user);
			}
		} else {
			log.error("Error en los parámetros alu y exam");
		}
		
		return mav;
	} // showStudentExam
	
	public List<ExamGlobalInfo> getPreviousExamsFiltered(long idInstitution, long idCourse, String year){
		currentExamGlobalInfoList = adminManagementService.getPreviousExamsFiltered(idInstitution,idCourse,year);
		return currentExamGlobalInfoList;
	}
	
	public List<ExamGlobalInfo> getActiveExamsFiltered(String Centro,
			String Asignatura, String startDateString, String endDateString){
		Date startDate = null;
		Date endDate= null;
		SimpleDateFormat sdf = null;
		if(startDateString!=null && !startDateString.equalsIgnoreCase("")){
			sdf = new SimpleDateFormat("dd/MM/yy");
			startDate = new Date();
			try {
				startDate = sdf.parse(startDateString);
			} catch (ParseException e) {
				startDate = null;
			}
		}
		if(endDateString!=null && !endDateString.equalsIgnoreCase("")){
			sdf = new SimpleDateFormat("dd/MM/yy");
			endDate = new Date();
			try {
				endDate = sdf.parse(endDateString);
			} catch (ParseException e) {
				endDate= null;
			}
		}
		setCurrentExamGlobalInfoList(adminManagementService.getActiveExamsFiltered(Centro,Asignatura,startDate,endDate));
		
		return currentExamGlobalInfoList;
	}
	
	public List<ExamGlobalInfo> getNextExamsFiltered(String Centro,
			String Asignatura, String startDateString, String endDateString){
		Date startDate = null;
		Date endDate= null;
		SimpleDateFormat sdf = null;
		if(startDateString!=null && !startDateString.equalsIgnoreCase("")){
			sdf = new SimpleDateFormat("dd/MM/yy");
			startDate = new Date();
			try {
				startDate = sdf.parse(startDateString);
			} catch (ParseException e) {
				startDate = null;
			}
		}
		if(endDateString!=null && !endDateString.equalsIgnoreCase("")){
			sdf = new SimpleDateFormat("dd/MM/yy");
			endDate = new Date();
			try {
				endDate = sdf.parse(endDateString);
			} catch (ParseException e) {
				endDate= null;
			}
		}
		currentExamGlobalInfoList=adminManagementService.getNextExamsFiltered(Centro,Asignatura,startDate,endDate);
		return currentExamGlobalInfoList;
	}
	
	public CourseStats getCourseStatsByExam(int idexam){
		return adminManagementService.getCourseStatsByExam(idexam);
	}
	
	public List<User> getSearchUsersFiltered(String name, String apes, String user, String tipo){
		currentUserList = adminManagementService.getSearchUsersFiltered(name,apes,user,tipo);
		return currentUserList;
	}
	
	public List<User> orderUserList(String orderby, boolean reverse){
		if (orderby != null && currentUserList!=null) {
			if (orderby.equals("name"))
				Collections.sort(currentUserList,new UserNameComparator());
			else if (orderby.equals("surname"))
				Collections.sort(currentUserList,new UserSurnameComparator());
			else if (orderby.equals("persid"))
				Collections.sort(currentUserList,new UserPersIdComparator());
			else if (orderby.equals("username"))
				Collections.sort(currentUserList,new UserUserNameComparator());
			else if (orderby.equals("role"))
				Collections.sort(currentUserList,new UserRoleComparator());
			else if (orderby.equals("id"))
				Collections.sort(currentUserList,new UserIdComparator());
			else if (orderby.equals("email"))
				Collections.sort(currentUserList,new UserEmailComparator());
			
		}
		if (reverse && currentUserList!=null) {
			Collections.reverse(currentUserList);
		}
		return currentUserList;
	}

	
	public List<ExamGlobalInfo> orderActiveExamsList(String orderby, boolean reverse){
		if (orderby != null && currentExamGlobalInfoList!=null) {
			if(orderby.equals("center"))
				Collections.sort(currentExamGlobalInfoList, new ExamGlobalInfoCenterComparator());
			else if(orderby.equals("subject"))
				Collections.sort(currentExamGlobalInfoList, new ExamGlobalInfoSubjectComparator());
			else if(orderby.equals("startDate"))
				Collections.sort(currentExamGlobalInfoList, new ExamGlobalInfoStartDateComparator());
			else if(orderby.equals("endDate"))
				Collections.sort(currentExamGlobalInfoList, new ExamGlobalInfoEndDateComparator());
		}
		
		if (reverse && currentExamGlobalInfoList!=null) {
			Collections.reverse(currentExamGlobalInfoList);
		}
		return currentExamGlobalInfoList;
	}
	
	public List<ExamGlobalInfo> orderNextExamsList(String orderby, boolean reverse){
		if (orderby != null && currentExamGlobalInfoList!=null) {
			if(orderby.equals("center"))
				Collections.sort(currentExamGlobalInfoList, new ExamGlobalInfoCenterComparator());
			else if(orderby.equals("subject"))
				Collections.sort(currentExamGlobalInfoList, new ExamGlobalInfoSubjectComparator());
			else if(orderby.equals("startDate"))
				Collections.sort(currentExamGlobalInfoList, new ExamGlobalInfoStartDateComparator());
			else if(orderby.equals("endDate"))
				Collections.sort(currentExamGlobalInfoList, new ExamGlobalInfoEndDateComparator());
		}
		
		if (reverse && currentExamGlobalInfoList!=null) {
			Collections.reverse(currentExamGlobalInfoList);
		}
		return currentExamGlobalInfoList;
	}
	
	public List<ExamGlobalInfo> orderPreviousExamsList(String orderby, boolean reverse){
		if (orderby != null && currentExamGlobalInfoList!=null) {
			if(orderby.equals("center"))
				Collections.sort(currentExamGlobalInfoList, new ExamGlobalInfoCenterComparator());
			else if(orderby.equals("subject"))
				Collections.sort(currentExamGlobalInfoList, new ExamGlobalInfoSubjectComparator());
			else if(orderby.equals("title"))
				Collections.sort(currentExamGlobalInfoList, new ExamGlobalInfoTitleComparator());
			else if(orderby.equals("startDate"))
				Collections.sort(currentExamGlobalInfoList, new ExamGlobalInfoStartDateComparator());
			else if(orderby.equals("endDate"))
				Collections.sort(currentExamGlobalInfoList, new ExamGlobalInfoEndDateComparator());
		}
		
		if (reverse && currentExamGlobalInfoList!=null) {
			Collections.reverse(currentExamGlobalInfoList);
		}
		return currentExamGlobalInfoList;
	}
	
	public List<Institution> findInstitution(String id, String nombre, String provincia, String localidad, String titulacion, String[] nivelEstudios){
		Map<String,Object> map = new HashMap<String,Object>();
		if(id!=null && id.trim().equalsIgnoreCase("")){
			map.put("id", null);
		}else{
			map.put("id", id);
		}
		if(nombre!=null && nombre.trim().equalsIgnoreCase("")){
			map.put("nombre", null);
		}else{
			map.put("nombre", "%"+nombre+"%");
		}
		if(provincia!=null && provincia.trim().equalsIgnoreCase("")){
			map.put("provincia", null);
		}else{
			map.put("provincia", "%"+provincia+"%");
		}
		if(localidad!=null && localidad.trim().equalsIgnoreCase("")){
			map.put("localidad", null);
		}else{
			map.put("localidad", "%"+localidad+"%");
		}
		if(titulacion!=null && titulacion.trim().equalsIgnoreCase("")){
			map.put("certification", null);
		}else{
			map.put("certification", titulacion);
		}
		for(int i=0;i<nivelEstudios.length;i++){
			if(nivelEstudios[i]!=null){
				map.put("estudios",true);
				map.put("Estudio"+i, nivelEstudios[i]);
			}
		}
		currentInstituionList = adminManagementService.getInstitutionsFiltered(map);
		
		return currentInstituionList;
	}
	
	public List<Institution> orderInstitutionList(String orderby, boolean reverse){
		if (orderby != null && currentInstituionList!=null) {
			if(orderby.equals("code"))
				Collections.sort(currentInstituionList, new InstitutionCodeComparator());
			else if(orderby.equals("name"))
				Collections.sort(currentInstituionList, new InstitutionNameComparator());
			else if(orderby.equals("city"))
				Collections.sort(currentInstituionList, new InstitutionCityComparator());
			else if(orderby.equals("area"))
				Collections.sort(currentInstituionList, new InstitutionAreaComparator());
			else if(orderby.equals("certification"))
				Collections.sort(currentInstituionList, new InstitutionCertificationComparator());
		}
		
		if (reverse && currentInstituionList!=null) {
			Collections.reverse(currentInstituionList);
		}
		return currentInstituionList;
	}
	
	public ModelAndView go100LastConections(HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("admin/last_conections");
		
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		//Hacer una consulta que devuelva todas las asignatruas
		currentConectionList = adminManagementService.get100LastConections();
		mav.addObject("conections", currentConectionList);
		
		return mav;
	} // goStatsByCenter
	
	public List<Conection> order100LastConectionsBy(String orderBy, boolean reverse){
		if(orderBy.equalsIgnoreCase("id")){
			Collections.sort(currentConectionList, new ConectionsIdComparator());
		}else if(orderBy.equalsIgnoreCase("userName")){
			Collections.sort(currentConectionList, new ConectionsUserNameComparator());
		}else if(orderBy.equalsIgnoreCase("date")){
			Collections.sort(currentConectionList, new ConectionsDateComparator());
		}else if(orderBy.equalsIgnoreCase("ip")){
			Collections.sort(currentConectionList, new ConectionsIpComparator());
		}
		if(reverse){
			Collections.reverse(currentConectionList);
		}
		return currentConectionList;
	}
	
	public List<Conection> runFilterAndSearch100Conections(Long idConection, String userNameConection, String startDate, String endDate){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
		Date auxStartDate = new Date();
		if(startDate!=null){
			String startDate2db = new String(startDate);
			try {
				auxStartDate = sdf.parse(startDate2db);
			} catch (ParseException e) {
				auxStartDate = null;
			}
		}
		Date auxEndDate = new Date();
		if(endDate!=null){
			String endDate2db = new String(endDate);
			try {
				auxEndDate = sdf.parse(endDate2db);
			} catch (ParseException e) {
				auxEndDate = null;
			}
		}
		currentConectionList = adminManagementService.runFilterAndSearch100Conections(idConection,userNameConection,auxStartDate,auxEndDate);
		return currentConectionList;
	}
	
	public List<Conection> show5LastConections(Long id){
		currentConectionList = adminManagementService.show5LastConections(id);
		return currentConectionList;
	}
	
	public List<User> showUsersNotVinculated(){
		currentUserList = adminManagementService.showUsersNotVinculated();
		return currentUserList;
	}
	
	public List<User> deleteUserById(long idUser){
		User user = adminManagementService.getUserById(idUser);
		adminManagementService.deleteUser(user);
		return adminManagementService.getSearchUsersFiltered("", "", "", "");
	}
	
	public List<ExamTest> deleteExams(long idGroup, long idUser, long idExam){
		tutorManagementService.deleteStudentGradeAndExam(idUser, idExam);
		/*List<User> users = tutorManagementService.getStudents(tutorManagementService.getGroup(idGroup));
		List<ExamTest> grades = new ArrayList<ExamTest>();
		for(int i=0;i<users.size();i++){
			Grade grade = learnerManagementService.getGradeByIdExam(idExam, users.get(i).getId());
			ExamTest aux = null;
			if(grade!=null){
				aux = new ExamTest();
				Exam ex = learnerManagementService.getExam(idExam, grade.getIdStudent());
				User user = adminManagementService.getUserById(users.get(i).getId());
				if(ex!=null && user!=null){
					aux.setExam(ex);
					aux.setUser(user);
					
				}
			}
			if(aux!=null)
				grades.add(aux);
		}
		*/
		for(int i=0;i<currentExamsTest.size();i++){
			if(currentExamsTest.get(i).getUser().getId()==idUser){
				currentExamsTest.remove(i);
			}
		}
		return currentExamsTest;
	}
	
	public List<ExamTest> deleteAllGeneratedExamsTest(long idGroup, long idExam){
		for(int i=currentExamsTest.size()-1;i>=0;i--){
			ExamTest ext = currentExamsTest.get(i);
			tutorManagementService.deleteStudentGradeAndExam(ext.getUser().getId(), idExam);
			currentExamsTest.remove(i);
		}
		return currentExamsTest;
	}
	
	public ModelAndView editUser (HttpServletRequest request, HttpServletResponse response) {
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
		
		Institution userInstitution = adminManagementService.getInstitutionByUserId(userEditing.getId());
		
		mav.addObject("institution",userInstitution);
		adminInstitutionManagementController.setCurrentInstitution(userInstitution);
		adminUserManagementController.setCurrentInstitution(userInstitution);
		BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
		if (userInstitution != null)
			breadCrumb.addStep(userInstitution.getName(),request.getContextPath()+"/admin/institution.itest?method=indexInstitution");
		breadCrumb.addBundleStep("textEditUser","");
		mav.addObject("breadCrumb",breadCrumb);
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		return mav;
	}
}
