package backend;

import java.util.ArrayList;

/**
 * MenuItem stores information about a single Item which is stored in the database
 *
 * @author Charles Card
 *
 */
public class MenuItem {
  private final int id;
  private String name;
  private String description;
  private int price;
  private Type type;
  private String[] allergens;
  private boolean available;
  private Database db = Database.getInstance();
  private final ArrayList<Ingredient> ingredients = new ArrayList<>();

  /**
   * Constructor to create a
   *
   * @param id
   */
  public MenuItem(int id) throws DatabaseException {
    this.id = id;
    this.name = this.db.getProductName(this.id);
    this.type = this.db.getProductType(this.id);
    this.description = this.db.getProductDescription(this.id);
    this.update();
  }

  /**
   * This method checks that the user has sufficient permissions and then delete the item from the
   * database
   *
   * @param user The user currently using the till
   * @throws Exception
   */
  public void deleteItem(User user) throws Exception {
    this.db.deleteProduct(this.id, user);
  }

  /**
   * Getter to get the allergens associated with an item
   *
   * @return A list of allergens in the item of food/drink
   */
  public String[] getAllergens() {
    return this.allergens;
  }

  /**
   * Gets the description of the MenuItem
   * 
   * @return The description
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Getter to get the id of the item
   *
   * @return The primary key (or id) that the item is stored with in the database
   */
  public int getId() {
    return this.id;
  }

  /**
   * Getter to get the name of the item
   *
   * @return The name of the item
   */
  public String getName() {
    return this.name;
  }

  /**
   * Getter to get the price of the item
   *
   * @return The price of the item
   */
  public int getPrice() {
    return this.price;
  }

  /**
   * Getter to get the type of the item
   *
   * @return
   */
  public Type getType() {
    return this.type;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    int hash = this.id;
    hash = hash * 31 + (this.isAvailable() ? 1 : 0);
    return hash;
  }

  /**
   *
   * @return <code>true</code> if item is available for purchase;<br>
   *         <code>false</code> otherwise.
   */
  public boolean isAvailable() {
    return this.available;
  }

  /**
   * Toggles the availability of the menu item
   *
   * @throws Exception
   */
  public void toggleAvailability(User user) throws Exception {
    Role role = user.getRole(); // The users role
    if (role.getPermissionLevel() >= 1) { // Must be at least FOH
      this.available = !this.available;
      this.db.setAvailability(this.id, this.available);
    } else {
      throw new Exception(
          "You do not have high enough permissions to change the availability of the menu");
    }
  }

  /**
   * Getter to get the list of Ingredients in this product
   * 
   * @return The list of ingredients
   */
  public ArrayList<Ingredient> getIngredients() {
    return this.ingredients;
  }

  /**
   * Gets the quantity of the ingredient by default
   * 
   * @param id The id of the ingredient
   * @return The quantity of the ingredient in the product
   */
  public int getIngredientQuantity(int id) {
    return 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return this.name;
  }

  /**
   * Updates all the attributes of the MenuItem class with values from the database. Uses an SQL
   * query to select the information from the database, and then uses a set of setters to set the
   * attribute values.
   */
  public void update() { // Attributes which would have a greater impact if they changed
    try {
      this.price = this.db.getProductPrice(this.id);
      this.available = this.db.getProductAvailability(this.id);
    } catch (DatabaseException e) {
      e.printStackTrace(); // Shouldn't ever be thrown
    }
  }

  @Override
  public boolean equals(Object that) {
    if (this == that) {
      return true; // Same memory space
    }
    if (!(that instanceof MenuItem)) {
      return false; // Same type
    }
    return this.hashCode() == that.hashCode(); // Same contents

  }
}
