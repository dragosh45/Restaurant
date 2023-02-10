package customer.gui;

import java.io.IOException;

import customer.Kiosk;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * The controller class to serve Paypal Payment.
 *
 * @author KristinaU
 */

public class PaypalController {

  /**
   * Params serving FXML fields
   */

  @FXML
  private Stage dialogStage;
  private GuiView gui;
  public boolean paid = false;

  /**
   * We assume that at the start of payment stage the order has not been paid
   * yet.
   */

  @FXML
  private AnchorPane pane;
  @FXML
  private TextField paypalAccount;

  @FXML
  private TextField paypalPassword;
  @FXML
  private Label sum;

  /**
   * Handles "Back" button simply closing the paypal payment screen leaving main
   * payment screen.
   *
   */

  @FXML
  private void handleBack() {
    try {
      Stage s = this.gui.getStage();
      s.setTitle("Navigator");
      FXMLLoader fl = new FXMLLoader(
          this.getClass().getResource("Payment_welcome.fxml"));
      AnchorPane root;
      root = (AnchorPane) fl.load();
      this.gui.getMainPane().getChildren().clear();
      this.gui.getMainPane().getChildren().add(root);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Aims to set Order Total Price as a Label
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

  /**
   * Checks that all paypal fields are filled
   *
   * @return isInputValid
   */

  private boolean isInputValid() {
    String errorMessage = "";

    if (this.paypalAccount.getText() == null
        || this.paypalAccount.getText().length() == 0) {
      errorMessage += "No account name!\n";
    }
    if (this.paypalPassword.getText() == null
        || this.paypalPassword.getText().length() == 0) {
      errorMessage += "No password!\n";

    }

    if (errorMessage.length() == 0) {
      return true;
    } else {
      // Show the error message.
      Alert alert = new Alert(AlertType.ERROR);
      alert.initOwner(this.dialogStage);
      alert.setTitle("Invalid PayPal data");
      alert.setHeaderText("Please correct invalid fields");
      alert.setContentText(errorMessage);

      alert.showAndWait();

      return false;
    }

  }

  /**
   * IsPaid returns True if order has been paid
   *
   * Basically returns True iff "Pay" button was pressed and input is valid
   *
   * @return isPaid
   */

  public boolean isPaid() {
    return this.paid;
  }

  /**
   * Payment button, processing payment if all paypal fields are valid
   *
   */

  @FXML
  public void pay() {
    if (this.isInputValid()) {

      System.out.println(this.paid);

      this.paid = true;

      Kiosk.getInstance().getCurrentOrder().setPaid(true);

      Alert alert = new Alert(AlertType.INFORMATION);
      alert.initOwner(this.dialogStage);
      alert.setTitle("Payment successful");
      alert.setHeaderText("Payment successful!");
      alert.setContentText("\nThank you for your payment!\n\nEnjoy your meal!");

      alert.showAndWait();

      System.out.println(this.paid);
      try {
        Stage s = this.gui.getStage();
        s.setTitle("Navigator");
        FXMLLoader fl = new FXMLLoader(
            this.getClass().getResource("Navigator.fxml"));
        NavigatorController controller = new NavigatorController(
            this.gui.getMainPane());
        fl.setController(controller);
        AnchorPane root;
        root = (AnchorPane) fl.load();
        this.gui.getMainPane().getChildren().clear();
        this.gui.getMainPane().getChildren().add(root);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
