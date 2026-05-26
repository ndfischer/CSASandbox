/**
 * Represents gold ore found in the American River and Sierra Mine.
 *
 * Extends Ore with a predefined price value.
 */
public class Gold extends Ore {

  /**
   * Constructs a Gold ore item.
   *
   * @param name the name of the gold entity
   * @param description the description of the gold entity
   */
  public Gold(String name, String description) {
    super(name, description, 2.50);
  }
}
