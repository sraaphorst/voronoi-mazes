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
import org.vorpal.maze.*;
import org.vorpal.ui.MazeCanvas;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        VoronoiMazeGenerator vm = new VoronoiMazeGenerator(
                new VoronoiRandomSeedStrategy(4),
                new ManhattanMetric2D(),
                new BacktrackerMazeGenerator().adapt()
        );

//        MazeGenerator gen = new BacktrackerMazeGenerator();
        Maze maze = vm.generate(50, 75);

        MazeCanvas canvas = new MazeCanvas(maze, 10);
        Scene scene = new Scene(new StackPane(canvas));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}