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
import utils.InputParsing;
import mole.IndexObject;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ColonnaIndice {
    private ColonnaIndice() {}

    public static void main(String[] args) {
        try (Scanner s = new Scanner(System.in)) {
            InputParsing.ColumnDescriptor cd =
                    (InputParsing.ColumnDescriptor) InputParsing.parseDescriptor(s.nextLine());
            String line = s.nextLine();
            InputParsing.IndexDescriptor idxDesc =
                    (InputParsing.IndexDescriptor) InputParsing.parseDescriptor(line);
            Column<?> col;

            if (idxDesc == null) {
                Object[] values = InputParsing.parseValues(line, cd.rows());
                List<Object> numericLabels = IntStream.range(0, cd.rows())
                        .boxed()
                        .collect(Collectors.toList())
                        .stream()
                        .map(Object.class::cast)
                        .collect(Collectors.toList());
                IndexObject idx = new IndexObject(numericLabels, null);
                col = new Column<>(Arrays.asList(values), idx, cd.name());
            } else {
                Object[] labels = InputParsing.parseValues(s.nextLine(), idxDesc.len());
                IndexObject idx = new IndexObject(Arrays.asList(labels), idxDesc.name());
                Object[] values = InputParsing.parseValues(s.nextLine(), cd.rows());
                col = new Column<>(Arrays.asList(values), idx, cd.name());
            }

            InputParsing.IndexDescriptor newIdxDesc =
                    (InputParsing.IndexDescriptor) InputParsing.parseDescriptor(s.nextLine());

            Object[] newLabels = InputParsing.parseValues(s.nextLine(), newIdxDesc.len());
            IndexObject newIndex = new IndexObject(Arrays.asList(newLabels), newIdxDesc.name());
            Column<?> newCol;
            if (newIndex.size() == col.size()) {
                newCol = col.withIndex(newIndex);
            } else {
                List<Object> standardLabels = IntStream.range(0, col.size())
                        .boxed()
                        .collect(Collectors.toList())
                        .stream()
                        .map(Object.class::cast)
                        .collect(Collectors.toList());
                IndexObject standardIndex = new IndexObject(standardLabels, null);
                newCol = col.withIndex(standardIndex);
            }

            System.out.print(newCol);
        }
    }
}