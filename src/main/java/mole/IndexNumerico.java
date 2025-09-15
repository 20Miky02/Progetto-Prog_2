package mole;

import java.util.*;

/**
 * Classe per la rappresentazione di un indice numerico, cioè una sequenza ordinata di etichette
 * intere generate automaticamente a partire da un valore iniziale, un valore
 * finale e un passo (step). La classe fornisce metodi per accedere alle etichette per posizione,
 * ottenere la posizione di una data etichetta, fondere due indici e rinominarli. È inoltre iterabile,
 * permettendo di scorrere le etichette generate.
 *
 * IR:
 * step != 0
 * step non puo essere maggiore di 0 se start maggiore od uguale end
 * step non puo essere minore di 0 se start minore o uguale ad end
 * name può essere null oppure una stringa qualsiasi
 * size() ≥ 0
 */
public class IndexNumerico implements Index{
    /**
     * Il nome dell'indice (Opzionale)
     */
    private final String name;
    /**
     * Passo iniziale delle etichette
     */
    private final int start;
    /**
     * Passo finale delle etichette
     */
    private final int end;
    /**
     * Step da fare
     */
    private final int step;

    /**
     * Metodo costruttore di un indice numerico a partire da un passo di partenza, da un passo finale e da
     * uno step e, opzionalmente, un nome.
     *
     * @param name     Il nome dell'indice
     * @param start    Il passo iniziale
     * @param end      Il passo finale
     * @param step     Lo step da prendere
     * @throws IllegalArgumentException     Se lo step è uguale a 0
     * @throws IllegalArgumentException     Se l'intervallo non è valito per lo step specificato
     */
    public IndexNumerico(String name, int start, int end, int step) {
        if (step == 0) {
            throw new IllegalArgumentException("Lo step deve essere diverso da zero.");
        }
        if ((step > 0 && start >= end) || (step < 0 && start <= end)) {
            throw new IllegalArgumentException("Intervallo non valido per lo step specificato.");
        }
        this.name = name;
        this.start = start;
        this.step = step;
        this.end = end;
    }

    @Override
    public int size() {
        return Math.max(0, (int) Math.ceil((end - start) / (double) step));
    }

    @Override
    public Object getLabelAt(int i) {
        return start + (step*i);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPositionOf(Object label) {
        if (!(label instanceof Integer)) return -1;
        int value = (Integer) label;

        if ((step > 0 && (value < start || value >= end)) || (step < 0 && (value > start || value <= end))) {
            return -1;
        }

        int diff = value - start;
        if (diff % step == 0) {
            return diff / step;
        }
        return -1;
    }

    @Override
    public Index merge(Index other, String newName) {
        if (other instanceof IndexNumerico) {
            IndexNumerico o = (IndexNumerico) other;
            if (this.step == o.step) {
                int newStart = Math.min(this.start, o.start);
                int newEnd = Math.max(this.end, o.end);
                return new IndexNumerico(newName, newStart, newEnd, this.step);
            }
        }
        List<Object> merged = new ArrayList<>();
        for (Object lbl : this) {
            if (!merged.contains(lbl)) merged.add(lbl);
        }
        for (Object lbl : other) {
            if (!merged.contains(lbl)) merged.add(lbl);
        }
        return new IndexObject(merged, newName);
    }

    @Override
    public Index rename(String newName) {
        return new IndexNumerico(newName, this.start, this.end, this.step);
    }

    @Override
    public String toString() {
        int max = 0;
        if (name != null) {
            max = name.length();
        }
        for (Object label : this) {
            max = Math.max(max, label.toString().length());
        }
        StringBuilder sb = new StringBuilder();
        if (name != null) {
            sb.append(" ".repeat(max - name.length())).append(name);
        }
        sb.append("\n").append("-".repeat(max)).append("\n");
        for (Object lbl : this) {
            String str = lbl.toString();
            sb.append(" ".repeat(max - str.length())).append(str).append("\n");
        }
        return sb.toString();
    }

    @Override
    public Iterator<Object> iterator() {
        return new Iterator<>() {
            int indice = start - step;
            @Override
            public boolean hasNext() {
                return (indice + step < end && step > 0) || (indice + step > end && step < 0);
            }
            @Override
            public Object next() {
                indice+= step;
                return indice;
            }
        };
    }
}
