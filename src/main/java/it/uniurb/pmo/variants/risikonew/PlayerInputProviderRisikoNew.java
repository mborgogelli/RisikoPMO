package it.uniurb.pmo.variants.risikonew;

import java.util.Map;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.IPlayerInputProvider;
import it.uniurb.pmo.framework.utils.Pair;

/**
 * Implementazione concreta di IPlayerInputProvider per la variante RisikoNew.
 * Acquisisce le scelte del giocatore tramite il canale di comunicazione specifico
 */
public class PlayerInputProviderRisikoNew implements IPlayerInputProvider {

    @Override
    public Map<String, Integer> acquireDeployment(IPlayer player, int tanksToDeploy) {
        // TODO: acquisire la scelta di dispiegamento dal client
        return Map.of();
    }

    @Override
    public Pair<String,String> acquireAttack(IPlayer player) {
        // TODO: acquisire (zonaBersaglio, zonaAttaccante dal client
        return null;
    }

    @Override
    public int acquireTroopMovement(IPlayer player, String fromZone, String toZone, int max) {
        // TODO: acquisire il numero di carri da spostare dopo una conquista
        return 1;
    }

    @Override
    public Pair<String,String> acquireStrategicMovement(IPlayer player) {
        // TODO: acquisire (zonaDestinazione,zonaOrigine) dal client
        return null;
    }
}
