package it.uniurb.pmo.framework.utils;

/** 
 * Enumerazione delle versioni di gioco disponibili.
 */
public enum EGameVersion {
	
	RISIKONEW("risikonew"),
	RISIKOANTARTIDE("risikoantartide"),
	SPQRISIKO("spqrisiko"),
	RISIKOOCEANO("risikooceano");
	
    private final String descrizione;
    
    EGameVersion(String descrizione) {
        this.descrizione = descrizione;
    }
    
	public String getDescrizione() {
        return this.descrizione;
    }
}