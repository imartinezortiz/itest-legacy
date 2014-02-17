package com.cesfelipesegundo.itis.dao.api;

import java.util.Date;
import java.util.List;

import com.cesfelipesegundo.itis.model.CourseStats;
import com.cesfelipesegundo.itis.model.ExamGlobalInfo;
import com.cesfelipesegundo.itis.model.Grade;
import com.cesfelipesegundo.itis.model.User;

import es.itest.engine.test.business.entity.Item;
import es.itest.engine.test.business.entity.ItemSession;
import es.itest.engine.test.business.entity.TestSession;
import es.itest.engine.test.business.entity.TestSessionForReview;

public interface ExamDAO extends DAO {
		
	/**
	 * Method to obtain the data of an already done exam: questions, answers,
	 * comments, grade, marked answers, correct answers, time employed...
	 * 
	 * @param user
	 *            that performed the exam
	 * @param idexam,
	 *            id of the configuration of the exam previously performed
	 * @return exam performed by the user
	 */
		
	public TestSession getAlreadyDoneExam(User user, Long id);
	
	/**
	 * Method to obtain the data of an already done exam: questions, answers,
	 * comments, grade, marked answers, correct answers, time employed...
	 * 
	 * @param idexam
	 *            id of the configuration of the exam previously performed
	 * @param iduser,
	 *            id of the user of the exam previously performed
	 * @return grade exam
	 */
	public Grade getExamGrade(Long idexam, Long iduser);
	
	/**
	 * Deleting an exam performed by the student in a given exam configuration.
	 * A student can only perform an exam for a given exam configuration.
	 * @param idstd	Student id
	 * @param idex		Configuration exam id
	 */
	public void deleteStudentExam(Long idstd, Long idex);

	/**
	 * Deleting a question from exam performed by the student in a given exam configuration.
	 * @param idex		Configuration exam id
	 * @param idPreg	Question that we want to delete
	 */
	public void deleteQuestionFromExam(Long idex, Long idPreg);
	
	/**
	 * 
	 * Obtain the next question for an exam
	 * 
	 * @param user User that takes the exam
	 * 
	 * @param idExam Exam identifier
	 * 
	 * @param lastQuestionId Last shown question to the candidate
	 * 
	 * @return Returns the next question if there is one or <code>null</code> if
	 * there is no more question to take.
	 */
	public ItemSession getNextQuestion(User user, Long idExam, Long lastQuestionId);
	
	/** Obtain a list of exams which included the question id given by parameter
	 * 
	 * @param idQuestion Question to filter by
	 * @return List of exams which included that question
	 */
	public List<TestSessionForReview> getExamsByQuestion(Long idQuestion);
	
	
	/** Obtain a list of exams
	 * 
	 * @param idExam Exam to filter by
	 * @return List of exams which included that question
	 */
	public List<TestSessionForReview> getExamsByIdExam(Long idExam);
	
	/**
	 * Obtain a list of the next exams ordered by date
	 * 
	 * @return List of the next exams
	 * */
	public List<ExamGlobalInfo> getNextExams();
	
	/**
	 * Obtain a list of the active exams ordered by date
	 * 
	 * @return List of the active exams
	 * */
	public List<ExamGlobalInfo> getActiveExams();

	/**
	 * Returns the exam with the given id
	 * @param id The id of the exam
	 * @param idalumn 
	 * @return exam 
	 * */
	public TestSession getExamById(Long id, Long idalumn);
	
	
	/**
	 * Return a list of Exams to this user for this group
	 * @iduser  User's id
	 * @idgroup Group's id
	 * @return the exam list of the user to this group
	 * */
	public List<TestSession> getAlreadyDoneExamGradeByGroup(long iduser, long idgroup);

	
	/**
	 * Return a list width the exams ids which have this question.
	 * @param question
	 * @return
	 * */
	public List<Long> getExamIds(Item question);

	/**
	 * Return the list width global info from exams filtered by idInstitution, idCourse and year
	 * @param idInstitution
	 * @param idCourse
	 * @param year
	 * @return List<ExamGlobalInfo>
	 * */
	public List<ExamGlobalInfo> getPreviousExamsFiltered(long idInstitution,
			long idCourse, String year);

	/**
	 * Return the stats from an exam
	 * @param idexam
	 * @return CourseStats
	 * */
	public CourseStats getPreviousExamsFiltered(int idexam);

	/**
	 * Returns the list of exams of all learners width this idexam
	 * @param idexam
	 * @return the list of exams
	 */
	public List<TestSession> getAllExams(Long idexam);

	public List<ExamGlobalInfo> getActiveExamsFiltered(String Centro,
			String Asignatura, Date startDate, Date endDate);

	public List<ExamGlobalInfo> getNextExamsFiltered(String centro,
			String asignatura, Date startDate, Date endDate);
}

