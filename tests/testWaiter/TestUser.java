package testWaiter;

import static org.junit.Assert.*;
import org.junit.Test;
import backend.User;

/**
 * A JUnit test class to test the methods of the User class. This class tests the authenticate
 * method, and the getDetails method.
 *
 * @author Stephen House
 *
 */
public class TestUser {

  /**
   * Tests the authenticate method in the user class for authenticating a user whose login number is
   * not in the database. Tested using JUnits assertEquals method to compare the result of
   * authenticate with false.
   */
  @Test
  public void testAuthenticateFalse() {
    User user = null;
    try {
      user = new User(1);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    assertEquals(false, user.authenticate(1001));
  }

  /**
   * Tests the authenticate method in the user class for authenticating a user whose login number is
   * in the database. Tested using JUnits assertEquals method to compare the result of authenticate
   * with true.
   */
  @Test
  public void testAuthenticateTrue() {
    User user = null;
    try {
      user = new User(3);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    assertEquals(true, user.authenticate(3));
  }

  /**
   * Tests the getDetails method in the user class to get the details of a user that exists in the
   * database and compare the results inside of the array that is returned with the values they are
   * supposed to be. Tested using JUnits assertEquals method.
   */
  @Test
  public void testGetDetails() {
    User user = null;
    try {
      user = new User(0003);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    String details[] = user.getDetails();
    assertEquals("3", details[0]);
    assertEquals("Stephen", details[1]);
    assertEquals("FOH", details[2]);
  }

}
