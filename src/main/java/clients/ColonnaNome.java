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
import mole.Index;
import mole.IndexNumerico;
import mole.IndexObject;
import utils.InputParsing;

import java.util.List;
import java.util.Scanner;

public class ColonnaNome {
    private ColonnaNome() {}

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Errore: specifica il nuovo nome della colonna come argomento.");
        }
        String newName = args[0].equals("null") ? null : args[0];

        try (Scanner s = new Scanner(System.in)) {
            InputParsing.ColumnDescriptor cd =
                    (InputParsing.ColumnDescriptor) InputParsing.parseDescriptor(s.nextLine());
            if (cd == null) {
                throw new IllegalArgumentException("La prima riga deve essere un descrittore di colonna.");
            }
            String line = s.nextLine();
            InputParsing.IndexDescriptor id =
                    (InputParsing.IndexDescriptor) InputParsing.parseDescriptor(line);

            Index index;
            Object[] values;

            if (id != null) {
                Object[] labels = InputParsing.parseValues(s.nextLine(), cd.rows());
                index = new IndexObject(List.of(labels), id.name());
                values = InputParsing.parseValues(s.nextLine(), cd.rows());
            } else {
                index = new IndexNumerico(null, 0, cd.rows(), 1);
                values = InputParsing.parseValues(line, cd.rows());
            }
            Column<?> col = new Column<>(List.of(values), index, cd.name()).rename(newName);
            System.out.println(col);
        }
    }
}