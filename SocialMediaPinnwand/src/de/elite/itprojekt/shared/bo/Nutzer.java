package de.elite.itprojekt.shared.bo;

public class Nutzer extends BusinessObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String vorName = "Gustav";
	private String nachName = "Gans";
	private String nickName = "GGans";
	
	
	
	public String getVorname() {
		return vorName;
	}
	public void setVorname(String vorname) {
		this.vorName = vorname;
	}
	public String getNachname() {
		return nachName;
	}
	public void setNachname(String nachname) {
		this.nachName = nachname;
	}
	public String getNickname() {
		return nickName;
	}
	public void setNickname(String nickname) {
		this.nickName = nickname;
	}
	public String toString() {
		    return super.toString() + " " + this.nickName + " " + this.vorName + " " + this.nachName;
	}
	public void setAbonnement() {
		
	}
	public void getAbonnement() {
		
	}
	
	
	
}