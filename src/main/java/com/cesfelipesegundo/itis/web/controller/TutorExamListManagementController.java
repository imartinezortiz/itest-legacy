package com.cesfelipesegundo.itis.web.controller;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cesfelipesegundo.itis.biz.api.TutorManagementService;
import com.cesfelipesegundo.itis.model.comparators.ConfigExamComparator;

import es.itest.engine.course.business.entity.Group;
import es.itest.engine.test.business.entity.TestSessionTemplate;

/**
 * It manages the operations related to LIST of exam configurations of the managed group
 * @author chema
 *
 */
public class TutorExamListManagementController {

	private static final Log log = LogFactory.getLog(TutorExamListManagementController.class);
	
	/**
	 * Service needed to manage requests from tutor
	 */
    private TutorManagementService tutorManagementService;
    
    /**
     * Exam configurations LIST being managed by the tutor
     */
    private List<TestSessionTemplate> currentExamList;
   
    /* ******** Getters and setters ******** */

	public TutorManagementService getTutorManagementService() {
		return tutorManagementService;
	}


	public void setTutorManagementService(
			TutorManagementService tutorManagementService) {
		this.tutorManagementService = tutorManagementService;
	}

	public List<TestSessionTemplate> getCurrentExamList() {
		return currentExamList;
	}


	public void setCurrentExamList(
			List<TestSessionTemplate> currentExamList) {
		this.currentExamList = currentExamList;
	}
	
	
    /* ---------------------- Exam configurations LIST Management (Ajax) ------------------------ */
	
	/**
	 * Deletes the exam configuration and all the related info from the database.
	 * Implements the "delete" action of the exam configurations list.
	 * @return List of exam configurations, needed for the callback function to repaint the list
	 */
	public List<TestSessionTemplate> deleteConfigExam (String idexam) {
		
        // Find the exam configuration to be deleted:
		Iterator<TestSessionTemplate> iterE = currentExamList.iterator();
		TestSessionTemplate exam = null;
		boolean aFound = false;
		
		while (iterE.hasNext() && (!aFound)) {
			exam = iterE.next();
			if (exam.getId().equals(Long.valueOf(idexam))) {
				aFound = true;
			}
		}
		
		if (!aFound) {
			// Exam not found
			log.error("- Configuración de examen "+idexam+" NO encontrada");
		} else {
			
			// Delete the exam configurarion from the database and all the related information
			tutorManagementService.deleteConfigExam(exam);
			
			// Removed from the list
			currentExamList.remove(exam);
						
			log.debug("- Borrando configuración de examen "+exam.getId()+": "+exam.getTitle());
		}
		
		return currentExamList;
		
	} // deleteConfigExam	

	/**
	 * Deletes an exam configuration list and all the related info from the database.
	 * @return List of exam configurations, needed for the callback function to repaint the list
	 */
	public List<TestSessionTemplate> deleteConfigExams (String[] exams) {
		for (int i = 0; i < exams.length; i++)
			deleteConfigExam(exams[i]);
		return currentExamList;		
	}

	
	/**
	 * Changes the value of the revision of the exam: true or not, depending on the value
	 * @return List of exam configurations, needed for the callback function to repaint the list
	 */
	public List<TestSessionTemplate> changeReviewExam (String idconfigexam, String value) {
		  // Find the exam configuration to be updated:
		Iterator<TestSessionTemplate> iter = currentExamList.iterator();
		TestSessionTemplate ce = null;
		boolean aFound = false;
		
		while (iter.hasNext() && (!aFound)) {
			ce = iter.next();
			if (ce.getId().equals(Long.valueOf(idconfigexam))) {
				aFound = true;
			}
		}
		
		if (!aFound) {
			// Exam configuration not found
			log.error("- Configuración de examen "+idconfigexam+" NO encontrada");
		} else {
			// Changes the revision state:
			if (value.equals("true")) ce.setActiveReview(true);
			else ce.setActiveReview(false);
			
			// Save exam configuration on the database:
			tutorManagementService.updateExamReview(ce);
			
			log.debug("- Cambio a "+value+" de revisión de "+ce.getId());
		}
		
		return currentExamList;
		
	} // changeReviewExam

	
	
	/**
	 * Applies for sorting exam configurations
	 * @return List of exam configurations sorted by "orderby", needed for the callback function to repaint the list
	 */
	public List<TestSessionTemplate> sort (String idgroup,String orderby, boolean reverse) {
		/* 
		 * We have to obtain from the database the list of exam configurations related to this group, and then we order
		 * the data using the parameter. 
		 */
		Group cGroup = new Group();
		cGroup.setId(Long.valueOf(idgroup));
		// Get the list, ordered by the selected field.
		List<TestSessionTemplate> tempCElist = tutorManagementService.getGroupConfigExams(cGroup,orderby);
		Collections.sort(tempCElist, new ConfigExamComparator(orderby));
		if(reverse){
			Collections.reverse(tempCElist);
		}
		
		// Setting the current list
		setCurrentExamList(tempCElist);
		
		return currentExamList;
		
	} // sort
	
	public void setExamVisible(long examId, int value){
		TestSessionTemplate exfromdb = new TestSessionTemplate();
		exfromdb.setId(examId);
		TestSessionTemplate configExam = tutorManagementService.getConfigExamFromId(exfromdb);
		configExam.setVisibility(value);
		tutorManagementService.saveExam(configExam);
	}

	
}
