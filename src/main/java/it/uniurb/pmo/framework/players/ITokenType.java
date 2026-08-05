package it.uniurb.pmo.framework.players;

/**
 * Rappresenta un tipo di pedina (token) utilizzabile nel gioco.
 * Le varianti concreti implementano questa interfaccia con enum specifici
 * (es. ERisikoNewToken per la variante classica).
 */
public interface ITokenType {

    /**
     * Restituisce il nome del tipo di pedina.
     *
     * @return nome identificativo del tipo (es. "TANK", "INFANTRY")
     */
    String getName();

    /**
     * Restituisce il valore numerico associato al tipo di pedina.
     * @return valore numerico del tipo
     */
    int getValue();
}
