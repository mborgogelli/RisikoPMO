package model.versions.risikockassic.card;

public enum MissionCardEnum {
	
    CONQUER_NORTH_AMERICA_AND_AFRICA("Conquistare il Nord America e l'Africa"),
    CONQUER_NORTH_AMERICA_AND_OCEANIA("Conquistare il Nord America e l'Oceania"),
    CONQUER_ASIA_AND_SOUTH_AMERICA("Conquistare l'Asia e il Sud America"),
    CONQUER_ASIA_AND_AFRICA("Conquistare l'Asia e l'Africa"),
    CONQUER_EUROPE_WESTERN_AUSTRALIA_AND_ONE_OTHER("Conquistare l'Europa, l'Australia Occidentale e un altro continente a scelta"),
    CONQUER_EUROPE_SOUTH_AMERICA_AND_ONE_OTHER("Conquistare l'Europa, il Sud America e un altro continente a scelta"),
    CONQUER_18_TERRITORIES_WITH_2_ARMIES("Conquistare 18 territori e occupare ciascuno con almeno 2 armate"),
    CONQUER_24_TERRITORIES("Conquistare 24 territori"),
    DESTROY_BLUE_ARMY("Distruggere completamente l'armata blu"),
    DESTROY_YELLOW_ARMY("Distruggere completamente l'armata gialla"),
    DESTROY_BLACK_ARMY("Distruggere completamente l'armata nera"),
    DESTROY_RED_ARMY("Distruggere completamente l'armata rossa"),
    DESTROY_GREEN_ARMY("Distruggere completamente l'armata verde"),
    DESTROY_WHITE_ARMY("Distruggere completamente l'armata bianca");

    private final String description;

    MissionCardEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
