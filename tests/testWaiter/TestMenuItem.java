package testWaiter;

import static org.junit.Assert.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;
import backend.Database;
import backend.MenuItem;
import backend.User;

/**
 * This class is a JUnit test class which supplies a suite of tests to test the methods in the
 * MenuItem class. It includes a basic setup method to setup the suite to test with. Methods
 * currently tested in this class: markUnavailable, markAvailable, deleteItem.
 *
 * @author steviebeans
 *
 */
public class TestMenuItem {

  MenuItem menuItemOne; // Mock object made for MenuItem
  MenuItem menuItemTwo;
  User waiter;

  /**
   * Sets up the MenuItem objects to be used in the test class.
   */
  @Before
  public void setUp() {
    this.menuItemOne = new MenuItem(2222, "Cup of Tea", 2f, "Drink", true);
    this.menuItemTwo = new MenuItem(501, "TACOS", 33.3f, "Main", true);
    try {
      this.waiter = new User(0001);
    } catch (Exception e) {
      System.out.println("User does not exist");
      e.printStackTrace();
    }
  }

  /**
   * Tests the deleteItem method in the MenuItem class. Tested by deleting a record and then
   * querying the database to check if the record is still there. Done by querying the database for
   * the record and if the result set of the query returns nothing it means it is not in the
   * database meaning the row was deleted.
   */
  @Test
  public void testDeleteItem() {
    this.menuItemTwo.deleteItem(this.waiter);
    String query = "SELECT PRODUCT_NAME FROM menu WHERE PRODUCT_NAME = 'TACOS';";
    ResultSet rs = Database.getInstance().executeQuery(query);
    try {
      if (!rs.next()) { // If there is no next result set query got nothing
        assertTrue(true);
      } else {
        assertTrue(false);
      }
    } catch (SQLException e) {
      System.out.println(e.toString());
      System.out.println(e.getStackTrace());
    }
  }

  @Test
  public void testMarkAvailable() {
    this.menuItemOne.markAvailable(this.waiter);
    String query = "SELECT PRODUCT_NAME FROM menu WHERE AVAILABILITY = 'Available'"
        + " AND PRODUCT_NAME = 'Cup of Tea';";
    ResultSet rs = Database.getInstance().executeQuery(query);
    String result = null;
    try {
      if (rs.next()) {
        result = rs.getString(1);
        System.out.println(result);
      }
      assertEquals(result, "Cup of Tea");
      assertTrue(this.menuItemOne.isAvailable());
    } catch (SQLException e) {
      System.out.println(e.toString());
      System.out.println(e.getStackTrace());
    }
  }

  /**
   * Tests the markUnavailable method in the MenuItem class by marking the availability of a Cup of
   * Tea to being unavailable, and then executing a query to get the required record from the menu
   * table. The result of this query is compared against the result that it is meant to be.
   */
  @Test
  public void testMarkUnavailable() {
    this.menuItemOne.markUnavailable(this.waiter);
    String query = "SELECT PRODUCT_NAME FROM menu WHERE AVAILABILITY = 'Unavailable'"
        + " AND PRODUCT_NAME = 'Cup of Tea';";
    ResultSet rs = Database.getInstance().executeQuery(query);
    String result = null;
    try {
      if (rs.next()) {
        result = rs.getString(1);
        System.out.println(result);
      }
      assertEquals(result, "Cup of Tea");
      assertFalse(this.menuItemOne.isAvailable());
    } catch (SQLException e) {
      System.out.println(e.toString());
      System.out.println(e.getStackTrace());
    }
  }

  /**
   * Tests the update method in the MenuItem class. Update is called in one of the constructors of
   * MenuItem, that said constructor is called in this test. The attributes of the MenuItem object
   * that has been made are compared to the values that they should be using JUnits assertEquals
   * method.
   */
  @Test
  public void testUpdate() {
    MenuItem menuItemThree = new MenuItem(0);
    assertEquals("Baked beans", menuItemThree.getName());
    assertEquals("MAIN", menuItemThree.getType());
    assertEquals(1.01, menuItemThree.getPrice(), 0001);
    menuItemThree.setName("Not baked beans");
  }

  /**
   * Tests the update method twice, once before a change is made to the attributes in the MenuItem
   * class and the other after a change is made to ensure that update changes the attributes in the
   * MenuItem class correctly. Tested using JUnit assertEquals and assertNotEquals method to compare
   * the attributes in the class before changing an attribute and after updating.
   */
  @Test
  public void testUpdateChangeUpdate() {
    MenuItem menuItemThree = new MenuItem(0);
    assertEquals("Baked beans", menuItemThree.getName());
    menuItemThree.setName("Not baked beans");
    assertNotEquals("Baked beans", menuItemThree.getName());
    menuItemThree.update();
    assertEquals("Baked beans", menuItemThree.getName());
  }

}
