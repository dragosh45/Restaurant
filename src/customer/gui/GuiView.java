package customer.gui;

import backend.Filter;
import backend.Type;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;

/**
 * The main part of the application which deals with the stage and scene.
 *
 * @author Emily Chaffey
 *
 */
public class GuiView extends Application {

  /**
   * Stores the instance of the Gui class so that the driver pattern can work.
   */
  private static volatile GuiView instance = null;

  /**
   * A method which gets the only instance of the class or creates a new instance if one hasn't been
   * created.
   *
   * @return The instance of the class.
   */
  public static synchronized GuiView getInstance() {
    if (instance == null) {
      new Thread(() -> Application.launch(GuiView.class)).start();
    }
    while (instance == null) {
    }
    return instance;
  }

  private Filter currentFilter = null;
  private Type currentType = null;
  private AnchorPane mainPane;
  private Stage primaryStage;
  private double screenHeight;
  private double screenWidth;

  /**
   * Getter for the current filter.
   *
   * @return The current filter.
   */
  public Filter getCurrentFilter() {
    return this.currentFilter;
  }

  /**
   * Getter for the current menu type.
   *
   * @return The current menu type.
   */
  public Type getCurrentType() {
    return this.currentType;
  }

  /**
   * Getter for the main pane which is used for displaying all of the content.
   *
   * @return The main pane.
   */
  public AnchorPane getMainPane() {
    return this.mainPane;
  }

  /**
   * Getter for the screen height.
   *
   * @return The screen height.
   */
  public double getScreenHeight() {
    return this.screenHeight;
  }

  /**
   * Getter for the screen width.
   *
   * @return The screen width.
   */
  public double getScreenWidth() {
    return this.screenWidth;
  }

  /**
   * Getter for the stage.
   *
   * @return The current instance of the stage.
   */
  public Stage getStage() {
    return this.primaryStage;
  }

  /**
   * Initial sets up for the Gui class.
   */
  @FXML
  void initialize() {
    instance = this;
  }

  /**
   * Setter for the current filter.
   *
   * @param filter The current filter.
   */
  public void setCurrentFilter(Filter filter) {
    this.currentFilter = filter;
  }

  /**
   * Setter for the current menu type.
   *
   * @param type The current menu type.
   */
  public void setCurrentType(Type type) {
    this.currentFilter = null;
    this.currentType = type;
  }

  /**
   * Setter for the main pane.
   *
   * @param pane The pane to be set as the main pane.
   */
  public void setMainPane(AnchorPane pane) {
    this.mainPane = pane;
  }

  @Override
  public void start(Stage stage) {
    instance = this;
    this.primaryStage = stage;
    try {
      // setting the screen size
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      this.screenWidth = screenSize.getWidth();
      this.screenHeight = screenSize.getHeight();
      // loading the main menu screen
      FXMLLoader fl = new FXMLLoader(this.getClass().getResource("MainBar.fxml"));
      AnchorPane root = (AnchorPane) fl.load();
      Scene scene = new Scene(root, this.screenWidth, this.screenHeight);
      this.primaryStage.setScene(scene);
      this.primaryStage.setResizable(false);
      this.primaryStage.setTitle("Navigator");
      this.primaryStage.setMaximized(true);
      this.primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
