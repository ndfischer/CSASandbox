// --- FILE: Livestock.java ---
/**
 * Tracks ranching infrastructure assets within the simulation.
 *
 * <p>Extends Item to map periodic agricultural outputs like milk and beef to 
 * the structural game framework.
 */
public class Livestock extends Item {
  private int productionInterval;
  private int productionAmount;
  private int goodsSellPrice;

  /**
   * Constructs a livestock asset archetype.
   *
   * @param name Unique taxonomic identifier.
   * @param description Visual description of the livestock type.
   * @param productionInterval Required elapsed turns to manufacture products.
   * @param productionAmount Number of units manufactured per operational cycle.
   * @param goodsSellPrice Fixed price per output unit.
   */
  public Livestock(String name, String description, int productionInterval, int productionAmount, int goodsSellPrice) {
    super(name, description);
    this.productionInterval = productionInterval;
    this.productionAmount = productionAmount;
    this.goodsSellPrice = goodsSellPrice;
  }

  /**
   * Gets the base operations interval.
   *
   * @return The structural turn clock requirement.
   */
  public int getProductionInterval() {
    return this.productionInterval;
  }

  /**
   * Gets the yield quantity produced per cycle.
   *
   * @return Structural output count.
   */
  public int getProductionAmount() {
    return this.productionAmount;
  }

  /**
   * Gets the unit sell price of the derived asset output.
   *
   * @return Output price in dollars.
   */
  public int getGoodsSellPrice() {
    return this.goodsSellPrice;
  }
}
