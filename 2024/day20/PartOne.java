// Day 20: Race Condition

import java.io.*;
import java.util.*;

public class PartOne {

    // https://adventofcode.com/2024/day/20/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            int rows = lines.size();
            int cols = lines.get(0).length();
            char[][] grid = new char[rows][cols];

            int startR = -1, startC = -1;
            int endR = -1, endC = -1;

            for (int r = 0; r < rows; r++) {
                String line = lines.get(r);
                for (int c = 0; c < cols; c++) {
                    grid[r][c] = line.charAt(c);
                    if (grid[r][c] == 'S') {
                        startR = r;
                        startC = c;
                    } else if (grid[r][c] == 'E') {
                        endR = r;
                        endC = c;
                    }
                }
            }

            // BFS from S for normal distances
            int[][] distS = bfsDistances(grid, startR, startC);
            // BFS from E for normal distances (reverse)
            int[][] distE = bfsDistances(grid, endR, endC);

            if (distS[endR][endC] < 0) {
                System.out.printf("No path from S to E\n");
                return;
            }
            int normalDist = distS[endR][endC];

            // Cheats are uniquely identified by their (start pos, end pos)
            int count = 0;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (!isTrack(grid[r][c])) continue;
                    if (distS[r][c] < 0) continue;
                    // BFS ignoring walls up to 2 steps from (r,c)
                    List<int[]> cheatEnds = cheatReach(grid, r, c);
                    for (int[] endPos : cheatEnds) {
                        int rr = endPos[0];
                        int cc = endPos[1];
                        if (!isTrack(grid[rr][cc])) continue;
                        if (distE[rr][cc] < 0) continue;
                        int costWithCheat = distS[r][c] + 2 + distE[rr][cc];
                        int saved = normalDist - costWithCheat;
                        if (saved >= 100) {
                            count++;
                        }
                    }
                }
            }
            System.out.printf("Number of cheats that save at least 100 picoseconds: %d\n", count);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static int[][] bfsDistances(char[][] grid, int sr, int sc) {
        final int[] DIRS = {0,1,0,-1,0};
        int rows = grid.length, cols = grid[0].length;
        int[][] dist = new int[rows][cols];
        for (int[] row : dist) {
            Arrays.fill(row, -1);
        }
        Queue<int[]> q = new ArrayDeque<>();
        dist[sr][sc] = 0;
        q.offer(new int[]{sr, sc});
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int r = cur[0], c = cur[1];
            for (int i = 0; i < 4; i++) {
                int rr = r + DIRS[i], cc = c + DIRS[i+1];
                if (rr < 0 || rr >= rows || cc < 0 || cc >= cols) continue;
                if (dist[rr][cc] >= 0) continue;
                if (!isTrack(grid[rr][cc])) continue;
                dist[rr][cc] = dist[r][c] + 1;
                q.offer(new int[]{rr, cc});
            }
        }
        return dist;
    }

    private static List<int[]> cheatReach(char[][] grid, int sr, int sc) {
        final int[] DIRS = {0,1,0,-1,0};
        int rows = grid.length, cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        List<int[]> results = new ArrayList<>();
        Queue<int[]> q = new ArrayDeque<>();
        q.offer(new int[]{sr, sc, 0}); // (row, col, steps)
        visited[sr][sc] = true;
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int r = cur[0], c = cur[1], steps = cur[2];
            if (steps > 0 && steps <= 2) {
                results.add(new int[]{r, c});
            }
            if (steps == 2) continue;
            for (int i = 0; i < 4; i++) {
                int rr = r + DIRS[i], cc = c + DIRS[i+1];
                if (rr < 0 || rr >= rows || cc < 0 || cc >= cols) continue;
                if (!visited[rr][cc]) {
                    visited[rr][cc] = true;
                    q.offer(new int[]{rr, cc, steps+1});
                }
            }
        }
        return results;
    }

    private static boolean isTrack(char ch) {
        return ch == '.' || ch == 'S' || ch == 'E';
    }
}
