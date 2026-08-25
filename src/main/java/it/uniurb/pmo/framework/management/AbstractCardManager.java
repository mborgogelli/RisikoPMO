package it.uniurb.pmo.framework.management;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.management.interfaces.ICardManager;
import it.uniurb.pmo.framework.management.interfaces.IMediator;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * Classe astratta che gestire un mazzo di carte
 * Fornisce metodi per gestire i mazzi di carte
 */
public abstract class AbstractCardManager implements ICardManager {
	
	private IMediator mediator;
	
	@Override
	public void setMediator(IMediator mediator) {
		this.mediator = mediator;
		this.mediator.registerManager(this);
	}
	
	/**
	 * Aggiunge una carta al mazzo specificato.
	 * Se la carta è già presente nel mazzo, lancia un'eccezione.
	 * @param cards
	 * @param card
	 */
	protected void addCard(List<ICard> cards, ICard card) {
		if (checkCard(cards, card)) {
			throw new IllegalArgumentException("Card already exists in the deck");
		}
		cards.add(card);
	}
	
	/**
	 * Rimuove una carta dal mazzo specificato
	 * Se la carta non esiste nel mazzo, lancia un'eccezione.
	 * @param cards
	 * @param card
	 */
	
	protected void removeCard(List<ICard> cards, ICard card) {
		if (!checkCard(cards, card)) {
			throw new IllegalArgumentException("Card does not exist in the deck");
		}
		cards.remove(card);	
	}
	
	/**
	 * Restituisce il mazzo di carte specificato  mischiato.
	 * @param cards
	 */
	protected  void shuffleCards(List<ICard> cards){
		Collections.shuffle(cards);
	}

	/**
	 * Controlla se una carta esiste nel mazzo specificato.
	 * @param cards
	 * @param card
	 * @return true se la carta esiste nel mazzo, false altrimenti
	 */
	private boolean checkCard(List<ICard> cards, ICard card) {
		if (cards == null || card == null) {
			throw new IllegalArgumentException("Deck or card is null");
		}
		return cards.contains(card);
	}


	/**
	* Restituisce uno Stream di tutte le combinazioni di dimensione 'k'.
	*/
	@Override
	public Stream<List<ICard>> getCombinationsOf(List<ICard> playerCards, int k) {
		// caso base
		if (k <= 0 || k > playerCards.size()) {
			return Stream.empty();
		}

		// caso base
		if (k == 1) {
			return playerCards.stream().map(List::of);
		}

		// Per ogni indice i, prende l'i-esimo elemento e lo concatena
		// a tutte le combinazioni di dimensione (k - 1) della sottolista successiva
		return IntStream.range(0, playerCards.size() - k + 1)
				.boxed()
				.flatMap(i -> getCombinationsOf(playerCards.subList(i + 1, playerCards.size()), k - 1)
						.map(subCombination -> Stream.concat(
								Stream.of(playerCards.get(i)), // concatena l'i-esimo elemento con tutte le combinazioni
								subCombination.stream()		   // di dimensione (k - 1) della sottolista successiva
						).toList())
				);
	}
}
