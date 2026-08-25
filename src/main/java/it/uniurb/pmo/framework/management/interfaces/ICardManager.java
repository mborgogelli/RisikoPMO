package it.uniurb.pmo.framework.management.interfaces;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.card.ICardType;
import it.uniurb.pmo.framework.players.IPlayer;

import java.util.List;
import java.util.stream.Stream;

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

	/**
	 *  Restituisce il mazzo di carte di un certo tipo di un giocatore
	 * @param player il giocatore che sceglie la carta
	 * @param deckType il tipo di mazzo di carte
	 * @return il mazzo di carte di un certo tipo di un giocatore
	 */
	List<ICard> getPlayerDeck(IPlayer player,ICardType deckType);

	/**
	 * Restituisce uno Stream di tutte le combinazioni di dimensione 'k' ottenibili
	 * dal mazzo di carte di un giocatore
	 * @param playerCards il mazzo di carte di un giocatore
	 * @param k la dimensione delle combinazioni
	 * @return uno Stream di tutte le combinazioni di dimensione 'k' ottenibili
	 */
	Stream<List<ICard>> getCombinationsOf(List<ICard> playerCards, int k);
}
