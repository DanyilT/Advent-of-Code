// Day 1: Historian Hysteria

import java.io.*;
import java.util.*;

public class PartTwo {

    // https://adventofcode.com/2024/day/1/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();
            List<Integer> leftList = new ArrayList<>();
            List<Integer> rightList = new ArrayList<>();

            for (String line : lines) {
                String[] sides = line.split("\\s+");
                leftList.add(Integer.parseInt(sides[0]));
                rightList.add(Integer.parseInt(sides[1]));
            }

            int similarity = 0;
            if (leftList.size() == rightList.size()) {
                for (int leftNumber : leftList) {
                    similarity += leftNumber * Collections.frequency(rightList, leftNumber);
                    System.out.printf("Line %d: Left (%d) * Right count (%d) | Similarity (%d)\n", leftList.indexOf(leftNumber) + 1, leftNumber, Collections.frequency(rightList, leftNumber), leftNumber * Collections.frequency(rightList, leftNumber));
                }
            }
            System.out.printf("Total similarity: %d\n", similarity);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
