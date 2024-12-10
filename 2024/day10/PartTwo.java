// Day 10: Hoof It

import java.io.*;
import java.util.*;

public class PartTwo {

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
        int rows = map.length;
        int cols = map[0].length;

        Set<String> distinctTrails = new HashSet<>();
        boolean[][] visited = new boolean[rows][cols];

        // Start DFS to explore all possible trails
        dfs(map, row, col, "", distinctTrails, visited);

        return distinctTrails.size();
    }

    // DFS to explore trails and record paths leading to height 9
    private static void dfs(int[][] map, int r, int c, String path, Set<String> trails, boolean[][] visited) {
        final int MAX_HEIGHT = 9;

        int rows = map.length;
        int cols = map[0].length;

        if (r < 0 || r >= rows || c < 0 || c >= cols || visited[r][c]) return;

        visited[r][c] = true;
        path += "(" + r + "," + c + ")";

        // Record the trail if it ends at height 9
        if (map[r][c] == MAX_HEIGHT) {
            trails.add(path);
        }

        // Explore neighbors (up, down, left, right)
        for (int[] direction : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}) {
            int newRow = r + direction[0];
            int newCol = c + direction[1];

            if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && map[newRow][newCol] == map[r][c] + 1) {
                dfs(map, newRow, newCol, path, trails, visited);
            }
        }

        visited[r][c] = false;
    }
}
