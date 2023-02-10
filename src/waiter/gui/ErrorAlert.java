package waiter.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;

/**
 * This class creates a java fx alert message to be disokayed which can be customised to the
 * situation that it is in with the paramaters passed into the constructor. This class is a
 * singleton and as such contains such methods to do this. This class includes three methods, a
 * constructor called ErrorAlert, getInstance and displayAlert.
 *
 * @author Stephen House
 *
 */
public class ErrorAlert {
  private String title;
  private String headerText;
  private String contentText;
  private AlertType type;

  /**
   * A constructor which sets the fields title, headerText, contextText and type to the values
   * passed into the constructor.
   *
   * @param title A string passed into the constructor to be the title of the alert
   * @param headerText A string of text to be header text of the alert
   * @param contentText A string of text to be context text of the alert
   * @param type An AlertType which is the type of javafx alert to be made.
   */
  public ErrorAlert(String title, String headerText, String contentText, AlertType type) {
    this.title = title;
    this.headerText = headerText;
    this.contentText = contentText;
    this.type = type;
  }

  /**
   * Makes the alert to the values specified in the fields of the class, and then displays the alert
   * on the window at the top level above the current GUI.
   */
  public void displayAlert() {
    Alert alert = new Alert(this.type);
    alert.initOwner(GuiView.getInstance().getCurrentStage()); // Makes the alert display at the very
                                                              // top level of the window
    alert.initModality(Modality.WINDOW_MODAL);
    alert.setTitle(this.title);
    alert.setHeaderText(this.headerText);
    alert.setContentText(this.contentText);
    alert.show();
  }

  /**
   * Gets the current String stored in the String field, type.
   *
   * @return The AlertType stored in the field type.
   */
  public AlertType getAlertType() {
    return this.type;
  }

  /**
   * Gets the current String stored in the String field, contentText.
   *
   * @return The String stored in the field contentText.
   */
  public String getContentText() {
    return this.contentText;
  }

  /**
   * Gets the current String stored in the String field, headerText.
   *
   * @return The String stored in the field headerText.
   */
  public String getHeaderText() {
    return this.headerText;
  }

  /**
   * Gets the current title stored in the String field, title.
   *
   * @return The String stored in the field title.
   */
  public String getTitle() {
    return this.title;
  }

}
