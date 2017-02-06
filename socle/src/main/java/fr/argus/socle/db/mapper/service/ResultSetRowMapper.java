package fr.argus.socle.db.mapper.service;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * © @author Mongi MIRAOUI 2 mai 2016
 * 
 * @param <T>
 */
public interface ResultSetRowMapper<T> {

	/**
	 * Les classes qui implémentent cette interface doivent implémenter cette
	 * méthode pour mapper chaque ligne de données dans le ResultSet . Cette
	 * méthode ne devrait pas appeler next () sur le ResultSet ; il est
	 * seulement censé mapper les valeurs de la ligne courante .
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	T mapRow(ResultSet rs) throws SQLException;

}
