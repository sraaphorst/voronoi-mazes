/**
 * Main.java
 * By Sebastian Raaphorst, 2025.
 */

package org.vorpal;

import java.awt.Point;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.vorpal.math.ChebyshevMetric2D;
import org.vorpal.math.EuclideanMetric2D;
import org.vorpal.math.ManhattanMetric2D;
import org.vorpal.maze.*;
import org.vorpal.ui.MazeCanvas;

public class Main extends Application {
    private static final int ROWS = 100;
    private static final int COLUMNS = 100;
    private static final int CELL_SIZE = 10;

    @Override
    public void start(Stage stage) throws Exception {
        // A binary maze generator.
        final BinaryTreeMazeGenerator bmg = new BinaryTreeMazeGenerator();

        // Maze generators.
//        MazeGenerator gen = new BinaryTreeMazeGenerator();
//        MazeGenerator gen = new BacktrackerMazeGenerator();
        final VoronoiMazeGenerator gen = new VoronoiMazeGenerator(
                //new VoronoiRandomSeedStrategy(3),
                new VoronoiRandomSeedStrategy((org.vorpal.maze.Stage s) -> s.cells().size() / 40),
                new ManhattanMetric2D(),
                //new EuclideanMetric2D(),
                //new ChebyshevMetric2D(),
                new BacktrackerMazeGenerator().adapt()
        );

        final Maze maze = gen.generate(ROWS, COLUMNS);
        final MazeCanvas canvas = new MazeCanvas(maze, CELL_SIZE);
        final List<Point> sol = MazeSolver.solve(maze);
        canvas.setSolution(sol);

        Scene scene = new Scene(new StackPane(canvas));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}