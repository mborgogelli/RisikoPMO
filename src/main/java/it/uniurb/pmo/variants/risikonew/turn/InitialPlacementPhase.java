package it.uniurb.pmo.variants.risikonew.turn;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import it.uniurb.pmo.framework.management.interfaces.IMediator;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.IPhase;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;

public class InitialPlacementPhase implements IPhase {

	public final static int MAX_DEPLOYABLE = 3;
	private final IMediatorRisikoNew mediator;
	private IPlayer player;

	public InitialPlacementPhase(IMediator mediator) {
		this.mediator = (IMediatorRisikoNew) mediator;
	}

	@Override
	public int getId() {
		return 0;
	}

	@Override
	public void playPhase(IPlayer player, IMediator mediator) {
		this.player = player;
		this.deployTanks(this.player);

	}

	private void deployTanks(IPlayer player) {
		int remaining = this.mediator.getPlayerTank(this.player);
		if (remaining > 0){
			int tanksToDeploy = Math.min(MAX_DEPLOYABLE, remaining);
			Map<String, Integer> targetZones = this.mediator.acquireTargetZones(player, tanksToDeploy);
			targetZones.forEach((z, t) -> this.mediator.deployTank(this.player, z, t));
		}else {
			throw new RuntimeException("Not enough tanks to deploy.");
		}
	}

	@Override
	public void clearPhase() {
		this.player = null;
	}

	//TODO Verifica del totale da deployare
}
