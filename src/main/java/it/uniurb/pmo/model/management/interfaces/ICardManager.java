package it.uniurb.pmo.model.management.interfaces;

import java.util.List;

import it.uniurb.pmo.model.card.ICard;
import it.uniurb.pmo.model.players.IPlayer;

public interface ICardManager extends IManager{
	
	void shuffleDeck();
	
	List<ICard> playCards(IPlayer player);
	
	IMission getMission(IPlayer player);
	
	ICard pickCard(IPlayer player);
	
}
