// Day 25: Code Chronicle

import java.io.*;
import java.util.*;

public class PartOne {

    // https://adventofcode.com/2024/day/25/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            final int SCHEMATIC_ROWS = 7;
            final int SCHEMATIC_COLS = 5;

            ArrayList<char[][]> schematics = new ArrayList<>();

            int index = 0;
            while (index < lines.size()) {
                int[] newIndex = new int[1];
                char[][] block = readSchematicBlock(lines, index, newIndex, SCHEMATIC_ROWS, SCHEMATIC_COLS);
                if (block == null) {
                    break;
                }
                schematics.add(block);
                index = newIndex[0];
            }

            // Separate locks vs keys, and parse their heights
            ArrayList<int[]> lockHeights = new ArrayList<>();
            ArrayList<int[]> keyHeights = new ArrayList<>();

            for (char[][] matrix : schematics) {
                if (isLock(matrix, SCHEMATIC_ROWS, SCHEMATIC_COLS)) {
                    lockHeights.add(parseLockHeights(matrix, SCHEMATIC_ROWS, SCHEMATIC_COLS));
                } else if (isKey(matrix, SCHEMATIC_ROWS, SCHEMATIC_COLS)) {
                    keyHeights.add(parseKeyHeights(matrix, SCHEMATIC_ROWS, SCHEMATIC_COLS));
                }
            }

            // Count how many unique (lock, key) pairs fit
            int fitCount = 0;
            for (int[] lHeights : lockHeights) {
                for (int[] kHeights : keyHeights) {
                    if (doTheyFit(lHeights, kHeights)) {
                        fitCount++;
                    }
                }
            }
            System.out.printf("Number of unique lock/key pairs that fit together: %d\n", fitCount);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static char[][] readSchematicBlock(List<String> lines, int startIndex, int[] newIndex, int SCHEMATIC_ROWS, int SCHEMATIC_COLS) {
        char[][] block = new char[SCHEMATIC_ROWS][SCHEMATIC_COLS];
        int linesRead = 0;
        int index = startIndex;

        while (index < lines.size()) {
            String check = lines.get(index).trim();
            if (!check.isEmpty()) {
                break;
            }
            index++;
        }

        for (int i = 0; i < SCHEMATIC_ROWS; i++) {
            if (index >= lines.size()) {
                return null; // unexpected EOF
            }
            String line = lines.get(index).trim();
            if (line.length() != SCHEMATIC_COLS) {
                return null; // malformed or short line => treat as invalid
            }
            for (int j = 0; j < SCHEMATIC_COLS; j++) {
                block[i][j] = line.charAt(j);
            }
            linesRead++;
            index++;
        }
        if (linesRead < SCHEMATIC_ROWS) {
            return null; // didn't get a full 7 lines
        }
        newIndex[0] = index;
        return block;
    }

    private static boolean isLock(char[][] matrix, int SCHEMATIC_ROWS, int SCHEMATIC_COLS) {
        for (int col = 0; col < SCHEMATIC_COLS; col++) {
            if (matrix[0][col] != '#') {
                return false;
            }
        }
        for (int col = 0; col < SCHEMATIC_COLS; col++) {
            if (matrix[SCHEMATIC_ROWS - 1][col] != '.') {
                return false;
            }
        }
        return true;
    }

    private static boolean isKey(char[][] matrix, int SCHEMATIC_ROWS, int SCHEMATIC_COLS) {
        for (int col = 0; col < SCHEMATIC_COLS; col++) {
            if (matrix[0][col] != '.') {
                return false;
            }
        }
        for (int col = 0; col < SCHEMATIC_COLS; col++) {
            if (matrix[SCHEMATIC_ROWS - 1][col] != '#') {
                return false;
            }
        }
        return true;
    }

    private static int[] parseLockHeights(char[][] matrix, int SCHEMATIC_ROWS, int SCHEMATIC_COLS) {
        int[] heights = new int[SCHEMATIC_COLS];
        for (int col = 0; col < SCHEMATIC_COLS; col++) {
            int count = 0;
            for (int row = 0; row < SCHEMATIC_ROWS; row++) {
                if (matrix[row][col] == '#') {
                    count++;
                } else {
                    break;
                }
            }
            heights[col] = count;
        }
        return heights;
    }

    private static int[] parseKeyHeights(char[][] matrix, int SCHEMATIC_ROWS, int SCHEMATIC_COLS) {
        int[] heights = new int[SCHEMATIC_COLS];
        for (int col = 0; col < SCHEMATIC_COLS; col++) {
            int count = 0;
            for (int row = SCHEMATIC_ROWS - 1; row >= 0; row--) {
                if (matrix[row][col] == '#') {
                    count++;
                } else {
                    break;
                }
            }
            heights[col] = count;
        }
        return heights;
    }

    private static boolean doTheyFit(int[] lock, int[] key) {
        for (int i = 0; i < lock.length; i++) {
            if (lock[i] + key[i] > 7) {
                return false;
            }
        }
        return true;
    }
}
