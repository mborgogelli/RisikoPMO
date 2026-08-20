package it.uniurb.pmo.framework.management.interfaces;

import java.util.List;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.card.ICardType;
import it.uniurb.pmo.framework.players.IPlayer;

public interface IDeckManager extends IManager {

	void shuffleDeck();

	List<ICard> playCards(IPlayer player, ICardType cardType);

	ICard pickCard(IPlayer player, ICardType cardType);
}


