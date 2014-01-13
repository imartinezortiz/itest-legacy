package com.cesfelipesegundo.itis.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.web.Constants;


   public class IndexAdminController implements Controller {

	   private AdminInstitutionManagementController adminInstitutionMgmtController;
	   
		public AdminInstitutionManagementController getAdminInstitutionMgmtController() {
			return adminInstitutionMgmtController;
		}

		public void setAdminInstitutionMgmtController(
				AdminInstitutionManagementController adminInstitutionManagementController) {
			this.adminInstitutionMgmtController = adminInstitutionManagementController;
		}

	   /**
		 * Este controlador muestra la vista principal de un administrador. Es invocado tras la autenticación e
		 * identificación del rol admin. Únicamente sirve para enlazar con el jsp que muestra el menú principal.
		 * @author ruben
		 *
		 */
		   
		public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

			// Identificación del usuario a partir del login, quien lo puso en el request:
			User user = (User) request.getSession().getAttribute(Constants.USER);			
					
			// Creación del ModelAndView: different depending on the role of the user
			ModelAndView mav = new ModelAndView();
			mav.setViewName("admin/index_admin");
			// Adición del usuario para mostrar nombre y apellidos
			mav.addObject("user",user);
			
			adminInstitutionMgmtController.setCurrentInstitution(null);
			
			return mav;
		}

		
		
	}
   
