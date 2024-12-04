// Day 4: Ceres Search

import java.io.*;
import java.util.*;

public class PartTwo {

    // https://adventofcode.com/2024/day/4/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            int rows = lines.size();
            int cols = lines.get(0).length();
            char[][] grid = new char[rows][cols];

            for (int i = 0; i < rows; i++) {
                grid[i] = lines.get(i).toCharArray();
            }

            int count = 0;

            for (int row = 0; row < rows - 2; row++) {
                for (int col = 0; col < cols - 2; col++) {
                    if (isXMAS(grid, row, col)) {
                        count++;
                    }
                }
            }
            System.out.printf("Occurrences of X-MAS: %d\n", count);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isXMAS(char[][] grid, int row, int col) {
        String[] patterns = {"MAS", "SAM"};
        for (String pattern1 : patterns) {
            for (String pattern2 : patterns) {
                if (grid[row][col] == pattern1.charAt(0) &&
                        grid[row + 1][col + 1] == pattern1.charAt(1) &&
                        grid[row + 2][col + 2] == pattern1.charAt(2) &&
                        grid[row + 2][col] == pattern2.charAt(0) &&
                        grid[row + 1][col + 1] == pattern2.charAt(1) &&
                        grid[row][col + 2] == pattern2.charAt(2)) {
                    return true;
                }
            }
        }
        return false;
    }
}
