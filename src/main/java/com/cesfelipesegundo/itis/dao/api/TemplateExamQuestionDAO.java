package com.cesfelipesegundo.itis.dao.api;

import java.util.List;

import com.cesfelipesegundo.itis.model.Query;

import es.itest.engine.test.business.entity.Item;
import es.itest.engine.test.business.entity.MediaElem;

public interface TemplateExamQuestionDAO {

	/**
	 * Updates <code>question</code> in the database.
	 * 
	 * @param question
	 */
	void update(Item question);

	/**
	 * Inserts <code>question</code> into the database
	 * 
	 * <p><code>question</code> is updated to reflect the database identifier</p>
	 * 
	 * @param question
	 */
	void save(Item question);
	
	/**
	 * Updates <code>mediaElem</code> from the question in the database.
	 * 
	 * @param question
	 * @param mediaElem
	 */
	
	void update(Item question, MediaElem mediaElem, boolean isQuestion);
	
	/**
	 * Inserts mediaElem into question into the database
	 * 
	 * <p><code>mediaElem</code> is updated to reflect the database identifier</p>
	 * 
	 * @param question
	 * @param mediaElem
	 */
	
	void save(Item question, MediaElem mediaElem, boolean isQuestion);
	
	/**
	 * Delete a mediaElem from a question from the database
	 * 
	 * @param question
	 * @param mediaElem
	 */
	
	void delete(Item question, MediaElem mediaElem, boolean isQuestion);

	/**
	 * 
	 * Find questions in the questions' pool using Query as criteria.
	 * 
	 * @param query
	 * @return
	 */
	List<Item> find(Query query);

	/**
	 * Delete <code>question</code> from the database
	 * @param question
	 */
	void delete(Item question);
	
	/**
	 * Fills all the object attributes from the DB just using the id of the question
	 * @param question
	 * @return
	 */
	Item getQuestionFromId(Item question);
	
	
	/**
	 * Fill question's <code>MediaElem</code> elements.
	 * 
	 * @param question
	 */
	void fillMediaElem(Item question);

	/**
	 * 
	 * Updates used_in_exam field
	 * 
	 * @param templateQuestion
	 */
	void updateUsedInExam(Item templateQuestion);

	/**
	 * Fills the answers of a question (including mmedia of answers)
	 * Used to show the number of answers in the question list.
	 * @param question
	 */
	void fillAnswers(Item templateQuestion);

	/**
	 * Checks and updates questions that not are used in exam
	 * */
	void updateQuestionNotUsedInExam(Long idgrp);

}
