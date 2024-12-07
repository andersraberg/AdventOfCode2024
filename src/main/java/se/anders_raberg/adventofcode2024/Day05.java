package se.anders_raberg.adventofcode2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import se.anders_raberg.adventofcode2024.utilities.Pair;

public class Day05 {
	private static final Logger LOGGER = Logger.getLogger(Day05.class.getName());
	private static final Pattern RULE_PATTERN = Pattern.compile("(\\d+)\\|(\\d+)");
	private static final Pattern UPDATE_PATTERN = Pattern.compile("\\d+,\\d+");

	private Day05() {
	}

	public static void run() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("inputs/input5.txt"));

		List<Pair<Integer, Integer>> rules = lines.stream() //
				.map(RULE_PATTERN::matcher) //
				.filter(Matcher::find) //
				.map(m -> new Pair<>(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)))).toList();

		List<List<Integer>> updates = lines.stream().filter(l -> UPDATE_PATTERN.matcher(l).find())
				.map(s -> Arrays.stream(s.split(",")).map(Integer::parseInt).toList()).toList();

		Map<Boolean, List<List<Integer>>> partitionedUpdates = updates.stream()
				.collect(Collectors.partitioningBy(u -> correctlyOrdered(u, rules)));

		LOGGER.info(() -> "Part 1: "
				+ partitionedUpdates.get(Boolean.TRUE).stream().mapToInt(u -> u.get(u.size() / 2)).sum());

		LOGGER.info(() -> "Part 2: " + partitionedUpdates.get(Boolean.FALSE).stream()
				.map(q -> performOrdering(q, rules)).mapToInt(u -> u.get(u.size() / 2)).sum());
	}

	private static boolean correctlyOrdered(List<Integer> update, List<Pair<Integer, Integer>> rules) {
		return rules.stream().filter(r -> update.containsAll(Set.of(r.first(), r.second())))
				.allMatch(r -> update.indexOf(r.second()) > update.indexOf(r.first()));
	}

	private static List<Integer> performOrdering(List<Integer> update, List<Pair<Integer, Integer>> rules) {
		List<Integer> result = new ArrayList<>(update);
		boolean change = true;

		while (change) {
			change = false;
			for (Pair<Integer, Integer> rule : rules) {
				if (result.containsAll(Set.of(rule.first(), rule.second()))) {
					int i1 = result.indexOf(rule.first());
					int i2 = result.indexOf(rule.second());
					if (i2 < i1) {
						change = true;
						int tmp = result.get(i2);
						result.set(i2, result.get(i1));
						result.set(i1, tmp);
					}
				}
			}
		}

		return result;
	}

}
