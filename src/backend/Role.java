package backend;

/**
 * @author Charles Card
 */
public enum Role {
  BOH(0), FOH(1), MANAGEMENT(2), ADMIN(3);
  private int permissionLevel;

  private Role(int permissionLevel) {
    this.permissionLevel = permissionLevel;
  }

  public int getPermissionLevel() {
    return this.permissionLevel;
  }
}
