package se.anders_raberg.adventofcode2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import se.anders_raberg.adventofcode2024.utilities.Pair;

public class Day06 {
    private static final Logger LOGGER = Logger.getLogger(Day06.class.getName());

    private static final Map<Pos, String> GRID = new HashMap<>();

    private record Pos(int x, int y) {
        public Pos step(Direction dir) {
            return new Pos(x + dir.getdX(), y + dir.getdY());
        }
    }

    private Day06() {
    }

    private enum Direction {
        UP(0, -1), DOWN(0, 1), RIGHT(1, 0), LEFT(-1, 0);

        private final int _dX;
        private final int _dY;

        Direction(int dX, int dY) {
            _dX = dX;
            _dY = dY;
        }

        public int getdX() {
            return _dX;
        }

        public int getdY() {
            return _dY;
        }

        public Direction turnRight() {
            return switch (this) {
            case UP -> RIGHT;
            case RIGHT -> DOWN;
            case DOWN -> LEFT;
            case LEFT -> UP;
            default -> throw new IllegalArgumentException("Unexpected direction: " + this);
            };
        }
    }

    public static void run() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/input6.txt"));
        for (int y = 0; y < lines.size(); y++) {
            String[] row = lines.get(y).trim().split("");
            for (int x = 0; x < row.length; x++) {
                GRID.put(new Pos(x, y), row[x]);
            }
        }

        Pos startPos = GRID.entrySet().stream().filter(e -> e.getValue().equals("^")).findFirst().orElseThrow()
                .getKey();

        LOGGER.info(() -> "Part 1: " + visitedCount(startPos).orElseThrow());

        int loopCounter = 0;
        for (Pos blockedPos : GRID.entrySet().stream().filter(e -> e.getValue().equals(".")).map(Entry::getKey)
                .toList()) {
            String saved = GRID.put(blockedPos, "#");
            if (visitedCount(startPos).isEmpty()) {
                loopCounter++;
            }
            GRID.put(blockedPos, saved);
        }

        LOGGER.info("Part 2: " + loopCounter);

    }

    private static Optional<Long> visitedCount(Pos startPos) {
        Pos currPos = startPos;
        Direction currDir = Direction.UP;
        Set<Pair<Pos, Direction>> visited = new HashSet<>();
        while (GRID.containsKey(currPos)) {
            if (visited.contains(new Pair<>(currPos, currDir))) {
                return Optional.empty();
            }
            if (!"#".equals(GRID.get(currPos.step(currDir)))) {
                visited.add(new Pair<>(currPos, currDir));
                currPos = currPos.step(currDir);
            } else {
                currDir = currDir.turnRight();
            }
        }
        return Optional.of(visited.stream().map(Pair::first).distinct().count());
    }
}
