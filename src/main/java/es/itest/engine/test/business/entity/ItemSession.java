package es.itest.engine.test.business.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Representa cada una de las preguntas que van a ser mostradas en un examen para un alumno
 * 
 * @author chema
 * 
 */
@Entity
public abstract class ItemSession implements Adaptable {

  @Id
  private Long id; // Id de la pregunta

  @ManyToOne
  protected Item item;

  protected BigDecimal grade; // Obtained grade for the question
  
  protected boolean examineeWasConfident; // el nivel de confianza

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public BigDecimal getQuestionGrade() {
    return grade;
  }
  
  public void setQuestionGrade(BigDecimal grade) {
    this.grade = grade;
  }

  // Return the text of the question splitted into paragraphs
  public String[] getTextParagraphs() {
    try {
      return (item.getText().trim()).split("\n");
    } catch (Exception e) {
      return null;
    }
  }

  // Return the text of the question comment splitted into paragraphs
  public String[] getCommentParagraphs() {
    try {
      return (item.getComment().trim()).split("\n");
    } catch (Exception e) {
      return null;
    }
  }

  public boolean getExamineeWasConfident() {
    return examineeWasConfident;
  }

  public void setExamineeWasConfident(boolean wasConfident) {
    this.examineeWasConfident = wasConfident;
  }


  public abstract BigDecimal grade(GradingParameters params);

  public Item getItem() {
    return item;
  }

}
