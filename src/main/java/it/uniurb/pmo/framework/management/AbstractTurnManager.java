package it.uniurb.pmo.framework.management;

import it.uniurb.pmo.framework.management.interfaces.IMediator;
import it.uniurb.pmo.framework.management.interfaces.ITurnManager;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.IPhase;

import java.util.List;

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

	/**
	 * Inizializza la lista di fasi che compongono un turno di gioco.
	 * Le classi figlie devono chiamare questo metodo durante il loro processo di startup/init.
	 */
	protected void initPhases(List<IPhase> phases) {
		this.phases = phases;
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
		currentPhase.playPhase(this.currentPlayer);
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
}
