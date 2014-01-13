package com.cesfelipesegundo.itis.biz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.dao.OptimisticLockingFailureException;

import com.cesfelipesegundo.itis.biz.api.TutorManagementService;
import com.cesfelipesegundo.itis.dao.api.ConfigExamDAO;
import com.cesfelipesegundo.itis.dao.api.CourseDAO;
import com.cesfelipesegundo.itis.dao.api.ExamDAO;
import com.cesfelipesegundo.itis.dao.api.GroupDAO;
import com.cesfelipesegundo.itis.dao.api.StatsDAO;
import com.cesfelipesegundo.itis.dao.api.SubjectDAO;
import com.cesfelipesegundo.itis.dao.api.TemplateExamAnswerDAO;
import com.cesfelipesegundo.itis.dao.api.TemplateExamDAO;
import com.cesfelipesegundo.itis.dao.api.TemplateExamQuestionDAO;
import com.cesfelipesegundo.itis.dao.api.TemplateGradeDAO;
import com.cesfelipesegundo.itis.dao.api.UserDAO;
import com.cesfelipesegundo.itis.model.AnsweredQuestionData;
import com.cesfelipesegundo.itis.model.CalifData;
import com.cesfelipesegundo.itis.model.ConfigExam;
import com.cesfelipesegundo.itis.model.ConfigExamSubject;
import com.cesfelipesegundo.itis.model.CustomExamUser;
import com.cesfelipesegundo.itis.model.Exam;
import com.cesfelipesegundo.itis.model.ExamAnswer;
import com.cesfelipesegundo.itis.model.ExamGlobalInfo;
import com.cesfelipesegundo.itis.model.ExamQuestion;
import com.cesfelipesegundo.itis.model.ExamStats;
import com.cesfelipesegundo.itis.model.Group;
import com.cesfelipesegundo.itis.model.Institution;
import com.cesfelipesegundo.itis.model.LearnerStats;
import com.cesfelipesegundo.itis.model.MediaElem;
import com.cesfelipesegundo.itis.model.Message;
import com.cesfelipesegundo.itis.model.Query;
import com.cesfelipesegundo.itis.model.QueryGrade;
import com.cesfelipesegundo.itis.model.QuestionStats;
import com.cesfelipesegundo.itis.model.Subject;
import com.cesfelipesegundo.itis.model.TemplateExam;
import com.cesfelipesegundo.itis.model.TemplateExamAnswer;
import com.cesfelipesegundo.itis.model.TemplateExamQuestion;
import com.cesfelipesegundo.itis.model.TemplateExamSubject;
import com.cesfelipesegundo.itis.model.TemplateGrade;
import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.model.comparators.SubjectOrderComparator;
import com.cesfelipesegundo.itis.web.Constants;

public class TutorManagementServiceImpl extends BaseService implements
		TutorManagementService {
	private static final Log log = LogFactory.getLog(TutorManagementServiceImpl.class);

	private GroupDAO groupDAO;

	private CourseDAO courseDAO;
	
	private TemplateExamQuestionDAO templateExamQuestionDAO;

	private TemplateExamAnswerDAO templateExamAnswerDAO;
	
	private ConfigExamDAO configExamDAO;

	private TemplateGradeDAO templateGradeDAO;
	
	private StatsDAO statsDAO;
	
	private TemplateExamDAO templateExamDAO;
	
	private ExamDAO examDAO;
	
	private UserDAO userDAO;
	
	private SubjectDAO subjectDAO;
	
	private Resource rootPath;

	public Resource getRootPath() {
		return rootPath;
	}

	public void setRootPath(Resource rootPath) {
		this.rootPath = rootPath;
	}

	public ExamDAO getExamDAO() {
		return examDAO;
	}

	public void setExamDAO(ExamDAO examDAO) {
		this.examDAO = examDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public TemplateExamDAO getTemplateExamDAO() {
		return templateExamDAO;
	}

	public void setTemplateExamDAO(TemplateExamDAO templateExamDAO) {
		this.templateExamDAO = templateExamDAO;
	}

	public ConfigExamDAO getConfigExamDAO() {
		return configExamDAO;
	}

	public void setConfigExamDAO(ConfigExamDAO configExamDAO) {
		this.configExamDAO = configExamDAO;
	}

	public GroupDAO getGroupDAO() {
		return groupDAO;
	}

	public void setGroupDAO(GroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}

	public CourseDAO getCourseDAO() {
		return courseDAO;
	}

	public void setCourseDAO(CourseDAO courseDAO) {
		this.courseDAO = courseDAO;
	}

	public TemplateExamQuestionDAO getTemplateExamQuestionDAO() {
		return templateExamQuestionDAO;
	}

	public void setTemplateExamQuestionDAO(
			TemplateExamQuestionDAO templateExamQuestionDAO) {
		this.templateExamQuestionDAO = templateExamQuestionDAO;
	}

	public TemplateExamAnswerDAO getTemplateExamAnswerDAO() {
		return templateExamAnswerDAO;
	}

	public void setTemplateExamAnswerDAO(TemplateExamAnswerDAO templateExamAnswerDAO) {
		this.templateExamAnswerDAO = templateExamAnswerDAO;
	}
		
	public TemplateGradeDAO getTemplateGradeDAO() {
		return templateGradeDAO;
	}

	public void setTemplateGradeDAO(TemplateGradeDAO templateGradeDAO) {
		this.templateGradeDAO = templateGradeDAO;
	}
	
	public StatsDAO getStatsDAO() {
		return statsDAO;
	}

	public void setStatsDAO(StatsDAO statsDAO) {
		this.statsDAO = statsDAO;
	}

	public SubjectDAO getSubjectDAO() {
		return subjectDAO;
	}

	public void setSubjectDAO(SubjectDAO subjectDAO) {
		this.subjectDAO = subjectDAO;
	}

	/**
	 * Devuelve una lista con los grupos en los que imparte clase el profesor
	 * cuyo id de usuario se pasa por parámetro.
	 * 
	 * @param id
	 * @return Lista de los grupos en los que imparte clase el profesor cuyo id
	 *         de usuario se pasa por parámetro.
	 */
	public List<Group> getTeachedGroups(Long id) {
		List<Group> groups = groupDAO.getTeachedGroups(id);
		return groups;
	}

	/**
	 * Devuelve una lista con los grupos en los que está matriculado el alumno
	 * cuyo id de usuario se pasa por parámetro.
	 * 
	 * @param id
	 * @return Lista de los grupos en los que está matriculado cuyo id
	 *         de usuario se pasa por parámetro.
	 */
	public List<Group> getMatriculatedGroups(Long id)
	{
		List<Group> groups = groupDAO.getMatriculatedGroups(id);
		return groups;
	}
	
	/**
	 * It returns a list of themes linked to the course whose id is passed as
	 * parameter
	 * 
	 * @param group object
	 * @return It returns a list of themes linked to the course whose id is
	 *         passed as parameter
	 */
	public List<TemplateExamSubject> getCourseSubjects(Group g) {

		// The results are obtained from the group object
		List<TemplateExamSubject> result = courseDAO.getSubjects(g);

		return result;
	}

	public List<Subject> getSubjects(Group g) {

		// The results are obtained from the group object
		List<Subject> result = subjectDAO.getSubjectsMinInfo(g);

		// Ordering the list of subjects using the "order" field
		Collections.sort(result,new SubjectOrderComparator());
		
		
		return result;
	}
	
	/**
	 * Returns all the group information read from the database
	 * 
	 * @param group
	 *            id
	 * @return the group object
	 */
	public Group getGroup(Long id) {
		Group result = groupDAO.getGroup(id);
//		groupDAO.fillCourse(result);
		return result;
	}

	/**
	 * Saves the question information into the database
	 * 
	 * @param question
	 * @return id of the question saved
	 */
	public void saveQuestion(TemplateExamQuestion question) {
		/*
		 * Se debe actualizar la tabla de preguntas con los datos que se pasan
		 * en currentQuesion. Si la pregunta es nueva hay que realizar una
		 * inserción Si la pregunta existía, hay que hacer una actualización. El
		 * método devuelve el id de la pregunta en cualquier caso.
		 */
		/*
		 * NOTA: si la pregunta es nueva, currentQuestion.id tendrá "null"
		 */
		if(question.getId() != null ){
			templateExamQuestionDAO.update(question);
		}else {
			templateExamQuestionDAO.save(question);
		}

	}

	/**
	 * Saves the answer information into the database
	 * 
	 * @param answer
	 */
	public void saveAnswer(TemplateExamAnswer answer ) {
		/*
		 * Se debe actualizar la tabla de respuestas con los datos que se pasan
		 * en answer. La respuesta SIEMPRE es nueva -> Inserción SIEMPRE EN LA
		 * BD Se pasa currentQuestion para tomar el id (hará falta en la BD)
		 */
		if(answer.getId() != null){
			templateExamAnswerDAO.update(answer);
		}else{
			templateExamAnswerDAO.save(answer);
		}
	}

	/**
	 * Gets from the database the list of questions related to this group
	 * 
	 * @param currentGroup
	 * @return list of TemplateExamQuestion: questions, answers, mmedia, ...
	 */
	public List<TemplateExamQuestion> getGroupQuestions(Group currentGroup) {
		return groupDAO.getQuestions(currentGroup);
	}

	
	public void deleteAnswer(TemplateExamAnswer answer) {
				
		// Delete the mmedia files associated with the answer
		
		List<MediaElem> mmlist = answer.getMmedia();
		if(mmlist!=null){
			Iterator<MediaElem> iterMM = mmlist.iterator();
			while (iterMM.hasNext()) {
				MediaElem mm = iterMM.next();
				deleteMmediaFile(mm.getPath());
			}
		}
		
		// delete the answer in the database
		
		templateExamAnswerDAO.delete(answer);
	}

	
	public void saveMediaElemToQuestion(TemplateExamQuestion question, MediaElem mediaElem){
		
		// Comportamiento similar a saveQuestion y saveAnswer con respecto al id
		
		if(mediaElem.getId() != null){
			templateExamQuestionDAO.update(question, mediaElem,true);
		}else{
			templateExamQuestionDAO.save(question, mediaElem,true);
		}
	}
	
	public void saveMediaElemToComment(TemplateExamQuestion question, MediaElem mediaElem){
		
		// Comportamiento similar a saveQuestion y saveAnswer con respecto al id
		
		if(mediaElem.getId() != null){
			templateExamQuestionDAO.update(question, mediaElem,false);
		}else{
			templateExamQuestionDAO.save(question, mediaElem,false);
		}
	}
	
	public void saveMediaElemToAnswer(TemplateExamAnswer answer, MediaElem mediaElem){
		
		// Comportamiento similar a saveQuestion y saveAnswer con respecto al id
		
		if(mediaElem.getId() != null){
			templateExamAnswerDAO.update(answer, mediaElem);
		}else{
			templateExamAnswerDAO.save(answer, mediaElem);
		}
	}
	
	public void deleteMediaElemFromComment(TemplateExamQuestion question, MediaElem mediaElem){
		
		deleteMmediaFile(mediaElem.getPath());
		templateExamQuestionDAO.delete(question, mediaElem,false);
	}
		
	public void deleteMediaElemFromQuestion(TemplateExamQuestion question, MediaElem mediaElem){
		
		deleteMmediaFile(mediaElem.getPath());
		templateExamQuestionDAO.delete(question, mediaElem,true);
	}
	
	public void deleteMediaElemFromAnswer(TemplateExamAnswer answer, MediaElem mediaElem){
		
		deleteMmediaFile(mediaElem.getPath());
		templateExamAnswerDAO.delete(answer, mediaElem);
	}

	public List<TemplateExamQuestion> find(Query query) {
		List<TemplateExamQuestion> result = templateExamQuestionDAO.find(query);
		
		for(TemplateExamQuestion question : result ){
			templateExamQuestionDAO.fillMediaElem(question);
			templateExamQuestionDAO.fillAnswers(question);
			
		}
		
		return result;
	}

	public void deleteQuestion(TemplateExamQuestion question) {
		
		
		// Delete the mmedia files of the question:
		List<MediaElem> mmlist = question.getMmedia();
		if (mmlist != null) {
			Iterator<MediaElem> iterMM = mmlist.iterator();
			while (iterMM.hasNext()) {
				MediaElem mm = iterMM.next();
				deleteMmediaFile(mm.getPath());
			}
		}
		
		// It also has to delete all the mmedia files of the answers.		
		List<TemplateExamAnswer> answList = question.getAnswers();
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
		
		templateExamQuestionDAO.delete(question);
	}
	
	public ConfigExam configExamCopy(ConfigExam examSource) {
		//1.- Create new ConfigExam instance and copy source attributes
		ConfigExam examCopy = new ConfigExam(examSource);
		//End 1
		
		//2.- Set new ConfigExam copy id to null. configExamDAO will insert a new entry on DB for the copy
		examCopy.setId(null);
		configExamDAO.save(examCopy);
		//End 2
		
		//3.- Copy subjects (no subject deep copy needed!!!)
		if (examSource.getSubjects()!=null) {
			List<ConfigExamSubject> examCopySubjects = new ArrayList<ConfigExamSubject>();
			for (ConfigExamSubject subject : examSource.getSubjects()) {
				/*La id de "subject" hay que ponerla a null porque a la hora de guardar
				 * en la BD no se crea una nueva fila si "subject" ya tiene id, lo que hace es 
				 * un UPDATE de la fila cuya id es igual a la de "subject"*/
				subject.setId(null);
				examCopySubjects.add(subject);
				/*
				 * Si la id de "subject" hubiese sido distindo de null lo que hubiese hecho en la
				 * BD es modificar el campo id de manera que la id la cambiaría por la de examCopy
				 * haciendo que examSource no tuviera ningún "subject"
				 * */
				saveSubjectToExam(examCopy, subject);
			}
			examCopy.setSubjects(examCopySubjects);
		}
		else examCopy.setSubjects(null);
		//End 3
		
		List<CustomExamUser> users = getUsersInCustomExam(examSource.getId());
		//4
		for(CustomExamUser user : users){
			addUser2CustomExam(examCopy.getId(), user.getId());
		}
		//End 4
		return examCopy;
			
	}
	public TemplateExamQuestion copyQuestion(TemplateExamQuestion question, boolean copy) {
		//1.- Crear un nuevo TemplateExamQuestion que copie los valores del pasado por parametro
		TemplateExamQuestion questionCopy = new TemplateExamQuestion();
		questionCopy.setActive(question.getActive());
		questionCopy.setComment(question.getComment());
		questionCopy.setDifficulty(question.getDifficulty());
		questionCopy.setGroup(question.getGroup());
		questionCopy.setNumCorrectAnswers(question.getNumCorrectAnswers());
		questionCopy.setSubject(question.getSubject());
		questionCopy.setText(question.getText());
		if(copy && questionCopy.getTitle()!=null && questionCopy.getTitle().length()<50){
			questionCopy.setTitle(question.getTitle()+" (copia)");
		}else{
			questionCopy.setTitle(question.getTitle());
		}
		questionCopy.setUsedInExam(false); //La nueva pregunta aun no ha sido usada en ningun examen, independientemente de si la original lo fue
		questionCopy.setVisibility(question.getVisibility());
		//Fin 1
		
		//2.- Establecer id del nuevo TemplateExamQuestion a null, para que el DAO lo guarde como una nueva pregunta
		questionCopy.setId(null);
		questionCopy.setType(question.getType());
		saveQuestion(questionCopy);
		//Fin 2
		
		//Obtenemos la ruta a los elementos multimedia
		String rutaMmedia;
		try {
			rutaMmedia = rootPath.getFile().getAbsolutePath()+File.separatorChar;
		} catch (IOException e) {
			rutaMmedia = Constants.MMEDIAPATH;
			log.error("No se puede acceder al directorio de ficheros multimedia. Es posible que no puedan duplicarse estos elementos...");
		}
		
		//3.- Copia en profundidad de los elementos multimedia (si los hay), utilizando duplicateMMediaFile y guardando la ruta en la nueva pregunta
		if (question.getMmedia() != null) {
			//Obtenemos la lista de elementos multimedia de la pregunta original
			List<MediaElem> listMMediaOriginal = question.getMmedia();
			//Creamos una lista de elementos para la pregunta duplicada
			List<MediaElem> listMMediaDuplicada = new ArrayList<MediaElem>();
			
			//Para cada elemento de la lista original
			for(MediaElem elemento : listMMediaOriginal){
				
				// Duplicate files and modify the path
				String lastPath = elemento.getPath();
				//Copiamos su informacion
				MediaElem copiaMmedia = new MediaElem(elemento);
				copiaMmedia.setId(null);
				//Duplicamos su archivo multimedia
				copiaMmedia.setPath(duplicateMmediaFile(rutaMmedia, elemento.getPath()));
				if(copiaMmedia.getPath()==null){
					copiaMmedia.setPath(lastPath);
				}
				//Si el archivo multimedia pudo duplicarse
				if (copiaMmedia.getPath() != null) {
					//Aniadimos el archivo multimedia a la pregunta
					saveMediaElemToQuestion(questionCopy, copiaMmedia);
					listMMediaDuplicada.add(copiaMmedia);
				}
				else
					//Sino, deja constancia en el log y a otra cosa...
					log.error("No pudo duplicarse el elemento multimedia \""+rutaMmedia+elemento.getPath()+"\" de la pregunta con ID: "+question.getId());
			}
			questionCopy.setMmedia(listMMediaDuplicada);
		}
		
		if (question.getMmediaComment() != null) {
			//Obtenemos la lista de elementos multimedia de la pregunta original
			List<MediaElem> listMMediaOriginal = question.getMmediaComment();
			//Creamos una lista de elementos para la pregunta duplicada
			List<MediaElem> listMMediaDuplicada = new ArrayList<MediaElem>();
			
			//Para cada elemento de la lista original
			for(MediaElem elemento : listMMediaOriginal){
				
				// Duplicate files and modify the path
				String lastPath = elemento.getPath();
				//Copiamos su informacion
				MediaElem copiaMmedia = new MediaElem(elemento);
				copiaMmedia.setId(null);
				//Duplicamos su archivo multimedia
				copiaMmedia.setPath(duplicateMmediaFile(rutaMmedia, elemento.getPath()));
				if(copiaMmedia.getPath()==null){
					copiaMmedia.setPath(lastPath);
				}
				//Si el archivo multimedia pudo duplicarse
				if (copiaMmedia.getPath() != null) {
					//Aniadimos el archivo multimedia a la pregunta
					saveMediaElemToComment(questionCopy, copiaMmedia);
					listMMediaDuplicada.add(copiaMmedia);
				}
				else
					//Sino, deja constancia en el log y a otra cosa...
					log.error("No pudo duplicarse el elemento multimedia \""+rutaMmedia+elemento.getPath()+"\" de la pregunta con ID: "+question.getId());
			}
			questionCopy.setMmediaComment(listMMediaDuplicada);
		}
		
		
		//Fin 3
		
		//4.- Copia en profundidad de la lista de respuestas, con sus correspondientes elementos multimedia. Consultar conveniencia de implementar public TemplateExamAnswer copyAnswer(TemplateExamAnswer answer)
		//Si la pregunta original tiene una lista de respuestas
		if (question.getAnswers()!=null) {
			//Obtenemos la lista de respuestas de la pregunta original
			List<TemplateExamAnswer> listAnswersOriginal = question.getAnswers();
			//Creamos una lista de respuestas de la pregunta original
			List<TemplateExamAnswer> listAnswersDuplicada = new ArrayList<TemplateExamAnswer>();
			
			//Para cada una de las respuestas
			for (TemplateExamAnswer respuestaOriginal : listAnswersOriginal) {
				TemplateExamAnswer respuestaCopia = new TemplateExamAnswer();
				
				//Copiamos los atributos de la respuesta original
				respuestaCopia.setActive(respuestaOriginal.getActive());
				respuestaCopia.setMarked(respuestaOriginal.getMarked());
				respuestaCopia.setQuestion(questionCopy); //La respuesta copia estara relacionada, obviamente, con la nueva pregunta copia
				respuestaCopia.setSolution(respuestaOriginal.getSolution());
				respuestaCopia.setText(respuestaOriginal.getText());
				respuestaCopia.setUsedInExam(false); // La nueva respuesta copia no ha sido utilizada en ningun examen, independientemente de si lo fue la original
				respuestaCopia.setValue(respuestaOriginal.getValue());
				
				//Persistencia
				respuestaCopia.setId(null);
				saveAnswer(respuestaCopia);
				
				//Si la respuesta tiene elementos multimedia
				if (respuestaOriginal.getMmedia()!=null) {
					//TESTING!!!: saveAnswer(respuestaCopia);
					//Obtenemos la lista de elementos multimedia de la respuesta original
					List<MediaElem> listMMediaRespuestaOriginal = respuestaOriginal.getMmedia();
					//Creamos una lista de elementos multimedia para la respuesta duplicada
					List<MediaElem> listMMediaRespuestaDuplicada = new ArrayList<MediaElem>();
					
					//Para cada elemento de la lista original
					for(MediaElem elemento : listMMediaRespuestaOriginal){
						//Copiamos su informacion
						MediaElem copiaMmedia = new MediaElem(elemento);
						copiaMmedia.setId(null);
						//Duplicamos su archivo multimedia
						copiaMmedia.setPath(duplicateMmediaFile(rutaMmedia, elemento.getPath()));
						//Si pudo duplicarse el elemento multimedia
						if (copiaMmedia.getPath() !=null) {
							//Aniadimos el nuevo a la pregunta copiada
							saveMediaElemToAnswer(respuestaCopia, copiaMmedia);
							listMMediaRespuestaDuplicada.add(copiaMmedia);
						}
						else
							//Sino, se deja constancia en el log y a otra cosa...
							log.error("No pudo duplicarse el elemento multimedia \""+rutaMmedia+elemento.getPath()+"\" de la respuesta con ID: "+respuestaOriginal.getId());
					}
					respuestaCopia.setMmedia(listMMediaRespuestaDuplicada);
				}
				//Persistencia
				//TESTING: saveAnswer(respuestaCopia);
				//A la lista de respuestas de la pregunta duplicada
				listAnswersDuplicada.add(respuestaCopia);
			}
			questionCopy.setAnswers(listAnswersDuplicada);
		}
		
		//Salvamos finalmente la pregunta copiada
		//TESTING: saveQuestion(questionCopy);
		
		//Devolvemos la instancia de la pregunta copiada
		return questionCopy;
		
	}

	public TemplateExamQuestion getQuestionFromId(TemplateExamQuestion question) {	
		return templateExamQuestionDAO.getQuestionFromId(question);
						
	}

// methods to manage exam configurations	
	
	public void deleteConfigExam(ConfigExam exam) {
		configExamDAO.delete(exam);
	}

	public List<ConfigExam> getGroupConfigExams(Group group, String orderby) {
		return configExamDAO.getGroupConfigExams(group,orderby);
	}

	public void updateExamReview(ConfigExam exam) {
		configExamDAO.updateReview(exam);
	}

	
	/**
	 * Saves the exam configuration into the database
	 * 
	 * @param exam
	 * @return id of the exam saved if update
	 */
	public void saveExam(ConfigExam exam) {
		/*
		 * Se debe actualizar la tabla de examenes con los datos que se pasan
		 * en exam. Si el examen es nuevo hay que realizar una
		 * inserción. Si el examen existía, hay que hacer una actualización.
		 * El método devuelve el id del examen en cualquier caso.
		 */
		/*
		 * NOTA: si el examen es nuevo, su id tendrá "null"
		 */
		if(exam.getId() != null ){
			configExamDAO.update(exam);
		}else {
			configExamDAO.save(exam);
		}

	}
	
	public void saveSubjectToExam(ConfigExam exam, ConfigExamSubject subject){
		
		// Comportamiento similar a saveQuestion y saveAnswer con respecto al id
		
		if(subject.getId() != null){
			configExamDAO.update(exam, subject);
		}else{
			configExamDAO.save(exam, subject);
		}
	}
	
	public void deleteSubjectFromExam(ConfigExam exam, ConfigExamSubject subject, long idGroup) {
	configExamDAO.delete(exam, subject);
           Query queryQuestions = new Query();
           long subjectId= subject.getSubject().getId();
           queryQuestions.setSubject(subjectId);
           List<TemplateExamQuestion> questions = find(queryQuestions);
           int max = subject.getMaxDifficulty();
           int min = subject.getMinDifficulty();
           for(TemplateExamQuestion question : questions){
                   if(question.getDifficulty()==max || question.getDifficulty()==min){
                           examDAO.deleteQuestionFromExam(exam.getId(), question.getId());
                   }
               }
	}

	public ConfigExam getConfigExamFromId(ConfigExam exfromdb) {
		return configExamDAO.getConfigExamFromId(exfromdb);
	}

	public List<TemplateGrade> find(QueryGrade query) {
		List<TemplateGrade> result = null;
		result = templateGradeDAO.find(query);
				
		return result;
		
	}

	public List<User> getStudents(Group currentGroup) {
		List<User> result = null;
		result = groupDAO.getStudents(currentGroup);
		
		return result;
	}


	public List<User> getStudents(Group currentGroup, String orderby) {
		List<User> result = null;
		result = groupDAO.getStudents(currentGroup,orderby);
		
		return result;
	}

	
	
	public List<Message> validate(ConfigExam config) {
		TemplateExam exam = templateExamDAO.getTemplateExam(config.getId());
		List<Message> result = new ArrayList<Message>();
		if(exam!=null){
			List<Message> messages = null;
			// 1. Validate Dates and duration
			messages = validateConfigExamDates(exam);
			if (!messages.isEmpty()) {
				result.addAll(messages);
			}
	
			// 2. Validate Subjects
			messages = validateSubjects(exam);
			if (!messages.isEmpty()) {
				result.addAll(messages);
			}
		}else{
			result.add(new Message("validation.noConfig",
					new Object[] {config.getTitle(), config.getId() },
			Message.MessageType.ERROR));
			log.error("Intentando validar configuración de examen no existente titulo="+config.getTitle()+" id="+config.getId());
		}

		return result;
	}

	private List<Message> validateSubjects(TemplateExam templateExam) {
		List<Message> result = new ArrayList<Message>();
		List<Message> messages = null;
		ConfigExam configExam = new ConfigExam();
		// TODO: Refactoring Needed !!!
		configExam.setVisibility(templateExam.getVisibility());
		configExam.setGroup(templateExam.getGroup());
		
		/*
		 * 1. Check if there are any Subjects
		 */
		if((templateExam.getSubjects() == null) || (templateExam.getSubjects().size()==0)){
			if (result == Collections.EMPTY_LIST) {
				result = new ArrayList<Message>();
			}
			result.add(new Message("validation.noSubjects",
					new Object[] {templateExam.getTitle() },
			Message.MessageType.ERROR));
			return result;
		}
		
		/*
		 * 1. Check if there are no duplicated subject+level
		 */
		for (TemplateExamSubject s1 : templateExam.getSubjects()) {
			for (TemplateExamSubject s2 : templateExam.getSubjects()) {
				if((s1.getId() != s2.getId())&&(s1.getSubject().equals(s2.getSubject()))&&(s1.getMaxDifficulty()==s2.getMaxDifficulty())&&s1.getQuestionsType()==s2.getQuestionsType()){
					if (result == Collections.EMPTY_LIST) {
						result = new ArrayList<Message>();
					}
					// TODO: We need to come up with a solution to internationalize the messages !!!
					String difficultyStr=null;
					if(s2.getMaxDifficulty()==Constants.LOW){
						difficultyStr = "baja";
					}else if(s2.getMaxDifficulty()==Constants.MEDIUM){
						difficultyStr = "media";
					}else {
						difficultyStr = "alta";
					}
					result.add(new Message("validation.duplicateSubjectSameDifficultyLevel",
							new Object[] {templateExam.getTitle(), s2.getSubject(), difficultyStr},
					Message.MessageType.ERROR));
					break;
				}
			}
		}

		
		/*
		 * Recursively check all the subjects
		 */
		for (TemplateExamSubject templateSubject : templateExam.getSubjects()) {
			messages = validateSubject(templateSubject, configExam);
			if (!messages.isEmpty()) {
				if (result == Collections.EMPTY_LIST) {
					result = new ArrayList<Message>();
				}
				result.addAll(messages);
			}
		}

		return result;
	}

	public List<Message> validateSubject(TemplateExamSubject templateSubject,
			ConfigExam configExam) {
		List<Message> result = Collections.EMPTY_LIST;

		// 1. Check Questions
		int visibility = configExam.getVisibility();
		Group examGroup = configExam.getGroup();

		int maxDifficulty = templateSubject.getMaxDifficulty();
		int minDifficulty = templateSubject.getMinDifficulty();
		int questionsNumber = templateSubject.getQuestionsNumber();

		List<TemplateExamQuestion> templateQuestions = templateSubject
				.getQuestions();

		// 1.1. Check questionsSize >= questionsNumber
		if (templateQuestions.size() < questionsNumber) {
			if (result == Collections.EMPTY_LIST) {
				result = new ArrayList<Message>();
			}
			result.add(new Message("validation.subject.notEnoughQuestions",
					new Object[] { templateSubject.getSubject(),
							templateQuestions.size(), questionsNumber },
					Message.MessageType.ERROR));
			return result;
		}

		int questionsCount;

		List<TemplateExamQuestion> previousStep = new ArrayList<TemplateExamQuestion>();
		List<TemplateExamQuestion> currentStep = new ArrayList<TemplateExamQuestion>();
		/*
		 * 1.2. Check if there are enogh active questions
		 */
		questionsCount = 0;
		for (TemplateExamQuestion templateQuestion : templateSubject
				.getQuestions()) {
			if (templateQuestion.getActive() == 1) {
				questionsCount++;
				currentStep.add(templateQuestion);
			}
		}
		if (questionsCount < questionsNumber) {
			if (result == Collections.EMPTY_LIST) {
				result = new ArrayList<Message>();
			}
			result.add(new Message(
					"validation.subject.notEnoughActiveQuestions",
					new Object[] { templateSubject.getSubject(),
							questionsCount, questionsNumber },
					Message.MessageType.ERROR));
			return result;
		}

		previousStep.clear();
		previousStep.addAll(currentStep);
		currentStep.clear();

		/*
		 * 1.3. Check questions (for the configuration visibility) >=
		 * questionNumber
		 */
		questionsCount = 0;
		boolean visible = false;
		int questionVisibility;
		Group questionGroup;
		for (TemplateExamQuestion templateQuestion : previousStep) {
			questionVisibility = templateQuestion.getVisibility();
			questionGroup = templateQuestion.getGroup();
			switch (visibility) {
			case 0:
				// Group visibility
				visible = questionGroup.equals(examGroup);
				break;

			case 1:
				// Institution visibility
				visible = questionGroup.equals(examGroup)
						|| questionGroup.getInstitution().equals(
								questionGroup.getInstitution());
				break;
			}
			if (visible) {
				questionsCount++;
				currentStep.add(templateQuestion);
			}
		}
		if (questionsCount < questionsNumber) {
			if (result == Collections.EMPTY_LIST) {
				result = new ArrayList<Message>();
			}
			result.add(new Message(
					"validation.subject.notEnoughVisibleQuestions",
					new Object[] { templateSubject.getSubject(),
							questionsCount, questionsNumber },
					Message.MessageType.ERROR));
			return result;
		}

		previousStep.clear();
		previousStep.addAll(currentStep);
		currentStep.clear();

		/*
		 * 1.4. Check questions (minDifficulty <= difficulty <= maxDifficulty) >=
		 * questionsNumber
		 */
		questionsCount = 0;
		for (TemplateExamQuestion templateQuestion : previousStep) {
			if ((templateQuestion.getDifficulty() <= maxDifficulty)
					&& (templateQuestion.getDifficulty() >= minDifficulty)) {
				questionsCount++;
				currentStep.add(templateQuestion);
			}
		}
		if (questionsCount < questionsNumber) {
			if (result == Collections.EMPTY_LIST) {
				result = new ArrayList<Message>();
			}
			result.add(new Message(
					"validation.subject.notEnoughAppropriateDifficultyQuestions",
					new Object[] { templateSubject.getSubject(),
							questionsCount, questionsNumber }, Message.MessageType.ERROR));
			return result;
		}

		// 2. Check answers
		List<Message> messages = null;
		ConfigExamSubject configExamSubject = new ConfigExamSubject();
		// TODO: ConfigExamSubject should be an attribute of
		// TemplateExamSubject. Refactoring needed !!!
		configExamSubject.setAnswersxQuestionNumber(templateSubject
				.getAnswersxQuestionNumber());

		for (TemplateExamQuestion templateQuestion : currentStep) {
			messages = validateQuestion(templateQuestion, configExamSubject);
			if (!messages.isEmpty()) {
				if (result == Collections.EMPTY_LIST) {
					result = new ArrayList<Message>();
				}
				result.addAll(messages);
			}
		}

		return result;

	}

	public List<Message> validateQuestion(
			TemplateExamQuestion templateQuestion,
			ConfigExamSubject configSubject) {

		List<Message> result = Collections.EMPTY_LIST;

		int answersxQuestionNumber = configSubject.getAnswersxQuestionNumber();

		List<TemplateExamAnswer> templateQuestionAnswers = templateQuestion
				.getAnswers();

		/*
		 * 1. Check answerSize >= answersxQuestionNumber
		 */
		if (templateQuestionAnswers.size() < answersxQuestionNumber) {
			if (result == Collections.EMPTY_LIST) {
				result = new ArrayList<Message>();
			}
			result
					.add(new Message("validation.question.notEnoughAnswers",
							new Object[] { templateQuestion.getId(),
									templateQuestionAnswers.size(),
									answersxQuestionNumber },
							Message.MessageType.ERROR));
			return result;
		}

		List<TemplateExamAnswer> previousStep = new ArrayList<TemplateExamAnswer>();
		previousStep.addAll(templateQuestionAnswers);
		List<TemplateExamAnswer> currentStep = new ArrayList<TemplateExamAnswer>();

		/*
		 * 2. Check if there are enough active answers
		 * 
		 */
		int answersCount = 0;
		for (TemplateExamAnswer templateAnswer : previousStep) {
			if (templateAnswer.getActive() == 1) {
				answersCount++;
				currentStep.add(templateAnswer);
			}
		}
		if (answersCount < answersxQuestionNumber) {
			if (result == Collections.EMPTY_LIST) {
				result = new ArrayList<Message>();
			}
			result
					.add(new Message(
							"validation.question.notEnoughActiveAnswers",
							new Object[] { templateQuestion.getId(),
									answersCount, answersxQuestionNumber },
							Message.MessageType.ERROR));
			return result;
		}

		previousStep.clear();
		previousStep.addAll(currentStep);
		currentStep.clear();


		/*
		 * 3. Check if there are at least one solution
		 * 
		 */
		int activeAnswersCount = answersCount;
		answersCount = 0;
		for (TemplateExamAnswer templateAnswer : previousStep) {
			if (templateAnswer.getSolution() == 1) {
				answersCount++;
				currentStep.add(templateAnswer);
			}
		}
		if (answersCount == 0) {
			if (result == Collections.EMPTY_LIST) {
				result = new ArrayList<Message>();
			}
			result.add(new Message("validation.question.noCorrectResponse",
					new Object[] { templateQuestion.getId() },
					Message.MessageType.ERROR));
			return result;

		}

		/*
		 * 4. Check if all active response are solutions
		 * 
		 */
		if (activeAnswersCount == answersCount  && templateQuestion.getType()==0) {
			if (result == Collections.EMPTY_LIST) {
				result = new ArrayList<Message>();
			}
			result.add(new Message(
					"validation.question.allActiveAnswerAreCorrect",
					new Object[] { templateQuestion.getId() },
					Message.MessageType.WARN));
			return result;
		}

		/*
		 * 5. Check if the configured answer number is greater than all active solutions
		 * 
		 */
		if (answersxQuestionNumber < answersCount) {
			if (result == Collections.EMPTY_LIST) {
				result = new ArrayList<Message>();
			}
			result.add(new Message(
					"validation.question.ConfiguredAnswersLessThanSolution",
					new Object[] { templateQuestion.getId(), answersxQuestionNumber, answersCount },
					Message.MessageType.WARN));
			return result;
		}
		
		return result;
	}

	private List<Message> validateConfigExamDates(TemplateExam exam) {
		List<Message> result = Collections.EMPTY_LIST;
		int duration = exam.getDuration();
		long startDate = exam.getStartDate().getTime();
		long endDate = exam.getEndDate().getTime();
		if (startDate > endDate) {
			if (result == Collections.EMPTY_LIST) {
				result = new ArrayList<Message>();
			}
			result.add(new Message("validation.startDateAfterEndDate",
					new Object[] { exam.getTitle(), exam.getStartDate(), exam.getEndDate() },
					Message.MessageType.WARN));
		}

		/*long datesDuration = (endDate - startDate) / (1000 * 60);
		if (datesDuration < duration) {
			if (result == Collections.EMPTY_LIST) {
				result = new ArrayList<Message>();
			}
			result
					.add(new Message(
							"validation.durationImpossibleInDates",
							new Object[] {exam.getTitle(), exam.getStartDate(),
									exam.getEndDate(), duration },
							Message.MessageType.WARN));
		}*/

		return result;
	}

	public List<ExamStats> getExamStatsByGroup(Group group) {
		
		
		List<CalifData> califList = statsDAO.getCalifsData(group);
		int learnersNumber = -1;
		int groupLearners = groupDAO.getStudents(group).size();
		
		List<ExamStats> statList = new ArrayList<ExamStats>();
 		
		 for (CalifData data: califList)
		 {
			 			 
			 Long id = data.getIdexam();
			 double grade = data.getGrade();
			 int time = data.getTime();
			 			 		 			
			 double maxGradeExam = data.getMaxGrade();
			 
			 ExamStats stat = new ExamStats();
			 stat.setId(id);
			 
			 stat.setTitle(data.getTitle());
			 stat.setDuration(data.getDuration());
			 stat.setMaxGrade(data.getMaxGrade());
			 ConfigExam ce = new ConfigExam();
			 ce.setId(id);
			 ce = configExamDAO.getConfigExamFromId(ce);
			 stat.setCustomized(ce.isCustomized());
			 if(ce.isCustomized()){
				 learnersNumber = getUsersInCustomExam(id).size();
			 }else{
				 learnersNumber = groupLearners;
			 }
			 stat.setLearnersNumber(learnersNumber);
			 
			 
			 stat.setDoneNumber(0);
			 stat.setPassNumber(0);
			 
			 stat.setGradeMax(Double.MIN_VALUE);
			 stat.setGradeMin(Double.MAX_VALUE);
			 stat.setGradeAverage(0);
			 stat.setGradeMedian(0);
			 stat.setGradeStandardDeviation(0);
		 
			 stat.setTimeMax(Integer.MIN_VALUE);
			 stat.setTimeMin(Integer.MAX_VALUE);
			 stat.setTimeAverage(0);
			 stat.setTimeMedian(0);
			 stat.setTimeStandardDeviation(0);
		 							 
			 stat.setGradeList(new ArrayList<Double>());
			 stat.setTimeList(new ArrayList<Integer>());
			 
			 int pos = statList.indexOf(stat);
			 if (pos != -1)
				 stat = statList.get(pos);
			 else
				 statList.add(stat);
			 
			 stat.setDoneNumber(stat.getDoneNumber() + 1);
			 
			 // about grade
			 
			 stat.getGradeList().add(grade);
			 
			 stat.setGradeAverage(stat.getGradeAverage() + grade);
			 
			 if (grade < stat.getGradeMin())
				 stat.setGradeMin(grade);
			 
			 if (grade > stat.getGradeMax())
				 stat.setGradeMax(grade);
			 
			 // about time
			 
			 stat.getTimeList().add(time);
			 
			 stat.setTimeAverage(stat.getTimeAverage() + time);
			 
			 if (time < stat.getTimeMin())
				 stat.setTimeMin(time);
			 
			 if (time > stat.getTimeMax())
				 stat.setTimeMax(time);
			 
			 // Learner that has been passed the exam
			 if (grade >= (maxGradeExam / 2))
				 stat.setPassNumber(stat.getPassNumber() + 1);
			 			 			 
			 
		 } 		 
		 
		 // Average
		 
		 for (ExamStats stat: statList)
		 {
			 stat.setGradeAverage(stat.getGradeAverage() / stat.getDoneNumber());
			 stat.setTimeAverage(stat.getTimeAverage() / stat.getDoneNumber());
		 }
		 
		 // Standard deviation and median
		 
		 for (ExamStats stat: statList)
		 {
			 ArrayList<Double> gradeList = stat.getGradeList();
			 ArrayList<Integer> timeList = stat.getTimeList();
			 
			 // median
			 
			 Collections.sort(gradeList);
			 Collections.sort(timeList);
			 int size = gradeList.size(); // equals to timeList.size()
			 
			 if (size % 2 == 1)
			 {
				 // size impar
				 stat.setGradeMedian(gradeList.get((size-1)/2));
				 stat.setTimeMedian(timeList.get((size-1)/2));
			 }
			 else
			 {
				 // size par
				 stat.setGradeMedian((gradeList.get((size/2)-1) + gradeList.get(size/2)) / 2);
				 stat.setTimeMedian((timeList.get((size/2)-1) + timeList.get(size/2)) / 2);
			 }
			 
			 // standard deviation for grades
			 
			 double average = stat.getGradeAverage();
			 double sum = 0;
			 
			 for (Double grade: gradeList)
			 	 sum = sum + Math.pow(grade - average, 2);
				 
			 sum = sum / size;
			 stat.setGradeStandardDeviation(Math.sqrt(sum));
			 
			 //	standard deviation for times
			 
			 average = stat.getTimeAverage();
			 sum = 0;
			 
			 for (Integer time: timeList)
			 	 sum = sum + Math.pow(time - average, 2);
				 
			 sum = sum / size;
			 stat.setTimeStandardDeviation(Math.sqrt(sum));
			 
		 }
		 
		 return statList;
		
	}
	
	public double getGradeScale()
	{
		// Fixed ad-hoc by the moment
		return 10.0;
	}
	
	
	public List<LearnerStats> getLearnerStatsByGroup(Group group)
	{
				 
		 List<CalifData> califList = statsDAO.getCalifsData(group);
		 List<LearnerStats> statList = new ArrayList<LearnerStats>();
		 		
		 for (CalifData data: califList)
		 {
			 			 
			 Long id = data.getIdalu();
			 double grade = data.getGrade();		 			 			 
			 double maxGradeExam = data.getMaxGrade();
			 
			 // The grade is scaled by gradeScale in order to assure the same scale over different exams  
			 double gradeScale = this.getGradeScale();		 
			 grade = ((grade / maxGradeExam) * gradeScale);
			 			 
			 LearnerStats stat = new LearnerStats();
			 stat.setId(id);
			 stat.setName(data.getName());
			 stat.setSurname(data.getSurname());
			 stat.setUsername(data.getUsername());
			 stat.setExamsNumber(0);
			 stat.setFailedNumber(0);
			 stat.setGradeMax(grade);
			 stat.setGradeMin(grade);
			 
			 stat.setGradeList(new ArrayList<Double>());
					 
			 stat.setGradeAverage(0);
			 stat.setGradeMedian(0);
			 stat.setGradeStandardDeviation(0);
			 
			 int pos = statList.indexOf(stat);
			 if (pos != -1)
				 stat = statList.get(pos);
			 else
				 statList.add(stat);
			 
			 stat.setExamsNumber(stat.getExamsNumber() + 1);
			 
			 stat.getGradeList().add(grade);
			 
			 stat.setGradeAverage(stat.getGradeAverage() + grade);
			 
			 if (grade < stat.getGradeMin())
				 stat.setGradeMin(grade);
			 
			 if (grade > stat.getGradeMax())
				 stat.setGradeMax(grade);
			 
			 //if (grade >= (maxGradeExam / 2))
			 if (grade >= (gradeScale / 2))
				 stat.setPassedNumber(stat.getPassedNumber() + 1);
			 else
				 stat.setFailedNumber(stat.getFailedNumber() + 1);
			 			 
			 
		 } 		 
		 
		 // Average
		 
		 for (LearnerStats stat: statList)
		 {
			 stat.setGradeAverage(stat.getGradeAverage() / stat.getExamsNumber());
		 }
		 
		 // Standard deviation and median
		 
		 for (LearnerStats stat: statList)
		 {
			 ArrayList<Double> gradeList = stat.getGradeList();
			 
			 // median
			 
			 Collections.sort(gradeList);
			 int size = gradeList.size();
			 
			 if (size % 2 == 1)
				 // size impar
				 stat.setGradeMedian(gradeList.get((size-1)/2));
			 else
				 // size par
				 stat.setGradeMedian((gradeList.get((size/2)-1) + gradeList.get(size/2)) / 2);
			 
			 
			 // standard deviation
			 
			 double average = stat.getGradeAverage();
			 double sum = 0;
			 
			 for (Double grade: gradeList)
			 {
				 sum = sum + Math.pow(grade - average, 2);
				 
			 }
				 
			 sum = sum / size;
			 
			 stat.setGradeStandardDeviation(Math.sqrt(sum));
			 
			 
		 }
		 
		 
		 return statList;

		 			
	}
	
	private List<QuestionStats> calculateQuestionStats(List<AnsweredQuestionData> answeredQuestionsList){
		List<QuestionStats> statList = new ArrayList<QuestionStats>();
 		
		 for (AnsweredQuestionData data: answeredQuestionsList)
		 {
			 Long id = data.getId();			 
			 
			 QuestionStats stat = new QuestionStats();
			 stat.setId(id);
			 stat.setTitle(data.getTitle());
			 stat.setText(data.getText());
			 stat.setComment(data.getComment());
			 stat.setSubject(data.getSubject());
			 stat.setAppeareances(0);
			 stat.setAnswers(0);
			 stat.setSuccesses(0);
			 
			 int pos = statList.indexOf(stat);	 
			 if (pos != -1)
				 stat = statList.get(pos);
			 else
				 statList.add(stat);
			 			 
			 stat.setAppeareances(stat.getAppeareances() + 1);			 
			
			 if (data.isAnswered())
			 {
				 stat.setAnswers(stat.getAnswers() + 1);		 	 				
				 if (data.isAnsweredWithSuccess())				 
					 stat.setSuccesses(stat.getSuccesses() + 1);				
			 }
			 if(data.isConfidenceLevelActive()){
				 
				 if (data.isAnswered())
				 {
					 stat.setConfidenceLevelAnswers(stat.getConfidenceLevelAnswers()+1);		 	 								
				 }
				 
				 stat.setConfidenceLevelAppeareances(stat.getConfidenceLevelAppeareances()+1);
				// si la pregunta tenia el nivel de confianza activo
				 if(data.isAnsweredWithSuccess()){
					//y además está bien contestada
					 if(data.isConfidenceLevelMarked()){
						//y además el nivel de confianza lo ha marcado
						 stat.setTrueEasiness(stat.getTrueEasiness()+1);
					 }else{
						 stat.setInsecurityEasiness(stat.getInsecurityEasiness()+1);
					 }
				 }else{
					 //si la pregunta está mal contestada
					 if(data.isConfidenceLevelMarked()){
						 //se ha marcado el nivel de confianza estando mal la pregunta
						 stat.setSpectiveEasiness(stat.getSpectiveEasiness()+1);
					 }else{
						 stat.setInsecurityEasiness(stat.getInsecurityEasiness()+1);
					 }
				 }
			 }
		 } 		 
		 
		 return statList;
	}
		
	public List<QuestionStats> getQuestionStatsByGroup(Group group)
	{	   
		 List<AnsweredQuestionData> answeredQuestionsList = statsDAO.getAnsweredQuestionsData(group);
		 List<QuestionStats> questionStats = calculateQuestionStats(answeredQuestionsList);
		 statsDAO.fillQuestionStatsByGroup(questionStats,group);
		 return questionStats;
	}

	
	public List<QuestionStats> getQuestionStatsByExam(ConfigExam currentExam)
	{	   
		 List<AnsweredQuestionData> answeredQuestionsList = statsDAO.getAnsweredQuestionsData(currentExam);
		 List<QuestionStats> questionStats = calculateQuestionStats(answeredQuestionsList);
		 statsDAO.fillQuestionStatsByExam(questionStats,currentExam);
		 return questionStats;
	}
	
	public void deleteSubject(Subject theme) {
		subjectDAO.deleteSubject(theme);
		return;
	}

	public Subject saveSubject(Subject theme) {
		return subjectDAO.saveSubject(theme);
	}


	public User getUserData(String studentUserName) {
		User user = userDAO.getUser(studentUserName);
		if (user == null) return null;
		else return user;
	}
	
	private Double gradeFillQuestion(ExamQuestion question,Exam currentExam,double maxGradePerQuestion){
		double questionGrade = 0.0;
		boolean correct = true;
		List<ExamAnswer> answers = question.getAnswers();
		if(answers.size()==1){
			ExamAnswer answer = answers.get(0);
			String textLearnerAnswer = null;
			if(question!=null && question.getLearnerFillAnswer()!=null){
				textLearnerAnswer = question.getLearnerFillAnswer().toLowerCase();
				questionGrade = calculateEntropy(answer,textLearnerAnswer,maxGradePerQuestion);
			}
			if(questionGrade != maxGradePerQuestion){
				correct = false;
			}
			if(currentExam.isConfidenceLevel() && question.isMarked()){
				if(questionGrade==0.0){
					questionGrade-=(maxGradePerQuestion*currentExam.getPenConfidenceLevel());
				}else{
					questionGrade+=(maxGradePerQuestion*currentExam.getRewardConfidenceLevel());
				}
			}
			if(!currentExam.isPartialCorrection() && !correct){
				if(!textLearnerAnswer.trim().equalsIgnoreCase("")){
					questionGrade-=(maxGradePerQuestion*currentExam.getPenQuestionFailed());
				}else{
					questionGrade-=(maxGradePerQuestion*currentExam.getPenQuestionNotAnswered());
				}
			}
		}else
			return 0.0;
		
		return questionGrade;
	}
	
	
	private Double calculateEntropy(ExamAnswer answer, String textLearnerAnswer, double maxGradePerQuestion){
		double result = maxGradePerQuestion;
		if(answer.getText().toLowerCase().equalsIgnoreCase(textLearnerAnswer)){
			return result*1.0;
		}else{
			return result*0.0;
		}
	}
	
	private Double gradeQuestion(ExamQuestion question, Exam currentExam, double maxGradePerQuestion){
		if(question.getType()==0)
			return gradeTestQuestion(question, currentExam, maxGradePerQuestion);
		else
			return gradeFillQuestion(question, currentExam, maxGradePerQuestion);
	}
	
	/**
	 * Calculates a question's grade.
	 * Grade depends on evaluation method choosen at exam configuration time (partial correction or no partial correction)
	 * Partial correction means that a question will be graded according to its correct and incorrect answers marked, and its corresponding bonus and penalty
	 * No partial correction means that a question will be graded to failed if there were one or more incorrect answers marked, or there were not marked all of its correct answers
	 * Both cases have penalty for question not answered
	 * 
	 * @param question Question to be graded
	 * @param currentExam Exam containing the question to be graded
	 * @param maxGradePerQuestion 
	 * @return Grade obtained
	 */
	private Double gradeTestQuestion(ExamQuestion question, Exam currentExam, double maxGradePerQuestion){
		
		double questionGrade = 0.0;
		double numCorrectAnswers = question.getNumCorrectAnswers();
		double numCorrectMarkedAnswers = 0.0;
		double numIncorrectMarkedAnswers = 0.0;
		
		//Correct and incorrect marked answers lists will be needed to calculate and save to DB each answer grade
		List<ExamAnswer> correctMarkedAnswers = new ArrayList<ExamAnswer>();
		List<ExamAnswer> incorrectMarkedAnswers = new ArrayList<ExamAnswer>();
		
		//Get correct and incorrect answers marked
		for (ExamAnswer answer : question.getAnswers()) {
			if (answer.getMarked()) 
				if (answer.getValue() != 0) {	
					numCorrectMarkedAnswers ++;
				}
				else {
					numIncorrectMarkedAnswers ++;
				}
		}
		
		//Grade the question according to correct and incorrect answers and the evaluation method choosen
		
		//If the question has no correct answers
		if (numCorrectAnswers == 0)
			//Question will be correct if it has no answers marked
			if (numCorrectMarkedAnswers == 0 && numIncorrectMarkedAnswers==0)
				questionGrade = maxGradePerQuestion;
			else {
				//Depending on evaluation method
				if (currentExam.isPartialCorrection())
					//At partial correction, question with no correct answers but answered will be graded at minQuestionGrade
					questionGrade = maxGradePerQuestion * currentExam.getMinQuestionGrade();
				else 
					//No partial correction grades question to penalty for questions failed
					questionGrade = -(maxGradePerQuestion * currentExam.getPenQuestionFailed());
								
			}
		else{
			//If the question was not answered
			if (numCorrectMarkedAnswers == 0 && numIncorrectMarkedAnswers == 0)
				questionGrade = -(maxGradePerQuestion * currentExam.getPenQuestionNotAnswered());
			//No database update needed (No answers marked!!!)
			else {
				//If exam has been configured to a partial correction, each question will be qualified attending to the number of correct and incorrect marked answers, with its corresponding penalty
				if (currentExam.isPartialCorrection()){
					//If the question was perfectly answered, its grade is maxGradePerQuestion (avoids rounding problems when the question is perfectly answered)
					if (numCorrectMarkedAnswers == numCorrectAnswers && numIncorrectMarkedAnswers == 0) {
						questionGrade = maxGradePerQuestion;
					}
					else {
						//Adds grade for each correct answer marked
						questionGrade += (maxGradePerQuestion / numCorrectAnswers) * numCorrectMarkedAnswers;
						//Subtracts penalty for incorrect answer marked, multiplied by the number of incorrect answers marked
						questionGrade -= (maxGradePerQuestion * currentExam.getPenAnswerFailed()) * numIncorrectMarkedAnswers;
						//If calculated grade for the question is less than minQuestionGrade configured by tutor for this exam, question grade will be minQuestionGrade
						if (questionGrade < maxGradePerQuestion * currentExam.getMinQuestionGrade()) {
							questionGrade = maxGradePerQuestion * currentExam.getMinQuestionGrade();
						}
						
					}
				}
				//Else, the question will be qualified to maxGradePerQuestion if it was correctly answered, or it will be qualified to the penalty for question failed
				else
					if (numCorrectMarkedAnswers == numCorrectAnswers && numIncorrectMarkedAnswers == 0) {
						questionGrade = maxGradePerQuestion;
					}
					else {
						questionGrade = -(maxGradePerQuestion * currentExam.getPenQuestionFailed());
					}			
			}
		}
		
		if(currentExam.isConfidenceLevel()){
			if(numCorrectAnswers == numCorrectMarkedAnswers && numIncorrectMarkedAnswers==0){
				if(question.isMarked()){
					questionGrade += maxGradePerQuestion*currentExam.getRewardConfidenceLevel();
				}
			}else{
				if(question.isMarked()){
					questionGrade -= maxGradePerQuestion*currentExam.getPenConfidenceLevel();
				}
			}
		}
		
		
		//Its possible that questionGrade equals -0 (For example, when penalty for question not answered is equal to 0)
		//Web interface could show -0.0 grade for that question, and it wont be elegant.
		//This code corrects that
		if (questionGrade == -0.0) return 0.0;
		else return questionGrade;
		}
	
	public Exam getStudentExam(Long idstd, Long idexam) {
		// Same performing like "LearnerManagementService.getAlreadyDoneExam", but we need a user object.
		User user = getUserData(idstd);
		if (user == null) return null;
		
		Exam exam = examDAO.getAlreadyDoneExam(user, idexam);

				
		/* The punctuation for each question is not stored in the database, just
		 * the punctuation for each answer. That's why it is calculated...
		 */
		
		int numberOfQuestions = exam.getQuestions().size();
		double maxGrade = exam.getMaxGrade();
		double maxGradePerQuestion = ((double) maxGrade)
				/ ((double) numberOfQuestions);
			

		for (ExamQuestion question : exam.getQuestions()) {
			
			double questionGrade = 0.0;
			/*
					
	
			for (ExamAnswer answer : question.getAnswers()) {
				
				// the answerGrade (and the questionGrade) only changes (initially is 0)
				// if the answer is marked (getMarked == true) and it is a correct answer (getValue != 0)
				
				// the questionGrade is calculated using the number of correct marked answers
				// (instead of using the answersGrades)
				// in order to avoid rounding problems
							
				if (answer.getMarked())
					if (answer.getValue() != 0)	{
						//double answerGrade = maxGradePerQuestion * answer.getValue() / 100.0;
						//questionGrade += answerGrade;
						numCorrectMarkedAnswers ++;
					}
					else
						numIncorrectMarkedAnswers ++;
				
			}
			
			
			questionGrade = numCorrectMarkedAnswers / numCorrectAnswers * maxGradePerQuestion;	
					
			question.setQuestionGrade(questionGrade);*/
				questionGrade = gradeQuestion(question, exam, maxGradePerQuestion);
				question.setQuestionGrade(questionGrade);
		}

		// The total grade of the exam is retrieved from the database
		return exam;
	}

	public List<User> getStudentsNotRegistered(Group currentGroup) {
		List<User> result = null;
		result = groupDAO.getStudentsNotRegistered(currentGroup);
		
		return result;
	}

	public List<User> getStudentsNotRegistered(Group currentGroup, String role) {
		// TODO: tener en cuenta el rol
		List<User> result = null;
		result = groupDAO.getStudentsNotRegistered(currentGroup,role);
		
		return result;
	}

	public User getUserData(Long id) {
		User user = userDAO.getUser(id);
		if (user == null) return null;
		else return user;
	}

	public void registerStudent(User student, Group group) {
		userDAO.registerStudent(student,group);
		return;
	}

	public void unRegisterStudent(User student, Group group) {
		userDAO.unRegisterStudent(student,group);
		return;
	}
	
	public boolean saveStudent(User student, Institution inst) {
		Boolean res = true;
		if (student.getId() == null) {
			// Only inserts a new user when the userName is available:
			if (this.userNameIsAvailable(student.getUserName())) {
				userDAO.saveUser(student,inst);
			} else {
				res = false;
			}
		} else {
			userDAO.updateUser(student,inst);
		}
		return res;
	}

	public boolean userNameIsAvailable(String userName) {
		// Check the user name
		User usu = userDAO.getUser(userName);
		// If the user name does not exits, the user object will be empty:
		return (usu == null);
	}

	public Subject getSubject(Long idSbj) {
		Subject sbj = subjectDAO.getSubject(idSbj);
		if (sbj == null) return null;
		else return sbj;
	}

	public boolean importQuestions(Group group, Subject sbj, List<TemplateExamQuestion> qList, String sourcePath) {
		// Iterate in all questions:
		Iterator<TemplateExamQuestion> iterQ = qList.iterator();
		TemplateExamQuestion q = null;
		
		while (iterQ.hasNext()) {
			q = iterQ.next();
			// Reset the Id in order to save the question as a new one
			q.setId(null);
			// Reset the "usedInExam" because is a new question
			q.setUsedInExam(false);
			// Sets the new theme
			q.setSubject(sbj);
			// Sets the new group
			q.setGroup(group);
			q.setVisibility(Constants.GROUP);
			// Saves the question into the database
			templateExamQuestionDAO.save(q);

			// Question mmedia files:
			List<MediaElem> qmm = q.getMmedia();
			Iterator<MediaElem> iterQMM = qmm.iterator();
			while (iterQMM.hasNext()) {
				MediaElem mm = iterQMM.next();
				// Duplicate files and modify the path
				String lastPath = mm.getPath();
				mm.setPath(duplicateMmediaFile(sourcePath,mm.getPath()));
				if(mm.getPath()==null){
					mm.setPath(lastPath);
				}
				// Resets the id
				mm.setId(null);
				// Saves the mmedia in the question
				if(mm.getPath()!=null)
					saveMediaElemToQuestion(q, mm);
			}
			// Copy the comments
			List<MediaElem> cmm = q.getMmediaComment();
			Iterator<MediaElem> iterCMM = cmm.iterator();
			while (iterCMM.hasNext()) {
				MediaElem mm = iterCMM.next();
				// Duplicate files and modify the path
				String lastPath = mm.getPath();
				mm.setPath(duplicateMmediaFile(sourcePath,mm.getPath()));
				if(mm.getPath()==null){
					mm.setPath(lastPath);
				}
				// Resets the id
				mm.setId(null);
				// Saves the mmedia in the question
				if(mm.getPath()!=null)
					saveMediaElemToComment(q, mm);
			}
			
			// Copy the answers
			List<TemplateExamAnswer> anwsList = q.getAnswers();
			Iterator<TemplateExamAnswer> iterAns = anwsList.iterator();
			while (iterAns.hasNext()) {
				TemplateExamAnswer answer = iterAns.next();
				// Reset the id to get duplicate:
				answer.setId(null);
				// Sets the new question (it's a new one)
				answer.setQuestion(q);
				// Reset the "usedInExam" because is a new answer
				answer.setUsedInExam(false);
				saveAnswer(answer);
				// Answer mmedia files:
				List<MediaElem> amm = answer.getMmedia();
				Iterator<MediaElem> iterAMM = amm.iterator();
				while (iterAMM.hasNext()) {
					MediaElem mm = iterAMM.next();
					// Duplicate files and modify the path
					mm.setPath(duplicateMmediaFile(sourcePath,mm.getPath()));
					// Resets the id to duplicate
					mm.setId(null);
					
					if(mm.getPath()==null){
						mm.setPath("");
					}
					// Saves the mmedia in the answer
					saveMediaElemToAnswer(answer, mm);
				}
			}
			
		}
		
		return true;
	}

	
    public String randomFilename(){
    	Random rnd = new Random();
    	StringBuffer name = new StringBuffer(Constants.MMEDIAPREFIX);
    	for (int i=0; i<10; i++) {
    		name.append(rnd.nextInt(10));
    	}
    	return name.toString();
    }
	

    /**
     * Duplicates a Mmedia file (concats a "med" string as a prefix)
     * @param sourcePath	Source directory path
     * @param srcFile		Source file name
     * @return Name of the destination (copy) file, null if a problem arises
     */
    private String duplicateMmediaFile(String sourcePath,String srcFile) {
    	// Gets a new random name for the mmedia file:
		// First the name and extension
		String filename;
		String extension ="";
		try{
			extension = srcFile.substring(srcFile.lastIndexOf("."));
		}catch(Exception e){
			// Si no tiene extensión es un archivo de youtube
			extension ="";
		}
		// New file
		File saveFile;
        do {
        	filename = randomFilename()+extension;
        	saveFile = new File(sourcePath+filename);
        } while (saveFile.exists());
		// Copy the file:
		try {
			//First, open source file
			FileInputStream reader = new FileInputStream(sourcePath+srcFile);
			//Second, create & open destination file
			//This way prevent from creepy destination files if source reading fails
			FileOutputStream writer = new FileOutputStream(sourcePath+filename);
			byte []buffer = new byte[Constants.FILEBUFSIZE];
			while (reader.read(buffer) != -1)
				writer.write(buffer);
			reader.close();
			writer.close();
		} 
		catch (Exception e) {
			return null;
		}
        
        return filename;
    }

	public void deleteStudentGradeAndExam(Long idstd, Long idex) {
		// Deleting grade
		this.statsDAO.deleteStudentGrade(idstd,idex);
		// Deleting exam
		this.examDAO.deleteStudentExam(idstd,idex);
	}
    
	/**
	 * Deletes a file from the multimedia path
	 * @param filename file name
	 */
	public boolean deleteMmediaFile (String filename) {
		try {
			File uploadedFile = new File(rootPath.getFile(), filename);
			if (!uploadedFile.delete()) {
				log.error("No se pudo borrar "+filename);
				return false;
			}
							
			/* If the media element is an applet we can't change its filename, so we store it into
    		 * a folder with a random name, then this folder has to be deleted */
						
			String extension = filename.substring(filename.lastIndexOf(".")+1).toLowerCase();
			
			if (extension.equals("class")) {
				File parentDirectory = uploadedFile.getParentFile(); 
				parentDirectory.delete();
			}
			
			log.info("Borrado fichero "+filename);
			return true;

						
		} catch (Exception e) {
			log.error("No se puede borrar: "+e.getMessage());
			return false;
		}
		
	}
	
	public boolean syllabusCopy (Group sourceGroup, Group destinationGroup){
		
		// Get both group syllabus, ordered asc
		// Check that db mapping doesn't initialize subject's "group" member
		List<Subject> syllabusSource = subjectDAO.getSubjectsOrderedAsc(sourceGroup.getId());
		List<Subject> syllabusDest = subjectDAO.getSubjectsOrderedAsc(destinationGroup.getId());
		
		// Gets how many subjects are on syllabus dest, to put copy subjects at the end
		int numSubjectsDest = 0;
		if (syllabusDest != null) numSubjectsDest = syllabusDest.size();
		// If there are no subjects for the destination group, need to instanciate an array list
		else syllabusDest = new ArrayList<Subject>();
		boolean repetido = false;
		boolean saved = false;
		if (syllabusSource != null){
			for (Subject originalSubject : syllabusSource){
				//Obtain subject copy (and, recursively, its questions & answers)
				Subject subjectCopy = subjectCopy(originalSubject,sourceGroup,destinationGroup);
				//Each subject copy would be at the end of the destination group syllabus, but maintaining the order of the source syllabus
				subjectCopy.setOrder(originalSubject.getOrder()+numSubjectsDest);
				//Save to DB
				do{
					repetido = false;
					saved = false;
					for(Subject destSubject : syllabusDest){
						if(subjectCopy.getSubject().trim().equalsIgnoreCase(destSubject.getSubject().trim())){
							repetido = true;
							subjectCopy.setSubject(subjectCopy.getSubject()+"(Copia)");
						}
					}
					if(!repetido){
						subjectDAO.saveSubject(subjectCopy);
						saved = true;
					}
					
				}while(!saved);
			}
			
			return true;
		}
		else return false;
	}
	
	public Subject subjectCopy(Subject originalSubject, Group sourceGroup, Group destinationGroup) {
		//Initializes subject copy with some of original subject values
		Subject subjectCopy = new Subject();
		subjectCopy.setId(null); //When accesing to its DAO to save it, we will get a valid id
		subjectCopy.setGroup(destinationGroup); //Set subject copy group to the destination group
		subjectCopy.setOrder(originalSubject.getOrder());
		subjectCopy.setNumQuestions(originalSubject.getNumQuestions());
		subjectCopy.setSubject(originalSubject.getSubject());
		subjectCopy.setUsedInExam((short)0); //NOT used in exam yet
		
		//Saves subject copy to get an id
		subjectDAO.saveSubject(subjectCopy);
		
		//Gets a list of questions from original subject
		List<TemplateExamQuestion> questionList = subjectDAO.getQuestionsBySubjectAndGroupId(originalSubject.getId(), sourceGroup.getId());
		if (questionList != null) //If not empty list
			for (TemplateExamQuestion oldQuestion : questionList){ //For each question from original subject
				//Set old question copy members to desired values (but, old question will not be modified on DB! Just to get a complete and working old question source)
				//It's done before copyQuestion because it's not possible to save a question to db without having group and subject
				
				//subjectDAO.getQuestionsBySubjectAndGroupId cannot fill oldQuestion group and subject members
				oldQuestion.setGroup(destinationGroup);
				oldQuestion.setSubject(subjectCopy);
				
				//subjectDAO.getQuestionsBySubjectAndGroupId cannot fill answers and media elements from oldQuestion
				templateExamQuestionDAO.fillAnswers(oldQuestion);
				templateExamQuestionDAO.fillMediaElem(oldQuestion);
				
				//Duplicate question (deep clone, see copyQuestion method)
				copyQuestion(oldQuestion,false);
			}
		
		return subjectCopy;
	}
	
	public long getQuestionsNumber(Long subjectId, Long questionType, Long groupId, Long difficulty) {
		return subjectDAO.getQuestionsNumber(subjectId, questionType, groupId, difficulty);
	}
	
	public long getAnswerMinNumber(Long subjectId, Long questionType, Long groupId, Long difficulty){
		return subjectDAO.getAnswerMinNumber(subjectId, questionType, groupId, difficulty);
	}
	
	public long getQuestionsNumber(Subject subject, Group group, Long difficulty) {
		return subjectDAO.getQuestionsNumber(subject.getId(),null, group.getId(), difficulty);
	}

	public List<ExamGlobalInfo> getNextExams() {
		return examDAO.getNextExams();
	}

	public List<ExamGlobalInfo> getActiveExams() {
		return examDAO.getActiveExams();
	}

	public List<Long> getExamIds(TemplateExamQuestion question) {
		return examDAO.getExamIds(question);
	}

	public Long getTotalQuestion(Long idTheme, Long questionType, Long idGroup, Long difficulty) {
		return subjectDAO.getTotalQuestion(idTheme,questionType,idGroup,difficulty);
	}

	public Group getGroupData(String groupName) {
		return groupDAO.getGroupData(groupName);
	}

	public List<User> getUsersNotInCustomExam(Long examId, Long groupId) {
		return userDAO.getUsersNotInCustomExam(examId,groupId);
	}

	public List<CustomExamUser> getUsersInCustomExam(Long examId) {
		return userDAO.getUsersInCustomExam(examId);
	}

	public void addUser2CustomExam(long idExam, long idUser) {
		userDAO.addUser2CustomExam(idExam,idUser);
	}

	public void removeUserFromCustomExam(long userId, Long examId) {
		userDAO.removeUserFromCustomExam(userId,examId);
	}

	public boolean isThemeRepeat(String themeText, long groupId) {
		return subjectDAO.isThemeRepeat(themeText,groupId);
	}

	public boolean isUserInGroup(long idUser, long idGroup) {
		return groupDAO.isUserInGroup(idUser,idGroup);
	}

	public void updateQuestionNotUsedInExam(Long idgrp) {
		templateExamQuestionDAO.updateQuestionNotUsedInExam(idgrp);
	}
}

