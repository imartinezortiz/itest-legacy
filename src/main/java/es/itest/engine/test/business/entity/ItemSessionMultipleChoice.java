package es.itest.engine.test.business.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
      if (ea.getSolution() == 1) {
        numAnswers++;
      }
    }
    return numAnswers;
  }

  public int getMarkedAnswer() {
    int marked = 0;
    for (int i = 0; i < responses.size(); i++) {
      ItemSessionResponse ea = responses.get(i);
      if (ea.getMarked()) {
        marked++;
      }
    }
    return marked;
  }
  public BigDecimal grade() {

    double questionGrade = 0.0;
    double numCorrectAnswers = getNumCorrectAnswers();
    double numCorrectMarkedAnswers = 0.0;
    double numIncorrectMarkedAnswers = 0.0;

    // Correct and incorrect marked answers lists will be needed to calculate and save to DB each
    // answer grade
    List<ItemSessionResponse> correctMarkedAnswers = new ArrayList<ItemSessionResponse>();
    List<ItemSessionResponse> incorrectMarkedAnswers = new ArrayList<ItemSessionResponse>();

    // Get correct and incorrect answers marked
    for (ItemSessionResponse answer : getAnswers()) {
      if (answer.getMarked())
        if (answer.getValue() != 0) {
          numCorrectMarkedAnswers++;
          if (updateDatabase)
            correctMarkedAnswers.add(answer);
        } else {
          numIncorrectMarkedAnswers++;
          if (updateDatabase)
            incorrectMarkedAnswers.add(answer);
        }
    }

    // Grade the question according to correct and incorrect answers and the evaluation method
    // choosen

    // If the question has no correct answers
    if (numCorrectAnswers == 0)
      // Question will be correct if it has no answers marked
      if (numCorrectMarkedAnswers == 0 && numIncorrectMarkedAnswers == 0)
        questionGrade = maxGradePerQuestion;
      else {
        // Depending on evaluation method
        if (currentExam.isPartialCorrection())
          // At partial correction, question with no correct answers but answered will be graded at
          // minQuestionGrade
          questionGrade = maxGradePerQuestion * currentExam.getMinQuestionGrade();
        else
          // No partial correction grades question to penalty for questions failed
          questionGrade = -(maxGradePerQuestion * currentExam.getPenQuestionFailed());

        if (updateDatabase) {
          // All answers marked were incorrect (question has not correct answers!!)
          double answerGrade = questionGrade / numIncorrectMarkedAnswers;
          for (ItemSessionResponse answer : incorrectMarkedAnswers)
            answerExamDAO.updateExamAnswerGrade(currentExam.getId(), id, question.getId(),
                answer.getId(), answerGrade);
        }
      }
    else {
      // If the question was not answered
      if (numCorrectMarkedAnswers == 0 && numIncorrectMarkedAnswers == 0)
        questionGrade = -(maxGradePerQuestion * currentExam.getPenQuestionNotAnswered());
      // No database update needed (No answers marked!!!)
      else {
        // If exam has been configured to a partial correction, each question will be qualified
        // attending to the number of correct and incorrect marked answers, with its corresponding
        // penalty
        if (currentExam.isPartialCorrection()) {
          // If the question was perfectly answered, its grade is maxGradePerQuestion (avoids
          // rounding problems when the question is perfectly answered)
          if (numCorrectMarkedAnswers == numCorrectAnswers && numIncorrectMarkedAnswers == 0) {
            questionGrade = maxGradePerQuestion;
            if (updateDatabase) {
              // All answers are correct
              double answerGrade = questionGrade / numCorrectMarkedAnswers;
              for (ItemSessionResponse answer : correctMarkedAnswers)
                answerExamDAO.updateExamAnswerGrade(currentExam.getId(), id, question.getId(),
                    answer.getId(), answerGrade);
            }
          } else {
            // Adds grade for each correct answer marked
            questionGrade += (maxGradePerQuestion / numCorrectAnswers) * numCorrectMarkedAnswers;
            // Subtracts penalty for incorrect answer marked, multiplied by the number of incorrect
            // answers marked
            questionGrade -=
                (maxGradePerQuestion * currentExam.getPenAnswerFailed())
                    * numIncorrectMarkedAnswers;
            // If calculated grade for the question is less than minQuestionGrade configured by
            // tutor for this exam, question grade will be minQuestionGrade
            if (questionGrade < maxGradePerQuestion * currentExam.getMinQuestionGrade()) {
              questionGrade = maxGradePerQuestion * currentExam.getMinQuestionGrade();
              // La ponctution de la question est divicé de la même façon pour les reponds corrects
              // et les incorrects
              // Question grade will be divided to equal parts among correct and incorrect answers
              if (updateDatabase) {
                double answerGrade =
                    questionGrade / (numCorrectMarkedAnswers + numIncorrectMarkedAnswers);
                for (ItemSessionResponse answer : correctMarkedAnswers)
                  answerExamDAO.updateExamAnswerGrade(currentExam.getId(), id, question.getId(),
                      answer.getId(), answerGrade);
                for (ItemSessionResponse answer : incorrectMarkedAnswers)
                  answerExamDAO.updateExamAnswerGrade(currentExam.getId(), id, question.getId(),
                      answer.getId(), answerGrade);
              }
            } else if (updateDatabase) {
              // Correct answers will be graded to maxGradePerQuestion/numCorrectAnswers
              double answerGrade = maxGradePerQuestion / numCorrectAnswers;
              for (ItemSessionResponse answer : correctMarkedAnswers)
                answerExamDAO.updateExamAnswerGrade(currentExam.getId(), id, question.getId(),
                    answer.getId(), answerGrade);
              // Incorrect answers will be graded to pre-configured penalty per answer failed
              answerGrade = -(maxGradePerQuestion * currentExam.getPenAnswerFailed());
              for (ItemSessionResponse answer : incorrectMarkedAnswers)
                answerExamDAO.updateExamAnswerGrade(currentExam.getId(), id, question.getId(),
                    answer.getId(), answerGrade);

            }
          }
        }
        // Else, the question will be qualified to maxGradePerQuestion if it was correctly answered,
        // or it will be qualified to the penalty for question failed
        else if (numCorrectMarkedAnswers == numCorrectAnswers && numIncorrectMarkedAnswers == 0) {
          questionGrade = maxGradePerQuestion;
          if (updateDatabase) {
            // There are only correct answers. Question grade will be divided to equal parts among
            // them
            double answerGrade = questionGrade / numCorrectAnswers;
            for (ItemSessionResponse answer : correctMarkedAnswers)
              answerExamDAO.updateExamAnswerGrade(currentExam.getId(), id, question.getId(),
                  answer.getId(), answerGrade);
          }
        } else {
          questionGrade = -(maxGradePerQuestion * currentExam.getPenQuestionFailed());
          if (updateDatabase) {
            // The question was failed
            // Correct answers will be graded to 0.0
            for (ItemSessionResponse answer : correctMarkedAnswers)
              answerExamDAO.updateExamAnswerGrade(currentExam.getId(), id, question.getId(),
                  answer.getId(), 0.0);
            // Question failed penalty will be divided to equal parts among incorrect answers
            double answerGrade = questionGrade / numIncorrectMarkedAnswers;
            for (ItemSessionResponse answer : incorrectMarkedAnswers)
              answerExamDAO.updateExamAnswerGrade(currentExam.getId(), id, question.getId(),
                  answer.getId(), answerGrade);
          }
        }
      }
    }
    if (currentExam.isConfidenceLevel()) {
      if (numCorrectAnswers == numCorrectMarkedAnswers && numIncorrectMarkedAnswers == 0) {
        if (question.getExamineeWasConfident()) {
          questionGrade += maxGradePerQuestion * currentExam.getRewardConfidenceLevel();
          if (updateDatabase) {
            // All answers are correct
            double answerGrade = questionGrade / numCorrectMarkedAnswers;
            for (ItemSessionResponse answer : correctMarkedAnswers)
              answerExamDAO.updateExamAnswerGrade(currentExam.getId(), id, question.getId(),
                  answer.getId(), answerGrade);
          }
        }
      } else {
        if (question.getExamineeWasConfident()) {
          questionGrade -= maxGradePerQuestion * currentExam.getPenConfidenceLevel();
          if (updateDatabase) {
            // The question was failed
            // Correct answers will be graded to 0.0
            for (ItemSessionResponse answer : correctMarkedAnswers)
              answerExamDAO.updateExamAnswerGrade(currentExam.getId(), id, question.getId(),
                  answer.getId(), 0.0);
            // Question failed penalty will be divided to equal parts among incorrect answers
            double answerGrade = questionGrade / numIncorrectMarkedAnswers;
            for (ItemSessionResponse answer : incorrectMarkedAnswers)
              answerExamDAO.updateExamAnswerGrade(currentExam.getId(), id, question.getId(),
                  answer.getId(), answerGrade);
          }
        }
      }
    }
    // Its possible that questionGrade equals -0 (For example, when penalty for question not
    // answered is equal to 0)
    // Web interface could show -0.0 grade for that question, and it wont be elegant.
    // This code corrects that
    if (questionGrade == -0.0)
      return 0.0;
    else
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
