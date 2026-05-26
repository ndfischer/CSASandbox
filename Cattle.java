/**
 * Represents cattle raised at the Californio Rancho.
 *
 * Extends Livestock with a predefined production interval and yields.
 */
public class Cattle extends Livestock {

  /**
   * Constructs a Cattle livestock item.
   *
   * @param name the name of the cattle
   * @param description the description of the cattle
   */
  public Cattle(String name, String description) {
    super(name, description, 10, 5, 10);
  }
}
