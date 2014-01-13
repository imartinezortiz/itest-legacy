package com.cesfelipesegundo.itis.test_DEPRECATED;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.cesfelipesegundo.itis.dao.api.UserDAO;
import com.cesfelipesegundo.itis.dao.UserDAOImpl;
import com.cesfelipesegundo.itis.model.User;

public class UserDAOTest extends AbstractDependencyInjectionSpringContextTests {
	
	protected String[] getConfigLocations() {
		return new String [] {"classpath:com/cesfelipesegundo/itis/dao/config/spring-dao.xml"};    
	}
	
	public void testListUsuariosDAO(){
	    logger.debug("Prueba listado grupos impartidos por un profesor.");
        UserDAO userDAO = (UserDAOImpl)this.applicationContext.getBean("userDAO");
        // Obtengo los usuarios de la BD
        User user = userDAO.getUser("nuria");
        System.out.println("Nombre del usuario: " +  user.getName());
        System.out.println("Apellidos del usuario: " +  user.getSurname());
        System.out.println("Nickname del usuario: " +  user.getUserName());
       	System.out.println("Id del usuario: " +  user.getId());
	    logger.debug("Fin de test..." );
	  }
}
