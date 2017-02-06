package fr.argus.socle.db.entity;

import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import lombok.Builder;
import lombok.Value;

/**
 * Cette classe représente la table modalite_appro dans la base referentiel.
 * 
 * © @author hongjie.zhang 06/12/2016
 */
@Value
@Builder
public class ModaliteAppro {
	Integer id;
	Integer idSource;
	Integer idGps;
	Integer idRds;
	String 	libelleSource;
	String 	libelleModalite;
	String 	masqueFichier;
	String 	prioritePanel;
	Integer rangPanel;
	String 	typeContenuUtilisateur;
	Boolean pointageAuto;
	Boolean dateFiable;
	Boolean generationLienHypertexte;
	Boolean generationVignette;
	Integer delaiConservationDesArchives;
	Integer delaiConservationDonneesDeTraitement;
	String 	typeModalite;
	Boolean active;
	Boolean traitement;
	String 	motDePassePdf;
	Timestamp dhMaj;
	
	//import org.springframework.util.ReflectionUtils;
	//ReflectionTestUtils.setField(fetcher, "maxMonth", 12);
	public static RowMapper<ModaliteAppro> rowMapper = (rs, rowNum) -> ModaliteAppro.builder()
			.idGps(rs.getInt("id_gps"))
			.libelleSource(rs.getString("libelle_source"))
			.dateFiable(rs.getBoolean("date_fiable"))
			.id(rs.getInt("id"))
			.build();
}