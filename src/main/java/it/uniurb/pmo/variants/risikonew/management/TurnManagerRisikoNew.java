package it.uniurb.pmo.variants.risikonew.management;

import it.uniurb.pmo.framework.management.AbstractTurnManager;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.PlayerTurnStatus;
import it.uniurb.pmo.framework.turn.IPhase;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.interfaces.ITurnManagerRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.gamecoordinator.IGameCoordinatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.phase_combat.CombatPhase;
import it.uniurb.pmo.variants.risikonew.turn.phase_initialplacement.InitialPlacementPhase;
import it.uniurb.pmo.variants.risikonew.turn.phase_reinforce.ReinforcePhase;
import it.uniurb.pmo.variants.risikonew.turn.phase_strategic.StrategicPhase;

import java.util.List;

public class TurnManagerRisikoNew extends AbstractTurnManager implements ITurnManagerRisikoNew {

	IMediatorRisikoNew mediator;
	IGameCoordinatorRisikoNew coordinator;

	//TODO dove passa a true?
	private boolean isReady;

	public TurnManagerRisikoNew(IGameCoordinatorRisikoNew gameCoordinator) {
		super();
		this.coordinator = gameCoordinator;
		this.isReady = false;
	}

	@Override
	public Boolean isReady() {
		return this.isReady;
	}

	@Override
	public void resetGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopGame() {
		// TODO Auto-generated method stub

	}
/*
	@Override
	public void startGame() {
		if(this.isReady) {
			this.runInitialPlacement();
			super.startGame();
		}
	}
*/
	@Override
	protected List<IPhase> createPhases() {
		this.initMediatorAndCoordinator();
		return List.of(new ReinforcePhase(mediator), new CombatPhase(mediator, coordinator), new StrategicPhase(mediator, coordinator));
	}

	protected List<IPhase> createSetupPhases() {
		return List.of(new InitialPlacementPhase(mediator) );
	}

	@Override
	protected List<IPlayer> shufflePlayers(List<IPlayer> players){
		return super.shufflePlayers(players);
	}

	@Override
	protected boolean hasMoreSetupWork(IPhase completedPhase) {
		return this.getPlayers().stream()
				.anyMatch(player -> this.mediator.getPlayerTank(player) > 0);
	}

	@Override
	protected IPlayer getNextSetupPlayer() {
		List<IPlayer> players = this.getPlayers();
		IPlayer currentPlayer = this.getCurrentPlayer();
		int currentIndex = players.indexOf(currentPlayer);
		IPlayer nextPlayer = null;
		boolean found = false;

		for (int offset = 1; offset <= players.size() && !found; offset++) {
			int nextIndex = (currentIndex + offset) % players.size();
			IPlayer candidate = players.get(nextIndex);

			boolean active = candidate.getTurnStatus() == PlayerTurnStatus.ACTIVE;
			boolean hasTanksToPlace = this.mediator.getPlayerTank(candidate) > 0;

			if (active && hasTanksToPlace) {
				nextPlayer = candidate;
				found = true;
			}
		}
		if (!found) {
			throw new IllegalStateException(
					"Nessun giocatore disponibile per il piazzamento iniziale."
			);
		}

		return nextPlayer;
	}


	/*private void runInitialPlacement() {
		InitialPlacementPhase initialPlacement = new InitialPlacementPhase(mediator);
		while (haveRemainingTanks(mediator)) {
			for (IPlayer player : super.getPlayers()) {
				if (mediator.getPlayerTank(player) > 0) {
					initialPlacement.playPhase(player);
				}
			}
		}
	}

	private boolean haveRemainingTanks(IMediatorRisikoNew mediator) {
		return super.getPlayers().stream().anyMatch(p -> mediator.getPlayerTank(p) > 0);
	}
*/
	private void initMediatorAndCoordinator() {
		this.mediator = (IMediatorRisikoNew) super.getMediator();
	}
}
