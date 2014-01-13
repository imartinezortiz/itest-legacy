package com.cesfelipesegundo.itis.dao.api;

import java.util.List;

import com.cesfelipesegundo.itis.model.MediaElem;
import com.cesfelipesegundo.itis.model.Query;
import com.cesfelipesegundo.itis.model.TemplateExamQuestion;

public interface TemplateExamQuestionDAO {

	/**
	 * Updates <code>question</code> in the database.
	 * 
	 * @param question
	 */
	void update(TemplateExamQuestion question);

	/**
	 * Inserts <code>question</code> into the database
	 * 
	 * <p><code>question</code> is updated to reflect the database identifier</p>
	 * 
	 * @param question
	 */
	void save(TemplateExamQuestion question);
	
	/**
	 * Updates <code>mediaElem</code> from the question in the database.
	 * 
	 * @param question
	 * @param mediaElem
	 */
	
	void update(TemplateExamQuestion question, MediaElem mediaElem, boolean isQuestion);
	
	/**
	 * Inserts mediaElem into question into the database
	 * 
	 * <p><code>mediaElem</code> is updated to reflect the database identifier</p>
	 * 
	 * @param question
	 * @param mediaElem
	 */
	
	void save(TemplateExamQuestion question, MediaElem mediaElem, boolean isQuestion);
	
	/**
	 * Delete a mediaElem from a question from the database
	 * 
	 * @param question
	 * @param mediaElem
	 */
	
	void delete(TemplateExamQuestion question, MediaElem mediaElem, boolean isQuestion);

	/**
	 * 
	 * Find questions in the questions' pool using Query as criteria.
	 * 
	 * @param query
	 * @return
	 */
	List<TemplateExamQuestion> find(Query query);

	/**
	 * Delete <code>question</code> from the database
	 * @param question
	 */
	void delete(TemplateExamQuestion question);
	
	/**
	 * Fills all the object attributes from the DB just using the id of the question
	 * @param question
	 * @return
	 */
	TemplateExamQuestion getQuestionFromId(TemplateExamQuestion question);
	
	
	/**
	 * Fill question's <code>MediaElem</code> elements.
	 * 
	 * @param question
	 */
	void fillMediaElem(TemplateExamQuestion question);

	/**
	 * 
	 * Updates used_in_exam field
	 * 
	 * @param templateQuestion
	 */
	void updateUsedInExam(TemplateExamQuestion templateQuestion);

	/**
	 * Fills the answers of a question (including mmedia of answers)
	 * Used to show the number of answers in the question list.
	 * @param question
	 */
	void fillAnswers(TemplateExamQuestion templateQuestion);

	/**
	 * Checks and updates questions that not are used in exam
	 * */
	void updateQuestionNotUsedInExam(Long idgrp);

}
