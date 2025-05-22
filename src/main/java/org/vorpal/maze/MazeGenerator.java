/**
 * MazeGenerator.java
 * By Sebastian Raaphorst, 2025.
 */

package org.vorpal.maze;

import java.awt.Point;
import java.util.IntSummaryStatistics;

@FunctionalInterface
public interface MazeGenerator {
    Maze generate(int rows, int columns);

    /**
     * Lift this MazeGenerator into a RegionMazeGenerator.
     * @return a RegionMazeGenerator that will generate a maze only over the given cells
     */
    default RegionMazeGenerator adapt() {
        return (maze, cells) -> {
            final IntSummaryStatistics rows = cells.stream().mapToInt(p -> p.x).summaryStatistics();
            final IntSummaryStatistics columns = cells.stream().mapToInt(p -> p.y).summaryStatistics();
            final int minR = rows.getMin();
            final int maxR = rows.getMax();
            final int minC = columns.getMin();
            final int maxC = columns.getMax();

            final int subRows = maxR - minR + 1;
            final int subCols = maxC - minC + 1;

            // Generate a submaze in the box.
            final Maze submaze = generate(subRows, subCols);

            // Copy the walls back into the main maze for each cell in cells.
            for (final Point p : cells) {
                final int r = p.x - minR;
                final int c = p.y - minC;

                // North
                if (submaze.hasWall(r, c, Maze.Direction.NORTH))
                    maze.fillWall(p.x, p.y, Maze.Direction.NORTH);
                else
                    maze.carveWall(p.x, p.y, Maze.Direction.NORTH);

                // East
                if (submaze.hasWall(r, c, Maze.Direction.EAST))
                    maze.fillWall(p.x, p.y, Maze.Direction.EAST);
                else
                    maze.carveWall(p.x, p.y, Maze.Direction.EAST);

                // South
                if (submaze.hasWall(r, c, Maze.Direction.SOUTH))
                    maze.fillWall(p.x, p.y, Maze.Direction.SOUTH);
                else
                    maze.carveWall(p.x, p.y, Maze.Direction.SOUTH);

                // West
                if (submaze.hasWall(r, c, Maze.Direction.WEST))
                    maze.fillWall(p.x, p.y, Maze.Direction.WEST);
                else
                    maze.carveWall(p.x, p.y, Maze.Direction.WEST);
            }
        };
    }
}
