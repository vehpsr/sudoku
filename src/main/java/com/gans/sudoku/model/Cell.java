package com.gans.sudoku.model;

/**
 * (x,y) coordinate wrapper
 */
public class Cell {

    private final int x;
    private final int y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Cell other = (Cell) obj;
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public int hashCode() {
        int h = 0;
        h = 31 * h + x;
        h = 31 * h + y;
        return h;
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", x, y);
    }

}
