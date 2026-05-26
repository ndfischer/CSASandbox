/**
 * Represents agricultural products in the game world.
 *
 * Demonstrates inheritance by extending Item and adding an agricultural sell price.
 * Serves as the base for plant-based goods.
 */
public class Crop extends Item {
  private int sellPrice;

  /**
   * Constructs a Crop item.
   *
   * @param name the name of the crop
   * @param description the description of the crop
   * @param sellPrice the price for which this crop can be sold
   */
  public Crop(String name, String description, int sellPrice) {
    super(name, description);
    this.sellPrice = sellPrice;
  }

  /**
   * Retrieves the selling price of the crop.
   *
   * @return the sell price as an integer
   */
  public int getSellPrice() {
    return sellPrice;
  }
}
