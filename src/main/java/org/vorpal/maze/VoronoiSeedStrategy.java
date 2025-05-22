/**
 * VoronoiSeedStrategy.java
 * By Sebastian Raaphorst, 2025.
 */

package org.vorpal.maze;

import java.awt.Point;
import java.util.Optional;
import java.util.Set;

/**
 * A strategy for generating seeds for a given Stage of a Voronoi maze generation algorithm.
 */
@FunctionalInterface
public interface VoronoiSeedStrategy {
    /**
     * Returns the seeds for the given stage.
     * @param stage the stage for which to generate seeds
     * @return the set of seeds for the given stage, or, if the stage is to be processed using another
     *         technique, None
     */
    Optional<Set<Point>> seedsFor(Stage stage);
}
