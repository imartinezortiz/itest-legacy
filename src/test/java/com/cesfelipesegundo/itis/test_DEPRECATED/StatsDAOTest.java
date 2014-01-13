package com.cesfelipesegundo.itis.test_DEPRECATED;

import java.util.List;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.cesfelipesegundo.itis.dao.StatsDAOImpl;
import com.cesfelipesegundo.itis.dao.api.StatsDAO;
import com.cesfelipesegundo.itis.model.AnsweredQuestionData;
import com.cesfelipesegundo.itis.model.CalifData;
import com.cesfelipesegundo.itis.model.Group;

public class StatsDAOTest extends AbstractDependencyInjectionSpringContextTests {
	
	protected String[] getConfigLocations() {
		return new String [] {"classpath:com/cesfelipesegundo/itis/dao/config/spring-dao.xml"};    
	}
	
	public void testGetCalifsData() {
		System.out.println("testGetCalifsData begins:");
        StatsDAO statsDAO = (StatsDAOImpl)this.applicationContext.getBean("statsDAO");
        Group group = new Group();
        group.setId(Long.valueOf(1));
        List<CalifData> results = statsDAO.getCalifsData(group);
        for(CalifData result : results) {
        	System.out.println("examId: " + result.getIdexam());
        	System.out.println("exam title: " + result.getTitle());
        	System.out.println("exam duration: " + result.getDuration());
        	System.out.println("exam maxGrade: " + result.getMaxGrade());
        	System.out.println("student name: " + result.getName());
        	System.out.println("student id: " + result.getIdalu());
        	System.out.println("student surname: " + result.getSurname());
        	System.out.println("student username: " + result.getUsername());
        	System.out.println("student grade: " + result.getGrade());
        	System.out.println("time spent to solve the exam:" + result.getTime());
        	System.out.println("======================================");
        }
        System.out.println("Test end." );
	}
	
	public void testGetAnsweredQuestionsData() {
		System.out.println("testGetAnsweredQuestionsData begins:");
        StatsDAO statsDAO = (StatsDAOImpl)this.applicationContext.getBean("statsDAO");
        Group group = new Group();
        for(int i=0; i<50; i++) {
        	group.setId(Long.valueOf(i));
        	List<AnsweredQuestionData> results = statsDAO.getAnsweredQuestionsData(group);
        	System.out.println("Group " + i + ". Results size: " + results.size());
        	/*for(AnsweredQuestionData result : results) {
        		System.out.println("Exam ID: " + result.getIdexam());
        		System.out.println("Student ID: " + result.getIdalu());
        		System.out.println("Question ID: " + result.getId());
        		System.out.println("Question: " + result.getText());
        		System.out.println("Subject: " + result.getSubject());
        		System.out.println("Answered: " + result.isAnswered());
        		System.out.println("Sum of answers grade/exam/student: " + result.getAnswersGradeSum());
        		System.out.println("Max grade for this question/exam: " + result.getMaxGradePerQuestion());
        		System.out.println("AnsweredWithSuccess: " + result.isAnsweredWithSuccess());
        		System.out.println("======================================");
        	}*/
        }
        System.out.println("Test end." );
	}

}
