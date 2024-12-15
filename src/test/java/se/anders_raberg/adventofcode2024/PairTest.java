package se.anders_raberg.adventofcode2024;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import se.anders_raberg.adventofcode2024.utilities.Pair;

class PairTest {

    @Test
    void testPair() {
        Pair<String, Integer> testee = new Pair<>("Hello", 42);
        assertEquals("Hello", testee.first());
        assertEquals((Integer) 42, testee.second());
        assertEquals("Pair[first=Hello, second=42]", testee.toString());
        assertEquals(new Pair<>("Hello", 42), testee);
        assertNotEquals(null, testee);
    }

}
