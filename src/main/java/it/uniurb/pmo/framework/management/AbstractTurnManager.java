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
	private int currentPhaseIndex = 0;
	private List<IPhase> phases;

	protected List<IPlayer> players;
	protected int currentTurn;
	protected boolean isReady;

	/**
	 * Inizializza la lista di fasi che compongono un turno di gioco.
	 * Le classi figlie devono chiamare questo metodo durante il loro processo di startup/init.
	 */
	protected void initPhases(List<IPhase> phases) {
		this.phases = phases;
	}

	/**
	 * Metodo astratto che deve restituire la lista delle fasi del gioco per la specializzazione concreta.
	 */
	protected abstract List<IPhase> createPhases();

	@Override
	public void initializeGame(List<IPlayer> players) {
		this.players = this.shufflePlayers(players);
		this.currentTurn = 0;
		this.initPhases(this.createPhases());
		this.isReady = true;
	}

	@Override
	public void startGame() {
		if (this.isReady && this.players != null && !this.players.isEmpty()) {
			this.playTurn(this.players.getFirst());
		}
	}

	@Override
	public Boolean isReady() {
		return this.isReady;
	}

	protected List<IPlayer> shufflePlayers(List<IPlayer> players){
		List<IPlayer> shuffledPlayers = new ArrayList<>(players);
		Collections.shuffle(shuffledPlayers);
		return shuffledPlayers;
	}

	@Override
	public int getCount() {
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
}
