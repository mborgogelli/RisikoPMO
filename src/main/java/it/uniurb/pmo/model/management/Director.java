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
	private boolean isGameStarted;

	private Mediator mediator;
	private List<IManager> managers;
	private GameVersion version;
	
	public Director(GameVersion version) {
		this.version = version;
		this.isReady = false;
		this.isGameStarted = false;
	}
	
	@Override
	public Boolean isReady() {
		return this.isReady;
	}
	
	@Override
	public void resetGame() {
		this.isReady = false;
		this.isGameStarted = false;
		this.resetManagers();
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
		this.mediator.startGame();
		this.isGameStarted = true;
	}

	@Override
	public void exitGame(IPlayer player) {
		// TODO Auto-generated method stub

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
	public Map<IManager,Boolean> getManagerStatus(){
		Map<IManager,Boolean> status = new HashMap<>();
		this.managers.stream()
					.forEach(m -> status.put(m, m.isReady()));
		return status;
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
	
    private void resetManagers() {
		for (IManager manager : this.managers) {
			manager.resetGame();
		}
    }

}