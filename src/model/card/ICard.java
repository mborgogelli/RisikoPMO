package model.card;

/*
 * Interfaccia che rappresenta una generica carta nel gioco.
 * Le carte possono essere utilizzate per vari scopi, come azioni speciali o bonus.
 * 
 */


public interface ICard {

	/** Restituisce il nome della carta.
	 * 
	 * @return il nome della carta
	 */
	Boolean isVisibile();

	/** Imposta lo stato di visibilità della carta.
	 * 
	 * @param visibile true se la carta è visibile, false altrimenti
	 */
	void show();

	/** Nasconde la carta, rendendola invisibile.
	 * 
	 */
	void hide();

	/** Restituisce il nome della carta.
	 * 
	 * @return il nome della carta
	 */
	String getName();
	
	/** Restituisce il tipo della carta.
	 * 
	 * @return il tipo della carta
	 */
	CardType getTypeCard();


	Boolean isPlayable();
	
}
