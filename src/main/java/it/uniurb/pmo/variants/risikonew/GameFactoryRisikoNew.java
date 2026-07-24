package it.uniurb.pmo.variants.risikonew;

import java.util.ArrayList;
import java.util.List;

import it.uniurb.pmo.framework.management.AbstractMediator;
import it.uniurb.pmo.framework.management.interfaces.IGameFactory;
import it.uniurb.pmo.framework.management.interfaces.IManager;
import it.uniurb.pmo.variants.risikonew.management.CardManagerRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.MapManagerRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.MediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.PlayerInputProviderRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.TankManager;
import it.uniurb.pmo.variants.risikonew.management.TurnManagerRisikoNew;

/**
 * Classe factory per la creazione dei manager e del mediatore
 */
public class GameFactoryRisikoNew implements IGameFactory {
	
	private final AbstractMediator mediator;
	private final List<IManager> managers;
	
	public GameFactoryRisikoNew() {
		this.managers = new ArrayList<>();
		this.mediator = new MediatorRisikoNew();
		this.createManagers();
		this.setMediator();
		this.setPlayerInputProvider();
	}
	
	@Override
	public AbstractMediator getMediator() {
		return this.mediator;
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
		this.managers.add(new TurnManagerRisikoNew());
	}
	
	/**
	 * Imposta il mediatore per ogni manager e inizializza i manager nel mediatore
	 */
	private void setMediator() {
		this.managers.forEach(manager -> manager.setMediator(this.mediator));
		this.mediator.initManagers();
	}

	/**
	 * Inietta il provider di input del giocatore nel mediatore
	 */
	private void setPlayerInputProvider() {
		this.mediator.setPlayerInputProvider(new PlayerInputProviderRisikoNew());
	}
}