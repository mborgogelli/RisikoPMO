package model.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.players.Player;
import model.utils.EnumColors;
import model.utils.GameVersion;

public class Room implements IRoom {
	
	private boolean isFull;
	private final int maxPlayers;
	private final GameVersion gameVersion;
	private final Map<Player, EnumColors> players;
	private List<EnumColors> colors;
	
	
	public Room(int maxPlayers, GameVersion gameVersion) {
		this.maxPlayers = maxPlayers;
		this.gameVersion = gameVersion;
		this.players = new HashMap<>();
		this.colors = new ArrayList<>(EnumColors.getAvailableColors());
		this.isFull = false;
	}
	
	@Override
	public void enterRoom(String playerName) {
		this.players.put(new Player(playerName), this.pickRandomColor());
	}

	@Override
	public void exitRoom(String playerName) {
	}

	@Override
	public void assignColor(Player player, String color) {
		this.players.putIfAbsent(player, this.pickRandomColor());
	}

	@Override
	public boolean isRoomFull() {
		return this.isFull;
	}

	@Override
	public int getNumberOfPlayers() {
		return this.players.size();
	}

	@Override
	public int getMaxPlayers() {
		return this.maxPlayers;
	}

	@Override
	public void kickPlayer(String playerName) {
        this.players.entrySet()
        			.removeIf(entry -> entry.getKey().getName().equals(playerName));
	}

	@Override
	public GameVersion getRisikoVersion() {
		return this.gameVersion;
	}

	@Override
	public EnumColors getAssignedColor(String playerName) {
		return this.players.entrySet().stream()
							.filter(entry -> entry.getKey().getName().equals(playerName))
							.map(Map.Entry::getValue)
							.findFirst().orElse(null);
	}
	
	private EnumColors pickRandomColor() {
		int index = (int) (Math.random() * this.colors.size());
		EnumColors color = this.colors.get(index);
		this.colors.remove(index);
		return color;
	}
	
    @Override
	public boolean hasPlayer(String playerName) {
		return this.players.keySet().stream()
							.anyMatch(player -> player.getName().equals(playerName));
	}

}
