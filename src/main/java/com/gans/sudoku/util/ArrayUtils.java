package com.gans.sudoku.util;

import java.util.function.Supplier;

public final class ArrayUtils {

    private ArrayUtils() {}

    public static <T> void fill(T[][] array, Supplier<T> supplier) {
        for (int i = 0; i < array.length; i++) {
            for (int y = 0; y < array[i].length; y++) {
                array[i][y] = supplier.get();
            }
        }
    }
}
