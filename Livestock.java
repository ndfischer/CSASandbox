/**
 * Represents livestock raised by the player.
 *
 * Extends Item to include production cycles and goods yield.
 */
public class Livestock extends Item {
  private int productionInterval;
  private int productionAmount;
  private int goodsSellPrice;

  /**
   * Constructs a Livestock item.
   *
   * @param name the name of the livestock
   * @param description the description of the livestock
   * @param productionInterval the number of turns required to produce goods
   * @param productionAmount the amount of goods produced per cycle
   * @param goodsSellPrice the price the goods sell for
   */
  public Livestock(String name, String description, int productionInterval, int productionAmount, int goodsSellPrice) {
    super(name, description);
    this.productionInterval = productionInterval;
    this.productionAmount = productionAmount;
    this.goodsSellPrice = goodsSellPrice;
  }

  /**
   * Retrieves the turn interval required for production.
   *
   * @return the interval as an integer
   */
  public int getProductionInterval() {
    return productionInterval;
  }

  /**
   * Retrieves the amount of goods produced.
   *
   * @return the amount of goods
   */
  public int getProductionAmount() {
    return productionAmount;
  }

  /**
   * Retrieves the sell price of the goods.
   *
   * @return the price of goods
   */
  public int getGoodsSellPrice() {
    return goodsSellPrice;
  }
}
