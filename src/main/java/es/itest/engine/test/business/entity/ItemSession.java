package es.itest.engine.test.business.entity;

import java.math.BigDecimal;
import java.util.List;

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
  private Item item;

  private BigDecimal grade; // Obtained grade for the question
  
  private boolean examineeWasConfident; // el nivel de confianza

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public BigDecimal getQuestionGrade() {
    return grade;
  }

  // Return the text of the question splitted into paragraphs
  public String[] getTextParagraphs() {
    try {
      return (text.trim()).split("\n");
    } catch (Exception e) {
      return null;
    }
  }

  // Return the text of the question comment splitted into paragraphs
  public String[] getCommentParagraphs() {
    try {
      return (comment.trim()).split("\n");
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


  public abstract BigDecimal grade();


}
