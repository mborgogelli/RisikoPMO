package it.uniurb.pmo.framework.turn.dto;

public class AttackChoiceDTO {

    private final String attackerZone;
    private final String defenderZone;
    private final int numberOfDice;

    public AttackChoiceDTO(String attackerZone, String defenderZone, int numberOfDice) {
        this.attackerZone = attackerZone;
        this.defenderZone = defenderZone;
        this.numberOfDice = numberOfDice;
    }

    public String getAttackerZone() {
        return this.attackerZone;
    }

    public String getDefenderZone() {
        return this.defenderZone;
    }

    public int getNumberOfDice() {
        return this.numberOfDice;
    }
}
