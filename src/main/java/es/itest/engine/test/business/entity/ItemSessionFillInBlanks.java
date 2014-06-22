package es.itest.engine.test.business.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@DiscriminatorValue(value="F")
public class ItemSessionFillInBlanks extends ItemSession {

  private String learnerFillAnswer;


  public String getLearnerFillAnswer() {
    return learnerFillAnswer;
  }

  public void setLearnerFillAnswer(String learnerFillAnswer) {
    this.learnerFillAnswer = learnerFillAnswer;
  }

  @Override
  public BigDecimal grade(GradingParameters params) {
    BigDecimal questionGrade = BigDecimal.ZERO;
    BigDecimal maxGradePerQuestion = params.getMaxGradePerQuestion();
    
    boolean correct = true;
    
    List<ItemResponse> answers = getItem().getAnswers();
    if (answers.size() == 1) {
      ItemResponse answer = answers.get(0);
      String textLearnerAnswer = null;
      if (getLearnerFillAnswer() != null) {
        textLearnerAnswer = getLearnerFillAnswer().toLowerCase();
        questionGrade = calculateEntropy(answer, textLearnerAnswer, maxGradePerQuestion);
      }
      if (questionGrade != maxGradePerQuestion) {
        correct = false;
      }
      if (params.isConfidenceLevel() && getExamineeWasConfident()) {
        if (questionGrade.equals(BigDecimal.ZERO)) {
          // Pregunta contestada incorrectamente
          questionGrade = questionGrade.subtract(maxGradePerQuestion.multiply(params.getPenConfidenceLevel()));
        } else {
          // Pregunta contestada correctamente
          questionGrade = questionGrade.add(maxGradePerQuestion.multiply(params.getRewardConfidenceLevel()));
        }
      }
      if (!params.isPartialCorrection() && !correct) {
        if (!textLearnerAnswer.trim().equalsIgnoreCase("")) {
          questionGrade = questionGrade.subtract(maxGradePerQuestion.multiply(params.getPenQuestionFailed()));
        } else {
          questionGrade = questionGrade.subtract(maxGradePerQuestion.multiply(params.getPenQuestionNotAnswered()));
        }
      }
    }
    
    return questionGrade;
  }


  private BigDecimal calculateEntropy(ItemResponse answer, String textLearnerAnswer,
      BigDecimal maxGradePerQuestion) {
    BigDecimal result = maxGradePerQuestion;
    if (answer.getText().toLowerCase().trim()
        .equalsIgnoreCase(textLearnerAnswer.toLowerCase().trim())) {
      result = result.multiply(BigDecimal.ONE);
    } else {
      result = result.multiply(BigDecimal.ZERO);
    }
    return result;
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
