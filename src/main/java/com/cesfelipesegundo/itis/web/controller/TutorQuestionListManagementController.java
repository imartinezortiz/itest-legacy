package com.cesfelipesegundo.itis.web.controller;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.cesfelipesegundo.itis.biz.api.AdminManagementService;
import com.cesfelipesegundo.itis.biz.api.LearnerManagementService;
import com.cesfelipesegundo.itis.biz.api.TutorManagementService;
import com.cesfelipesegundo.itis.model.ExamForReview;
import com.cesfelipesegundo.itis.model.Group;
import com.cesfelipesegundo.itis.model.Institution;
import com.cesfelipesegundo.itis.model.Query;
import com.cesfelipesegundo.itis.model.Subject;
import com.cesfelipesegundo.itis.model.TemplateExamQuestion;
import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.model.comparators.TemplateExamQuestionComparator;
import com.cesfelipesegundo.itis.web.Constants;

/**
 * It manages the operations related to LIST of question of the managed group
 * 
 * Extends MultiActionController to use some servlet properties like "getServletContext"
 * 
 * @author chema
 *
 */
public class TutorQuestionListManagementController extends MultiActionController {

	private static final Log log = LogFactory.getLog(TutorQuestionListManagementController.class);
	
	
	private LearnerManagementService learnerManagementService;
	/**
	 * Service needed to manage requests from tutor
	 */
    private TutorManagementService tutorManagementService;
    
    /**
	 * Service needed to manage requests from tutor. Only for receive Institutions and Groups of Institution.
	 */
    private AdminManagementService adminManagementService;
    
    /**
     * Question LIST being managed by the tutor
     */
    private List<TemplateExamQuestion> currentQuestionList;

    /**
     * Question LIST to maintain the questions to be imported
     */
    private List<TemplateExamQuestion> preImportedQuestionList;
    
    /* ******** Getters and setters ******** */

	public TutorManagementService getTutorManagementService() {
		return tutorManagementService;
	}


	public void setTutorManagementService(
			TutorManagementService tutorManagementService) {
		this.tutorManagementService = tutorManagementService;
	}

	
	public AdminManagementService getAdminManagementService() {
		return adminManagementService;
	}


	public void setAdminManagementService(
			AdminManagementService adminManagementService) {
		this.adminManagementService = adminManagementService;
	}


	public List<TemplateExamQuestion> getCurrentQuestionList() {
		return currentQuestionList;
	}


	public void setCurrentQuestionList(
			List<TemplateExamQuestion> currentQuestionList) {
		this.currentQuestionList = currentQuestionList;
	}
	
	public List<TemplateExamQuestion> getPreImportedQuestionList() {
		return preImportedQuestionList;
	}


	public void setPreImportedQuestionList(
			List<TemplateExamQuestion> preImportedQuestionList) {
		this.preImportedQuestionList = preImportedQuestionList;
	}

	public LearnerManagementService getLearnerManagementService() {
		return learnerManagementService;
	}


	public void setLearnerManagementService(
			LearnerManagementService learnerManagementService) {
		this.learnerManagementService = learnerManagementService;
	}
	
	/* ----------------------- Auxiliar Methods -------------------------------------- */


	/**
	 * Deletes a file from the multimedia path
	 * @param filename file name
	 */
	private void deleteMmediaFile (String filename) {
		try {
			File file = new File(getServletContext().getRealPath("/")+Constants.MMEDIAPATH+filename);
			file.delete();
			log.debug("Borrado fichero "+filename);
		} catch (Exception e) {
			log.error("No se puede borrar: "+e.getMessage());
		}
	}	
	
	
    /* ---------------------- Question LIST Management (Ajax) ------------------------ */
	

	/**
	 * Deletes the question and all the related info from the database.
	 * Implements the "delete" action of the question list.
	 * @return List of questions, needed for the callback function to repaint the list
	 */
	public List<TemplateExamQuestion> deleteQuestion (String idquestion) {
		
        // Find the question to be deleted:
		Iterator<TemplateExamQuestion> iterQ = currentQuestionList.iterator();
		TemplateExamQuestion question = null;
		boolean aFound = false;
		
		while (iterQ.hasNext() && (!aFound)) {
			question = iterQ.next();
			if (question.getId().equals(Long.valueOf(idquestion))) {
				aFound = true;
			}
		}
		
		if (!aFound) {
			// Question not found
			log.error("- Pregunta "+idquestion+" NO encontrada");
		} else {
			
			// Need the info of the answers from the database to delete the files
			//TemplateExamQuestion qaux = tutorManagementService.getQuestionFromId(question);

			// this is done in the service
/*			
			// Delete the mmedia files of the question:
			List<MediaElem> mmlist = qaux.getMmedia();
			if (mmlist != null) {
				Iterator<MediaElem> iterMM = mmlist.iterator();
				while (iterMM.hasNext()) {
					MediaElem mm = iterMM.next();
					deleteMmediaFile(mm.getPath());
				}
			}
			
			// It also has to delete all the mmedia files of the answers.		
			List<TemplateExamAnswer> answList = qaux.getAnswers();
			if (answList != null) {
				Iterator<TemplateExamAnswer> iterAL = answList.iterator();
				while (iterAL.hasNext()){
					TemplateExamAnswer ans = iterAL.next();
					// For each answer, delete the files
					List<MediaElem> ansMM = ans.getMmedia();
					if (ansMM != null) {
						Iterator<MediaElem> iterMMA = ansMM.iterator();
						while (iterMMA.hasNext()) {
							MediaElem mm = iterMMA.next();
							deleteMmediaFile(mm.getPath());
						}
					}
				}
			}
*/				
			// Delete the question from the database and all the related information
			if(!question.getUsedInExam()){
				// Remove question from the list
				log.debug("- Borrando pregunta "+question.getId()+": "+question.getText());
				tutorManagementService.deleteQuestion(question);
				currentQuestionList.remove(question);
			}else{
				log.debug("No se puede borrar la pregunta con id: "+question.getId()+" porque aparece en un examen");
			}
			
			
		}
		
		return currentQuestionList;
		
	} // deleteQuestion
	
	/**
	 * Deletes the question and all the related info from the database.
	 * Implements the "delete" action of the question list.
	 * @return List of questions, needed for the callback function to repaint the list
	 */
	public Object[] deleteQuestionForbiden (String idquestion) {
		Object[] respuesta = new Object[2];
		respuesta[1]= 0;
        // Find the question to be deleted:
		Iterator<TemplateExamQuestion> iterQ = currentQuestionList.iterator();
		TemplateExamQuestion question = null;
		boolean aFound = false;
		
		while (iterQ.hasNext() && (!aFound)) {
			question = iterQ.next();
			if (question.getId().equals(Long.valueOf(idquestion))) {
				aFound = true;
			}
		}
		
		if (!aFound) {
			// Question not found
			log.error("- Pregunta "+idquestion+" NO encontrada");
		} else {
			
			// Obtengo una lista de ids de los examenes que se verán modificados.
			List<Long> examids = tutorManagementService.getExamIds(question);
			
			//Se elimina la pregunta que se borrará en cascada.
			tutorManagementService.deleteQuestion(question);
			
			// Remove question from the list
			currentQuestionList.remove(question);
			
			// Recoregimos los examenes alterados
			int examsModify = 0;
			for(int i=0;i<examids.size();i++){
				List<ExamForReview> exams = learnerManagementService.examReviewByIdExam(examids.get(i));
				examsModify += exams.size();
			}
						
			log.debug("- Borrando pregunta "+question.getId()+": "+question.getText());
			
			
			
			respuesta[1]= examsModify;
		}
		respuesta[0]= currentQuestionList;
		return respuesta;
		
	} // deleteQuestion

	/**
	 * Deletes a list of questions and all the related info from the database.
	 * @return List of questions, needed for the callback function to repaint the list
	 */
	public List<TemplateExamQuestion> deleteQuestions (String[] questions) {
		for (int i = 0; i < questions.length; i++)
			deleteQuestion(questions[i]);
		return currentQuestionList;		
	}
	
	/**
	 * Changes the activity of the question: active or not, depending on the value
	 * @return List of questions, needed for the callback function to repaint the list
	 */
	public List<TemplateExamQuestion> changeActivityQuestion (String idquestion, String value) {
		  // Find the question to be updated:
		Iterator<TemplateExamQuestion> iterQ = currentQuestionList.iterator();
		TemplateExamQuestion question = null;
		boolean aFound = false;
		
		while (iterQ.hasNext() && (!aFound)) {
			question = iterQ.next();
			if (question.getId().equals(Long.valueOf(idquestion))) {
				aFound = true;
			}
		}
		
		if (!aFound) {
			// Answer not found
			log.error("- Pregunta "+idquestion+" NO encontrada");
		} else {
			// Change the activity:
			question.setActive(Integer.valueOf(value));
			
			// Save question on the database:
			tutorManagementService.saveQuestion(question);

		}
		
		return currentQuestionList;
		
	} // changeActivityQuestion
	
	/**
	 * Changes the activity of a list of questions: active or not, depending on the value
	 * @return List of questions, needed for the callback function to repaint the list
	 */
	public List<TemplateExamQuestion> changeActivityQuestions (String[] questions, String value) {
		for (int i = 0; i < questions.length; i++)
			changeActivityQuestion(questions[i],value);
		return currentQuestionList;
	}
	
	/**
	 * Changes the visibility of a list of questions to the value specified
	 * @return List of questions, needed for the callback function to repaint the list
	 */
	public List<TemplateExamQuestion> changeVisibilityQuestions (String[] questions, String value) {
		for (int i = 0; i < questions.length; i++) {
			Iterator<TemplateExamQuestion> iterQ = currentQuestionList.iterator();
			TemplateExamQuestion question = null;
			boolean aFound = false;
			
			while (iterQ.hasNext() && (!aFound)) {
				question = iterQ.next();
				if (question.getId().equals(Long.valueOf(questions[i]))) {
					aFound = true;
				}
			}
			
			if (!aFound) {
				// Answer not found
				log.error("- Pregunta "+questions[i]+" NO encontrada");
			} else {
				// Change the visibility:
				question.setVisibility(Integer.valueOf(value));
				
				// Save question on the database:
				tutorManagementService.saveQuestion(question);
				
			}
		}
		return currentQuestionList;
	}
	
	/**
	 * Changes the difficulty of a list of questions to the value specified
	 * @return List of questions, needed for the callback function to repaint the list
	 */
	public List<TemplateExamQuestion> changeDifficultyQuestions (String[] questions, String value) {
		for (int i = 0; i < questions.length; i++) {
			Iterator<TemplateExamQuestion> iterQ = currentQuestionList.iterator();
			TemplateExamQuestion question = null;
			boolean aFound = false;
			
			while (iterQ.hasNext() && (!aFound)) {
				question = iterQ.next();
				if (question.getId().equals(Long.valueOf(questions[i]))) {
					aFound = true;
				}
			}
			
			if (!aFound) {
				// Answer not found
				log.error("- Pregunta "+questions[i]+" NO encontrada");
			} else {
				// Change the difficulty:
				question.setDifficulty(Integer.valueOf(value));
				
				// Save question on the database:
				tutorManagementService.saveQuestion(question);
				
			}
		}
		return currentQuestionList;
	}
	
	/**
	 * Changes the subject of a list of questions to the value specified
	 * @return List of questions, needed for the callback function to repaint the list
	 */
	public List<TemplateExamQuestion> changeSubjectQuestions (String[] questions, String value) {
		Subject subject = tutorManagementService.getSubject(Long.valueOf(value));
		if (subject == null) {
			log.error("se ha intentado cambiar a un tema inexistente: "+value);
		}
		else {
			for (int i = 0; i < questions.length; i++) {
				Long id = null;
				try{
					id = Long.valueOf(questions[i]);
				}catch(Exception e){
					id = new Long(-1);
				}
				/*
				 * Los id = -1 son porque en el javascript los cambio si la pregunta a caido en algun examen
				 * o que no se ha hecho bien la conversion de String a Long
				 * */
				if(id!=-1){
					Iterator<TemplateExamQuestion> iterQ = currentQuestionList.iterator();
					TemplateExamQuestion question = null;
					boolean aFound = false;
					
					while (iterQ.hasNext() && (!aFound)) {
						question = iterQ.next();
						if (question.getId().equals(id)) {
							aFound = true;
						}
					}
					
					if (!aFound) {
						// Answer not found
						log.error("- Pregunta "+questions[i]+" NO encontrada");
					} else {
						// Change the visibility:
						question.setSubject(subject);
						
						// Save question on the database:
						tutorManagementService.saveQuestion(question);
						
					}
				}
			}
		}
		return currentQuestionList;
	}
	
	/**
	 * Applies for filters and searches related to questions
	 * 
	 * Note that any time the filter is applied, the list of preimported questions is cleared
	 * 
	 * @return List of questions that comply with the filter, needed for the callback function to repaint the list
	 */
	public List<TemplateExamQuestion> filterAndSearch (String idgroup, String idquestion, String idtheme,String text,String textTheme,String diff,String scope,String active,String orderby,String idInstitution,boolean reverse,boolean limit,int questionType) {
		/* 
		 * We have to obtain from the database the list of questions related to this group, using the
		 * data from the parameters. 
		 */
		Query queryQuestions = new Query();
		try{
			if (!idgroup.equals(new String(""))) queryQuestions.setGroup(Long.valueOf(idgroup));
			if (!idquestion.equals(new String(""))) queryQuestions.setId(Long.valueOf(idquestion));
			if (!idtheme.equals(new String(""))) queryQuestions.setSubject(Long.valueOf(idtheme));
			if (!text.equals(new String(""))) queryQuestions.setText(text);
			if (!textTheme.equals(new String(""))) queryQuestions.setTextTheme(textTheme);
			if (!diff.equals(new String(""))) queryQuestions.setDifficulty(Short.valueOf(diff));
			if (!scope.equals(new String(""))) queryQuestions.setScope(Short.valueOf(scope));
			if (!idInstitution.equals(new String(""))) queryQuestions.setInstitution(Long.valueOf(idInstitution));
			if (!active.equals(new String(""))) {
				if (Short.valueOf(active).equals(Constants.YES)) {
					queryQuestions.setActive(true);
				} else {
					if (Short.valueOf(active).equals(Constants.NO)) {
						queryQuestions.setActive(false);
					}
				}
			}
			if(questionType!=-1)queryQuestions.setQuestionType(questionType);
		}catch(Exception e){
			return new Vector<TemplateExamQuestion>();
		}
		
		// Order:
		if (orderby.equals("text")) {
			queryQuestions.setOrder(Query.OrderBy.TITLE_XOR_TEXT);
		} else {
			if (orderby.equals(new String("id"))) {
				queryQuestions.setOrder(Query.OrderBy.ID);
			} else {
				if (orderby.equals(new String("subject"))) {
					queryQuestions.setOrder(Query.OrderBy.SUBJECT);
				} else {			
					if (orderby.equals("diff")) {
						queryQuestions.setOrder(Query.OrderBy.DIFFICULTY);
					} else {
						if (orderby.equals("scope")) {
							queryQuestions.setOrder(Query.OrderBy.SCOPE);
						}
						else {
							if(orderby.equals("course")) {
								queryQuestions.setOrder(Query.OrderBy.GROUP);
							}
						}
					}
				}
			}
		}
		queryQuestions.setFirstResult(0);
		if(limit)
			queryQuestions.setMaxResultCount(100);
		List<TemplateExamQuestion> qlist = tutorManagementService.find(queryQuestions);
		if(reverse){
			Collections.reverse(qlist);
		}
		// Setting the current question list
		setCurrentQuestionList(qlist);
		
		// In case of being used, the preImportedQuestionList is cleared to avoid confusion.
		if (preImportedQuestionList != null) preImportedQuestionList.clear();
		
		return currentQuestionList;
		
	} // filterAndSearch
		
	
	/* ------------- Ajax for imported questions -------------------------- */

	/**
	 * Imports the questions of the preImportedQuestionList to the theme indicated as a parameter.
	 * 
	 * @return List of questions, needed for the callback function to repaint the list
	 */
	public List<TemplateExamQuestion> importQuestions (String idgroup, String idtheme) {
		
		// Get the theme and group
		if (!idtheme.equals(new String("")) && !idgroup.equals(new String(""))) {
			Subject sbj = tutorManagementService.getSubject(Long.valueOf(idtheme));
			Group group = tutorManagementService.getGroup(Long.valueOf(idgroup));
			// Import questions:
			try{
				tutorManagementService.importQuestions(group,sbj,preImportedQuestionList,getServletContext().getRealPath("/")+Constants.MMEDIAPATH);
			}catch(Exception e){
				log.error("Cannot import questions. Error at import");
				// Lanzar un alert desde aqui, ¿Se puede hacer?
			}
			log.debug("Questions imported");
		} else {
			log.error("Cannot import questions. Empty theme id.");
		}
		
		// Once imported, the "pre-imported" questions list is cleared and the currentQuestionList is returned
		preImportedQuestionList.clear();
		
		return currentQuestionList;
		
	} // importQuestions

	
	/**
	 * Pre-import a question: adds or removes a question from the list of "pre-imported"
	 * questions. The questions of that list are imported once the user selects the option
	 * "import selected questions". This list only have the questions selected by the user.
	 * If a filter is run, the list is cleared.
	 * 
	 * @param idq		Id of the question
	 * @param value	May be "true" which means that the question has to be pre-imported
	 * 					or "false" which means to remove the pre-import.
	 * 
	 * @return Always returns true. This is important to return to the function that unlocks the screen.
	 */
	public boolean preImportQuestion (String idq, String value) {
		
		// Depending on the "value"
		if (value.equals("true")) {
		    // Adds the question to the preimported question list:
			
		    // Obtains all the data of the question:
		    TemplateExamQuestion q = new TemplateExamQuestion();
		    q.setId(Long.valueOf(idq));
		    q = tutorManagementService.getQuestionFromId(q);
		    // Adds the question to the list
		    if (!preImportedQuestionList.add(q))
			   log.error("Pre-import of question "+idq+" failed");
		    else
			   log.debug("Question "+idq+" pre-imported");
		    
		} else {
		    // Removes the question from the preimported question list:
			
			// Find the question:
			Iterator<TemplateExamQuestion> iterQ = preImportedQuestionList.iterator();
			TemplateExamQuestion q = null;
			boolean aFound = false;
			
			while (iterQ.hasNext() && (!aFound)) {
				q = iterQ.next();
				if (q.getId().equals(Long.valueOf(idq))) {
					aFound = true;
				}
			}
			
			if (!aFound) {
				// Answer not found
				log.error("- Question "+idq+" not found as preimported");
			} else {
				// Remove the question:
				if (!preImportedQuestionList.remove(q))
					log.error("Remove pre-imported question "+idq+" failed");
			    else
					log.debug("Question "+idq+" de-pre-imported");
			}
		}
		   
		// Always returns true. This is important to return to the function that unlocks the screen.
		return true;
	} // preImportQuestion

	
	/**
	 * Find Institutions registered into application
	 * @return List<Institution> list of Institutions
	 */
	
	public List<Institution> getInstitutions(){
		List<Institution> listInstitutions=adminManagementService.getInstitutions();
		if (listInstitutions==null){
			log.debug("Error, no hay centros en la BD");
		}
		return listInstitutions;
	}
	
	public List<Institution> getInstitutionsWidthPublicQuestions(){
		List<Institution> listInstitutions=adminManagementService.getInstitutionsWidthPublicQuestions();
		if (listInstitutions==null){
			log.debug("Error, no hay centros en la BD");
		}
		return listInstitutions;
	}
	
	/**
	 * Find Groups of a Institution selected
	 * @param idInstitution
	 * @return List<Group> List of Groups by Institution selected
	 */
	public List<Group> getGroupsByInstitutions(Long idInstitution){
		Institution centro=adminManagementService.getInstitution(idInstitution);
		return adminManagementService.getGroups(centro);
	}

	public List<Subject> getSubjectsByGroup(long idGroup){
		try{
			return tutorManagementService.getSubjects(tutorManagementService.getGroup(idGroup));
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Preview for the question being created/edited. May also be invoked by the import interface. In this
	 * latter case, an argument is passed at the request.
	 * @param request
	 * @param response
	 * @return ModelAndView corresponding to the preview for the question
    */
	public ModelAndView questionPreview (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		ModelAndView mav = new ModelAndView("tutor/question_preview");
		
		// The parameter must be filled in, then the question is previewed (used in "import question interface")
		TemplateExamQuestion question = new TemplateExamQuestion();
		if (request.getParameter("qId") != null) {
			Long idq = Long.valueOf(request.getParameter("qId"));
			question.setId(idq);
			question = tutorManagementService.getQuestionFromId(question);
		}
		
		// Addition of the question:
		mav.addObject("question",question);
		// Kind of students of the group
		mav.addObject("grouprole",request.getParameter("role"));
		
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		
		return mav;
	}
	
	public List<TemplateExamQuestion> orderQuestionList(String orderby,boolean reverse){
		Collections.sort(currentQuestionList,new TemplateExamQuestionComparator(orderby));
		if(reverse)
			Collections.reverse(currentQuestionList);
		return currentQuestionList;
	}
	
}
