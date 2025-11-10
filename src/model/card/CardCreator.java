package model.card;

import java.util.List;

/**
* Classe astratta che offre il comportamento di base per costruire le carte del gioco
* Questa classe serve come “scheletro” per tutte le varianti di giochi: fornisce metodi utili per creare e gestire
* la struttura delle carte , lasciando alle sottoclassi il compito di definire come popolare i dettagli specifici
* delle carte in base alle regole della variante di Risiko implementata.
*/

public abstract class CardCreator implements ICard {

    /**
     * Crea e restituisce una lista di carte.
     * Utilizza i metodi astratti createCards() e resetCards() per creare e inizializzare le carte.
     * 
     * @return una lista di carte create e inizializzate
     */
    protected abstract List<ICard> createCards();

    /**
     * Resetta e inizializza le carte fornite.
     * 
     * @param cards lista di carte da resettare
     */
    protected abstract void resetCards(List<ICard> cards);

    /**
     * Configura i dettagli specifici delle carte, come adiacenze o bonus.
     * Questo metodo deve essere implementato dalle sottoclassi per definire i dettagli specifici.
     * 
     * @param card la carta da configurare
     */
    protected abstract void configureCardDetails(ICard card);

    /**
     * Configura le regole specifiche per una variante del gioco.
     * Questo metodo permette di gestire varianti diverse di Risiko.
     */
    protected abstract void configureVariantRules();

    /**
     * Carica le carte da un file JSON.
     * 
     * @param filePath percorso del file JSON
     * @return una lista di carte caricate
     */
    protected abstract List<ICard> loadCardsFromJson(String filePath);

    /**
     * Salva le carte in un file JSON.
     * 
     * @param cards lista di carte da salvare
     * @param filePath percorso del file JSON
     */
    protected abstract void saveCardsToJson(List<ICard> cards, String filePath);
}