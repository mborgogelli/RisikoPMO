package it.uniurb.pmo.variants.risikonew.turn;

import java.util.Comparator;
import java.util.List;
import it.uniurb.pmo.framework.management.interfaces.IMediator;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.IPhase;
import it.uniurb.pmo.variants.risikonew.management.MediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;

public class InitialPlacementPhase implements IPhase {

	private IMediatorRisikoNew mediator;

	public InitialPlacementPhase() {
	}

	@Override
	public int getId() {
		return 0;
	}

	@Override
	public void playPhase(IPlayer player, IMediator mediator) {
		this.mediator = (MediatorRisikoNew) mediator;

		int remaining = this.mediator.getPlayerTank(player);
		int toDeploy = Math.min(3, remaining);
		List<String> ownedZones = this.mediator.getZonesOwnedBy(player);
		String targetZone = this.selectPlacementZone(ownedZones);
		this.mediator.deployTank(player, targetZone, toDeploy);
	}

	//TODO Lasciare scegliere al giocatore? Non ricordo
	private String selectPlacementZone(List<String> ownedZones) {
		return ownedZones.stream()
				.min(Comparator.comparingInt(this.mediator::getZoneTank).thenComparing(String::compareTo))
				.get();
	}

	@Override
	public void clearPhase() {

	}
}
