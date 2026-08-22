package it.uniurb.pmo.variants.risikonew.card;
public enum ERisikoNewMissionType {

    CONTROL("Controlla:"),
    CONQUER("Conquista: "),
    DESTROY("Distruggi l'armata: ");

    private final String description;

    ERisikoNewMissionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
