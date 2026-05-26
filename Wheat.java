/**
 * Represents wheat grown at Sutter's Fort.
 *
 * Extends Crop with predefined agricultural value.
 */
public class Wheat extends Crop {

  /**
   * Constructs a Wheat crop item.
   *
   * @param name the name of the wheat entity
   * @param description the description of the wheat entity
   */
  public Wheat(String name, String description) {
    super(name, description, 5);
  }
}
