package com.cesfelipesegundo.itis.dao.api;

import java.util.List;
import java.util.Map;

import com.cesfelipesegundo.itis.model.CustomExamUser;
import com.cesfelipesegundo.itis.model.Group;
import com.cesfelipesegundo.itis.model.Institution;
import com.cesfelipesegundo.itis.model.User;

/**
 * DAO que gestiona los usuarios
 * @author ivan, J. M. Colmenar
 *
 */
public interface UserDAO extends DAO {
	
	/**
	 * Devuelve un <code>User</code> a partir de su nombre de usuario
	 * 
	 * @param userName Nombre de usuario.
	 */
	public User getUser(String userName);

	/**
	 * Devuelve un <code>User</code> a partir de su id
	 * 
	 * @param id  User id.
	 */
	public User getUser(Long id);

	/**
	 * Checks the user password
	 * @param user
	 * @param passwd
	 * @return true if the password of the user matches with the argument string
	 */
	public Boolean checkUserPasswd(User user, String passwd);

	/**
	 * Updates the password of the user in the database using the argument string
	 * @param user
	 * @param passwd1
	 * @return the user object with the password update
	 */
	public User updatePassword(User user, String passwd);

	/**
	 * Insert the data of a NEW user
	 * @param usu	User object (student)
	 * @param inst		Institution related to the user
	 */
	public void saveUser(User usu, Institution inst);

	/**
	 * Updates all the data of the user BUT the userName
	 * @param usu	User object (student)
	 * @param inst		Institution related to the user
	 */
	public void updateUser(User usu, Institution inst);

	/**
	 * Register the student in the given group
	 * @param student	User object (student)
	 * @param group	Group to register to
	 */
	public void registerStudent(User student, Group group);

	/**
	 * Unregister the student from the given group
	 * @param student	User object (student)
	 * @param group	Group to de-register from
	 */
	public void unRegisterStudent(User student, Group group);

	/**
	 * Register the tutor in the given group
	 * @param tutor	User object (tutor)
	 * @param group	Group to register to
	 */
	public void registerTutor(User tutor, Group group);

	/**
	 * Unregister the tutor from the given group
	 * @param tutor	User object (tutor)
	 * @param group	Group to de-register from
	 */
	public void unRegisterTutor(User tutor, Group group);

	/**
	 * Search for tutors that are not assigned to the provided group
	 * 
	 * @param group group to search
	 * @return a list of Users (tutors) that are not assigned to group
	 */
	public  List<User> getUnAssignedTutors(Group group);

	public List<User> getAssignedTutors(Group group);

	public List<User> getTutors(Institution institution);
	
	/**
	 * Deletes a User from the database
	 * @param user
	 */
	
	public void deleteUser (User user);
	
	public List<User> getLearners(Institution institution);

	public List<User> getUsers(Institution i);

	public List<User> getUsersByFilter(Map<String, Object> map);

	public List<User> getFilteredUsers(Map<String, Object> map);

	/**
	 * Return the list width users filtered by idInstitution, idCourse and year
	 * @param name 
	 * @param apes
	 * @param user
	 * @param tipo
	 * @return List<User>
	 * */
	public List<User> getSearchUsersFiltered(String name, String apes, String user, String tipo);

	public List<User> showUsersNotVinculated();
	
	/**
	 * Return a list of users who aren't in the custom exam
	 * @param examId exam's id
	 * @param groupId group's id
	 * @return list of users who aren't in the custom exam
	 * */
	List<User> getUsersNotInCustomExam(Long examId, Long groupId);

	/**
	 * Return a list of users who are in the custom exam
	 * @param examId the exam's id
	 * @return Return List of users
	 * */
	List<CustomExamUser> getUsersInCustomExam(Long examId);

	public void addUser2CustomExam(long idExam, long idUser);

	public void removeUserFromCustomExam(long userId, long examId);
	
}