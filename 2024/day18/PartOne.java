// Day 18: RAM Run

import java.io.*;
import java.util.*;

public class PartOne {

    // https://adventofcode.com/2024/day/18/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            final int GRID_SIZE = 71;
            final int BYTE_COUNT = 1024;

            boolean[][] memorySpace = new boolean[GRID_SIZE][GRID_SIZE];

            for (int i = 0; i < Math.min(BYTE_COUNT, lines.size()); i++) {
                String[] parts = lines.get(i).split(",");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                memorySpace[y][x] = true; // Mark as corrupted
            }

            int steps = findShortestPath(memorySpace, GRID_SIZE);
            System.out.printf("Minimum number of steps needed to reach the exit: %d\n", steps);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static int findShortestPath(boolean[][] memorySpace, int GRID_SIZE) {
        final int[] DX = {-1, 1, 0, 0};
        final int[] DY = {0, 0, -1, 1};
        boolean[][] visited = new boolean[GRID_SIZE][GRID_SIZE];
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{0, 0, 0}); // {x, y, steps}
        visited[0][0] = true;

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int x = cell[0];
            int y = cell[1];
            int steps = cell[2];

            if (x == GRID_SIZE - 1 && y == GRID_SIZE - 1) {
                return steps;
            }

            for (int i = 0; i < 4; i++) {
                int nx = x + DX[i];
                int ny = y + DY[i];

                if (nx >= 0 && ny >= 0 && nx < GRID_SIZE && ny < GRID_SIZE && !memorySpace[ny][nx] && !visited[ny][nx]) {
                    visited[ny][nx] = true;
                    queue.add(new int[]{nx, ny, steps + 1});
                }
            }
        }

        return -1; // No path found
    }
}
