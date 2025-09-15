package mole;

import java.util.*;
import java.util.function.Function;

/**
 * Classe per la rappresentazione e manipolazione delle tabelle. Una tabella contiene una sequenza di colonne
 * con nomi distinti e col medesimo indice e dello stesso tipo della tabella. Alla creazione, le colonne
 * senza nome vengono rinominate di default in 'Column_i', dove i corrisponde all'i-esima colonna con nome
 * di default. Non ha un nome. La classe offre metodi per rinominare le colonne, cambiare l’indice di riga,
 * reindicizzare, impilare (pileUp) o affiancare (stack) altre tabelle, trasformare i valori (mapValues) o
 * le colonne (mapColumns).
 *
 * IR:
 * La tabella deve contenere almeno una colonna
 * columns ≠ null
 * Tutte le colonne hanno lo stesso Index di riga;
 * Ogni colonna ha un nome non nullo e distinto (se mancante, viene rinominata automaticamente come "Column_i")
 * columns è immutabile
 *
 * @param <T> tipo della tabella
 */
public class Table<T> implements Iterable<Column<T>> {
    /**
     * Le colonne della tabella
     */
    private final List<Column<T>> columns;
    /**
     * Indice di riga
     */
    private final Index rowIndex;

    /**
     * Metodo costruttore per creare una nuova tabella a partire da una lista di colonne
     * Le colonne devono avere lo stesso indice e nomi distinti (se null, verranno rinominati come "Column_i").
     *
     * @param columns     lista di colonne
     * @throws IllegalArgumentException     Se la tabella non contiene colonne
     * @throws IllegalArgumentException     Se la tabella contiene colonne con indici diversi
     * @throws IllegalArgumentException     Se i nomi delle colonne non sono distinti
     */
    public Table(List<Column<T>> columns) {
        if (columns == null || columns.isEmpty()) {
            throw new IllegalArgumentException("La tabella deve contenere almeno una colonna.");
        }

        Index firstIndex = columns.get(0).getIndex();
        Set<String> seenNames = new HashSet<>();
        List<Column<T>> newCols = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            Column<T> col = columns.get(i);
            if (!col.getIndex().equals(firstIndex)) {
                throw new IllegalArgumentException("Tutte le colonne devono avere lo stesso indice di riga.");
            }

            String name = col.getName();
            if (name == null || name.isEmpty()) {
                name = "Column_" + i;
            }
            if (!seenNames.add(name)) {
                throw new IllegalArgumentException("I nomi delle colonne devono essere distinti.");
            }
            newCols.add(col.rename(name));
        }
        this.columns = Collections.unmodifiableList(newCols);
        this.rowIndex = firstIndex;
    }

    /**
     * Restituisce il numero di colonne.
     *
     * @return numero di colonne nella tabella
     */
    public int getColumnCount() {
        return columns.size();
    }

    /**
     * Restituisce il numero di righe.
     *
     * @return numero di righe nella tabella
     */
    public int getRowCount() {
        return rowIndex.size();
    }

    /**
     * Restituisce la colonna alla posizione specificata.
     *
     * @param i     indice della colonna
     * @return colonna all'indice i
     */
    public Column<T> getColumnAt(int i) {
        Column<T> column = columns.get(i);
        return column.rename(column.getName());
    }

    /**
     * Restituisce la colonna con il nome specificato.
     *
     * @param name     nome della colonna
     * @return la colonna corrispondente o null se non esiste
     */
    private Column<T> getColumnByName(String name) {
        for (Column<T> col : columns) {
            if (col.getName().equals(name)) {
                return col;
            }
        }
        return null;
    }

    /**
     * Restituisce il valore nella riga i della colonna j.
     *
     * @param rowIndex       indice della riga
     * @param columnIndex    indice della colonna
     * @return valore nella posizione specificata
     */
    public T getValueAt(int rowIndex, int columnIndex) {
        return columns.get(columnIndex).getValueAt(rowIndex);
    }

    /**
     * Restituisce il valore nella riga identificata da rowLabel e nella colonna con nome columnName.
     *
     * @param rowLabel     etichetta della riga
     * @param columnName   nome della colonna
     * @return valore corrispondente o null se non trovato
     */
    public T getValue(Object rowLabel, String columnName) {
        Column<T> col = getColumnByName(columnName);
        if (col == null) return null;
        return col.getValue(rowLabel);
    }

    /**
     * Cambia l'indice di riga della tabella (reindicizzazione).
     *
     * @param newRowIndex     nuovo indice per le righe
     * @throws IllegalArgumentException     Se l'indice è null
     * @throws IllegalArgumentException     Se la dimensione del nuovo indice non corrisponde al numero di righe
     * @return nuova tabella con le colonne reindicizzate
     */
    public Table<T> withRowIndex(Index newRowIndex) {
        if (newRowIndex == null) {
            throw new IllegalArgumentException("L'indice non può essere null.");
        }
        if (newRowIndex.size() != this.getRowCount()) {
            throw new IllegalArgumentException("La dimensione del nuovo indice deve corrispondere al numero di righe.");
        }
        List<Column<T>> newCols = new ArrayList<>();
        for (Column<T> col : columns) {
            newCols.add(col.withIndex(newRowIndex));  // <-- withIndex invece di reindex!
        }
        return new Table<>(newCols);
    }

    /**
     * Rinomina le colonne della tabella usando un nuovo Index per le intestazioni.
     *
     * @param newColIndex     nuovo indice di intestazione delle colonne
     * @return una nuova tabella con le stesse colonne rinominate
     * @throws IllegalArgumentException Se il numero di etichette non corrisponde al numero di colonne
     * @throws IllegalArgumentException Se le etichette non sono stringhe
     * @throws IllegalArgumentException Se il nuovo indice è null
     * @throws IllegalArgumentException Se i nomi delle colonne non sono distinti
     */
    public Table<T> renameColumns(Index newColIndex) {
        if (newColIndex == null) {
            throw new IllegalArgumentException("Il nuovo indice di intestazione non può essere null.");
        }
        if (newColIndex.size() != columns.size()) {
            throw new IllegalArgumentException("Il numero di nuove etichette deve corrispondere al numero di colonne.");
        }

        Set<String> seen = new HashSet<>();
        List<Column<T>> renamed = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            Object label = newColIndex.getLabelAt(i);
            if (!(label instanceof String)) {
                throw new IllegalArgumentException("Tutte le etichette devono essere stringhe.");
            }
            String newName = (String) label;
            if (newName == null || newName.isEmpty()) {
                newName = "Column_" + i;
            }
            if (!seen.add(newName)) {
                throw new IllegalArgumentException("I nomi delle colonne devono essere univoci.");
            }
            renamed.add(columns.get(i).rename(newName));
        }
        return new Table<>(renamed);
    }

    /**
     * Restituisce una nuova tabella con tutte le colonne reindicizzate secondo il nuovo indice di riga.
     * I valori vengono associati alle nuove etichette usando le etichette esistenti come chiave.
     *
     * @param newRowIndex Il nuovo indice da usare per le righe.
     * @return Nuova tabella con colonne reindicizzate.
     * @throws NullPointerException se newRowIndex è null.
     */
    public Table<T> reindex(Index newRowIndex) {
        if (newRowIndex == null) {
            throw new NullPointerException("Il nuovo indice di riga non può essere null.");
        }
        List<Column<T>> newColumns = new ArrayList<>();
        for (Column<T> col : columns) {
            newColumns.add(col.reindex(newRowIndex));
        }
        return new Table<>(newColumns);
    }

    /**
     * Impila questa tabella con un'altra verticalmente.
     * Gli indici di riga delle due tabelle non devono avere etichette in comune.
     * Il risultato sarà una nuova tabella con un indice di riga che è la fusione degli indici di riga originali.
     *
     * @param secondTable    la tabella da impilare sotto a questa.
     * @return una nuova tabella impilata verticalmente.
     * @throws NullPointerException se la seconda tabella è null.
     * @throws IllegalArgumentException se gli indici di riga delle due tabelle hanno etichette in comune.
     */
    public Table<T> pileUp(Table<T> secondTable) {
        Objects.requireNonNull(secondTable, "La seconda tabella non può essere null.");
        for (Object label : this.rowIndex) {
            if (secondTable.rowIndex.getPositionOf(label) != -1) {
                throw new IllegalArgumentException("Gli indici di riga delle tabelle devono essere disgiunti.");
            }
        }
        Index newRowIndex = this.rowIndex.merge(secondTable.rowIndex, this.rowIndex.getName());
        List<Column<T>> newColumns = new ArrayList<>();
        Set<String> processedColumnNames = new HashSet<>();
        for (Column<T> column : this.columns) {
            Column<T> secondTableColumn = secondTable.getColumnByName(column.getName());
            if (secondTableColumn != null) {
                newColumns.add(column.pileUp(secondTableColumn));
                processedColumnNames.add(column.getName());
            } else {
                newColumns.add(column.reindex(newRowIndex));
                processedColumnNames.add(column.getName());
            }
        }
        for (Column<T> secondColumn : secondTable) {
            if (!processedColumnNames.contains(secondColumn.getName())) {
                newColumns.add(secondColumn.reindex(newRowIndex));
            }
        }
        return new Table<>(newColumns);
    }

    /**
     * Affianca questa tabella con un'altra orizzontalmente.
     * Gli indici di colonna delle due tabelle non devono avere etichette in comune.
     * Il risultato sarà una nuova tabella con un indice di riga e un indice di colonna che sono la fusione
     * degli indici di riga e di colonna originali, rispettivamente.
     *
     * @param other     la tabella da affiancare a destra di questa.
     * @return una nuova tabella affiancata orizzontalmente.
     * @throws NullPointerException       se other è null.
     * @throws IllegalArgumentException   se i nomi delle colonne delle due tabelle hanno etichette in comune.
     */
    public Table<T> stack(Table<T> other) {
        Objects.requireNonNull(other, "La tabella da affiancare non può essere null.");
        Set<String> thisColNames = new HashSet<>();
        for (Column<T> col : this.columns) {
            thisColNames.add(col.getName());
        }
        for (Column<T> col : other) {
            if (thisColNames.contains(col.getName())) {
                throw new IllegalArgumentException("I nomi delle colonne delle tabelle da affiancare devono essere disgiunti.");
            }
        }
        Index newRowIndex = this.rowIndex.merge(other.rowIndex, this.rowIndex.getName());
        List<Column<T>> combinedColumns = new ArrayList<>();
        for (Column<T> col : this.columns) {
            combinedColumns.add(col.reindex(newRowIndex));
        }
        for (Column<T> col : other) {
            combinedColumns.add(col.reindex(newRowIndex));
        }
        return new Table<>(combinedColumns);
    }

    /**
     * Applica una funzione a ciascun valore di ogni colonna della tabella, producendo una nuova tabella
     * con valori trasformati. Gli indici di riga e i nomi delle colonne rimangono invariati.
     *
     * @param mapper     La funzione da applicare a ciascun valore (da T a R).
     * @param <R>        Il tipo dei valori della nuova tabella.
     * @return Una nuova tabella con i valori trasformati.
     * @throws NullPointerException se mapper è null.
     */
    public <R> Table<R> mapValues(Function<T, R> mapper) {
        Objects.requireNonNull(mapper, "La funzione mapper non può essere null.");
        List<Column<R>> newColumns = new ArrayList<>();
        for (Column<T> col : this.columns) {
            newColumns.add(col.map(mapper));
        }
        return new Table<>(newColumns);
    }

    /**
     * Data una funzione, restituisce una nuova tabella con i nuovi valori data dall'applicazione della
     * funzione alle sue colonne.
     * @param mapper  La funzione da applicare
     * @param <R>     Tipo del risultato della trasformazione
     * @return La tabella risultante dall'applicazione della funzione
     * @throws NullPointerException     Se la funzione data è null
     */
    public <R> Table<R> mapColumns(Function<Column<T>, Column<R>> mapper) {
        Objects.requireNonNull(mapper, "La funzione mapper non può essere null.");
        List<Column<R>> newColumns = new ArrayList<>();
        Index firstIndex = null;

        for (Column<T> col : columns) {
            Column<R> transformed = mapper.apply(col);
            if (firstIndex == null) {
                firstIndex = transformed.getIndex();
            } else {
                transformed = transformed.withIndex(firstIndex);
            }
            transformed = transformed.rename(col.getName());
            newColumns.add(transformed);
        }
        return new Table<>(newColumns);
    }

    @Override
    public Iterator<Column<T>> iterator() {
        return Collections.unmodifiableList(columns).iterator();
    }

    @Override
    public String toString() {
        if (columns.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();

        String rowHeaderTitle = (rowIndex.getName() != null) ? rowIndex.getName() : "";
        int rowLabelWidth = Math.max(1, rowHeaderTitle.length());
        for (int i = 0; i < rowIndex.size(); i++) {
            Object label = rowIndex.getLabelAt(i);
            if (label != null) {
                rowLabelWidth = Math.max(rowLabelWidth, label.toString().length());
            }
        }

        int[] colWidths = new int[columns.size()];
        for (int colIdx = 0; colIdx < columns.size(); colIdx++) {
            Column<T> col = columns.get(colIdx);
            int width = (col.getName() != null) ? col.getName().length() : 0;
            for (int i = 0; i < col.size(); i++) {
                T value = col.getValueAt(i);
                if (value != null) {
                    width = Math.max(width, value.toString().length());
                }
            }
            colWidths[colIdx] = Math.max(width, 1);
        }

        sb.append(String.format("%-" + rowLabelWidth + "s", rowHeaderTitle));
        for (int colIdx = 0; colIdx < columns.size(); colIdx++) {
            sb.append(" | ").append(String.format("%-" + colWidths[colIdx] + "s", columns.get(colIdx).getName()));
        }
        sb.append("\n");

        sb.append("-".repeat(rowLabelWidth));
        for (int colIdx = 0; colIdx < columns.size(); colIdx++) {
            sb.append("-+-");
            sb.append("-".repeat(colWidths[colIdx]));
        }
        sb.append("\n");

        for (int r = 0; r < rowIndex.size(); r++) {
            Object rowLabel = rowIndex.getLabelAt(r);
            sb.append(String.format("%" + rowLabelWidth + "s", rowLabel != null ? rowLabel : ""));
            for (int colIdx = 0; colIdx < columns.size(); colIdx++) {
                Column<T> col = columns.get(colIdx);
                T value = col.getValueAt(r);
                sb.append(" | ").append(String.format("%-" + colWidths[colIdx] + "s", value != null ? value : ""));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
