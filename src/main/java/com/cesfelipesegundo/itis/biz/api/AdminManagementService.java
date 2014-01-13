package com.cesfelipesegundo.itis.biz.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cesfelipesegundo.itis.model.Conection;
import com.cesfelipesegundo.itis.model.CourseStats;
import com.cesfelipesegundo.itis.model.Exam;
import com.cesfelipesegundo.itis.model.ExamGlobalInfo;
import com.cesfelipesegundo.itis.model.GroupDetails;
import com.cesfelipesegundo.itis.model.Institution;
import com.cesfelipesegundo.itis.model.InstitutionStats;
import com.cesfelipesegundo.itis.model.InstitutionStudies;
import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.model.Course;
import com.cesfelipesegundo.itis.model.Group;
import com.mysql.jdbc.exceptions.MySQLTransactionRollbackException;

/**
 * Interfaz para gestión de administradores
 * 
 * @author ruben
 * 
 */
public interface AdminManagementService {

	/**
	 * Returns the list of institutions
	 * @return list of institutions
	 */
	List<Institution> getInstitutions();

	/**
	 * Returns the list of users of an institution
	 * @param institution
	 * @return list of users of the institution passed as parameter
	 */
	List<User> getUsers(Institution i);
	
	/**
	 * Returns the list of tutors of an institution
	 * @param institution
	 * @return list of users tutors of the institution passed as parameter
	 */
	List<User> getTutors(Institution i);

	/**
	 * Returns the list of learners of an institution
	 * @param institution
	 * @return list of users learners of the institution passed as parameter
	 */
	List<User> getLearners(Institution i);

	/**
	 * Returns the list of tutors of a group
	 * @param group
	 * @return list of tutors of the group passed as parameter
	 */
	List<User> getTutors(Group g);

	/**
	 * Returns the list of tutors not assigned to a group
	 * @param group
	 * @return list of tutors not assigned the group passed as parameter
	 */
	List<User> getUnassignedTutors(Group g);

	/**
	 * Returns the list of courses
	 * @return list of courses
	 */
	List<Course> getCourses();
	
	/**
	 * Returns the list of courses of an specific institution
	 * @return list of courses
	 */
	List<Course> getCourses(long institutionId);

	/**
	 * Returns the list of groups of an specific course
	 * @param course
	 * @return list of groups of the course passed as parameter
	 */
	List<Group> getGroups(Course c);
	
	/**
	 * Returns the list of groups of an specific institution
	 * @param institution
	 * @return list of groups of the institution passed as parameter
	 */
	List<Group> getGroups(Institution i);

	/**
	 * Obtains the data from an institution, given the id
	 * @param institution id
	 * @return institution object
	 */
	Institution getInstitution(Long id);

	/**
	 * Obtains the data from a course, given the id
	 * @param course id
	 * @return course object
	 */
	Course getCourse(Long id);
		
	/**
	 * Obtains the data from a course, given the code
	 * @param code
	 * @return course object
	 */
	Course getCourseByCode(String code);
	
	/**
	 * Inserts or updates a Institution data into the database
	 * @param institution
	 * @param studies
	 * @return Institution object with the id filled
	 */
	Institution saveInstitution(Institution institution, InstitutionStudies studies);

	/**
	 * Saves the course configuration into the database
	 * 
	 * @param course
	 * @return Course object with the id filled
	 */
	Course saveCourse(Course course);
	
	/**
	 * Saves the group configuration into the database
	 * 
	 * @param group
	 * @return Group object with the id filled
	 */
	Group saveGroup(Group group);

	
	/**
	 * Deletes an institution from the database
	 * @param institution to be deleted
	 */
	void deleteInstitution(Institution institution);

	/**
	 * Deletes a course from the database
	 * @param course to be deleted
	 */
	void deleteCourse(Course course);

	/**
	 * Deletes a group from the database
	 * @param institution to be deleted
	 */
	void deleteGroup(Group group);

	/**
	 * Deletes an user from the database
	 * @param user to be deleted
	 */
	void deleteUser(User user);
	
	/**
	 * Assigns a tutor to the group passed as a parameter
	 * @param tutor	Tutor object previously filled.
	 * @param group	Group object previously fille.
	 * @return true if the assignation was successful, false in other case
	*/
	boolean assignTutor(User tutor, Group group);

	/**
	 * Un-Assigns the tutor from the group passed as a parameter
	 * @param tutor	Tutor object previously filled.
	 * @param group	Group object previously fille.
	 * @return true if the un-assignation was successful, false in other case
	*/
	boolean unAssignTutor(User tutor, Group group);

	/**
	 * Saves or adds the data of the given user in the given institution
	 * @param newUser
	 * @param institution
	 * @return false if the username is already used. False i.o.c.
	 */
	boolean saveUser(User newUser, Institution institution);

	/**
	 * Return a list width the next exams
	 * @return
	 * */
	public List<ExamGlobalInfo> getNextExams();
	
	/**
	 * Return a list width the active exams
	 * @return
	 * */
	public List<ExamGlobalInfo> getActiveExams();

	/**
	 * Return the stats from the current course
	 * @return
	 * */
	CourseStats getCourseStats(long institution, long course, String year);

	/**
	 * Returns the list of groups of an specific institution and course
	 * @param institution
	 * @return list of groups of the institution passed as parameter
	 */
	List<Group> getGroups(Course course, long idInstitution);

	/**
	 * Returns the list of Courses of an specific institution and group
	 * @param idInstitution
	 * @param year
	 * @return list of groups of the institution passed as parameter
	 */
	List<Course> getCourseByInstitutionAndGroup(long idInstitution, String year);
	

	/**
	 * Return the stats from the current course
	 * @return
	 * */
	InstitutionStats getInstitutionStats(long idInstitution, String year, long idCourse);
	
	/**
	 * Returns the list of studies of an specific institution
	 * @param idInstitution
	 * @return list of studies
	 */
	InstitutionStudies getInstitutionStudies(long idInstitution);

	/**
	 * Returns the list Institutions filter by the map
	 * @param map
	 * @return list of institutions
	 */
	List<Institution> getInstitutionsFiltered(Map<String, Object> map);

	/**
	 * Returns the list Users filter by the map
	 * @param map
	 * @return list of user
	 */
	List<User> getUsersByFilter(Map<String, Object> map);

	/**
	 * Returns the list Groups filter by the map
	 * @param map
	 * @return list of groups
	 */
	List<Group> getGroupsByFilter(Map<String, Object> map);

	/**
	 * Returns the list Courses filter by the attributes
	 * @param codigo
	 * @param nombre
	 * @param curso
	 * @param estudios
	 * @return list of courses
	 */
	List<Course> getCoursesFiltered(String codigo, String nombre, String curso, String estudios);

	/**
	 * Returns the list Users filter by the map
	 * @param map
	 * @return list of user
	 */
	List<User> getFilteredUsers(Map<String, Object> map);

	/**
	 * Returns the list Groups filter by the map
	 * @param map
	 * @return list of groups
	 */
	List<Group> getFilteredGroups(Map<String, Object> map);

	/**
	 * Return the list width global info from exams filtered by idInstitution, idCourse and year
	 * @param idInstitution
	 * @param idCourse
	 * @param year
	 * @return List<ExamGlobalInfo>
	 * */
	List<ExamGlobalInfo> getPreviousExamsFiltered(long idInstitution,
			long idCourse, String year);

	/**
	 * Return the stats from an exam
	 * @param idexam
	 * @return CourseStats
	 * */
	CourseStats getCourseStatsByExam(int idexam);

	/**
	 * Return the list width users filtered by idInstitution, idCourse and year
	 * @param name
	 * @param apes
	 * @param user 
	 * @param tipo
	 * @return List<User>
	 * */
	List<User> getSearchUsersFiltered(String name, String apes, String user, String tipo);

	/**
	 * Return the list width groups from the current user
	 * @param id
	 * @return List<Group>
	 * */
	List<Group> getUserInfoGroups(long id);

	/**
	 * Return a list with all kind of certifications
	 * @return List<String>
	 * */
	List<String> getAllCertifications();

	/**
	 * Return a list of groups from the current parameters
	 * @param idInstitution
	 * @param year
	 * @param idCourse
	 * @return List<Group>
	 * */
	List<GroupDetails> getGroupDetails(long idInstitution, String year, long idCourse);

	/**
	 * Return the list width global info from active exams filtered by idInstitution, idCourse and year
	 * @param idInstitution
	 * @param idCourse
	 * @param year
	 * @return List<ExamGlobalInfo>
	 * */
	List<ExamGlobalInfo> getActiveExamsFiltered(String Centro,
			String Asignatura, Date startDate, Date endDate);

	/**
	 * Return the list width global info from next exams filtered by idInstitution, idCourse and year
	 * @param idInstitution
	 * @param idCourse
	 * @param year
	 * @return List<ExamGlobalInfo>
	 * */
	List<ExamGlobalInfo> getNextExamsFiltered(String centro, String asignatura,
			Date startDate, Date endDate);
	
	/**
	 * Return a list with the 100 last conections
	 * */
	List<Conection> get100LastConections();

	/**
	 * Return a filtered list of conections
	 * @param idConection filtro por id de conexión
	 * @param userNameConection filtro por el nombre de usuario de la conexión
	 * @param date1 filtro para fechas mayores o iguales que esta
	 * @param date2 filtro para fechas menores o iguales que esta
	 * @return List<Conection>
	 * */
	List<Conection> runFilterAndSearch100Conections(Long idConection,
			String userNameConection, Date date1, Date date2);

	/**
	 * Return a list with the 5 last conections
	 * @param id
	 * @return List<Conection>
	 * */
	List<Conection> show5LastConections(Long id);

	/**
	 * Return a list with users not vinculated to the table "imparten" o "matriculas"
	 * @return List<User>
	 * */
	List<User> showUsersNotVinculated();
	
	/**
	 * Return an instance of User whith the same id
	 * @param idUser el id del usuario del que queremos obtener una instancia
	 * @return User
	 * */
	User getUserById(long idUser);
	
	/**
	 * Return a list of users from the given group
	 * @param group an instance of the group 
	 * @return a list of users from the given group
	 * */
	public List<User> getLearnerByGroup(Group group);
	
	/**
	 * Return an instance from the gruop with the same id than given
	 * @param idGroup the given group's id
	 * @return An instance of Group with the same id than idGroup
	 * */
	public Group getGroup(long idGroup);
	
	/** 
	 * Method to obtain the data of an already done exam: questions, answers, comments, grade, marked answers, correct answers,
	 * time employed...
	 * @param user that performed the exam
	 * @param idexam, id of the configuration of the exam previously performed
	 * @return
	 */
	public Exam getAlreadyDoneExam(User user, long idexam);

	Exam getNewExam(User user, long idExam, String remoteAddr);

	void checkExam(Exam ex, long iduser) throws MySQLTransactionRollbackException, Exception;
	
	/**
	 * Returns a list width institutions with at least one public question
	 * @return list of institutions with at least one public question
	 * */
	List<Institution> getInstitutionsWidthPublicQuestions();
	
	/**
	 * Returns the institution of the given user's id
	 * @param id user's id
	 * */
	Institution getInstitutionByUserId(Long id);
}
