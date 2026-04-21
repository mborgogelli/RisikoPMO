package it.uniurb.pmo.model.versions.risikockassic.management;

import java.util.*;

import it.uniurb.pmo.model.management.AbstractTurnManager;
import it.uniurb.pmo.model.players.IPlayer;
import it.uniurb.pmo.model.turn.IPhase;
import it.uniurb.pmo.model.versions.risikockassic.management.interfaces.ITurnManagerRisikoNew;
import it.uniurb.pmo.model.versions.risikockassic.turn.CombatPhase;
import it.uniurb.pmo.model.versions.risikockassic.turn.ReinforcePhase;
import it.uniurb.pmo.model.versions.risikockassic.turn.StrategicPhase;

public class TurnManagerRisikoNew extends AbstractTurnManager implements ITurnManagerRisikoNew {
	
	private boolean isReady;
	private int currentTurn;
	private List<IPlayer> players;

	public TurnManagerRisikoNew() {
		this.isReady = false;
	}
	
	@Override
	public void initializeGame(List<IPlayer> players) {
		this.players = players;
		this.currentTurn = 0;
		this.initPhases(this.initialiazePhases()); // Inizializza le fasi sulla classe padre
		this.isReady = true;
	}

	@Override
	public void startGame() {
		if (this.isReady && !this.players.isEmpty()) {
			this.playTurn(this.players.getFirst());
		}
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
	public IPlayer getNextPlayer() {
		if (this.getCurrentPlayer() == null) return this.players.getFirst();
		int currentIndex = this.players.indexOf(this.getCurrentPlayer());
		int nextIndex = (currentIndex + 1) % this.players.size();
		if (nextIndex == 0) {
			this.currentTurn++;
		}
		return this.players.get(nextIndex);
	}

	@Override
	public int getCount() {
		return this.currentTurn;
	}

	@Override
	public Optional<IPlayer> checkWinner() {
		return Optional.empty();
	}

	private List<IPhase> initialiazePhases() {
		// Risiko Classico: prima rinforzo, poi attacco, poi spostamento (strategica)
		return List.of(new ReinforcePhase(), new CombatPhase(), new StrategicPhase());
	}
}
