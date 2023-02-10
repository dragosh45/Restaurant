package waiter.gui;

import backend.OrderItem;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class OrderItemProperty {
  private OrderItem item;

  private final StringProperty name = new SimpleStringProperty();
  private final StringProperty price = new SimpleStringProperty();

  public OrderItemProperty(OrderItem item) {
    this.item = item;
    this.update();
  }

  /**
   * Returns the name property
   *
   * @return The name property
   */
  public final String getName() {
    return this.name.get();
  }

  /**
   * Gets the order item of which this is a property for
   *
   * @return The OrderItem
   */
  public OrderItem getOrderItem() {
    return this.item;
  }

  /**
   * Returns the price property
   *
   * @return The price property
   */
  public final String getPrice() {
    return this.price.get();
  }

  /**
   * Returns the name property
   *
   * @return The name property
   */
  public final StringProperty nameProperty() {
    return this.name;
  }

  /**
   * Returns the price property
   *
   * @return The price property
   */
  public final StringProperty priceProperty() {
    return this.price;
  }

  /**
   * Updates the properties based on the OrderItem
   */
  public void update() {
    if (this.item.getQuantity() > 1) {
      this.name.set("(" + this.item.getQuantity() + ") " + this.item.getName());
    } else {
      this.name.set(this.item.getName());
    }
    this.price.set(String.format("\u00A3%.2f", this.item.getPrice() / 100));
  }
}
