package com.gans.sudoku.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import com.gans.sudoku.util.Sets;

public class Board {

    private final int n;

    /** possible values for point */
    // if possible values are narrowed to single number -- that's our solution for this point
    private final Map<Cell, Set<Integer>> values = new LinkedHashMap<>();

    public Board(int n) {
        this.n = n;
        for (int x = 0; x < n * n; x++) {
            for (int y = 0; y < n * n; y++) {
                values.put(new Cell(x, y), Sets.range(1, n * n));
            }
        }
    }

    public void solve() {
        if (isSolved()) {
            return;
        }

        for (Entry<Cell, Set<Integer>> entry : values.entrySet()) {
            Cell cell = entry.getKey();
            Set<Integer> possibleValues = entry.getValue();
            if (possibleValues.size() == 1) {
                continue;
            }

            for (int value : possibleValues) {
                try {
                    Board board = this.copy();
                    board.set(cell, value);
                } catch (IllegalStateException e) {
                    continue;
                }
                set(cell, value);
                break;
            }
            break;
        }
    }

    private Board copy() { // this copy() is redonkulous
        Board board = new Board(n);
        for (Entry<Cell, Set<Integer>> entry : values.entrySet()) {
            Cell cell = entry.getKey();
            Set<Integer> possibleValues = entry.getValue();
            if (possibleValues.size() == 1) {
                board.set(cell, possibleValues.iterator().next());
            }
        }
        return board;
    }

    private boolean isSolved() {
        for (Entry<Cell, Set<Integer>> entry : values.entrySet()) {
            if (entry.getValue().size() > 1) {
                return false;
            }
        }
        return true;
    }

    public void set(Cell cell, int value) {
        Objects.requireNonNull(cell);
        ensureInRange(cell, 0, n * n - 1);
        values.put(cell, Sets.of(value));
        while (computePossibleValues()) {}
    }

    private boolean computePossibleValues() {
        int before = values.hashCode();

        Map<Cell, Set<Integer>> clusterValueMap = clusterValueMap();

        for (Entry<Cell, Set<Integer>> entry : values.entrySet()) {
            Cell cell = entry.getKey();
            Set<Integer> possibleValues = entry.getValue();
            if (possibleValues.size() == 1) { // we have our solution
                continue;
            }

            Set<Integer> clusterValues = clusterValueMap.get(cell);
            Set<Integer> columnValues = columnValues(cell);
            Set<Integer> rowValues = rowValues(cell);

            possibleValues.removeAll(clusterValues);
            possibleValues.removeAll(columnValues);
            possibleValues.removeAll(rowValues);

            if (possibleValues.isEmpty()) {
                throw new IllegalStateException("Inconsistent state: " + this);
            }
        }

        for (Entry<Cell, Set<Integer>> entry : values.entrySet()) {
            Cell cell = entry.getKey();
            Set<Integer> possibleValues = entry.getValue();
            if (possibleValues.size() == 1) { // we have our solution
                continue;
            }

            Set<Set<Integer>> neighborCellValues = new HashSet<>(); // possible values from neighbor cells of the same cluster
            int clusterX = cell.x() / n;
            int clusterY = cell.y() / n;
            for (int cellX = 0; cellX < n; cellX++) {
                for (int cellY = 0; cellY < n; cellY++) {
                    int x = clusterX * n + cellX;
                    int y = clusterY * n + cellY;
                    Cell neighbor = new Cell(x, y);
                    if (cell.equals(neighbor)) {
                        continue; // exclude self
                    }
                    Set<Integer> neighbors = values.get(neighbor);
                    neighborCellValues.add(neighbors);
                }
            }

            Integer uniqueValue = null;
            for (Integer value : possibleValues) {
                boolean isUniqueValue = true;
                for (Set<Integer> neighbors : neighborCellValues) {
                    if (neighbors.contains(value)) {
                        isUniqueValue = false;
                    }
                }
                if (isUniqueValue) {
                    uniqueValue = value;
                    break;
                }
            }

            if (uniqueValue != null) {
                possibleValues.clear();
                possibleValues.add(uniqueValue);
            }
        }

        int after = values.hashCode();
        return before != after; // repeat if algorithm made some change
    }

    // cluster -- square of n * n points
    private Map<Cell, Set<Integer>> clusterValueMap() {
        Map<Cell, Set<Integer>> clusterValueMap = new HashMap<>();
        for (int clusterX = 0; clusterX < n; clusterX++) {
            for (int clusterY = 0; clusterY < n; clusterY++) {
                Set<Integer> clusterValues = new HashSet<>();
                for (int cellX = 0; cellX < n; cellX++) {
                    for (int cellY = 0; cellY < n; cellY++) {
                        int x = clusterX * n + cellX;
                        int y = clusterY * n + cellY;
                        Cell cell = new Cell(x, y);
                        clusterValueMap.put(cell, clusterValues); // all n*n cells of single cluster will share the same map
                        Set<Integer> cellValues = values.get(cell);
                        if (cellValues.size() == 1) {
                            boolean status = clusterValues.addAll(cellValues);
                            ensureSuccess("Invalid cluster state", status, cell);
                        }
                    }
                }
            }
        }
        return clusterValueMap;
    }

    private Set<Integer> rowValues(Cell cell) {
        return projectionValues("row", i -> new Cell(i, cell.y()));
    }

    private Set<Integer> columnValues(Cell cell) {
        return projectionValues("column", i -> new Cell(cell.x(), i));
    }

    private Set<Integer> projectionValues(String projection, Function<Integer, Cell> cellFunction) {
        Set<Integer> projectionValues = new HashSet<>();
        for (int i = 0; i < n * n; i++) {
            Cell cell = cellFunction.apply(i);
            Set<Integer> cellValues = values.get(cell);
            if (cellValues.size() == 1) {
                boolean status = projectionValues.addAll(cellValues);
                ensureSuccess(String.format("Invalid %s state", projection), status, cell);
            }
        }
        return projectionValues;
    }

    private void ensureSuccess(String msg, boolean status, Cell cell) {
        if (!status) {
            throw new IllegalStateException(String.format("%s for [%s] in: %s", msg, cell, this));
        }
    }

    private static void ensureInRange(Cell cell, int low, int high) {
        ensureInRange(cell.x(), low, high);
        ensureInRange(cell.y(), low, high);
    }

    private static void ensureInRange(int val, int low, int high) {
        if (val < low || val > high) {
            throw new IllegalArgumentException(String.format("%s value is out of range [%s, %s]", val, low, high));
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int y = 0; y < n * n; y++) {
            for (int x = 0; x < n * n; x++) {
                Set<Integer> set = values.get(new Cell(x, y));
                String val = set.size() == 1 ? set.iterator().next() + "" : "-";
                builder.append(val).append(" ");
                if ((x + 1) % n == 0) {
                    builder.append(" ");
                }
            }
            builder.append("\n");
            if ((y + 1) % n == 0) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

}
