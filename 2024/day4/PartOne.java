// Day 4: Ceres Search

import java.io.*;
import java.util.*;

public class PartOne {

    // https://adventofcode.com/2024/day/4/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            final String WORD = "XMAS";

            int rows = lines.size();
            int cols = lines.get(0).length();
            char[][] grid = new char[rows][cols];

            for (int i = 0; i < rows; i++) {
                grid[i] = lines.get(i).toCharArray();
            }

            int count = 0;
            int[][] directions = {
                    {0, 1}, {1, 0}, {1, 1}, {1, -1}, // right, down, down-right, down-left
                    {0, -1}, {-1, 0}, {-1, -1}, {-1, 1} // left, up, up-left, up-right
            };

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    for (int[] direction : directions) {
                        if (searchWord(grid, row, col, direction, WORD)) {
                            count++;
                        }
                    }
                }
            }
            System.out.printf("Occurrences of %s: %d\n", WORD, count);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean searchWord(char[][] grid, int row, int col, int[] direction, String WORD) {
        int rows = grid.length;
        int cols = grid[0].length;
        for (int i = 0; i < WORD.length(); i++) {
            int newRow = row + i * direction[0];
            int newCol = col + i * direction[1];
            if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols || grid[newRow][newCol] != WORD.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}
