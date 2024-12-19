// Day 19: Linen Layout

import java.io.*;
import java.util.*;

public class PartTwo {

    // https://adventofcode.com/2024/day/19/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            List<String> towelPatterns = Arrays.asList(lines.getFirst().split(", "));
            List<String> designs = lines.subList(2, lines.size());

            long totalWays = 0;

            for (String design : designs) {
                totalWays += countWaysToFormDesign(design, towelPatterns);
            }
            System.out.printf("Total number of ways to form all designs: %d\n", totalWays);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static long countWaysToFormDesign(String design, List<String> towelPatterns) {
        int n = design.length();
        long[] dynamicProgramming = new long[n + 1];
        dynamicProgramming[0] = 1;

        for (int i = 1; i <= n; i++) {
            for (String pattern : towelPatterns) {
                int len = pattern.length();
                if (i >= len && design.substring(i - len, i).equals(pattern)) {
                    dynamicProgramming[i] += dynamicProgramming[i - len];
                }
            }
        }

        return dynamicProgramming[n];
    }
}
