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

import mole.IndexNumerico;
import mole.IndexObject;
import utils.InputParsing;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IndiceNumericoFusoSalti {

    private IndiceNumericoFusoSalti() {}

    public static void main(String[] args) {
        if (args.length != 4) {
            throw new IllegalArgumentException("Specificare inizio, fine, passo e salto come argomenti da linea di comando.");
        }
        int start = Integer.parseInt(args[0]);
        int end = Integer.parseInt(args[1]);
        int step = Integer.parseInt(args[2]);
        int salto = Integer.parseInt(args[3]);
        if (salto <= 0) {
            throw new IllegalArgumentException("Specificare inizio, fine, passo e salto come argomenti da linea di comando.");
        }
        IndexNumerico firstIndex = new IndexNumerico(null, start, end, step);
        IndexObject secondIndex = null;
        try (Scanner scanner = new Scanner(System.in)) {
            if (scanner.hasNextLine()) {
                InputParsing.IndexDescriptor descriptor =
                        (InputParsing.IndexDescriptor) InputParsing.parseDescriptor(scanner.nextLine());
                if (descriptor != null && scanner.hasNextLine()) {
                    List<Object> labels =
                            List.of(InputParsing.parseValues(scanner.nextLine(), descriptor.len()));
                    secondIndex = new IndexObject(labels, descriptor.name());
                }
            }
        }
        if (secondIndex == null) {
            throw new IllegalArgumentException("Impossibile leggere il secondo indice dall'input.");
        }
        List<Object> output = new ArrayList<>();
        int sizeNum = firstIndex.size();
        for (int pos = 0; pos < sizeNum; pos += salto) {
            output.add(firstIndex.getLabelAt(pos));
        }
        int startSecond = 0;
        int posInMerged = sizeNum;
        for (Object lbl : secondIndex) {
            if ((posInMerged - startSecond) % salto == 0) {
                output.add(lbl);
            }
            posInMerged++;
        }
        System.out.println(String.join(", ", output.stream().map(Object::toString).toList()));
    }
}