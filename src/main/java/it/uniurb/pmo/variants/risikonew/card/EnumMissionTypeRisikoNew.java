package it.uniurb.pmo.variants.risikonew.card;
import it.uniurb.pmo.framework.card.ICardType;

public enum EnumMissionTypeRisikoNew implements ICardType {

    CONTROL("Controlla:"),
    CONQUER("Conquista: "),
    DESTROY("Distruggi l'armata: ");

    private final String description;

    EnumMissionTypeRisikoNew(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
