package customer.gui;

import java.io.IOException;

import backend.MenuItem;
import backend.MenuItemList;
import backend.OrderItem;
import customer.Kiosk;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Controller for the product items, enabling users to add items to the order.
 *
 * @author Emily Chaffey
 *
 */
public class ProductController {

  @FXML
  private Label allegens;
  private HBox box;
  @FXML
  private Label calories;
  private HBox column;
  @FXML
  private TextField comments;
  @FXML
  private Label description;
  private GuiView gui;
  @FXML
  private ImageView image;
  private MenuItem item;
  @FXML
  private AnchorPane pane;
  @FXML
  private Label price;
  @FXML
  private Label prodName;
  @FXML
  private Label quantity;
  /**
   * Controller which takes an MenuItem to initialise which product the Product
   * is representing.
   *
   * @param item
   *          The MenuItem which the Product is representing.
   */
  public ProductController(MenuItem item) {
    this.item = item;
  }

  /**
   * Adds a menu item to the menu screen.
   *
   * @param m
   *          The menu item to be added.
   */
  public void addProduct(MenuItem m) {
    ProductUnzoomedController controller = new ProductUnzoomedController(m);
    FXMLLoader fl = new FXMLLoader(
        this.getClass().getResource("ProductUnzoomed.fxml"));
    fl.setController(controller);
    try {
      AnchorPane anchorPane = fl.load();
      this.column.getChildren().add(anchorPane);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Adds the item to the order.
   *
   * @param event
   *          The event triggered when the user clicks the add button.
   */
  @FXML
  void addToOrder(ActionEvent event) {
    // adds product to order.
    MenuItem menuItem = null;
    try {
      menuItem = MenuItemList.getInstance().getItem(this.item.getId());
    } catch (Exception e) {
      e.printStackTrace(); // Item does not exist
    }
    OrderItem orderItem = new OrderItem(menuItem,
        Integer.parseInt(this.quantity.getText()), this.comments.getText());
    Kiosk.getInstance().addItem(orderItem);
  }

  /**
   * Decreases the quantity of the item.
   *
   * @param event
   *          The event triggered when the user clicks the decrease button.
   */
  @FXML
  void decrease(ActionEvent event) {
    int amount = 1;
    try {
      amount = Integer.parseInt(this.quantity.getText());
      if (amount > 1) {
        amount--;
      }
    } catch (NumberFormatException e) {
      // data that is not an int has been entered.
    }
    this.quantity.setText("" + amount);
  }

  //////////////// COPIED FROM FoodItemsController:
  /**
   * Generates the content for the current menu type and the current filter.
   */
  private void generateContent() {
    VBox list = new VBox();
    list.setSpacing(10);
    this.column = new HBox();
    this.column.setSpacing(10);
    int count = 0;
    MenuItemList menu = MenuItemList.getInstance();
    for (MenuItem item : menu.filter(this.gui.getCurrentType(),
        this.gui.getCurrentFilter())) {
      if (count % 4 == 0) {
        list.getChildren().add(this.column);
        this.column = new HBox();
        this.column.setSpacing(10);
      }
      this.addProduct(item);
      count++;
    }
    VBox container = new VBox();
    list.getChildren().add(this.column);
    container.getChildren().add(list);
    this.box.getChildren().clear();
    this.box.getChildren().add(container);
  }

  /**
   * Returns the user back to the Navigator page.
   *
   * @param event
   *          The event triggered when the user clicks on the go back button.
   */
  @FXML
  void goBack(MouseEvent event) {
    this.box = (HBox) this.pane.getParent();
    this.generateContent();
  }

  /**
   * Increases the quantity of the item.
   *
   * @param event
   *          The event triggered when the user clicks on the increase button.
   */
  @FXML
  void increase(ActionEvent event) {
    int amount = 1;
    try {
      amount = Integer.parseInt(this.quantity.getText());
      if (amount < 10) {
        amount++;
      }
    } catch (NumberFormatException e) {
      // data that is not an int has been entered.
    }
    this.quantity.setText("" + amount);
  }

  /**
   * Initialiser method which sets up the product item and its contents.
   */
  @FXML
  private void initialize() {
    this.gui = GuiView.getInstance();
    String poundSterling = new String(Character.toChars(163));
    this.price.setText(String.format("%s%.2f", poundSterling,
        (float) this.item.getPrice() / 100));
    this.description.setText(this.item.getDescription());
    String[] allergensInProduct = this.item.getAllergens();
    String allergenList = "";
    if (allergensInProduct != null) {
      for (String allergen : allergensInProduct) {
        allergenList += allergen + ", ";
      }
      int length = allergenList.length();
      allergenList = allergenList.substring(0, length - 3);
    }
    this.allegens.setText(allergenList);
    this.calories.setText(" ");
    this.prodName.setText(this.item.getName());
    this.quantity.setText("1");
    Image picture;
    try {
      picture = new Image(
          this.getClass()
              .getResourceAsStream("../img/food/" + this.item.getId() + ".jpg"),
          200, 200, false, false);
    } catch (NullPointerException e) {
      picture = new Image(
          this.getClass().getResourceAsStream("../img/default.jpg"), 200, 200,
          false, false);
    }
    this.image.setImage(picture);
  }

}
