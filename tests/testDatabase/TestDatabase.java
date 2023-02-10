package testDatabase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import backend.Database;

/**
 * TestDBsetup class creates a suite of tests to check if the DatabaseSetup class makes the
 * connection to the server, creates and drops tables, but also if the Database can be queried and
 * whether data can be read into the database using files.
 *
 * @author Dragos Preda and Stephen House
 *
 */
public class TestDatabase {

  String username;
  String password;
  String dbUrl;
  String tableDesc;
  String tableDesc2;
  String tableDesc3;
  String print;
  Statement db;

  /**
   * Initalises the username, password and dbUrl fields with the appropriate heroku db login
   * details.
   */
  @Before
  public void setUp() {
    this.username = "pukdztdrlwizrd";
    this.password = "9fcf6d1c0c337ebe8d481aa56f8657b23b0020d403150c76e22ba58763d31fd3";
    this.dbUrl = "jdbc:postgresql://ec2-54-217-214-201.eu-west-1.compute.amazonaws.com:"
        + "5432/d8cvilvhht35us?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
  }

  /**
   * Tests if the getConnection method works. Done using JUnits assertNotNull method to check if the
   * connection returned from the getConnection method is null or not. Not null meaning a connection
   * was made.
   */
  @Test
  public void testConnection1() {
    Database db = Database.getInstance();
    assertNotNull(db.getConnection(this.username, this.password, this.dbUrl));
  }

  /**
   * Tests to see if the constructor that calls the getConnection method makes the connection with
   * the server. Done using JUnits assertNotNull method to check if the db.conn attribute of the
   * DatabaseSetup class is Null or not. Not null meaning a connection was made.
   */
  @Test
  public void testConnection2() {
    Database db = Database.getInstance(this.username, this.password, this.dbUrl);
    assertNotNull(db);
  }

  /**
   * Tests to see if the constructor that takes no parameters makes the connection to the database
   * server. Done using JUnits assertNotNull method to check if the db.conn attribute of the
   * DatabaseSetup class is Null or not. Not null meaning a connection was made.
   */
  @Test
  public void testConnection3() {
    Database db = Database.getInstance();
    assertNotNull(db);
  }

  @Test
  /**
   * Tests to see if tables can be dropped and created. The tables are dropped and then created.
   * How: If something is wrong, usually an SQLException is thrown. Execute method will return false
   * if it's an update to the Database. Also in the console prints will show when tables are created
   * and dropped.
   *
   * @throws SQLException Thrown so that the SQL methods can be used.
   */
  public void testCreateDropTable() throws SQLException {
    Database db = Database.getInstance();
    this.tableDesc =
        "orders(ORDER_NUMBER int primary key, TABLE_NUMBER int, PRODUCT_ID varchar(10),"
            + " STAFF_ID varchar(10), STATUS varchar(10), TIME TIME, foreign key(PRODUCT_ID)"
            + " references menu, foreign key(STAFF_ID) references staff);";
    this.tableDesc2 = "menu(PRODUCT_ID varchar(10) primary key, PRODUCT_NAME varchar(30),"
        + "MEAL_TYPE varchar(10), DESCRIPTION varchar(100), PRICE float);";
    this.tableDesc3 =
        "staff(STAFF_ID varchar(10) primary key, NAME varchar(30), ROLE varchar(10));";
    DatabaseMetaData dbm = db.getMetaData();
    ResultSet tableOrders = dbm.getTables(null, null, "orders", null);
    ResultSet tableMenu = dbm.getTables(null, null, "menu", null);
    ResultSet tableStaff = dbm.getTables(null, null, "staff", null);
    ResultSet tableMenuItems = dbm.getTables(null, null, "menuitems", null);
    System.out.println("------testCreateDropTable-------");
    if (tableMenuItems.next()) {
      System.out.println(">Table menuitems already in the database. =>Table Dropped");
      db.execute("DROP TABLE " + "menuitems");
    }
    if (tableOrders.next()) {
      System.out.println(">Table orders already in the database. =>Table Dropped");
      db.execute("DROP TABLE " + "orders");
    }
    if (tableMenu.next()) {
      System.out.println(">Table menu already in the database. =>Table Dropped");
      db.execute("DROP TABLE " + "menu");
    }
    if (tableStaff.next()) {
      System.out.println(">Table staff already in the database. =>Table Dropped");
      db.execute("DROP TABLE " + "staff");
    }
    if (db.execute("CREATE TABLE " + this.tableDesc2) == false
        && db.execute("CREATE TABLE " + this.tableDesc3) == false) {
      System.out.println(">Tables orders, menu, staff succsesfully created.");
    } else {
      System.out.println(">Tables failed to create.");
    }
    assertEquals(db.execute("CREATE TABLE " + this.tableDesc), false);
    db.tableSetup();
  }

  @Test
  /**
   * Tests the executeQuery method, to see if queries can be made and tableSetup to create all the
   * tables at once. How:If something is wrong, usually an SQLException is thrown. Creating tables
   * with tableSetup then, Inserting data into the tables, then using the result of a SELECT query
   * to assertEquals with the string it should produce.
   *
   * @throws SQLException Thrown so that the SQL methods can be used.
   */
  public void testExecuteQuery() throws SQLException {
    Database db = Database.getInstance(this.username, this.password, this.dbUrl);
    db.tableSetup();
    db.executeUpdate("INSERT INTO staff VALUES(" + "'" + Integer.parseInt("4024") + "','" + "John"
        + "','Waiter')");
    db.executeUpdate("INSERT INTO menu VALUES(" + "'" + Integer.parseInt("101")
        + "','Waffle','Dessert','" + Float.parseFloat("4") + "')");
    db.executeUpdate("INSERT INTO orders VALUES(" + "'" + Integer.parseInt("13") + "'" + "," + "'"
        + Integer.parseInt("4") + "'" + "," + "'" + Integer.parseInt("101") + "'" + "," + "'"
        + Integer.parseInt("4024") + "'" + "," + "'" + "not-ready" + "'" + "," + "'"
        + "2018-01-13 12:35" + "')");
    String query = "SELECT STATUS FROM orders WHERE STATUS = 'not-ready'";
    ResultSet rs = db.executeQuery(query);
    String result = null;
    if (rs.next()) {
      result = rs.getString(1);
    }
    assertEquals(result, "not-ready");
  }

  @Test
  /**
   * Tests to see if data can be inserted into tables and also tests if a SELECT query can be
   * executed on the Database. 1 row for each table is inserted, then the orders table is SELECT *
   * queried. The numbers in the single Orders table row represent orderNr,tableNr,productID,status
   * and time(7). How: If something is wrong, usually an SQLException is thrown. Asserts the single
   * row in table Orders with a similar String.
   *
   * @throws SQLException Thrown so that the SQL methods can be used.
   */
  public void testInsertAndQueryData() throws SQLException {
    System.out.println("------testInsertAndQueryData-------");
    Database db = Database.getInstance();
    db.tableSetup();
    System.out.println("Test");
    db.executeUpdate("INSERT INTO staff VALUES(" + "'" + Integer.parseInt("4024") + "','" + "John"
        + "','Waiter')");
    db.executeUpdate("INSERT INTO menu VALUES(" + "'" + Integer.parseInt("101")
        + "','Waffle','Dessert','" + Float.parseFloat("4") + "')");
    db.executeUpdate("INSERT INTO orders VALUES(" + "'" + Integer.parseInt("13") + "'" + "," + "'"
        + Integer.parseInt("4") + "'" + "," + "'" + Integer.parseInt("101") + "'" + "," + "'"
        + Integer.parseInt("4024") + "'" + "," + "'" + "not-ready" + "'" + "," + "'"
        + "2018-01-13 12:35" + "')");

    String query = "SELECT * FROM orders";
    ResultSet rs = null;
    rs = db.executeQuery(query);
    if (rs.next() == true) {
      this.print = rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " "
          + rs.getString(4) + " " + rs.getString(5) + " " + rs.getString(6);
      System.out.println(this.print);
    }

    assertEquals(this.print, "13 4 101 4024 not-ready 12:35:00");

  }

  /**
   * Tests the insertData method to see if the test data is added to the database correctly. Done by
   * using the insertData method to add the data to the database, and then querying the database to
   * see if the data was actually added and then comparing the result of the query against what the
   * query should show using JUnits assertEquals method.
   *
   * @throws SQLException SQLException Thrown so that the JDBC SQL result set methods can be used.
   */
  @Test
  public void testInsertData() throws SQLException {
    System.out.println("---------testInsertDataMenu-----------");
    Database db = Database.getInstance();
    db.tableSetup();
    String query = "SELECT PRODUCT_NAME FROM menu WHERE PRODUCT_NAME = 'Cup of Tea'";
    db = Database.getInstance();
    ResultSet rs = db.executeQuery(query);
    String result = null;
    if (rs.next()) {
      result = rs.getString(1); // Turns query result to a string.
    }
    System.out.println(result);
    assertEquals(result, "Cup of Tea"); // Compares query result to the actual result.

    query = "SELECT NAME FROM staff WHERE NAME = 'Stephen'";
    rs = db.executeQuery(query);
    if (rs.next()) {
      result = rs.getString(1);
    }
    System.out.println(result);
    assertEquals(result, "Stephen");

    query = "SELECT ORDER_NUMBER FROM orders WHERE ORDER_NUMBER = '10'";
    rs = db.executeQuery(query);
    if (rs.next()) {
      result = rs.getString(1);
    }
    System.out.println(result);
    assertEquals(result, "10");

    query = "SELECT COMMENTS FROM menuitems WHERE COMMENTS = 'No milk'";
    rs = db.executeQuery(query);
    if (rs.next()) {
      result = rs.getString(1);
    }
    System.out.println(result);
    assertEquals(result, "No milk");
  }
  
  @Test
  public void testDoesMenuItemExist() {
    Database db = Database.getInstance();
    db.tableSetup();
    int testId = 0;
    Boolean result = db.doesMenuItemExist(testId);
    System.out.println(result);
    assertEquals(result, true);
  }

  /**
   * Tests the ReadBasicFileMenu method to see if the file is read. Done by using the
   * readBasicFileMenu method and then querying the database to see if the file was read and then
   * comparing the result of the query against data that should be in the database using
   * assertEquals. Tested using the testMenu.txt file.
   *
   * @throws SQLException Thrown so that the JDBC SQL result set methods can be used.
   */
  @Test
  public void testReadBasicFileMenu() throws SQLException {
    System.out.println("------testReadBasicMenu------");
    Database db = Database.getInstance(this.username, this.password, this.dbUrl);
    db.tableSetup();
    String filePath = "testMenu.txt";
    db.readBasicMenuFile(filePath);
    String query = "SELECT PRODUCT_ID FROM menu WHERE PRODUCT_ID = '1111'";
    ResultSet rs = db.executeQuery(query);
    String result = null;
    if (rs.next()) {
      result = rs.getString(1);
    }
    assertEquals(result, "1111");
  }
  
  @Test
  public testAssignTables() {
    assignTables(1);
  }

}
