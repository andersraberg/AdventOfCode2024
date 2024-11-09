package se.anders_raberg.adventofcode2024.utilities;

public class ToStringUtilities {
    private ToStringUtilities() {
    }

    public static <X> String toString(X[][] grid) {
        StringBuilder sb = new StringBuilder();
        for (X[] xes : grid) {
            for (int x = 0; x < grid[0].length; x++) {
                sb.append(xes[x]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String toString(int[][] grid) {
        StringBuilder sb = new StringBuilder();
        for (int[] ints : grid) {
            for (int x = 0; x < grid[0].length; x++) {
                sb.append(ints[x]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String toString(char[][] grid) {
        StringBuilder sb = new StringBuilder();
        for (char[] chars : grid) {
            for (int x = 0; x < grid[0].length; x++) {
                sb.append(chars[x]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
