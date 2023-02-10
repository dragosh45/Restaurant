package testWaiter;

import static org.junit.Assert.*;
import org.junit.Test;
import backend.MenuItem;
import backend.MenuItemList;

/**
 * A JUnit test class to test methods in the MenuItemList class. So far this class tests the
 * setMenuItemList method.
 *
 * @author Stephen House
 *
 */
public class TestMenuItemList {

  /**
   * Tests the setMenuItemList method in the MenuItemList class. Tested by creating an instance of
   * MenuItemList which inside of the constructor for MenuItemList setMenuItemList is called. From
   * here, a MenuItem is returned from the list using the getItem method and the name of the item
   * returned is compared against the value it should be using JUnits assertEquals method.
   */
  @Test
  public void testSetMenuItemList() {
    MenuItemList menuItemList = MenuItemList.getInstance();
    MenuItem mI = null;
    try {
      mI = menuItemList.getItem(0); // Item with id 0 should be baked beans
    } catch (Exception e) {
      e.printStackTrace();
    }
    assertEquals("Baked beans", mI.getName());
  }
}
