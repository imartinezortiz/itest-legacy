package com.cesfelipesegundo.itis.test_DEPRECATED;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.cesfelipesegundo.itis.dao.AnswerExamDAOImpl;
import com.cesfelipesegundo.itis.dao.api.AnswerExamDAO;
import com.cesfelipesegundo.itis.model.TemplateExamAnswer;

public class AnswerExamDAOTest extends AbstractDependencyInjectionSpringContextTests {
	
	protected String[] getConfigLocations() {
		return new String [] {"classpath:com/cesfelipesegundo/itis/dao/config/spring-dao.xml"};    
	}
	
	public void testAnswers(){
		System.out.println("Respuestadas dadas por un alumno:");
		AnswerExamDAO answerExamDAO = (AnswerExamDAOImpl)this.applicationContext.getBean("answerExamDAO");
		answerExamDAO.addNewExamAnswer(Long.valueOf(1), Long.valueOf(1), Long.valueOf(1101), Long.valueOf(1000), System.currentTimeMillis());
		answerExamDAO.updateExamAnswer(Long.valueOf(1), Long.valueOf(1), Long.valueOf(1101), Long.valueOf(1000), System.currentTimeMillis(), Boolean.valueOf(true));
		answerExamDAO.updateExamAnswerGrade(Long.valueOf(1), Long.valueOf(1), Long.valueOf(1101), Long.valueOf(1000), Double.valueOf(4));
        System.out.println("Fin de test..." );
	  }

	public void testUpdateAnswerUsedInExam() {
		System.out.println("testUpdateAnswerUsedInExam begins:");
		AnswerExamDAO answerExamDAO = (AnswerExamDAOImpl)this.applicationContext.getBean("answerExamDAO");
		TemplateExamAnswer templateAnswer = new TemplateExamAnswer();
		templateAnswer.setId(Long.valueOf(1000));
		templateAnswer.setUsedInExam(true);
		answerExamDAO.updateUsedInExam(templateAnswer);
        System.out.println("Fin de test..." );
	  }
}
