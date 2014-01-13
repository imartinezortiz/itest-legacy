package com.cesfelipesegundo.itis.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cesfelipesegundo.itis.dao.api.UserDAO;
import com.cesfelipesegundo.itis.model.CustomExamUser;
import com.cesfelipesegundo.itis.model.ExamGlobalInfo;
import com.cesfelipesegundo.itis.model.Group;
import com.cesfelipesegundo.itis.model.Institution;
import com.cesfelipesegundo.itis.model.User;

/**
 * Clase que implementa la interfaz <code>UserDAO</code> desarrollada por Iván.
 * @author José Luis Risco Martín, J.M. Colmenar
 *
 */
public class UserDAOImpl extends SqlMapClientDaoSupport implements UserDAO {
	/**
	 * Constructor.
	 *
	 */
	public UserDAOImpl() {
		super();
	}
	
	/**
	 * Función que dado un nombre de usuario retorna el usuario instanciado de la base de datos.
	 * @param userName Nombre del usuario en la aplicación, se corresponde con
	 * la columna usuario de la tabla usuarios. 
	 */
	public User getUser(String userName) {
		User record = (User)getSqlMapClientTemplate().queryForObject("User.selectByUserName", userName);
		return record;
	}

	public User getUser(Long id) {
		User record = (User)getSqlMapClientTemplate().queryForObject("User.getUserById", id);
		return record;
	}

	public Boolean checkUserPasswd(User user, String passwd) {
		// If a record is retured, the passwd is correct
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("userId",user.getId());
		parameters.put("pass",passwd);
		User record = (User)getSqlMapClientTemplate().queryForObject("User.getUserByIdAndPassw", parameters);
		return (record != null);
	}

	public User updatePassword(User user, String passwd) {
		// Update the password (assume the user already exists)
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("userId",user.getId());
		parameters.put("pass",passwd);
		/*int rows =*/ super.getSqlMapClientTemplate().update("User.updateUserPassword", parameters);	
		
		// Then, retrieves the user data
		return getUser(user.getId());
	}

	public void saveUser(User usu, Institution inst) {
		// First, inserts the data of a new user
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("user",usu);
		parameters.put("inst",inst);
		/* Long newKey = (Long)*/getSqlMapClientTemplate().insert("User.insertUserData", parameters);
		// Second, the role is inserted
		getSqlMapClientTemplate().insert("User.insertUserRole", usu);
	}

	public void updateUser(User usu, Institution inst) {
		// First, updates the data of a new user
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("user",usu);
		parameters.put("inst",inst);
		super.getSqlMapClientTemplate().update("User.updateUserData", parameters);
		// Second, the role is inserted
		super.getSqlMapClientTemplate().update("User.updateUserRoleByUserName", usu);	
	}

	public void registerStudent(User student, Group group) {
		// Data of the student and group
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("stdId",student.getId());
		parameters.put("groupId",group.getId());
		/* Long newKey = (Long)*/getSqlMapClientTemplate().insert("User.registerStudent", parameters);		
	}

	public void unRegisterStudent(User student, Group group) {
		// Data of the student and group
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("stdId",student.getId());
		parameters.put("groupId",group.getId());
		/* Long newKey = (Long)*/getSqlMapClientTemplate().delete("User.unRegisterStudent", parameters);
	}

	public void registerTutor(User student, Group group) {
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("tutorId",student.getId());
		parameters.put("groupId",group.getId());
		/* Long newKey = (Long)*/getSqlMapClientTemplate().insert("User.registerTutor", parameters);		
	}

	public void unRegisterTutor(User student, Group group) {
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("tutorId",student.getId());
		parameters.put("groupId",group.getId());
		/* Long newKey = (Long)*/getSqlMapClientTemplate().delete("User.unRegisterTutor", parameters);
	}

	@SuppressWarnings("unchecked")
	public List<User> getAssignedTutors(Group group) {
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("groupId",group.getId());
		parameters.put("institutionId",group.getInstitution().getId());
		return getSqlMapClientTemplate().queryForList("User.getAssignedTutors", parameters);
	}


	@SuppressWarnings("unchecked")
	public List<User> getUnAssignedTutors(Group group) {
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("groupId",group.getId());
		parameters.put("institutionId",group.getInstitution().getId());
		return getSqlMapClientTemplate().queryForList("User.getUnAssignedTutors", parameters);
	}

	@SuppressWarnings("unchecked")
	public List<User> getTutors(Institution institution) {
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("institutionId",institution.getId());
		return getSqlMapClientTemplate().queryForList("User.getTutors", parameters);
	}

	@SuppressWarnings("unchecked")
	public List<User> getLearners(Institution institution) {
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("institutionId",institution.getId());
		return getSqlMapClientTemplate().queryForList("User.getLearners", parameters);
	}
	
	public void deleteUser(User user) {
		super.getSqlMapClientTemplate().delete("User.deleteUser", user);
		
	}
	
	@SuppressWarnings("unchecked")
	public List<User> getUsers(Institution institution) {
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("institutionId",institution.getId());
		return getSqlMapClientTemplate().queryForList("User.getUsers", parameters);
	}

	public List<User> getUsersByFilter(Map<String, Object> map) {
		List<User> userList = super.getSqlMapClientTemplate().queryForList("User.getUsersByFilter",map);
		return userList;
	}

	public List<User> getFilteredUsers(Map<String, Object> map) {
		List<User> userList = super.getSqlMapClientTemplate().queryForList("User.getUsersSearched",map);
		return userList;
	}

	public List<User> getSearchUsersFiltered(String name, String apes, String user, String tipo) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("name", "%"+name+"%");
		map.put("apes","%"+apes+"%");
		map.put("user","%"+user+"%");
		if(tipo == null || tipo.trim().equalsIgnoreCase("")){
			map.put("type",null);
		}else{
			map.put("type","%"+tipo+"%");
		}
		return (List<User>)super.getSqlMapClientTemplate().queryForList("User.getSearchUsersFiltered",map);
	}

	public List<User> showUsersNotVinculated() {
		return (List<User>)super.getSqlMapClientTemplate().queryForList("User.showUsersNotVinculated");
	}
	
	public List<User> getUsersNotInCustomExam(Long examId, Long groupId) {
		HashMap<String,Long> map = new HashMap<String,Long>();
		map.put("idExam", examId);
		map.put("idGroup", groupId);
		List<User> users = super.getSqlMapClientTemplate().queryForList("User.getUsersNotInCustomExam",map);
		return users;
	}

	public List<CustomExamUser> getUsersInCustomExam(Long examId) {
		List<CustomExamUser> users = super.getSqlMapClientTemplate().queryForList("User.getUsersInCustomExam",examId);
		HashMap<String,Long> map = null;
		for(CustomExamUser user : users){
			map = new HashMap<String,Long>();
			map.put("idexam",examId);
			map.put("iduser", user.getId());
			if(super.getSqlMapClientTemplate().queryForObject("Grade.selectGrade",map)!=null){
				user.setInExam(true);
			}else{
				user.setInExam(false);
			}
		}
		return users;
	}

	public void addUser2CustomExam(long idExam, long idUser) {
		HashMap<String,Long> map = new HashMap<String,Long>();
		map.put("idExam",idExam);
		map.put("idUser", idUser);
		super.getSqlMapClientTemplate().insert("User.addUser2CustomExam",map);
	}

	public void removeUserFromCustomExam(long userId, long examId) {
		HashMap<String,Long> map = new HashMap<String,Long>();
		map.put("userId", userId);
		map.put("examId", examId);
		super.getSqlMapClientTemplate().delete("User.removeUserFromCustomExam",map);
	}
}
