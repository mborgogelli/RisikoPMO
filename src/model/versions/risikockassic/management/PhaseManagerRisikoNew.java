package model.versions.risikockassic.management;

import java.util.List;

import model.management.PhaseManager;
import model.players.IPlayer;

public class PhaseManagerRisikoNew extends PhaseManager {
	
	private boolean isReady;
	
	public PhaseManagerRisikoNew() {
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
