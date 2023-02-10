package customer.gui;

import java.io.IOException;

import backend.Order;
import backend.OrderItem;
import customer.Kiosk;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controller class for the Basket page - which displays the customer's order.
 *
 * @author Emily Chaffey
 *
 */
public class BasketController {

  @FXML
  private VBox basket;
  private GuiView gui;
  @FXML
  private AnchorPane mainPane;
  private boolean stopTimer = false;
  @FXML
  private Label total;

  /**
   * Loads the order item into the VBox for the basket.
   *
   * @param orderItem
   *          The OrderItem to be loaded.
   */
  private void addProduct(OrderItem orderItem) {
    BasketItemController controller = new BasketItemController(orderItem);
    FXMLLoader fl = new FXMLLoader(
        this.getClass().getResource("BasketItem.fxml"));
    fl.setController(controller);
    try {
      AnchorPane pane = fl.load();
      this.basket.getChildren().add(pane);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Cancels the order and return the user to the Navigator screen when the
   * button is clicked.
   *
   * @param event
   *          Button click event which triggers the event.
   */
  @FXML
  void cancelEvent(ActionEvent event) {
    try {
      this.stopTimer = true;
      Kiosk.getInstance().newOrder();
      Stage stage = this.gui.getStage();
      stage.setTitle("Navigator");
      FXMLLoader fl = new FXMLLoader(
          this.getClass().getResource("Navigator.fxml"));
      NavigatorController controller = new NavigatorController(
          this.gui.getMainPane());
      fl.setController(controller);
      AnchorPane root = (AnchorPane) fl.load();
      this.gui.getMainPane().getChildren().clear();
      this.gui.getMainPane().getChildren().add(root);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Confirms the order and sends it to the database. Then returns the user to
   * the Navigator screen.
   *
   * @param event
   *          Button click event which triggers the event.
   */
  @FXML
  void confirmEvent(ActionEvent event) {
    if (Kiosk.getInstance().getCurrentOrder().getList().size() == 0) {
      Alert alert = new Alert(AlertType.WARNING);
      alert.setTitle("Warning");
      alert.setHeaderText("No Items in Order");
      alert.setContentText("You must have at least one item in an order!");
      alert.showAndWait();
    } else {
      this.stopTimer = true;
      Kiosk.getInstance().getCurrentOrder().send();
      try {
        Stage stage = this.gui.getStage();
        stage.setTitle("Navigator");
        FXMLLoader fl = new FXMLLoader(
            this.getClass().getResource("Navigator.fxml"));
        NavigatorController controller = new NavigatorController(
            this.gui.getMainPane(), true);
        fl.setController(controller);
        AnchorPane root = (AnchorPane) fl.load();
        this.gui.getMainPane().getChildren().clear();
        this.gui.getMainPane().getChildren().add(root);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Takes the user back to the FoodItems page so that they can add more items
   * to their order.
   *
   * @param event
   *          The event which triggers the method when the user click on the go
   *          back button.
   */
  @FXML
  void goBack(ActionEvent event) {
    try {
      Stage stage = this.gui.getStage();
      stage.setTitle("Menu");
      FXMLLoader fl = new FXMLLoader(
          this.getClass().getResource("FoodItems.fxml"));
      AnchorPane root = (AnchorPane) fl.load();
      this.gui.getMainPane().getChildren().clear();
      this.gui.getMainPane().getChildren().add(root);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Initialiser for the controller - sets up the animation timer and the pane
   * size.
   */
  @FXML
  private void initialize() {
    this.gui = GuiView.getInstance();

    this.mainPane.maxWidthProperty()
        .bind(this.gui.getMainPane().widthProperty());
    this.mainPane.minWidthProperty()
        .bind(this.gui.getMainPane().widthProperty());
    this.mainPane.maxHeightProperty()
        .bind(this.gui.getMainPane().heightProperty());
    this.mainPane.minHeightProperty()
        .bind(this.gui.getMainPane().heightProperty());

    this.loadOrders();
    new AnimationTimer() {

      @Override
      public void handle(long now) {
        BasketController.this.updateTotal();
        if (BasketController.this.stopTimer) {
          this.stop();
        }
      }
    }.start();
  }

  /**
   * Loads the orders onto the basket screen.
   */
  private void loadOrders() {
    this.basket.getChildren().clear();
    Order order = Kiosk.getInstance().getCurrentOrder();
    this.basket.setSpacing(10);
    for (OrderItem orderItem : order.getList()) {
      this.addProduct(orderItem);
    }
  }

  /**
   * Updates the total price of the order.
   */
  private void updateTotal() {
    Order order = Kiosk.getInstance().getCurrentOrder();
    String poundSterling = new String(Character.toChars(163));
    this.total.setText(
        String.format("%s%.2f", poundSterling, order.getPrice() / 100));
  }

}
