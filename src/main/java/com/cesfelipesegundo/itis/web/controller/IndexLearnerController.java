package com.cesfelipesegundo.itis.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.cesfelipesegundo.itis.biz.api.LearnerManagementService;
import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.web.Constants;

import es.itest.engine.course.business.entity.Group;
import es.itest.engine.test.business.entity.TestDetails;


   public class IndexLearnerController implements Controller {
		
		/**
		 * Este controlador muestra la vista principal de un alumno. Es invocado tras la autenticación e
		 * identificación del rol learner. Obtiene el listado de exámenes disponibles para hacer, así como
		 * el listado de revisiones posibles.
		 * @author chema
		 *
		 */
		
	   private LearnerManagementService learnerManagementService;
		
	   public LearnerManagementService getLearnerManagementService() {
			return learnerManagementService;
		}


		public void setLearnerManagementService(
				LearnerManagementService learnerManagementService) {
			this.learnerManagementService = learnerManagementService;
		}
		
	   
	   
		public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

			// Identificación del usuario a partir del login, quien lo puso en el request:
			User user = (User) request.getSession().getAttribute(Constants.USER);			
			
			
			// Creación del ModelAndView: different depending on the role of the user
			ModelAndView mav = new ModelAndView();
			// Learner KID: tiene interfaz especial:
			if (user.getRole().equals(new String(Constants.KID))) {
				// Obtención de la lista de exámenes disponibles para este alumno a través del gestor de servicios
				List<TestDetails> pendingExamslist = learnerManagementService.getPendingTests(user.getId());
				
				// Nombre lógico: index_kid
				 mav.setViewName("learner/index_kid");
				 mav.addObject("pendingExams",pendingExamslist);
			} else {
				// Nombre lógico: index_learner
				mav.setViewName("learner/index_learner");
				//Obtenemos los grupos en los que está matriculado el alumno
				List<Group> groupList = learnerManagementService.getUserGroups(user.getId());
				mav.addObject("groupList", groupList);
			}
			
			
			// Adición del usuario para mostrar nombre y apellidos
			mav.addObject("user",user);
			
			return mav;
		}
		
		/*
		 * public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

			// Identificación del usuario a partir del login, quien lo puso en el request:
			User user = (User) request.getSession().getAttribute(Constants.USER);			
					
			// Obtención de la lista de exámenes disponibles para este alumno a través del gestor de servicios
			List<BasicDataExam> pendingExamslist = learnerManagementService.getPendingExams(user.getId());
			
			// Obtención de la lista de exámenes a revisar para este alumno:
			List<BasicDataExam> doneExamslist = learnerManagementService.getExamsForRevision(user.getId());

			// Creación del ModelAndView: different depending on the role of the user
			ModelAndView mav = new ModelAndView();
			// Learner KID: tiene interfaz especial:
			if (user.getRole().equals(new String(Constants.KID))) {
				// Nombre lógico: index_kid
				 mav.setViewName("learner/index_kid");
			} else {
				// Nombre lógico: index_learner
				mav.setViewName("learner/index_learner");
				// Adición de las listas al M&V
				mav.addObject("doneExams",doneExamslist);
			}
			
			// Listas comunes a ambos M&V
			mav.addObject("pendingExams",pendingExamslist);
			// Adición del usuario para mostrar nombre y apellidos
			mav.addObject("user",user);
			
			return mav;
		}
		 * */
		
	}
   
