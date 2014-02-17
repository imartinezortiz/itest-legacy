package com.cesfelipesegundo.itis.biz;

import java.io.File;
import java.io.FileInputStream;
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
import com.cesfelipesegundo.itis.model.CustomExamUser;
import com.cesfelipesegundo.itis.model.ExamGlobalInfo;
import com.cesfelipesegundo.itis.model.ExamStats;
import com.cesfelipesegundo.itis.model.Institution;
import com.cesfelipesegundo.itis.model.LearnerStats;
import com.cesfelipesegundo.itis.model.Message;
import com.cesfelipesegundo.itis.model.Query;
import com.cesfelipesegundo.itis.model.QueryGrade;
import com.cesfelipesegundo.itis.model.QuestionStats;
import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.model.comparators.SubjectOrderComparator;
import com.cesfelipesegundo.itis.web.Constants;

import es.itest.engine.course.business.entity.Group;
import es.itest.engine.course.business.entity.Subject;
import es.itest.engine.course.business.entity.TestSessionTemplateSubject;
import es.itest.engine.test.business.boundary.TestManager;
import es.itest.engine.test.business.entity.Item;
import es.itest.engine.test.business.entity.ItemResponse;
import es.itest.engine.test.business.entity.ItemSession;
import es.itest.engine.test.business.entity.ItemSessionResponse;
import es.itest.engine.test.business.entity.MediaElem;
import es.itest.engine.test.business.entity.Test;
import es.itest.engine.test.business.entity.TestSession;
import es.itest.engine.test.business.entity.TestSessionGrade;
import es.itest.engine.test.business.entity.TestSessionTemplate;
import es.itest.engine.test.business.entity.TestSubject;

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

	Resource getRootPath() {
		return rootPath;
	}

	void setRootPath(Resource rootPath) {
		this.rootPath = rootPath;
	}

	ExamDAO getExamDAO() {
		return examDAO;
	}

	void setExamDAO(ExamDAO examDAO) {
		this.examDAO = examDAO;
	}

	UserDAO getUserDAO() {
		return userDAO;
	}

	void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	TemplateExamDAO getTemplateExamDAO() {
		return templateExamDAO;
	}

	void setTemplateExamDAO(TemplateExamDAO templateExamDAO) {
		this.templateExamDAO = templateExamDAO;
	}

	ConfigExamDAO getConfigExamDAO() {
		return configExamDAO;
	}

	void setConfigExamDAO(ConfigExamDAO configExamDAO) {
		this.configExamDAO = configExamDAO;
	}

	GroupDAO getGroupDAO() {
		return groupDAO;
	}

	void setGroupDAO(GroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}

	CourseDAO getCourseDAO() {
		return courseDAO;
	}

	void setCourseDAO(CourseDAO courseDAO) {
		this.courseDAO = courseDAO;
	}

	TemplateExamQuestionDAO getTemplateExamQuestionDAO() {
		return templateExamQuestionDAO;
	}

	void setTemplateExamQuestionDAO(
			TemplateExamQuestionDAO templateExamQuestionDAO) {
		this.templateExamQuestionDAO = templateExamQuestionDAO;
	}

	TemplateExamAnswerDAO getTemplateExamAnswerDAO() {
		return templateExamAnswerDAO;
	}

	void setTemplateExamAnswerDAO(TemplateExamAnswerDAO templateExamAnswerDAO) {
		this.templateExamAnswerDAO = templateExamAnswerDAO;
	}
		
	TemplateGradeDAO getTemplateGradeDAO() {
		return templateGradeDAO;
	}

	void setTemplateGradeDAO(TemplateGradeDAO templateGradeDAO) {
		this.templateGradeDAO = templateGradeDAO;
	}
	
	StatsDAO getStatsDAO() {
		return statsDAO;
	}

	void setStatsDAO(StatsDAO statsDAO) {
		this.statsDAO = statsDAO;
	}

	SubjectDAO getSubjectDAO() {
		return subjectDAO;
	}

	void setSubjectDAO(SubjectDAO subjectDAO) {
		this.subjectDAO = subjectDAO;
	}

	private TestManager testManager;
	
	void setTestManager(TestManager testManager) {
		this.testManager = testManager;
	}
	
	TestManager getTestManager() {
		return this.testManager;
	}
	
	//
	// Business method
	//
	
	
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
	public List<TestSubject> getCourseSubjects(Group g) {

		// The results are obtained from the group object
		List<TestSubject> result = courseDAO.getSubjects(g);

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
	 * Gets from the database the list of questions related to this group
	 * 
	 * @param currentGroup
	 * @return list of TemplateExamQuestion: questions, answers, mmedia, ...
	 */
	public List<Item> getGroupQuestions(Group currentGroup) {
		return groupDAO.getQuestions(currentGroup);
	}
	
	public void saveSubjectToExam(TestSessionTemplate exam, TestSessionTemplateSubject subject){
		
		// Comportamiento similar a saveQuestion y saveAnswer con respecto al id
		
		if(subject.getId() != null){
			configExamDAO.update(exam, subject);
		}else{
			configExamDAO.save(exam, subject);
		}
	}
	
	public void deleteSubjectFromExam(TestSessionTemplate exam, TestSessionTemplateSubject subject, long idGroup) {
	configExamDAO.delete(exam, subject);
           Query queryQuestions = new Query();
           long subjectId= subject.getSubject().getId();
           queryQuestions.setSubject(subjectId);
           List<Item> questions = find(queryQuestions);
           int max = subject.getMaxDifficulty();
           int min = subject.getMinDifficulty();
           for(Item question : questions){
                   if(question.getDifficulty()==max || question.getDifficulty()==min){
                           examDAO.deleteQuestionFromExam(exam.getId(), question.getId());
                   }
               }
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

	private List<Message> validateSubjects(Test templateExam) {
		List<Message> result = new ArrayList<Message>();
		List<Message> messages = null;
		TestSessionTemplate configExam = new TestSessionTemplate();
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
		for (TestSubject s1 : templateExam.getSubjects()) {
			for (TestSubject s2 : templateExam.getSubjects()) {
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
		for (TestSubject templateSubject : templateExam.getSubjects()) {
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

	public List<Message> validateSubject(TestSubject templateSubject,
			TestSessionTemplate configExam) {
		List<Message> result = Collections.EMPTY_LIST;

		// 1. Check Questions
		int visibility = configExam.getVisibility();
		Group examGroup = configExam.getGroup();

		int maxDifficulty = templateSubject.getMaxDifficulty();
		int minDifficulty = templateSubject.getMinDifficulty();
		int questionsNumber = templateSubject.getQuestionsNumber();

		List<Item> templateQuestions = templateSubject
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

		List<Item> previousStep = new ArrayList<Item>();
		List<Item> currentStep = new ArrayList<Item>();
		/*
		 * 1.2. Check if there are enogh active questions
		 */
		questionsCount = 0;
		for (Item templateQuestion : templateSubject
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
		for (Item templateQuestion : previousStep) {
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
		for (Item templateQuestion : previousStep) {
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
		TestSessionTemplateSubject configExamSubject = new TestSessionTemplateSubject();
		// TODO: ConfigExamSubject should be an attribute of
		// TemplateExamSubject. Refactoring needed !!!
		configExamSubject.setAnswersxQuestionNumber(templateSubject
				.getAnswersxQuestionNumber());

		for (Item templateQuestion : currentStep) {
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
			 TestSessionTemplate ce = new TestSessionTemplate();
			 ce.setId(id);
			 ce = configExamDAO.getTestSessionTemplate(id);
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

	
	public List<QuestionStats> getQuestionStatsByExam(TestSessionTemplate currentExam)
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
		List<Item> questionList = subjectDAO.getQuestionsBySubjectAndGroupId(originalSubject.getId(), sourceGroup.getId());
		if (questionList != null) //If not empty list
			for (Item oldQuestion : questionList){ //For each question from original subject
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
	
	/**
	 * Saves the question information into the database
	 * 
	 * @param question
	 * @return id of the question saved
	 */
	public void saveQuestion(Item question) {
		testManager.saveQuestion(question);
	}

	/**
	 * Saves the answer information into the database
	 * 
	 * @param answer
	 */
	public void saveAnswer(ItemResponse answer ) {
		testManager.saveAnswer(answer);
	}

	public void deleteAnswer(ItemResponse answer) {
		testManager.deleteAnswer(answer);
	}

	public void saveMediaElemToQuestion(Item question, MediaElem mediaElem){
		testManager.saveMediaElemToQuestion(question, mediaElem);
	}
	
	public void saveMediaElemToComment(Item question, MediaElem mediaElem){
		testManager.saveMediaElemToComment(question, mediaElem);
	}
	
	public void saveMediaElemToAnswer(ItemResponse answer, MediaElem mediaElem){
		testManager.saveMediaElemToAnswer(answer, mediaElem);
	}
	
	public void deleteMediaElemFromComment(Item question, MediaElem mediaElem){
		testManager.deleteMediaElemFromComment(question, mediaElem);
	}
		
	public void deleteMediaElemFromQuestion(Item question, MediaElem mediaElem){
		testManager.deleteMediaElemFromQuestion(question, mediaElem);
	}
	
	public void deleteMediaElemFromAnswer(ItemResponse answer, MediaElem mediaElem){
		testManager.deleteMediaElemFromAnswer(answer, mediaElem);
	}

	public List<Item> find(Query query) {
		return testManager.find(query);
	}

	public void deleteQuestion(Item question) {
		testManager.deleteQuestion(question);
	}
	
	public TestSessionTemplate configExamCopy(TestSessionTemplate examSource) {
		return testManager.configExamCopy(examSource);
	}
	
	public Item copyQuestion(Item question, boolean copy) {
		return testManager.copyQuestion(question, copy);
	}

	public Item getQuestionFromId(Item question) {	
		return testManager.getQuestionFromId(question);
	}

// methods to manage exam configurations	
	
	public void deleteConfigExam(TestSessionTemplate exam) {
		testManager.deleteConfigExam(exam);
	}

	public List<TestSessionTemplate> getGroupConfigExams(Group group, String orderby) {
		return testManager.getGroupConfigExams(group, orderby);
	}

	public void updateExamReview(TestSessionTemplate exam) {
		testManager.updateExamReview(exam);
	}

	
	/**
	 * Saves the exam configuration into the database
	 * 
	 * @param exam
	 * @return id of the exam saved if update
	 */
	public void saveExam(TestSessionTemplate exam) {
		testManager.saveExam(exam);
	}


	public TestSessionTemplate getConfigExamFromId(TestSessionTemplate exfromdb) {
		return testManager.getConfigExamFromId(exfromdb);
	}

	public List<TestSessionGrade> find(QueryGrade query) {
		return testManager.find(query);
	}
	
	
	public List<Message> validate(TestSessionTemplate config) {
		return testManager.validate(config);
	}

	public List<Message> validateQuestion(Item templateQuestion, TestSessionTemplateSubject configSubject) {
		return testManager.validateQuestion(templateQuestion, configSubject);
	}
	
	public double getGradeScale() {
		return testManager.getGradeScale();
	}
	
	public TestSession getStudentExam(Long idstd, Long idexam) {
		return testManager.getStudentExam(idstd, idexam);
	}

	public boolean importQuestions(Group group, Subject sbj, List<Item> qList, String sourcePath) {
		return testManager.importQuestions(group, sbj, qList, sourcePath);
	}

	public void deleteStudentGradeAndExam(Long idstd, Long idex) {
		testManager.deleteStudentGradeAndExam(idstd, idex);
	}
    
	/**
	 * Deletes a file from the multimedia path
	 * @param filename file name
	 */
	public boolean deleteMmediaFile (String filename) {
		return testManager.deleteMmediaFile(filename);
	}

	public List<ExamGlobalInfo> getNextExams() {
		return testManager.getNextExams();
	}

	public List<ExamGlobalInfo> getActiveExams() {
		return testManager.getActiveExams();
	}

	public List<Long> getExamIds(Item question) {
		return testManager.getExamIds(question);
	}

	public void updateQuestionNotUsedInExam(Long idgrp) {
		testManager.updateQuestionNotUsedInExam(idgrp);
	}

	@Override
	public String randomFilename() {
		return testManager.randomFilename();
	}
}

