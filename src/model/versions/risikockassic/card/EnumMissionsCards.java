package model.versions.risikockassic.card;

public enum EnumMissionsCards {

	    CONQUER_18_TERRITORIES_WITH_2("Conquistare 18 territori e presidiarli con almeno 2 armate ciascuno."),
	    CONQUER_24_TERRITORIES("Conquistare 24 territori."),
	    CONQUER_NORTH_AMERICA_AND_AFRICA("Conquistare tutto il Nord America e l’Africa."),
	    CONQUER_NORTH_AMERICA_AND_OCEANIA("Conquistare tutto il Nord America e l’Oceania."),
	    CONQUER_ASIA_AND_SOUTH_AMERICA("Conquistare tutta l’Asia e il Sud America."),
	    CONQUER_ASIA_AND_AFRICA("Conquistare tutta l’Asia e l’Africa."),
	    CONQUER_EUROPE_SOUTH_AMERICA_AND_ONE("Conquistare tutta l’Europa, il Sud America e un terzo continente a scelta."),
	    CONQUER_EUROPE_OCEANIA_AND_ONE("Conquistare tutta l’Europa, l’Oceania e un terzo continente a scelta."),
	    ELIMINATE_RED("Distruggere completamente l’armata Rossa. Se questo obiettivo non è possibile, sostituirlo con 'Conquistare 24 territori'."),
	    ELIMINATE_BLUE("Distruggere completamente l’armata Blu. Se questo obiettivo non è possibile, sostituirlo con 'Conquistare 24 territori'."),
	    ELIMINATE_GREEN("Distruggere completamente l’armata Verde. Se questo obiettivo non è possibile, sostituirlo con 'Conquistare 24 territori'."),
	    ELIMINATE_YELLOW("Distruggere completamente l’armata Gialla. Se questo obiettivo non è possibile, sostituirlo con 'Conquistare 24 territori'."),
	    ELIMINATE_BLACK("Distruggere completamente l’armata Nera. Se questo obiettivo non è possibile, sostituirlo con 'Conquistare 24 territori'."),
	    ELIMINATE_PURPLE("Distruggere completamente l’armata Viola. Se questo obiettivo non è possibile, sostituirlo con 'Conquistare 24 territori'.");

	    private final String description;

	    EnumMissionsCards(String description) {
	        this.description = description;
	    }

	    public String getDescription() {
	        return description;
	    }
}
