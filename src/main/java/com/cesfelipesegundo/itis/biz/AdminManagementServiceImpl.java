package com.cesfelipesegundo.itis.biz;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cesfelipesegundo.itis.biz.api.AdminManagementService;
import com.cesfelipesegundo.itis.biz.api.LearnerManagementService;
import com.cesfelipesegundo.itis.model.Conection;
import com.cesfelipesegundo.itis.dao.api.ConectionsDAO;
import com.cesfelipesegundo.itis.dao.api.CourseDAO;
import com.cesfelipesegundo.itis.dao.api.ExamDAO;
import com.cesfelipesegundo.itis.dao.api.GroupDAO;
import com.cesfelipesegundo.itis.dao.api.InstitutionDAO;
import com.cesfelipesegundo.itis.dao.api.SubjectDAO;
import com.cesfelipesegundo.itis.dao.api.TemplateExamDAO;
import com.cesfelipesegundo.itis.dao.api.UserDAO;
import com.cesfelipesegundo.itis.model.Course;
import com.cesfelipesegundo.itis.model.CourseStats;
import com.cesfelipesegundo.itis.model.ExamGlobalInfo;
import com.cesfelipesegundo.itis.model.GroupDetails;
import com.cesfelipesegundo.itis.model.Institution;
import com.cesfelipesegundo.itis.model.InstitutionStats;
import com.cesfelipesegundo.itis.model.InstitutionStudies;
import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.biz.api.TutorManagementService;
import com.mysql.jdbc.exceptions.MySQLTransactionRollbackException;

import es.itest.engine.course.business.entity.Group;
import es.itest.engine.test.business.entity.Item;
import es.itest.engine.test.business.entity.TestSession;



public class AdminManagementServiceImpl extends BaseService implements
		AdminManagementService {

	private InstitutionDAO institutionDAO;
	private UserDAO userDAO;
	private CourseDAO courseDAO;
	private GroupDAO groupDAO;
	private SubjectDAO subjectDAO;
	private ExamDAO examDAO;
	private ConectionsDAO conectionsDAO;
	
	private TutorManagementService tutorManagementService;
	private LearnerManagementService learnerManagementService;
	
	
	
	public LearnerManagementService getLearnerManagementService() {
		return learnerManagementService;
	}

	public void setLearnerManagementService(
			LearnerManagementService learnerManagementService) {
		this.learnerManagementService = learnerManagementService;
	}

	public ConectionsDAO getConectionsDAO() {
		return conectionsDAO;
	}

	public void setConectionsDAO(ConectionsDAO conectionsDAO) {
		this.conectionsDAO = conectionsDAO;
	}

	public ExamDAO getExamDAO() {
		return examDAO;
	}

	public void setExamDAO(ExamDAO examDAO) {
		this.examDAO = examDAO;
	}

	public InstitutionDAO getInstitutionDAO() {
		return institutionDAO;
	}

	public void setInstitutionDAO(InstitutionDAO institutionDAO) {
		this.institutionDAO = institutionDAO;
	}
	
	
	public SubjectDAO getSubjectDAO() {
		return subjectDAO;
	}

	public void setSubjectDAO(SubjectDAO subjectDAO) {
		this.subjectDAO = subjectDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	public CourseDAO getCourseDAO() {
		return courseDAO;
	}

	public void setCourseDAO(CourseDAO courseDAO) {
		this.courseDAO = courseDAO;
	}

	public GroupDAO getGroupDAO() {
		return groupDAO;
	}

	public void setGroupDAO(GroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}

	public TutorManagementService getTutorManagementService() {
		return tutorManagementService;
	}

	public void setTutorManagementService(
			TutorManagementService tutorManagementService) {
		this.tutorManagementService = tutorManagementService;
	}
	
	public List<Institution> getInstitutions() {
		List<Institution> result = institutionDAO.getInstitutions();
		return result;
	}
	
	public Institution getInstitution(Long id) {
		return institutionDAO.getInstitution(id);
	}
	
	public Institution saveInstitution(Institution institution, InstitutionStudies studies) {
		return institutionDAO.saveInstitution(institution,studies);
		
	}
	
	public void deleteInstitution(Institution institution) {
		
		List<Group> groupList = groupDAO.getGroups(institution);
		
		if (groupList != null)
		{
					
			Iterator<Group> iterGroupList = groupList.iterator();
			while (iterGroupList.hasNext())
			{
			
				Group group = iterGroupList.next();
				deleteGroup(group);				
				
			} 		
		}
				
		institutionDAO.deleteInstitution(institution);
		
	}
	
	public List<User> getTutors(Institution i) {
		return userDAO.getTutors(i);
	}

	public List<User> getTutors(Group g) {
		return userDAO.getAssignedTutors(g);
	}
	
	public List<User> getUnassignedTutors(Group g) {
		return userDAO.getUnAssignedTutors(g);
	}
	
	public List<Group> getGroups(Course c) {
		return groupDAO.getCourseGroups(c);
	}

	public List<Group> getGroups(Institution i) {
		return groupDAO.getGroups(i);
	}
	
	
	public Course getCourse(Long id) {
		return courseDAO.getCourse(id);
	}
	
	public Course getCourseByCode(String code){
		return courseDAO.getCourseByCode(code);
	}
	
	public boolean assignTutor(User tutor, Group group) {
		userDAO.registerTutor(tutor,group);
		return true;
	}

	public boolean unAssignTutor(User tutor, Group group) {
		userDAO.unRegisterTutor(tutor,group);
		return true;
	}
	
	public List<User> getUsers(Institution i) {
		return userDAO.getUsers(i);
	}

	public List<User> getLearners(Institution i) {
		return userDAO.getLearners(i);
	}

	


	public List<Course> getCourses() {

		List<Course> result = courseDAO.getCourses();
		return result;	
		
	}

	

	public Course saveCourse(Course course) {
				
		return courseDAO.saveCourse(course);
		
		
	}


	public void deleteCourse(Course course) {
		
		List<Group> groupList = groupDAO.getCourseGroups(course);
		
		if (groupList != null)
		{
					
			Iterator<Group> iterGroupList = groupList.iterator();
			while (iterGroupList.hasNext())
			{
			
				Group group = iterGroupList.next();
				deleteGroup(group);				
				
			} 		
		}
		
		courseDAO.deleteCourse(course);
		
		
	}
	
	public Group saveGroup(Group group) {	
		return groupDAO.saveGroup(group);
		
	}	
	
	public void deleteGroup(Group group) {
		
		// Delete the questions of the group
		List<Item> questionList = groupDAO.getQuestions(group);
		
		if (questionList != null)
		{
		
			Iterator<Item> iterQuestionList = questionList.iterator();
			while (iterQuestionList.hasNext())
			{
			
				Item question = iterQuestionList.next();
				tutorManagementService.deleteQuestion(question);				
				
			} // while iterQuestionList		
		
		}	
			
		groupDAO.deleteGroup(group);
	}


	

	public void deleteUser(User user) {
		userDAO.deleteUser(user);
		
	}

	public boolean saveUser(User newUser, Institution institution) {
			Boolean res = true;
			if (newUser.getId() == null) {
				// Only inserts a new user when the userName is available:
				if (this.userNameIsAvailable(newUser.getUserName())) {
					userDAO.saveUser(newUser,institution);
				} else {
					res = false;
				}
			} else {
				userDAO.updateUser(newUser,institution);
			}
			return res;
	}

	public boolean userNameIsAvailable(String userName) {
		// Check the user name
		User usu = userDAO.getUser(userName);
		// If the user name does not exits, the user object will be empty:
		return (usu == null);
	}

	public List<ExamGlobalInfo> getNextExams() {
		return tutorManagementService.getNextExams();
	}

	public List<ExamGlobalInfo> getActiveExams() {
		return tutorManagementService.getActiveExams();
	}

	public List<Course> getCourses(long institutionId) {
		
		return courseDAO.getCourses(institutionId);
	}

	public CourseStats getCourseStats(long institution, long course, String year) {
		return courseDAO.getCourseStats(institution,course,year);
	}

	public List<Group> getGroups(Course course, long idInstitution) {
		return groupDAO.getGroups(course,idInstitution);
	}

	public List<Course> getCourseByInstitutionAndGroup(long idInstitution,
			String year) {
		return courseDAO.getCourseByInstitutionAndGroup(idInstitution,year);
	}

	public InstitutionStats getInstitutionStats(long idInstitution,
			String year, long idCourse) {
		return institutionDAO.getInstitutionStats(idInstitution,year,idCourse);
	}

	public InstitutionStudies getInstitutionStudies(long idInstitution) {
		return institutionDAO.getInstitutionStudies(idInstitution);
	}

	public List<Institution> getInstitutionsFiltered(Map<String, Object> map) {
		return institutionDAO.getInstitutionsFiltered(map);
	}

	public List<User> getUsersByFilter(Map<String, Object> map) {
		return userDAO.getUsersByFilter(map);
	}

	public List<Group> getGroupsByFilter(Map<String, Object> map) {
		return groupDAO.getGroupsByFilter(map);
	}

	public List<Course> getCoursesFiltered(String codigo, String nombre, String curso, String estudios) {
		return courseDAO.getCoursesFiltered(codigo,nombre,curso,estudios);
	}

	public List<User> getFilteredUsers(Map<String, Object> map) {
		return userDAO.getFilteredUsers(map);
	}

	public List<Group> getFilteredGroups(Map<String, Object> map) {
		return groupDAO.getFilteredGroups(map);
	}

	public List<ExamGlobalInfo> getPreviousExamsFiltered(long idInstitution,
			long idCourse, String year) {
		
		return examDAO.getPreviousExamsFiltered(idInstitution,idCourse,year);
	}

	public CourseStats getCourseStatsByExam(int idexam) {
		return examDAO.getPreviousExamsFiltered(idexam);
	}

	public List<User> getSearchUsersFiltered(String name, String apes, String user, String tipo) {
		return userDAO.getSearchUsersFiltered(name,apes,user,tipo);
	}

	public List<Group> getUserInfoGroups(long id) {
		return groupDAO.getUserInfoGroups(userDAO.getUser(id));
	}

	public List<String> getAllCertifications() {
		 List<String> list = institutionDAO.getAllCertifications();
		 for(int i=list.size()-1;i>=0;i--){
			 if(list.get(i)!=null){
				 if(list.get(i).trim().equalsIgnoreCase(""))
					 list.remove(i);
			 }else{
				 list.remove(i);
			 }
		 }
		return list;
	}

	public List<GroupDetails> getGroupDetails(long idInstitution, String year,
			long idCourse) {
		return groupDAO.getGroupDetails(idInstitution, year, idCourse);
	}

	public List<ExamGlobalInfo> getActiveExamsFiltered(String Centro,
			String Asignatura, Date startDate, Date endDate) {
		return examDAO.getActiveExamsFiltered(Centro,Asignatura,startDate,endDate);
	}

	public List<ExamGlobalInfo> getNextExamsFiltered(String centro,
			String asignatura, Date startDate, Date endDate) {
		return examDAO.getNextExamsFiltered(centro,asignatura,startDate,endDate);
	}
	
	public List<Conection> get100LastConections(){
		return conectionsDAO.get100LastConections();
	}

	public List<Conection> runFilterAndSearch100Conections(Long idConection,
			String userNameConection, Date date1, Date date2) {
		return conectionsDAO.runFilterAndSearch100Conections(idConection,userNameConection,date1,date2);
	}

	public List<Conection> show5LastConections(Long id) {
		return conectionsDAO.show5LastConections(id);
	}

	public List<User> showUsersNotVinculated() {
		return userDAO.showUsersNotVinculated();
	}
	
	public User getUserById(long idUser){
		return userDAO.getUser(idUser);
	}
	
	public List<User> getLearnerByGroup(Group group){
		return tutorManagementService.getStudents(group);
	}
	
	public Group getGroup(long idGroup){
		return tutorManagementService.getGroup(idGroup);
	}
	
	public TestSession getAlreadyDoneExam(User user, long idexam){
		return learnerManagementService.getAlreadyDoneExam(user, idexam);
	}

	public TestSession getNewExam(User user, long idExam, String remoteAddr) {
		return learnerManagementService.createTestSession(user, idExam, remoteAddr);
	}

	public void checkExam(TestSession ex,long iduser) throws MySQLTransactionRollbackException, Exception {
		learnerManagementService.checkExam(ex, iduser);
	}

	public List<Institution> getInstitutionsWidthPublicQuestions() {
		return institutionDAO.getInstitutionsWidthPublicQuestions();
	}
	
	public Institution getInstitutionByUserId(Long id) {
		return institutionDAO.getInstitutionByUserId(id);
	}

}
