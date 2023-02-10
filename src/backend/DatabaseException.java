package backend;

/**
 * Called when there is an error in the database class
 *
 * @author Charles Card
 */

public class DatabaseException extends Exception {
  private static final long serialVersionUID = -1428273698734423691L;

  /**
   *
   * @param message The message to be displayed when raised
   */
  public DatabaseException(String message) {
    super(message);
  }

}
