package es.itest.engine.test.business.entity;

import javax.persistence.Embeddable;


/**
 * Represents a multi media asset associated to an {@link Item}
 * 
 */
@Embeddable
public class MediaElem {
  public enum MediaElemTypeEnum {
    FLASH(1), IMAGE(2), SOUND(3), GEOGEBRA(4), JAVAAPPLET(5), SIBELIUS(6), YOUTUBE(7);
    
    private int value;
    
    private MediaElemTypeEnum(final int value) {
      this.value = value;
    }
  };
  
  public enum GeogebraTypeEnum {
    BASIC, ADVANCED;
  }
  
  private String name;
  
  private String path; // Ruta al elemento
  
  private MediaElemTypeEnum type; // Tipo de elemento: Flash, Imagen, Sonido
  
  private String width; // Ancho del elemento - B.D. V3.5
  
  private String height; // Alto del elemento - B.D. V3.5
  
  private GeogebraTypeEnum geogebraType;

  public MediaElem() {

  }

  /**
   * Constructor copia Copia la informacion sobre un elemento almacenado. ATENCION: NO duplica el
   * fichero del elemento multimedia
   * 
   * @param copyFrom : Objeto MediaElem desde el que se copia la informacion
   */
  public MediaElem(MediaElem copyFrom) {

    // Uso del constructor copia de String para evitar posibles problemas con las referencias
    if (copyFrom.path != null)
      path = new String(copyFrom.path);

    type = copyFrom.type;
    geogebraType = copyFrom.geogebraType;
    if (copyFrom.width != null)
      width = new String(copyFrom.width);

    if (copyFrom.height != null)
      height = new String(copyFrom.height);

    if (copyFrom.name != null)
      name = new String(copyFrom.name);
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public MediaElemTypeEnum getType() {
    return type;
  }

  public void setType(MediaElemTypeEnum type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getWidth() {
    return width;
  }

  public void setWidth(String width) {
    this.width = width;
  }

  public String getHeight() {
    return height;
  }

  public void setHeight(String height) {
    this.height = height;
  }

  public String getExtension() {
    return path.substring(path.lastIndexOf('.') + 1).toLowerCase();
  }

  public String getWidthType() {
    if (width != null) {
      try {
        String devolver = "" + width.charAt(width.length() - 1);
        return devolver;
      } catch (Exception e) {
        return "";
      }
    }
    return "";
  }

  public String getHeightType() {
    if (height != null) {
      try {
        String devolver = "" + height.charAt(height.length() - 1);
        return devolver;
      } catch (Exception e) {
        return "";
      }
    }
    return "";
  }

  public GeogebraTypeEnum getGeogebraType() {
    return geogebraType;
  }

  public void setGeogebraType(GeogebraTypeEnum geogebraType) {
    this.geogebraType = geogebraType;
  }



}
