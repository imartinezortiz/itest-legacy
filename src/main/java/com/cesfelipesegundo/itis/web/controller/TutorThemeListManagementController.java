package com.cesfelipesegundo.itis.web.controller;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ServletContextAware;

import com.cesfelipesegundo.itis.biz.api.TutorManagementService;
import com.cesfelipesegundo.itis.model.DetailsTheme;
import com.cesfelipesegundo.itis.model.Group;
import com.cesfelipesegundo.itis.model.Subject;
import com.cesfelipesegundo.itis.model.comparators.SubjectOrderComparator;
import com.cesfelipesegundo.itis.web.Constants;


/**
 * It manages the main operations related to the themes of a group.
 * @author chema
 *
 */
public class TutorThemeListManagementController implements ServletContextAware {

	private static final Log log = LogFactory.getLog(TutorThemeListManagementController.class);
	
	/**
	 * Service needed to manage requests from tutor
	 */
    private TutorManagementService tutorManagementService;
    
     /**
     * Theme list being managed by the tutor
     */
    private List<Subject> currentThemeList;
    
    /**
     * Current group being managed by the tutor
     */
    private Group currentGroup;
    
    private ServletContext servletContext;
   
    /* ******** Getters and setters ******** */


	public TutorManagementService getTutorManagementService() {
		return tutorManagementService;
	}

	public void setTutorManagementService(
			TutorManagementService tutorManagementService) {
		this.tutorManagementService = tutorManagementService;
	}
	
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	public ServletContext getServletContext() {
		return servletContext;
	}
	
	public List<Subject> getCurrentThemeList() {
		return currentThemeList;
	}

	public void setCurrentThemeList(List<Subject> currentThemeList) {
		this.currentThemeList = currentThemeList;
	}
	
	
    /* ---------------------- Theme Management (Ajax) ------------------------ */
	
	/**
	 * Includes a new theme into the database. Implements the "add" action of the tutor interface for a new theme
	 * @return List of themes available for the current group.
	 */
	public List<Subject> addTheme (String themeText, String order) {
		boolean repetida = tutorManagementService.isThemeRepeat(themeText,currentGroup.getId());
		// All the attributes come to form a Theme.
		if(!repetida){
			// Transmission of data to the object
			// Theme (Subject)
			Subject theme = new Subject();
			currentThemeList = tutorManagementService.getSubjects(currentGroup);
			// Data
			theme.setSubject(themeText);
			theme.setOrder(currentThemeList.size()+1);
			theme.setGroup(currentGroup);
			
			/* 
			 * Always added as a new theme
			 */		
			theme = tutorManagementService.saveSubject(theme);
		
			// Also added to the list of themes
			currentThemeList.add(theme);
			
			log.debug("Nuevo tema "+order+".- "+themeText);
		}else
			return null;
		
		return currentThemeList;
		
	} // addTheme

		
	
	/**
	 * Updates the order in the theme list, receiving two numbers in String data type
	 * @param receives two string data
	 * @return List of themes. 
	 */
	public  List<Subject> changeOrderTheme(String oldorder, String neworder) {
		int fromorder, toorder;
		fromorder=Integer.valueOf(oldorder);
		toorder=Integer.valueOf(neworder);
		return changeOrderTheme(fromorder, toorder);
	}
	
	
	/**
	 * Updates the order in the theme list, receiving two numbers in integer data type
	 * @param receives two string data
	 * @return List of mmedia. 
	 */
	public  List<Subject> changeOrderTheme(int fromorder, int toorder) {
		// Theme list: assume is not empty
		int i, j;
		boolean up;
		if (fromorder == toorder || toorder<=0 || toorder > currentThemeList.size())
			return currentThemeList;
		else if (fromorder < toorder) {
			i = fromorder;
			j = toorder;
			up = false;
		} else {
			i = toorder;
			j = fromorder;
			up = true;
		}
		log.debug("Moviendo tema de "+fromorder+" a "+toorder);

        // Find the theme to be reordered:
		Iterator<Subject> itersbj = currentThemeList.iterator();
		Subject sbj = null;
		
		while (itersbj.hasNext()) {
			sbj = itersbj.next();
			int order = sbj.getOrder();
			boolean changed = false;
			if ((order == fromorder)) {
				sbj.setOrder(toorder);
				changed = true;
			} else if (!up && order > i && order <=j) {
				sbj.setOrder(order-1);
				changed = true;
			} else if (up && order >= i && order <j) {
				sbj.setOrder(order+1);
				changed = true;
			}
			if (changed) {
				sbj = tutorManagementService.saveSubject(sbj);
			}
		}
		
		// Reordering the list:
		Collections.sort(currentThemeList,new SubjectOrderComparator());
		
		return currentThemeList;
	} // changeOrderTheme

	
	/**
	 * Deletes the theme indicated by the id.
	 * @return List of remaining themes for this group. 
	 */
	public  List<Subject> deleteTheme(String idthm) {
		
        // Find the theme to be deleted:
		Iterator<Subject> iter = currentThemeList.iterator();
		Subject thm = null;
		boolean aFound = false;
		
		while (iter.hasNext() && (!aFound)) {
			thm = iter.next();
			if (thm.getId().equals(Long.valueOf(idthm))) aFound = true;
		}
		
		if (!aFound) {
			// Theme not found
			log.error("- Tema "+idthm+" NO encontrado");
		} else {
			// Change order to avoid empty order slots
			changeOrderTheme(thm.getOrder(),currentThemeList.size());
			// Delete the theme from the database (has to be made first)
			tutorManagementService.deleteSubject(thm);
			// Remove from the list
			currentThemeList.remove(thm);
						
			log.debug("- Borrando tema "+thm.getId());
		}
		
		return currentThemeList;
	} // deleteTheme	
	
	

	/**
	 * Used to modify the subject of an existing theme into the database. Implements the "edit" action of the tutor interface.
	 * @return List of themes available for the current group.
	 */
	public List<Subject> saveTheme (String idTheme, String themeText) {
		
		 // Find the subject to be updated:
		Iterator<Subject> iter = currentThemeList.iterator();
		Subject theme = null;
		boolean aFound = false;
		
		while (iter.hasNext() && (!aFound)) {
			theme = iter.next();
			if(theme.getSubject().equalsIgnoreCase(themeText) && !theme.getId().equals(Long.valueOf(idTheme))){
				//El tema está repetido
				return null;
			}
			if (theme.getId().equals(Long.valueOf(idTheme))) {
				aFound = true;
				break;
			}
		}
		
		if (!aFound) {
			// Theme not found
			log.error("- Tema "+idTheme+" NO encontrado");
		} else {
			// Change the subject:
			theme.setSubject(themeText);
			// Include the group (needed)
			theme.setGroup(currentGroup);
			
			// Save theme on the database:
			theme = tutorManagementService.saveSubject(theme);
			
			log.debug("- Tema "+theme.getId()+" modificado a " + theme.getSubject());
		}
		return currentThemeList;
		
	} // saveTheme

	public Group getCurrentGroup() {
		return currentGroup;
	}

	public void setCurrentGroup(Group currentGroup) {
		this.currentGroup = currentGroup;
	}

	/**
	 * 
	 * @param idTheme
	 * @param idGroup
	 * @return DetailsTheme (idTheme,Number of questions with difficultyLow,Number of questions with difficultyMedium and Number of questions with difficultyHigh)
	 */
	public DetailsTheme showDetailsTheme(Long idTheme, Long questionType, Long idGroup) {
		DetailsTheme detallesTema = new DetailsTheme();
		detallesTema.setId(idTheme);
		detallesTema.setDifficultyLow(tutorManagementService.getQuestionsNumber(idTheme, questionType, idGroup, new Long(Constants.LOW)));
		detallesTema.setDifficultyMedium(tutorManagementService.getQuestionsNumber(idTheme, questionType, idGroup, new Long(Constants.MEDIUM)));
		detallesTema.setDifficultyHigh(tutorManagementService.getQuestionsNumber(idTheme,questionType,  idGroup, new Long(Constants.HIGH)));
		detallesTema.setAnswerDifficultyLow(tutorManagementService.getAnswerMinNumber(idTheme, questionType, idGroup, new Long(Constants.LOW)));
		detallesTema.setAnswerDifficultyMedium(tutorManagementService.getAnswerMinNumber(idTheme, questionType, idGroup, new Long(Constants.MEDIUM)));
		detallesTema.setAnswerDifficultyHigh(tutorManagementService.getAnswerMinNumber(idTheme, questionType, idGroup, new Long(Constants.HIGH)));
		detallesTema.setTotalQuestionLow(tutorManagementService.getTotalQuestion(idTheme, questionType, idGroup, new Long(Constants.LOW)));
		detallesTema.setTotalQuestionMedium(tutorManagementService.getTotalQuestion(idTheme, questionType, idGroup, new Long(Constants.MEDIUM)));
		detallesTema.setTotalQuestionHigh(tutorManagementService.getTotalQuestion(idTheme, questionType, idGroup, new Long(Constants.HIGH)));
		detallesTema.setDifficultyLowFill(tutorManagementService.getTotalQuestion(idTheme, 1L, idGroup, new Long(Constants.LOW)));
		detallesTema.setDifficultyMediumFill(tutorManagementService.getTotalQuestion(idTheme, 1L, idGroup, new Long(Constants.LOW)));
		detallesTema.setDifficultyHighFill(tutorManagementService.getTotalQuestion(idTheme, 1L, idGroup, new Long(Constants.LOW)));
		return detallesTema;
	}
	
}
