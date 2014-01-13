package com.cesfelipesegundo.itis.biz.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.cesfelipesegundo.itis.model.BasicDataExam;
import com.cesfelipesegundo.itis.model.ConfigExam;
import com.cesfelipesegundo.itis.model.CustomExamUser;
import com.cesfelipesegundo.itis.model.Exam;
import com.cesfelipesegundo.itis.model.ExamForReview;
import com.cesfelipesegundo.itis.model.ExamGlobalInfo;
import com.cesfelipesegundo.itis.model.ExamQuestion;
import com.cesfelipesegundo.itis.model.Grade;
import com.cesfelipesegundo.itis.model.Group;
import com.cesfelipesegundo.itis.model.TemplateExamQuestion;
import com.cesfelipesegundo.itis.model.User;
import com.lowagie.text.DocumentException;
import com.mysql.jdbc.exceptions.MySQLTransactionRollbackException;

/**
 * Interfaz para gestión de alumnos
 * @author chema
 *
 */

public interface LearnerManagementService {

	/**
	 * Devuelve una lista con los datos de los exámenes que el alumno puede realizar en la fecha actual (aquellos activos a fecha
	 * actual, correspondientes a grupos en los que está matriculado el alumno y que no han sido ya hechos por este alumno). Los
	 * datos del examen son: id, título y asignatura. El identificador de alumno se pasa por parámetro. 
	 * @param id del alumno
	 * @return Lista de exámenes disponibles para ser realizados por el alumno, ordenados por nombre de asignatura. 
	 */
	List<BasicDataExam> getPendingExams(Long id);
	
	
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
	Grade getExamGrade(Long idexam, Long iduser);
	
	/**
	 * Devuelve una lista con los datos de los exámenes que el alumno ha realizado hasta la fecha actual, si están en su periodo de
	 * revisión. Los datos del examen son: id, título y asignatura. El identificador de alumno se pasa por parámetro. 
	 * @param id del alumno
	 * @return Lista de exámenes en periodo de revisión ya realizados por el alumno, ordenados por nombre de asignatura.
	 */
	List<BasicDataExam> getExamsForRevision(Long id);
	
	/**
	 * Obtains a new exam for the learner taking into account the configuration idexam
	 * It should also register that the exam was started by the learner:
	 *  - Create a new row on table califs with all data but fecha_fin and nota
	 *  - Create one row for each answer of the exam on table log_exams 
	 * @param learner, alumno que realizará el examen
	 * @param idexam, id de la configuración del examen a realizar
	 * @param ipAddress, IP address of the client
	 * @return Examen, lista de preguntas con contenido multimedia y respuestas asociadas, ordenadas por tema.
	 * 			null there exists a previous exam of the same student.
	 */
	Exam getNewExam(User learner, Long idexam, String ipAddress);

	/**
	 * Updates the database in order to register an answer as "marked" or "not marked" in relation to a question, exam and learner
	 * @param idexam
	 * @param iduser
	 * @param idquestion
	 * @param idanswer
	 * @param marked
	 * @return number of rows updated
	 */  
	
	int updateExamAnswer(Long idexam, Long iduser, Long idquestion, Long idanswer, Boolean marked);

	/**
	 * This method should:
	 *  - Obtain the final grade of the exam
	 *  - Update the corresponding rows of log_exams table
	 *  - Update the information of table califs: fecha_fin and nota
	 * @param id of the user (learner)
	 * @param currentExam, that is, the object consisting in the exam performed by the user
	 * @return Final grade of the exam
	 */
	Double gradeExam(Long id, Exam currentExam);

	/** 
	 * Method to obtain the data of an already done exam: questions, answers, comments, grade, marked answers, correct answers,
	 * time employed...
	 * @param user that performed the exam
	 * @param idexam, id of the configuration of the exam previously performed
	 * @return
	 */
	Exam getAlreadyDoneExam(User user, Long idexam);
	
	/**
	 * 
	 * Obtain the next question for an exam
	 * 
	 * @param user User that takes the exam
	 * 
	 * @param idExam Exam identifier
	 * 
	 * @param lastQuestionId Last question shown to the candidate
	 * 
	 * @return Returns the next question if there is one or <code>null</code> if
	 * there is no more question to take.
	 */
	public ExamQuestion getNextQuestion(User user, Long idExam, Long lastQuestionId);
	
	
	/**
	 * Calculate the grade for a previewed exam
	 * 
	 * @param previewedExam Previewed exam
	 * @return
	 */
	public Double gradePreviewedExam(Exam previewedExam );
	
	/**
	 * Generate an exam from a configuration without storing it in the database
	 * 
	 * @param idexam id of the exam to be previewed
	 * @return
	 */
	public Exam getPreviewExam(Long idexam);
	
	/** Reviews all the exams containing the question passed by parameter
	 * If a question was incorrectly introduced into DB, or had an error on its answers;
	 * exams containing that question will be incorrectly graded.
	 * This method reviews all the exams containing the question, grading again learners
	 * who did each exam.
	 * @param question Question changed
	 * @return Reviewed exam list
	 */
	public List<ExamForReview> examReviewByQuestion(TemplateExamQuestion question);
	
	/** Reviews all the exams
	 * If a question was incorrectly introduced into DB, or had an error on its answers;
	 * exams containing that question will be incorrectly graded.
	 * @param idExam id of the Exam
	 * @return Reviewed exam list
	 */
	public List<ExamForReview> examReviewByIdExam(long idExam);
	
	/** Generates a PDF file from question given as parameter
	 * @param question TemplateExamQuestion object to be parsed to PDF
	 * @return ByteArrayOutputStream Byte array corresponding to PDF created from the question given
	 * @throws NullPointerException If the question object given is a null pointer
	 * @throws DocumentException If the IText library can not parse PDF document
	 * @throws IOException If was not possible to access to file system*/
	public ByteArrayOutputStream parse2PDF(TemplateExamQuestion question) throws NullPointerException,DocumentException,IOException;
	
	
	/** Generates a PDF file from exam given as parameter
	 * @param exam Exam object to be parsed to PDF
	 * @return ByteArrayOutputStream Byte array corresponding to PDF created from the exam given
	 * @throws NullPointerException If the exam object given is a null pointer
	 * @throws DocumentException If the IText library can not parse PDF document
	 * @throws IOException If was not possible to access to file system
	 */
	public ByteArrayOutputStream parse2PDF(Exam exam) throws NullPointerException,DocumentException,IOException;

	/**
	 * Returns the exam with the given id
	 * @param id The id of the exam
	 * @return exam 
	 * */
	public Exam getExam(Long idexam, Long idalum);
	
	/**
	 * Returns the groups where is register the user
	 * @param iduser The user's id
	 * @return group
	 * */
	
	public List<Group> getUserGroups(Long iduser);
	
	/**
	 * Returns the groups with the given id
	 * @param iduser The group's id
	 * @return group
	 * */
	public Group getGroup(Long idgroup);
	
	/**
	 * Return the grade of the current exam given
	 * @idexam Exam's id
	 * @return the grade of the exam
	 * */
	public Grade getGradeByIdExam(Long idexam, Long iduser);
	
	/**
	 * Return the configExam of the current exam given
	 * @param exam empty config exam
	 * @return the config exam
	 * */
	public ConfigExam getConfigExamFromId(ConfigExam exam);
	
	/**
	 * Return a list of Exams to this user for this group
	 * @iduser  User's id
	 * @idgroup Group's id
	 * @return the exam list of the user to this group
	 * */
	public List<Exam>getAlreadyDoneExamGradeByGroup(long iduser, long idgroup);
	
	/**
	 * Return a list of grades to this user for this group
	 * @iduser  User's id
	 * @idgroup Group's id
	 * @return the grade list of the user to this group
	 * */
	public List<Grade>getAlreadyDoneGradeByGroup(long iduser, long idgroup);
	
	/**
	 * Return a list width the next exams from this user
	 * @return
	 * */
	public List<BasicDataExam> getNextExams(long userId, long idGroup);
	
	/**
	 * Returns the list of tutors of a group
	 * @param group
	 * @return list of tutors of the group passed as parameter
	 */
	public List<User> getTutors(Group g);

	/**
	 * Returns the list of exams of all learners width this idexam
	 * @param idexam
	 * @return the list of exams
	 */
	public List<Exam> getAllExams(Long idexam);
	
	/**
	 * Updates the confidence level for a question exam
	 * @param examId
	 * @param userId
	 * @param questionId
	 * @param checked
	 * */
	public int updateConfidenceLevel(long examId, long userId, long questionId, boolean checked);
	
	/**
	 * Add the answers from idquestion from currentExam to the log_exams' table
	 * */
	public void addNewExamAnswer2LogExams(Exam currentExam,long idlearner,long idquestion,long startingDate);

	/**
	 * Check if the exam was correctly saved into the 'log_exams' table
	 * @throws MySQLTransactionRollbackException 
	 * @throws Exception 
	 * */
	public void checkExam(Exam currentExam, long iduser) throws MySQLTransactionRollbackException, Exception;
	
	/**
	 * Return an User width the same id as idUser
	 * @param idUser the user id
	 * @return an User width id equals than idUser
	 * */
	public User getUser(long idUser);
	
	/**
	 * Return all of grades from the current user
	 * @param userName The user's name
	 * @return all of grades from the current user
	 * */
	public List<Grade> getGradesByUser(String userName);


	public List<CustomExamUser> getUsersInCustomExam(Long id);
	
	
	/**
	 * This method should:
	 *  - Obtain the final grade of the exam
	 *  - Update the corresponding rows of log_exams table
	 *  - Update the information of table califs: fecha_fin and nota
	 * @param id of the user (learner)
	 * @param currentExam, that is, the object consisting in the exam performed by the user
	 * @return Final grade of the exam
	 */
	Double reGradeExam(Long idStd, Exam ex);

	/**
	 * Updates in the data base the changes in a fill answer in the given question for the current
	 * exam given.
	 * @param idExam exam's id
	 * @param idUser user's id
	 * @param idQuestion question's id
	 * @param idAnswer answer's id
	 * @param textAnswer answer's solution given by learner
	 * */
	void updateExamFillAnswer(Long idExam, Long idUser, Long idQuestion,
			Long idAnswer, String textAnswer);
}