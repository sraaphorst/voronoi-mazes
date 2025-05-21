/**
 * MazeGenerator.java
 * By Sebastian Raaphorst, 2025.
 */

package org.vorpal.maze;

import org.vorpal.math.Metric2D;

@FunctionalInterface
public interface MazeGenerator {
    Maze generate(int rows, int columns, Metric2D metric);
}
