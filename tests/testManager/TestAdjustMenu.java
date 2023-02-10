package testManager;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import backend.AdjustMenu;
import backend.Database;

public class TestAdjustMenu {
	
  /**
   * Tests the retrieveName method when the new name is passed into the method, i.e.
   * when the textField for the name has an entry, and then compares the result of the
   * retrieveName method, stored in a private field called name,
   * to the actual result which should be Baked beans due that being the name
   * passed into the method. Done using JUnits assertEquals method and getters in
   * the AdjustMenu class.
   */
  @Test
  public void testRetrieveName() {
	Database db = Database.getInstance();
    db.tableSetup();
    AdjustMenu aM = new AdjustMenu(); 
    aM.retrieveName("Baked beans", 0);
    assertEquals("Baked beans", aM.getName());
  }
  
  /**
   * Tests the retrieveName method when nothing is passed in to the method, i.e. when
   * the textField for the name is empty, and then compares the result of retrieveName, stored
   * in a private field called name,
   * to the actual result which should be Baked beans due to the id being 0. Done
   * using JUnits assertEquals method and getters in the AdjustMenu class.
   */
  @Test
  public void testRetrieveNameNull() {
	Database db = Database.getInstance();
    db.tableSetup();
    AdjustMenu aM = new AdjustMenu();
    aM.retrieveName("", 0);
    assertEquals("Baked beans", aM.getName());
  }
  
  /**
   * Tests the retrievePrice method when the new price is passed into the method, i.e when
   * the textField for the new name has the new name, and then compares the result of retrievePrice,
   * stored in a private field called price, to the actual result which should be same result
   * passed into the method as that is the new name.
   * Tested using JUnits assertEquals method and getters in the AdjustMenu class.
   */
  @Test
  public void testRetrievePrice() {
	Database db = Database.getInstance();
	db.tableSetup();
    AdjustMenu aM = new AdjustMenu();
    aM.retrievePrice(50, 0);
    assertEquals(50, aM.getPrice());
  }
  
  /**
   * Tests the retrievePrice method when 0 is passed into the method, i.e when the textField for the new
   * price is empty, and then compares the result of retriecePrice, stored in
   * a private field called price, to the actual result which should be 101 due
   * to the ID being 0. Tested using JUnits assertEquals method and getters in the AdjustMenu class.
   */
  @Test
  public void testRetrievePriceZero() {
	Database db = Database.getInstance();
	db.tableSetup();
    AdjustMenu aM = new AdjustMenu();
    aM.retrievePrice(0,0);
    assertEquals(101, aM.getPrice());
  }
  
  /**
   * Tests the retrieveDesciption method when the new description value is passed into the method, i.e. when
   * the textArea for the description has new text in it, and then compares the result of retrieveDescription,
   * stored in a private field called desc,
   * to the actual result which should be the same result passed into the method as that is the new description 
   * to be inserted into the database. Tested using JUnits assertEquals method and getters in the AdjustMenu class.
   */
  @Test
  public void testRetrieveDescription() {
	Database db = Database.getInstance();
    db.tableSetup();
    AdjustMenu aM = new AdjustMenu();
    aM.retrieveDescription("New description", 0);
    assertEquals("New description", aM.getDesc());
  }
  
  /**
   * Tests the retrieveDescription when a string of nothing has been passed into the method, i.e. when the textArea
   * for the description has nothing in it, and then compares the result of the retrieveDescription method,
   * stored in a private field called price,
   * to the actual result which should be the description of the product whose ID is the ID that was passed into the 
   * method. Tested using JUnits assertEquals method and getters in the AdjustMenu class.
   */
  @Test
  public void testRetrieveDescriptionNull() {
	Database db = Database.getInstance();
    db.tableSetup();
    AdjustMenu aM = new AdjustMenu();
    aM.retrieveDescription("", 0);
    assertEquals("Beany goodness", aM.getDesc());
  }
  
  /**
   * Tests the retrieveApproxTime method when the new approximated time value is passed into the method, i.e.
   * when the textField for the new approximated time has a value in it, and compares the result of the 
   * retrieveApproxTime method, stored in a private field called approxTime,
   * to the actual result which should be the same value as it is new approximated time
   * to be stored in the database. Tested using JUnits assertEquals method and getters in the AdjustMenu class.
   */
  @Test
  public void testRetrieveApproxTime() {
	Database db = Database.getInstance();
	db.tableSetup();
    AdjustMenu aM = new AdjustMenu();
    aM.retrieveApproxTime(15, 0);
    assertEquals(15, aM.getApproxTime());
  }
  
  /**
   * Tests the retrieveApproxTime method when 0 is passed into the method, i.e. when the textField for the new
   * approximated time has nothing in it, and compares the result of the retrieveApproxTime method, stored in a 
   * private field called approxTime, to the actual result which is 30 as that is the default time
   * that is stored in the database.
   * Tested using JUnits assertEquals method and getters in the AdjustMenu class.
   */
  @Test
  public void testRetrieveApproxTimeZero() {
	Database db = Database.getInstance();
	db.tableSetup();
    AdjustMenu aM = new AdjustMenu();
    aM.retrieveApproxTime(0, 0);
    assertEquals(30, aM.getApproxTime());
  }
  
  /**
   * Tests the makeAdjustments method. Firstly all the retrieve methods are called to set all the 
   * fields with their appropriate new values, i.e. when the manager has filled out the form 
   * and pressed submit button. Then the makeAdjustments method is called to make the adjustments,
   * then we use a simple SQL query to get the new adjustments made to the DB and we check if the 
   * adjustments have been made using JUnits assertEquals method.   
   * @throws SQLException An exception thrown so rs.next() can be used.
   */
  @Test
  public void testMakeAjustments() throws SQLException {
    Database db = Database.getInstance();
    AdjustMenu aM = new AdjustMenu();
    aM.retrieveID("", 0);
    aM.retrieveName("new Baked Beans", 0);
    aM.retrievePrice(500, 0);
    aM.retrieveDescription("TEST DESCRIPTION", 0);
    aM.retrieveApproxTime(60, 0);
    aM.setType("Dessert", 0);
    aM.makeAdjustments();
    String testQuery = "SELECT * FROM product WHERE id ='0'";
    ResultSet rs = db.executeQuery(testQuery);
    rs.next();
    assertEquals(0, rs.getInt(1));
    assertEquals("new Baked Beans", rs.getString(2));
    assertEquals("Dessert", rs.getString(3));
    assertEquals("TEST DESCRIPTION", rs.getString(4));
    assertEquals(500, rs.getInt(5));
    assertEquals(60, rs.getInt(7));
  }
  
  

}
