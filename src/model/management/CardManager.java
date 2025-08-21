package model.management;

import java.util.Collections;
import java.util.List;

import model.card.ICard;
import model.management.interfaces.IManager;

public abstract class CardManager implements IManager{

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
	
	private boolean checkCard(List<ICard> cards, ICard card) {
		if (cards == null || card == null) {
			throw new IllegalArgumentException("Cards list and card cannot be null");
		}
		return cards.contains(card);
	}
}
