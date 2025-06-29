/**
 * BacktrackerMazeGenerator.java
 * By Sebastian Raaphorst, 2025.
 */

package org.vorpal.maze;

import java.awt.Point;
import java.util.*;

/**
 * A simple depth-first recursive backtracker maze generator.
 */
public class BacktrackerMazeGenerator implements MazeGenerator {
    private final Random rnd = new Random();

    @Override
    public Maze generate(int rows, int columns) {
        final Maze maze = new Maze(rows, columns);
        final Set<Point> visited = new HashSet<>();
        final Deque<Point> stack = new ArrayDeque<>();

        // Start at the top-left corner.
        final Point start = new Point(0, 0);
        stack.push(start);
        visited.add(start);

        while (!stack.isEmpty()) {
            final Point cell = stack.peek();

            // Collect the unvisited neighbours.
            final List<Maze.Direction> neighbours = new ArrayList<>();
            if (cell.x > 0 && !visited.contains(Maze.neighbour(cell, Maze.Direction.NORTH)))
                neighbours.add(Maze.Direction.NORTH);
            if (cell.y < columns - 1 && !visited.contains(Maze.neighbour(cell, Maze.Direction.EAST)))
                neighbours.add(Maze.Direction.EAST);
            if (cell.x < rows - 1 && !visited.contains(Maze.neighbour(cell, Maze.Direction.SOUTH)))
                neighbours.add(Maze.Direction.SOUTH);
            if (cell.y > 0 && !visited.contains(Maze.neighbour(cell,  Maze.Direction.WEST)))
                neighbours.add(Maze.Direction.WEST);

            if (!neighbours.isEmpty()) {
                // Choose a random direction.
                final Maze.Direction dir = neighbours.get(rnd.nextInt(neighbours.size()));
                Point next;
                switch (dir) {
                    case NORTH -> next = Maze.neighbour(cell, Maze.Direction.NORTH);
                    case EAST  -> next = Maze.neighbour(cell, Maze.Direction.EAST);
                    case SOUTH -> next = Maze.neighbour(cell, Maze.Direction.SOUTH);
                    case WEST  -> next = Maze.neighbour(cell, Maze.Direction.WEST);
                    default    -> throw new IllegalStateException("Unexpected value: " + dir);
                }

                maze.carveWall(cell, dir);
                visited.add(next);
                stack.push(next);
            } else
                stack.pop();
        }

        return maze;
    }
}
