package waiter;

/**
 * Called when there is an error on the Till
 *
 * @author Charles Card
 */

public class TillException extends Exception {
  private static final long serialVersionUID = -2300089865091168325L;

  /**
   *
   * @param message The message to be displayed when raised
   */
  public TillException(String message) {
    super(message);
  }

}
