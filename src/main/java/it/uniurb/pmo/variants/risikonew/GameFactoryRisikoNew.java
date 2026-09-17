package it.uniurb.pmo.variants.risikonew;

import it.uniurb.pmo.framework.management.interfaces.IGameFactory;
import it.uniurb.pmo.framework.management.interfaces.IManager;
import it.uniurb.pmo.framework.management.interfaces.IMediator;
import it.uniurb.pmo.framework.turn.IGameCoordinator;
import it.uniurb.pmo.variants.risikonew.management.*;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.gamecoordinator.GameCoordinatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.gamecoordinator.IGameCoordinatorRisikoNew;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe factory per la creazione dei manager e del mediatore
 */
public class GameFactoryRisikoNew implements IGameFactory {

	private final IMediatorRisikoNew mediator;
	private final IGameCoordinatorRisikoNew gameCoordinator;
	private final List<IManager> managers;
	
	public GameFactoryRisikoNew() {
		this.managers = new ArrayList<>();
		this.mediator = new MediatorRisikoNew();
		this.gameCoordinator = new GameCoordinatorRisikoNew();
		this.createManagers();
		this.setMediator();
	}

	@Override
	public IMediator getMediator() {
		return this.mediator;
	}

	@Override
	public IGameCoordinator getGameCoordinator() {
		return this.gameCoordinator;
	}

	@Override
	public List<IManager> getManagers() {
		return this.managers;
	}
	
	/**
	 * Crea i manager specifici per la versione RisikoNew
	 */
	private void createManagers() {
		this.managers.add(new MapManagerRisikoNew());
		this.managers.add(new TankManager());
		this.managers.add(new CardManagerRisikoNew());
		TurnManagerRisikoNew turnManager = new TurnManagerRisikoNew(this.gameCoordinator);
		turnManager.addObserver(this.gameCoordinator);
		this.gameCoordinator.setCommandReceiver(turnManager);
		this.managers.add(turnManager);
	}
	
	/**
	 * Imposta il mediatore per ogni manager e inizializza i manager nel mediatore
	 */
	private void setMediator() {
		this.managers.forEach(manager -> manager.setMediator(this.mediator));
		this.mediator.initManagers();
	}

}
