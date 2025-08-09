package model.versions.risikockassic;

import model.utils.GameVersion;

/**
 * Enumerazione che definisce i diversi tipi di zone nel gioco.
 * Ogni tipo ha caratteristiche specifiche per il gameplay.
 */
public enum RisikoClassic implements GameVersion {
	
	RISIKOCLASSIC("risikonew"),
	
    /** Territorio - l'unità fondamentale del gioco classico */
    TERRITORIES("territories"),
    
    /** Continente - raggruppa più territori */
    CONTINENTS("continents"),
	
	ARMY("army");
	
    
    private final String descrizione;
    
    RisikoClassic(String descrizione) {
        this.descrizione = descrizione;
    }
    
    @Override
	public String getDescrizione() {
        return this.descrizione;
    }
    
    @Override
    public String getGameVersion() {
    	return RISIKOCLASSIC.getDescrizione();
    }
}