// --- FILE: Crop.java ---
/**
 * Represents agricultural commodities harvested by the player.
 *
 * <p>Extends Item to handle farming yields, growth tracking parameters, and 
 * associated market prices.
 */
public class Crop extends Item {
  private int sellPrice;

  /**
   * Constructs a crop commodity.
   *
   * @param name Label for the crop variety.
   * @param description Narrative description of the agricultural product.
   * @param sellPrice Fixed dollar payment per unit harvested.
   */
  public Crop(String name, String description, int sellPrice) {
    super(name, description);
    this.sellPrice = sellPrice;
  }

  /**
   * Fetches the unit market payout price.
   *
   * @return Market dollar value per crop.
   */
  public int getSellPrice() {
    return this.sellPrice;
  }
}
