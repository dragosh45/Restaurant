package backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * OrderList stores a list of Orders The Orders are gathered from the database based upon the status
 * of the order A screen on the interface will at any one time only require to see one set of orders
 * such as the current orders page only needing to see orders with a status of preparing
 *
 * @author Charles Card
 *
 */
public class OrderList implements Iterable<Order> {
  private Status[] status;
  private List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
  private int previousHash = -1;

  /**
   * Constructor to create a new OrderList Object
   *
   * @param status The status which all the orders in the list should have
   */
  public OrderList(Status status) {
    this.status = new Status[] {status};
    this.update();
  }

  /**
   * Constructor to create a new OrderList Object
   *
   * @param status The status which all the orders in the list should have
   */
  public OrderList(Status[] status) {
    this.status = status;
    this.update();
  }

  /**
   * Used to find if an order exists within the OrderList
   *
   * @param order The order to find
   * @return <code>true</code> if order is contained within the ArrayList;<code>false</code>
   *         otherwise.
   */
  public boolean contains(Order order) {
    return this.orders.contains(order);
  }

  /**
   * Checks if an orderId is contained within the ArrayList
   *
   * @param id The id to check
   * @return <code>true</code>if list contains order; <code>false</code> otherwise.
   */
  public boolean containsOrder(int id) {
    for (Order order : this.orders) {
      if (order.getOrderId() == id) {
        return true;
      }
    }
    return false;
  }

  /**
   * Gets a list of the current order id's
   *
   * @return A list of the current Order Ids in the ArrayList
   */
  private int[] getIds() {
    int[] result = new int[this.orders.size()];
    for (int i = 0; i < this.orders.size(); i++) {
      result[i] = this.orders.get(i).getOrderId();
    }
    return result;
  }

  /**
   * Getter to get the currently stored list of Orders
   *
   * @return The ArrayList of Orders
   */
  public List<Order> getList() {
    ArrayList<Order> clone = new ArrayList<Order>(this.orders);
    return clone;
  }

  /**
   * Getter to get the status of the group of Orders
   *
   * @return The status for the group of Orders
   */
  public Status[] getStatus() {
    return this.status;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return this.orders.hashCode();
  }

  /**
   * Checks if the OrderSet contains any elements, if it is empty is will return true, otherwise
   * false is returned.
   *
   * @return <code>true</code> if the OrderSet is empty <br>
   *         <code>false</code> otherwise
   */
  public boolean isEmpty() {
    return this.orders.isEmpty();
  }

  /**
   * Checks if any more ready orders have been added to the arraylist of ready orders by comparing
   * the sizeOfOrders field with the current size of arraylist of ready orders. If the current size
   * is bigger, true is returned, otherwise false is returned.
   *
   * @return <code>true</code> if the current size of orders is bigger than the sizeOfOrders field;
   *         <code>false</code>otherwise.
   */
  public boolean isOrderReady() {
    this.update();
    boolean result = false;
    int currentHash = this.orders.hashCode();
    if (currentHash > this.previousHash && this.previousHash != -1) {
      result = true;
    }
    this.previousHash = currentHash;
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Iterable#iterator()
   */
  @Override
  public Iterator<Order> iterator() {
    return this.getList().iterator();
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return this.orders.toString();
  }

  /**
   * Updates the list of Orders from the database with orders which have the same status
   *
   */
  public void update() {
    int[] orderIds = Database.getInstance().getOrders(this.status);
    if (!Arrays.equals(this.getIds(), orderIds)) {
      for (Order order : this.getList()) {
        if (!Arrays.stream(orderIds).boxed().collect(Collectors.toList())
            .contains(order.getOrderId())) {
          this.orders.remove(order);
        }
      }
      for (int id : orderIds) {
        if (!this.containsOrder(id)) {
          this.orders.add(new Order(id)); // add new orders
        }
      }
    }
  }
}
