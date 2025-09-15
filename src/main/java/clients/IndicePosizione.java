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
import mole.IndexObject;
import java.util.Scanner;
import java.util.List;

public class IndicePosizione {

    private IndicePosizione() {
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("E' necessario passare un'etichetta come argomento.");
        }
        try (Scanner s = new Scanner(System.in)) {
            InputParsing.IndexDescriptor descriptor = (InputParsing.IndexDescriptor) InputParsing.parseDescriptor(s.nextLine());
            if (descriptor == null) {
                throw new IllegalArgumentException("Descrittore non valido");
            }
            Object[] values = InputParsing.parseValues(s.nextLine(), descriptor.len());
            IndexObject index = new IndexObject(List.of(values), descriptor.name());
            Object label = InputParsing.parseValues(args[0], 1)[0];
            System.out.println(index.getPositionOf(label));
        }
    }
}