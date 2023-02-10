package customer;

import java.sql.Timestamp;
import backend.Database;
import backend.Order;
import backend.OrderItem;

public class Kiosk {
  private static Kiosk instance = null;

  /**
   * Getter to get the instance of the Kiosk as this is a singleton class
   *
   * @return The instance of the Object
   */
  public static Kiosk getInstance() {
    if (instance == null) {
      instance = new Kiosk();
    }
    return instance;
  }

  private Order currentOrder = null;
  private Database database;
  private int tableNumber = 4; // TODO read from tableNumber.txt

  private Timestamp waiterTime = null;

  /**
   * Constructor to construct a new Kiosk Object
   */
  private Kiosk() {
    this.database = Database.getInstance();
  }

  /**
   * Adds a new OrderItem to the current order
   *
   * @param item The OrderItem to be added
   */
  public void addItem(OrderItem item) {
    this.currentOrder.addItem(item);
  }

  /**
   * Edits the database to allow the waiters to know if a customer requires assistance
   *
   * @param request The request message to the waiter
   */
  public void callWaiter(String request) {
    this.waiterTime = this.database.getTime();
    this.database.setAssist(this.tableNumber, request);
  }

  /**
   * Removes the currently stored order
   */
  public void cancelOrder() {
    this.currentOrder = null;
  }

  /**
   * Decrease the quantity of an OrderItem, delete the OrderItem if it has 0 quantity
   *
   * @param item The item to be removed
   */
  public void duplicateItem(OrderItem item) {
    item.increment();
  }

  /**
   * Getter to get the currently stored order
   *
   * @return The current order
   */
  public Order getCurrentOrder() {
    return this.currentOrder;
  }

  /**
   * Getter to get the time the waiter was called from the table
   *
   * @return The time the waiter was called
   */
  public Timestamp getWaiterTime() {
    return this.waiterTime;
  }

  /**
   * Creates a new Order object which OrderItems can be added to
   */
  public void newOrder() {
    this.currentOrder = Order.newOrder(this.tableNumber);
  }

  /**
   * Decrease the quantity of an OrderItem, delete the OrderItem if it has 0 quantity
   *
   * @param item The item to be removed
   */
  public void removeItem(OrderItem item) {
    this.currentOrder.remove(item);
  }

  /**
   * Sends the current order to the Database where it can be confirmed by a waiter
   */
  public void sendOrder() {
    this.currentOrder.send();
  }
}
