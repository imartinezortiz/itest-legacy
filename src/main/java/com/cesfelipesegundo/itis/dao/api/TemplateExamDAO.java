package com.cesfelipesegundo.itis.dao.api;

import es.itest.engine.test.business.entity.Test;

public interface TemplateExamDAO extends DAO {
		
	/**
	 * Devuelve toda la información asociada a un examen, es decir, los temas
	 * que contiene, las preguntas por tema, y las respuestas por pregunta
	 * @param id del examen
	 * @return Plantilla de examen con toda la información asociada a un posible examen
	 */
		
	Test getTest(Long id);

	
}
