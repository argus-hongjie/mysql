/**
 * 
 */
package fr.argus.socle.db.entity;

import java.sql.Timestamp;

/**
 * Cette classe représente la table signalement dans la base referentiel.
 * 
 * © @author Mongi MIRAOUI 29 avr. 2016
 */
public class Signalement {

	/**
	 * Identifiant unique pour la table.
	 */
	private Integer id;

	/**
	 * Code du signalement qui correspond a une erreure, une alerte ou un log.
	 */
	private String code;

	/**
	 * Libelle de l'erreur, alerte ou log.
	 */
	private String libelle;

	/**
	 * Description du signalement.
	 */
	private String description;

	/**
	 * Le type de signalement.
	 */
	private String type;

	/**
	 * Date de creation ou de modification de l'erreur, de l'alerte ou de log.
	 */
	private Timestamp date;

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
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the libelle
	 */
	public String getLibelle() {
		return libelle;
	}

	/**
	 * @param libelle
	 *            the libelle to set
	 */
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
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

}
