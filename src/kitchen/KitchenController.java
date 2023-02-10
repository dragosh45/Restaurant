package kitchen;

import java.io.IOException;
/**
 * <h3>KitchenController</h3> The KitchenController class is what controls any actions that get
 * undertaken by the user in the GUI and makes the relevant changes.
 *
 * @author Justus Marius Mueller (100862809)
 * @version 0.2
 */
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import backend.Order;
import backend.OrderList;
import backend.Status;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class KitchenController {

  public static Stage stage;
  static Order info;

  public static Stage getStage() {
    return stage;
  }

  private CustomOrderController orderController;
  private FXMLLoader loader;

  Parent root;

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private Button refreshButton;

  @FXML
  private Button managerButton;

  @FXML
  private GridPane aGridPane;

  private int x = 0, y = 0;

  private boolean space = true;
  private Status status = Status.KITCHEN;
  private OrderList orders = new OrderList(status);
  private ArrayList<Order> displayed = new ArrayList<Order>();

  @FXML
  void initialize() throws IOException, InterruptedException {
    new AnimationTimer() {
      @Override
      public void handle(long now) {
        orders.update();
        if (x < 5 && y < 2) {
          displayed = populate(orders, displayed);
          System.out.println("size of storage = " + displayed.size());
          for (int i = 0; i < displayed.size(); i++) {
            loader = new FXMLLoader(this.getClass().getResource("customOrder.fxml"));
            info = displayed.get(i);
            newItem(space, x, y, loader, info);
            orderController = loader.getController();

            System.out.println("x = " + x + " y = " + y + " size of storage = " + i);
            
            if (y == 1 && x == 4) {
              System.out.println("TOO MUCH");
              space = false;
            } else if (x < 4 && space == true) {
              x++;
              space = true;
            } else {
              x = 0;
              y++;
              space = true;
            }
          }
          x = 0;
          y = 0;
          displayed.clear();
        } else {
          System.out.println("OUT OF BOUNDS");
        }
      }
    }.start();
  }

  @FXML
  void managerPopUp(ActionEvent event) {
    try {
      stage = new Stage();
      this.root = FXMLLoader.load(this.getClass().getResource("managerPopUp.fxml"));
      stage.setScene(new Scene(this.root));
      stage.setTitle("Manager Interface");
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.initOwner(this.managerButton.getScene().getWindow());
      stage.showAndWait();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  void newItem(boolean space, int x, int y, FXMLLoader loader, Order order) {
    if (space == true) {
      try {
        this.aGridPane.add(loader.load(), x, y);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @FXML
  void refresh(ActionEvent event) {
    this.aGridPane.getChildren().clear();
    this.x = 0;
    this.y = 0;
    this.space = true;
    this.displayed.clear();
  }

  private ArrayList<Order> populate(OrderList orders, ArrayList<Order> storage) {
    this.displayed.clear();
    for (Order order : orders) {
      storage.add(order);
    }
    return storage;
  }
}
