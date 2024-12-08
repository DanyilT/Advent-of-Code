// Day 8: Resonant Collinearity

import java.io.*;
import java.util.*;

public class PartOne {

    // https://adventofcode.com/2024/day/8/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            int rows = lines.size();
            int cols = lines.get(0).length();
            Map<Character, List<int[]>> antennaMap = new HashMap<>();

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    char c = lines.get(i).charAt(j);
                    if (c != '.') {
                        antennaMap.computeIfAbsent(c, k -> new ArrayList<>()).add(new int[]{i, j});
                    }
                }
            }

            Set<String> antinodes = new HashSet<>();

            for (Map.Entry<Character, List<int[]>> entry : antennaMap.entrySet()) {
                List<int[]> positions = entry.getValue();
                for (int i = 0; i < positions.size(); i++) {
                    for (int j = i + 1; j < positions.size(); j++) {
                        int[] pos1 = positions.get(i);
                        int[] pos2 = positions.get(j);
                        int r1 = pos1[0], c1 = pos1[1];
                        int r2 = pos2[0], c2 = pos2[1];

                        // Calculate the potential antinode positions
                        int rAntinode1 = 2 * r1 - r2;
                        int cAntinode1 = 2 * c1 - c2;
                        int rAntinode2 = 2 * r2 - r1;
                        int cAntinode2 = 2 * c2 - c1;

                        // Check if the antinode positions are within the bounds of the map
                        if (isWithinBounds(rAntinode1, cAntinode1, rows, cols)) {
                            antinodes.add(rAntinode1 + "," + cAntinode1);
                        }
                        if (isWithinBounds(rAntinode2, cAntinode2, rows, cols)) {
                            antinodes.add(rAntinode2 + "," + cAntinode2);
                        }
                    }
                }
            }
            System.out.printf("Number of unique antinode locations: %d\n", antinodes.size());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isWithinBounds(int r, int c, int rows, int cols) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }
}
