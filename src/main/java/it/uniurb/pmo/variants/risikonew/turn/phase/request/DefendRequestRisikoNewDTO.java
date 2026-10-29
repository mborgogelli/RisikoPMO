package it.uniurb.pmo.variants.risikonew.turn.phase.request;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.turn.phase.request.IDefendRequestDTO;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

public record DefendRequestRisikoNewDTO(IPlayer player, String attackerName, String attackerTerritory,
                                        String defenderTerritory, int maxDefenderTokens) implements IDefendRequestDTO {

    @Override
    public String defenderName() {
        return player.getName();
    }

    @Override
    public ITokenType defenderTokenType() {
        return ERisikoNewToken.TANK;
    }
}
