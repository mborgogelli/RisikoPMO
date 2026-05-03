package it.uniurb.pmo.variants.risikonew.management;

import java.util.ArrayList;
import java.util.List;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.management.AbstractCardManager;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.variants.risikonew.management.interfaces.ICardManagerRisikoNew;

public class CardManagerRisikoNew extends AbstractCardManager implements ICardManagerRisikoNew {
	
	private final List<ICard> territoryCards;
	private final List<ICard> missionCards;
	private final List<ICard> playedCards;
	private boolean isReady;
	
	public CardManagerRisikoNew() {
		super();
		this.isReady = false;
		this.territoryCards = new ArrayList<ICard>();
		this.missionCards = new ArrayList<ICard>();
		this.playedCards = new ArrayList<ICard>();
	}

	@Override
	public void initializeGame(List<IPlayer> players) {
		// TODO Auto-generated method stub
		this.isReady = true;
	}


//	@Override
	// io non lo devo inizializzare ma controllare che sia stato inizializzato dal mapmanager
//	public void initializeGame() {
//		//TODO -- Assegnazione 
////	    List<IZone> territories = MapManagerRisikoNew.getInstance().getAllTerritories();
////	    for (IZone territory : territories) {
////	        territoryCards.add(new TerritoryCard(territory)); // Assicurati che TerritoryCard implementi ICard
////	    }
//
//	}

	@Override
	public Boolean isReady() {
		return this.isReady;
	}
	
	@Override
	protected void addCard(List<ICard> cards, ICard card) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void removeCard(List<ICard> cards, ICard card) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void resetGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shuffleDeck() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ICard> playCards(IPlayer player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICard pickCard(IPlayer player) {
		// TODO Auto-generated method stub
		return null;
	}


//	@Test
//	public void getTerritoriesList(){
//		MapManagerRisikoNew.getInstance().initializeGame();
//		List<IZone> territories = MapManagerRisikoNew.getInstance().getAllTerritories();
//		System.out.println(territories);
//
//	}

}
