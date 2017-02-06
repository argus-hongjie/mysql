package fr.argus.socle.db.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.argus.socle.db.entity.Signalement;
import fr.argus.socle.db.mapper.service.ResultSetRowMapper;

/**
 * © @author Mongi MIRAOUI 2 mai 2016
 */
public class SignalementRowMapper implements ResultSetRowMapper<Signalement> {

	private static final Logger logger = LogManager.getLogger(SignalementRowMapper.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.argus.socle.db.mapper.ResultSetRowMapper#mapRow(java.sql.ResultSet)
	 */
	public Signalement mapRow(ResultSet rs) {
		Signalement signalement = new Signalement();
		try {
			signalement.setId(rs.getInt("id"));
			signalement.setCode(rs.getString("code"));
			signalement.setLibelle(rs.getString("libelle"));
			signalement.setDescription(rs.getString("description"));
			signalement.setType(rs.getString("type"));
			signalement.setDate(rs.getTimestamp("date"));

		} catch (SQLException e) {
			logger.error("Erreur de mapping de resultset vers l'objet Signalement : " + e.getMessage());
		} catch (Throwable e) {
			logger.error("Erreur de mapping de resultset vers l'objet Signalement : " + e.getMessage());
		}

		return signalement;
	}

}
