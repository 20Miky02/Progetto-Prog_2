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

import java.util.Arrays;
import java.util.Scanner;

public class ColonnaMoltiplica {
    private ColonnaMoltiplica() {}

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Numero inserito di argomenti sbagliato.");
        }
        int fattore;
        fattore = Integer.parseInt(args[0]);
        try (Scanner s = new Scanner(System.in)) {
            Column<Integer> column = readIntegerColumn(s);
            Column<Integer> multipliedColumn = column.map(value -> {
                if (value == null) return null;
                return value * fattore;
            });
            System.out.print(multipliedColumn);

        }
    }

    private static Column<Integer> readIntegerColumn(Scanner scanner) {
        if (!scanner.hasNextLine()) {
            throw new IllegalArgumentException("Input vuoto: atteso descrittore colonna");
        }
        String line = scanner.nextLine().trim();
        InputParsing.ColumnDescriptor cd =
                (InputParsing.ColumnDescriptor) InputParsing.parseDescriptor(line);
        if (cd == null) {
            throw new IllegalArgumentException("Descrittore colonna non valido: " + line);
        }
        if (!scanner.hasNextLine()) {
            throw new IllegalArgumentException("Input incompleto: attesa riga successiva dopo descrittore colonna");
        }
        String nextLine = scanner.nextLine().trim();
        InputParsing.IndexDescriptor indexDescriptor =
                (InputParsing.IndexDescriptor) InputParsing.parseDescriptor(nextLine);

        Index columnIndex;
        Object[] rawValues;

        if (indexDescriptor == null) {
            columnIndex = new IndexNumerico(null, 0, cd.rows(), 1);
            rawValues = InputParsing.parseValues(nextLine, cd.rows());
        } else {
            if (!scanner.hasNextLine()) {
                throw new IllegalArgumentException("Input incompleto: attese etichette indice");
            }
            Object[] labels = InputParsing.parseValues(scanner.nextLine(), indexDescriptor.len());
            columnIndex = new IndexObject(Arrays.asList(labels), indexDescriptor.name());

            if (!scanner.hasNextLine()) {
                throw new IllegalArgumentException("Input incompleto: attesi valori colonna");
            }
            rawValues = InputParsing.parseValues(scanner.nextLine(), cd.rows());
        }
        Integer[] integerValues = new Integer[rawValues.length];
        for (int i = 0; i < rawValues.length; i++) {
            if (rawValues[i] == null) {
                integerValues[i] = null;
            } else if (rawValues[i] instanceof Integer) {
                integerValues[i] = (Integer) rawValues[i];
            } else if (rawValues[i] instanceof Number) {
                integerValues[i] = ((Number) rawValues[i]).intValue();
            } else {
                throw new IllegalArgumentException("Valore non convertibile a Integer alla posizione " + i + ": " + rawValues[i]);
            }
        }
        return new Column<>(Arrays.asList(integerValues), columnIndex, cd.name());
    }
}