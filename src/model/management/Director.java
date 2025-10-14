package model.management;

import java.util.List;

import model.management.interfaces.IDirector;
import model.management.interfaces.IGameFactory;
import model.players.IPlayer;
import model.utils.EnumColors;
import model.utils.GameFactoryProvider;
import model.utils.GameVersion;

public class Director implements IDirector{
	
	private boolean isReady;
	private boolean isGameStarted;

	private MapManager mapManager;
	private TokenManager tokenManager;
	private CardManager cardManager;
	private PhaseManager phaseManager;
	
	public Director() {
		this.isReady = false;
		this.isGameStarted = false;
	}
	
	
	@Override
	public Boolean isReady() {
		return this.isReady;
	}
	
	@Override
	public void initializeGame(List<IPlayer> players, GameVersion version) {
		IGameFactory factory = GameFactoryProvider.getFactory(version);

	    this.mapManager = factory.createMapManager();
	    this.tokenManager = factory.createTokenManager();
	    this.cardManager = factory.createCardManager();
	    this.phaseManager = factory.createTurnManager();

	    mapManager.initializeGame(players);
	    tokenManager.initializeGame(players);
	    cardManager.initializeGame(players);
	    phaseManager.initializeGame(players);

	    this.isReady = true;		
	}

	@Override
	public void resetGame() {
		this.isReady = false;
		this.isGameStarted = false;
		this.mapManager = null;
		this.tokenManager = null;
		this.cardManager = null;
		this.phaseManager = null;
	}

	@Override
	public boolean isGameStarted() {
		return this.isGameStarted;
	}

	@Override
	public EnumColors getColor(IPlayer player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkWin(IPlayer player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkLoss(IPlayer player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void StartGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitGame(EnumColors color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializeGame(List<IPlayer> players) {
		throw new UnsupportedOperationException("Use initializeGame with GameVersion");
	}

}