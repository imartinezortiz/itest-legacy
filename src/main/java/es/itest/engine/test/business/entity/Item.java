package es.itest.engine.test.business.entity;

import java.util.Collections;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.cesfelipesegundo.itis.model.MediaElemComparator;

import es.itest.engine.course.business.entity.Subject;

/**
 * Represents a test question 
 */
@Entity
public class Item {
  /**
   * Difficulty constants
   */
  public enum DifficultyEnum {
      LOW, MEDIUM, HIGH;
  }

  /**
   * Visibility constants
   */
  public enum VisibilityEnum {
    GROUP, COURSE, PUBLIC;
  }
  
  public enum QuestionTypeEnum {
    MULTIPLE_CHOICE // 0 database vaue
    , FILL_IN_BLANKS // 1 database value;
    }
  
  @Id
  private Long id;
  
  /**
   * Title of the question
   */
  private String title;
  
  private DifficultyEnum difficulty;
  
  private VisibilityEnum visibility;

  private QuestionTypeEnum type;

  private boolean usedInExam;
  
  private String body;
  
  private List<MediaElem> mmedia;
  
  private List<ItemResponse> answers;
  
  @Transient
  private int numCorrectAnswers;

  @ManyToOne
  private Subject subject; // Related theme

  private boolean active;

  private String comment;
  
  private List<MediaElem> mmediaComment;

  public Item(QuestionTypeEnum type) {
    this.type = type;
  }

  public QuestionTypeEnum getType() {
    return type;
  }

  public void setType(QuestionTypeEnum type) {
    this.type = type;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public DifficultyEnum getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(DifficultyEnum difficulty) {
    this.difficulty = difficulty;
  }

  public VisibilityEnum getVisibility() {
    return visibility;
  }

  public void setVisibility(VisibilityEnum visibility) {
    this.visibility = visibility;
  }

  public List<ItemResponse> getAnswers() {
    return answers;
  }

  public void setAnswers(List<ItemResponse> answers) {
    this.answers = answers;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public List<MediaElem> getMmedia() {
    // Orders the list:
    if (mmedia != null)
      Collections.sort(mmedia, new MediaElemComparator());
    return mmedia;
  }

  public void setMmedia(List<MediaElem> mmedia) {
    this.mmedia = mmedia;
  }

  public List<MediaElem> getMmediaComment() {
    if (mmediaComment != null)
      Collections.sort(mmediaComment, new MediaElemComparator());
    return mmediaComment;
  }

  public void setMmediaComment(List<MediaElem> mmediaComment) {
    this.mmediaComment = mmediaComment;
  }

  public String getText() {
    return body;
  }

  public void setText(String text) {
    this.body = text;
  }

  public Subject getSubject() {
    return subject;
  }

  public void setSubject(Subject subject) {
    this.subject = subject;
  }

  public int getNumCorrectAnswers() {
    return numCorrectAnswers;
  }

  public void setNumCorrectAnswers(int numCorrectAnswers) {
    this.numCorrectAnswers = numCorrectAnswers;
  }

  public boolean isUsedInExam() {
    return usedInExam;
  }

  public void setUsedInExam(boolean usedInExam) {
    this.usedInExam = usedInExam;
  }

  // Return the text of the question splitted into paragraphs
  public String[] getTextParagraphs() {
    if (body != null)
      return (body.trim()).split("\n");
    else
      return null;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  // Return the text of the question comment splitted into paragraphs
  public String[] getCommentParagraphs() {
    if (comment != null)
      return (comment.trim()).split("\n");
    else
      return null;
  }

  public int getSize() {
    if (mmedia != null)
      return mmedia.size();
    else
      return 0;
  }

  public int getCommentSize() {
    if (mmediaComment != null)
      return mmediaComment.size();
    else
      return 0;
  }
}
