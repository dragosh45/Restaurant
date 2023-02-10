package customer.gui;

import java.util.Optional;

import backend.OrderItem;
import customer.Kiosk;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller class for an item in the basket.
 *
 * @author Emily Chaffey
 *
 */
public class BasketItemController {

  @FXML
  private TextField comments;
  @FXML
  private ImageView image;
  private OrderItem orderItem;
  @FXML
  private AnchorPane pane;
  @FXML
  private Label price;
  @FXML
  private Label prodName;
  @FXML
  private Label quantity;

  /**
   * Controller which takes an OrderItem to initialise which order the
   * BasketItem is representing.
   *
   * @param orderItem
   *          The OrderItem which the BasketItem is representing.
   */
  public BasketItemController(OrderItem orderItem) {
    this.orderItem = orderItem;
  }

  /**
   * Decreases the quantity of the product in the order. If the quantity reaches
   * 0 it is removed from the order/basket.
   *
   * @param event
   *          The event which is triggered when the user clicks on the decrease
   *          button.
   */
  @FXML
  void decrease(ActionEvent event) {
    int quantity = this.orderItem.getQuantity();
    if (quantity > 1) {
      this.orderItem.remove();;
    }
    this.quantity.setText(String.valueOf(this.orderItem.getQuantity()));
    if (quantity == 1) {
      Alert alert = new Alert(AlertType.CONFIRMATION);
      alert.setTitle("Removal of Order Item");
      alert.setHeaderText("This item will no longer be in the order.");
      alert.setContentText("Do you wish to continue?");

      Optional<ButtonType> result = alert.showAndWait();
      if (result.get() == ButtonType.OK) {
        Kiosk.getInstance().getCurrentOrder().remove(this.orderItem);
        VBox box = (VBox) this.pane.getParent();
        box.getChildren().remove(this.pane);
      }
    }
  }

  /**
   * Increases the quantity of the product in the order.
   *
   * @param event
   *          The event which is triggered when the user clicks on the increase
   *          button.
   */
  @FXML
  void increase(ActionEvent event) {
    int quantity = this.orderItem.getQuantity();
    if (quantity < 10) {
      this.orderItem.increment();
    }
    this.quantity.setText(String.valueOf(this.orderItem.getQuantity()));
  }

  /**
   * The initialiser method which sets up the BasketItem according to the
   * OrderItem it represents.
   */
  @FXML
  private void initialize() {
    String poundSterling = new String(Character.toChars(163));
    this.price.setText(String.format("%s%.2f", poundSterling,
        (float) this.orderItem.getProduct().getPrice() / 100));
    this.prodName.setText(this.orderItem.getName());
    this.quantity.setText(String.valueOf(this.orderItem.getQuantity()));
    Image picture;
    try {
      picture = new Image(
          this.getClass().getResourceAsStream(
              "../img/food/" + this.orderItem.getProduct().getId() + ".jpg"),
          200, 200, false, false);
    } catch (NullPointerException e) {
      picture = new Image(
          this.getClass().getResourceAsStream("../img/default.jpg"), 200, 200,
          false, false);
    }
    this.image.setImage(picture);
    if (this.orderItem.getComment().length() > 0) {
      this.comments.setText(this.orderItem.getComment());
    }
  }

  /**
   * Removes the BasketItem from the basket/order when the cross is clicked.
   *
   * @param e
   *          The mouse event which is triggered when the user clicks on the
   *          cross image.
   */
  @FXML
  void remove(MouseEvent e) {
    Kiosk.getInstance().getCurrentOrder().remove(this.orderItem);
    VBox box = (VBox) this.pane.getParent();
    box.getChildren().remove(this.pane);
  }

  /**
   * Updates the comments of the order item.
   *
   * @param event
   *          The event of the user typing in the text field.
   */
  @FXML
  void updateComments(KeyEvent event) {
    this.orderItem.setComment(this.comments.getText());
  }

}
