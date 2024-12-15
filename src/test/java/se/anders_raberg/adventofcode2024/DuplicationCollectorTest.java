package se.anders_raberg.adventofcode2024;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import se.anders_raberg.adventofcode2024.utilities.DuplicationCollector;

class DuplicationCollectorTest {

    private static final List<Integer> TESTEE = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

    @Test
    void test1() {
        List<Integer> expected = Arrays.asList(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8);
        List<Integer> actual = TESTEE.stream().collect(new DuplicationCollector<>(2));

        System.out.println(actual);

        assertEquals(expected, actual);
    }

}
