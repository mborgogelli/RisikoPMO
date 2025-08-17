package model.management;

import java.util.List;

import model.IPlayer;

public abstract class TokenManager implements IManager{
	
	private final List<IPlayer> players;
	private Boolean isReady;
	
	protected TokenManager(List<IPlayer> players) {
		this.players = players;
		this.isReady = false;
	}
	
	@Override
	public abstract void initializeGame();

	@Override
	public Boolean isReady() {
		return this.isReady;
	};
	
	
	
	

}
