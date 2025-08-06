package model.utils;

/**
 * Enumerazione che definisce i diversi tipi di zone nel gioco.
 * Ogni tipo ha caratteristiche specifiche per il gameplay.
 */
public enum EnumRisikoClassic implements IEnumRisiko {
	
	GAMEVERSION("risikonew"),
	
    /** Territorio - l'unità fondamentale del gioco classico */
    TERRITORIES("territories"),
    
    /** Continente - raggruppa più territori */
    CONTINENTS("continents");
    
    private final String descrizione;
    
    EnumRisikoClassic(String descrizione) {
        this.descrizione = descrizione;
    }
    
    @Override
	public String getDescrizione() {
        return this.descrizione;
    }
    
    public String getGameVersion() {
    	return GAMEVERSION.getDescrizione();
    }
}