package customer.gui;

import java.io.IOException;

import backend.MenuItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * Controller for the unzoomed product item.
 *
 * @author Emily Chaffey
 *
 */
public class ProductUnzoomedController {

  @FXML
  private Label allegens;
  @FXML
  private Label description;
  @FXML
  private ImageView image;
  private MenuItem item;
  @FXML
  private AnchorPane pane;
  @FXML
  private Label price;
  @FXML
  private Label prodName;

  /**
   * Controller which takes an MenuItem to initialise which product the Product
   * is representing.
   *
   * @param item
   *          The MenuItem which the Product is representing.
   */
  public ProductUnzoomedController(MenuItem item) {
    this.item = item;
  }

  /**
   * Initialiser method which sets up the product and its contents.
   */
  @FXML
  private void initialize() {
    String poundSterling = new String(Character.toChars(163));
    this.price.setText(String.format("%s%.2f", poundSterling,
        (float) this.item.getPrice() / 100));
    this.description.setText(item.getDescription());
    String[] allergensInProduct = item.getAllergens();
    String allergenList = "";
    if (allergensInProduct != null) {
      for (String allergen : allergensInProduct) {
        allergenList += allergen + ", ";
      }
      int length = allergenList.length();
      allergenList = allergenList.substring(0, length - 3);
    }
    this.allegens.setText(allergenList);
    this.prodName.setText(this.item.getName());
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

  /**
   * When the unzoomed item is clicked it will open up the menu item in a zoomed
   * in version so that the user can add it to their order.
   *
   * @param event
   *          The event triggered when the user clicks on the image of the
   *          product.
   */
  @FXML
  void zoom(MouseEvent event) {
    HBox box = (HBox) this.pane.getParent();
    ProductController controller = new ProductController(this.item);
    FXMLLoader fl = new FXMLLoader(this.getClass().getResource("Product.fxml"));
    fl.setController(controller);
    try {
      AnchorPane anchorPane = fl.load();
      box.getChildren().clear();
      box.getChildren().add(anchorPane);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
