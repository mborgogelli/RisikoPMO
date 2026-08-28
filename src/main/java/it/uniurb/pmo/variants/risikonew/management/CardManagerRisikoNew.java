package it.uniurb.pmo.variants.risikonew.management;

import java.util.ArrayList;
import java.util.List;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.card.ICardType;
import it.uniurb.pmo.framework.management.AbstractCardManager;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.variants.risikonew.card.ERisikoNewCardType;
import it.uniurb.pmo.variants.risikonew.management.interfaces.ICardManagerRisikoNew;

public class CardManagerRisikoNew extends AbstractCardManager implements ICardManagerRisikoNew {
	
	private final List<ICard> territoryCards;
	private final List<ICard> missionCards;
	private final List<ICard> playedTerritoryCards;
	private boolean isReady;
	
	public CardManagerRisikoNew() {
		super();
		this.isReady = false;
		this.territoryCards = new ArrayList<ICard>();
		this.missionCards = new ArrayList<ICard>();
		this.playedTerritoryCards = new ArrayList<ICard>();
	}

	@Override
	public void initializeGame(List<IPlayer> players) {
		// TODO Auto-generated method stub
		this.isReady = true;
	}


	@Override
	public Boolean isReady() {
		return this.isReady;
	}
	
	@Override
	protected void addCard(List<ICard> cards, ICard card) {
	    validateCardType(cards, card);   	// Validazione specifica
	    super.addCard(cards, card);        	// Logica generica
		
	}
	
	@Override
	protected void removeCard(List<ICard> cards, ICard card) {
	    validateCardType(cards, card);    // Validazione specifica
	    super.removeCard(cards, card);        // Logica generica
	}
	
	@Override
	public void resetGame() {
	    this.territoryCards.clear();
	    this.missionCards.clear();
	    this.playedTerritoryCards.clear();
	    this.isReady = false;
	}

	@Override
	public void shuffleDeck(ICardType deckType) {
	    if (deckType == null) {
	        throw new IllegalArgumentException("deckType cannot be null");
	    }
	    if (!(deckType instanceof ERisikoNewCardType type)) {
	        throw new IllegalArgumentException("Unsupported deck type: " + deckType);
	    }
		
		switch (type) {
            case ERisikoNewCardType.TERRITORY:
                this.shuffleCards(this.territoryCards);
                break;
            case ERisikoNewCardType.MISSION:
                this.shuffleCards(this.missionCards);
                break;
            default:
                throw new IllegalArgumentException("Invalid deck type: " + type);
        }
	}

	@Override
	public void playCard(IPlayer player, ICard cardToPlay) {
		// TODO Auto-generated method stub
	}

	@Override
	public void pickCard(IPlayer player) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<ICard> getPlayerDeck(IPlayer player, ICardType deckType) {
		return List.of();
	}
	
	private void validateCardType(List<ICard> cards, ICard card) {
	    List<ICard> expectedDeck = switch (card.getCardType()) {
	        case ERisikoNewCardType.TERRITORY -> this.territoryCards;
	        case ERisikoNewCardType.MISSION -> this.missionCards;
	        default -> throw new IllegalArgumentException("Unknown card type: " + card.getCardType());
	    };
	    
	    if (cards != expectedDeck) {
	        throw new IllegalArgumentException("Card type mismatch with deck");
	    }
	}

}
