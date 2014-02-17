package com.cesfelipesegundo.itis.dao.api;

import java.util.List;

import com.cesfelipesegundo.itis.model.AnsweredQuestionData;
import com.cesfelipesegundo.itis.model.CalifData;
import com.cesfelipesegundo.itis.model.QuestionStats;

import es.itest.engine.course.business.entity.Group;
import es.itest.engine.test.business.entity.TestSession;
import es.itest.engine.test.business.entity.TestSessionTemplate;

public interface StatsDAO extends DAO {
	
	
	/**
	 * Returns a list of questionData with information about the questions appeared in all exams of the group  
	 * @param group
	 * @return list of questionData with information about the questions appeared in all exams of the group
	 */
	
	List<AnsweredQuestionData> getAnsweredQuestionsData(Group group);
	
	
	/**
	 * Returns a list of questionData with information about the questions appeared in an exam  
	 * @param exam
	 * @return list of questionData with information about the questions appeared in all exams of the group
	 */
	
	List<AnsweredQuestionData> getAnsweredQuestionsData(TestSessionTemplate exam);
	
	/**
	 * Returns a list of califsData with information about the califs obtained in all exams of the group
	 * @param group
	 * @return list of califsData with information about the califs obtained in all exams of the group
	 */
	
	List<CalifData> getCalifsData(Group group);

	/**
	 * Deleting the grade of the student in a given exam configuration. A student can only perform an exam for a given
	 * exam configuration.
	 * @param idstd	Student id
	 * @param idex		Configuration exam id
	 */
	void deleteStudentGrade(Long idstd, Long idex);

	/**
	 * Fill the list recived whith the AnswerStats
	 * @param questionStats list will be filled
	 * @param group 
	 * */
	void fillQuestionStatsByGroup(List<QuestionStats> questionStats, Group group);

	/**
	 * Fill the list recived whith the AnswerStats
	 * @param questionStats list will be filled
	 * @param currentExam 
	 * */
	void fillQuestionStatsByExam(List<QuestionStats> questionStats,
			TestSessionTemplate currentExam);
		
}
