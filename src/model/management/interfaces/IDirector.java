package model.management.interfaces;

import java.util.List;

import model.players.IPlayer;
import model.utils.GameVersion;

public interface IDirector extends IManager {
	
	boolean isGameStarted();

	boolean checkWin(IPlayer player);

	boolean checkLoss(IPlayer player);

	void StartGame();

	void exitGame(IPlayer player);
	
}
