package it.uniurb.pmo.model.versions.risikockassic.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import it.uniurb.pmo.model.board.IGameBoard;
import it.uniurb.pmo.model.board.IZone;
import it.uniurb.pmo.model.utils.GameVersion;

class GameBoardRisikoNew implements IGameBoard {
	
	private final List<IZone> continents;
	private final GameVersion gameVersion;
	private final Map<String, List<String>> neighbours;
	
	GameBoardRisikoNew(List<IZone> continents) {
		this.continents = continents;
		this.gameVersion = GameVersion.RISIKONEW;
		this.neighbours = getNeighboursFromMap();
	}
	
	@Override
	public List<IZone> getZones() {
		return this.continents;
	}
	
	@Override
	public GameVersion getGameVersion() {
		return this.gameVersion;
	}

	@Override
	public IZone findZoneByName(String zoneName) {
	    return this.continents.stream()
	            .flatMap(continent -> continent.getChildZones().stream())
	            .filter(territory -> territory.getName().equalsIgnoreCase(zoneName))
	            .findFirst()
	            .orElseGet(() -> this.continents.stream()
	                .filter(continent -> continent.getName().equalsIgnoreCase(zoneName))
	                .findFirst()
	                .orElse(null));
	}

	@Override
	public List<String> getNeighbours(String zoneName) {
		return this.neighbours.get(zoneName);
	}

	@Override
	public Optional<IZone> whereIsZone(String zoneName) {
	    return this.continents.stream()
	        .filter(continent -> continent.getChildZones().stream()
            .anyMatch(zone -> zone.getName().equalsIgnoreCase(zoneName)))
	        .findFirst();
	}

	@Override
	public boolean canReach(String zoneTo, String zoneFrom) {
		return this.neighbours.containsKey(zoneFrom) && 
		       this.neighbours.get(zoneFrom).contains(zoneTo);
	}

	@Override
	public Integer getValue(String zoneName) {
		IZone zone = findZoneByName(zoneName);
		return zone.getValue();
	}
	
	public Integer getZoneBonus(String zoneName) {
		return this.getValue(zoneName);
	}
	
	/**
	 * Crea una mappa dei territori e dei loro territori confinanti.
	 * 
	 * @return Mappa dei territori e dei loro territori confinanti.
	 */
	private Map<String, List<String>> getNeighboursFromMap() {
	    Map<String, List<String>> neighbours = new HashMap<>();
	    this.continents.stream()
	        .flatMap(continent -> continent.getChildZones().stream())
	        .forEach(territory -> {
	            String territoryName = territory.getName();
	            List<String> territoryNeighbours = extractNeighboursName(territory);
	            neighbours.put(territoryName, territoryNeighbours);
	        });
	    
	    return neighbours;
	}

	/**
	 * Estrae i nomi dei territori confinanti da un territorio.
	 * 
	 * @param zone
	 * @return
	 */
	private List<String> extractNeighboursName(IZone zone) {
		return zone.getNeighbours().stream()
				.toList();
	}
}
