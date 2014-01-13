package com.cesfelipesegundo.itis.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cesfelipesegundo.itis.dao.api.GroupDAO;
import com.cesfelipesegundo.itis.model.Course;
import com.cesfelipesegundo.itis.model.Group;
import com.cesfelipesegundo.itis.model.GroupDetails;
import com.cesfelipesegundo.itis.model.Institution;
import com.cesfelipesegundo.itis.model.MediaElem;
import com.cesfelipesegundo.itis.model.TemplateExamAnswer;
import com.cesfelipesegundo.itis.model.TemplateExamQuestion;
import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.web.Constants;

public class GroupDAOImpl extends SqlMapClientDaoSupport implements GroupDAO {
	/**
	 * Constructor
	 *
	 */
	public GroupDAOImpl() {
		super();
	}
	
	public List<Group> getTeachedGroups(Long userId) {
		List<Group> list = super.getSqlMapClientTemplate().queryForList("Group.getTeachedGroups", userId);
		return list;
	}

	
	public List<Group> getMatriculatedGroups(Long userId)
	{
		List<Group> list = super.getSqlMapClientTemplate().queryForList("Group.getMatriculatedGroups", userId);
		return list;
	}
	
	public void fillCourse(Group group) {
		Course course = (Course)super.getSqlMapClientTemplate().queryForObject("Group.fillCourse", group.getId());
		group.setCourse(course);
	}

	public Group getGroup(Long id) {
		Group group = new Group();
		group = (Group)super.getSqlMapClientTemplate().queryForObject("Group.getGroup", id);
		return group;
	}

	public List<TemplateExamQuestion> getQuestions(Group group) {
		// Lista de preguntas del grupo correspondiente
		List<TemplateExamQuestion> questions = super.getSqlMapClientTemplate().queryForList("TemplateExam.getTemplateExamQuestionByGroupId", group.getId());

		// Además, a cada question hay que añadirle:
		for(TemplateExamQuestion question : questions) {
			
			// Lista de los elementos multimedia
			List<MediaElem> questionMedia = super.getSqlMapClientTemplate().queryForList("TemplateExam.getQuestionMedia", question.getId());
			question.setMmedia(questionMedia);
			
			// Lista de respuestas a esta pregunta
			List<TemplateExamAnswer> answers = super.getSqlMapClientTemplate().queryForList("TemplateExam.getTemplateExamAnswer", question.getId());
			question.setAnswers(answers);
			
			// Y finalmente a cada respuesta hay que añadirle
			for(TemplateExamAnswer answer : answers) {
				// Lista de elementos multimedia
				List<MediaElem> answerMedia = super.getSqlMapClientTemplate().queryForList("TemplateExam.getAnswerMedia", answer.getId());
				answer.setMmedia(answerMedia);
			}
		}

		return questions;
	}

	public List<User> getStudents(Group currentGroup) {
		// By default, the order is the surname
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("groupId",currentGroup.getId());
		parameters.put("orderBy","ORDER BY "+Constants.SURNAME+", "+Constants.NAME+" ASC");
		List<User> users = super.getSqlMapClientTemplate().queryForList("Group.getStudentsByGroupId", parameters);
		return users;
	}

	public List<User> getStudents(Group currentGroup, String orderby) {
		// By default, the order is the surname
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("groupId",currentGroup.getId());
		parameters.put("orderBy","ORDER BY "+orderby+" ASC");
		List<User> users = super.getSqlMapClientTemplate().queryForList("Group.getStudentsByGroupId", parameters);
		return users;
	}	
	
	public List<User> getStudentsNotRegistered(Group currentGroup) {
		List<User> users = super.getSqlMapClientTemplate().queryForList("Group.getStudentsNotInGroupById", currentGroup);
		return users;
	}

	public List<User> getStudentsNotRegistered(Group currentGroup, String role) {
		// The order is the surname
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("groupId", currentGroup.getId());
		parameters.put("institutionId", currentGroup.getInstitution().getId());
		parameters.put("roleId", role);
		List<User> users = super.getSqlMapClientTemplate().queryForList("Group.getStudentsNotInGroupByIdAndRole", parameters);
		return users;
	}

	public List<Group> getGroups(Institution institution) {
		List<Group> groups = super.getSqlMapClientTemplate().queryForList("Group.getInstitutionGroups", institution.getId());
		return groups;
	}

	public List<Group> getCourseGroups(Course course) {
		List<Group> groups = super.getSqlMapClientTemplate().queryForList("Group.getCourseGroups", course.getId());
		return groups;
	}
	
	public Group saveGroup(Group group) {
		
		if(group.getId() == null) {
			// New Group has to be created.
			Long newKey = (Long)super.getSqlMapClientTemplate().insert("Group.insertGroup", group);
			group.setId(newKey);
		} else {
			// Current Group data have to be updated.
			super.getSqlMapClientTemplate().update("Group.updateGroup", group);
		}
		return group;
	}
	
	public void deleteGroup(Group group) {
		super.getSqlMapClientTemplate().delete("Group.deleteGroup", group);
	}

	public List<Group> getUserGroups(Long iduser) {
		return super.getSqlMapClientTemplate().queryForList("Group.getUserGroups", iduser);
	}

	public List<Group> getGroups(Course course, long idInstitution) {
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("center",idInstitution);
		parameters.put("course",course.getId());
		List<Group> groups = super.getSqlMapClientTemplate().queryForList("Group.getInstitutionCourseGroups", parameters);
		return groups;
	}

	public List<Group> getGroupsByFilter(Map<String, Object> map) {
		List<Group> groups = super.getSqlMapClientTemplate().queryForList("Group.getGroupsByFilter", map);
		return groups;
	}

	public List<Group> getFilteredGroups(Map<String, Object> map) {
		return super.getSqlMapClientTemplate().queryForList("Group.getFilteredGroups", map);
	}

	public List<Group> getUserInfoGroups(User user) {
		List<Group> groupList = null;
		if(user.getRole().equalsIgnoreCase(Constants.TUTOR) || user.getRole().equalsIgnoreCase(Constants.TUTORAV))
			groupList = ((List<Group>)super.getSqlMapClientTemplate().queryForList("Group.getTutorInfoGroup", user.getId()));
		else
			groupList = ((List<Group>)super.getSqlMapClientTemplate().queryForList("Group.getLearnerInfoGroup", user.getId()));
		return groupList;
	}

	public List<GroupDetails> getGroupDetails(long idInstitution, String year,
			long idCourse) {
		Map<String,Object> map = new HashMap<String,Object>();
		if(idInstitution <=0){
			map.put("institution", null);
		}else{
			map.put("institution", idInstitution);
		}
		if(idCourse<=0){
			map.put("course", null);
		}else{
			map.put("course", idCourse);
		}
		if(year.equalsIgnoreCase("-1")||year.equals("")){
			map.put("year", null);
		}else{
			map.put("year", year);
		}
		List<GroupDetails> list = ((List<GroupDetails>)super.getSqlMapClientTemplate().queryForList("Group.getGroupsDetails", map));
		for(int i=0;i<list.size();i++){
			GroupDetails groupDetails = list.get(i);
			list.remove(i);
			groupDetails = fillGroupDetails(groupDetails);
			list.add(i,groupDetails);
		}
		return list;
	}
	public GroupDetails fillGroupDetails (GroupDetails groupDetails){
		groupDetails.setNumQuestion((Integer)super.getSqlMapClientTemplate().queryForObject("Group.getNumQuestionForGroup",groupDetails.getIdGroup()));
		groupDetails.setNumTheme((Integer)super.getSqlMapClientTemplate().queryForObject("Group.getNumThemesForGroup",groupDetails.getIdGroup()));
		groupDetails.setTeachers((List<String>) super.getSqlMapClientTemplate().queryForList("Group.getTeachersByGroup",groupDetails.getIdGroup()));
		return groupDetails;
	}

	public Group getGroupData(String groupName) {
		return (Group)super.getSqlMapClientTemplate().queryForObject("Group.getGroupData",groupName);
	}

	public boolean isUserInGroup(long idUser, long idGroup) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("idUser", idUser);
		map.put("idGroup", idGroup);
		return (Boolean) super.getSqlMapClientTemplate().queryForObject("Group.isUserInGroup",map);
	}
}