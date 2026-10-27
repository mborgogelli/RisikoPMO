package it.uniurb.pmo.variants.risikonew.turn;

import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.turn.IGameCoordinator;

public interface IGameCoordinatorRisikoNew extends IGameCoordinator {

    @Override
    ITokenType resolveTokenType(String tokenTypeName);

    @Override
    void submitAttack(String playerName, String attackerZone, String defenderZone,
                      ITokenType tokenType, int tokenAmount);

    @Override
    void submitDefend(String playerName, ITokenType tokenType, int tokenAmount);

}
