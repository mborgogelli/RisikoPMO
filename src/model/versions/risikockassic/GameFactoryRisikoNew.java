package model.versions.risikockassic;

import java.util.List;

import model.management.CardManager;
import model.management.Mediator;
import model.management.PhaseManager;
import model.management.TokenManager;
import model.management.interfaces.IGameFactory;
import model.management.interfaces.IManager;
import model.management.interfaces.IMediator;
import model.versions.risikockassic.interfaces.IMapManagerRisikoNew;
import model.versions.risikockassic.managers.CardManagerRisikoNew;
import model.versions.risikockassic.managers.MapManagerRisikoNew;
import model.versions.risikockassic.managers.PhaseManagerRisikoNew;
import model.versions.risikockassic.managers.MediatorRisikoNew;
import model.versions.risikockassic.managers.TankManager;

public class GameFactoryRisikoNew implements IGameFactory {
	
	
	private IMapManagerRisikoNew mapManager;
	private TokenManager tokenManager;
	private CardManager cardManager;
	private PhaseManager phaseManager;
	
	private Mediator mediator;
	private List<IManager> managers;
	
	@Override
	public Mediator getMediator() {
		return this.mediator;
	}

	@Override
	public List<IManager> getManagers() {
		if (this.managers == null) {
			this.createManagers();
		}
		return this.managers;
	}
	
	private void createManagers() {
		this.mediator = new MediatorRisikoNew();
		this.managers.add(new MapManagerRisikoNew());
		this.managers.add(new TankManager());
		this.managers.add(new CardManagerRisikoNew());
		this.managers.add(new PhaseManagerRisikoNew());
	}



	
	
	
}