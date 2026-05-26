// --- FILE: Crop.java ---
/**
 * Represents a harvestable crop in the game.
 *
 * Demonstrates inheritance by extending Item and adding a sellPrice field.
 * Used as a base class for specific agricultural products like Wheat.
 */
public class Crop extends Item {
  private int sellPrice;

  /**
   * Constructs a new Crop item.
   *
   * @param name the name of the crop
   * @param description the description of the crop
   * @param sellPrice the price the crop sells for per unit
   */
  public Crop(String name, String description, int sellPrice) {
    super(name, description);
    this.sellPrice = sellPrice;
  }

  /**
   * Retrieves the sell price of the crop.
   *
   * @return the sell price
   */
  public int getSellPrice() {
    return this.sellPrice;
  }
}
