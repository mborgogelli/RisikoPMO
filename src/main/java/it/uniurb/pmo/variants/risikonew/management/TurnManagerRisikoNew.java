package it.uniurb.pmo.variants.risikonew.management;

import java.util.*;

import it.uniurb.pmo.framework.management.AbstractTurnManager;
import it.uniurb.pmo.framework.turn.IPhase;
import it.uniurb.pmo.variants.risikonew.management.interfaces.ITurnManagerRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.gamecoordinator.IGameCoordinatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.phase_combat.CombatPhase;
import it.uniurb.pmo.variants.risikonew.turn.phase_reinforce.ReinforcePhase;
import it.uniurb.pmo.variants.risikonew.turn.phase_strategic.StrategicPhase;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.gamecoordinator.GameCoordinatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.phase_initialplacement.InitialPlacementPhase;

public class TurnManagerRisikoNew extends AbstractTurnManager implements ITurnManagerRisikoNew {

	//TODO dove passa a true?
	private boolean isReady;

	public TurnManagerRisikoNew(IGameCoordinatorRisikoNew gameCoordinatorRisikoNew) {
		super(gameCoordinatorRisikoNew);
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


	@Override
	public void startGame() {
		if(this.isReady) {
			super.initPhases();
			this.runInitialPlacement();
			super.startGame();
		}
	}

	@Override
	protected List<IPhase> createPhases() {
		IMediatorRisikoNew mediator = (IMediatorRisikoNew) super.getMediator();
		IGameCoordinatorRisikoNew coordinator = (IGameCoordinatorRisikoNew) super.getGameCoordinator();
		return List.of(new ReinforcePhase(mediator, coordinator), new CombatPhase(mediator, coordinator), new StrategicPhase(mediator, coordinator));
	}

	@Override
	protected List<IPlayer> shufflePlayers(List<IPlayer> players){
		return super.shufflePlayers(players);
	}

	private void runInitialPlacement() {
		IMediatorRisikoNew mediator = (IMediatorRisikoNew) super.getMediator();
		GameCoordinatorRisikoNew coordinator = new GameCoordinatorRisikoNew();
		InitialPlacementPhase initialPlacement = new InitialPlacementPhase(mediator, coordinator);
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

}
