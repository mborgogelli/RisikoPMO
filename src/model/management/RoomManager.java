package model.management;

import model.management.interfaces.IRoomManager;
import model.utils.GameVersion;

public class RoomManager implements IRoomManager {
	
	
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
}
