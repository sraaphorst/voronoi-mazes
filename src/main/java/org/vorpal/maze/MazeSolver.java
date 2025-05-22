/**
 * MazeSolver.java
 * By Sebastian Raaphorst, 2025.
 */

package org.vorpal.maze;

import java.awt.Point;
import java.util.*;

public class MazeSolver {
    /**
     * Solve the maze (shortest path) using BFS.
     * @param maze the Maze to solve
     * @return list of Points from start (inclusive) to end (inclusive), or empty if no path
     */
    public static List<Point> solve(Maze maze) {
        int rows    = maze.getRows();
        int cols    = maze.getColumns();

        Point start = new Point(0, 0);
        Point end   = new Point(rows-1, cols-1);

        boolean[][] visited = new boolean[rows][cols];
        Map<Point, Point> parent = new HashMap<>();
        Deque<Point> queue = new ArrayDeque<>();

        queue.add(start);
        visited[start.x][start.y] = true;
        parent.put(start, null);

        while (!queue.isEmpty()) {
            Point cur = queue.removeFirst();
            if (cur.equals(end)) break;

            int r = cur.x, c = cur.y;
            for (Maze.Direction d : Maze.Direction.values()) {
                if (!maze.hasWall(r, c, d)) {
                    Point nbr = switch (d) {
                        case NORTH -> new Point(r-1, c);
                        case EAST  -> new Point(r,   c+1);
                        case SOUTH -> new Point(r+1, c);
                        case WEST  -> new Point(r,   c-1);
                    };
                    if (nbr.x>=0 && nbr.x<rows && nbr.y>=0 && nbr.y<cols && !visited[nbr.x][nbr.y]) {
                        visited[nbr.x][nbr.y] = true;
                        parent.put(nbr, cur);
                        queue.addLast(nbr);
                    }
                }
            }
        }

        // Reconstruct path
        if (!visited[end.x][end.y]) {
            return Collections.emptyList();  // no path
        }
        List<Point> path = new ArrayList<>();
        for (Point at = end; at != null; at = parent.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        return Collections.unmodifiableList(path);
    }
}
