package waiter.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import backend.MenuItem;
import backend.MenuItemList;
import backend.Type;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import waiter.Till;

/**
 * Allows users to change the availability of menu items
 *
 * @author Charles Card
 *
 */
public class ChangeMenuController implements Initializable {
  @FXML
  private GridPane grid;
  @FXML
  private AnchorPane pane;

  private MenuItemList menuItemList;

  /**
   * Changes the type of menu items being displayed to the desserts
   */
  @FXML
  private void dessertsEvent(ActionEvent event) {
    this.setGrid(Type.DESSERT);
  }

  /**
   * Changes the type of menu items being displayed to the drinks
   */
  @FXML
  private void drinksEvent(ActionEvent event) {
    this.setGrid(Type.DRINK);
  }

  /**
   *
   * @param event
   */
  @FXML
  private void homeEvent(ActionEvent event) {
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
    this.menuItemList = MenuItemList.getInstance();
    this.setGrid(Type.MAIN);
  }

  /**
   * Changes the type of menu items being displayed to the mains
   */
  @FXML
  private void mainEvent(ActionEvent event) {
    this.setGrid(Type.MAIN);
  }

  /**
   * Changes what menu items are being displayed
   *
   * @param type The type of menu item to be displayed
   */
  private void setGrid(Type type) {
    this.grid.getChildren().clear();
    Button tempButton;
    int i = 0;
    for (MenuItem menuItem : MenuItemList.getInstance()) {
      if (menuItem.getType() == type) {
        tempButton = new Button(menuItem.getName());
        tempButton.prefWidthProperty().bind(this.grid.widthProperty());
        tempButton.prefHeightProperty().bind(this.grid.heightProperty());
        tempButton.setId(String.valueOf(menuItem.getId()));
        if (!menuItem.isAvailable()) {
          tempButton.setStyle("-fx-background-color: gray;");
        }
        tempButton.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            ChangeMenuController.this.toggleAvailableEvent(event);
            if (!menuItem.isAvailable()) {
              ((Button) event.getSource()).setStyle("-fx-background-color: gray;");
            } else {
              ((Button) event.getSource()).setStyle(null);
            }
          }
        });
        this.grid.add(tempButton, i % 5, Math.floorDiv(i, 5));
        i = i + 1;
      }
    }
    // TODO pages if the menu overflows - Sprint 3.
  }

  /**
   * Changes the type of menu items being displayed to the starters
   */
  @FXML
  private void startersEvent(ActionEvent event) {
    this.setGrid(Type.STARTER);
  }

  /**
   * Toggles the availability of the MenuItem which was clicked on
   *
   * @param event
   */
  @FXML
  private void toggleAvailableEvent(ActionEvent event) {
    String id = ((Button) event.getSource()).getId();
    try {
      this.menuItemList.getItem(Integer.valueOf(id))
          .toggleAvailability(Till.getInstance().getCurrentUser());
    } catch (NumberFormatException e) {
      // Bad fx:id which shouldn't be thrown
      e.printStackTrace();
    } catch (Exception e) {
      ErrorAlert alert =
          new ErrorAlert("Error", "Insufficient permissions", e.getMessage(), AlertType.ERROR);
      alert.displayAlert();
    }
  }
}
