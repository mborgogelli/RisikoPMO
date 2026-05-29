package it.uniurb.pmo.framework.lobby;

import java.util.*;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.utils.EnumColors;
import it.uniurb.pmo.framework.utils.GameVersion;

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

	@Override
	public void closeRoom(String roomId) {
		this.activeRooms.remove(roomId, this.getRoom(roomId));
	}

	@Override
	public List<String> getActiveRooms() {
		return new ArrayList<>(this.activeRooms.keySet());
	}

	@Override
	public void setPlayerReady(String roomId, String playerName, boolean isReady) {
		this.getRoom(roomId).setPlayerReady(playerName, isReady);
	}

	@Override
	public boolean areAllPlayersReady(String roomId) {
		return this.getRoom(roomId).areAllPlayersReady();
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
