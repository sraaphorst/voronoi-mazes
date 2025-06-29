/**
 * Maze.java
 * By Sebastian Raaphorst, 2025.
 */

package org.vorpal.maze;

import java.awt.*;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Maze {
    public enum Direction {
        NORTH, EAST, SOUTH, WEST
    }

    /**
     * Given a cell and a direction, return the neighbouring cell.
     * @param cell the cell whose neighbour we want
     * @param direction the direction
     * @return the neighbouring Point coordinates
     */
    public static Point neighbour(Point cell, Direction direction) {
        return switch (direction) {
            case NORTH -> new Point(cell.x-1, cell.y);
            case EAST  -> new Point(cell.x, cell.y+1);
            case SOUTH -> new Point(cell.x+1, cell.y);
            case WEST  -> new Point(cell.x, cell.y-1);
        };
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

        IntStream.range(0, rows).forEach(r -> {
            Arrays.fill(eastWall[r], true);
            Arrays.fill(southWall[r], true);
        });
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setWall(Point cell, Direction direction, boolean state) {
        boolean isBorder =
                (cell.x == 0 && direction == Direction.NORTH) ||
                (cell.x == rows - 1 && direction == Direction.SOUTH) ||
                (cell.y == 0 && direction == Direction.WEST) ||
                (cell.y == columns - 1 && direction == Direction.EAST);

        // Forbid erasing a permanent border wall:
        if (isBorder && !state)
            throw new IllegalArgumentException("Cannot erase a border wall.");

        switch (direction) {
            case NORTH -> southWall[cell.x - 1][cell.y] = state;
            case EAST  -> eastWall[cell.x][cell.y]      = state;
            case SOUTH -> southWall[cell.x][cell.y]     = state;
            case WEST  -> eastWall[cell.x][cell.y - 1]  = state;
        }
    }

    public void carveWall(Point cell, Direction direction) {
        setWall(cell, direction, false);
    }

    public void fillWall(Point cell, Direction direction) {
        setWall(cell, direction, true);
    }

    public boolean hasWall(Point cell, Direction direction) {
        return switch (direction) {
            case NORTH -> cell.x == 0 || southWall[cell.x-1][cell.y];
            case EAST -> eastWall[cell.x][cell.y];
            case SOUTH -> southWall[cell.x][cell.y];
            case WEST -> cell.y == 0 || eastWall[cell.x][cell.y-1];
        };
    }
}
