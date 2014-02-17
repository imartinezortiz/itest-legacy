package com.cesfelipesegundo.itis.web.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Collections;
import java.util.ListIterator;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import com.cesfelipesegundo.itis.biz.api.LearnerManagementService;
import com.cesfelipesegundo.itis.biz.api.TutorManagementService;
import com.cesfelipesegundo.itis.model.CustomExamUser;
import com.cesfelipesegundo.itis.model.ExamStats;
import com.cesfelipesegundo.itis.model.LearnerStats;
import com.cesfelipesegundo.itis.model.Message;
import com.cesfelipesegundo.itis.model.Query;
import com.cesfelipesegundo.itis.model.QueryGrade;
import com.cesfelipesegundo.itis.model.QuestionStats;
import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.web.BreadCrumb;
import com.cesfelipesegundo.itis.web.Constants;
import com.cesfelipesegundo.itis.model.comparators.*;
import com.lowagie.text.DocumentException;

import es.itest.engine.course.business.entity.Group;
import es.itest.engine.course.business.entity.Subject;
import es.itest.engine.test.business.entity.Item;
import es.itest.engine.test.business.entity.ItemResponse;
import es.itest.engine.test.business.entity.ItemSession;
import es.itest.engine.test.business.entity.ItemSessionResponse;
import es.itest.engine.test.business.entity.TestSession;
import es.itest.engine.test.business.entity.TestSessionGrade;
import es.itest.engine.test.business.entity.TestSessionTemplate;
import es.itest.engine.test.business.entity.TestSubject;

/**
 * Delegate class. It manages the main operations related to a group, and to the questions related
 * (i.e., generate new question, edit or delete questions, show list of questions...)
 * @author J.M. Colmenar
 *
 */
public class TutorGroupManagementController {
	
	private static final Log log = LogFactory.getLog(TutorGroupManagementController.class);
	
	/**
	 * Service needed to manage requests from tutor
	 */
    private TutorManagementService tutorManagementService;
    
    /**
	 * Service needed to manage requests from learner
	 */
    private LearnerManagementService learnerManagementService;
    
    /**
     * Group being managed: it is selected in the principal page for the tutor: index_tutor
     */
    private Group currentGroup;

    /**
     * Question manager: needed for adding and editing questions (currentQuestion is its property)
     */
    private TutorQuestionManagementController tutorQuestionManagementController;
    
    /**
     * Question LIST manager: needed for managing questions (edit, delete, sort...)
     */
    private TutorQuestionListManagementController tutorQuestionListManagementController;
    
    /**
     * Exam manager: needed for adding and editing exams (currentTutorExam is its property)
     */
    private TutorExamManagementController tutorExamManagementController;

    /**
     * Exam Configuration LIST manager: needed for managing exam configurations (edit, delete, sort...)
     */
    private TutorExamListManagementController tutorExamListManagementController;
    
    /**
     * Theme LIST manager: needed for managing the theme list of the group (add, delete, sort)
     */
    private TutorThemeListManagementController tutorThemeListManagementController;
    
    /**
     * Student LIST manager: needed for managing the list of students of the group (add, delete, sort)
     */
    private TutorStudentListManagementController tutorStudentListManagementController;
    
    /**
     * Grades LIST: needed for view showGrades
     * */
    private TutorGradeListManagementController tutorGradeListManagementController;
    
    
    /* ******** Getters and setters ******** */
	
    
    public TutorManagementService getTutorManagementService() {
		return tutorManagementService;
	}
    

	public TutorGradeListManagementController getTutorGradeListManagementController() {
		return tutorGradeListManagementController;
	}


	public void setTutorGradeListManagementController(
			TutorGradeListManagementController tutorGradeListManagementController) {
		this.tutorGradeListManagementController = tutorGradeListManagementController;
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

	public Group getCurrentGroup() {
		return currentGroup;
	}

	public void setCurrentGroup(Group currentGroup) {
		this.currentGroup = currentGroup;
	}
	
	public TutorQuestionManagementController getTutorQuestionManagementController() {
		return tutorQuestionManagementController;
	}

	public void setTutorQuestionManagementController(
			TutorQuestionManagementController tutorQuestionManagementController) {
		this.tutorQuestionManagementController = tutorQuestionManagementController;
	}

	public TutorQuestionListManagementController getTutorQuestionListManagementController() {
		return tutorQuestionListManagementController;
	}

	public void setTutorQuestionListManagementController(
			TutorQuestionListManagementController tutorQuestionListManagementController) {
		this.tutorQuestionListManagementController = tutorQuestionListManagementController;
	}

	public TutorExamManagementController getTutorExamManagementController() {
		return tutorExamManagementController;
	}

	public void setTutorExamManagementController(
			TutorExamManagementController tutorExamManagementController) {
		this.tutorExamManagementController = tutorExamManagementController;
	}
	
	public TutorExamListManagementController getTutorExamListManagementController() {
		return tutorExamListManagementController;
	}

	public void setTutorExamListManagementController(
			TutorExamListManagementController tutorExamListManagementController) {
		this.tutorExamListManagementController = tutorExamListManagementController;
	}
	
	public TutorThemeListManagementController getTutorThemeListManagementController() {
		return tutorThemeListManagementController;
	}

	public void setTutorThemeListManagementController(
			TutorThemeListManagementController tutorThemeListManagementController) {
		this.tutorThemeListManagementController = tutorThemeListManagementController;
	}	
	
	public TutorStudentListManagementController getTutorStudentListManagementController() {
		return tutorStudentListManagementController;
	}

	public void setTutorStudentListManagementController(
			TutorStudentListManagementController tutorStudentListManagementController) {
		this.tutorStudentListManagementController = tutorStudentListManagementController;
	}

	
	/* ********  Controller Methods ******** */
	
	/**
	 * Tutor's main interface for managing groups
	 * @param request
	 * @param response
	 * @return ModelAndView corresponding to the main interface to manage groups
    */
	public ModelAndView indexGroup (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("tutor/index_group");
		
		/* 
		 * Group Id to be managed: when this method is invoked from the index_tutor, the group may have been changed
		 */ 
		if (request.getParameter("idgroup") != null) {
			currentGroup = tutorManagementService.getGroup(Long.valueOf(request.getParameter("idgroup")));
			
		    log.debug("Grupo seleccionado: "+currentGroup.getId().toString());
		}
		
		// Addition of the group object:
		mav.addObject("group",currentGroup);
		
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		
		return mav;
	}
	
	/**
	 * Tutor's main interface for managing groups
	 * @param request
	 * @param response
	 * @return ModelAndView corresponding to the main interface to manage groups
    */
	public ModelAndView importGroup (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("tutor/index_group");
		//despues de copiarlo iré al index_group.Estaremos dentro del grupo en el que hemos importado.
		
		/* 
		 * En groupid esta el grupo que hay que importar al currentgroup
		 * 
		 */ 
		if (request.getParameter("idgroup") != null) {
			//tutorManagementService.importGroup le pasamos el idgroup del grupo que queremos que sea importado y el currentgroup al que lo tenemos que importar.Queremos que nos devuelva el grupo actual en el que estamos que seguira siendo el mismo que currentgroup
			
			Group sourceGroup=tutorManagementService.getGroup(Long.valueOf(request.getParameter("idgroup")));
			Group destinationGroup=currentGroup;
			 if (!tutorManagementService.syllabusCopy(sourceGroup,destinationGroup)){
				 log.debug("SE HA PRODUCIDO UN ERROR COPIANDO GRUPO. ATENCIÓN: NO SE HA COPIADO");	
				 mav.addObject("errorKey","importado correctamente");
			 }
			currentGroup=tutorManagementService.getGroup(currentGroup.getId());
		    log.debug("El Grupo: "+request.getParameter("idgroup").toString()+" se ha importado en el grupo "+currentGroup.getId().toString());
		}
		
		// Addition of the group object:
		mav.addObject("group",currentGroup);
		
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		mav.addObject("successKey","importado correctamente");
		return mav;
	}
 
	
	/**
	 * Shows the template for the addition of a new question 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView newQuestion (HttpServletRequest request, HttpServletResponse response) {
		if(currentGroup==null){
			long idGroup = -1;
			try{
				idGroup = Long.parseLong(request.getParameter("idgroup"));
			}catch(Exception e){
				log.error("No se encuentra el grupo actual");
				ModelAndView mav = new ModelAndView("error");
				return mav;
			}
			if(idGroup!=-1)
				currentGroup = tutorManagementService.getGroup(idGroup);
		}
		// We have to obtain from the database the list of themes for the course of this group 
		List<TestSubject> thlist = tutorManagementService.getCourseSubjects(currentGroup);

		// The currentQuestion is new:
		tutorQuestionManagementController.setCurrentQuestion(new Item());
		// And the currentAnswer also is new
		tutorQuestionManagementController.setCurrentAnswer(new ItemResponse());
		// Set the group
		tutorQuestionManagementController.setCurrentGroup(currentGroup);
		
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("tutor/new_question");
		
		// Addition of the group object:
		mav.addObject("group",currentGroup);
		// Addtion of the themes:
		mav.addObject("themes",thlist);
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);

		
		return mav;
	}	
    
	
	/**
	 * Shows the template for the edition of a question 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView editQuestion (HttpServletRequest request, HttpServletResponse response) {
		// The same view as the new question one, but with the currentQuestion precharged 
		
		// We have to obtain the question from the id:
		Item qfromdb = new Item(); 
		if (request.getParameter("idquestion") != null) {
			qfromdb.setId(Long.valueOf(request.getParameter("idquestion")));
			qfromdb = tutorManagementService.getQuestionFromId(qfromdb);
			log.debug("Editando pregunta "+qfromdb.getId().toString());
		} else {
			log.error("ERROR editando pregunta");
		}
		
		// The rest of the elements for the view have to be added:
		// List of themes for the course of this group 
		List<TestSubject> thlist = tutorManagementService.getCourseSubjects(currentGroup);

		// CurrentQuestion:
		tutorQuestionManagementController.setCurrentQuestion(qfromdb);
		// The currentAnswer is new
		tutorQuestionManagementController.setCurrentAnswer(new ItemResponse());
		// Set the group
		tutorQuestionManagementController.setCurrentGroup(currentGroup);
		
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("tutor/new_question");
		
		// Addition of the group object:
		mav.addObject("group",currentGroup);
		// Addtion of the themes:
		mav.addObject("themes",thlist);
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		Item question = tutorQuestionManagementController.getCurrentQuestion();
		if(question!= null && question.getType()==1){
			if(question.getAnswers()!=null && question.getAnswers().size()==1){
				mav.addObject("fillAnswer", question.getAnswers().get(0));
				tutorQuestionManagementController.setCurrentAnswer(question.getAnswers().get(0));
			} 
		}
		// We are EDITING THE CURRENT QUESTION. These parameters are NOT included in the new questions
		mav.addObject("question",question);
		return mav;
	}	
	
	
	/**
	 * Shows the list of questions related to this group
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView showQuestionsList (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("tutor/questions_list");

		// We have to obtain from the database the list of questions related to this group, sorted by theme (default order):
		Query queryQuestions = new Query();
		queryQuestions.setGroup(currentGroup.getId());
		queryQuestions.setMaxResultCount(100);
		tutorManagementService.updateQuestionNotUsedInExam(currentGroup.getId());
		List<Item> tqlist = tutorManagementService.find(queryQuestions);
		
		// We have to obtain from the database the list of themes for the course of this group 
		List<TestSubject> thlist = tutorManagementService.getCourseSubjects(currentGroup);

		// Setting the question list into the question list controller
		tutorQuestionListManagementController.setCurrentQuestionList(tqlist);
		
		// Addition of the group object:
		mav.addObject("group",currentGroup);
		// Addtion of the questions:
		mav.addObject("questions",tqlist);
		// Addtion of the themes:
		mav.addObject("themes",thlist);
		
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		return mav;
	} // showQuestionsList
		
	/**
	 * Shows the template for the configuration of a new exam 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView newExam (HttpServletRequest request, HttpServletResponse response) {

		// We have to obtain from the database the list of themes for the course of this group 
		List<TestSubject> thlist = tutorManagementService.getCourseSubjects(currentGroup);

		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("tutor/new_exam");
		TestSessionTemplate ex = new TestSessionTemplate();
		ex.setGroup(currentGroup);
		// New currentTutorConfigExam
		tutorExamManagementController.setCurrentTutorExam(ex);
		// Course Subjects List
		// TODO: this list is used to identify the subject for the exam. They should be read from the id
		tutorExamManagementController.setCourseSubjectsList(thlist);
		List<User> usersNotInCustomExam = tutorManagementService.getStudents(currentGroup);
		// Addition of the group object:
		mav.addObject("group",currentGroup);
		mav.addObject("usersNotInCustomExam", usersNotInCustomExam);
		// Addtion of the themes:
		mav.addObject("themes",thlist);
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		
		return mav;
	}	


	/**
	 * Shows the template for the edition of a exam configuration 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView editConfigExam (HttpServletRequest request, HttpServletResponse response) {
		// The same view as the new exam one, but with the currentTutorExam precharged 
		
		// We have to obtain the exam configuration from the id:
		TestSessionTemplate exfromdb = new TestSessionTemplate(); 
		if (request.getParameter("idconfigexam") != null) {
			exfromdb.setId(Long.valueOf(request.getParameter("idconfigexam")));
			exfromdb = tutorManagementService.getConfigExamFromId(exfromdb);
			log.debug("Editando configuración "+exfromdb.getId().toString());
		} else {
			log.error("ERROR editando configuración de examen");
		}
		
		// The rest of the elements for the view have to be added:
		// List of themes for the course of this group 
		List<TestSubject> thlist = tutorManagementService.getCourseSubjects(currentGroup);
		List<User> usersNotInCustomExam = tutorManagementService.getUsersNotInCustomExam(exfromdb.getId(),currentGroup.getId());
		List<CustomExamUser> usersInCustomExam = tutorManagementService.getUsersInCustomExam(exfromdb.getId());
		// Set the current exam configuration:
		if(exfromdb!=null)
			tutorExamManagementController.setCurrentTutorExam(exfromdb);
		else{
			return new ModelAndView("error");
		}
		// Course Subjects List
		// TODO: this list is used to identify the subject for the exam. They should be read from the id
		tutorExamManagementController.setCourseSubjectsList(thlist);
		
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("tutor/new_exam");
		mav.addObject("usersNotInCustomExam",usersNotInCustomExam);
		mav.addObject("usersInCustomExam", usersInCustomExam);
		// Addition of the group object:
		mav.addObject("group",currentGroup);
		// Addtion of the themes:
		mav.addObject("themes",thlist);
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		
		// We are EDITING THE CURRENT EXAM CONFIGURATION. These parameters are NOT included in the new exam configurations
		mav.addObject("exam",tutorExamManagementController.getCurrentTutorExam());
		// Config exam subjects
		mav.addObject("cesubjects",tutorExamManagementController.getCurrentTutorExam().getSubjects());
		
		return mav;
	}	
	
	
	/**
	 * Shows the list of exam configurations related to this group
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView showExamsList (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("tutor/exams_list");

		// We have to obtain from the database the list of exams related to this group, sorted by theme (default order):
		List<TestSessionTemplate> telist = tutorManagementService.getGroupConfigExams(currentGroup,"title");
		// We have to obtain from the database the list of themes for the course of this group 
		List<TestSubject> thlist = tutorManagementService.getCourseSubjects(currentGroup);

		// Setting the exam list into the exam list controller
		tutorExamListManagementController.setCurrentExamList(telist);
		
		// Addition of the group object:
		mav.addObject("group",currentGroup);
		// Addtion of the exams:
		mav.addObject("exams",telist);
		// Addtion of the themes:
		mav.addObject("themes",thlist);
		
		// Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		
		return mav;
	} // showExamsList
	
	
	/**
	 * Validate the current exam configuration
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView validateExam (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("tutor/exam_error_list");

		// We have to obtain the current exam configuration
		TestSessionTemplate currentConfigExam = tutorExamManagementController.getCurrentTutorExam();
		List<Message> msglist = new ArrayList<Message>();
		
		if (currentConfigExam != null) {
			msglist = tutorManagementService.validate(currentConfigExam);
		}
			
		// Addition of the group object:
		mav.addObject("group",currentGroup);
		// Addtion of the messages:
		mav.addObject("mlist",msglist);
		
		return mav;
	} // validateExam
	

	/**
	 * Shows the list of grades for the learner who made exams related to this group
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView showGradesList (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("tutor/grades_list");
		
		// We have to obtain from the database the list of grades related to this group, sorted by student (default order): use the query
		QueryGrade query = new QueryGrade();
		query.setMaxResultCount(100);
		query.setGroup(currentGroup.getId());
		List<TestSessionGrade> grlist = tutorManagementService.find(query);
		// We have to obtain from the database the list of students related to this group, sorted by surname:
		List<User> stlist = tutorManagementService.getStudents(currentGroup);
		// We have to obtain from the database the list of exams related to this group, sorted by title
		List<TestSessionTemplate> celist = tutorManagementService.getGroupConfigExams(currentGroup, "title");		
		tutorGradeListManagementController.setCurrentGradeList(grlist);
		// Addition of the group object:
		mav.addObject("group",currentGroup);
		// Addtion of the grades:
		mav.addObject("grades",grlist);
		// Addtion of the students:
		mav.addObject("students",stlist);
		// Addtion of the exams:
		mav.addObject("exams",celist);
		
		// Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		
		return mav;
	} // showGradesList
	
	
	/**
	 * Shows the list of themes that conform the syllabus for this group
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView showSyllabus (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("tutor/themes_list");

		// We have to obtain from the database the list of themes for the course of this group 
		List<Subject> thlist = new ArrayList<Subject>(); 
		thlist = tutorManagementService.getSubjects(currentGroup);
	
		// Setting the theme list into the theme list controller
		tutorThemeListManagementController.setCurrentThemeList(thlist);

		// Setting of the current group to relate the new themes:
		tutorThemeListManagementController.setCurrentGroup(currentGroup);
		
		// Addition of the group object:
		mav.addObject("group",currentGroup);
		// Addtion of the themes:
		mav.addObject("themes",thlist);
		
		// Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		
		return mav;
	} // showSyllabus
	
	/**
	 * Shows the index statistics page related to this group
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView showIndexStats (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("tutor/index_stats");
		
		// Addition of the group object:
		mav.addObject("group",currentGroup);
		
		// Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		
		return mav;
	} // showIndexStats

	/**
	 * Shows stats for exams
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView showStatsExams (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("tutor/exams_stats");

		// Addition of the group object:
		mav.addObject("group",currentGroup);

		// Get the stats
		List<ExamStats> stats = tutorManagementService.getExamStatsByGroup(currentGroup);
		// Order if asked
		String orderby = request.getParameter("orderby");
		if (orderby != null) {
			if (orderby.equals("title"))
				Collections.sort(stats,new ExamStatsTitleComparator());
			else if (orderby.equals("duration"))
				Collections.sort(stats,new ExamStatsDurationComparator());
			else if (orderby.equals("maxGrade"))
				Collections.sort(stats,new ExamStatsMaxGradeComparator());
			else if (orderby.equals("learnersNumber"))
				Collections.sort(stats,new ExamStatsLearnersNumberComparator());
			else if (orderby.equals("doneNumber"))
				Collections.sort(stats,new ExamStatsDoneNumberComparator());			
			else if (orderby.equals("undonePercentage"))
				Collections.sort(stats,new ExamStatsUndonePercentageComparator());
			else if (orderby.equals("failedPercentage"))
				Collections.sort(stats,new ExamStatsFailedPercentageComparator());
			else if (orderby.equals("passedPercentage")){
				Collections.sort(stats,new ExamStatsPassedPercentageComparator());
			}else if (orderby.equals("duration"))
				Collections.sort(stats,new ExamStatsDurationComparator());
			else if (orderby.equals("failedPercentage"))
				Collections.sort(stats,new ExamStatsFailedPercentageComparator());
			else if (orderby.equals("gradeAverage"))
				Collections.sort(stats,new ExamStatsGradeAverageComparator());
			else if (orderby.equals("gradeMax"))
				Collections.sort(stats,new ExamStatsGradeMaxComparator());
			else if (orderby.equals("gradeMin"))
				Collections.sort(stats,new ExamStatsGradeMinComparator());
			else if (orderby.equals("gradeMedian"))
				Collections.sort(stats,new ExamStatsGradeMedianComparator());
			else if (orderby.equals("gradeStandardDeviation"))
				Collections.sort(stats,new ExamStatsGradeStandardDeviationComparator());
			else if (orderby.equals("timeAverage"))
				Collections.sort(stats,new ExamStatsTimeAverageComparator());
			else if (orderby.equals("timeMax"))
				Collections.sort(stats,new ExamStatsTimeMaxComparator());
			else if (orderby.equals("timeMin"))
				Collections.sort(stats,new ExamStatsTimeMinComparator());
			else if (orderby.equals("timeMedian"))
				Collections.sort(stats,new ExamStatsTimeMedianComparator());
			else if (orderby.equals("timeStandardDeviation"))
				Collections.sort(stats,new ExamStatsTimeStandardDeviationComparator());
			else if (orderby.equals("customized"))
				Collections.sort(stats,new ExamStatsCustomized());
			mav.addObject("orderby",orderby);
		}
		// Reverse if asked
		String reverse = request.getParameter("reverse");
		if (reverse!=null) {
			Collections.reverse(stats);
			mav.addObject("reverse","yes");
		}
		mav.addObject("stats",stats);
		
		// Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		
		// View selected
		String view = request.getParameter("view");
		if (view == null) view = "global";
		if (!(view.equals("grade") || view.equals("time"))) view = "global";
		mav.addObject("view",view);
		
		return mav;
	} // showStatsExams
	
	/**
	 * Shows stats for learners' outcome in exams
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView showStatsLearners (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("tutor/learners_stats");

		// Addition of the group object:
		mav.addObject("group",currentGroup);

		// Get the stats
		List<LearnerStats> stats = tutorManagementService.getLearnerStatsByGroup(currentGroup); 
		// Order if asked
		String orderby = request.getParameter("orderby");
		if (orderby != null) {
			if (orderby.equals("fullName"))
				Collections.sort(stats,new LearnerStatsFullNameComparator());
			else if (orderby.equals("examsNumber"))
				Collections.sort(stats,new LearnerStatsExamsNumberComparator());
			else if (orderby.equals("passedNumber"))
				Collections.sort(stats,new LearnerStatsPassedNumberComparator());
			else if (orderby.equals("gradeAverage"))
				Collections.sort(stats,new LearnerStatsGradeAverageComparator());
			else if (orderby.equals("gradeMax"))
				Collections.sort(stats,new LearnerStatsGradeMaxComparator());
			else if (orderby.equals("gradeMin"))
				Collections.sort(stats,new LearnerStatsGradeMinComparator());
			else if (orderby.equals("gradeMedian"))
				Collections.sort(stats,new LearnerStatsGradeMedianComparator());
			else if (orderby.equals("gradeStandardDeviation"))
				Collections.sort(stats,new LearnerStatsGradeStandardDeviationComparator());
			mav.addObject("orderby",orderby);
		}
		// Reverse if asked
		String reverse = request.getParameter("reverse");
		if (reverse!=null) {
			Collections.reverse(stats);
			mav.addObject("reverse","yes");
		}
		mav.addObject("stats",stats);
		
		// Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		
		return mav;
	} // showStatsLearners

	/**
	 * Shows stats for questions appeared in exams
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView showStatsQuestions (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("tutor/questions_stats");
		
		// Breadcrumb
		BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
		breadCrumb.addStep(currentGroup.getCourse().getName()+" ("+currentGroup.getName()+")",request.getContextPath()+"/tutor/managegroup.itest?method=indexGroup");
		breadCrumb.addBundleStep("titleIndexStats",request.getContextPath()+"/tutor/managegroup.itest?method=showIndexStats");
		breadCrumb.addBundleStep("titleQuestionsStats","");
		request.setAttribute("breadCrumb",breadCrumb);

		// Addition of the group object:
		mav.addObject("group",currentGroup);

		//Setting the method to call when a sort is required
		String method = "showStatsQuestions";
		mav.addObject("method",method);
	
		// Get the stats
		List<QuestionStats> stats = tutorManagementService.getQuestionStatsByGroup(currentGroup); 
		// Order if asked
		String orderby = request.getParameter("orderby");
		if (orderby != null) {
			if (orderby.equals("text"))
				Collections.sort(stats,new QuestionStatsTextComparator());
			else if (orderby.equals("id"))
				Collections.sort(stats,new QuestionStatsIDComparator());
			else if (orderby.equals("subject"))
				Collections.sort(stats,new QuestionStatsSubjectComparator());
			else if (orderby.equals("appearances")&& request.getParameter("view").equalsIgnoreCase("answerMarked"))
				Collections.sort(stats,new QuestionStatsAppearancesComparator());
			else if (orderby.equals("answers"))
				Collections.sort(stats,new QuestionStatsAnswersComparator());
			else if (orderby.equals("successPercentage"))
				Collections.sort(stats,new QuestionStatsSuccessPercentageComparator());
			else if (orderby.equals("failPercentage"))
				Collections.sort(stats,new QuestionStatsFailPercentageComparator());
			else if (orderby.equals("unansweredPercentage"))
				Collections.sort(stats,new QuestionStatsUnansweredPercentageComparator());
			else if (orderby.equals("spectiveEasiness"))
				Collections.sort(stats,new QuestionStatsSpectiveEasinessComparator());
			else if (orderby.equals("trueEasiness"))
				Collections.sort(stats,new QuestionStatsTrueEasinessComparator());
			else if (orderby.equals("insecurityEasiness"))
				Collections.sort(stats,new QuestionStatsInsecurityEasinessComparator());
			else if (orderby.equals("appearances")&& request.getParameter("view").equalsIgnoreCase("confidenceLevelView"))
				Collections.sort(stats,new QuestionStatsConfidenceLevelAppearancesComparator());
			else if (orderby.equals("confidenceLevelAnswers")&& request.getParameter("view").equalsIgnoreCase("confidenceLevelView"))
				Collections.sort(stats,new QuestionStatsConfidenceLevelAnswerComparator());
			mav.addObject("orderby",orderby);
		}
		String view = request.getParameter("view");
		if(view!=null){
			mav.addObject("view",view);
		}else{
			mav.addObject("view","answerMarked");
		}
		// Reverse if asked
		String reverse = request.getParameter("reverse");
		if (reverse!=null) {
			Collections.reverse(stats);
			mav.addObject("reverse","yes");
		}
		mav.addObject("stats",stats);
		
		// Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		
		return mav;
	} // showStatsQuestions
	
	/**
	 * Shows stats for questions appeared one exam
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView showStatsQuestionsByExam (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("tutor/questions_stats");
		// Getting the exam
		TestSessionTemplate exam = new TestSessionTemplate();
		String examid = request.getParameter("examid");
		exam.setId(new Long(examid));
		exam = tutorManagementService.getConfigExamFromId(exam);
		String view = request.getParameter("view");
		// Breadcrumb
		BreadCrumb breadCrumb = new BreadCrumb(request.getHeader("Accept-Language"));
		breadCrumb.addStep(currentGroup.getCourse().getName()+" ("+currentGroup.getName()+")",request.getContextPath()+"/tutor/managegroup.itest?method=indexGroup");
		breadCrumb.addBundleStep("titleIndexStats",request.getContextPath()+"/tutor/managegroup.itest?method=showIndexStats");
		breadCrumb.addStep(exam.getTitle(), "");
		breadCrumb.addBundleStep("titleQuestionsStats","");
		request.setAttribute("breadCrumb",breadCrumb);

		// Addition of the group object:
		mav.addObject("group",currentGroup);
		
		//Setting the method to call when a sort is required
		String method = "showStatsQuestionsByExam&examid=" + examid;
		mav.addObject("method",method);

		// Get the stats
//		List<QuestionStats> stats = tutorManagementService.getQuestionStatsByGroup(currentGroup);
		List<QuestionStats> stats = tutorManagementService.getQuestionStatsByExam(exam); 
		// TODO: obtener las estadísticas del examen
		// Order if asked
		String orderby = request.getParameter("orderby");
		if (orderby != null) {
			if (orderby.equals("text"))
				Collections.sort(stats,new QuestionStatsTextComparator());
			else if (orderby.equals("id"))
				Collections.sort(stats,new QuestionStatsIDComparator());
			else if (orderby.equals("subject"))
				Collections.sort(stats,new QuestionStatsSubjectComparator());
			else if (orderby.equals("appearances"))
				Collections.sort(stats,new QuestionStatsAppearancesComparator());
			else if (orderby.equals("answers"))
				Collections.sort(stats,new QuestionStatsAnswersComparator());
			else if (orderby.equals("successPercentage"))
				Collections.sort(stats,new QuestionStatsSuccessPercentageComparator());
			else if (orderby.equals("failPercentage"))
				Collections.sort(stats,new QuestionStatsFailPercentageComparator());
			else if (orderby.equals("unansweredPercentage"))
				Collections.sort(stats,new QuestionStatsUnansweredPercentageComparator());
			else if (orderby.equals("spectiveEasiness"))
				Collections.sort(stats,new QuestionStatsSpectiveEasinessComparator());
			else if (orderby.equals("trueEasiness"))
				Collections.sort(stats,new QuestionStatsTrueEasinessComparator());
			else if (orderby.equals("insecurityEasiness"))
				Collections.sort(stats,new QuestionStatsInsecurityEasinessComparator());
			else if (orderby.equals("appearances")&& request.getParameter("view").equalsIgnoreCase("confidenceLevelView"))
				Collections.sort(stats,new QuestionStatsConfidenceLevelAppearancesComparator());
			else if (orderby.equals("confidenceLevelAnswers"))
				Collections.sort(stats,new QuestionStatsConfidenceLevelAnswerComparator());
			mav.addObject("orderby",orderby);
		}
		// Reverse if asked
		String reverse = request.getParameter("reverse");
		if (reverse!=null) {
			Collections.reverse(stats);
			mav.addObject("reverse","yes");
		}
		if(view == null)
			view ="answerMarked";
		mav.addObject("stats",stats);
		mav.addObject("view",view);
		
		// Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		
		return mav;
	} // showStatsQuestionsByExam


	/**
	 * Shows a preview of the current exam configuration
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView previewExam (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		// The error list is used by default, because may be some errors
		ModelAndView mav = new ModelAndView("tutor/exam_error_list");

		// We have to obtain the current exam configuration
		TestSessionTemplate currentConfigExam = tutorExamManagementController.getCurrentTutorExam();
 		// The exam is validate before generating a preview 
		List<Message> msglist = new ArrayList<Message>();
		
		if (currentConfigExam != null) {
			msglist = tutorManagementService.validate(currentConfigExam);
			
			// The preview is generated only if there are no errors
			if (msglist.isEmpty()) {
				mav.setViewName("tutor/exam_preview");
				//Exam preview = tutorManagementService.getExamPreview(currentConfigExam);
				TestSession preview = learnerManagementService.createTestSessionPreview(currentConfigExam.getId()); 
				// Adding the exam
				mav.addObject("exam",preview);
				// Store the exam in the session
				request.getSession().setAttribute("examPreview", preview);
				// User data from the session object. To be included at the header of the preview.
				User user = (User) request.getSession().getAttribute(Constants.USER);
				mav.addObject("user",user);
			}
		} 
			
		// Addition of the group object:
		mav.addObject("group",currentGroup);
		// Addtion of the messages:
		mav.addObject("mlist",msglist);
		
		return mav;
	} // previewExam
	
	/**
	 * Grades a previewed exam
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView gradePreviewedExam (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("tutor/grade_preview");
		
		TestSession exam = (TestSession) request.getSession().getAttribute("examPreview");
		// iterating on questions
		ListIterator<ItemSession> iterQuestions = exam.getQuestions().listIterator();
		while (iterQuestions.hasNext()) {
			ItemSession question = iterQuestions.next();
			String check = request.getParameter("confidenceQuestion"+question.getId());
			if (check!=null) {
				question.setExamineeWasConfident(true);
			}
			// iterating on answers
			ListIterator<ItemSessionResponse> iterAnswers = question.getAnswers().listIterator();
			while (iterAnswers.hasNext()) {
				ItemSessionResponse answer = iterAnswers.next();
				String marked = request.getParameter("check"+answer.getId());
				if (marked!=null) {
					answer.setMarked(true);
				}
				String fill = request.getParameter("inputFillAnswer"+answer.getId());
				if(fill!=null){
					question.setLearnerFillAnswer(fill);
				}
			}
		}
		
		// Adición de la calificación:
		Double grade = learnerManagementService.gradeTestSessionPreview(exam);
		mav.addObject("grade",grade);
		mav.addObject("maxgrade",exam.getMaxGrade());
		
		// Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		
		// Delete exam from session
		request.getSession().removeAttribute("examPreview");
		
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
		boolean regrade = false;
		if(request.getParameter("reGrade")!=null){
			try{
				regrade = Boolean.parseBoolean(request.getParameter("reGrade"));
			}catch(Exception e){
				
			}
		}
		if ((idStd != null) && (idConfigExam != null)) {
			// Obtention of the exam from the database.
			TestSession ex = tutorManagementService.getStudentExam(idStd,idConfigExam);
			if(regrade){
				double grade = learnerManagementService.reGradeExam(idStd, ex);
				ex.setExamGrade(grade);
			}
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
		
		// Addition of the group object:
		mav.addObject("group",currentGroup);
		
		return mav;
	} // showStudentExam
	

	/**
	 * Shows the list of students related to this group
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView showStudentsList (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("tutor/students_list");
		
		// We have to obtain from the database the list of students for this group
		// Sorted by Surname
		List<User> stlist = tutorManagementService.getStudents(currentGroup);

		// And the students that are not registered in this group (but in the current center)
		// If the group has a studentType, then only those of the specified role
		List<User> otherStList = null;
		if (currentGroup.getStudentType().intValue() == 1)
			otherStList = tutorManagementService.getStudentsNotRegistered(currentGroup,"LEARNER");
		else if (currentGroup.getStudentType().intValue() == 2)
			otherStList = tutorManagementService.getStudentsNotRegistered(currentGroup,"KID");
		else
			otherStList = tutorManagementService.getStudentsNotRegistered(currentGroup);
		
		// Setting the current group into the student list controller, and the order criteria
		tutorStudentListManagementController.setCurrentGroup(currentGroup);
		tutorStudentListManagementController.setCurrentOrder(Constants.SURNAME);
		
		// Setting the student list into the student list controller
		tutorStudentListManagementController.setCurrentStudentList(stlist);
		
		// Addition of the group object:
		mav.addObject("group",currentGroup);
		// Addtion of the students:
		mav.addObject("students",stlist);
		// Addtion of the rest of students:
		mav.addObject("otherStudents",otherStList);
		
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		
		// The group is needed !!
		mav.addObject("group",currentGroup);
		
		return mav;
	} // showStudentsList
	
	
	/**
	 * Shows the list of students to import
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView showImportStudents(HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("tutor/studens_import");
		User user = (User) request.getSession().getAttribute(Constants.USER);
		// We have to obtain from the database the list of groups wich we can import students
		List<Group> gplist = tutorManagementService.getTeachedGroups(user.getId());
		//Remove current group for the import group list
		for(int i=0;i<gplist.size();i++){
			if(gplist.get(i).getId().compareTo(currentGroup.getId())==0)
				gplist.remove(i);
		}
		// Addition of the group object:
		mav.addObject("group",currentGroup);
		
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		
		mav.addObject("user",user);
		mav.addObject("gplist",gplist);
		// The group is needed !!
		mav.addObject("group",currentGroup);
		
		return mav;
	} // showStudentsList
	
	/**
	 * Shows the interface to import questions 
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView showImportQuestions (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("tutor/questions_imports");
		
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		
		// We have to obtain from the database the list of questions related to this user, but not in this
		// group:
		Query queryQuestions = new Query();
		queryQuestions.setExcludeGroup(currentGroup.getId());
		queryQuestions.setUserId(user.getId());
		
		List<Item> tqlist = tutorManagementService.find(queryQuestions);
		
		// Setting the question list into the question list controller
		tutorQuestionListManagementController.setCurrentQuestionList(tqlist);
		
		// The preimported question list has to be initialized:
		tutorQuestionListManagementController.setPreImportedQuestionList(new ArrayList<Item>());
			
		// We have to obtain from the database the list of themes for the course of this group
		// The theme is used to decided where to import the questions.
		List<TestSubject> thlist = tutorManagementService.getCourseSubjects(currentGroup);
		
		// We also need to know the groups teached by the user:
		List<Group> mygroups = tutorManagementService.getTeachedGroups(user.getId());
		
		// Addition of the group object:
		mav.addObject("group",currentGroup);
		// Addtion of the questions:
		mav.addObject("questions",tqlist);
		// Addtion of the themes:
		mav.addObject("themes",thlist);
		// Addtion of the teached groups:
		mav.addObject("mygroups",mygroups);		
		
		return mav;
	} // showImportQuestions
	
	
	/**
	 * Shows an "under construction" page
	 * @param request
	 * @param response
	 * @return ModelAndView
	 */
	public ModelAndView underConstruction (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("tutor/underc");
		
		return mav;
	}

	public void reviewExam2PDF(HttpServletRequest request,HttpServletResponse response) {
		TestSession currentExam = (TestSession)request.getSession().getAttribute("examPreview");
		try {
			String archivo = currentExam.getTitle()+"_"+currentExam.getGroup().getCourse().getName()+"_"+currentExam.getId()+".pdf";
			log.info("generando el archivo "+archivo);
			ByteArrayOutputStream pdfByte = learnerManagementService.parse2PDF(currentExam);
			log.info("generado el archivo "+archivo);
			response.setHeader("Content-disposition", "attachment; filename=\""+archivo+"\""); 
			response.setContentType("application/pdf");
			response.setContentLength(pdfByte.size());
			ServletOutputStream out = response.getOutputStream();
			pdfByte.writeTo(out);
			out.flush();
		}
		catch(DocumentException e1) {
			log.error("Se ha producido un error al intentar generar el PDF con la revision del examen "+currentExam.getId()+"\n"+e1.getLocalizedMessage());
		}
		catch(IOException e2) {
			log.error("Se ha producido un error de E/S cuando se intentaba generar el PDF con la revision del examen "+currentExam.getId()+"\n"+e2.getLocalizedMessage());
		}
		catch(NullPointerException e3) {
			log.error("Se ha producido un error cuando se intentaba generar el PDF con la revision de un examen: EXAMEN NO VALIDO");
		}
		log.info("Se ha generado correctamente el PDF con la revision del examen "+currentExam.getId())	;	
	}
	
	/**
	 * Shows the incidents page
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * */
	public ModelAndView showIncidents(HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		User user = (User) request.getSession().getAttribute(Constants.USER);
		ModelAndView mav = new ModelAndView("tutor/incidents");
		mav.addObject("group",currentGroup);
		mav.addObject("user",user);
		return mav;
	}
	
	public ModelAndView importStudentFromFile(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("tutor/import_user_file");
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("group",currentGroup);
		mav.addObject("user",user);
		tutorStudentListManagementController.setCurrentStudentList(new ArrayList<User>());
		tutorStudentListManagementController.setCurrentGroup(currentGroup);
		return mav;
	}
	
	public ModelAndView getCSVTemplate(HttpServletRequest request, HttpServletResponse response){
		getTemplateFile(request, response, "plantilla.csv");
		return null;
	}
	
	public ModelAndView getXLSTemplate(HttpServletRequest request, HttpServletResponse response){
		getTemplateFile(request, response, "plantilla.xls");
		return null;
	}
	
	
	private void getTemplateFile(HttpServletRequest request, HttpServletResponse response, String filename){
		File file = new File(request.getSession().getServletContext().getRealPath("/")+Constants.MTEMPLATESPATH+filename);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if(fis!=null){
			response.setHeader("Content-Disposition:", "attachment;filename=" + file.getName() );
			BufferedInputStream bis= new BufferedInputStream(fis);
			ServletOutputStream sos = null;
			try {
				sos = response.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(sos!=null){
				byte[] buffer = new byte[5000];
				boolean isAviable = false;
				try {
					response.setHeader("Content-Length:", file.length()+"");
					isAviable = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(isAviable){
					while (true) {
						   int bytesRead;
						try {
							bytesRead = bis.read(buffer, 0, buffer.length);
							if (bytesRead < 0)
								   break;
							sos.write(buffer, 0, bytesRead);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					try{
						fis.close();
						sos.flush();
						sos.close();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				
			} 
		}
	}
}