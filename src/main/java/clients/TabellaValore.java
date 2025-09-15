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

public class TabellaValore {
    private TabellaValore() {}

    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Numero di argomenti inseriti incorretto.");
        }

        try (Scanner s = new Scanner(System.in)) {
            InputParsing.TableDescriptor tableDescriptor =
                    (InputParsing.TableDescriptor) InputParsing.parseDescriptor(s.nextLine());
            if (tableDescriptor == null) {
                throw new IllegalArgumentException("Descrittore tabella non valido");
            }
            List<Column<Object>> columns = new ArrayList<>();
            Index sharedIndex = null;
            for (int i = 0; i < tableDescriptor.cols(); i++) {
                InputParsing.ColumnDescriptor columnDescriptor =
                        (InputParsing.ColumnDescriptor) InputParsing.parseDescriptor(s.nextLine());
                if (columnDescriptor == null) {
                    throw new IllegalArgumentException("Descrittore colonna non valido");
                }

                String line = s.nextLine();
                InputParsing.IndexDescriptor indexDescriptor =
                        (InputParsing.IndexDescriptor) InputParsing.parseDescriptor(line);
                Column<Object> column;

                if (indexDescriptor == null) {
                    List<Object> values = Arrays.asList(InputParsing.parseValues(line, columnDescriptor.rows()));
                    if (sharedIndex == null) {
                        sharedIndex = new IndexNumerico(null, 0, columnDescriptor.rows(), 1);
                    }
                    column = new Column<>(values, sharedIndex, columnDescriptor.name());
                } else {
                    List<Object> indexValues = Arrays.asList(InputParsing.parseValues(s.nextLine(), indexDescriptor.len()));
                    if (sharedIndex == null) {
                        sharedIndex = new IndexObject(indexValues, indexDescriptor.name());
                    }
                    List<Object> values = Arrays.asList(InputParsing.parseValues(s.nextLine(), columnDescriptor.rows()));
                    column = new Column<>(values, sharedIndex, columnDescriptor.name());
                }
                columns.add(column);
            }

            Table<Object> table = new Table<>(columns);
            Object rowLabel = InputParsing.parseValues(args[0], 1)[0];
            String columnLabel = args[1];
            Object value = table.getValue(rowLabel, columnLabel);
            if (value == null) {
                throw new IllegalArgumentException("Valore non trovato per riga: " + rowLabel +
                        " e colonna: " + columnLabel);
            }
            System.out.println(value);
        }
    }
}