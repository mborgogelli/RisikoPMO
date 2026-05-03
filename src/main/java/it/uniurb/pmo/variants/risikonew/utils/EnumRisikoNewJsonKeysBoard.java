package it.uniurb.pmo.variants.risikonew.utils;

/**
 * Enumerazione che definisce i diversi tipi di zone nel gioco.
 * Ogni tipo ha caratteristiche specifiche per il gameplay.
 */
public enum EnumRisikoNewJsonKeysBoard {

	/** Territorio - l'unità fondamentale del gioco classico */
    TERRITORIES("territories"),
    
    /** Continente - raggruppa più territori */
    CONTINENTS("continents"),
	
	ARMYBONUS("armybonus");
	
    private final String descrizione;
    
    EnumRisikoNewJsonKeysBoard(String descrizione) {
        this.descrizione = descrizione;
    }
    
	public String getDescrizione() {
        return this.descrizione;
    }
    
}