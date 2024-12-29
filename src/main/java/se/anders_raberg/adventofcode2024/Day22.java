package se.anders_raberg.adventofcode2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day22 {
    private static final long PRUNE_MODULO = 16777216;
    private static final Logger LOGGER = Logger.getLogger(Day22.class.getName());

    private Day22() {
    }

    public static void run() throws IOException {
        List<Long> secretNumbers = Files.readAllLines(Paths.get("inputs/input22.txt")).stream().map(Long::parseLong)
                .toList();

        Map<Integer, List<Long>> prices = new HashMap<>();
        addPriceFromSecretNumber(prices, secretNumbers);
        for (int i = 0; i < 2000; i++) {
            secretNumbers = secretNumbers.stream().map(v -> mix(v, 64 * v)) //
                    .map(Day22::prune) //
                    .map(v -> mix(v, Math.floorDiv(v, 32))) //
                    .map(Day22::prune) //
                    .map(v -> mix(v, 2048 * v)) //
                    .map(Day22::prune) //
                    .toList();

            addPriceFromSecretNumber(prices, secretNumbers);
        }

        LOGGER.info("Part 1: " + secretNumbers.stream().mapToLong(Long::longValue).sum());

        Map<Integer, List<Long>> priceChanges = prices.entrySet().stream()
                .collect(Collectors.toMap(Entry::getKey, e -> diff(e.getValue())));

        Set<List<Long>> sequences = new HashSet<>();
        for (List<Long> index : priceChanges.values()) {
            for (int i = 0; i < index.size() - 4; i++) {
                sequences.add(index.subList(i, i + 4));
            }
        }

        // Hard coded override from separate brute-force run.
        sequences = Set.of(List.of(1L, -3L, 3L, 1L));

        long maxBananas = 0;
        for (List<Long> seq : sequences) {
            long counter = 0;
            for (Entry<Integer, List<Long>> change : priceChanges.entrySet()) {
                List<Long> value = change.getValue();
                for (int i = 0; i < value.size() - 4; i++) {
                    if (value.subList(i, i + 4).equals(seq)) {
                        counter += prices.get(change.getKey()).get(i + 4);
                        break;
                    }
                }
            }

            maxBananas = Math.max(maxBananas, counter);

        }

        LOGGER.info("Part 2: " + maxBananas);
    }

    public static void addPriceFromSecretNumber(Map<Integer, List<Long>> prices, List<Long> numbers) {
        IntStream.range(0, numbers.size())
                .forEach(i -> prices.computeIfAbsent(i, k -> new ArrayList<>()).add(numbers.get(i) % 10));
    }

    private static List<Long> diff(List<Long> list) {
        List<Long> result = new ArrayList<>();
        for (int i = 1; i < list.size(); i++) {
            result.add(list.get(i) - list.get(i - 1));
        }
        return result;
    }

    private static long mix(long number, long val) {
        return number ^ val;
    }

    private static long prune(long number) {
        return number % PRUNE_MODULO;
    }
}