package com.cesfelipesegundo.itis.test_DEPRECATED;

import java.util.List;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.cesfelipesegundo.itis.dao.TemplateExamDAOImpl;
import com.cesfelipesegundo.itis.dao.api.TemplateExamDAO;
import com.cesfelipesegundo.itis.model.Course;
import com.cesfelipesegundo.itis.model.Group;
import com.cesfelipesegundo.itis.model.Institution;
import com.cesfelipesegundo.itis.model.MediaElem;
import com.cesfelipesegundo.itis.model.TemplateExam;
import com.cesfelipesegundo.itis.model.TemplateExamAnswer;
import com.cesfelipesegundo.itis.model.TemplateExamQuestion;
import com.cesfelipesegundo.itis.model.TemplateExamSubject;

public class TemplateExamDAOTest extends AbstractDependencyInjectionSpringContextTests {
	
	protected String[] getConfigLocations() {
		return new String [] {"classpath:com/cesfelipesegundo/itis/dao/config/spring-dao.xml"};    
	}
	
	public void testGetTemplate(){
		System.out.println("Configuración de examen:");
        TemplateExamDAO templateExamDAO = (TemplateExamDAOImpl)this.applicationContext.getBean("templateExamDAO");
        // Pruebo con un id de examen: 1
        TemplateExam exam = templateExamDAO.getTemplateExam(Long.valueOf(1));
        if(exam!=null) this.printTemplateExam(exam);
        System.out.println("Fin de test..." );
	  }

	public void testGetTemplateExamByGroupId(){
		System.out.println("testGetTemplateExamByGroupId:");
        //TemplateExamDAO templateExamDAO = (TemplateExamDAOImpl)this.applicationContext.getBean("templateExamDAO");
        Group group = new Group();
        group.setId(Long.valueOf(100));
        /*List<TemplateExam> exams = templateExamDAO.getGroupExams(group);
        for(TemplateExam exam : exams) {
        	this.printTemplateExam(exam);
        }*/
	}
	
	public void printTemplateExam(TemplateExam exam) {
		//exam.get
        // Listo todas las propiedades:
		System.out.println("exam.duration: " +  exam.getDuration());
		System.out.println("exam.endDate: " + exam.getEndDate());
        Group group = exam.getGroup();
        Course course = group.getCourse();
        System.out.println("exam.group.course.id: " +  course.getId());
        System.out.println("exam.group.course.name: " +  course.getName());
        System.out.println("exam.group.id: " +  group.getId());
    	Institution institution = group.getInstitution();
    	System.out.println("group.institution.id: " +  institution.getId());
    	System.out.println("group.institution.address: " +  institution.getAddress());
    	System.out.println("group.institution.city: " +  institution.getCity());
    	System.out.println("group.institution.name: " +  institution.getName());
    	System.out.println("group.institution.state: " +  institution.getState());
    	System.out.println("group.institution.zipCode: " +  institution.getZipCode());
    	System.out.println("group.institution.code: " +  institution.getCode());
    	System.out.println("exam.group.name: " +  group.getName());
    	System.out.println("exam.id: " +  exam.getId());
    	System.out.println("exam.maxGrade: " +  exam.getMaxGrade());
    	System.out.println("exam.questionDistribution: " +  exam.getQuestionDistribution());
    	System.out.println("exam.startDate: " +  exam.getStartDate());

    	List<TemplateExamSubject> subjects = exam.getSubjects();
        for(TemplateExamSubject subject : subjects) {
        	System.out.println("Id del tema: " + subject.getId());
        	System.out.println("Nombre del tema: " + subject.getSubject());
        	System.out.println("Número de preguntas: " + subject.getQuestionsNumber());
        	System.out.println("Número de respuestas por pregunta: " + subject.getAnswersxQuestionNumber());
        	System.out.println("Máxima dificultad: " + subject.getMaxDifficulty());
        	System.out.println("Mínima dificultad: " + subject.getMinDifficulty());
        	
        	List<TemplateExamQuestion> questions = subject.getQuestions();
        	for(TemplateExamQuestion question : questions) {
        		System.out.println("Id de la pregunta: " + question.getId());
        		System.out.println("Texto de la pregunta: " + question.getText());
        		System.out.println("Número de preguntas correctas: " + question.getNumCorrectAnswers());
        		System.out.println("Usada en examen: " + question.getUsedInExam());
        		
        		List<MediaElem> questionMedias = question.getMmedia();
        		for(MediaElem questionMedia : questionMedias) {
        			System.out.println("Id del multimedia: " + questionMedia.getId());
        			System.out.println("Path: " + questionMedia.getPath());
        			System.out.println("Tipo: " + questionMedia.getType());
        			System.out.println("Orden: " + questionMedia.getOrder());
        			System.out.println("Nombre: " + questionMedia.getName());
        		}
        		
        		List<TemplateExamAnswer> answers = question.getAnswers();
        		for(TemplateExamAnswer answer : answers) {
        			System.out.println("Id de la respuesta: " + answer.getId());
        			System.out.println("Texto: " + answer.getText());
        			System.out.println("Marcada: " + answer.getMarked());
        			System.out.println("Activa: " + answer.getActive());
        			System.out.println("Solución: " + answer.getSolution());
        			System.out.println("Valor: " + answer.getValue());
        			System.out.println("Usada en examen: " + answer.getUsedInExam());
        			
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
        }
        System.out.println("exam.title: " +  exam.getTitle());
        System.out.println("exam.visibility: " +  exam.getVisibility());
        System.out.println("exam.activeReview: " +  exam.isActiveReview());
	}
}
