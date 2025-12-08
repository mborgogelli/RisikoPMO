package model.versions.risikockassic;

import java.util.ArrayList;
import java.util.List;


import model.management.Mediator;
import model.management.interfaces.IGameFactory;
import model.management.interfaces.IManager;
import model.versions.risikockassic.management.CardManagerRisikoNew;
import model.versions.risikockassic.management.MapManagerRisikoNew;
import model.versions.risikockassic.management.MediatorRisikoNew;
import model.versions.risikockassic.management.TurnManagerRisikoNew;
import model.versions.risikockassic.management.TankManager;

/**
 * Classe factory per la creazione dei manager e del mediatore
 */
public class GameFactoryRisikoNew implements IGameFactory {
	
	private final Mediator mediator;
	private final List<IManager> managers;
	
	public GameFactoryRisikoNew() {
		this.managers = new ArrayList<>();
		this.mediator = new MediatorRisikoNew();
		this.createManagers();
		this.setMediator();
	}
	
	@Override
	public Mediator getMediator() {
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
		for (IManager manager : this.managers) {
			manager.setMediator(this.mediator);
		}
		this.mediator.initManagers();
	}
}