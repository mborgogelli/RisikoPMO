package it.uniurb.pmo.framework.utils;

/** 
 * Enumerazione delle versioni di gioco disponibili.
 */
public enum GameVersion {
	
	RISIKONEW("risikonew"),
	RISIKOANTARTIDE("risikoantartide"),
	SPQRISIKO("spqrisiko"),
	RISIKOOCEANO("risikooceano");
	
    private final String descrizione;
    
    GameVersion(String descrizione) {
        this.descrizione = descrizione;
    }
    
	public String getDescrizione() {
        return this.descrizione;
    }
}