// --- FILE: Cattle.java ---
/**
 * Represents cattle livestock in the game.
 *
 * Extends Livestock with specific production intervals and outputs. Can be
 * purchased at Brannan's Store and placed at the Californio Rancho.
 */
public class Cattle extends Livestock {
  /**
   * Constructs a new Cattle item with predefined production mechanics.
   */
  public Cattle() {
    super("Cattle", "Sturdy beasts that produce beef and milk.", 10, 5, 10);
  }
}
