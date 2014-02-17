package com.cesfelipesegundo.itis.dao.api;

import java.util.List;

import es.itest.engine.course.business.entity.Group;
import es.itest.engine.course.business.entity.Subject;
import es.itest.engine.test.business.entity.Item;

/**
 * Interfaz DAO para el modelo <code>Subject</code>.
 * @author José Manuel Colmenar
 *
 */
public interface SubjectDAO extends DAO {

	/**
	 * Returns the information of a subject
	 * 
	 * <p>Returns the information stored in the subjects table</p>
	 * 
	 * @param id Identifier of the subject
	 * 
	 * @return Subject
	 */
	public Subject getSubject(Long id);

	
	/**
	 * Deletes a subject from the database
	 * @param theme
	 */
	public void deleteSubject(Subject theme);

	
	/**
	 * Inserts or updates a subject data into the database
	 * @param theme
	 * @return Subject object with the id filled
	 */
	public Subject saveSubject(Subject theme);

	
	/**
	 * Obtain the list of subjects (syllabus) of the group from the database.
	 * Returns only the minimum information for each subject (theme): subject and order.
	 * 
	 * @param group object
	 * @return list of subjects.
	 */
	public List<Subject> getSubjectsMinInfo(Group g);
	
	/** Obtain the syllabus of the group from database.
	 * Some Subject class members, like "group", would be at null value
	 * 
	 * @param g Group of which we want to get its syllabus
	 * @return List<Subject> representing syllabus of Group g
	 */
	public List<Subject> getSubjectsOrderedAsc(Long groupId);
	
	/** Obtain a questions list related to given subject and group
	 * 
	 * @param subjectId
	 * @param groupId
	 * @return
	 */
	public List<Item> getQuestionsBySubjectAndGroupId(Long subjectId,Long groupId);

	/** Obtain number of questions for subject, group and difficulty given
	 * 
	 * @param subjectId Subject ID
	 * @param groupId Group ID
	 * @param difficulty Difficulty filter for the questions
	 * @return Number of questions
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
	 * Return the number of all question for this theme
	 * @param idTheme
	 * @param idGroup
	 * @param difficulty
	 * @return number of all question for this theme
	 * */
	public Long getTotalQuestion(Long idTheme, Long questionType, Long idGroup, Long difficulty);

	/**
	 * Returns if the given theme's name is repeated
	 * @return true if theme is repeated else false
	 * */
	public boolean isThemeRepeat(String themeText, long groupId);
	
	/**
	 * Return the subject of the question
	 * @param id question's id
	 * */
	public Subject getSubjectByQuestionId(Long id);
}