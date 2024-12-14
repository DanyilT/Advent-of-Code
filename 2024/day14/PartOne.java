// Day 14: Restroom Redoubt

import java.io.*;
import java.util.*;

public class PartOne {

    // https://adventofcode.com/2024/day/14/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            final int WIDTH = 101;
            final int HEIGHT = 103;
            final int SECONDS = 100;

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

            int[][] grid = new int[WIDTH][HEIGHT];

            for (Robot robot : robots) {
                for (int i = 0; i < SECONDS; i++) {
                    robot.move(WIDTH, HEIGHT);
                }
                grid[robot.px][robot.py]++;
            }

            int q1 = 0, q2 = 0, q3 = 0, q4 = 0;
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    if (x == WIDTH / 2 || y == HEIGHT / 2) continue;
                    if (x < WIDTH / 2 && y < HEIGHT / 2) q1 += grid[x][y];
                    else if (x >= WIDTH / 2 && y < HEIGHT / 2) q2 += grid[x][y];
                    else if (x < WIDTH / 2 && y >= HEIGHT / 2) q3 += grid[x][y];
                    else if (x >= WIDTH / 2 && y >= HEIGHT / 2) q4 += grid[x][y];
                }
            }
            int safetyFactor = q1 * q2 * q3 * q4;
            System.out.printf("Safety Factor: %d\n", safetyFactor);
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
}
