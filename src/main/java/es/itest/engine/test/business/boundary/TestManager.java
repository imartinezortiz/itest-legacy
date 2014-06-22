package es.itest.engine.test.business.boundary;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;

import com.cesfelipesegundo.itis.dao.api.AnswerExamDAO;
import com.cesfelipesegundo.itis.dao.api.BasicDataExamDAO;
import com.cesfelipesegundo.itis.dao.api.ConfigExamDAO;
import com.cesfelipesegundo.itis.dao.api.ExamDAO;
import com.cesfelipesegundo.itis.dao.api.TemplateExamAnswerDAO;
import com.cesfelipesegundo.itis.dao.api.TemplateExamDAO;
import com.cesfelipesegundo.itis.dao.api.TemplateExamQuestionDAO;
import com.cesfelipesegundo.itis.dao.api.TemplateGradeDAO;
import com.cesfelipesegundo.itis.dao.api.UserDAO;
import com.cesfelipesegundo.itis.model.CustomExamUser;
import com.cesfelipesegundo.itis.model.ExamGlobalInfo;
import com.cesfelipesegundo.itis.model.Grade;
import com.cesfelipesegundo.itis.model.Message;
import com.cesfelipesegundo.itis.model.Query;
import com.cesfelipesegundo.itis.model.QueryGrade;
import com.cesfelipesegundo.itis.model.User;
import com.cesfelipesegundo.itis.web.Constants;
import com.lowagie.text.DocumentException;

import es.itest.engine.course.business.entity.Group;
import es.itest.engine.course.business.entity.Subject;
import es.itest.engine.course.business.entity.TestSessionTemplateSubject;
import es.itest.engine.test.business.control.ItemPdfWriter;
import es.itest.engine.test.business.control.TestSessionPdfWriter;
import es.itest.engine.test.business.entity.Action1;
import es.itest.engine.test.business.entity.DomainEventRegistrationRemover;
import es.itest.engine.test.business.entity.DomainEvents;
import es.itest.engine.test.business.entity.Item;
import es.itest.engine.test.business.entity.ItemResponse;
import es.itest.engine.test.business.entity.ItemSession;
import es.itest.engine.test.business.entity.ItemSessionResponse;
import es.itest.engine.test.business.entity.MediaElem;
import es.itest.engine.test.business.entity.Test;
import es.itest.engine.test.business.entity.TestDetails;
import es.itest.engine.test.business.entity.TestSession;
import es.itest.engine.test.business.entity.TestSessionForReview;
import es.itest.engine.test.business.entity.TestSessionGrade;
import es.itest.engine.test.business.entity.TestSessionTemplate;

public class TestManager {
  private static final Log log = LogFactory.getLog(TestManager.class);

  private static final Log examLog = LogFactory.getLog("com.cesfelipesegundo.itis.exam.journal");

  private Resource rootPath;

  /**
   * @uml.property name="userDAO"
   * @uml.associationEnd
   */
  private UserDAO userDAO;

  /**
   * @uml.property name="basicDataExamDAO"
   * @uml.associationEnd
   */
  private BasicDataExamDAO basicDataExamDAO;

  /**
   * @uml.property name="configExamDAO"
   * @uml.associationEnd
   */
  private ConfigExamDAO testSessionTemplateDAO;

  /**
   * @uml.property name="templateExamDAO"
   * @uml.associationEnd
   */
  private TemplateExamDAO testDAO;

  /**
   * @uml.property name="answerExamDAO"
   * @uml.associationEnd
   */
  private AnswerExamDAO answerExamDAO;

  /**
   * @uml.property name="examDAO"
   * @uml.associationEnd
   */
  private ExamDAO examDAO;

  /**
   * @uml.property name="templateExamQuestionDAO"
   * @uml.associationEnd
   */
  private TemplateExamQuestionDAO templateExamQuestionDAO;

  /**
   * @uml.property name="templateGradeDAO"
   * @uml.associationEnd
   */
  private TemplateGradeDAO templateGradeDAO;

  private TemplateExamAnswerDAO templateExamAnswerDAO;

  private ConfigExamDAO configExamDAO;

  private TemplateExamDAO templateExamDAO;

  TemplateExamAnswerDAO getTemplateExamAnswerDAO() {
    return templateExamAnswerDAO;
  }

  void setTemplateExamAnswerDAO(TemplateExamAnswerDAO templateExamAnswerDAO) {
    this.templateExamAnswerDAO = templateExamAnswerDAO;
  }

  /**
   * @return
   * @uml.property name="basicDataExamDAO"
   */
  BasicDataExamDAO getBasicDataExamDAO() {
    return basicDataExamDAO;
  }

  /**
   * @param basicDataExamDAO
   * @uml.property name="basicDataExamDAO"
   */
  void setBasicDataExamDAO(BasicDataExamDAO basicDataExamDAO) {
    this.basicDataExamDAO = basicDataExamDAO;
  }

  Resource getRootPath() {
    return this.rootPath;
  }

  void setRootPath(Resource rootPath) {
    this.rootPath = rootPath;
  }

  /**
   * @return
   * @uml.property name="configExamDAO"
   */
  ConfigExamDAO getConfigExamDAO() {
    return testSessionTemplateDAO;
  }

  /**
   * @param configExamDAO
   * @uml.property name="configExamDAO"
   */
  void setConfigExamDAO(ConfigExamDAO configExamDAO) {
    this.testSessionTemplateDAO = configExamDAO;
  }

  /**
   * @return
   * @uml.property name="templateExamDAO"
   */
  TemplateExamDAO getTemplateExamDAO() {
    return testDAO;
  }

  /**
   * @param templateExamDAO
   * @uml.property name="templateExamDAO"
   */
  void setTemplateExamDAO(TemplateExamDAO templateExamDAO) {
    this.testDAO = templateExamDAO;
  }

  /**
   * @param userDAO
   * @uml.property name="userDAO"
   */
  UserDAO getUserDAO() {
    return userDAO;
  }

  /**
   * @param userDAO
   * @uml.property name="userDAO"
   */
  void setUserDAO(UserDAO userDAO) {
    this.userDAO = userDAO;
  }

  /**
   * @return
   * @uml.property name="answerExamDAO"
   */
  AnswerExamDAO getAnswerExamDAO() {
    return answerExamDAO;
  }

  /**
   * @param answerExamDAO
   * @uml.property name="answerExamDAO"
   */
  void setAnswerExamDAO(AnswerExamDAO answerExamDAO) {
    this.answerExamDAO = answerExamDAO;
  }

  /**
   * @return
   * @uml.property name="examDAO"
   */
  ExamDAO getExamDAO() {
    return examDAO;
  }

  /**
   * @param examDAO
   * @uml.property name="examDAO"
   */
  void setExamDAO(ExamDAO examDAO) {
    this.examDAO = examDAO;
  }

  /**
   * @return
   * @uml.property name="templateExamQuestionDAO"
   */
  TemplateExamQuestionDAO getTemplateExamQuestionDAO() {
    return templateExamQuestionDAO;
  }

  /**
   * @param templateExamQuestionDAO
   * @uml.property name="templateExamQuestionDAO"
   */
  void setTemplateExamQuestionDAO(TemplateExamQuestionDAO templateExamQuestionDAO) {
    this.templateExamQuestionDAO = templateExamQuestionDAO;
  }

  /**
   * @return
   * @uml.property name="templateGradeDAO"
   */
  TemplateGradeDAO getTemplateGradeDAO() {
    return templateGradeDAO;
  }

  /**
   * @param templateGradeDAO
   * @uml.property name="templateGradeDAO"
   */
  void setTemplateGradeDAO(TemplateGradeDAO templateGradeDAO) {
    this.templateGradeDAO = templateGradeDAO;
  }

  //
  // Business Methods
  //

  public List<TestDetails> getTestSessionsForReview(Long exameneeId) {
    List<TestDetails> exams = basicDataExamDAO.getExamsForRevision(exameneeId);
    return exams;

  }

  public List<TestDetails> getPendingTests(Long exameneeId) {
    List<TestDetails> exams = basicDataExamDAO.getPendingExams(exameneeId);
    return exams;
  }

  public TestSession createTestSession(User examenee, long testId, String ipAddress) {
    // If demo user, then no updates on DB should be done
    boolean updateDB;

    if (isDemoUser(examenee))
      updateDB = false;
    else
      updateDB = true;

    try {
      return createTestSession(examenee.getId(), testId, ipAddress, updateDB);
    } catch (Exception e) {
      return null;
    }

  }

  /**
   * Private method that checks if a user is only for demo. It checks surname string from user
   * object
   * 
   * @param examenee
   * @return
   */
  private boolean isDemoUser(User examenee) {
    if (examenee.getSurname() != null) {
      if (examenee.getSurname().equals(Constants.USERDEMOSURNAME))
        return true;
      else
        return false;
    } else {
      // Surname is null
      return false;
    }
  }

  public TestSession createTestSessionPreview(long testId) {
    try {
      return createTestSession(-1l, testId, "0.0.0.0", false);
    } catch (Exception e) {
      return null;
    }
  }

  private class UpdateItemResponseOnUseAction implements Action1<ItemResponse> {

    private boolean updateDatabase;

    public UpdateItemResponseOnUseAction(boolean updateDatabase) {
      this.updateDatabase = updateDatabase;
    }

    @Override
    public void execute(ItemResponse obj) {
      if (this.updateDatabase) {
        answerExamDAO.updateUsedInExam(obj);
      }
    }
  };

  private class UpdateItemOnUseAtion implements Action1<Item> {

    private boolean updateDatabase;

    public UpdateItemOnUseAtion(boolean updateDatabase) {
      this.updateDatabase = updateDatabase;
    }

    @Override
    public void execute(Item obj) {
      if (this.updateDatabase) {
        templateExamQuestionDAO.updateUsedInExam(obj);
      }
    }
  };

  public TestSession createTestSession(long examineeId, long testId, String ipAddress,
      boolean updateDatabase) {

    TestSessionTemplate ce = testSessionTemplateDAO.getTestSessionTemplate(testId);
    Test test = ce.createTest();

    try (DomainEventRegistrationRemover r1 =
        DomainEvents.ITEM_RESPONSE_USED.register(new UpdateItemResponseOnUseAction(updateDatabase));
        DomainEventRegistrationRemover r2 =
            DomainEvents.ITEM_USED.register(new UpdateItemOnUseAtion(updateDatabase))) {

      TestSession exam = test.createTestSession();

      // A new calif is added with the starting date of the examn
      if (updateDatabase) {
        long startingDate = exam.getStartingDate();
        try {
          basicDataExamDAO.addNewCalif(examineeId, testId, ipAddress, startingDate);
        } catch (DataIntegrityViolationException dive) {
          examLog.warn("Alumno: " + examineeId + " ha intentado ejecutar nuevamente el examen: "
              + testId);
          return null;
        }

        Date fecha = new Date(startingDate);
        for (ItemSession question : exam.getQuestions()) {
          List<ItemSessionResponse> answers = question.getAnswers();

          for (ItemSessionResponse answer : answers) {
            StringBuilder buffer = new StringBuilder();
            if (question.getType() == 0) {
              answerExamDAO.addNewExamAnswer(testId, examineeId, question.getId(), answer.getId(),
                  startingDate);
              buffer
                  .append("INSERT INTO log_exams (exam, alu, preg, resp, marcada, puntos, hora_resp) VALUES");
              buffer.append("(" + testId + " , " + examineeId + " , " + question.getId() + " , "
                  + answer.getId() + " , 0 , 0 , '" + fecha.getYear() + "-" + fecha.getMonth()
                  + "-" + fecha.getDate() + " " + fecha.getHours() + ":" + fecha.getMinutes() + ":"
                  + fecha.getSeconds() + "' )");
            } else {
              answerExamDAO.addNewExamFillAnswer(testId, examineeId, question.getId(),
                  answer.getId(), null, startingDate);
              buffer
                  .append("INSERT INTO log_exams_fill (exam, alu, preg, puntos, hora_resp) VALUES");
              buffer.append("(" + testId + " , " + examineeId + " , " + question.getId()
                  + " , 0.0 , '" + fecha.getYear() + "-" + fecha.getMonth() + "-" + fecha.getDate()
                  + " " + fecha.getHours() + ":" + fecha.getMinutes() + ":" + fecha.getSeconds()
                  + "' )");
            }

            examLog.debug(buffer);
          }

        }
        examLog.debug("Examen: " + testId + " generado para Alumno :" + examineeId + " IP: "
            + ipAddress);
      }

      return exam;
    }
  }

  public int updateItemSessionResponse(long testId, long examineeId, long itemSessionId,
      long itemSessionReponseId, boolean marked) {

    int rows =
        answerExamDAO.updateExamAnswer(testId, examineeId, itemSessionId, itemSessionReponseId,
            System.currentTimeMillis(), marked);

    if (examLog.isDebugEnabled()) {
      Date fecha = new Date(System.currentTimeMillis());
      StringBuilder buffer = new StringBuilder();
      buffer.append("UPDATE log_exams SET ");
      buffer.append("marcada = " + marked + ", ");
      buffer.append("puntos = 0.0, ");
      buffer.append("hora_resp = '" + fecha.getYear() + "-" + fecha.getMonth() + "-"
          + fecha.getDate() + " " + fecha.getHours() + ":" + fecha.getMinutes() + ":"
          + fecha.getSeconds() + "' ");
      buffer.append("WHERE exam =" + testId + " AND alu =" + examineeId + " AND preg ="
          + itemSessionId + " AND resp =" + itemSessionReponseId);
      examLog.debug(buffer.toString());
    }
    return rows;
  }

  /**
   * This method should: - Obtain the grade of each individual answer to each question and update
   * the corresponding rows of log_exams table - Update the information of table califs: fecha_fin
   * and nota
   * 
   * @param id , of the user (learner)
   * @param currentExam , that is, the object consisting in the exam performed by the user
   * @return Final grade of the exam
   */
  public Double gradeTestSession(long examineeId, TestSession testSession) {
    return calculateExamGrade(examineeId, testSession, true, false, false);
  }

  public Double gradeTestSessionPreview(TestSession testSessionPreview) {
    return calculateExamGrade(-1l, testSessionPreview, false, false, false);
  }

  private Double gradeQuestion(ItemSession question, Long id, TestSession currentExam,
      double maxGradePerQuestion, boolean updateDatabase) {
    if (question.getType() == 0)
      return gradeTestQuestion(question, id, currentExam, maxGradePerQuestion, updateDatabase);
    else
      return gradeFillQuestion(question, id, currentExam, maxGradePerQuestion, updateDatabase);
  }

  /**
   * Calculates a question's grade. Grade depends on evaluation method choosen at exam configuration
   * time (partial correction or no partial correction) Partial correction means that a question
   * will be graded according to its correct and incorrect answers marked, and its corresponding
   * bonus and penalty No partial correction means that a question will be graded to failed if there
   * were one or more incorrect answers marked, or there were not marked all of its correct answers
   * Both cases have penalty for question not answered
   * 
   * @param question Question to be graded
   * @param id User id
   * @param currentExam Exam containing the question to be graded
   * @param maxGradePerQuestion
   * @param updateDatabase If true, grade by answer marked will be save into DB Log
   * @return Grade obtained
   */
  private Double gradeTestQuestion(ItemSession question, Long id, TestSession currentExam,
      double maxGradePerQuestion, boolean updateDatabase) {
    return question.grade().doubleValue();
  }

  private Double gradeFillQuestion(ItemSession question, Long userId, TestSession currentExam,
      double maxGradePerQuestion, boolean updateDatabase) {
    return question.grade().doubleValue();
  }

  private Double calculateExamGrade(Long id, TestSession currentExam, boolean updateDatabase,
      boolean isReview, boolean checkConfigExam) {
    return currentExam.grade().doubleValue();
  }

  public ItemSession getNextQuestion(User examinee, long testSessionId, long lastItemSessionId) {
    return examDAO.getNextQuestion(examinee, testSessionId, lastItemSessionId);
  }

  /**
   * Method to obtain the data of an already done exam: questions, answers, comments, grade, marked
   * answers, correct answers, time employed...
   * 
   * @param examinee that performed the exam
   * @param idexam , id of the configuration of the exam previously performed
   * @return
   */
  public TestSession getAlreadyDoneExam(User examinee, long testSessionId) {

    TestSession exam = examDAO.getAlreadyDoneExam(examinee, testSessionId);

    if (exam != null) {
      /*
       * The punctuation for each question is not stored in the database, just the punctuation for
       * each answer. That's why it is calculated...
       */
      int numberOfQuestions = exam.getQuestions().size();
      double maxGrade = exam.getMaxGrade();
      double maxGradePerQuestion = ((double) maxGrade) / ((double) numberOfQuestions);

      for (ItemSession question : exam.getQuestions()) {
        double questionGrade =
            gradeQuestion(question, examinee.getId(), exam, maxGradePerQuestion, false);
        question.setQuestionGrade(questionGrade);
      }
      // The total grade of the exam is retrieved from the database
    }

    return exam;

  }

  public List<TestSessionForReview> examReviewByQuestion(Item question) {
    // 1.- Obtain a list of exams in which the question appeared, and users
    // who did it
    List<TestSessionForReview> examsForReview = examDAO.getExamsByQuestion(question.getId());
    // 2.- Recalculate grade for each exam and user
    for (TestSessionForReview examForReview : examsForReview) {
      // User object just for getAlreadyDoneExam method calling
      User fakeUser = new User();
      fakeUser.setId(examForReview.getIdLearner());
      // Retrieves the exam done by user and re-calculates ("review") its
      // grade
      TestSession examDone = getAlreadyDoneExam(fakeUser, examForReview.getIdExam());
      examForReview.setPostGrade(calculateExamGrade(examForReview.getIdLearner(), examDone, true,
          true, false));
    }
    // 3.- Return list containing exams reviewed
    return examsForReview;
  }

  public List<TestSessionForReview> examReviewByIdExam(long idExam) {
    // 1.- Obtain a list of exams in which the question appeared, and users
    // who did it
    List<TestSessionForReview> examsForReview = examDAO.getExamsByIdExam(idExam);
    // 2.- Recalculate grade for each exam and user
    for (TestSessionForReview examForReview : examsForReview) {
      // User object just for getAlreadyDoneExam method calling
      User fakeUser = new User();
      fakeUser.setId(examForReview.getIdLearner());
      // Retrieves the exam done by user and re-calculates ("review") its
      // grade
      TestSession examDone = getAlreadyDoneExam(fakeUser, examForReview.getIdExam());
      examForReview.setPostGrade(calculateExamGrade(examForReview.getIdLearner(), examDone, true,
          true, true));
    }
    // 3.- Return list containing exams reviewed
    return examsForReview;
  }

  public ByteArrayOutputStream parse2PDF(Item question) throws NullPointerException,
      DocumentException, IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ItemPdfWriter writer = new ItemPdfWriter(rootPath.getFile(), question);
    writer.write(baos);
    return baos;
  }

  public ByteArrayOutputStream parse2PDF(TestSession exam) throws NullPointerException,
      DocumentException, IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    TestSessionPdfWriter writer = new TestSessionPdfWriter(rootPath.getFile(), exam);
    writer.write(baos);
    return baos;
  }

  public Grade getTestSessionGrade(long testSessionId, long examineeId) {
    return examDAO.getExamGrade(testSessionId, examineeId);
  }

  public TestSession getTestSession(long testSessionId, long idalumn) {
    return examDAO.getExamById(testSessionId, idalumn);
  }

  public Grade getGradeByIdExam(Long idexam, Long iduser) {
    return templateGradeDAO.getGradeByIdExam(idexam, iduser);
  }

  public List<TestSession> getAllExams(Long idexam) {
    return examDAO.getAllExams(idexam);
  }

  public int updateConfidenceLevel(long examId, long userId, long questionId, boolean checked) {
    Item question = new Item();
    question.setId(questionId);
    question = templateExamQuestionDAO.getQuestionFromId(question);
    return answerExamDAO.updateConfidenceLevel(examId, userId, questionId, checked,
        question.getType());
  }

  public void addNewExamAnswer2LogExams(TestSession currentExam, long idlearner, long idquestion,
      long startingDate) {
    List<ItemSession> questions = currentExam.getQuestions();
    for (ItemSession question : questions) {
      if (question.getId().equals(idquestion)) {
        List<ItemSessionResponse> answers = question.getAnswers();
        for (ItemSessionResponse answer : answers) {
          if (question.getType() == 0)
            answerExamDAO.addNewExamAnswer(currentExam.getId(), idlearner, question.getId(),
                answer.getId(), startingDate);
          else
            answerExamDAO.addNewExamFillAnswer(currentExam.getId(), idlearner, question.getId(),
                answer.getId(), question.getLearnerFillAnswer(), startingDate);
        }
      }
    }
  }

  public void checkExam(TestSession currentExam, long iduser) {
    for (ItemSession question : currentExam.getQuestions()) {
      for (ItemSessionResponse answer : question.getAnswers()) {
        if (question.getType() == 0) {
          int rows =
              answerExamDAO.updateExamAnswer(currentExam.getId(), iduser, question.getId(), answer);
          if (rows == 0) {
            answerExamDAO.addNewExamAnswer(currentExam.getId(), iduser, question.getId(),
                answer.getId(), System.currentTimeMillis());
          }
        } else if (question.getType() == 1) {
          int rows =
              answerExamDAO.updateExamFillAnswer(currentExam.getId(), iduser, question.getId(),
                  answer.getId(), question.getLearnerFillAnswer(), null);
          if (rows == 0) {
            answerExamDAO.addNewExamFillAnswer(currentExam.getId(), iduser, question.getId(),
                answer.getId(), question.getLearnerFillAnswer(), System.currentTimeMillis());
          }
        }
      }
    }
  }

  public List<Grade> getGradesByUser(String userName) {
    return templateGradeDAO.getGradesByUser(userName);
  }

  public List<CustomExamUser> getUsersInCustomExam(Long examId) {
    return userDAO.getUsersInCustomExam(examId);
  }

  public void updateExamFillAnswer(Long idExam, Long idUser, Long idQuestion, Long idAnswer,
      String textAnswer) {
    int rows =
        answerExamDAO.updateExamFillAnswer(idExam, idUser, idQuestion, idAnswer, textAnswer,
            new Long(0));
    StringBuilder buffer = new StringBuilder();
    Date fecha = new Date(System.currentTimeMillis());
    buffer.append("UPDATE log_exams_fill ");
    buffer.append("resp = '" + textAnswer + "'");
    buffer.append("puntos = 0.0, ");
    buffer.append("hora_resp = '" + fecha.getYear() + "-" + fecha.getMonth() + "-"
        + fecha.getDate() + " " + fecha.getHours() + ":" + fecha.getMinutes() + ":"
        + fecha.getSeconds() + "' ");
    buffer.append("WHERE exam =" + idExam + " AND alu =" + idUser + " AND preg =" + idQuestion
        + " AND resp =" + idAnswer);
    if (rows == 0) {
      answerExamDAO.addNewExamFillAnswer(idExam, idUser, idQuestion, idAnswer, textAnswer,
          System.currentTimeMillis());
    }
  }

  public Double reGradeExam(Long idStd, TestSession ex) {
    // Aqui se eliminarian las preguntas que no est�n en la config. de
    // ex�men
    return calculateExamGrade(idStd, ex, true, true, true);
  }

  /**
   * Saves the question information into the database
   * 
   * @param question
   * @return id of the question saved
   */
  public void saveQuestion(Item question) {
    /*
     * Se debe actualizar la tabla de preguntas con los datos que se pasan en currentQuesion. Si la
     * pregunta es nueva hay que realizar una inserción Si la pregunta existía, hay que hacer una
     * actualización. El método devuelve el id de la pregunta en cualquier caso.
     */
    /*
     * NOTA: si la pregunta es nueva, currentQuestion.id tendrá "null"
     */
    if (question.getId() != null) {
      templateExamQuestionDAO.update(question);
    } else {
      templateExamQuestionDAO.save(question);
    }

  }

  /**
   * Saves the answer information into the database
   * 
   * @param answer
   */
  public void saveAnswer(ItemResponse answer) {
    /*
     * Se debe actualizar la tabla de respuestas con los datos que se pasan en answer. La respuesta
     * SIEMPRE es nueva -> Inserción SIEMPRE EN LA BD Se pasa currentQuestion para tomar el id (hará
     * falta en la BD)
     */
    if (answer.getId() != null) {
      templateExamAnswerDAO.update(answer);
    } else {
      templateExamAnswerDAO.save(answer);
    }
  }

  public void deleteAnswer(ItemResponse answer) {

    // Delete the mmedia files associated with the answer

    List<MediaElem> mmlist = answer.getMmedia();
    if (mmlist != null) {
      Iterator<MediaElem> iterMM = mmlist.iterator();
      while (iterMM.hasNext()) {
        MediaElem mm = iterMM.next();
        deleteMmediaFile(mm.getPath());
      }
    }

    // delete the answer in the database

    templateExamAnswerDAO.delete(answer);
  }

  public void saveMediaElemToQuestion(Item question, MediaElem mediaElem) {

    // Comportamiento similar a saveQuestion y saveAnswer con respecto al id

    if (mediaElem.getId() != null) {
      templateExamQuestionDAO.update(question, mediaElem, true);
    } else {
      templateExamQuestionDAO.save(question, mediaElem, true);
    }
  }

  public void saveMediaElemToComment(Item question, MediaElem mediaElem) {

    // Comportamiento similar a saveQuestion y saveAnswer con respecto al id

    if (mediaElem.getId() != null) {
      templateExamQuestionDAO.update(question, mediaElem, false);
    } else {
      templateExamQuestionDAO.save(question, mediaElem, false);
    }
  }

  public void saveMediaElemToAnswer(ItemResponse answer, MediaElem mediaElem) {

    // Comportamiento similar a saveQuestion y saveAnswer con respecto al id

    if (mediaElem.getId() != null) {
      templateExamAnswerDAO.update(answer, mediaElem);
    } else {
      templateExamAnswerDAO.save(answer, mediaElem);
    }
  }

  public void deleteMediaElemFromComment(Item question, MediaElem mediaElem) {

    deleteMmediaFile(mediaElem.getPath());
    templateExamQuestionDAO.delete(question, mediaElem, false);
  }

  public void deleteMediaElemFromQuestion(Item question, MediaElem mediaElem) {

    deleteMmediaFile(mediaElem.getPath());
    templateExamQuestionDAO.delete(question, mediaElem, true);
  }

  public void deleteMediaElemFromAnswer(ItemResponse answer, MediaElem mediaElem) {

    deleteMmediaFile(mediaElem.getPath());
    templateExamAnswerDAO.delete(answer, mediaElem);
  }

  /**
   * Deletes a file from the multimedia path
   * 
   * @param filename file name
   */
  public boolean deleteMmediaFile(String filename) {
    try {
      File uploadedFile = new File(rootPath.getFile(), filename);
      if (!uploadedFile.delete()) {
        log.error("No se pudo borrar " + filename);
        return false;
      }

      /*
       * If the media element is an applet we can't change its filename, so we store it into a
       * folder with a random name, then this folder has to be deleted
       */

      String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

      if (extension.equals("class")) {
        File parentDirectory = uploadedFile.getParentFile();
        parentDirectory.delete();
      }

      log.info("Borrado fichero " + filename);
      return true;

    } catch (Exception e) {
      log.error("No se puede borrar: " + e.getMessage());
      return false;
    }

  }

  public List<Item> find(Query query) {
    List<Item> result = templateExamQuestionDAO.find(query);

    for (Item question : result) {
      templateExamQuestionDAO.fillMediaElem(question);
      templateExamQuestionDAO.fillAnswers(question);

    }

    return result;
  }

  public void deleteQuestion(Item question) {

    // Delete the mmedia files of the question:
    List<MediaElem> mmlist = question.getMmedia();
    if (mmlist != null) {
      Iterator<MediaElem> iterMM = mmlist.iterator();
      while (iterMM.hasNext()) {
        MediaElem mm = iterMM.next();
        deleteMmediaFile(mm.getPath());
      }
    }

    // It also has to delete all the mmedia files of the answers.
    List<ItemResponse> answList = question.getAnswers();
    if (answList != null) {
      Iterator<ItemResponse> iterAL = answList.iterator();
      while (iterAL.hasNext()) {
        ItemResponse ans = iterAL.next();
        // For each answer, delete the files
        List<MediaElem> ansMM = ans.getMmedia();
        if (ansMM != null) {
          Iterator<MediaElem> iterMMA = ansMM.iterator();
          while (iterMMA.hasNext()) {
            MediaElem mm = iterMMA.next();
            deleteMmediaFile(mm.getPath());
          }
        }
      }
    }

    templateExamQuestionDAO.delete(question);
  }

  public TestSessionTemplate configExamCopy(TestSessionTemplate examSource) {
    // 1.- Create new ConfigExam instance and copy source attributes
    TestSessionTemplate examCopy = new TestSessionTemplate(examSource);
    // End 1

    // 2.- Set new ConfigExam copy id to null. configExamDAO will insert a
    // new entry on DB for the copy
    examCopy.setId(null);
    configExamDAO.save(examCopy);
    // End 2

    // 3.- Copy subjects (no subject deep copy needed!!!)
    if (examSource.getSubjects() != null) {
      List<TestSessionTemplateSubject> examCopySubjects =
          new ArrayList<TestSessionTemplateSubject>();
      for (TestSessionTemplateSubject subject : examSource.getSubjects()) {
        /*
         * La id de "subject" hay que ponerla a null porque a la hora de guardar en la BD no se crea
         * una nueva fila si "subject" ya tiene id, lo que hace es un UPDATE de la fila cuya id es
         * igual a la de "subject"
         */
        subject.setId(null);
        examCopySubjects.add(subject);
        /*
         * Si la id de "subject" hubiese sido distindo de null lo que hubiese hecho en la BD es
         * modificar el campo id de manera que la id la cambiaría por la de examCopy haciendo que
         * examSource no tuviera ningún "subject"
         */
        saveSubjectToExam(examCopy, subject);
      }
      examCopy.setSubjects(examCopySubjects);
    } else
      examCopy.setSubjects(null);
    // End 3

    List<CustomExamUser> users = getUsersInCustomExam(examSource.getId());
    // 4
    for (CustomExamUser user : users) {
      addUser2CustomExam(examCopy.getId(), user.getId());
    }
    // End 4
    return examCopy;

  }

  public Item copyQuestion(Item question, boolean copy) {
    // 1.- Crear un nuevo TemplateExamQuestion que copie los valores del
    // pasado por parametro
    Item questionCopy = new Item();
    questionCopy.setActive(question.getActive());
    questionCopy.setComment(question.getComment());
    questionCopy.setDifficulty(question.getDifficulty());
    questionCopy.setGroup(question.getGroup());
    questionCopy.setNumCorrectAnswers(question.getNumCorrectAnswers());
    questionCopy.setSubject(question.getSubject());
    questionCopy.setText(question.getText());
    if (copy && questionCopy.getTitle() != null && questionCopy.getTitle().length() < 50) {
      questionCopy.setTitle(question.getTitle() + " (copia)");
    } else {
      questionCopy.setTitle(question.getTitle());
    }
    questionCopy.setUsedInExam(false); // La nueva pregunta aun no ha sido
    // usada en ningun examen,
    // independientemente de si la
    // original lo fue
    questionCopy.setVisibility(question.getVisibility());
    // Fin 1

    // 2.- Establecer id del nuevo TemplateExamQuestion a null, para que el
    // DAO lo guarde como una nueva pregunta
    questionCopy.setId(null);
    questionCopy.setType(question.getType());
    saveQuestion(questionCopy);
    // Fin 2

    // Obtenemos la ruta a los elementos multimedia
    String rutaMmedia;
    try {
      rutaMmedia = rootPath.getFile().getAbsolutePath() + File.separatorChar;
    } catch (IOException e) {
      rutaMmedia = Constants.MMEDIAPATH;
      log.error("No se puede acceder al directorio de ficheros multimedia. Es posible que no puedan duplicarse estos elementos...");
    }

    // 3.- Copia en profundidad de los elementos multimedia (si los hay),
    // utilizando duplicateMMediaFile y guardando la ruta en la nueva
    // pregunta
    if (question.getMmedia() != null) {
      // Obtenemos la lista de elementos multimedia de la pregunta
      // original
      List<MediaElem> listMMediaOriginal = question.getMmedia();
      // Creamos una lista de elementos para la pregunta duplicada
      List<MediaElem> listMMediaDuplicada = new ArrayList<MediaElem>();

      // Para cada elemento de la lista original
      for (MediaElem elemento : listMMediaOriginal) {

        // Duplicate files and modify the path
        String lastPath = elemento.getPath();
        // Copiamos su informacion
        MediaElem copiaMmedia = new MediaElem(elemento);
        copiaMmedia.setId(null);
        // Duplicamos su archivo multimedia
        copiaMmedia.setPath(duplicateMmediaFile(rutaMmedia, elemento.getPath()));
        if (copiaMmedia.getPath() == null) {
          copiaMmedia.setPath(lastPath);
        }
        // Si el archivo multimedia pudo duplicarse
        if (copiaMmedia.getPath() != null) {
          // Aniadimos el archivo multimedia a la pregunta
          saveMediaElemToQuestion(questionCopy, copiaMmedia);
          listMMediaDuplicada.add(copiaMmedia);
        } else
          // Sino, deja constancia en el log y a otra cosa...
          log.error("No pudo duplicarse el elemento multimedia \"" + rutaMmedia
              + elemento.getPath() + "\" de la pregunta con ID: " + question.getId());
      }
      questionCopy.setMmedia(listMMediaDuplicada);
    }

    if (question.getMmediaComment() != null) {
      // Obtenemos la lista de elementos multimedia de la pregunta
      // original
      List<MediaElem> listMMediaOriginal = question.getMmediaComment();
      // Creamos una lista de elementos para la pregunta duplicada
      List<MediaElem> listMMediaDuplicada = new ArrayList<MediaElem>();

      // Para cada elemento de la lista original
      for (MediaElem elemento : listMMediaOriginal) {

        // Duplicate files and modify the path
        String lastPath = elemento.getPath();
        // Copiamos su informacion
        MediaElem copiaMmedia = new MediaElem(elemento);
        copiaMmedia.setId(null);
        // Duplicamos su archivo multimedia
        copiaMmedia.setPath(duplicateMmediaFile(rutaMmedia, elemento.getPath()));
        if (copiaMmedia.getPath() == null) {
          copiaMmedia.setPath(lastPath);
        }
        // Si el archivo multimedia pudo duplicarse
        if (copiaMmedia.getPath() != null) {
          // Aniadimos el archivo multimedia a la pregunta
          saveMediaElemToComment(questionCopy, copiaMmedia);
          listMMediaDuplicada.add(copiaMmedia);
        } else
          // Sino, deja constancia en el log y a otra cosa...
          log.error("No pudo duplicarse el elemento multimedia \"" + rutaMmedia
              + elemento.getPath() + "\" de la pregunta con ID: " + question.getId());
      }
      questionCopy.setMmediaComment(listMMediaDuplicada);
    }

    // Fin 3

    // 4.- Copia en profundidad de la lista de respuestas, con sus
    // correspondientes elementos multimedia. Consultar conveniencia de
    // implementar public TemplateExamAnswer copyAnswer(TemplateExamAnswer
    // answer)
    // Si la pregunta original tiene una lista de respuestas
    if (question.getAnswers() != null) {
      // Obtenemos la lista de respuestas de la pregunta original
      List<ItemResponse> listAnswersOriginal = question.getAnswers();
      // Creamos una lista de respuestas de la pregunta original
      List<ItemResponse> listAnswersDuplicada = new ArrayList<ItemResponse>();

      // Para cada una de las respuestas
      for (ItemResponse respuestaOriginal : listAnswersOriginal) {
        ItemResponse respuestaCopia = new ItemResponse();

        // Copiamos los atributos de la respuesta original
        respuestaCopia.setActive(respuestaOriginal.getActive());
        respuestaCopia.setExamineeWasConfident(respuestaOriginal.getMarked());
        respuestaCopia.setQuestion(questionCopy); // La respuesta copia
        // estara
        // relacionada,
        // obviamente, con
        // la nueva pregunta
        // copia
        respuestaCopia.setSolution(respuestaOriginal.getSolution());
        respuestaCopia.setText(respuestaOriginal.getText());
        respuestaCopia.setUsedInExam(false); // La nueva respuesta copia
        // no ha sido utilizada
        // en ningun examen,
        // independientemente de
        // si lo fue la original
        respuestaCopia.setValue(respuestaOriginal.getValue());

        // Persistencia
        respuestaCopia.setId(null);
        saveAnswer(respuestaCopia);

        // Si la respuesta tiene elementos multimedia
        if (respuestaOriginal.getMmedia() != null) {
          // TESTING!!!: saveAnswer(respuestaCopia);
          // Obtenemos la lista de elementos multimedia de la
          // respuesta original
          List<MediaElem> listMMediaRespuestaOriginal = respuestaOriginal.getMmedia();
          // Creamos una lista de elementos multimedia para la
          // respuesta duplicada
          List<MediaElem> listMMediaRespuestaDuplicada = new ArrayList<MediaElem>();

          // Para cada elemento de la lista original
          for (MediaElem elemento : listMMediaRespuestaOriginal) {
            // Copiamos su informacion
            MediaElem copiaMmedia = new MediaElem(elemento);
            copiaMmedia.setId(null);
            // Duplicamos su archivo multimedia
            copiaMmedia.setPath(duplicateMmediaFile(rutaMmedia, elemento.getPath()));
            // Si pudo duplicarse el elemento multimedia
            if (copiaMmedia.getPath() != null) {
              // Aniadimos el nuevo a la pregunta copiada
              saveMediaElemToAnswer(respuestaCopia, copiaMmedia);
              listMMediaRespuestaDuplicada.add(copiaMmedia);
            } else
              // Sino, se deja constancia en el log y a otra
              // cosa...
              log.error("No pudo duplicarse el elemento multimedia \"" + rutaMmedia
                  + elemento.getPath() + "\" de la respuesta con ID: " + respuestaOriginal.getId());
          }
          respuestaCopia.setMmedia(listMMediaRespuestaDuplicada);
        }
        // Persistencia
        // TESTING: saveAnswer(respuestaCopia);
        // A la lista de respuestas de la pregunta duplicada
        listAnswersDuplicada.add(respuestaCopia);
      }
      questionCopy.setAnswers(listAnswersDuplicada);
    }

    // Salvamos finalmente la pregunta copiada
    // TESTING: saveQuestion(questionCopy);

    // Devolvemos la instancia de la pregunta copiada
    return questionCopy;

  }

  public Item getQuestionFromId(Item question) {
    return templateExamQuestionDAO.getQuestionFromId(question);

  }

  public void deleteConfigExam(TestSessionTemplate exam) {
    configExamDAO.delete(exam);
  }

  public List<TestSessionTemplate> getGroupConfigExams(Group group, String orderby) {
    return configExamDAO.getGroupConfigExams(group, orderby);
  }

  public void updateExamReview(TestSessionTemplate exam) {
    configExamDAO.updateReview(exam);
  }

  /**
   * Saves the exam configuration into the database
   * 
   * @param exam
   * @return id of the exam saved if update
   */
  public void saveExam(TestSessionTemplate exam) {
    /*
     * Se debe actualizar la tabla de examenes con los datos que se pasan en exam. Si el examen es
     * nuevo hay que realizar una inserción. Si el examen existía, hay que hacer una actualización.
     * El método devuelve el id del examen en cualquier caso.
     */
    /*
     * NOTA: si el examen es nuevo, su id tendrá "null"
     */
    if (exam.getId() != null) {
      configExamDAO.update(exam);
    } else {
      configExamDAO.save(exam);
    }

  }

  public TestSessionTemplate getConfigExamFromId(TestSessionTemplate exfromdb) {
    return configExamDAO.getTestSessionTemplate(exfromdb.getId());
  }

  public List<TestSessionGrade> find(QueryGrade query) {
    List<TestSessionGrade> result = null;
    result = templateGradeDAO.find(query);

    return result;

  }

  public List<Message> validate(TestSessionTemplate config) {
    Test exam = templateExamDAO.getTest(config.getId());
    List<Message> result = new ArrayList<Message>();
    if (exam != null) {
      List<Message> messages = null;
      // 1. Validate Dates and duration
      messages = validateConfigExamDates(exam);
      if (!messages.isEmpty()) {
        result.addAll(messages);
      }

      // 2. Validate Subjects
      messages = validateSubjects(exam);
      if (!messages.isEmpty()) {
        result.addAll(messages);
      }
    } else {
      result.add(new Message("validation.noConfig",
          new Object[] {config.getTitle(), config.getId()}, Message.MessageType.ERROR));
      log.error("Intentando validar configuración de examen no existente titulo="
          + config.getTitle() + " id=" + config.getId());
    }

    return result;
  }

  public List<Message> validateQuestion(Item templateQuestion,
      TestSessionTemplateSubject configSubject) {

    List<Message> result = Collections.EMPTY_LIST;

    int answersxQuestionNumber = configSubject.getAnswersxQuestionNumber();

    List<ItemResponse> templateQuestionAnswers = templateQuestion.getAnswers();

    /*
     * 1. Check answerSize >= answersxQuestionNumber
     */
    if (templateQuestionAnswers.size() < answersxQuestionNumber) {
      if (result == Collections.EMPTY_LIST) {
        result = new ArrayList<Message>();
      }
      result.add(new Message("validation.question.notEnoughAnswers", new Object[] {
          templateQuestion.getId(), templateQuestionAnswers.size(), answersxQuestionNumber},
          Message.MessageType.ERROR));
      return result;
    }

    List<ItemResponse> previousStep = new ArrayList<ItemResponse>();
    previousStep.addAll(templateQuestionAnswers);
    List<ItemResponse> currentStep = new ArrayList<ItemResponse>();

    /*
     * 2. Check if there are enough active answers
     */
    int answersCount = 0;
    for (ItemResponse templateAnswer : previousStep) {
      if (templateAnswer.getActive() == 1) {
        answersCount++;
        currentStep.add(templateAnswer);
      }
    }
    if (answersCount < answersxQuestionNumber) {
      if (result == Collections.EMPTY_LIST) {
        result = new ArrayList<Message>();
      }
      result.add(new Message("validation.question.notEnoughActiveAnswers", new Object[] {
          templateQuestion.getId(), answersCount, answersxQuestionNumber},
          Message.MessageType.ERROR));
      return result;
    }

    previousStep.clear();
    previousStep.addAll(currentStep);
    currentStep.clear();

    /*
     * 3. Check if there are at least one solution
     */
    int activeAnswersCount = answersCount;
    answersCount = 0;
    for (ItemResponse templateAnswer : previousStep) {
      if (templateAnswer.getSolution() == 1) {
        answersCount++;
        currentStep.add(templateAnswer);
      }
    }
    if (answersCount == 0) {
      if (result == Collections.EMPTY_LIST) {
        result = new ArrayList<Message>();
      }
      result.add(new Message("validation.question.noCorrectResponse",
          new Object[] {templateQuestion.getId()}, Message.MessageType.ERROR));
      return result;

    }

    /*
     * 4. Check if all active response are solutions
     */
    if (activeAnswersCount == answersCount && templateQuestion.getType() == 0) {
      if (result == Collections.EMPTY_LIST) {
        result = new ArrayList<Message>();
      }
      result.add(new Message("validation.question.allActiveAnswerAreCorrect",
          new Object[] {templateQuestion.getId()}, Message.MessageType.WARN));
      return result;
    }

    /*
     * 5. Check if the configured answer number is greater than all active solutions
     */
    if (answersxQuestionNumber < answersCount) {
      if (result == Collections.EMPTY_LIST) {
        result = new ArrayList<Message>();
      }
      result
          .add(new Message("validation.question.ConfiguredAnswersLessThanSolution", new Object[] {
              templateQuestion.getId(), answersxQuestionNumber, answersCount},
              Message.MessageType.WARN));
      return result;
    }

    return result;
  }

  private List<Message> validateConfigExamDates(Test exam) {
    List<Message> result = Collections.EMPTY_LIST;
    int duration = exam.getDuration();
    long startDate = exam.getStartDate().getTime();
    long endDate = exam.getEndDate().getTime();
    if (startDate > endDate) {
      if (result == Collections.EMPTY_LIST) {
        result = new ArrayList<Message>();
      }
      result.add(new Message("validation.startDateAfterEndDate", new Object[] {exam.getTitle(),
          exam.getStartDate(), exam.getEndDate()}, Message.MessageType.WARN));
    }

    /*
     * long datesDuration = (endDate - startDate) / (1000 * 60); if (datesDuration < duration) { if
     * (result == Collections.EMPTY_LIST) { result = new ArrayList<Message>(); } result .add(new
     * Message( "validation.durationImpossibleInDates", new Object[] {exam.getTitle(),
     * exam.getStartDate(), exam.getEndDate(), duration }, Message.MessageType.WARN)); }
     */

    return result;
  }

  public double getGradeScale() {
    // Fixed ad-hoc by the moment
    return 10.0;
  }

  private Double gradeFillQuestion(ItemSession question, TestSession currentExam,
      double maxGradePerQuestion) {
    double questionGrade = 0.0;
    boolean correct = true;
    List<ItemSessionResponse> answers = question.getAnswers();
    if (answers.size() == 1) {
      ItemSessionResponse answer = answers.get(0);
      String textLearnerAnswer = null;
      if (question != null && question.getLearnerFillAnswer() != null) {
        textLearnerAnswer = question.getLearnerFillAnswer().toLowerCase();
        questionGrade = calculateEntropy(answer, textLearnerAnswer, maxGradePerQuestion);
      }
      if (questionGrade != maxGradePerQuestion) {
        correct = false;
      }
      if (currentExam.isConfidenceLevel() && question.getExamineeWasConfident()) {
        if (questionGrade == 0.0) {
          questionGrade -= (maxGradePerQuestion * currentExam.getPenConfidenceLevel());
        } else {
          questionGrade += (maxGradePerQuestion * currentExam.getRewardConfidenceLevel());
        }
      }
      if (!currentExam.isPartialCorrection() && !correct) {
        if (!textLearnerAnswer.trim().equalsIgnoreCase("")) {
          questionGrade -= (maxGradePerQuestion * currentExam.getPenQuestionFailed());
        } else {
          questionGrade -= (maxGradePerQuestion * currentExam.getPenQuestionNotAnswered());
        }
      }
    } else
      return 0.0;

    return questionGrade;
  }

  private Double calculateEntropy(ItemSessionResponse answer, String textLearnerAnswer,
      double maxGradePerQuestion) {
    double result = maxGradePerQuestion;
    if (answer.getText().toLowerCase().equalsIgnoreCase(textLearnerAnswer)) {
      return result * 1.0;
    } else {
      return result * 0.0;
    }
  }

  private Double gradeQuestion(ItemSession question, TestSession currentExam,
      double maxGradePerQuestion) {
    if (question.getType() == 0)
      return gradeTestQuestion(question, currentExam, maxGradePerQuestion);
    else
      return gradeFillQuestion(question, currentExam, maxGradePerQuestion);
  }

  /**
   * Calculates a question's grade. Grade depends on evaluation method choosen at exam configuration
   * time (partial correction or no partial correction) Partial correction means that a question
   * will be graded according to its correct and incorrect answers marked, and its corresponding
   * bonus and penalty No partial correction means that a question will be graded to failed if there
   * were one or more incorrect answers marked, or there were not marked all of its correct answers
   * Both cases have penalty for question not answered
   * 
   * @param question Question to be graded
   * @param currentExam Exam containing the question to be graded
   * @param maxGradePerQuestion
   * @return Grade obtained
   */
  private Double gradeTestQuestion(ItemSession question, TestSession currentExam,
      double maxGradePerQuestion) {

    double questionGrade = 0.0;
    double numCorrectAnswers = question.getNumCorrectAnswers();
    double numCorrectMarkedAnswers = 0.0;
    double numIncorrectMarkedAnswers = 0.0;

    // Correct and incorrect marked answers lists will be needed to
    // calculate and save to DB each answer grade
    List<ItemSessionResponse> correctMarkedAnswers = new ArrayList<ItemSessionResponse>();
    List<ItemSessionResponse> incorrectMarkedAnswers = new ArrayList<ItemSessionResponse>();

    // Get correct and incorrect answers marked
    for (ItemSessionResponse answer : question.getAnswers()) {
      if (answer.getMarked())
        if (answer.getValue() != 0) {
          numCorrectMarkedAnswers++;
        } else {
          numIncorrectMarkedAnswers++;
        }
    }

    // Grade the question according to correct and incorrect answers and the
    // evaluation method choosen

    // If the question has no correct answers
    if (numCorrectAnswers == 0)
      // Question will be correct if it has no answers marked
      if (numCorrectMarkedAnswers == 0 && numIncorrectMarkedAnswers == 0)
        questionGrade = maxGradePerQuestion;
      else {
        // Depending on evaluation method
        if (currentExam.isPartialCorrection())
          // At partial correction, question with no correct answers
          // but answered will be graded at minQuestionGrade
          questionGrade = maxGradePerQuestion * currentExam.getMinQuestionGrade();
        else
          // No partial correction grades question to penalty for
          // questions failed
          questionGrade = -(maxGradePerQuestion * currentExam.getPenQuestionFailed());

      }
    else {
      // If the question was not answered
      if (numCorrectMarkedAnswers == 0 && numIncorrectMarkedAnswers == 0)
        questionGrade = -(maxGradePerQuestion * currentExam.getPenQuestionNotAnswered());
      // No database update needed (No answers marked!!!)
      else {
        // If exam has been configured to a partial correction, each
        // question will be qualified attending to the number of correct
        // and incorrect marked answers, with its corresponding penalty
        if (currentExam.isPartialCorrection()) {
          // If the question was perfectly answered, its grade is
          // maxGradePerQuestion (avoids rounding problems when the
          // question is perfectly answered)
          if (numCorrectMarkedAnswers == numCorrectAnswers && numIncorrectMarkedAnswers == 0) {
            questionGrade = maxGradePerQuestion;
          } else {
            // Adds grade for each correct answer marked
            questionGrade += (maxGradePerQuestion / numCorrectAnswers) * numCorrectMarkedAnswers;
            // Subtracts penalty for incorrect answer marked,
            // multiplied by the number of incorrect answers marked
            questionGrade -=
                (maxGradePerQuestion * currentExam.getPenAnswerFailed())
                    * numIncorrectMarkedAnswers;
            // If calculated grade for the question is less than
            // minQuestionGrade configured by tutor for this exam,
            // question grade will be minQuestionGrade
            if (questionGrade < maxGradePerQuestion * currentExam.getMinQuestionGrade()) {
              questionGrade = maxGradePerQuestion * currentExam.getMinQuestionGrade();
            }

          }
        }
        // Else, the question will be qualified to maxGradePerQuestion
        // if it was correctly answered, or it will be qualified to the
        // penalty for question failed
        else if (numCorrectMarkedAnswers == numCorrectAnswers && numIncorrectMarkedAnswers == 0) {
          questionGrade = maxGradePerQuestion;
        } else {
          questionGrade = -(maxGradePerQuestion * currentExam.getPenQuestionFailed());
        }
      }
    }

    if (currentExam.isConfidenceLevel()) {
      if (numCorrectAnswers == numCorrectMarkedAnswers && numIncorrectMarkedAnswers == 0) {
        if (question.getExamineeWasConfident()) {
          questionGrade += maxGradePerQuestion * currentExam.getRewardConfidenceLevel();
        }
      } else {
        if (question.getExamineeWasConfident()) {
          questionGrade -= maxGradePerQuestion * currentExam.getPenConfidenceLevel();
        }
      }
    }

    // Its possible that questionGrade equals -0 (For example, when penalty
    // for question not answered is equal to 0)
    // Web interface could show -0.0 grade for that question, and it wont be
    // elegant.
    // This code corrects that
    if (questionGrade == -0.0)
      return 0.0;
    else
      return questionGrade;
  }

  public TestSession getStudentExam(Long idstd, Long idexam) {
    // Same performing like "LearnerManagementService.getAlreadyDoneExam",
    // but we need a user object.
    User user = getUserData(idstd);
    if (user == null)
      return null;

    TestSession exam = examDAO.getAlreadyDoneExam(user, idexam);

    /*
     * The punctuation for each question is not stored in the database, just the punctuation for
     * each answer. That's why it is calculated...
     */

    int numberOfQuestions = exam.getQuestions().size();
    double maxGrade = exam.getMaxGrade();
    double maxGradePerQuestion = ((double) maxGrade) / ((double) numberOfQuestions);

    for (ItemSession question : exam.getQuestions()) {

      double questionGrade = 0.0;
      /*
       * 
       * 
       * for (ExamAnswer answer : question.getAnswers()) {
       * 
       * // the answerGrade (and the questionGrade) only changes (initially is 0) // if the answer
       * is marked (getMarked == true) and it is a correct answer (getValue != 0)
       * 
       * // the questionGrade is calculated using the number of correct marked answers // (instead
       * of using the answersGrades) // in order to avoid rounding problems
       * 
       * if (answer.getMarked()) if (answer.getValue() != 0) { //double answerGrade =
       * maxGradePerQuestion * answer.getValue() / 100.0; //questionGrade += answerGrade;
       * numCorrectMarkedAnswers ++; } else numIncorrectMarkedAnswers ++;
       * 
       * }
       * 
       * 
       * questionGrade = numCorrectMarkedAnswers / numCorrectAnswers * maxGradePerQuestion;
       * 
       * question.setQuestionGrade(questionGrade);
       */
      questionGrade = gradeQuestion(question, exam, maxGradePerQuestion);
      question.setQuestionGrade(questionGrade);
    }

    // The total grade of the exam is retrieved from the database
    return exam;
  }

  public boolean importQuestions(Group group, Subject sbj, List<Item> qList, String sourcePath) {
    // Iterate in all questions:
    Iterator<Item> iterQ = qList.iterator();
    Item q = null;

    while (iterQ.hasNext()) {
      q = iterQ.next();
      // Reset the Id in order to save the question as a new one
      q.setId(null);
      // Reset the "usedInExam" because is a new question
      q.setUsedInExam(false);
      // Sets the new theme
      q.setSubject(sbj);
      // Sets the new group
      q.setGroup(group);
      q.setVisibility(Constants.GROUP);
      // Saves the question into the database
      templateExamQuestionDAO.save(q);

      // Question mmedia files:
      List<MediaElem> qmm = q.getMmedia();
      Iterator<MediaElem> iterQMM = qmm.iterator();
      while (iterQMM.hasNext()) {
        MediaElem mm = iterQMM.next();
        // Duplicate files and modify the path
        String lastPath = mm.getPath();
        mm.setPath(duplicateMmediaFile(sourcePath, mm.getPath()));
        if (mm.getPath() == null) {
          mm.setPath(lastPath);
        }
        // Resets the id
        mm.setId(null);
        // Saves the mmedia in the question
        if (mm.getPath() != null)
          saveMediaElemToQuestion(q, mm);
      }
      // Copy the comments
      List<MediaElem> cmm = q.getMmediaComment();
      Iterator<MediaElem> iterCMM = cmm.iterator();
      while (iterCMM.hasNext()) {
        MediaElem mm = iterCMM.next();
        // Duplicate files and modify the path
        String lastPath = mm.getPath();
        mm.setPath(duplicateMmediaFile(sourcePath, mm.getPath()));
        if (mm.getPath() == null) {
          mm.setPath(lastPath);
        }
        // Resets the id
        mm.setId(null);
        // Saves the mmedia in the question
        if (mm.getPath() != null)
          saveMediaElemToComment(q, mm);
      }

      // Copy the answers
      List<ItemResponse> anwsList = q.getAnswers();
      Iterator<ItemResponse> iterAns = anwsList.iterator();
      while (iterAns.hasNext()) {
        ItemResponse answer = iterAns.next();
        // Reset the id to get duplicate:
        answer.setId(null);
        // Sets the new question (it's a new one)
        answer.setQuestion(q);
        // Reset the "usedInExam" because is a new answer
        answer.setUsedInExam(false);
        saveAnswer(answer);
        // Answer mmedia files:
        List<MediaElem> amm = answer.getMmedia();
        Iterator<MediaElem> iterAMM = amm.iterator();
        while (iterAMM.hasNext()) {
          MediaElem mm = iterAMM.next();
          // Duplicate files and modify the path
          mm.setPath(duplicateMmediaFile(sourcePath, mm.getPath()));
          // Resets the id to duplicate
          mm.setId(null);

          if (mm.getPath() == null) {
            mm.setPath("");
          }
          // Saves the mmedia in the answer
          saveMediaElemToAnswer(answer, mm);
        }
      }

    }

    return true;
  }

  public String randomFilename() {
    Random rnd = new Random();
    StringBuffer name = new StringBuffer(Constants.MMEDIAPREFIX);
    for (int i = 0; i < 10; i++) {
      name.append(rnd.nextInt(10));
    }
    return name.toString();
  }

  /**
   * Duplicates a Mmedia file (concats a "med" string as a prefix)
   * 
   * @param sourcePath Source directory path
   * @param srcFile Source file name
   * @return Name of the destination (copy) file, null if a problem arises
   */
  private String duplicateMmediaFile(String sourcePath, String srcFile) {
    // Gets a new random name for the mmedia file:
    // First the name and extension
    String filename;
    String extension = "";
    try {
      extension = srcFile.substring(srcFile.lastIndexOf("."));
    } catch (Exception e) {
      // Si no tiene extensión es un archivo de youtube
      extension = "";
    }
    // New file
    File saveFile;
    do {
      filename = randomFilename() + extension;
      saveFile = new File(sourcePath + filename);
    } while (saveFile.exists());
    // Copy the file:
    try {
      // First, open source file
      FileInputStream reader = new FileInputStream(sourcePath + srcFile);
      // Second, create & open destination file
      // This way prevent from creepy destination files if source reading
      // fails
      FileOutputStream writer = new FileOutputStream(sourcePath + filename);
      byte[] buffer = new byte[Constants.FILEBUFSIZE];
      while (reader.read(buffer) != -1)
        writer.write(buffer);
      reader.close();
      writer.close();
    } catch (Exception e) {
      return null;
    }

    return filename;
  }

  public void deleteStudentGradeAndExam(Long idstd, Long idex) {
    // Deleting grade
    this.statsDAO.deleteStudentGrade(idstd, idex);
    // Deleting exam
    this.examDAO.deleteStudentExam(idstd, idex);
  }


  public List<ExamGlobalInfo> getNextExams() {
    return examDAO.getNextExams();
  }

  public List<ExamGlobalInfo> getActiveExams() {
    return examDAO.getActiveExams();
  }

  public List<Long> getExamIds(Item question) {
    return examDAO.getExamIds(question);
  }

  public void updateQuestionNotUsedInExam(Long idgrp) {
    templateExamQuestionDAO.updateQuestionNotUsedInExam(idgrp);
  }
}
