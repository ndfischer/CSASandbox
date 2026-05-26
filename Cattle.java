/**
 * Represents Cattle raised at the Rancho.
 *
 * Extends Livestock with specific intervals and yields for cattle.
 */
public class Cattle extends Livestock {
  /**
   * Constructs the default Cattle livestock item.
   */
  public Cattle() {
    super("Cattle", "Sturdy grazing livestock.", 10, 5, 10);
  }
}
