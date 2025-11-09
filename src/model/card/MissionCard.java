package model.card;

import model.board.IZone;

/**
 * Classe che rappresenta una carta missione nel gioco Risiko.
 */
public class MissionCard implements ICard {

    private final String missionDescription;
    private boolean missionCompleted;

    public MissionCard(String missionDescription) {
        this.missionDescription = missionDescription;
        this.missionCompleted = false;
    }

    public String getMissionDescription() {
        return missionDescription;
    }

    public boolean isMissionCompleted() {
        return missionCompleted;
    }

    public void completeMission() {
        this.missionCompleted = true;
    }

    @Override
    public IZone getZone() {
        // Le carte missione potrebbero non avere una zona associata
        return null;
    }

    @Override
    public ISymbolCard getSymbol() {
        // Le carte missione potrebbero non avere un simbolo associato
        return null;
    }
}