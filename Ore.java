// --- FILE: Ore.java ---
/**
 * Represents an ore resource in the game.
 *
 * Demonstrates inheritance by extending Item and adding a pricePerGram field.
 * Used as a base class for specific minable resources like Gold and Coal.
 */
public class Ore extends Item {
  private double pricePerGram;

  /**
   * Constructs a new Ore item.
   *
   * @param name the name of the ore
   * @param description the description of the ore
   * @param pricePerGram the market value of the ore per gram
   */
  public Ore(String name, String description, double pricePerGram) {
    super(name, description);
    this.pricePerGram = pricePerGram;
  }

  /**
   * Retrieves the price per gram of the ore.
   *
   * @return the price per gram
   */
  public double getPricePerGram() {
    return this.pricePerGram;
  }
}
