package it.uniurb.pmo.framework.management.interfaces;

import java.util.List;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.players.IPlayer;

public interface ICardManager extends IManager{
	
	void shuffleDeck();
	
	List<ICard> playCards(IPlayer player);
	
	IMission getMission(IPlayer player);
	
	ICard pickCard(IPlayer player);
	
}
