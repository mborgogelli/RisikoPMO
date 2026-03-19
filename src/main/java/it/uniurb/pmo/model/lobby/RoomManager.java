package it.uniurb.pmo.model.lobby;

import java.util.*;
import java.util.stream.Collectors;

import it.uniurb.pmo.model.players.IPlayer;
import it.uniurb.pmo.model.utils.EnumColors;
import it.uniurb.pmo.model.utils.GameVersion;

public class RoomManager implements IRoomManager {
	
	
	private static RoomManager instance;
	private final Map<String, Room> activeRooms;
	
	private RoomManager() {
		this.activeRooms = new HashMap<>();
	}
	
	public static RoomManager getInstance() {
		if (instance == null) {
			instance = new RoomManager();
		}
		return instance;
	}
	
    @Override
	public List<String> filterRoomsByGameVersion(GameVersion gameVersion) {
		return this.activeRooms.entrySet().stream()
							.filter(e -> gameVersion.equals(e.getValue().getRisikoVersion()))
							.map(Map.Entry::getKey)
							.toList();
	}

	@Override
	public int getPlayersNumber(String roomId) {
		return this.getRoom(roomId).getNumberOfPlayers();
	}

	@Override
	public EnumColors getPlayerColor(String roomId, String playerName) {
		return this.getRoom(roomId).getAssignedColor(playerName);
	}

	@Override
	public GameVersion getGameVersion(String roomId) {
		return this.getRoom(roomId).getRisikoVersion();
	}

	@Override
	public void enterRoom(String roomId, String nomeGiocatore) {
		this.getRoom(roomId).enterRoom(nomeGiocatore);
	}

	@Override
	public String createRoom(String nomeGiocatore, int maxPlayers, GameVersion gameVersion) {
        String idStanza = UUID.randomUUID().toString().substring(0, 5);
        this.activeRooms.put(idStanza, new Room(maxPlayers, gameVersion));
        this.activeRooms.get(idStanza).enterRoom(nomeGiocatore);
        return idStanza;
    }

	@Override
	public void exitRoom(String roomId, String nomeGiocatore) {
		//TO DO Check if last player
		this.getRoom(roomId).exitRoom(nomeGiocatore);
	}

	@Override
	public int getMaxPlayers(String roomId) {
		return this.getRoom(roomId).getMaxPlayers();
	}

	@Override
	public List<IPlayer> getPlayers(String roomId) {
		return this.getRoom(roomId).getPlayers();
	}

	@Override
	public Boolean isFull(String roomId) {
		return this.getRoom(roomId).isRoomFull();
	}

	private void checkRoom(String roomId) {
		if (!this.activeRooms.containsKey(roomId)) {
			throw new IllegalArgumentException("Room with ID " + roomId + " does not exist.");
		}
	}

	private IRoom getRoom(String roomId) {
		this.checkRoom(roomId);
		return this.activeRooms.get(roomId);
	}
}
