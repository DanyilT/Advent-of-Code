// Day 15: Warehouse Woes

import java.io.*;
import java.util.*;

public class PartTwo {

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
            Obstacle robot = new Obstacle(0, 0, ObstacleType.Robot);

            for (int y = 0; y < lines.size(); y++) {
                String line = lines.get(y);
                if (line.startsWith(String.valueOf(WALL)))
                    warehouse.add(line);
                else if (!line.isEmpty())
                    moves.add(line);
                if (line.contains(String.valueOf(ROBOT))) {
                    robot = new Obstacle(line.indexOf(ROBOT) * 2, y, ObstacleType.Robot);
                }
            }

            Obstacle[][] obstacles = new Obstacle[warehouse.size()][warehouse.getFirst().length() * 2];
            for (int y = 0; y < warehouse.size(); y++) {
                String line = warehouse.get(y);
                for (int x = 0; x < line.length(); x++) {
                    char tile = line.charAt(x);
                    if (tile == WALL) {
                        obstacles[y][x * 2] = new Obstacle(x * 2, y, ObstacleType.Wall);
                    } else if (tile == BOX) {
                        obstacles[y][x * 2] = new Obstacle(x * 2, y, ObstacleType.Box);
                    }
                }
            }

            for (String move : moves) {
                for (char m : move.toCharArray()) {
                    attemptMove(directions.indexOf(m), robot, obstacles, deltaMap);
                }
            }

            int sumOfBoxes = 0;
            for (int y = 0; y < obstacles.length; y++) {
                for (int x = 0; x < obstacles[0].length; x++) {
                    if (obstacles[y][x] != null && obstacles[y][x].type == ObstacleType.Box) {
                        sumOfBoxes += 100 * y + x;
                    }
                }
            }
            System.out.printf("Sum of all boxes' GPS coordinates: %d\n", sumOfBoxes);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void attemptMove(int direction, Obstacle robot, Obstacle[][] obstacles, int[][] deltaMap) {
        int dY = robot.y + deltaMap[direction][0];
        int dX = robot.x + deltaMap[direction][1];

        Obstacle obstruction = null;
        if (direction != 3 && obstacles[dY][dX] != null) {
            obstruction = obstacles[dY][dX];
        } else if (direction != 1 && obstacles[dY][dX - 1] != null) {
            obstruction = obstacles[dY][dX - 1];
        }

        if (obstruction != null && obstruction.type == ObstacleType.Wall) {
            return;  // Hit a wall, can't move
        }

        if (obstruction != null) {
            if (obstruction.tryMove(obstacles, deltaMap, direction, false)) {
                obstruction.tryMove(obstacles, deltaMap, direction, true);
            } else {
                return;
            }
        }

        robot.y = dY;
        robot.x = dX;
    }

    static class Obstacle {
        int x, y;
        ObstacleType type;

        Obstacle(int x, int y, ObstacleType type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }

        boolean tryMove(Obstacle[][] obstacles, int[][] deltaMap, int direction, boolean doMove) {
            int dX = x + deltaMap[direction][1];
            int dY = y + deltaMap[direction][0];

            List<Obstacle> boxes = new ArrayList<>();
            if (direction % 2 == 0 && obstacles[dY][dX] != null) {
                if (obstacles[dY][dX].type == ObstacleType.Wall) {
                    return false;  // Hit a wall
                } else {
                    boxes.add(obstacles[dY][dX]);
                }
            }

            if (direction != 1 && obstacles[dY][dX - 1] != null) {
                if (obstacles[dY][dX - 1].type == ObstacleType.Wall) {
                    return false;  // Hit a wall
                } else {
                    boxes.add(obstacles[dY][dX - 1]);
                }
            }

            if (direction != 3 && obstacles[dY][dX + 1] != null) {
                if (obstacles[dY][dX + 1].type == ObstacleType.Wall) {
                    return false;  // Hit a wall
                } else {
                    boxes.add(obstacles[dY][dX + 1]);
                }
            }

            if (boxes.isEmpty()) {
                if (doMove) {
                    move(obstacles, deltaMap, direction);
                }
                return true;
            } else {
                boolean possible = true;
                for (Obstacle box : boxes) {
                    possible = possible && box.tryMove(obstacles, deltaMap, direction, false);
                }

                if (possible && doMove) {
                    for (Obstacle box : boxes) {
                        box.tryMove(obstacles, deltaMap, direction, true);
                    }
                    move(obstacles, deltaMap, direction);
                }
                return possible;
            }
        }

        private void move(Obstacle[][] obstacles, int[][] deltaMap, int direction) {
            obstacles[y][x] = null;
            x += deltaMap[direction][1];
            y += deltaMap[direction][0];
            obstacles[y][x] = this;
        }
    }

    enum ObstacleType {
        Box, Robot, Wall
    }
}
