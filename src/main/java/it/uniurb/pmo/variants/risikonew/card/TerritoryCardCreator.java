package it.uniurb.pmo.variants.risikonew.card;

import java.util.ArrayList;
import java.util.List;

import it.uniurb.pmo.framework.card.CardCreator;
import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.card.ICardType;
import it.uniurb.pmo.framework.management.interfaces.IMediator;

public class TerritoryCardCreator extends CardCreator {
	 private final IMediator mediator;
	 
	public TerritoryCardCreator(IMediator mediator) {
		if (mediator == null) {
			throw new IllegalArgumentException("mediator cannot be null");
		}
		this.mediator = mediator;
	}

	@Override
	public List<ICard> getDeck() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<ICard> createDeck(ICardType cardType) {
		// TODO Auto-generated method stub
		return null;
	}


	//TODO: Implementare la logica per creare le carte del territorio utilizzando il mediatore
	// 
//    protected List<ICard> createDeck() {
//        List<ICard> deck = new ArrayList<>();
//
//        List<String> zones = this.mediator.getAllZones();
//        for (String zoneName : zones) {
//            int zoneValue = this.mediator.getZoneValue(zoneName);
//
//            // Adatta al tuo costruttore reale di TerritoryCard
//           //ICard card = new TerritoryCard(zoneName, zoneValue, ERisikoNewCardType.TERRITORY);
//            ICard card = new TerritoryCard(null, zoneName);
//            deck.add(card);
//        }
//
//        return deck;
  //  }
	 

}
