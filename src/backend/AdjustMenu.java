package backend;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Backend class for the AdjustMenu GUI - mainly for the submit button to make changes to the database
 * with the new adjustments. There are a set of retrieve methods which get the data from the database
 * if no entry in the textField was made and then stores it in the fields in the class, there are a set 
 * of getters to help test the retrieve methods, and then there is the insertAdjustments method to
 * actually make changes to the database with the new adjustments.
 * @author Stephen House - ZDAC059
 *
 */
public class AdjustMenu {
	
  private Database db;
  private String name;
  private String desc;
  private int oldID;
  private int newID;
  private int price;
  private int approxTime;
  private String type;
  
  /**
   * Constructor which creates an instance of the class and initialises the database field.
   */
  public AdjustMenu() {
    this.db = Database.getInstance();
  }
  
  /**
   * Updates the database with the new adjustments made by the manager. Creates an SQL string 
   * with all the new data inputed into the form GUI.
   */
  public void makeAdjustments() {
    String updateSQL = "UPDATE product SET name ='" + name + "', meal_type ='" + type + "', description ='" 
    		+ desc + "', price ='" + price + "', approx_time ='" + approxTime + "' WHERE"
    				+ " id ='" + oldID + "';";
    db.executeUpdate(updateSQL);
  }

  /**
   * Sets the fields newID and oldID with their respective values. This method is needed
   * if nothing is entered into the new ID textField as the newID will become the same as the old
   * ID.
   * @param newID A string containing the new ID for the product.
   * @param oldID An integer value which is the currentID of the product. Used in the update SQL.
   */
  public void retrieveID(int newID, int oldID) {
	this.oldID = newID;
  }

  /**
   * Sets the private field type to the type passed into the method.
   * @param type A string containing the type for the product retrieved from the radio buttons on the GUI.
   * @param id
   */
  public void setType(String type, int id) {
    this.type = type;
    System.out.println(this.type);
  }
  
  /**
   * Retrieves the name of the product from the database if nothing is inputed into the new name
   * textField, and then stores the result into the private field name.
   * @param name The new name of the product being adjusted.
   * @param id The ID of the product being adjusted.
   */
  public void retrieveName(String name, int id) {
    if (name.equals("")) {
      String nameQuery = "SELECT name FROM product WHERE ID ='" + id + "';";
      ResultSet rs = db.executeQuery(nameQuery);
      try {
        rs.next();
        name = rs.getString(1);
	  } catch (SQLException e) {
        e.printStackTrace();
	  }
    }
    this.name = name;
  }
  
  /**
   * Retrieves the price of the product from the database if nothing is inputed into the new price
   * textField, and then stores the result into the private field price. 
   * @param price The new price of the product being adjusted.
   * @param id The ID of the product being adjusted.
   */
  public void retrievePrice(String price, int id) {
    int databasePrice = 0;
    if (price.equals("")) {
      String priceQuery = "SELECT price FROM product WHERE ID ='" + id + "';";
      ResultSet rs = db.executeQuery(priceQuery);
      try {
        rs.next();
        databasePrice = rs.getInt(1);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else {
      databasePrice = Integer.parseInt(price);
    }
    this.price = databasePrice;
  }
  
  /**
   * Retrieves the description of the product from the database if nothing is inputed into the new 
   * description textArea, and then stores the result into the private field desc.
   * @param desc The new description of the product.
   * @param id The ID of the product being adjusted.
   */
  public void retrieveDescription(String desc, int id) {
    if (desc.equals("")) {
      String descQuery = "SELECT description FROM product WHERE ID = '" + id + "';";
      ResultSet rs = db.executeQuery(descQuery);
      try {
        rs.next();
        desc = rs.getString(1);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    this.desc = desc;
  }
  
  /**
   * Retrieves the approximated time of the product from the database if nothing is inputed into the 
   * new approximated Time textField, and then stores the result into the private field approxTime.
   * @param time The new approximated cooking time of the product.
   * @param id The ID of the product being adjusted.
   */
  public void retrieveApproxTime(String time, int id) {
    int databaseTime = 0;
    if (time.equals("")) {
      String priceQuery = "SELECT approx_time FROM product WHERE ID = '" + id + "';";
      ResultSet rs = db.executeQuery(priceQuery);
      try {
        rs.next();
        databaseTime = rs.getInt(1);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else {
      databaseTime = Integer.parseInt(time);
    }
    this.approxTime = databaseTime;
  }
  
  /**
   * Gets the current value stored in the private field name.
   * @return The current String value stored in the private field name.
   */
  public String getName() {
    return this.name;
  }
  
  /**
   * Gets the current value stored in the private field desc.
   * @return The current String value stored in the private field desc.
   */
  public String getDesc() {
    return this.desc;
  }
  
  /**
   * Gets the current value stored in the private field price.
   * @return The current integer value stored in the private field price.
   */
  public int getPrice() {
    return this.price;
  }
  
  /**
   * Gets the current value stored in the private field oldID.
   * @return The current integer value stored in the private field oldID.
   */
  public int getOldID() {
    return this.oldID;
  }
  
  /**
   * Gets the current value stored in the private field newID.
   * @return The current integer value stored in the private field newID.
   */
  public int getNewID() {
    return this.newID;
  }
  
  /**
   * Gets the current value stored in the private field approxTime.
   * @return The current integer value stored in the private field approxTime.
   */
  public int getApproxTime() {
    return this.approxTime;
  }

  /**
   * Gets the current value stored in the private field Type.
   * @return The current String value stored in the private field type.
   */
  public String getType() {
    return this.type;
  }
  
}
