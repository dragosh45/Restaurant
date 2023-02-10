package customer.gui;

import java.io.IOException;
import java.util.Calendar;

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
 * The controller class to serve Card Payment.
 *
 * @author KristinaU
 */

public class CardController {

  @FXML
  private TextField cardNumber;

  @FXML
  private Stage dialogStage;
  @FXML
  private TextField expiryMonth;
  @FXML
  private TextField expiryYear;
  private GuiView gui;
  /**
   * We assume that at the start of payment stage the order has not been paid
   * yet.
   */

  public boolean paid = false;
  /**
   * Params serving FXML fields
   */

  @FXML
  private AnchorPane pane;

  @FXML
  private Label sum;

  /**
   * Handles "Back" button simply closing the card payment screen leaving main
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
   * Assumes to set Order Total price on a Label.
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
   * Checks if input is valid.
   *
   * Checks card number being 16 digits
   *
   * Checks expiry month being a two digits int between 01 and 12
   *
   * Checks expiry year being a two digits int between current year and "current
   * year + 6"
   *
   */

  private boolean isInputValid() {

    int year = Calendar.getInstance().get(Calendar.YEAR) - 2000; // Get current
                                                                 // year to
                                                                 // validate
                                                                 // Card expiry
                                                                 // year.

    String errorMessage = ""; // Create an empty error message to combine the
                              // stack of errors.

    try {
      Long.parseLong(this.cardNumber.getText());
      Integer.parseInt(this.expiryMonth.getText());
      Integer.parseInt(this.expiryYear.getText());
    } catch (NumberFormatException e) {
      errorMessage += "Invalid symbols in numeric enter!\n";

      Alert alert = new Alert(AlertType.ERROR);
      alert.initOwner(this.dialogStage);
      alert.setTitle("Invalid Card data");
      alert.setHeaderText("Please correct invalid fields");
      alert.setContentText(errorMessage);

      alert.showAndWait();

      return false;

    }

    if (this.cardNumber.getText() == null
        || this.cardNumber.getText().length() != 16) { // Check
                                                       // the card
      // number has 16
      // digits
      errorMessage += "Invalid card number!\n";
    }

    if (this.expiryMonth.getText() == null
        || this.expiryMonth.getText().length() != 2 // Check the
                                                    // Month has
        // 2 digits
        || Integer.parseInt(this.expiryMonth.getText()) > 12
        || Integer.parseInt(this.expiryMonth.getText()) < 1
        // Check the Month is not greater than 12 or less than 12.
        || Integer.parseInt(this.expiryYear.getText()) > year + 6
        || Integer.parseInt(this.expiryYear.getText()) < year
        // Check the Year is not less than current or greater than current+6
        || this.expiryYear.getText() == null // Check the Year has 2 digits
        || this.expiryYear.getText().length() != 2) {
      errorMessage += "Invalid expiry date!\n";
    }

    if (errorMessage.length() == 0) {
      return true;
    } else {
      // Show the error message.
      Alert alert = new Alert(AlertType.ERROR);
      alert.initOwner(this.dialogStage);
      alert.setTitle("Invalid Card data");
      alert.setHeaderText("Please correct invalid fields");
      alert.setContentText(errorMessage);

      alert.showAndWait();

      return false;
    }
  }

  /**
   * IsPaid returns True if order has been paid
   *
   * Basically returns True iff "Pay" button was pressed and input was valid
   *
   * @return isPaid
   */

  public boolean isPaid() {
    return this.paid;
  }

  /**
   * Payment button, processing payment if all card fields are valid
   *
   */

  @FXML
  public void pay() {
    if (this.isInputValid()) {

      System.out.println(this.paid); // For internal testing, to clear off in
                                     // production.

      this.paid = true;

      Kiosk.getInstance().getCurrentOrder().setPaid(true);

      Alert alert = new Alert(AlertType.INFORMATION);
      alert.initOwner(this.dialogStage);
      alert.setTitle("Payment successful");
      alert.setHeaderText("Payment successful!");
      alert.setContentText("Thank you for your payment!\n\nEnjoy your meal!");

      alert.showAndWait();

      System.out.println(this.paid); // For internal testing, to clear off in
                                     // production.

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
