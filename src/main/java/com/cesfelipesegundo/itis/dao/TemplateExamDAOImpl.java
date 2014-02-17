package com.cesfelipesegundo.itis.dao;


import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cesfelipesegundo.itis.dao.api.TemplateExamDAO;

import es.itest.engine.test.business.entity.Item;
import es.itest.engine.test.business.entity.ItemResponse;
import es.itest.engine.test.business.entity.MediaElem;
import es.itest.engine.test.business.entity.Test;
import es.itest.engine.test.business.entity.TestSubject;

public class TemplateExamDAOImpl extends SqlMapClientDaoSupport implements TemplateExamDAO {
	/**
	 * Constructor
	 *
	 */
	public TemplateExamDAOImpl() {
		super();
	}
	
	/**
	 * Function that fills information related to the TemplateExam:
	 * subjects, questions per subject and answers per question.
	 * @param exam The exam to be filled.
	 */
	private void fillTemplateExam(Test exam) {
		// Cojo la lista de temas ...
		List<TestSubject> subjects = super.getSqlMapClientTemplate().queryForList("TemplateExam.getTemplateExamSubject", exam.getId());
		// ... y se la asigno al examen
		exam.setSubjects(subjects);
		
		// A cada subject hay que añadirle:
		for(TestSubject subject : subjects) {
			
			// Lista de preguntas de del tema correspondiente
			List<Item> questions = super.getSqlMapClientTemplate().queryForList("TemplateExam.getTemplateExamQuestion", subject);
			subject.setQuestions(questions);
			
			// Además, a cada question hay que añadirle:
			for(Item question : questions) {
				
				// Lista de los elementos multimedia
				List<MediaElem> questionMedia = super.getSqlMapClientTemplate().queryForList("TemplateExam.getQuestionMedia", question.getId());
				question.setMmedia(questionMedia);
				
				// Lista de respuestas a esta pregunta
				List<ItemResponse> answers = super.getSqlMapClientTemplate().queryForList("TemplateExam.getTemplateExamAnswer", question.getId());
				question.setAnswers(answers);
				
				// Y finalmente a cada respuesta hay que añadirle
				for(ItemResponse answer : answers) {
					// Lista de elementos multimedia
					List<MediaElem> answerMedia = super.getSqlMapClientTemplate().queryForList("TemplateExam.getAnswerMedia", answer.getId());
					answer.setMmedia(answerMedia);
				}
			}
		}		
	}

	public Test getTest(Long id) {
		// En primer lugar cojo la información básica del examen
		// Sin la lista de temas.
		Test exam = (Test)super.getSqlMapClientTemplate().queryForObject("TemplateExam.getTemplateExam", id);
		// Relleno toda la información extra
		if(exam!=null) this.fillTemplateExam(exam);
		return exam;
	}

	}
