package clients;


import mole.*;
import utils.InputParsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TabellaImpilamento {
    private TabellaImpilamento() {}

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Table<Object> firstTable = readTable(scanner);
        Table<Object> secondTable = readTable(scanner);
        Table<Object> piledTable = firstTable.pileUp(secondTable);
        System.out.println(piledTable);
        scanner.close();
    }

    private static Table<Object> readTable(Scanner scanner) {
        if (!scanner.hasNext()) {
            throw new IllegalArgumentException("Riga vuota inaspettata");
        }
        String line = scanner.nextLine();
        InputParsing.TableDescriptor td = (InputParsing.TableDescriptor) InputParsing.parseDescriptor(line);
        if (td == null) {
            System.err.println("Descrittore tabella non valido.");
            System.exit(1);
        }
        List<Column<Object>> columns = new ArrayList<>();

        for (int col = 0; col < td.cols(); col++) {
            line = scanner.nextLine();
            InputParsing.ColumnDescriptor cd = (InputParsing.ColumnDescriptor) InputParsing.parseDescriptor(line);
            if (cd == null) {
                System.err.println("Descrittore colonna non valido.");
                System.exit(1);
            }
            line = scanner.nextLine();
            InputParsing.IndexDescriptor id = (InputParsing.IndexDescriptor) InputParsing.parseDescriptor(line);
            Index columnIndex;
            Object[] values;

            if (id != null) {
                Object[] labels = InputParsing.parseValues(scanner.nextLine(), id.len());
                columnIndex = new IndexObject(Arrays.asList(labels), id.name());
                values = InputParsing.parseValues(scanner.nextLine(), cd.rows());
            } else {
                columnIndex = new IndexNumerico(null, 0, cd.rows(), 1);
                values = InputParsing.parseValues(line, cd.rows());
            }
            Column<Object> column = new Column<>(Arrays.asList(values), columnIndex, cd.name());
            columns.add(column);
        }
        return new Table<>(columns);
    }
}
