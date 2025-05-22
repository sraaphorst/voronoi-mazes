/**
 * RegionMazeGenerator.java
 * By Sebastian Raaphorst, 2025.
 */

package org.vorpal.maze;

import java.util.Set;
import java.awt.Point;

/**
 * A maze generator that carves a maze only over a given subset of cells
 * in an existing Maze instance.
 */
@FunctionalInterface
public interface RegionMazeGenerator {
    /**
     * Carves a maze over the given cells in the given Maze instance.
     * @param maze  the Maze under construction
     * @param cells the exact set of cells over which to carve the maze
     */
    void carve(Maze maze, Set<Point> cells);
}
