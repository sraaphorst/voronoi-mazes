/**
 * VoronoiMazeGenerator.java
 * By Sebastian Raaphorst, 2025.
 */

package org.vorpal.maze;

import org.vorpal.math.Metric2D;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.IntStream;

public class VoronoiMazeGenerator implements MazeGenerator {
    private final VoronoiSeedStrategy seedSupplier;
    private final Metric2D metric;
    private final RegionMazeGenerator fallbackGenerator;
    private final Random rnd = new Random();

    public VoronoiMazeGenerator(VoronoiSeedStrategy seedSupplier,
                                Metric2D metric,
                                RegionMazeGenerator fallbackGenerator) {
        this.seedSupplier = seedSupplier;
        this.metric = metric;
        this.fallbackGenerator = fallbackGenerator;
    }

    @Override
    public Maze generate(int rows, int columns) {
        final Maze maze = new Maze(rows, columns);
        final Set<Point> allCells = new HashSet<>();
        IntStream.range(0, rows).forEach(r ->
                IntStream.range(0, columns).forEach(c ->
                        allCells.add(new Point(r, c))
                )
        );
        subdivide(maze, new Stage(0, allCells));
        return maze;
    }

    private void subdivide(Maze maze, Stage stage) {
        final Optional<Set<Point>> maybeSeeds = seedSupplier.seedsFor(stage);

        if (maybeSeeds.isEmpty()) {
            fallbackFinish(maze, stage.cells());
            return;
        }

        // Assign every cell to its nearest seed.
        final Set<Point> seeds = maybeSeeds.get();
        final Map<Point, Set<Point>> regions = new HashMap<>();
        seeds.forEach(seed -> regions.put(seed, new HashSet<>()));
        stage.cells().forEach(cell -> {
            final Point nearestSeed = seeds.stream()
                    .min(Comparator.comparingInt(seed -> metric.distance(seed, cell)))
                    .orElseThrow();
            regions.get(nearestSeed).add(cell);
        });

        // Build the adjacencies: for each cell, look east and south.
        // They are neighbours whenever they belong to different seeds.
        // We record that border.
        record RegionEdge(Point seed1, Point seed2, Point cell, Maze.Direction dir) {
        }
        final Map<Set<Point>, RegionEdge> edgeMap = new HashMap<>();

        for (final Point cell : stage.cells()) {
            // Helper to handle one neighbour direction.
            BiConsumer<Maze.Direction, Point> checkNeighbour = (dir, nbr) -> {
                if (stage.cells().contains(nbr)) {
                    final Point s1 = findRegionSeed(regions, cell);
                    final Point s2 = findRegionSeed(regions, nbr);
                    if (!s1.equals(s2)) {
                        final Set<Point> key = Set.of(s1, s2);
                        edgeMap.putIfAbsent(key, new RegionEdge(s1, s2, cell, dir));
                    }
                }
            };

            final int r = cell.x;
            final int c = cell.y;

            // Handle the east neighbour.
            checkNeighbour.accept(Maze.Direction.EAST, new Point(r, c + 1));
            // Handle the south neighbour.
            checkNeighbour.accept(Maze.Direction.SOUTH, new Point(r + 1, c));
        }

        // Pick a random spanning tree over the seeds.
        final List<RegionEdge> allEdges = new ArrayList<>(edgeMap.values());
        Collections.shuffle(allEdges, rnd);

        // Execute a simple union-find over the seeds.
        final Map<Point, Point> parentMap = new HashMap<>();
        seeds.forEach(seed -> parentMap.put(seed, seed));
        final Function<Point, Point> find = new Function<>() {
            public Point apply(Point x) {
                if (parentMap.get(x) != x)
                    parentMap.put(x, apply(parentMap.get(x)));
                return parentMap.get(x);
            }
        };

        final BiConsumer<Point, Point> union = (p1, p2) ->
                parentMap.put(find.apply(p1), find.apply(p2));

        // A spanning tree across all regions requires carving (# of regions - 1) holes.
        int needed = seeds.size() - 1;
        for (final RegionEdge edge : allEdges) {
            final Point r1 = find.apply(edge.seed1);
            final Point r2 = find.apply(edge.seed2);
            if (!r1.equals(r2)) {
                // Carve exactly that shared wall.
                maze.carveWall(edge.cell, edge.dir);
                union.accept(edge.seed1, edge.seed2);
                if (--needed == 0) break;
            }
        }

        // Recurse on each region.
        for (final Set<Point> region : regions.values())
            subdivide(maze, new Stage(stage.depth() + 1, region));
    }

    private void fallbackFinish(Maze maze, Set<Point> region) {
        // A simple DFS backtracker over an arbitrary cell‐set
        Random rnd = new Random();
        Set<Point> unvisited = new HashSet<>(region);
        Deque<Point> stack = new ArrayDeque<>();

        // Seed the walk at any cell in the region
        Point start = unvisited.iterator().next();
        stack.push(start);
        unvisited.remove(start);

        while (!stack.isEmpty()) {
            final Point cell = stack.peek();

            // find all unvisited neighbors *within* this region
            final List<Maze.Direction> nbrs = new ArrayList<>();
            if (unvisited.contains(new Point(cell.x - 1, cell.y))) nbrs.add(Maze.Direction.NORTH);
            if (unvisited.contains(new Point(cell.x, cell.y + 1))) nbrs.add(Maze.Direction.EAST);
            if (unvisited.contains(new Point(cell.x + 1, cell.y))) nbrs.add(Maze.Direction.SOUTH);
            if (unvisited.contains(new Point(cell.x, cell.y - 1))) nbrs.add(Maze.Direction.WEST);

            if (!nbrs.isEmpty()) {
                // carve to a random neighbor
                final Maze.Direction dir = nbrs.get(rnd.nextInt(nbrs.size()));
                final Point next = Maze.neighbour(cell, dir);
                maze.carveWall(cell, dir);
                stack.push(next);
                unvisited.remove(next);
            } else {
                // dead end, backtrack
                stack.pop();
            }
        }
    }

//    private void fallbackFinish(Maze maze, Set<Point> cells) {
//        fallbackGenerator.carve(maze, cells);
//    }

    /**
     * Helper function to determine which seed "owns" a cell in the regions map.
     */
    private Point findRegionSeed(Map<Point, Set<Point>> regions, Point cell) {
        return regions.entrySet().stream()
                .filter(entry -> entry.getValue().contains(cell))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow();
    }
}
