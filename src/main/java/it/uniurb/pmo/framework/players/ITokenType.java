package it.uniurb.pmo.framework.players;

/**
 * Rappresenta un tipo di pedina (token) utilizzabile nel gioco.
 * Le varianti concreti implementano questa interfaccia con enum specifici
 * (es. RisikoToken per la variante classica).
 *
 * Collocata nel framework per consentire a IPlayerInputProvider di essere
 * generica rispetto ai tipi di pedina senza dipendere da classi di variante.
 */
public interface ITokenType {

    /**
     * Restituisce il nome del tipo di pedina.
     *
     * @return nome identificativo del tipo (es. "TANK", "INFANTRY")
     */
    String getName();
}
