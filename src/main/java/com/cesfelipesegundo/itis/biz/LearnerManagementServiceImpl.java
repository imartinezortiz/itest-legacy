package com.cesfelipesegundo.itis.biz;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;

import com.cesfelipesegundo.itis.biz.api.LearnerManagementService;
import com.cesfelipesegundo.itis.dao.api.AnswerExamDAO;
import com.cesfelipesegundo.itis.dao.api.BasicDataExamDAO;
import com.cesfelipesegundo.itis.dao.api.ConfigExamDAO;
import com.cesfelipesegundo.itis.dao.api.ExamDAO;
import com.cesfelipesegundo.itis.dao.api.GroupDAO;
import com.cesfelipesegundo.itis.dao.api.SubjectDAO;
import com.cesfelipesegundo.itis.dao.api.TemplateExamDAO;
import com.cesfelipesegundo.itis.dao.api.TemplateExamQuestionDAO;
import com.cesfelipesegundo.itis.dao.api.TemplateGradeDAO;
import com.cesfelipesegundo.itis.dao.api.UserDAO;
import com.cesfelipesegundo.itis.model.CustomExamUser;
import com.cesfelipesegundo.itis.model.Grade;
import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.web.Constants;
import com.lowagie.text.DocumentException;

import es.itest.engine.course.business.entity.Group;
import es.itest.engine.test.business.boundary.TestManager;
import es.itest.engine.test.business.control.ItemPdfWriter;
import es.itest.engine.test.business.control.TestSessionPdfWriter;
import es.itest.engine.test.business.entity.Action1;
import es.itest.engine.test.business.entity.DomainEventRegistrationRemover;
import es.itest.engine.test.business.entity.DomainEvents;
import es.itest.engine.test.business.entity.Item;
import es.itest.engine.test.business.entity.ItemResponse;
import es.itest.engine.test.business.entity.ItemSession;
import es.itest.engine.test.business.entity.ItemSessionResponse;
import es.itest.engine.test.business.entity.TestDetails;
import es.itest.engine.test.business.entity.TestSession;
import es.itest.engine.test.business.entity.TestSessionForReview;
import es.itest.engine.test.business.entity.TestSessionTemplate;

/**
 * @author  chema
 */
public class LearnerManagementServiceImpl implements LearnerManagementService {

	private TestManager testManager;
	
	/**
	 * @uml.property  name="groupDAO"
	 * @uml.associationEnd  
	 */
	private GroupDAO groupDAO;
	

	/**
	 * @uml.property  name="examDAO"
	 * @uml.associationEnd  
	 */
	private ExamDAO examDAO;
	
	/**
	 * @uml.property  name="templateGradeDAO"
	 * @uml.associationEnd  
	 */
	private TemplateGradeDAO templateGradeDAO;
	
	/**
	 * @uml.property  name="basicDataExamDAO"
	 * @uml.associationEnd  
	 */
	private BasicDataExamDAO basicDataExamDAO;

	/**
	 * @uml.property  name="userDAO"
	 * @uml.associationEnd  
	 */
	private UserDAO userDAO;
	
	/**
	 * @param userDAO
	 * @uml.property  name="userDAO"
	 */
	UserDAO getUserDAO() {
		return userDAO;
	}

	/**
	 * @param userDAO
	 * @uml.property  name="userDAO"
	 */
	void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	/**
	 * @return
	 * @uml.property  name="basicDataExamDAO"
	 */
	BasicDataExamDAO getBasicDataExamDAO() {
		return basicDataExamDAO;
	}
	
	/**
	 * @param basicDataExamDAO
	 * @uml.property  name="basicDataExamDAO"
	 */
	void setBasicDataExamDAO(BasicDataExamDAO basicDataExamDAO) {
		this.basicDataExamDAO = basicDataExamDAO;
	}	
	
	/**
	 * @return
	 * @uml.property  name="templateGradeDAO"
	 */
	TemplateGradeDAO getTemplateGradeDAO() {
		return templateGradeDAO;
	}

	/**
	 * @param templateGradeDAO
	 * @uml.property  name="templateGradeDAO"
	 */
	void setTemplateGradeDAO(
			TemplateGradeDAO templateGradeDAO) {
		this.templateGradeDAO = templateGradeDAO;
	}
	
	/**
	 * @return
	 * @uml.property  name="examDAO"
	 */
	ExamDAO getExamDAO() {
		return examDAO;
	}

	/**
	 * @param examDAO
	 * @uml.property  name="examDAO"
	 */
	void setExamDAO(ExamDAO examDAO) {
		this.examDAO = examDAO;
	}
	
	/**
	 * @return
	 * @uml.property  name="groupDAO"
	 */
	GroupDAO getGroupDAO() {
		return groupDAO;
	}

	/**
	 * @param basicDataExamDAO
	 * @uml.property  name="groupDAO"
	 */
	void setGroupDAO(GroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}
	
	void setTestManager(TestManager testManager) {
		this.testManager = testManager;
	}
	
	TestManager getTestManager() {
		return this.testManager;
	}
	
	/*
	 * Implementation of LearnerManagementService
	 */

	public List<Group> getUserGroups(Long iduser) {
		return groupDAO.getUserGroups(iduser);
	}

	public Group getGroup(Long idgroup) {
		return groupDAO.getGroup(idgroup);
	}

	public List<TestSession> getAlreadyDoneExamGradeByGroup(long iduser, long idgroup) {
		return examDAO.getAlreadyDoneExamGradeByGroup(iduser, idgroup);
	}
	
	public List<Grade> getAlreadyDoneGradeByGroup(long iduser, long idgroup) {
		return templateGradeDAO.getAlreadyDoneGradeByGroup(iduser, idgroup);
	}

	public List<TestDetails> getNextExams(long userId, long idGroup) {
		return basicDataExamDAO.getNextExams(userId,idGroup);
	}	

	public List<User> getTutors(Group g) {
		return userDAO.getAssignedTutors(g);
	}	

	public User getUser(long idUser) {
		return userDAO.getUser(idUser);
	}
	
	
	public List<TestDetails> getTestSessionsForReview(Long exameneeId) {
		return testManager.getTestSessionsForReview(exameneeId);
	}

	public List<TestDetails> getPendingTests(Long exameneeId) {
		return testManager.getPendingTests(exameneeId);
	}

	public TestSession createTestSession(User examenee, long testId, String ipAddress){
		return testManager.createTestSession(examenee, testId, ipAddress);
	}

	public TestSession createTestSessionPreview(long testId){
		return testManager.createTestSessionPreview(testId);
	}
	
	public int updateItemSessionResponse(long testId, long examineeId, long itemSessionId, long itemSessionReponseId, boolean marked) {

		return testManager.updateItemSessionResponse(testId, examineeId, itemSessionId, itemSessionReponseId, marked);
	}

	/**
	 * This method should: - Obtain the grade of each individual answer to each
	 * question and update the corresponding rows of log_exams table - Update
	 * the information of table califs: fecha_fin and nota
	 * 
	 * @param id,
	 *            of the user (learner)
	 * @param currentExam,
	 *            that is, the object consisting in the exam performed by the
	 *            user
	 * @return Final grade of the exam
	 */
	public Double gradeTestSession(long examineeId, TestSession testSession) {
		return testManager.gradeTestSession(examineeId, testSession);
	}

	public Double gradeTestSessionPreview(TestSession testSessionPreview){
		return testManager.gradeTestSessionPreview(testSessionPreview);
	}
	
	public ItemSession getNextQuestion(User examinee, long testSessionId, long lastItemSessionId){
		return testManager.getNextQuestion(examinee, testSessionId, lastItemSessionId);
	}

	/**
	 * Method to obtain the data of an already done exam: questions, answers,
	 * comments, grade, marked answers, correct answers, time employed...
	 * 
	 * @param examinee
	 *            that performed the exam
	 * @param idexam,
	 *            id of the configuration of the exam previously performed
	 * @return
	 */
	public TestSession getAlreadyDoneExam(User examinee, long testSessionId) {
		return testManager.getAlreadyDoneExam(examinee, testSessionId);
	}
	
	public List<TestSessionForReview> examReviewByQuestion(Item question) {
		return testManager.examReviewByQuestion(question);
	}
	
	public List<TestSessionForReview> examReviewByIdExam(long idExam) {
		return testManager.examReviewByIdExam(idExam);
	}
	
	public ByteArrayOutputStream parse2PDF(Item question) throws NullPointerException,DocumentException,IOException{
		return testManager.parse2PDF(question);
	}
	
	public ByteArrayOutputStream parse2PDF(TestSession exam) throws NullPointerException,DocumentException,IOException{
		return testManager.parse2PDF(exam);
	}

	public Grade getTestSessionGrade(long testSessionId, long examineeId) {
		return testManager.getTestSessionGrade(testSessionId, examineeId);
	}

	public TestSession getTestSession(long testSessionId, long idalumn) {
		return testManager.getTestSession(testSessionId, idalumn);
	}

	public Grade getGradeByIdExam(Long idexam, Long iduser) {
		return testManager.getGradeByIdExam(idexam, iduser);
	}

	public TestSessionTemplate getConfigExamFromId(TestSessionTemplate exam) {
		return testManager.getConfigExamFromId(exam);
	}

	public List<TestSession> getAllExams(Long idexam) {
		return testManager.getAllExams(idexam);
	}

	public int updateConfidenceLevel(long examId, long userId, long questionId, boolean checked) {
		return testManager.updateConfidenceLevel(examId, userId, questionId, checked);
	}
	
	public void addNewExamAnswer2LogExams(TestSession currentExam,long idlearner,long idquestion,long startingDate){
		testManager.addNewExamAnswer2LogExams(currentExam, idlearner, idquestion, startingDate);
	}

	public void checkExam(TestSession currentExam, long iduser) {
		testManager.checkExam(currentExam, iduser);
	}

	public List<Grade> getGradesByUser(String userName) {
		return testManager.getGradesByUser(userName);
	}
	
	public List<CustomExamUser> getUsersInCustomExam(Long examId) {
		return testManager.getUsersInCustomExam(examId);
	}

	public void updateExamFillAnswer(Long idExam, Long idUser, Long idQuestion,	Long idAnswer, String textAnswer) {
		testManager.updateExamFillAnswer(idExam, idUser, idQuestion, idAnswer, textAnswer);
	}
	
	public Double reGradeExam(Long idStd, TestSession ex) {
		return testManager.reGradeExam(idStd, ex);
	}
}
