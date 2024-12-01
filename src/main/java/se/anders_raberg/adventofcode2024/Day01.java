package se.anders_raberg.adventofcode2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import se.anders_raberg.adventofcode2024.utilities.Pair;

public class Day01 {
	private static final Logger LOGGER = Logger.getLogger(Day01.class.getName());

	private Day01() {
	}

	public static void run() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("inputs/input1.txt"));
		List<Pair<Integer, Integer>> list = lines.stream().map(Day01::parseLine).toList();

		List<Integer> left = list.stream().map(Pair::first).sorted().toList();
		List<Integer> right = list.stream().map(Pair::second).sorted().toList();

		int sum1 = IntStream.range(0, left.size()).map(i -> Math.abs(left.get(i) - right.get(i))).sum();

		LOGGER.info("Part 1: " + sum1);

		Map<Integer, Long> result = right.stream()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		long sum2 = left.stream().mapToLong(v -> v * result.getOrDefault(v, 0L)).sum();

		LOGGER.info("Part 2: " + sum2);
	}

	private static Pair<Integer, Integer> parseLine(String line) {
		String[] split = line.split("\\W+");
		return new Pair<>(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
	}
}