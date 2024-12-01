// Day 1: Historian Hysteria

import java.io.*;
import java.util.*;

public class PartOne {

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

            Collections.sort(leftList);
            Collections.sort(rightList);

            int distance = 0;
            if (leftList.size() == rightList.size()) {
                for (int i = 0; i < leftList.size(); i++) {
                    distance += Math.abs(rightList.get(i) - leftList.get(i));
                    System.out.printf("Line %d: Left (%d) - Right (%d) | Distance (%d)\n", i + 1, leftList.get(i), rightList.get(i), Math.abs(rightList.get(i) - leftList.get(i)));
                }
            }
            System.out.printf("Total distance: %d\n", distance);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
