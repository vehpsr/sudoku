package com.gans.sudoku.model;

import com.gans.sudoku.util.ArrayUtils;

public class Area {

    private final Square[][] squares;

    public Area() {
        this.squares = new Square[3][3];
        ArrayUtils.fill(squares, Square::new);
    }
}
