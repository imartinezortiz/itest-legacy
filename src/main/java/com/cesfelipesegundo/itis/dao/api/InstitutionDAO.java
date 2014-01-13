package com.cesfelipesegundo.itis.dao.api;

import java.util.List;
import java.util.Map;

import com.cesfelipesegundo.itis.model.Group;
import com.cesfelipesegundo.itis.model.Institution;
import com.cesfelipesegundo.itis.model.InstitutionStats;
import com.cesfelipesegundo.itis.model.InstitutionStudies;
import com.cesfelipesegundo.itis.model.Subject;
import com.cesfelipesegundo.itis.model.User;

/**
 * Interfaz DAO para el modelo <code>Institution</code>.
 * @author Alberto Díaz
 *
 */
public interface InstitutionDAO extends DAO {

	
	/**
	 * Returns the list of institutions
	 * @return list of institutions
	 */
	
	public List<Institution> getInstitutions();
	
	/**
	 * Returns the information of a institution
	 * 
	 * <p>Returns the information stored in the institutions table</p>
	 * 
	 * @param id Identifier of the institution
	 * 
	 * @return Institution
	 */
	public Institution getInstitution(Long id);
	
	/**
	 * Deletes a Institution from the database
	 * @param institution
	 */
	public void deleteInstitution(Institution institution);

	
	/**
	 * Inserts or updates a Institution data into the database
	 * @param institution
	 * @param studies
	 * @return Institution object with the id filled
	 */
	public Institution saveInstitution(Institution institution, InstitutionStudies studies);

	/**
	 * Return the stats from the current course
	 * @return
	 * */
	public InstitutionStats getInstitutionStats(long idInstitution,
			String year, long idCourse);

	/**
	 * Returns the list of studies of an specific institution
	 * @param idInstitution
	 * @return list of studies
	 */
	public InstitutionStudies getInstitutionStudies(long idInstitution);

	/**
	 * Returns the list Institutions filter by the map
	 * @param map
	 * @return list of institutions
	 */
	public List<Institution> getInstitutionsFiltered(Map<String, Object> map);

	/**
	 * Return a list with all kind of certifications
	 * @return List<String>
	 * */
	public List<String> getAllCertifications();

	/**
	 * Returns a list width institutions with at least one public question
	 * @return list of institutions with at least one public question
	 * */
	public List<Institution> getInstitutionsWidthPublicQuestions();

	/**
	 * Returns the institution of the given user's id
	 * @param id user's id
	 * */
	public Institution getInstitutionByUserId(Long id);
	
}
