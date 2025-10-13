package model.versions.risikockassic.managers;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import model.board.IZone;
import model.card.ICard;
import model.management.CardManager;
import model.players.IPlayer;
import model.versions.risikockassic.card.TerritoryCard;

public class CardManagerRisikoNew extends CardManager {
	
	private static CardManagerRisikoNew instance;
	
	private final List<ICard> territoryCards;
	private final List<ICard> missionCards;
	private final List<ICard> playedCards;
	
	private CardManagerRisikoNew() {
		this.territoryCards = new ArrayList<ICard>();
		this.missionCards = new ArrayList<ICard>();
		this.playedCards = new ArrayList<ICard>();
	}

	@Override
	public void initializeGame(List<IPlayer> players) {
		// TODO Auto-generated method stub
		
	}
	
	public static CardManagerRisikoNew getInstance() {
		if (instance == null) {
			instance = new CardManagerRisikoNew();
		}
		return instance;
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
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void addCard(List<ICard> cards, ICard card) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void removeCard(List<ICard> cards, ICard card) {
		// TODO Auto-generated method stub
		
	}
	
	private List<IZone> getTerritoriesList(){
		return MapManagerRisikoNew.getInstance().getAllZones();
	}

	@Override
	public void resetGame() {
		// TODO Auto-generated method stub
		
	}


//	@Test
//	public void getTerritoriesList(){
//		MapManagerRisikoNew.getInstance().initializeGame();
//		List<IZone> territories = MapManagerRisikoNew.getInstance().getAllTerritories();
//		System.out.println(territories);
//
//	}

}
