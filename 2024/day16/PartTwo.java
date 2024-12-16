// Day 16: Reindeer Maze

import java.io.*;
import java.util.*;

public class PartTwo {

    // https://adventofcode.com/2024/day/16/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) throws IOException {
        List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

        int[] start = new int[2];
        for (int r = 0; r < lines.size(); r++) {
            for (int c = 0; c < lines.get(r).length(); c++) {
                if (lines.get(r).charAt(c) == 'S') {
                    start[0] = r;
                    start[1] = c;
                    break;
                }
            }
        }

        PriorityQueue<State> queue = new PriorityQueue<>(Comparator.comparingInt(s -> s.cost));
        Set<String> seen = new HashSet<>();

        List<int[]> startPath = new ArrayList<>();
        startPath.add(new int[] { start[0], start[1] });
        queue.add(new State(0, start[0], start[1], 0, 1, startPath));
        seen.add(start[0] + "," + start[1] + ",0,1");

        int bestCost = Integer.MAX_VALUE;
        Set<String> points = new HashSet<>();

        int[][] directions = {{1, 0}, {0, -1}, {-1, 0}, {0, 1}};

        while (!queue.isEmpty()) {
            State current = queue.poll();
            int r = current.r, c = current.c, dr = current.dr, dc = current.dc, cost = current.cost;
            List<int[]> path = current.path;

            seen.add(r + "," + c + "," + dr + "," + dc);

            if (lines.get(r).charAt(c) == 'E') {
                if (cost <= bestCost) {
                    bestCost = cost;
                    for (int[] point : path) {
                        points.add(Arrays.toString(point));
                    }
                } else {
                    break;
                }
            }

            // Move in the current direction
            if (r + dr >= 0 && r + dr < lines.size() && c + dc >= 0 && c + dc < lines.get(r).length() &&
                    lines.get(r + dr).charAt(c + dc) != '#' && !seen.contains((r + dr) + "," + (c + dc) + "," + dr + "," + dc)) {
                List<int[]> newPath = new ArrayList<>(path);
                newPath.add(new int[]{r + dr, c + dc});
                queue.add(new State(cost + 1, r + dr, c + dc, dr, dc, newPath));
            }

            // Try turning left or right
            for (int[] newDirection : new int[][]{{-dc, dr}, {dc, -dr}}) {
                int ndr = newDirection[0], ndc = newDirection[1];
                if (r + ndr >= 0 && r + ndr < lines.size() && c + ndc >= 0 && c + ndc < lines.get(r).length() &&
                        lines.get(r + ndr).charAt(c + ndc) != '#' && !seen.contains(r + "," + c + "," + ndr + "," + ndc)) {
                    List<int[]> newPath = new ArrayList<>(path);
                    newPath.add(new int[]{r, c});
                    queue.add(new State(cost + 1000, r, c, ndr, ndc, newPath));
                }
            }
        }
        System.out.printf("Tiles are part of the shortest path: %d\n", points.size());
    }

    static class State {
        int cost, r, c, dr, dc;
        List<int[]> path;

        public State(int cost, int r, int c, int dr, int dc, List<int[]> path) {
            this.cost = cost;
            this.r = r;
            this.c = c;
            this.dr = dr;
            this.dc = dc;
            this.path = path;
        }
    }
}
