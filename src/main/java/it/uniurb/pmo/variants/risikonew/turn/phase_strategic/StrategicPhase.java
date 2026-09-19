package it.uniurb.pmo.variants.risikonew.turn.phase_strategic;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.command.IGameCommand;
import it.uniurb.pmo.framework.turn.dto.FortifyChoiceDTO;
import it.uniurb.pmo.framework.turn.dto.FortifyRequestDTO;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameState;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.gamecoordinator.IGameCoordinatorRisikoNew;

import java.util.List;

public class StrategicPhase implements IStrategicPhase {

	private IPlayer player;
	private final IMediatorRisikoNew mediator;
	private final IGameCoordinatorRisikoNew coordinator;

	public StrategicPhase(IMediatorRisikoNew mediator, IGameCoordinatorRisikoNew coordinator) {
		this.mediator = mediator;
		this.coordinator = coordinator;
	}

	@Override
	public IGameEvent<? extends IGameState> playPhase(IPlayer player) {
		this.player = player;
		List<String> ownedZones = this.mediator.getZonesOwnedBy(player);
		FortifyChoiceDTO choice = this.coordinator.sendFortifyRequest(new FortifyRequestDTO(player, ownedZones));
		this.clearPhase();
		return null;
	}

	@Override
	public void clearPhase() {
		this.player = null;
	}

	@Override
	public IGameEvent<? extends IGameState> handleCommand(IGameCommand command) {
		return null;
	}

	@Override
	public boolean isValidCommand(IGameCommand command) {
		return false;
	}
}
