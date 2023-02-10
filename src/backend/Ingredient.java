package backend;

/**
 * @author Charles Card
 *
 */
public class Ingredient {
  private final int id;
  private String name;
  private int price;
  private int procurementPrice;
  private int calories;
  private int quantity;
  private int stock;
  private Database db = Database.getInstance();

  /**
   * Constructor to construct a new Ingredient
   * 
   * @param id The id of the Ingredient in the database
   */
  public Ingredient(int id) {
    this.id = id;
    this.quantity = 1;
    this.update();
  }

  /**
   * Constructor to construct a new Ingredient
   * 
   * @param id The id of the Ingredient in the database
   * @param quantity The quantity of the Ingredient used in a Product/OrderItem
   */
  public Ingredient(int id, int quantity) {
    this.id = id;
    this.quantity = quantity;
    this.update();
  }

  /**
   * Updates all the attributes of the Ingredient class with the values from the database.
   */
  public void update() {
    try {
      this.calories = this.db.getIngredientCalories(this.id);
      this.name = this.db.getIngredientName(this.id);
      this.price = this.db.getIngredientPrice(this.id);
      this.procurementPrice = this.db.getIngredientProcurementPrice(this.id);
      this.stock = this.db.getIngredientStock(this.id);
    } catch (DatabaseException e) {
      e.printStackTrace(); // Ingredient does not exist or incorrect id
    }
  }

  /**
   * Getter to get the amount of calories for this ingredient
   * 
   * @return Amount of calories
   */
  public int getCalories() {
    return this.calories;
  }

  /**
   * Getter to get the price this ingredient was procured for
   * 
   * @return The price of procurement
   */
  public int getProcurementPrice() {
    return this.procurementPrice;
  }

  /**
   * Getter to get the price this ingredient is being sold for
   * 
   * @return The price of the ingredient
   */
  public int getPrice() {
    return this.price * this.quantity;
  }

  /**
   * Getter to get the name of this ingredient
   * 
   * @return The name of the ingredient
   */
  public String getName() {
    return this.name;
  }

  /**
   * Getter to get the id of this ingredient
   * 
   * @return The id of the ingredient
   */
  public int getId() {
    return this.id;
  }

  /**
   * Getter to get the current stock of this Ingredient
   * 
   * @return The stock of the ingredient
   */
  public int getStock() {
    return this.stock;
  }

  /**
   * Getter to get the quantity of this ingredient
   * 
   * @return The quantity of the ingredient
   */
  public int getQuantity() {
    return this.quantity;
  }

  /**
   * Setter to set the quantity of this ingredient
   * 
   * @param quantity The quantity to set the ingredient to
   */
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  /**
   * Increments the quantity of the ingredient
   */
  public void increment() {
    this.quantity = this.quantity + 1;
  }

  /**
   * Decrements the quantity of the ingredient
   */
  public void decrement() {
    this.quantity = this.quantity - 1;
  }

  @Override
  public int hashCode() {
    int hash = this.id;
    hash = hash * 31 + this.quantity;
    return hash;
  }

  @Override
  public String toString() {
    return "ID: " + this.id + ", Name: " + this.name;
  }
}
