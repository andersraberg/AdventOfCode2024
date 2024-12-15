package se.anders_raberg.adventofcode2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class Day09 {
    private static final Logger LOGGER = Logger.getLogger(Day09.class.getName());
    private static final int EMPTY_SPACE_TOKEN = -1;

    private Day09() {
    }

    private record ContiguousBlocks(int start, int end) {
        public int size() {
            return end - start + 1;
        }
    }

    public static void run() throws IOException {
        String[] split = new String(Files.readAllBytes(Paths.get("inputs/input9.txt"))).trim().split("");
        List<Integer> input = Arrays.stream(split).map(Integer::parseInt).toList();

        List<Integer> diskMap = new ArrayList<>();
        int idCounter = 0;
        for (int i = 0; i < input.size(); i++) {
            diskMap.addAll(Collections.nCopies(input.get(i), isEven(i) ? idCounter++ : EMPTY_SPACE_TOKEN));
        }

        // Part 1
        List<Integer> diskMapPart1 = new ArrayList<>(diskMap);
        int index;
        int lastFileBlock;
        while (true) {
            index = diskMapPart1.indexOf(EMPTY_SPACE_TOKEN);
            if (index == EMPTY_SPACE_TOKEN || index >= diskMapPart1.size()) {
                break;
            }
            lastFileBlock = removeLastFileBlock(diskMapPart1);
            diskMapPart1.set(index, lastFileBlock);
        }

        LOGGER.info(() -> "Part 1: " + calcChecksum(diskMapPart1));

        // Part 2
        List<Integer> diskMapPart2 = new ArrayList<>(diskMap);
        List<ContiguousBlocks> fileLocations = findFileLocations(diskMapPart2).reversed();
		for (int i = 0; i < fileLocations.size(); i++) {
			ContiguousBlocks file = fileLocations.get(i);
            Optional<ContiguousBlocks> firstLargeEnough = findFreeSpace(diskMapPart2).stream()
                    .filter(space -> file.size() <= space.size() && space.start < file.start).findFirst();

            firstLargeEnough.ifPresent(f -> moveBlock(file, f, diskMapPart2));
        }

        LOGGER.info(() -> "Part 2: " + calcChecksum(diskMapPart2));
    }

    private static long calcChecksum(List<Integer> diskMap) {
        return IntStream.range(0, diskMap.size()).mapToLong(i -> i * Math.max(0, diskMap.get(i))).sum();
    }

    private static void moveBlock(ContiguousBlocks file, ContiguousBlocks freeSpace, List<Integer> diskMap) {
        for (int i = 0; i < file.size(); i++) {
            diskMap.set(freeSpace.start + i, diskMap.get(file.start + i));
            diskMap.set(file.start + i, EMPTY_SPACE_TOKEN);
        }
    }

    private static List<ContiguousBlocks> findFreeSpace(List<Integer> diskMap) {
        List<ContiguousBlocks> result = new ArrayList<>();
        boolean searchForStart = true;
        int nextStart = 0;
        int nextStop = 0;
        for (int i = 0; i < diskMap.size(); i++) {
            if (searchForStart) {
                if (diskMap.get(i).equals(EMPTY_SPACE_TOKEN)) {
                    nextStart = i;
                    searchForStart = false;
                }
            } else {
                if (!diskMap.get(i).equals(EMPTY_SPACE_TOKEN)) {
                    nextStop = i - 1;
                    searchForStart = true;
                    result.add(new ContiguousBlocks(nextStart, nextStop));
                }
            }
        }

        return result;
    }

    private static List<ContiguousBlocks> findFileLocations(List<Integer> diskMap) {
        List<ContiguousBlocks> result = new ArrayList<>();
        int idCounter = 0;

        while (diskMap.indexOf(idCounter) != -1) {
            result.add(new ContiguousBlocks(diskMap.indexOf(idCounter), diskMap.lastIndexOf(idCounter)));
            idCounter++;
        }
        return result;
    }

    private static int removeLastFileBlock(List<Integer> list) {
        Integer remove;
        Integer last;
        do {
            remove = list.removeLast();
        } while (remove == EMPTY_SPACE_TOKEN);

        do {
            last = list.removeLast();
        } while (last == EMPTY_SPACE_TOKEN);
        list.add(last);
        return remove;
    }

    private static boolean isEven(int val) {
        return ((val % 2) == 0);
    }

}
