// Day 11: Plutonian Pebbles

import java.io.*;
import java.util.*;

public class PartOne {

    // https://adventofcode.com/2024/day/11/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            final int BLINKS = 25;

            List<Long> stones = new ArrayList<>();
            for (String line : lines) {
                for (String num : line.split("\\s+")) {
                    stones.add(Long.parseLong(num));
                }
            }

            for (int blink = 0; blink < BLINKS; blink++) {
                List<Long> newStones = new ArrayList<>();
                for (long stone : stones) {
                    if (stone == 0) { // Rule 1
                        newStones.add(1L);
                    } else if (String.valueOf(stone).length() % 2 == 0) { // Rule 2
                        String digits = String.valueOf(stone);
                        int mid = digits.length() / 2;
                        newStones.add(Long.parseLong(digits.substring(0, mid)));
                        newStones.add(Long.parseLong(digits.substring(mid)));
                    } else { // Rule 3
                        newStones.add(stone * 2024);
                    }
                }
                stones = newStones;
            }
            System.out.printf("Number of stones after %d blinks: %d\n", BLINKS, stones.size());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
