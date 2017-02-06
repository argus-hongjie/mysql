package fr.argus.socle.db.mapper;

import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.bind.JAXB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.argus.socle.db.entity.Parametres;
import fr.argus.socle.db.entity.TypeModule;
import fr.argus.socle.db.mapper.service.ResultSetRowMapper;

/**
 * © @author Mongi MIRAOUI 2 mai 2016
 */
public class TypeModuleRowMapper implements ResultSetRowMapper<TypeModule> {

	private static final Logger logger = LogManager.getLogger(TypeModuleRowMapper.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.argus.socle.db.mapper.ResultSetRowMapper#mapRow(java.sql.ResultSet )
	 */
	public TypeModule mapRow(ResultSet rs) {
		TypeModule typeModule = new TypeModule();

		try {

			typeModule.setId(rs.getInt("id"));
			typeModule.setNom(rs.getString("nom"));
			typeModule.setLibelle(rs.getString("libelle"));
			typeModule.setDescription(rs.getString("description"));
			typeModule.setDate(rs.getTimestamp("date"));
			typeModule.setIdScriptEntree(rs.getInt("id_script_entree"));
			typeModule.setIdScriptSortie(rs.getInt("id_script_sortie"));
			typeModule.setIdTypeModuleSuivant(rs.getInt("id_type_module_suivant"));
			typeModule.setIdResourceAutoTestEntree(rs.getInt("id_ressource_autotest_entree"));
			typeModule.setIdResourceAutoTestSortie(rs.getInt("id_ressource_autotest_sortie"));
			typeModule.setActif(rs.getBoolean("actif"));

			if (rs.getString("parametres") != null && !rs.getString("parametres").isEmpty()) {
				Parametres parametres = JAXB.unmarshal(new StringReader(rs.getString("parametres")), Parametres.class);
				typeModule.setParametres(parametres);
			}

		} catch (SQLException e) {
			logger.error("Erreur de mapping de resultset vers l'objet TypeModule : " + e.getMessage());
		}

		catch (Throwable e) {
			logger.error("Erreur de mapping de resultset vers l'objet TypeModule : " + e.getMessage());
		}

		return typeModule;

	}

}
