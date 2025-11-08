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

public class GameFactoryRisikoNew implements IGameFactory {
	
	private Mediator mediator;
	private List<IManager> managers;
	
	public GameFactoryRisikoNew() {
		this.managers = new ArrayList<>();
	}
	
	@Override
	public Mediator getMediator() {
		if (this.mediator == null) {
			this.mediator = new MediatorRisikoNew();
		}
		return this.mediator;
	}

	@Override
	public List<IManager> getManagers() {
		if (this.managers.isEmpty()) {
			this.createManagers();
			this.setMediator();
		}
		return this.managers;
	}
	
	private void createManagers() {
		this.managers.add(new MapManagerRisikoNew());
		this.managers.add(new TankManager());
		this.managers.add(new CardManagerRisikoNew());
		this.managers.add(new PhaseManagerRisikoNew());
	}
	
	private void setMediator() {
		for (IManager manager : this.managers) {
			manager.setMediator(this.getMediator());
		}
		this.mediator.initManagers();
	}
}