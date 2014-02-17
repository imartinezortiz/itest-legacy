package com.cesfelipesegundo.itis.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.cesfelipesegundo.itis.biz.api.TutorManagementService;
import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.web.Constants;

import es.itest.engine.course.business.entity.Group;

public class IndexTutorController implements Controller {
	
	private TutorManagementService tutorManagementService;
	
	public TutorManagementService getTutorManagementService() {
		return tutorManagementService;
	}

	public void setTutorManagementService(
			TutorManagementService tutorManagementService) {
		this.tutorManagementService = tutorManagementService;
	}

	/**
	 * Este controlador accede a los grupos que imparte un profesor. Corresponde al contenido mostrado al iniciar sesión
	 * como profesor. Es invocado tras la autenticación e identificación del rol tutor.
	 * @author chema
	 *
	 */
	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// Identificación del usuario a partir del login, quien lo puso en el request:
		User user = (User) request.getSession().getAttribute(Constants.USER);
		
		// Obtención de la lista de grupos que imparte este profesor
		List<Group> list = tutorManagementService.getTeachedGroups(user.getId());

		// Creación del ModelAndView. Nombre lógico: tutor/index_tutor
		ModelAndView mav = new ModelAndView("tutor/index_tutor");

		// Adición del mapa (modelo) al objeto ModelAndView
		mav.addObject("groups",list);
		//Adición del usuario para mostrar nombre y apellidos
		mav.addObject("user",user);
		
		return mav;
	}	
	
}
