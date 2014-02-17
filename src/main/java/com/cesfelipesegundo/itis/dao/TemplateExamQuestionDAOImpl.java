package com.cesfelipesegundo.itis.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cesfelipesegundo.itis.dao.api.TemplateExamQuestionDAO;
import com.cesfelipesegundo.itis.model.Query;

import es.itest.engine.test.business.entity.Item;
import es.itest.engine.test.business.entity.ItemResponse;
import es.itest.engine.test.business.entity.MediaElem;

public class TemplateExamQuestionDAOImpl extends SqlMapClientDaoSupport implements TemplateExamQuestionDAO {
	/**
	 * Constructor
	 *
	 */
	public TemplateExamQuestionDAOImpl() {
		super();
	}
	
	public void update(Item question) {
		/*int rows =*/ getSqlMapClientTemplate().update("TemplateExam.updateTemplateExamQuestion", question);		
	}
	
	/** Salva el objeto en la bbdd
	 * Modificado para asignar al objeto la nueva id proporcionada por la bbdd
	 * @param question : Objeto a guardar en la bbdd
	 * @see GroupDAOImpl
	 */
	public void save(Item question) {
		/*Object newKey =*/ /*super.getSqlMapClientTemplate().insert("TemplateExam.addNewTemplateExamQuestion", question);*/
		
		if (question.getId() == null){
			Long newKey = (Long)super.getSqlMapClientTemplate().insert("TemplateExam.addNewTemplateExamQuestion", question);
			question.setId(newKey);
		}
		else update(question);
		
	}

	public void update(Item question, MediaElem mediaElem, boolean isQuestion){
		if(isQuestion){
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("idextrap", mediaElem.getId());
			map.put("preg", question.getId());
			map.put("ruta", mediaElem.getPath());
			map.put("tipo", mediaElem.getType());
			map.put("orden", mediaElem.getOrder());
			map.put("nombre", mediaElem.getName());
			map.put("width", mediaElem.getWidth());
			map.put("height", mediaElem.getHeight());
			map.put("geogebraType", mediaElem.getGeogebraType());
			/*int rows =*/ super.getSqlMapClientTemplate().update("TemplateExam.updateQuestionMedia", map);		
		}else{
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("idextrapcom", mediaElem.getId());
			map.put("preg", question.getId());
			map.put("ruta", mediaElem.getPath());
			map.put("tipo", mediaElem.getType());
			map.put("orden", mediaElem.getOrder());
			map.put("nombre", mediaElem.getName());
			map.put("width", mediaElem.getWidth());
			map.put("height", mediaElem.getHeight());
			map.put("geogebraType", mediaElem.getGeogebraType());
			/*int rows =*/ super.getSqlMapClientTemplate().update("TemplateExam.updateCommentMedia", map);		
		}
	}
		
	public void save(Item question, MediaElem mediaElem, boolean isQuestion){
		if(mediaElem.getId()!=null) this.update(question, mediaElem, isQuestion);
		else {
			if(isQuestion){
				HashMap<String,Object> map = new HashMap<String,Object>();
				map.put("preg", question.getId());
				map.put("ruta", mediaElem.getPath());
				map.put("tipo", mediaElem.getType());
				map.put("orden", mediaElem.getOrder());
				map.put("nombre", mediaElem.getName());
				map.put("width", mediaElem.getWidth());
				map.put("height", mediaElem.getHeight());
				map.put("geogebraType", mediaElem.getGeogebraType());
				Long newKey = (Long)super.getSqlMapClientTemplate().insert("TemplateExam.addNewQuestionMedia", map);
				mediaElem.setId(newKey);
			}else{
				HashMap<String,Object> map = new HashMap<String,Object>();
				map.put("preg", question.getId());
				map.put("ruta", mediaElem.getPath());
				map.put("tipo", mediaElem.getType());
				map.put("orden", mediaElem.getOrder());
				map.put("nombre", mediaElem.getName());
				map.put("width", mediaElem.getWidth());
				map.put("height", mediaElem.getHeight());
				map.put("geogebraType", mediaElem.getGeogebraType());
				Long newKey = (Long)super.getSqlMapClientTemplate().insert("TemplateExam.addNewCommentMedia", map);
				mediaElem.setId(newKey);
			}
		}		
	}
	
	public void delete(Item question, MediaElem mediaElem,boolean isQuestion){
		if(isQuestion){
			/*int rows =*/ super.getSqlMapClientTemplate().delete("TemplateExam.deleteQuestionMedia", mediaElem.getId());
		}else{
			super.getSqlMapClientTemplate().delete("TemplateExam.deleteCommentMedia", mediaElem.getId());
		}
	}

	public List<Item> find(Query query) {
		// Relleno el mapa que hará de parámetro de consulta
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("id", query.getId());
		// Titulo y texto Texto: Requiere un tratamiento especial.
		if(query.getTitle()!=null) map.put("title", "%" + query.getTitle() + "%");
		if(query.getText()!=null) map.put("text", "%" + query.getText() + "%");
		if(query.getTextTheme()!=null) map.put("textTheme", "%" + query.getTextTheme() + "%");
		map.put("subject", query.getSubject());
		map.put("group", query.getGroup());
		map.put("difficulty",query.getDifficulty());
		if (query.getInstitution()!=null) map.put("institution",query.getInstitution());
		map.put("active", query.getActive());
		map.put("userId", query.getUserId());
		map.put("excludeGroup", query.getExcludeGroup());
		map.put("questionType", query.getQuestionType());
		// Orden (requiere un tratamiento especial).
		Query.OrderBy order = query.getOrder();
		if(order!=null) {
			if(order.equals(Query.OrderBy.ID)) map.put("preferredOrder", "preguntas.idpreg ASC");
			else if(order.equals(Query.OrderBy.SUBJECT)) map.put("preferredOrder", "temas.orden ASC");
			else if(order.equals(Query.OrderBy.TITLE)) map.put("preferredOrder", "preguntas.titulo ASC");
			else if(order.equals(Query.OrderBy.TEXT)) map.put("preferredOrder", "preguntas.enunciado ASC");
			else if(order.equals(Query.OrderBy.TITLE_XOR_TEXT)) map.put("preferredOrder", "CONCAT(preguntas.titulo,preguntas.enunciado) ASC");
			else if(order.equals(Query.OrderBy.DIFFICULTY)) map.put("preferredOrder", "preguntas.dificultad ASC");
			else if(order.equals(Query.OrderBy.SCOPE)) map.put("preferredOrder", "preguntas.visibilidad ASC");
			else if(order.equals(Query.OrderBy.GROUP)) map.put("preferredOrder", "asignaturas.nombre ASC, grupos.grupo ASC");
		}		
		map.put("scope",query.getScope());
		// Paginación: 
		// Supongo que firstResult siempre me viene dado.
		// Si maxResultCount <= 0 no se aplica LIMIT.  
		map.put("firstResult", Integer.valueOf(query.getFirstResult()));
		if ((query.getMaxResultCount() != null) && (query.getMaxResultCount()>0)) 
			map.put("maxResultCount", Integer.valueOf(query.getMaxResultCount()));
		
		List<Item> questions = super.getSqlMapClientTemplate().queryForList("TemplateExam.findTemplateExamQuestion", map);
		
		// Obtencion de multimedia:
		for(Item question : questions) {
			// Lista de los elementos multimedia
			List<MediaElem> questionMedia = super.getSqlMapClientTemplate().queryForList("TemplateExam.getQuestionMedia", question.getId());
			question.setMmedia(questionMedia);
			List<MediaElem> commentMedia = super.getSqlMapClientTemplate().queryForList("TemplateExam.getCommentMedia", question.getId());
			question.setMmediaComment(commentMedia);
		}
		
		return questions;
	}

	public void delete(Item question) {
		/*int rows =*/ super.getSqlMapClientTemplate().delete("TemplateExam.deleteTemplateExamQuestion", question.getId());		
	}
	
	public Item getQuestionFromId(Item question){
		// Cojo el question por ID
		question = (Item) super.getSqlMapClientTemplate().queryForObject("TemplateExam.getTemplateExamQuestionById", question.getId());
		
		// Lista de los elementos multimedia
		this.fillMediaElem(question);
		
		// Lista de respuestas a esta pregunta
		this.fillAnswers(question);
		
		return question;		
	}

	public void fillMediaElem(Item question) {
		// Lista de los elementos multimedia
		List<MediaElem> questionMedia = super.getSqlMapClientTemplate().queryForList("TemplateExam.getQuestionMedia", question.getId());
		question.setMmedia(questionMedia);	
		List<MediaElem> commentMedia = super.getSqlMapClientTemplate().queryForList("TemplateExam.getCommentMedia", question.getId());
		question.setMmediaComment(commentMedia);	
	}

	public void updateUsedInExam(Item question) {
		/*int rows =*/ super.getSqlMapClientTemplate().update("TemplateExam.updateTemplateExamQuestionUsedInExam", question);		
	}

	public void fillAnswers(Item templateQuestion) {
		List<ItemResponse> answers = super.getSqlMapClientTemplate().queryForList("TemplateExam.getTemplateExamAnswer", templateQuestion.getId());
		templateQuestion.setAnswers(answers);

		// Y finalmente a cada respuesta hay que añadirle
		for(ItemResponse answer : answers) {
			// Lista de elementos multimedia
			List<MediaElem> answerMedia = super.getSqlMapClientTemplate().queryForList("TemplateExam.getAnswerMedia", answer.getId());
			answer.setMmedia(answerMedia);
		}		
	}

	public void updateQuestionNotUsedInExam(Long idgrp) {
		super.getSqlMapClientTemplate().update("TemplateExam.updateQuestionNotUsedInExam",idgrp);
		super.getSqlMapClientTemplate().update("TemplateExam.updateAnswersNotUsedInExam",idgrp);
	}
	
}
