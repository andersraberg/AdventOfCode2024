package se.anders_raberg.adventofcode2024.utilities;

@FunctionalInterface
public interface TriFunction<T, U, V, R> {
    R apply(T t, U u, V v);
}
