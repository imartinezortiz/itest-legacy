package com.cesfelipesegundo.itis.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import com.cesfelipesegundo.itis.biz.api.LearnerManagementService;
import com.cesfelipesegundo.itis.biz.api.TutorManagementService;
import com.cesfelipesegundo.itis.model.ExamForReview;
import com.cesfelipesegundo.itis.model.Group;
import com.cesfelipesegundo.itis.model.MediaElem;
import com.cesfelipesegundo.itis.model.Query;
import com.cesfelipesegundo.itis.model.Subject;
import com.cesfelipesegundo.itis.model.TemplateExamAnswer;
import com.cesfelipesegundo.itis.model.TemplateExamQuestion;
import com.cesfelipesegundo.itis.model.TemplateExamSubject;
import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.web.Constants;
import com.lowagie.text.DocumentException;

/**
 * It manages the main operations related to the new or edited question and
 * their related answers.
 * @author chema
 *
 */
public class TutorQuestionManagementController implements ServletContextAware {

	private static final Log log = LogFactory.getLog(TutorQuestionManagementController.class);
	
	/**
	 * Service needed to manage requests from tutor
	 */
    private TutorManagementService tutorManagementService;
    
    /**
     * Service needed to manage some requests from tutor (review exams, for example)
     */
    private LearnerManagementService learnerManagementService;
    
    /**
     * Question being managed (added or edited) by the tutor
     */
    private TemplateExamQuestion currentQuestion;

    /**
     * Answer being managed (added or edited) by the tutor
     */
    private TemplateExamAnswer currentAnswer;
    
    /**
     * Group being managed
     */
    private Group currentGroup;
    
    private ServletContext servletContext;
   
    /* ******** Getters and setters ******** */

	public TemplateExamQuestion getCurrentQuestion() {
		return currentQuestion;
	}


	public void setCurrentQuestion(TemplateExamQuestion currentQuestion) {
		this.currentQuestion = currentQuestion;
	}


	public TutorManagementService getTutorManagementService() {
		return tutorManagementService;
	}


	public void setTutorManagementService(
			TutorManagementService tutorManagementService) {
		this.tutorManagementService = tutorManagementService;
	}
	
	public LearnerManagementService getLearnerManagementService() {
		return this.learnerManagementService;
	}


	public void setLearnerManagementService(
			LearnerManagementService learnerManagementService) {
		this.learnerManagementService = learnerManagementService;
	}

	public TemplateExamAnswer getCurrentAnswer() {
		return currentAnswer;
	}


	public void setCurrentAnswer(TemplateExamAnswer currentAnswer) {
		this.currentAnswer = currentAnswer;
	}
	
	public Group getCurrentGroup() {
		return currentGroup;
	}


	public void setCurrentGroup(Group currentGroup) {
		this.currentGroup = currentGroup;
	}


	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	
    /* ---------------------- Question Management (Ajax) ------------------------ */
	
	/**
	 * Saves a new question to the database. Implements the "save" action of the tutor interface for a new question
	 * @return Question id on a successful addition operation
	 */
	public Long saveQuestion (String idgroup, String idtheme, String title, String difficulty, String visibility, String enunc, String comment,int questionType) {
		boolean error = false;
		if (currentQuestion == null) {
			currentQuestion = new TemplateExamQuestion();
			log.debug("Se ha intentado salvar una pregunta sin estar establecida en la sesión");
		}
		//check if the current group is null
		if(currentGroup==null){
			log.debug("Se ha intentado salvar una pregunta sin estar en un grupo");
			//if current group is null we need to know the group to assign a group to the question
			long groupId = -1;
			try{
				groupId = Long.parseLong(idgroup);
				currentGroup = tutorManagementService.getGroup(groupId);
			}catch(Exception e){
				error = true;
				return new Long(-1);
			}
			//groupId must be >= 0
			if(groupId <= -1)
				return groupId;
		}
		// All the attributes come to form a question.
		
		// Transmission of data to the object
		// Group
		currentQuestion.setGroup(currentGroup);
		
		// Theme
		Subject subj = new Subject();
		subj.setId(Long.valueOf(idtheme));
		currentQuestion.setSubject(subj);
		
		// If the question is new, never was used in an exam, in other case, maintain the value
		if (currentQuestion.getId() == null) currentQuestion.setUsedInExam(false);
		
		// Rest of the data
		currentQuestion.setTitle(title);
		if (enunc == null)
			currentQuestion.setText("");
		else
			currentQuestion.setText(enunc);
		currentQuestion.setComment(comment);
		currentQuestion.setDifficulty(Integer.valueOf(difficulty));
		currentQuestion.setVisibility(Integer.valueOf(visibility));
		currentQuestion.setActive(Constants.YES);
		currentQuestion.setType(questionType);
		
		// Answers: create object only if there was no list
		if (currentQuestion.getAnswers() == null) currentQuestion.setAnswers(new ArrayList<TemplateExamAnswer>());
		
		/* 
		 * If the question is new, the service must insert all the data into the DB.
		 * if the question is not new, the service must update the data.
		 * 
		 * FIX: currentQuestion is updated by the service
		 */
		tutorManagementService.saveQuestion(currentQuestion);
		
		log.debug("- Salvando pregunta "+currentQuestion.getId()+": "+currentQuestion.getText());
		
		return currentQuestion.getId();
	} // saveQuestion


	
	// ----------------------------------- Answers -----------------------------------------------------------

	
	/**
	 * Gets the list of answers for the current question
	 * @return List of answers from the current question, including mmedia info
	 */
	public  List<TemplateExamAnswer> getCurrentQuestionAnswers () {
		if (currentQuestion.getAnswers()  == null) {
			return new ArrayList<TemplateExamAnswer>();
		} else {
			return currentQuestion.getAnswers();
		}
	} // getCurrentQuestionAnswers
	
	
	/**
	 * Resets the current answer object.
	 * @return void
	 */
	public void newAnswer () {
		
		setCurrentAnswer(new TemplateExamAnswer());
		
		return;
		
	} // newAnswer	
	
	
	
	/**
	 * Saves the answer to the database. Implements the "save" action of the answer.
	 * @return List of answers to be re-displayed
	 */
	public List<TemplateExamAnswer> saveAnswer (String texto, String solution, String isnewanswer) {
		
		if (currentQuestion == null) {
			currentQuestion = new TemplateExamQuestion();
			log.debug("Se ha intentado salvar una respuesta sin estar establecida la pregunta en la sesión");
		}
		if (currentAnswer == null) {
			currentAnswer = new TemplateExamAnswer();
			log.debug("Se ha intentado salvar una respuesta sin estar establecida en la sesión");
		}
		
		// Aux variables to update the value of the solution:
		boolean updateSolutionValues = false;
		int solutionValue = 0;
		
		// Saves current answer:
		currentAnswer.setActive(Constants.YES);
		if(texto==null)
			texto = "";
		currentAnswer.setText(texto);
		
		// If the answer is new, never was used in an exam, in other case, maintain the value
		if (currentAnswer.getId() == null) currentAnswer.setUsedInExam(false);
		
		// The values of solution depend on the older values:
		if (!Integer.valueOf(solution).equals(currentAnswer.getSolution())) {
			// The old and new values of solution are different
			if (Short.valueOf(solution).equals(Constants.YES)) {
				// This means that the answer WAS NOT a solution and now it IS
				currentAnswer.setSolution(Constants.YES);
				// The question info must be updated
			    currentQuestion.setNumCorrectAnswers(currentQuestion.getNumCorrectAnswers()+1);
			} else {
				// This means that the answer WAS a solution and now IS NOT
				currentAnswer.setSolution(Constants.NO);
				// The question info must be updated
			    currentQuestion.setNumCorrectAnswers(currentQuestion.getNumCorrectAnswers()-1);
			}
			
			// It is necessary to update the solution values and to save the question
		    // Integer division to correct the solution values
			if (currentQuestion.getNumCorrectAnswers() > 0) {
		        solutionValue = 100/currentQuestion.getNumCorrectAnswers();
		        updateSolutionValues = true;
			} else {
				solutionValue = 0;
			}
			
			// This has to be done here, after the obtention of solutionValue:
			if (Short.valueOf(solution).equals(Constants.YES))
				currentAnswer.setValue(solutionValue);
		    else
		    	currentAnswer.setValue(0);

			// Save question
		    tutorManagementService.saveQuestion(currentQuestion);
		}
			
		/* 
		 * If the answer is new, the service must insert all the data into the DB.
		 * if the answer is not new, the service must update the data.
		 * 
		 * FIX: answer is updated by the service to reflect the dabaseid
		 */
		currentAnswer.setQuestion(currentQuestion);
		tutorManagementService.saveAnswer(currentAnswer);
			
		// The answer can only be saved when the question was saved before
		List<TemplateExamAnswer> answers = currentQuestion.getAnswers();
		
		// It is important to update the values of the correct answers...
		if (updateSolutionValues && answers!=null) {
			Iterator<TemplateExamAnswer> iterAnsw = answers.iterator();
			int pendSols = currentQuestion.getNumCorrectAnswers();
			TemplateExamAnswer answ = null;
			
			while (iterAnsw.hasNext() && (pendSols > 0)) {
				answ = iterAnsw.next();
				if (answ!=null && answ.getSolution() == Constants.YES) {
					// Update solution value:
					answ.setValue(solutionValue);
					tutorManagementService.saveAnswer(answ);
				}
			}
		} // updateSolutionValues
		
		// Adds the new answer to currentQuestion only if is new, also creating the mmedia list
		if (isnewanswer.equalsIgnoreCase("true")) {
			currentAnswer.setMmedia(new ArrayList<MediaElem>());
		    answers.add(currentAnswer);
		}
		
		log.debug("- Salvando respuesta: "+currentAnswer.getText()+" "+isnewanswer);
		
		return answers;
		
	} // saveAnswer
	
	
	/**
	 * Deletes the answer from the database. Implements the "delete" action of the answer.
	 * Deletes also the mmedia files associated to the answer.
	 * @return List of answers to be re-displayed
	 */
	public List<TemplateExamAnswer> deleteAnswer (String idresp) {

		// Aux variable to update the value of the remaining solutions:
		boolean updateSolutionValues = false;
		
		// The answer can only be deleted when the question was saved before
		List<TemplateExamAnswer> answers = currentQuestion.getAnswers();

        // Find the answer to be deleted:
		Iterator<TemplateExamAnswer> iterAnsw = answers.iterator();
		TemplateExamAnswer answ = null;
		boolean aFound = false;
		int pendSols = 0;
		
		while (iterAnsw.hasNext() && (!aFound)) {
			answ = iterAnsw.next();
			if (answ.getId().equals(Long.valueOf(idresp))) {
				aFound = true;
				// If the answer is a solution we have to update the solution values and the number of correct answers from the question
				if (answ.getSolution() == Constants.YES) {
					pendSols = currentQuestion.getNumCorrectAnswers()-1;
					updateSolutionValues = true;
				}
			}
		}
		
		if (!aFound) {
			// Answer not found
			log.debug("- Respuesta "+idresp+" NO encontrada");
		} else {
			
			// Delete the answer from the database (has to be made first)
			tutorManagementService.deleteAnswer(answ);
			
			// Delete the mmedia files:
			// this is done in the service
/*			
			List<MediaElem> mmlist = answ.getMmedia();
			Iterator<MediaElem> iterMM = mmlist.iterator();
			while (iterMM.hasNext()) {
				MediaElem mm = iterMM.next();
				deleteMmediaFile(mm.getPath());
			}
*/
			
			// Removed from the list
			answers.remove(answ);
			
			// If the answer is a solution, the question info is updated:
			if (updateSolutionValues) {
				currentQuestion.setNumCorrectAnswers(pendSols);
			    tutorManagementService.saveQuestion(currentQuestion);
			    // Only updating if there are any pending solutions:
				if (pendSols > 0) {
					// Integer division to correct the solution values
					int solutionValue = 100/currentQuestion.getNumCorrectAnswers();
					TemplateExamAnswer answAux = null;
					// Reset the iterator in order to begin from the first answer
					iterAnsw = answers.iterator();
					while (iterAnsw.hasNext() && (pendSols > 0)) {
						answAux = iterAnsw.next();
						if (answAux.getSolution() == Constants.YES) {
							// Update solution value:
							answAux.setValue(solutionValue);
							tutorManagementService.saveAnswer(answAux);
						}
					} // while
				}
			} // updateSolutionValues
			
			log.debug("- Borrando respuesta "+answ.getId()+": "+answ.getText());
		}
		
		return answers;
		
	} // deleteAnswer
	
	
	/**
	 * Loads the answer from the list and changes the currentAnswer.
	 * @return actual (selected) currentAnswer
	 */
	public TemplateExamAnswer editAnswer (String idresp) {
		
        // Find the answer to be edited:
		Iterator<TemplateExamAnswer> iterAnsw = currentQuestion.getAnswers().iterator();
		TemplateExamAnswer answ = null;
		boolean aFound = false;
		
		while (iterAnsw.hasNext() && (!aFound)) {
			answ = iterAnsw.next();
			if (answ.getId().equals(Long.valueOf(idresp))) {
				aFound = true;
			}
		}
		
		if (!aFound) {
			// Answer not found
			log.debug("- Respuesta "+idresp+" NO encontrada");
			setCurrentAnswer(new TemplateExamAnswer());
		} else {
			setCurrentAnswer(answ);
		}
		
		return currentAnswer;
		
	} // editAnswer	
	
	
	// ----------------------------------- Multimedia files ------------------------------------
	
	/**
	 * Adds the mmedia to the database for a question. Implements the "add" action of the mmedia.
	 * @return mmedia list to be displayed by the callback function
	 */
	public List<MediaElem> addQuestionMmedia (String fileName, String uploadedFileName) {
		Integer order = 1;
		
		// If no question is selected is impossible to attach a file to it
		if (currentQuestion == null || currentQuestion.getId() == null) {
			// first we delete the file just uploaded
			// after waiting some time, because otherwise the OS doesn´t allow to delete the file
			int waits = 0;
			do {
				try {
					Thread.sleep(200);
					waits++;
				} catch (InterruptedException e) {
				}
				log.debug("intento "+waits+" de borrar "+uploadedFileName+" (recién subido)");
			} while (!tutorManagementService.deleteMmediaFile(uploadedFileName) && waits < 20);
			return null;
		}
		
		if ((currentQuestion.getMmedia() != null) && (currentQuestion.getMmedia().size() > 0)) {
			// The order comes from the mmedia list:
			order = currentQuestion.getMmedia().get(currentQuestion.getMmedia().size()-1).getOrder() + 1;
		} else {
			// There were no mmedia: new list
			currentQuestion.setMmedia(new ArrayList<MediaElem>());			
		}
		
		MediaElem mmedia = createMediaElem(fileName,uploadedFileName,order);
		// If there was any problem, the mmedia is not saved
		if (mmedia != null) {
			//attach the media elem to the question into de DB
			tutorManagementService.saveMediaElemToQuestion(currentQuestion,mmedia);
			log.debug("Multimedia "+fileName+" asociada a pregunta "+currentQuestion.getId());
			// Adds the mmedia to the question list:
			currentQuestion.getMmedia().add(mmedia);			
		}
		return currentQuestion.getMmedia();
		
	} // addQuestionMmedia
	
	
	/**
	 * Adds the comment mmedia to the database for a question. Implements the "add" action of the mmedia.
	 * @return mmedia list to be displayed by the callback function
	 */
	public List<MediaElem> addCommentMmedia (String fileName, String uploadedFileName) {
		Integer order = 1;
		
		// If no question is selected is impossible to attach a file to it
		if (currentQuestion == null || currentQuestion.getId() == null) {
			// first we delete the file just uploaded
			// after waiting some time, because otherwise the OS doesn´t allow to delete the file
			int waits = 0;
			do {
				try {
					Thread.sleep(200);
					waits++;
				} catch (InterruptedException e) {
				}
				log.debug("intento "+waits+" de borrar "+uploadedFileName+" (recién subido)");
			} while (!tutorManagementService.deleteMmediaFile(uploadedFileName) && waits < 20);
			return null;
		}
		
		if ((currentQuestion.getMmediaComment() != null) && (currentQuestion.getMmediaComment().size() > 0)) {
			// The order comes from the mmedia list:
			order = currentQuestion.getMmediaComment().get(currentQuestion.getMmediaComment().size()-1).getOrder() + 1;
		} else {
			// There were no mmedia: new list
			currentQuestion.setMmediaComment(new ArrayList<MediaElem>());			
		}
		
		MediaElem mmedia = createMediaElem(fileName,uploadedFileName,order);
		// If there was any problem, the mmedia is not saved
		if (mmedia != null) {
			//attach the media elem to the question into de DB
			tutorManagementService.saveMediaElemToComment(currentQuestion,mmedia);
			log.debug("Multimedia "+fileName+" asociada a pregunta "+currentQuestion.getId());
			// Adds the mmedia to the question list:
			currentQuestion.getMmediaComment().add(mmedia);			
		}
		return currentQuestion.getMmediaComment();
		
	} // addCommentMmedia

	
	
	/**
	 * Deletes the question mmedia indicated by the id.
	 * @return List of remaining mmedia. 
	 */
	public  List<MediaElem> deleteQuestionMmedia(String idqmm) {
		// Mmedia list: assume is not empty
		List<MediaElem> qmmedias = currentQuestion.getMmedia();

        // Find the mmedia to be deleted:
		Iterator<MediaElem> iterQmm = qmmedias.iterator();
		MediaElem qmm = null;
		boolean aFound = false;
		
		while (iterQmm.hasNext() && (!aFound)) {
			qmm = iterQmm.next();
			if (qmm.getId().equals(Long.valueOf(idqmm))) aFound = true;
		}
		
		if (!aFound) {
			// Mmedia not found
			log.error("- Mmedia "+idqmm+" NO encontrado");
		} else {
			// Change mmedia order to avoid empty order slots
			changeOrderQuestionMmedia(qmm.getOrder(),qmmedias.size());
			// Delete the mmedia from the database (has to be made first)
			tutorManagementService.deleteMediaElemFromQuestion(currentQuestion, qmm);
			// Delete the file: this is done in the service
//			deleteMmediaFile(qmm.getPath());
			// Remove from the list
			qmmedias.remove(qmm);
						
			log.debug("- Borrando mmedia de pregunta "+qmm.getId());
		}
		
		return qmmedias;
	} // deleteQuestionMmedia
	
	/**
	 * Deletes the question mmedia indicated by the id.
	 * @return List of remaining mmedia. 
	 */
	public  List<MediaElem> deleteCommentMmedia(String idqmm) {
		// Mmedia list: assume is not empty
		List<MediaElem> cmmedias = currentQuestion.getMmediaComment();

        // Find the mmedia to be deleted:
		Iterator<MediaElem> iterQmm = cmmedias.iterator();
		MediaElem qmm = null;
		boolean aFound = false;
		
		while (iterQmm.hasNext() && (!aFound)) {
			qmm = iterQmm.next();
			if (qmm.getId().equals(Long.valueOf(idqmm))) aFound = true;
		}
		
		if (!aFound) {
			// Mmedia not found
			log.error("- Mmedia "+idqmm+" NO encontrado");
		} else {
			// Change mmedia order to avoid empty order slots
			//changeOrderQuestionMmedia(qmm.getOrder(),cmmedias.size());
			// Delete the mmedia from the database (has to be made first)
			tutorManagementService.deleteMediaElemFromComment(currentQuestion, qmm);
			// Delete the file: this is done in the service
//			deleteMmediaFile(qmm.getPath());
			// Remove from the list
			cmmedias.remove(qmm);
						
			log.debug("- Borrando mmedia de pregunta "+qmm.getId());
		}
		
		return cmmedias;
	} // deleteQuestionMmedia
	
	
	/**
	 * Updates the order in the comment mmedia list
	 * @return List of mmedia. 
	 */
	public  List<MediaElem> changeOrderCommentMmedia(String oldorder, String neworder) {
		int fromorder, toorder;
		fromorder=Integer.valueOf(oldorder);
		toorder=Integer.valueOf(neworder);
		return changeOrderCommentMmedia(fromorder, toorder);
	}
	
	/**
	 * Updates the order in the comment mmedia list
	 * @return List of mmedia. 
	 */
	public  List<MediaElem> changeOrderCommentMmedia(int fromorder, int toorder) {
		// Mmedia list: assume is not empty
		List<MediaElem> qmmedias = currentQuestion.getMmediaComment();
		
		int i, j;
		boolean up;
		if (fromorder == toorder || toorder<=0 || toorder > qmmedias.size())
			return qmmedias;
		else if (fromorder < toorder) {
			i = fromorder;
			j = toorder;
			up = false;
		} else {
			i = toorder;
			j = fromorder;
			up = true;
		}
		log.debug("Moviendo multimedia de "+fromorder+" a "+toorder);

        // Find the mmedia to be reordered:
		Iterator<MediaElem> iterQmm = qmmedias.iterator();
		MediaElem qmm = null;
		
		while (iterQmm.hasNext()) {
			qmm = iterQmm.next();
			int order = qmm.getOrder();
			boolean changed = false;
			if ((order == fromorder)) {
				qmm.setOrder(toorder);
				changed = true;
			} else if (!up && order > i && order <=j) {
				qmm.setOrder(order-1);
				changed = true;
			} else if (up && order >= i && order <j) {
				qmm.setOrder(order+1);
				changed = true;
			}
			if (changed) {
				tutorManagementService.saveMediaElemToComment(currentQuestion, qmm);
			}
		}
		
		// Retrieve media list ordered
		qmmedias = currentQuestion.getMmediaComment();
		
		return qmmedias;
	} // changeOrderQuestionMmedia
	
	/**
	 * Updates the order in the question mmedia list
	 * @return List of mmedia. 
	 */
	public  List<MediaElem> changeOrderQuestionMmedia(String oldorder, String neworder) {
		int fromorder, toorder;
		fromorder=Integer.valueOf(oldorder);
		toorder=Integer.valueOf(neworder);
		return changeOrderQuestionMmedia(fromorder, toorder);
	}
	
	
	/**
	 * Updates the order in the question mmedia list
	 * @return List of mmedia. 
	 */
	public  List<MediaElem> changeOrderQuestionMmedia(int fromorder, int toorder) {
		// Mmedia list: assume is not empty
		List<MediaElem> qmmedias = currentQuestion.getMmedia();
		
		int i, j;
		boolean up;
		if (fromorder == toorder || toorder<=0 || toorder > qmmedias.size())
			return qmmedias;
		else if (fromorder < toorder) {
			i = fromorder;
			j = toorder;
			up = false;
		} else {
			i = toorder;
			j = fromorder;
			up = true;
		}
		log.debug("Moviendo multimedia de "+fromorder+" a "+toorder);

        // Find the mmedia to be reordered:
		Iterator<MediaElem> iterQmm = qmmedias.iterator();
		MediaElem qmm = null;
		
		while (iterQmm.hasNext()) {
			qmm = iterQmm.next();
			int order = qmm.getOrder();
			boolean changed = false;
			if ((order == fromorder)) {
				qmm.setOrder(toorder);
				changed = true;
			} else if (!up && order > i && order <=j) {
				qmm.setOrder(order-1);
				changed = true;
			} else if (up && order >= i && order <j) {
				qmm.setOrder(order+1);
				changed = true;
			}
			if (changed) {
				tutorManagementService.saveMediaElemToQuestion(currentQuestion, qmm);
			}
		}
		
		// Retrieve media list ordered
		qmmedias = currentQuestion.getMmedia();
		
		return qmmedias;
	} // changeOrderQuestionMmedia
	
	
	/**
	 * Adds the mmedia to the database for an answer. Implements the "save" action of the mmedia.
	 * @return mmedia list to be displayed by the callback function
	 */
	public  List<MediaElem> addAnswerMmedia (String fileName, String uploadedFileName) {
		Integer order = 1;
		
		// If no answer is selected is impossible to attach a file to it
		if (currentAnswer == null || currentAnswer.getId() == null) {
			// first we delete the file just uploaded
			// after waiting some time, because otherwise the OS doesn´t allow to delete the file
			int waits = 0;
			do {
				try {
					Thread.sleep(200);
					waits++;
				} catch (InterruptedException e) {
				}
				log.debug("intento "+waits+" de borrar "+uploadedFileName+" (recién subido)");
			} while (!tutorManagementService.deleteMmediaFile(uploadedFileName) && waits < 20);
			return null;
		}

		
		if ((currentAnswer.getMmedia() != null) && (currentAnswer.getMmedia().size() > 0)) {
			// The order comes from the mmedia list:
			order = currentAnswer.getMmedia().get(currentAnswer.getMmedia().size()-1).getOrder() + 1;
		} else {
			// There were no mmedia: new list
			currentAnswer.setMmedia(new ArrayList<MediaElem>());			
		}
				
		MediaElem mmedia = createMediaElem(fileName,uploadedFileName,order);
		// If there was any problem, the mmedia is not saved
		if (mmedia != null) {
			//attach the media elem to the answer into de DB
			tutorManagementService.saveMediaElemToAnswer(currentAnswer,mmedia);
			log.debug("Multimedia "+fileName+" asociada a respuesta "+currentAnswer.getId());
			// Adds the mmedia to the answer list:
			currentAnswer.getMmedia().add(mmedia);			
		}
		return currentAnswer.getMmedia();
		
	} // addAnswerMmedia
	
	
	
	/**
	 * Deletes the answer mmedia indicated by the id.
	 * @return List of remaining mmedia. 
	 */
	public  List<MediaElem> deleteAnswerMmedia(String idamm) {
		// Mmedia list: assume is not empty
		List<MediaElem> ammedias = currentAnswer.getMmedia();

        // Find the mmedia to be deleted:
		Iterator<MediaElem> iterQmm = ammedias.iterator();
		MediaElem amm = null;
		boolean aFound = false;
		
		while (iterQmm.hasNext() && (!aFound)) {
			amm = iterQmm.next();
			if (amm.getId().equals(Long.valueOf(idamm))) aFound = true;
		}
		
		if (!aFound) {
			// Mmedia not found
			log.error("- Mmedia "+idamm+" NO encontrado");
		} else {
			// Change mmedia order to avoid empty order slots
			changeOrderAnswerMmedia(amm.getOrder(),ammedias.size());
			// Delete the mmedia from the database (has to be made first)
			tutorManagementService.deleteMediaElemFromAnswer(currentAnswer, amm);
			// Delete the file: this is done in the service
//			deleteMmediaFile(amm.getPath());
			// Remove from the list
			ammedias.remove(amm);
						
			log.debug("- Borrando mmedia de respuesta "+amm.getId());
		}
		
		return ammedias;
	} // deleteAnswerMmedia
	
	
	/**
	 * Updates the order in the answer mmedia list
	 * @return List of mmedia. 
	 */
	public  List<MediaElem> changeOrderAnswerMmedia(String oldorder, String neworder) {
		int fromorder, toorder;
		fromorder=Integer.valueOf(oldorder);
		toorder=Integer.valueOf(neworder);
		return changeOrderAnswerMmedia(fromorder, toorder);
	}
	
	
	/**
	 * Updates the order in the answer mmedia list
	 * @return List of mmedia. 
	 */
	public  List<MediaElem> changeOrderAnswerMmedia(int fromorder, int toorder) {
		// Mmedia list: assume is not empty
		List<MediaElem> ammedias = currentAnswer.getMmedia();
		
		int i, j;
		boolean up;
		if (fromorder == toorder || toorder<=0 || toorder > ammedias.size())
			return ammedias;
		else if (fromorder < toorder) {
			i = fromorder;
			j = toorder;
			up = false;
		} else {
			i = toorder;
			j = fromorder;
			up = true;
		}
		log.debug("Moviendo multimedia de "+fromorder+" a "+toorder);

        // Find the mmedia to be reordered:
		Iterator<MediaElem> iterQmm = ammedias.iterator();
		MediaElem qmm = null;
		
		while (iterQmm.hasNext()) {
			qmm = iterQmm.next();
			int order = qmm.getOrder();
			boolean changed = false;
			if ((order == fromorder)) {
				qmm.setOrder(toorder);
				changed = true;
			} else if (!up && order > i && order <=j) {
				qmm.setOrder(order-1);
				changed = true;
			} else if (up && order >= i && order <j) {
				qmm.setOrder(order+1);
				changed = true;
			}
			if (changed) {
				tutorManagementService.saveMediaElemToAnswer(currentAnswer, qmm);
			}
		}
		
		//retrieve media list ordered
		ammedias = currentAnswer.getMmedia();

		return ammedias;
	} // changeOrderAnswerMmedia
	
	
	/**
	 * Gets the multimedia list for the currentAnswer
	 */
	public  List<MediaElem> getCurrentAnswerMmedia () {
		if (currentAnswer.getMmedia() == null) {
			return new ArrayList<MediaElem>();
		} else {
			return currentAnswer.getMmedia();
		}
	} // getCurrentAnswerMmedia
	
	/**
	 * Gets a media elem attached to the question by its id
	 */
	public  MediaElem getQuestionMmediaByID (String id) {
		if (currentQuestion.getMmedia() == null) {
			return null;
		}
		else {
			Iterator<MediaElem> it = currentQuestion.getMmedia().iterator();
			while (it.hasNext()) {
				MediaElem elem = it.next();
				if (elem.getId().toString().equals(id))
					return elem;
			}
			return null;
		}
	} // getQuestionMmediaByID
	
	/**
	 * Gets a media elem attached to the current answer by its id
	 */
	public  MediaElem getCurrentAnswerMmediaByID (String id) {
		if (currentAnswer.getMmedia() == null) {
			return null;
		}
		else {
			Iterator<MediaElem> it = currentAnswer.getMmedia().iterator();
			while (it.hasNext()) {
				MediaElem elem = it.next();
				if (elem.getId().toString().equals(id))
					return elem;
			}
			return null;
		}
	} // getCurrentAnswerMmediaByID
	
	/**
	 * Sets the size for a multimedia item
	 */
	public void setQuestionMmediaSize(String idqmm, String width, String height) {
		// Mmedia list: assume is not empty
		List<MediaElem> qmmedias = currentQuestion.getMmedia();

        // Find the mmedia to be edited:
		Iterator<MediaElem> iterQmm = qmmedias.iterator();
		MediaElem qmm = null;
		boolean aFound = false;
		
		while (iterQmm.hasNext() && (!aFound)) {
			qmm = iterQmm.next();
			if (qmm.getId().equals(Long.valueOf(idqmm))) aFound = true;
		}
		
		if (!aFound) {
			// Mmedia not found
			log.error("- Mmedia "+idqmm+" NO encontrado");
		} else {
			qmm.setWidth(width);
			qmm.setHeight(height);
			tutorManagementService.saveMediaElemToQuestion(currentQuestion, qmm);
									
			log.debug("- Editando mmedia de pregunta "+qmm.getId());
		}
	}
	
	/**
	 * Creates a MediaElem, given its data
	 */
	private MediaElem createMediaElem(String fileName, String uploadedFileName, Integer order) {
		String extension = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
		
		// Create a new media elem:
		MediaElem mmedia = new MediaElem();
		// A name is normalized if it is not a YouTube video.
		if (fileName.substring(0,3).equals("YT:")) {
			extension = "YTvideo";
			// Remove YT prefix 
			fileName = fileName.substring(3,fileName.length());
		} else {
			fileName = normalizeFileName(fileName);
		}
		mmedia.setName(fileName);
		mmedia.setOrder(order);
		mmedia.setPath(uploadedFileName);
		if ( extension.equals("jpg") || extension.equals("gif") || extension.equals("png") ||
			 extension.equals("bmp") || extension.equals("pcx") || extension.equals("jpeg") ||
			 extension.equals("wmf") || extension.equals("psd") || extension.equals("tiff"))
			mmedia.setType(MediaElem.IMAGE);
		else if (extension.equals("swf"))
			mmedia.setType(MediaElem.FLASH);
		else if (extension.equals("mp3") || extension.equals("wav") || extension.equals("midi") || extension.equals("mid"))
			mmedia.setType(MediaElem.SOUND);
		else if (extension.equals("ggb"))
			mmedia.setType(MediaElem.GEOGEBRA);
		else if (extension.equals("class"))
			mmedia.setType(MediaElem.JAVAAPPLET);
		else if (extension.equals("sib") || extension.equals("sch"))
			mmedia.setType(MediaElem.SIBELIUS);
		else if (extension.equals("YTvideo"))
			mmedia.setType(MediaElem.YOUTUBE);
		else {
			log.info("Formato de fichero desconocido");
			deleteMmediaFile(uploadedFileName);
			return null;
		}
		return mmedia;
	}
	
	/**
	 * Deletes a file from the multimedia path
	 * @param filename file name
	 */
	
	private void deleteMmediaFile (String filename) {
		try {
			File uploadedFile = new File(servletContext.getRealPath("/")+Constants.MMEDIAPATH+filename);
			uploadedFile.delete();
			log.debug("Borrado fichero "+filename);
		} catch (Exception e) {
			log.error("No se puede borrar: "+e.getMessage());
		}
	}
	
	/**
	 * Normalizes a file name, takes out the path part and if it's too long trunks it
	 * @param fileName
	 * @return
	 */
	private String normalizeFileName(String fileName) {
		String result = fileName.substring(Math.max(fileName.lastIndexOf("/"),fileName.lastIndexOf("\\"))+1);
		log.debug("Normalizando "+result);
		if (result.length()<=20)
			return result;
		else {
			int dot = result.lastIndexOf('.');
			String extension = result.substring(dot+1);
			return result.substring(0, Math.min(19-extension.length(), dot-1))+"."+extension;
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
		
		// If the parameter is filled, then that question is previewed (used in "import question interface")
		TemplateExamQuestion question;
		if (request.getParameter("qId") != null) {
			Long idq = Long.valueOf(request.getParameter("qId"));
			question = new TemplateExamQuestion();
			question.setId(idq);
			question = tutorManagementService.getQuestionFromId(question);
		} else {
			// In other case, the current question is previewed
			question = currentQuestion;
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
	
	
	public void generateQuestionPDF (HttpServletRequest request, HttpServletResponse response) {
		
		String idString = request.getParameter("idQuestion");
		TemplateExamQuestion question = new TemplateExamQuestion();
		long idLong = -1;
		try{
			idLong =Long.parseLong(idString);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(idLong == -1){
			log.error("No se ha podido parsear el id de la pregunta");
			return;
		}
		question.setId(idLong);
		question = tutorManagementService.getQuestionFromId(question);
		ByteArrayOutputStream pdfByte = null;
		try {
			pdfByte = learnerManagementService.parse2PDF(question);
			response.setHeader("Content-disposition", "inline; filename=\""+question.getId()+"_"+question.getTitle()+".pdf\""); 
			response.setContentType("application/pdf");
			response.setContentLength(pdfByte.size());
			ServletOutputStream out = response.getOutputStream();
			pdfByte.writeTo(out);
			out.flush();
		}
		catch(DocumentException e1) {
			log.error("Se ha producido un error al intentar generar el PDF con la revision de la pregunta "+question.getId()+"\n"+e1.getLocalizedMessage());
		}
		catch(IOException e2) {
			log.error("Se ha producido un error de E/S cuando se intentaba generar el PDF con la revision de la pregunta "+question.getId()+"\n"+e2.getLocalizedMessage());
		}
		catch(NullPointerException e3) {
			log.error("Se ha producido un error cuando se intentaba generar el PDF con la revision de una pregunta: PREGUNTA NO VALIDA");
		}
		log.info("Se ha generado correctamente el PDF con la revision del examen "+question.getId());	
	}
	
	/**
	 * Copy a question
	 * @param request
	 * @param response
	 * @return ModelAndView corresponding to the preview for the question
    */
	public ModelAndView questionCopy (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		//ModelAndView mav = new ModelAndView("tutor/new_question");
		ModelAndView mav = new ModelAndView("tutor/new_question");
		// copiar la pregunta
		
		TemplateExamQuestion question = tutorManagementService.copyQuestion(currentQuestion,true);
		log.info("Duplicada pregunta: "+currentQuestion.getId()+" a la nueva pregunta: "+question.getId());
		
		// List of themes for the course of this group 
		List<TemplateExamSubject> thlist = tutorManagementService.getCourseSubjects(currentGroup);
		// Addtion of the themes:
		mav.addObject("themes",thlist);
		setCurrentQuestion(question);//actualizamos el currenQuestion para que en la vista jsp tire de �l con los datos actualizados.
		// Addition of the question:
		mav.addObject("question",question);
		if(question!= null && question.getType()==1){
			if(question.getAnswers()!=null && question.getAnswers().size()>0){
				mav.addObject("fillAnswer", question.getAnswers().get(0));
				setCurrentAnswer(question.getAnswers().get(0));
			} 
		}
		
		log.info("Nº RESPUESTAS:"+question.getAnswers().size());
		mav.addObject("question",question);
		
		//Addition of the confirm message
		boolean sourceActionIsCopy=true;
		mav.addObject("sourceActionIsCopy",sourceActionIsCopy);
		// Addition of the group object:
		mav.addObject("group",currentGroup);
		
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		
		return mav;
	}
	
	public ModelAndView copyQuestionUsedInExam (HttpServletRequest request, HttpServletResponse response) {
		// Creación del ModelAndView. 
		//ModelAndView mav = new ModelAndView("tutor/new_question");
		ModelAndView mav = new ModelAndView("tutor/new_question");
		String selectedIdQuestion = request.getParameter("selectedIdQuestion");
		boolean error = false;
		if(selectedIdQuestion!=null){
			long idQuestion;
			try{
				idQuestion = Long.parseLong(selectedIdQuestion);
				currentQuestion = new TemplateExamQuestion();
				currentQuestion.setId(idQuestion);
				currentQuestion = tutorManagementService.getQuestionFromId(currentQuestion);
			}catch(Exception e){
				error = true;
			}
		}else{
			log.debug("No se a seleccionado ninguna pregunta para copiar");
			error = true;
		}
		if(currentGroup==null){
			String groupId = request.getParameter("idGroup");
			long idGroup;
			try{
				idGroup = Long.parseLong(groupId);
				currentGroup = tutorManagementService.getGroup(idGroup);
			}catch(Exception e){
				log.error("No se encuentra el grupo actual");
				mav = new ModelAndView("error");
			}
		}
		
		// copiar la pregunta
		TemplateExamQuestion question = null;
		/*
		 * If there are some error we create a new question else we copy the selectedQuestion
		 * */
		if(error){
			question = new TemplateExamQuestion();
		}else{
			question = tutorManagementService.copyQuestion(currentQuestion,true);
			log.info("Duplicada pregunta: "+currentQuestion.getId()+" a la nueva pregunta: "+question.getId());
		}
		
		// List of themes for the course of this group 
		List<TemplateExamSubject> thlist = tutorManagementService.getCourseSubjects(currentGroup);
		// Addtion of the themes:
		mav.addObject("themes",thlist);
		setCurrentQuestion(question);//actualizamos el currenQuestion para que en la vista jsp tire de él con los datos actualizados.
		if(question!=null && question.getAnswers()!=null)
			log.info("Nº RESPUESTAS:"+question.getAnswers().size());
		else
			log.info("Nº RESPUESTAS:0");
		// Addition of the question:
		mav.addObject("question",question);
		
		//Addition of the confirm message
		boolean sourceActionIsCopy=true;
		mav.addObject("sourceActionIsCopy",sourceActionIsCopy);
		// Addition of the group object:
		mav.addObject("group",currentGroup);
		if(question.getType()==1 && question.getAnswers()!=null && question.getAnswers().size()==1){
			mav.addObject("fillAnswer",question.getAnswers().get(0));
		}
		//Adición del usuario para mostrar nombre y apellidos. Está en la sesión
		User user = (User) request.getSession().getAttribute(Constants.USER);
		mav.addObject("user",user);
		
		return mav;
	}
	
	/**
	 * Review exams
	 * @return List<ExamForReview>
	 */
	public List<ExamForReview> reviewExamsByQuestion() {
		log.debug("Corrigiendo examenes en los que ha aparecido la pregunta con ID "+getCurrentQuestion().getId());
		/*List<ExamForReview> infoCorregidos*/return learnerManagementService.examReviewByQuestion(getCurrentQuestion());
		
	}
	
	/**
	 * Returns true if the question appears on a exam else return false
	 * Review exams
	 * @param id
	 * @return boolean
	 */
	public boolean isInExam(String id){
		Query queryQuestions = new Query();
		queryQuestions.setGroup(currentGroup.getId());
		List<TemplateExamQuestion> tqlist = tutorManagementService.find(queryQuestions);
		Long idQuestion = null;
		try{
		idQuestion = Long.parseLong(id);
		}catch(Exception e){
			log.error("La id recibida"+id+" no puede ser correcta");
		}
		for(int i=0;i<tqlist.size();i++){
			if(tqlist.get(i).getId().equals(idQuestion)){
				if(tqlist.get(i).getUsedInExam()){
					return true;
				}
			}
		}
		return false;
	}
	
	public int removeAnswersFromQuestion(int questionType){
		if(currentQuestion!=null){
			List<TemplateExamAnswer> answers = currentQuestion.getAnswers();
			if(answers!=null){
				for(int i=answers.size()-1;i>=0;i--){
					tutorManagementService.deleteAnswer(answers.get(i));
					if(answers.get(i).getMmedia()!=null)
						answers.get(i).getMmedia().removeAll(answers.get(i).getMmedia());
					answers.remove(i);
				}
			}
			currentQuestion.setType(questionType);
			currentQuestion.setNumCorrectAnswers(0);
		}
		if(currentAnswer !=null){
			tutorManagementService.deleteAnswer(currentAnswer);
			currentAnswer = null;
		}
		if(currentQuestion!=null){
			return currentQuestion.getType();
		}
		return -1;
	}
	
	public int saveFillAnswer(String answerText, boolean isNewAnswer){
		if(currentQuestion!=null && currentQuestion.getType()==1){
			currentAnswer = new TemplateExamAnswer();
			currentAnswer.setText(answerText);
			currentAnswer.setQuestion(currentQuestion);
			currentAnswer.setSolution(Constants.YES);
			currentAnswer.setActive(Constants.YES);
			currentAnswer.setMmedia(null);
			currentAnswer.setValue(100);
			currentAnswer.setUsedInExam(false);
			List<TemplateExamAnswer> answers = currentQuestion.getAnswers();
			TemplateExamAnswer answer = null;
			//Eliminamos todas las respuestas para que al a�adir la nueva solo haya una respuesta.
			if(answers.size()>0){
				for(int i=answers.size()-1;i>=0;i--){
					answer = answers.get(i);
					tutorManagementService.deleteAnswer(answer);
					answers.remove(i);
				}
			}
			currentQuestion.setAnswers(answers);
			currentQuestion.setNumCorrectAnswers(1);
			if(currentAnswer!=null){
				currentQuestion.getAnswers().add(currentAnswer);
			}
			tutorManagementService.saveAnswer(currentAnswer);
			tutorManagementService.saveQuestion(currentQuestion);
			return 0;
		}else
			return -1;
		
	}
	
	public boolean editGeogebraMM(long geogebraId,boolean advanced){
		if(currentQuestion==null)
			return false;
		List<MediaElem> MMList = currentQuestion.getMmedia();
		for(MediaElem elem : MMList){
			if(elem.getId().equals(geogebraId)){
				if(advanced)
					elem.setGeogebraType(1);
				else
					elem.setGeogebraType(0);
				tutorManagementService.saveMediaElemToQuestion(currentQuestion, elem);
				return true;
			}
		}
		
		return false;
	}

}
