/**
 * Represents minable ore resources in the game.
 *
 * Extends Item to include pricing for ores sold at the store.
 */
public class Ore extends Item {
  private double pricePerGram;

  /**
   * Constructs an Ore item.
   *
   * @param name the name of the ore
   * @param description the description of the ore
   * @param pricePerGram the value of the ore per gram
   */
  public Ore(String name, String description, double pricePerGram) {
    super(name, description);
    this.pricePerGram = pricePerGram;
  }

  /**
   * Retrieves the price per gram of the ore.
   *
   * @return the price value as a double
   */
  public double getPricePerGram() {
    return pricePerGram;
  }
}
