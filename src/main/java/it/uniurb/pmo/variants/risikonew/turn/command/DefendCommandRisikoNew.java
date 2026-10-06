package it.uniurb.pmo.variants.risikonew.turn.command;

import it.uniurb.pmo.framework.turn.command.IDefendCommand;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

public record DefendCommandRisikoNew(int tanks) implements IDefendCommand<ERisikoNewToken> {

    @Override
    public ERisikoNewToken getTokenType() {
        return ERisikoNewToken.TANK;
    }

    @Override
    public int defenderTokens() {
        return tanks;
    }
}
