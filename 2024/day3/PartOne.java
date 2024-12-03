// Day 3: Mull It Over

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class PartOne {

    // https://adventofcode.com/2024/day/3/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            int multiplications = 0;
            for (String line : lines) {
                Pattern pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    multiplications += Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2));
                    System.out.printf("Instruction %s | Multiplication (%d)\n", matcher.group(), Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2)));
                }
            }
            System.out.printf("Multiplications: %d\n", multiplications);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
