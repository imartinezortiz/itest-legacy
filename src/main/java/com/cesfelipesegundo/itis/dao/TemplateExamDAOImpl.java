package com.cesfelipesegundo.itis.dao;


import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cesfelipesegundo.itis.dao.api.TemplateExamDAO;
import com.cesfelipesegundo.itis.model.MediaElem;
import com.cesfelipesegundo.itis.model.TemplateExam;
import com.cesfelipesegundo.itis.model.TemplateExamAnswer;
import com.cesfelipesegundo.itis.model.TemplateExamQuestion;
import com.cesfelipesegundo.itis.model.TemplateExamSubject;

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
	private void fillTemplateExam(TemplateExam exam) {
		// Cojo la lista de temas ...
		List<TemplateExamSubject> subjects = super.getSqlMapClientTemplate().queryForList("TemplateExam.getTemplateExamSubject", exam.getId());
		// ... y se la asigno al examen
		exam.setSubjects(subjects);
		
		// A cada subject hay que añadirle:
		for(TemplateExamSubject subject : subjects) {
			
			// Lista de preguntas de del tema correspondiente
			List<TemplateExamQuestion> questions = super.getSqlMapClientTemplate().queryForList("TemplateExam.getTemplateExamQuestion", subject);
			subject.setQuestions(questions);
			
			// Además, a cada question hay que añadirle:
			for(TemplateExamQuestion question : questions) {
				
				// Lista de los elementos multimedia
				List<MediaElem> questionMedia = super.getSqlMapClientTemplate().queryForList("TemplateExam.getQuestionMedia", question.getId());
				question.setMmedia(questionMedia);
				
				// Lista de respuestas a esta pregunta
				List<TemplateExamAnswer> answers = super.getSqlMapClientTemplate().queryForList("TemplateExam.getTemplateExamAnswer", question.getId());
				question.setAnswers(answers);
				
				// Y finalmente a cada respuesta hay que añadirle
				for(TemplateExamAnswer answer : answers) {
					// Lista de elementos multimedia
					List<MediaElem> answerMedia = super.getSqlMapClientTemplate().queryForList("TemplateExam.getAnswerMedia", answer.getId());
					answer.setMmedia(answerMedia);
				}
			}
		}		
	}

	public TemplateExam getTemplateExam(Long id) {
		// En primer lugar cojo la información básica del examen
		// Sin la lista de temas.
		TemplateExam exam = (TemplateExam)super.getSqlMapClientTemplate().queryForObject("TemplateExam.getTemplateExam", id);
		// Relleno toda la información extra
		if(exam!=null) this.fillTemplateExam(exam);
		return exam;
	}

	}
