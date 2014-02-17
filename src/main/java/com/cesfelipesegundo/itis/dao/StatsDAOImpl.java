package com.cesfelipesegundo.itis.dao;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cesfelipesegundo.itis.dao.api.StatsDAO;
import com.cesfelipesegundo.itis.model.AnswerData;
import com.cesfelipesegundo.itis.model.AnswerStats;
import com.cesfelipesegundo.itis.model.AnsweredQuestionData;
import com.cesfelipesegundo.itis.model.CalifData;
import com.cesfelipesegundo.itis.model.QuestionStats;
import com.cesfelipesegundo.itis.model.comparators.AnswerStatsComparator;
import com.cesfelipesegundo.itis.model.comparators.QuestionStatsTextComparator;

import es.itest.engine.course.business.entity.Group;
import es.itest.engine.test.business.entity.ItemSessionResponse;
import es.itest.engine.test.business.entity.TestSession;
import es.itest.engine.test.business.entity.TestSessionTemplate;

public class StatsDAOImpl extends SqlMapClientDaoSupport implements StatsDAO {
	private static final Log log = LogFactory.getLog(StatsDAOImpl.class);
	/**
	 * Constructor
	 *
	 */
	public StatsDAOImpl() {
		super();
	}
	
	public List<AnsweredQuestionData> getAnsweredQuestionsData(Group group) {
		
		
//		The information to manage if an answer have been correcty answered is now calculated using the attributes
// 	    answered and answeredWithSuccess, instead of the attributes answeredGradesSum and maxGradePerQuestion
		
		
		// With this query, the field maxGradePerQuestion is not filled.
		List<AnsweredQuestionData> results = super.getSqlMapClientTemplate().queryForList("Grade.selectAnsweredQuestionDatasByGroupId", group.getId());
		
/*		
				 
		// Next I fill the empty field (It is a little redundant, but I could not think in other way to solve this point
		// I prefer the creation of a new container (Exam, MaxGrade) over make reduntant queries
	 		
		Map<Long,Double> maxGradePerQuestion = new HashMap<Long,Double>();
		for(AnsweredQuestionData result : results) 
			maxGradePerQuestion.put(result.getIdexam(), 0.0);
		
		Iterator<Long> itr = maxGradePerQuestion.keySet().iterator();
		while(itr.hasNext()) {
			Long examId = itr.next();
			Double maxGrade = (Double)super.getSqlMapClientTemplate().queryForObject("Grade.selectMaxGradePerQuestion", examId);
			if(maxGrade==null) maxGrade = Double.valueOf(0);
			maxGradePerQuestion.put(examId, maxGrade);
		}

		// I fill the maxGradePerQuestion property
		for(AnsweredQuestionData result : results)
			result.setMaxGradePerQuestion(maxGradePerQuestion.get(result.getIdexam()));
*/		
		// Next step: answeredWithSuccess property. Initially: false
		for(AnsweredQuestionData result : results) {
			//Si son preguntas tipo test
			if(result!=null){
				if(result.getQuestionType()==0){
					if(result.isAnswered()) {
						List<Long> answersPerQuestion = super.getSqlMapClientTemplate().queryForList("Grade.selectAnswersPerQuestion", result);
						// Number of answers:
						int size = answersPerQuestion.size();
						for(Long answerId : answersPerQuestion) {
							Map<String, Long> answer = new HashMap<String,Long>();
							answer.put("questionId",result.getId());
							answer.put("answerId", answerId);
							// If the answer is correct, the number of answers is decremented
							Boolean isCorrect = (Boolean)super.getSqlMapClientTemplate().queryForObject("Grade.isAnswerCorrect", answer);
							if(isCorrect) size--;
						}
						if(size==0) result.setAnsweredWithSuccess(true);
					}
				}//Si son de rellenar
				else{
					Boolean isCorrectAnswered = false;
					Map<String,Object> map = null;
					Map<String, Long> answer = new HashMap<String,Long>();
					answer.put("idpreg", result.getId());
					answer.put("idexam", result.getIdexam());
					answer.put("idalu", result.getIdalu());
					try{
						map = (Map)super.getSqlMapClientTemplate().queryForObject("Grade.getUserFillAnswer",answer);
					}catch(Exception e){
						map = null;
						List<Map<String,Object>> listMap = (List<Map<String,Object>>)super.getSqlMapClientTemplate().queryForList("Grade.getUserFillAnswer",answer);
						if(listMap!=null){
							if(listMap.size() >1){
								log.error("para la pregunta con id: "+result.getId()+", con alumno con id: "+result.getIdalu()+", con examen con id: "+result.getIdexam()+"se han recibido "+listMap.size()+"resultados cuando se esperaba 1");
							}
						}else
							log.error("Excepcion "+e.getMessage()+"  "+e.getLocalizedMessage()+"  "+e.getCause());
					}
					if(map!=null){
						String userAnswer = (String)map.get("userFillAnswer");
						String solution = (String)map.get("solution");
						result.setAnsweredWithSuccess(isCorrectFillAnswer(userAnswer, solution));
						result.setAnswered(true);
					}else{
						result.setAnswered(false);
						result.setAnsweredWithSuccess(false);
					}
				}
			}
		}
		
		return results;
	}
	
			
		
	public List<AnsweredQuestionData> getAnsweredQuestionsData(TestSessionTemplate exam)
	{
		List<AnsweredQuestionData> results = super.getSqlMapClientTemplate().queryForList("Grade.selectAnsweredQuestionDatasByExamId", exam.getId());
		for(AnsweredQuestionData result : results) {
			//Si son preguntas tipo test
			if(result != null ){
				if(result.getQuestionType()==0){
					if(result.isAnswered()) {
						List<Long> answersPerQuestion = super.getSqlMapClientTemplate().queryForList("Grade.selectAnswersPerQuestion", result);
						// Number of answers:
						int size = answersPerQuestion.size();
						for(Long answerId : answersPerQuestion) {
							Map<String, Long> answer = new HashMap<String,Long>();
							answer.put("questionId",result.getId());
							answer.put("answerId", answerId);
							// If the answer is correct, the number of answers is decremented
							Boolean isCorrect = (Boolean)super.getSqlMapClientTemplate().queryForObject("Grade.isAnswerCorrect", answer);
							if(isCorrect) size--;
						}
						if(size==0) result.setAnsweredWithSuccess(true);
					}
				}//Si son de rellenar
				else{
					Boolean isCorrectAnswered = false;
					Map<String,Object> map = null;
					Map<String, Long> answer = new HashMap<String,Long>();
					answer.put("idpreg", result.getId());
					answer.put("idexam", result.getIdexam());
					answer.put("idalu", result.getIdalu());
					map = (Map<String,Object>)super.getSqlMapClientTemplate().queryForObject("Grade.getUserFillAnswer",answer);
					if(map!=null){
						String userAnswer = (String)map.get("userFillAnswer");
						String solution = (String)map.get("solution");
						result.setAnsweredWithSuccess(isCorrectFillAnswer(userAnswer, solution));
						result.setAnswered(true);
					}else{
						result.setAnswered(false);
						result.setAnsweredWithSuccess(false);
					}
				}
			}
		}
		
		
		
		return results;
	}
	private boolean isCorrectFillAnswer(String userAnswer, String solution){
		if(userAnswer==null){
			if(solution == null || (solution!=null && solution.trim().equalsIgnoreCase("")))
				return true;
			else
				return false;
		}
		if(userAnswer.toLowerCase().trim().equalsIgnoreCase(solution.toLowerCase().trim())){
			return true;
		}else{
			return false;
		}
	}
	
	public List<CalifData> getCalifsData(Group group) {
		List<CalifData> results = super.getSqlMapClientTemplate().queryForList("Grade.selectCalifDatasByGroupId", group.getId());		
		return results;
	}

	public void deleteStudentGrade(Long idstd, Long idex) {
		// Data of the student and group
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("stdId",idstd);
		parameters.put("examId",idex);
		/* Long newKey = (Long)*/getSqlMapClientTemplate().delete("Grade.deleteStudentGrade", parameters);	
	}

	public void fillQuestionStatsByExam(List<QuestionStats> questionStats,
			TestSessionTemplate currentExam) {
		List<AnswerData> answerData = getSqlMapClientTemplate().queryForList("Grade.getAnswerTestStatsByExam",currentExam.getId());
		List<AnswerData> answerData2 = getSqlMapClientTemplate().queryForList("Grade.getAnswerFillStatsByExam",currentExam.getId());
		answerData.addAll(answerData2);
		List<AnswerStats> answerStats = getAnswerStats(answerData);
		Collections.sort(answerStats,new AnswerStatsComparator("idQuestion"));
		for(QuestionStats qstat : questionStats){
			int totalMarked = 0;
			int incorrectAnswers = 0;
			AnswerStats rest = null;
			List<AnswerStats> auxList = new ArrayList<AnswerStats>();
			for(int i=0;i< answerStats.size();i++){
				AnswerStats astat = answerStats.get(i);
				if(astat.getIdQuestion().equals(qstat.getId())){
					totalMarked +=astat.getMarkedNumber();
					if(astat.getSolution()==0){
						incorrectAnswers++;
					}
					if(incorrectAnswers>5 && astat.getSolution()==0){
						if(rest!=null){
							rest.setMarkedNumber(rest.getMarkedNumber()+astat.getMarkedNumber());
						}else{
							rest = new AnswerStats();
							rest.setIdQuestion(astat.getIdQuestion());
							rest.setIdResp((long)-1);
							rest.setRespText("");
							rest.setMarkedNumber(astat.getMarkedNumber());
						}
					}else{
						auxList.add(astat);
					}
					answerStats.remove(i);
					i--;
				}
			}
			
			Collections.sort(auxList,new AnswerStatsComparator("markedNumber"));
			Collections.sort(auxList,new AnswerStatsComparator("solution"));
			Collections.reverse(auxList);
			if(rest!=null)
				auxList.add(rest);
			
			
			qstat.setAnswerStats(auxList);
			for(AnswerStats astat : auxList){
				astat.setPercentage((((float)astat.getMarkedNumber())/((float)totalMarked))*100);
			}
		}
		
		
	}

	public void fillQuestionStatsByGroup(List<QuestionStats> questionStats,
			Group group) {
		List<AnswerData> answerData = getSqlMapClientTemplate().queryForList("Grade.getAnswerTestStatsByGroup",group.getId());
		List<AnswerData> answerData2 = getSqlMapClientTemplate().queryForList("Grade.getAnswerFillStatsByGroup",group.getId());
		answerData.addAll(answerData2);
		List<AnswerStats> answerStats = getAnswerStats(answerData);
		Collections.sort(answerStats,new AnswerStatsComparator("idQuestion"));
		for(QuestionStats qstat : questionStats){
			int totalMarked = 0;
			int incorrectAnswers = 0;
			AnswerStats rest = null;
			List<AnswerStats> auxList = new ArrayList<AnswerStats>();
			for(int i=0;i< answerStats.size();i++){
				AnswerStats astat = answerStats.get(i);
				if(astat.getIdQuestion().equals(qstat.getId())){
					totalMarked +=astat.getMarkedNumber();
					if(astat.getSolution()==0){
						incorrectAnswers++;
					}
					if(incorrectAnswers>5 && astat.getSolution()==0){
						if(rest!=null){
							rest.setMarkedNumber(rest.getMarkedNumber()+astat.getMarkedNumber());
						}else{
							rest = new AnswerStats();
							rest.setIdQuestion(astat.getIdQuestion());
							rest.setIdResp((long)-1);
							rest.setRespText("");
							rest.setMarkedNumber(astat.getMarkedNumber());
						}
					}else{
						auxList.add(astat);
					}
					answerStats.remove(i);
					i--;
				}
			}
			Collections.sort(auxList,new AnswerStatsComparator("markedNumber"));
			Collections.sort(auxList,new AnswerStatsComparator("solution"));
			Collections.reverse(auxList);
			if(rest!=null)
				auxList.add(rest);
			qstat.setAnswerStats(auxList);
			for(AnswerStats astat : auxList){
				astat.setPercentage((((float)astat.getMarkedNumber())/((float)totalMarked))*100);
			}
		}
		
	}
	
	private List<AnswerStats> getAnswerStats(List<AnswerData> answerData){
		List<AnswerStats> stats = new ArrayList<AnswerStats>();
		for(AnswerData data : answerData){
			if(data.getTipoPreg()==0){
				if(data.getMarcada()>0){
					boolean aniadir = true;
					AnswerStats aux = null;
					for(AnswerStats stat : stats){
						if(data.getQuestionId().equals(stat.getIdQuestion()) && data.getId().equals(stat.getIdResp())){
							aniadir = false;
							aux = stat;
							break;
						}
					}
					if(aniadir){
						aux = new AnswerStats();
						aux.setIdQuestion(data.getQuestionId());
						aux.setIdResp(data.getId());
						aux.setMarkedNumber(data.getMarcada());
						aux.setRespText(data.getText());
						aux.setSolution(data.getSolution());
						aux.setTipoPreg(data.getTipoPreg());
						stats.add(aux);
					}else{
						aux.setMarkedNumber(aux.getMarkedNumber()+1);
					}
				}
			}else{
				if(data.getResp()==null)
					data.setResp("");
				if(data.getText()==null)
					data.setText("");
				boolean aniadir = true;
				for(AnswerStats stat : stats){
					if(data.getQuestionId().equals(stat.getIdQuestion())){
						aniadir = false;
						break;
					}
				}
				if(aniadir){
					List<AnswerStats> list =  getSqlMapClientTemplate().queryForList("Grade.getFillAnswerStat",data.getQuestionId());
					for(AnswerStats auxStat : list){
						if(auxStat!=null && auxStat.getRespText()!=null && data.getText().toLowerCase().trim().equalsIgnoreCase(auxStat.getRespText().toLowerCase().trim())){
							auxStat.setSolution(1);
							auxStat.setTipoPreg(data.getTipoPreg());
						}else{
							auxStat.setSolution(0);
							auxStat.setTipoPreg(data.getTipoPreg());
						}
					}
					stats.addAll(list);
				}
			}
		}
		return stats;
	}
}
