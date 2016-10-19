package com.gans.sudoku;

import com.gans.sudoku.model.Board;
import com.gans.sudoku.model.Cell;

public class SudokuSolver {

    public static void main(String[] args) {
        Board board = new Board(3);

        // set up
        board.set(new Cell(0, 0), 8);
        board.set(new Cell(1, 2), 1);

        board.set(new Cell(3, 0), 4);
        board.set(new Cell(5, 0), 6);

        board.set(new Cell(8, 0), 7);
        board.set(new Cell(6, 1), 4);
        board.set(new Cell(6, 2), 6);
        board.set(new Cell(7, 2), 5);

        board.set(new Cell(0, 3), 5);
        board.set(new Cell(2, 3), 9);
        board.set(new Cell(1, 5), 4);
        board.set(new Cell(2, 5), 8);

        board.set(new Cell(4, 3), 3);
        board.set(new Cell(4, 4), 7);
        board.set(new Cell(4, 5), 2);

        board.set(new Cell(6, 3), 7);
        board.set(new Cell(7, 3), 8);
        board.set(new Cell(6, 5), 1);
        board.set(new Cell(8, 5), 3);

        board.set(new Cell(1, 6), 5);
        board.set(new Cell(2, 6), 2);
        board.set(new Cell(2, 7), 1);
        board.set(new Cell(0, 8), 3);

        board.set(new Cell(3, 8), 9);
        board.set(new Cell(5, 8), 2);

        board.set(new Cell(7, 6), 9);
        board.set(new Cell(8, 8), 5);

        // solve
        board.solve();

        // print
        System.out.println(board);
    }


}
