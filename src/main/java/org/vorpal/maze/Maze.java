/**
 * Maze.java
 * By Sebastian Raaphorst, 2025.
 */

package org.vorpal.maze;

import java.util.Arrays;

public class Maze {
    public enum Direction {
        NORTH, EAST, SOUTH, WEST
    }

    private final int rows;
    private final int columns;
    private final boolean[][] eastWall;
    private final boolean[][] southWall;

    public Maze(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        eastWall = new boolean[rows][columns];
        southWall = new boolean[rows][columns];

        for (int r = 0; r < rows; r++) {
            Arrays.fill(eastWall[r], true);
            Arrays.fill(southWall[r], true);
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setWall(int row, int column, Direction direction, boolean state) {
        boolean isBorder =
                (row == 0 && direction == Direction.NORTH) ||
                (row == rows - 1 && direction == Direction.SOUTH) ||
                (column == 0 && direction == Direction.WEST) ||
                (column == columns - 1 && direction == Direction.EAST);

        // Forbid erasing a permanent border wall:
        if (isBorder && !state)
            throw new IllegalArgumentException("Cannot erase a border wall.");

        switch (direction) {
            case NORTH -> southWall[row - 1][column] = state;
            case EAST  -> eastWall[row][column]     = state;
            case SOUTH -> southWall[row][column]    = state;
            case WEST  -> eastWall[row][column - 1] = state;
        }
    }

    public void carveWall(int row, int column, Direction direction) {
        setWall(row, column, direction, false);
    }

    public void fillWall(int row, int column, Direction direction) {
        setWall(row, column, direction, true);
    }

    public boolean hasWall(int row, int column, Direction direction) {
        return switch (direction) {
            case NORTH -> row == 0 || southWall[row-1][column];
            case EAST -> eastWall[row][column];
            case SOUTH -> southWall[row][column];
            case WEST -> column == 0 || eastWall[row][column-1];
        };
    }
}
