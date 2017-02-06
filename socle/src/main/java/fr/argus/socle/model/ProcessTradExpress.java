package fr.argus.socle.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "ip","command","login", "password"
})
@XmlRootElement(name="PROCESSTRADEXPRESS")
public class ProcessTradExpress {

	
	@XmlElement(name = "ip" , required = true)
	protected String ip;
	
	@XmlElement(name = "command" , required = true)
	protected String command;
	
	@XmlElement(name = "login" , required = true)
	protected String login;
	
	@XmlElement(name = "password" , required =true)
	protected String password;


	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

}
