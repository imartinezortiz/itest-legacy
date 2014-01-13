/**
 * 
 */
package com.cesfelipesegundo.itis.test_DEPRECATED;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.cesfelipesegundo.itis.dao.TemplateExamAnswerDAOImpl;
import com.cesfelipesegundo.itis.dao.api.TemplateExamAnswerDAO;
import com.cesfelipesegundo.itis.model.MediaElem;
import com.cesfelipesegundo.itis.model.TemplateExamAnswer;
import com.cesfelipesegundo.itis.model.TemplateExamQuestion;

/**
 * @author jlrisco
 *
 */
public class TemplateExamAnswerDAOImplTest extends AbstractDependencyInjectionSpringContextTests {

	protected String[] getConfigLocations() {
		return new String [] {"classpath:com/cesfelipesegundo/itis/dao/config/spring-dao.xml"};    
	}

	/**
	 * Test method for {@link com.cesfelipesegundo.itis.dao.TemplateExamAnswerDAOImpl#update(com.cesfelipesegundo.itis.model.TemplateExamAnswer)}.
	 */
	public final void testUpdateAnswer() {
		System.out.println("testUpdateAnswer");
        TemplateExamAnswerDAO templateExamAnswerDAO = (TemplateExamAnswerDAOImpl)this.applicationContext.getBean("templateExamAnswerDAO");
        TemplateExamAnswer answer = new TemplateExamAnswer();
        answer.setId(Long.valueOf(1346));
        answer.setText("Borrar2");
        answer.setActive(0);
        templateExamAnswerDAO.update(answer);		
	}

	/**
	 * Test method for {@link com.cesfelipesegundo.itis.dao.TemplateExamAnswerDAOImpl#save(com.cesfelipesegundo.itis.model.TemplateExamAnswer)}.
	 */
	public final void testSaveAnswer() {
		System.out.println("testSaveAnswer");
        TemplateExamAnswerDAO templateExamAnswerDAO = (TemplateExamAnswerDAOImpl)this.applicationContext.getBean("templateExamAnswerDAO");
        TemplateExamQuestion question = new TemplateExamQuestion();
        question.setId(Long.valueOf(1101));
        TemplateExamAnswer answer = new TemplateExamAnswer();
        answer.setText("Borrar");
        answer.setQuestion(question);
        answer.setSolution(1);
        answer.setValue(5);
        answer.setActive(1);
        templateExamAnswerDAO.save(answer);
	}

	public final void testDeleteAnswer() {
		System.out.println("testDeleteAnswer");
        TemplateExamAnswerDAO templateExamAnswerDAO = (TemplateExamAnswerDAOImpl)this.applicationContext.getBean("templateExamAnswerDAO");
        
        TemplateExamAnswer answer = new TemplateExamAnswer();
        answer.setId(Long.valueOf(1));
        templateExamAnswerDAO.delete(answer);
	}

	public final void testNewAnswerMedia() {
		System.out.println("testNewAnswerMedia");
        TemplateExamAnswerDAO templateExamAnswerDAO = (TemplateExamAnswerDAOImpl)this.applicationContext.getBean("templateExamAnswerDAO");
        
        MediaElem mediaElem = new MediaElem();
        mediaElem.setName("nombre");
        mediaElem.setOrder(1);
        mediaElem.setPath("path");
        mediaElem.setType(1);
        TemplateExamAnswer answer = new TemplateExamAnswer();
        answer.setId(Long.valueOf(1));
        templateExamAnswerDAO.save(answer,mediaElem);
	}

	public final void testUpdateAnswerMedia() {
		System.out.println("testUpdateAnswerMedia");
        TemplateExamAnswerDAO templateExamAnswerDAO = (TemplateExamAnswerDAOImpl)this.applicationContext.getBean("templateExamAnswerDAO");
        
        MediaElem mediaElem = new MediaElem();
        mediaElem.setId(Long.valueOf(1));
        mediaElem.setName("nombreUpdated");
        TemplateExamAnswer answer = new TemplateExamAnswer();
        answer.setId(Long.valueOf(1));
        templateExamAnswerDAO.save(answer,mediaElem);
	}

	public final void testDeleteAnswerMedia() {
		System.out.println("testDeleteAnswerMedia");
        TemplateExamAnswerDAO templateExamAnswerDAO = (TemplateExamAnswerDAOImpl)this.applicationContext.getBean("templateExamAnswerDAO");
        
        MediaElem mediaElem = new MediaElem();
        mediaElem.setId(Long.valueOf(1));
        TemplateExamAnswer answer = new TemplateExamAnswer();
        answer.setId(Long.valueOf(1));
        templateExamAnswerDAO.delete(answer,mediaElem);
	}
}
