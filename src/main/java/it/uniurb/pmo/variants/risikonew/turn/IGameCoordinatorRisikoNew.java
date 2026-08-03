package it.uniurb.pmo.variants.risikonew.turn;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.IGameCoordinator;

import java.util.List;

public interface IGameCoordinatorRisikoNew extends IGameCoordinator {

    /**
     * Chiede al giocatore quali carte riscattare.
     *
     * @param player il giocatore che deve scegliere le carte
     * @param cards  le carte disponibili in mano al giocatore
     * @return la lista di carte scelte per il riscatto
     */
    List<ICard> sendCardRedemptionRequest(IPlayer player, List<ICard> cards);
}
