package com.cesfelipesegundo.itis.biz.api;

import java.util.List;

import com.cesfelipesegundo.itis.model.ConfigExam;
import com.cesfelipesegundo.itis.model.ConfigExamSubject;
import com.cesfelipesegundo.itis.model.CustomExamUser;
import com.cesfelipesegundo.itis.model.Exam;
import com.cesfelipesegundo.itis.model.ExamGlobalInfo;
import com.cesfelipesegundo.itis.model.ExamStats;
import com.cesfelipesegundo.itis.model.Group;
import com.cesfelipesegundo.itis.model.Institution;
import com.cesfelipesegundo.itis.model.LearnerStats;
import com.cesfelipesegundo.itis.model.MediaElem;
import com.cesfelipesegundo.itis.model.Message;
import com.cesfelipesegundo.itis.model.Query;
import com.cesfelipesegundo.itis.model.QueryGrade;
import com.cesfelipesegundo.itis.model.QuestionStats;
import com.cesfelipesegundo.itis.model.ExamForReview;
import com.cesfelipesegundo.itis.model.Subject;
import com.cesfelipesegundo.itis.model.TemplateExamAnswer;
import com.cesfelipesegundo.itis.model.TemplateExamQuestion;
import com.cesfelipesegundo.itis.model.TemplateExamSubject;
import com.cesfelipesegundo.itis.model.TemplateGrade;
import com.cesfelipesegundo.itis.model.User;

/**
 * Interfaz para gestión de profesores
 * 
 * @author chema
 * 
 */
public interface TutorManagementService {

	/**
	 * Devuelve una lista con los grupos en los que imparte clase el profesor
	 * cuyo id de usuario se pasa por parámetro.
	 * 
	 * @param id
	 * @return Lista de los grupos en los que imparte clase el profesor cuyo id
	 *         de usuario se pasa por parámetro.
	 */
	List<Group> getTeachedGroups(Long id);

	/**
	 * Devuelve una lista con los grupos en los que está matriculado el alumno
	 * cuyo id de usuario se pasa por parámetro.
	 * 
	 * @param id
	 * @return Lista de los grupos en los que está matriculado cuyo id
	 *         de usuario se pasa por parámetro.
	 */
	List<Group> getMatriculatedGroups(Long id);
	
	
	/**
	 * It returns a list of themes linked to the group passed as parameter
	 * The subjects include information about number of questions, answers, ...
	 * 
	 * @param group
	 * @return It returns a list of themes linked to the group whose id is
	 *         passed as parameter
	 */
	List<TemplateExamSubject> getCourseSubjects(Group g);

	/**
	 * It returns a list of themes linked to the group passed as parameter
	 * The subjects have the minimum info: subject and order
	 * 
	 * @param group
	 * @return It returns a list of themes linked to the group whose id is
	 *         passed as parameter
	 */
	List<Subject> getSubjects(Group currentGroup);
	
	/**
	 * Returns all the group information read from the database
	 * 
	 * @param group
	 *            id
	 * @return the group object
	 */
	Group getGroup(Long id);

	/**
	 * Saves the question information into the database.
	 * <p>
	 * If question has an id, the database is updated, if not the answer is
	 * inserted and <code>question</code> is updated to include the assigned
	 * id from the database
	 * </p>
	 * 
	 * @param question
	 */
	void saveQuestion(TemplateExamQuestion question);

	/**
	 * Saves the answer information into the database.
	 * <p>
	 * The answer is updated to include the assigned id from the database
	 * </p>
	 * 
	 * @param answer
	 */
	void saveAnswer(TemplateExamAnswer answer);

	/**
	 * Deletes an answer from the database
	 * @param answ
	 */
	void deleteAnswer(TemplateExamAnswer answer);
	
	/**
	 * Gets from the database the list of questions related to this group
	 * 
	 * @param currentGroup
	 * @return list of TemplateExamQuestion: questions, answers, mmedia, ...
	 */
	List<TemplateExamQuestion> getGroupQuestions(Group currentGroup);
	
	/**
	 * Saves the mediaElem into the question into the database.
	 * <p>
	 * The mediaElem is updated to include the assigned id from the database
	 * </p>
	 * 	 * 
	 * @param question
	 * @param mediaElem
	 */

	void saveMediaElemToQuestion(TemplateExamQuestion question, MediaElem mediaElem);
	
	/**
	 * Saves the mediaElem into the comment into the database.
	 * <p>
	 * The mediaElem is updated to include the assigned id from the database
	 * </p>
	 * 	 * 
	 * @param question
	 * @param mediaElem
	 */

	void saveMediaElemToComment(TemplateExamQuestion question, MediaElem mediaElem);
	
	
	/**
	 * Saves the mediaElem into the answer into the database.
	 * <p>
	 * The mediaElem is updated to include the assigned id from the database
	 * </p>
	 * 	 * 
	 * @param question
	 * @param mediaElem
	 */
	
	void saveMediaElemToAnswer(TemplateExamAnswer answer, MediaElem mediaElem);
	
	/**
	 * Delete a mediaElem from a question
	 * 
	 * @param question
	 * @param mediaElem
	 */
	
	void deleteMediaElemFromQuestion(TemplateExamQuestion question, MediaElem mediaElem);
	
	/**
	 * Delete a comment mediaElem from a question
	 * 
	 * @param question
	 * @param mediaElem
	 */
	
	void deleteMediaElemFromComment(TemplateExamQuestion question, MediaElem mediaElem);

	/**
	 * Delete a mediaElem from an answer
	 * 
	 * @param answer
	 * @param mediaElem
	 */
	
	void deleteMediaElemFromAnswer(TemplateExamAnswer answer, MediaElem mediaElem);
	
	/**
	 * Find questions in the questions' pool using a criteria.
	 * 
	 * @param query
	 * @return
	 */
	List<TemplateExamQuestion> find(Query query);
	
	/**
	 * 
	 * Delete <code>question</code> from the questions' pool.
	 * 
	 * @param question
	 */
	void deleteQuestion(TemplateExamQuestion question);
	
	/** Duplica (en profundidad) la pregunta pasada por parametro.
	 * En profundidad quiere decir que:
	 * - Duplica los elementos multimedia de la pregunta si los hubiere
	 * - Duplica las respuestas de la pregunta si las hubiere, y ademas duplica sus correspondientes archivos multimedia.
	 * @param question : Pregunta a duplicar
	 * @param copy : Indica si el final del titulo hay que añadirle "copia" o no
	 * @return La pregunta resultado de la duplicacion.
	 */
	TemplateExamQuestion copyQuestion(TemplateExamQuestion question,boolean copy);

	/**
	 * Fills all the object attributes from the DB just using the id of the question
	 * @param question
	 * @return
	 */
	TemplateExamQuestion getQuestionFromId(TemplateExamQuestion question);
	

	/**
	 * 
	 * @param group and orderby ("title", "min" minutes, "sdate" start date, "edate" end date)
	 * 
	 * @return Returns the list of exam configurations associated to a group, sorted by the field "orderby"
	 */
	List<ConfigExam> getGroupConfigExams(Group group, String orderby);
	
	
	/**
	 * 
	 * Removes <code>exam</code> from the exam's pool.
	 * 
	 * @param exam
	 */
	void deleteConfigExam(ConfigExam exam);
	
	/**
	 * 
	 * Updates the active review field.
	 * 
	 * @param exam
	 */	
	void updateExamReview(ConfigExam exam);
	
	/**
	 * Saves the exam configuration into the database
	 * 
	 * @param exam
	 * @return id of the exam saved if update
	 */
	void saveExam(ConfigExam exam);
	
	/**
	 * Saves the subject into the exam into the database.
	 * <p>
	 * The subject is updated to include the assigned id from the database
	 * </p>
	 * 	 * 
	 * @param exam
	 * @param subject
	 */
	
	void saveSubjectToExam(ConfigExam exam, ConfigExamSubject subject);
	
	/**
	 * Delete a subject from an exam
	 * 
	 * @param exam
	 * @param subject
	 */
	
	void deleteSubjectFromExam(ConfigExam exam, ConfigExamSubject subject, long idGroup);

	/**
	 * Fills all the object attributes from the DB just using the id of the exam configuration
	 * @param exam configuration (just id filled)
	 * @return exam configuration (filled)
	 */
	ConfigExam getConfigExamFromId(ConfigExam exfromdb);

	/**
	 * Fills all the object attributes from the DB related to exams, learners and grades related
	 * to the parameter query
	 * @param QueryGrade
	 * @return list of student grades with the exam configuration and the related student info that comply with the query
	 */
	public List<TemplateGrade> find(QueryGrade query);

	/**
	 * Returns the list of users (students) related to the current group sorted by surname
	 * @param currentGroup
	 * @return list of users (students) related to the current group sorted by surname
	 */
	List<User> getStudents(Group currentGroup);
	
	/**
	 * Returns the list of users (students) related to the current group sorted by surname
	 * @param currentGroup
	 * @param orderby	Constant to assert the order criteria
	 * @return list of users (students) related to the current group sorted by orderby
	 */
	List<User> getStudents(Group currentGroup, String orderby);
	
	/**
	 * Returns the list of exam stats related to the current group, sorted by title
	 * @param currentGroup
	 * @return list of exam stats related to the current group, sorted by title 
	 */
	
	List<ExamStats> getExamStatsByGroup(Group currentGroup);
	
	/**
	 * Returns the list of learner stats related to the current group, sorted by surname and name
	 * @param currentGroup
	 * @return list of learner stats related to the current group, sorted by surname and name 
	 */
	
	List<LearnerStats> getLearnerStatsByGroup(Group currentGroup);
	
	/**
	 * Returns the list of question stats related to the current group, sorted by subject and text
	 * @param currentGroup
	 * @return list of question stats related to the current group, sorted by subject and text
	 */
	
	List<QuestionStats> getQuestionStatsByGroup(Group currentGroup);
	
		
	/**
	 * Returns the list of question stats related to the current exam, sorted by subject and text
	 * @param currentExam
	 * @return list of question stats related to the current exam, sorted by subject and text
	 */
	
	List<QuestionStats> getQuestionStatsByExam(ConfigExam currentExam);
	
	/**
	 * Validate an exam configuration
	 * 
	 * @param exam
	 * @return
	 */
	List<Message> validate(ConfigExam exam);

	/**
	 * Saves the subject into the database.
	 * <p>
	 * The subject is updated to include the assigned id from the database
	 * </p>
	 * 	 * 
	 * @param subject to be saved or added
	 * @return Object with the id filled.
	 */
	Subject saveSubject(Subject theme);

	/**
	 * Deletes a subject from the database.
	 * <p>
	 * It uses the id of the object to locate the object to be deleted
	 * </p>
	 * 	 * 
	 * @param subject to be deleted
	 */
	void deleteSubject(Subject thm);

	
	/**
	 * Obtains the data from a student, given the userName
	 * @param studentUserName
	 * @return user object
	 */
	User getUserData(String studentUserName);

	/**
	 * Obtains the data from a student, given the id
	 * @param student id
	 * @return user object
	 */
	User getUserData(Long id);
	
	/**
	 * Retrieves all the data from an exam already performed by a student.
	 * This is the way a tutor may check an exam from a student
	 * @param idStd student id
	 * @param idConfigExam exam configuration id
	 * @return Exam object with all information about questions, answers, etc.
	 */
	Exam getStudentExam(Long idStd, Long idConfigExam);

	
	/**
	 * Returns the list of users (students) not registered in the current group, sorted by surname
	 * @param currentGroup
	 * @return list of users (students) not registered in the current group but linked to the same
	 * "institution" of the the group sorted by surname
	 */
	List<User> getStudentsNotRegistered(Group currentGroup);
	
	/**
	 * Returns the list of users (students) of a apecified role not registered in the current group, sorted by surname
	 * @param currentGroup
	 * @param role (LEARNER or KID)
	 * @return list of users (students) not registered in the current group but linked to the same
	 * "institution" of the the group sorted by surname
	 */
	List<User> getStudentsNotRegistered(Group currentGroup, String role);

	/**
	 * Registers the student in the group passed as a parameter
	 * @param student	Student object previously filled.
	 * @param group	Group object previously fille.
	*/
	void registerStudent(User student, Group group);

	/**
	 * Un-Registers the student from the group passed as a parameter
	 * @param student	Student object previously filled.
	 * @param group	Group object previously fille.
	*/
	void unRegisterStudent(User student, Group group);

	/**
	 * Saves the data of a student, including username and password. If the id of the user is null,
	 * it should add the student as a new user.
	 * 
	 * @param student		Student user object
	 * @param inst			Institution which the user is related (from current group)
	 * @return				false if trying to isert when the userName is already used. 
	 */
	boolean saveStudent(User student, Institution inst);

	/**
	 * Checks if the userName is available or not
	 * 
	 * @param userName		string as the user name
	 * @return true if the userName is free, false if the userName is already used
	 */
	boolean userNameIsAvailable(String userName);

	
	/**
	 * Gets a subject from the database
	 * @param idSbj	Id of the theme
	 * @return		Subject object with all the params filled in
	 */
	Subject getSubject(Long idSbj);

	
	/**
	 * Performs a copy of the questions of the list into the given subject.
	 * It has to copy all the data, including the mmedia. The resulting questions (imported) were
	 * not used in any exam, then that field has to be cleared.
	 * @param group					Destination group`of the question
	 * @param sbj						Subject object
	 * @param preImportedQuestionList	List of questions to import
	 * @param sourcePath				Full Path of the source files (either for questions and answers)
	 * 
	 * @return false if there was a problem copying files
	 */
	boolean importQuestions(Group group, Subject sbj, List<TemplateExamQuestion> preImportedQuestionList, String sourcePath);

	
	
	/**
	 * Utility to obtain random filename. Used in "FileUploadController" and
	 * in "TutorQuestionListManagementController". That's why this method is here.
	 * 
	 * @return random string beginning with "med"
	 */
	public String randomFilename();

	/**
	 * Deletes from the database a grade and an exam already performed by a student.
	 * Should delete just one grade and one exam.
	 * @param idstd	Id of the student
	 * @param idex		Id of the exam configuration
	 */
	void deleteStudentGradeAndExam(Long idstd, Long idex);
	
	/** Copy entire syllabus from a source group to another group
	 * If the destination group syllabus was empty, it will be filled with source syllabus, preserving the original order of its subjects
	 * But, if destination group syllabus was not empty, subjects from source syllabus will be at the end of destination syllabus, preserving the original order.
	 * Copying syllabus implies, recursively, to copy all the questions related to it
	 * This method gets a fully independent and working copy of original syllabus for destination group
	 * @param sourceGroup Source group
	 * @param destinationGroup Destination group
	 * @return True if syllabus copy was done succesfully
	 */
	public boolean syllabusCopy(Group sourceGroup, Group destinationGroup);
	
	/** Copy a subject from a source group to another destination group (could be the same)
	 * Copying a subject implies to copy all the questions related to it.
	 * This method gets a fully independent and working copy of original subject on destination group
	 * IMPORTANT: If the original subject and source group are not related (for example, subject does not belong to source group), will NOT be questions for subject copy
	 * @param originalSubject Original subject to be copied
	 * @param sourceGroup Original subject group
	 * @param destinationGroup Destination group for subject copy
	 * @return Subject copy object
	 */
	public Subject subjectCopy(Subject originalSubject, Group sourceGroup, Group destinationGroup);
	
	/** Deletes a multimedia file from the system
	 * @param filename Name of the file to be deleted (without path)
	 * @return true if the file was succesfully deleted
	 */
	public boolean deleteMmediaFile (String filename);
	
	/** Duplicates an exam from configured exams list
	 * 
	 * @param examSource ConfigExam to copy from
	 * @return Exam copy
	 */
	public ConfigExam configExamCopy (ConfigExam examSource);
	
	/** Obtain number of questions for a subject, group and difficulty given
	 * 
	 * @param subject
	 * @param group
	 * @param difficulty
	 * @return
	 */
	public long getQuestionsNumber(Subject subject, Group group, Long difficulty);
	
	/** Overloads getQuestionsNumber
	 * For testing purposes, but fully working
	 * @param subjectId
	 * @param groupId
	 * @param difficulty
	 * @return
	 */
	public long getQuestionsNumber(Long subjectId, Long questionType, Long groupId, Long difficulty);
	
	
	/** Obtain minimum number of answer for subject, group and difficulty given
	 * 
	 * @param subjectId Subject ID
	 * @param groupId Group ID
	 * @param difficulty Difficulty filter for the questions
	 * @return Number of questions
	 * */
	
	public long getAnswerMinNumber(Long subjectId,Long questionType, Long groupId, Long difficulty);
	
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
	 * Return a list width the exams ids which have this question.
	 * @param question
	 * @return
	 * */
	public List<Long> getExamIds(TemplateExamQuestion question);

	/**
	 * Return the number of all question for this theme
	 * @param idTheme
	 * @param idGroup
	 * @param difficulty
	 * @return number of all question for this theme
	 * */
	public Long getTotalQuestion(Long idTheme, Long questionType, Long idGroup, Long difficulty);

	/**
	 * Return an instance of a group by the group's name
	 * @param groupName the group's name
	 * @return an instance of a group
	 * */
	public Group getGroupData(String groupName);

	/**
	 * Return a list of users who aren't in the custom exam
	 * @param examId exam's id
	 * @param groupId group's id
	 * @return list of users who aren't in the custom exam
	 * */
	public List<User> getUsersNotInCustomExam(Long examId, Long groupId);

	/**
	 * Return a list of users who are in the custom exam
	 * @param examId the exam's id
	 * @return Return List of users
	 * */
	List<CustomExamUser> getUsersInCustomExam(Long examId);

	/**
	 * Add an user to the custom exam table
	 * @param idUser 
	 * */
	void addUser2CustomExam(long idExam, long idUser);

	void removeUserFromCustomExam(long userId, Long examId);

	/**
	 * Returns if the given theme's name is repeated
	 * @return true if theme is repeated else false
	 * */
	boolean isThemeRepeat(String themeText, long groupId);

	/**
	 * Checks if user is registered in current group
	 * */
	boolean isUserInGroup(long idUser, long idGroup);

	/**
	 * Checks and updates questions that not are used in exam
	 * */
	void updateQuestionNotUsedInExam(Long idgrp);
	
}
