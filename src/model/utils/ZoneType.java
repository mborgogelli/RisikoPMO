package model.utils;

/**
 * Enumerazione che definisce i diversi tipi di zone nel gioco.
 * Ogni tipo ha caratteristiche specifiche per il gameplay.
 */
public enum ZoneType {
    /** Territorio - l'unità fondamentale del gioco classico */
    TERRITORIO("territory"),
    
    /** Continente - raggruppa più territori */
    CONTINENTE("continents");
    
    private final String descrizione;
    
    ZoneType(String descrizione) {
        this.descrizione = descrizione;
    }
    
    public String getDescrizione() {
        return descrizione;
    }
}