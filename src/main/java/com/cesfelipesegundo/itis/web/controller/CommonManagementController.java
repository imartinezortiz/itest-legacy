package com.cesfelipesegundo.itis.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import com.cesfelipesegundo.itis.biz.CommonManagementService;
import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.web.Constants;


/**
 * Common controller to allow students, teachers and admin to share functionalities.
 * 
 * @author J. M. Colmenar
 *
 */
public class CommonManagementController {


	private CommonManagementService commonManagementService;
	
	public CommonManagementService getCommonManagementService() {
		return commonManagementService;
	}

	public void setCommonManagementService(
			CommonManagementService commonManagementService) {
		this.commonManagementService = commonManagementService;
	}

	private static final Log log = LogFactory.getLog(CommonManagementController.class);
	
	
	/**
	 * Change password first step
	 * @param request
	 * @param response
	 * @return ModelAndView corresponding to the form to change the password
    */
	public ModelAndView changePassword (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("common/change_pass");
		
		// Just adding the user to show name. Comes from the session
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);

		return mav;
	}
	

	/**
	 * Change password second step: the password is stored (or not) into the database.
	 * @param request
	 * @param response
	 * @return ModelAndView corresponding to the form to change the password
    */
	public ModelAndView changePasswordStep2 (HttpServletRequest request, HttpServletResponse response) {
		// Create the ModelAndView. 
		ModelAndView mav = new ModelAndView("common/change_pass2");
		// Response for the ModelAndView
		Boolean resp = true;
		
		// Need the data from the user
		User user = (User) request.getSession().getAttribute(Constants.USER);
		
		// Comprobations:
		// 1.- Old password is correct (matches with the user that started the session)
		String oldPasswd = String.valueOf(request.getParameter("oldPasswd"));
		if (oldPasswd != null) {
			resp = commonManagementService.checkPassword(user,oldPasswd);
		} else {
			resp = false;
		}
		
		// 2.- Check that the new passwords are correct and are the same:
		if (resp) {
			String newPasswd1 = String.valueOf(request.getParameter("newPasswd1"));
			String newPasswd2 = String.valueOf(request.getParameter("newPasswd2"));
			if ((newPasswd1 != null) && (newPasswd2 != null) && (newPasswd1.equals(newPasswd2))) {
				// Do the modification
				user = commonManagementService.updatePassword(user,newPasswd1);
			} else {
				// Cannot perform the update
				resp = false;
				log.info("Change password not allowed: new password is not correct");
			}
		} else {
			log.info("Change password not allowed: user password is not correct");
		}
		
		// If the password was successfully updated, the session user needs an update:
		if (resp)
			request.getSession().setAttribute(Constants.USER, user);

		// User needed for the interface
		mav.addObject("user",user);
		// Response to the ModelAndView
		mav.addObject("resp",resp);

		return mav;
	}
	
	/**
	 * Muestra la pantalla de comprobación de plugins previa a la generación del examen. Se colocan en ella los
	 * datos necesarios para invocar al examen seleccionado.
	 * @param request
	 * @param response
	 * @return ModelAndView correspondiente a la pantalla de plugins
	 */
	public ModelAndView checkPlugins (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. Nombre lógico: common/check_plugins
		ModelAndView mav = new ModelAndView("common/check_plugins");
		
		// Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		
		return mav;
	}

 
}
