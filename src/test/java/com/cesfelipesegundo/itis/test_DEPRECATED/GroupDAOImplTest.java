/**
 * 
 */
package com.cesfelipesegundo.itis.test_DEPRECATED;

import java.util.List;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.cesfelipesegundo.itis.dao.GroupDAOImpl;
import com.cesfelipesegundo.itis.dao.api.GroupDAO;
import com.cesfelipesegundo.itis.model.Course;
import com.cesfelipesegundo.itis.model.Group;
import com.cesfelipesegundo.itis.model.Institution;
import com.cesfelipesegundo.itis.model.MediaElem;
import com.cesfelipesegundo.itis.model.TemplateExamAnswer;
import com.cesfelipesegundo.itis.model.TemplateExamQuestion;
import com.cesfelipesegundo.itis.model.User;

/**
 * @author jlrisco
 *
 */
public class GroupDAOImplTest extends AbstractDependencyInjectionSpringContextTests {

	protected String[] getConfigLocations() {
		return new String [] {"classpath:com/cesfelipesegundo/itis/dao/config/spring-dao.xml"};    
	}

	/**
	 * Test method for {@link com.cesfelipesegundo.itis.dao.GroupDAOImpl#getTeachedGroups(java.lang.Long)}.
	 */
	public final void testGetTeachedGroups() {
		System.out.println("testGetTeachedGroups");
        GroupDAO groupDAO = (GroupDAOImpl)this.applicationContext.getBean("groupDAO");
        // Pruebo con un usuario: 4
        List<Group> groupList = groupDAO.getTeachedGroups(Long.valueOf(100));
        for(Group group : groupList) {
        	System.out.println("Id del grupo: " +  group.getId());
        	System.out.println("Nombre del grupo: " +  group.getName());
        	Course course = group.getCourse();
        	System.out.println("Id del curso: " +  course.getId());
        	System.out.println("Nombre del curso: " +  course.getName());
        	Institution institution = group.getInstitution();
        	System.out.println("Id del centro: " +  institution.getId());
        	System.out.println("Dirección: " +  institution.getAddress());
        	System.out.println("Ciudad: " +  institution.getCity());
        	System.out.println("Nombre: " +  institution.getName());
        	System.out.println("Provinvia: " +  institution.getState());
        	System.out.println("Cod. Postal: " +  institution.getZipCode());
        	System.out.println("Código: " +  institution.getCode());
        }
	}

	/**
	 * Test method for {@link com.cesfelipesegundo.itis.dao.GroupDAOImpl#getCourseGroups(com.cesfelipesegundo.itis.model.Course)}.
	 */
	public final void testGetCourseGroups() {
		System.out.println("testGetCourseGroups");
        GroupDAO groupDAO = (GroupDAOImpl)this.applicationContext.getBean("groupDAO");
        // Pruebo con un curso: 1
        Course cour = new Course();
        cour.setId(Long.valueOf(1));
        List<Group> groupList = groupDAO.getCourseGroups(cour);
        for(Group group : groupList) {
        	System.out.println("Id del grupo: " +  group.getId());
        	System.out.println("Nombre del grupo: " +  group.getName());
        	Course course = group.getCourse();
        	System.out.println("Id del curso: " +  course.getId());
        	System.out.println("Nombre del curso: " +  course.getName());
        	Institution institution = group.getInstitution();
        	System.out.println("Id del centro: " +  institution.getId());
        	System.out.println("Dirección: " +  institution.getAddress());
        	System.out.println("Ciudad: " +  institution.getCity());
        	System.out.println("Nombre: " +  institution.getName());
        	System.out.println("Provinvia: " +  institution.getState());
        	System.out.println("Cod. Postal: " +  institution.getZipCode());
        	System.out.println("Código: " +  institution.getCode());
        }
	}

	/**
	 * Test method for {@link com.cesfelipesegundo.itis.dao.GroupDAOImpl#fillCourse(com.cesfelipesegundo.itis.model.Group)}.
	 */
	public final void testFillCourse() {
		System.out.println("testFillCourse");
        GroupDAO groupDAO = (GroupDAOImpl)this.applicationContext.getBean("groupDAO");
        Group group = groupDAO.getGroup(Long.valueOf(1));
        groupDAO.fillCourse(group);
       	System.out.println("Id del grupo: " +  group.getId());
       	System.out.println("Nombre del grupo: " +  group.getName());
       	Course course = group.getCourse();
       	System.out.println("Id del curso: " +  course.getId());
       	System.out.println("Nombre del curso: " +  course.getName());
	}

	/**
	 * Test method for {@link com.cesfelipesegundo.itis.dao.GroupDAOImpl#getGroup(java.lang.Long)}.
	 */
	public final void testGetGroup() {
		System.out.println("testGetGroup");
        GroupDAO groupDAO = (GroupDAOImpl)this.applicationContext.getBean("groupDAO");
        Group group = groupDAO.getGroup(Long.valueOf(1));
       	System.out.println("Id del grupo: " +  group.getId());
       	System.out.println("Nombre del grupo: " +  group.getName());
	}

	/**
	 * Test method for {@link com.cesfelipesegundo.itis.dao.GroupDAOImpl#getQuestions(com.cesfelipesegundo.itis.model.Group)}.
	 */
	public final void testGetQuestions() {
		System.out.println("testGetQuestions");
		GroupDAO groupDAO = (GroupDAOImpl)this.applicationContext.getBean("groupDAO");
		Group group = groupDAO.getGroup(Long.valueOf(100));
    	List<TemplateExamQuestion> questions = groupDAO.getQuestions(group);
    	for(TemplateExamQuestion question : questions) {
    		System.out.println("Id de la pregunta: " + question.getId());
    		System.out.println("Texto de la pregunta: " + question.getText());
    		System.out.println("Número de preguntas correctas: " + question.getNumCorrectAnswers());
    		System.out.println("Nombre del tema:" + question.getSubject().getSubject());
    		System.out.println("Orden del tema:" + question.getSubject().getOrder());
    		
    		List<MediaElem> questionMedias = question.getMmedia();
    		for(MediaElem questionMedia : questionMedias) {
    			System.out.println("Id del multimedia: " + questionMedia.getId());
    			System.out.println("Path: " + questionMedia.getPath());
    			System.out.println("Tipo: " + questionMedia.getType());
    			System.out.println("Orden: " + questionMedia.getOrder());
    			System.out.println("Nombre: " + questionMedia.getName());
    		}
    		
    		List<TemplateExamAnswer> answers = question.getAnswers();
    		for(TemplateExamAnswer answer : answers) {
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

	public final void testGetStudents() {
		System.out.println("testGetStudents");
		GroupDAO groupDAO = (GroupDAOImpl)this.applicationContext.getBean("groupDAO");
		Group group = new Group();
		group.setId(Long.valueOf(1));
		List<User> users = groupDAO.getStudents(group);
		for(User user : users) {
			System.out.println("Name: " + user.getName());
			System.out.println("SurName: " + user.getSurname());
			System.out.println("UserName: " + user.getUserName());
			System.out.println("Id: " + user.getId());
		}
	}
	
	public final void testGetStudentsNotRegistered() {
		System.out.println("testGetStudentsNotRegistered");
		GroupDAO groupDAO = (GroupDAOImpl)this.applicationContext.getBean("groupDAO");
		Group group = new Group();
		group.setId(Long.valueOf(6));
		Institution institution = new Institution();
		institution.setId(Long.valueOf(1));
		group.setInstitution(institution);
		List<User> users = groupDAO.getStudentsNotRegistered(group, "LEARNER");
		for(User user : users) {
			System.out.println("Name: " + user.getName());
			System.out.println("SurName: " + user.getSurname());
			System.out.println("UserName: " + user.getUserName());
			System.out.println("Role: " + user.getRole());
			System.out.println("Id: " + user.getId());
		}
	}
}
