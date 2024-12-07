// Day 7: Bridge Repair

import java.io.*;
import java.util.*;

public class PartOne {

    // https://adventofcode.com/2024/day/7/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            long totalCalibrationResult = 0;

            for (String line : lines) {
                String[] parts = line.split(": ");
                long testValue = Long.parseLong(parts[0]);
                String[] numbers = parts[1].split(" ");
                long[] nums = Arrays.stream(numbers).mapToLong(Long::parseLong).toArray();

                if (canBeTrue(testValue, nums)) {
                    totalCalibrationResult += testValue;
                }
            }
            System.out.printf("Total Calibration Result: %d\n", totalCalibrationResult);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean canBeTrue(long testValue, long[] nums) {
        return evaluate(nums, 0, nums[0], testValue);
    }

    private static boolean evaluate(long[] nums, int index, long currentValue, long targetValue) {
        if (index == nums.length - 1) {
            return currentValue == targetValue;
        }

        long nextValue = nums[index + 1];
        return evaluate(nums, index + 1, currentValue + nextValue, targetValue) ||
                evaluate(nums, index + 1, currentValue * nextValue, targetValue);
    }
}
