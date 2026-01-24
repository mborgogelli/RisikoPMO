package it.uniurb.pmo.model.versions.risikockassic.card;
import it.uniurb.pmo.model.card.ISymbolCard;

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

