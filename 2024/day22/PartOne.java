// Day 22: Monkey Market

import java.io.*;
import java.util.*;

public class PartOne {

    // https://adventofcode.com/2024/day/22/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            final int CHANGE_COUNT = 2000;

            long sum = 0;
            for (String line : lines) {
                int secret = Integer.parseInt(line.trim());
                for (int i = 0; i < CHANGE_COUNT; i++) {
                    secret = nextSecret(secret);
                }
                sum += secret;
            }
            System.out.printf("Sum of secrets: %d\n", sum);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static int nextSecret(int secret) {
        final int MODULO = 16777216;

        // Step 1: multiply by 64, XOR, then prune
        long r1 = (long) secret * 64;
        secret = (int) (( secret ^ r1) % MODULO);

        // Step 2: divide by 32 (floor), XOR, then prune
        long r2 = secret / 32;
        secret = (int) (( secret ^ r2) % MODULO);

        // Step 3: multiply by 2048, XOR, then prune
        long r3 = (long) secret * 2048;
        secret = (int) (( secret ^ r3) % MODULO);

        return secret;
    }
}
