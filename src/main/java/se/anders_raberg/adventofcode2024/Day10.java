package se.anders_raberg.adventofcode2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.google.common.collect.Sets;

public class Day10 {
    private static final Logger LOGGER = Logger.getLogger(Day10.class.getName());
    private static final Map<Pos, Integer> GRID = new HashMap<>();
    private static int width;
    private static int height;

    private record Pos(int x, int y) {
        public Pos up() {
            return new Pos(x, y - 1);
        }

        public Pos down() {
            return new Pos(x, y + 1);
        }

        public Pos right() {
            return new Pos(x + 1, y);
        }

        public Pos left() {
            return new Pos(x - 1, y);
        }

        @Override
        public String toString() {
            return String.format("(%s,%s)", x, y);
        }
    }

    private Day10() {
    }

    public static void run() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/input10.txt"));
        width = lines.get(0).length();
        height = lines.size();

        for (int y = 0; y < height; y++) {
            String[] row = lines.get(y).trim().split("");
            for (int x = 0; x < width; x++) {
                GRID.put(new Pos(x, y), Integer.parseInt(row[x]));
            }
        }

        LOGGER.info(() -> "Part 1: " + GRID.entrySet().stream() //
                .filter(e -> e.getValue().equals(0)) //
                .mapToInt(e -> countScore(e.getKey()).size()).sum());

        LOGGER.info(() -> "Part 2: " + GRID.entrySet().stream() //
                .filter(e -> e.getValue().equals(0)) //
                .mapToInt(e -> countRating(e.getKey())).sum());
    }

    private static Set<Pos> countScore(Pos pos) {
        if (GRID.get(pos) == 9) {
            return Set.of(pos);
        } else {
            return List.of(pos.up(), pos.down(), pos.right(), pos.left()).stream() //
                    .filter(Day10::inside) //
                    .filter(p -> GRID.get(p) == GRID.get(pos) + 1) //
                    .map(Day10::countScore).reduce(Collections.emptySet(), Sets::union);
        }
    }

    private static int countRating(Pos pos) {
        if (GRID.get(pos) == 9) {
            return 1;
        } else {
            return List.of(pos.up(), pos.down(), pos.right(), pos.left()).stream() //
                    .filter(Day10::inside) //
                    .filter(p -> GRID.get(p) == GRID.get(pos) + 1) //
                    .mapToInt(Day10::countRating).sum();
        }
    }

    private static boolean inside(Pos pos) {
        return pos.x < width && pos.x >= 0 && pos.y < height && pos.y >= 0;
    }

}