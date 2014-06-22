package es.itest.engine.test.business.entity;

import java.math.BigDecimal;
import java.util.List;


/**
 * Añade, a los datos básicos de un examen, el listado de preguntas y respuestas
 * 
 * @author chema
 * 
 */
public class TestSession extends TestDetails {

  private List<ItemSession> questions; // Lista de preguntas con contenido multimedia y respuestas
  
  private GradingParameters gradingParameters;
  
  private TestSessionParameters testSessionParameters;
  
  private BigDecimal examGrade; // Obtained after the grade: just for revision

  private long finalDate;

  public BigDecimal getExamGrade() {
    return examGrade;
  }

  public void setExamGrade(BigDecimal examGrade) {
    this.examGrade = examGrade;
  }

  public TestSessionParameters getTestSessionParameters() {
    return testSessionParameters;
  }

  public void setTestSessionParameters(TestSessionParameters testSessionParameters) {
    this.testSessionParameters = testSessionParameters;
  }


  //
  // Business methods
  //


  public void end() {
    long startingDate = 0;
    long maxDate = 0;
    int durationExam = 0;
    
    // Calculate and check exam duration, starting and ending date
    startingDate = testSessionParameters.getStartingDate();
    // Real ending date
    finalDate = System.currentTimeMillis();
    // Max ending date possible (Starting date + Pre-configured exam duration)
    maxDate = startingDate + (testSessionParameters.getDuration() * 60000);
    durationExam = 0;
    // If real ending date < max ending date possible => NO correction needed
    if (finalDate < maxDate)
      durationExam = (int) Math.round((finalDate - startingDate) / 60000.0); // in minutes
    // Else, correction to pre-configured exam duration and max ending date possible
    else {
      durationExam = testSessionParameters.getDuration();
      finalDate = maxDate;
    }
    // End date calculation

  }

  public GradingParameters getGradingParameters() {
    return gradingParameters;
  }

  public void setGradingParameters(GradingParameters gradingParameters) {
    this.gradingParameters = gradingParameters;
  }

  public BigDecimal grade() {


    BigDecimal grade = BigDecimal.ZERO;


    // Obtain the maximun exam's grade and maximun grade per question
    BigDecimal maxGrade = testSessionParameters.getMaxGrade();
    BigDecimal maxGradePerQuestion = maxGrade.divide(new BigDecimal(questions.size()));

    
    // For each question
    GradingParameters gradingParameters = getGradingParameters();
    for (ItemSession question : questions) {
      BigDecimal questionGrade = question.grade(gradingParameters);
      question.setQuestionGrade(questionGrade);
      grade = grade.add(questionGrade);
    }

    return grade;
  }

}
