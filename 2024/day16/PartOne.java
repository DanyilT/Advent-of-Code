// Day 16: Reindeer Maze

import java.io.*;
import java.util.*;

public class PartOne {

    // https://adventofcode.com/2024/day/16/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            int rows = lines.size();
            int cols = lines.get(0).length();
            char[][] maze = new char[rows][cols];
            int startX = 0, startY = 0, endX = 0, endY = 0;

            for (int i = 0; i < rows; i++) {
                maze[i] = lines.get(i).toCharArray();
                for (int j = 0; j < cols; j++) {
                    if (maze[i][j] == 'S') {
                        startX = i;
                        startY = j;
                    } else if (maze[i][j] == 'E') {
                        endX = i;
                        endY = j;
                    }
                }
            }

            int result = findLowestScore(maze, startX, startY, endX, endY, rows, cols);
            System.out.printf("Lowest Score: %d\n", result);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static int findLowestScore(char[][] maze, int startX, int startY, int endX, int endY, int rows, int cols) {
        final int[] DX = {0, 1, 0, -1}; // East, South, West, North
        final int[] DY = {1, 0, -1, 0};
        final int MOVE_COST = 1;
        final int ROTATE_COST = 1000;

        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(s -> s.score + Math.abs(s.x - endX) + Math.abs(s.y - endY)));
        Set<String> visited = new HashSet<>();
        pq.add(new State(startX, startY, 0, 0)); // Start facing East

        while (!pq.isEmpty()) {
            State current = pq.poll();
            if (current.x == endX && current.y == endY) {
                return current.score;
            }

            String stateKey = current.x + "," + current.y + "," + current.direction;
            if (visited.contains(stateKey)) {
                continue;
            }
            visited.add(stateKey);

            // Move forward
            int nx = current.x + DX[current.direction];
            int ny = current.y + DY[current.direction];
            if (nx >= 0 && ny >= 0 && nx < rows && ny < cols && maze[nx][ny] != '#') {
                pq.add(new State(nx, ny, current.direction, current.score + MOVE_COST));
            }

            // Rotate clockwise
            pq.add(new State(current.x, current.y, (current.direction + 1) % 4, current.score + ROTATE_COST));

            // Rotate counterclockwise
            pq.add(new State(current.x, current.y, (current.direction + 3) % 4, current.score + ROTATE_COST));
        }

        return -1; // No path found
    }

    static class State {
        int x, y, direction, score;

        State(int x, int y, int direction, int score) {
            this.x = x;
            this.y = y;
            this.direction = direction;
            this.score = score;
        }
    }
}
