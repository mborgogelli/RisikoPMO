package model.management;

import java.util.Collections;
import java.util.List;

import model.board.IZone;
import model.card.ICard;
import model.management.interfaces.IManager;
import model.management.interfaces.IMediator;


/**
 * Classe astratta che gestire un mazzo di carte
 * Fornisce metodi per gestire i mazzi di carte
 */
public abstract class CardManager implements IManager{
	
	private IMediator mediator;
	
	@Override
	public void setMediator(Mediator mediator) {
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
	
	
}
