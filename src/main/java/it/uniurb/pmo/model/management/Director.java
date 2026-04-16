package it.uniurb.pmo.model.management;

import java.util.List;

import it.uniurb.pmo.model.management.interfaces.IDirector;
import it.uniurb.pmo.model.management.interfaces.IGameFactory;
import it.uniurb.pmo.model.management.interfaces.IManager;
import it.uniurb.pmo.model.management.interfaces.IMediator;
import it.uniurb.pmo.model.players.IPlayer;
import it.uniurb.pmo.model.utils.GameFactoryProvider;
import it.uniurb.pmo.model.utils.GameVersion;

public class Director implements IDirector{
	
	private boolean isReady;
	private IMediator mediator;
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
		this.managers.forEach(IManager::resetGame);
	}

	@Override
	public void declareWinner(IPlayer player) {
		this.stopGame();
	}

	@Override
	public void StartGame() {
		if(this.isReady){
			this.mediator.startGame();
		}
	}

	@Override
	public void exitGame(IPlayer player) {
		player.removeColor();
		this.players.remove(player);
	}

	@Override
	public void initializeGame(List<IPlayer> players) {
		this.initializeGame(players, this.version);
	}

	@Override
	public void setMediator(IMediator mediator) {
		if (this.mediator == null) {
			this.mediator = mediator;
		}
	}
	
	@Override
	public void stopGame() {
		this.isReady = false;
		this.players.forEach(this::exitGame);
		this.players.clear();
		this.managers.forEach(IManager::resetGame);
	}

	private void initializeGame(List<IPlayer> players, GameVersion version) {
		IGameFactory factory = GameFactoryProvider.getFactory(version);
		this.mediator = factory.getMediator();
		this.managers = factory.getManagers();
		this.managers.forEach(managers -> managers.initializeGame(players));
	    this.isReady = this.checkManagersReady();
	}
	
	private boolean checkManagersReady() {
		return this.managers.stream().allMatch(IManager::isReady);
	}

}