package es.itest.engine.test.business.entity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import com.cesfelipesegundo.itis.model.MediaElemComparator;

/**
 * Represents an {@link Item} response.
 */
@Embeddable
public class ItemResponse {
	
	private Long id;					// Id de la respuesta
	
	private String text;				// Texto de la respuesta
	
	private List<MediaElem> mmedia;		// Lista de elementos multimedia ordenador por su campo de orden

	private boolean active;

	private boolean solution;
	
	/**
	 * Weight of the response in the grade
	 */
	@Transient
	private BigDecimal value;
	
	private Item question;
	
	private boolean usedInExam;
	
	public ItemResponse(){
	  value = BigDecimal.ZERO;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isSolution() {
		return solution;
	}

	public void setSolution(boolean solution) {
		this.solution = solution;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<MediaElem> getMmedia() {
		// Orders the list:
		if (mmedia != null) Collections.sort(mmedia,new MediaElemComparator());
		return mmedia;
	}
	public void setMmedia(List<MediaElem> mmedia) {
		this.mmedia = mmedia;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public void setQuestion(Item question) {
		this.question = question;
	}
	public Item getQuestion(){
		return this.question;
	}

	public Boolean getUsedInExam() {
		return usedInExam;
	}

	public void setUsedInExam(Boolean usedInExam) {
		this.usedInExam = usedInExam;
	}
	
	//	 Return the text of the answer splitted into paragraphs
	public String[] getTextParagraphs() {
		if(text!=null)
			return (text.trim()).split("\n");
		else
			return null;
	}

	
}
