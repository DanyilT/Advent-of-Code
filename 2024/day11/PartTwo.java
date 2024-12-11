// Day 11: Plutonian Pebbles

import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class PartTwo {

    // https://adventofcode.com/2024/day/11/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            // Pretty much the same as PartOne.java, but using BigInteger instead of Long & Map instead of List for better performance
            final int BLINKS = 75;

            Map<BigInteger, BigInteger> stones = new HashMap<>();
            for (String line : lines) {
                for (String num : line.split("\\s+")) {
                    stones.put(new BigInteger(num), stones.getOrDefault(new BigInteger(num), BigInteger.ZERO).add(BigInteger.ONE));
                }
            }

            for (int blink = 0; blink < BLINKS; blink++) {
                Map<BigInteger, BigInteger> newStones = new HashMap<>();
                for (Map.Entry<BigInteger, BigInteger> entry : stones.entrySet()) {
                    if (entry.getKey().equals(BigInteger.ZERO)) { // Rule 1
                        newStones.put(BigInteger.ONE, newStones.getOrDefault(BigInteger.ONE, BigInteger.ZERO).add(entry.getValue()));
                    } else if (String.valueOf(entry.getKey()).length() % 2 == 0) { // Rule 2
                        String digits = String.valueOf(entry.getKey());
                        int mid = digits.length() / 2;
                        newStones.put(new BigInteger(digits.substring(0, mid)), newStones.getOrDefault(new BigInteger(digits.substring(0, mid)), BigInteger.ZERO).add(entry.getValue()));
                        newStones.put(new BigInteger(digits.substring(mid)), newStones.getOrDefault(new BigInteger(digits.substring(mid)), BigInteger.ZERO).add(entry.getValue()));
                    } else { // Rule 3
                        newStones.put(entry.getKey().multiply(BigInteger.valueOf(2024)), newStones.getOrDefault(entry.getKey().multiply(BigInteger.valueOf(2024)), BigInteger.ZERO).add(entry.getValue()));
                    }
                }
                stones = newStones;
            }
            System.out.printf("Number of stones after %d blinks: %d\n", BLINKS, stones.values().stream().reduce(BigInteger.ZERO, BigInteger::add));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
