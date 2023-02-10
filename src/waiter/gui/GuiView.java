package waiter.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

/**
 * Starts the GUI for the till application
 *
 * @author Charles Card
 */
public class GuiView extends Application {
  private volatile static GuiView instance = null;

  public synchronized static GuiView getInstance() {
    if (instance == null) {
      new Thread() {
        @Override
        public void run() {
          javafx.application.Application.launch(GuiView.class);
        }
      }.start();
    }
    while (instance == null) {
    } // Wait for the instance variable to be set
    return instance;
  }

  private Stage currentStage;

  public Stage getCurrentStage() {
    return this.currentStage;
  }

  @Override
  public void start(Stage stage) throws Exception {
    instance = this;
    this.currentStage = stage;
    Parent root = FXMLLoader.load(this.getClass().getResource("Login.fxml"));
    Scene scene = new Scene(root, 1920, 1080);
    stage.setTitle("Till System");
    stage.setScene(scene);
    stage.setFullScreen(true); // Sets the application to full screen
    stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // Cannot escape full screen
    stage.setFullScreenExitHint(""); // No annoying message on startup
    stage.show();
  }
}
