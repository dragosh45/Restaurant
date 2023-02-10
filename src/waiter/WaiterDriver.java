package waiter;

import backend.Database;
import backend.MenuItemList;
import waiter.gui.GuiView;

/**
 * Driver which is used to create the till instance and start the GUI in a new thread
 *
 * @author Charles Card
 */
public class WaiterDriver {
  /**
   * Starts the application, but is also a useful testing ground for testing the database and how it
   * interacts with the methods I have written so far
   */
  public static void main(String[] args) {
    Database db = Database.getInstance();
    db.tableSetup();
    System.out.println(db.getTime());
    try {
      Till.getInstance(0.0f);
    } catch (TillException e) {
      e.printStackTrace();
    }
    MenuItemList.getInstance();
    GuiView.getInstance();
  }
}
