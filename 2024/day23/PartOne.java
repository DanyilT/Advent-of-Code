// Day 23: LAN Party

import java.io.*;
import java.util.*;

public class PartOne {

    // https://adventofcode.com/2024/day/23/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            Map<String, Set<String>> adjacency = new HashMap<>();
            for (String line : lines) {
                String[] parts = line.split("-");

                String a = parts[0].trim();
                String b = parts[1].trim();

                adjacency.putIfAbsent(a, new HashSet<>());
                adjacency.putIfAbsent(b, new HashSet<>());
                adjacency.get(a).add(b);
                adjacency.get(b).add(a);
            }

            Set<String> triangles = new HashSet<>();
            for (String node : adjacency.keySet()) {
                List<String> neighborList = new ArrayList<>(adjacency.get(node));
                for (int i = 0; i < neighborList.size() - 1; i++) {
                    for (int j = i + 1; j < neighborList.size(); j++) {
                        String n1 = neighborList.get(i);
                        String n2 = neighborList.get(j);
                        if (adjacency.get(n1).contains(n2)) {
                            String[] triple = new String[]{node, n1, n2};
                            Arrays.sort(triple);
                            triangles.add(String.join(",", triple));
                        }
                    }
                }
            }

            long countWithT = triangles.stream().map(triangle -> triangle.split(",")).filter(arr -> arr[0].startsWith("t") || arr[1].startsWith("t") || arr[2].startsWith("t")).count();
            System.out.printf("Sets of three inter-connected computers with at least one computer starting with 't': %d\n", countWithT);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
