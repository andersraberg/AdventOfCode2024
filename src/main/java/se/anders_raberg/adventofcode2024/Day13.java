package se.anders_raberg.adventofcode2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import com.google.common.math.DoubleMath;

public class Day13 {
    private static final Logger LOGGER = Logger.getLogger(Day13.class.getName());
    private static final long OFFSET = 10_000_000_000_000L;
    private static final Pattern PATTERN = Pattern.compile( //
            "Button A\\: X\\+(\\d+), Y\\+(\\d+)\\s*" + //
                    "Button B\\: X\\+(\\d+), Y\\+(\\d+)\\s*" + //
                    "Prize\\: X=(\\d+), Y=(\\d+)",
            Pattern.DOTALL);

    private Day13() {
    }

    public static void run() throws IOException {
        String[] data = new String(Files.readAllBytes(Paths.get("inputs/input13.txt"))).split("\n\n");

        LOGGER.info(() -> "Part 1: " + sumTokens(data, m -> calcTokens(m, 0)));
        LOGGER.info(() -> "Part 2: " + sumTokens(data, m -> calcTokens(m, OFFSET)));
    }

    private static long sumTokens(String[] data, Function<String, Optional<Long>> fun) {
        return Arrays.stream(data).map(fun::apply).mapToLong(o -> o.orElse(0L)).sum();
    }

    private static Optional<Long> calcTokens(String machine, long priceOffset) {
        Matcher m = PATTERN.matcher(machine);
        if (m.find()) {
            double[][] coefficients = { //
                    { Double.parseDouble(m.group(1)), Double.parseDouble(m.group(3)) }, //
                    { Double.parseDouble(m.group(2)), Double.parseDouble(m.group(4)) } };

            RealMatrix matrix = new Array2DRowRealMatrix(coefficients);

            double[] constants = { priceOffset + Double.parseDouble(m.group(5)),
                    priceOffset + Double.parseDouble(m.group(6)) };

            RealVector constantsVector = new ArrayRealVector(constants);

            DecompositionSolver solver = new LUDecomposition(matrix).getSolver();
            RealVector solution = solver.solve(constantsVector);

            double aPresses = solution.getEntry(0);
            double bPresses = solution.getEntry(1);
            if (properButtonPresses(aPresses) && properButtonPresses(bPresses)) {
                return Optional.of(Math.round(aPresses * 3 + Math.round(bPresses)));
            }
        }
        return Optional.empty();

    }

    private static boolean properButtonPresses(double val) {
        return DoubleMath.fuzzyEquals(val, Math.round(val), 0.001);
    }

}
