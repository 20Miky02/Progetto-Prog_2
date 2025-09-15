/*

Copyright 2024 Massimo Santini

This file is part of "Programmazione 2 @ UniMI" teaching material.

This is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This material is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this file.  If not, see <https://www.gnu.org/licenses/>.

*/

package clients;

import mole.*;
import utils.InputParsing;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TabellaAffiancamento {
    private TabellaAffiancamento() {}

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Table<Object> firstTable = readTable(scanner);
        Table<Object> secondTable = readTable(scanner);
        Table<Object> stackedTable = firstTable.stack(secondTable);
        System.out.println(stackedTable);
        scanner.close();
    }

    private static Table<Object> readTable(Scanner scanner) {
        String line = scanner.nextLine();
        InputParsing.TableDescriptor td = (InputParsing.TableDescriptor) InputParsing.parseDescriptor(line);
        if (td == null) {
            throw new IllegalArgumentException("Descrittore tabella non valido.");
        }
        List<Column<Object>> columns = new ArrayList<>();

        for (int col = 0; col < td.cols(); col++) {
            line = scanner.nextLine();
            InputParsing.ColumnDescriptor cd = (InputParsing.ColumnDescriptor) InputParsing.parseDescriptor(line);
            if (cd == null) {
                throw new IllegalArgumentException("Descrittore colonna non valido.");
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