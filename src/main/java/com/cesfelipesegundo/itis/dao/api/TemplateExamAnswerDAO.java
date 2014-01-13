package com.cesfelipesegundo.itis.dao.api;

import com.cesfelipesegundo.itis.model.MediaElem;
import com.cesfelipesegundo.itis.model.TemplateExamAnswer;
import com.cesfelipesegundo.itis.model.TemplateExamQuestion;

public interface TemplateExamAnswerDAO {

	/**
	 * Updates <code>answer</code> in the database
	 * 
	 * @param answer
	 */
	void update(TemplateExamAnswer answer);

	/**
	 * Inserts <code>answer</code> in the database.
	 * 
	 * <p><code>answer</code> is updated to reflect the database identifier</p>
	 * 
	 * @param answer
	 */
	void save(TemplateExamAnswer answer);
	
	/**
	 * Delete <code>answer</code> from the database.
	 * 
	 * @param answer
	 */
	
	void delete(TemplateExamAnswer answer);
	
	
	/**
	 * Updates <code>mediaElem</code> from the answer in the database.
	 * 
	 * @param answer
	 * @param mediaElem
	 */
	
	void update(TemplateExamAnswer answer, MediaElem mediaElem);
	
	/**
	 * Inserts mediaElem into answer into the database
	 * 
	 * <p><code>mediaElem</code> is updated to reflect the database identifier</p>
	 * 
	 * @param question
	 * @param mediaElem
	 */
	
	void save(TemplateExamAnswer answer, MediaElem mediaElem);
	
	
	/**
	 * Delete a mediaElem from an answer from the database
	 * 
	 * @param answer
	 * @param mediaElem
	 */
	
	public void delete(TemplateExamAnswer answer, MediaElem mediaElem);

}
