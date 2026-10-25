package it.uniurb.pmo.variants.risikonew.turn.command;

import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.turn.command.IMoveCommand;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

public record MoveCommandRisikoNew(String source, String destination,int tanks) implements IMoveCommand {
    @Override
    public ITokenType tokenType() {
        return ERisikoNewToken.TANK;
    }

    @Override
    public int tokensAmount() {
        return tanks;
    }
}
