/**
 * 
 */
package fr.argus.socle.model;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mamadou.dansoko
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariableContenu {
	private String retour;
	private String codeSignalement;
	private String typeSignalement;
	private String libelleSignalement;
	private String destination;
	private Vector<String> listAdresseWebServices;
	private Vector<String> listeFichiers;
	private HashMap<String, String> parametres;
	private Integer timeout;
	private Integer numeroPublication;	
	private StringBuffer contenuRetourTraitement;
	private String modeEnvoi;
	private List<Ticket> listeTicketsFilsSousLots;		
	private String idPere;
	private String idOcr;
	private String nomAttribut;
	private String valeurAttribut;
	private int quantiteAttribut;
		
}
