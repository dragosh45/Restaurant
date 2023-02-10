package backend;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * This class sets up the initial structure of the database including the tables. It includes
 * methods to create tables, to check if a table exist and to delete tables. The class includes a
 * constructor so its methods can be used else where. The url to the database we are using is :
 * jdbc:postgresql://ec2-54-217-214-201.eu-west-1.compute.amazonaws.com:
 * 5432/d8cvilvhht35us?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory"; The username
 * and password to the database we are using can be found on our heroku.
 *
 * @author Stephen House and Dragos Preda and Charles Card.
 */
public class Database {
  private static Database instance;

  /**
   *
   * @return
   */
  public static Database getInstance() {
    if (instance == null) {
      instance = new Database();
    }
    return instance;
  }

  /**
   *
   * @param username The username of user of the database
   * @param password The password of the user trying to set up the database
   * @param dbUrl The url to the database.
   *
   * @return instance
   */
  public static Database getInstance(String username, String password, String dbUrl) {
    if (instance == null) {
      instance = new Database(username, password, dbUrl);
    }
    return instance;
  }

  private Connection conn;

  /**
   * A constructor to create an instance of the DatabaseSetup class which sets up a connection to
   * the Heroku database being used.
   */
  private Database() {
    String username = "pukdztdrlwizrd";
    String password = "9fcf6d1c0c337ebe8d481aa56f8657b23b0020d403150c76e22ba58763d31fd3";
    String dbUrl = "jdbc:postgresql://ec2-54-217-214-201.eu-west-1.compute.amazonaws.com:"
        + "5432/d8cvilvhht35us?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
    this.conn = this.getConnection(username, password, dbUrl);
  }

  /**
   * A constructor to create an instance of the DatabaseSetup class which connects to the database
   * specified in the dbUrl passed into the constructor. The username and password are parameters to
   * be used in the getConnection method.
   *
   * @param username The username of user of the database
   * @param password The password of the user trying to set up the database
   * @param dbUrl The url to the database.
   */
  private Database(String username, String password, String dbUrl) {
    this.conn = this.getConnection(username, password, dbUrl);
  }

  /**
   * Adds an entry to the TableAssignment table to assign a table to a user
   *
   * @param loginNumber The user to assign the table to
   * @param tableNumber The table to be assigned to the user
   */
  public void assignTable(int loginNumber, int tableNumber) {

  }

  /**
   * Assigns all staff to tables automatically
   *
   * @param tables Amount of tables in the restaurant
   */
  public void assignTables(int tables) {
    try {
      PreparedStatement pstmt = this.conn.prepareStatement(
          "SELECT Staff.ID FROM Staff, Labour WHERE Labour.Staff_ID = Staff.ID AND Labour.CLOCK_OUT = NULL");
      ResultSet rs = pstmt.executeQuery();
      System.out.println("succ");
      while (rs.next()) {
        System.out.println(rs.getInt(1));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Authenticates the existence of a user based on their loginNumber
   *
   * @param loginNumber The login number to be authenticated
   * @return <code>true</code> if user exists; <code>false</code> otherwise.
   */
  public boolean authenticateUser(int loginNumber) {
    try {
      PreparedStatement pstmt =
          this.conn.prepareStatement("SELECT count(1) FROM Staff WHERE ID = ?;");
      pstmt.setInt(1, loginNumber);
      ResultSet rs = pstmt.executeQuery();
      rs.next();
      return rs.getBoolean(1);
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Adds a clock in entry to the Labour table for a user
   *
   * @param loginNumber The user to be clocked in
   * @throws DatabaseException If the user does not exist or is already clocked in
   */
  public void clockIn(int loginNumber) throws DatabaseException {
    if (!this.authenticateUser(loginNumber)) {
      throw new DatabaseException("User does not exist");
    }
    if (!this.isClockedIn(loginNumber)) {
      try {
        PreparedStatement pstmt = this.conn.prepareStatement(
            "INSERT INTO Labour(STAFF_ID, CLOCK_IN) VALUES(?, CURRENT_TIMESTAMP);");
        pstmt.setInt(1, loginNumber);
        pstmt.execute();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else {
      throw new DatabaseException("User is already clocked in.");
    }
    this.assignTables(20);
  }

  /**
   * Updates the Labour table to have a clock_out time for a user
   *
   * @param loginNumber The user to be clocked out
   * @throws DatabaseException If the user is not clocked in or does not exist
   */
  public void clockOut(int loginNumber) throws DatabaseException {
    if (!this.authenticateUser(loginNumber)) {
      throw new DatabaseException("User does not exist");
    }
    if (this.isClockedIn(loginNumber)) {
      try {
        PreparedStatement pstmt = this.conn.prepareStatement(
            "UPDATE Labour SET CLOCK_OUT = CURRENT_TIMESTAMP WHERE STAFF_ID = ? AND CLOCK_OUT = NULL");
        pstmt.setInt(1, loginNumber);
        pstmt.execute();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else {
      throw new DatabaseException("User is not clocked in");
    }
  }

  /**
   * Creates a new Order in the database
   *
   * @param tableNumber The table for the order
   * @return The OrderId
   */
  public int createOrder(int tableNumber) {
    try {
      PreparedStatement pstmt =
          this.conn.prepareStatement("INSERT INTO Orders(TABLE_NUM) VALUES(?);");
      pstmt.setInt(1, tableNumber);
      pstmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    try {
      PreparedStatement pstmt = this.conn.prepareStatement(
          "SELECT ID FROM Orders WHERE TABLE_NUM = ? AND CREATION_TIME = (SELECT MAX(CREATION_TIME) FROM Orders WHERE TABLE_NUM = ?);");
      pstmt.setInt(1, tableNumber);
      pstmt.setInt(2, tableNumber);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return rs.getInt(1);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return -1;
  }

  /*
   * Below here are methods which can be used to get and manipulate data in the database
   */

  /**
   * Deleted an order from the Orders table
   *
   * @param orderNumber The order to be deleted
   * @throws DatabaseException If order doesn't exist
   */
  public void deleteOrder(int orderNumber) throws DatabaseException {
    if (!this.orderExists(orderNumber)) {
      throw new DatabaseException("Order does not exist");
    }
    try {
      PreparedStatement pstmt1 =
          this.conn.prepareStatement("DELETE FROM OrderItem WHERE ORDER_ID = ?;");
      PreparedStatement pstmt2 = this.conn.prepareStatement("DELETE FROM Orders WHERE ID = ?;");
      pstmt1.setInt(1, orderNumber);
      pstmt2.setInt(1, orderNumber);
      pstmt1.execute();
      pstmt2.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Deletes Product from the database
   *
   * @param id The id of the product
   * @param user The user attempting the delete
   * @throws DatabaseException If the user has insufficient permission
   */
  public void deleteProduct(int id, User user) throws DatabaseException {
    if (user.getRole().getPermissionLevel() >= 2) {
      try {
        PreparedStatement pstmt =
            this.conn.prepareStatement("DELETE FROM Product WHERE ID = ? CASCADE;");
        pstmt.setInt(1, id);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else {
      throw new DatabaseException("Insufficient permission to delete Product.");
    }
  }

  /**
   * Checks whether the string passed into the database is a table and whether the table exists. If
   * the table exists, a boolean true is returned other wise false is returned.
   *
   * @param table A string containing the name of the table.
   * @return A boolean value indicating whether the table exists or not.
   */
  private void dropTables() {
    Statement st = null;
    try {
      DatabaseMetaData dmb = this.conn.getMetaData();
      ResultSet tables = dmb.getTables(null, null, "%", new String[] {"TABLE"});
      while (tables.next()) {
        st = this.conn.createStatement();
        st.executeUpdate("DROP TABLE " + tables.getString(3) + " CASCADE;");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void executeFile(String file) {
    Statement st = null;
    String result = null;
    try (BufferedReader br = new BufferedReader(new FileReader(file))) { // Try with resources, auto
                                                                         // closes
      st = this.conn.createStatement();
      StringBuilder sb = new StringBuilder();
      String line = br.readLine();
      while (line != null) {
        sb.append(line);
        line = br.readLine();
      }
      result = sb.toString();
      st.executeUpdate(result);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets a list of assigned tables to a specific user
   *
   * @param loginNumber The user to get the tables of
   * @return The list of assigned tables
   */
  public int[] getAssignedTables(int loginNumber) {
    return new int[2];
  }

  /**
   * Returns the time the user was clocked in
   *
   * @param loginNumber The user to get the clock in time of
   * @return The time the user was clocked in
   * @throws DatabaseException If the user does not exist
   */
  public Timestamp getClockInTime(int loginNumber) throws DatabaseException {
    if (!this.isClockedIn(loginNumber)) {
      throw new DatabaseException("User is not clocked in");
    }
    try {
      PreparedStatement pstmt = this.conn.prepareStatement(
          "SELECT CLOCK_IN FROM Labour WHERE STAFF_ID = ? AND CLOCK_OUT is null;");
      pstmt.setInt(1, loginNumber);
      ResultSet rs = pstmt.executeQuery();
      rs.next();
      return rs.getTimestamp(1);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Gets the database    m,, according the parameters passed into the method.
   *
   * @param username A string containing the username of the user to log into the database
   * @param password A string containing the password of the user to log into the database
   * @param dbUrl A string containing the url of the database to connect to
   * @return connection The sql database connection to be used.
   */
  public Connection getConnection(String username, String password, String dbUrl) {
    Connection connection = null;
    try {
      connection = DriverManager.getConnection(dbUrl, username, password);
    } catch (SQLException e) {
      System.out.println("Connection failed");
      e.printStackTrace();
    }
    return connection;
  }

  /**
   * Gets the first name of a user
   *
   * @param loginNumber The login number to get the first name of
   * @return The first name of the user
   * @throws DatabaseException If the user does not exist
   */
  public String getFirstName(int loginNumber) throws DatabaseException {
    if (!this.authenticateUser(loginNumber)) {
      throw new DatabaseException("User does not exist");
    }
    try {
      PreparedStatement pstmt =
          this.conn.prepareStatement("SELECT FIRST_NAME FROM Staff WHERE ID = ?;");
      pstmt.setInt(1, loginNumber);
      ResultSet rs = pstmt.executeQuery();
      rs.next();
      return rs.getString(1);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Gets the last name of a user
   *
   * @param loginNumber The login number to get the last name of
   * @return The last name of the user
   * @throws DatabaseException If the user does not exist
   */
  public String getLastName(int loginNumber) throws DatabaseException {
    if (!this.authenticateUser(loginNumber)) {
      throw new DatabaseException("User does not exist");
    }
    try {
      PreparedStatement pstmt =
          this.conn.prepareStatement("SELECT LAST_NAME FROM Staff WHERE ID = ?;");
      pstmt.setInt(1, loginNumber);
      ResultSet rs = pstmt.executeQuery();
      rs.next();
      return rs.getString(1);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public DatabaseMetaData getMetaData() {
    DatabaseMetaData meta = null;
    try {
      meta = this.conn.getMetaData();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return meta;
  }

  /**
   * Get the comment on an OrderItem
   *
   * @param id The id of the OrderItem
   * @return The comment on the OrderItem
   * @throws DatabaseException If the OrderItem does not exist
   */
  public String getOrderItemComment(int id) throws DatabaseException {
    String comment = null;
    try {
      PreparedStatement pstmt =
          this.conn.prepareStatement("SELECT COMMENTS FROM OrderItem WHERE ID = ?;");
      pstmt.setInt(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        comment = rs.getString(1);
      } else {
        throw new DatabaseException("OrderItem does not exist.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return comment;
  }

  /**
   * Get the Quantity of an OrderItem
   *
   * @param id The id of the OrderItem
   * @return The Quantity of the OrderItem
   * @throws DatabaseException If the OrderItem does not exist
   */
  public int getOrderItemQuantity(int id) throws DatabaseException {
    int quantity = 0;
    try {
      PreparedStatement pstmt =
          this.conn.prepareStatement("SELECT QUANTITY FROM OrderItem WHERE ID = ?;");
      pstmt.setInt(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        quantity = rs.getInt(1);
      } else {
        throw new DatabaseException("OrderItem does not exist.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return quantity;
  }

  /**
   * Gets the paid status of an order
   *
   * @param orderNumber The order to get the paid status of
   * @return <code>true</code> if order is paid for; <code>false</code> otherwise.
   */
  public boolean getOrderPaid(int orderNumber) {
    try {
      PreparedStatement pstmt = this.conn.prepareStatement("SELECT PAID FROM Orders WHERE ID = ?;");
      pstmt.setInt(1, orderNumber);
      ResultSet rs = pstmt.executeQuery();
      rs.next();
      return rs.getBoolean(1);
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }

  }

  /**
   * Gets A list of order id's from the database
   *
   * @param status The list of status' of orders to get
   * @return An int array of all Order ID's
   */
  public int[] getOrders(Status[] status) {
    int[] result = null;
    try {
      PreparedStatement pstmt =
          this.conn.prepareStatement("SELECT ID FROM Orders WHERE Status = ANY(?);",
              ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
      Array statusArray = this.conn.createArrayOf("VARCHAR", status);
      pstmt.setArray(1, statusArray);
      ResultSet rs = pstmt.executeQuery();
      int size = 0;
      int i = 0;
      if (rs.last()) {
        size = rs.getRow();
        rs.beforeFirst();
      }
      result = new int[size];
      while (rs.next()) {
        result[i] = rs.getInt(1);
        i = i + 1;
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * Get the time the order was created from the database
   *
   * @param orderNumber The order to check the time of
   * @return The time of the order
   */
  public Timestamp getOrderTime(int orderNumber) {
    try {
      PreparedStatement pstmt =
          this.conn.prepareStatement("SELECT CREATION_TIME FROM Orders WHERE ID = ?;");
      pstmt.setInt(1, orderNumber);
      ResultSet rs = pstmt.executeQuery();
      rs.next();
      return rs.getTimestamp(1);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Gets a list of OrderItems for an Order
   *
   * @param id OrderId to get the OrderItems for
   * @return The list of OrderItems
   * @throws DatabaseException If the Order does not exist
   */
  public ArrayList<OrderItem> getOrderUpdate(int id) throws DatabaseException {
    MenuItemList products = MenuItemList.getInstance();
    ArrayList<OrderItem> list = new ArrayList<>();
    try {
      PreparedStatement pstmt = this.conn.prepareStatement(
          "SELECT ID, PRODUCT_ID, QUANTITY, COMMENTS FROM OrderItem WHERE ORDER_ID = ?;");
      pstmt.setInt(1, id);
      ResultSet rs = pstmt.executeQuery();
      MenuItem product = null;
      while (rs.next()) {
        product = products.getItem(rs.getInt(2));
        list.add(new OrderItem(rs.getInt(1), product, rs.getInt(3), rs.getString(4)));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (Exception e) { // Product does not exist
      e.printStackTrace();
    }
    return list;
  }

  /**
   * Get the availability of a Product
   *
   * @param id The id of the Product
   * @return <code>true</code>If the Product is available;<code>false</code>otherwise.
   * @throws DatabaseException If the Product does not exist
   */
  public boolean getProductAvailability(int id) throws DatabaseException {
    boolean available = false;
    try {
      PreparedStatement pstmt =
          this.conn.prepareStatement("SELECT AVAILABILITY FROM Product WHERE ID = ?;");
      pstmt.setInt(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        available = rs.getBoolean(1);
      } else {
        throw new DatabaseException("Product does not exist.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return available;
  }

  /**
   * Get the description of a Product
   *
   * @param id The id of the Product
   * @return The description of the Product
   * @throws DatabaseException If the Product does not exist
   */
  public String getProductDescription(int id) throws DatabaseException {
    String description = null;
    try {
      PreparedStatement pstmt =
          this.conn.prepareStatement("SELECT DESCRIPTION FROM Product WHERE ID = ?;");
      pstmt.setInt(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        description = rs.getString(1);
      } else {
        throw new DatabaseException("Product does not exist.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return description;
  }

  /**
   * @return Set of Product Ids
   */
  public int[] getProductIds() {
    int[] ids = null;
    try {
      PreparedStatement pstmt = this.conn.prepareStatement("SELECT ID FROM Product;",
          ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
      ResultSet rs = pstmt.executeQuery();
      int size = 0;
      int i = 0;
      if (rs.last()) {
        size = rs.getRow();
        rs.beforeFirst();
      }
      ids = new int[size];
      while (rs.next()) {
        ids[i] = rs.getInt(1);
        i = i + 1;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return ids;
  }

  /**
   * Get the name of a Product
   *
   * @param id The id of the Product
   * @return The name of the Product
   * @throws DatabaseException If the Product does not exist
   */
  public String getProductName(int id) throws DatabaseException {
    String name = null;
    try {
      PreparedStatement pstmt =
          this.conn.prepareStatement("SELECT NAME FROM Product WHERE ID = ?;");
      pstmt.setInt(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        name = rs.getString(1);
      } else {
        throw new DatabaseException("Product does not exist.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return name;
  }

  /**
   * Get the price of a Product
   *
   * @param id The id of the Product
   * @return The price of the product as an int (100x)
   * @throws DatabaseException If the Product does not exist
   */
  public int getProductPrice(int id) throws DatabaseException {
    int price = 0;
    try {
      PreparedStatement pstmt =
          this.conn.prepareStatement("SELECT PRICE FROM Product WHERE ID = ?;");
      pstmt.setInt(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        price = rs.getInt(1);
      } else {
        throw new DatabaseException("Product does not exist.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return price;
  }

  /**
   * Get the time taken for the customer to receive a Product
   *
   * @param id The id of the Product
   * @return The time taken for the customer to receive the Product
   * @throws DatabaseException If the Product does not exist
   */
  public Timestamp getProductTime(int id) throws DatabaseException {
    Timestamp time = null;
    try {
      PreparedStatement pstmt =
          this.conn.prepareStatement("SELECT APPROX_TIME FROM Product WHERE ID = ?;");
      pstmt.setInt(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        time = rs.getTimestamp(1);
      } else {
        throw new DatabaseException("Product does not exist.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return time;
  }

  /**
   * Get the type of meal of a Product
   *
   * @param id The id of the Product
   * @return The type of the Product
   * @throws DatabaseException If the Product does not exist
   */
  public Type getProductType(int id) throws DatabaseException {
    Type type = null;
    try {
      PreparedStatement pstmt =
          this.conn.prepareStatement("SELECT MEAL_TYPE FROM Product WHERE ID = ?;");
      pstmt.setInt(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        type = Type.valueOf(rs.getString(1));
      } else {
        throw new DatabaseException("Product does not exist.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return type;
  }

  /**
   * Gets the role of a user
   *
   * @param loginNumber The login number to get the role of
   * @return The role of the user
   * @throws DatabaseException If the user does not exist
   */
  public Role getRole(int loginNumber) throws DatabaseException {
    if (!this.authenticateUser(loginNumber)) {
      throw new DatabaseException("User does not exist");
    }
    try {
      PreparedStatement pstmt = this.conn.prepareStatement("SELECT ROLE FROM Staff WHERE ID = ?;");
      pstmt.setInt(1, loginNumber);
      ResultSet rs = pstmt.executeQuery();
      rs.next();
      return Role.valueOf(rs.getString(1));
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Get the current status of an order
   *
   * @param orderNumber The order to check the status of
   * @return The status of the order
   */
  public Status getStatus(int orderNumber) {
    try {
      PreparedStatement pstmt =
          this.conn.prepareStatement("SELECT STATUS FROM Orders WHERE ID = ?;");
      pstmt.setInt(1, orderNumber);
      ResultSet rs = pstmt.executeQuery();
      rs.next();
      return Status.valueOf(rs.getString(1));
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Get the current table of an order
   *
   * @param orderNumber The order to check the table of
   * @return The table number of the order
   * @throws DatabaseException If order does not exist
   */
  public int getTable(int orderNumber) throws DatabaseException {
    if (!this.orderExists(orderNumber)) {
      throw new DatabaseException("Order does not exist");
    }
    try {
      PreparedStatement pstmt =
          this.conn.prepareStatement("SELECT TABLE_NUM FROM Orders WHERE ID = ?;");
      pstmt.setInt(1, orderNumber);
      ResultSet rs = pstmt.executeQuery();
      rs.next();
      return rs.getInt(1);
    } catch (SQLException e) {
      e.printStackTrace();
      return 0;
    }
  }

  /**
   * Return the current time in the database
   *
   * @return The current system time of the database
   */
  public Timestamp getTime() {
    try {
      PreparedStatement pstmt = this.conn.prepareStatement("SELECT CURRENT_TIMESTAMP;");
      ResultSet rs = pstmt.executeQuery();
      rs.next();
      return rs.getTimestamp(1);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Returns a User if the loginNumber corresponds to a user
   *
   * @param loginNumber The login number of the user
   * @return The user
   * @throws DatabaseException If user does not exist
   */
  public User getUser(int loginNumber) throws DatabaseException {
    if (!this.authenticateUser(loginNumber)) {
      throw new DatabaseException("User does not exist.");
    }
    try {
      this.clockIn(loginNumber);
    } catch (DatabaseException e) {
      // User is already clocked in, do nothing and continue
    }
    PreparedStatement pstmt = null;
    User user = null;
    try {
      pstmt = this.conn
          .prepareStatement("SELECT FIRST_NAME, LAST_NAME, ROLE FROM Staff WHERE STAFF_ID = ?;");
      pstmt.setInt(1, loginNumber);

      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        user = new User(loginNumber, rs.getString(1), rs.getString(2),
            Role.valueOf(rs.getString(3)), this.getClockInTime(loginNumber));
      } else {
        throw new DatabaseException("User does not exist");
        // Should never get to this line hopefully
        // Unless a user is deleted in the time between original user check and the execution of the
        // pstmt
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return user;
  }

  /**
   * Check if a user is assigned to a specific table
   *
   * @param loginNumber The user to check for
   * @param tableNumber The table to check for
   * @return <code>true</code> If the table is assigned to the user; <code>false</code> otherwise.
   */
  public boolean isAssignedTo(int loginNumber, int tableNumber) {
    return false;
  }

  /**
   * Checks if a user is currently clocked in
   *
   * @param loginNumber The user to check if currently clocked in
   * @return <code>true</code> if clocked in; <code>false</code> otherwise.
   * @throws DatabaseException If the user does not exist
   */
  public boolean isClockedIn(int loginNumber) throws DatabaseException {
    if (!this.authenticateUser(loginNumber)) {
      throw new DatabaseException("User does not exist");
    }
    try {
      PreparedStatement pstmt = this.conn.prepareStatement(
          "SELECT count(*) FROM Labour WHERE STAFF_ID = ? AND CLOCK_OUT is null;");
      pstmt.setInt(1, loginNumber);
      ResultSet rs = pstmt.executeQuery();
      rs.next();
      return rs.getBoolean(1);
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
  
  /**
   * Checks if the id passed into the method exists in the Product table by querying the database
   * and selecting the id from the database, if there is anything in the resultSet returned from the query, 
   * the menu item exists, otherwise it does not.  
   * @param id	An integer id value to see the id exists in the database.
   * @return <code>true<code> if the menu item id exists in the database, <code>false> if the menu item id
   * does not exist in the database.
   */
  public boolean doesMenuItemExist(int id) {
    try {
      PreparedStatement pstmt = conn.prepareStatement("SELECT ID FROM Product WHERE ID= ?;");
      pstmt.setInt(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return true;
      }
    } catch (SQLException e) {
      System.out.println(e.toString());
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Checks if an order exists in the Orders table
   *
   * @param orderNumber The order to check
   * @return <code>true</code> if order exists; <code>false</code> otherwise.
   */
  public boolean orderExists(int orderNumber) {
    try {
      PreparedStatement pstmt =
          this.conn.prepareStatement("SELECT count(1) FROM Orders WHERE ID = ?;");
      pstmt.setInt(1, orderNumber);
      ResultSet rs = pstmt.executeQuery();
      rs.next();
      return rs.getBoolean(1);
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * import java.sql.ResultSetMetaData; Reads a basic text file that has 4 pieces of information per
   * line where each piece is separated by a tab.
   *
   * @param file The name of the text file to be read.
   */
  public void readBasicMenuFile(String file) {
    BufferedReader br = null;
    PreparedStatement pstmt = null;
    try {
      String currentLine;
      String[] split;
      br = new BufferedReader(new FileReader(file));
      while ((currentLine = br.readLine()) != null) {
        split = currentLine.split("	");
        pstmt = this.conn.prepareStatement("INSERT INTO Menu VALUES(?, ?, ?, ?, ?, ?);");
        pstmt.setString(1, split[0]);
        pstmt.setString(2, split[1]);
        pstmt.setString(3, split[2]);
        pstmt.setString(4, split[3]);
        pstmt.setString(5, split[4]);
        pstmt.setString(6, split[5]);
        pstmt.executeUpdate();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   *
   * @param tableNumber
   * @param request
   */
  public void setAssist(int tableNumber, String request) { // TODO
    try {
      PreparedStatement pstmt =
          this.conn.prepareStatement("UPDATE TableAssist SET ASSIST = ? WHERE TABLE_NUM = ?;");
      pstmt.setString(1, request);
      pstmt.setInt(2, tableNumber);
      pstmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Sets the availability for a product in the database
   *
   * @param id The id of the product
   * @param available The availability to set for that product
   */
  public void setAvailability(int id, boolean available) {
    try {
      PreparedStatement pstmt =
          this.conn.prepareStatement("UPDATE Product SET AVAILABILITY = ? WHERE ID = ?;");
      pstmt.setBoolean(1, available);
      pstmt.setInt(2, id);
      pstmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  public void setOrderUpdate(int id, ArrayList<OrderItem> orderItems, int tableNumber) {
    PreparedStatement pstmt = null;
    try {
      pstmt = this.conn.prepareStatement("UPDATE Orders SET TABLE_NUM = ? WHERE ID = ?;");
      pstmt.setInt(1, tableNumber);
      pstmt.setInt(2, id);
      pstmt.execute();
      pstmt = this.conn.prepareStatement("");
      for (OrderItem item : orderItems) {
        pstmt = this.conn
            .prepareStatement("UPDATE OrderItem SET COMMENTS = ?, QUANTITY = ? WHERE ID = ?;"
                + " INSERT INTO OrderItem(PRODUCT_ID, ORDER_ID, QUANTITY, COMMENTS)"
                + " SELECT ?, ?, ?, ? WHERE NOT EXISTS (SELECT 1 FROM OrderItem WHERE ID = ?);");
        pstmt.setString(1, item.getComment());
        pstmt.setInt(2, item.getQuantity());
        pstmt.setInt(3, item.getId());
        pstmt.setInt(4, item.getProduct().getId());
        pstmt.setInt(5, id);
        pstmt.setInt(6, item.getQuantity());
        pstmt.setString(7, item.getComment());
        pstmt.setInt(8, item.getId());
        pstmt.execute();
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Changes the status of an order in the Orders table
   *
   * @param orderNumber The order to change the status of
   * @param status The status to change the order to
   */
  public void setStatus(int orderNumber, Status status) {
    try {
      PreparedStatement pstmt =
          this.conn.prepareStatement("UPDATE Orders SET STATUS = ? WHERE ID = ?;");
      pstmt.setString(1, status.toString());
      pstmt.setInt(2, orderNumber);
      pstmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Changes the table of an order in the Orders table
   *
   * @param orderNumber The order to change the table of
   * @param tableNumber The table to change the order to
   * @throws DatabaseException If order does not exist
   */
  public void setTable(int orderNumber, int tableNumber) throws DatabaseException {
    if (!this.orderExists(orderNumber)) {
      throw new DatabaseException("Order does not exist");
    }
    try {
      PreparedStatement pstmt =
          this.conn.prepareStatement("UPDATE Orders SET TABLE_NUM = ? WHERE ID = ?;");
      pstmt.setInt(1, tableNumber);
      pstmt.setInt(2, orderNumber);
      pstmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Sets up the tables in the database by creating the tables, dropping the current tables in the
   * database and then reading in the data to be put into the database.
   */
  public void tableSetup() {
    this.dropTables(); // Drop existing tables
    try { // Some peoples computers require a different path, unsure why currently
      this.executeFile("src/backend/tables.sql"); // Create Tables
      this.executeFile("src/backend/test_data.sql"); // Insert test data
    } catch (Exception e) {
      this.executeFile("Restaurant/src/backend/tables.sql"); // Create Tables
      this.executeFile("Restaurant/src/backend/test_data.sql"); // Insert test data
    }
    // readBasicMenuFile("src/database/menu.txt"); //Insert menu into the database -
    // for when not using test data - actual menu
  }

  /**
   * Gets the name for an Ingredient from the database
   * 
   * @param id The id of the Ingredient
   * @return The name of the Ingredient
   * @throws DatabaseException When the Ingredient does not exist
   */
  public String getIngredientName(int id) throws DatabaseException {
    String name = null;
    try {
      PreparedStatement pstmt = conn.prepareStatement("SELECT NAME FROM Ingredient WHERE ID = ?;");
      pstmt.setInt(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        name = rs.getString(1);
      } else {
        throw new DatabaseException("Ingredient does not exist.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return name;
  }

  /**
   * Gets the price for an Ingredient from the database
   * 
   * @param id The id of the Ingredient
   * @return The price of the Ingredient
   * @throws DatabaseException When the Ingredient does not exist
   */
  public int getIngredientPrice(int id) throws DatabaseException {
    int price = 0;
    try {
      PreparedStatement pstmt = conn.prepareStatement("SELECT PRICE FROM Ingredient WHERE ID = ?;");
      pstmt.setInt(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        price = rs.getInt(1);
      } else {
        throw new DatabaseException("Ingredient does not exist.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return price;
  }

  /**
   * Gets the procurement price for an Ingredient from the database
   * 
   * @param id The id of the Ingredient
   * @return The procurement price of the ingredient
   * @throws DatabaseException When the Ingredient does not exist
   */
  public int getIngredientProcurementPrice(int id) throws DatabaseException {
    int price = 0;
    try {
      PreparedStatement pstmt =
          conn.prepareStatement("SELECT PROCUREMENT_PRICE FROM Ingredient WHERE ID = ?;");
      pstmt.setInt(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        price = rs.getInt(1);
      } else {
        throw new DatabaseException("Ingredient does not exist.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return price;
  }

  /**
   * Gets the Calories for an Ingredient from the database
   * 
   * @param id The id of the Ingredient
   * @return The calories for the Ingredient
   * @throws DatabaseException When the Ingredient does not exist
   */
  public int getIngredientCalories(int id) throws DatabaseException {
    int calories = 0;
    try {
      PreparedStatement pstmt =
          conn.prepareStatement("SELECT CALORIES FROM Ingredient WHERE ID = ?;");
      pstmt.setInt(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        calories = rs.getInt(1);
      } else {
        throw new DatabaseException("Ingredient does not exist.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return calories;
  }

  /**
   * Gets the current stock of an Ingredient in the database
   * 
   * @param id The id of the Ingredient
   * @return The stock of the Ingredient
   * @throws DatabaseException When the Ingredient does not exist
   */
  public int getIngredientStock(int id) throws DatabaseException {
    int stock = 0;
    try {
      PreparedStatement pstmt = conn.prepareStatement("Do a table join");
      pstmt.setInt(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        stock = rs.getInt(1);
      } else {
        throw new DatabaseException("Ingredient does not exist");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return stock;
  }
  
  public ResultSet executeQuery(String query) {
    Statement st = null;
    ResultSet rs = null;
    try {
      st = this.conn.createStatement();
      rs = st.executeQuery(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return rs;
  }
  
  public void executeUpdate(String query) {
    Statement st = null;
    try {
      st = this.conn.createStatement();
      st.executeUpdate(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
