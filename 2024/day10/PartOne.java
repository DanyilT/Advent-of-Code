// Day 10: Hoof It

import java.io.*;
import java.util.*;

public class PartOne {

    // https://adventofcode.com/2024/day/10/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            final int MIN_HEIGHT = 0;

            int rows = lines.size();
            int cols = lines.get(0).length();
            int[][] map = new int[rows][cols];

            for (int i = 0; i < rows; i++) {
                String line = lines.get(i);
                for (int j = 0; j < cols; j++) {
                    map[i][j] = line.charAt(j) - '0';
                }
            }

            int totalScore = 0;

            // Traverse the map to find all trailheads
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (map[i][j] == MIN_HEIGHT) {
                        totalScore += calculateTrailheadScore(map, i, j);
                    }
                }
            }
            System.out.printf("Sum of trailhead scores: %d\n", totalScore);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // Calculate the score for a specific trailhead
    private static int calculateTrailheadScore(int[][] map, int row, int col) {
        final int MAX_HEIGHT = 9;

        int rows = map.length;
        int cols = map[0].length;
        boolean[][] visited = new boolean[rows][cols];
        int score = 0;

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{row, col});

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int r = current[0];
            int c = current[1];

            if (visited[r][c]) continue;
            visited[r][c] = true;

            if (map[r][c] == MAX_HEIGHT) {
                score++;
            }

            // Explore neighbors (up, down, left, right)
            for (int[] direction : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}) {
                int newRow = r + direction[0];
                int newCol = c + direction[1];

                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && !visited[newRow][newCol] && map[newRow][newCol] == map[r][c] + 1) {
                    queue.add(new int[]{newRow, newCol});
                }
            }
        }
        return score;
    }
}
