/**
 * 
 */
package com.cesfelipesegundo.itis.test_DEPRECATED;

import java.util.List;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.cesfelipesegundo.itis.dao.TemplateExamQuestionDAOImpl;
import com.cesfelipesegundo.itis.dao.api.TemplateExamQuestionDAO;
import com.cesfelipesegundo.itis.model.Group;
import com.cesfelipesegundo.itis.model.MediaElem;
import com.cesfelipesegundo.itis.model.Query;
import com.cesfelipesegundo.itis.model.Subject;
import com.cesfelipesegundo.itis.model.TemplateExamAnswer;
import com.cesfelipesegundo.itis.model.TemplateExamQuestion;

/**
 * @author jlrisco
 *
 */
public class TemplateExamQuestionDAOImplTest extends AbstractDependencyInjectionSpringContextTests {

	protected String[] getConfigLocations() {
		return new String [] {"classpath:com/cesfelipesegundo/itis/dao/config/spring-dao.xml"};    
	}

	/**
	 * Test method for {@link com.cesfelipesegundo.itis.dao.TemplateExamQuestionDAOImpl#update(com.cesfelipesegundo.itis.model.TemplateExamQuestion)}.
	 */
	public final void testUpdate() {
		System.out.println("testUpdate");
        TemplateExamQuestionDAO templateExamQuestionDAO = (TemplateExamQuestionDAOImpl)this.applicationContext.getBean("templateExamQuestionDAO");
        TemplateExamQuestion question = new TemplateExamQuestion();
        question.setId(Long.valueOf(1));
        //question.setUsedInExam(true);
        question.setText("Borrar3");
        templateExamQuestionDAO.update(question);
        //templateExamQuestionDAO.updateUsedInExam(question);
	}

	/**
	 * Test method for {@link com.cesfelipesegundo.itis.dao.TemplateExamQuestionDAOImpl#add(com.cesfelipesegundo.itis.model.TemplateExamQuestion)}.
	 */
	public final void testAdd() {
		System.out.println("testAdd");
        TemplateExamQuestionDAO templateExamQuestionDAO = (TemplateExamQuestionDAOImpl)this.applicationContext.getBean("templateExamQuestionDAO");
        TemplateExamQuestion question = new TemplateExamQuestion();
        question.setText("Borrar");
        question.setDifficulty(0);
        Subject subject = new Subject();
        subject.setId(Long.valueOf(1));
        question.setSubject(subject);
        Group group = new Group();
        group.setId(Long.valueOf(1));
        question.setGroup(group);
        question.setVisibility(0);
        question.setComment("");
        question.setActive(1);
        question.setNumCorrectAnswers(1);
        templateExamQuestionDAO.save(question);

	}
	
	public final void testNewQuestionMedia() {
		System.out.println("testNewQuestionMedia");
		TemplateExamQuestionDAO templateExamQuestionDAO = (TemplateExamQuestionDAOImpl)this.applicationContext.getBean("templateExamQuestionDAO");
        
        MediaElem mediaElem = new MediaElem();
        mediaElem.setName("nombre");
        mediaElem.setOrder(1);
        mediaElem.setPath("path");
        mediaElem.setType(1);
        TemplateExamQuestion question = new TemplateExamQuestion();
        question.setId(Long.valueOf(1));
        templateExamQuestionDAO.save(question,mediaElem,true);
	}

	public final void testUpdateQuestionMedia() {
		System.out.println("testUpdateQuestionMedia");
		TemplateExamQuestionDAO templateExamQuestionDAO = (TemplateExamQuestionDAOImpl)this.applicationContext.getBean("templateExamQuestionDAO");
        
        MediaElem mediaElem = new MediaElem();
        mediaElem.setId(Long.valueOf(1));
        mediaElem.setName("nombreUpdated");
        TemplateExamQuestion question = new TemplateExamQuestion();
        question.setId(Long.valueOf(1));
        templateExamQuestionDAO.save(question,mediaElem,true);
	}

	public final void testDeleteQuestionMedia() {
		System.out.println("testDeleteQuestionMedia");
		TemplateExamQuestionDAO templateExamQuestionDAO = (TemplateExamQuestionDAOImpl)this.applicationContext.getBean("templateExamQuestionDAO");
        
        MediaElem mediaElem = new MediaElem();
        mediaElem.setId(Long.valueOf(1));
        TemplateExamQuestion question = new TemplateExamQuestion();
        question.setId(Long.valueOf(1));
        templateExamQuestionDAO.delete(question,mediaElem,true);
	}

	public final void testDeleteQuestion() {
		System.out.println("testDeleteQuestion");
		TemplateExamQuestionDAO templateExamQuestionDAO = (TemplateExamQuestionDAOImpl)this.applicationContext.getBean("templateExamQuestionDAO");
        
        TemplateExamQuestion question = new TemplateExamQuestion();
        question.setId(Long.valueOf(1));
        templateExamQuestionDAO.delete(question);
	}

	public final void testFindQuestions() {
		System.out.println("testFindQuestions");
		TemplateExamQuestionDAO templateExamQuestionDAO = (TemplateExamQuestionDAOImpl)this.applicationContext.getBean("templateExamQuestionDAO");
        
		Query query = new Query();
		//query.setText("Enun");
		//query.setDifficulty(Integer.valueOf(1).shortValue());
		query.setOrder(Query.OrderBy.SCOPE);
		//query.setFirstResult(1);
		//query.setMaxResultCount(2);
		//query.setActive(Boolean.FALSE);
		//query.setDifficulty(Integer.valueOf(1).shortValue());
		List<TemplateExamQuestion> questions = templateExamQuestionDAO.find(query);
		for(TemplateExamQuestion question : questions) {
			System.out.println("=========================================================================");
			System.out.println("Id: " + question.getId());
			System.out.println("Tema.Orden: " + question.getSubject().getOrder());
			System.out.println("Text:" + question.getText());
			System.out.println("Dificultad: " + question.getDifficulty());
			System.out.println("Visibilidad: " + question.getVisibility());
			/*System.out.println("Comentario: " + question.getComment());
			System.out.println("GroupId: " + question.getGroup().getId());
			System.out.println("RespCorrectas: " + question.getNumCorrectAnswers());
			System.out.println("SubjectId: " + question.getSubject().getId());
			*/
			
			// Relleno los elementos multimedia
			/*templateExamQuestionDAO.fillMediaElem(question);
    		List<MediaElem> questionMedias = question.getMmedia();
    		for(MediaElem questionMedia : questionMedias) {
    			System.out.println("Id del multimedia: " + questionMedia.getId());
    			System.out.println("Path: " + questionMedia.getPath());
    			System.out.println("Tipo: " + questionMedia.getType());
    			System.out.println("Orden: " + questionMedia.getOrder());
    			System.out.println("Nombre: " + questionMedia.getName());
    		}*/
		}
	}
	
	public final void testGetQuestionFromId() {
		System.out.println("testGetQuestionFromId");
		TemplateExamQuestionDAO templateExamQuestionDAO = (TemplateExamQuestionDAOImpl)this.applicationContext.getBean("templateExamQuestionDAO");
		TemplateExamQuestion question = new TemplateExamQuestion();
		question.setId(Long.valueOf(1));
		question = templateExamQuestionDAO.getQuestionFromId(question);
		System.out.println("=========================================================================");
		System.out.println("Activa: " + question.getActive());
		System.out.println("Comentario: " + question.getComment());
		System.out.println("Dificultad: " + question.getDifficulty());
		System.out.println("GroupId: " + question.getGroup().getId());
		System.out.println("RespCorrectas: " + question.getNumCorrectAnswers());
		System.out.println("SubjectId: " + question.getSubject().getId());
		System.out.println("Text: " + question.getText());
		System.out.println("Visibilidad: " + question.getVisibility());
		
		// Elementos multimedia
   		List<MediaElem> questionMedias = question.getMmedia();
   		for(MediaElem questionMedia : questionMedias) {
   			System.out.println("Id del multimedia: " + questionMedia.getId());
   			System.out.println("Path: " + questionMedia.getPath());
   			System.out.println("Tipo: " + questionMedia.getType());
   			System.out.println("Orden: " + questionMedia.getOrder());
   			System.out.println("Nombre: " + questionMedia.getName());
   		}  
   		
   		// Respuestas
		List<TemplateExamAnswer> answers = question.getAnswers();
		for(TemplateExamAnswer answer : answers) {
			System.out.println("----------------------------------------------------------------------");
			System.out.println("Id de la respuesta: " + answer.getId());
			System.out.println("Texto: " + answer.getText());
			System.out.println("Marcada: " + answer.getMarked());
			System.out.println("Activa: " + answer.getActive());
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
}
