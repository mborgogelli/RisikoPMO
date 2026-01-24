package it.uniurb.pmo.model.lobby;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
	public boolean gameVersionIsValid(GameVersion gameVersion) {
		Boolean isValid = false;
		for (GameVersion version : GameVersion.values()) {
			if (version == gameVersion) {
				isValid = true;
				break;
			}
		}
		return isValid;
	}
    
	@Override
	public String getRoomIdByPlayerName(String playerName) {
		return this.activeRooms.entrySet().stream()
							.filter(entry -> entry.getValue().hasPlayer(playerName))
							.map(Map.Entry::getKey)
							.findFirst().orElse(null);
	}
	
	@Override
	public Room getRoom(String roomId) {
		return this.activeRooms.get(roomId);
	}
	
	@Override
	public void enterRoom(String roomId, String nomeGiocatore) {
		if (roomExists(roomId)) {
			Room room = this.activeRooms.get(roomId);
			room.enterRoom(nomeGiocatore);
		} else {
			throw new IllegalArgumentException("Room with ID " + roomId + " does not exist.");
		}
	}

	@Override
	public Room createRoom(String nomeGiocatore, int maxPlayers, GameVersion gameVersion) {
        String idStanza = UUID.randomUUID().toString().substring(0, 5);
        this.activeRooms.put(idStanza, new Room(maxPlayers, gameVersion));
        this.activeRooms.get(idStanza).enterRoom(nomeGiocatore);
        return this.activeRooms.get(idStanza);
	}

	@Override
	public void exitRoom(String roomId, String nomeGiocatore) {
		// TODO Auto-generated method stub
		
	}
	
	private boolean roomExists(String roomId) {
		return this.activeRooms.containsKey(roomId);
	}

}
