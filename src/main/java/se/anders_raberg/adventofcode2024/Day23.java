package se.anders_raberg.adventofcode2024;

import static com.google.common.collect.Sets.intersection;
import static com.google.common.collect.Sets.union;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;

import se.anders_raberg.adventofcode2024.utilities.Pair;

public class Day23 {
    // Solved using the Bron-Kerbosch algorithm.
    // https://en.wikipedia.org/wiki/Bron-Kerbosch_algorithm
    //
    private static final Logger LOGGER = Logger.getLogger(Day23.class.getName());
    private static final Pattern PATTERN = Pattern.compile("(\\w\\w)-(\\w\\w)");

    private Day23() {
    }

    public static void run() throws IOException {
        List<Pair<String, String>> list = Files.readAllLines(Paths.get("inputs/input23.txt")).stream()
                .map(Day23::parseConnection) //
                .toList();

        ImmutableGraph.Builder<String> graphBuilder = //
                GraphBuilder.undirected() //
                        .immutable();

        list.forEach(l -> graphBuilder.putEdge(l.first(), l.second()));

        ImmutableGraph<String> graph = graphBuilder.build();

        LOGGER.info(() -> "Part 1: " + findCliquesOfSize(graph, 3).stream()
                .filter(c -> c.stream().anyMatch(n -> n.startsWith("t"))).count());

        LOGGER.info(() -> "Part 2: "
                + findLargestClique(graph).stream().sorted().reduce("", (a, b) -> a + ',' + b).substring(1));
    }

    private static Pair<String, String> parseConnection(String str) {
        Matcher m = PATTERN.matcher(str);
        if (m.matches()) {
            return new Pair<>(m.group(1), m.group(2));
        }
        throw new IllegalArgumentException(str);
    }

    public static Set<String> findLargestClique(Graph<String> graph) {
        Set<String> maxClique = new HashSet<>();
        bronKerbosch(graph, new HashSet<>(), new HashSet<>(graph.nodes()), new HashSet<>(), maxClique);
        return maxClique;
    }

    public static Set<Set<String>> findCliquesOfSize(Graph<String> graph, int k) {
        Set<Set<String>> cliques = new HashSet<>();
        bronKerbosch(graph, new HashSet<>(), new HashSet<>(graph.nodes()), new HashSet<>(), k, cliques);
        return cliques;
    }

    private static void bronKerbosch(Graph<String> graph, Set<String> currentClique, Set<String> potentialCandidates,
            Set<String> processed, Set<String> maxClique) {
        if (union(potentialCandidates, processed).isEmpty()) {
            if (currentClique.size() > maxClique.size()) {
                maxClique.clear();
                maxClique.addAll(currentClique);
            }
            return;
        }

        for (String cand : new HashSet<>(potentialCandidates)) {
            Set<String> neighbors = graph.adjacentNodes(cand);
            bronKerbosch(graph, union(currentClique, Set.of(cand)),
                    new HashSet<>(intersection(potentialCandidates, neighbors)),
                    new HashSet<>(intersection(processed, neighbors)), //
                    maxClique);

            potentialCandidates.remove(cand);
            processed.add(cand);
        }
    }

    private static void bronKerbosch(Graph<String> graph, Set<String> currentClique, Set<String> potentialCandidates,
            Set<String> processed, int requestedSize, Set<Set<String>> cliques) {
        if (currentClique.size() == requestedSize) {
            cliques.add(new HashSet<>(currentClique));
            return;
        }

        if (potentialCandidates.isEmpty() || currentClique.size() > requestedSize) {
            return;
        }

        for (String cand : new HashSet<>(potentialCandidates)) {
            Set<String> neighbors = graph.adjacentNodes(cand);

            bronKerbosch(graph, union(currentClique, Set.of(cand)),
                    new HashSet<>(intersection(potentialCandidates, neighbors)),
                    new HashSet<>(intersection(processed, neighbors)), //
                    requestedSize, //
                    cliques);

            potentialCandidates.remove(cand);
            processed.add(cand);
        }
    }
}
