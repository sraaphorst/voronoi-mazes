/**
 * BacktrackerMazeGenerator.java
 * By Sebastian Raaphorst, 2025.
 */

package org.vorpal.maze;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/**
 * A simple depth-first recursive backtracker maze generator.
 */
public class BacktrackerMazeGenerator implements MazeGenerator {
    private final Random rnd = new Random();

    @Override
    public Maze generate(int rows, int columns) {
        final Maze maze = new Maze(rows, columns);
        final boolean[][] visited = new boolean[rows][columns];
        final Deque<Point> stack = new ArrayDeque<>();

        // Start at the top-left corner.
        stack.push(new Point(0, 0));
        visited[0][0] = true;

        while (!stack.isEmpty()) {
            final Point p = stack.peek();
            final int r = p.x;
            final int c = p.y;

            // Collect the unvisited neighbours.
            final List<Maze.Direction> neighbours = new ArrayList<>();
            if (r > 0 && !visited[r-1][c]) neighbours.add(Maze.Direction.NORTH);
            if (c < columns - 1 && !visited[r][c+1]) neighbours.add(Maze.Direction.EAST);
            if (r < rows - 1 && !visited[r+1][c]) neighbours.add(Maze.Direction.SOUTH);
            if (c > 0 && !visited[r][c-1]) neighbours.add(Maze.Direction.WEST);

            if (!neighbours.isEmpty()) {
                // Choose a random direction.
                final Maze.Direction dir = neighbours.get(rnd.nextInt(neighbours.size()));
                Point next;
                switch (dir) {
                    case NORTH -> next = new Point(r-1, c);
                    case EAST  -> next = new Point(r, c+1);
                    case SOUTH -> next = new Point(r+1, c);
                    case WEST  -> next = new Point(r, c-1);
                    default    -> throw new IllegalStateException("Unexpected value: " + dir);
                }

                maze.carveWall(r, c, dir);
                visited[next.x][next.y] = true;
                stack.push(next);
            } else {
                stack.pop();
            }
        }

        return maze;
    }
}
