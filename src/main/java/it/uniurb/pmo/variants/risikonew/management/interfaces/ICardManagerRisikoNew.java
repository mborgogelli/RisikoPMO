package it.uniurb.pmo.variants.risikonew.management.interfaces;

import java.util.List;

import it.uniurb.pmo.framework.management.interfaces.ICardManager;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.variants.risikonew.card.ERisikoNewCardType;
import it.uniurb.pmo.variants.risikonew.card.ITerritoryCard;

public interface ICardManagerRisikoNew extends ICardManager {

	default List<ITerritoryCard> getTerritoryCardsForPlayer(IPlayer player){
		return this.getPlayerDeck(player, ERisikoNewCardType.TERRITORY).stream().map(card -> (ITerritoryCard) card).toList();
	};
}
