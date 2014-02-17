package com.cesfelipesegundo.itis.dao.api;

import java.util.List;

import com.cesfelipesegundo.itis.model.ExamGlobalInfo;

import es.itest.engine.test.business.entity.TestDetails;

public interface BasicDataExamDAO extends DAO {
	
	/**
	 * Devuelve una lista con los datos de los exámenes que el alumno puede realizar en la fecha actual (aquellos activos a fecha
	 * actual, correspondientes a grupos en los que está matriculado el alumno y que no han sido ya hechos por este alumno). Los
	 * datos del examen son: id, título y asignatura. El identificador de alumno se pasa por parámetro. 
	 * @param id del alumno
	 * @return Lista de exámenes disponibles para ser realizados por el alumno, ordenados por nombre de asignatura. 
	 */
	List<TestDetails> getPendingExams(Long id);
	
	/**
	 * Devuelve una lista con los datos de los exámenes que el alumno ha realizado hasta la fecha actual, si están en su periodo de
	 * revisión. Los datos del examen son: id, título y asignatura. El identificador de alumno se pasa por parámetro. 
	 * @param id del alumno
	 * @return Lista de exámenes en periodo de revisión ya realizados por el alumno, ordenados por nombre de asignatura.
	 */
	List<TestDetails> getExamsForRevision(Long id);

	/**
	 * // Add a new calif with the starting date of the examn 
	 * @param idlearner, user id
	 * @param idexam, exam id
	 * @param ipAddress, user ipAddress  
	 * @param startingDate, starting date of the exam in Long format 
	 */
	
	void addNewCalif(Long idlearner, Long idexam, String ipAddress, Long startingDate);
	
	/**
	 * Update a calif with the final date and the final grade of the exam 
	 * @param idlearner, user id
	 * @param idexam, exam id
	 * @param finalDate, final date of the exam in Long format
	 * @param finalGrade, final grade of the exam
	 * @param durationExam, duration of the exam performed by the learner
	 */
	void updateCalif(Long idlearner, Long idexam, Long finalDate, Double finalGrade, Integer durationExam);
	
	/**
	 * Overloads updateCalif method 
	 * @param idlearner, user id
	 * @param idexam, exam id
	 * @param finalGrade, final grade of the exam
	 */
	void updateCalif(Long idLearner, Long idExam, Double finalGrade);
	
	/**
	 * Obtain a list of the next exams from this user
	 * @param idGroup 
	 * 
	 * @return List of the next exams
	 * */
	public List<TestDetails> getNextExams(long userId, long idGroup);
}
