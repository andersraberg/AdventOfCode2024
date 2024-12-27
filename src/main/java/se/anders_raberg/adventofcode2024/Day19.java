package se.anders_raberg.adventofcode2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Day19 {
    private static final Logger LOGGER = Logger.getLogger(Day19.class.getName());
    private static final Map<String, Long> MEMOIZE = new HashMap<>();

    private Day19() {
    }

    public static void run() throws IOException {
        String[] input = new String(Files.readAllBytes(Paths.get("inputs/input19.txt"))).split("\n\n");
        List<String> patterns = Arrays.stream(input[0].split(", ")).toList();
        List<String> designs = Arrays.stream(input[1].split("\n")).toList();

        LOGGER.info(() -> "Part 1: " + designs.stream().filter(d -> checkDesign(d, patterns) > 0).count());
        LOGGER.info(() -> "Part 2: " + designs.stream().mapToLong(d -> checkDesign(d, patterns)).sum());
    }

    private static long checkDesign(String design, List<String> patterns) {
        if (design.isEmpty()) {
            return 1;
        }

        if (MEMOIZE.containsKey(design)) {
            return MEMOIZE.get(design);
        }

        long result = patterns.stream()
                .mapToLong(p -> design.startsWith(p) ? checkDesign(design.substring(p.length()), patterns) : 0).sum();

        MEMOIZE.put(design, result);
        return result;
    }
}
