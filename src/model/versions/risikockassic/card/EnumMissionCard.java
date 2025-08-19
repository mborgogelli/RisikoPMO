package model.versions.risikockassic.card;

import java.util.ArrayList;
import java.util.List;

public enum EnumMissionCard {

	    CONQUER_NORTH_AMERICA_AND_AFRICA("Conquistare tutto il Nord America e l’Africa."),
	    CONQUER_NORTH_AMERICA_AND_OCEANIA("Conquistare tutto il Nord America e l’Oceania."),
	    CONQUER_ASIA_AND_SOUTH_AMERICA("Conquistare tutta l’Asia e il Sud America."),
	    CONQUER_ASIA_AND_AFRICA("Conquistare tutta l’Asia e l’Africa."),
	    CONQUER_EUROPE_SOUTH_AMERICA_AND_ONE("Conquistare tutta l’Europa, il Sud America e un terzo continente a scelta."),
	    CONQUER_EUROPE_OCEANIA_AND_ONE("Conquistare tutta l’Europa, l’Oceania e un terzo continente a scelta."),
	    CONQUER_18_TERRITORIES_WITH_2_ARMIES("Conquistare 18 territori e presidiarli con almeno 2 armate ciascuno."),
	    CONQUER_24_TERRITORIES("Conquistare 24 territori."),
	    DESTROY_RED_ARMY("Distruggere completamente l’armata Rossa. Se questo obiettivo non è possibile, sostituirlo con 'Conquistare 24 territori'."),
	    DESTROY_BLUE_ARMY("Distruggere completamente l’armata Blu. Se questo obiettivo non è possibile, sostituirlo con 'Conquistare 24 territori'."),
	    DESTROY_GREEN_ARMY("Distruggere completamente l’armata Verde. Se questo obiettivo non è possibile, sostituirlo con 'Conquistare 24 territori'."),
	    DESTROY_YELLOW_ARMY("Distruggere completamente l’armata Gialla. Se questo obiettivo non è possibile, sostituirlo con 'Conquistare 24 territori'."),
	    DESTROY_BLACK_ARMY("Distruggere completamente l’armata Nera. Se questo obiettivo non è possibile, sostituirlo con 'Conquistare 24 territori'."),
	    ELIMINATE_PURPLE_ARMY("Distruggere completamente l’armata Viola. Se questo obiettivo non è possibile, sostituirlo con 'Conquistare 24 territori'.");

	    private final String description;

	    EnumMissionCard(String description) {
	        this.description = description;
	    }

	    /**
	     * Restituisce la descrizione della carta Obiettivo.
	     * 
	     * @return Descrizione della carta Obiettivo
	     */
	    public String getDescription() {
	        return description;
	    }
	    
	    /**
	     * Restituisce la lista delle carte Obiettivo disponibili.
	     * 
	     * @return Lista di descrizioni delle carte Obiettivo
	     */
//	    public static List<String> getAllMissions() {
//	    	List<String> missions = new ArrayList<>();
//	    	for (EnumMissionCard card : EnumMissionCard.values()) {
//	    		missions.add(card.getDescription());
//	    	}
//			return missions;	
//	    }
	    
	    public static List<EnumMissionCard> GetAllMissions(){
	    	return List.of(EnumMissionCard.values());
	    }
}
