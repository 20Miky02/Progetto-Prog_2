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

import java.util.List;
import java.util.Scanner;

public class IndiceMedesimo {
    private IndiceMedesimo() {}

    public static void main(String[] args) {
        try (Scanner s = new Scanner(System.in)) {
            while (s.hasNextLine()) {
                String desc1Line = s.nextLine();
                if (desc1Line.isEmpty()) break;
                InputParsing.IndexDescriptor desc1 = (InputParsing.IndexDescriptor) InputParsing.parseDescriptor(desc1Line);
                if (desc1 == null) break;

                String values1Line = s.nextLine();
                Object[] values1 = InputParsing.parseValues(values1Line, desc1.len());
                Index index1 = new IndexObject(List.of(values1), desc1.name());

                if (!s.hasNextLine()) break;
                String desc2Line = s.nextLine();
                InputParsing.IndexDescriptor desc2 = (InputParsing.IndexDescriptor) InputParsing.parseDescriptor(desc2Line);
                if (desc2 == null) break;

                if (!s.hasNextLine()) break;
                String values2Line = s.nextLine();
                Object[] values2 = InputParsing.parseValues(values2Line, desc2.len());
                Index index2 = new IndexObject(List.of(values2), desc2.name());

                System.out.println(index1.equals(index2));
            }
        }
    }
}
