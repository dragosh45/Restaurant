package customer.gui;

import java.io.IOException;

import backend.OrderItem;
import customer.Kiosk;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * The controller class for main Payment screen.
 *
 * @author KristinaU
 *
 */

public class WelcomeController {

  private GuiView gui;

  @FXML
  private AnchorPane pane;

  @FXML
  private Stage stage;

  @FXML
  private Label sum;

  /**
   * Handles "Back to order" button simply closing the payment screen.
   *
   */
  @FXML
  private void handleBack() {
    try {
      Stage s = this.gui.getStage();
      s.setTitle("Navigator");
      FXMLLoader fl = new FXMLLoader(
          this.getClass().getResource("Navigator.fxml"));
      NavigatorController controller = new NavigatorController(
          this.gui.getMainPane(), true);
      fl.setController(controller);
      AnchorPane root;
      root = (AnchorPane) fl.load();
      this.gui.getMainPane().getChildren().clear();
      this.gui.getMainPane().getChildren().add(root);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Handles Pay with card button opening new screen to pay with a card.
   *
   */
  @FXML
  private void handleCard() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(this.getClass().getResource("Payment_card.fxml"));
      AnchorPane root = (AnchorPane) loader.load();
      this.gui.getMainPane().getChildren().clear();
      this.gui.getMainPane().getChildren().add(root);
      // Create the dialog Stage.
      Stage stage = gui.getStage();
      stage.setTitle("Card payment");

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
  void handleOrderInfo(ActionEvent event) {
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

  /**
   * Handles Pay with paypal button opening new screen to pay with paypal.
   *
   */
  @FXML
  private void handlePaypal() {
    try {

      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(this.getClass().getResource("Payment_paypal.fxml"));
      AnchorPane root = (AnchorPane) loader.load();
      this.gui.getMainPane().getChildren().clear();
      this.gui.getMainPane().getChildren().add(root);
      // Create the dialog Stage.
      Stage stage = gui.getStage();
      stage.setTitle("PayPal payment");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method aims to set Total order price to show to a customer.
   *
   */
  @FXML
  private void initialize() {
    this.gui = GuiView.getInstance();
    this.pane.maxWidthProperty().bind(this.pane.widthProperty());
    this.pane.minWidthProperty().bind(this.pane.widthProperty());
    this.pane.maxHeightProperty().bind(this.pane.heightProperty());
    this.pane.minHeightProperty().bind(this.pane.heightProperty());
    this.sum.setText(String.format("%.2f",
        Kiosk.getInstance().getCurrentOrder().getPrice() / 100));
  }

}
