/**
 * Main.java
 * By Sebastian Raaphorst, 2025.
 */

package org.vorpal;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.vorpal.math.ManhattanMetric2D;
import org.vorpal.maze.BinaryTreeMazeGenerator;
import org.vorpal.maze.Maze;
import org.vorpal.maze.MazeGenerator;
import org.vorpal.ui.MazeCanvas;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        MazeGenerator gen = new BinaryTreeMazeGenerator();
        Maze maze = gen.generate(50, 75, new ManhattanMetric2D());

        MazeCanvas canvas = new MazeCanvas(maze, 10);
        Scene scene = new Scene(new StackPane(canvas));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}