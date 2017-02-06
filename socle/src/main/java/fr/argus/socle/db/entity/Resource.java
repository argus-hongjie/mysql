/**
 * 
 */
package fr.argus.socle.db.entity;

import java.sql.Timestamp;

/**
 * 
 * Cette classe représente la table resource dans la base referentiel.
 * 
 * © @author Mongi MIRAOUI 29 avr. 2016
 */
public class Resource {

	/**
	 * L'identifiant de la table.
	 */
	private Integer id;

	/**
	 * Nom de la ressource (utilise comme clef dans les requetes).
	 */
	private String nom;

	/**
	 * Contenu de la ressource.
	 */
	private byte[] contenu;

	/**
	 * date et heure de creation/mise a jour de la ressource.
	 */
	private Timestamp date;

	/**
	 * Identifiant du type_module associé à la ressource
	 */
	private Integer id_type_module;
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param nom
	 *            the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * @return the contenu
	 */
	public byte[] getContenu() {
		return contenu;
	}

	/**
	 * @param contenu
	 *            the contenu to set
	 */
	public void setContenu(byte[] contenu) {
		this.contenu = contenu;
	}

	/**
	 * @return the date
	 */
	public Timestamp getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Timestamp date) {
		this.date = date;
	}

	Override newInstance() {
		return null;

	}

	public Integer getIdTypeModule() {
		return id_type_module;
	}

	public void setIdTypeModule(Integer id_type_module) {
		this.id_type_module = id_type_module;
	}

}
