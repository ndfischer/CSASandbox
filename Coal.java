// --- FILE: Coal.java ---
/**
 * Represents coal ore found in the game.
 *
 * Extends Ore to provide specific pricing for coal. It is placed in the
 * Sierra Mine as a minable resource.
 */
public class Coal extends Ore {
  /**
   * Constructs a new Coal item with predefined value.
   */
  public Coal() {
    super("Coal", "Chunks of dark, combustible coal.", 0.50);
  }
}
