package it.uniurb.pmo.framework.management.interfaces;

import java.util.List;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.card.ICardType;
import it.uniurb.pmo.framework.players.IPlayer;

public interface IDeckManager<T extends ICardType, TCardContent> extends IManager {

	void shuffleDeck();

	List<ICard<T, TCardContent>> playCards(IPlayer player);

	ICard<T, TCardContent> pickCard(IPlayer player);
}


