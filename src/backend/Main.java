package backend;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class Main extends Application {

	
  static Scene scene;
  public static void main(String[] args) {
    launch(args);
  }
  

  @Override
  public void start(Stage stage)
  {
    final int width = 1500;
    final int height = 950;
    final Pane root = new Pane();
    Scale scale = new Scale(0, 0, 0, 0);
    final Scene scene = new Scene(root, width, height);
    Pane view = null;
    
	try {
		view = (Pane) FXMLLoader.load(getClass().getResource("ManagerGUI.fxml"));
	} catch (IOException e) {
		e.printStackTrace();
	}
	
    view.setPrefWidth(width);
    view.setPrefHeight(height);
    root.getChildren().add(view);    
    scale.xProperty().bind(root.widthProperty().divide(width));
    scale.yProperty().bind(root.heightProperty().divide(height));
    root.getTransforms().add(scale);
    stage.setTitle("Manager Interface");
    stage.setScene(scene);
    stage.setResizable(true);
    stage.setFullScreenExitHint("");
    stage.show();
  }
}
