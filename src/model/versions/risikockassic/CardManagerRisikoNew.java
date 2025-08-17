package model.versions.risikockassic;

import java.util.ArrayList;
import java.util.List;

import model.card.ICard;
import model.management.CardManager;

public class CardManagerRisikoNew extends CardManager {
	
	private final List<ICard> territoryCards;
	private final List<ICard> missionCards;
	private final List<ICard> playedCards;
	
	private CardManagerRisikoNew() {
		this.territoryCards = new ArrayList<ICard>();
		this.missionCards = new ArrayList<ICard>();
		this.playedCards = new ArrayList<ICard>();
	}
	
	@Override
	public void initializeGame() {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean isReady() {
		// TODO Auto-generated method stub
		return null;
	}

}
