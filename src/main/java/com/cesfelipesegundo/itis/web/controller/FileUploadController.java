package com.cesfelipesegundo.itis.web.controller;

import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.*;

import com.cesfelipesegundo.itis.model.Group;
import com.cesfelipesegundo.itis.model.ImportUser;
import com.cesfelipesegundo.itis.model.ReadFile;
import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.model.comparators.UserUserNameComparator;
import com.cesfelipesegundo.itis.web.Constants;
import com.cesfelipesegundo.itis.web.FileUploadBean;
import com.cesfelipesegundo.itis.biz.api.TutorManagementService;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class FileUploadController extends SimpleFormController {
	
	protected static final Log log = LogFactory.getLog(FileUploadController.class);
	
	/**
	 * Service needed to manage requests from tutor
	 */
    private TutorManagementService tutorManagementService;
    
    
    private TutorStudentListManagementController tutorStudentListManagementController;
    
    /* ******** Getters and setters ******** */
	
    public TutorManagementService getTutorManagementService() {
		return tutorManagementService;
	}

	public void setTutorManagementService(
			TutorManagementService tutorManagementService) {
		this.tutorManagementService = tutorManagementService;
	}
	
	public TutorStudentListManagementController getTutorStudentListManagementController() {
		return tutorStudentListManagementController;
	}

	public void setTutorStudentListManagementController(
			TutorStudentListManagementController tutorStudentListManagementController) {
		this.tutorStudentListManagementController = tutorStudentListManagementController;
	}

	protected ModelAndView onSubmit(
        HttpServletRequest request,
        HttpServletResponse response,
        Object command,
        BindException errors) throws ServletException, IOException {

		String procedencia = request.getParameter("importUserFile");
		if(procedencia==null)
			return subirMultimedia(request,response,command,errors);
		else
			return subirArchivoImportarAlumnos(request,response,command,errors);
	}

    private ModelAndView subirArchivoImportarAlumnos(
			HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) {
    	
    	File saveFile;
    	String callback,extension;
    	 // cast the bean
        FileUploadBean bean = (FileUploadBean) command;
        MultipartFile file = bean.getFile();
        
        // get file extension 
        String originalFileName = file.getOriginalFilename(); 
        extension = originalFileName.substring(originalFileName.lastIndexOf(".")+1).toLowerCase();
        if(!extension.equalsIgnoreCase("xls") && !extension.equalsIgnoreCase("csv")){
        	ModelAndView mav = new ModelAndView(getSuccessView());
        	mav.addObject("filename", "");
            mav.addObject("callback", "notAllowedExtendsion");
            return mav;
        }
        
        //compose the path for the uploading file
        String filename = request.getParameter("filename");
		if (filename == null || filename.equals("")) {
	        do {
	        	filename = tutorManagementService.randomFilename();
	        	if (extension.equals("class")) {
	        		/* If the media element is an applet we can't change its filename, so we store it into
	        		 * a folder with a random name	 */
	        		File folder = new File(getServletContext().getRealPath("/")+Constants.MMEDIAPATH+filename);
	        		while (folder.exists()) {
	        			filename = tutorManagementService.randomFilename();
	        			folder = new File(getServletContext().getRealPath("/")+Constants.MMEDIAPATH+filename);
	        		}
	        		folder.mkdir();
	        		filename += "/" + originalFileName;
	        	} else {
	        		filename += "."+extension;
	        	}
	        	saveFile = new File(getServletContext().getRealPath("/")+Constants.MMEDIAPATH+filename);
	        } while (saveFile.exists());
		}
		else {
			saveFile = new File(getServletContext().getRealPath("/")+Constants.MMEDIAPATH+filename);
		}
		log.debug("Iniciando subida del fichero: "+filename);
		ModelAndView mav = new ModelAndView(getSuccessView());
		if (file == null) {
			// hmm, that's strange, the user did not upload anything
			log.error("No hay fichero");
			callback="noFileFound";
		}else{
			if(file.getSize() > 2097152){// Si el tamaño del archivo es mayor que 5MB
				log.error("El archivo ocupa más de 2MB");
				callback = "MMSizeError";
				mav.addObject("callback", callback);
		        if(saveFile!=null)
		        	saveFile.delete();
				return mav;
			}else{
				try {
					FileOutputStream writer = new FileOutputStream(saveFile);
					writer.write(file.getBytes());
					writer.flush();
					writer.close();
					log.debug("Subido");
					callback="succesUpload";
				} catch (Exception e) {
					log.error("Error subiendo fichero: "+e.getMessage());
					callback="errorUploading";
					mav.addObject("callback", callback);
			        if(saveFile!=null)
			        	saveFile.delete();
					return mav;
				}
			}
		}
		
		
		/*
		 * AQUI HAY QUE:
		 * 1º COMPROBAR EL TIPO DE ARCHIVO
		 * 2º OBTENER LA LISTA DE ALUMNOS DEL ARCHIVO
		 * 3º DEVOLVER MEDIANTE EL MODELANDVIEW LA LISTA DE ALUMNOS EN FORMA DE XML
		 * 4º GUARDAR LA LISTA DE USUARIOS EN TUTORSTUDENTLISTMANAGEMENTCONTROLLER
		 * */
		List<ImportUser> users = null;
		List<User> usersList = new ArrayList<User>();
		if(extension.equalsIgnoreCase("csv")){
			log.info("Leyendo archivo con extensión \".csv\"");
			users = ReadFile.readFileCSVUserList(saveFile, log);
		}else if(extension.equalsIgnoreCase("xls")){
			users = ReadFile.readFileXLSUserList(saveFile, log);
			log.info("Leyendo archivo con extensión \".xls\"");
		}else{
			log.error("Not suported extension \""+extension+"\"");
        	mav.addObject("filename", "");
            mav.addObject("callback", "notAllowedExtendsion");
            return mav;
		}
		List<String> xml = new ArrayList<String>();
		String xmlEle = null;
        if(users!=null && users.size()>0){
        	//Ordenamos los usuarios en funcion de su nombre de usuario
        	Collections.sort(users,new UserUserNameComparator());
    		xml.add("<userList>");
    		User auxUser = null;
        	for(ImportUser user : users){
        		auxUser = tutorManagementService.getUserData(user.getUserName());
        		if(auxUser==null){
        			user.setInDB(false);
        		}else{
        			user.setInDB(true);
        		}
        		usersList.add(user);
        		xmlEle=user.getXML();
        		xml.add(xmlEle);
        	}
        	xml.add("</userList>");
        }
        tutorStudentListManagementController.setCurrentStudentList(usersList);
        if(xml.size()>0){
        	mav.addObject("filename",xml);
        }else{
        	mav.addObject("filename",null);
        }
        mav.addObject("callback", callback);
        if(saveFile!=null)
        	log.info("borrando fichero: "+saveFile.delete());
		return mav;
	}

	private ModelAndView subirMultimedia(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) {
    	String callback,extension;
		File saveFile;
		ModelAndView mav = new ModelAndView(getSuccessView());
				
         // cast the bean
        FileUploadBean bean = (FileUploadBean) command;
        MultipartFile file = bean.getFile();
        
        // get file extension 
        String originalFileName = file.getOriginalFilename(); 
        extension = originalFileName.substring(originalFileName.lastIndexOf(".")+1).toLowerCase();
        
        // check if the extension is allowed
        String allowedExtensions=request.getParameter("allowedExtensions");
        if (allowedExtensions != null) {
        	String array[] = allowedExtensions.split(" ");
        	boolean isAllowed = false;
        	for (int i=0;!isAllowed && i<array.length;i++) {
        		if (extension.equals(array[i]))
        			isAllowed = true;
        	}
        	if (!isAllowed) {
        		log.warn("Fichero con extensiÃ³n no permitida");
        		mav.addObject("callback", request.getParameter("errorCallback"));
        		return mav;
        	}
        }

        //compose the path for the uploading file
        String filename = request.getParameter("filename");
		if (filename == null || filename.equals("")) {
	        do {
	        	filename = tutorManagementService.randomFilename();
	        	if (extension.equals("class")) {
	        		/* If the media element is an applet we can't change its filename, so we store it into
	        		 * a folder with a random name	 */
	        		File folder = new File(getServletContext().getRealPath("/")+Constants.MMEDIAPATH+filename);
	        		while (folder.exists()) {
	        			filename = tutorManagementService.randomFilename();
	        			folder = new File(getServletContext().getRealPath("/")+Constants.MMEDIAPATH+filename);
	        		}
	        		folder.mkdir();
	        		filename += "/" + originalFileName;
	        	} else {
	        		filename += "."+extension;
	        	}
	        	saveFile = new File(getServletContext().getRealPath("/")+Constants.MMEDIAPATH+filename);
	        } while (saveFile.exists());
		}
		else {
			saveFile = new File(getServletContext().getRealPath("/")+Constants.MMEDIAPATH+filename);
		}
		log.debug("Iniciando subida del fichero: "+filename);

        if (file == null) {
             // hmm, that's strange, the user did not upload anything
        	log.error("No hay fichero");
        	callback=request.getParameter("errorCallback");
        }
        else {
        	if(file.getSize() > 2097152){// Si el tamaÃ±o del archivo es mayor que 5MB
        		log.error("El archivo ocupa mÃ¡s de 2MB");
        		callback = "MMSizeError";
        	}else{
	        	try {
		        	FileOutputStream writer = new FileOutputStream(saveFile);
		        	writer.write(file.getBytes());
		        	log.debug("Subido");
		        	callback=request.getParameter("successCallback");
	        	} catch (Exception e) {
	        		log.error("Error subiendo fichero: "+e.getMessage());
	        		callback=request.getParameter("errorCallback");
	        	}
        	}
        }

        mav.addObject("filename", filename);
        mav.addObject("callback", callback);
        		
		return mav;
	}

	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
        throws ServletException {
        // to actually be able to convert Multipart instance to byte[]
        // we have to register a custom editor
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
        // now Spring knows how to handle multipart object and convert them
    }
    
}
