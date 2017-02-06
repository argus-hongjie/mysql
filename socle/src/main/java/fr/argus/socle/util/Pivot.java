/**
 * 
 */
package fr.argus.socle.util;

import static fr.argus.socle.util.Constant.FICHIER_INFOS_ZIP;
import static fr.argus.socle.util.Helper.defaultIfNullOrException;
import static java.nio.file.Paths.get;
import static org.apache.commons.lang3.ArrayUtils.contains;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.function.Predicate;

import fr.argus.socle.db.DBClient;
import fr.argus.socle.model.Fichier;
import fr.argus.socle.model.Fichiers;
import fr.argus.socle.model.Livraison;
import fr.argus.socle.model.LivraisonNumerique;
import fr.argus.socle.model.Reception;

/**
 * @author mamadou.dansoko
 *
 */
public class Pivot {
	/**
	 * Generate Pivot format XML wrapper
	 * @param idLivraisonNumerique
	 * @param dBClient
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws SQLException
	 */
	public static Livraison createXmlFormatPivot(Integer idLivraisonNumerique,DBClient dBClient) throws IOException, URISyntaxException, SQLException {
		LivraisonNumerique livraisonNumerique = dBClient.getLivraisonNumerique(idLivraisonNumerique);
		Fichiers fichiers = dBClient.getReceptionContent(idLivraisonNumerique).getFICHIERS();
		Predicate<Fichier> predicate = fichier->contains(FICHIER_INFOS_ZIP, get(fichier.getNom()).getFileName().toString().toLowerCase());
		Integer idLotPere = livraisonNumerique.getIdLivraisonOrigine();
		if (fichiers.getFICHIER().stream().noneMatch(predicate)) {
			if (idLotPere != null && idLotPere >= 0) dBClient.getReceptionContent(idLotPere).getFICHIERS().getFICHIER().stream().filter(predicate).findFirst().ifPresent(fichierInfo->fichiers.getFICHIER().add(fichierInfo));
		}
		Reception reception = livraisonNumerique.getReception();
		fichiers.getFICHIER().stream().forEach(fichier->{
			fichier.setNom(get(fichier.getNom()).getFileName().toString());
			if (defaultIfNullOrException(null, ()->fichier.getCONTENU().getValue())==null) fichier.setCONTENU(null);			
		});
		reception.setFILES(null);
		reception.setAttributs(null);
		Livraison livraison = Livraison.builder()
				.idLivraisonNumerique(idLivraisonNumerique)
				.idSourceAppro(livraisonNumerique.getIdSourceAppro())
				.idPere(null)
				.bpIndex(reception.getBpIndex())
				.clefIndexPivot(livraisonNumerique.getCleIndexPivot())
				.reception(reception)
				.fichiers(fichiers)
				.attributs(dBClient.getAttributs(idLivraisonNumerique)).build();
		return livraison;
	}
}
