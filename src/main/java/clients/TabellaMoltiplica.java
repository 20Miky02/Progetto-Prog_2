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

public class TabellaMoltiplica {
    private TabellaMoltiplica() {}

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Inserire fattore moltiplicativo.");
        }

        int fattore;
        fattore = Integer.parseInt(args[0]);
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        InputParsing.TableDescriptor td = (InputParsing.TableDescriptor) InputParsing.parseDescriptor(line);
        if (td == null) {
            throw new IllegalArgumentException("Descrittore tabella non valido.");
        }
        Index commonIndex = new IndexNumerico(null, 0, td.rows(), 1);
        List<Column<Object>> columns = new ArrayList<>();

        for (int col = 0; col < td.cols(); col++) {
            if (!scanner.hasNextLine()) {
                throw new IllegalArgumentException("Fine input inaspettata per colonna " + col);
            }
            line = scanner.nextLine();
            InputParsing.ColumnDescriptor cd = (InputParsing.ColumnDescriptor) InputParsing.parseDescriptor(line);
            if (cd == null) {
                throw new IllegalArgumentException("Descrittore colonna non valido.");
            }
            if (cd.rows() != td.rows()) {
                throw new IllegalArgumentException("Il numero di righe della colonna " + col + " non corrisponde al descrittore della tabella.");
            }
            if (!scanner.hasNextLine()) {
                throw new IllegalArgumentException("Fine input inaspettata per dati colonna " + col);
            }
            line = scanner.nextLine();
            InputParsing.IndexDescriptor id = (InputParsing.IndexDescriptor) InputParsing.parseDescriptor(line);
            Object[] values;

            if (id != null) {
                if (!scanner.hasNextLine()) {
                    throw new IllegalArgumentException("Fine input inaspettata per etichette indice colonna " + col);
                }
                Object[] labels = InputParsing.parseValues(scanner.nextLine(), id.len());
                if (!scanner.hasNextLine()) {
                    throw new IllegalArgumentException("Fine input inaspettata per valori colonna " + col);
                }
                values = InputParsing.parseValues(scanner.nextLine(), cd.rows());
                if (col == 0) {
                    commonIndex = new IndexObject(Arrays.asList(labels), id.name());
                }
            } else {
                values = InputParsing.parseValues(line, cd.rows());
            }
            Column<Object> column = new Column<>(Arrays.asList(values), commonIndex, cd.name());
            columns.add(column);
        }
        Table<Object> originalTable = new Table<>(columns);
        Table<Object> multipliedTable = originalTable.mapValues(value -> {
            if (value instanceof Integer) {
                return (Integer) value * fattore;
            }
            return value;
        });
        System.out.println(multipliedTable);
        scanner.close();
    }
}