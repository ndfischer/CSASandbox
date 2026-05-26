// --- FILE: Gold.java ---
/**
 * Represents gold ore found in the game.
 *
 * Extends Ore to provide specific pricing for gold. It is placed in the
 * American River and Sierra Mine to represent pan and mine targets.
 */
public class Gold extends Ore {
  /**
   * Constructs a new Gold item with predefined value.
   */
  public Gold() {
    super("Gold", "Shiny native gold.", 2.50);
  }
}
