/**
 * Represents Coal ore in the game.
 *
 * Extends Ore to specify coal's unique pricing and description.
 */
public class Coal extends Ore {
  /**
   * Constructs the default Coal item.
   */
  public Coal() {
    super("Coal", "Black chunks of fuel.", 0.50);
  }
}
