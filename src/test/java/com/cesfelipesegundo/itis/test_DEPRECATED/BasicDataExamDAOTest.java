package com.cesfelipesegundo.itis.test_DEPRECATED;

import java.util.List;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.cesfelipesegundo.itis.dao.BasicDataExamDAOImpl;
import com.cesfelipesegundo.itis.dao.api.BasicDataExamDAO;
import com.cesfelipesegundo.itis.model.BasicDataExam;
import com.cesfelipesegundo.itis.model.Institution;

public class BasicDataExamDAOTest extends AbstractDependencyInjectionSpringContextTests {
	
	protected String[] getConfigLocations() {
		return new String [] {"classpath:com/cesfelipesegundo/itis/dao/config/spring-dao.xml"};    
	}
	
	public void testGetExams(){
		System.out.println("Listado exámenes pendientes para un alumno:");
        BasicDataExamDAO basicDataExamDAO = (BasicDataExamDAOImpl)this.applicationContext.getBean("basicDataExamDAO");
        // Pruebo con un id de usuario: 1
        List<BasicDataExam> examList = basicDataExamDAO.getPendingExams(Long.valueOf(1));
        for(BasicDataExam exam : examList) {
        	System.out.println("Id del examen: " + exam.getId());
        	System.out.println("Título del examen: " + exam.getTitle());
        	System.out.println("Id del grupo: " +  exam.getGroup().getId());
        	System.out.println("Nombre del grupo: " +  exam.getGroup().getName());
        	System.out.println("Id del curso: " +  exam.getGroup().getCourse().getId());
        	System.out.println("Nombre del curso: " +  exam.getGroup().getCourse().getName());
        	Institution institution = exam.getGroup().getInstitution();
        	System.out.println("Id del centro: " +  institution.getId());
        	System.out.println("Dirección: " +  institution.getAddress());
        	System.out.println("Ciudad: " +  institution.getCity());
        	System.out.println("Nombre: " +  institution.getName());
        	System.out.println("Provinvia: " +  institution.getState());
        	System.out.println("Cod. Postal: " +  institution.getZipCode());
        	System.out.println("Código: " +  institution.getCode());
        }
        System.out.println("Listado exámenes hechos por un alumno:");
        // Pruebo con un id de usuario: 1
        examList = basicDataExamDAO.getExamsForRevision(Long.valueOf(1));
        for(BasicDataExam exam : examList) {
        	System.out.println("Id del examen: " + exam.getId());
        	System.out.println("Título del examen: " + exam.getTitle());
        	System.out.println("Id del grupo: " +  exam.getGroup().getId());
        	System.out.println("Nombre del grupo: " +  exam.getGroup().getName());
        	System.out.println("Id del curso: " +  exam.getGroup().getCourse().getId());
        	System.out.println("Nombre del curso: " +  exam.getGroup().getCourse().getName());
        	Institution institution = exam.getGroup().getInstitution();
        	System.out.println("Id del centro: " +  institution.getId());
        	System.out.println("Dirección: " +  institution.getAddress());
        	System.out.println("Ciudad: " +  institution.getCity());
        	System.out.println("Nombre: " +  institution.getName());
        	System.out.println("Provinvia: " +  institution.getState());
        	System.out.println("Cod. Postal: " +  institution.getZipCode());
        	System.out.println("Código: " +  institution.getCode());
        }
        System.out.println("Fin de test..." );
	  }
	
	public void testCalif() {
        BasicDataExamDAO basicDataExamDAO = (BasicDataExamDAOImpl)this.applicationContext.getBean("basicDataExamDAO");
		System.out.println("Inserción de una nota en log_exams.");
		basicDataExamDAO.addNewCalif(Long.valueOf(1), Long.valueOf(1), "127.0.0.1", System.currentTimeMillis());
		System.out.println("Actualización de una nota en log_exams.");
		basicDataExamDAO.updateCalif(Long.valueOf(1), Long.valueOf(1), System.currentTimeMillis(), Double.valueOf(1037373.32498749873), Integer.valueOf(65));
	}
}
