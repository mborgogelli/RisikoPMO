package it.uniurb.pmo.variants.risikonew.turn.command;

import it.uniurb.pmo.framework.turn.command.IAttackCommand;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

public record AttackCommandRisikoNew(String attackerZone, String defenderZone, int tanks) implements IAttackCommand<ERisikoNewToken> {

    /** Compatibility constructor; the phase already owns the attacking player. */
    public AttackCommandRisikoNew(IPlayer attacker, String attackerZone, String defenderZone, int tanks) {
        this(attackerZone, defenderZone, tanks);
    }

    @Override
    public ERisikoNewToken tokenType() {
        return ERisikoNewToken.TANK;
    }

    @Override
    public int attackerTokens() {
        return tanks;
    }
}
