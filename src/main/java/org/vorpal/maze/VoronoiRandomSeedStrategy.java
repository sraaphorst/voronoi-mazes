/**
 * VoronoiRandomSeedStrategy.java
 * By Sebastian Raaphorst, 2025.
 */

package org.vorpal.maze;

import java.awt.Point;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * A strategy for generating seeds for a given Stage of a Voronoi maze generation algorithm.
 * If the count function returns 0 or negative, Optional.empty() is returned.
 */
final public class VoronoiRandomSeedStrategy implements VoronoiSeedStrategy {
    private final Function<Stage, Integer> countFunction;
    private final Random rnd;

    public VoronoiRandomSeedStrategy(int fixedCount) {
        this((IntFunction<Integer>) stage -> fixedCount);
    }

    public VoronoiRandomSeedStrategy(IntFunction<Integer> countFunction) {
        this.countFunction = stage -> countFunction.apply(stage.depth());;
        this.rnd = new Random();
    }

    public VoronoiRandomSeedStrategy(Function<Stage, Integer> countFunction) {
        this.countFunction = countFunction;
        this.rnd = new Random();
    }

    @Override
    public Optional<Set<Point>> seedsFor(Stage stage) {
        final int k = countFunction.apply(stage);
        if (k <= 0 || stage.cells().size() < Math.max(2, k))
            return Optional.empty();

        final List<Point> available = new ArrayList<>(stage.cells());
        final Set<Point> seeds = new HashSet<>();

        while (seeds.size() < k) {
            final int i = rnd.nextInt(available.size());
            seeds.add(available.get(i));
            available.remove(i);
        }
        return Optional.of(Collections.unmodifiableSet(seeds));
    }
}
