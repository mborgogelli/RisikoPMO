package it.uniurb.pmo.framework.management;

import java.util.List;

import it.uniurb.pmo.framework.management.interfaces.IDirector;
import it.uniurb.pmo.framework.management.interfaces.IGameFactory;
import it.uniurb.pmo.framework.management.interfaces.IManager;
import it.uniurb.pmo.framework.management.interfaces.IMediator;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.PlayerTurnStatus;
import it.uniurb.pmo.framework.utils.GameFactoryProvider;
import it.uniurb.pmo.framework.utils.EGameVersion;

public class Director implements IDirector {
	
	private boolean isReady;
	private IMediator mediator;
	private List<IManager> managers;
	private final List<IPlayer> players;
	
	public Director(EGameVersion version, List<IPlayer> players) {
		this.players = players;
		this.isReady = false;
		this.initializeGame(this.players, version);
		this.startGame();
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
	public void startGame() {
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
	public void stopGame() {
		this.isReady = false;
		this.players.forEach(this::exitGame);
		this.players.clear();
		this.managers.forEach(IManager::resetGame);
	}

	private void initializeGame(List<IPlayer> players, EGameVersion version) {
		IGameFactory factory = GameFactoryProvider.getFactory(version);
		this.mediator = factory.getMediator();
		this.managers = factory.getManagers();
		players.forEach(player -> player.setPlayerTurnStatus(PlayerTurnStatus.ACTIVE));
		this.managers.forEach(managers -> managers.initializeGame(players));
	    this.isReady = this.checkManagersReady();
	}
	
	private boolean checkManagersReady() {
		return this.managers.stream().allMatch(IManager::isReady);
	}

}