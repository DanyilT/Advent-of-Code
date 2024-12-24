// Day 24: Crossed Wires

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class PartTwo {

    // https://adventofcode.com/2024/day/24/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            final Map<String, Gate> wireMap = new HashMap<>();

            Pattern gatePattern = Pattern.compile("^(\\S+)\\s+(AND|OR|XOR)\\s+(\\S+)\\s*->\\s*(\\S+)$"); // Regex for lines of form "<wireA> <OP> <wireB> -> <wireOut>"

            for (String line : lines) {
                // Try matching gate pattern
                Matcher mGate = gatePattern.matcher(line);
                if (mGate.matches()) {
                    String in1 = mGate.group(1).trim();
                    String op = mGate.group(2).trim().toUpperCase();
                    String in2 = mGate.group(3).trim();
                    String out = mGate.group(4).trim();
                    wireMap.put(out, new Gate(in1, in2, op, out));
                }
            }

            Set<String> swapWires = new HashSet<>();
            for (int n = 0; n < 45; n++) {
                boolean valid = true;
                for (int x = 0; x < 2; x++) {
                    for (int y = 0; y < 2; y++) {
                        for (int c = 0; c < 2; c++) {
                            List<Integer> initX = new ArrayList<>();
                            List<Integer> initY = new ArrayList<>();
                            for (int i = 0; i < (44 - n); i++) {
                                initX.add(0);
                                initY.add(0);
                            }
                            initX.add(x);
                            initY.add(y);
                            if (n > 0) {
                                initX.add(c);
                                for (int i = 0; i < (n - 1); i++) {
                                    initX.add(0);
                                }
                                initY.add(c);
                                for (int i = 0; i < (n - 1); i++) {
                                    initY.add(0);
                                }
                            } else {
                                if (c > 0) continue;
                            }
                            Collections.reverse(initX);
                            Collections.reverse(initY);
                            Map<String, List<Integer>> initMap = new HashMap<>();
                            initMap.put("x", initX);
                            initMap.put("y", initY);
                            int z = runWire(makeWire("z", n), initMap, wireMap);
                            if (z != ((x + y + c) % 2)) {
                                valid = false;
                            }
                        }
                    }
                }
                if (valid) {
                    continue;
                }
                Gate prevand = findWire("AND", makeWire("x", n - 1), makeWire("y", n - 1), wireMap);
                Gate prevxor = findWire("XOR", makeWire("x", n - 1), makeWire("y", n - 1), wireMap);

                Gate m2 = findWire("AND", prevxor.out, null, wireMap);
                Gate m1 = findWire("OR", m2.out, prevand.out, wireMap);
                Gate nxor = findWire("XOR", makeWire("x", n), makeWire("y", n), wireMap);

                Gate zn = findWire("XOR", nxor.out, m1.out, wireMap);
                List<String> swapped = new ArrayList<>();
                if (zn == null) {
                    zn = wireMap.get(makeWire("z", n));
                    Set<String> set1 = new HashSet<>();
                    set1.add(zn.in1);
                    set1.add(zn.in2);
                    Set<String> set2 = new HashSet<>();
                    set2.add(nxor.out);
                    set2.add(m1.out);
                    Set<String> symDiff = new HashSet<>(set1);
                    symDiff.addAll(set2);
                    Set<String> intersection = new HashSet<>(set1);
                    intersection.retainAll(set2);
                    symDiff.removeAll(intersection);
                    swapped.addAll(symDiff);
                }

                if (!zn.out.equals(makeWire("z", n))) {
                    swapped = new ArrayList<>();
                    swapped.add(makeWire("z", n));
                    swapped.add(zn.out);
                }

                Gate temp = wireMap.get(swapped.get(0));
                wireMap.put(swapped.get(0), wireMap.get(swapped.get(1)));
                wireMap.put(swapped.get(1), temp);
                swapWires.addAll(swapped);
            }

            List<String> sortedWires = new ArrayList<>(swapWires);
            Collections.sort(sortedWires);
            System.out.printf("Sorted swapped wires: %s%n", String.join(",", sortedWires));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static int runWire(String wire, Map<String, List<Integer>> init, Map<String, Gate> wireMap) {
        // If wire is an input wire (x?? or y??), return its corresponding bit from the init mapping.
        if (wire.matches("^(x|y)\\d{2}$")) {
            String var = wire.substring(0, 1);
            int num = Integer.parseInt(wire.substring(1));
            return init.get(var).get(num);
        }

        // Otherwise, it must be produced by a gate in the wireMap.
        Gate gate = wireMap.get(wire);
        int val1 = runWire(gate.in1, init, wireMap);
        int val2 = runWire(gate.in2, init, wireMap);

        return switch (gate.op) {
            case "AND" -> (val1 == 1 && val2 == 1) ? 1 : 0;
            case "OR"  -> (val1 == 1 || val2 == 1) ? 1 : 0;
            case "XOR" -> (val1 == val2) ? 0 : 1;
            default -> throw new IllegalArgumentException("Unknown operation: " + gate.op);
        };
    }

    public static String makeWire(String var, int num) {
        return var + String.format("%02d", num);
    }

    public static Gate findWire(String op, String in1, String in2, Map<String, Gate> wireMap) {
        for (Gate gate : wireMap.values()) {
            if (op != null && !op.equals(gate.op)) {
                continue;
            }
            if (in1 != null && !(in1.equals(gate.in1) || in1.equals(gate.in2))) {
                continue;
            }
            if (in2 != null && !(in2.equals(gate.in1) || in2.equals(gate.in2))) {
                continue;
            }
            return gate;
        }
        return null;
    }

    static class Gate {
        String in1;
        String in2;
        String op; // AND, OR, XOR
        String out;

        Gate(String in1, String in2, String op, String out) {
            this.in1 = in1;
            this.in2 = in2;
            this.op = op;
            this.out = out;
        }
    }
}
