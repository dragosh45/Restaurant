package backend;

import java.util.ArrayList;

/**
 * This class stores the information about an Item on an Order An Order will have multiple
 * OrderItems stored in an array This is to keep track of all of the items on an order and be able
 * to manipulate fields such as its comment
 *
 * @author Charles Card
 *
 */
public class OrderItem {
  private int id;
  private final MenuItem product;
  private final ArrayList<Ingredient> ingredients = new ArrayList<>();
  private int quantity = 1;
  private String comment;

  /**
   * Constructs a new OrderItem and sets the id, name, and price It also makes sure that the
   * OrderItem does exist in the database before constructing the Object
   *
   * @param id The id of the OrderItem which is to be constructed
   */
  public OrderItem(int id, MenuItem product, int quantity) {
    this.id = id;
    this.product = product;
    this.quantity = quantity;
    this.comment = null;
  }

  /**
   * Constructs a new OrderItem and sets the id, name, and price It also makes sure that the
   * OrderItem does exist in the database before constructing the Object
   *
   * @param id The id of the OrderItem which is to be constructed
   */
  public OrderItem(int id, MenuItem product, int quantity, String comment) {
    this.id = id;
    this.product = product;
    this.quantity = quantity;
    this.comment = comment;
  }

  /**
   * Constructs a new OrderItem and sets the id, name, and price It also makes sure that the
   * OrderItem does exist in the database before constructing the Object
   *
   */
  public OrderItem(MenuItem product, int quantity) {
    this.id = -1; // Not yet inserted into database
    this.product = product;
    this.quantity = quantity;
    this.comment = null;
  }

  /**
   * Constructs a new OrderItem and sets the id, name, and price It also makes sure that the
   * OrderItem does exist in the database before constructing the Object
   *
   */
  public OrderItem(MenuItem product, int quantity, String comment) {
    this.id = -1; // Not yet inserted into database
    this.product = product;
    this.quantity = quantity;
    this.comment = comment;
  }

  /**
   * Increments the quantity by 1
   */
  public void increment() {
    this.quantity = this.quantity + 1;
  }
  
  /**
   * Increments the quantity by i
   */
  public void increment(int i) {
    this.quantity = this.quantity + i;
  }

  /**
   * Getter to get the comment currently on the OrderItem
   *
   * @return The comment on the OrderItem
   */
  public String getComment() {
    return this.comment;
  }

  /**
   * Getter to get the id of the OrderItem
   *
   * @return The id of the OrderItem
   */
  public final int getId() {
    return this.id;
  }

  /**
   * Gets the ingredients in this OrderItem
   *
   * @return
   */
  public ArrayList<Ingredient> getIngredients() {
    return this.ingredients; // TODO get defaults as well
  }

  /**
   * Getter to get the name of the OrderItem
   *
   * @return The name of the OrderItem
   */
  public String getName() {
    return this.product.getName();
  }

  /**
   * Getter to get the price of the OrderItem
   *
   * @return The price of the OrderItem
   */
  public float getPrice() {
    int price = this.product.getPrice();
    for (Ingredient ingredient : this.ingredients) {
      price = price + ingredient.getPrice(); // Add price of extra ingredients
    }
    return price * this.quantity;
  }

  public MenuItem getProduct() {
    return this.product;
  }

  /**
   * Getter to get the quantity of the OrderItem
   *
   * @return The quantity of the OrderItem
   */
  public int getQuantity() {
    return this.quantity;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    int hash = this.id;
    hash = hash * 31 + this.quantity;
    return hash;
  }

  /**
   * Decrement the quantity by 1
   */
  public void remove() {
    this.quantity = this.quantity - 1;
  }

  /**
   * Updates the comment on the OrderItem
   *
   * @param comment The comment which will overwrite the previous comment
   */
  public void setComment(String comment) {
    this.comment = comment;
  }

  /**
   * Updates quantity of the OrderItem
   *
   * @param quantity The quantity which will overwrite the previous quantity
   */
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public void removeIngredient(int id) {
    Ingredient ingredient = null;
    int quantity = this.getIngredientQuantity(id);
    if (quantity > 0) {
      if ((ingredient = getIngredient(id)) != null) {
        ingredient.decrement();
      } else {
        ingredient = new Ingredient(id, -1);
        this.ingredients.add(ingredient);
      }
    } else {
      // Does not contain ingredient
    }
  }

  /**
   * Adds an ingredient to the ingredients list
   * 
   * @param id The id of the ingredient
   */
  public void addIngredient(int id) {
    Ingredient ingredient = null;
    if ((ingredient = getIngredient(id)) != null) {
      ingredient.increment();
    } else {
      ingredient = new Ingredient(id);
      ingredients.add(ingredient);
    }
  }

  /**
   * Get the Ingredient object if it exists in the ingredients list
   * 
   * @param id The id of the ingredient
   * @return The ingredient in the list
   */
  private Ingredient getIngredient(int id) {
    for (Ingredient ingredient : this.ingredients) {
      if (ingredient.getId() == id) {
        return ingredient;
      }
    }
    return null; // If not in list
  }

  @Override
  public String toString() {
    return "Name: " + this.getName() + ", Quantity: " + this.quantity;
  }

  /**
   * Get the quantity of the ingredient for this OrderItem
   * 
   * @param id The id of the Ingredient
   * @return The quantity of the ingredient
   */
  private int getIngredientQuantity(int id) {
    int quantity = this.product.getIngredientQuantity(id);
    quantity = quantity + this.getIngredient(id).getQuantity();
    return quantity;
  }

  /**
   * Updates the OrderItem from the database
   */
  public void update() {
    Database db = Database.getInstance();
    this.product.update();
    try {
      this.quantity = db.getOrderItemQuantity(this.id);
      this.comment = db.getOrderItemComment(this.id);
    } catch (DatabaseException e) {
      e.printStackTrace(); // Should not be called, only if item does not exist
    }
  }


}
