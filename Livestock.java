/**
 * Represents living animals raised for resources.
 *
 * Demonstrates deeper inheritance by extending Item and adding fields for production rates.
 */
public class Livestock extends Item {
  private int productionInterval;
  private int productionAmount;
  private int goodsSellPrice;

  /**
   * Constructs a Livestock item.
   *
   * @param name the name of the animal
   * @param description the description of the animal
   * @param productionInterval turns required to produce goods
   * @param productionAmount amount of goods produced per interval
   * @param goodsSellPrice value of the produced goods
   */
  public Livestock(String name, String description, int productionInterval, int productionAmount, int goodsSellPrice) {
    super(name, description);
    this.productionInterval = productionInterval;
    this.productionAmount = productionAmount;
    this.goodsSellPrice = goodsSellPrice;
  }

  /**
   * Gets the turns required to produce goods.
   *
   * @return the interval in turns
   */
  public int getProductionInterval() {
    return productionInterval;
  }

  /**
   * Gets the amount of goods produced.
   *
   * @return the production amount
   */
  public int getProductionAmount() {
    return productionAmount;
  }

  /**
   * Gets the sell price of the produced goods.
   *
   * @return the price per unit of goods
   */
  public int getGoodsSellPrice() {
    return goodsSellPrice;
  }
}
