package customer.gui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import customer.Kiosk;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * Controller class for the main header for the program.
 *
 * @author Emily Chaffey
 *
 */
public class MainBarController {

  @FXML
  private AnchorPane containerPane;
  @FXML
  private AnchorPane mainPane;
  @FXML
  private Label time;

  /**
   * Calls the waiter with the predetermined options.
   *
   * @param event
   *          The event triggered when the user clicks on the call waiter
   *          button.
   */
  @FXML
  void callWaiter(ActionEvent event) {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Waiter");
    alert.setHeaderText("Why would you like to call a waiter?");
    alert.setContentText("Please select an option.");

    ButtonType askQuestion = new ButtonType("Ask Quesiton");
    ButtonType changeOrder = new ButtonType("Change Order");
    ButtonType confirmOrder = new ButtonType("Confirm Order");
    ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

    alert.getButtonTypes().setAll(askQuestion, changeOrder, confirmOrder,
        cancel);

    Optional<ButtonType> result = alert.showAndWait();

    if (result.get() == confirmOrder) {
      Kiosk.getInstance().callWaiter("Confirm order");
    } else if (result.get() == changeOrder) {
      Kiosk.getInstance().callWaiter("Change order");
    } else if (result.get() == askQuestion) {
      Kiosk.getInstance().callWaiter("Ask question");
    }

    if (result.get() != cancel) {
      String timeText = new SimpleDateFormat("HH:mm")
          .format(Kiosk.getInstance().getWaiterTime());
      this.time.setAlignment(Pos.CENTER);
      this.time.setText("Called at: " + timeText);
    }
  }

  /**
   * Initialiser method which sets up the page and its content.
   */
  @FXML
  private void initialize() {
    Kiosk.getInstance().newOrder();
    GuiView gui = GuiView.getInstance();
    this.mainPane.maxWidthProperty().bind(this.containerPane.widthProperty());
    this.mainPane.minWidthProperty().bind(this.containerPane.widthProperty());
    this.mainPane.maxHeightProperty().bind(this.containerPane.heightProperty());
    this.mainPane.minHeightProperty().bind(this.containerPane.heightProperty());
    try {
      FXMLLoader fl = new FXMLLoader(
          this.getClass().getResource("Navigator.fxml"));
      NavigatorController controller = new NavigatorController(this.mainPane);
      fl.setController(controller);
      AnchorPane root = (AnchorPane) fl.load();
      this.mainPane.getChildren().clear();
      this.mainPane.getChildren().add(root);
      gui.setMainPane(this.mainPane);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
