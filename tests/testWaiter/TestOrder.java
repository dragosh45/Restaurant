package testWaiter;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import backend.Database;
import backend.Status;
import backend.Type;
import backend.MenuItem;
import backend.MenuItemList;
import backend.Order;
import backend.OrderItem;
import waiter.Till;
import waiter.TillException;
import backend.DatabaseException;

class TestOrder {
  Database db;
  Order orderLocal1;
  Order orderLocal2;
  Order orderLocal3;
  Order orderLocal11;
  OrderItem orderItem1;
  OrderItem orderItem2;
  OrderItem orderItem3;
  OrderItem orderItem4;
  MenuItem menuItem0;
  MenuItem menuItem1;
  MenuItem menuItem2;
  MenuItem menuItem3;
  MenuItem menuItem4;
  MenuItemList menuItemList;
  Till till;
  String lastRow;

  /**
   * Prints current OrderItem table from database and returns the last row
   * 
   * @return last OrderItem table row
   * @throws SQLException
   */
  private String printOrderItemTable() throws SQLException {
    String print;
    String querySelect = "SELECT * FROM OrderItem";
    ResultSet rs = null;
    rs = db.executeQuery(querySelect);
    while (rs.next() == true) {
      print = rs.getInt(1) + " " + rs.getInt(2) + " ";
      System.out.println(print);
      lastRow = rs.getInt(1) + " " + rs.getInt(2) + " ";
    }
    System.out.println();
    return lastRow;
  }

  /**
   * Print current Menu table from database and return the last row
   * 
   * @return last Menu table row
   * @throws SQLException
   */
  private String printMenuTable() throws SQLException {
    String print;
    String querySelect = "SELECT * FROM Menu";
    db.executeQuery(querySelect);
    ResultSet rs = null;
    rs = db.executeQuery(querySelect);
    while (rs.next() == true) {
      print = rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4)
          + " " + rs.getFloat(5) + " ";
      System.out.println(print);
      lastRow = rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4)
          + " " + rs.getFloat(5) + " ";
    }
    System.out.println();
    return lastRow;
  }

  /**
   * Print current Order table from database and return the last row
   * 
   * @return last Order table row
   * @throws SQLException
   */
  private String printOrderTable() throws SQLException {
    System.out.println();
    String print;
    String querySelect = "SELECT * FROM Orders";
    db.executeQuery(querySelect);
    ResultSet rs = null;
    rs = db.executeQuery(querySelect);
    while (rs.next() == true) {
      print = rs.getInt(1) + " " + rs.getInt(2) + " " + rs.getInt(3) + " " + rs.getString(4) + " "
          + rs.getString(5) + " ";
      System.out.println(print);
      lastRow = rs.getInt(1) + " " + rs.getInt(2) + " " + rs.getInt(3) + " " + rs.getString(4) + " "
          + rs.getString(5) + " ";
    }
    System.out.println();
    return lastRow;
  }

  private void printTest(String test) {
    String seperator = new String(new char[10]).replace('\0', '-');
    System.out.println(seperator + test + seperator);
  }

  @BeforeEach
  void setUp() throws TillException {
    try {
      till = Till.getInstance(10.0f);
    } catch (Exception e) {
    }
    db = Database.getInstance();
    db.tableSetup();
    // constructing objects in java which will be compared to the ones in database
    // orderLocal1 the same order as order 1 in the database except OrderItem list which here has
    // menuItem 2 in
    // orderLocal2 different from order 1 in the database
    try {
      menuItem1 = new MenuItem(1, "Baked beans", Type.MAIN, "Beany goodness", 101, true);
      menuItem2 =
          new MenuItem(2, "Cup of Tea", Type.DRINK, "The best hot drink fight me", 200, true);
      menuItem3 = new MenuItem(3, "Pasta", Type.MAIN, "Good carbs", 300, false);
      menuItem4 =
          new MenuItem(4, "Tacos", Type.MAIN, "Description of Tacos goes here", 3330, false);
    } catch (Exception DatabaseException) {
      DatabaseException.printStackTrace();
    }
    orderItem2 = new OrderItem(1, menuItem1, 1);
    orderItem3 = new OrderItem(3, menuItem2, 2);
    orderItem4 = new OrderItem(5, menuItem4, 1);
    orderLocal1 = new Order(1, 1, Status.SENT, Timestamp.valueOf("2018-2-10 13:00:00"), true);
    orderLocal1.addItem(orderItem3);
    orderLocal1.addItem(orderItem2);
    orderLocal2 = new Order(2, 2, Status.READY, Timestamp.valueOf("2018-2-10 13:00:00"), false);
    orderLocal2.addItem(orderItem2);
    orderLocal2.addItem(orderItem3);
    orderLocal2.addItem(orderItem4);
    orderLocal3 =
        new Order(3, 3, Status.UNCONFIRMED, Timestamp.valueOf("2018-2-10 13:00:00"), false);
    orderLocal11 =
        new Order(-1, 5, Status.UNCONFIRMED, Timestamp.valueOf("2018-2-10 13:00:00"), false);
    orderLocal11.addItem(orderItem4);
  }

  /**
   * Tests getUpdate() method from Order class. If getUpdate() is successful the Status, TableNumber
   * and time of a local orderLocal1 will change accordingly to what is stored in the Order table
   * from database. This is showed by printing the current orderLocal1 Status, TableNumber and time
   * before and after update and asserting orderLocal1 TableNumber before and after update
   * 
   * @throws SQLException
   */
  @Test
  void testGetUpdateInOrderTable() throws SQLException, DatabaseException {
    int javaTableNr = orderLocal2.getTableNumber(); // which is 2 local
    int dbTableNr = db.getTable(2); // which is 1 in the database
    printTest("getUpdateFromOrderTable");
    System.out.println("***-Before the update: orderLocal2 table number,status and time is: ");
    System.out.println("Table nr: " + javaTableNr + "  Status: " + orderLocal2.getStatus());
    orderLocal2.getUpdate();
    javaTableNr = orderLocal2.getTableNumber();// change from 2 to 1
    System.out.println();
    System.out.println("***-After the update: orderLocal2 table number,status and time is: ");
    System.out.println("Table nr: " + javaTableNr + "  Status: " + orderLocal2.getStatus());
    assertEquals(dbTableNr, javaTableNr);
  }

  @Test
  /**
   * Tests getUpdate() method from Order class. If getUpdate() is successful the orderItems in the
   * current orderLocal1 list will be updated with what is stored in the table OrderItem from
   * database. This is showed by printing orderLocal1 itemOrder list before and after the update and
   * asserting database OrderItems Ids from Order Id 1 list with the new updated local list in
   * OrderLocal1 OrderItems list. local string: 23, database string: 12
   * 
   * @throws SQLException
   */
  void testGetUpdateInOrderItemTable() throws SQLException, DatabaseException {
    printTest("getUpdateFromOrderItemTable");
    String databaseOrderItemsList = "";
    String localOrderItemsList = "";
    System.out.println("**Table OrderItem has: ");
    // constructing String with database OrderItems Ids from Order Id 1 list
    for (OrderItem orderItem : db.getOrderUpdate(1)) {
      databaseOrderItemsList = databaseOrderItemsList + " " + orderItem.getId();
    }
    System.out.println(databaseOrderItemsList);
    System.out.println();
    orderLocal1.getUpdate();
    System.out.println("**After the update: orderLocal1 Order items are: ");
    // constructing String with local updated OrderItems list
    for (OrderItem orderItem : orderLocal1.getList()) {
      localOrderItemsList = localOrderItemsList + " " + orderItem.getId();
    }
    System.out.println(localOrderItemsList);
    assertEquals(databaseOrderItemsList, localOrderItemsList);
  }

  /**
   * Test setUpdate() method from Order class on database Order table: if setUpdate() is successful
   * the tablenr and OrderItems of Order table will change to the tablenr and OrderItems of
   * OrderLocal2. This is showed by print on console before and after the update and assertion on
   * tablenr before and after the update. localTableNr: 2 , databaseTableNr: 1.
   * 
   * @throws SQLException
   */

  @Test
  void testSetUpdateOnOrderTable() throws SQLException, DatabaseException {
    printTest("setUpdateOnOrderTable");
    int localTableNr = orderLocal2.getTableNumber();
    int databaseTableNr = 0;
    System.out.println("Before the update orderLocal2 tableNr was " + localTableNr + ": ");
    printOrderTable();
    orderLocal2.setUpdate();
    System.out.println("After the update orderLocal2 tableNr is " + databaseTableNr + ": ");
    databaseTableNr = db.getTable(2);
    printOrderTable();
    assertEquals(localTableNr, databaseTableNr);
  }

  /**
   * Test setUpdate() method from Order class on OrderItem table: if setUpdate() is successful the
   * OrderItems from Order Table with Id 1 will be updated accordingly to local Order orderLocal1 This is
   * showed by print on the console before and after the update and asserting the string constructed
   * with OrderItems Ids from Order 2 in Table OrderItems with string constructed with local
   * OrderItems after calling update. localOrder2 OrderItemsIDs string: 135 \ database string
   * OrderItemsIDs: 12 for order ID 2.
   * 
   * @throws SQLException
   */
  @Test
  // Test fail as the setUpdate() does not update from local to database
  void testSetUpdateOnOrderItemTable() throws SQLException, DatabaseException {
    printTest("setUpdateOnOrderItemTable");
    String databaseOrderItemsList;
    String localOrderItemsList;
    orderLocal2.setUpdate();
    localOrderItemsList = orderLocal2.getList().toString();
    databaseOrderItemsList = db.getOrderUpdate(2).toString();
    assertEquals(localOrderItemsList, databaseOrderItemsList);
  }

  /**
   * Test confirm(): orderLocal3 has status Unconfirmed and it's confirmed in the database by
   * changing Order Id 3 status to Kitchen
   * 
   * @throws SQLException
   */
  @Test
  void testConfirm() throws SQLException {
    Status kitchenStatus = Status.KITCHEN;
    orderLocal3.confirm();
    Status databaseStatus = db.getStatus(3);
    assertEquals(kitchenStatus, databaseStatus);
  }

  /**
   * Test delete() method from Order table. If delete() is successful orderLocal1 will be deleted.
   * This is showed by printing Order table before and after the update and asserting if there is a
   * null now at orderLocal1 address
   * 
   * @throws SQLException
   */
  @Test
  // Throws sql exception because of foreign keys in ingredients, I try dissable triggers in psql
  // did not work
  // but it works on the app so don't really know what's happening.
  void testDelete() throws SQLException {
    printTest("Delete");
    System.out.println("---Before delete: Order 1 exists");
    printOrderTable();
    orderLocal1.delete();
    System.out.println("---After delete: Order 1 deleted");
    printOrderTable();
    assertFalse(db.orderExists(orderLocal1.getOrderId()));
  }

  /**
   * Test sent() method from Order table. If sent() is successful Order4 status will change from
   * KITCHEN to SENT. This is showed by printing the Order table before and after the update and
   * asserting the Status "Sent" with the Status after the call
   * 
   * @throws SQLException
   */
  @Test
  void testSent() throws SQLException {
    printTest("Sent");
    Order order4 = new Order(4, 12, Status.KITCHEN, Timestamp.valueOf("2018-2-10 13:05:02"), true);
    System.out.println("---Before sent: Order 4 has status KITCHEN");
    printOrderTable();
    Status statusSent = Status.SENT;
    order4.sent();
    Status afterSentCall = db.getStatus(order4.getOrderId());
    System.out.println("---After sent: Order 4 has status SENT");
    printOrderTable();
    assertEquals(statusSent, afterSentCall);
  }

  @Test
  /**
   * Test contains() method from Order table. If contains() is successful, will return true on an
   * orderItem that is stored in a current order. This is showed by asserting if orderItem3 is in
   * orderLocal1
   * 
   * @throws SQLException
   */
  void testContains() throws SQLException {
    assertEquals(orderLocal1.contains(orderItem3), true);
  }

  @Test
  /**
   * Test addItem() method : asserts if orderItem4 is in orderLocal1 list after calling
   * addItem(orderItem4) method
   */
  void testaddItem() {
    orderLocal1.addItem(orderItem4);
    assertEquals(orderLocal1.contains(orderItem4), true);
  }

  @Test
  /**
   * Test remove() method: asserts if orderItem4 has been removed from orderLocal1 list after
   * calling remove(orderItem3) method
   */
  void testRemove() {
    orderLocal1.remove(orderItem3);
    assertEquals(orderLocal1.contains(orderItem3), false);
  }

  /**
   * Test removeItem() method: asserts if orderItem2 which has quantity 1 will be removed from
   * orderLocal1 list
   */
  @Test
  void testremoveItem1() {
    orderLocal1.removeItem(orderItem2);
    assertEquals(orderLocal1.contains(orderItem2), false);
  }

  @Test
  /**
   * Test removeItem() method: asserts if orderItem3 which has quantity 2 will become 1 after
   * calling removeItem(orderItem3)
   */
  void testremoveItem2() {
    orderLocal1.removeItem(orderItem3);
    assertEquals(orderItem3.getQuantity(), 1);
  }

  @Test
  /**
   * Test send() method: asserts if an order is created when it is send and not in the database
   * 
   */
  void testSend1() {
    orderLocal11.send();
    assertEquals(db.orderExists(orderLocal11.getOrderId()), true);
  }

  /**
   * Test send() method: asserts if and order that exists in the database and locally but with
   * different OrderItems when is Send() updates the local with the database
   */
  @Test
  void testSend2() throws DatabaseException {
    String databaseList = db.getOrderUpdate(2).toString();
    orderLocal2.send();
    assertEquals(orderLocal2.getList().toString(), databaseList);
  }
}
