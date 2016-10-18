package com.gans.sudoku.model;

import com.gans.sudoku.util.ArrayUtils;

public class Square {

    private final Cell[][] cells;

    public Square() {
        this.cells = new Cell[3][3];
        ArrayUtils.fill(cells, Cell::new);
    }
}
