// --- FILE: Gold.java ---
/**
 * Represents native gold ore materials mined or panned.
 *
 * <p>Updated to lock down AP CS A compliant market configurations 
 * matching the updated specification valuation schema ($2.50/gram).
 */
public class Gold extends Ore {
  /**
   * Instantiates an active Gold commodity unit with baseline spec values.
   */
  public Gold() {
    super("Gold", "Glittering native gold ore matrices.", 2.50);
  }
}
