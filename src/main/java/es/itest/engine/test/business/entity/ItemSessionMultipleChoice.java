package es.itest.engine.test.business.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@DiscriminatorValue(value="M")
public class ItemSessionMultipleChoice extends ItemSession {

  private List<ItemSessionResponse> responses; // Lista de respuestas

  public List<ItemSessionResponse> getAnswers() {
    return responses;
  }

  public void setAnswers(List<ItemSessionResponse> answers) {
    this.responses = answers;
  }

  public int getCorrectAnswers() {
    int numAnswers = 0;
    for (int i = 0; i < responses.size(); i++) {
      ItemSessionResponse ea = this.responses.get(i);
      if (ea.getResponse().isSolution()) {
        numAnswers++;
      }
    }
    return numAnswers;
  }

  public int getMarkedAnswer() {
    int marked = 0;
    for (int i = 0; i < responses.size(); i++) {
      ItemSessionResponse ea = responses.get(i);
      if (ea.isMarked()) {
        marked++;
      }
    }
    return marked;
  }
  
  public BigDecimal grade(GradingParameters params) {

    BigDecimal questionGrade = BigDecimal.ZERO;
    int numCorrectAnswers = item.getNumCorrectAnswers();
    int numCorrectMarkedAnswers = 0;
    int numIncorrectMarkedAnswers = 0;
    

    // Get correct and incorrect answers marked
    for (ItemSessionResponse answer : getAnswers()) {
      if (answer.isMarked())
        if ( answer.getResponse().isSolution()) {
          numCorrectMarkedAnswers++;
        } else {
          numIncorrectMarkedAnswers++;
        }
    }
    
    /* 
     * Regular grading
     */
    if (params.isPartialCorrection()) {
      questionGrade = partialGrading(params, numCorrectAnswers, numCorrectMarkedAnswers, numIncorrectMarkedAnswers);
    } else {
      questionGrade = notPartialGrading(params, numCorrectAnswers, numCorrectMarkedAnswers, numIncorrectMarkedAnswers);
    }
    
    /*
     * Confidence level extra credit
     */
    
    if (params.isConfidenceLevel()) {
      questionGrade.add(calculateConfidendeLevelExtraCredit(params, numCorrectAnswers, numCorrectMarkedAnswers, numIncorrectMarkedAnswers));
    }
   
    return questionGrade;
  }

  private BigDecimal calculateConfidendeLevelExtraCredit(GradingParameters params, int numCorrectAnswers, int numCorrectMarkedAnswers, int numIncorrectMarkedAnswers) {

    BigDecimal extraCredit = BigDecimal.ZERO;
    
    BigDecimal maxGradePerQuestion = params.getMaxGradePerQuestion();
    if (numCorrectAnswers == numCorrectMarkedAnswers && numIncorrectMarkedAnswers == 0) {
      if (getExamineeWasConfident()) {
        extraCredit = maxGradePerQuestion.multiply(params.getRewardConfidenceLevel());
      }
    } else {
      if (getExamineeWasConfident()) {
        BigDecimal penalty = maxGradePerQuestion.multiply(params.getPenConfidenceLevel());
        extraCredit = penalty.negate();
      }
    }
    return extraCredit;
  }

  private BigDecimal notPartialGrading(GradingParameters params, int numCorrectAnswers, int numCorrectMarkedAnswers, int numIncorrectMarkedAnswers) {

    BigDecimal questionGrade = BigDecimal.ZERO;
    
    // If the question has no correct answers
    if (numCorrectAnswers == 0) {
      // Question will be correct if it has no answers marked
      if (numCorrectMarkedAnswers == 0 && numIncorrectMarkedAnswers == 0)
        questionGrade = params.getMaxGradePerQuestion();
      else {
        // No partial correction grades question to penalty for questions failed
        questionGrade = params.getMaxGradePerQuestion().multiply(params.getPenQuestionFailed()).negate();
      }
    } else {
      BigDecimal maxGradePerQuestion = params.getMaxGradePerQuestion();
      // If the question was not answered
      if (numCorrectMarkedAnswers == 0 && numIncorrectMarkedAnswers == 0)
        questionGrade = maxGradePerQuestion.multiply(params.getPenQuestionNotAnswered()).negate();
      // No database update needed (No answers marked!!!)
      else {

        // Else, the question will be qualified to maxGradePerQuestion if it was correctly answered,
        // or it will be qualified to the penalty for question failed
        if (numCorrectMarkedAnswers == numCorrectAnswers && numIncorrectMarkedAnswers == 0) {
          questionGrade = maxGradePerQuestion;
        } else {
          questionGrade = maxGradePerQuestion.multiply(params.getPenQuestionFailed()).negate();
        }
      }
    }
    return questionGrade;
  }

  private BigDecimal partialGrading(GradingParameters params, int numCorrectAnswers, int numCorrectMarkedAnswers, int numIncorrectMarkedAnswers) {

    BigDecimal questionGrade = BigDecimal.ZERO;

    /*
     * Question will be correct if it has no answers marked
     */
    if (numCorrectAnswers == 0) {
      if (numCorrectMarkedAnswers == 0 && numIncorrectMarkedAnswers == 0) {
        questionGrade = params.getMaxGradePerQuestion();
      } else {
        /*
         *  At partial correction, question with no correct answers but answered will be graded at minQuestionGrade
         */
        questionGrade = params.getMaxGradePerQuestion().multiply(params.getMinQuestionGrade());
      }
    } else {
      BigDecimal maxGradePerQuestion = params.getMaxGradePerQuestion();
      // If the question was not answered
      if (numCorrectMarkedAnswers == 0 && numIncorrectMarkedAnswers == 0)
        questionGrade = maxGradePerQuestion.multiply(params.getPenQuestionNotAnswered()).negate();
      // No database update needed (No answers marked!!!)
      else {
        // If exam has been configured to a partial correction, each question will be qualified
        // attending to the number of correct and incorrect marked answers, with its corresponding
        // penalty

        // If the question was perfectly answered, its grade is maxGradePerQuestion (avoids
        // rounding problems when the question is perfectly answered)
        if (numCorrectMarkedAnswers == numCorrectAnswers && numIncorrectMarkedAnswers == 0) {
          questionGrade = maxGradePerQuestion;
        } else {
          // Adds grade for each correct answer marked
          questionGrade = (maxGradePerQuestion.divide(new BigDecimal(numCorrectAnswers))).multiply(new BigDecimal(numCorrectMarkedAnswers));
                
          BigDecimal penalty = 
              maxGradePerQuestion.multiply(params.getPenAnswerFailed()).multiply(new BigDecimal(numIncorrectMarkedAnswers));

          // Subtracts penalty for incorrect answer marked, multiplied by the number of incorrect
          // answers marked
          questionGrade = questionGrade.subtract(penalty);

          // If calculated grade for the question is less than minQuestionGrade configured by
          // tutor for this exam, question grade will be minQuestionGrade
          BigDecimal minQuestionGrade = maxGradePerQuestion.multiply(params.getMinQuestionGrade());
          if (questionGrade.compareTo(minQuestionGrade) < 0) {
            questionGrade = minQuestionGrade;
          }
        }
      }
    }
    return questionGrade;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getAdapter(Class<T> clazz) {
    T result = null;
    if (ItemSessionMultipleChoice.class.equals(clazz)) {
      result = (T) this;
    }
    return result;
  }
}
