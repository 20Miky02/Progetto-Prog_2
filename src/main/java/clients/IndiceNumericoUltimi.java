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
import java.util.Scanner;

public class IndiceNumericoUltimi {
    private IndiceNumericoUltimi() {}

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("Specificare inizio, fine e passo come argomenti da linea di comando.");
        }

        int start = Integer.parseInt(args[0]);
        int end   = Integer.parseInt(args[1]);
        int step  = Integer.parseInt(args[2]);

        IndexNumerico index = new IndexNumerico(null, start, end, step);

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextInt()) {
                int N = scanner.nextInt();
                if (N <= 0) continue;

                int size = index.size();
                int begin = Math.max(0, size - N);

                StringBuilder sb = new StringBuilder();
                for (int i = begin; i < size; i++) {
                    if (sb.length() > 0) sb.append(", ");
                    sb.append(index.getLabelAt(i));
                }
                System.out.println(sb);
            }
        }
    }
}