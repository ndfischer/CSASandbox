// --- FILE: Player.java ---
import java.util.ArrayList;

/**
 * Tracks the player's entity properties, inventory tokens, and location markers.
 *
 * <p>Operates alongside rooms and the engine to validate spatial or material tasks 
 * while maintaining strict encapsulation.
 */
public class Player {
  private ArrayList<Item> inventory;
  private Room currentRoom;

  /**
   * Initializes state elements tracking individual player records.
   */
  public Player() {
    this.inventory = new ArrayList<Item>();
  }

  /**
   * Standardizes movement updates resetting current coordinates.
   *
   * @param room Next target Room object node mapping location.
   */
  public void setCurrentRoom(Room room) {
    this.currentRoom = room;
  }

  /**
   * Returns current structural zone coordinates.
   *
   * @return The active Room object reference context.
   */
  public Room getCurrentRoom() {
    return this.currentRoom;
  }

  /**
   * Fetches full container object lists.
   *
   * @return Active standard inventory element lists.
   */
  public ArrayList<Item> getInventory() {
    return this.inventory;
  }

  /**
   * Appends items inside internal inventory trackers.
   *
   * @param item Item being collected.
   */
  public void addItem(Item item) {
    this.inventory.add(item);
  }

  /**
   * Removes targeted items by structural collection index offsets.
   *
   * @param index Targeted loop iteration indices tracking inventory.
   */
  public void removeItem(int index) {
    this.inventory.remove(index);
  }

  /**
   * Evaluates if an item matches target criteria using clean looping paradigms.
   *
   * @param name Target multi-word lookup identity token.
   * @return Matching item instance if found, or null.
   */
  public Item findItem(String name) {
    // Manual loop required — built-in search methods are not allowed per AP CS A constraints
    for (Item item : this.inventory) {
      if (item.getName().equalsIgnoreCase(name)) {
        return item;
      }
    }
    return null;
  }
}
