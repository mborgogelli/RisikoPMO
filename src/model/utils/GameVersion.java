package model.utils;

public enum GameVersion {
	
	RISIKONEW("risikonew"),
	RISIKOANTARTIDE("risikoantartide"),
	SPQRISIKO("spqrisiko"),
	RISIKOOCEANI("risikooceani");
	
    private final String descrizione;
    
    GameVersion(String descrizione) {
        this.descrizione = descrizione;
    }
    
	public String getDescrizione() {
        return this.descrizione;
    }
}