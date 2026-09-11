package it.uniurb.pmo.framework.management;

import it.uniurb.pmo.framework.management.interfaces.IMediator;
import it.uniurb.pmo.framework.management.interfaces.ITurnManager;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.PlayerTurnStatus;
import it.uniurb.pmo.framework.turn.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Classe astratta che modella una macchina a stati finiti per la gestione dei turni di gioco.
 * Implementa l'interfaccia ITurnManager e fornisce un'implementazione di base per la gestione dei turni,
 * lasciando ai sottotipi la responsabilità di definire l'ordine delle fasi e il reset del contatore delle fasi.
 */
public abstract class AbstractTurnManager implements ITurnManager, IGameEventPublisher {

	private IMediator mediator;
	private IGameCoordinator gameCoordinator;
	private IPlayer currentPlayer;
	private int currentTurn;
	private int currentPhaseIndex;
	private List<IPhase> phases;
	private List<IPlayer> players;

	private final List<IGameEventReceiver> observers;

	public AbstractTurnManager() {
		this.observers = new ArrayList<>();
	}

	@Override
	public void initializeGame(List<IPlayer> players) {
		this.players = this.shufflePlayers(players);
		this.currentTurn = 1;
		this.currentPhaseIndex = 0;
		this.initPhases();
	}

	@Override
	public void startGame() {
		this.startTurn(this.getNextPlayer());
	}

	@Override
	public int getPlayedTurns() {
		return this.currentTurn;
	}

	@Override
	public Optional<IPlayer> checkWinner() {
		Optional<IPlayer> winner = Optional.empty();
		if (this.mediator.checkVictory(this.getCurrentPlayer())) {
			winner = Optional.of(this.getCurrentPlayer());
		}
		return winner;
	}

	@Override
	public void setMediator(IMediator mediator) {
		if (this.mediator == null) {
			this.mediator = mediator;
			this.mediator.registerManager(this);
		}
	}

	@Override
	public void startTurn(IPlayer player) {
		this.currentPlayer = player;
		this.currentPhaseIndex = 1;
		//TODO
		//this.notifyObservers(new GameEvent(EGameEventType.TURN_STARTED, player.getName(), this.currentTurn, null));
		if (this.phases != null && !this.phases.isEmpty()) {
			this.startPhase(this.phases.getFirst());
		}
	}

	@Override
	public void endTurn(IPlayer player){
		if(this.checkWinner().isPresent()) {
			this.mediator.notifyWinner(this.checkWinner().get());
		}else{
			this.startTurn(this.getNextPlayer());
		}
	}

	@Override
	public IPlayer getNextPlayer() {
		IPlayer currentPlayer = this.getCurrentPlayer();
		int currentIndex = (currentPlayer == null) ? -1 : this.players.indexOf(currentPlayer);
		IPlayer nextPlayer = null;
		boolean found = false;

		for (int i = 1; i <= this.players.size() && !found; i++) {
			int nextIndex = (currentIndex + i) % this.players.size();
			IPlayer candidate = this.players.get(nextIndex);


			 // Trova il prossimo giocatore attivo
			if (candidate.getPlayerTurnStatus() == PlayerTurnStatus.ACTIVE) {
				/**
				 * Se l'indice del prossimo giocatore è uguale a zero e l'indice corrente
				 * è maggiore o uguale a zero, incrementa il turno corrente.
				 */
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
	public void startPhase(IPhase currentPhase){
		// TODO
		//this.notifyObservers(new GameEvent(EGameEventType.PHASE_STARTED, this.currentPlayer.getName(), this.currentTurn, currentPhase.getPhaseId()));
		currentPhase.playPhase(this.currentPlayer);
	}

	@Override
	public void addObserver(IGameEventReceiver observer) {
		this.observers.add(observer);
	}

	@Override
	public void removeObserver(IGameEventReceiver observer) {
		this.observers.remove(observer);
	}

	@Override
	public int nextPhase() {
		this.currentPhaseIndex++;

		if (this.currentPhaseIndex > this.phases.size()) {
			this.endTurn(this.currentPlayer);
		} else {
			this.startPhase(this.phases.get(this.currentPhaseIndex));
		}
		return this.currentPhaseIndex;
	}

	@Override
	public IPlayer getCurrentPlayer() {
		return this.currentPlayer;
	}

	@Override
	public List<IPlayer> getPlayers() {
		return Collections.unmodifiableList(this.players);
	}

	/**
	 * Metodo astratto che deve restituire la lista delle fasi del gioco per la specializzazione concreta.
	 */
	protected abstract List<IPhase> createPhases();

	protected List<IPlayer> shufflePlayers(List<IPlayer> players){
		List<IPlayer> shuffledPlayers = new ArrayList<>(players);
		Collections.shuffle(shuffledPlayers);
		return shuffledPlayers;
	}

	/**
	 * Inizializza la lista di fasi che compongono un turno di gioco.
	 * Le classi figlie devono chiamare questo metodo durante il loro processo di startup/init.
	 */
	protected void initPhases() {
		this.phases = this.createPhases();
	}

	protected IGameCoordinator getGameCoordinator() {
		return this.gameCoordinator;
	}

	protected IMediator getMediator() {
		return this.mediator;
	}

	private void notifyObservers(IGameEvent event) {
		this.observers.forEach(observer -> observer.onGameEvent(event));
	}

}
