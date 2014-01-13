package com.cesfelipesegundo.itis.biz;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Vector;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;

import be.ugent.caagt.jmathtex.TeXConstants;
import be.ugent.caagt.jmathtex.TeXFormula;
import be.ugent.caagt.jmathtex.TeXIcon;
import be.ugent.caagt.jmathtex.mathml.MathMLException;
import be.ugent.caagt.jmathtex.mathml.MathMLParser;

import com.cesfelipesegundo.itis.biz.api.LearnerManagementService;
import com.cesfelipesegundo.itis.dao.ExamDAOImpl;
import com.cesfelipesegundo.itis.dao.api.AnswerExamDAO;
import com.cesfelipesegundo.itis.dao.api.BasicDataExamDAO;
import com.cesfelipesegundo.itis.dao.api.ConfigExamDAO;
import com.cesfelipesegundo.itis.dao.api.ExamDAO;
import com.cesfelipesegundo.itis.dao.api.GroupDAO;
import com.cesfelipesegundo.itis.dao.api.SubjectDAO;
import com.cesfelipesegundo.itis.dao.api.TemplateExamDAO;
import com.cesfelipesegundo.itis.dao.api.TemplateExamQuestionDAO;
import com.cesfelipesegundo.itis.dao.api.TemplateGradeDAO;
import com.cesfelipesegundo.itis.dao.api.UserDAO;
import com.cesfelipesegundo.itis.model.BasicDataExam;
import com.cesfelipesegundo.itis.model.ConfigExam;
import com.cesfelipesegundo.itis.model.ConfigExamSubject;
import com.cesfelipesegundo.itis.model.CustomExamUser;
import com.cesfelipesegundo.itis.model.Exam;
import com.cesfelipesegundo.itis.model.ExamAnswer;
import com.cesfelipesegundo.itis.model.ExamForReview;
import com.cesfelipesegundo.itis.model.ExamGlobalInfo;
import com.cesfelipesegundo.itis.model.ExamQuestion;
import com.cesfelipesegundo.itis.model.Grade;
import com.cesfelipesegundo.itis.model.Group;
import com.cesfelipesegundo.itis.model.MediaElem;
import com.cesfelipesegundo.itis.model.Subject;
import com.cesfelipesegundo.itis.model.TemplateExam;
import com.cesfelipesegundo.itis.model.TemplateExamAnswer;
import com.cesfelipesegundo.itis.model.TemplateExamQuestion;
import com.cesfelipesegundo.itis.model.TemplateExamSubject;
import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.web.Constants;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.html.HtmlEncoder;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * @author  chema
 */
public class LearnerManagementServiceImpl implements LearnerManagementService {
	private static final Log examLog = LogFactory.getLog("com.cesfelipesegundo.itis.exam.journal");
	
	private Resource rootPath;

	/**
	 * @uml.property  name="userDAO"
	 * @uml.associationEnd  
	 */
	private UserDAO userDAO;
	
	
	/**
	 * @uml.property  name="basicDataExamDAO"
	 * @uml.associationEnd  
	 */
	private BasicDataExamDAO basicDataExamDAO;
	
	/**
	 * @uml.property  name="configExamDAO"
	 * @uml.associationEnd  
	 */
	private ConfigExamDAO configExamDAO;

	/**
	 * @uml.property  name="templateExamDAO"
	 * @uml.associationEnd  
	 */
	private TemplateExamDAO templateExamDAO;

	/**
	 * @uml.property  name="answerExamDAO"
	 * @uml.associationEnd  
	 */
	private AnswerExamDAO answerExamDAO;

	/**
	 * @uml.property  name="examDAO"
	 * @uml.associationEnd  
	 */
	private ExamDAO examDAO;

	/**
	 * @uml.property  name="templateExamQuestionDAO"
	 * @uml.associationEnd  
	 */
	private TemplateExamQuestionDAO templateExamQuestionDAO;
	
	/**
	 * @uml.property  name="templateGradeDAO"
	 * @uml.associationEnd  
	 */
	private TemplateGradeDAO templateGradeDAO;
	
	/**
	 * @uml.property  name="groupDAO"
	 * @uml.associationEnd  
	 */
	private GroupDAO groupDAO;
	
	/**
	 * @uml.property  name="subjectDAO"
	 * @uml.associationEnd  
	 */
	private SubjectDAO subjectDAO;

	
	/**
	 * @return
	 * @uml.property  name="subjectDAO"
	 */
	public SubjectDAO getSubjectDAO() {
		return subjectDAO;
	}
	
	/**
	 * @param subjectDAO
	 * @uml.property  name="subjectDAO"
	 */
	public void setSubjectDAO(SubjectDAO subjectDAO) {
		this.subjectDAO = subjectDAO;
	}

	/**
	 * @return
	 * @uml.property  name="groupDAO"
	 */
	public GroupDAO getGroupDAO() {
		return groupDAO;
	}

	/**
	 * @param basicDataExamDAO
	 * @uml.property  name="groupDAO"
	 */
	public void setGroupDAO(GroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}

	/**
	 * @return
	 * @uml.property  name="basicDataExamDAO"
	 */
	public BasicDataExamDAO getBasicDataExamDAO() {
		return basicDataExamDAO;
	}
	
	/**
	 * @param basicDataExamDAO
	 * @uml.property  name="basicDataExamDAO"
	 */
	public void setBasicDataExamDAO(BasicDataExamDAO basicDataExamDAO) {
		this.basicDataExamDAO = basicDataExamDAO;
	}
	
	public Resource getRootPath() {
		return this.rootPath;
	}

	public void setRootPath(Resource rootPath) {
		this.rootPath = rootPath;
	}
	
	/**
	 * @return
	 * @uml.property  name="configExamDAO"
	 */
	public ConfigExamDAO getConfigExamDAO() {
		return configExamDAO;
	}

	/**
	 * @param configExamDAO
	 * @uml.property  name="configExamDAO"
	 */
	public void setConfigExamDAO(ConfigExamDAO configExamDAO) {
		this.configExamDAO = configExamDAO;
	}

	/**
	 * @return
	 * @uml.property  name="templateExamDAO"
	 */
	public TemplateExamDAO getTemplateExamDAO() {
		return templateExamDAO;
	}

	/**
	 * @param templateExamDAO
	 * @uml.property  name="templateExamDAO"
	 */
	public void setTemplateExamDAO(TemplateExamDAO templateExamDAO) {
		this.templateExamDAO = templateExamDAO;
	}
	
	/**
	 * @param userDAO
	 * @uml.property  name="userDAO"
	 */
	public UserDAO getUserDAO() {
		return userDAO;
	}

	/**
	 * @param userDAO
	 * @uml.property  name="userDAO"
	 */
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	/**
	 * @return
	 * @uml.property  name="answerExamDAO"
	 */
	public AnswerExamDAO getAnswerExamDAO() {
		return answerExamDAO;
	}

	/**
	 * @param answerExamDAO
	 * @uml.property  name="answerExamDAO"
	 */
	public void setAnswerExamDAO(AnswerExamDAO answerExamDAO) {
		this.answerExamDAO = answerExamDAO;
	}

	/**
	 * @return
	 * @uml.property  name="examDAO"
	 */
	public ExamDAO getExamDAO() {
		return examDAO;
	}

	/**
	 * @param examDAO
	 * @uml.property  name="examDAO"
	 */
	public void setExamDAO(ExamDAO examDAO) {
		this.examDAO = examDAO;
	}

	/**
	 * @return
	 * @uml.property  name="templateExamQuestionDAO"
	 */
	public TemplateExamQuestionDAO getTemplateExamQuestionDAO() {
		return templateExamQuestionDAO;
	}

	/**
	 * @param templateExamQuestionDAO
	 * @uml.property  name="templateExamQuestionDAO"
	 */
	public void setTemplateExamQuestionDAO(
			TemplateExamQuestionDAO templateExamQuestionDAO) {
		this.templateExamQuestionDAO = templateExamQuestionDAO;
	}
	
	/**
	 * @return
	 * @uml.property  name="templateGradeDAO"
	 */
	public TemplateGradeDAO getTemplateGradeDAO() {
		return templateGradeDAO;
	}

	/**
	 * @param templateGradeDAO
	 * @uml.property  name="templateGradeDAO"
	 */
	public void setTemplateGradeDAO(
			TemplateGradeDAO templateGradeDAO) {
		this.templateGradeDAO = templateGradeDAO;
	}

	/*
	 * Implementation of LearnerManagementService
	 */

	public List<BasicDataExam> getExamsForRevision(Long id) {
		List<BasicDataExam> exams = basicDataExamDAO.getExamsForRevision(id);
		return exams;

	}

	public List<BasicDataExam> getPendingExams(Long id) {
		List<BasicDataExam> exams = basicDataExamDAO.getPendingExams(id);
		return exams;
	}

	public Exam getNewExam(User learner, Long idexam, String ipAddress){
		// If demo user, then no updates on DB should be done
		boolean updateDB;
		
		if (isDemoUser(learner)) updateDB = false;
		else updateDB = true;
		
		try{
			return generateExam(learner.getId(), idexam, ipAddress, updateDB);
		}catch(Exception e){
			return null;
		}
		
	}

	/**
	 *  Private method that checks if a user is only for demo. It checks surname string from user object
	 * @param learner
	 * @return
	 */
	private boolean isDemoUser(User learner) {
		if (learner.getSurname() != null) {
			if (learner.getSurname().equals(Constants.USERDEMOSURNAME)) return true;
			else return false;
		} else {
			// Surname is null
		    return false;
		}
	}

	public Exam getPreviewExam(Long idexam){
		try{
			return generateExam(-1l, idexam, "0.0.0.0", false);
		}catch(Exception e){
			return null;
		}
	}
	
	private Exam generateExam(Long idlearner, Long idexam, String ipAddress, boolean updateDatabase){
		// startingDate of the exam, to be added to califs and each answer of
		// log_exam
		Long startingDate = System.currentTimeMillis();
		
		// A new calif is added with the starting date of the examn
		if(updateDatabase){
			try{
				basicDataExamDAO
						.addNewCalif(idlearner, idexam, ipAddress, startingDate);
			}catch(DataIntegrityViolationException dive){
				examLog.warn("Alumno: "+idlearner + " ha intentado ejecutar nuevamente el examen: "+idexam);
				return null;
			}
		}
		
		TemplateExam templateExam = templateExamDAO.getTemplateExam(idexam);
		Exam exam = new Exam();

		exam.setStartingDate(startingDate);
		ConfigExam ce = new ConfigExam();
		ce.setId(templateExam.getId());
		ce = configExamDAO.getConfigExamFromId(ce);
		exam.setPenConfidenceLevel(ce.getPenConfidenceLevel());
		exam.setRewardConfidenceLevel(ce.getRewardConfidenceLevel());
		exam.setId(templateExam.getId());
		exam.setTitle(templateExam.getTitle());
		exam.setGroup(templateExam.getGroup());

		exam.setDuration(templateExam.getDuration());
		exam.setMaxGrade(templateExam.getMaxGrade());
		
		/* Get new parameters from database v3.6 */
		exam.setPartialCorrection(templateExam.isPartialCorrection());
		exam.setShowNumCorrectAnswers(templateExam.isShowNumCorrectAnswers());
		exam.setPenQuestionFailed(templateExam.getPenQuestionFailed());
		exam.setPenAnswerFailed(templateExam.getPenAnswerFailed());
		exam.setPenQuestionNotAnswered(templateExam.getPenQuestionNotAnswered());
		exam.setMinQuestionGrade(templateExam.getMinQuestionGrade());
		exam.setConfidenceLevel(templateExam.isConfidenceLevel());

		int visibility = templateExam.getVisibility();
		Random generator = new Random();

		List<TemplateExamSubject> templateSubjects = templateExam.getSubjects();
		ListIterator<TemplateExamSubject> itTemplateSubjects = templateSubjects
				.listIterator();

		// Questions of the exam
		List<ExamQuestion> questions = new ArrayList<ExamQuestion>();
		Group examGroup = templateExam.getGroup();

		while (itTemplateSubjects.hasNext()) {
			TemplateExamSubject templateSubject = itTemplateSubjects.next();

			int maxDifficulty = templateSubject.getMaxDifficulty();
			int minDifficulty = templateSubject.getMinDifficulty();
			int answersxQuestionNumber = templateSubject
					.getAnswersxQuestionNumber();
			int questionsNumber = templateSubject.getQuestionsNumber();
			List<TemplateExamQuestion> templateQuestions = templateSubject
					.getQuestions();

			// Supposing that questionsSize >= questionsNumber

			int questionsCount = 0;
			while (questionsCount < questionsNumber) {
				// selection randomly of the question

				int randomQuestion = generator
						.nextInt(templateQuestions.size());
				TemplateExamQuestion templateQuestion = templateQuestions
						.get(randomQuestion);

				boolean visible = false;

				Group questionGroup = templateQuestion.getGroup();
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
				
				if ((templateQuestion.getActive() == 1)
						&& (templateQuestion.getDifficulty() <= maxDifficulty)
						&& (templateQuestion.getDifficulty() >= minDifficulty)
						&& visible) {

					questionsCount++;

					ExamQuestion question = new ExamQuestion();
					questions.add(question);

					question.setComment(templateQuestion.getComment());
					question.setId(templateQuestion.getId());
					question.setMmedia(templateQuestion.getMmedia());
					question.setMmediaComment(templateQuestion.getMmediaComment());
					question.setText(templateQuestion.getText());
					question.setType(templateQuestion.getType());
					question.setNumCorrectAnswers(templateQuestion
							.getNumCorrectAnswers());

					List<TemplateExamAnswer> templateAnswers = templateQuestion
							.getAnswers();

					// Answers of the question
					List<ExamAnswer> answers = new ArrayList<ExamAnswer>();

					// Supposing that answerSize >= answersxQuestionNumber

					// Firsty, the correct answers are selected
					int answersCount = 0;

					ListIterator<TemplateExamAnswer> itTemplateAnswers = templateAnswers
							.listIterator();
					while (itTemplateAnswers.hasNext()) {
						TemplateExamAnswer templateAnswer = itTemplateAnswers
								.next();

						if ((templateAnswer.getSolution() == 1)
								&& (templateAnswer.getActive() == 1))

						{
							answersCount++;

							ExamAnswer answer = new ExamAnswer();

							answer.setId(templateAnswer.getId());
							answer.setMmedia(templateAnswer.getMmedia());
							answer.setSolution(templateAnswer.getSolution());
							answer.setValue(templateAnswer.getValue());
							answer.setText(templateAnswer.getText());
							answer.setMarked(false);

							answers.add(answer);

//							answerExamDAO.addNewExamAnswer(idexam, idlearner,
//									question.getId(), answer.getId(),
//									startingDate);

							// Remove the answer added
							itTemplateAnswers.remove();
							templateAnswer.setUsedInExam(true);
							if(updateDatabase){
								answerExamDAO.updateUsedInExam(templateAnswer);
							}
						}

					} // correct answers

					// Second, some incorrect answers are selected
					// XXXX Not supposing that answerSize >=
					// answersxQuestionNumber
					//

					while ((answersCount < answersxQuestionNumber)
							&& (templateAnswers.size() > 0)) {
						// selection randomly of the answer

						int randomAnswer = generator.nextInt(templateAnswers
								.size());
						TemplateExamAnswer templateAnswer = templateAnswers
								.get(randomAnswer);

						if (templateAnswer.getActive() == 1) {

							ExamAnswer answer = new ExamAnswer();

							answer.setId(templateAnswer.getId());
							answer.setMmedia(templateAnswer.getMmedia());
							answer.setSolution(templateAnswer.getSolution());
							answer.setValue(templateAnswer.getValue());
							answer.setText(templateAnswer.getText());
							answer.setMarked(false);

							answers.add(answer);

//							answerExamDAO.addNewExamAnswer(idexam, idlearner,
//									question.getId(), answer.getId(),
//									startingDate);

							templateAnswer.setUsedInExam(true);
							if(updateDatabase){
								answerExamDAO.updateUsedInExam(templateAnswer);
							}
							
							answersCount++;

						} // if answer can be added

						// Remove the answer added or not selectable

						templateAnswers.remove(randomAnswer);

					} // answers

					// shuffle the answers to avoid the correct answers are the
					// first

					Collections.shuffle(answers);
					question.setAnswers(answers);

					
					templateQuestion.setUsedInExam(true);
					if(updateDatabase){
						templateExamQuestionDAO.updateUsedInExam(templateQuestion);
					}

				} // if question can be added

				// Remove the question added or not selectable
				templateQuestions.remove(randomQuestion);

			} // questions

		} // itSubjects

		Collections.shuffle(questions);

		exam.setQuestions(questions);
		
		
		// to guarantee same order in exam generation and revision
		if(updateDatabase){
			Date fecha = new Date(startingDate);
			for (ExamQuestion question: questions)
			{
				List<ExamAnswer> answers = question.getAnswers();
				
				for (ExamAnswer answer: answers)
				{
					if(question.getType()==0){
						answerExamDAO.addNewExamAnswer(idexam, idlearner,
								question.getId(), answer.getId(),
								startingDate);
					}else{
						answerExamDAO.addNewExamFillAnswer(idexam, idlearner,
								question.getId(), answer.getId(),null,
								startingDate);
					}
					StringBuilder buffer = new StringBuilder();
					
					if(question.getType()==0){
						buffer.append("INSERT INTO log_exams (exam, alu, preg, resp, marcada, puntos, hora_resp) VALUES");
						buffer.append("("+idexam+" , "+idlearner+" , "+question.getId()+" , "+answer.getId()+" , 0 , 0 , '"+fecha.getYear()+"-"+fecha.getMonth()+"-"+fecha.getDate()+" "+fecha.getHours()+":"+fecha.getMinutes()+":"+fecha.getSeconds()+"' )");
					}else if(question.getType()==1){
						buffer.append("INSERT INTO log_exams_fill (exam, alu, preg, puntos, hora_resp) VALUES");
						buffer.append("("+idexam+" , "+idlearner+" , "+question.getId()+" , 0.0 , '"+fecha.getYear()+"-"+fecha.getMonth()+"-"+fecha.getDate()+" "+fecha.getHours()+":"+fecha.getMinutes()+":"+fecha.getSeconds()+"' )");
					}
					examLog.debug(buffer);
				}
				
			}
			examLog.debug("Examen: "+ idexam+" generado para Alumno :"+idlearner+" IP: "+ipAddress);
		}
		
		return exam;
	}

	public int updateExamAnswer(Long idexam, Long iduser, Long idquestion,
			Long idanswer, Boolean marked) {

		int rows = answerExamDAO.updateExamAnswer(idexam, iduser, idquestion, idanswer,
				System.currentTimeMillis(), marked);

		if(examLog.isDebugEnabled()){
			Date fecha = new Date(System.currentTimeMillis());
			StringBuilder buffer = new StringBuilder();
			buffer.append("UPDATE log_exams SET ");
			if(marked!=null)
				buffer.append("marcada = "+marked+", ");
			buffer.append("puntos = 0.0, ");
			buffer.append("hora_resp = '"+fecha.getYear()+"-"+fecha.getMonth()+"-"+fecha.getDate()+" "+fecha.getHours()+":"+fecha.getMinutes()+":"+fecha.getSeconds()+"' ");
			buffer.append("WHERE exam ="+idexam+" AND alu ="+iduser+" AND preg ="+idquestion+" AND resp ="+idanswer);
			examLog.debug(buffer.toString());
		}
		return rows;
	}

	/**
	 * This method should: - Obtain the grade of each individual answer to each
	 * question and update the corresponding rows of log_exams table - Update
	 * the information of table califs: fecha_fin and nota
	 * 
	 * @param id,
	 *            of the user (learner)
	 * @param currentExam,
	 *            that is, the object consisting in the exam performed by the
	 *            user
	 * @return Final grade of the exam
	 */
	public Double gradeExam(Long id, Exam currentExam) {
		return calculateExamGrade(id, currentExam, true, false, false);
	}

	public Double gradePreviewedExam(Exam previewedExam ){
		return calculateExamGrade(-1l, previewedExam, false, false, false);
	}
	
	private Double gradeFillQuestion(ExamQuestion question,Long userId,Exam currentExam,double maxGradePerQuestion,boolean updateDatabase){
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
					//Pregunta contestada incorrectamente
					questionGrade-=(maxGradePerQuestion*currentExam.getPenConfidenceLevel());
				}else{
					//Pregunta contestada correctamente
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
			if(updateDatabase){
				answerExamDAO.updateExamAnswerGrade(currentExam.getId(), userId, question.getId(), answer.getId(), questionGrade);		
			}
		}else
			return 0.0;
		return questionGrade;
	}
	
	
	private Double calculateEntropy(ExamAnswer answer, String textLearnerAnswer, double maxGradePerQuestion){
		double result = maxGradePerQuestion;
		if(answer.getText().toLowerCase().trim().equalsIgnoreCase(textLearnerAnswer.toLowerCase().trim())){
			return result*1.0;
		}else{
			return result*0.0;
		}
	}
	
	private Double gradeQuestion(ExamQuestion question, Long id, Exam currentExam, double maxGradePerQuestion, boolean updateDatabase){
		if(question.getType()==0)
			return gradeTestQuestion(question, id, currentExam, maxGradePerQuestion, updateDatabase);
		else
			return gradeFillQuestion(question, id, currentExam, maxGradePerQuestion, updateDatabase);
	}
	
	/**
	 * Calculates a question's grade.
	 * Grade depends on evaluation method choosen at exam configuration time (partial correction or no partial correction)
	 * Partial correction means that a question will be graded according to its correct and incorrect answers marked, and its corresponding bonus and penalty
	 * No partial correction means that a question will be graded to failed if there were one or more incorrect answers marked, or there were not marked all of its correct answers
	 * Both cases have penalty for question not answered
	 * 
	 * @param question Question to be graded
	 * @param id User id
	 * @param currentExam Exam containing the question to be graded
	 * @param maxGradePerQuestion 
	 * @param updateDatabase If true, grade by answer marked will be save into DB Log
	 * @return Grade obtained
	 */
	private Double gradeTestQuestion(ExamQuestion question, Long id, Exam currentExam, double maxGradePerQuestion, boolean updateDatabase){
		
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
					if (updateDatabase) 
						correctMarkedAnswers.add(answer);
				}
				else {
					numIncorrectMarkedAnswers ++;
					if (updateDatabase)
						incorrectMarkedAnswers.add(answer);
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
				
				if(updateDatabase) {
					//All answers marked were incorrect (question has not correct answers!!)
					double answerGrade = questionGrade / numIncorrectMarkedAnswers;
					for (ExamAnswer answer : incorrectMarkedAnswers)
						answerExamDAO.updateExamAnswerGrade(currentExam.getId(),id, question.getId(), answer.getId(), answerGrade);
				}				
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
						if (updateDatabase) {
							//All answers are correct
							double answerGrade = questionGrade / numCorrectMarkedAnswers;
							for (ExamAnswer answer : correctMarkedAnswers)
								answerExamDAO.updateExamAnswerGrade(currentExam.getId(),id, question.getId(), answer.getId(), answerGrade);
						}
					}
					else {
						//Adds grade for each correct answer marked
						questionGrade += (maxGradePerQuestion / numCorrectAnswers) * numCorrectMarkedAnswers;
						//Subtracts penalty for incorrect answer marked, multiplied by the number of incorrect answers marked
						questionGrade -= (maxGradePerQuestion * currentExam.getPenAnswerFailed()) * numIncorrectMarkedAnswers;
						//If calculated grade for the question is less than minQuestionGrade configured by tutor for this exam, question grade will be minQuestionGrade
						if (questionGrade < maxGradePerQuestion * currentExam.getMinQuestionGrade()) {
							questionGrade = maxGradePerQuestion * currentExam.getMinQuestionGrade();
							//La ponctution de la question est divicé de la même façon pour les reponds corrects et les incorrects
							//Question grade will be divided to equal parts among correct and incorrect answers
							if (updateDatabase) {
								double answerGrade = questionGrade / (numCorrectMarkedAnswers+numIncorrectMarkedAnswers);
								for (ExamAnswer answer : correctMarkedAnswers)
									answerExamDAO.updateExamAnswerGrade(currentExam.getId(),id, question.getId(), answer.getId(), answerGrade);
								for (ExamAnswer answer : incorrectMarkedAnswers)
									answerExamDAO.updateExamAnswerGrade(currentExam.getId(),id, question.getId(), answer.getId(), answerGrade);
							}
						}
						else 
							if (updateDatabase) {
								//Correct answers will be graded to maxGradePerQuestion/numCorrectAnswers
								double answerGrade = maxGradePerQuestion / numCorrectAnswers;
								for (ExamAnswer answer : correctMarkedAnswers)
									answerExamDAO.updateExamAnswerGrade(currentExam.getId(),id, question.getId(), answer.getId(), answerGrade);
								//Incorrect answers will be graded to pre-configured penalty per answer failed
								answerGrade = -(maxGradePerQuestion * currentExam.getPenAnswerFailed());
								for (ExamAnswer answer : incorrectMarkedAnswers)
									answerExamDAO.updateExamAnswerGrade(currentExam.getId(),id, question.getId(), answer.getId(), answerGrade);
						
							}
					}
				}
				//Else, the question will be qualified to maxGradePerQuestion if it was correctly answered, or it will be qualified to the penalty for question failed
				else
					if (numCorrectMarkedAnswers == numCorrectAnswers && numIncorrectMarkedAnswers == 0) {
						questionGrade = maxGradePerQuestion;
						if (updateDatabase) {
							//There are only correct answers. Question grade will be divided to equal parts among them
							double answerGrade = questionGrade / numCorrectAnswers;
							for (ExamAnswer answer : correctMarkedAnswers)
								answerExamDAO.updateExamAnswerGrade(currentExam.getId(),id, question.getId(), answer.getId(), answerGrade);
						}
					}
					else {
						questionGrade = -(maxGradePerQuestion * currentExam.getPenQuestionFailed());
						if (updateDatabase) {
							//The question was failed
							//Correct answers will be graded to 0.0
							for (ExamAnswer answer : correctMarkedAnswers)
								answerExamDAO.updateExamAnswerGrade(currentExam.getId(),id, question.getId(), answer.getId(), 0.0);
							//Question failed penalty will be divided to equal parts among incorrect answers
							double answerGrade = questionGrade / numIncorrectMarkedAnswers;
							for (ExamAnswer answer : incorrectMarkedAnswers)
								answerExamDAO.updateExamAnswerGrade(currentExam.getId(),id, question.getId(), answer.getId(), answerGrade);
						}
					}			
			}
		}
		if(currentExam.isConfidenceLevel()){
			if(numCorrectAnswers == numCorrectMarkedAnswers && numIncorrectMarkedAnswers==0){
				if(question.isMarked()){
					questionGrade += maxGradePerQuestion*currentExam.getRewardConfidenceLevel();
					if (updateDatabase) {
						//All answers are correct
						double answerGrade = questionGrade / numCorrectMarkedAnswers;
						for (ExamAnswer answer : correctMarkedAnswers)
							answerExamDAO.updateExamAnswerGrade(currentExam.getId(),id, question.getId(), answer.getId(), answerGrade);
					}
				}
			}else{
				if(question.isMarked()){
					questionGrade -= maxGradePerQuestion*currentExam.getPenConfidenceLevel();
					if (updateDatabase) {
						//The question was failed
						//Correct answers will be graded to 0.0
						for (ExamAnswer answer : correctMarkedAnswers)
							answerExamDAO.updateExamAnswerGrade(currentExam.getId(),id, question.getId(), answer.getId(), 0.0);
						//Question failed penalty will be divided to equal parts among incorrect answers
						double answerGrade = questionGrade / numIncorrectMarkedAnswers;
						for (ExamAnswer answer : incorrectMarkedAnswers)
							answerExamDAO.updateExamAnswerGrade(currentExam.getId(),id, question.getId(), answer.getId(), answerGrade);
					}
				}
			}
		}
		//Its possible that questionGrade equals -0 (For example, when penalty for question not answered is equal to 0)
		//Web interface could show -0.0 grade for that question, and it wont be elegant.
		//This code corrects that
		if (questionGrade == -0.0) return 0.0;
		else return questionGrade;
	}
	
	private Double calculateExamGrade(Long id, Exam currentExam, boolean updateDatabase, boolean isReview, boolean checkConfigExam) {
		long startingDate = 0;
		long finalDate = 0;
		long maxDate = 0;
		int durationExam = 0;
		//If reviewing an exam, is not needed to calculate dates again
		if (!isReview) {
			//Calculate and check exam duration, starting and ending date
			startingDate = currentExam.getStartingDate();
			//Real ending date
			finalDate = System.currentTimeMillis();
			//Max ending date possible (Starting date + Pre-configured exam duration)
			maxDate = currentExam.getStartingDate() + (currentExam.getDuration()*60000);
			durationExam = 0;
			//If real ending date < max ending date possible => NO correction needed
			if (finalDate < maxDate)
				durationExam = (int) Math.round((finalDate - startingDate) / 60000.0); // in minutes
			//Else, correction to pre-configured exam duration and max ending date possible
			else {
				durationExam = currentExam.getDuration();
				finalDate = maxDate;
				examLog.info("El examen "+currentExam.getId()+" del alumno "+id+"ha registrado un tiempo de realizacion mayor que el maximo previsto. Se ha procedido a su correcion");
			}
			//End date calculation
		}
		double grade = 0.0;
		
		ConfigExam c = new ConfigExam();
		c.setId(currentExam.getId());
		c = configExamDAO.getConfigExamFromId(c);
		
		int numberOfQuestions = 0;
		//Obtain the total number of questions
		for(ConfigExamSubject subjects : c.getSubjects()){
			numberOfQuestions+=subjects.getQuestionsNumber();
		}
		//Obtain the maximun exam's grade and maximun grade per question
		double maxGrade = currentExam.getMaxGrade();
		double maxGradePerQuestion = maxGrade
				/  numberOfQuestions;
		List<ExamQuestion> questions = currentExam.getQuestions();
		if(checkConfigExam){
			for (ExamQuestion question : questions) {
				boolean deleteQuestion = true;
				/*
				 * Recorremos la lista de temas para buscar a que tema pertenece esta pregunta*/
				for(ConfigExamSubject subject : c.getSubjects()){
					Subject theme = subjectDAO.getSubjectByQuestionId(question.getId());
					if(subject.getSubject().getId().equals(theme.getId()) && subject.getQuestionType()==question.getType()){
						long qdif = question.getDifficulty();
						int min = subject.getMinDifficulty();
						int max = subject.getMaxDifficulty();
						if((min==qdif)||(max==qdif)){
							if(subject.getQuestionsNumber()>0){
								subject.setQuestionsNumber(subject.getQuestionsNumber()-1);
								deleteQuestion = false;
								break;
							}
						}
					}
				}
				if(deleteQuestion){
					examDAO.deleteQuestionFromExam(currentExam.getId(), question.getId());
				}
			}
		}
		//For each question
		for (ExamQuestion question : questions) {
			double questionGrade = gradeQuestion(question,id,currentExam,maxGradePerQuestion,updateDatabase);			
			question.setQuestionGrade(questionGrade);
			grade += questionGrade;
		}
		if(updateDatabase){
			boolean checked = false;
			int retries = 0;
			do{
				//If reviewing, no date update needed!!
				if (!isReview)
					basicDataExamDAO.updateCalif(id, currentExam.getId(), finalDate, grade, durationExam);
				else
					basicDataExamDAO.updateCalif(id, currentExam.getId(), grade);
				
				checked = templateGradeDAO.checkGrade(id, currentExam.getId(), grade);
				if (!checked){
					++retries;
					examLog.error("No se ha podido almacenar correctamente la calificacion del examen "+currentExam.getId()+" del alumno "+id+". Se procede al reintento "+retries);
				}
			} while (!checked);
		}
		
		return new Double(grade);
	}
	
	public ExamQuestion getNextQuestion(User user, Long idExam, Long lastQuestionId){
		return examDAO.getNextQuestion(user, idExam, lastQuestionId );
	}

	/**
	 * Method to obtain the data of an already done exam: questions, answers,
	 * comments, grade, marked answers, correct answers, time employed...
	 * 
	 * @param user
	 *            that performed the exam
	 * @param idexam,
	 *            id of the configuration of the exam previously performed
	 * @return
	 */
	public Exam getAlreadyDoneExam(User user, Long idexam) {

		Exam exam = examDAO.getAlreadyDoneExam(user, idexam);

		if (exam != null) {
			/* The punctuation for each question is not stored in the database, just
			 * the punctuation for each answer. That's why it is calculated...
			 */			
			int numberOfQuestions = exam.getQuestions().size();
			double maxGrade = exam.getMaxGrade();
			double maxGradePerQuestion = ((double) maxGrade)
					/ ((double) numberOfQuestions);
			
			for (ExamQuestion question : exam.getQuestions()) {
				double questionGrade = gradeQuestion(question,user.getId(),exam,maxGradePerQuestion,false);
				question.setQuestionGrade(questionGrade);
			}
		    // The total grade of the exam is retrieved from the database
		}
		
		return exam;

	}
	
	public List<ExamForReview> examReviewByQuestion(TemplateExamQuestion question) {
		//1.- Obtain a list of exams in which the question appeared, and users who did it
		List<ExamForReview> examsForReview = examDAO.getExamsByQuestion(question.getId());
		//2.- Recalculate grade for each exam and user
		for (ExamForReview examForReview : examsForReview) {
			//User object just for getAlreadyDoneExam method calling
			User fakeUser = new User();
			fakeUser.setId(examForReview.getIdLearner());
			//Retrieves the exam done by user and re-calculates ("review") its grade
			Exam examDone = getAlreadyDoneExam(fakeUser, examForReview.getIdExam());
			examForReview.setPostGrade(calculateExamGrade(examForReview.getIdLearner(), examDone, true, true,false));
		}
		//3.- Return list containing exams reviewed
		return examsForReview;
	}
	
	public List<ExamForReview> examReviewByIdExam(long idExam) {
		//1.- Obtain a list of exams in which the question appeared, and users who did it
		List<ExamForReview> examsForReview = examDAO.getExamsByIdExam(idExam);
		//2.- Recalculate grade for each exam and user
		for (ExamForReview examForReview : examsForReview) {
			//User object just for getAlreadyDoneExam method calling
			User fakeUser = new User();
			fakeUser.setId(examForReview.getIdLearner());
			//Retrieves the exam done by user and re-calculates ("review") its grade
			Exam examDone = getAlreadyDoneExam(fakeUser, examForReview.getIdExam());
			examForReview.setPostGrade(calculateExamGrade(examForReview.getIdLearner(), examDone, true, true, true));
		}
		//3.- Return list containing exams reviewed
		return examsForReview;
	}
	
	public ByteArrayOutputStream parse2PDF(TemplateExamQuestion question) throws NullPointerException,DocumentException,IOException{
		Document document = new Document();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, baos);
		document.open();
		if(question!=null){
			document.addCreationDate();
			document.addCreator("Herramienta de generacion automatizada para visualizar preguntas en PDF de iTest");
			document.addKeywords("pregunta,"+question.getTitle()+","+question.getId()+".pdf");
			document.addTitle("Pregunta: "+question.getId()+" Titulo: "+question.getTitle()+".pdf");
			
			PdfPTable questionInfo = new PdfPTable(1);
			questionInfo.getDefaultCell().setGrayFill(0.8f);
			questionInfo.getDefaultCell().setBorderWidth(0.0f);
			questionInfo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			questionInfo.addCell("Pregunta "+question.getId()+"  Respuestas correctas: "+question.getNumCorrectAnswers());
				
			questionInfo.setSpacingBefore(40.0f);
			questionInfo.setSpacingAfter(10.0f);
			document.add(questionInfo);
			
			//Body
			PdfPTable questionBody = new PdfPTable(1);
			questionInfo.getDefaultCell().setBorder(3);
			questionInfo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_JUSTIFIED_ALL);
			Paragraph body = new Paragraph();
			
			//Question text
			String text = question.getText();
			Paragraph questionForm = new Paragraph("",FontFactory.getFont(FontFactory.HELVETICA,10.0f,Font.NORMAL,Color.BLACK));
			
			body.add(rellenaParrafo(text, questionForm,true));
			
			
			body.add("\n\n");
			if (question.getMmedia()!=null)
				for (MediaElem mmediaElem : question.getMmedia()) {
					try {
						if (mmediaElem.getType() != MediaElem.IMAGE) throw new DocumentException();
						Image imgElem = Image.getInstance(rootPath.getFile().getAbsolutePath()+File.separatorChar+mmediaElem.getPath());
						imgElem.scaleToFit(200, 200);
						body.add(new Chunk(imgElem,100,0));
						body.add(Chunk.NEWLINE);
						body.add(Chunk.NEWLINE);
					}
					catch(Exception e) {
						body.add("--> Elemento multimedia deberia ir aqui <--\n\n");
					}
				}

			for (TemplateExamAnswer answer : question.getAnswers()) {
				if(question.getType() == 0){
					if (answer.getSolution() == 1 && answer.getMarked()) {
						Paragraph correctAnswer = new Paragraph("\t\t\t\t\t\t[X]\t\t\t",FontFactory.getFont(FontFactory.HELVETICA,10.0f,Font.BOLD,Color.GREEN));
						text = answer.getText();
						body.add(rellenaParrafo(text,correctAnswer,false));
						body.add(Chunk.NEWLINE);
						body.add(Chunk.NEWLINE);
						
					}
					if (answer.getSolution() == 0 && answer.getMarked()) {
						Paragraph incorrectAnswer = new Paragraph("\t\t\t\t\t\t[X]\t\t\t",FontFactory.getFont(FontFactory.HELVETICA,10.0f,Font.BOLD,Color.RED));
						text = answer.getText();
						body.add(rellenaParrafo(text,incorrectAnswer,false));
						body.add(Chunk.NEWLINE);
						body.add(Chunk.NEWLINE);
						continue;
					}
					Paragraph noAnswer = new Paragraph("\t\t\t\t\t\t[   ]\t\t\t",FontFactory.getFont(FontFactory.HELVETICA,10.0f,Font.NORMAL,Color.BLACK));
					text = answer.getText();
					body.add(rellenaParrafo(text,noAnswer,false));
					body.add(Chunk.NEWLINE);
					body.add(Chunk.NEWLINE);
					for (MediaElem mmediaElem : answer.getMmedia()) {
						try {
							if (mmediaElem.getType() == MediaElem.IMAGE) {
								Image imgElem = Image.getInstance(rootPath.getFile().getAbsolutePath()+File.separatorChar+mmediaElem.getPath());
								imgElem.scaleToFit(100, 100);
								body.add(new Chunk(imgElem,50,0));
								body.add(Chunk.NEWLINE);
								body.add(Chunk.NEWLINE);
							}
						}
						catch(Exception e) {
							body.add("--> Elemento multimedia deberia ir aqui <--\n\n");
						}
					}
					continue;
				}else{
					Paragraph bodyAnswer = new Paragraph();
					text = answer.getText();
					bodyAnswer.add(new Phrase("************************************************************"));
					bodyAnswer.add(Chunk.NEWLINE);
					bodyAnswer.add(new Phrase("Respuesta correcta: "+"\""+text+"\""));
					bodyAnswer.add(Chunk.NEWLINE);
					bodyAnswer.add(new Phrase("************************************************************"));
					bodyAnswer.add(Chunk.NEWLINE);
					bodyAnswer.add(new Phrase("Respuesta del alumno: "));
					bodyAnswer.add(Chunk.NEWLINE);
					bodyAnswer.add(new Phrase("************************************************************"));
					body.add(bodyAnswer);
					body.add(Chunk.NEWLINE);
				}
			}
			questionBody.addCell(body);
			document.add(questionBody);
			
			
		}
		document.close();
		
		return baos;
	}
	
	public ByteArrayOutputStream parse2PDF(Exam exam) throws NullPointerException,DocumentException,IOException{
		Document document = new Document();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, baos);
		document.open();
		if (exam!=null) {
			boolean questionAnswered = false;
			//META
			document.addCreationDate();
			document.addCreator("Herramienta de generacion automatizada de examenes iTest");
			document.addKeywords("examen,"+exam.getTitle()+","+exam.getGroup().getCourse().getName()+".pdf");
			document.addTitle("Examen: "+exam.getTitle()+" Asignatura: "+exam.getGroup().getCourse().getName()+".pdf");
			
			//Leyenda
			PdfPTable leyenda = new PdfPTable(1);
			leyenda.getDefaultCell().setGrayFill(0.8f);
			leyenda.getDefaultCell().setBorderWidth(0.0f);
			leyenda.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			leyenda.addCell("Leyenda");
			leyenda.setSpacingAfter(10.0f);
			document.add(leyenda);

			PdfPTable leyenda2 = new PdfPTable(1);
			leyenda2.getDefaultCell().setBackgroundColor(Color.PINK);
			leyenda2.getDefaultCell().setBorder(0);
			leyenda2.addCell("1.- Las respuestas contestadas por el alumno y las respuestas correctas se identifican según:");

			Paragraph respuestaCorrecta = new Paragraph("\t\t\t\t\t\t[X]\t\t\tRespuesta correcta",FontFactory.getFont(FontFactory.HELVETICA,Font.DEFAULTSIZE,Font.BOLD,Color.GREEN));
			Paragraph respuestaIncorrecta = new Paragraph("\t\t\t\t\t\t[X]\t\t\tRespuesta incorrecta",FontFactory.getFont(FontFactory.HELVETICA,Font.DEFAULTSIZE,Font.BOLD,Color.RED));
			Paragraph respuestaNoContestada = new Paragraph("\t\t\t\t\t\t[   ]\t\t\tRespuesta no contestada",FontFactory.getFont(FontFactory.HELVETICA,Font.DEFAULTSIZE,Font.NORMAL,Color.BLACK));
			leyenda2.addCell(respuestaCorrecta);
			leyenda2.addCell(respuestaIncorrecta);
			leyenda2.addCell(respuestaNoContestada);

			leyenda2.addCell("2.- La puntuación obtenida en cada pregunta se muestra al lado del título de la misma");
			leyenda2.addCell("3.- Existen comentarios del profesor al final de algunas preguntas");
			leyenda2.setSpacingAfter(40.0f);
			document.add(leyenda2);

			//Titulo examen
			PdfPTable info = new PdfPTable(1);
			info.getDefaultCell().setBackgroundColor(Color.ORANGE);
			info.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			info.getDefaultCell().setBorder(0);
			info.addCell(new Paragraph("Asignatura: "+exam.getGroup().getCourse().getName()+" ("+exam.getGroup().getName()+")",FontFactory.getFont(FontFactory.HELVETICA,Font.DEFAULTSIZE,Font.UNDERLINE,Color.BLACK)));
			info.addCell(new Paragraph("Titulo: "+exam.getTitle(),FontFactory.getFont(FontFactory.HELVETICA,Font.DEFAULTSIZE,Font.BOLD,Color.BLACK)));
			document.add(info);

			//Preguntas
			int numQuestion = 1;
			for (ExamQuestion question : exam.getQuestions()) {
				//Info
				PdfPTable questionInfo = new PdfPTable(1);
				questionInfo.getDefaultCell().setGrayFill(0.8f);
				questionInfo.getDefaultCell().setBorderWidth(0.0f);
				questionInfo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				if (exam.isShowNumCorrectAnswers())
					questionInfo.addCell("Pregunta "+numQuestion+": "+question.getQuestionGrade()+" puntos ("+question.getNumCorrectAnswers()+" respuestas correctas)");
				else
					questionInfo.addCell("Pregunta "+numQuestion+": "+question.getQuestionGrade()+" puntos");
				questionInfo.setSpacingBefore(40.0f);
				questionInfo.setSpacingAfter(10.0f);
				document.add(questionInfo);
				numQuestion++;
				
				//Body
				PdfPTable questionBody = new PdfPTable(1);
				questionInfo.getDefaultCell().setBorder(3);
				questionInfo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_JUSTIFIED_ALL);
				Paragraph body = new Paragraph();
				
				//Question text
				String text = question.getText();
				Paragraph questionForm = new Paragraph("",FontFactory.getFont(FontFactory.HELVETICA,10.0f,Font.NORMAL,Color.BLACK));
				
				body.add(rellenaParrafo(text, questionForm,true));
				
				
				body.add("\n\n");
				if (question.getMmedia()!=null)
					for (MediaElem mmediaElem : question.getMmedia()) {
						try {
							if (mmediaElem.getType() == MediaElem.IMAGE){
								Image imgElem = Image.getInstance(rootPath.getFile().getAbsolutePath()+File.separatorChar+mmediaElem.getPath());
								imgElem.scaleToFit(200, 200);
								body.add(new Chunk(imgElem,100,0));
								body.add(Chunk.NEWLINE);
								body.add(Chunk.NEWLINE);
							}
						}
						catch(Exception e) {
							body.add("--> Elemento multimedia deberia ir aqui <--\n\n");
						}
					}

				for (ExamAnswer answer : question.getAnswers()) {
					if(question.getType() == 0){
						//ES UNA PREGUNTA DE TIPO TEST
						if (answer.getSolution() == 1 && answer.getMarked()) {
							//ES UNA PREGUNTA CORRECTA
							Paragraph correctAnswer = new Paragraph("\t\t\t\t\t\t[X]\t\t\t",FontFactory.getFont(FontFactory.HELVETICA,10.0f,Font.BOLD,Color.GREEN));
							text = answer.getText();
							body.add(rellenaParrafo(text,correctAnswer,false));
							body.add(Chunk.NEWLINE);
							body.add(Chunk.NEWLINE);
							questionAnswered = true;
							continue;
						}
						if (answer.getSolution() == 0 && answer.getMarked()) {
							//ES UNA PREGUNTA INCORRECTA
							Paragraph incorrectAnswer = new Paragraph("\t\t\t\t\t\t[X]\t\t\t",FontFactory.getFont(FontFactory.HELVETICA,10.0f,Font.BOLD,Color.RED));
							text = answer.getText();
							body.add(rellenaParrafo(text,incorrectAnswer,false));
							body.add(Chunk.NEWLINE);
							body.add(Chunk.NEWLINE);
							questionAnswered = true;
							continue;
						}
						Paragraph noAnswer = new Paragraph("\t\t\t\t\t\t[   ]\t\t\t",FontFactory.getFont(FontFactory.HELVETICA,10.0f,Font.NORMAL,Color.BLACK));
						text = answer.getText();
						body.add(rellenaParrafo(text,noAnswer,false));
						body.add(Chunk.NEWLINE);
						body.add(Chunk.NEWLINE);
						for (MediaElem mmediaElem : answer.getMmedia()) {
							try {
								if (mmediaElem.getType() == MediaElem.IMAGE) {
									Image imgElem = Image.getInstance(rootPath.getFile().getAbsolutePath()+File.separatorChar+mmediaElem.getPath());
									imgElem.scaleToFit(100, 100);
									body.add(new Chunk(imgElem,50,0));
									body.add(Chunk.NEWLINE);
									body.add(Chunk.NEWLINE);
								}
							}
							catch(Exception e) {
								body.add("--> Elemento multimedia deberia ir aqui <--\n\n");
							}
						}
						continue;
					}else{
						Paragraph bodyAnswer = new Paragraph();
						text = answer.getText();
						/*bodyAnswer.add(new Phrase("************************************************************"));
						bodyAnswer.add(Chunk.NEWLINE);
						bodyAnswer.add(new Phrase("Respuesta correcta: "+"\""+text+"\""));
						bodyAnswer.add(Chunk.NEWLINE);
						bodyAnswer.add(new Phrase("************************************************************"));
						bodyAnswer.add(Chunk.NEWLINE);*/
						if(question.getLearnerFillAnswer()!=null){
							bodyAnswer.add(new Phrase("Respuesta del alumno: "));
							if(question.getLearnerFillAnswer().toLowerCase().trim().equalsIgnoreCase(text.trim().toLowerCase())){
								bodyAnswer.add(new Phrase("\""+question.getLearnerFillAnswer()+"\"",FontFactory.getFont(FontFactory.HELVETICA,10.0f,Font.BOLD,Color.GREEN)));
								questionAnswered = true;
							}else{
								bodyAnswer.add(new Phrase("\""+question.getLearnerFillAnswer()+"\"",FontFactory.getFont(FontFactory.HELVETICA,10.0f,Font.BOLD,Color.RED)));
								questionAnswered = true;
							}
						}else
							bodyAnswer.add(new Phrase("Respuesta del alumno: "+"\"\""));
						bodyAnswer.add(Chunk.NEWLINE);
						bodyAnswer.add(new Phrase("************************************************************"));
						body.add(bodyAnswer);
						body.add(Chunk.NEWLINE);
					}
				}
				questionBody.addCell(body);
				document.add(questionBody);
			}
			//Puntuacion
			PdfPTable gradeTable = new PdfPTable(1);
			gradeTable.setSpacingBefore(100.0f);
			gradeTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			gradeTable.getDefaultCell().setBorderWidth(0.8f);
			Paragraph gradeParagraph = new Paragraph();
			Paragraph infoGrade = new Paragraph("Puntuacion final:\n",FontFactory.getFont(FontFactory.HELVETICA,10.0f,Font.NORMAL,Color.black));
			Paragraph finalGrade = null;
			if(questionAnswered)
				finalGrade = new Paragraph(""+exam.getExamGrade(),FontFactory.getFont(FontFactory.HELVETICA,20.0f,Font.BOLD,Color.black));
			else
				finalGrade = new Paragraph("",FontFactory.getFont(FontFactory.HELVETICA,20.0f,Font.BOLD,Color.black));
			gradeParagraph.add(infoGrade);
			gradeParagraph.add(finalGrade);
			gradeTable.addCell(gradeParagraph);
			document.add(gradeTable);
			document.close();
		}
		else throw new NullPointerException();
		return baos;
	}
	
	private Phrase tratarTexto(String str){
		try{
			Phrase p = new Phrase();
			long b = str.indexOf("[b]");
			if(b==-1)
				b=Long.MAX_VALUE;
			long s = str.indexOf("[del]");
			if(s==-1)
				s=Long.MAX_VALUE;
			long u = str.indexOf("[em]");
			if(u==-1)
				u=Long.MAX_VALUE;
			long color = str.indexOf("[font]");
			if(color==-1)
				color = Long.MAX_VALUE;
			long link = str.indexOf("[a]");
			if(link ==-1)
				link = Long.MAX_VALUE;
			int inicio =0;
			int fin =0;
			//bold
			if(b<s && b<u && b<color && b<link && b!=Long.MAX_VALUE){
				inicio = str.indexOf("[b]");
				fin = str.indexOf("[/b]")+4;
				if(inicio!=0)
					p.add(new Phrase(str.substring(0, inicio)));
				Phrase auxp = tratarTexto(str.substring(inicio+3, fin-4));
				auxp.getFont().setStyle(Font.BOLD);
				p.add(auxp);
				p.add(tratarTexto(str.substring(fin, str.length())));
			}
			if(s<b && s<u && s<color && s<link && s!=Long.MAX_VALUE){
				inicio = str.indexOf("[del]");
				fin = str.indexOf("[/del]")+6;
				if(inicio!=0)
					p.add(new Phrase(str.substring(0, inicio)));
				Phrase auxp = tratarTexto(str.substring(inicio+5, fin-6));
				auxp.getFont().setStyle(Font.UNDERLINE);
				p.add(auxp);
				p.add(tratarTexto(str.substring(fin, str.length())));
				
			}
			if(u<b && u<s && u<color && u<link && u!=Long.MAX_VALUE){
				inicio = str.indexOf("[em]");
				fin = str.indexOf("[/em]")+5;
				if(inicio!=0)
					p.add(new Phrase(str.substring(0, inicio)));
				Phrase auxp = tratarTexto(str.substring(inicio+4, fin-5));
				auxp.getFont().setStyle(Font.ITALIC);
				p.add(auxp);
				p.add(tratarTexto(str.substring(fin, str.length())));
			}
			if(color<b && color<u && color<s && color<link && color!=Long.MAX_VALUE){
				inicio = str.indexOf("[font]");
				fin = str.indexOf("[/font]")+7;
				int inicioColor = str.indexOf("[color]");
				int finColor = str.indexOf("[/color]")+8;
				String sColor = str.substring(inicioColor+7, finColor-8);
				Phrase auxp = tratarTexto(str.substring(finColor, fin-7));
				if(sColor.equalsIgnoreCase("red"))
					auxp = new Phrase(auxp.getContent(),FontFactory.getFont(FontFactory.defaultEncoding,12,Font.NORMAL,Color.RED));
				if(sColor.equalsIgnoreCase("green"))
					auxp = new Phrase(auxp.getContent(),FontFactory.getFont(FontFactory.defaultEncoding,12,Font.NORMAL,Color.GREEN));
				if(sColor.equalsIgnoreCase("blue"))
					auxp = new Phrase(auxp.getContent(),FontFactory.getFont(FontFactory.defaultEncoding,12,Font.NORMAL,Color.BLUE));
				if(inicio!=0)
					p.add(new Phrase(str.substring(0, inicio)));
				p.add(auxp);
				p.add(tratarTexto(str.substring(fin, str.length())));
			}
			if(link<b && link<u && link<s && link<color && link!=Long.MAX_VALUE){
				inicio = str.indexOf("[a]");
				fin = str.indexOf("[/a]")+4;
				int inicioUrl= str.indexOf("[href]");
				int finUrl = str.indexOf("[/href]")+7;
				int inicioTexto = str.indexOf("[text]");
				int finTexto = str.indexOf("[/text]")+7;
				String url = str.substring(inicioUrl+6, finUrl-7);
				String texto = str.substring(inicioTexto+6, finTexto-7);
				Phrase auxp = tratarTexto(texto+"(hipervinculo: "+url+")");
				if(inicio!=0)
					p.add(new Phrase(str.substring(0, inicio)));
				p.add(auxp);
				p.add(tratarTexto(str.substring(fin, str.length())));
				
			}
			if(u==Long.MAX_VALUE && b == Long.MAX_VALUE && s == Long.MAX_VALUE && color == Long.MAX_VALUE && link == Long.MAX_VALUE)
				p = new Phrase(str);
			return p;
		}catch(Exception e){
			return new Phrase(str);
		}
	}
	
	
	private Paragraph rellenaParrafo(String text, Paragraph questionForm, boolean pregunta){
		/*
		 * QuestionForm puede ser el "Paragraph" que represente una pregunta o una respuesta
		 * */
		if(text!=null){	
			boolean salir = false;
			if(text.indexOf("`")==-1){
				questionForm.add(tratarTexto(text));
				return questionForm;
			}else
				while(text.indexOf("`")!=-1 && !salir){
					try{
						String texto0 = text.split("`")[0];
						String latex = text.split("`")[1];
						if(!texto0.trim().equalsIgnoreCase(""))
							questionForm.add(tratarTexto(texto0));
						TeXFormula formula = null;
						boolean createdLatexIcon = false;
						try {
							formula = new TeXFormula(latex);
							createdLatexIcon = true;
						} catch (Exception e1) {
							questionForm.add(tratarTexto("It's not a valid Latex form"));
							createdLatexIcon = false;
						}
						if(createdLatexIcon && formula!=null){
							TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);
							icon.setInsets(new Insets(3, 5, 3, 5));
							BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
							Graphics2D g2 = image.createGraphics();
							g2.setColor(Color.white);
							g2.fillRect(0,0,icon.getIconWidth(),icon.getIconHeight());
							JLabel jl = new JLabel();
							jl.setForeground(new Color(0, 0, 0));
							icon.paintIcon(jl, g2, 0, 0);
							File file = new File("latex.png");
							try{
								ImageIO.write(image, "png", file.getAbsoluteFile());
								Image imgElem = Image.getInstance(file.getAbsolutePath());
								Chunk imagen = null;
								if(texto0.trim().equalsIgnoreCase("")){
									imagen = new Chunk(imgElem,questionForm.getSpacingAfter()+20,questionForm.getFirstLineIndent()-5);
								}else{
									imagen = new Chunk(imgElem,questionForm.getSpacingAfter(),questionForm.getFirstLineIndent()-10);
								}
								
								questionForm.add(imagen);
							}catch(Exception e){
								e.printStackTrace();
								salir=true;
							}
						}
						
						
						String aux = new String(text);
						if(text.indexOf("`")!=-1){
							text= text.substring(texto0.length()+latex.length()+2);
						}
						if(text.equalsIgnoreCase(aux))
							salir = true;
					}catch(Exception e){
						salir = true;
					}
				}
			}
		if(!text.trim().equalsIgnoreCase(""))
			questionForm.add(tratarTexto(text));
		return questionForm;
	}

	public Grade getExamGrade(Long idexam, Long iduser) {
		return examDAO.getExamGrade(idexam, iduser);
	}

	public Exam getExam(Long idexam, Long idalumn) {
		return examDAO.getExamById(idexam,idalumn);
	}

	public List<Group> getUserGroups(Long iduser) {
		return groupDAO.getUserGroups(iduser);
	}

	public Group getGroup(Long idgroup) {
		return groupDAO.getGroup(idgroup);
	}

	public Grade getGradeByIdExam(Long idexam, Long iduser) {
		return templateGradeDAO.getGradeByIdExam(idexam,iduser);
	}

	public ConfigExam getConfigExamFromId(ConfigExam exam) {
		return configExamDAO.getConfigExamFromId(exam);
	}

	public List<Exam> getAlreadyDoneExamGradeByGroup(long iduser, long idgroup) {
		return examDAO.getAlreadyDoneExamGradeByGroup(iduser, idgroup);
	}
	
	public List<Grade> getAlreadyDoneGradeByGroup(long iduser, long idgroup) {
		return templateGradeDAO.getAlreadyDoneGradeByGroup(iduser, idgroup);
	}

	public List<BasicDataExam> getNextExams(long userId, long idGroup) {
		return basicDataExamDAO.getNextExams(userId,idGroup);
	}

	public List<User> getTutors(Group g) {
		return userDAO.getAssignedTutors(g);
	}

	public List<Exam> getAllExams(Long idexam) {
		return examDAO.getAllExams(idexam);
	}

	public int updateConfidenceLevel(long examId, long userId,
			long questionId, boolean checked) {
		TemplateExamQuestion question = new TemplateExamQuestion();
		question.setId(questionId);
		question = templateExamQuestionDAO.getQuestionFromId(question);
		return answerExamDAO.updateConfidenceLevel(examId,userId,questionId,checked,question.getType());
	}
	
	public void addNewExamAnswer2LogExams(Exam currentExam,long idlearner,long idquestion,long startingDate){
		List<ExamQuestion> questions = currentExam.getQuestions();
		for(ExamQuestion question : questions){
			if(question.getId().equals(idquestion)){
				List<ExamAnswer> answers = question.getAnswers();
				for(ExamAnswer answer : answers){
					if(question.getType()==0)
						answerExamDAO.addNewExamAnswer(currentExam.getId(), idlearner,
								question.getId(), answer.getId(),
								startingDate);
					else
						answerExamDAO.addNewExamFillAnswer(currentExam.getId(), idlearner, question.getId(), 
								answer.getId(), question.getLearnerFillAnswer(), startingDate);
				}
			}
		}
	}

	public void checkExam(Exam currentExam, long iduser) {
		for(ExamQuestion question : currentExam.getQuestions()){
			for(ExamAnswer answer : question.getAnswers()){
				if(question.getType()==0){
					int rows = answerExamDAO.updateExamAnswer(currentExam.getId(), iduser, question.getId(), answer);
					if(rows==0){
						answerExamDAO.addNewExamAnswer(currentExam.getId(), iduser, question.getId(), answer.getId(), System.currentTimeMillis());
					}
				}else if(question.getType()==1){
					int rows = answerExamDAO.updateExamFillAnswer(currentExam.getId(), iduser, question.getId(), answer.getId(),question.getLearnerFillAnswer(),null);
					if(rows==0){
						answerExamDAO.addNewExamFillAnswer(currentExam.getId(), iduser, question.getId(), answer.getId(), question.getLearnerFillAnswer(), System.currentTimeMillis());
					}
				}
			}
		}
	}

	public User getUser(long idUser) {
		return userDAO.getUser(idUser);
	}

	public List<Grade> getGradesByUser(String userName) {
		return templateGradeDAO.getGradesByUser(userName);
	}
	
	public List<CustomExamUser> getUsersInCustomExam(Long examId) {
		return userDAO.getUsersInCustomExam(examId);
	}

	public void updateExamFillAnswer(Long idExam, Long idUser, Long idQuestion,
			Long idAnswer, String textAnswer) {
		int rows = answerExamDAO.updateExamFillAnswer(idExam,idUser,idQuestion,idAnswer,textAnswer,new Long(0));
		StringBuilder buffer = new StringBuilder();
		Date fecha = new Date(System.currentTimeMillis());
		buffer.append("UPDATE log_exams_fill ");
		buffer.append("resp = '"+textAnswer+"'");
		buffer.append("puntos = 0.0, ");
		buffer.append("hora_resp = '"+fecha.getYear()+"-"+fecha.getMonth()+"-"+fecha.getDate()+" "+fecha.getHours()+":"+fecha.getMinutes()+":"+fecha.getSeconds()+"' ");
		buffer.append("WHERE exam ="+idExam+" AND alu ="+idUser+" AND preg ="+idQuestion+" AND resp ="+idAnswer);
		if(rows==0){
			answerExamDAO.addNewExamFillAnswer(idExam,idUser,idQuestion,idAnswer,textAnswer,System.currentTimeMillis());
		}
	}
	
	public Double reGradeExam(Long idStd, Exam ex) {
		//Aqui se eliminarian las preguntas que no est�n en la config. de ex�men
		return calculateExamGrade(idStd, ex, true, true, true);
	}
}
