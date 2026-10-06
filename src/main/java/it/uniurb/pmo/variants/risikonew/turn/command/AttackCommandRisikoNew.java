package it.uniurb.pmo.variants.risikonew.turn.command;

import it.uniurb.pmo.framework.turn.command.IAttackCommand;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

public record AttackCommandRisikoNew(String attackerZone, String defenderZone, int tanks, int numberOfDice) implements IAttackCommand<ERisikoNewToken> {

    @Override
    public ERisikoNewToken tokenType() {
        return ERisikoNewToken.TANK;
    }

    @Override
    public int tokenAmount() {
        return tanks;
    }
}
