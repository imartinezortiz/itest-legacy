package com.cesfelipesegundo.itis.dao.api;

import com.cesfelipesegundo.itis.model.AnswerExam;

import es.itest.engine.test.business.entity.ItemResponse;
import es.itest.engine.test.business.entity.ItemSessionResponse;

public interface AnswerExamDAO extends DAO {
	
	
	/**
	 * Add a new answer to the log of the exam 
	 * @param idExam, exam id
	 * @param idLearner, use id
	 * @param idQuestion, question id
	 * @param idAnswer, answer id
	 * @param startingDate, date of exam starting 
	 */
	
	void addNewExamAnswer(Long idExam, Long idLearner, Long idQuestion, Long idAnswer, Long startingDate );
	
	/**
	 * Updates the log of the exam in order to register an answer as "marked" or "not marked" in relation to a question, exam and learner
	 * @param idexam
	 * @param iduser
	 * @param idquestion
	 * @param idanswer
	 * @param marked
	 * @return number of rows updated
	 */  
	
	int updateExamAnswer(Long idexam, Long iduser, Long idquestion, Long idanswer, Long answerTime, Boolean marked);

	/**
	 * Updates the log of the exam in order to register the final grade and the answer time of an answer
	 * @param idexam
	 * @param iduser
	 * @param idquestion
	 * @param idanswer
	 * @param answerTime
	 * @param grade
	 */
	 
	
	
	void updateExamAnswerGrade(Long idexam, Long iduser, Long idquestion, Long idanswer, Double grade);

	
	/**
	 * 
	 * Updates used_in_exam field of this answer
	 * 
	 * @param templateAnswer
	 */
	void updateUsedInExam(ItemResponse templateAnswer);

	/**
	 * Updates the confidence level for a question exam
	 * @param examId
	 * @param userId
	 * @param questionId
	 * @param checked
	 * @return number of rows updated
	 * */
	int updateConfidenceLevel(long examId, long userId, long questionId,
			boolean checked,int questionType);
	
	int updateExamAnswer(Long idexam, Long iduser, Long idquestion, ItemSessionResponse eAnswer);

	void addNewExamFillAnswer(Long idExam, Long idUser, Long idQuestion,
			Long idAnswer, String textAnswer, long currentTimeMillis);

	int updateExamFillAnswer(Long idExam, Long idUser, Long idQuestion,
			Long idAnswer, String textAnswer, Long grade);
	
}
