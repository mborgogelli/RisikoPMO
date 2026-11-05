package it.uniurb.pmo.framework.management;

import it.uniurb.pmo.framework.management.interfaces.IMediator;
import it.uniurb.pmo.framework.management.interfaces.ITurnManager;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.players.PlayerTurnStatus;
import it.uniurb.pmo.framework.turn.command.IGameCommand;
import it.uniurb.pmo.framework.turn.command.IGameCommandReceiver;
import it.uniurb.pmo.framework.turn.event.EGameEventType;
import it.uniurb.pmo.framework.turn.event.GameEvent;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEventPublisher;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEventReceiver;
import it.uniurb.pmo.framework.turn.phase.IPhase;
import it.uniurb.pmo.framework.turn.phase.IPhasePayload;
import it.uniurb.pmo.framework.turn.phase.request.IGameRequest;
import it.uniurb.pmo.framework.turn.phase.result.ICombatResultDTO;
import it.uniurb.pmo.framework.turn.phase.result.IPhaseResult;
import it.uniurb.pmo.framework.turn.snapshot.GameSnapshotDTO;
import it.uniurb.pmo.framework.turn.snapshot.IGameSnapshot;
import it.uniurb.pmo.framework.turn.snapshot.IPlayerSnapshot;
import it.uniurb.pmo.framework.turn.snapshot.PlayerSnapshot;

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
		List<IPlayer> activePlayers = this.players.stream()
				.filter(player -> player.getTurnStatus() != PlayerTurnStatus.ELIMINATED)
				.toList();
		return activePlayers.size() == 1
				? Optional.of(activePlayers.getFirst())
				: Optional.empty();
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
		this.currentPhase = (this.phases != null && !this.phases.isEmpty())
				? this.phases.getFirst()
				: null;

		this.publish(this.updateEvent(EGameEventType.TURN_STARTED));

		if (this.currentPhase != null) {
			this.startPhase(this.currentPhase);
		}
	}

	@Override
	public void endTurn(IPlayer player) {
		this.checkWinner().ifPresentOrElse(
				winningPlayer -> {
					this.publish(this.updateEvent(EGameEventType.GAME_ENDED));
					this.mediator.notifyWinner(winningPlayer);
				},
				() -> this.startTurn(this.getNextPlayer())
		);
	}

	@Override
	public IPlayer getNextPlayer() {
		int currentIndex = (this.currentPlayer == null) ? -1 : this.players.indexOf(this.currentPlayer);

		for (int i = 1; i <= this.players.size(); i++) {
			int nextIndex = (currentIndex + i) % this.players.size();
			IPlayer candidate = this.players.get(nextIndex);

			// Trova il prossimo giocatore attivo
			if (candidate.getTurnStatus() == PlayerTurnStatus.ACTIVE) {
				/*
				 * Se avevamo già un giocatore attivo e l'indice del prossimo è minore o uguale
				 * all'indice corrente (wrap-around della lista circolare), incrementa il turno.
				 */
				if (currentIndex >= 0 && nextIndex <= currentIndex) {
					this.currentTurn++;
				}
				return candidate;
			}
		}

		throw new IllegalStateException("Nessun giocatore attivo disponibile.");
	}

	@Override
	public void startPhase(IPhase currentPhase) {
		this.currentPhase = currentPhase;
		this.processPhaseResult(currentPhase.playPhase(this.currentPlayer));
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
	public IGameEvent handleCommand(IGameCommand command) {
		IPhaseResult<? extends IPhasePayload> result = this.currentPhase.handleCommand(command);
		return this.processPhaseResult(result);
	}

	@Override
	public boolean isValidCommand(IGameCommand command) {
		return this.currentPhase.isValidCommand(command);
	}

	@Override
	public Map<String, IPlayerSnapshot> playerSnapshot() {
		return this.players.stream()
				.collect(Collectors.toUnmodifiableMap(IPlayer::getName, this::playerState));
	}

	/**
	 * Metodo astratto che deve restituire la lista delle fasi del gioco per la specializzazione concreta.
	 */
	protected abstract List<IPhase> createPhases();

	/**
	 * Metodo astratto per ottenere la lista di eventuali fasi di setup del gioco.
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
	 * ha già terminato il piazzamento iniziale.
	 */
	protected IPlayer getNextSetupPlayer() {
		return this.getNextPlayer();
	}

	protected List<IPlayer> shufflePlayers(List<IPlayer> players) {
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

	/**
	 * Crea un nuovo snapshot di gioco variante a partire da uno snapshot comune.
	 * @param commonSnapshot lo snapshot comune da cui partire
	 * @param phasePayload il payload della fase corrente
	 * @return il nuovo snapshot di gioco variante
	 */
	protected IGameSnapshot createVariantSnapshot(GameSnapshotDTO commonSnapshot, IPhasePayload phasePayload) {
		return commonSnapshot;
	}

	private void startSetupPhases() {
		this.setupInProgress = true;
		this.currentSetupPhaseIndex = 0;
		this.startSetupPhase();
	}

	private void startSetupPhase() {
		this.currentPlayer = this.getNextSetupPlayer();
		this.currentPhase = this.setupPhases.get(this.currentSetupPhaseIndex);
		this.publish(this.updateEvent(EGameEventType.PHASE_STARTED));
		this.processPhaseResult(this.currentPhase.playPhase(this.currentPlayer));
	}

	//TODO Non abbastanza generica
	private IGameEvent processPhaseResult(IPhaseResult<? extends IPhasePayload> result) {
		IGameEvent event = result.phasePayload()
				.map(this::createPayloadEvent)
				.orElseGet(() -> result.isCompleted()
						? this.updateEvent(EGameEventType.PHASE_COMPLETED)
						: null);

		this.publish(event);
		if (result.isCompleted()) {
			this.advanceAfterCompletedPhase();
		}
		return event;
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

	private void publish(IGameEvent event) {
		if (event != null) {
			this.observers.forEach(observer -> observer.onGameEvent(event));
		}
	}

	private IPlayerSnapshot playerState(IPlayer player) {
		Map<String, Map<ITokenType, Integer>> playerTerritories = this.mediator.getZonesOwnedBy(player)
				.stream()
				.collect(Collectors.toMap(t -> t, t -> this.mediator.getZoneTokens(t)));
		Map<ITokenType, Integer> playerTokens = this.mediator.getTokensOwnedBy(player);
		return new PlayerSnapshot(
				player.getName(),
				player.getColor(),
				player.getTurnStatus(),
				playerTerritories,
				playerTokens);
	}

	private GameEvent updateEvent(EGameEventType eventType) {
		GameSnapshotDTO commonSnapshot = new GameSnapshotDTO(
				this.currentTurn,
				Optional.ofNullable(this.currentPhase).map(phase -> phase.getPhaseType().phaseCode()),
				Optional.ofNullable(this.currentPlayer).map(IPlayer::getName),
				this.playerSnapshot(),
				Optional.empty(),
				this.checkWinner().map(IPlayer::getName));
		return new GameEvent(eventType, this.createVariantSnapshot(commonSnapshot, null));
	}

	private GameEvent createPayloadEvent(IPhasePayload payload) {
		EGameEventType eventType = payload instanceof ICombatResultDTO
				? EGameEventType.COMBAT_RESOLVED
				: EGameEventType.CHOICE_REQUIRED;
		Optional<IGameRequest> pendingAction = (payload instanceof IGameRequest action
				&& !(payload instanceof ICombatResultDTO))
				? Optional.of(action)
				: Optional.empty();

		GameSnapshotDTO commonSnapshot = new GameSnapshotDTO(
				this.currentTurn,
				Optional.ofNullable(this.currentPhase).map(phase -> phase.getPhaseType().phaseCode()),
				Optional.ofNullable(this.currentPlayer).map(IPlayer::getName),
				this.playerSnapshot(),
				pendingAction,
				this.checkWinner().map(IPlayer::getName));
		return new GameEvent(eventType, this.createVariantSnapshot(commonSnapshot, payload));
	}

}