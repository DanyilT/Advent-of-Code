// Day 12: Garden Groups

import java.io.*;
import java.util.*;

public class PartOne {

    // https://adventofcode.com/2024/day/12/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            int rows = lines.size();
            int cols = lines.get(0).length();
            char[][] garden = new char[rows][cols];
            boolean[][] visited = new boolean[rows][cols];

            for (int i = 0; i < rows; i++) {
                garden[i] = lines.get(i).toCharArray();
            }

            int totalPrice = 0;

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (!visited[i][j]) {
                        int[] result = floodFill(garden, visited, i, j, rows, cols);
                        int area = result[0];
                        int perimeter = result[1];
                        System.out.printf("Area: %d | Perimeter: %d\n", area, perimeter);
                        totalPrice += area * perimeter;
                    }
                }
            }
            System.out.printf("Total Price (Area * Perimeter): %d\n", totalPrice);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static int[] floodFill(char[][] garden, boolean[][] visited, int x, int y, int rows, int cols) {
        final int[] DX = {-1, 1, 0, 0};
        final int[] DY = {0, 0, -1, 1};

        char plantType = garden[x][y];
        int area = 0;
        int perimeter = 0;
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{x, y});
        visited[x][y] = true;

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int cx = cell[0];
            int cy = cell[1];
            area++;

            for (int i = 0; i < 4; i++) {
                int nx = cx + DX[i];
                int ny = cy + DY[i];

                if (nx < 0 || ny < 0 || nx >= rows || ny >= cols || garden[nx][ny] != plantType) {
                    perimeter++;
                } else if (!visited[nx][ny]) {
                    visited[nx][ny] = true;
                    queue.add(new int[]{nx, ny});
                }
            }
        }

        return new int[]{area, perimeter};
    }
}
