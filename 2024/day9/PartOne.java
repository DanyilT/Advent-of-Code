// Day 9: Disk Fragmenter

import java.io.*;
import java.util.*;

public class PartOne {

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

            // Compact the disk by moving file blocks to the leftmost free space
            for (int i = 0; i < disk.size(); i++) {
                if (disk.get(i) == -1) {
                    int block = -1;
                    while (block == -1) {
                        block = disk.remove(disk.size() - 1);
                    }
                    if (disk.size() <= i) {
                        disk.add(block);
                        break;
                    }
                    disk.set(i, block);
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
