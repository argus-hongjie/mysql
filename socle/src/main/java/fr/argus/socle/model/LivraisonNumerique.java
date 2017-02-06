/**
 * 
 */
package fr.argus.socle.model;

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
public class LivraisonNumerique {
	private Integer idLivraison;
	private Reception  reception;
	private ReceptionContent  receptionContent;
	private Integer idSourceAppro;
	private String nomSecondaire;
	private String cleIndexPivot;
	private String dateCreation;
	private String cheminLivraisonHash;	
	private String cheminLivaraisonArchive;
	private Integer idContenuLivraisonNumeriuque;
	private Integer idLivraisonOrigine;
	private String idOcr;
	private String fournisseur;
	private String nomLotPere;
}
