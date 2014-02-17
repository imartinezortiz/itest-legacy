package com.cesfelipesegundo.itis.dao.api;

import java.util.List;
import java.util.Map;

import com.cesfelipesegundo.itis.model.Course;
import com.cesfelipesegundo.itis.model.GroupDetails;
import com.cesfelipesegundo.itis.model.Institution;
import com.cesfelipesegundo.itis.model.User;

import es.itest.engine.course.business.entity.Group;
import es.itest.engine.test.business.entity.Item;

/**
 * Interfaz DAO para el modelo <code>Group</code>.
 * @author José Luis Risco Martín
 *
 */
public interface GroupDAO extends DAO {
	/**
	 * Función que dado un id de usuario retorna la lista de grupos que imparte.
	 * @param userId Id del usuario profesor
	 * @return Lista de grupos impartidos por el usuario dado.
	 */
	public List<Group> getTeachedGroups(Long userId);

	/**
	 * Función que dado un id de usuario retorna la lista de grupos en las que está matriculado.
	 * @param userId Id del usuario alumno
	 * @return Lista de grupos en las que está matriculado el usuario dado.
	 */
	public List<Group> getMatriculatedGroups(Long userId);
	
	/**
	 * Fills the <code>course</code> property of <code>group</code>
	 * 
	 * 
	 * @param group
	 */
	public void fillCourse(Group group);

	/**
	 * Returns basic information of a group
	 * 
	 * <p>Returns only the information stored in the group table</p>
	 * 
	 * @param id Identifier of the group
	 * 
	 * @return
	 */
	public Group getGroup(Long id);

	/**
	 * Returns the questions of the group.
	 * 
	 * @param group Group
	 * 
	 * @return Returns a list with the questions of the group
	 */
	public List<Item> getQuestions(Group group);

	/**
	 * Returns the list of students of the <code>currentGroup Group</code>
	 * 
	 * @param currentGroup
	 * @return
	 */
	public List<User> getStudents(Group currentGroup);

	/**
	 * Returns the list of students of the <code>currentGroup Group</code>
	 * 
	 * @param currentGroup
	 * @return
	 */
	public List<User> getStudents(Group currentGroup, String orderby);

	/**
	 * Returns the list of students not registered in the <code>currentGroup Group</code>, but
	 * coming from the same "institution"
	 * 
	 * @param currentGroup
	 * @return
	 */
	public List<User> getStudentsNotRegistered(Group currentGroup);

	/**
	 * Returns the list of users (students) of a specified role not registered in the current group, sorted by surname
	 * @param currentGroup
	 * @param role (LEARNER or KID)
	 * @return list of users (students) not registered in the current group but linked to the same
	 * "institution" of the the group sorted by surname
	 */
	public List<User> getStudentsNotRegistered(Group currentGroup, String role);

	/**
	 * Gets the Groups of an institution
	 * 
	 * @param institution an Institution
	 * @return
	 */
	public List<Group> getGroups(Institution institution);

	/**
	 * Gets the groups of a course
	 * 
	 * @param course
	 * @return
	 */
	public List<Group> getCourseGroups(Course course);
	
	/**
	 * Deletes a Group from the database
	 * @param Group
	 */
	public void deleteGroup(Group group);

	
	/**
	 * Inserts or updates a Group data into the database
	 * @param group
	 * @return Group object with the id filled
	 */
	public Group saveGroup(Group group);

	/**
	 * Returns the groups where is register the user
	 * @param iduser The user's id
	 * @return group
	 * */
	public List<Group> getUserGroups(Long iduser);

	/**
	 * Gets the Groups of an institution and course
	 * 
	 * @param iduser
	 * @return List<Group>
	 */
	public List<Group> getGroups(Course course, long idInstitution);

	/**
	 * Returns the list Groups filter by the map
	 * @param map
	 * @return List<Group>
	 */
	public List<Group> getGroupsByFilter(Map<String, Object> map);

	/**
	 * Returns the list Groups filter by the map
	 * @param map
	 * @return List<Group>
	 */
	public List<Group> getFilteredGroups(Map<String, Object> map);

	/**
	 * Returns the list Groups from the current user
	 * @param user
	 * @return List<Group>
	 */
	public List<Group> getUserInfoGroups(User user);


	/**
	 * Return a list of groups from the current parameters
	 * @param idInstitution
	 * @param year
	 * @param idCourse
	 * @return List<Group>
	 * */
	public List<GroupDetails> getGroupDetails(long idInstitution, String year,
			long idCourse);

	/**
	 * Return an instance of a group by the group's name
	 * @param groupName the group's name
	 * @return an instance of a group
	 * */
	public Group getGroupData(String groupName);

	/**
	 * Checks if user is registered in current group
	 * */
	public boolean isUserInGroup(long idUser, long idGroup);
	
}
