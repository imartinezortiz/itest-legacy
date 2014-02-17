package es.itest.engine.test.business.entity;

import es.itest.engine.course.business.entity.Group;

/**
 * Clase para modelar los datos básicos de un examen. Utilizado para mostrar listas de exámenes.
 * @author chema
 *
 */
public class TestDetails {

	private Long id;			// Id del examen
	private String title;
	private Group group;		// Grupo al que pertenece el examen
	
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	

}
