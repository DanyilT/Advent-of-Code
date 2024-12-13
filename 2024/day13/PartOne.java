// Day 13: Claw Contraption

import java.io.*;
import java.util.*;

public class PartOne {

    // https://adventofcode.com/2024/day/13/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            int totalTokens = 0;
            for (int i = 0; i < lines.size(); i += 4) {
                String[] buttonA = lines.get(i).split(", ");
                String[] buttonB = lines.get(i + 1).split(", ");
                String[] prize = lines.get(i + 2).split(", ");

                int ax = Integer.parseInt(buttonA[0].split("\\+")[1]);
                int ay = Integer.parseInt(buttonA[1].split("\\+")[1]);
                int bx = Integer.parseInt(buttonB[0].split("\\+")[1]);
                int by = Integer.parseInt(buttonB[1].split("\\+")[1]);
                int px = Integer.parseInt(prize[0].split("=")[1]);
                int py = Integer.parseInt(prize[1].split("=")[1]);

                int tokens = findMinimumTokens(ax, ay, bx, by, px, py);
                if (tokens != Integer.MAX_VALUE) {
                    totalTokens += tokens;
                }
            }

            System.out.printf("Total minimum tokens to win all possible prizes: %d\n", totalTokens);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static int findMinimumTokens(int ax, int ay, int bx, int by, int px, int py) {
        int minTokens = Integer.MAX_VALUE;
        for (int a = 0; a <= 100; a++) {
            for (int b = 0; b <= 100; b++) {
                if (a * ax + b * bx == px && a * ay + b * by == py) {
                    minTokens = Math.min(minTokens, a * 3 + b);
                }
            }
        }
        return minTokens;
    }
}
