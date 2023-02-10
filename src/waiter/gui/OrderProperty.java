package waiter.gui;

import backend.Order;
import backend.Status;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class OrderProperty {
  private Order order;
  private final IntegerProperty tableNumber = new SimpleIntegerProperty();
  private final StringProperty time = new SimpleStringProperty();
  private final StringProperty status = new SimpleStringProperty();

  OrderProperty(Order order) {
    this.order = order;
    this.update();
  }

  public Order getOrder() {
    return this.order;
  }

  /**
   * Returns the status of the order which is stored in the attribute status
   *
   * @return The status of the order
   */
  public final Status getStatus() {
    return Status.valueOf(this.status.get());
  }

  /**
   * Returns the table number of the order
   *
   * @return The table number of the order
   */
  public final int getTableNumber() {
    return this.tableNumber.get();
  }

  /**
   * Returns the time the order was created
   *
   * @return The time the order was created
   */
  public final String getTime() {
    return this.time.get();
  }

  /**
   * Returns the status property
   *
   * @return The status property
   */
  public final StringProperty statusProperty() {
    return this.status;
  }

  /**
   * Returns the tableNumber property
   *
   * @return The tableNumber property
   */
  public final IntegerProperty tableNumProperty() {
    return this.tableNumber;
  }

  /**
   * Returns the creation time property
   *
   * @return The creation time property
   */
  public final StringProperty timeProperty() {
    return this.time;
  }

  public void update() {
    this.tableNumber.set(this.order.getTableNumber());
    this.time.set(this.order.getTime().toString());
    this.status.set(this.order.getStatus().toString());
  }
}
