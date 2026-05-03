package it.uniurb.pmo.variants.risikonew.card;
import it.uniurb.pmo.framework.card.ICardType;

public enum EnumMissionType implements ICardType {
    CONQUER("Conquista: "),
    DESTROY("Distruggi l'armata: ");

    private final String description;

    EnumMissionType(String description) {
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

