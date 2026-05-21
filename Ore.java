// --- FILE: Ore.java ---
/**
 * Represents mined geological resources in the simulation.
 *
 * <p>Demonstrates inheritance by extending Item and adding a monetary market valuation 
 * modifier specific to raw materials.
 */
public class Ore extends Item {
  private double pricePerGram;

  /**
   * Constructs an ore resource instance.
   *
   * @param name Unique name of the specific ore matrix.
   * @param description Textual description of the material.
   * @param pricePerGram Floating-point value of each unit gram in dollars.
   */
  public Ore(String name, String description, double pricePerGram) {
    super(name, description);
    this.pricePerGram = pricePerGram;
  }

  /**
   * Fetches the market value rate of this ore.
   *
   * @return Price per gram in dollars.
   */
  public double getPricePerGram() {
    return this.pricePerGram;
  }
}
