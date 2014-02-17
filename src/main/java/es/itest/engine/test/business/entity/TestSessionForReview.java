package es.itest.engine.test.business.entity;

/** Clase para modelar la relacion entre un alumno y un examen realizado por el
 * Unicamente contendra el ID del alumno y el ID de un examen realizado por el mismo
 * @author Gonzalo Alonso Gonzalez
 *
 */
public class TestSessionForReview {
	private Long idExam;
	private Long idLearner;
	private Double preGrade;
	private Double postGrade;	
	
	public Double getPreGrade() {
		return this.preGrade;
	}
	public void setPreGrade(Double preGrade) {
		this.preGrade = preGrade;
	}
	public Double getPostGrade() {
		return this.postGrade;
	}
	public void setPostGrade(Double postGrade) {
		this.postGrade = postGrade;
	}
	public Long getIdExam() {
		return this.idExam;
	}
	public void setIdExam(Long idExam) {
		this.idExam = idExam;
	}
	public Long getIdLearner() {
		return this.idLearner;
	}
	public void setIdLearner(Long idLearner) {
		this.idLearner = idLearner;
	}
}
