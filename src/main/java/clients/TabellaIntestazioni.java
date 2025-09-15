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
import java.util.*;

public class TabellaIntestazioni {
    private TabellaIntestazioni() {}

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        InputParsing.TableDescriptor td = (InputParsing.TableDescriptor) InputParsing.parseDescriptor(line);
        if (td == null) {
            throw new IllegalArgumentException("Descrittore tabella non valido");
        }
        List<Column<Object>> columns = new ArrayList<>();
        Index tableRowIndex = null;

        for (int i = 0; i < td.cols(); i++) {
            String colLine = scanner.nextLine();
            InputParsing.ColumnDescriptor cd = (InputParsing.ColumnDescriptor) InputParsing.parseDescriptor(colLine);
            if (cd == null) {
                throw new IllegalArgumentException("Descrittore colonna non valido");
            }
            String nextLine = scanner.nextLine();
            InputParsing.IndexDescriptor id = (InputParsing.IndexDescriptor) InputParsing.parseDescriptor(nextLine);
            Index columnIndex;
            Object[] values;
            if (id != null) {
                Object[] indexLabels = InputParsing.parseValues(scanner.nextLine(), id.len());
                columnIndex = new IndexObject(Arrays.asList(indexLabels), id.name());
                values = InputParsing.parseValues(scanner.nextLine(), cd.rows());
            } else {
                columnIndex = new IndexNumerico(null, 0, cd.rows(), 1);
                values = InputParsing.parseValues(nextLine, cd.rows());
            }
            Column<Object> column = new Column<>(Arrays.asList(values), columnIndex, cd.name());
            columns.add(column);
            if (tableRowIndex == null) {
                tableRowIndex = columnIndex;
            }
        }

        List<Column<Object>> reindexedColumns = new ArrayList<>();
        for (Column<Object> col : columns) {
            reindexedColumns.add(col.reindex(tableRowIndex));
        }
        Table<Object> originalTable = new Table<>(reindexedColumns);
        List<Object> newNames = new ArrayList<>();
        for (String arg : args) {
            newNames.add(arg);
        }
        Index newColumnIndex = new IndexObject(newNames, null);
        Table<Object> renamedTable = originalTable.renameColumns(newColumnIndex);
        System.out.println(renamedTable);
        for (int i = 0; i < columns.size(); i++) {
            Column<Object> originalCol = columns.get(i);
            String newName = args[i];
            Column<Object> renamedCol = originalCol.rename(newName);
            System.out.println(renamedCol);
        }
    }
}