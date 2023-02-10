package waiter;

import backend.Order;
import backend.OrderItem;
import backend.User;

/**
 * Till is a singleton as no physical Till will require holding information about multiple tills
 * This object stores information about the till, it also tracks how much money is stored in the
 * till draw
 *
 * @author Charles Card
 */

public final class Till {
  private static Till instance;

  /**
   * Used to get the instance of Till
   *
   * @return The only instance of Till
   * @throws Exception Thrown in the case that there is an attempt to construct the Till object
   *         without any money
   */
  public static Till getInstance() throws TillException {
    if (instance == null) {
      throw new TillException("Trying to create instance of Till without setting amount of money.");
    }
    return instance;
  }

  /**
   * Used primarily when constructing a new Till object to set its starting amount of money
   *
   * @param startMoney The amount of money which the till object will be created will
   * @return The only instance of Till
   * @throws TillException
   */
  public static Till getInstance(float startMoney) throws TillException {
    if (instance == null) {
      instance = new Till(startMoney); // TODO add else and throw Exception
    } else {
      throw new TillException("Attempting to setup the till after already being created");
    }
    return instance;
  }

  private float expectedMoney;
  private float startMoney;
  private User currentUser;

  private Order currentOrder = null; // A currently stored order

  private OrderItem currentItem = null;

  /**
   * Constructs a new till object
   *
   * @param startMoney The amount of money the till is created with
   */
  protected Till(float startMoney) {
    this.startMoney = startMoney;
    this.expectedMoney = startMoney;
  }

  /**
   * This is to calculate the difference between what is expected to be in the till and what
   * actually is stored in the till (Theft/bad counting)
   *
   * @param actualMoney To be manually entered by a manager, the actual amount of money stored in
   *        the till
   */
  public void calculateDifference(float actualMoney) {
    System.out.println(this.expectedMoney - actualMoney); // Money lost from theft/change issues
    // TODO
  }

  /**
   * This method is used to calculate the total expected turnover earned through this till
   *
   */
  public void calculateTurnover() {
    System.out.println(this.expectedMoney - this.startMoney); // Money earned so far
    // TODO
  }

  /**
   * Getter to get the current user using the till (Used later to check permission levels)
   *
   * @return The current user of the Till
   */
  public User getCurrentUser() {
    return this.currentUser;
  }

  /**
   * Getter to get the current item stored in the till
   *
   * @return The current item
   */
  public OrderItem getItem() {
    return this.currentItem;
  }

  /**
   * Getter to get the current order stored in the till
   *
   * @return The current order
   */
  public Order getOrder() {
    return this.currentOrder;
  }

  /**
   * This method is used to "open the till" As this project will not be used/tested on any physical
   * till it will just print to the console that the till opened The Till cannot continue if the
   * cash draw is left open
   *
   */
  private void openTill() {
    System.out.println("Till Opened");
    // TODO Add check that till was closed in order to continue
    boolean tillOpen = false;
    while (tillOpen) {
    } // Cannot continue unless the draw is closed
      // This is a place holder method as there is not a real till which can be opened
  }

  /**
   * Check if the current user has sufficient permissions to open the till if they do open the till
   * for them, else display an error message
   *
   * @param Reason The reason the manager needs to open the till e.g. cash-up, change error,
   *        etcetera
   */
  public void openTillManager(String Reason) {
    if (this.currentUser.getRole().getPermissionLevel() >= 2) { // At least management
      this.openTill();
      // TODO wait until till is closed, implement method for this?
      // TODO Log this in the database
    } else {
      // TODO Error message, probably should return a boolean if draw opened
    }
  }

  /**
   * This method is used so a table can pay for their order, this then updates the database
   *
   * @param order The order being paid for
   */
  public void payForOrder(Order order) {
    this.expectedMoney = this.expectedMoney + order.getPrice();
    // TODO update database
    // TODO Card/Cash, calculate change etc.
  }

  /**
   * This method will set a new user of the till when someone logs into it using their id
   *
   * @param currentUser The user to be set as the current user of this till
   */
  public void setCurrentUser(User currentUser) {
    this.currentUser = currentUser;
  }

  /**
   * Setter to set the currently selected item
   *
   * @param order The new current item selected by the till
   */
  public void setItem(OrderItem item) {
    if (this.currentOrder != null) {
      if (this.currentOrder.contains(item)) {
        this.currentItem = item;
      }
    } // TODO throw exceptions
  }

  /**
   * Setter to set the current order
   *
   * @param order The new current order to be displayed by the till
   */
  public void setOrder(Order order) {
    this.currentOrder = order;
    this.currentItem = null;
  }

}
