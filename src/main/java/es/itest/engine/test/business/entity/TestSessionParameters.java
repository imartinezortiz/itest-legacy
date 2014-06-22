package es.itest.engine.test.business.entity;

import java.math.BigDecimal;
import java.util.Date;

public class TestSessionParameters {

  private int duration;

  private BigDecimal maxGrade;

  private long startingDate;

  private Date startingDateExam;

  /** Sets partial correction by answers */
  private boolean partialCorrection;

  /** Show number of correct answers */
  private boolean showNumCorrectAnswers;

  /** Exam is active confidence level */
  private boolean confidenceLevel;

  /** Min question grade */
  private double minQuestionGrade;

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public BigDecimal getMaxGrade() {
    return maxGrade;
  }

  public void setMaxGrade(BigDecimal maxGrade) {
    this.maxGrade = maxGrade;
  }

  public long getStartingDate() {
    return startingDate;

  }

  public void setStartingDate(long startingDate) {
    this.startingDate = startingDate;
  }

  public Date getStartingDateExam() {
    return startingDateExam;
  }

  public void setStartingDateExam(Date startingDateExam) {
    this.startingDateExam = startingDateExam;
    this.startingDate = startingDateExam.getTime();
  }

  public boolean isConfidenceLevel() {
    return confidenceLevel;
  }

  public void setConfidenceLevel(boolean confidenceLevel) {
    this.confidenceLevel = confidenceLevel;
  }

  public boolean isShowNumCorrectAnswers() {
    return showNumCorrectAnswers;
  }

  public void setShowNumCorrectAnswers(boolean showNumCorrectAnswers) {
    this.showNumCorrectAnswers = showNumCorrectAnswers;
  }

}
