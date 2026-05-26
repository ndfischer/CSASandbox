/**
 * Base class representing an interactable object or entity.
 *
 * Provides core properties like name and description that are extended
 * by specific resource and entity types throughout the game.
 */
public class Item {
  private String name;
  private String description;

  /**
   * Constructs a basic Item.
   *
   * @param name the name of the item
   * @param description the description of the item
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
    return name;
  }

  /**
   * Retrieves the description of the item.
   *
   * @return the item's description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns a string representation of the item.
   *
   * @return the item's name
   */
  @Override
  public String toString() {
    return name;
  }
}
