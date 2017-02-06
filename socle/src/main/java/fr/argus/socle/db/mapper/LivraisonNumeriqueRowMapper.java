/**
 * 
 */
package fr.argus.socle.db.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.argus.socle.db.mapper.service.ResultSetRowMapper;
import fr.argus.socle.model.LivraisonNumerique;
import fr.argus.socle.model.Reception;
import fr.argus.socle.util.Constant;
import fr.argus.socle.util.Helper;

/**
 * @author mamadou.dansoko
 *
 */
public class LivraisonNumeriqueRowMapper implements ResultSetRowMapper<LivraisonNumerique> {
	private static final Logger logger = LogManager.getLogger(LivraisonNumeriqueRowMapper.class);
	/* (non-Javadoc)
	 * @see fr.argus.socle.db.mapper.service.ResultSetRowMapper#mapRow(java.sql.ResultSet)
	 */
	@Override
	public LivraisonNumerique mapRow(ResultSet rs) throws SQLException {
		LivraisonNumerique livraison = new LivraisonNumerique();
		try {
			if(rs.next()){
				livraison.setIdSourceAppro(rs.getInt("id_source_appro"));
				SQLXML contenuXML = rs.getSQLXML("contenu_pivot");
				Reception reception = (Reception) Helper.unmarshallFromString(Reception.class,contenuXML.getString());
				livraison.setReception(reception);
				livraison.setCleIndexPivot(rs.getString("clef_index_pivot"));
				livraison.setDateCreation(Helper.convertDateToString(rs.getDate("date_creation"),Constant.FORMAT_YYYY_MM_DD_HH_MM_SS));
				livraison.setCheminLivraisonHash(reception.getCheminLivraisonHash());	
				livraison.setIdLivraison(rs.getInt("id"));
				livraison.setIdLivraisonOrigine(rs.getInt("id_pere"));
			}
		} catch (SQLException e) {
			logger.error("Erreur de mapping de resultset vers l'objet Resource : " + e.getMessage());
		}

		catch (Throwable e) {
			logger.error("Erreur de mapping de resultset vers l'objet Resource : " + e.getMessage());
		}

		return livraison;
	}
	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public LivraisonNumerique mapRowCoverPage(ResultSet rs) throws SQLException {
		LivraisonNumerique livraison = new LivraisonNumerique();
		try {
			if(rs.next()){
				livraison.setIdSourceAppro(rs.getInt("id_source_appro"));

				SQLXML contenuXML = rs.getSQLXML("contenu_pivot");
				Reception reception = (Reception) Helper.unmarshallFromString(Reception.class,contenuXML.getString());
				livraison.setReception(reception);				
				livraison.setCheminLivraisonHash(reception.getCheminLivraisonHash());	
				livraison.setIdLivraison(rs.getInt("id"));
			}
		} catch (SQLException e) {
			logger.error("Erreur de mapping de resultset vers l'objet Resource : " + e.getMessage());
		}

		catch (Throwable e) {
			logger.error("Erreur de mapping de resultset vers l'objet Resource : " + e.getMessage());
		}

		return livraison;
	}
	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public LivraisonNumerique mapRowContenuPivot(ResultSet rs) throws SQLException {
		LivraisonNumerique livraison = new LivraisonNumerique();
		try {
			if(rs.next()){				
				SQLXML contenuXML = rs.getSQLXML("contenu_pivot");
				Reception reception = (Reception) Helper.unmarshallFromString(Reception.class,contenuXML.getString());
				livraison.setReception(reception);							
			}
		} catch (SQLException e) {
			logger.error("Erreur de mapping de resultset vers l'objet Resource : " + e.getMessage());
		}

		catch (Throwable e) {
			logger.error("Erreur de mapping de resultset vers l'objet Resource : " + e.getMessage());
		}

		return livraison;
	}
	/**
	 * Retrieves all sous_lots of lot_pere
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public List<Reception> mapRowContenuPivotSousLots(ResultSet rs) throws SQLException {
		List<Reception> souslots = new ArrayList<Reception>();
		try {
			while(rs.next()){				
				SQLXML contenuXML = rs.getSQLXML("contenu_pivot");
				Reception reception = (Reception) Helper.unmarshallFromString(Reception.class,contenuXML.getString());
				reception.setId(rs.getInt("id"));
				souslots.add(reception);							
			}
		} catch (SQLException e) {
			logger.error("Erreur de mapping de resultset vers l'objet Resource : " + e.getMessage());
		}

		catch (Throwable e) {
			logger.error("Erreur de mapping de resultset vers l'objet Resource : " + e.getMessage());
		}

		return souslots;
	}

}
