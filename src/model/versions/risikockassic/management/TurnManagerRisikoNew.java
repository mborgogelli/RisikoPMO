package model.versions.risikockassic.management;

import java.util.List;
import java.util.Map;

import model.management.TurnManager;
import model.players.IPlayer;
import model.utils.EnumColors;

public class TurnManagerRisikoNew extends TurnManager {
	
	private boolean isReady;
	private Map<IPlayer,Integer> myTurn;
	private List<EnumColors> playOrder;
	
	
	public TurnManagerRisikoNew() {
		this.isReady = false;
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
	public void resetGame() {
		// TODO Auto-generated method stub
		
	}

}
