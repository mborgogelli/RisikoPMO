package it.uniurb.pmo.variants.risikonew.management;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.uniurb.pmo.framework.management.AbstractMediator;
import it.uniurb.pmo.framework.management.interfaces.IDirector;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.turn.IGameCoordinator;
import it.uniurb.pmo.variants.risikonew.dto.DeploymentRequestDTO;
import it.uniurb.pmo.variants.risikonew.dto.DeploymentResponseDTO;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;
import it.uniurb.pmo.variants.risikonew.management.interfaces.ICardManagerRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMapManagerRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.interfaces.ITankManager;
import it.uniurb.pmo.variants.risikonew.management.interfaces.ITurnManagerRisikoNew;

public class MediatorRisikoNew extends AbstractMediator implements IMediatorRisikoNew {
	
	private IMapManagerRisikoNew mapManager;
	private ICardManagerRisikoNew cardManager;
	private ITankManager tankManager;
	private ITurnManagerRisikoNew turnManager;
	private IDirector director;
	
	
	@Override
	public List<String> getAllZones() {
		return mapManager.getAllZones();
	}

	@Override
	public List<String> getZonesOwnedBy(IPlayer player) {
		return mapManager.getZonesOwnedBy(player);
	}

	@Override
	public boolean canMoveBetween(IPlayer player, String toZone, String fromZone) {
		return mapManager.canMoveBetween(player, toZone, fromZone);
	}

	@Override
	public Map<String, Integer> acquireTargetZones(IPlayer player, ITokenType tanks, int toDeploy) {
		Map<ITokenType, Integer> available = Map.of(tanks, toDeploy);
		List<String> deployableZones = this.mapManager.getZonesOwnedBy(player);
		DeploymentRequestDTO request = new DeploymentRequestDTO(player, deployableZones, available);

		IGameCoordinator<DeploymentRequestDTO, DeploymentResponseDTO> coordinator = this::resolveDeployment;
		DeploymentResponseDTO response = coordinator.sendRequest(request);

		return response.getDeployment().entrySet().stream()
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						e -> e.getValue().getOrDefault(tanks, 0)
				));
	}

	private DeploymentResponseDTO resolveDeployment(DeploymentRequestDTO request) {
		List<String> deployableZones = request.getDeployableZones();
		Map<ITokenType, Integer> availableTokens = request.getAvailableTokens();

		if (deployableZones == null || deployableZones.isEmpty()) {
			throw new IllegalArgumentException("No deployable zones available.");
		}
		if (availableTokens == null || availableTokens.isEmpty()) {
			throw new IllegalArgumentException("No available tokens provided.");
		}

		String selectedZone = deployableZones.stream()
				.min(Comparator.naturalOrder())
				.orElseThrow(() -> new IllegalArgumentException("No deployable zones available."));

		int availableTanks = availableTokens.getOrDefault(ERisikoNewToken.TANK, 0);
		if (availableTanks <= 0) {
			throw new IllegalArgumentException("No tanks available for deployment.");
		}

		Map<String, Map<ITokenType, Integer>> deployment = new HashMap<>();
		deployment.put(selectedZone, Map.of(ERisikoNewToken.TANK, availableTanks));
		return new DeploymentResponseDTO(deployment);
	}

	@Override
	public void notifyWinner(IPlayer player) {
		this.director.declareWinner(player);
	}

	@Override
	public void initManagers() {
		this.mapManager = super.resolveManager(MapManagerRisikoNew.class);
		this.tankManager = super.resolveManager(TankManager.class);
		this.cardManager = super.resolveManager(CardManagerRisikoNew.class);
		this.turnManager = super.resolveManager(TurnManagerRisikoNew.class);
	}

	@Override
	public void startGame() {
		this.turnManager.startGame();
	}

	@Override
	public void stopGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getPlayerTank(IPlayer player) {
		return this.tankManager.getPlayerTank(player);
	}

	@Override
	public int getZoneTank(String zone) {
		return this.tankManager.getZoneTank(zone);
	}

	@Override
	public void deployTank(IPlayer player, String zone, int tanks) {
		this.tankManager.deployTank(player, zone, tanks);
	}

	@Override
	public Map<String, Integer> acquireTargetZones(IPlayer player, int toDeploy) {
		return this.acquireTargetZones(player, ERisikoNewToken.TANK, toDeploy);
	}
}
