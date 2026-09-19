package it.uniurb.pmo.variants.risikonew.turn.phase_initialplacement;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.IPhase;
import it.uniurb.pmo.framework.turn.command.DeployCommand;
import it.uniurb.pmo.framework.turn.command.IGameCommand;
import it.uniurb.pmo.framework.turn.dto.IDeployRequestDTO;
import it.uniurb.pmo.framework.turn.dto.IPlayerStateDTO;
import it.uniurb.pmo.framework.turn.event.DeployRequestEvent;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.dto.DeployRequestRisikoNewDTO;

import java.util.List;
import java.util.Map;

public class InitialPlacementPhase implements IPhase {

	public final static int MAX_DEPLOYABLE = 3;

	private IPlayer player;
    private final IMediatorRisikoNew mediator;

    public InitialPlacementPhase(IMediatorRisikoNew mediator) {
		this.mediator = mediator;
    }


	@Override
	public IGameEvent<IDeployRequestDTO> playPhase(IPlayer player) {
		this.player = player;
		return new DeployRequestEvent(this.deployRequest());
	}

    @Override
	public void clearPhase() {
		this.player = null;
	}

	@Override
	public IGameEvent<IPlayerStateDTO> handleCommand(IGameCommand command) {
		if (this.isValidCommand(command)){
			DeployCommand deployCommand = (DeployCommand) command;
			this.deployTanks(deployCommand.deployment());
			return null;
		}
		return null;
	}

	@Override
	public boolean isValidCommand(IGameCommand command) {
		boolean valid = command instanceof DeployCommand;
		List<String> playerTerritories = this.mediator.getZonesOwnedBy(this.player);

		if (valid) {
			DeployCommand deployCommand = (DeployCommand) command;

			valid = !deployCommand.deployment().isEmpty()
					&& deployCommand.deployment().values().stream()
						.allMatch(tanks -> tanks != null && tanks > 0)
					&& deployCommand.deployment().keySet().stream()
						.allMatch(playerTerritories::contains)
					&& deployCommand.deployment().values().stream()
						.mapToInt(Integer::intValue)
						.sum() <= MAX_DEPLOYABLE;
		}
		return valid;
	}

	private DeployRequestRisikoNewDTO deployRequest() {
		int remaining = this.mediator.getPlayerTank(this.player);
		if (remaining > 0) {
			int tanksToDeploy = Math.min(MAX_DEPLOYABLE, remaining);
            List<String> deployableZones = this.mediator.getZonesOwnedBy(player);
			return new DeployRequestRisikoNewDTO(player.getName(), player.getColor(), deployableZones, tanksToDeploy);
		} else {
			throw new RuntimeException("Not enough tanks to deploy.");
		}
	};

	private void deployTanks(Map<String, Integer> targetZones) {
		targetZones.forEach((zone, tanks) -> this.mediator.deployTank(this.player, zone, tanks));
	}

}
