package it.uniurb.pmo.framework.management.interfaces;

import java.util.List;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.players.IPlayer;

public interface ICardManager extends IManager{
	
	/**
	 * Mescola il mazzo di carte
	 */
	void shuffleDeck();
	
	/**
	 * Distribuisce le carte ai giocatori
	 * 
	 * @param players
	 */
	List<ICard> playCards(IPlayer player, int numberOfCards);
	
	/**
	 * Permette a un giocatore di scegliere una carta da giocare
	 * 
	 * @param player il giocatore che sceglie la carta
	 * @return la carta scelta
	 */
	ICard pickCard(IPlayer player);
	
}
