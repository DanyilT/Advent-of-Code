// Day 24: Crossed Wires

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class PartOne {

    // https://adventofcode.com/2024/day/24/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            final Map<String, Integer> wireValues = new HashMap<>();

            Pattern initialValuePattern = Pattern.compile("^([^:]+):\\s*([01])\\s*$"); // Regex for lines of form "<wire>: <value>"
            Pattern gatePattern = Pattern.compile("^(\\S+)\\s+(AND|OR|XOR)\\s+(\\S+)\\s*->\\s*(\\S+)$"); // Regex for lines of form "<wireA> <OP> <wireB> -> <wireOut>"

            List<Gate> gates = new ArrayList<>();

            for (String line : lines) {
                // Try matching initial assignment pattern
                Matcher mInit = initialValuePattern.matcher(line);
                if (mInit.matches()) {
                    String wireName = mInit.group(1).trim();
                    String valueStr = mInit.group(2).trim();
                    int value = Integer.parseInt(valueStr);
                    wireValues.put(wireName, value);
                    continue;
                }

                // Try matching gate pattern
                Matcher mGate = gatePattern.matcher(line);
                if (mGate.matches()) {
                    String in1 = mGate.group(1).trim();
                    String op = mGate.group(2).trim().toUpperCase();
                    String in2 = mGate.group(3).trim();
                    String out = mGate.group(4).trim();
                    gates.add(new Gate(in1, op, in2, out));
                    continue;
                }
            }

            boolean changed = true;
            while (changed) {
                changed = false;
                for (Gate g : gates) {
                    Integer existingOutVal = wireValues.get(g.out);
                    Integer in1Val = wireValues.get(g.in1);
                    Integer in2Val = wireValues.get(g.in2);

                    if (in1Val != null && in2Val != null) {
                        int newVal;
                        switch (g.op) {
                            case "AND" -> newVal = (in1Val == 1 && in2Val == 1) ? 1 : 0;
                            case "OR"  -> newVal = (in1Val == 1 || in2Val == 1) ? 1 : 0;
                            case "XOR" -> newVal = (in1Val.intValue() == in2Val.intValue()) ? 0 : 1;
                            default -> throw new IllegalArgumentException("Unknown operation: " + g.op);
                        }

                        if (existingOutVal == null || existingOutVal != newVal) {
                            wireValues.put(g.out, newVal);
                            changed = true;
                        }
                    }
                }
            }

            List<Integer> zBits = new ArrayList<>();
            for (String wire : wireValues.keySet()) {
                if (wire.startsWith("z")) {
                    int bitIndex = Integer.parseInt(wire.substring(1));
                    while (zBits.size() <= bitIndex) {
                        zBits.add(0);
                    }
                    zBits.set(bitIndex, wireValues.get(wire));
                }
            }

            // Convert the bits from LSB to MSB into a decimal number
            long result = 0;
            long place = 1;
            for (int bit : zBits) {
                if (bit == 1) {
                    result += place;
                }
                place <<= 1; // multiply by 2
            }
            System.out.printf("Decimal number represented by z-bits: %d\n", result);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static class Gate {
        final String in1;
        final String in2;
        final String op;   // AND, OR, XOR
        final String out;

        Gate(String in1, String op, String in2, String out) {
            this.in1 = in1;
            this.in2 = in2;
            this.op = op;
            this.out = out;
        }
    }
}
