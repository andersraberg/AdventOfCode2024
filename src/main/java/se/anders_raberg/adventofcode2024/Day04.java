package se.anders_raberg.adventofcode2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class Day04 {
	private static final Logger LOGGER = Logger.getLogger(Day04.class.getName());
	private static final Table<Integer, Integer, String> GRID = HashBasedTable.create();
	private static final List<String> WORD = List.of("X", "M", "A", "S");

	private Day04() {
	}

	public static void run() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("inputs/input4.txt"));

		for (int y = 0; y < lines.size(); y++) {
			String[] row = lines.get(y).trim().split("");
			for (int x = 0; x < row.length; x++) {
				GRID.put(x, y, row[x]);
			}
		}

		LOGGER.info(() -> "Part 1: "
				+ GRID.cellSet().stream().mapToInt(c -> count1(c.getRowKey(), c.getColumnKey())).sum());

		LOGGER.info(() -> "Part 1: " + GRID.cellSet().stream().filter(c -> "A".equals(c.getValue()))
				.filter(c -> check(c.getRowKey(), c.getColumnKey())).count());

	}

	private static boolean check(int x, int y) {
		return checkPermutation(x, y, List.of("M", "M", "S", "S"))
				|| checkPermutation(x, y, List.of("M", "S", "S", "M"))
				|| checkPermutation(x, y, List.of("S", "S", "M", "M"))
				|| checkPermutation(x, y, List.of("S", "M", "M", "S"));
	}

	private static boolean checkPermutation(int x, int y, List<String> s) {
		return s.get(0).equals(GRID.get(x - 1, y - 1)) && //
				s.get(1).equals(GRID.get(x + 1, y - 1)) && //
				s.get(2).equals(GRID.get(x + 1, y + 1)) && //
				s.get(3).equals(GRID.get(x - 1, y + 1));

	}

	private static int count1(int x, int y) {
		return up(x, y) + down(x, y) + right(x, y) + left(x, y) + upLeft(x, y) + upRight(x, y) + downLeft(x, y)
				+ downRight(x, y);
	}

	private static int up(int x, int y) {
		return IntStream.rangeClosed(0, 3).allMatch(i -> WORD.get(i).equals((GRID.get(x, y - i)))) ? 1 : 0;
	}

	private static int down(int x, int y) {
		return IntStream.rangeClosed(0, 3).allMatch(i -> WORD.get(i).equals((GRID.get(x, y + i)))) ? 1 : 0;
	}

	private static int right(int x, int y) {
		return IntStream.rangeClosed(0, 3).allMatch(i -> WORD.get(i).equals((GRID.get(x + i, y)))) ? 1 : 0;
	}

	private static int left(int x, int y) {
		return IntStream.rangeClosed(0, 3).allMatch(i -> WORD.get(i).equals((GRID.get(x - i, y)))) ? 1 : 0;
	}

	private static int upLeft(int x, int y) {
		return IntStream.rangeClosed(0, 3).allMatch(i -> WORD.get(i).equals((GRID.get(x - i, y - i)))) ? 1 : 0;
	}

	private static int upRight(int x, int y) {
		return IntStream.rangeClosed(0, 3).allMatch(i -> WORD.get(i).equals((GRID.get(x + i, y - i)))) ? 1 : 0;
	}

	private static int downLeft(int x, int y) {
		return IntStream.rangeClosed(0, 3).allMatch(i -> WORD.get(i).equals((GRID.get(x - i, y + i)))) ? 1 : 0;
	}

	private static int downRight(int x, int y) {
		return IntStream.rangeClosed(0, 3).allMatch(i -> WORD.get(i).equals((GRID.get(x + i, y + i)))) ? 1 : 0;
	}

}