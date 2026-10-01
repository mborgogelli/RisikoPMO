package it.uniurb.pmo.framework.management;

import it.uniurb.pmo.framework.management.interfaces.IMediator;
import it.uniurb.pmo.framework.management.interfaces.ITurnManager;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.players.PlayerTurnStatus;
import it.uniurb.pmo.framework.turn.IPhase;
import it.uniurb.pmo.framework.turn.command.IGameCommand;
import it.uniurb.pmo.framework.turn.command.IGameCommandReceiver;
import it.uniurb.pmo.framework.turn.dto.GameSnapshotDTO;
import it.uniurb.pmo.framework.turn.dto.IGameState;
import it.uniurb.pmo.framework.turn.dto.IPlayerStateDTO;
import it.uniurb.pmo.framework.turn.dto.PlayerStateDTO;
import it.uniurb.pmo.framework.turn.event.EGameEventType;
import it.uniurb.pmo.framework.turn.event.GameEvent;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEventPublisher;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEventReceiver;
import it.uniurb.pmo.framework.turn.event.interfaces.IPhaseResult;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe astratta che modella una macchina a stati finiti per la gestione dei turni di gioco.
 * Implementa l'interfaccia ITurnManager e fornisce un'implementazione di base per la gestione dei turni,
 * lasciando ai sottotipi la responsabilità di definire l'ordine delle fasi e il reset del contatore delle fasi.
 */
public abstract class AbstractTurnManager implements ITurnManager, IGameEventPublisher, IGameCommandReceiver {

	private IMediator mediator;

	private List<IPlayer> players;
	private IPlayer currentPlayer;

	private IPhase currentPhase;
	private int currentTurn;
	private int currentPhaseIndex;
	private int currentSetupPhaseIndex;
	private boolean setupInProgress;

	private List<IPhase> setupPhases;
	private List<IPhase> phases;

	private final List<IGameEventReceiver> observers;

	public AbstractTurnManager() {
		this.observers = new ArrayList<>();
	}

	@Override
	public void initializeGame(List<IPlayer> players) {
		this.players = this.shufflePlayers(players);
		this.initPhases();
		this.currentPhase = null;
		this.currentPhaseIndex = 0;
		this.currentSetupPhaseIndex = 0;
		this.setupInProgress = false;
		this.currentTurn = 1;
	}

	@Override
	public void startGame() {
		if (this.setupPhases.isEmpty()) {
			this.startTurn(this.getNextPlayer());
		} else {
			this.startSetupPhases();
		}
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
		this.currentPhaseIndex = 0;
		this.publish(updateEvent(EGameEventType.TURN_STARTED));
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
			if (candidate.getTurnStatus() == PlayerTurnStatus.ACTIVE) {
				/*
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
		this.currentPhase = currentPhase;
		IGameEvent<? extends IGameState> event = currentPhase.playPhase(this.currentPlayer);
		this.publish(event);
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

		if (this.currentPhaseIndex >= this.phases.size()) {
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

	@Override
	public IGameEvent<? extends IGameState> handleCommand(IGameCommand command) {
		IPhaseResult result = this.currentPhase.handleCommand(command);
		IGameEvent<? extends IGameState> event = null;

		if (result.isCompleted()) {
			event = updateEvent(EGameEventType.PHASE_COMPLETED);
		} else if (result.choiceRequired().isPresent()) {
			event = new GameEvent<>(EGameEventType.CHOICE_REQUIRED,
					result.choiceRequired().get());
		}

		this.publish(event);

		if (result.isCompleted()) {
			this.advanceAfterCompletedPhase();
		}

		return event;
	}

	@Override
	public boolean isValidCommand(IGameCommand command) {
		return this.currentPhase.isValidCommand(command);
	}

	@Override
	public Map<String, IPlayerStateDTO> gameSnapshot() {
		return this.players.stream()
				.collect(Collectors.toUnmodifiableMap(IPlayer::getName, this::playerState));
	}

	/**
	 * Metodo astratto che deve restituire la lista delle fasi del gioco per la specializzazione concreta.
	 */
	protected abstract List<IPhase> createPhases();

	/**
	 * Metodo astratto per ottenere la lista di eventuali fasi di setup del gioco
	 */
	protected abstract List<IPhase> createSetupPhases();

	/**
	 * Indica se la setup phase appena conclusa deve essere proposta nuovamente.
	 * Il caso tipico è il piazzamento iniziale: la stessa fase viene ripetuta per
	 * i giocatori finché esistono ancora carri iniziali da schierare.
	 */
	protected boolean hasMoreSetupWork(IPhase completedPhase) {
		return false;
	}

	/**
	 * Restituisce il prossimo giocatore che deve eseguire una setup phase.
	 * Le varianti possono ridefinirlo, ad esempio per saltare un giocatore che
	 * ha gia' terminato il piazzamento iniziale.
	 */
	protected IPlayer getNextSetupPlayer() {
		return this.getNextPlayer();
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
	protected void initPhases() {
		this.phases = this.createPhases();
		this.setupPhases = this.createSetupPhases();
	}

	protected IMediator getMediator() {
		return this.mediator;
	}

	private void startSetupPhases() {
		this.setupInProgress = true;
		this.currentSetupPhaseIndex = 0;
		this.startSetupPhase();
	}

	private void startSetupPhase() {
		this.currentPlayer = this.getNextSetupPlayer();
		this.currentPhase = this.setupPhases.get(this.currentSetupPhaseIndex);
		IGameEvent<? extends IGameState> event = this.currentPhase.playPhase(this.currentPlayer);
		this.publish(event);
	}

	private void advanceAfterCompletedPhase() {
		if (this.setupInProgress) {
			this.advanceSetupPhase();
		} else {
			this.nextPhase();
		}
	}

	private void advanceSetupPhase() {
		IPhase completedPhase = this.setupPhases.get(this.currentSetupPhaseIndex);

		if (this.hasMoreSetupWork(completedPhase)) {
			this.startSetupPhase();
		} else {
			this.currentSetupPhaseIndex++;

			if (this.currentSetupPhaseIndex < this.setupPhases.size()) {
				this.startSetupPhase();
			} else {
				this.setupInProgress = false;
				this.currentPlayer = null;
				this.startTurn(this.getNextPlayer());
			}
		}
	}

	private void publish(IGameEvent<? extends IGameState> event) {
		if (event != null) {
			this.observers.forEach(observer -> observer.onGameEvent(event));
		}
	}

	private IPlayerStateDTO playerState(IPlayer player) {
		Map<String,Map<ITokenType,Integer>> playerTerritories = this.mediator.getZonesOwnedBy(player)
				.stream()
				.collect(Collectors.toMap(t -> t, t -> this.mediator.getZoneTokens(t)));
		Map<ITokenType,Integer> playerTokens = this.mediator.getTokensOwnedBy(player);
		return new PlayerStateDTO(player, playerTerritories, playerTokens);
	}

	private GameEvent<IGameState> updateEvent (EGameEventType eventType) {
		return new GameEvent<>(eventType, new GameSnapshotDTO(this.gameSnapshot()));
	}
}
