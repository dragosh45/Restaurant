package testWaiter;

import static org.junit.Assert.*;
import org.junit.Test;
import javafx.scene.control.Alert.AlertType;
import waiter.gui.ErrorAlert;

public class TestErrorAlert {

  /**
   * Tests the constructor and getInstance methods of the ErrorAlert class to check if the
   * infomation passed into the getInstance method goes to the Constructor and an instance of
   * ErrorAlert is created with the correct values stored in the fields of ErrorAlert.
   */
  @Test
  public void testErrorAlertConstructor() {
    String title = "Test title";
    String headerText = "Test header text";
    String contentText = "Test content text";
    AlertType type = AlertType.INFORMATION;

    ErrorAlert testAlert = ErrorAlert.getInstance(title, headerText, contentText, type);
    assertEquals(title, testAlert.getTitle());
    assertEquals(headerText, testAlert.getHeaderText());
    assertEquals(contentText, testAlert.getContentText());
    assertEquals(type, testAlert.getAlertType());
  }
}
