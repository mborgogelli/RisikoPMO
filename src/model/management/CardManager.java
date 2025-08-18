package model.management;

import java.util.Collections;
import java.util.List;

import model.card.ICard;

public abstract class CardManager implements IManager{


	// Aggiunge una carta nel mazzo specificato come parametro
	// Le carte possono essere di tipo territorio, missione, ecc.
	protected abstract void addCard(List<ICard> cards, ICard card);
	
	// Rimuove una carta dal mazzo specificato come parametro
	protected abstract void removeCard(List<ICard> cards, ICard card);
	
	// Mischia il mazzo specificato come parametro
	public void	 shuffleCards(List<ICard> cards){
		Collections.shuffle(cards);
	}
}
