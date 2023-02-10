package customer.gui;

import java.io.IOException;

import backend.Filter;
import backend.MenuItem;
import backend.MenuItemList;
import backend.Type;
import customer.Kiosk;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Controller for the page which displays the menus/filters.
 *
 * @author Emily Chaffey
 *
 */
public class FoodItemsController {

  private boolean change = true;
  private HBox column;
  @FXML
  private VBox container;
  @FXML
  private VBox filterBox;
  @FXML
  private GridPane grid;
  private GuiView gui;
  private VBox list;
  @FXML
  private AnchorPane mainPane;
  private boolean menuChanges = false;
  private int menuOnScreen = 0;
  @FXML
  private AnchorPane pane;
  private boolean stopTimer = false;
  private boolean viewMenuState = false;

  /**
   * Adds a filter to the screen.
   *
   * @param filter
   *          The filter to be added.
   */
  private void addFilter(Filter filter) {
    VBox content = new VBox();
    content.setAlignment(Pos.CENTER);
    String filterName = filter.toString();
    filterName = filterName.charAt(0)
        + filterName.substring(1, filterName.length()).toLowerCase();
    Label label = new Label(filterName);
    label.setAlignment(Pos.CENTER);
    label.setFont(new Font(14.0));
    String fileName = filter.toString().toLowerCase();
    if (fileName.equals("hot")) {
      fileName += "_" + this.gui.getCurrentType().toString().toLowerCase();
    }
    ImageView imageView = getImage("../img/filter/" + fileName + ".jpg", 150);
    imageView.addEventFilter(MouseEvent.MOUSE_PRESSED,
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent mouseEvent) {
            FoodItemsController.this.gui.setCurrentFilter(filter);
            // display filtered menu.
            FoodItemsController.this.generateContent();
            FoodItemsController.this.viewMenuState = true;
          }
        });
    content.getChildren().add(label);
    content.getChildren().add(imageView);
    this.column.getChildren().add(content);
  }

  /**
   * Adds a menu item to the menu screen.
   *
   * @param item
   *          The menu item to be added.
   */
  private void addProduct(MenuItem item) {
    ProductUnzoomedController controller = new ProductUnzoomedController(item);
    FXMLLoader fl = new FXMLLoader(
        this.getClass().getResource("ProductUnzoomed.fxml"));
    fl.setController(controller);
    try {
      AnchorPane root = fl.load();
      this.column.getChildren().add(root);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Takes the user to the basket when the button is clicked.
   *
   * @param event
   *          The event which is triggered when the user clicks on the view
   *          order button.
   */
  @FXML
  void basketEvent(ActionEvent event) {
    try {
      Stage stage = this.gui.getStage();
      stage.setTitle("Basket");
      FXMLLoader fl = new FXMLLoader(
          this.getClass().getResource("Basket.fxml"));
      AnchorPane root = (AnchorPane) fl.load();
      this.gui.getMainPane().getChildren().clear();
      this.gui.getMainPane().getChildren().add(root);
      this.stopTimer = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Cancels the order and takes the user to the Navigator page.
   *
   * @param event
   *          The even which is triggered when the user clicks on the cancel
   *          order button.
   */
  @FXML
  void cancelEvent(ActionEvent event) {
    try {
      Kiosk.getInstance().newOrder();
      Stage stage = this.gui.getStage();
      stage.setTitle("Navigator");
      FXMLLoader fl = new FXMLLoader(
          this.getClass().getResource("Navigator.fxml"));
      NavigatorController controller = new NavigatorController(
          this.gui.getMainPane());
      fl.setController(controller);
      AnchorPane root = (AnchorPane) fl.load();
      this.gui.getMainPane().getChildren().clear();
      this.gui.getMainPane().getChildren().add(root);
      this.stopTimer = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Checks to see if there have been any alterations to the menu.
   */
  private void checkForMenuChanges() {
    MenuItemList menuToCompare = MenuItemList.getInstance();
    menuToCompare.update();
    menuChanges = !(menuToCompare.hashCode() == this.menuOnScreen);
    this.menuOnScreen = menuToCompare.hashCode();
  }

  /**
   * Determines which filters are needed for the current menu type.
   *
   * @return An array of the filters for that menu type.
   */
  private Filter[] determineFiltersForMenus() {
    if (this.gui.getCurrentType() == Type.MAIN) {
      Filter[] filters = {Filter.ALL, Filter.VEGETARIAN, Filter.VEGAN};
      return filters;
    } else if (this.gui.getCurrentType() == Type.DESSERT) {
      Filter[] filters = {Filter.ALL, Filter.SWEET, Filter.SAVOURY, Filter.HOT,
          Filter.COLD,};
      return filters;
    } else {
      // menuSelected == Type.DRINKS
      Filter[] filters = {Filter.ALL, Filter.HOT, Filter.SOFT,
          Filter.ALCOHOLIC};
      return filters;
    }
  }

  /**
   * Generates the content for the current menu type and filter.
   */
  private void generateContent() {
    this.container.getChildren().clear();
    this.list = new VBox();
    this.list.setSpacing(10);
    this.column = new HBox();
    MenuItemList menu = MenuItemList.getInstance();
    this.column.setSpacing(10);
    menu.update();
    int count = 0;
    for (MenuItem item : menu.filter(this.gui.getCurrentType(),
        this.gui.getCurrentFilter())) {
      if (count % 4 == 0) {
        this.list.getChildren().add(this.column);
        this.column = new HBox();
        this.column.setSpacing(10);
      }
      this.addProduct(item);
      count++;
    }
    this.list.getChildren().add(this.column);
    this.container.getChildren().add(this.list);
  }

  /**
   * Generates the filters for the selected menu.
   */
  private void generateFiltersForMenus() {
    this.list = new VBox();
    this.list.setSpacing(10);
    this.column = new HBox();
    this.column.setSpacing(10);
    int count = 0;
    Filter[] filters = this.determineFiltersForMenus();
    for (Filter filter : filters) {
      if (count % 4 == 0) {
        this.list.getChildren().add(this.column);
        this.column = new HBox();
        this.column.setSpacing(10);
      }
      this.addFilter(filter);
      count++;
    }
    this.list.getChildren().add(this.column);
    this.container.getChildren().add(this.list);
  }

  /**
   * Generates the menu types (i.e. Mains, Drinks and Desserts).
   */
  private void generateTypeFilters() {
    String[] imageFiles = {"../img/Mains.jpg", "../img/Drinks.jpg",
        "../img/Desserts.jpg"};
    for (int i = 0; i < imageFiles.length; i++) {
      ImageView imageView = getImage(imageFiles[i], 200);
      if (i == 0) {
        imageView.addEventFilter(MouseEvent.MOUSE_PRESSED,
            new EventHandler<MouseEvent>() {
              @Override
              public void handle(MouseEvent mouseEvent) {
                FoodItemsController.this.gui.setCurrentType(Type.MAIN);
                FoodItemsController.this.change = true;
                FoodItemsController.this.viewMenuState = false;
              }
            });
      } else if (i == 1) {
        imageView.addEventFilter(MouseEvent.MOUSE_PRESSED,
            new EventHandler<MouseEvent>() {
              @Override
              public void handle(MouseEvent mouseEvent) {
                FoodItemsController.this.gui.setCurrentType(Type.DRINK);
                FoodItemsController.this.change = true;
                FoodItemsController.this.viewMenuState = false;
              }
            });
      } else {
        imageView.addEventFilter(MouseEvent.MOUSE_PRESSED,
            new EventHandler<MouseEvent>() {
              @Override
              public void handle(MouseEvent mouseEvent) {
                FoodItemsController.this.gui.setCurrentType(Type.DESSERT);
                FoodItemsController.this.change = true;
                FoodItemsController.this.viewMenuState = false;
              }
            });
      }
      this.filterBox.getChildren().add(imageView);
    }
  }

  /**
   * Creates an ImageView containing the image (if it exists) or the default
   * image (if it doesn't exit).
   *
   * @param source
   *          The source of the image that is to be loaded into the ImageView.
   * @param size
   *          The size of the image.
   * @return An ImageView containing the image.
   */
  private ImageView getImage(String source, int size) {
    Image image;
    try {
      image = new Image(this.getClass().getResourceAsStream(source), size, size,
          false, false);
    } catch (NullPointerException e) {
      image = new Image(
          this.getClass().getResourceAsStream("../img/default.jpg"), size, size,
          false, false);
    }
    ImageView imageView = new ImageView();
    imageView.setImage(image);
    return imageView;
  }

  /**
   * Initialiser method which does the setup for the filters/FoodItem content to
   * be displayed.
   */
  @FXML
  private void initialize() {
    // setup for the filters/FoodItems content to be displayed.
    this.gui = GuiView.getInstance();

    this.pane.maxWidthProperty().bind(this.gui.getMainPane().widthProperty());
    this.pane.minWidthProperty().bind(this.gui.getMainPane().widthProperty());
    this.pane.maxHeightProperty().bind(this.gui.getMainPane().heightProperty());
    this.pane.minHeightProperty().bind(this.gui.getMainPane().heightProperty());

    this.grid.maxWidthProperty().bind(this.pane.widthProperty());
    this.grid.minWidthProperty().bind(this.pane.widthProperty());

    ColumnConstraints column = this.grid.getColumnConstraints().get(0);
    column.setPercentWidth(17);
    column = this.grid.getColumnConstraints().get(1);
    column.setPercentWidth(83);

    this.gui.setCurrentType(Type.MAIN);
    this.generateTypeFilters();

    new AnimationTimer() {

      @Override
      public void handle(long now) {
        if (FoodItemsController.this.change) {
          FoodItemsController.this.container.getChildren().clear();
          FoodItemsController.this.generateFiltersForMenus();
          FoodItemsController.this.change = false;
        }
        if (FoodItemsController.this.viewMenuState) {
          FoodItemsController.this.checkForMenuChanges();
          if (FoodItemsController.this.menuChanges) {
            FoodItemsController.this.generateContent();
            FoodItemsController.this.menuChanges = false;
          }
        }
        if (FoodItemsController.this.stopTimer) {
          this.stop();
        }
      }
    }.start();
  }
}
