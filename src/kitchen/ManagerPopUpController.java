package kitchen;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ManagerPopUpController {

  @FXML
  private ResourceBundle resources;

  @FXML
  private URL location;

  @FXML
  private Button backButton;

  @FXML
  private Button closeButton;

  @FXML
  void back(ActionEvent event) {
    Stage stage = KitchenController.getStage();
    stage.close();
  }

  @FXML
  void close(ActionEvent event) {
    System.exit(0);
  }

  @FXML
  void initialize() {
    assert this.backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'managerPopUp.fxml'.";
    assert this.closeButton != null : "fx:id=\"closeButton\" was not injected: check your FXML file 'managerPopUp.fxml'.";
  }
}
