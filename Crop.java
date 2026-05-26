/**
 * Represents an agricultural crop grown by the player.
 *
 * Extends Item to include selling prices for crops.
 */
public class Crop extends Item {
  private int sellPrice;

  /**
   * Constructs a Crop item.
   *
   * @param name the name of the crop
   * @param description the description of the crop
   * @param sellPrice the value the crop sells for
   */
  public Crop(String name, String description, int sellPrice) {
    super(name, description);
    this.sellPrice = sellPrice;
  }

  /**
   * Retrieves the sell price of the crop.
   *
   * @return the sell price as an integer
   */
  public int getSellPrice() {
    return sellPrice;
  }
}
