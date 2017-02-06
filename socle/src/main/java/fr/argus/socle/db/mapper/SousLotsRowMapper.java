/**
 * 
 */
package fr.argus.socle.db.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.argus.socle.db.mapper.service.ResultSetRowMapper;
import fr.argus.socle.util.Filename;

/**
 * @author mamadou.dansoko
 *
 */
public class SousLotsRowMapper implements ResultSetRowMapper<LinkedHashMap<Integer, List<String>>> {
	private static final Logger logger = LogManager.getLogger(SousLotsRowMapper.class);
	/* (non-Javadoc)
	 * @see fr.argus.socle.db.mapper.service.ResultSetRowMapper#mapRow(java.sql.ResultSet)
	 */
	@Override
	public LinkedHashMap<Integer, List<String>> mapRow(ResultSet rs) throws SQLException {
		
		LinkedHashMap<Integer, List<String>> groupMap =  new LinkedHashMap <Integer,List<String>>();
		try {
			while(rs != null && rs.next()){
				int numGroup = rs.getInt("num_group");
				String fichier = rs.getString("fichier");
				Filename infos = new Filename(fichier, java.io.File.separatorChar, '.');
				if(groupMap.containsKey(numGroup)){
					groupMap.get(numGroup).add(infos.fullFilename());
				}else{
					List<String> sousLots = new ArrayList<String>();
					sousLots.add(infos.fullFilename());
					groupMap.put(numGroup, sousLots);
				}
			}
		} catch (SQLException e) {
			logger.error("Erreur de mapping de resultset vers l'objet Resource : " + e.getMessage());
		}

		catch (Throwable e) {
			logger.error("Erreur de mapping de resultset vers l'objet Resource : " + e.getMessage());
		}

		return groupMap;
	}

}
