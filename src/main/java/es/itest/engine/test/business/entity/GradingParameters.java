package es.itest.engine.test.business.entity;

import java.math.BigDecimal;

public class GradingParameters {
  
  private BigDecimal maxGradePerQuestion;
  
  private boolean isPartialCorrection;
  
  private boolean confidenceLevel;
  
  private BigDecimal MinQuestionGrade;
  
  private BigDecimal penQuestionFailed;
  
  private BigDecimal penQuestionNotAnswered;
  
  private BigDecimal penAnswerFailed;
  
  private BigDecimal rewardConfidenceLevel;
  
  private BigDecimal penConfidenceLevel;

  public BigDecimal getMaxGradePerQuestion() {
    return maxGradePerQuestion;
  }

  public void setMaxGradePerQuestion(BigDecimal maxGradePerQuestion) {
    this.maxGradePerQuestion = maxGradePerQuestion;
  }

  public boolean isPartialCorrection() {
    return isPartialCorrection;
  }

  public boolean isConfidenceLevel() {
    return confidenceLevel;
  }

  public void setConfidenceLevel(boolean confidenceLevel) {
    this.confidenceLevel = confidenceLevel;
  }

  public void setPartialCorrection(boolean isPartialCorrection) {
    this.isPartialCorrection = isPartialCorrection;
  }

  public BigDecimal getMinQuestionGrade() {
    return MinQuestionGrade;
  }

  public void setMinQuestionGrade(BigDecimal minQuestionGrade) {
    MinQuestionGrade = minQuestionGrade;
  }

  public BigDecimal getPenQuestionFailed() {
    return penQuestionFailed;
  }

  public void setPenQuestionFailed(BigDecimal penQuestionFailed) {
    this.penQuestionFailed = penQuestionFailed;
  }

  public BigDecimal getPenQuestionNotAnswered() {
    return penQuestionNotAnswered;
  }

  public void setPenQuestionNotAnswered(BigDecimal penQuestionNotAnswered) {
    this.penQuestionNotAnswered = penQuestionNotAnswered;
  }

  public BigDecimal getPenAnswerFailed() {
    return penAnswerFailed;
  }

  public void setPenAnswerFailed(BigDecimal penAnswerFailed) {
    this.penAnswerFailed = penAnswerFailed;
  }

  public BigDecimal getRewardConfidenceLevel() {
    return rewardConfidenceLevel;
  }

  public void setRewardConfidenceLevel(BigDecimal rewardConfidenceLevel) {
    this.rewardConfidenceLevel = rewardConfidenceLevel;
  }

  public BigDecimal getPenConfidenceLevel() {
    return penConfidenceLevel;
  }

  public void setPenConfidenceLevel(BigDecimal penConfidenceLevel) {
    this.penConfidenceLevel = penConfidenceLevel;
  }
  
  
}
