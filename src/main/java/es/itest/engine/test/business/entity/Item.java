package es.itest.engine.test.business.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;

/**
 * Represents a test question
 */
@Entity
public abstract class Item {
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

  public enum ItemTypeEnum {
    MULTIPLE_CHOICE // 0 database vaue
    , FILL_IN_BLANKS // 1 database value;
  }


  private static final int ITEM_BODY_MAX_lENGTH = 4096;

  private static final int COMMENT_MAX_LENGTH = 1024;
  
  private static final int ITEM_TITLE_MAX_LENGTH = 60;
  
  @Id
  private Long id;

  /**
   * Title of the question
   */
  @Size(max=ITEM_TITLE_MAX_LENGTH)
  @Column(length=ITEM_TITLE_MAX_LENGTH)
  private String title;

  private DifficultyEnum difficulty;

  private VisibilityEnum visibility;

  private ItemTypeEnum type;

  private boolean usedInExam;

  @Size(max=ITEM_BODY_MAX_lENGTH)
  @Column(length=ITEM_BODY_MAX_lENGTH)
  private String itemBody;

  private List<MediaElem> mmedia;

  private List<ItemResponse> answers;

  private int numCorrectAnswers;

  private boolean active;

  @Size(max=COMMENT_MAX_LENGTH)
  @Column(length=COMMENT_MAX_LENGTH)
  private String comment;

  private List<MediaElem> mmediaComment;

  public Item(ItemTypeEnum type) {
    this.type = type;
  }

  public ItemTypeEnum getType() {
    return type;
  }

  public void setType(ItemTypeEnum type) {
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
    return mmedia;
  }

  public void setMmedia(List<MediaElem> mmedia) {
    this.mmedia = mmedia;
  }

  public List<MediaElem> getMmediaComment() {
    return mmediaComment;
  }

  public void setMmediaComment(List<MediaElem> mmediaComment) {
    this.mmediaComment = mmediaComment;
  }

  public String getText() {
    return itemBody;
  }

  public void setText(String text) {
    this.itemBody = text;
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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
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
