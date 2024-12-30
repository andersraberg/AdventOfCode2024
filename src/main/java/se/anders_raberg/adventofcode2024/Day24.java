package se.anders_raberg.adventofcode2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BinaryOperator;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day24 {
    private static final Logger LOGGER = Logger.getLogger(Day24.class.getName());
    private static final Pattern WIRE_PATTERN = Pattern.compile("(\\w+)\\: (\\d)");
    private static final Pattern GATE_PATTERN = Pattern.compile("(\\w+) (\\w++) (\\w+) -> (\\w+)");

    private enum GateKind {
        AND((a, b) -> a & b), //
        OR((a, b) -> a | b), //
        XOR((a, b) -> a ^ b);

        private final BinaryOperator<Integer> _operator;

        GateKind(BinaryOperator<Integer> operator) {
            _operator = operator;
        }

        public BinaryOperator<Integer> operator() {
            return _operator;
        }
    }

    private record Gate(String inA, GateKind kind, String inB, String out) {
    }

    private Day24() {
    }

    public static void run() throws IOException {
        String[] input = new String(Files.readAllBytes(Paths.get("inputs/input24.txt"))).split("\n\n");

        Map<String, Integer> wires = Arrays.stream(input[0].split("\n")) //
                .map(WIRE_PATTERN::matcher) //
                .filter(Matcher::matches) //
                .collect(Collectors.toMap(m -> m.group(1), m -> Integer.parseInt(m.group(2))));

        List<Gate> gates = new ArrayList<>(Arrays.stream(input[1].split("\n")) //
                .map(GATE_PATTERN::matcher) //
                .filter(Matcher::matches) //
                .map(m -> new Gate(m.group(1), GateKind.valueOf(m.group(2)), m.group(3), m.group(4))).toList());

        LOGGER.info(() -> "Part 1: " + calcValue(getWireEndState(wires, gates), "z"));
    }

    private static Map<String, Integer> getWireEndState(Map<String, Integer> wires, List<Gate> gates) {
        List<Gate> tmpGates = new ArrayList<>(gates);
        Map<String, Integer> tmpWires = new HashMap<>(wires);
        while (!tmpGates.isEmpty()) {
            Gate gate = tmpGates.removeFirst();
            Integer inA = tmpWires.get(gate.inA);
            Integer inB = tmpWires.get(gate.inB);
            if (inA != null && inB != null) {
                tmpWires.put(gate.out, gate.kind.operator().apply(inA, inB));
            } else {
                tmpGates.addLast(gate);
            }
        }
        return tmpWires;
    }

    private static long calcValue(Map<String, Integer> wires, String prefix) {
        return Long.parseLong(wires.entrySet().stream() //
                .filter(e -> e.getKey().startsWith(prefix)) //
                .sorted((Map.Entry.<String, Integer>comparingByKey()).reversed()) //
                .map(Entry::getValue) //
                .map(Object::toString) //
                .reduce("", String::concat), 2);
    }
}
