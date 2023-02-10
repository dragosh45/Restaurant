package testKitchen;

import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.Test;
import kitchen.Orders;

class TestOrders {

  @Test
  void getOrderCooked() {
    Orders.changeOrderStatus(true);
    assertTrue(Orders.getOrderCooked() == true);
  }

  @Test
  void testSetOrderCooked() {
    Orders.changeOrderStatus(true);
    assertTrue(Orders.getOrderCooked() == true);
  }
}
