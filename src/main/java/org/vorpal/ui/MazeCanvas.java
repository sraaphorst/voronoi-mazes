/**
 * MazeCanvas.java
 * By Sebastian Raaphorst, 2025.
 */

package org.vorpal.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.vorpal.maze.Maze;

public class MazeCanvas extends Canvas {
    private final Maze maze;
    private final double cellSize;

    public MazeCanvas(Maze maze, double cellSize) {
        super(maze.getColumns() * cellSize, maze.getRows() * cellSize);
        this.maze = maze;
        this.cellSize = cellSize;
        draw();
    }

    private void draw() {
        GraphicsContext g = getGraphicsContext2D();
        g.setStroke(Color.BLACK);
        g.setLineWidth(2);
        int rows = maze.getRows();
        int cols = maze.getColumns();


        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double x = c * cellSize;
                double y = r * cellSize;

                if (maze.hasWall(r, c, Maze.Direction.NORTH))
                    g.strokeLine(x, y,x + cellSize, y);
                if (maze.hasWall(r, c, Maze.Direction.EAST))
                    g.strokeLine(x + cellSize, y,x + cellSize,y + cellSize);
                if (maze.hasWall(r, c, Maze.Direction.SOUTH))
                    g.strokeLine(x,y + cellSize,x + cellSize,y + cellSize);
                if (maze.hasWall(r, c, Maze.Direction.WEST))
                    g.strokeLine(x, y, x,y + cellSize);
            }
        }
    }
}