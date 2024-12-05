// Day 5: Print Queue

import java.io.*;
import java.util.*;

public class PartTwo {

    // https://adventofcode.com/2024/day/5/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            List<String> rules = new ArrayList<>();
            List<List<Integer>> updates = new ArrayList<>();

            boolean isRuleSection = true;
            for (String line : lines) {
                if (line.isEmpty()) {
                    isRuleSection = false;
                    continue;
                }
                if (isRuleSection) {
                    rules.add(line);
                } else {
                    List<Integer> update = new ArrayList<>();
                    for (String page : line.split(",")) {
                        update.add(Integer.parseInt(page));
                    }
                    updates.add(update);
                }
            }

            Map<Integer, Set<Integer>> orderMap = new HashMap<>();
            for (String rule : rules) {
                String[] parts = rule.split("\\|");
                int before = Integer.parseInt(parts[0]);
                int after = Integer.parseInt(parts[1]);
                orderMap.computeIfAbsent(before, k -> new HashSet<>()).add(after);
            }

            int sumOfMiddlePages = 0;
            for (List<Integer> update : updates) {
                if (!isValidUpdate(update, orderMap)) {
                    List<Integer> sortedUpdate = sortUpdate(update, orderMap);
                    int middleIndex = sortedUpdate.size() / 2;
                    sumOfMiddlePages += sortedUpdate.get(middleIndex);
                    System.out.printf("Invalid update: %s, sorted update: %s, middle page: %d\n", update, sortedUpdate, sortedUpdate.get(middleIndex));
                } else {
                    System.out.printf("Valid update: %s\n", update);
                }
            }
            System.out.printf("Sum of middle pages: %d\n", sumOfMiddlePages);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isValidUpdate(List<Integer> update, Map<Integer, Set<Integer>> orderMap) {
        for (int i = 0; i < update.size(); i++) {
            for (int j = i + 1; j < update.size(); j++) {
                int before = update.get(i);
                int after = update.get(j);
                if (orderMap.containsKey(after) && orderMap.get(after).contains(before)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static List<Integer> sortUpdate(List<Integer> update, Map<Integer, Set<Integer>> orderMap) {
        List<Integer> sortedUpdate = new ArrayList<>(update);
        sortedUpdate.sort((a, b) -> {
            if (orderMap.containsKey(a) && orderMap.get(a).contains(b)) {
                return -1;
            } else if (orderMap.containsKey(b) && orderMap.get(b).contains(a)) {
                return 1;
            } else {
                return 0;
            }
        });
        return sortedUpdate;
    }
}
