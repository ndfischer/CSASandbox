/**
 * Represents minable resources in the game world.
 *
 * Demonstrates inheritance by extending Item and adding a price field.
 * Used to model valuable earth resources like Gold and Coal.
 */
public class Ore extends Item {
  private double pricePerGram;

  /**
   * Constructs an Ore item.
   *
   * @param name the name of the ore
   * @param description the description of the ore
   * @param pricePerGram the monetary value per gram of this ore
   */
  public Ore(String name, String description, double pricePerGram) {
    super(name, description);
    this.pricePerGram = pricePerGram;
  }

  /**
   * Gets the selling price per gram.
   *
   * @return the price per gram as a double
   */
  public double getPricePerGram() {
    return pricePerGram;
  }
}
