package fr.argus.socle.db.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * © @author Mongi MIRAOUI 10 mai 2016
 */
@XmlRootElement
public class Parametres {

	private String nomModule;

	private String ipPortWS;
	
	private Integer portSupervision;

	private String banetteEntree;

	private String banetteSortie;
	
	private Integer durationSupervisionSeconds;
	
	private Integer scrutationBanetteDelaySeconds;
	
	private Integer scrutationPileDelaySeconds;
	
	private Integer durationProcessTicketMinutes;
	
	private Integer autotestDelaySeconds;
	
	private Integer nbTryToStop;
	
	private String mailSmtpHost;
	
	private String supportMailTo;
	
	private String supportMailFrom;
	
	private String supportMailSubject;
	
	private Integer nombreThreadsMax;
	
	

	/**
	 * @return the nomModule
	 */
	public String getNomModule() {
		return nomModule;
	}

	/**
	 * @param nomModule
	 *            the nomModule to set
	 */
	@XmlElement(name = "nom_module")
	public void setNomModule(String nomModule) {
		this.nomModule = nomModule;
	}

	/**
	 * @return the ipPortWS
	 */
	public String getIpPortWS() {
		return ipPortWS;
	}

	/**
	 * @param ipPortWS
	 *            the ipPortWS to set
	 */
	@XmlElement(name = "ip_port_ws")
	public void setIpPortWS(String ipPortWS) {
		this.ipPortWS = ipPortWS;
	}
	
	/**
	 * @return the portSupervision
	 */
	public Integer getPortSupervision() {
		return portSupervision;
	}

	/**
	 * @param portSupervision the portSupervision to set
	 */
	@XmlElement(name = "port_supervision")
	public void setPortSupervision(Integer portSupervision) {
		this.portSupervision = portSupervision;
	}


	/**
	 * @return the banetteEntree
	 */
	public String getBanetteEntree() {
		return banetteEntree;
	}

	/**
	 * @param banetteEntree
	 *            the banetteEntree to set
	 */
	@XmlElement(name = "banette_entree")
	public void setBanetteEntree(String banetteEntree) {
		this.banetteEntree = banetteEntree;
	}

	/**
	 * @return the banetteSortie
	 */
	public String getBanetteSortie() {
		return banetteSortie;
	}

	/**
	 * @param banetteSortie
	 *            the banetteSortie to set
	 */
	@XmlElement(name = "banette_sortie")
	public void setBanetteSortie(String banetteSortie) {
		this.banetteSortie = banetteSortie;
	}

	/**
	 * @return the durationSupervisionSeconds
	 */
	public Integer getDurationSupervisionSeconds() {
		return durationSupervisionSeconds;
	}

	/**
	 * @param durationSupervisionSeconds the durationSupervisionSeconds to set
	 */
	@XmlElement(name = "duration_supervision_seconds")
	public void setDurationSupervisionSeconds(Integer durationSupervisionSeconds) {
		this.durationSupervisionSeconds = durationSupervisionSeconds;
	}

	/**
	 * @return the scrutationBanetteDelaySeconds
	 */
	public Integer getScrutationBanetteDelaySeconds() {
		return scrutationBanetteDelaySeconds;
	}

	/**
	 * @param scrutationBanetteDelaySeconds the scrutationBanetteDelaySeconds to set
	 */
	@XmlElement(name = "scrutation_banette_delay_seconds")
	public void setScrutationBanetteDelaySeconds(Integer scrutationBanetteDelaySeconds) {
		this.scrutationBanetteDelaySeconds = scrutationBanetteDelaySeconds;
	}

	/**
	 * @return the scrutationPileDelaySeconds
	 */
	public Integer getScrutationPileDelaySeconds() {
		return scrutationPileDelaySeconds;
	}

	/**
	 * @param scrutationPileDelaySeconds the scrutationPileDelaySeconds to set
	 */
	@XmlElement(name = "scrutation_pile_delay_seconds")
	public void setScrutationPileDelaySeconds(Integer scrutationPileDelaySeconds) {
		this.scrutationPileDelaySeconds = scrutationPileDelaySeconds;
	}

	/**
	 * @return the autotestDelaySeconds
	 */
	public Integer getAutotestDelaySeconds() {
		return autotestDelaySeconds;
	}

	/**
	 * @param autotestDelaySeconds the autotestDelaySeconds to set
	 */
	@XmlElement(name = "autotest_delay_seconds")
	public void setAutotestDelaySeconds(Integer autotestDelaySeconds) {
		this.autotestDelaySeconds = autotestDelaySeconds;
	}
	
	
	

	/**
	 * @return the durationProcessTicketMinutes
	 */
	public Integer getDurationProcessTicketMinutes() {
		return durationProcessTicketMinutes;
	}

	/**
	 * @param durationProcessTicketMinutes the durationProcessTicketMinutes to set
	 */
	@XmlElement(name = "duration_process_ticket_minutes")
	public void setDurationProcessTicketMinutes(Integer durationProcessTicketMinutes) {
		this.durationProcessTicketMinutes = durationProcessTicketMinutes;
	}

	/**
	 * @return the nbTryToStop
	 */
	public Integer getNbTryToStop() {
		return nbTryToStop;
	}

	/**
	 * @param nbTryToStop the nbTryToStop to set
	 */
	@XmlElement(name = "nbtry_to_stop")
	public void setNbTryToStop(Integer nbTryToStop) {
		this.nbTryToStop = nbTryToStop;
	}

	/**
	 * @return the mailSmtpHost
	 */
	public String getMailSmtpHost() {
		return mailSmtpHost;
	}

	/**
	 * @param mailSmtpHost the mailSmtpHost to set
	 */
	@XmlElement(name = "mail_smtp_host")
	public void setMailSmtpHost(String mailSmtpHost) {
		this.mailSmtpHost = mailSmtpHost;
	}

	/**
	 * @return the supportMailTo
	 */
	public String getSupportMailTo() {
		return supportMailTo;
	}

	/**
	 * @param supportMailTo the supportMailTo to set
	 */
	@XmlElement(name = "support_mail_to_address")
	public void setSupportMailTo(String supportMailTo) {
		this.supportMailTo = supportMailTo;
	}

	/**
	 * @return the supportMailFrom
	 */
	public String getSupportMailFrom() {
		return supportMailFrom;
	}

	/**
	 * @param supportMailFrom the supportMailFrom to set
	 */
	@XmlElement(name = "support_mail_from_address")
	public void setSupportMailFrom(String supportMailFrom) {
		this.supportMailFrom = supportMailFrom;
	}

	/**
	 * @return the supportMailSubject
	 */
	public String getSupportMailSubject() {
		return supportMailSubject;
	}

	/**
	 * @param supportMailSubject the supportMailSubject to set
	 */
	@XmlElement(name = "support_mail_subject")
	public void setSupportMailSubject(String supportMailSubject) {
		this.supportMailSubject = supportMailSubject;
	}

	/**
	 * @return
	 */
	public int getPortWS() {
		try {			
			int portWebService = Integer.valueOf(ipPortWS);
			return portWebService;
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return 0;
		}
	}

	/**
	 * @return the nombreThreadsMax
	 */
	@XmlElement(name = "max_thread_ws")
	public Integer getNombreThreadsMax() {
		return nombreThreadsMax;
	}

	/**
	 * @param nombreThreadsMax the nombreThreadsMax to set
	 */
	public void setNombreThreadsMax(Integer nombreThreadsMax) {
		this.nombreThreadsMax = nombreThreadsMax;
	}

	
	
}