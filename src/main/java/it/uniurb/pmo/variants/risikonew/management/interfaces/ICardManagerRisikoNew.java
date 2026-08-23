package it.uniurb.pmo.variants.risikonew.management.interfaces;

import it.uniurb.pmo.framework.management.interfaces.ICardManager;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.variants.risikonew.card.ITerritoryCardContent;

import java.util.List;

public interface ICardManagerRisikoNew extends ICardManager {

    /**
     * Restituisce il numero di rinforzi migliori che si possono ottenere dalle carte del giocatore
     * @param player il giocatore
     * @return il numero di rinforzi migliori che si possono ottenere dalle carte del giocatore
     */
    int getBestReinforcementByCards(IPlayer player);

}
