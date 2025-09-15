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
import mole.Index;
import mole.IndexObject;
import java.util.Scanner;
import java.util.List;

public class IndiceFusione {
    private IndiceFusione() {}

    public static void main(String[] args) {
        try (Scanner s = new Scanner(System.in)) {
            while (s.hasNextLine()) {
                InputParsing.IndexDescriptor desc1 = (InputParsing.IndexDescriptor) InputParsing.parseDescriptor(s.nextLine());
                Object[] values1 = InputParsing.parseValues(s.nextLine(), desc1.len());
                List<Object> labels1 = List.of(values1);

                if (!s.hasNextLine()) break;
                InputParsing.IndexDescriptor desc2 = (InputParsing.IndexDescriptor) InputParsing.parseDescriptor(s.nextLine());
                Object[] values2 = InputParsing.parseValues(s.nextLine(), desc2.len());
                List<Object> labels2 = List.of(values2);

                Index index1 = new IndexObject(labels1, desc1.name());
                Index index2 = new IndexObject(labels2, desc2.name());

                Index mergedIndex = index1.merge(index2,desc1.name());
                System.out.println(mergedIndex);
            }
        }
    }
}