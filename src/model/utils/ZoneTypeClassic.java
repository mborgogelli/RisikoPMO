package model.utils;

/**
 * Enumerazione che definisce i diversi tipi di zone nel gioco.
 * Ogni tipo ha caratteristiche specifiche per il gameplay.
 */
public enum ZoneTypeClassic {
    /** Territorio - l'unità fondamentale del gioco classico */
    TERRITORIES("territories"),
    
    /** Continente - raggruppa più territori */
    CONTINENTS("continents");
    
    private final String descrizione;
    
    ZoneTypeClassic(String descrizione) {
        this.descrizione = descrizione;
    }
    
    public String getDescrizione() {
        return descrizione;
    }
}