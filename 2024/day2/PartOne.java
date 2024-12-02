// Day 2: Red-Nosed Reports

import java.io.*;
import java.util.*;

public class PartOne {

    // https://adventofcode.com/2024/day/2/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();
            Map<String, List<Integer>> reports = new LinkedHashMap<>();

            for (String line : lines) {
                List<Integer> levels = Arrays.stream(line.split("\\s+")).map(Integer::parseInt).toList();
                reports.put(line, levels);
            }

            int safeReports = 0;
            for (Map.Entry<String, List<Integer>> report : reports.entrySet()) {
                List<Integer> levels = report.getValue();

                safeReports += isSafeReport(levels) ? 1 : 0;
                System.out.printf("Report %s | Safe report (%b)\n", levels, isSafeReport(levels));
            }
            System.out.printf("Safe reports: %d\n", safeReports);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isSafeReport(List<Integer> levels) {
        List<Integer> increasingOrder = new ArrayList<>(levels);
        Collections.sort(increasingOrder);
        List<Integer> decreasingOrder = new ArrayList<>(levels);
        decreasingOrder.sort(Collections.reverseOrder());
        boolean isSorted = levels.equals(increasingOrder) || levels.equals(decreasingOrder);

        if (isSorted) {
            for (int i = 0; i < levels.size() - 1; i++) {
                if (Math.abs(levels.get(i) - levels.get(i + 1)) == 0 || Math.abs(levels.get(i) - levels.get(i + 1)) > 3) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
