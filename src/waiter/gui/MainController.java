package waiter.gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import backend.OrderList;
import backend.Status;
import backend.User;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import waiter.Till;

/**
 * This provides the main screen, all other files will be loaded into the correct position within
 * this page This screen provides a small bar at the bottom of the screen which allows users to see
 * their name, position, and the time.
 *
 * @author Charles Card
 *
 */
public class MainController implements Initializable {
  @FXML
  private Label labelName;
  @FXML
  private Label labelPosition;
  @FXML
  private Label labelTime;
  @FXML
  private Pane MainPane; // Contents of this pane will change
  private Till till = null;
  private OrderList orders;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    try {
      this.till = Till.getInstance();
    } catch (Exception e) {
      System.out.println("Programming issue. Till has not been assigned with any money");
      e.printStackTrace();
    }
    User user = this.till.getCurrentUser();
    this.labelName.setText(user.getFirstName() + " " + user.getLastName());
    this.labelPosition.setText(user.getRole().toString());
    this.orders = new OrderList(Status.READY);
    // System.out.println("END");

    new AnimationTimer() {
      @Override
      public void handle(long now) {
        MainController.this.labelTime
            .setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))); // Update
        // clock
        if (MainController.this.orders.isOrderReady()) {
          ErrorAlert waiterAlert = new ErrorAlert("Notification", "Order ready",
              "A order is ready to be delivered to the customer", AlertType.INFORMATION);
          waiterAlert.displayAlert();
        }
      }
    }.start();

    AnchorPane newPane = null;
    try {
      newPane = (AnchorPane) FXMLLoader.load(this.getClass().getResource("OrderSpace.fxml"));
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("That failed");
    }
    this.MainPane.getChildren().add(newPane);
    // Adding the ordering space from fxml file as this child can change
  }

}
