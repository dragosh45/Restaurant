package kitchen;

/**
 * <h3>CustomOrderController</h3> The CustomOrderController class is what controls any actions that
 * get undertaken by the user on the customOrder aspect of the GUI.
 *
 * @author Justus Marius Mueller (100862809)
 * @version 0.2
 */
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import backend.OrderItem;
import backend.Status;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class CustomOrderController {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private Button cancelButton;

  @FXML
  private Button completeButton;

  @FXML
  private AnchorPane customOrder;

  @FXML
  private TextArea orderDetails;

  @FXML
  private Label orderStatus;
  
  @FXML
  private TextArea orderTime;

  private String temp = "----------------=========Order=========----------------";
  private String name;
  private String comment;
  private Timestamp time;

  private int quantity;
  private ArrayList<OrderItem> items;
  private Iterator<OrderItem> iterate;
  private OrderItem item;

  @FXML
  void cancelOrder(ActionEvent event) {
    KitchenController.info.unconfirm();
  }

  @FXML
  void completeOrder(ActionEvent event) {
    KitchenController.info.ready();
  }

  @FXML
  public void fillText() {
    this.items = KitchenController.info.getList();
    this.iterate = this.items.iterator();
    while (this.iterate.hasNext()) {
      this.item = this.iterate.next();
      this.name = this.item.getName();
      this.comment = this.item.getComment();
      this.quantity = this.item.getQuantity();

      this.temp = this.temp + "\n" + this.name + "\t\t\t\t\t\t\t Quantity = " + this.quantity
          + "\n    - " + this.comment + "\n";
    }
    this.orderDetails.setText(this.temp);
  }
  
  @FXML
  public void fillTime() {
    this.time = KitchenController.info.getTime();
    this.orderTime.setText("Order was placed at: " + time);
  }

  @FXML
  void initialize() {
    this.fillText();    
    this.fillTime();

    new AnimationTimer() {
      @Override
      public void handle(long now) {
        CustomOrderController.this.orderStatusEnquiry(KitchenController.info.getStatus());
      }
    }.start();
  }

  void orderStatusEnquiry(Status status) {
    if (status.equals(Status.KITCHEN)) {
      this.orderStatus.setStyle("-fx-background-color: blue;");
    } else if (status.equals(Status.READY)) {
      this.orderStatus.setStyle("-fx-background-color: green;");
    } else if (status.equals(Status.UNCONFIRMED)) {
      this.orderStatus.setStyle("-fx-background-color: red;");
    }
  }
}
