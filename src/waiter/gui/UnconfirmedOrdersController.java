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

public class UnconfirmedOrdersController implements Initializable {
  @FXML
  private TableView<OrderProperty> tableView;
  @FXML
  private TableColumn<OrderProperty, Integer> tableCol;
  @FXML
  private TableColumn<OrderProperty, String> timeCol, statusCol;
  @FXML
  private AnchorPane pane;

  private OrderList orders = new OrderList(Status.UNCONFIRMED);
  private boolean stopTimer = false;
  private Till till;
  private int previousOrdersHash = 0;

  /**
   *
   * @param event
   */
  @FXML
  private void confirmOrderEvent(ActionEvent event) {
    this.till.getOrder().confirm();
    this.till.setOrder(null);
  }

  /**
   *
   * @param event
   */
  @FXML
  private void deleteOrderEvent(ActionEvent event) {
    this.till.getOrder().delete();
    this.till.setOrder(null);
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

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      this.till = Till.getInstance();
    } catch (TillException e) {
      e.printStackTrace(); // Till hasn't been setup
    }

    // This adds a listener to the tableView, changed triggers every time the
    // tableView is clicked
    this.tableView.getSelectionModel().selectedItemProperty()
        .addListener(new ChangeListener<OrderProperty>() {
          @Override
          public void changed(ObservableValue<? extends OrderProperty> arg0, OrderProperty arg1,
              OrderProperty arg2) {
            if (arg2 != null) {
              UnconfirmedOrdersController.this.till.setOrder(arg2.getOrder());
            }
          }
        });

    // May need to be changed or updated every 5 seconds? Lag machine?
    new AnimationTimer() { // This will update the order being displayed every frame
      @Override
      public void handle(long now) {
        UnconfirmedOrdersController.this.updateUnconfirmedOrders();
        if (UnconfirmedOrdersController.this.stopTimer) {
          this.stop();
        }
      }
    }.start();
  }

  /**
   *
   */
  private void updateUnconfirmedOrders() {
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
