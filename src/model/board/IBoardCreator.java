package model.board;

public interface IBoardCreator {

	/**
	 * Metodo che restituisce la mappa del gioco.
	 * Se la mappa non è stata creata correttamente, lancia un'eccezione.
	 *
	 * @return Un oggetto IGameBoard che rappresenta la mappa del gioco
	 * @throws IllegalStateException se la mappa non è stata creata correttamente
	 */
	IGameBoard getMap();

}