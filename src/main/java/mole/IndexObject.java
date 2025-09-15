package mole;

import java.util.*;

/**
 * Classe per la rappresentazione di un indice object, cioè una collezione esplicita di etichette
 * arbitrarie (oggetti), mantenute nell'ordine fornito e senza duplicati.
 * La classe fornisce metodi per accedere alle etichette per posizione, ottenere la posizione di una data
 * etichetta, fondere due insiemi di etichette eliminando duplicati, e rinominare l'indice. È inoltre
 * iterabile, permettendo di scorrere le etichette contenute.
 *
 * IR:
 * labels ≠ null e non vuota
 * tutti gli elementi di labels sono unici (nessun duplicato)
 * name può essere null oppure una stringa qualsiasi
 * labelToPosition contiene esattamente le stesse etichette di labels ed associa ciascuna etichetta alla sua
 * posizione nella sequenza
 */
public final class IndexObject implements Index{
    /**
     * Le etichette appartenenti all'indice
     */
    private final List<Object> labels;
    /**
     * Il nome dell'indice (Opzionale)
     */
    private final String name;
    /**
     * Associazione etichetta e relativa sua posizione
     */
    private final Map<Object, Integer> labelToPosition;

    /**
     * Metodo costruttore di un indice di tipo object a partire da una serie di etichette e, opzionalmente,
     * un nome.
     *
     * @param labels      L'insieme di etichette
     * @param name        Il nome dell'indice
     * @throws IllegalArgumentException       Se la lista è vuota
     * @throws NullPointerException           Se la lista è null
     */
    public IndexObject(List<Object> labels, String name) {
        Objects.requireNonNull(labels, "La lista di etichette non può essere null.");
        if (labels.isEmpty()) {
            throw new IllegalArgumentException("Dev'esserci almeno un'etichetta");
        }
        List<Object> uniqueLabels = new ArrayList<>();
        Set<Object> seen = new HashSet<>();
        Map<Object, Integer> tempMap = new HashMap<>();
        for (Object label : labels) {
            if (label != null && seen.add(label)) {
                tempMap.put(label, uniqueLabels.size());
                uniqueLabels.add(label);
            }
        }
        this.labels = Collections.unmodifiableList(uniqueLabels);
        this.name = name;
        this.labelToPosition = Collections.unmodifiableMap(tempMap);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IndexObject)) return false;
        IndexObject other = (IndexObject) obj;
        return this.labels.equals(other.labels);
    }

    @Override
    public int hashCode() {
        return labels.hashCode();
    }

    /**
     * Calcola la lunghezza massima tra il nome dell'indice e tutte le etichette convertite in stringa.
     * Se il nome è null, viene considerato di lunghezza 0.
     *
     * @return la lunghezza massima tra il nome dell'indice e le etichette
     */
    private int maxLenghtLabel() {
        int max = name == null ? 0 : name.length();
        for (Object label : labels) {
            if (label.toString().length() > max) {
                max = label.toString().length();
            }
        }
        return max;
    }

    @Override
    public String toString() {
        int max = maxLenghtLabel();
        StringBuilder sb = new StringBuilder();
        int namelength = name == null ? 0 : name.length();
        sb.append(" ".repeat(max-namelength)).append(name == null ? "" : name);
        sb.append("\n").append("-".repeat(maxLenghtLabel())).append("\n");
        for (Object label : labels) {
            sb.append(" ".repeat(max-label.toString().length())).append(label).append("\n");
        }
        return sb.toString();
    }

    @Override
    public int size() {
        return labels.size();
    }

    @Override
    public Object getLabelAt(int i) {
        return labels.get(i);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPositionOf(Object label) {
        for (int i = 0; i < labels.size(); i++) {
            if (Objects.equals(labels.get(i), label)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Index merge(Index other, String newName) {
        if (other == null) {
            throw new NullPointerException("L'indice da fondere non può essere null.");
        }
        List<Object> merged = new ArrayList<>(this.labels);
        for (Object label : other) {
            if (!labelToPosition.containsKey(label)) {
                merged.add(label);
            }
        }
        return new IndexObject(merged, newName);
    }

    @Override
    public Index rename(String newName) {
        return new IndexObject(this.labels, newName);
    }

    @Override
    public Iterator<Object> iterator() {
        return labels.iterator();
    }
}
