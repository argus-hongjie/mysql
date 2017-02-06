package fr.argus.socle.db.entity;

import java.sql.Timestamp;

/**
 * Cette classe représente la table type_module dans la base referentiel.
 * 
 * © @author Mongi MIRAOUI 29 avr. 2016
 */
public class TypeModule {

	/**
	 * L'identifiant de la table.
	 */
	private Integer id;

	/**
	 * Nom du module (utilise comme clef dans les requetes).
	 */
	private String nom;

	/**
	 * Libelle du module (utilise pour l'affichage et les logs).
	 */
	private String libelle;

	/**
	 * Description du module.
	 */
	private String description;

	/**
	 * date et heure de creation de la ligne.
	 */
	private Timestamp date;

	/**
	 * identifiant de la ressource.
	 */
	private Integer idScriptEntree;

	/**
	 * identifiant de la ressource.
	 */
	private Integer idScriptSortie;

	/**
	 * Identifiant de la ressource correspondant au type de module suivant
	 * utilise pour les appels webservices post traitement (valeur par defaut).
	 */
	private Integer idTypeModuleSuivant;

	/**
	 * Identifiant de la ressource d'entree pour l'auto test.
	 */
	private Integer idResourceAutoTestEntree;

	/**
	 * Identifiant de la ressource de sortie pour l'auto test.
	 */
	private Integer idResourceAutoTestSortie;

	/**
	 * Si le status est à false le moduule n'execute pas sont traitement et le
	 * tread deticket n'est pas en service.
	 */
	private Boolean actif;

	/**
	 * Parametres d'execution necessaires au module sous forme XML casse
	 * insensible.
	 */
	private Parametres parametres;

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

	/**
	 * @return the idScriptEntree
	 */
	public Integer getIdScriptEntree() {
		return idScriptEntree;
	}

	/**
	 * @param idScriptEntree
	 *            the idScriptEntree to set
	 */
	public void setIdScriptEntree(Integer idScriptEntree) {
		this.idScriptEntree = idScriptEntree;
	}

	/**
	 * @return the idScriptSortie
	 */
	public Integer getIdScriptSortie() {
		return idScriptSortie;
	}

	/**
	 * @param idScriptSortie
	 *            the idScriptSortie to set
	 */
	public void setIdScriptSortie(Integer idScriptSortie) {
		this.idScriptSortie = idScriptSortie;
	}

	/**
	 * @return the idTypeModuleSuivant
	 */
	public Integer getIdTypeModuleSuivant() {
		return idTypeModuleSuivant;
	}

	/**
	 * @param idTypeModuleSuivant
	 *            the idTypeModuleSuivant to set
	 */
	public void setIdTypeModuleSuivant(Integer idTypeModuleSuivant) {
		this.idTypeModuleSuivant = idTypeModuleSuivant;
	}

	/**
	 * @return the idResourceAutoTestEntree
	 */
	public Integer getIdResourceAutoTestEntree() {
		return idResourceAutoTestEntree;
	}

	/**
	 * @param idResourceAutoTestEntree
	 *            the idResourceAutoTestEntree to set
	 */
	public void setIdResourceAutoTestEntree(Integer idResourceAutoTestEntree) {
		this.idResourceAutoTestEntree = idResourceAutoTestEntree;
	}

	/**
	 * @return the idResourceAutoTestSortie
	 */
	public Integer getIdResourceAutoTestSortie() {
		return idResourceAutoTestSortie;
	}

	/**
	 * @param idResourceAutoTestSortie
	 *            the idResourceAutoTestSortie to set
	 */
	public void setIdResourceAutoTestSortie(Integer idResourceAutoTestSortie) {
		this.idResourceAutoTestSortie = idResourceAutoTestSortie;
	}

	/**
	 * @return the actif
	 */
	public Boolean getActif() {
		return actif;
	}

	/**
	 * @param actif
	 *            the actif to set
	 */
	public void setActif(Boolean actif) {
		this.actif = actif;
	}

	/**
	 * @return the parametres
	 */
	public Parametres getParametres() {
		return parametres;
	}

	/**
	 * @param parametres
	 *            the parametres to set
	 */
	public void setParametres(Parametres parametres) {
		this.parametres = parametres;
	}

}
