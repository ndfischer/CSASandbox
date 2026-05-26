/**
 * Represents coal ore found in the Sierra Mine.
 *
 * Extends Ore with a predefined price value.
 */
public class Coal extends Ore {

  /**
   * Constructs a Coal ore item.
   *
   * @param name the name of the coal entity
   * @param description the description of the coal entity
   */
  public Coal(String name, String description) {
    super(name, description, 0.50);
  }
}
