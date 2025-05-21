/**
 * BinaryTreeMazeGenerator.java
 * By Sebastian Raaphorst, 2025.
 */

package org.vorpal.maze;

import org.vorpal.math.Metric2D;

import java.util.Random;

/**
 * A simple test to produce an simple Maze for testing purposes.
 */
final public class BinaryTreeMazeGenerator implements MazeGenerator {
    @Override
    public Maze generate(int rows, int columns, Metric2D metric) {
        final Random rnd = new Random();
        final Maze maze = new Maze(rows, columns);

        for (int r = 0; r < rows; ++r) {
            for (int c = 0; c < columns; ++c) {
                // Do not carve out the bottom-right corner.
                if (r == rows - 1 && c == columns - 1) continue;

                final boolean canCarveEast  = (c < columns - 1);
                final boolean canCarveSouth = (r < rows - 1);

                if (canCarveEast && canCarveSouth)
                    if (rnd.nextBoolean())
                        maze.carveWall(r, c, Maze.Direction.EAST);
                    else
                        maze.carveWall(r, c, Maze.Direction.SOUTH);
                else if (canCarveEast)
                    maze.carveWall(r, c, Maze.Direction.EAST);
                else if (canCarveSouth)
                    maze.carveWall(r, c, Maze.Direction.SOUTH);
            }
        }
        return maze;
    }
}
