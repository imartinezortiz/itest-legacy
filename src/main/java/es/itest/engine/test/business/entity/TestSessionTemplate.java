package es.itest.engine.test.business.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import es.itest.engine.course.business.entity.Group;
import es.itest.engine.course.business.entity.Subject;
import es.itest.engine.course.business.entity.TestSessionTemplateSubject;


/**
 * Añade, a los datos básicos de un examen, la configuración de un examen
 * 
 * @author chema
 * 
 * NOTA: Este es el examen proper creado
 * 
 */
public class TestSessionTemplate extends TestDetails {

  private Test test;

  private List<TestSessionTemplateSubject> subjects; // Lista de temas
  private Date startDateRevision;
  private Date endDateRevision;
  private int weight; // Weight of the exam into the final grade
  private int questionNumber;
  private boolean customized;

  private int visibility;

  private int questionDistribution;

  private BigDecimal maxGrade;

  private boolean activeReview;

  private Date startDate;

  private Date endDate;



  public boolean isCustomized() {
    return customized;
  }

  public void setCustomized(boolean customized) {
    this.customized = customized;
  }

  public TestSessionTemplate() {

  }
  
  private GradingParameters gradingParameters;
  
  private TestSessionParameters testSessionParameters;

  public TestSessionTemplate(TestSessionTemplate exam) {
    super();
    this.setGroup(exam.getGroup());
    this.setTitle(exam.getTitle());
    this.setId(exam.getId());
    this.visibility = exam.visibility;
    this.questionDistribution = exam.questionDistribution;
    this.maxGrade = exam.maxGrade;
    this.subjects = exam.subjects;
    this.activeReview = exam.activeReview;
    this.startDate = exam.startDate;
    this.endDate = exam.endDate;
    this.startDateRevision = exam.startDateRevision;
    this.endDateRevision = exam.endDateRevision;
    this.weight = exam.weight;
    this.questionNumber = exam.questionNumber;
    this.gradingParameters = gradingParameters;
    this.testSessionParameters = testSessionParameters;

    this.published = exam.published;
    this.customized = exam.customized;
  }


  /** Exam is published */
  private boolean published;


  public List<TestSessionTemplateSubject> getSubjects() {
    return subjects;
  }

  public void setSubjects(List<TestSessionTemplateSubject> subjects) {
    this.subjects = subjects;
  }

  public int getQuestionDistribution() {
    return questionDistribution;
  }

  public void setQuestionDistribution(int questionDistribution) {
    this.questionDistribution = questionDistribution;
  }

  public int getVisibility() {
    return visibility;
  }

  public void setVisibility(int visibility) {
    this.visibility = visibility;
  }

  public BigDecimal getMaxGrade() {
    return maxGrade;
  }

  public void setMaxGrade(BigDecimal maxGrade) {
    this.maxGrade = maxGrade;
  }

  public boolean isActiveReview() {
    return activeReview;
  }

  public void setActiveReview(boolean activeReview) {
    this.activeReview = activeReview;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }


  public Date getStartDateRevision() {
    return startDateRevision;
  }

  public void setStartDateRevision(Date startDateRevision) {
    this.startDateRevision = startDateRevision;
  }

  public Date getEndDateRevision() {
    return endDateRevision;
  }

  public void setEndDateRevision(Date endDateRevision) {
    this.endDateRevision = endDateRevision;
  }

  public int getQuestionNumber() {
    return questionNumber;
  }

  public void setQuestionNumber(int questionNumber) {
    this.questionNumber = questionNumber;
  }

  public boolean isPublished() {
    return published;
  }

  public void setPublished(boolean published) {
    this.published = published;
  }
  
  

  //
  // Business methods
  //

  public GradingParameters getGradingParameters() {
    return gradingParameters;
  }

  public void setGradingParameters(GradingParameters gradingParameters) {
    this.gradingParameters = gradingParameters;
  }

  public TestSessionParameters getTestSessionParameters() {
    return testSessionParameters;
  }

  public void setTestSessionParameters(TestSessionParameters testSessionParameters) {
    this.testSessionParameters = testSessionParameters;
  }

  public TestSession createTestSession() {
    Long startingDate = System.currentTimeMillis();

    TestSession exam = new TestSession();

    exam.setGradingParameters(getGradingParameters());
    exam.setTestSessionParameters(getTestSessionParameters());
    
    exam.setId(test.getId());
    exam.setTitle(test.getTitle());
    exam.setGroup(test.getGroup());

    int visibility = test.getVisibility();
    Random generator = new Random();

    List<TestSubject> templateSubjects = test.getSubjects();
    ListIterator<TestSubject> itTemplateSubjects = templateSubjects.listIterator();

    // Questions of the exam
    List<ItemSession> questions = new ArrayList<ItemSession>();
    Group examGroup = test.getGroup();

    while (itTemplateSubjects.hasNext()) {
      TestSubject templateSubject = itTemplateSubjects.next();

      int maxDifficulty = templateSubject.getMaxDifficulty();
      int minDifficulty = templateSubject.getMinDifficulty();
      int answersxQuestionNumber = templateSubject.getAnswersxQuestionNumber();
      int questionsNumber = templateSubject.getQuestionsNumber();
      List<Item> templateQuestions = templateSubject.getQuestions();

      // Supposing that questionsSize >= questionsNumber

      int questionsCount = 0;
      while (questionsCount < questionsNumber) {
        // selection randomly of the question

        int randomQuestion = generator.nextInt(templateQuestions.size());
        Item templateQuestion = templateQuestions.get(randomQuestion);

        boolean visible = false;

        Group questionGroup = templateQuestion.getGroup();
        switch (visibility) {
          case 0:
            // Group visibility
            visible = questionGroup.equals(examGroup);
            break;

          case 1:
            // Institution visibility
            visible =
                questionGroup.equals(examGroup)
                    || questionGroup.getInstitution().equals(questionGroup.getInstitution());
            break;
        }

        if ((templateQuestion.getActive() == 1)
            && (templateQuestion.getDifficulty() <= maxDifficulty)
            && (templateQuestion.getDifficulty() >= minDifficulty) && visible) {

          questionsCount++;

          ItemSession question = new ItemSession();
          questions.add(question);

          question.setComment(templateQuestion.getComment());
          question.setId(templateQuestion.getId());
          question.setMmedia(templateQuestion.getMmedia());
          question.setMmediaComment(templateQuestion.getMmediaComment());
          question.setText(templateQuestion.getText());
          question.setType(templateQuestion.getType());
          question.setNumCorrectAnswers(templateQuestion.getNumCorrectAnswers());

          List<ItemResponse> templateAnswers = templateQuestion.getAnswers();

          // Answers of the question
          List<ItemSessionResponse> answers = new ArrayList<ItemSessionResponse>();

          // Supposing that answerSize >= answersxQuestionNumber

          // Firsty, the correct answers are selected
          int answersCount = 0;

          ListIterator<ItemResponse> itTemplateAnswers = templateAnswers.listIterator();
          while (itTemplateAnswers.hasNext()) {
            ItemResponse templateAnswer = itTemplateAnswers.next();

            if ((templateAnswer.getSolution() == 1) && (templateAnswer.getActive() == 1))

            {
              answersCount++;

              ItemSessionResponse answer = new ItemSessionResponse();

              answer.setId(templateAnswer.getId());
              answer.setMmedia(templateAnswer.getMmedia());
              answer.setSolution(templateAnswer.getSolution());
              answer.setValue(templateAnswer.getValue());
              answer.setText(templateAnswer.getText());
              answer.setMarked(false);

              answers.add(answer);

              // answerExamDAO.addNewExamAnswer(idexam, idlearner,
              // question.getId(), answer.getId(),
              // startingDate);

              // Remove the answer added
              itTemplateAnswers.remove();
              templateAnswer.setUsedInExam(true);
              DomainEvents.ITEM_RESPONSE_USED.raise(templateAnswer);
            }

          } // correct answers

          // Second, some incorrect answers are selected
          // XXXX Not supposing that answerSize >=
          // answersxQuestionNumber
          //

          while ((answersCount < answersxQuestionNumber) && (templateAnswers.size() > 0)) {
            // selection randomly of the answer

            int randomAnswer = generator.nextInt(templateAnswers.size());
            ItemResponse templateAnswer = templateAnswers.get(randomAnswer);

            if (templateAnswer.getActive() == 1) {

              ItemSessionResponse answer = new ItemSessionResponse();

              answer.setId(templateAnswer.getId());
              answer.setMmedia(templateAnswer.getMmedia());
              answer.setSolution(templateAnswer.getSolution());
              answer.setValue(templateAnswer.getValue());
              answer.setText(templateAnswer.getText());
              answer.setMarked(false);

              answers.add(answer);

              // answerExamDAO.addNewExamAnswer(idexam, idlearner,
              // question.getId(), answer.getId(),
              // startingDate);

              templateAnswer.setUsedInExam(true);
              DomainEvents.ITEM_RESPONSE_USED.raise(templateAnswer);

              answersCount++;

            } // if answer can be added

            // Remove the answer added or not selectable

            templateAnswers.remove(randomAnswer);

          } // answers

          // shuffle the answers to avoid the correct answers are the
          // first

          Collections.shuffle(answers);
          question.setAnswers(answers);


          templateQuestion.setUsedInExam(true);
          DomainEvents.ITEM_USED.raise(templateQuestion);

        } // if question can be added

        // Remove the question added or not selectable
        templateQuestions.remove(randomQuestion);

      } // questions

    } // itSubjects

    Collections.shuffle(questions);

    exam.setQuestions(questions);

    return exam;
  }
  
}
