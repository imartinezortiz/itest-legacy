package es.itest.engine.test.business.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import es.itest.engine.course.business.entity.Group;
import es.itest.engine.course.business.entity.TestSessionTemplateSubject;


/**
 * Añade, a los datos básicos de un examen, la configuración de un examen
 * @author chema
 *
 */
public class TestSessionTemplate  extends TestDetails {

	private int visibility;				// Visibility for the questions uses to make the exam
	private int questionDistribution;	// Distribution of the questions: continuous or by questions 
	private int duration;				// Duration
	private double maxGrade;				// Maximun grade for the exam
	private List<TestSessionTemplateSubject> subjects;	// Lista de temas
	private boolean activeReview;
	private Date startDate;
	private Date endDate;
	private Date startDateRevision;
	private Date endDateRevision;
	private int weight;				// Weight of the exam into the final grade
	private int questionNumber;
	private boolean customized;
	
	
	
	public boolean isCustomized() {
		return customized;
	}

	public void setCustomized(boolean customized) {
		this.customized = customized;
	}

	public TestSessionTemplate(){
		
	}
	
	public TestSessionTemplate(TestSessionTemplate exam) {
		super();
		this.setGroup(exam.getGroup());
		this.setTitle(exam.getTitle());
		this.setId(exam.getId());
		this.visibility = exam.visibility;
		this.questionDistribution = exam.questionDistribution;
		this.duration = exam.duration;
		this.maxGrade = exam.maxGrade;
		this.subjects = exam.subjects;
		this.activeReview = exam.activeReview;
		this.startDate = exam.startDate;
		this.endDate = exam.endDate;
		this.startDateRevision = exam.startDateRevision;
		this.endDateRevision = exam.endDateRevision;
		this.weight = exam.weight;
		this.questionNumber = exam.questionNumber;
		this.partialCorrection = exam.partialCorrection;
		this.showNumCorrectAnswers = exam.showNumCorrectAnswers;
		this.penQuestionFailed = exam.penQuestionFailed;
		this.penQuestionNotAnswered = exam.penQuestionNotAnswered;
		this.penAnswerFailed = exam.penAnswerFailed;
		this.penConfidenceLevel = exam.penConfidenceLevel;
		this.rewardConfidenceLevel = exam.rewardConfidenceLevel;
		this.minQuestionGrade = exam.minQuestionGrade;
		this.published = exam.published;
		this.confidenceLevel = exam.confidenceLevel;
		this.customized = exam.customized;
	}

	/** Sets partial correction by answers*/
	private boolean partialCorrection;
	
	/** Show number of correct answers*/
	private boolean showNumCorrectAnswers;
	
	/** Penalty by question failed*/
	private double penQuestionFailed;
	
	/** Penalty by question not answered*/
	private double penQuestionNotAnswered;
	
	/** Penalty by answer failed*/
	private double penAnswerFailed;
	
	/** Penalty by confidence level*/
	private double penConfidenceLevel;
	
	/** Reward by confidence level*/
	private double rewardConfidenceLevel;
	
	/** Min question grade*/
	private double minQuestionGrade;
	
	/** Exam is published*/
	private boolean published;
	
	/** Exam is active confidence level*/
	private boolean confidenceLevel;
	
	
	
	public boolean isConfidenceLevel() {
		return confidenceLevel;
	}

	public void setConfidenceLevel(boolean confidenceLevel) {
		this.confidenceLevel = confidenceLevel;
	}

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

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public double getMaxGrade() {
		return maxGrade;
	}

	public void setMaxGrade(double maxGrade) {
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
	
	public boolean isPartialCorrection() {
		return partialCorrection;
	}

	public void setPartialCorrection(boolean partialCorrection) {
		this.partialCorrection = partialCorrection;
	}

	public boolean isShowNumCorrectAnswers() {
		return showNumCorrectAnswers;
	}

	public void setShowNumCorrectAnswers(boolean showNumCorrectAnswers) {
		this.showNumCorrectAnswers = showNumCorrectAnswers;
	}

	public double getPenQuestionFailed() {
		return penQuestionFailed;
	}

	public void setPenQuestionFailed(double penQuestionFailed) {
		this.penQuestionFailed = penQuestionFailed;
	}

	public double getPenQuestionNotAnswered() {
		return penQuestionNotAnswered;
	}

	public void setPenQuestionNotAnswered(double penQuestionNotAnswered) {
		this.penQuestionNotAnswered = penQuestionNotAnswered;
	}

	public double getPenAnswerFailed() {
		return penAnswerFailed;
	}

	public void setPenAnswerFailed(double penAnswerFailed) {
		this.penAnswerFailed = penAnswerFailed;
	}

	public double getMinQuestionGrade() {
		return minQuestionGrade;
	}

	public void setMinQuestionGrade(double minQuestionGrade) {
		this.minQuestionGrade = minQuestionGrade;
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

	public double getPenConfidenceLevel() {
		return penConfidenceLevel;
	}

	public void setPenConfidenceLevel(double penConfidenceLevel) {
		this.penConfidenceLevel = penConfidenceLevel;
	}

	public double getRewardConfidenceLevel() {
		return rewardConfidenceLevel;
	}

	public void setRewardConfidenceLevel(double rewardConfidenceLevel) {
		this.rewardConfidenceLevel = rewardConfidenceLevel;
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
	
	private Test test;
	
	public TestSession createTestSession() {
		Long startingDate = System.currentTimeMillis();

		TestSession exam = new TestSession();

		exam.setStartingDate(startingDate);
		exam.setPenConfidenceLevel(getPenConfidenceLevel());
		exam.setRewardConfidenceLevel(getRewardConfidenceLevel());
		exam.setId(test.getId());
		exam.setTitle(test.getTitle());
		exam.setGroup(test.getGroup());

		exam.setDuration(test.getDuration());
		exam.setMaxGrade(test.getMaxGrade());
		
		/* Get new parameters from database v3.6 */
		exam.setPartialCorrection(test.isPartialCorrection());
		exam.setShowNumCorrectAnswers(test.isShowNumCorrectAnswers());
		exam.setPenQuestionFailed(test.getPenQuestionFailed());
		exam.setPenAnswerFailed(test.getPenAnswerFailed());
		exam.setPenQuestionNotAnswered(test.getPenQuestionNotAnswered());
		exam.setMinQuestionGrade(test.getMinQuestionGrade());
		exam.setConfidenceLevel(test.isConfidenceLevel());

		int visibility = test.getVisibility();
		Random generator = new Random();

		List<TestSubject> templateSubjects = test.getSubjects();
		ListIterator<TestSubject> itTemplateSubjects = templateSubjects
				.listIterator();

		// Questions of the exam
		List<ItemSession> questions = new ArrayList<ItemSession>();
		Group examGroup = test.getGroup();

		while (itTemplateSubjects.hasNext()) {
			TestSubject templateSubject = itTemplateSubjects.next();

			int maxDifficulty = templateSubject.getMaxDifficulty();
			int minDifficulty = templateSubject.getMinDifficulty();
			int answersxQuestionNumber = templateSubject
					.getAnswersxQuestionNumber();
			int questionsNumber = templateSubject.getQuestionsNumber();
			List<Item> templateQuestions = templateSubject
					.getQuestions();

			// Supposing that questionsSize >= questionsNumber

			int questionsCount = 0;
			while (questionsCount < questionsNumber) {
				// selection randomly of the question

				int randomQuestion = generator
						.nextInt(templateQuestions.size());
				Item templateQuestion = templateQuestions
						.get(randomQuestion);

				boolean visible = false;

				Group questionGroup = templateQuestion.getGroup();
				switch (visibility) {
					case 0:
						// Group visibility
						visible = questionGroup.equals(examGroup);
						break;
	
					case 1:
						// Institution visibility
						visible = questionGroup.equals(examGroup)
								|| questionGroup.getInstitution().equals(
										questionGroup.getInstitution());
						break;
				}
				
				if ((templateQuestion.getActive() == 1)
						&& (templateQuestion.getDifficulty() <= maxDifficulty)
						&& (templateQuestion.getDifficulty() >= minDifficulty)
						&& visible) {

					questionsCount++;

					ItemSession question = new ItemSession();
					questions.add(question);

					question.setComment(templateQuestion.getComment());
					question.setId(templateQuestion.getId());
					question.setMmedia(templateQuestion.getMmedia());
					question.setMmediaComment(templateQuestion.getMmediaComment());
					question.setText(templateQuestion.getText());
					question.setType(templateQuestion.getType());
					question.setNumCorrectAnswers(templateQuestion
							.getNumCorrectAnswers());

					List<ItemResponse> templateAnswers = templateQuestion
							.getAnswers();

					// Answers of the question
					List<ItemSessionResponse> answers = new ArrayList<ItemSessionResponse>();

					// Supposing that answerSize >= answersxQuestionNumber

					// Firsty, the correct answers are selected
					int answersCount = 0;

					ListIterator<ItemResponse> itTemplateAnswers = templateAnswers
							.listIterator();
					while (itTemplateAnswers.hasNext()) {
						ItemResponse templateAnswer = itTemplateAnswers
								.next();

						if ((templateAnswer.getSolution() == 1)
								&& (templateAnswer.getActive() == 1))

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

//							answerExamDAO.addNewExamAnswer(idexam, idlearner,
//									question.getId(), answer.getId(),
//									startingDate);

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

					while ((answersCount < answersxQuestionNumber)
							&& (templateAnswers.size() > 0)) {
						// selection randomly of the answer

						int randomAnswer = generator.nextInt(templateAnswers
								.size());
						ItemResponse templateAnswer = templateAnswers
								.get(randomAnswer);

						if (templateAnswer.getActive() == 1) {

							ItemSessionResponse answer = new ItemSessionResponse();

							answer.setId(templateAnswer.getId());
							answer.setMmedia(templateAnswer.getMmedia());
							answer.setSolution(templateAnswer.getSolution());
							answer.setValue(templateAnswer.getValue());
							answer.setText(templateAnswer.getText());
							answer.setMarked(false);

							answers.add(answer);

//							answerExamDAO.addNewExamAnswer(idexam, idlearner,
//									question.getId(), answer.getId(),
//									startingDate);

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
