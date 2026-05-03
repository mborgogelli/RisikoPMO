package it.uniurb.pmo.variants.risikonew.card;
import it.uniurb.pmo.framework.card.ICardType;

public enum EnumMissionSymbol implements ICardType {
    CONQUER("Conquista: "),
    DESTROY("Distruggi l'armata: ");

    private final String description;

    EnumMissionSymbol(String description) {
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

