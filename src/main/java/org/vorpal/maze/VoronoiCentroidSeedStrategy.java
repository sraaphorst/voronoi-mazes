/**
 * VoronoiCentroidSeedStrategy.java
 * By Sebastian Raaphorst, 2025.
 */

package org.vorpal.maze;

import java.awt.Point;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;

public class VoronoiCentroidSeedStrategy implements VoronoiSeedStrategy {
    private final Function<Stage, Integer> countFunction;

    public VoronoiCentroidSeedStrategy(int fixedCount) {
        this((IntFunction<Integer>) stage -> fixedCount);
    }

    public VoronoiCentroidSeedStrategy(IntFunction<Integer> countFunction) {
        this.countFunction = stage -> countFunction.apply(stage.depth());
    }

    public VoronoiCentroidSeedStrategy(Function<Stage, Integer> countFunction) {
        this.countFunction = countFunction;
    }

    @Override
    public Optional<Set<Point>> seedsFor(Stage stage) {
        final int k = countFunction.apply(stage);
        if (k < 2 || stage.cells().size() < k)
            return Optional.empty();

        // Compute bounding‐box to decide split-axis.
        final int minR = stage.cells().stream().mapToInt(p -> p.x).min().getAsInt();
        final int maxR = stage.cells().stream().mapToInt(p -> p.x).max().getAsInt();
        final int minC = stage.cells().stream().mapToInt(p -> p.y).min().getAsInt();
        final int maxC = stage.cells().stream().mapToInt(p -> p.y).max().getAsInt();

        final boolean splitByRow = (maxR - minR) >= (maxC - minC);

        // Sort cells along that axis
        final List<Point> sorted = stage.cells().stream()
                .sorted(splitByRow
                        ? Comparator.comparingInt(p -> p.x)
                        : Comparator.comparingInt(p -> p.y))
                .toList();

        final int n = sorted.size();
        final Set<Point> seeds = new HashSet<>();

        // Partition into k buckets and pick each bucket’s centroid
        for (int i = 0; i < k; i++) {
            final int start = i * n / k;
            final int end   = (i + 1) * n / k;
            if (start >= n) break;
            final List<Point> bucket = sorted.subList(start, Math.min(end, n));

            // Compute average coords.
            final double avgR = bucket.stream().mapToInt(p -> p.x).average().orElse(minR);
            final double avgC = bucket.stream().mapToInt(p -> p.y).average().orElse(minC);
            Point ctr = new Point((int) Math.round(avgR), (int) Math.round(avgC));

            // if that exact cell isn’t in the bucket, snap to the bucket’s median.
            if (!bucket.contains(ctr))
                ctr = bucket.get(bucket.size() / 2);
            seeds.add(ctr);
        }

        return Optional.of(Collections.unmodifiableSet(seeds));
    }
}
