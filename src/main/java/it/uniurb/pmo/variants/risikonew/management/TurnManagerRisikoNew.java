package it.uniurb.pmo.variants.risikonew.management;

import java.util.*;

import it.uniurb.pmo.framework.management.AbstractTurnManager;
import it.uniurb.pmo.framework.turn.IPhase;
import it.uniurb.pmo.variants.risikonew.management.interfaces.ITurnManagerRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.CombatPhase;
import it.uniurb.pmo.variants.risikonew.turn.ReinforcePhase;
import it.uniurb.pmo.variants.risikonew.turn.StrategicPhase;

public class TurnManagerRisikoNew extends AbstractTurnManager implements ITurnManagerRisikoNew {

	private boolean isReady;

	public TurnManagerRisikoNew() {
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
			this.runInitialPlacement();
			super.startGame();
		}
	}

	@Override
	protected List<IPhase> createPhases() {
		// Risiko Classico: prima rinforzo, poi attacco, poi spostamento (strategica)
		return List.of(new ReinforcePhase(), new CombatPhase(), new StrategicPhase());
	}

	private void runInitialPlacement() {

	}



}
