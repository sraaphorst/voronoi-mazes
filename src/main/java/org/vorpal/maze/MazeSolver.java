/**
 * MazeSolver.java
 * By Sebastian Raaphorst, 2025.
 */

package org.vorpal.maze;

import java.awt.Point;
import java.util.*;

public final class MazeSolver {
    private MazeSolver() {}

    /**
     * Solve the maze (via the shortest path) using BFS.
     * This should only have a single solution anyway if the maze is perfect.
     * @param maze  the Maze to solve
     * @param start the starting point
     * @param end   the goal point
     * @return list of Points from start (inclusive) to end (inclusive), or empty if no path
     */
    public static List<Point> solve(Maze maze, Point start, Point end) {
        final int rows = maze.getRows();
        final int cols = maze.getColumns();

        Set<Point> visited = new HashSet<>();
        Map<Point, Point> parent = new HashMap<>();
        Deque<Point> queue = new ArrayDeque<>();

        queue.add(start);
        visited.add(start);
        parent.put(start, null);

        while (!queue.isEmpty()) {
            Point cur = queue.removeFirst();
            if (cur.equals(end)) break;

            for (final Maze.Direction d : Maze.Direction.values()) {
                if (!maze.hasWall(cur, d)) {
                    final Point nbr = Maze.neighbour(cur, d);
                    if (nbr.x >= 0 && nbr.x < rows && nbr.y >= 0 && nbr.y < cols && !visited.contains(nbr)) {
                        visited.add(nbr);
                        parent.put(nbr, cur);
                        queue.addLast(nbr);
                    }
                }
            }
        }

        // Reconstruct path
        if (!visited.contains(end))
            return Collections.emptyList();
        final List<Point> path = new ArrayList<>();
        for (Point at = end; at != null; at = parent.get(at))
            path.add(at);
        Collections.reverse(path);
        return Collections.unmodifiableList(path);
    }

    /**
     * Solve the maze (via the shortest path) using BFS.
     * This should only have a single solution anyway if the maze is perfect.
     * @param maze the Maze to solve
     * @return immutable list of Points from the upper-left to bottom-right corners, or empty if no path exists
     */
    public static List<Point> solve(Maze maze) {
        final Point start = new Point(0, 0);
        final Point end   = new Point(maze.getRows()-1, maze.getColumns()-1);
        return solve(maze, start, end);
    }
}
