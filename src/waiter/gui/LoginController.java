package waiter.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import backend.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import waiter.Till;



// TODO check if money is in the till, if not then assume being setup by
// Manager,
// after correct manager code ask for how much is in the till and open the till
// so the new draw can be inserted
/**
 * The login page is used to authenticate users and check permission levels
 *
 * @author Charles Card
 */
public class LoginController implements Initializable {
  @FXML
  private Label labelNumber;

  /**
   * Clears the currently typed in login number
   *
   * @param event
   */
  @FXML
  private void clearEvent(ActionEvent event) {
    this.labelNumber.setText(""); // Clears the label of its contents
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {}

  /**
   * Authenticates the login number exists, if it does exist assigns them to be the user of the till
   * Allows users to access functions for their permission level
   *
   * @param event
   */
  @FXML
  private void loginEvent(ActionEvent event) {
    String number = this.labelNumber.getText();
    int loginNumber = -1;
    try {
      if (number.length() > 0) {
        loginNumber = Integer.valueOf(number); // Get the login number that was typed in
      }
    } catch (Exception e) {
      System.out.println("Error occured in LoginController Class");
      e.printStackTrace();
    }
    User user = null;
    boolean isValidUser = true;
    try {
      user = new User(loginNumber); // The constructor authenticates the users existence
    } catch (Exception e) {
      isValidUser = false;
      ErrorAlert alert =
          new ErrorAlert("ERROR", "Incorrect Login Number", e.getMessage(), AlertType.INFORMATION);
      alert.displayAlert();
    }
    if (isValidUser) { // If user has been authenticated assign them as the current user and load
                       // the Main page
      Till till = null;
      try {
        till = Till.getInstance();
      } catch (Exception e) {
        System.out.println("Till has not been assigned any money");
        e.printStackTrace();
      }
      till.setCurrentUser(user); // Assign the user as the tills current user
      Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      Scene scene = null;
      try {
        scene = new Scene((Parent) FXMLLoader.load(this.getClass().getResource("Main.fxml")), 1920,
            1080);
      } catch (IOException e) {
        System.out.println("Missing file: Main.fxml");
        e.printStackTrace();
      }
      boolean fullScreen = currentStage.isFullScreen(); // As there is a bug in javafx, the window
                                                        // will not retain the fullscreen state
      currentStage.setScene(scene);
      currentStage.setFullScreen(fullScreen);
    }
  }

  /**
   * Adds the pressed number to the end of the currently typed in login number
   */
  @FXML
  private void numberEvent(ActionEvent event) {
    Button button = (Button) event.getSource(); // The Button which was pressed
    String old = this.labelNumber.getText(); // Gets the text currently stored in the label javafx
                                             // change
    // scene fullscreen
    if (old.length() < 16) { // Just because longer than this is really unnecessary
      String next = button.getId(); // The number pressed to be added to the label
      String current = old + next; // Concatenates the old value with the value of the button which
                                   // was just pressed
      this.labelNumber.setText(current); // Sets the text of the label to the updated value
    }
  }
}
