package it.uniurb.pmo.variants.risikonew;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.IPlayerInputProvider;
import it.uniurb.pmo.framework.players.ITokenType;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementazione concreta di IPlayerInputProvider per la variante RisikoNew.
 *
 * I metodi acquire* creano un CompletableFuture per il giocatore e si bloccano
 * (future.join()) finché il controller REST non chiama il corrispondente submit*,
 * che completa il future e sblocca il thread di gioco.
 *
 * Il controller REST dipende da IPlayerInputProvider (non da questa classe concreta)
 * per invocare i metodi submit*.
 */
public class PlayerInputProviderRisikoNew implements IPlayerInputProvider {

    private final Map<String, CompletableFuture<Map<String, Map<ITokenType, Integer>>>> deploymentFutures
            = new ConcurrentHashMap<>();

    private final Map<String, CompletableFuture<String[]>> attackFutures
            = new ConcurrentHashMap<>();

    private final Map<String, CompletableFuture<Integer>> troopMovementFutures
            = new ConcurrentHashMap<>();

    private final Map<String, CompletableFuture<String[]>> strategicMovementFutures
            = new ConcurrentHashMap<>();

    // ── acquire* ─────────────────────────────────────────────────────────────

    @Override
    public Map<String, Map<ITokenType, Integer>> acquireDeployment(IPlayer player, Map<ITokenType, Integer> availableTokens) {
        CompletableFuture<Map<String, Map<ITokenType, Integer>>> future = new CompletableFuture<>();
        this.deploymentFutures.put(player.getName(), future);
        return future.join();
    }

    @Override
    public String[] acquireAttack(IPlayer player) {
        CompletableFuture<String[]> future = new CompletableFuture<>();
        this.attackFutures.put(player.getName(), future);
        return future.join();
    }

    @Override
    public int acquireTroopMovement(IPlayer player, String fromZone, String toZone, int max) {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        this.troopMovementFutures.put(player.getName(), future);
        return future.join();
    }

    @Override
    public String[] acquireStrategicMovement(IPlayer player) {
        CompletableFuture<String[]> future = new CompletableFuture<>();
        this.strategicMovementFutures.put(player.getName(), future);
        return future.join();
    }

    // ── submit* ───────────────────────────────────────────────────────────────

    @Override
    public void submitDeployment(String playerName, Map<String, Map<ITokenType, Integer>> choice) {
        CompletableFuture<Map<String, Map<ITokenType, Integer>>> future = this.deploymentFutures.remove(playerName);
        if (future != null) {
            future.complete(choice);
        }
    }

    @Override
    public void submitAttack(String playerName, String[] choice) {
        CompletableFuture<String[]> future = this.attackFutures.remove(playerName);
        if (future != null) {
            future.complete(choice);
        }
    }

    @Override
    public void submitTroopMovement(String playerName, int count) {
        CompletableFuture<Integer> future = this.troopMovementFutures.remove(playerName);
        if (future != null) {
            future.complete(count);
        }
    }

    @Override
    public void submitStrategicMovement(String playerName, String[] choice) {
        CompletableFuture<String[]> future = this.strategicMovementFutures.remove(playerName);
        if (future != null) {
            future.complete(choice);
        }
    }
}
