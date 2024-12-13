// Day 13: Claw Contraption

import java.io.*;
import java.util.*;

public class PartTwo {

    // https://adventofcode.com/2024/day/13/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            final long OFFSET = 10000000000000L;

            long totalTokens = 0;
            for (int i = 0; i < lines.size(); i += 4) {
                String[] buttonA = lines.get(i).split(", ");
                String[] buttonB = lines.get(i + 1).split(", ");
                String[] prize = lines.get(i + 2).split(", ");

                int ax = Integer.parseInt(buttonA[0].split("\\+")[1]);
                int ay = Integer.parseInt(buttonA[1].split("\\+")[1]);
                int bx = Integer.parseInt(buttonB[0].split("\\+")[1]);
                int by = Integer.parseInt(buttonB[1].split("\\+")[1]);
                long px = Long.parseLong(prize[0].split("=")[1]) + OFFSET;
                long py = Long.parseLong(prize[1].split("=")[1]) + OFFSET;

                totalTokens += findMinimumTokens(ax, ay, bx, by, px, py);
            }
            System.out.printf("Total minimum tokens to win all possible prizes: %d\n", totalTokens);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static long findMinimumTokens(int ax, int ay, int bx, int by, long px, long py) {
        long minTokens = 0;

        double timesB = (double) (py * ax - px * ay) / (by * ax - bx * ay);
        double timesA = (px - bx * timesB) / ax;

        if (Math.abs(timesA - Math.round(timesA)) < 1e-9 && Math.abs(timesB - Math.round(timesB)) < 1e-9) {
            minTokens = Math.round(timesA) * 3 + Math.round(timesB);
        }
        return minTokens;
    }
}
