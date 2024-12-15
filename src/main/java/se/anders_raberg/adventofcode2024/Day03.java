package se.anders_raberg.adventofcode2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class Day03 {
    private static final Logger LOGGER = Logger.getLogger(Day03.class.getName());
    private static final Pattern MUL_PATTERN = Pattern.compile("mul\\((\\d+)\\,(\\d+)\\)", Pattern.DOTALL);
    private static final Pattern DO_DO_NOT_PATTERN = Pattern.compile("do\\(\\)(.*?)don't\\(\\)", Pattern.DOTALL);

    private Day03() {
    }

    public static void run() throws IOException {
        String input = new String(Files.readAllBytes(Paths.get("inputs/input3.txt"))).trim();
        LOGGER.info(() -> "Part 1: " + sumOfMuls(input));

        String inputDoToDont = DO_DO_NOT_PATTERN.matcher("do()" + input + "don't()").results().map(m -> m.group(1))
                .reduce("", String::concat);

        LOGGER.info(() -> "Part 2: " + sumOfMuls(inputDoToDont));
    }

    private static int sumOfMuls(String input) {
        return MUL_PATTERN.matcher(input).results()
                .mapToInt(m -> Integer.parseInt(m.group(1)) * Integer.parseInt(m.group(2))).sum();
    }
}
