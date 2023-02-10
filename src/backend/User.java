package backend;

import java.sql.Timestamp;

/**
 * Stores the information about a user and allows other classes to authenticate a staff member and
 * get their current permission level
 *
 * @author Charles Card and Stephen House
 */
public class User {
  private int loginNumber;
  private String firstName;
  private String lastName;
  private Role role;
  private Timestamp clockInTime;
  private Database database = Database.getInstance();

  /**
   * Constructor which validates the loginNumber and if valid gets all necessary information from
   * the database. Including: first name, last name and position in restaurant. This also sets the
   * clock in time for the user to the time at which the object is created. If the user is not
   * authenticated, an alert is made and displayed for the user to see.
   *
   * @param loginNumber The login number of an employee
   */
  public User(int loginNumber) throws Exception {
    if (this.database.authenticateUser(loginNumber)) {
      if (!this.database.isClockedIn(loginNumber)) {
        this.database.clockIn(loginNumber);
      }
      this.loginNumber = loginNumber;
      this.firstName = this.database.getFirstName(loginNumber);
      this.lastName = this.database.getLastName(loginNumber);
      this.role = this.database.getRole(loginNumber);
      this.clockInTime = this.database.getClockInTime(loginNumber);
    } else {
      throw new Exception("Incorrect Login Number");
    }
  }

  /**
   * Constructor only to be created from this package
   *
   * @param loginNumber The login number of the user
   * @param firstName The first name of the user
   * @param lastName The last name of the user
   * @param role The role of the user
   * @param clockInTime The time the user clocked in
   */
  User(int loginNumber, String firstName, String lastName, Role role, Timestamp clockInTime) {
    this.loginNumber = loginNumber;
    this.firstName = firstName;
    this.lastName = lastName;
    this.role = role;
    this.clockInTime = clockInTime;
  }

  /**
   * This method is used to clock out the user so the company knows how long the employee has worked
   * and how much to pay them. This information is entered into the database. Only clock in and out
   * times are stored in the database, the time worked is to be displayed to the user ONLY.
   *
   */
  public void clockOut() {
    try {
      this.database.clockOut(this.loginNumber);
    } catch (DatabaseException e) {
      e.printStackTrace();
    }
  }

  /**
   * Getter to get the time that the employee clocked in
   *
   * @return The time that the employee clocked in
   */
  public Timestamp getClockInTime() {
    return this.clockInTime;
  }

  /**
   * Getter to get the first name of the user
   *
   * @return the first name of the user
   */
  public String getFirstName() {
    return this.firstName;
  }

  /**
   * Getter to get the last name of the user
   *
   * @return The last name of the user
   */
  public String getLastName() {
    return this.lastName;
  }

  /**
   * Getter to get the login number of the user
   *
   * @return The login number of the user
   */
  public int getLoginNumber() {
    return this.loginNumber;
  }

  /**
   * Getter to get the position of the user
   *
   * @return Position the user currently has in the restaurant
   */
  public Role getRole() {
    return this.role;
  }

}
