
package fr.argus.socle.db.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.argus.socle.db.entity.Resource;
import fr.argus.socle.db.mapper.service.ResultSetRowMapper;

/**
 * © @author Mongi MIRAOUI 2 mai 2016
 */
public class ResourceRowMapper implements ResultSetRowMapper<Resource> {

	private static final Logger logger = LogManager.getLogger(ResourceRowMapper.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.argus.socle.db.mapper.ResultSetRowMapper#mapRow(java.sql.ResultSet)
	 */
	public Resource mapRow(ResultSet rs) {
		Resource resource = new Resource();
		try {
			if(rs.next()){
				resource.setId(rs.getInt("id"));
				resource.setNom(rs.getString("nom"));
				resource.setDate(rs.getTimestamp("date"));
				resource.setContenu(rs.getBytes("contenu"));
				resource.setIdTypeModule(rs.getInt("id_type_module"));
			}
		} catch (SQLException e) {
			logger.error("Erreur de mapping de resultset vers l'objet Resource : " + e.getMessage());
		}

		catch (Throwable e) {
			logger.error("Erreur de mapping de resultset vers l'objet Resource : " + e.getMessage());
		}

		return resource;
	}
	
	public List<Resource> mapListRow(ResultSet rs){
		List<Resource> list = new ArrayList<Resource>();
		
		try{
			while(rs != null && rs.next()){
				Resource resource = new Resource();
				resource.setId(rs.getInt("id"));
				resource.setNom(rs.getString("nom"));
				resource.setContenu(rs.getBytes("contenu"));
				resource.setDate(rs.getTimestamp("date"));
				resource.setIdTypeModule(rs.getInt("id_type_module"));
				list.add(resource);
			}
		}catch(Exception e){
			logger.error("Erreur de récupération des ressources ",Arrays.toString(e.getStackTrace()) );
		}
		return list;
		
	}

}
