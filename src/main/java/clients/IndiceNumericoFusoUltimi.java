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

public class IndiceNumericoFusoUltimi {

    private IndiceNumericoFusoUltimi() {}

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("Specificare inizio, fine e passo come argomenti da linea di comando.");
        }

        int start = Integer.parseInt(args[0]);
        int end = Integer.parseInt(args[1]);
        int step = Integer.parseInt(args[2]);

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
        int n = 10;
        List<Object> lastNumericLabels = new ArrayList<>();
        int sizeNum = firstIndex.size();
        int startIndex = Math.max(0, sizeNum - n);
        for (int i = startIndex; i < sizeNum; i++) {
            lastNumericLabels.add(firstIndex.getLabelAt(i));
        }
        List<Object> lastMerged = new ArrayList<>(lastNumericLabels);
        for (Object lbl : secondIndex) {
            if (!lastMerged.contains(lbl)) {
                lastMerged.add(lbl);
            }
        }
        int startFinal = Math.max(0, lastMerged.size() - n);
        List<Object> output = lastMerged.subList(startFinal, lastMerged.size());
        System.out.println(String.join(", ", output.stream().map(Object::toString).toList()));
    }
}