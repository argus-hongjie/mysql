/**
 * 
 */
package fr.argus.socle.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mamadou.dansoko
 *
 */
public class SousLots {
	private int numeroGroupe;
	private List<String> listeFichiers;
	/**
	 * @return the numeroGroupe
	 */
	public int getNumeroGroupe() {
		return numeroGroupe;
	}
	/**
	 * @param numeroGroupe the numeroGroupe to set
	 */
	public void setNumeroGroupe(int numeroGroupe) {
		this.numeroGroupe = numeroGroupe;
	}
	/**
	 * @return the listeFichiers
	 */
	public List<String> getListeFichiers() {
		if(listeFichiers == null){
			listeFichiers = new ArrayList<String>();
		}
		return listeFichiers;
	}
	/**
	 * @param listeFichiers the listeFichiers to set
	 */
	public void setListeFichiers(List<String> listeFichiers) {
		this.listeFichiers = listeFichiers;
	}
	
}
