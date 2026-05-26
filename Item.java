/**
 * Base class representing any physical entity or fixture in the game world.
 *
 * Demonstrates basic encapsulation and serves as the root of the item hierarchy.
 * Used by Room to hold interactable fixtures and by subclasses for specialized data.
 */
public class Item {
  private String name;
  private String description;

  /**
   * Constructs a new Item with a name and description.
   *
   * @param name the display name of the item
   * @param description the visual or functional description of the item
   */
  public Item(String name, String description) {
    this.name = name;
    this.description = description;
  }

  /**
   * Retrieves the name of the item.
   *
   * @return the item name
   */
  public String getName() {
    return name;
  }

  /**
   * Retrieves the description of the item.
   *
   * @return the item description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the item name as its string representation.
   *
   * @return the string name of the item
   */
  @Override
  public String toString() {
    return name;
  }
}
