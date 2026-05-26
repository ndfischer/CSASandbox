import java.util.ArrayList;

/**
 * Represents the user navigating the text adventure.
 *
 * Contains the player's physical inventory and tracks their location.
 * Used heavily by Game to determine positional state.
 */
public class Player {
  private ArrayList<Item> inventory;
  private Room currentRoom;

  /**
   * Constructs a new Player and initializes an empty inventory.
   */
  public Player() {
    inventory = new ArrayList<Item>();
  }

  /**
   * Adds an item to the player's inventory.
   *
   * @param item the Item to add
   */
  public void addItem(Item item) {
    inventory.add(item);
  }

  /**
   * Removes an item from the player's inventory by its exact name.
   *
   * @param name the name of the item to remove
   */
  public void removeItem(String name) {
    // Manual loop required — built-in search methods are not allowed per AP CS A constraints
    for (int i = 0; i < inventory.size(); i++) {
      if (inventory.get(i).getName().equalsIgnoreCase(name)) {
        inventory.remove(i);
        return;
      }
    }
  }

  /**
   * Searches the player's inventory for an item by name.
   *
   * @param name the name of the item to search for
   * @return the Item if found, or null if it does not exist in inventory
   */
  public Item findItem(String name) {
    // Manual loop required — built-in search methods are not allowed
    for (Item item : inventory) {
      if (item.getName().equalsIgnoreCase(name)) {
        return item;
      }
    }
    return null;
  }

  /**
   * Sets the room the player is currently occupying.
   *
   * @param room the destination Room object
   */
  public void setCurrentRoom(Room room) {
    this.currentRoom = room;
  }

  /**
   * Retrieves the current room the player is in.
   *
   * @return the Room object
   */
  public Room getCurrentRoom() {
    return currentRoom;
  }
}
