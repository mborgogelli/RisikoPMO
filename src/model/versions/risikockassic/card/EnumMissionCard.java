package model.versions.risikockassic.card;
import model.card.ISymbolCard;

public enum EnumMissionCard implements ISymbolCard {
    CONQUER("Conquista: "),
    DESTROY("Distruggi l'armata: ");

    private final String description;

    EnumMissionCard(String description) {
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

