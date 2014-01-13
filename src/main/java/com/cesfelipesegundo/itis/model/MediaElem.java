package com.cesfelipesegundo.itis.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cesfelipesegundo.itis.web.controller.TutorQuestionManagementController;

/**
 * Representa cada uno de los elementos multimedia asociados a preguntas o respuestas
 * @author J. M. Colmenar
 *
 */
public class MediaElem {
	
	private static final Log log = LogFactory.getLog(TutorQuestionManagementController.class);

	private Long id;
	private String path;	// Ruta al elemento
	private int type;		// Tipo de elemento: Flash, Imagen, Sonido
	private int order;		// Orden para ser mostrado
	private String width;	// Ancho del elemento - B.D. V3.5
	private String height;	// Alto del elemento - B.D. V3.5
	private int geogebraType;
	private String name;
	
	/**
	 * Constantes para los tipos de contenido:
	 */
	public static final int FLASH = 1;
	public static final int IMAGE = 2;
	public static final int SOUND = 3;
	public static final int GEOGEBRA = 4;
	public static final int JAVAAPPLET = 5;
	public static final int SIBELIUS = 6;
	public static final int YOUTUBE = 7;
	
	
	public MediaElem(){
		
	}
	/** Constructor copia
	 * Copia la informacion sobre un elemento almacenado.
	 * ATENCION: NO duplica el fichero del elemento multimedia
	 * @param copyFrom : Objeto MediaElem desde el que se copia la informacion
	 */
	public MediaElem (MediaElem copyFrom){
		id = copyFrom.id;
		
		//Uso del constructor copia de String para evitar posibles problemas con las referencias
		if (copyFrom.path != null) path = new String (copyFrom.path);
		
		type = copyFrom.type;
		order = copyFrom.order;
		geogebraType = copyFrom.geogebraType;
		if (copyFrom.width != null) width = new String (copyFrom.width);
		
		if (copyFrom.height != null) height = new String (copyFrom.height);
		
		if (copyFrom.name != null) name = new String (copyFrom.name);
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
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
		return path.substring(path.lastIndexOf('.')+1).toLowerCase();
	}
	public String getWidthType(){
		if(width!=null){
			try{
				String devolver = ""+width.charAt(width.length()-1);
				return devolver;
			}catch(Exception e){
				return "";
			}
		}
		return "";
	}
	public String getHeightType(){
		if(height!=null){
			try{
				String devolver = ""+height.charAt(height.length()-1);
				return devolver;
			}catch(Exception e){
				return "";
			}
		}
		return "";
	}
	public int getGeogebraType() {
		return geogebraType;
	}
	public void setGeogebraType(int geogebraType) {
		this.geogebraType = geogebraType;
	}
	
	
	
}
