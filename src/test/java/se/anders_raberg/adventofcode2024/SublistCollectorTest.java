package se.anders_raberg.adventofcode2024;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import se.anders_raberg.adventofcode2024.utilities.SublistCollector;

class SublistCollectorTest {

    private static final List<Integer> TESTEE = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

    @Test
    void test1() {
        List<List<Integer>> expected = Arrays.asList(Arrays.asList(1, 2), Arrays.asList(3, 4), Arrays.asList(5, 6),
                Arrays.asList(7, 8));
        List<List<Integer>> actual = TESTEE.stream().collect(new SublistCollector<>(2, false));

        assertEquals(expected, actual);
    }

    @Test
    void test2() {
        List<List<Integer>> expected = Arrays.asList(Arrays.asList(1, 2, 3, 4), Arrays.asList(5, 6, 7, 8));
        List<List<Integer>> actual = TESTEE.stream().collect(new SublistCollector<>(4, false));

        assertEquals(expected, actual);
    }

}
