/**
 * BinaryTreeMazeGenerator.java
 * By Sebastian Raaphorst, 2025.
 */

package org.vorpal.maze;

import java.awt.*;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * A simple test to produce a simple Maze for testing purposes.
 */
final public class BinaryTreeMazeGenerator implements MazeGenerator {
    @Override
    public Maze generate(int rows, int columns) {
        final Random rnd = new Random();
        final Maze maze = new Maze(rows, columns);

        IntStream.range(0, rows).forEach(r -> {
                    final boolean canCarveSouth = r < rows - 1;
                    IntStream.range(0, columns).forEach(c -> {
                        // Do not carve out the bottom-right corner.
                        final boolean canCarveEast = c < columns - 1;
                        final Point cell = new Point(r, c);
                        if (canCarveEast || canCarveSouth) {
                            if (canCarveEast && canCarveSouth)
                                if (rnd.nextBoolean())
                                    maze.carveWall(cell, Maze.Direction.EAST);
                                else
                                    maze.carveWall(cell, Maze.Direction.SOUTH);
                            else if (canCarveEast)
                                maze.carveWall(cell, Maze.Direction.EAST);
                            else // canCarveSouth
                                maze.carveWall(cell, Maze.Direction.SOUTH);
                        }
                    });
                }
        );
        return maze;
    }
}
