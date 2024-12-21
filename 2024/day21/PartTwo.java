// Day 21: Keypad Conundrum

import java.io.*;
import java.util.*;

public class PartTwo {

    // https://adventofcode.com/2024/day/21/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            Map<String, Long> GET_LENGTH_CACHE = new HashMap<>();
            Map<Pair, List<String>> NUM_PAD_PATHS = new HashMap<>();
            Map<Pair, List<String>> DIR_PAD_PATHS = new HashMap<>();

            final Map<Point, String> NUM_PAD = new HashMap<>();
            NUM_PAD.put(new Point(0,0), "7"); NUM_PAD.put(new Point(0,1), "8"); NUM_PAD.put(new Point(0,2), "9");
            NUM_PAD.put(new Point(1,0), "4"); NUM_PAD.put(new Point(1,1), "5"); NUM_PAD.put(new Point(1,2), "6");
            NUM_PAD.put(new Point(2,0), "1"); NUM_PAD.put(new Point(2,1), "2"); NUM_PAD.put(new Point(2,2), "3");
            NUM_PAD.put(new Point(3,1), "0"); NUM_PAD.put(new Point(3,2), "A");

            final Map<Point, String> DIR_PAD = new HashMap<>();
            DIR_PAD.put(new Point(0,1), "^");
            DIR_PAD.put(new Point(0,2), "A");
            DIR_PAD.put(new Point(1,0), "<");
            DIR_PAD.put(new Point(1,1), "v");
            DIR_PAD.put(new Point(1,2), ">");

            long sumOfComplexities = 0;

            buildPadPaths(NUM_PAD, NUM_PAD_PATHS);
            buildPadPaths(DIR_PAD, DIR_PAD_PATHS);

            for (String code : lines) {
                String numericPartStr = code.replaceAll("[^0-9]", "");
                if (numericPartStr.isEmpty()) numericPartStr = "0";
                int numericValue = Integer.parseInt(numericPartStr);

                long shortestPresses = Long.MAX_VALUE;

                List<String> allMergedPaths = keysToPaths(code, NUM_PAD_PATHS);

                for (String path1 : allMergedPaths) {
                    long tmp = getLength(path1, 25, GET_LENGTH_CACHE, DIR_PAD_PATHS);
                    if (tmp < shortestPresses) {
                        shortestPresses = tmp;
                    }
                }

                sumOfComplexities += numericValue * shortestPresses;
            }
            System.out.printf("Sum of complexities: %d\n", sumOfComplexities);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> findBestXtoY(Map<Point, String> pad, Point start, Point end) {
        final Move[] MOVES = {
                new Move( 1,  0, "v"),
                new Move(-1,  0, "^"),
                new Move( 0,  1, ">"),
                new Move( 0, -1, "<")
        };

        if (start.equals(end)) {
            return Collections.singletonList("A");
        }

        Queue<BFSNode> queue = new ArrayDeque<>();
        queue.add(new BFSNode(start.r, start.c, ""));

        long bestLength = 10;
        List<String> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            BFSNode cur = queue.poll();
            for (Move mov : MOVES) {
                long nr = cur.r + mov.dr;
                long nc = cur.c + mov.dc;
                if (!pad.containsKey(new Point(nr, nc))) {
                    continue;
                }
                String newPath = cur.path + mov.symbol;
                if (newPath.length() + 1 > bestLength) {
                    continue;
                }
                if (nr == end.r && nc == end.c) {
                    String finalPath = newPath + "A";
                    result.add(finalPath);
                    bestLength = Math.min(bestLength, finalPath.length());
                } else {
                    queue.add(new BFSNode(nr, nc, newPath));
                }
            }
        }
        return result;
    }

    private static void buildPadPaths(Map<Point, String> pad, Map<Pair, List<String>> pathMap) {
        List<Point> allPoints = new ArrayList<>(pad.keySet());
        for (Point p1 : allPoints) {
            for (Point p2 : allPoints) {
                String fromChar = pad.get(p1);
                String toChar = pad.get(p2);
                if (fromChar == null || toChar == null) continue;
                List<String> paths = findBestXtoY(pad, p1, p2);
                pathMap.put(new Pair(fromChar, toChar), paths);
            }
        }
    }

    private static List<String> keysToPaths(String code, Map<Pair, List<String>> NUM_PAD_PATHS) {
        String extended = "A" + code;
        int n = code.length();
        List<List<String>> pathLists = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String b1 = String.valueOf(extended.charAt(i));
            String b2 = String.valueOf(code.charAt(i));
            Pair p = new Pair(b1, b2);
            List<String> partial = NUM_PAD_PATHS.getOrDefault(p, Collections.emptyList());
            pathLists.add(partial);
        }

        List<String> result = new ArrayList<>();
        cartesianProductJoin(pathLists, 0, new StringBuilder(), result);
        return result;
    }

    private static void cartesianProductJoin(List<List<String>> pathLists, int index, StringBuilder current, List<String> result) {
        if (index == pathLists.size()) {
            result.add(current.toString());
            return;
        }

        for (String seg : pathLists.get(index)) {
            int lengthBefore = current.length();
            current.append(seg);
            cartesianProductJoin(pathLists, index + 1, current, result);
            current.setLength(lengthBefore);
        }
    }

    private static long getLength(String code, long depth, Map<String, Long> GET_LENGTH_CACHE, Map<Pair, List<String>> DIR_PAD_PATHS) {
        String key = code + "." + depth;
        if (GET_LENGTH_CACHE.containsKey(key)) {
            return GET_LENGTH_CACHE.get(key);
        }

        long result;
        if (depth == 0) {
            result = code.length();
        } else {
            long sum = 0;
            String extended = "A" + code;
            for (int i = 0; i < code.length(); i++) {
                String p1 = String.valueOf(extended.charAt(i));
                String p2 = String.valueOf(code.charAt(i));
                Pair pairObj = new Pair(p1, p2);
                List<String> allPaths = DIR_PAD_PATHS.getOrDefault(pairObj, Collections.emptyList());
                if (allPaths.isEmpty()) {
                    continue;
                }
                long minVal = Long.MAX_VALUE;
                for (String pathCandidate : allPaths) {
                    long val = getLength(pathCandidate, depth - 1, GET_LENGTH_CACHE, DIR_PAD_PATHS);
                    if (val < minVal) {
                        minVal = val;
                    }
                }
                sum += minVal;
            }
            result = sum;
        }

        GET_LENGTH_CACHE.put(key, result);
        return result;
    }

    private static class BFSNode {
        final long r, c;
        final String path;

        BFSNode(long r, long c, String path) {
            this.r = r; this.c = c; this.path = path;
        }
    }

    private static class Point {
        final long r, c;

        Point(long r, long c) {
            this.r = r; this.c = c;
        }

        @Override
        public boolean equals(Object o) {
            if(!(o instanceof Point)) return false;
            Point other = (Point)o;
            return this.r == other.r && this.c == other.c;
        }

        @Override
        public int hashCode() {
            return Objects.hash(r,c);
        }
    }

    private static class Move {
        final long dr, dc;
        final String symbol;

        Move(long dr, long dc, String symbol) {
            this.dr = dr; this.dc = dc; this.symbol = symbol;
        }
    }

    private static class Pair {
        final String from;
        final String to;

        Pair(String from, String to) {
            this.from = from; this.to = to;
        }

        @Override
        public boolean equals(Object o) {
            if(!(o instanceof Pair)) return false;
            Pair other = (Pair)o;
            return Objects.equals(this.from, other.from) && Objects.equals(this.to, other.to);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }
    }
}
