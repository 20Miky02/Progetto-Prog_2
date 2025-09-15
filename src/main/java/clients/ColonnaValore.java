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

import utils.InputParsing;
import utils.InputParsing.ColumnDescriptor;
import utils.InputParsing.IndexDescriptor;

import java.util.Scanner;

public class ColonnaValore {

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Errore: Specificare l'etichetta da cercare come argomento.");
        }

        String targetLabel = args[0];
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine().trim();
        ColumnDescriptor column = (ColumnDescriptor) InputParsing.parseDescriptor(line);
        line = scanner.nextLine().trim();
        IndexDescriptor index = (IndexDescriptor) InputParsing.parseDescriptor(line);
        line = scanner.nextLine().trim();
        Object[] labels = InputParsing.parseValues(line, index.len());
        line = scanner.nextLine().trim();
        Object[] values = InputParsing.parseValues(line, column.rows());
        for (int i = 0; i < labels.length; i++) {
            if (labels[i].toString().equals(targetLabel)) {
                System.out.println(values[i]);
                return;
            }
        }
        System.err.println("Etichetta non trovata: " + targetLabel);
        System.exit(2);
    }
}