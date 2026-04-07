package it.uniurb.pmo.model.management;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.uniurb.pmo.model.management.interfaces.IDirector;
import it.uniurb.pmo.model.management.interfaces.IGameFactory;
import it.uniurb.pmo.model.management.interfaces.IManager;
import it.uniurb.pmo.model.players.IPlayer;
import it.uniurb.pmo.model.utils.GameFactoryProvider;
import it.uniurb.pmo.model.utils.GameVersion;

public class Director implements IDirector{
	
	private boolean isReady;
	private Mediator mediator;
	private List<IManager> managers;
	private final GameVersion version;
	private final List<IPlayer> players;
	
	public Director(GameVersion version, List<IPlayer> players) {
		this.version = version;
		this.players = players;
		this.isReady = false;
		this.initializeGame(this.players, version);
		this.StartGame();
	}
	
	@Override
	public Boolean isReady() {
		return this.isReady;
	}
	
	@Override
	public void resetGame() {
		this.isReady = false;
		this.resetManagers();
	}

	@Override
	public void declareWinner(IPlayer player) {
		this.stopGame();
	}

	@Override
	public void StartGame() {
		if(this.checkManagersReady()) {
			this.isReady = true;
			this.mediator.startGame();
		}
	}

	@Override
	public void exitGame(IPlayer player) {
		player.removeColor();
	}

	@Override
	public void initializeGame(List<IPlayer> players) {
		this.initializeGame(players, this.version);
	}

	@Override
	public void setMediator(Mediator mediator) {
		if (this.mediator == null) {
			this.mediator = mediator;
		}
	}
	
	@Override
	public void stopGame() {
		this.isReady = false;
		for (IPlayer player : players) {
			exitGame(player);
		}
		this.players.clear();
		for (IManager manager : this.managers) {
			manager.resetGame();
		}
	}

	private void initializeGame(List<IPlayer> players, GameVersion version) {
		IGameFactory factory = GameFactoryProvider.getFactory(version);
		this.setMediator(factory.getMediator());
		this.managers = factory.getManagers();
		this.initializeManagers(players);
	    this.isReady = true;		
	}
	
	private void initializeManagers(List<IPlayer> players) {
		for (IManager manager : this.managers) {
			manager.initializeGame(players);
		}
	}

	private boolean checkManagersReady() {
		return this.managers.stream().allMatch(IManager::isReady);
	}

    private void resetManagers() {
		for (IManager manager : this.managers) {
			manager.resetGame();
		}
    }

}