package com.cesfelipesegundo.itis.dao;


import java.util.Date;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cesfelipesegundo.itis.dao.api.AnswerExamDAO;
import com.cesfelipesegundo.itis.model.AnswerExam;

import es.itest.engine.test.business.entity.ItemResponse;
import es.itest.engine.test.business.entity.ItemSessionResponse;

public class AnswerExamDAOImpl extends SqlMapClientDaoSupport implements AnswerExamDAO {
	private static final Log log = LogFactory.getLog(AnswerExamDAOImpl.class);
	/**
	 * Constructor
	 *
	 */
	public AnswerExamDAOImpl() {
		super();
	}
	
	/**
	 * Adds a new answer of an exam to the data base.
	 * @param answer The answer.
	 * @return The new key created.
	 */
	private Long addNewAnswerExam(AnswerExam answer) {
		Object newKey = null;
		if(getAnswerExam(answer)==null){
			try{
				newKey = super.getSqlMapClientTemplate().insert("AnswerExam.addNewAnswerExam", answer);
			}catch(Exception e){
				e.printStackTrace();
			}
			return (Long)newKey;
		}else{
			log.info("No se ha podido insertar la respuesta con id "+answer.getIdAnswer()+" para la pregunta con id"+answer.getIdQuestion()+" para el alumno: "+answer.getIdStudent()+" en el examen "+answer.getIdExam()+" en log_exams fill porque ya est� insertada");
			return new Long(-1);
		}
	}
	
	private void lanzarExcepcion() throws Exception{
		throw new Exception();
	}
	
	/**
	 * Updates an answer of the database.
	 * The answer is updated accord to the foreigned keys.
	 * @param answer Answer to update.
	 * @return Number of rows affected.
	 */
	private int updateAnswerExam(AnswerExam answer) {
		AnswerExam aux2 = getAnswerExam(answer);
		try{
			int rows = getSqlMapClientTemplate().update("AnswerExam.updateAnswerExam", answer);
			return rows;
		}catch(Exception e){
			if(answer!=null && answer.getIdAnswer()!=null){
				/*
				 * Construyendo la consulta
				 * */
				String sql ="";
				sql+="UPDATE log_exams ";
				if(answer.getMarked()!=null || answer.getGrade()!=null || answer.getAnswerTime()!=null){
					sql+="SET ";
					if(answer.getMarked()!=null){
						aux2.setMarked(answer.getMarked());
						sql+=" marcada = "+answer.getMarked();
					}
					if(answer.getGrade()!=null){
						aux2.setGrade(answer.getGrade());
						sql+=" puntos = "+answer.getGrade();
					}
					if(answer.getAnswerTime()!=null){
						aux2.setAnswerTime(answer.getAnswerTime());
						sql+=" hora_resp = "+answer.getAnswerTime();
					}
				}
				sql+="WHERE ";
				if(answer.getIdExam()!=null)
					sql+="exam = "+answer.getIdExam();
				if(answer.getIdStudent()!=null)
					sql+="alu = "+answer.getIdStudent();
				if(answer.getIdQuestion()!=null)
					sql+="preg = "+answer.getIdQuestion();
				if(answer.getIdAnswer()!=null)
					sql+="resp = "+answer.getIdAnswer();
				
				/*
				 * FIN
				 * */
				log.error("AnswerExamDAOImpl.updateAnswerExam, Query: "+sql);
				AnswerExam aux = (AnswerExam) getSqlMapClientTemplate().queryForObject("AnswerExam.selectAnswerExam",answer);
				if(aux!=null && aux.iguales(aux2)){
					return 1;
				}
			}else{
				log.error("El parametro AnswerExam recibido es null");
			}
			return 0;
		}
	}

	
	public int updateExamAnswer(Long idexam, Long iduser, Long idquestion, ItemSessionResponse eAnswer) {
		AnswerExam answer = new AnswerExam();
		answer.setAnswerTime(null);
		if(eAnswer.getMarked()){
			answer.setMarked(AnswerExam.MARKED);
		}else{
			answer.setMarked(AnswerExam.NOT_MARKED);
		}
		answer.setIdAnswer(eAnswer.getId());
		answer.setIdExam(idexam);
		answer.setIdQuestion(idquestion);
		answer.setIdStudent(iduser);
		int rows = this.updateAnswerExam(answer);
		return rows;
	}
	
	public void addNewExamAnswer(Long idExam, Long idLearner, Long idQuestion, Long idAnswer, Long startingDate) {
		AnswerExam answer = new AnswerExam();
		answer.setIdAnswer(idAnswer);
		answer.setIdExam(idExam);
		answer.setIdQuestion(idQuestion);
		answer.setIdStudent(idLearner);
		answer.setMarked(AnswerExam.NOT_MARKED);
		answer.setGrade(Double.valueOf(0));
		answer.setAnswerTime(new Date(startingDate));
		this.addNewAnswerExam(answer);
	}

	public int updateExamAnswer(Long idexam, Long iduser, Long idquestion, Long idanswer, Long answerTime, Boolean marked) {
		AnswerExam answer = new AnswerExam();
		if(answerTime != null){
			answer.setAnswerTime(new Date(answerTime));
		}else{
			answer.setAnswerTime(null);
		}
		if(marked == Boolean.TRUE) answer.setMarked(AnswerExam.MARKED);
		else answer.setMarked(AnswerExam.NOT_MARKED);
		answer.setIdAnswer(idanswer);
		answer.setIdExam(idexam);
		answer.setIdQuestion(idquestion);
		answer.setIdStudent(iduser);
		int rows = this.updateAnswerExam(answer);
		return rows;
	}

	public void updateExamAnswerGrade(Long idexam, Long iduser, Long idquestion, Long idanswer, Double grade) {
		AnswerExam answer = new AnswerExam();
		answer.setGrade(grade);
		answer.setIdAnswer(idanswer);
		answer.setIdExam(idexam);
		answer.setIdQuestion(idquestion);
		answer.setIdStudent(iduser);
		this.updateAnswerExam(answer);
	}

	public void updateUsedInExam(ItemResponse templateAnswer) {
		int rows = getSqlMapClientTemplate().update("TemplateExam.updateTemplateExamAnswerUsedInExam", templateAnswer);
	}

	public int updateConfidenceLevel(long examId, long userId,
			long questionId, boolean checked, int questionType) {
		HashMap<String,Long> parameters = new HashMap<String,Long>();
		parameters.put("userId", userId);
		parameters.put("examId", examId);
		parameters.put("questionId", questionId);
		if(checked)
			parameters.put("checked", new Long(1));
		else
			parameters.put("checked", new Long(0));
		if(questionType == 0)
			return getSqlMapClientTemplate().update("AnswerExam.updateConfidenceLevel", parameters);
		else
			return getSqlMapClientTemplate().update("AnswerExam.updateConfidenceLevelFillQuestion",parameters);
	}

	public void addNewExamFillAnswer(Long idExam, Long idUser, Long idQuestion,
			Long idAnswer, String textAnswer, long currentTimeMillis) {
		
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("idExam", idExam);
		parameters.put("idStudent", idUser);
		parameters.put("idQuestion", idQuestion);
		parameters.put("grade", Double.valueOf(0));
		parameters.put("textAnswer", textAnswer);
		parameters.put("answerTime", new Date(currentTimeMillis));
		this.getSqlMapClientTemplate().insert("AnswerExam.addNewExamFillAnswer",parameters);
	}

	public int updateExamFillAnswer(Long idExam, Long idUser, Long idQuestion,
			Long idAnswer, String textAnswer, Long grade) {
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("idExam", idExam);
		parameters.put("idStudent", idUser);
		parameters.put("idQuestion", idQuestion);
		if(grade == null)
			parameters.put("grade", new Long(0));
		else
			parameters.put("grade", grade);
		parameters.put("textAnswer", textAnswer);
		return getSqlMapClientTemplate().update("AnswerExam.updateExamFillAnswer",parameters);
	}
	
	private AnswerExam getAnswerExam(AnswerExam answerExam){
		AnswerExam aux = (AnswerExam) getSqlMapClientTemplate().queryForObject("AnswerExam.selectAnswerExam",answerExam);
		return aux;
	}
}
