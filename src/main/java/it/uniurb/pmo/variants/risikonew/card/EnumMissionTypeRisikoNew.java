package it.uniurb.pmo.variants.risikonew.card;
import it.uniurb.pmo.framework.card.ICardType;
import it.uniurb.pmo.framework.card.IMissionType;

public enum EnumMissionTypeRisikoNew implements IMissionType {

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

    @Override
    public ICardType getCardType() {
        return this;
    }
	    
}

