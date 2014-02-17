package com.cesfelipesegundo.itis.dao.api;

import java.util.List;

import com.cesfelipesegundo.itis.model.User;

import es.itest.engine.course.business.entity.Group;
import es.itest.engine.course.business.entity.TestSessionTemplateSubject;
import es.itest.engine.test.business.entity.TestSessionTemplate;

public interface ConfigExamDAO extends DAO {
		
	
	
	/**
	 * 
	 * Returns the exams associated to a group
	 * 
	 * @param group
	 * @return
	 */
	List<TestSessionTemplate> getGroupConfigExams(Group group);


	/**
	 * 
	 * Returns the exams associated to a group, order by: ("title", "min" minutes, "sdate" start date, "edate" end date)
	 * 
	 * @param group Group filter.
	 * @param orderBy Parameter used to specify the order of the resulted list.
	 * @return The list of ConfigExam objets which are configurated for this group and ordered by the orderBy parameter.
	 */
	List<TestSessionTemplate> getGroupConfigExams(Group group, String orderBy);

	/**
	 * 
	 * Updates <code>exam</code> active review flag in the database
	 * 
	 * @param exam
	 */
	void updateReview(TestSessionTemplate exam);
	
	/**
	 * Remove <code>exam</code> from the database.
	 * @param exam
	 */
	void delete(TestSessionTemplate exam);
	
	
	/**
	 * Updates <code>exam</code> in the database.
	 * 
	 * @param exam
	 */
	void update(TestSessionTemplate exam);

	/**
	 * Inserts <code>exam</code> into the database
	 * 
	 * <p><code>exam</code> is updated to reflect the database identifier</p>
	 * 
	 * @param exam
	 */
	void save(TestSessionTemplate exam);
	
	
	
	/**
	 * Updates <code>subject</code> from the exam in the database.
	 * 
	 * @param exam
	 * @param subject
	 */
		
	void update(TestSessionTemplate exam, TestSessionTemplateSubject subject);
	
	/**
	 * Inserts a TemplateSubject into exam into the database
	 * 
	 * <p><code>subject</code> is updated to reflect the database identifier</p>
	 * 
	 * @param exam
	 * @param subject
	 */
	
	void save(TestSessionTemplate exam, TestSessionTemplateSubject subject);
	
	/**
	 * Delete a TemplateSubject from a exam from the database
	 * 
	 * @param exam
	 * @param subject
	 */
	
	void delete(TestSessionTemplate exam, TestSessionTemplateSubject subject);

	/**
	 * Fills all the object attributes from the DB just using the id of the object
	 * @param config exam
	 * @return config exam
	 */
	TestSessionTemplate getTestSessionTemplate(long testSessionTemplateId);

	

}
