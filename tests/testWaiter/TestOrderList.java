package testWaiter;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import backend.Database;
import backend.OrderList;

class TestOrderList {
  Database db;
  OrderList orderlist;

  private void printOrderTable() throws SQLException {
    System.out.println();
    String print;
    String querySelect = "SELECT * FROM Orders";
    this.db.executeQuery(querySelect);
    ResultSet rs = null;
    rs = this.db.executeQuery(querySelect);
    while (rs.next() == true) {
      print = rs.getInt(1) + " " + rs.getInt(2) + " " + rs.getInt(3) + " " + rs.getString(4) + " "
          + rs.getString(5) + " ";
      System.out.println(print);
    }
    System.out.println();
  }

  @BeforeEach
  void setUp() throws Exception {
    this.db = Database.getInstance();
  }

  /*
   * TestsUpdate tests the orderList to be updated accordingly to the database or the database
   * accordingly with the java (orderlist) How: A.(UPDATED OrderList) printing in console the table
   * Orders changes if updates from java to db B.(NOT UPDATED OrderList) printing in console the
   * status of order items from OrderList if updates from db to java
   */
  @Test
  void testUpdate() throws SQLException {

  }
}
