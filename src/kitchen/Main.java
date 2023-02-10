package kitchen;

/**
 * <h3>Main</h3> The Main class is what sets the stage and starts the program.
 *
 * <p>
 * The start() method sets and shows the scene.
 *
 * <p>
 * The main() method which runs the program.
 *
 * @author Justus Marius Mueller (100862809)
 * @version 0.2
 */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class Main extends Application {

  static Scene scene;

  /**
   * Starts the program.
   *
   * @param args any arguments given before running the file.
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Uses the stage to set the scene and display it.
   */
  @Override
  public void start(Stage stage) throws Exception {
    final int width = 1920;
    final int height = 1080;
    final Pane root = new Pane();

    Pane view = (Pane) FXMLLoader.load(this.getClass().getResource("view.fxml"));
    view.setPrefWidth(width);
    view.setPrefHeight(height);
    root.getChildren().add(view);

    Scale scale = new Scale(1, 1, 0, 0);
    scale.xProperty().bind(root.widthProperty().divide(width));
    scale.yProperty().bind(root.heightProperty().divide(height));
    root.getTransforms().add(scale);

    final Scene scene = new Scene(root, width, height);
    stage.setTitle("Kitchen Interface");
    stage.setScene(scene);
    stage.setResizable(true);
    stage.setFullScreen(true);
    stage.setFullScreenExitHint("");
    stage.show();
  }
}
