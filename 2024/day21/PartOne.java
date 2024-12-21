// Day 21: Keypad Conundrum

import java.io.*;
import java.util.*;

public class PartOne {

    // https://adventofcode.com/2024/day/21/input
    private static final String FILENAME = "input.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = new BufferedReader(new FileReader(FILENAME)).lines().toList();

            int sumOfComplexities = 0;

            for (String codeLine : lines) {
                int shortestPresses = findShortestPressCount(codeLine);
                String numericPartStr = codeLine.replaceAll("[^0-9]", "");
                if (numericPartStr.isEmpty()) numericPartStr = "0";
                int numericValue = Integer.parseInt(numericPartStr);
                sumOfComplexities += shortestPresses * numericValue;
            }
            System.out.printf("Sum of complexities: %d\n", sumOfComplexities);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static int findShortestPressCount(String code) {
        final int[] REMOTE_START = {0, 2};
        final int[] NUMERIC_START = {3, 2};

        Queue<State> queue = new LinkedList<>();
        Map<State, Integer> dist = new HashMap<>();

        State start = new State(
                REMOTE_START[0], REMOTE_START[1],
                REMOTE_START[0], REMOTE_START[1],
                NUMERIC_START[0], NUMERIC_START[1],
                0
        );
        queue.offer(start);
        dist.put(start, 0);

        while (!queue.isEmpty()) {
            State cur = queue.poll();
            int d = dist.get(cur);

            if (cur.typedCount == code.length()) {
                return d;
            }

            for (String input : new String[] { "^", "v", "<", ">", "A" }) {
                State next = applyTopInput(cur, input, code);
                if (next == null) {
                    continue;
                }
                if (!dist.containsKey(next)) {
                    dist.put(next, d + 1);
                    queue.offer(next);
                }
            }
        }
        return Integer.MAX_VALUE; // not found
    }

    private static State applyTopInput(State cur, String input, String code) {
        final String[][] REMOTE_KEYPAD = {
                { null, "^",   "A"   },
                { "<",  "v",   ">"   }
        };
        final Map<String, int[]> DIRECTION_OFFSETS = new HashMap<>();
        DIRECTION_OFFSETS.put("^", new int[] { -1,  0 });
        DIRECTION_OFFSETS.put("v", new int[] {  1,  0 });
        DIRECTION_OFFSETS.put("<", new int[] {  0, -1 });
        DIRECTION_OFFSETS.put(">", new int[] {  0,  1 });

        int newTopR = cur.topR;
        int newTopC = cur.topC;
        int newMidR = cur.midR;
        int newMidC = cur.midC;
        int newBotR = cur.botR;
        int newBotC = cur.botC;
        int newTypedCount = cur.typedCount;

        if (DIRECTION_OFFSETS.containsKey(input)) {
            int[] off = DIRECTION_OFFSETS.get(input);
            int r2 = newTopR + off[0];
            int c2 = newTopC + off[1];
            if (!valid(r2, c2, REMOTE_KEYPAD)) return null;
            newTopR = r2;
            newTopC = c2;
        } else if ("A".equals(input)) {
            String btn = REMOTE_KEYPAD[newTopR][newTopC];
            if (btn == null) return null;
            MidResult mres = pressSecondLevel(btn, newMidR, newMidC, newBotR, newBotC, newTypedCount, code, REMOTE_KEYPAD, DIRECTION_OFFSETS);
            if (mres == null) return null;
            newMidR = mres.midR;
            newMidC = mres.midC;
            newBotR = mres.botR;
            newBotC = mres.botC;
            newTypedCount = mres.typedCount;
        }

        return new State(newTopR, newTopC, newMidR, newMidC, newBotR, newBotC, newTypedCount);
    }

    private static MidResult pressSecondLevel(String topBtn, int midR, int midC, int botR, int botC, int typedCount, String code, String[][] REMOTE_KEYPAD, Map<String, int[]> DIRECTION_OFFSETS) {
        if (DIRECTION_OFFSETS.containsKey(topBtn)) {
            int[] off = DIRECTION_OFFSETS.get(topBtn);
            int r2 = midR + off[0];
            int c2 = midC + off[1];
            if (!valid(r2, c2, REMOTE_KEYPAD)) return null;
            return new MidResult(r2, c2, botR, botC, typedCount);
        }
        else if ("A".equals(topBtn)) {
            String midBtn = REMOTE_KEYPAD[midR][midC];
            if (midBtn == null) return null;
            BotResult bres = pressThirdLevel(midBtn, botR, botC, typedCount, code, DIRECTION_OFFSETS);
            if (bres == null) return null;
            return new MidResult(midR, midC, bres.botR, bres.botC, bres.typedCount);
        }
        return null;
    }

    private static BotResult pressThirdLevel(String midBtn, int botR, int botC, int typedCount, String code, Map<String, int[]> DIRECTION_OFFSETS) {
        final String[][] NUMERIC_KEYPAD = {
                { "7", "8", "9" },
                { "4", "5", "6" },
                { "1", "2", "3" },
                { null, "0", "A" }
        };

        if (DIRECTION_OFFSETS.containsKey(midBtn)) {
            int[] off = DIRECTION_OFFSETS.get(midBtn);
            int r2 = botR + off[0];
            int c2 = botC + off[1];
            if (!valid(r2, c2, NUMERIC_KEYPAD)) return null;
            return new BotResult(r2, c2, typedCount);
        }
        else if ("A".equals(midBtn)) {
            String numBtn = NUMERIC_KEYPAD[botR][botC];
            if (numBtn == null) return null;

            if (typedCount < code.length() && code.charAt(typedCount) == numBtn.charAt(0)) {
                return new BotResult(botR, botC, typedCount + 1);
            } else {
                return null;
            }
        }
        return null;
    }

    private static boolean valid(int r, int c, String[][] keypad) {
        if (r < 0 || r >= keypad.length) return false;
        if (c < 0 || c >= keypad[r].length) return false;
        return keypad[r][c] != null;
    }

    static class State {
        final int topR, topC;
        final int midR, midC;
        final int botR, botC;
        final int typedCount;

        State(int tr, int tc, int mr, int mc, int br, int bc, int typedCount) {
            this.topR = tr;
            this.topC = tc;
            this.midR = mr;
            this.midC = mc;
            this.botR = br;
            this.botC = bc;
            this.typedCount = typedCount;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof State)) return false;
            State s = (State) o;
            return topR == s.topR && topC == s.topC &&
                    midR == s.midR && midC == s.midC &&
                    botR == s.botR && botC == s.botC &&
                    typedCount == s.typedCount;
        }

        @Override
        public int hashCode() {
            return Objects.hash(topR, topC, midR, midC, botR, botC, typedCount);
        }
    }

    static class MidResult {
        int midR, midC;
        int botR, botC;
        int typedCount;
        MidResult(int mr, int mc, int br, int bc, int typedCount) {
            this.midR = mr; this.midC = mc;
            this.botR = br; this.botC = bc;
            this.typedCount = typedCount;
        }
    }

    static class BotResult {
        int botR, botC;
        int typedCount;
        BotResult(int br, int bc, int typedCount) {
            this.botR = br; this.botC = bc;
            this.typedCount = typedCount;
        }
    }
}
