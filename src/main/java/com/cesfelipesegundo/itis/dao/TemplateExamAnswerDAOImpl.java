package com.cesfelipesegundo.itis.dao;


import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cesfelipesegundo.itis.dao.api.TemplateExamAnswerDAO;
import com.cesfelipesegundo.itis.model.MediaElem;
import com.cesfelipesegundo.itis.model.TemplateExamAnswer;

public class TemplateExamAnswerDAOImpl extends SqlMapClientDaoSupport implements TemplateExamAnswerDAO {
	private static final Log log = LogFactory.getLog(TemplateExamAnswerDAOImpl.class);
	/**
	 * Constructor
	 *
	 */
	public TemplateExamAnswerDAOImpl() {
		super();
	}
	
	public void update(TemplateExamAnswer answer) {
		/*int rows =*/ getSqlMapClientTemplate().update("TemplateExam.updateTemplateExamAnswer", answer);		
	}
	
	public void save(TemplateExamAnswer answer) {
		/*Object newKey = super.getSqlMapClientTemplate().insert("TemplateExam.addNewTemplateExamAnswer", answer);*/
		if (answer.getId()==null) {
			try{
				Long newKey = (Long)super.getSqlMapClientTemplate().insert("TemplateExam.addNewTemplateExamAnswer", answer);
				answer.setId(newKey);
			}catch(Exception e){
				if(answer!=null){
					/*
					 * Construyendo la consulta
					 * */
					String sql ="INSERT INTO respuestas (texto, preg, solucion, valor, activa, used_in_exam_question) VALUES";
					String text = answer.getText();
					if(text==null)
						text="null";
					String questionId = "null";
					if(answer.getQuestion()!=null && answer.getQuestion().getId()!=null)
						questionId = ""+answer.getQuestion().getId();
					sql+="("+text+", "+questionId+", "+answer.getSolution()+", "+answer.getValue()+", "+answer.getActive()+", "+answer.getUsedInExam()+")";
					
					/*
					 * FIN
					 * */
					log.error("TemplateExamAnswerDAOImpl.save, Query: "+sql);
				}else{
					log.error("El parametro TemplateExamAnswer recibido es null");
				}
			}
		}
		else update(answer);
	}
	
	public void delete(TemplateExamAnswer answer) {
		/*int rows =*/ super.getSqlMapClientTemplate().delete("TemplateExam.deleteTemplateExamAnswer", answer.getId());
	}
	
	public void update(TemplateExamAnswer answer, MediaElem mediaElem){
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("idextrar", mediaElem.getId());
		map.put("resp", answer.getId());
		map.put("ruta", mediaElem.getPath());
		map.put("tipo", mediaElem.getType());
		map.put("orden", mediaElem.getOrder());
		map.put("nombre", mediaElem.getName());
		map.put("width", mediaElem.getWidth());
		map.put("height", mediaElem.getHeight());
		/*int rows =*/ super.getSqlMapClientTemplate().update("TemplateExam.updateAnswerMedia", map);
	}
		
	public void save(TemplateExamAnswer answer, MediaElem mediaElem){
		if(mediaElem.getId()!=null) this.update(answer, mediaElem);
		else {
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("resp", answer.getId());
			map.put("ruta", mediaElem.getPath());
			map.put("tipo", mediaElem.getType());
			map.put("orden", mediaElem.getOrder());
			map.put("nombre", mediaElem.getName());
			map.put("width", mediaElem.getWidth());
			map.put("height", mediaElem.getHeight());
			Long newKey = (Long)super.getSqlMapClientTemplate().insert("TemplateExam.addNewAnswerMedia", map);
			mediaElem.setId(newKey);
		}
	}
	
	
	public void delete(TemplateExamAnswer answer, MediaElem mediaElem){
		/*int rows =*/ super.getSqlMapClientTemplate().delete("TemplateExam.deleteAnswerMedia", mediaElem.getId());
	}
	
}
