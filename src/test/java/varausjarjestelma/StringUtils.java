package varausjarjestelma;

import fi.helsinki.cs.tmc.edutestutils.MockInOut;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtils {

    public static List<String> lines(MockInOut io) {
        return Arrays.asList(io.getOutput().split("\n"));
    }

    public static List<String> linesFromLastOccurrence(MockInOut io, String expected) {
        return linesFromLastOccurrence(lines(io), expected);
    }

    public static List<String> linesFromLastOccurrence(List<String> lines, String expected) {
        expected = expected.trim().toLowerCase();

        int lastIndex = -1;
        for (int i = lines.size() - 1; i > 0; i--) {
            String line = lines.get(i).trim().toLowerCase();
            if (calculateDist(line, expected) <= 2) {
                lastIndex = i;
                break;
            }

        }

        if (lastIndex == -1) {
            return new ArrayList<>();
        }

        return lines.subList(lastIndex, lines.size());
    }

    public static final int calculateDist(String x, String y) {
        int[][] dp = new int[x.length() + 1][y.length() + 1];

        for (int i = 0; i <= x.length(); i++) {
            for (int j = 0; j <= y.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = min(dp[i - 1][j - 1]
                            + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)),
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1);
                }
            }
        }

        return dp[x.length()][y.length()];
    }

    private static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    private static int min(int... numbers) {
        return Arrays.stream(numbers)
                .min().orElse(Integer.MAX_VALUE);
    }
}
