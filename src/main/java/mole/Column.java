package mole;

import java.util.*;
import java.util.function.Function;

/**
 * Classe per la rappresentazione  e manipolazione di una colonna. Una colonna contiene una sequenza di valori,
 * un numero di righe e un indice di lunghezza coincidente col numero di righe e, opzionalmente, un nome.
 * La classe offre metodi per enumerare i propri valore, accedere ai valori tramite posizione o etichetta,
 * rinominare la colonna, sostituirne l’indice, concatenarla con un’altra colonna (pileUp), oppure trasformarne
 * i valori tramite una funzione (map). Una colonna è considerata uguale a un'altra se hanno il medesimo
 * indice e lo stesso nome.
 *
 * IR:
 * values ≠ null
 * values.size() > 0
 * index ≠ null
 * index.size() == values.size()
 * name può essere null oppure una stringa non vuota
 * la lista values è immutabile
 *
 * @param <T> tipo degli elementi contenuti nella colonna
 */
public class Column<T> implements Iterable<T> {
    /**
     * Lista dei valori della colonna
     */
    private final List<T> values;
    /**
     * L'indice della colonna
     */
    private final Index index;
    /**
     * Il nome della colonna
     */
    private final String name;

    /**
     * Metodo costruttore di una colonna a partire da un indice, una sequenza di valori e, opzionalmente, un
     * nome.
     *
     * @param values     Valori della colonna
     * @param index      Indice della colonna
     * @param name       Nome della colonna
     * @throws IllegalArgumentException     Se la lista dei volori è null
     * @throws IllegalArgumentException     Se l'indice è null
     * @throws IllegalArgumentException     Se la colonna non contiene valori
     * @throws IllegalArgumentException     Se viene fornito un indice con dimensione differente a quella della
     *                                      colonna
     */
    public Column(List<T> values, Index index, String name) {
        if (values == null) {
            throw new IllegalArgumentException("La lista dei valori non può essere null.");
        }
        if (index == null) {
            throw new IllegalArgumentException("L'indice non può essere null.");
        }
        if (values.isEmpty()) {
            throw new IllegalArgumentException("Una colonna deve avere almeno un valore.");
        }
        if (values.size() != index.size()) {
            throw new IllegalArgumentException("I due valori non coincidono");
        }

        this.values = Collections.unmodifiableList(new ArrayList<>(values));
        this.index = index;
        this.name = name;
    }

    /**
     * Restituisce il numero di righe (valori) della colonna.
     * Deve essere > 0.
     *
     * @return Il numero di righe della colonna.
     */
    public int size() {
        return values.size();
    }

    /**
     * Restituisce l'indice associato a questa colonna.
     * L'indice deve avere la stessa lunghezza della colonna (size()).
     *
     * @return L'indice della colonna.
     */
    public Index getIndex() {
        return index;
    }

    /**
     * Restituisce il nome opzionale della colonna.
     *
     * @return Il nome della colonna o null se non ha un nome.
     */
    public String getName() {
        return name;
    }

    /**
     * Restituisce il valore alla posizione i-esima della colonna.
     *
     * @param i La posizione (indice numerico) del valore da recuperare.
     * @return Il valore alla posizione specificata. Può essere null.
     */
    public T getValueAt(int i) {
        return values.get(i);
    }

    /**
     * Restituisce il valore corrispondente a una data etichetta dell'indice.
     *
     * @param label L'etichetta dell'indice.
     * @return Il valore corrispondente all'etichetta o null se l'etichetta non è presente nell'indice della
     * colonna.
     */
    public T getValue(Object label) {
        int position = index.getPositionOf(label);
        if (position == -1) {
            return null;
        }
        return values.get(position);
    }

    /**
     * Produce una nuova colonna con gli stessi valori di questa colonna, ma con un nuovo indice. Il nuovo
     * indice deve avere la stessa lunghezza dei valori originali.
     *
     * @param newIndex     Il nuovo indice da associare alla colonna.
     * @return Una nuova istanza di Column con lo stesso nome, stessi valori ma nuovo indice.
     * @throws IllegalArgumentException se newIndex è null o se la sua dimensione non corrisponde a size().
     */
    public Column<T> withIndex(Index newIndex) {
        if (newIndex == null) {
            throw new IllegalArgumentException("Il nuovo indice non può essere null.");
        }
        if (newIndex.size() != this.size()) {
            throw new IllegalArgumentException("La dimensione del nuovo indice deve corrispondere alla dimensione della colonna");
        }
        return new Column<>(this.values, newIndex, this.name);
    }

    /**
     * Produce una nuova colonna con gli stessi valori e indice di questa colonna, ma con un nuovo nome.
     *
     * @param newName      Il nuovo nome della colonna. Può essere null per rimuovere il nome.
     * @return Una nuova istanza di Column con lo stesso indice, stessi valori ma nuovo nome.
     */
    public Column<T> rename(String newName) {
        if (Objects.equals(this.name, newName)) {
            return this;
        }
        return new Column<>(this.values, this.index, newName);
    }

    /**
     * Esegue la reindicizzazione della colonna utilizzando un nuovo indice. Crea una nuova colonna la cui
     * lunghezza è pari alla lunghezza di newIndex.
     *
     * @param newIndex      Il nuovo indice da usare per la reindicizzazione.
     * @return Una nuova colonna reindicizzata.
     * @throws NullPointerException     Se newIndex è null.
     */
    public Column<T> reindex(Index newIndex) {
        Objects.requireNonNull(newIndex, "Il nuovo indice non può essere null.");
        List<T> newValues = new ArrayList<>();
        for (Object label : newIndex) {
            newValues.add(this.getValue(label));
        }
        return new Column<>(newValues, newIndex, this.name);
    }

    /**
     * Restituisce una nuova colonna ottenuta impilando (concatenando) questa colonna con un'altra.
     * Gli indici delle due colonne devono essere disgiunti, ovvero non devono contenere etichette in comune.
     *
     * @param secondColumn la colonna da impilare sotto a questa
     * @return una nuova colonna con valori concatenati e indice risultante dalla fusione dei due
     * @throws NullPointerException se la colonna passata è null
     * @throws IllegalArgumentException se gli indici non sono disgiunti (etichette duplicate)
     */
    public Column<T> pileUp(Column<T> secondColumn) {
        Objects.requireNonNull(secondColumn, "La seconda colonna non può essere null.");
        for (Object label : this.index) {
            if (secondColumn.getIndex().getPositionOf(label) != -1) {
                throw new IllegalArgumentException("Gli indici delle colonne devono essere disgiunti per l'impilamento.");
            }
        }
        Index newIndex = this.index.merge(secondColumn.index, this.index.getName());
        List<T> newValues = new ArrayList<>();
        for (T value : this.values) {
            newValues.add(value);
        }
        for (T value : secondColumn) {
            newValues.add(value);
        }
        return new Column<>(newValues, newIndex, this.name);
    }

    /**
     * Data una funzione, restituisce una nuova colonna con i nuovi valori risultanti dall'applicazione della
     * funzione. L'indice e il nome della colonna rimangono invariati.
     *
     * @param f     la funzione da applicare
     * @return la colonna risultante dall'applicazione della funzione
     * @param <R>   tipo del risultato della trasformazione
     * @throws NullPointerException se la funzione data è null
     */
    public <R> Column<R> map(Function<T, R> f) {
        Objects.requireNonNull(f, "La funzione non può essere null.");
        List<R> mappedList = new ArrayList<>(this.values.size());
        for (T value : this.values) {
            mappedList.add(f.apply(value));
        }
        return new Column<>(mappedList, this.index, this.name);
    }

    @Override
    public Iterator<T> iterator() {
        return Collections.unmodifiableList(values).iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Column<?> that = (Column<?>) o;
        return values.equals(that.values) &&
                index.equals(that.index) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values, index, name);
    }

    @Override
    public String toString() {
        if (index == null || values == null || index.size() != values.size()) {
            return "";
        }

        String indexName = index.getName() != null ? index.getName() : "";
        int maxIndexWidth = indexName.length();
        for (int i = 0; i < index.size(); i++) {
            Object label = index.getLabelAt(i);
            if (label != null) {
                maxIndexWidth = Math.max(maxIndexWidth, label.toString().length());
            }
        }

        String columnName = name != null ? name : "";
        int maxValueWidth = columnName.length();
        for (T value : values) {
            if (value != null) {
                maxValueWidth = Math.max(maxValueWidth, value.toString().length());
            }
        }

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%" + maxIndexWidth + "s", indexName));
        sb.append(" | ");
        sb.append(String.format("%-" + maxValueWidth + "s", columnName));
        sb.append("\n");

        sb.append("-".repeat(maxIndexWidth));
        sb.append("-+-");
        sb.append("-".repeat(maxValueWidth));
        sb.append("\n");

        for (int i = 0; i < index.size(); i++) {
            Object label = index.getLabelAt(i);
            T value = values.get(i);
            sb.append(String.format("%" + maxIndexWidth + "s", label != null ? label : ""));
            sb.append(" | ");
            sb.append(String.format("%-" + maxValueWidth + "s", value != null ? value : ""));
            sb.append("\n");
        }
        return sb.toString();
    }
}
