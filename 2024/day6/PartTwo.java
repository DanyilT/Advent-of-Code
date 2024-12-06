// Day 6: Guard Gallivant

import java.io.*;
import java.util.*;

public class PartTwo {

    // https://adventofcode.com/2024/day/6/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            final char GUARD = '^';
            final char OBSTACLE = '#';
            final char EMPTY = '.';

            int rows = lines.size();
            int cols = lines.get(0).length();
            char[][] map = new char[rows][cols];

            int guardRow = 0, guardCol = 0, dirIndex = 0;

            for (int i = 0; i < rows; i++) {
                map[i] = lines.get(i).toCharArray();
                for (int j = 0; j < cols; j++) {
                    if (map[i][j] == GUARD) {
                        guardRow = i;
                        guardCol = j;
                    }
                }
            }

            int initialGuardRow = guardRow;
            int initialGuardCol = guardCol;

            Set<String> visited = new HashSet<>();
            visited.add(guardRow + "," + guardCol);

            int[][] directions = {
                    {-1, 0}, // up
                    {0, 1},  // right
                    {1, 0},  // down
                    {0, -1}  // left
            };
            char[] directionSymbols = {'^', '>', 'v', '<'};

            while (true) {
                int newRow = guardRow + directions[dirIndex][0];
                int newCol = guardCol + directions[dirIndex][1];

                System.out.printf("Guard position: (%d, %d), direction: %d\n", guardRow, guardCol, dirIndex);

                if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols) {
                    break; // guard has left the map
                }

                if (map[newRow][newCol] == OBSTACLE) {
                    dirIndex = (dirIndex + 1) % 4; // turn right
                } else {
                    guardRow = newRow;
                    guardCol = newCol;
                    visited.add(guardRow + "," + guardCol);
                }
            }

            Set<String> loopPositions = new HashSet<>();
            for (String pos : visited) {
                String[] parts = pos.split(",");
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);

                if (row == initialGuardRow && col == initialGuardCol) continue; // skip starting position

                char original = map[row][col];
                map[row][col] = 'O';

                if (isGuardStuck(map, initialGuardRow, initialGuardCol, directions)) {
                    loopPositions.add(pos);
                }

                map[row][col] = original; // restore original map
            }
            System.out.printf("Number of positions to place obstruction: %d\n", loopPositions.size());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isGuardStuck(char[][] map, int guardRow, int guardCol, int[][] directions) {
        int rows = map.length;
        int cols = map[0].length;
        int dirIndex = 0;
        Set<String> visited = new HashSet<>();

        while (true) {
            int newRow = guardRow + directions[dirIndex][0];
            int newCol = guardCol + directions[dirIndex][1];

            if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols) {
                return false; // guard has left the map
            }

            if (visited.contains(newRow + "," + newCol + "," + dirIndex)) {
                return true; // guard is stuck in a loop
            }

            if (map[newRow][newCol] == '#' || map[newRow][newCol] == 'O') {
                dirIndex = (dirIndex + 1) % 4; // turn right
            } else {
                visited.add(newRow + "," + newCol + "," + dirIndex);
                guardRow = newRow;
                guardCol = newCol;
            }
        }
    }
}
