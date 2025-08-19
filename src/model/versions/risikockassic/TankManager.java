package model.versions.risikockassic;

import java.util.List;

import model.board.IZone;
import model.management.TokenManager;
import model.players.IPlayer;
import model.utils.EnumToken;

public class TankManager extends TokenManager {

	private Boolean isReady;
	private final EnumToken tanks;
	
	private static TankManager instance;
	
	protected TankManager() {
		super();
		this.isReady = false;
		this.tanks = EnumToken.getRisikoNewTokens().get(0);
	}
	
	public TankManager getInstance() {
		if (instance == null) {
			instance = new TankManager();
		}
		return instance;
	}
	
	@Override
	public void initializeGame(List<IPlayer> players) {
		
	}

	@Override
	public Boolean isReady() {
		return this.isReady;
	}

	@Override
	protected void assignTokensToZones(List<IZone> zones) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void assignTokensToPlayers(List<IPlayer> players) {
		switch(players.size()) {
			case 3: for (IPlayer p : players) { super.addPlayerToken(p, tanks , 35);};
			case 4: for (IPlayer p : players) { super.addPlayerToken(p, tanks , 30);};
			case 5: for (IPlayer p : players) { super.addPlayerToken(p, tanks , 25);};
			case 6: for (IPlayer p : players) { super.addPlayerToken(p, tanks , 20);};
			default: throw new IllegalArgumentException("Wrong numbers of players");
		}
	}

}
