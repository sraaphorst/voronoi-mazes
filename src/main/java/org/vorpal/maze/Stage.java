/**
 * Stage.java
 * By Sebastian Raaphorst, 2025.
 */

package org.vorpal.maze;

import java.awt.Point;
import java.util.Set;

/**
 * A stage in a recursive maze generation algorithm.
 * @param depth the depth of the stage into the recursive algorithm
 * @param cells the cells in this stage
 */
public record Stage(int depth, Set<Point> cells) {
}
