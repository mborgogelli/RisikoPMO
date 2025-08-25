package model.card;

import java.util.List;

/**
* Classe astratta che offre il comportamento di base per costruire le carte del gioco
* Questa classe serve come “scheletro” per tutte le varianti di giochi: fornisce metodi utili per creare e gestire
* la struttura delle carte , lasciando alle sottoclassi il compito di definire come popolare i dettagli specifici
* (ad esempio le adiacenze o altri dati particolari).
* 
*/

public abstract class CardCreator {

	/**
	 * Crea e restituisce una lista di carte.
	 * Utilizza i metodi astratti createCards() e resetCards() per creare e inizializzare le carte.
	 * 
	 * @return una lista di carte create e inizializzate
	 */
	protected abstract List<ICard> createCards();
	
	protected abstract void resetCards(List<ICard> cards);
	
	
}