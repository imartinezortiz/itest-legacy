package com.cesfelipesegundo.itis.dao.api;

import java.util.List;
import java.util.Map;

import com.cesfelipesegundo.itis.model.Course;
import com.cesfelipesegundo.itis.model.CourseStats;
import com.cesfelipesegundo.itis.model.Institution;

import es.itest.engine.course.business.entity.Group;
import es.itest.engine.course.business.entity.Subject;
import es.itest.engine.test.business.entity.TestSubject;

public interface CourseDAO {

	List<TestSubject> getSubjects(Group group);

	Course getCourse(Long id);
	
	/**
	 * Deletes a Course from the database
	 * @param Course
	 */
	public void deleteCourse(Course course);

	/**
	 * Returns the course with the same code
	 * @param code
	 * @return Course with the same code
	 * */
	public Course getCourseByCode(String code);
	
	/**
	 * Inserts or updates a Course data into the database
	 * @param group
	 * @return Course object with the id filled
	 */
	public Course saveCourse(Course course);
	
	/**
	 * Returns the list of courses
	 * @return list of courses
	 */
	
	public List<Course> getCourses();

	/**
	 * Returns the list of courses of an specific institution
	 * @return list of courses
	 */
	List<Course> getCourses(long institutionId);

	/**
	 * Return the stats from the current course
	 * @return
	 * */
	CourseStats getCourseStats(long institution, long course, String year);

	/**
	 * Returns the list of Courses of an specific institution and group
	 * @param idInstitution
	 * @param year
	 * @return list of groups of the institution passed as parameter
	 */
	List<Course> getCourseByInstitutionAndGroup(long idInstitution, String year);

	/**
	 * Returns the list Courses filter by the attributes
	 * @param codigo
	 * @param nombre
	 * @param curso
	 * @param estudios
	 * @return list of courses
	 */
	List<Course> getCoursesFiltered(String codigo, String nombre, String curso, String estudios);
	
}
