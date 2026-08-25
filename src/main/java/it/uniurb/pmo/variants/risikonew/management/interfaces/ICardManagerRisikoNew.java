package it.uniurb.pmo.variants.risikonew.management.interfaces;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.management.interfaces.ICardManager;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.variants.risikonew.card.ERisikoNewCardType;
import it.uniurb.pmo.variants.risikonew.card.ITerritoryCard;

import java.util.List;
import java.util.stream.Stream;

public interface ICardManagerRisikoNew extends ICardManager {

	default List<ITerritoryCard> getTerritoryCardsForPlayer(IPlayer player){
		return this.getPlayerDeck(player, ERisikoNewCardType.TERRITORY).stream().map(card -> (ITerritoryCard) card).toList();
	};
	
	default Stream<List<ICard>> getAvailableTris(IPlayer player) {
		List<ICard> territoryCards = this.getPlayerDeck(player, ERisikoNewCardType.TERRITORY);
		return this.getCombinationsOf(territoryCards, 3);
	}

	default void playTris(IPlayer player, List<ICard> cards){
		cards.stream().forEach(card -> this.playCard(player, card));
	};
}
