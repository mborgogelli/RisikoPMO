package it.uniurb.pmo.model.versions.risikockassic.turn;

import it.uniurb.pmo.model.management.AbstractTurnManager;
import it.uniurb.pmo.model.players.IPlayer;
import it.uniurb.pmo.model.turn.IPhase;
import it.uniurb.pmo.model.utils.EnumPhase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TurnManagerRisikoNew extends AbstractTurnManager {

    private final List<IPhase> phases;

    public TurnManagerRisikoNew() {
        this.phases = new ArrayList<>();
    }

    @Override
    protected List<EnumPhase> getOrderedPhase() {
        return Arrays.stream(EnumPhase.values()).sorted().toList();
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

    @Override
    public Boolean isReady() {
        return null;
    }

    @Override
    public void initializeGame(List<IPlayer> players) {
        this.initialiazePhases();
    }

    @Override
    public void resetGame() {

    }

    private void initialiazePhases() {
        this.phases.add(new ReinforcePhase());
        this.phases.add(new CombatPhase());
        this.phases.add(new StrategicPhase());
    }
}
