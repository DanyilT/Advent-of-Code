// Day 15: Warehouse Woes

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class PartOne {

    // https://adventofcode.com/2024/day/15/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            final char WALL = '#';
            final char EMPTY = '.';
            final char BOX = 'O';
            final char ROBOT = '@';

            String directions = "^>v<";
            int[][] deltaMap = { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } };

            List<String> warehouse = new ArrayList<>();
            List<String> moves = new ArrayList<>();
            Robot robot = new Robot(0, 0);

            for (String line : lines) {
                if (line.startsWith(String.valueOf(WALL))) {
                    warehouse.add(line);
                } else if (!line.isEmpty()) {
                    moves.add(line);
                }
                if (line.contains(String.valueOf(ROBOT))) {
                    robot = new Robot(line.indexOf(ROBOT), warehouse.size() - 1);
                }
            }

            char[][] map = new char[warehouse.size()][warehouse.getFirst().length()];
            for (int y = 0; y < warehouse.size(); y++) {
                for (int x = 0; x < warehouse.get(y).length(); x++) {
                    map[y][x] = warehouse.get(y).charAt(x);
                }
            }

            for (String move : moves) {
                for (char m : move.toCharArray()) {
                    attemptMove(directions.indexOf(m), map, robot, deltaMap, WALL, EMPTY, BOX, ROBOT);
                }
            }

            int sumOfBoxes = 0;
            for (int y = 0; y < map.length; y++) {
                for (int x = 0; x < map[y].length; x++) {
                    if (map[y][x] == BOX) {
                        sumOfBoxes += 100 * y + x;
                    }
                }
            }
            System.out.printf("Sum of all boxes' GPS coordinates: %d\n", sumOfBoxes);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void attemptMove(int direction, char[][] map, Robot robot, int[][] deltaMap, char WALL, char EMPTY, char BOX, char ROBOT) {
        int dY = robot.y + deltaMap[direction][0];
        int dX = robot.x + deltaMap[direction][1];

        if (map[dY][dX] == EMPTY) {
            map[robot.y][robot.x] = EMPTY;
            map[dY][dX] = ROBOT;
            robot.y = dY;
            robot.x = dX;
        }

        if (map[dY][dX] == BOX) {
            int nextY = dY, nextX = dX, steps = 0;
            boolean wall = false, empty = false;
            while (!wall && !empty) {
                nextY += deltaMap[direction][0];
                nextX += deltaMap[direction][1];
                steps++;

                if (map[nextY][nextX] == EMPTY) {
                    empty = true;
                } else if (map[nextY][nextX] == WALL) {
                    wall = true;
                }
            }

            if (empty) {
                int previousY = 0, previousX = 0;
                for (int i = 0; i <= steps; i++) {
                    previousY = nextY - deltaMap[direction][0];
                    previousX = nextX - deltaMap[direction][1];

                    map[nextY][nextX] = map[previousY][previousX];

                    if (map[previousY][previousX] == ROBOT) {
                        map[robot.y][robot.x] = EMPTY;
                        robot.y = nextY;
                        robot.x = nextX;
                    }

                    nextY = previousY;
                    nextX = previousX;
                }
            }
        }
    }

    static class Robot {
        int x, y;

        Robot(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
