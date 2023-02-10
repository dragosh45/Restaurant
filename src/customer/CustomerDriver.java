package customer;

import customer.gui.GuiView;

/**
 * The class which is run to start the application.
 *
 * @author Emily Chaffey
 */
public class CustomerDriver {

  /**
   * The main of the application.
   *
   * @param args The arguments parsed when the application is run.
   */
  public static void main(String[] args) {
    GuiView.getInstance();
  }
}
