package com.cesfelipesegundo.itis.web.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cesfelipesegundo.itis.biz.api.TutorManagementService;
import com.cesfelipesegundo.itis.model.Group;
import com.cesfelipesegundo.itis.model.ImportUser;
import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.web.Constants;

/**
 * It manages the operations related to LIST of question of the managed group
 * @author chema
 *
 */
public class TutorStudentListManagementController {

	private static final Log log = LogFactory.getLog(TutorStudentListManagementController.class);
	
	/**
	 * Service needed to manage requests from tutor
	 */
    private TutorManagementService tutorManagementService;
    
    /**
     * Student LIST being managed by the tutor
     */
    private List<User> currentStudentList;
    
    /**
     * Current ordering parameter
     */
    private String currentOrder;
    
    
    /**
     * Current group, needed for some functions
     */
    private Group currentGroup;
    
    /* ******** Getters and setters ******** */

	public TutorManagementService getTutorManagementService() {
		return tutorManagementService;
	}


	public void setTutorManagementService(
			TutorManagementService tutorManagementService) {
		this.tutorManagementService = tutorManagementService;
	}


	public List<User> getCurrentStudentList() {
		return currentStudentList;
	}


	public void setCurrentStudentList(List<User> currentStudentList) {
		this.currentStudentList = currentStudentList;
	}
	
	public Group getCurrentGroup() {
		return currentGroup;
	}


	public void setCurrentGroup(Group currentGroup) {
		this.currentGroup = currentGroup;
	}	
	
	public String getCurrentOrder() {
		return currentOrder;
	}


	public void setCurrentOrder(String currentOrder) {
		this.currentOrder = currentOrder;
	}
	
	
	/* ---------------------- Student LIST Management (Ajax) ------------------------ */
	

	/**
	 * Register an student into the current group.
	 * 
	 * @return List of registered students, needed for the callback function to repaint the list
	 */
	public List<User> registerStudent (String idStudent) {
		
		
		// Retrieve student data
		User student = tutorManagementService.getUserData(Long.valueOf(idStudent));
		
		boolean registered = tutorManagementService.isUserInGroup(Long.valueOf(idStudent),currentGroup.getId());
		if(!registered){
			// Registering the student in the current group
			tutorManagementService.registerStudent(student,currentGroup);
			// Log:
			log.debug("- Registro de alumno "+idStudent+" en grupo "+Long.valueOf(currentGroup.getId()));
		}
		
		// Returns the student list conveniently sorted
		return tutorManagementService.getStudents(currentGroup,currentOrder);
		
	} // registerStudent	


	/**
	 * Un-Register an student from the current group.
	 * 
	 * @return List of registered students, needed for the callback function to repaint the list
	 */
	public List<User> unRegisterStudent (String idStudent) {
		
		// Retrieve student data
		User student = tutorManagementService.getUserData(Long.valueOf(idStudent));

		// Un-registering the student from the current group
		tutorManagementService.unRegisterStudent(student,currentGroup);
		// Log:
		log.debug("- Alumno desmatriculado "+idStudent+" del grupo "+Long.valueOf(currentGroup.getId()));
		
		// Returns the student list conveniently sorted
		return tutorManagementService.getStudents(currentGroup,currentOrder);
		
	} // unRegisterStudent
	
	
	/**
	 * Return an student list with the students not registered in this group
	 * 
	 * @return List of unregistered students, needed for the callback function to repaint the list
	 */
	public List<User> getUnRegisteredStudent () {
		List<User> otherStList = null;
		if (currentGroup.getStudentType().intValue() == 1)
			otherStList = tutorManagementService.getStudentsNotRegistered(currentGroup,"LEARNER");
		else if (currentGroup.getStudentType().intValue() == 2)
			otherStList = tutorManagementService.getStudentsNotRegistered(currentGroup,"KID");
		else
			otherStList = tutorManagementService.getStudentsNotRegistered(currentGroup);
		
		return otherStList;
		
	} // getUnRegisteredStudent
	
	/**
	 * Save the information of a student. If the id is not provided means "new" student, and has to be
	 * registered in the current group. Otherwise, the information of a given student is changed.
	 * 
	 * The input parameters must be checked before invoking this method. 
	 * 
	 * @return true when no problems where detected. False when inserting and the username is used.
	 */
	public boolean saveStudent (String stdId, String persId, String surname, String name,
									String email, String userName,String passwd, String role) {
		boolean register = true;
		
		// New user object:
		User newStudent = new User();
		// User data:
		if (!stdId.equals(new String(""))) {
			// Is not a new student, must no be registered again
			newStudent.setId(Long.valueOf(stdId));
			register = false;
		}
		newStudent.setPersId(persId);
		newStudent.setSurname(surname);
		newStudent.setName(name);
		newStudent.setEmail(email);
		newStudent.setUserName(userName);
		newStudent.setPasswd(passwd);
		if (role.equals("LEARNER")) {
			newStudent.setRole(Constants.LEARNER);			
		} else {
			newStudent.setRole(Constants.KID);
		}
				
		// Log:
		log.debug("- Guardando alumno "+stdId);
				
		// Saving the student in the current institution
		boolean res = tutorManagementService.saveStudent(newStudent,currentGroup.getInstitution());
		
		// If the student as to be registered and the saving was OK...
		if (register && res) {
			// 	Retrieve student data (don't have the id), so we use the userName
			User student = tutorManagementService.getUserData(newStudent.getUserName());

			// Registering the student in the current group
			tutorManagementService.registerStudent(student,currentGroup);
			// Log:
			log.debug("- Registro de alumno alumno "+String.valueOf(student.getId()));

		}
		
		return res;
		
	} // saveStudent	
	


	/**
	 * Checks if the userName is already under use 
	 * 
	 * @return true if the userName is free, false if is used
	 */
	public boolean checkNewUserName (String userName) {
		return tutorManagementService.userNameIsAvailable(userName);
	}
	
	
		
	/**
	 * Applies for an order to the list.
	 * @return List of questions that comply with the order, needed for the callback function to repaint the list
	 */
	public List<User>  runOrderBy (String orderby, boolean reverse) {

		// Order:
		if (orderby.equals("persId")) {
			setCurrentOrder(Constants.PERSID);
		} else {
			if (orderby.equals(new String("surname"))) {
				setCurrentOrder(Constants.SURNAME);
			} else {
				if (orderby.equals(new String("name"))) {
					setCurrentOrder(Constants.NAME);
				} else {			
					if (orderby.equals("userName")) {
						setCurrentOrder(Constants.USERNAME);
					}
				}
			}
		}
		List<User> ulist = tutorManagementService.getStudents(currentGroup,currentOrder);
		if(reverse){
			Collections.reverse(ulist);
		}
		// Returns the student list conveniently sorted
		return ulist;
		
	} // runOrderBy
	
	/**
	 * Select an student to import in a group
	 * */
	
	public void selectStudent(Long idUser, boolean selected){
		User user = tutorManagementService.getUserData(idUser);
		if(selected)
			currentStudentList.add(user);
		else{
			for(int i=0; i<currentStudentList.size();i++){
				if(currentStudentList.get(i).getId().compareTo(idUser)==0)
					currentStudentList.remove(i);
			}
		}	
	}
	
	/**
	 * Gets the students for the group selected
	 * 
	 * @return List<User> 
	 * */
	
	public List<User> getStudents(Long idGroup, Long currentGroupId){
		currentGroup = tutorManagementService.getGroup(currentGroupId);
		Group gp = tutorManagementService.getGroup(idGroup);
		List<User> users = tutorManagementService.getStudents(gp);
		if(currentGroup!=null)
			for(int i=0;i<users.size();i++){
				if(!currentGroup.getStudentRole().equalsIgnoreCase("ANY")){
					if(!users.get(i).getRole().equalsIgnoreCase(currentGroup.getStudentRole())){
						users.remove(i);
						i--;
					}else{
						if(checkIfStudentIsImported(users.get(i))){
							users.remove(i);
							i--;
						}
					}
				}else{
					if(checkIfStudentIsImported(users.get(i))){
						users.remove(i);
						i--;
					}
				}
			}
		currentStudentList=new ArrayList<User>();
		return users;
	}
	
	private boolean checkIfStudentIsImported(User user){
		List<User> users = tutorManagementService.getStudents(currentGroup);
		for(int i=0;i<users.size();i++){
			if(users.get(i).getId().compareTo(user.getId())==0){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Applies for an order to the list.
	 * @return List of questions that comply with the order, needed for the callback function to repaint the list
	 */
	public List<User>  orderStudents (String orderby, Long idGroup) {

		// Order:
		if (orderby.equals("persId")) {
			setCurrentOrder(Constants.PERSID);
		} else {
			if (orderby.equals(new String("surname"))) {
				setCurrentOrder(Constants.SURNAME);
			} else {
				if (orderby.equals(new String("name"))) {
					setCurrentOrder(Constants.NAME);
				} else {			
					if (orderby.equals("userName")) {
						setCurrentOrder(Constants.USERNAME);
					}
				}
			}
		}
		Group gp = tutorManagementService.getGroup(idGroup);
		// Returns the student list conveniently sorted
		return tutorManagementService.getStudents(gp,currentOrder);
		
	} // runOrderBy
	
	/**
	 * Register the selected  students into the current group
	 * 
	 * @return List a empty user list
	 * */
	public List<User> importStudents(){
		if(currentStudentList!=null)
			for(int i=0;i<currentStudentList.size();i++){
				tutorManagementService.registerStudent(currentStudentList.get(i), currentGroup);
			}
		currentStudentList = new ArrayList<User>();
		return currentStudentList;
	}
	
	
	public int importStudentsFromCurrentList(){
		int usersImported = 0;
		if(currentGroup != null && currentStudentList != null){
			for(int i=0;i<currentStudentList.size();i++){
				if(currentStudentList.get(i) instanceof ImportUser){
					ImportUser user = (ImportUser) currentStudentList.get(i);
					/*
					 * Si el usuario se puede importar y no está repetido en la lista
					 * y el usuario no está en la BBDD lo registramos
					 * */
					if(user.isImportable() && !user.isRepeated() && !user.isInDB()){
						if(currentGroup.getStudentRole().equalsIgnoreCase(Constants.KID))
							user.setRole(Constants.KID);
						else
							user.setRole(Constants.LEARNER);
							
						tutorManagementService.saveStudent(user, currentGroup.getInstitution());
						//Aqui obtenemos el id del alumno
						User userAux = tutorManagementService.getUserData(user.getUserName());
						tutorManagementService.registerStudent(userAux, currentGroup);
						usersImported++;
					}
				}
			}
			return usersImported;
		}
		return 0;
	}
}
