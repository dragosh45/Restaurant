package backend;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class is to store information about an order Actions relating to an order can be called here
 * such as deleting, adding to, or changing the status of an Order
 *
 * @author Charles Card
 */
public class Order implements Iterable<OrderItem> {
  private static Database database = Database.getInstance();

  private int id;
  private final ArrayList<OrderItem> orderItems = new ArrayList<OrderItem>();
  private int tableNumber;
  private Timestamp time = null;
  private Status status;
  private boolean paid;


  public Order(int orderId) {
    this.id = orderId;
    this.getUpdate();
  }

  public static Order newOrder(int tableNumber) {
	int id = database.createOrder(tableNumber);
    Order order = new Order(id);
    return order;
  }

  /**
   * Constructor is used to create a new order for a table
   *
   * @param tableNumber The table number at which the person ordering is seated
   */
  public Order(int orderId, int tableNumber, Status status, Timestamp time, boolean paid) {
    this.tableNumber = tableNumber;
    this.time = time;
    this.status = status;
    this.id = orderId;
    this.paid = paid;
  }

  /**
   * This method is used to add a new item to a tables order
   *
   * @param item The item to be added to a tables order
   */
  public void addItem(OrderItem item) {
    OrderItem item2 = null;
    if ((item2 = getOrderItem(item)) != null) {
      item2.increment(item2.getQuantity());
    } else {
      this.orderItems.add(item);
    }
  }

  public OrderItem getOrderItem(OrderItem item) {
    for (OrderItem item2 : orderItems) {
      if (item2.getProduct().equals(item.getProduct())
          && item.getIngredients().equals(item.getIngredients())) {
        return item2;
      }
    }
    return null;
  }

  /**
   * This method is used to apply discounts to an order. e.g. NHS, Military, Emergency Services,
   * Student, or promotions
   */
  public void applyDiscount() {

  }

  /**
   * Confirms an order by updating the status of the order to "Kitchen"
   */
  public void confirm() {
    this.status = Status.KITCHEN;
    Order.database.setStatus(this.getOrderId(), this.status);
  }

  /**
   * Used to find if an item is contained within the List
   *
   * @param item The OrderItem to be checked if it is contained by the ArrayList
   * @return <code>true</code> if ArrayList contains the item; <code>false</code> otherwise.
   */
  public boolean contains(OrderItem item) {
    return this.orderItems.contains(item);
  }

  /**
   * Deletes an Order and all OrderItems associated with the Order
   */
  public void delete() {
    try {
      Order.database.deleteOrder(this.id);
    } catch (DatabaseException e) {
      e.printStackTrace();
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object that) {
    if (this == that) {
      return true; // Same memory space
    }
    if (!(that instanceof Order)) {
      return false; // Same type
    }
    return this.hashCode() == that.hashCode(); // Same contents
  }

  /**
   * Locates the position of an OrderItem within the ArrayList and returns that Object
   *
   * @param id The id of the item being searched for
   * @return The item if found; null otherwise.
   */
  public OrderItem find(int id) { // TODO improve from linear search
    for (OrderItem item : this.orderItems) {
      if (item.getId() == id) {
        return item;
      }
    }
    return null;
  }

  /**
   * Getter to get all of the OrderItems within an Order
   *
   * @return The ArrayList of order Items
   */
  public ArrayList<OrderItem> getList() {
    ArrayList<OrderItem> clone = new ArrayList<OrderItem>(this.orderItems);
    return clone;
  }

  /**
   * Returns the order ID for the Order
   *
   * @return The order ID
   */
  public int getOrderId() {
    return this.id;
  }

  /**
   *
   * @return <code>true</code> if order has been paid for;<code>false</code> otherwise.
   */
  public boolean getPaid() {
    return this.paid;
  }

  /**
   * This method calculates the total price of the order
   *
   * @return The total price of the order
   */
  public float getPrice() {
    float total = 0;
    for (OrderItem item : this.orderItems) {
      total = total + item.getPrice();
    }
    return total;
  }

  /**
   * Returns the status of the order which is stored in the attribute status
   *
   * @return The status of the order
   */
  public Status getStatus() {
    return this.status;
  }

  /**
   * Returns the table number of the order
   *
   * @return The table number of the order
   */
  public int getTableNumber() {
    return this.tableNumber;
  }

  /**
   * Returns the time the order was created
   *
   * @return The time the order was created
   */
  public Timestamp getTime() {
    return this.time;
  }

  /**
   * Updates the Order from the database Gets all OrderItems associated with the order and stores
   * them in an ArrayList
   */
  public void getUpdate() {
    ArrayList<OrderItem> items = null;
    try {
      this.time = Order.database.getOrderTime(this.id);
      this.status = Order.database.getStatus(this.id);
      this.tableNumber = Order.database.getTable(this.id);
      items = Order.database.getOrderUpdate(this.id);
    } catch (DatabaseException e) {
      e.printStackTrace(); // Order does not exist
    }
    for (OrderItem item : this.getList()) {
      if (!items.contains(item)) {
        this.orderItems.remove(item);
      }
    }
    for (OrderItem item : items) {
      if (!this.orderItems.contains(item)) {
        this.orderItems.add(item);
      }
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return this.orderItems.hashCode();
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Iterable#iterator()
   */
  @Override
  public Iterator<OrderItem> iterator() {
    return this.getList().iterator();
  }

  /**
   * Confirms an order by updating the status of the order to "Kitchen"
   */
  public void ready() {
    this.status = Status.READY;
    Order.database.setStatus(this.getOrderId(), this.status);
  }

  /**
   * Removes an item from the list
   *
   * @param item The item to be removed
   */
  public void remove(OrderItem item) {
    this.orderItems.remove(item);
  }

  /**
   * Decreases the item quantity by 1
   *
   * @param item
   */
  public void removeItem(OrderItem item) {
    int quantity = item.getQuantity();
    if (quantity > 1) {
      item.setQuantity(quantity - 1);
    } else {
      this.remove(item);
    }
  }

  /**
   * Sends this order to the database if it doesn't already exist
   */
  public void send() {
    if (this.id == -1) {
      this.id = Order.database.createOrder(this.tableNumber); // Creates the order in the database
    }
    this.setUpdate(); // Adds the OrderItems to the database
    this.getUpdate();
  }

  /**
   * This is called when the order has been sent to the table This updates the status of the order
   * to 'SENT'
   */
  public void sent() {
    this.status = Status.SENT;
    Order.database.setStatus(this.getOrderId(), this.status);
  }

  /**
   * Sets if the order has been paid for or not.
   *
   * @param paid The new value of paid
   */
  public void setPaid(boolean paid) {
    this.paid = paid;
  }

  /**
   * Confirms an order by updating the status of the order to "Kitchen"
   */
  public void setStatusUpdate(Status status) {
    this.status = status;
    Order.database.setStatus(this.getOrderId(), status);
  }

  /**
   * Updates the tableNumber of the order
   *
   * @param tableNumber The tableNumber which will overwrite the existing table number
   */
  public void setTableNumber(int tableNumber) {
    this.tableNumber = tableNumber;
  }

  /**
   * Updates the time the order was created
   *
   * @param timestamp The time which will overwrite the existing time
   */
  public void setTime(Timestamp timestamp) {
    this.time = timestamp;
  }

  /**
   * Updates the database with the OrderItems stored in the orderItemList Also updates the table
   * number and status
   */
  public void setUpdate() {
    Order.database.setOrderUpdate(this.id, this.orderItems, this.tableNumber);
  }

  /**
   * Creates a readable String for the Order Object
   */
  @Override
  public String toString() {
    String result = "Order(ID:" + this.id + ", TABLE:" + this.tableNumber + ")";
    return result;
  }

  /**
   * Confirms an order by updating the status of the order to "Kitchen"
   */
  public void unconfirm() {
    this.status = Status.UNCONFIRMED;
    Order.database.setStatus(this.getOrderId(), this.status);
  }
}
