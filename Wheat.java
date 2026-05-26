// --- FILE: Wheat.java ---
/**
 * Represents wheat crops in the game.
 *
 * Extends Crop with a specific sell price and description. It represents
 * the physical wheat grown at Sutter's Fort.
 */
public class Wheat extends Crop {
  /**
   * Constructs a new Wheat item with predefined values.
   */
  public Wheat() {
    super("Wheat", "Golden, ripe wheat stalks.", 5);
  }
}
