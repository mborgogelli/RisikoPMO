package model.management;

import java.util.List;

import model.management.interfaces.IDirector;
import model.management.interfaces.IGameFactory;
import model.management.interfaces.IManager;
import model.management.interfaces.IMapManager;
import model.management.interfaces.IMediator;
import model.players.IPlayer;
import model.utils.EnumColors;
import model.utils.GameFactoryProvider;
import model.utils.GameVersion;

public class Director implements IDirector{
	
	private boolean isReady;
	private boolean isGameStarted;

	private Mediator mediator;
	private List<IManager> managers;
	
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
		this.setMediator(factory.getMediator());
		this.managers = factory.getManagers();
		this.initializeManagers(players);
	    this.isReady = true;		
	}
	
	@Override
	public void resetGame() {
		this.isReady = false;
		this.isGameStarted = false;
	}

	@Override
	public boolean isGameStarted() {
		return this.isGameStarted;
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


	@Override
	public void setMediator(Mediator mediator) {
		if (mediator == null) {
			this.mediator = mediator;
		}
	}
	
	private void initializeManagers(List<IPlayer> players) {
		for (IManager manager : this.managers) {
			manager.initializeGame(players);
		}
	}
	
    private void resetManagers() {
		for (IManager manager : this.managers) {
			manager.resetGame();
		}
    }

}