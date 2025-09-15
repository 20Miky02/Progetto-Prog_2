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
import mole.Column;
import mole.IndexObject;
import utils.InputParsing;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ColonnaImpilamento {
    private ColonnaImpilamento() {}

    public static void main(String[] args) {
        try (Scanner s = new Scanner(System.in)) {
            boolean first = true;
            while (s.hasNext()) {
                InputParsing.ColumnDescriptor columnDescriptor1 =
                        (InputParsing.ColumnDescriptor) InputParsing.parseDescriptor(s.nextLine());
                if (columnDescriptor1 == null) break;
                InputParsing.IndexDescriptor indexDescriptor1 =
                        (InputParsing.IndexDescriptor) InputParsing.parseDescriptor(s.nextLine());
                if (indexDescriptor1 == null) {
                    throw new IllegalArgumentException("Errore: indice esplicito atteso per prima colonna");
                }

                IndexObject index1 = new IndexObject(
                        Arrays.asList(InputParsing.parseValues(s.nextLine(), indexDescriptor1.len())),
                        indexDescriptor1.name()
                );
                List<Object> values1 = Arrays.asList(InputParsing.parseValues(s.nextLine(), columnDescriptor1.rows()));
                Column<Object> column1 = new Column<>(values1, index1, columnDescriptor1.name());

                if (!s.hasNext()) break;
                InputParsing.ColumnDescriptor columnDescriptor2 =
                        (InputParsing.ColumnDescriptor) InputParsing.parseDescriptor(s.nextLine());
                if (columnDescriptor2 == null) break;

                InputParsing.IndexDescriptor indexDescriptor2 =
                        (InputParsing.IndexDescriptor) InputParsing.parseDescriptor(s.nextLine());
                if (indexDescriptor2 == null) {
                    throw new IllegalArgumentException("Errore: indice esplicito atteso per seconda colonna");
                }

                IndexObject index2 = new IndexObject(
                        Arrays.asList(InputParsing.parseValues(s.nextLine(), indexDescriptor2.len())),
                        indexDescriptor2.name()
                );
                List<Object> values2 = Arrays.asList(InputParsing.parseValues(s.nextLine(), columnDescriptor2.rows()));
                Column<Object> column2 = new Column<>(values2, index2, columnDescriptor2.name());

                if (!first) {
                    System.out.println();
                }
                first = false;
                Column<Object> piledColumn = column1.pileUp(column2);
                System.out.print(piledColumn);
            }
        }
    }
}