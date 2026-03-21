package it.uniurb.pmo.model.versions.risikockassic.management;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import it.uniurb.pmo.model.management.TurnManager;
import it.uniurb.pmo.model.players.IPlayer;
import it.uniurb.pmo.model.turn.IPhase;
import it.uniurb.pmo.model.utils.EnumColors;
import it.uniurb.pmo.model.utils.EnumPhase;

public class TurnManagerRisikoNew extends TurnManager {
	
	private boolean isReady;
	private Map<IPlayer,Integer> myTurn;
	private List<EnumColors> playOrder;
	
	
	public TurnManagerRisikoNew() {
		this.isReady = false;
	}
	
	@Override
	public void initializeGame(List<IPlayer> players) {
		// TODO Auto-generated method stub
		this.isReady = true;
		
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
	protected List<EnumPhase> getOrderedPhase() {
		return List.of();
	}

	@Override
	public void playTurn(IPlayer p) {

	}

	@Override
	public void endTurn(IPlayer p) {

	}

	@Override
	public void playPhase(IPhase phase) {

	}

	@Override
	public int nextPhase() {
		return 0;
	}

	@Override
	public IPlayer getCurrentPlayer() {
		return null;
	}

	@Override
	public IPlayer getNextPlayer() {
		return null;
	}

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public Optional<IPlayer> checkVictory() {
		return Optional.empty();
	}

	@Override
	public void startTurn() {

	}
}
