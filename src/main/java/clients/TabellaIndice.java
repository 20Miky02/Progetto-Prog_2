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

public class TabellaIndice {
    private TabellaIndice() {}

    public static void main(String[] args) {
        try (Scanner s = new Scanner(System.in)) {
            InputParsing.TableDescriptor td =
                    (InputParsing.TableDescriptor) InputParsing.parseDescriptor(s.nextLine());
            if (td == null) {
                throw new IllegalArgumentException("Descrittore tabella non valido.");
            }
            List<Column<Object>> columns = new ArrayList<>();
            Index sharedIndex = null;
            List<Object> newLabels = Arrays.asList((Object[]) args);

            for (int i = 0; i < td.cols(); i++) {
                InputParsing.ColumnDescriptor cd =
                        (InputParsing.ColumnDescriptor) InputParsing.parseDescriptor(s.nextLine());
                if (cd == null) {
                    throw new IllegalArgumentException("Descrittore colonna non valido.");
                }

                String line = s.nextLine();
                InputParsing.IndexDescriptor id =
                        (InputParsing.IndexDescriptor) InputParsing.parseDescriptor(line);

                Column<Object> column;

                if (id == null) {
                    Object[] values = InputParsing.parseValues(line, cd.rows());

                    if (sharedIndex == null) {
                        sharedIndex = new IndexNumerico(null, 0, cd.rows(), 1);
                    }

                    column = new Column<>(Arrays.asList(values), sharedIndex, cd.name());
                } else {
                    Object[] labels = InputParsing.parseValues(s.nextLine(), id.len());
                    if (sharedIndex == null) {
                        sharedIndex = new IndexObject(Arrays.asList(labels), id.name());
                    }

                    Object[] values = InputParsing.parseValues(s.nextLine(), cd.rows());
                    column = new Column<>(Arrays.asList(values), sharedIndex, cd.name());
                }
                columns.add(column);
            }

            Table<Object> originalTable = new Table<>(columns);
            Index newRowIndex = new IndexObject(newLabels, null);
            Table<Object> newTable = originalTable.withRowIndex(newRowIndex);

            System.out.println(newTable);
            for (Column<Object> column : newTable) {
                System.out.println(column);
            }
        }
    }
}