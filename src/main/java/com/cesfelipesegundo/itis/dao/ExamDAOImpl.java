package com.cesfelipesegundo.itis.dao;


import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cesfelipesegundo.itis.dao.api.ExamDAO;
import com.cesfelipesegundo.itis.model.CourseStats;
import com.cesfelipesegundo.itis.model.Exam;
import com.cesfelipesegundo.itis.model.ExamAnswer;
import com.cesfelipesegundo.itis.model.ExamGlobalInfo;
import com.cesfelipesegundo.itis.model.ExamQuestion;
import com.cesfelipesegundo.itis.model.Grade;
import com.cesfelipesegundo.itis.model.MediaElem;
import com.cesfelipesegundo.itis.model.ExamForReview;
import com.cesfelipesegundo.itis.model.TemplateExamQuestion;
import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.model.comparators.ExamGlobalInfoIdComparator;

public class ExamDAOImpl extends SqlMapClientDaoSupport implements ExamDAO {
	/**
	 * Constructor
	 *
	 */
	public ExamDAOImpl() {
		super();
	}
	
	public Exam getAlreadyDoneExam(User user, Long id) {
		
		// En primer lugar cojo la información básica del examen
		// Sin la lista de preguntas.
		HashMap<String,Long> parameters = new HashMap<String,Long>();
		parameters.put("idUser", user.getId());
		parameters.put("idExam", id);
		Exam exam = (Exam)super.getSqlMapClientTemplate().queryForObject("Exam.getExam", parameters);
		
		// Retrieve questions only if exam was already done
		if (exam != null) {
		
			// Lista de preguntas del examen correspondienete hecho por user
			List<ExamQuestion> questions = super.getSqlMapClientTemplate().queryForList("Exam.getExamQuestion", parameters);
			
	
			// A cada question hay que añadirle:
			for(ExamQuestion question : questions) {
				
				// Lista de los elementos multimedia
				List<MediaElem> questionMedia = super.getSqlMapClientTemplate().queryForList("Exam.getQuestionMedia", question.getId());
				question.setMmedia(questionMedia);
				List<MediaElem> questionMediaComment = super.getSqlMapClientTemplate().queryForList("Exam.getCommentMedia", question.getId());
				question.setMmediaComment(questionMediaComment);
					
				// Lista de respuestas a esta pregunta
				parameters.put("idQuestion", question.getId());
				List<ExamAnswer> answers = super.getSqlMapClientTemplate().queryForList("Exam.getExamAnswer", parameters);
				question.setAnswers(answers);
					
				// Y finalmente a cada respuesta hay que añadirle
				for(ExamAnswer answer : answers) {
					// Lista de elementos multimedia
					List<MediaElem> answerMedia = super.getSqlMapClientTemplate().queryForList("Exam.getAnswerMedia", answer.getId());
					answer.setMmedia(answerMedia);
				}
			} // for
			
			List<ExamQuestion> fillQuestions = super.getSqlMapClientTemplate().queryForList("Exam.getExamFillQuestion", parameters);
			for(ExamQuestion question : fillQuestions){
				List<MediaElem> questionMedia = super.getSqlMapClientTemplate().queryForList("Exam.getQuestionMedia", question.getId());
				question.setMmedia(questionMedia);
				List<MediaElem> questionMediaComment = super.getSqlMapClientTemplate().queryForList("Exam.getCommentMedia", question.getId());
				question.setMmediaComment(questionMediaComment);
				
				// Lista de respuestas a esta pregunta
				parameters.put("idQuestion", question.getId());
				List<ExamAnswer> answers = super.getSqlMapClientTemplate().queryForList("Exam.getExamFillAnswer", parameters);
				question.setAnswers(answers);
				
			}
			
			questions.addAll(fillQuestions);
			exam.setQuestions(questions);
			
		} // if
		
		return exam;
	}
	
	public ExamQuestion getNextQuestion(User user, Long idExam, Long lastQuestionId){
		ExamQuestion question = null;

		HashMap<String,Long> parameters = new HashMap<String,Long>();
		parameters.put("idUser", user.getId());
		parameters.put("idExam", idExam);
		parameters.put("lastId", lastQuestionId);
		
		question = (ExamQuestion) super.getSqlMapClientTemplate().queryForObject("Exam.getNextQuestion", parameters);

		// Lista de los elementos multimedia
		List<MediaElem> questionMedia = super.getSqlMapClientTemplate().queryForList("Exam.getQuestionMedia", question.getId());
		question.setMmedia(questionMedia);
		List<MediaElem> questionMediaComment = super.getSqlMapClientTemplate().queryForList("Exam.getCommentMedia", question.getId());
		question.setMmediaComment(questionMediaComment);
		// Lista de respuestas a esta pregunta
		parameters.put("idQuestion", question.getId());
		List<ExamAnswer> answers = super.getSqlMapClientTemplate().queryForList("Exam.getExamAnswer", parameters);
		question.setAnswers(answers);
			
		// Y finalmente a cada respuesta hay que añadirle
		for(ExamAnswer answer : answers) {
			// Lista de elementos multimedia
			List<MediaElem> answerMedia = super.getSqlMapClientTemplate().queryForList("Exam.getAnswerMedia", answer.getId());
			answer.setMmedia(answerMedia);
		}
		
		return question;
	}

	public void deleteStudentExam(Long idstd, Long idex) {
		// Data of the student and group
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("stdId",idstd);
		parameters.put("examId",idex);
		/* Long newKey = (Long)*/getSqlMapClientTemplate().delete("Exam.deleteStudentExam", parameters);
		getSqlMapClientTemplate().delete("Exam.deleteStudentExamFill",parameters);
	}
	
	public void deleteQuestionFromExam(Long idex, Long idPreg) {
		// Data of the student and group
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("examId",idex);
		parameters.put("idpreg",idPreg);
		getSqlMapClientTemplate().delete("Exam.deleteQuestionFromExam", parameters);
		getSqlMapClientTemplate().delete("Exam.deleteFillQuestionFromExam",parameters);
	}
	
	public List<ExamForReview> getExamsByQuestion(Long idQuestion){
		return super.getSqlMapClientTemplate().queryForList("Exam.getExamsByQuestion", idQuestion);
	}
	
	public List<ExamForReview> getExamsByIdExam(Long idExam){
		return super.getSqlMapClientTemplate().queryForList("Exam.getExamsByIdExam", idExam);
	}

	public List<ExamGlobalInfo> getNextExams() {
		List<ExamGlobalInfo> nextExams = (List<ExamGlobalInfo>)super.getSqlMapClientTemplate().queryForList("TemplateExam.getNextExams");
		nextExams = fillExamsGlobalInfo(nextExams,true);
		return nextExams;
	}
	
	public List<ExamGlobalInfo> getActiveExams() {
		List<ExamGlobalInfo> nextExams = (List<ExamGlobalInfo>)super.getSqlMapClientTemplate().queryForList("TemplateExam.getActiveExams");
		nextExams = fillExamsGlobalInfo(nextExams,false);
		return nextExams;
	}
	
	private List<ExamGlobalInfo> fillExamsGlobalInfo(List<ExamGlobalInfo> listExams, boolean isFromNextExam){
		/*
		 * listExams must be sorted by examId
		 * */
		for(int i=0;i<listExams.size()-1;i++){
			if((i+1)<listExams.size()){
				if(listExams.get(i).getExamId().equals(listExams.get(i+1).getExamId())){
					listExams.get(i).getTeachers().add(listExams.get(i+1).getTeacher());
					listExams.remove(i+1);
					i--;
				}
			}
		}
		//Removes all the exams unless the first 10
		if((isFromNextExam)&&(listExams.size()>10)){
			for(int i=10;i<listExams.size();i++){
				listExams.remove(i);
			}
		}
		return listExams;
	}

	public Grade getExamGrade(Long idexam, Long iduser) {
		HashMap<String,Long> parameters = new HashMap<String,Long>();
		parameters.put("idexam", idexam);
		parameters.put("iduser", iduser);
		return (Grade) super.getSqlMapClientTemplate().queryForObject("Grade.selectGrade", parameters);
	}

	public Exam getExamById(Long idexam, Long iduser) {
		HashMap<String,Long> parameters = new HashMap<String,Long>();
		parameters.put("idExam", idexam);
		parameters.put("idUser", iduser);
		Exam exam = (Exam)super.getSqlMapClientTemplate().queryForObject("Exam.getExam", parameters);
		List<ExamQuestion> questions = super.getSqlMapClientTemplate().queryForList("Exam.getExamQuestion", parameters);
		for(ExamQuestion question : questions) {
			
			// Lista de los elementos multimedia
			List<MediaElem> questionMedia = super.getSqlMapClientTemplate().queryForList("Exam.getQuestionMedia", question.getId());
			question.setMmedia(questionMedia);
			List<MediaElem> questionMediaComment = super.getSqlMapClientTemplate().queryForList("Exam.getCommentMedia", question.getId());
			question.setMmediaComment(questionMediaComment);
				
			// Lista de respuestas a esta pregunta
			parameters.put("idQuestion", question.getId());
			List<ExamAnswer> answers = super.getSqlMapClientTemplate().queryForList("Exam.getExamAnswer", parameters);
			question.setAnswers(answers);
				
			// Y finalmente a cada respuesta hay que añadirle
			for(ExamAnswer answer : answers) {
				// Lista de elementos multimedia
				List<MediaElem> answerMedia = super.getSqlMapClientTemplate().queryForList("Exam.getAnswerMedia", answer.getId());
				answer.setMmedia(answerMedia);
			}
		} // for
		List<ExamQuestion> fillQuestions = super.getSqlMapClientTemplate().queryForList("Exam.getExamFillQuestion", parameters);
		for(ExamQuestion question : fillQuestions){
			List<MediaElem> questionMedia = super.getSqlMapClientTemplate().queryForList("Exam.getQuestionMedia", question.getId());
			question.setMmedia(questionMedia);
			List<MediaElem> questionMediaComment = super.getSqlMapClientTemplate().queryForList("Exam.getCommentMedia", question.getId());
			question.setMmediaComment(questionMediaComment);
			
			// Lista de respuestas a esta pregunta
			parameters.put("idQuestion", question.getId());
			List<ExamAnswer> answers = super.getSqlMapClientTemplate().queryForList("Exam.getExamFillAnswer", parameters);
			question.setAnswers(answers);
			
		}
		questions.addAll(fillQuestions);
		exam.setQuestions(questions);
		return exam;
	}

	
	public List<Exam> getAlreadyDoneExamGradeByGroup(long iduser, long idgroup){
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("user", iduser);
		map.put("group", idgroup);
		return (List<Exam>) super.getSqlMapClientTemplate().queryForList("Exam.selectExamByGroupAndUser",map);
	}

	public List<Long> getExamIds(TemplateExamQuestion question) {
		return (List<Long>) super.getSqlMapClientTemplate().queryForList("Exam.getExamIds",question.getId());
	}

	public List<ExamGlobalInfo> getPreviousExamsFiltered(long idInstitution,
			long idCourse, String year) {
		Map<String,Object> map = new HashMap<String,Object>();
		if(idInstitution <= 0){
			map.put("idInstitution", null);
		}else{
			map.put("idInstitution", idInstitution);
		}
		if(idCourse <= 0){
			map.put("idCourse", null);
		}else{
			map.put("idCourse", idCourse);
		}
		if(year.equalsIgnoreCase("") || year.equalsIgnoreCase("-1")){
			map.put("year", null);
		}else{
			map.put("year",year );
		}
		List<ExamGlobalInfo> previousExams = (List<ExamGlobalInfo>)super.getSqlMapClientTemplate().queryForList("TemplateExam.getPreviousExams",map);
		fillExamsGlobalInfo(previousExams,false);
		return previousExams;
	}

	public CourseStats getPreviousExamsFiltered(int idexam) {
		CourseStats stats = new CourseStats();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("idexam", idexam);
		map.put("valMin", -10);
		map.put("valMax", 4.999999999);
		stats.setSs(((Integer) super.getSqlMapClientTemplate().queryForObject("Exam.getExamCalifs",map)));
		map.remove("valMin");
		map.remove("valMax");
		map.put("valMin", 5);
		map.put("valMax", 6.999999999);
		stats.setAp(((Integer) super.getSqlMapClientTemplate().queryForObject("Exam.getExamCalifs",map)));
		map.remove("valMin");
		map.remove("valMax");
		map.put("valMin", 7);
		map.put("valMax", 8.999999999);
		stats.setNt(((Integer) super.getSqlMapClientTemplate().queryForObject("Exam.getExamCalifs",map)));
		map.remove("valMin");
		map.remove("valMax");
		map.put("valMin", 9);
		map.put("valMax", 10.00000001);
		stats.setSb(((Integer) super.getSqlMapClientTemplate().queryForObject("Exam.getExamCalifs",map)));
		stats.setGroups(1);
		stats.setTotalStudentByGroup(((Integer) super.getSqlMapClientTemplate().queryForObject("Exam.getAllStudentGroup",map)));
		stats.setNumExams(((Integer) super.getSqlMapClientTemplate().queryForObject("Exam.getNumExams",map)));
		return stats;
	}

	public List<Exam> getAllExams(Long idexam) {
		return (((List<Exam>) super.getSqlMapClientTemplate().queryForList("Exam.getAllExamsForId",idexam)));
	}

	public List<ExamGlobalInfo> getActiveExamsFiltered(String centro,
			String asignatura, Date startDate, Date endDate) {
		Map<String,Object> map = new HashMap<String,Object>();
		if(centro!=null && !centro.equalsIgnoreCase(""))
			map.put("centro", "%"+centro+"%");
		if(asignatura!=null && !asignatura.equalsIgnoreCase(""))
			map.put("asignatura", "%"+asignatura+"%");
		if(startDate!=null && !startDate.toString().equalsIgnoreCase("Thu Jan 01 01:00:00 CET 1970"))
			map.put("startDate", startDate);
		if(endDate!=null && !endDate.toString().equalsIgnoreCase("Thu Jan 01 01:00:00 CET 1970"))
			map.put("endDate", endDate);
		
		List<ExamGlobalInfo> activeExams = (List<ExamGlobalInfo>)super.getSqlMapClientTemplate().queryForList("TemplateExam.getActiveExamsFiltered",map);
		Collections.sort(activeExams,new ExamGlobalInfoIdComparator());
		fillExamsGlobalInfo(activeExams,false);
		return activeExams;	
	}

	public List<ExamGlobalInfo> getNextExamsFiltered(String centro,
			String asignatura, Date startDate, Date endDate) {
		Map<String,Object> map = new HashMap<String,Object>();
		if(centro!=null && !centro.equalsIgnoreCase(""))
			map.put("centro", "%"+centro+"%");
		if(asignatura!=null && !asignatura.equalsIgnoreCase(""))
			map.put("asignatura", "%"+asignatura+"%");
		if(startDate!=null && !startDate.toString().equalsIgnoreCase("Thu Jan 01 01:00:00 CET 1970"))
			map.put("startDate", startDate);
		if(endDate!=null && !endDate.toString().equalsIgnoreCase("Thu Jan 01 01:00:00 CET 1970"))
			map.put("endDate", endDate);
		
		List<ExamGlobalInfo> nextExams = (List<ExamGlobalInfo>)super.getSqlMapClientTemplate().queryForList("TemplateExam.getNextExamsFiltered",map);
		Collections.sort(nextExams,new ExamGlobalInfoIdComparator());
		fillExamsGlobalInfo(nextExams,false);
		return nextExams;
	}
}
