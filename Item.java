// --- FILE: Item.java ---
/**
 * Base class for all interactive and descriptive items in the game.
 *
 * This class provides the fundamental properties of an item, including its name
 * and description. It is extended by various resource classes (Ore, Crop, Livestock)
 * and used to represent physical fixtures in the game world.
 */
public class Item {
  private String name;
  private String description;

  /**
   * Constructs a new Item with the specified name and description.
   *
   * @param name the name of the item
   * @param description a brief description of the item
   */
  public Item(String name, String description) {
    this.name = name;
    this.description = description;
  }

  /**
   * Retrieves the name of the item.
   *
   * @return the item's name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Retrieves the description of the item.
   *
   * @return the item's description
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Returns a string representation of the item, which is its name.
   *
   * @return the name of the item
   */
  @Override
  public String toString() {
    return this.name;
  }
}
