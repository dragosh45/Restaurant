package backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * Singleton Object MenuItemList Stores an ArrayList of MenuItems which is created from the database
 *
 * @author Charles Card
 *
 */
public class MenuItemList implements Iterable<MenuItem> {
  private static MenuItemList instance = null;

  /**
   * If there is no existing instance of MenuItemList it will create a new instance it will then
   * return the instance attribute of the class
   *
   * @return Returns the instance of MenuItemList
   */
  public static MenuItemList getInstance() {
    if (instance == null) {
      instance = new MenuItemList();
    }
    return instance;
  }

  private final ArrayList<MenuItem> items = new ArrayList<>();

  /**
   * Private constructor for a singleton
   */
  private MenuItemList() { // Loaded from the database
    this.update();
  }

  /**
   * Checks if a MenuItems id is contained within the ArrayList
   *
   * @param id The id to check
   * @return <code>true</code>if list contains MenuItem; <code>false</code> otherwise.
   */
  public boolean containsItem(int id) {
    for (MenuItem item : this.items) {
      if (item.getId() == id) {
        return true;
      }
    }
    return false;
  }

  /**
   * Filter the list based on the type of meal
   *
   * @param type
   * @return
   */
  private ArrayList<MenuItem> filter(Type type) {
    ArrayList<MenuItem> filteredList = new ArrayList<>();
    for (MenuItem item : this.items) {
      if (item.getType() == type && item.isAvailable()) {
        filteredList.add(item);
      }
    }
    return filteredList;
  }

  /**
   * Filter the list based on the type and filtertype of the meal
   *
   * @param type
   * @param filter
   * @return
   */
  public ArrayList<MenuItem> filter(Type type, Filter filter) {
    if (filter.equals(Filter.ALL)) {
      return this.filter(type);
    }

    ArrayList<MenuItem> filteredList = new ArrayList<>();
    for (MenuItem item : this.items) {
      if (item.getType() == type) { // TODO add filter as well once implemented in database
        filteredList.add(item);
      }
    }
    return filteredList;
  }

  /**
   * Gets a list of all the item ids within the ArrayList
   *
   * @return An array of item ids
   */
  private int[] getIds() {
    int[] result = new int[this.items.size()];
    for (int i = 0; i < this.items.size(); i++) {
      result[i] = this.items.get(i).getId();
    }
    return result;
  }

  /**
   * This method searches through the List of MenuItems until it finds the one with the same id if
   * an item with the same id does not exist it will raise an Exception
   *
   * @param id The id of the MenuItem to be returned if it exists
   * @return The item from the list of MenuItem's that was being searched for
   * @throws Exception If the item being searched for does not exist in the ArrayList
   */
  public MenuItem getItem(int id) throws Exception { // TODO Upgrade from a linear search
    for (MenuItem item : this.items) {
      if (item.getId() == id) {
        return item;
      }
    }
    throw new Exception("Item does not exist");
  }

  /**
   * Temporary to allow tests to work
   *
   * @return The ArrayList of MenuItems
   */
  public ArrayList<MenuItem> getList() {
    return this.items;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    int hash = this.items.hashCode();
    return hash;
  }

  /**
   * This allows you to iterate over the ArrayList of MenuItems without returning the ArrayList
   * object
   */
  @Override
  public Iterator<MenuItem> iterator() {
    Iterator<MenuItem> iterator = new Iterator<MenuItem>() {
      private int index = -1;

      @Override
      public boolean hasNext() {
        return this.index + 1 < MenuItemList.this.items.size();
      }

      @Override
      public MenuItem next() {
        this.index = this.index + 1;
        return MenuItemList.this.items.get(this.index);
      }

    };
    return iterator;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return this.items.toString();
  }

  /**
   * Sets the ArrayList, items, with MenuItem objects created from information from the database.
   * The MenuItem objects are created using the results from an sql query which selects all the
   * information about the menu from the database.
   */
  public void update() {
    int[] ids = Database.getInstance().getProductIds();
    if (!Arrays.equals(this.getIds(), ids)) { // Check if the id arrays are the same
      for (MenuItem item : this.items) { // Remove items not in the database
        if (!Arrays.stream(ids).boxed().collect(Collectors.toList()).contains(item.getId())) {
          this.items.remove(item);
        }
      }
      for (int id : ids) { // Add items in the database
        if (!this.containsItem(id)) {
          try {
            this.items.add(new MenuItem(id));
          } catch (DatabaseException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

}
