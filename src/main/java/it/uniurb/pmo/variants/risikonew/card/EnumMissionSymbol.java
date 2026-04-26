package it.uniurb.pmo.variants.risikonew.card;
import it.uniurb.pmo.framework.card.ISymbolCard;

public enum EnumMissionSymbol implements ISymbolCard {
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
    public ISymbolCard getSymbol() {
        return this;
    }
	    
}

