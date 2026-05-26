/**
 * Represents Gold ore in the game.
 *
 * Extends Ore to specify gold's unique pricing and description.
 */
public class Gold extends Ore {
  /**
   * Constructs the default Gold item.
   */
  public Gold() {
    super("Gold", "Native gold gleaned from the river or mine.", 2.50);
  }
}
