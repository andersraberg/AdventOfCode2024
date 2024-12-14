package se.anders_raberg.adventofcode2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import se.anders_raberg.adventofcode2024.utilities.Pair;

public class Day08 {
	private static final Logger LOGGER = Logger.getLogger(Day08.class.getName());
	private static final Map<Pos, String> GRID = new HashMap<>();
	private static int width;
	private static int height;

	public record Pos(int x, int y) {
	}

	private Day08() {
	}

	public static void run() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("inputs/input8.txt"));
		for (int y = 0; y < lines.size(); y++) {
			String[] row = lines.get(y).trim().split("");
			for (int x = 0; x < row.length; x++) {
				GRID.put(new Pos(x, y), row[x]);
			}
		}

		width = lines.get(0).length();
		height = lines.size();

		Map<String, Set<Pos>> positionsByFreq = GRID.entrySet().stream() //
				.filter(e -> !".".equals(e.getValue())) //
				.collect(Collectors.groupingBy(Entry::getValue, //
						Collectors.mapping(Entry::getKey, Collectors.toSet())));

		Map<String, Set<Pair<Pos, Pos>>> posCombinationsByFreq = positionsByFreq.entrySet().stream()
				.collect(Collectors.toMap(Entry::getKey, e -> generatePairs(e.getValue())));

		List<Pair<Pos, Pos>> posCombinations = posCombinationsByFreq.values().stream().flatMap(Collection::stream)
				.toList();

		LOGGER.info(() -> "Part 1: " + countAntiNodes(posCombinations, 2, 2));
		LOGGER.info(() -> "Part 2: " + countAntiNodes(posCombinations, 1, 60));
	}

	private static long countAntiNodes(List<Pair<Pos, Pos>> www, int start, int stop) {
		return www.stream().map(p -> findAntiNodes(p, start, stop)).flatMap(Collection::stream).filter(Day08::inside)
				.distinct().count();
	}

	private static Set<Pos> findAntiNodes(Pair<Pos, Pos> posPair, int start, int stop) {
		int xF = posPair.first().x;
		int yF = posPair.first().y;
		int xS = posPair.second().x;
		int yS = posPair.second().y;

		Set<Pos> result = new HashSet<>();
		for (int i = start; i <= stop; i++) {
			int xA = xS - i * (xS - xF);
			int yA = yS - i * (yS - yF);
			int xB = xF - i * (xF - xS);
			int yB = yF - i * (yF - yS);
			result.addAll(Set.of(new Pos(xA, yA), new Pos(xB, yB)));
		}

		return result;
	}

	private static Set<Pair<Pos, Pos>> generatePairs(Set<Pos> positions) {
		if (positions.size() >= 2) {
			return Sets.combinations(positions, 2).stream().map(c -> new ArrayList<>(c))
					.map(c -> new Pair<>(c.get(0), c.get(1))).collect(Collectors.toSet());
		} else {
			return Collections.emptySet();
		}
	}

	private static boolean inside(Pos pos) {
		return pos.x < width && pos.x >= 0 && pos.y < height && pos.y >= 0;
	}
}
