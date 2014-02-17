package com.cesfelipesegundo.itis.dao.api;

import es.itest.engine.test.business.entity.Item;
import es.itest.engine.test.business.entity.ItemResponse;
import es.itest.engine.test.business.entity.MediaElem;

public interface TemplateExamAnswerDAO {

	/**
	 * Updates <code>answer</code> in the database
	 * 
	 * @param answer
	 */
	void update(ItemResponse answer);

	/**
	 * Inserts <code>answer</code> in the database.
	 * 
	 * <p><code>answer</code> is updated to reflect the database identifier</p>
	 * 
	 * @param answer
	 */
	void save(ItemResponse answer);
	
	/**
	 * Delete <code>answer</code> from the database.
	 * 
	 * @param answer
	 */
	
	void delete(ItemResponse answer);
	
	
	/**
	 * Updates <code>mediaElem</code> from the answer in the database.
	 * 
	 * @param answer
	 * @param mediaElem
	 */
	
	void update(ItemResponse answer, MediaElem mediaElem);
	
	/**
	 * Inserts mediaElem into answer into the database
	 * 
	 * <p><code>mediaElem</code> is updated to reflect the database identifier</p>
	 * 
	 * @param question
	 * @param mediaElem
	 */
	
	void save(ItemResponse answer, MediaElem mediaElem);
	
	
	/**
	 * Delete a mediaElem from an answer from the database
	 * 
	 * @param answer
	 * @param mediaElem
	 */
	
	public void delete(ItemResponse answer, MediaElem mediaElem);

}
