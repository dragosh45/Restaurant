package waiter.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import backend.Order;
import backend.OrderList;
import backend.Status;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import waiter.Till;
import waiter.TillException;

/**
 *
 * @author Charles Card
 *
 */
public class CurrentOrdersController implements Initializable {
  @FXML
  private TableView<OrderProperty> tableView;
  @FXML
  private TableColumn<OrderProperty, Integer> tableCol;
  @FXML
  private TableColumn<OrderProperty, String> timeCol, statusCol;
  @FXML
  private AnchorPane pane;

  private int previousOrdersHash = 0;

  private OrderList orders = new OrderList(Status.READY);
  private boolean stopTimer = false;
  private Till till;

  /**
   *
   * @param event
   */
  @FXML
  private void deleteEvent(ActionEvent event) {
    Order order = this.till.getOrder();
    order.delete(); // Remove from database
    this.till.setOrder(null); // Remove from till
  }

  /**
   *
   * @param event
   */
  @FXML
  private void editOrderEvent(ActionEvent event) {
    AnchorPane newPane = null;
    try {
      newPane = (AnchorPane) FXMLLoader.load(this.getClass().getResource("MenuItem.fxml"));
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("That failed");
    }

    AnchorPane root = (AnchorPane) this.pane.getParent();
    root.getChildren().clear();
    root.getChildren().add(newPane);
  }

  /**
   *
   * @param event
   */
  @FXML
  private void homeEvent(ActionEvent event) {
    this.stopTimer = true;
    AnchorPane newPane = null;
    try {
      newPane = (AnchorPane) FXMLLoader.load(this.getClass().getResource("MainMenu.fxml"));
    } catch (IOException e) {
      System.out.println("That failed");
      e.printStackTrace();
    }
    AnchorPane root = (AnchorPane) this.pane.getParent();
    root.getChildren().clear();
    root.getChildren().add(newPane);
  }

  /*
   * (non-Javadoc)
   *
   * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    try {
      this.till = Till.getInstance();
    } catch (TillException e) {
      System.out.println("Error with Till in CurrentOrderController");
      e.printStackTrace();
    }

    // This adds a listener to the tableView, changed is called every time the
    // tableView is clicked
    this.tableView.getSelectionModel().selectedItemProperty()
        .addListener(new ChangeListener<OrderProperty>() {
          @Override
          public void changed(ObservableValue<? extends OrderProperty> arg0, OrderProperty arg1,
              OrderProperty arg2) {
            if (arg2 != null) {
              CurrentOrdersController.this.till.setOrder(arg2.getOrder());
            }
          }
        });

    // May need to be changed or updated every 5 seconds? Lag machine?
    new AnimationTimer() { // This will update the order being displayed every frame
      @Override
      public void handle(long now) {
        CurrentOrdersController.this.updateCurrentOrders();
        if (CurrentOrdersController.this.stopTimer) {
          this.stop();
        }
      }
    }.start();
  }

  /**
   *
   * @param event
   */
  @FXML
  private void payEvent(ActionEvent event) {
    // TODO Change screen to pay for the order. Sprint 3.
  }

  /**
   *
   * @param event
   */
  @FXML
  private void sentEvent(ActionEvent event) {
    Order order = this.till.getOrder();
    order.sent();
    this.till.setOrder(null); // Remove from till
  }

  /**
   *
   */
  private void updateCurrentOrders() {
    this.orders.update();
    int currentHash = this.orders.hashCode();
    if (currentHash != this.previousOrdersHash) {
      this.previousOrdersHash = currentHash;
      this.tableView.getItems().clear();
      for (Order order : this.orders) {
        this.tableView.getItems().add(new OrderProperty(order));
      }
    }
  }
}
