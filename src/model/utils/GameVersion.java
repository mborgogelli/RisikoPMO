package model.utils;

public enum GameVersion {
	
	RISIKOCLASSIC("risikonew");
	
    private final String descrizione;
    
    GameVersion(String descrizione) {
        this.descrizione = descrizione;
    }
    
	public String getDescrizione() {
        return this.descrizione;
    }
}