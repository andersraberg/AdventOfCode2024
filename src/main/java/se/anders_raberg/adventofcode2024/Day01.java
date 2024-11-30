package se.anders_raberg.adventofcode2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

public class Day01 {
	private static final Logger LOGGER = Logger.getLogger(Day01.class.getName());

	private Day01() {
	}

	public static void run() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("inputs/input1.txt"));
		LOGGER.info(() -> "Part 1: " + lines);
	}

}