package se.anders_raberg.adventofcode2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import se.anders_raberg.adventofcode2024.utilities.Pair;

public class Day18 {
    private static final Logger LOGGER = Logger.getLogger(Day18.class.getName());
    private static final Map<Pos, String> GRID = new HashMap<>();
    private static final int WIDTH = 71;
    private static final int HEIGHT = 71;
    private static final int NO_OF_BYTES = 1024;

    public record Pos(int x, int y) {
        public List<Pos> neighbors() {
            return List.of(new Pos(x + 1, y), new Pos(x, y + 1), new Pos(x - 1, y), new Pos(x, y - 1));
        }
    }

    private Day18() {
    }

    public static void run() throws IOException {
        List<Pos> positions = Files.readAllLines(Paths.get("inputs/input18.txt")).stream() //
                .map(Day18::parsePos) //
                .toList();

        for (int i = 0; i < NO_OF_BYTES; i++) {
            GRID.put(positions.get(i), "#");
        }
        LOGGER.info("Part 1 : " + findPath(GRID).getFirst().second());

        for (int i = NO_OF_BYTES; i < positions.size(); i++) {
            Pos pos = positions.get(i);
            GRID.put(pos, "#");
            if (findPath(GRID).isEmpty()) {
                LOGGER.info(() -> "Part 2: " + pos);
                break;
            }
        }

    }

    private static List<Pair<Pos, Integer>> findPath(Map<Pos, String> grid) {
        List<Pair<Pos, Integer>> ppp = new ArrayList<>(List.of(new Pair<>(new Pos(0, 0), 0)));
        Set<Pos> visited = new HashSet<>();
        while (!ppp.isEmpty()) {
            Pair<Pos, Integer> removeFirst = ppp.removeFirst();
            if (removeFirst.first().equals(new Pos(WIDTH - 1, HEIGHT - 1))) {
                break;
            }

            if (visited.add(removeFirst.first())) {
                removeFirst.first().neighbors().stream()//
                        .filter(Day18::inside) //
                        .filter(p -> !"#".equals(grid.get(p))) //
                        .forEach(p -> ppp.addLast(new Pair<>(p, removeFirst.second() + 1)));
            }
        }

        return ppp;
    }

    private static Pos parsePos(String l) {
        String[] split = l.split(",");
        return new Pos(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }

    private static boolean inside(Pos pos) {
        return pos.x < WIDTH && pos.x >= 0 && pos.y < HEIGHT && pos.y >= 0;
    }
}
