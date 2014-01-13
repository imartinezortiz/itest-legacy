package com.cesfelipesegundo.itis.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import com.cesfelipesegundo.itis.biz.api.LearnerManagementService;
import com.cesfelipesegundo.itis.biz.api.TutorManagementService;
import com.cesfelipesegundo.itis.model.ConfigExam;
import com.cesfelipesegundo.itis.model.ConfigExamSubject;
import com.cesfelipesegundo.itis.model.CustomExamUser;
import com.cesfelipesegundo.itis.model.ExamForReview;
import com.cesfelipesegundo.itis.model.Group;
import com.cesfelipesegundo.itis.model.Subject;
import com.cesfelipesegundo.itis.model.TemplateExamQuestion;
import com.cesfelipesegundo.itis.model.TemplateExamSubject;
import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.model.comparators.ConfigExamSubjectComparator;
import com.cesfelipesegundo.itis.web.Constants;

/**
 * It manages the main operations related to the new or edited exams.
 * Works only with methods that DO NOT return ModelAndView objects 
 * 
 * @author rubén, chema
 *
 */
public class TutorExamManagementController {
	
	private static final Log log = LogFactory.getLog(TutorExamManagementController.class);
	
	/**
	 * Service needed to manage requests from tutor
	 */
    private TutorManagementService tutorManagementService;
    
    /**
	 * Service needed to manage requests from tutor
	 */
    private LearnerManagementService learnerManagementService;
    
    /**
     * Exam being managed (added or edited) by the tutor
     */
    private ConfigExam currentTutorExam;
    
    /**
     * List of Subjects
     */
    private List<TemplateExamSubject> courseSubjectsList;
    
    /* ******** Getters and setters ******** */
    
    

	public List<TemplateExamSubject> getCourseSubjectsList() {
		return courseSubjectsList;
	}

	public LearnerManagementService getLearnerManagementService() {
		return learnerManagementService;
	}

	public void setLearnerManagementService(
			LearnerManagementService learnerManagementService) {
		this.learnerManagementService = learnerManagementService;
	}

	public void setCourseSubjectsList(List<TemplateExamSubject> courseSubjectsList) {
		this.courseSubjectsList = courseSubjectsList;
	}

	public ConfigExam getCurrentTutorExam() {
		return currentTutorExam;
	}

	public void setCurrentTutorExam(ConfigExam currentTutorExam) {
		this.currentTutorExam = currentTutorExam;
	}

	public TutorManagementService getTutorManagementService() {
		return tutorManagementService;
	}

	public void setTutorManagementService(
			TutorManagementService tutorManagementService) {
		this.tutorManagementService = tutorManagementService;
	}


	/* ---------------------- Exam Configuration Management (Ajax) ------------------------ */
	
	
	public void setPublished(boolean isPublic){
		if(currentTutorExam!=null){
			currentTutorExam.setPublished(isPublic);
		}
	}
	
	/**
	 * Saves a new exam configuration to the database. Implements the "save" action of the tutor interface for a new exam
	 * @return True on a successful addition operation
	 */
	public Boolean saveConfigExam (String idgroup, String examTitle, String duration, String visibility, String startDate, String startTimeH,
								   String startTimeM, String endDate, String endTimeH, String endTimeM,String startDateRev, String startTimeHRev,
								   String startTimeMRev, String endDateRev, String endTimeHRev, String endTimeMRev, String maxGrade, String weight, boolean activeReview, boolean partialCorrection,boolean showNumCorrectAnswers, double penQuestionFailed, double penQuestionNotAnswered, double penAnswerFailed, double minQuestionGrade, double confidencePenalization, double confidenceReward, boolean customized, boolean published) {
		// All the attributes come to form the exam configuration
		// Transmission of data to the object
		// Group
		Long id = Long.parseLong(idgroup);
		// Get the current user's group 
		Group group = tutorManagementService.getGroup(id);
		currentTutorExam.setGroup(group);
		currentTutorExam.setPublished(published);
		// Other exam data
		currentTutorExam.setTitle(examTitle);
		try{
			currentTutorExam.setDuration(Integer.valueOf(duration));
		}catch(Exception e){
			currentTutorExam.setDuration(0);
		}
		currentTutorExam.setVisibility(Integer.valueOf(visibility));
		if (maxGrade.length() > 0) currentTutorExam.setMaxGrade(Double.valueOf(maxGrade));
		if (weight.length() > 0) currentTutorExam.setWeight(Integer.valueOf(weight));
		// Start date / time:
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy-HH:mm");
		Date auxStartDate = new Date();
		if(startDate!=null && startTimeH!=null && startTimeM!=null){
			String startDate2db = new String(startDate+"-"+startTimeH+":"+startTimeM);
			try {
				auxStartDate = sdf.parse(startDate2db);
			} catch (ParseException e) {

			}
		}
		currentTutorExam.setStartDate(auxStartDate);
		// Start Date revision
		String startDateRev2db = new String(startDateRev+"-"+startTimeHRev+":"+startTimeMRev);
		Date auxStartDateRev = new Date();
		try {
			auxStartDateRev = sdf.parse(startDateRev2db);
		} catch (ParseException e) {
			auxStartDateRev = new Date(0);
		}
		
		currentTutorExam.setStartDateRevision(auxStartDateRev);
		// End date /time:
		Date auxEndDate = new Date();
		if(endDate!=null && endTimeH!=null && endTimeM!=null){
			String endDate2db = new String(endDate+"-"+endTimeH+":"+endTimeM);
			try {
				auxEndDate = sdf.parse(endDate2db);
			} catch (ParseException e) {
				
			}
			
		}
		currentTutorExam.setEndDate(auxEndDate);
		
		// Start Date revision
		String endDateRev2db = new String(endDateRev+"-"+endTimeHRev+":"+endTimeMRev);
		Date auxEndDateRev = new Date();
		try {
			auxEndDateRev = sdf.parse(endDateRev2db);
		} catch (ParseException e) {
			auxEndDateRev = new Date(0);
		}
		
		currentTutorExam.setEndDateRevision(auxEndDateRev);
		// The review is not touch 
		
		//Saving new attributes December 2008 -Penalization Exam-
		try {
			currentTutorExam.setPartialCorrection(partialCorrection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			currentTutorExam.setShowNumCorrectAnswers(showNumCorrectAnswers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			currentTutorExam.setPenQuestionFailed(Double.valueOf(penQuestionFailed));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			currentTutorExam.setPenQuestionNotAnswered(Double.valueOf(penQuestionNotAnswered));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			currentTutorExam.setPenAnswerFailed(Double.valueOf(penAnswerFailed));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			currentTutorExam.setMinQuestionGrade(Double.valueOf(minQuestionGrade));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			currentTutorExam.setPenConfidenceLevel(confidencePenalization);
		} catch (Exception e){
			e.printStackTrace();
		}
		try{
			currentTutorExam.setRewardConfidenceLevel(confidenceReward);
		}catch(Exception e){
			e.printStackTrace();
		}
		auxStartDate = new Date(auxStartDate.getTime());
		auxStartDate.setMinutes(auxStartDate.getMinutes()+currentTutorExam.getDuration());
		/*if(currentTutorExam.getVisibility()==1){
			if(auxEndDate.compareTo(auxStartDate)<=0){
				return false;
			}
		}*/
		currentTutorExam.setCustomized(customized);
		if(!customized){
			List<CustomExamUser> users = tutorManagementService.getUsersInCustomExam(currentTutorExam.getId());
			for(int i=0;i<users.size();i++){
				tutorManagementService.removeUserFromCustomExam(users.get(i).getId(), currentTutorExam.getId());
			}
		}
		currentTutorExam.setActiveReview(activeReview);
		// Saving configuration into DB. Id is updated by the service.
		tutorManagementService.saveExam(currentTutorExam);
		
		log.debug("- Salvando configuración de examen "+currentTutorExam.getId());
		return true;
	} // saveConfigExam
	
	
	/**
	 * Includes a new theme in the exam configuration
	 * @return list of included themes
	 */
	public List<ConfigExamSubject> addConfigExamTheme(String idSubject, String answersxQuestionNumber, String maxDifficulty, String minDifficulty, String questionsNumber, String questionType) { 
		ConfigExamSubject subject = new ConfigExamSubject();
		subject.setAnswersxQuestionNumber(Integer.valueOf(answersxQuestionNumber));
		subject.setMaxDifficulty(Integer.valueOf(maxDifficulty));
		subject.setMinDifficulty(Integer.valueOf(minDifficulty));
		subject.setQuestionsNumber(Integer.valueOf(questionsNumber));
		subject.setQuestionType(Integer.valueOf(questionType));
		
		// Theme linked to the configuration
		Subject sub = new Subject();
		sub.setId(Long.valueOf(idSubject));
		// TODO: the data of the subject should come from the DB by ID
		// Find the title of the theme:
		Iterator<TemplateExamSubject> iter = courseSubjectsList.iterator();
		TemplateExamSubject tce = null;	// Subject
		boolean aFound = false;
		
		while (iter.hasNext() && (!aFound)) {
			tce = iter.next();
			if (tce.getId().equals(Long.valueOf(idSubject))) aFound = true;
		}
		
		if (!aFound) {
			// Subject not found
			log.error("- Tema "+idSubject+" NO encontrado");
		} else {
			// Title:
			sub.setSubject(tce.getSubject());
			sub.setOrder(tce.getSort());
		}
		// Set subject for the exam
		subject.setSubject(sub);
		
		// Saving ...
		tutorManagementService.saveSubjectToExam(currentTutorExam, subject);
		// Adding to current exam:
		if (currentTutorExam.getSubjects() == null) currentTutorExam.setSubjects(new ArrayList<ConfigExamSubject>());
		currentTutorExam.getSubjects().add(subject);
		
		Collections.sort(currentTutorExam.getSubjects(), new ConfigExamSubjectComparator());
		
		return currentTutorExam.getSubjects();

	} // addConfigExamTheme
	
	
	/**
	 * Deletes a theme from the exam configuration
	 * @return list of included themes
	 */
	public List<ConfigExamSubject> deleteConfigExamTheme(String idSubject,long idGroup) {
			
		// The theme can only be deleted when the exam configuration was saved before
		List<ConfigExamSubject> subjects = currentTutorExam.getSubjects();

        // Find the theme to be deleted:
		Iterator<ConfigExamSubject> iterSce = subjects.iterator();
		ConfigExamSubject sce = null;	// Subject config exam
		boolean aFound = false;
		
		while (iterSce.hasNext() && (!aFound)) {
			sce = iterSce.next();
			if (sce.getId().equals(Long.valueOf(idSubject))) aFound = true;
		}
		
		if (!aFound) {
			// Subject not found
			log.error("- Tema "+idSubject+" NO encontrado");
		} else {
			
			// Delete the subject from the database (has to be made first)
			tutorManagementService.deleteSubjectFromExam(currentTutorExam, sce, idGroup);
			
			// Removed from the list
			subjects.remove(sce);
			Collections.sort(currentTutorExam.getSubjects(), new ConfigExamSubjectComparator());

		}
		
		return  currentTutorExam.getSubjects();
	} // deleteConfigExamTheme

	
	
	public ModelAndView configExamCopy (HttpServletRequest request, HttpServletResponse response) {
		// The same view as the new exam one, but with the currentTutorExam precharged 
		// We have to copy the current Exam configuration 
		currentTutorExam.setTitle(currentTutorExam.getTitle()+" Copia");
		ConfigExam examenCopia = tutorManagementService.configExamCopy(currentTutorExam);
		log.info("Duplicado el examen: "+currentTutorExam.getId()+" al nuevo examen: "+examenCopia.getId());
		
		// The rest of the elements for the view have to be added:
		// List of themes for the course of this group 
		List<TemplateExamSubject> thlist = tutorManagementService.getCourseSubjects(currentTutorExam.getGroup());

		// Set the current exam configuration:
		this.setCurrentTutorExam(examenCopia);
		// Course Subjects List
		// TODO: this list is used to identify the subject for the exam. They should be read from the id
		this.setCourseSubjectsList(thlist);
		List<User> usersNotInCustomExam = tutorManagementService.getUsersNotInCustomExam(currentTutorExam.getId(), currentTutorExam.getGroup().getId());
		List<CustomExamUser> usersInCustomExam = tutorManagementService.getUsersInCustomExam(currentTutorExam.getId());
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("tutor/new_exam");
		mav.addObject("usersNotInCustomExam",usersNotInCustomExam);
		mav.addObject("usersInCustomExam", usersInCustomExam);
		// Addition of the group object:
		mav.addObject("group",currentTutorExam.getGroup());
		// Addtion of the themes:
		mav.addObject("themes",thlist);
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		
		// We are EDITING THE CURRENT EXAM CONFIGURATION. These parameters are NOT included in the new exam configurations
		mav.addObject("exam",this.getCurrentTutorExam());
		// Config exam subjects
		mav.addObject("cesubjects",this.getCurrentTutorExam().getSubjects());
		
		return mav;
	}	
	
	public List<ExamForReview> recorrectExam (long idExam){
		List<ExamForReview> reviewExam = learnerManagementService.examReviewByIdExam(idExam);
		return reviewExam;
	}
	
	public void activeConfidenceLevel(boolean activar){
		if(currentTutorExam != null){
			currentTutorExam.setConfidenceLevel(activar);
		}
	}
	
	public List<CustomExamUser> addUser2CustomExam(long idUser){
		if(currentTutorExam != null){
			tutorManagementService.addUser2CustomExam(currentTutorExam.getId(),idUser);
			return tutorManagementService.getUsersInCustomExam(currentTutorExam.getId());
		}
		return null;
	}

	public List<User> removeUserFromCustomExam(long userId, long groupId){
		if(currentTutorExam != null){
			tutorManagementService.removeUserFromCustomExam(userId,currentTutorExam.getId());
			return tutorManagementService.getUsersNotInCustomExam(currentTutorExam.getId(), groupId);
		}
		return null;
	}
	
	public List<User> getUsersNotInCustomExam(long groupId){
		if(currentTutorExam != null)
			return tutorManagementService.getUsersNotInCustomExam(currentTutorExam.getId(), groupId);
		return null;
	}
	
	public List<User> setCurrentExamCustomized(long groupId, boolean customized){
		Group group = tutorManagementService.getGroup(groupId);
		if(currentTutorExam!=null){
			currentTutorExam.setCustomized(customized);
			if(currentTutorExam.getGroup()==null)
				currentTutorExam.setGroup(group);
			if(!customized){
				List<CustomExamUser> users = tutorManagementService.getUsersInCustomExam(currentTutorExam.getId());
				for(CustomExamUser user : users){
					tutorManagementService.removeUserFromCustomExam(user.getId(), currentTutorExam.getId());
				}
			}
		}
		
		return tutorManagementService.getStudents(group);
	}
 }
