package se.anders_raberg.adventofcode2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import se.anders_raberg.adventofcode2024.utilities.Pair;

public class Day11 {
    private static final Logger LOGGER = Logger.getLogger(Day11.class.getName());
    private static final Map<Pair<Long, Integer>, Long> MEMOIZE = new HashMap<>();

    private Day11() {
    }

    public static void run() throws IOException {
        List<Long> input = Arrays
                .stream(new String(Files.readAllBytes(Paths.get("inputs/input11.txt"))).trim().split("\\s+"))
                .map(Long::parseLong).toList();

        LOGGER.info(() -> "Part 1: " + input.stream().mapToLong(s -> runBlinks(s, 25)).sum());
        LOGGER.info(() -> "Part 2: " + input.stream().mapToLong(s -> runBlinks(s, 75)).sum());
    }

    private static Long runBlinks(Long stone, int blinks) {
        Long result;
        if (MEMOIZE.containsKey(new Pair<>(stone, blinks))) {
            result = MEMOIZE.get(new Pair<>(stone, blinks));
        } else if (blinks == 0) {
            result = 1L;
        } else if (stone == 0) {
            result = runBlinks(1L, blinks - 1);
        } else if (stone.toString().length() % 2 == 0) {
            String s = stone.toString();
            int half = s.length() / 2;
            long left = Long.parseLong(s.substring(0, half));
            long right = Long.parseLong(s.substring(half));
            result = runBlinks(left, blinks - 1) + runBlinks(right, blinks - 1);
        } else {
            result = runBlinks(stone * 2024, blinks - 1);
        }
        MEMOIZE.put(new Pair<>(stone, blinks), result);
        return result;
    }
}