package es.itest.engine.test.business.entity;

import java.math.BigDecimal;
import java.util.List;

public class ItemSessionFillInBlanks extends ItemSession {
  
  private String learnerFillAnswer;


  public String getLearnerFillAnswer() {
    return learnerFillAnswer;
  }

  public void setLearnerFillAnswer(String learnerFillAnswer) {
    this.learnerFillAnswer = learnerFillAnswer;
  }
	@Override
	public BigDecimal grade() {
		double questionGrade = 0.0;
		boolean correct = true;
		List<ItemSessionResponse> answers = getAnswers();
		if(answers.size()==1){
			ItemSessionResponse answer = answers.get(0);
			String textLearnerAnswer = null;
			if(question!=null && getLearnerFillAnswer()!=null){
				textLearnerAnswer = getLearnerFillAnswer().toLowerCase();
				questionGrade = calculateEntropy(answer,textLearnerAnswer,maxGradePerQuestion);
			}
			if(questionGrade != maxGradePerQuestion){
				correct = false;
			}
			if(currentExam.isConfidenceLevel() && getExamineeWasConfident()){
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
	
	
	private Double calculateEntropy(ItemSessionResponse answer, String textLearnerAnswer, double maxGradePerQuestion){
		double result = maxGradePerQuestion;
		if(answer.getText().toLowerCase().trim().equalsIgnoreCase(textLearnerAnswer.toLowerCase().trim())){
			return result*1.0;
		}else{
			return result*0.0;
		}
	}

	  @SuppressWarnings("unchecked")
	  @Override
	  public <T> T getAdapter(Class<T> clazz) {
	    T result = null;
	    if (ItemSessionFillInBlanks.class.equals(clazz)) {
	      result = (T) this;
	    }
	    return result;
	  }
}
