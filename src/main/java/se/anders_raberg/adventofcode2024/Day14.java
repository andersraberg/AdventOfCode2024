package se.anders_raberg.adventofcode2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14 {
    private static final Logger LOGGER = Logger.getLogger(Day14.class.getName());
    private static final Pattern ROBOT_PATTERN = Pattern.compile("p=(\\-?\\d+),(\\-?\\d+) v=(\\-?\\d+),(\\-?\\d+)");
    private static final int WIDTH = 101;
    private static final int HEIGHT = 103;

    private Day14() {
    }

    private record Pos(int x, int y) {
    }

    private record Vel(int vx, int vy) {
    }

    private static class Robot {
        private Pos _pos;
        private Vel _vel;

        public Robot(Pos pos, Vel vel) {
            _pos = pos;
            _vel = vel;
        }

        public static Robot parseRobot(String str) {
            Matcher m = ROBOT_PATTERN.matcher(str);
            if (m.matches()) {
                return new Robot( //
                        new Pos(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2))),
                        new Vel(Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4))));
            } else {
                throw new IllegalArgumentException(str);
            }
        }

        public void move() {
            _pos = new Pos(mod(_pos.x + _vel.vx, WIDTH), mod(_pos.y + _vel.vy, HEIGHT));
        }

        public Pos getPos() {
            return _pos;
        }

        private static int mod(int a, int n) {
            return ((a % n) + n) % n;
        }
    }

    public static void run() throws IOException {
        List<Robot> robots = Files.readAllLines(Paths.get("inputs/input14.txt")).stream() //
                .map(Robot::parseRobot) //
                .toList();

        for (int i = 0; i < 100; i++) {
            for (Robot robot : robots) {
                robot.move();
            }
        }

        int midW = WIDTH / 2;
        int midH = HEIGHT / 2;
        long count1 = robots.stream().map(Robot::getPos).filter(p -> p.x() < midW).filter(p -> p.y() < midH).count();
        long count2 = robots.stream().map(Robot::getPos).filter(p -> p.x() > midW).filter(p -> p.y() < midH).count();
        long count3 = robots.stream().map(Robot::getPos).filter(p -> p.x() < midW).filter(p -> p.y() > midH).count();
        long count4 = robots.stream().map(Robot::getPos).filter(p -> p.x() > midW).filter(p -> p.y() > midH).count();

        LOGGER.info(() -> "Part 1: " + count1 * count2 * count3 * count4);

        int count = 101;
        while (true) {
            for (Robot robot : robots) {
                robot.move();
            }
            if (allInUniquePlaces(robots)) {
                break;
            }
            count++;
        }
        LOGGER.info("Part 2: " + count);
    }

    private static boolean allInUniquePlaces(List<Robot> robots) {
        return robots.stream().map(Robot::getPos).distinct().count() == robots.size();
    }
}
