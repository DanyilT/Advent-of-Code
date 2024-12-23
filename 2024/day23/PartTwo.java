// Day 23: LAN Party

import java.io.*;
import java.util.*;

public class PartTwo {

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

            List<String> nodes = new ArrayList<>(adjacency.keySet());

            Set<String> R = new HashSet<>();        // Current clique
            Set<String> P = new HashSet<>(nodes);   // Candidates
            Set<String> X = new HashSet<>();        // Already excluded
            List<Set<String>> cliques = new ArrayList<>(); // All maximal cliques

            bronKerbosch(adjacency, R, P, X, cliques);

            Set<String> maxClique = new HashSet<>();
            for (Set<String> clique : cliques) {
                if (clique.size() > maxClique.size()) {
                    maxClique = clique;
                }
            }

            List<String> password = new ArrayList<>(maxClique);
            Collections.sort(password);
            System.out.printf("Password: %s\n", String.join(",", password));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void bronKerbosch(Map<String, Set<String>> adjacency, Set<String> R, Set<String> P, Set<String> X, List<Set<String>> cliques) {
        if (P.isEmpty() && X.isEmpty()) {
            // R is a maximal clique
            cliques.add(new HashSet<>(R));
            return;
        }

        // To reduce recursion, pick a pivot
        String pivot = null;
        if (!P.isEmpty()) {
            pivot = P.iterator().next();
        } else if (!X.isEmpty()) {
            pivot = X.iterator().next();
        }

        // The pivot's neighbors
        Set<String> pivotNeighbors = pivot != null ? adjacency.get(pivot) : Collections.emptySet();

        // For each candidate outside the pivot's neighbors (This is the Bron-Kerbosch pivot optimization)
        Set<String> candidates = new HashSet<>(P);
        candidates.removeAll(pivotNeighbors);

        for (String v : candidates) {
            // Move v from P to R
            Set<String> newR = new HashSet<>(R);
            newR.add(v);

            // Intersect P with neighbors(v)
            Set<String> newP = intersect(P, adjacency.get(v));

            // Intersect X with neighbors(v)
            Set<String> newX = intersect(X, adjacency.get(v));

            bronKerbosch(adjacency, newR, newP, newX, cliques);

            // Remove v from P, add to X
            P.remove(v);
            X.add(v);
        }
    }

    private static Set<String> intersect(Set<String> s1, Set<String> s2) {
        Set<String> result = new HashSet<>();
        for (String item : s1) {
            if (s2.contains(item)) {
                result.add(item);
            }
        }
        return result;
    }
}
