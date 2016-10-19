package com.gans.sudoku.util;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Sets {

    private Sets() { }

    public static Set<Integer> range(int from, int to) {
        return IntStream.rangeClosed(from, to).boxed().collect(Collectors.toSet());
    }

    public static Set<Integer> of(int... values) {
        Set<Integer> set = new HashSet<>();
        for (int value : values) {
            set.add(value);
        }
        return set;
    }
}
