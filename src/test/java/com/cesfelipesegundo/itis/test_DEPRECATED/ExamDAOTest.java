package com.cesfelipesegundo.itis.test_DEPRECATED;

import java.util.List;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.cesfelipesegundo.itis.dao.ExamDAOImpl;
import com.cesfelipesegundo.itis.dao.api.ExamDAO;
import com.cesfelipesegundo.itis.model.Exam;
import com.cesfelipesegundo.itis.model.ExamAnswer;
import com.cesfelipesegundo.itis.model.ExamQuestion;
import com.cesfelipesegundo.itis.model.Institution;
import com.cesfelipesegundo.itis.model.MediaElem;
import com.cesfelipesegundo.itis.model.User;

public class ExamDAOTest extends AbstractDependencyInjectionSpringContextTests {
	
	protected String[] getConfigLocations() {
		return new String [] {"classpath:com/cesfelipesegundo/itis/dao/config/spring-dao.xml"};    
	}
	
	public void testGetExam() {
		System.out.println("Examen:");
        ExamDAO examDAO = (ExamDAOImpl)this.applicationContext.getBean("examDAO");
        // Pruebo con un id de examen y usuario: 1, 1
        User user = new User();
        user.setId(Long.valueOf(1));
        Exam exam = examDAO.getAlreadyDoneExam(user, Long.valueOf(1));
        // Listo todas las propiedades:
        System.out.println("id: " + exam.getId());
        System.out.println("title: " + exam.getTitle());
        System.out.println("group.name: " + exam.getGroup().getName());
        System.out.println("duration: " + exam.getDuration());
        System.out.println("Nota máxima: " + exam.getMaxGrade());
    	Institution institution = exam.getGroup().getInstitution();
    	System.out.println("Id del centro: " +  institution.getId());
    	System.out.println("Dirección: " +  institution.getAddress());
    	System.out.println("Ciudad: " +  institution.getCity());
    	System.out.println("Nombre: " +  institution.getName());
    	System.out.println("Provinvia: " +  institution.getState());
    	System.out.println("Cod. Postal: " +  institution.getZipCode());
    	System.out.println("Código: " +  institution.getCode());

        List<ExamQuestion> questions = exam.getQuestions();
        for(ExamQuestion question : questions) {
        	System.out.println("Id de la pregunta: " + question.getId());
        	System.out.println("Texto de la pregunta: " + question.getText());
        	System.out.println("Número de respuestas correctas: " + question.getNumCorrectAnswers());
        		
        	List<MediaElem> questionMedias = question.getMmedia();
        	for(MediaElem questionMedia : questionMedias) {
        		System.out.println("Id del multimedia: " + questionMedia.getId());
        		System.out.println("Path: " + questionMedia.getPath());
        		System.out.println("Tipo: " + questionMedia.getType());
        		System.out.println("Orden: " + questionMedia.getOrder());
        		System.out.println("Nombre: " + questionMedia.getName());
        	}
        		
        	List<ExamAnswer> answers = question.getAnswers();
        	for(ExamAnswer answer : answers) {
        		System.out.println("Id de la respuesta: " + answer.getId());
        		System.out.println("Texto: " + answer.getText());
        		System.out.println("Marcada: " + answer.getMarked());
        		System.out.println("Solución: " + answer.getSolution());
        		System.out.println("Valor: " + answer.getValue());
        		
            	List<MediaElem> answerMedias = answer.getMmedia();
            	for(MediaElem answerMedia : answerMedias) {
            		System.out.println("Id del multimedia: " + answerMedia.getId());
            		System.out.println("Path: " + answerMedia.getPath());
            		System.out.println("Tipo: " + answerMedia.getType());
            		System.out.println("Orden: " + answerMedia.getOrder());
            		System.out.println("Nombre: " + answerMedia.getName());
            	}
        	}
        }        
        System.out.println("Fin de test..." );
    }
	
}
