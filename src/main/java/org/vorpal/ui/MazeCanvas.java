package org.vorpal.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.vorpal.maze.Maze;

import java.awt.Point;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class MazeCanvas extends Canvas {
    private final Maze maze;
    private final double cellSize;
    private List<Point> solution;   // new field

    public MazeCanvas(Maze maze, double cellSize) {
        super(maze.getColumns() * cellSize, maze.getRows() * cellSize);
        this.maze = maze;
        this.cellSize = cellSize;
        draw();
    }

    /** Set (or replace) the solution path and redraw. */
    public void setSolution(List<Point> solution) {
        this.solution = solution;
        draw();
    }

    private void draw() {
        final GraphicsContext g = getGraphicsContext2D();
        g.clearRect(0,0, getWidth(), getHeight());

        // 1) draw walls
        g.setStroke(Color.BLACK);
        g.setLineWidth(2);

        final int rows = maze.getRows();
        final int cols = maze.getColumns();
        IntStream.range(0, rows).forEach(r -> {
            final double y = r * cellSize;
            final Function<Integer, Point> cellGenerator = c -> new Point(r, c);
           IntStream.range(0, cols).forEach(c -> {
              final double x = c * cellSize;
              final Point cell = cellGenerator.apply(c);

              if (maze.hasWall(cell, Maze.Direction.NORTH))
                  g.strokeLine(x, y, x + cellSize, y);
               if (maze.hasWall(cell, Maze.Direction.EAST))
                   g.strokeLine(x + cellSize, y,x + cellSize,y + cellSize);
               if (maze.hasWall(cell, Maze.Direction.SOUTH))
                   g.strokeLine(x,y + cellSize,x + cellSize,y + cellSize);
               if (maze.hasWall(cell, Maze.Direction.WEST))
                   g.strokeLine(x, y, x,y + cellSize);
           });
        });

        // Draw the solution if present.
        if (solution != null && !solution.isEmpty()) {
            g.setStroke(Color.RED);
            g.setLineWidth(cellSize * 0.3);

            // draw a continuous path through the center of each cell
            IntStream.range(0, solution.size() - 1).forEach(i -> {
                final Point p1 = solution.get(i);
                final Point p2 = solution.get(i+1);

                final double x1 = p1.y * cellSize + cellSize/2;
                final double y1 = p1.x * cellSize + cellSize/2;
                final double x2 = p2.y * cellSize + cellSize/2;
                final double y2 = p2.x * cellSize + cellSize/2;

                g.strokeLine(x1, y1, x2, y2);
            });
        }
    }
}