// Day 6: Guard Gallivant

import java.io.*;
import java.util.*;

public class PartOne {

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

            Set<String> visited = new HashSet<>();
            map[guardRow][guardCol] = 'X';
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
                    map[guardRow][guardCol] = 'X'; // mark visited
                    guardRow = newRow;
                    guardCol = newCol;
                    visited.add(guardRow + "," + guardCol);
                }

                map[guardRow][guardCol] = directionSymbols[dirIndex]; // mark guard position
            }

            // Print the final map
            for (char[] row : map) {
                System.out.println(new String(row));
            }
            System.out.printf("Distinct positions visited: %d\n", visited.size());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
