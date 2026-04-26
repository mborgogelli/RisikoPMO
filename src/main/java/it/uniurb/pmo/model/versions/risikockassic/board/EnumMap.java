package it.uniurb.pmo.model.versions.risikockassic.board;

/**
 * Enumerazione che definisce i diversi tipi di zone nel gioco.
 * Ogni tipo ha caratteristiche specifiche per il gameplay.
 */
public enum EnumMap {
	
	/** Territorio - l'unità fondamentale del gioco classico */
    TERRITORIES("territories"),
    
    /** Continente - raggruppa più territori */
    CONTINENTS("continents"),
	
	ARMY("army");
	
    
    private final String descrizione;
    
    EnumMap(String descrizione) {
        this.descrizione = descrizione;
    }
    
	public String getDescrizione() {
        return this.descrizione;
    }
    
}