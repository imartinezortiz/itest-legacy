package com.cesfelipesegundo.itis.test_DEPRECATED;

import java.util.Date;
import java.util.List;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.cesfelipesegundo.itis.dao.ConfigExamDAOImpl;
import com.cesfelipesegundo.itis.dao.api.ConfigExamDAO;
import com.cesfelipesegundo.itis.model.ConfigExam;
import com.cesfelipesegundo.itis.model.ConfigExamSubject;
import com.cesfelipesegundo.itis.model.Course;
import com.cesfelipesegundo.itis.model.Group;
import com.cesfelipesegundo.itis.model.Institution;
import com.cesfelipesegundo.itis.model.Subject;

public class ConfigExamDAOTest extends AbstractDependencyInjectionSpringContextTests {
	
	protected String[] getConfigLocations() {
		return new String [] {"classpath:com/cesfelipesegundo/itis/dao/config/spring-dao.xml"};    
	}
	
	public void testGetExam(){
		System.out.println("testGetExam:");
        ConfigExamDAO configExamDAO = (ConfigExamDAOImpl)this.applicationContext.getBean("configExamDAO");
        // Pruebo con un id de examen: 1
        Group group = new Group();
        group.setId(Long.valueOf(100));
        List<ConfigExam> exams = configExamDAO.getGroupConfigExams(group,"title");
        for(ConfigExam exam : exams)
        	this.printConfigExam(exam);
        System.out.println("Fin de test..." );
	}
	
	
	public void testDeleteExam(){
		System.out.println("testDelete:");
        ConfigExamDAO configExamDAO = (ConfigExamDAOImpl)this.applicationContext.getBean("configExamDAO");
        // Pruebo con un id de examen: 1
        ConfigExam exam = new ConfigExam();
        exam.setId(Long.valueOf(1));
        configExamDAO.delete(exam);
        System.out.println("Fin de test..." );
	}

	public void testDeleteSubjectExam(){
		System.out.println("testDeleteSubject:");
        ConfigExamDAO configExamDAO = (ConfigExamDAOImpl)this.applicationContext.getBean("configExamDAO");
        // Id de subject (1)
        ConfigExamSubject subject = new ConfigExamSubject();
        subject.setId(Long.valueOf(4));
        configExamDAO.delete(null, subject);
        System.out.println("Fin de test..." );
	}
	
	public void testSaveExam() {
		System.out.println("testSaveExam:");
        ConfigExamDAO configExamDAO = (ConfigExamDAOImpl)this.applicationContext.getBean("configExamDAO");
        // Pruebo con un id de examen: 1
        ConfigExam exam = new ConfigExam();
        exam.setActiveReview(false);
        exam.setDuration(20);
        exam.setEndDate(new Date(System.currentTimeMillis()+90000));
        Group group = new Group();
        group.setId(Long.valueOf(1));
        exam.setGroup(group);
        exam.setMaxGrade(10);
        exam.setQuestionDistribution(1);
        exam.setStartDate(new Date(System.currentTimeMillis()));
        exam.setTitle("TestExam");
        exam.setVisibility(1);
        configExamDAO.save(exam);
        System.out.println("Fin de test..." );		
	}

	public void testUpdateExam() {
		System.out.println("testUpdateExam:");
        ConfigExamDAO configExamDAO = (ConfigExamDAOImpl)this.applicationContext.getBean("configExamDAO");
        // Pruebo con un id de examen: 1
        ConfigExam exam = new ConfigExam();
        exam.setId(Long.valueOf(2));
        exam.setDuration(100);
        configExamDAO.update(exam);
        System.out.println("Fin de test..." );		
	}

	public void testUpdateExamReview() {
		System.out.println("testUpdateExamReview:");
        ConfigExamDAO configExamDAO = (ConfigExamDAOImpl)this.applicationContext.getBean("configExamDAO");
        // Pruebo con un id de examen: 1
        ConfigExam exam = new ConfigExam();
        exam.setId(Long.valueOf(2));
        exam.setActiveReview(false);
        configExamDAO.updateReview(exam);
        System.out.println("Fin de test..." );		
	}

	public void testUpdateExamSubject() {
		System.out.println("testUpdateExamSubject:");
        ConfigExamDAO configExamDAO = (ConfigExamDAOImpl)this.applicationContext.getBean("configExamDAO");
        // Pruebo con un id de examen: 1
        ConfigExam exam = new ConfigExam();
        exam.setId(Long.valueOf(100));
        ConfigExamSubject examSubject = new ConfigExamSubject();
        examSubject.setId(Long.valueOf(1));
        examSubject.setAnswersxQuestionNumber(1);
        examSubject.setQuestionsNumber(1);
        configExamDAO.update(exam,examSubject);
        System.out.println("Fin de test..." );		
	}

	public void testSaveExamSubject() {
		System.out.println("testSaveExamSubject:");
        ConfigExamDAO configExamDAO = (ConfigExamDAOImpl)this.applicationContext.getBean("configExamDAO");
        // Pruebo con un id de examen: 1
        ConfigExam exam = new ConfigExam();
        exam.setId(Long.valueOf(100));
        ConfigExamSubject examSubject = new ConfigExamSubject();
        Subject subject = new Subject();
        subject.setId(Long.valueOf(100));
        examSubject.setAnswersxQuestionNumber(5);
        examSubject.setMaxDifficulty(5);
        examSubject.setMinDifficulty(5);
        examSubject.setQuestionsNumber(5);
        examSubject.setSubject(subject);
        //subject.setSubject("Tema 1");
        configExamDAO.save(exam,examSubject);
        System.out.println("Fin de test..." );		
	}

	public void printConfigExam(ConfigExam exam) {
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

    	List<ConfigExamSubject> subjects = exam.getSubjects();
        for(ConfigExamSubject subject : subjects) {
        	System.out.println("Id del tema_exam: " + subject.getId());
        	System.out.println("Nombre del tema: " + subject.getSubject().getSubject());
        	System.out.println("Número de preguntas: " + subject.getQuestionsNumber());
        	System.out.println("Número de respuestas por pregunta: " + subject.getAnswersxQuestionNumber());
        	System.out.println("Máxima dificultad: " + subject.getMaxDifficulty());
        	System.out.println("Mínima dificultad: " + subject.getMinDifficulty());
        }
        System.out.println("exam.title: " +  exam.getTitle());
        System.out.println("exam.visibility: " +  exam.getVisibility());
        System.out.println("exam.activeReview: " +  exam.isActiveReview());
	}
}
