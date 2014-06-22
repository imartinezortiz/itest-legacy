package es.itest.engine.test.business.entity;

import javax.persistence.Embeddable;

/**
 * Representa cada una de las respuestas que van a ser mostradas en cada pregunta de un examen para
 * un alumno
 * 
 * @author chema
 * 
 */
@Embeddable
public class ItemSessionResponse {

  private Long id; // Id de la respuesta

  private ItemResponse response;

  private boolean marked = false; // Al iniciar el examen ninguna respuesta está marcada

  public boolean isMarked() {
    return this.marked;
  }

  public void setMarked(boolean marked) {
    this.marked = marked;
  }

  public ItemResponse getResponse() {
    return response;
  }


}
