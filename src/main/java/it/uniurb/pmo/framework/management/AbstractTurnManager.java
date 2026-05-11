package it.uniurb.pmo.framework.management;

import it.uniurb.pmo.framework.management.interfaces.IMediator;
import it.uniurb.pmo.framework.management.interfaces.ITurnManager;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.IPhase;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

/**
 * Classe astratta che modella una macchina a stati finiti per la gestione dei turni di gioco.
 * Implementa l'interfaccia ITurnManager e fornisce un'implementazione di base per la gestione dei turni,
 * lasciando ai sottotipi la responsabilità di definire l'ordine delle fasi e il reset del contatore delle fasi.
 */
public abstract class AbstractTurnManager implements ITurnManager {

	private IMediator mediator;
	private IPlayer currentPlayer;
	private int currentTurn;
	private int currentPhaseIndex;
	private List<IPhase> phases;
	private List<IPlayer> players;

	/**
	 * Metodo astratto che deve restituire la lista delle fasi del gioco per la specializzazione concreta.
	 */
	protected abstract List<IPhase> createPhases();

	@Override
	public void initializeGame(List<IPlayer> players) {
		this.players = this.shufflePlayers(players);
		this.currentTurn = 1;
		this.currentPhaseIndex = 0;
		this.initPhases(this.createPhases());
	}

	@Override
	public void startGame() {
			this.playTurn(this.getNextPlayer());
	}

	@Override
	public int getPlayedTurns() {
		return this.currentTurn;
	}

	@Override
	public Optional<IPlayer> checkWinner() {
		Optional<IPlayer> winner = Optional.empty();
		if (this.getMediator() != null && this.getMediator().checkVictory(this.getCurrentPlayer())) {
			winner = Optional.of(this.getCurrentPlayer());
		}
		return winner;
	}

	@Override
	public void setMediator(IMediator mediator) {
		this.mediator = mediator;
		this.mediator.registerManager(this);
	}

	@Override
	public void playTurn(IPlayer player) {
		if (this.phases == null || this.phases.isEmpty()) {
			throw new IllegalStateException("Le fasi del turno non sono state inizializzate. Chiamare initPhases() prima di playTurn().");
		}
		this.currentPlayer = player;
		this.currentPhaseIndex = 0;
		this.playPhase(this.phases.getFirst());
	}

	@Override
	public void endTurn(IPlayer player){
		if(this.checkWinner().isPresent()) {
			this.mediator.notifyWinner(this.checkWinner().get());
		}else{
			this.playTurn(this.getNextPlayer());
		}
	}

	@Override
	public IPlayer getNextPlayer() {
		int currentIndex = this.players.indexOf(this.getCurrentPlayer());
		IPlayer nextPlayer = null;
		boolean found = false;

		for (int i = 1; i <= this.players.size() && !found; i++) {
			int nextIndex = (currentIndex + i) % this.players.size();
			IPlayer candidate = this.players.get(nextIndex);

			if (this.mediator.isPlayerActive(candidate)) {
				if (nextIndex == 0 && currentIndex >= 0) {
					this.currentTurn++;
				}
				nextPlayer = candidate;
				found = true;
			}
		}

		if (!found) {
			throw new IllegalStateException("Nessun giocatore attivo disponibile.");
		}

		return nextPlayer;
	}

	@Override
	public void playPhase(IPhase currentPhase){
		currentPhase.playPhase(this.currentPlayer, this.mediator);
	}

	@Override
	public int nextPhase() {
		this.currentPhaseIndex++;

		if (this.currentPhaseIndex > this.phases.size()) {
			this.endTurn(this.currentPlayer);
		} else {
			this.playPhase(this.phases.get(this.currentPhaseIndex));
		}
		return this.currentPhaseIndex;
	}

	@Override
	public IPlayer getCurrentPlayer() {
		return this.currentPlayer;
	}

	protected IMediator getMediator() {
		return this.mediator;
	}

	protected List<IPlayer> shufflePlayers(List<IPlayer> players){
		List<IPlayer> shuffledPlayers = new ArrayList<>(players);
		Collections.shuffle(shuffledPlayers);
		return shuffledPlayers;
	}

	/**
	 * Inizializza la lista di fasi che compongono un turno di gioco.
	 * Le classi figlie devono chiamare questo metodo durante il loro processo di startup/init.
	 */
	protected void initPhases(List<IPhase> phases) {
		this.phases = phases;
	}

	protected List<IPlayer> getPlayers() {
		return this.players;
	}
}
