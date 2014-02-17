package com.cesfelipesegundo.itis.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Date;

import com.cesfelipesegundo.itis.biz.api.LearnerManagementService;
import com.cesfelipesegundo.itis.biz.api.TutorManagementService;
import com.cesfelipesegundo.itis.model.Grade;
import com.cesfelipesegundo.itis.model.QueryGrade;
import com.cesfelipesegundo.itis.model.comparators.TemplateGradeComparator;

import es.itest.engine.test.business.entity.TestSession;
import es.itest.engine.test.business.entity.TestSessionGrade;

/**
 * It manages the operations related to LIST of grades of the managed group
 * @author chema
 *
 */
public class TutorGradeListManagementController {

	/**
	 * Service needed to manage requests from tutor
	 */
    private TutorManagementService tutorManagementService;
    
    /**
	 * Service needed to manage requests from learner
	 */
    private LearnerManagementService learnerManagementService;
    
    private List<TestSessionGrade> currentGradeList;
   
    /* ******** Getters and setters ******** */

    
	public TutorManagementService getTutorManagementService() {
		return tutorManagementService;
	}

	public List<TestSessionGrade> getCurrentGradeList() {
		return currentGradeList;
	}

	public void setCurrentGradeList(List<TestSessionGrade> currentGradeList) {
		this.currentGradeList = currentGradeList;
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

	
    /* ---------------------- Grade LIST Management (Ajax) ------------------------ */


	/**
	 * Applies filters and searches related to grades
	 * @return List of grades that comply with the filter, needed for the callback function to repaint the list
	 */
	
	public List<TestSessionGrade> filterAndSearch (String idgroup, String idstudent, String idexam, String grade,
			 									String startDate, String endDate, String dur, String orderby, String typeOrder,boolean limit) {
		/* 
		 * We have to obtain from the database the list of questions related to this group, using the
		 * data from the parameters. 
		 */
		QueryGrade queryGrades = new QueryGrade();
		if(typeOrder.equalsIgnoreCase("")){
			queryGrades.setTypeOrder("ASC");
		}else{
			queryGrades.setTypeOrder(typeOrder);
		}
		queryGrades.setGroup(Long.valueOf(idgroup));
		if (!idstudent.equals(new String(""))) queryGrades.setLearner(Long.valueOf(idstudent));
		if (!idexam.equals(new String(""))) queryGrades.setExam(Long.valueOf(idexam));
		if (!grade.equals(new String(""))) queryGrades.setGrade(Double.valueOf(grade));
		if (limit)
			queryGrades.setMaxResultCount(100);
		// Working with dates
		Date auxDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		if (!startDate.equals(new String(""))) {
			// Concatenate start hour: 00:00:00
			try {
				startDate = startDate.concat(" 00:00:00");
				auxDate = sdf.parse(startDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			queryGrades.setBegin(auxDate);
		}
		if (!endDate.equals(new String(""))) {
			// Concatenate end hour: 23:59:59
			try {
				endDate = endDate.concat(" 23:59:59");
				auxDate = sdf.parse(endDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			queryGrades.setEnd(auxDate);
		}
		if (!dur.equals(new String(""))) queryGrades.setTime(Integer.valueOf(dur));
		
		// Order:
		if (orderby.equals(new String("student"))) {
			queryGrades.setOrder(QueryGrade.OrderBy.STUDENT);
		} else {
			if (orderby.equals("exam")) {
				queryGrades.setOrder(QueryGrade.OrderBy.EXAMTITLE);
			} else {
				if (orderby.equals("grade")) {
					queryGrades.setOrder(QueryGrade.OrderBy.GRADE);
				} else {
					if (orderby.equals("startDate")) {
						queryGrades.setOrder(QueryGrade.OrderBy.STARTDATE);
					} else {
						if (orderby.equals("endDate")) {
							queryGrades.setOrder(QueryGrade.OrderBy.ENDDATE);
						} else {
							if (orderby.equals("duration")) {
								queryGrades.setOrder(QueryGrade.OrderBy.TIME);
							} else{
								if(orderby.equals("maxGrade")){
									queryGrades.setOrder(QueryGrade.OrderBy.MAXGRADE);
								}
							}
						}
					}
				}
			}
		}
		setCurrentGradeList(tutorManagementService.find(queryGrades));
		
		return currentGradeList;
		
	} // filterAndSearch

	public List<TestSessionGrade> orderCurrentGradeList(String typeOrder, String orderby){
		Collections.sort(currentGradeList, new TemplateGradeComparator(orderby));
		if(typeOrder.equalsIgnoreCase("DESC"))
			Collections.reverse(currentGradeList);
		return currentGradeList;
	}
	
	/**
	 * Deletes an exam done by a student and the obtained grade
	 * @return List of grades needed for the callback function to repaint the list
	 **/
	
	public List<TestSessionGrade> deleteStudentExam (String idgroup, String idstudent, String idexam) {

		// Getting input data:
		Long idgrp = null;
		Long idstd = null;
		Long idex = null;
		if (!idgroup.equals(new String(""))) idgrp = Long.valueOf(idgroup);
		if (!idstudent.equals(new String(""))) idstd = Long.valueOf(idstudent);
		if (!idexam.equals(new String(""))) idex = Long.valueOf(idexam);
		
		if ((idgrp != null) && (idstd != null) && (idex != null)) {
			// Deleting student grade and exam
			tutorManagementService.deleteStudentGradeAndExam(idstd,idex);
			tutorManagementService.updateQuestionNotUsedInExam(idgrp);
		}
		for(int i=0;i<currentGradeList.size();i++){
			TestSessionGrade gr = currentGradeList.get(i);
			if(gr.getLearner().getId().equals(idstd) && gr.getExam().getId().equals(idex)){
				currentGradeList.remove(gr);
				break;
			}
		}
		return currentGradeList;
	} // deleteStudentExam
	
	public List<TestSessionGrade> gradeExam(String idgroup, Long idUser, Long idExam){
		TestSession exam = learnerManagementService.getTestSession(idExam, idUser);
		Grade grade = learnerManagementService.getGradeByIdExam(idExam, idUser);
		if(grade!=null){
			exam.setStartingDate(grade.getBegin().getTime());
		}
		if(exam.getStartingDate()==0){
			exam.setStartingDate(System.currentTimeMillis()- (exam.getDuration()*60000));
		}
		
		grade.setGrade(learnerManagementService.gradeTestSession(idUser, exam));
		grade.setEnd(learnerManagementService.getGradeByIdExam(idExam, idUser).getEnd());
		for(int i=0;i<currentGradeList.size();i++){
			if(currentGradeList.get(i).getExam().getId().equals(idExam) && currentGradeList.get(i).getLearner().getId().equals(idUser)){
				currentGradeList.get(i).setGrade(grade.getGrade());
				currentGradeList.get(i).setEnd(grade.getEnd());
			}
		}
		return currentGradeList;
	}
}
