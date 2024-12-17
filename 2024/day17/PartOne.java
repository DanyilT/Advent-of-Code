// Day 17: Chronospatial Computer

import java.io.*;
import java.util.*;

public class PartOne {

    // https://adventofcode.com/2024/day/17/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            int registerA = Integer.parseInt(lines.get(0).split(": ")[1]);
            int registerB = Integer.parseInt(lines.get(1).split(": ")[1]);
            int registerC = Integer.parseInt(lines.get(2).split(": ")[1]);

            String[] programStr = lines.get(4).split(": ")[1].split(",");
            int[] program = Arrays.stream(programStr).mapToInt(Integer::parseInt).toArray();

            List<Integer> output = new ArrayList<>();
            int instructionPointer = 0;

            while (instructionPointer < program.length) {
                int opcode = program[instructionPointer];
                int operand = program[instructionPointer + 1];
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
                        registerB = (int) (registerA / Math.pow(2, getComboOperandValue(operand, registerA, registerB, registerC)));
                        break;
                    case 7: // cdv
                        registerC = (int) (registerA / Math.pow(2, getComboOperandValue(operand, registerA, registerB, registerC)));
                        break;
                }
            }
            System.out.printf("Output into single string: %s\n", String.join(",", output.stream().map(String::valueOf).toArray(String[]::new)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static int getComboOperandValue(int operand, int registerA, int registerB, int registerC) {
        return switch (operand) {
            case 0, 1, 2, 3 -> operand;
            case 4 -> registerA;
            case 5 -> registerB;
            case 6 -> registerC;
            default -> throw new IllegalArgumentException("Invalid combo operand: " + operand);
        };
    }
}
