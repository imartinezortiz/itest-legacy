package com.cesfelipesegundo.itis.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import com.cesfelipesegundo.itis.biz.MailSenderManagementServiceImpl;
import com.cesfelipesegundo.itis.biz.api.LearnerManagementService;
import com.cesfelipesegundo.itis.biz.api.TutorManagementService;
import com.cesfelipesegundo.itis.model.CustomExamUser;
import com.cesfelipesegundo.itis.model.Grade;
import com.cesfelipesegundo.itis.model.Message;
import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.web.Constants;
import com.lowagie.text.DocumentException;

import es.itest.engine.course.business.entity.Group;
import es.itest.engine.test.business.entity.Item.ItemTypeEnum;
import es.itest.engine.test.business.entity.ItemSession;
import es.itest.engine.test.business.entity.ItemSessionResponse;
import es.itest.engine.test.business.entity.TestDetails;
import es.itest.engine.test.business.entity.TestSession;
import es.itest.engine.test.business.entity.TestSessionTemplate;

/**
 * Clase delegada, encargada de la transición entre la pantalla principal del alumno y el examen seleccionado.
 * @author  chema
 */
public class LearnerExamController {
	
	private static final Log log = LogFactory.getLog(LearnerExamController.class);
	private static final Log examLog = LogFactory.getLog("com.cesfelipesegundo.itis.exam.journal");
	
	/**
	 * Service needed to manage requests from learner
	 * @uml.property  name="learnerManagementService"
	 * @uml.associationEnd  
	 */
    private LearnerManagementService learnerManagementService;
    
    
    /**
	 * Service needed to manage requests from learner
	 * @uml.property  name="tutorManagementService"
	 * @uml.associationEnd  
	 */
    private TutorManagementService tutorManagementService;
    
    /**
	 * Service needed to send mails
	 * @uml.property  name="mailSenderManagement"
	 * @uml.associationEnd  
	 */
    private MailSenderManagementServiceImpl mailSenderManagementService;
   
   
	/**
	 * Exam done by the user. It is a Spring session bean proxied in order to let the learnerExamController make use of the precise exam for each session.
	 *  The object is filled in with the current exam through the goNewExam method.
	 * @uml.property  name="currentExam"
	 * @uml.associationEnd  
	 */
    private TestSession currentExam;

    /**
     * Current grade: necessary to avoid errors when the grade page is reloaded
     */
    private Double currentGrade;
    
    /**
     * Current max grade: to show when the grade page is reloaded
     */
    private Double currentMaxGrade;

    /**
     * Current state: necessary to avoid reloading exam when going back from the grade page
     */
    private Boolean onExam = false;
    
    /* ******** Getters and setters ******** */
 
	/**
	 * True if user is currently developing an exam. Otherwise returns false.
	 * @return onExam
	 * @uml.property  name="onExam"
	 */
    public Boolean getOnExam() {
		return onExam;
	}

	/**
	 * Sets the current state of an exam: true (=currently performing) or false (not performing)
	 * @return onExam
	 * @uml.property  name="onExam"
	 */
	public void setOnExam(Boolean onExam) {
		this.onExam = onExam;
	}

	/**
	 * @return
	 * @uml.property  name="mailSenderManagementService"
	 */
	public MailSenderManagementServiceImpl getMailSenderManagementService() {
		return mailSenderManagementService;
	}

	/**
	 * @return mailSenderManagement
	 * @uml.property  name="mailSenderManagementService"
	 */
	public void setMailSenderManagementService(MailSenderManagementServiceImpl mailSenderManagement) {
		this.mailSenderManagementService = mailSenderManagement;
	}

	/**
	 * @return
	 * @uml.property  name="learnerManagementService"
	 */
    public LearnerManagementService getLearnerManagementService() {
		return learnerManagementService;
	}


	/**
	 * @param learnerManagementService
	 * @uml.property  name="learnerManagementService"
	 */
	public void setLearnerManagementService(
		LearnerManagementService learnerManagementService) {
		this.learnerManagementService = learnerManagementService;
	}	
	
	/**
	 * @return currentExam
	 * @uml.property  name="currentExam"
	 */
	public TestSession getCurrentExam() {
		return currentExam;
	}


	/**
	 * @param currentExam
	 * @uml.property  name="currentExam"
	 */
	public void setCurrentExam(TestSession currentExam) {
		this.currentExam = currentExam;
	}
	
	/**
	 * @return tutorManagementService
	 * @uml.property  name="tutorManagementService"
	 */
	public TutorManagementService getTutorManagementService() {
		return tutorManagementService;
	}

	/**
	 * @param tutorManagementService
	 * @uml.property  name="tutorManagementService"
	 */
	public void setTutorManagementService(
			TutorManagementService tutorManagementService) {
		this.tutorManagementService = tutorManagementService;
	}
	
	
	/* ********  Controller Methods ******** */
	
	/*
	 * Muestra la pantalla de comprobación de plugins previa a la generación del examen. Se colocan en ella los
	 * datos necesarios para invocar al examen seleccionado.
	 * @param request
	 * @param response
	 * @return ModelAndView correspondiente a la pantalla de plugins
	 */
	/*public ModelAndView checkPlugins (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. Nombre lógico: common/check_plugins
		ModelAndView mav = new ModelAndView("common/check_plugins");
		
		// Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		
		// Adición del id de la configuración de examen a generar
		String idexam=request.getParameter("idexam");
		if (idexam!=null) {
			mav.addObject("idexam",idexam);		
			examLog.info("Solicitado inicio de examen, configuración con id: "+request.getParameter("idexam").toString());
		}
		
		return mav;
	}*/
    

	/**
	 * Pide a la BD los detalles del examen para poder mostrarlos en el jsp
	 * @param request
	 * @param response
	 * @return ModelAndView correspondiente al examen
	 * @throws ServletException 
	 * */
	public ModelAndView goExamDetails(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ModelAndView
		ModelAndView mav = new ModelAndView();		
		// User
		User user = (User) request.getSession().getAttribute(Constants.USER);
		// Adding user to the view
		mav.addObject("user",user);
		// Exam id
		String idex = request.getParameter("idexam");
		if (idex == null) {
			// Ending exam to avoid concurrency
			mav.setViewName("learner/exam_error");
			mav.addObject("keyError","examNotDefined");
			examLog.error("Intenta acceder por URL sin examen !! - Alumno: "+ user.getUserName()+"("+ user.getId()+")");
			return mav;
		}
		// If ok, continue
		Long idexam = Long.valueOf(idex);
		// Petición de generación de un examen para este usuario, con la configuración que diga el idexam y la IP del cliente:		
		TestSession exam =learnerManagementService.createTestSessionPreview(idexam);
		mav.addObject("idexam",idexam);
		mav.addObject("exam",exam);
		mav.setViewName("learner/exam_details");		
		return mav;
	}

	/**
	 * Pide a la BD un nuevo examen para el alumno que lo solicitó e incluye los objetos necesarios en el model and view
	 * @param request
	 * @param response
	 * @return ModelAndView correspondiente al examen
	 * @throws ServletException 
	 */
	public ModelAndView goNewExam (HttpServletRequest request, HttpServletResponse response) throws Exception {
		// ModelAndView
		ModelAndView mav = new ModelAndView();		
		
		// User
		User user = (User) request.getSession().getAttribute(Constants.USER);
		// Adding user to the view
		mav.addObject("user",user);
		
		// Exam id
		String idex = request.getParameter("idexam");
		if (idex == null) {
			// Ending exam to avoid concurrency
			mav.setViewName("learner/exam_error");
			mav.addObject("keyError","examNotDefined");
			examLog.error("Intenta acceder por URL sin examen !! - Alumno: "+ user.getUserName()+"("+ user.getId()+")");
			return mav;
		}
		// If ok, continue
		Long idexam = null;
		try{
			idexam = Long.parseLong(idex);
		}catch(Exception e){
			mav.setViewName("learner/exam_error");
			mav.addObject("keyError","examNotDefined");
			examLog.error("El id de examen no es num�rico - Alumno: "+ user.getUserName()+"("+ user.getId()+")");
			return mav;
		}
		Long startingDate = Long.parseLong("0");
		// First, check if the user is currently on exam and is recharging -> there already exists an exam !!
		if (getOnExam()) {
			/*
			 * Si se pulsa el botón F5 entra en este if 
			 * */
			if(user!=null)
				examLog.error("Recarga de Examen !! - Alumno: "+ user.getUserName()+"("+ user.getId()+") - Examen finalizado");
			else
				examLog.error("Error inesperado, no hay ning�n usuario en sesi�n");
			//mav = endExam(request, response);
			
		} 
		TestSession ex = null;
		// Then, checking if the exam is already done:
		if (learnerManagementService.getAlreadyDoneExam(user, idexam) != null) {
			if(currentExam!=null){
				startingDate = currentExam.getStartingDate();
			}
			examLog.error("Intenta empezar examen ya empezado !! - Alumno: "+ user.getUserName()+"("+ user.getId()+") - Examen: "+idexam);
			ex = learnerManagementService.getAlreadyDoneExam(user, idexam);
			ex.setStartingDate(startingDate);
			if(!getOnExam()){
				// Ending exam to avoid concurrency
				mav.setViewName("learner/exam_error");
				mav.addObject("keyError","examAlreadyStarted");
				return mav;
			}
			
		} else{
			try{
				ex = learnerManagementService.createTestSession(user, idexam ,request.getRemoteAddr());
				if (ex == null) {					
					
					examLog.error("Examen concurrente !! - Alumno: "+ user.getUserName()+"("+ user.getId()+") - Examen finalizado");
					
					// Ending exam to avoid concurrency
					mav = endExam(request, response);
					mav.setViewName("learner/exam_error");
					mav.addObject("keyError","examNotDefined");
					return mav;
					
				}else{
					ex.setStartingDate(System.currentTimeMillis());
				}
			}catch(Exception e){
				examLog.error("No se ha podido generar el examen");
			}
		}
		
		// Petición de generación de un examen para este usuario, con la configuración que diga el idexam y la IP del cliente:		
		
		// Maybe there is a concurrent access: has to be controlled
		if (ex == null) {					
			
			examLog.error("Examen concurrente !! - Alumno: "+ user.getUserName()+"("+ user.getId()+") - Examen finalizado");
			
			// Ending exam to avoid concurrency
			mav = endExam(request, response);
			mav.setViewName("learner/exam_error");
			mav.addObject("keyError","");
			return mav;
			
		} else {
			TestSessionTemplate exam = new TestSessionTemplate();
			exam.setId(idexam);
			exam = learnerManagementService.getConfigExamFromId(exam);
			List<Message> messages = tutorManagementService.validate(exam);
			if(ex.getQuestions().size()==0 || !messages.isEmpty()){
				tutorManagementService.deleteStudentGradeAndExam(user.getId(), idexam);
				mav.setViewName("learner/exam_error");
				mav.addObject("keyError","examNotDefined");
				return mav;
			}
			// Currently performing an exam
			setOnExam(true);
			
			// Adición del examen
			mav.addObject("exam",ex);
			// Inclusion of the exam and max grade as session objects
			//ex never is null
			setCurrentExam(ex);
			setCurrentMaxGrade(new Double(ex.getMaxGrade()));
			learnerManagementService.checkExam(ex, user.getId());
			// If the exam's duration is longer than the session expiration time we fix this one to the former
			if (request.getSession().getMaxInactiveInterval() < 60 * ex.getDuration()) {
				request.getSession().setMaxInactiveInterval(60 * ex.getDuration());
			}
			
			// Learner KID: special interface
			if (user.getRole().equals(new String(Constants.KID))) {
				// ModelAndView. Nombre lógico: learner/exam_kid
				mav.setViewName("learner/exam_kid");
				// Adición de la primera pregunta
				mav.addObject("question",ex.getQuestions().get(0));
				// Número de pregunta
				mav.addObject("questionNumber",1);
				// Índice de la siguiente pregunta, si hay
				if (ex.getQuestions().size() > 1)
					mav.addObject("nextQuestionIndex",1);
				// Duration of the exam
				//Real ending date
				
			} else { 						// Regular interface
				// ModelAndView. Nombre lógico: learner/exam
				mav.setViewName("learner/exam");
			}	
								
			examLog.info("Examen generado. Alumno: "+ user.getUserName()+"("+ user.getId()+") - Id de configuración: "+ex.getId());
			
		}



		long finalDate = 0;
		long maxDate = 0;
		int durationExam = 0;
		
		//If reviewing an exam, is not needed to calculate dates again
		//Calculate and check exam duration, starting and ending date
		startingDate = currentExam.getStartingDate();
		//Real ending date
		finalDate = System.currentTimeMillis();
		//Max ending date possible (Starting date + Pre-configured exam duration)
		maxDate = currentExam.getStartingDate() + (currentExam.getDuration()*60000);
		
		durationExam = (int) Math.round((finalDate - startingDate) / 1000.0); // in seconds
		
		//If real ending date < max ending date possible => NO correction needed
		if(((60 * ex.getDuration()) - durationExam) < 0){
			return endExam(request, response);
		}
		mav.addObject("timeRemaining", 60 * ex.getDuration() - durationExam);
		
		return mav;
	}
	
	/**
	 * Pide a la BD la siguiente pregunta de un examen (para interfaz KID)
	 * @param request
	 * @param response
	 * @return ModelAndView correspondiente al examen
	 * @throws ServletException 
	 */
	public ModelAndView goNextQuestion (HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Usuario
		User user = (User) request.getSession().getAttribute(Constants.USER);
		
		// ModelAndView
		ModelAndView mav = new ModelAndView();
		
		// Get question to show
		ItemSession question = null;
		int questionIndex;
		try {
			questionIndex = Integer.valueOf(request.getParameter("nextQuestionIndex"));
			question = currentExam.getQuestions().get(questionIndex);
			mav.addObject("questionNumber",questionIndex+1);
		}
		catch (Exception e) {
			// Exam has ended
			return endExam(request, response);
		}
		// ModelAndView. Nombre lógico: learner/exam_kid
		mav.setViewName("learner/exam_kid");
		// Adición del usuario
		mav.addObject("user",user);
		// Adición del examen
		mav.addObject("exam",currentExam);
		// Adición de la pregunta
		mav.addObject("question",question);
		// Tiempo restante
		long startingDate = currentExam.getStartingDate();
		long nowDate = System.currentTimeMillis();
		int consumed = (int) Math.round((nowDate - startingDate) / 1000.0);
		int timeRemaining = 60 * currentExam.getDuration() - consumed;
		mav.addObject("timeRemaining", timeRemaining);

		return mav;
	}
	
	/**
	 * Send a mail to the teacher.
	 * @param request
	 * @param response
	 * @return ModelAndView correspondiente al examen
	 */
	public ModelAndView sendMail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = (User) request.getSession().getAttribute(Constants.USER);
		ModelAndView mav = new ModelAndView("learner/index_learner");
		if(!mailSenderManagementService.sendMail(user.getEmail(), request.getParameter("subject"), request.getParameter("message"), null,request.getParameter("toSend") )){
			mav.addObject("sendedMail", false);
		}
		
		mav.addObject("user",user);
		List<Group> groupList = learnerManagementService.getUserGroups(user.getId());
		mav.addObject("groupList", groupList);
		return mav;
	}
	
	public ModelAndView goToSendMail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = (User) request.getSession().getAttribute(Constants.USER);
		Long idGroup = Long.parseLong(request.getParameter("group"));
		ModelAndView mav;
		if(!user.getEmail().trim().equalsIgnoreCase("")){
			mav = new ModelAndView("learner/send_mail");
			List<User> tutors = learnerManagementService.getTutors(learnerManagementService.getGroup(idGroup));
			mav.addObject("tutors", tutors);
			mav.addObject("user",user);
			mav.addObject("group",learnerManagementService.getGroup(idGroup));
		}else{
			mav = new ModelAndView("learner/index_learner");
			mav.addObject("user",user);
			List<Group> groupList = learnerManagementService.getUserGroups(user.getId());
			mav.addObject("groupList", groupList);
			mav.addObject("noUserMail", false);
		}
		return  mav;
	}
	
	/**
	 * Obtains the complete exam for this learner.
	 * @param request
	 * @param response
	 * @return ModelAndView correspondiente al examen
	 */
	public ModelAndView goGroupExams(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = (User) request.getSession().getAttribute(Constants.USER);	
		
		Long idGroup = Long.parseLong(request.getParameter("group"));
		
		// Obtención de la lista de exámenes disponibles para este alumno a través del gestor de servicios
		List<TestDetails> pendingExamslist = learnerManagementService.getPendingTests(user.getId());
		List<TestSessionTemplate> pendingConfigExams = new ArrayList<TestSessionTemplate>();
		for(int i=0;i<pendingExamslist.size();i++){
			if(pendingExamslist.get(i).getGroup().getId().compareTo(idGroup)!=0){
				pendingExamslist.remove(i);
				i--;
			}else{
				TestSessionTemplate configExam = new TestSessionTemplate();
				configExam.setId(pendingExamslist.get(i).getId());
				configExam = learnerManagementService.getConfigExamFromId(configExam);
				if(configExam.isCustomized()){
					List<CustomExamUser> users = learnerManagementService.getUsersInCustomExam(configExam.getId());
					for(CustomExamUser usuario : users){
						if(user.getId().equals(usuario.getId())){
							pendingConfigExams.add(configExam);
							break;
						}
					}
				}else{
					pendingConfigExams.add(configExam);
				}
					
			}
		}
		// Obtención de la lista de exámenes a revisar para este alumno:
		List<TestDetails> doneExamslist = learnerManagementService.getTestSessionsForReview(user.getId());
		List<TestSession> doneExamsDetailslist = new ArrayList<TestSession>();
		List<Grade> califData = new ArrayList<Grade>();
		for(int i=0;i<doneExamslist.size();i++){
			if(doneExamslist.get(i).getGroup().getId().compareTo(idGroup)!=0){
				doneExamslist.remove(i);
				i--;
			}else{
				califData.add(learnerManagementService.getGradeByIdExam(doneExamslist.get(i).getId(),user.getId()));
				doneExamsDetailslist.add(learnerManagementService.createTestSessionPreview(doneExamslist.get(i).getId()));
			}
		}
		
		// Obtención de la lista con los próximos examenes
		List<TestDetails> nextExamslist = learnerManagementService.getNextExams(user.getId(),idGroup);
		List<TestSessionTemplate> nextConfigExams = new ArrayList<TestSessionTemplate>();
		for (int i=0;i<nextExamslist.size();i++){
			TestSessionTemplate configExam = new TestSessionTemplate();
			configExam.setId(nextExamslist.get(i).getId());
			configExam = learnerManagementService.getConfigExamFromId(configExam);
			if(configExam.isCustomized()){
				List<CustomExamUser> users = learnerManagementService.getUsersInCustomExam(configExam.getId());
				for(CustomExamUser usuario : users){
					if(user.getId().equals(usuario.getId())){
						nextConfigExams.add(configExam);
						break;
					}
				}
			}else{
				nextConfigExams.add(configExam);
			}
		}
		
		List<TestSession> alreadyDoneExams = learnerManagementService.getAlreadyDoneExamGradeByGroup(user.getId(),idGroup);
		List<Grade> alreadyDoneExamsGrade = learnerManagementService.getAlreadyDoneGradeByGroup(user.getId(),idGroup);
		ModelAndView mav = new ModelAndView("learner/index_group");
		Group group = learnerManagementService.getGroup(idGroup);
		// Adición del usuario
		mav.addObject("user",user);
		mav.addObject("group",group);
		mav.addObject("pendingExamsConfig",pendingConfigExams);
		mav.addObject("doneExamsDetails", doneExamsDetailslist);
		mav.addObject("alreadyDoneExams", alreadyDoneExams);
		mav.addObject("alreadyDoneExamsGrade", alreadyDoneExamsGrade);
		mav.addObject("califData", califData);
		mav.addObject("nextExamslist",nextConfigExams);
		return mav;
	
	}
	
	/**
	 * Comprueba si la última respuesta se ha respondido correctamente (para interfaz KID)
	 * @param request
	 * @param response
	 * @return ModelAndView correspondiente al examen
	 * @throws ServletException 
	 */
	public ModelAndView gradeQuestion (HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Usuario
		User user = (User) request.getSession().getAttribute(Constants.USER);
		
		// ModelAndView
		ModelAndView mav = new ModelAndView();
		
		// Get question to grade
		ItemSession question = null;
		int questionIndex, nextQuestionIndex;
		try {
			questionIndex = Integer.valueOf(request.getParameter("questionNumber")) - 1;
			question = currentExam.getQuestions().get(questionIndex);
			mav.addObject("questionNumber",questionIndex+1);
			nextQuestionIndex = questionIndex +1;
			// Adición del siguiente índice si lo hay
			if (currentExam.getQuestionsNumber() > nextQuestionIndex)
				mav.addObject("nextQuestionIndex",new Integer(nextQuestionIndex));
		}
		catch (Exception e) {
			// Exam has ended
			return endExam(request, response);
		}
		
		// Checking if the question is correctly answered
		boolean correct = true;
		if(question.getItem().getType()==ItemTypeEnum.MULTIPLE_CHOICE){
			Iterator<ItemSessionResponse> iterator = question.getAnswers().iterator();
			while (iterator.hasNext()) {
				ItemSessionResponse answer = iterator.next();
				if ((answer.getMarked() && answer.getSolution() == 0) || (!answer.getMarked() && answer.getSolution() != 0))
					correct = false;
			}
		}else{
			if(question.getAnswers()!=null && question.getAnswers().size()==1){
				ItemSessionResponse answer = question.getAnswers().get(0);
				String learnerAnswer = question.getLearnerFillAnswer();
				if(learnerAnswer!=null && answer.getText()!=null && answer.getText().toLowerCase().trim().equalsIgnoreCase(learnerAnswer.toLowerCase().trim())){
					correct=true;
				}else{
					correct=false;
				}
			}else{
				correct = false;
			}
		}

		// ModelAndView. Nombre lógico: learner/exam_kid
		mav.setViewName("learner/grade_question_kid");
		// Adición del usuario
		mav.addObject("user",user);
		// Adición de graduación
		mav.addObject("grade", correct);

		return mav;
	}
	
	/**
	 * Updates the current exam and reverts the changes to the database in order to register an answer as "marked" in 
	 * relation to a question, exam and learner 
	 * @param iduser User ID
	 * @param idquestion Question ID
	 * @param idanswer Answer marked ID
	 * @param mark
	 * @return true if question was answered in time
	 */	
	public boolean updateQuestion(String idexam, String iduser, String idquestion, String idanswer, String mark){
		try{
			if(currentExam==null) {
				log.info("No existe el examen actual");
				log.info("Recargando el examen actual");
				long idUser = -1;
				try{
					idUser = Long.parseLong(iduser);
				}catch(Exception e){
					e.printStackTrace();
				}
				long idExam= -1;
				try{
					idExam = Long.parseLong(idexam);
				}catch(Exception e){
					e.printStackTrace();
				}
				User user = learnerManagementService.getUser(idUser);
				currentExam = learnerManagementService.getAlreadyDoneExam(user, idExam);
			}
			return updateQuestion(iduser,idquestion,idanswer,mark,0);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Updates the current exam and reverts the changes to the database in order to register an answer as "marked" in 
	 * relation to a question, exam and learner 
	 * @param iduser User ID
	 * @param idquestion Question ID
	 * @param idanswer Answer marked ID
	 * @param mark
	 * @param retries Number of retries to save answer marking on DB
	 * @return true if question was answered in time
	 */
	private boolean updateQuestion (String iduser, String idquestion, String idanswer, String mark, int retries) {
		//Checking if question was answered in time
		//Max time to answer a question = Starting time (ms) + Exam duration (ms) + 5 sec grace period (ms) + 1 sec per retry saving to DB (ms)
		long maxTime = currentExam.getStartingDate() + (currentExam.getDuration()*60000) + 5000 + (1000*retries);
		//Answered in time = Current time <= maxTime
		//If not answered in time return false and logs it
		if (!(System.currentTimeMillis() <= maxTime)){
			examLog.info("El usuario "+iduser+" ha respondido a la pregunta "+idquestion+" con la respuesta "+idanswer+" FUERA DE TIEMPO");
			return false;
		}
		int rows = 0;	
		//End checking
		
		// Atributes from form:
		Boolean marked;
		// If the answer was checked, it will appear in the request object, otherwise it won't.
		if ((mark != null) && (mark.equals("true")))
			marked = true;
		else
			marked = false;
		
		// Update current exam:
		
		// 1.- Search for the question to be updated
		// Questions
		ListIterator<ItemSession> iterQuestions = null;
		try {
			iterQuestions = currentExam.getQuestions().listIterator();
		} catch (NullPointerException e) {
			examLog.error("Intento de marcar una respuesta sin examen establecido - Alumno: "+iduser+" - Pregunta: "+idquestion.toString()+" - Respuesta: "+idanswer.toString()+" - Marcada: "+marked.toString());
			return true;
		} catch (Exception e) {
			examLog.error("Error desconocido al marcar una respuesta - Alumno: "+iduser+" - Pregunta: "+idquestion.toString()+" - Respuesta: "+idanswer.toString()+" - Marcada: "+marked.toString()+" - Excepción: "+e.toString());
			return true;
		}
		boolean qFound = false;				// Guadian
		ItemSession question = null;		// Particular question
		ItemSessionResponse answer = null;			// Particular answer
		
		
		while (iterQuestions.hasNext() && !qFound) {
			question = iterQuestions.next();
					
			if (question.getId().equals(Long.valueOf(idquestion)))
				qFound = true;
		}
		if (qFound) {
		    // 2.- Search for answer
			if(question.getCorrectAnswers()==question.getMarkedAnswer() && marked){
				return false;
			}
		    // Answers
			ListIterator<ItemSessionResponse> iterAnswers = question.getAnswers().listIterator();
			
			// Guardian
			boolean aFound = false;
			
			while (iterAnswers.hasNext() && !aFound) {
				answer = iterAnswers.next();

				if (answer.getId().equals(Long.valueOf(idanswer)))
					aFound = true;
			}
		   
			if (aFound) {
			   // Update exam object:
			   answer.setMarked(marked);
  		       // Update database info
			   try {
				   rows = learnerManagementService.updateItemSessionResponse(currentExam.getId(),Long.valueOf(iduser),Long.valueOf(idquestion),Long.valueOf(idanswer),marked);
			   } catch (Exception e) {
				   examLog.info("Excepción al marcar respuesta: "+e.toString());
			   }
		
		       examLog.info("Examen: "+currentExam.getId().toString()+" - Alumno: "+iduser+" - Pregunta: "+idquestion.toString()+" - Respuesta: "+idanswer.toString()+" - Marcada: "+marked.toString());
			} else {
			   // Answer not found
			   examLog.info("Respuesta "+idanswer.toString()+" no encontrada");
		    }			
			
		} else {
			// Question not found
			examLog.info("Pregunta "+idquestion.toString()+" no encontrada");
		}
		if(rows ==1){
			return true;
		}else if(rows == 0){
			learnerManagementService.addNewExamAnswer2LogExams(currentExam,Long.valueOf(iduser),Long.valueOf(idquestion),System.currentTimeMillis());
			learnerManagementService.updateItemSessionResponse(currentExam.getId(),Long.valueOf(iduser),Long.valueOf(idquestion),Long.valueOf(idanswer),marked);
			return true;
		}else{
			return false;
		}
	} // updateQuestion
	
	/**
	 * Retry updating a question (just for generating a log) 
	 * @param request
	 * @param response
	 */
	public boolean reUpdateQuestion (String iduser, String idquestion, String idanswer, String mark, String attempt) {
		try{
			examLog.info("Reintento "+attempt+" (web) de marcar una respuesta. Examen: "+currentExam.getId().toString()+" - Alumno: "+iduser+" - Pregunta: "+idquestion.toString()+" - Respuesta: "+idanswer.toString()+" - Marcada: "+mark);
			log.info("Reintento "+attempt+" (web) de marcar una respuesta. Examen: "+currentExam.getId().toString()+" - Alumno: "+iduser+" - Pregunta: "+idquestion.toString()+" - Respuesta: "+idanswer.toString()+" - Marcada: "+mark);
			int retry = 0;
			try{
				retry = Integer.parseInt(attempt);
			}
			catch(NumberFormatException e){
				retry = 0;
			}
			boolean result = updateQuestion(iduser,idquestion,idanswer,mark,retry);
			examLog.info("Completado reintento "+attempt+" (web) de marcar una respuesta. Examen: "+currentExam.getId().toString()+" - Alumno: "+iduser+" - Pregunta: "+idquestion.toString()+" - Respuesta: "+idanswer.toString()+" - Marcada: "+mark);
			log.info("Completado reintento "+attempt+" (web) de marcar una respuesta. Examen: "+currentExam.getId().toString()+" - Alumno: "+iduser+" - Pregunta: "+idquestion.toString()+" - Respuesta: "+idanswer.toString()+" - Marcada: "+mark);
			return result;
		}catch(Exception e){
			return false;
		}
	}	// reUpdateQuestion
	
	
	public boolean updateConfidenceLevel(boolean checked, long userId, long questionId){
		try{
			long examId = currentExam.getId();
			for(ItemSession question : currentExam.getQuestions()){
				if(question.getId() == questionId){
					question.setExamineeWasConfident(checked);
				}
			}
			
			
			int rows = learnerManagementService.updateConfidenceLevel(examId, userId, questionId, checked);
			if(rows==0){
				log.info("No se ha encontrado la pregunta en el log para el usuario con id: "+userId+" en el examen con id: "+examId+" para la pregunta con id: "+questionId);
				learnerManagementService.addNewExamAnswer2LogExams(currentExam,userId,questionId,System.currentTimeMillis());
				rows = learnerManagementService.updateConfidenceLevel(examId, userId, questionId, checked);
				if(rows==0)
					return false;
			}
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	
	/**
	 * Ends up the exam, showing the grade. 
	 * @param request
	 * @param response
	 */
	public ModelAndView endExam (HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Retrieve the user information
		User user = (User) request.getSession().getAttribute(Constants.USER);
		
		// Grading the exam:
		Double gr = 0.0, maxGr = 0.0;
		if (currentExam != null) {
			//learnerManagementService.checkExam(currentExam,user.getId());
			gr = learnerManagementService.gradeTestSession(user.getId(),currentExam);
			maxGr = new Double(currentExam.getMaxGrade());
			examLog.info("Examen CORREGIDO. Alumno: "+ user.getUserName()+"("+ user.getId()+") - Calificación: "+gr);
			setCurrentGrade(gr);
		} else  {
			if (currentGrade != null){
				gr = currentGrade;
			}
			if (currentMaxGrade != null){
				maxGr = currentMaxGrade;
			}
		}
		// Different grade interface depending on the role of the user
		ModelAndView mav = new ModelAndView();
		// Learner KID: special interface
		if (user.getRole().equals(new String(Constants.KID))) {
			// grade_kid
			 mav.setViewName("learner/grade_kid");
		} else {
			// Regular grading
			mav.setViewName("learner/grade");
		}		
		long now = System.currentTimeMillis();
		
		// Adición del usuario
		mav.addObject("user",user);
		TestSessionTemplate exam = new TestSessionTemplate();
		//Aqui salta una excepcion siempre que se intenta hacer algun examen que no se ha validado bien
		try{
			exam.setId(currentExam.getId());
			exam = learnerManagementService.getConfigExamFromId(exam);
		}catch(Exception e){
			mav.setViewName("learner/exam_error");
			mav.addObject("keyError","examNotDefined");
			return mav;
		}
		// Cuando comienza la fecha de revision
		mav.addObject("startDateReview", exam.getStartDateRevision());
		mav.addObject("endDateReview", exam.getEndDateRevision());
		boolean goToRevision;
		if((exam.getStartDateRevision().getTime()<now && exam.getEndDateRevision().getTime()>now) && exam.isActiveReview()){
			goToRevision = true;
		}else{
			goToRevision = false;
		}
		// Adición de la calificación:
		mav.addObject("grade",gr);
		mav.addObject("goToRevision", goToRevision);
		mav.addObject("idExam", currentExam.getId());
		// Adición de la máxima calificación posible:
		mav.addObject("maxgrade",maxGr);

		// We have to reset the currentExam, because the user will maybe do another exam:
		this.setCurrentExam(null);
		// The exam is finished
		if(request.getParameter("pressedEndButton")!=null){
			setOnExam(false);
		}
		
		return mav;
	} // endExam
	
	
	/* ------------------------------ Exam Review ----------------------------- */ 
	public void reviewExam2PDF(HttpServletRequest request,HttpServletResponse response) {
		try {
			if(currentExam==null)
				currentExam = (TestSession)request.getSession().getAttribute("examPreview");
			ByteArrayOutputStream pdfByte = learnerManagementService.parse2PDF(currentExam);
			response.setHeader("Content-disposition", "attachment; filename=\""+currentExam.getTitle()+currentExam.getGroup().getCourse().getName()+currentExam.getId()+".pdf\""); 
			response.setContentType("application/pdf");
			response.setContentLength(pdfByte.size());
			ServletOutputStream out = response.getOutputStream();
			pdfByte.writeTo(out);
			out.flush();
		}
		catch(DocumentException e1) {
			log.error("Se ha producido un error al intentar generar el PDF con la revision del examen "+currentExam.getId()+"\n"+e1.getLocalizedMessage());
		}
		catch(IOException e2) {
			log.error("Se ha producido un error de E/S cuando se intentaba generar el PDF con la revision del examen "+currentExam.getId()+"\n"+e2.getLocalizedMessage());
		}
		catch(NullPointerException e3) {
			log.error("Se ha producido un error cuando se intentaba generar el PDF con la revision de un examen: EXAMEN NO VALIDO");
		}
		log.info("Se ha generado correctamente el PDF con la revision del examen "+currentExam.getId())	;	
	}
	/**
	 * Obtains the complete exam for this learner.
	 * @param request
	 * @param response
	 * @return ModelAndView correspondiente al examen
	 */
	public ModelAndView reviewExam (HttpServletRequest request, HttpServletResponse response) {
		// Usuario
		User user = (User) request.getSession().getAttribute(Constants.USER);
		
		// Based on an exam id, obtains from the database the data of the exam to be reviewed		
		TestSession ex = learnerManagementService.getAlreadyDoneExam(user, Long.valueOf(request.getParameter("idexam")));
		List<TestSession> examenes = learnerManagementService.getAllExams(ex.getId());
		double sumaNotas = 0;
		for(int i=0;i<examenes.size();i++){
			TestSession examen = examenes.get(i);
			sumaNotas += examen.getExamGrade();
		}
		double media = sumaNotas/examenes.size();
		media /=ex.getMaxGrade();
		media*=100;
		// Creación del ModelAndView. Nombre lógico: learner/exam_review
		ModelAndView mav = new ModelAndView("learner/exam_review");

		// Adición del usuario
		mav.addObject("user",user);
		// Adición del examen
		mav.addObject("exam",ex);
		mav.addObject("media", media);
		
		
		// Inclusion of the exam as the session object
		setCurrentExam(ex);
		
		examLog.info("Revisión Examen. Alumno: "+ user.getUserName()+"("+ user.getId()+") - Id de configuración: "+ex.getId());
		
		return mav;
	}

	/**
	 * 	receives an id and returns the corresponding test based on the id 
	 *  @param idExam
	 *  @return exam
	 * */
	public TestSession getExam(String idExam){
		Long idEx = Long.parseLong(idExam);
		TestSession exam =learnerManagementService.createTestSessionPreview(idEx);
		return exam;
	}

	/**
	 * @return
	 * @uml.property  name="currentGrade"
	 */
	public Double getCurrentGrade() {
		return currentGrade;
	}


	/**
	 * @param currentGrade
	 * @uml.property  name="currentGrade"
	 */
	public void setCurrentGrade(Double currentGrade) {
		this.currentGrade = currentGrade;
	}


	/**
	 * @return
	 * @uml.property  name="currentMaxGrade"
	 */
	public Double getCurrentMaxGrade() {
		return currentMaxGrade;
	}


	/**
	 * @param currentMaxGrade
	 * @uml.property  name="currentMaxGrade"
	 */
	public void setCurrentMaxGrade(Double currentMaxGrade) {
		this.currentMaxGrade = currentMaxGrade;
	}
	
	public boolean updateFillAnswer(Long idExam, Long idUser, Long idQuestion, Long idAnswer, String textAnswer){
		try{
			if(currentExam!=null){
				List<ItemSession> questions = currentExam.getQuestions();
				for(ItemSession question : questions){
					if(question.getId().equals(idQuestion)){
						if(textAnswer.trim().equalsIgnoreCase("")){
							question.setLearnerFillAnswer("");
						}else{
							question.setLearnerFillAnswer(textAnswer);
						}
					}
				}
			}
			learnerManagementService.updateExamFillAnswer(idExam, idUser, idQuestion, idAnswer, textAnswer);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

}
