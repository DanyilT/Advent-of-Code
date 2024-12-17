// Day 17: Chronospatial Computer

import java.io.*;
import java.util.*;

public class PartTwo {

    // https://adventofcode.com/2024/day/17/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            long registerA = Long.parseLong(lines.get(0).split(": ")[1]);
            long registerB = Long.parseLong(lines.get(1).split(": ")[1]);
            long registerC = Long.parseLong(lines.get(2).split(": ")[1]);

            String[] programStr = lines.get(4).split(": ")[1].split(",");
            long[] program = Arrays.stream(programStr).mapToLong(Long::parseLong).toArray();

            long initialA = findInitialA(program, registerB, registerC);
            System.out.printf("Lowest positive initial value for register A: %d\n", initialA);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // Probably should optimize this, because the answer is 1e9+1, so it's going for days to finish the job
    private static long findInitialA(long[] program, long registerB, long registerC) {
        for (long initialA = 1; ; initialA++) {
            System.out.printf("Trying initial value for register A: %d\n", initialA);
            if (doesProgramOutputItself(initialA, registerB, registerC, program)) {
                return initialA;
            }
        }
    }

    private static boolean doesProgramOutputItself(long registerA, long registerB, long registerC, long[] program) {
        List<Long> output = new ArrayList<>();
        int instructionPointer = 0;

        while (instructionPointer < program.length) {
            int opcode = (int) program[instructionPointer];
            int operand = (int) program[instructionPointer + 1];
            instructionPointer += 2;

            switch (opcode) {
                case 0: // adv
                    registerA /= Math.pow(2, getComboOperandValue(operand, registerA, registerB, registerC));
                    break;
                case 1: // bxl
                    registerB ^= operand;
                    break;
                case 2: // bst
                    registerB = getComboOperandValue(operand, registerA, registerB, registerC) % 8;
                    break;
                case 3: // jnz
                    if (registerA != 0) {
                        instructionPointer = operand;
                    }
                    break;
                case 4: // bxc
                    registerB ^= registerC;
                    break;
                case 5: // out
                    output.add(getComboOperandValue(operand, registerA, registerB, registerC) % 8);
                    break;
                case 6: // bdv
                    registerB = (long) (registerA / Math.pow(2, getComboOperandValue(operand, registerA, registerB, registerC)));
                    break;
                case 7: // cdv
                    registerC = (long) (registerA / Math.pow(2, getComboOperandValue(operand, registerA, registerB, registerC)));
                    break;
            }
        }

        return Arrays.equals(output.stream().mapToLong(Long::longValue).toArray(), program);
    }

    private static long getComboOperandValue(int operand, long registerA, long registerB, long registerC) {
        return switch (operand) {
            case 0, 1, 2, 3 -> operand;
            case 4 -> registerA;
            case 5 -> registerB;
            case 6 -> registerC;
            default -> throw new IllegalArgumentException("Invalid combo operand: " + operand);
        };
    }
}
