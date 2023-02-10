package waiter.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import waiter.Till;
import waiter.TillException;

/**
 * This page shows all of the functions that can be performed by cashiers
 * 
 * @author Charles Card
 *
 */
public class MainMenuController implements Initializable {
  private Till till;
  @FXML
  private AnchorPane masterAnchorPane;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    try {
      this.till = Till.getInstance();
    } catch (TillException e) {
      e.printStackTrace();
    }
  }

  /**
   * Logs the user out of the till and returns to the login page
   * 
   */
  @FXML
  private void exitEvent(ActionEvent event) {
    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = null;
    try {
      scene = new Scene((Parent) FXMLLoader.load(getClass().getResource("Login.fxml")), 1920, 1080);
    } catch (IOException e) {
      System.out.println("Missing file: Login.fxml");
      e.printStackTrace();
    }
    this.till.setCurrentUser(null); // Log out
    boolean fullScreen = currentStage.isFullScreen();
    currentStage.setScene(scene);
    currentStage.setFullScreen(fullScreen);
  }

  /**
   * Loads the page for displaying the current orders
   * 
   */
  @FXML
  private void currentOrdersEvent(ActionEvent event) {
    AnchorPane newPane = null;
    try {
      newPane = (AnchorPane) FXMLLoader.load(getClass().getResource("CurrentOrders.fxml"));
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("That failed");
    }

    AnchorPane root = (AnchorPane) masterAnchorPane.getParent();
    root.getChildren().clear();
    root.getChildren().add(newPane);
  }

  /**
   * Loads the page for creating a new order
   * 
   */
  @FXML
  private void newOrderEvent(ActionEvent event) {
    AnchorPane newPane = null;
    try {
      newPane = (AnchorPane) FXMLLoader.load(getClass().getResource("MenuItem.fxml"));
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("That failed");
    }

    AnchorPane root = (AnchorPane) masterAnchorPane.getParent();
    root.getChildren().clear();
    root.getChildren().add(newPane);
  }

  /**
   * Loads the page for displaying old orders
   * 
   */
  @FXML
  private void oldOrdersEvent(ActionEvent event) {
    // TODO
  }

  /**
   * Loads the page for handling clocking out of the system
   * 
   */
  @FXML
  private void clockOutEvent(ActionEvent event) {
    // TODO
  }

  /**
   * Loads the manager functions page which shows a list of functions only a manager can perform
   * 
   */
  @FXML
  private void managerFunctionsEvent(ActionEvent event) {
    AnchorPane newPane = null;
    try {
      newPane = (AnchorPane) FXMLLoader.load(this.getClass().getResource("AdjustMenu.fxml"));
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("That failed");
    }
    AnchorPane root = (AnchorPane) masterAnchorPane.getParent();
    root.getChildren().clear();
    root.getChildren().add(newPane);
  }

  /**
   * Loads the edit menu page which allows waiters to edit the menu
   *
   */
  @FXML
  private void editMenuEvent(ActionEvent event) {
    AnchorPane newPane = null;
    try {
      newPane = (AnchorPane) FXMLLoader.load(this.getClass().getResource("ChangeMenu.fxml"));
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("That failed");
    }

    AnchorPane root = (AnchorPane) masterAnchorPane.getParent();
    root.getChildren().clear();
    root.getChildren().add(newPane);
  }

  /**
   * Loads the unconfirmed order page which allows waiters to verify orders and then send them to
   * the kitchen
   * 
   */
  @FXML
  private void unconfirmedOrdersEvent(ActionEvent event) {
    AnchorPane newPane = null;
    try {
      newPane = (AnchorPane) FXMLLoader.load(getClass().getResource("UnconfirmedOrders.fxml"));
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("That failed");
    }

    AnchorPane root = (AnchorPane) masterAnchorPane.getParent();
    root.getChildren().clear();
    root.getChildren().add(newPane);
  }
}
