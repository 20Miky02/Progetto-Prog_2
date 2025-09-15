package mole;

/**
 * Interfaccia adibita per la rappresentazione di un indice, ovvero una sequenza ordinata di etichette
 * utilizzata per identificare in modo univoco le righe o le colonne di una tabella.
 * L’interfaccia fornisce metodi per:
 * - Ottenere la dimensione e accedere alle etichette per posizione
 * - Ricercare la posizione corrispondente a una determinata etichetta
 * - Creare una copia rinominata dell’indice
 * - Fondere due indici in uno nuovo, mantenendo tutte le etichette senza duplicati
 * - Restituire il nome di un indice
 * Mi sono avvalsa del supporto dell'IA per il debug.
 */
public interface Index extends Iterable<Object>{
    /**
     * Restituisce la lunghezza dell'indice.
     * @return la lunghezza dell'indice
     */
    int size();

    /**
     * Restituisce l'etichetta in posizione i-esima.
     * @param i la posizione
     * @return l'etichetta alla posizione
     */
    Object getLabelAt(int i);

    /**
     * Restituisce il nome dell'indice.
     * @return il nome
     */
    String getName();

    /**
     * Restituisce la posizione di una data etichetta.
     * @param label l'etichetta
     * @return la posizione dell'etichetta
     */
    int getPositionOf(Object label);

    /**
     * Fonde questo indice con un altro, includendo etichette non duplicate.
     * @param other il secondo indice con cui fondere il primo
     * @param newName il nuovo nome dell'indice fuso
     * @return il nuovo indice dato dalla fusione dei due precedenti
     */
    Index merge(Index other, String newName);

    /**
     * Crea una copia dell'indice con un nuovo nome.
     * @param newName il nuovo nome per l'indice
     * @return l'indice con nome aggiornato
     */
    Index rename(String newName);
}