package it.uniurb.pmo.framework.management.interfaces;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.card.ICardType;
import it.uniurb.pmo.framework.players.IPlayer;

public interface ICardManager extends IManager{
	
	/**
	 * Mescola il mazzo di carte
	 */
	void shuffleDeck(ICardType deckType);
	
	/**
	 * Gioca una carta
	 *
	 * @param player     il giocatore che riceve le carte
	 * @param cardToPlay la carta da giocare
	 */
	void playCard(IPlayer player, ICard cardToPlay);
	
	/**
	 * Pesca una carta
	 *
	 * @param player il giocatore che sceglie la carta
	 */
	void pickCard(IPlayer player);


}
