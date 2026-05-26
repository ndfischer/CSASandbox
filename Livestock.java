// --- FILE: Livestock.java ---
/**
 * Represents livestock animals in the game.
 *
 * Demonstrates inheritance by extending Item and adding fields for production
 * tracking. Serves as a base class for specific animals like Cattle.
 */
public class Livestock extends Item {
  private int productionInterval;
  private int productionAmount;
  private int goodsSellPrice;

  /**
   * Constructs a new Livestock item.
   *
   * @param name the name of the livestock
   * @param description the description of the livestock
   * @param productionInterval the number of turns required to produce goods
   * @param productionAmount the amount of goods produced per interval
   * @param goodsSellPrice the price the produced goods sell for
   */
  public Livestock(String name, String description, int productionInterval, int productionAmount, int goodsSellPrice) {
    super(name, description);
    this.productionInterval = productionInterval;
    this.productionAmount = productionAmount;
    this.goodsSellPrice = goodsSellPrice;
  }
}
