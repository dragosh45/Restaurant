package waiter.gui;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;

import backend.AdjustMenu;
import backend.Database;
import backend.Type;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

/**
 * This class is the class for the AdjustMenu screen. It will control all the backend for when the 
 * manager wants to use the AdjustMenu part of the GUI to adjust the menu.
 * @author Stephen House
 *
 */
public class AdjustMenuController implements Initializable {
  @FXML
  private TextField menuItemField;
  @FXML
  private TextField idField;
  @FXML
  private TextField nameField;
  @FXML
  private TextArea descriptionArea;
  @FXML
  private RadioButton starterTypeRadioButton;
  @FXML
  private RadioButton mainTypeRadioButton;
  @FXML
  private RadioButton dessertTypeRadioButton;
  @FXML
  private RadioButton drinkTypeRadioButton;
  @FXML
  private TextField priceField;
  @FXML
  private TextField approxTimeField;
  @FXML
  private Button submitButton;
  @FXML
  private Button cancelButton;
  @FXML
  private AnchorPane pane;
  @FXML
  private ToggleGroup menuType;
  private AdjustMenu adjustMenu;
  private Database database;

  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {
    database = Database.getInstance();
    adjustMenu = new AdjustMenu();
    starterTypeRadioButton.setUserData("STARTER");
    mainTypeRadioButton.setUserData("MAIN");
    dessertTypeRadioButton.setUserData("DESSERT");
    drinkTypeRadioButton.setUserData("DRINK");  
  }

  /**
   * When the submitButton is pressed, this method is called which gets
   * all the relevant information from the text fields and turns them into variables
   * and changes the database if and only if the id entered in the idField exists
   * in the database - Can't alter a product if it does not exist.
   * If it does not exist, an error message is displayed using the ErrorAlert class.
   * @param event
   */
  @FXML
  private void submitChangesEvent(ActionEvent event) {
	//checkIfTextFieldsEmpty();
	printMenu();
	if (menuItemField.getText().isEmpty()) {
	  ErrorAlert emptyAlert = new ErrorAlert("Error",
			  "Menu Item Text Field is empty", "The Menu Item Text Field cannot be empty",
			  AlertType.ERROR);
	  emptyAlert.displayAlert();
	}
    int id = Integer.parseInt(menuItemField.getText());
    if (database.doesMenuItemExist(id)) {
      //Gets the data from the textFields and stores them in fields in an AdjustMenu object.
      adjustMenu.retrieveName(nameField.getText(), id);
      adjustMenu.retrieveID(id, id);
      adjustMenu.retrieveDescription(descriptionArea.getText(), id);
      adjustMenu.retrievePrice(priceField.getText(), id);
      adjustMenu.retrieveApproxTime(approxTimeField.getText(), id);
      adjustMenu.setType(menuType.getSelectedToggle().getUserData().toString(), id);      
      adjustMenu.makeAdjustments(); //Makes the adjustments to the database.
      printMenu();
    } else {
      ErrorAlert idDoesNotExistAlert = new ErrorAlert("Error",
                "Product ID does not exist", "The product ID you have entered does not exist"
			  	     + ", please input one that does exist to alter it", AlertType.ERROR);
      idDoesNotExistAlert.displayAlert();
    }
  }
  
  public void printMenu() {
    ResultSet rs = database.executeQuery("SELECT * FROM product;");
    try {
      while(rs.next()) {
        System.out.println("ID: " + rs.getString(1));
        System.out.println("NAME: " + rs.getString(2));
        System.out.println("Meal type:" + rs.getString(3));
        System.out.println("Desc: " + rs.getString(4));
        System.out.println("Price: " + rs.getInt(5));
        System.out.println("Approx Time: " + rs.getString(7));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * When the cancel button is pressed this method is called, which will return the user
   * from the AdjustMenu screen part of the GUI to the MainMenu part of the GUI.
   * @param event
   */
  @FXML
  private void cancelEvent(ActionEvent event) {
    AnchorPane newPane = null;
    try { 
      newPane = (AnchorPane) FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
    } catch (IOException e) {
      System.out.println(e.toString());
      System.out.println(e.getStackTrace());
    }
    AnchorPane root = (AnchorPane) pane.getParent();
    root.getChildren().clear();
    root.getChildren().add(newPane);
  }
}
