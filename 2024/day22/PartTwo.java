// Day 22: Monkey Market

import java.io.*;
import java.util.*;

public class PartTwo {

    // https://adventofcode.com/2024/day/22/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            final int CHANGE_COUNT = 2000;
            final int SEARCH_LENGTH = 4;
            final int BASE = 19;
            final int AGGREGATOR_SIZE = BASE * BASE * BASE * BASE;

            List<Integer> buyers = new ArrayList<>();
            for (String line : lines) {
                buyers.add(Integer.parseInt(line.trim()));
            }

            List<int[]> allBuyerPrices = new ArrayList<>();
            List<int[]> allBuyerChanges = new ArrayList<>();

            for (int secret : buyers) {
                int[] prices = new int[CHANGE_COUNT + 1];
                prices[0] = getPrice(secret);

                int currentSecret = secret;
                for (int i = 1; i <= CHANGE_COUNT; i++) {
                    currentSecret = nextSecret(currentSecret);
                    prices[i] = getPrice(currentSecret);
                }

                int[] changes = new int[CHANGE_COUNT];
                for (int i = 0; i < CHANGE_COUNT; i++) {
                    changes[i] = prices[i + 1] - prices[i];
                }

                allBuyerPrices.add(prices);
                allBuyerChanges.add(changes);
            }

            int[] aggregator = new int[AGGREGATOR_SIZE];

            for (int buyerIndex = 0; buyerIndex < buyers.size(); buyerIndex++) {
                int[] changes = allBuyerChanges.get(buyerIndex);
                int[] prices = allBuyerPrices.get(buyerIndex);

                boolean[] usedPattern = new boolean[AGGREGATOR_SIZE];

                for (int i = 0; i <= CHANGE_COUNT - SEARCH_LENGTH; i++) {
                    int c1 = changes[i];
                    int c2 = changes[i + 1];
                    int c3 = changes[i + 2];
                    int c4 = changes[i + 3];

                    int patternId = encodePattern(c1, c2, c3, c4);
                    if (patternId < 0) continue;

                    if (!usedPattern[patternId]) {
                        aggregator[patternId] += prices[i + 4];
                        usedPattern[patternId] = true;
                    }
                }
            }

            int maxSum = 0;
            for (int value : aggregator) {
                if (value > maxSum) {
                    maxSum = value;
                }
            }
            System.out.printf("Most profitable pattern (bananas can get): %d\n", maxSum);
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

    private static int getPrice(int secret) {
        return Math.abs(secret % 10);
    }

    private static int encodePattern(int c1, int c2, int c3, int c4) {
        final int OFFSET = 9;
        final int BASE = 19;

        int p1 = c1 + OFFSET;
        int p2 = c2 + OFFSET;
        int p3 = c3 + OFFSET;
        int p4 = c4 + OFFSET;

        if (p1 < 0 || p1 >= BASE || p2 < 0 || p2 >= BASE || p3 < 0 || p3 >= BASE || p4 < 0 || p4 >= BASE) return -1;
        return p1 * (BASE * BASE * BASE) + p2 * (BASE * BASE) + p3 * BASE + p4;
    }
}
