package se.anders_raberg.adventofcode2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day07 {
    private static final Logger LOGGER = Logger.getLogger(Day07.class.getName());
    private static final Pattern PATTERN = Pattern.compile("(\\d+)\\:(.+)");

    private Day07() {
    }

    private record Equation(long testValue, List<Long> numbers) {
    }

    public static void run() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/input7.txt"));

        List<Equation> equations = lines.stream() //
                .map(PATTERN::matcher) //
                .filter(Matcher::matches) //
                .map(m -> new Equation(Long.parseLong(m.group(1)), parse(m.group(2)))) //
                .toList();

        LOGGER.info(() -> "Part 1: " + equations.stream().filter(
                e -> checkPart1(e.testValue(), e.numbers().getFirst(), e.numbers().subList(1, e.numbers().size())))
                .mapToLong(e -> e.testValue()).sum());

        LOGGER.info(() -> "Part 2: " + equations.stream().filter(
                e -> checkPart2(e.testValue(), e.numbers().getFirst(), e.numbers().subList(1, e.numbers().size())))
                .mapToLong(e -> e.testValue()).sum());
    }

    private static List<Long> parse(String numbers) {
        return Arrays.stream(numbers.trim().split(" +")).map(Long::parseLong).toList();
    }

    private static boolean checkPart1(long testValue, long accuValue, List<Long> values) {
        if (values.isEmpty()) {
            return accuValue == testValue;
        } else {
            List<Long> remValues = values.subList(1, values.size());
            Long next = values.getFirst();
            return checkPart1(testValue, accuValue + next, remValues)
                    || checkPart1(testValue, accuValue * next, remValues);
        }
    }

    private static boolean checkPart2(long testValue, long accuValue, List<Long> values) {
        if (values.isEmpty()) {
            return accuValue == testValue;
        } else {
            List<Long> remValues = values.subList(1, values.size());
            Long next = values.getFirst();
            return checkPart2(testValue, accuValue + next, remValues)
                    || checkPart2(testValue, accuValue * next, remValues)
                    || checkPart2(testValue, concatenation(accuValue, next), remValues);
        }
    }

    private static Long concatenation(long a, long b) {
        return Long.parseLong(String.format("%d%d", a, b));
    }
}
