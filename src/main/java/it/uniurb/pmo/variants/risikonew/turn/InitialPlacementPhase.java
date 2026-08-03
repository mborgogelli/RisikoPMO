package it.uniurb.pmo.variants.risikonew.turn;

import java.util.List;
import java.util.Map;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.IPhase;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;

public class InitialPlacementPhase implements IPhase {

	public final static int MAX_DEPLOYABLE = 3;

	private IPlayer player;
	private final IMediatorRisikoNew mediator;
	private final IGameCoordinatorRisikoNew coordinator;
	private final int phaseId;

    public InitialPlacementPhase(IMediatorRisikoNew mediator, IGameCoordinatorRisikoNew coordinator) {
        this.phaseId = 0;
		this.mediator = mediator;
		this.coordinator = coordinator;
    }

    @Override
	public int getPhaseId() {
		return this.phaseId;
	}

	@Override
	public int getStepId() {
		return 0;
	}

	@Override
	public void playPhase(IPlayer player) {
		this.player = player;
		this.deployTanks(this.player);
		this.clearPhase();
	}

	@Override
	public void nextStep(IPlayer player) {
	}

	@Override
	public void clearPhase() {
		this.player = null;
	}

	private void deployTanks(IPlayer player) {
		int remaining = this.mediator.getPlayerTank(this.player);
		if (remaining > 0){
			int tanksToDeploy = Math.min(MAX_DEPLOYABLE, remaining);
			List<String> deployableZones = this.mediator.getZonesOwnedBy(player);
			Map<String, Integer> targetZones = this.coordinator.sendDeployRequest(player, deployableZones, tanksToDeploy);
			this.deployTanks(targetZones, tanksToDeploy);
		} else {
			throw new RuntimeException("Not enough tanks to deploy.");
		}
	}

	private boolean checkMaxDeployable(Map<String, Integer> targetZones, int tanksToDeploy) {
		return targetZones.entrySet().stream()
					.reduce(0, (accumulator, entry) -> accumulator + entry.getValue(), Integer::sum) != tanksToDeploy;
	}

	private void deployTanks(Map<String, Integer> targetZones, int tanksToDeploy) {
		if (this.checkMaxDeployable(targetZones, tanksToDeploy)){
			throw new RuntimeException("Not enough tanks provided to deploy.");
		} else {
			targetZones.forEach((zone, tanks) -> this.mediator.deployTank(this.player, zone, tanks));
		}
	}
}
