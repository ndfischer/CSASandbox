// --- FILE: Item.java ---
/**
 * Base class for all interactive items and fixtures in the world.
 *
 * <p>Provides a foundation for tracking physical components via standard polymorphic 
 * structures within rooms and player inventories.
 */
public class Item {
  private String name;
  private String description;

  /**
   * Constructs an item with a name and a narrative description.
   *
   * @param name The multi-word string key identifying the item.
   * @param description The textual inspection text shown to players.
   */
  public Item(String name, String description) {
    this.name = name;
    this.description = description;
  }

  /**
   * Returns the string identifier of the item.
   *
   * @return The item name.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the narrative description of the item.
   *
   * @return The detailed description text.
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Overrides string serialization to print the name of the item.
   *
   * @return The item's clean name string.
   */
  @Override
  public String toString() {
    return this.name;
  }
}
