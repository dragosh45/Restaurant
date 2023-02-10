package customer.gui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import com.jfoenix.controls.JFXButton;

import backend.Order;
import backend.OrderItem;
import backend.Status;
import customer.Kiosk;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Controller class for the navigator page. Lets the user view the progress of
 * their current order and create a new order.
 *
 * @author Emily Chaffey
 *
 */
public class NavigatorController {

  @FXML
  private JFXButton callWaiter;
  private GuiView gui;
  private AnchorPane mainPane;
  @FXML
  private JFXButton newOrder;
  private boolean orderChanges = true;
  private boolean orderSent = false;
  private Status orderStatus;
  @FXML
  private AnchorPane pane;
  @FXML
  private HBox payment;
  @FXML
  private Label status;
  @FXML
  private VBox statusBox;

  private boolean stopTimer = false;

  @FXML
  private Label time;

  /**
   * Constructor for the controller which is used when an order has not been
   * completed.
   *
   * @param mainPane
   *          The main pane for the page to be displayed in.
   */
  public NavigatorController(AnchorPane mainPane) {
    this.mainPane = mainPane;
  }

  /**
   * Constructor for the controller which is used when an order has been placed.
   * This enables order tracking information to be displayed.
   *
   * @param mainPane
   *          The main pane for the page to be displayed in.
   * @param done
   *          Determines that the order has been completed.
   */
  public NavigatorController(AnchorPane mainPane, boolean done) {
    this.mainPane = mainPane;
    this.orderSent = done;
  }

  /**
   * Takes the customer to the card payments screen.
   *
   * @param event
   *          The action of the user clicking on the cardPayment button.
   */
  @FXML
  void cardPayment(ActionEvent event) {

    try {
      stopTimer = true;

      FXMLLoader fl = new FXMLLoader(
          getClass().getResource("Payment_welcome.fxml"));
      AnchorPane root = (AnchorPane) fl.load();
      gui.getMainPane().getChildren().clear();
      gui.getMainPane().getChildren().add(root);
      payment.setVisible(false);

    } catch (IOException e) {
      e.printStackTrace();
    }

  }
  /**
   * Calls the waiter so that the customer can pay their bill with cash.
   *
   * @param event
   *          The action of the user clicking on the cashPayment button.
   */
  @FXML
  void cashPayment(ActionEvent event) {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Call Waiter");
    alert.setHeaderText(
        "In order to pay with cash you will need to call a waiter.");
    alert.setContentText("Do you wish to continue?");

    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK) {
      Kiosk.getInstance().callWaiter("Pay bill cash");
      this.payment.setVisible(false);
    }
  }

  /**
   * Checks for changes in the order status.
   */
  private void checkForChangeInStatus() {
    Order orderToCompare = Kiosk.getInstance().getCurrentOrder();
    orderToCompare.getUpdate();
    orderChanges = !(orderToCompare.getStatus() == this.orderStatus);
    this.orderStatus = orderToCompare.getStatus();
  }

  /**
   * Initialiser method which sets up the page and starts the animation timer
   * depending on whether there has been a successful order placed.
   */
  @FXML
  private void initialize() {
    this.gui = GuiView.getInstance();
    this.pane.maxWidthProperty().bind(this.mainPane.widthProperty());
    this.pane.minWidthProperty().bind(this.mainPane.widthProperty());
    this.pane.maxHeightProperty().bind(this.mainPane.heightProperty());
    this.pane.minHeightProperty().bind(this.mainPane.heightProperty());
    this.payment.setVisible(false);
    if (this.orderSent) {
      String timeText = new SimpleDateFormat("HH:mm")
          .format(Kiosk.getInstance().getCurrentOrder().getTime());
      this.time.setAlignment(Pos.CENTER);
      this.time.setText("Ordered at: " + timeText);
      NavigatorController.this.orderStatus = Kiosk.getInstance()
          .getCurrentOrder().getStatus();
      NavigatorController.this.status
          .setText(NavigatorController.this.orderStatus.toString());
      new AnimationTimer() {
        @Override
        public void handle(long now) {
          checkForChangeInStatus();
          if (orderChanges) {
            NavigatorController.this.status
                .setText(NavigatorController.this.orderStatus.toString());
          }
          NavigatorController.this.payment
              .setVisible(NavigatorController.this.orderStatus == Status.SENT);
          if (stopTimer) {
            stop();
          }
        }
      }.start();
    } else {
      this.statusBox.setVisible(false);
    }
  }

  /**
   * Opens the FoodItems page when the button is clicked.
   *
   * @param event
   *          The event which is triggered when the New button is clicked.
   */
  @FXML
  void openFoodItems(ActionEvent event) {
    // opens the foodItems fxml stuff
    try {
      Kiosk.getInstance().newOrder();
      this.stopTimer = true;
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
   * Displays the summary of the order.
   *
   * @param event
   *          The action of the customer clicking on the orderSummary button.
   */
  @FXML
  void orderSummary(ActionEvent event) {
    String summary = "";
    String price;
    String data;
    String poundSterling = new String(Character.toChars(163));
    for (OrderItem item : Kiosk.getInstance().getCurrentOrder()) {
      price = String.format("%s%.2f", poundSterling, item.getPrice() / 100);
      if (item.getComment().length() > 0) {
        data = item.getName() + " (" + item.getComment() + ") " + "x "
            + item.getQuantity();
      } else {
        data = item.getName() + " x " + item.getQuantity();
      }
      summary += data + " " + price + "\n";
    }
    summary += "---------------------------\n";
    summary += "Total Price:\n";
    summary += String.format("%s%.2f", poundSterling,
        Kiosk.getInstance().getCurrentOrder().getPrice() / 100);
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Order Summary");
    alert.setHeaderText("Order Summary");
    alert.setContentText(summary);
    alert.showAndWait();
  }
}
