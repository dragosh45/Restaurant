package waiter.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import backend.Order;
import backend.OrderItem;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import waiter.Till;
import waiter.TillException;

/**
 * This page displays a table on the left hand side to show the currently selected item It also
 * provides space on the right hand side to display a menu which can be loaded in from another file
 *
 * @author Charles Card
 *
 */
public class OrderSpaceController implements Initializable {

  @FXML
  private AnchorPane menuSpace; // Contents will change in this pane
  @FXML
  private TableView<OrderItemProperty> tableView;
  @FXML
  private TableColumn<OrderItemProperty, String> nameCol;
  @FXML
  private TableColumn<OrderItemProperty, Float> priceCol;
  @FXML
  private Label labelTableNumber;

  private Till till;
  private boolean stopTimer = false;
  private int previousOrder = 0;
  private int previousHash = 0;

  /**
   * Scrolls down
   */
  @FXML
  private void downEvent(ActionEvent event) {
    System.out.println("DOWN");
    // TODO Scrolling
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    try {
      this.till = Till.getInstance();
    } catch (TillException e) {
      e.printStackTrace();
    }

    AnchorPane newPane = null;
    try {
      newPane = (AnchorPane) FXMLLoader.load(this.getClass().getResource("MainMenu.fxml"));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    this.menuSpace.getChildren().add(newPane);
    // Adding the menu of functions for a user

    this.tableView.getSelectionModel().selectedItemProperty()
        .addListener(new ChangeListener<OrderItemProperty>() {
          @Override
          public void changed(ObservableValue<? extends OrderItemProperty> arg0,
              OrderItemProperty arg1, OrderItemProperty arg2) {
            if (arg2 != null) {
              OrderSpaceController.this.till.setItem(arg2.getOrderItem());
            }
          }
        });

    new AnimationTimer() { // This updates the order being displayed every frame
      @Override
      public void handle(long now) {
        if (OrderSpaceController.this.stopTimer) {
          this.stop();
        }
        OrderSpaceController.this.updateView();
      }
    }.start();
  }

  public void stop() {
    this.stopTimer = true;
  }

  /**
   * Updates the order items being displayed
   */
  private void updateView() {
    Order order = this.till.getOrder();
    if (order == null) {
      this.tableView.getItems().clear();
    } else {
      if (order.getOrderId() != this.previousOrder) {
        this.previousOrder = order.getOrderId();
        order.getUpdate();
      }
      int currentHash = order.hashCode();
      if (this.previousHash != currentHash) {
        this.previousHash = currentHash;
        this.labelTableNumber.setText(String.valueOf(order.getTableNumber())); // Set table number
                                                                               // to label
        this.tableView.getItems().clear();
        for (OrderItem item : order) {
          this.tableView.getItems().add(new OrderItemProperty(item));
        }
      }
    }
  }

  /**
   * Scrolls up
   */
  @FXML
  private void upEvent(ActionEvent event) {
    System.out.println("UP");
    // TODO Scrolling
  }

}
