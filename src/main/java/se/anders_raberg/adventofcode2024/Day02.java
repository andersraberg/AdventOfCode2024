package se.anders_raberg.adventofcode2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import com.google.common.collect.Range;

public class Day02 {
    private static final Logger LOGGER = Logger.getLogger(Day02.class.getName());
    private static final Range<Integer> INCREASE_RANGE = Range.closed(1, 3);
    private static final Range<Integer> DECREASE_RANGE = Range.closed(-3, -1);

    private Day02() {
    }

    public static void run() throws IOException {
        List<List<Integer>> reports = Files.readAllLines(Paths.get("inputs/input2.txt")).stream().map(Day02::parse)
                .toList();

        LOGGER.info(() -> "Part 1: " + reports.stream().filter(Day02::safe).count());
        LOGGER.info(() -> "Part 2: " + reports.stream().filter(Day02::safeWDampener).count());
    }

    private static List<Integer> parse(String report) {
        return Arrays.stream(report.split("\\W+")).map(Integer::parseInt).toList();
    }

    private static boolean safe(List<Integer> report) {
        List<Integer> differences = IntStream.range(1, report.size()).map(i -> report.get(i) - report.get(i - 1))
                .boxed().toList();

        return differences.stream().allMatch(DECREASE_RANGE::contains)
                || differences.stream().allMatch(INCREASE_RANGE::contains);
    }

    private static boolean safeWDampener(List<Integer> report) {
        return safe(report) || genLists(report).stream().anyMatch(Day02::safe);
    }

    private static List<List<Integer>> genLists(List<Integer> report) {
        return IntStream.range(0, report.size()).boxed().map(i -> sublist(report, i)).toList();
    }

    private static List<Integer> sublist(List<Integer> list, int index) {
        List<Integer> result = new LinkedList<>(list);
        result.remove(index);
        return result;
    }

}