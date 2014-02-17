package com.cesfelipesegundo.itis.web.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cesfelipesegundo.itis.biz.CommonManagementService;
import com.cesfelipesegundo.itis.biz.MailSenderManagementServiceImpl;
import com.cesfelipesegundo.itis.biz.api.LearnerManagementService;
import com.cesfelipesegundo.itis.biz.api.TutorManagementService;
import com.cesfelipesegundo.itis.model.QueryGrade;
import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.web.Constants;

import es.itest.engine.course.business.entity.Group;
import es.itest.engine.test.business.entity.TestSessionGrade;

public class ApiController{
	
	private static final Log log = LogFactory.getLog(ApiController.class);
	
	/**
	 * Service needed to manage requests from tutor
	 */
    private TutorManagementService tutorManagementService;
    private LearnerManagementService learnerManagementService;
    private CommonManagementService commonManagementService;
    /**
	 * Service needed to send mails
	 * @uml.property  name="mailSenderManagement"
	 * @uml.associationEnd  
	 */
    private MailSenderManagementServiceImpl mailSenderManagementService;
    
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
	
    
    public CommonManagementService getCommonManagementService() {
		return commonManagementService;
	}

	public void setCommonManagementService(
			CommonManagementService commonManagementService) {
		this.commonManagementService = commonManagementService;
	}

	private static String convertToHex(byte[] data) { 
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) { 
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do { 
                if ((0 <= halfbyte) && (halfbyte <= 9)) 
                    buf.append((char) ('0' + halfbyte));
                else 
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        } 
        return buf.toString();
    } 
 
    public static String MD5(String text) 
    throws NoSuchAlgorithmException, UnsupportedEncodingException  { 
        MessageDigest md;
        md = MessageDigest.getInstance("MD5");
        byte[] md5hash = new byte[32];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        md5hash = md.digest();
        return convertToHex(md5hash);
    }
    
	public TutorManagementService getTutorManagementService() {
		return tutorManagementService;
	}

	public void setTutorManagementService(
			TutorManagementService tutorManagementService) {
		this.tutorManagementService = tutorManagementService;
	}

	
	public LearnerManagementService getLearnerManagementService() {
		return learnerManagementService;
	}

	public void setLearnerManagementService(
			LearnerManagementService learnerManagementService) {
		this.learnerManagementService = learnerManagementService;
	}

	public static Log getLog() {
		return log;
	}
    
	/**
	 * Get a user's name and his password. If the data base gets an user with 
	 * the given user's name and password returns an instance of the user else returns null.
	 * @param userName the current user's name
	 * @param pass the current password from the user
	 * @return the user with the user's name and password given
	 * 
	 * */
	private User checkUser(String userName, String pass){
		User user = tutorManagementService.getUserData(userName);
		if(user!=null){
			try {
				if(user.getPasswd().equalsIgnoreCase(this.MD5(pass)))
					return user;
				else
					return null;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return null;
		}else
			return null;
	}
	
	/*
	 * Method sumary
	 * */
	
	
    public void getGradeList(HttpServletRequest request, HttpServletResponse response){
    	log.info("Comprobando nombre de usuario y contraseña");
    	User user = checkUser(request.getParameter("userName"),request.getParameter("pass"));
    	if(user!=null){
    		if(user.getRole()==Constants.LEARNER){
    			try {
    				log.info("El nombre de usuario o la contraseña recibidos no son correctos");
					response.sendError(403);
				} catch (IOException e) {
					e.printStackTrace();
				}
    			return;
    		}
    		QueryGrade query = new QueryGrade();
    		User learner = null;
    		Group group = null;
    		
    		if(request.getParameter("learner")!=null && !request.getParameter("learner").trim().equalsIgnoreCase("")){
    			learner = tutorManagementService.getUserData(request.getParameter("learner"));
    			/*
    			 * if learner == null, there isn't any learners with the current user's name
    			 * */
    			if(learner==null){
    				try {
    					log.error("El nombre de usuario introducido para el alumno no es correcto");
    					response.sendError(response.SC_BAD_REQUEST);
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    				return;
    			}
    		}
    		if(request.getParameter("group")!=null && !request.getParameter("group").trim().equalsIgnoreCase("")){
    			List<Group> groupList = tutorManagementService.getTeachedGroups(user.getId());
    			for(Group groupElement : groupList){
    				if(groupElement.getId().toString().trim().equalsIgnoreCase(request.getParameter("group").trim())){
    					group = groupElement;
    					break;
    				}
    			}
    		}
			if(group!=null){
				query.setGroup(group.getId());
				log.debug("Nombre del grupo: "+group.getName()+", id del grupo: "+group.getId());
				if(learner!=null){
	    			query.setLearner(learner.getId());
					log.debug("Alumno: "+learner.getSurname()+", "+learner.getName()+", id del alumno: "+learner.getId());
				}
			}else{
				try {
					log.info("nombre del grupo no encontrado");
					response.sendError(response.SC_BAD_REQUEST);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
        	List<TestSessionGrade> gradeList = tutorManagementService.find(query);
        	/* Use array list in order to avoid String limit */
        	List<String> xml = new ArrayList<String>();
        	Integer xmlLength = 0;
        	String xmlElem = null;
        	xmlElem = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        	xmlElem += "<gradeList>\n";
        	xml.add(xmlElem);
        	xmlLength += xmlElem.length();
        		for(int i=0;i<gradeList.size();i++){
        			TestSessionGrade grade = gradeList.get(i);
        			xmlElem="<grade>\n";
        			xmlElem+="<examId>"+grade.getExam().getId()+"</examId>";
        			xmlElem+="<examTittle>"+grade.getExam().getTitle()+"</examTittle>";
        			xmlElem+="<studentUserName>"+grade.getLearner().getUserName()+"</studentUserName>";
        			xmlElem+="<gradeValue>"+grade.getGrade()+"</gradeValue>";
        			xmlElem+="<maxGradeValue>"+grade.getExam().getMaxGrade()+"</maxGradeValue>";
        			xmlElem+="<time>"+grade.getTime()+"</time>";
        			xmlElem+="<maxTime>"+grade.getExam().getDuration()+"</maxTime>";
        			xmlElem+="</grade>\n";
        			xml.add(xmlElem);
                	xmlLength += xmlElem.length();
        		}
        	xmlElem = "</gradeList>\n";
        	xml.add(xmlElem);
        	xmlLength += xmlElem.length();
        	
        	if(gradeList!=null){
        		response.setHeader("Content-disposition", "inline; filename=\"gradeList.xml\""); 
        		response.setContentType("text/xml");
    			response.setContentLength(xmlLength);
    			ServletOutputStream out;
    			try {
    				out = response.getOutputStream();
    				Iterator<String> iter = xml.iterator();
    				while (iter.hasNext()) {
    					out.write(iter.next().getBytes());
    					out.flush();
    				}
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
        	}
        	else
        		try {
        			log.info("No se ha encontrado ninguna calificación");
					response.sendError(response.SC_BAD_REQUEST);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
    	}else{
    		try {
    			log.info("El nombre de usuario o la contraseña no son correctos");
				response.sendError(403);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
    	}
    }
    
    public void retrievePassword(HttpServletRequest request, HttpServletResponse response){
    	String userName = request.getParameter("userName");
    	User user = tutorManagementService.getUserData(userName);
		ServletOutputStream out;
		String xmlElement;
		if(user!=null && user.getEmail()!=null && !user.getEmail().isEmpty()){
			String token = retrieveUserPassword(user);
			if(token!=null){
				try{
					mailSenderManagementService.sendMail(user.getEmail(), "iTest - Recuperación de contraseña", "Pinche sobre el siguiente enlace para cambiar su contraseña: http://www.itest.es"+request.getContextPath()+"/api?method=go2ChangePassword&t="+token);
					xmlElement = "<resultado>true</resultado>";
				}catch(Exception e){
					commonManagementService.deleteTokenUser(token);
					xmlElement = "<resultado>noMailSent</resultado>";
					log.error("No se pudo generar el email de recuperacion de contraseña para el usuario: "+user.getUserName());
				}
			}else{
				xmlElement = "<resultado>false</resultado>";
			}
			
		}else{
			if(user!=null){
				log.info("El usuario "+user.getUserName()+" no tiene email");
				xmlElement = "<resultado>false</resultado>";
			}else{
				log.info("El usuario "+userName+" no existe");
				xmlElement = "<resultado>undefined</resultado>";
			}
		}try{
			response.setHeader("Content-disposition", "inline; filename=\"ajax.xml\""); 
			response.setContentType("text/xml");
			response.setContentLength(xmlElement.length());
			out = response.getOutputStream();
			out.write(xmlElement.getBytes());
			out.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
    }

    /**
     * Recibe un usuario y genera un token para que pueda restaurar su contraseña
     * @param user Usuario que va a restaurar la contraseña
     * @return el token aleatorio para la url de recuperación de la contraseña
     * */
	private String retrieveUserPassword(User user) {
		log.info("Generando token para recuperar mail");
		//Obtenemos la fecha del sistema
		Long now = System.currentTimeMillis();
		long tries = 1;
		boolean repetir;
		String token = null;
		//7200000ml + "now"= 2 horas despues de "now"
		long expireDate = now+7200000;
		Date tokenDate = new Date(now);
		Date expiredDate = new Date(expireDate);
		do{
			log.info(tries+"º intento para generar el token");
			//Generamos el token a partir de la fecha del sistema en MD5
			try {
				token = MD5(now.toString());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			//Habria que comprobar que el token no existe en la BBDD
			boolean isInDB = commonManagementService.checkTokenRetrievePassord(token);
			if(!isInDB){
				repetir = false;
			}else{
				repetir = true;
				log.info("El token "+token+" ya exisite en la BBDD en el intento "+tries+"º");
				tries++;
				now = System.currentTimeMillis();
				expireDate = now+7200000;
				tokenDate = new Date(now);
				expiredDate = new Date(expireDate);
			}
		}while(repetir && tries<=10);
		
		if(!repetir && token!=null){
			log.info("generando url de recuperación: http://www.itest.es/itest/api?method=go2ChangePassword&t="+token);
			commonManagementService.insertToken(user.getId(),token,tokenDate,expiredDate);
		}else{
			log.info("No se ha conseguido generar el token");
		}
		return token;
	}
	
	/**
	 * Change password
	 * @param request
	 * @param response
    */
	public void go2ChangePassword (HttpServletRequest request, HttpServletResponse response) {
		log.info("Redireccionando para cambiar de contraseña");
		String token = request.getParameter("t");
		HttpSession session = request.getSession();
		User user = null;
		if(token!=null)
			try{
				//Si el token no está en la BBDD salta un NullPointerException
				user = commonManagementService.getTokenUser(token);
			}catch(Exception e){
				
			}
		if(user!=null){
			Date fechaCaducidad = commonManagementService.getTokenDateEnd(token);
			Date fechaCambio = commonManagementService.getTokenDateChange(token);
			log.info("El usuario "+user.getUserName()+" está cambiando su contraseña");
			String jsp;
			if(fechaCambio==null && fechaCaducidad.after(new Date(System.currentTimeMillis()))){
				jsp = "/changePassword.jsp";
				log.info("redireccionando a la página para restaurar la contraseña");
				//Aqui hay que guardar al usuario y el token en la sesion
				session.setAttribute(Constants.USER, user);
				session.setAttribute("TOKEN", token);
			}else{
				if(fechaCambio!=null){
					log.info("Se ha intentado restablecer una contraseña ya restablecida con el token "+token);
					request.setAttribute("error",true);
					request.setAttribute("message","transactionError");
				}else{
					log.info("Fecha de caducidad sobrepasada para el token "+token);
					request.setAttribute("message","labelUrlExpired");
					request.setAttribute("error",true);
				}
				jsp = "/changePassword_error.jsp";
			}
			RequestDispatcher rd = session.getServletContext().getRequestDispatcher(jsp);
			try {
				rd.forward(request, response);
			}catch (Exception e) {
				log.error("Imposible redireccionar la página");
				e.printStackTrace();
			}
		}else{
			log.info("La url recibida no es válida");
			try {
				request.setAttribute("message","transactionError");
				request.setAttribute("error",true);
				RequestDispatcher rd = session.getServletContext().getRequestDispatcher("/changePassword_error.jsp");
				rd.forward(request, response);
			}catch (Exception e) {
				log.error("Imposible redireccionar la página");
			}
		}
		
	}
	
	public void changePassword(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		String newPasswd1 = request.getParameter("newPasswd1");
		String newPasswd2 = request.getParameter("newPasswd2");
		User user = (User) session.getAttribute(Constants.USER);
		String token = (String) session.getAttribute("TOKEN");
		if(user!=null && token !=null){
			log.info("Cambiando contraseña para el usuario "+user.getUserName());
			ServletOutputStream out;
			String xmlElement;
			if(newPasswd1.equalsIgnoreCase(newPasswd2)){
				user = commonManagementService.updatePassword(user, newPasswd1);
				try{
					if(user.getPasswd().equalsIgnoreCase(MD5(newPasswd1))){
						//Password restaurada
						log.info("Password restaurada para el usuario "+user.getUserName());
						xmlElement = "<resultado>0</resultado>";
						commonManagementService.updateTokenDateChange(token,new Date(System.currentTimeMillis()));
					}else{
						//Password no restaurada
						log.info("Password no restaurada para el usuario "+user.getUserName());
						xmlElement = "<resultado>1</resultado>";
					}
				}catch(Exception e){
					e.printStackTrace();
					//Password no restaurada
					log.info("Password no restaurada para el usuario "+user.getUserName());
					xmlElement = "<resultado>1</resultado>";
				}
			}else{
				//Passwords incorrectos, no son iguales
				log.info("Passwords incorrectos, no son iguales");
				xmlElement = "<resultado>2</resultado>";
			}try{
				response.setHeader("Content-disposition", "inline; filename=\"ajax.xml\""); 
				response.setContentType("text/xml");
				response.setContentLength(xmlElement.length());
				out = response.getOutputStream();
				out.write(xmlElement.getBytes());
				out.flush();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
    }
}
