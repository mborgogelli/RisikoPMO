package model.board;

import java.util.List;

public interface IBoardCreator {
	
		/**
	 * Restituisce la lista delle zone della mappa.
	 * 
	 * @return lista di IZone che rappresentano le zone della mappa
	 */
	List<IZone> getMap();
}
