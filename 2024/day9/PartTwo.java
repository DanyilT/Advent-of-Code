// Day 9: Disk Fragmenter

import java.io.*;
import java.util.*;

public class PartTwo {

    // https://adventofcode.com/2024/day/9/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();
            List<Integer> disk = new ArrayList<>();

            boolean isSpace = false;
            int id = 0;

            for (char c : lines.get(0).toCharArray()) {
                int count = Character.getNumericValue(c);
                for (int i = 0; i < count; i++) {
                    disk.add(isSpace ? -1 : id);
                }
                if (!isSpace) id++;
                isSpace = !isSpace;
            }

            int highestId = id - 1;

            // Process files in decreasing file ID order
            for (int fileId = highestId; fileId >= 0; fileId--) {
                int fileStart = disk.indexOf(fileId);
                int fileEnd = disk.lastIndexOf(fileId);
                int fileLength = fileEnd - fileStart + 1;

                // Find the leftmost span of free space large enough for the file
                int bestStart = -1;

                for (int i = 0; i <= disk.size() - fileLength; i++) {
                    boolean canFit = true;
                    for (int j = i; j < i + fileLength; j++) {
                        if (disk.get(j) != -1) {
                            canFit = false;
                            break;
                        }
                    }
                    if (canFit && (bestStart == -1 || i < bestStart)) {
                        bestStart = i;
                    }
                }

                // Move the file if a valid position was found
                if (bestStart != -1 && bestStart < fileStart) {
                    for (int j = fileStart; j <= fileEnd; j++) {
                        disk.set(j, -1);
                    }
                    for (int j = bestStart; j < bestStart + fileLength; j++) {
                        disk.set(j, fileId);
                    }
                }
            }

            // Compute checksum
            long checksum = 0;
            for (int i = 0; i < disk.size(); i++) {
                if (disk.get(i) != -1) {
                    checksum += (long) i * disk.get(i);
                }
            }
            System.out.printf("Filesystem checksum: %d\n", checksum);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
