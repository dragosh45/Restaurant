package waiter.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import backend.DatabaseException;
import backend.MenuItem;
import backend.MenuItemList;
import backend.OrderItem;
import backend.Type;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import waiter.Till;
import waiter.TillException;

/**
 * This page allows a user to see all the current menu items the page is split up into different
 * sections for Main, Starters, desserts, and drinks they can all be accessed by clicking on the
 * corresponding buttons
 *
 * @author Charles Card
 */

public class MenuItemController implements Initializable {
  @FXML
  private GridPane grid;
  @FXML
  private AnchorPane pane;

  private Till till;

  @FXML
  private void commentEvent(ActionEvent event) {
    // TODO add comment to MenuItemOrder
  }

  /**
   * This removes the currently selected item in the currently selected order
   */
  @FXML
  private void deleteEvent(ActionEvent event) {
    this.till.getOrder().removeItem(this.till.getItem());
  }

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
  private void duplicateEvent(ActionEvent event) {
    //this.till.getItem().duplicate();
    this.till.getItem();
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

  /**
   *
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    try {
      this.till = Till.getInstance();
    } catch (TillException e) {
      e.printStackTrace();
    }
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
   *
   * @param event
   */
  @FXML
  private void modifyAddEvent(ActionEvent event) {
    // TODO Sprint 3/4 When dealing with ingredients
  }

  /**
   *
   * @param event
   */
  @FXML
  private void modifyRemoveEvent(ActionEvent event) {
    // TODO Sprint 3/4 When dealing with ingredients
  }

  /**
   * Changes what menu items are being displayed
   *
   * @param type The type of menu item to be displayed
   */
  private void setGrid(Type type) {
    this.grid.getChildren().clear();
    Button button;
    int i = 0;
    for (MenuItem menuItem : MenuItemList.getInstance()) {
      if (menuItem.getType() == type) {
        button = new Button(menuItem.getName());
        button.prefWidthProperty().bind(this.grid.widthProperty());
        button.prefHeightProperty().bind(this.grid.heightProperty());
        button.setId(String.valueOf(menuItem.getId()));
        button.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            if (MenuItemController.this.till.getOrder() != null) {
              MenuItem item = null;
              try {
                item = new MenuItem(Integer.valueOf(((Button) event.getSource()).getId()));
              } catch (NumberFormatException e) {
                e.printStackTrace();
              } catch (DatabaseException e) {
                e.printStackTrace();
              }
              MenuItemController.this.till.getOrder().addItem(new OrderItem(item, 1));
            } else {
              System.out.println("No order to add items to");
              // TODO throw new exception
            }
          }
        });
        this.grid.add(button, i % 5, Math.floorDiv(i, 5));
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
}
