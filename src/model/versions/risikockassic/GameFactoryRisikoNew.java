package model.versions.risikockassic;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import model.management.CardManager;
import model.management.Mediator;
import model.management.PhaseManager;
import model.management.TokenManager;
import model.management.interfaces.IGameFactory;
import model.management.interfaces.IManager;
import model.management.interfaces.IMediator;
import model.players.IPlayer;
import model.versions.risikockassic.interfaces.IMapManagerRisikoNew;
import model.versions.risikockassic.managers.CardManagerRisikoNew;
import model.versions.risikockassic.managers.MapManagerRisikoNew;
import model.versions.risikockassic.managers.PhaseManagerRisikoNew;
import model.versions.risikockassic.managers.MediatorRisikoNew;
import model.versions.risikockassic.managers.TankManager;

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
		this.managers.add(new PhaseManagerRisikoNew());
	}
	
	/**
	 * Imposta il mediatore per ogni manager e inizializza i manager nel mediatore
	 */
	private void setMediator() {
		for (IManager manager : this.managers) {
			manager.setMediator(this.mediator);
			System.out.println(manager.getClass());
		}
		this.mediator.initManagers();
	}
}