// Day 14: Restroom Redoubt

import java.io.*;
import java.util.*;

public class PartTwo {

    // https://adventofcode.com/2024/day/14/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            final int WIDTH = 101;
            final int HEIGHT = 103;

            List<Robot> robots = new ArrayList<>();

            for (String line : lines) {
                String[] parts = line.split(" ");
                String[] pos = parts[0].substring(2).split(",");
                String[] vel = parts[1].substring(2).split(",");
                int px = Integer.parseInt(pos[0]);
                int py = Integer.parseInt(pos[1]);
                int vx = Integer.parseInt(vel[0]);
                int vy = Integer.parseInt(vel[1]);
                robots.add(new Robot(px, py, vx, vy));
            }

            int seconds = 0;
            while (true) {
                int[][] grid = new int[WIDTH][HEIGHT];
                for (Robot robot : robots) {
                    robot.move(WIDTH, HEIGHT);
                    grid[robot.px][robot.py]++;
                }
                seconds++;
                if (isChristmasTree(grid)) break;
            }
            System.out.printf("Fewest number of seconds: %d\n", seconds);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static class Robot {
        int px, py, vx, vy;

        Robot(int px, int py, int vx, int vy) {
            this.px = px;
            this.py = py;
            this.vx = vx;
            this.vy = vy;
        }

        void move(int width, int height) {
            px = (px + vx + width) % width;
            py = (py + vy + height) % height;
        }
    }

    private static boolean isChristmasTree(int[][] grid) {
        int[][] treePattern = {
                {0, 1, 0},
                {1, 1, 1},
                {0, 1, 0},
                {1, 1, 1},
                {0, 1, 0}
        };

        for (int i = 0; i <= grid.length - treePattern.length; i++) {
            for (int j = 0; j <= grid[0].length - treePattern[0].length; j++) {
                if (matchesPattern(grid, treePattern, i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean matchesPattern(int[][] grid, int[][] pattern, int startX, int startY) {
        for (int i = 0; i < pattern.length; i++) {
            for (int j = 0; j < pattern[0].length; j++) {
                if (pattern[i][j] == 1 && grid[startX + i][startY + j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
