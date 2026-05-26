// --- FILE: Room.java ---
import java.util.ArrayList;

/**
 * Represents a physical location in the game world.
 *
 * Holds the room's name, its dynamic description logic, a list of items contained
 * within it, and a list of exits formatted as "Direction:Destination".
 * Used by Game to handle movement and environment interaction.
 */
public class Room {
  private String name;
  private String description;
  private ArrayList<Item> items;
  private ArrayList<String> exits;

  /**
   * Constructs a new Room with the given name and a base description.
   *
   * @param name the name of the room
   * @param description the fallback/base description of the room
   */
  public Room(String name, String description) {
    this.name = name;
    this.description = description;
    this.items = new ArrayList<Item>();
    this.exits = new ArrayList<String>();
  }

  /**
   * Retrieves the name of the room.
   *
   * @return the room name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Retrieves the dynamic description of the room based on current game and player state.
   *
   * @param game the current game instance containing global state
   * @param player the player instance containing single-item flags and counters
   * @return the appropriate description text for the current state
   */
  public String getDescription(Game game, Player player) {
    if (this.name.equalsIgnoreCase("Sutter's Fort")) {
      if (game.isCropsReady()) {
        return "Your wheat is golden and ripe. You could harvest it now.";
      } else {
        return "Timber walls enclose a patch of tilled soil. To the north lies the American River; east, Sacramento. The year is 1849. Your fields lie quiet — if you had seeds, you could plant them here.";
      }
    } else if (this.name.equalsIgnoreCase("Sierra Mine")) {
      if (!player.isT1PickaxeOwned() && !player.isT2PickaxeOwned()) {
        return "Coal veins streak the walls; gold gleams from deeper stone. You have no tool to mine any of it.";
      } else if (player.isT1PickaxeOwned() && !player.isT2PickaxeOwned()) {
        return "Coal seams are soft enough for your iron pickaxe — use the pickaxe on the coal vein to mine it. The gold veins are still out of reach.";
      } else if (player.isT2PickaxeOwned()) {
        return "Coal and gold are both within reach. Your steel pickaxe bites cleanly into either — use the pickaxe to mine them.";
      }
    } else if (this.name.equalsIgnoreCase("Californio Rancho")) {
      if (player.getCattleCount() == 0) {
        return "A grassy expanse with adobe outbuildings and empty corrals. A fine place to raise cattle, if you bought some at Brannan's Store.";
      } else if (player.getCattleCount() > 0 && !game.isResourcesAvailable()) {
        return "Your cattle graze peacefully. They have not produced anything new yet.";
      } else if (player.getCattleCount() > 0 && game.isResourcesAvailable()) {
        return "Your cattle have produced fresh beef and milk. You could collect the goods now.";
      }
    } else if (this.name.equalsIgnoreCase("Brannan's Store")) {
      if (!player.isMembershipBought()) {
        return "Sam Brannan's General Store. He eyes you over a ledger. 'Members only, friend. You can sell to me, but you can't buy until you've paid your dues — $100.'";
      } else {
        return "Brannan tips his hat. 'Welcome back. Buy seeds, a pickaxe, an upgrade, or cattle — or sell me your gold, coal, wheat, or goods.'";
      }
    } else if (this.name.equalsIgnoreCase("San Francisco Exchange")) {
      if (!player.isLocalStoreOwned() && !player.isRailroadBondOwned()) {
        return "The trading floor of the SF Exchange. A chalkboard lists storefront leases and railroad bonds — you could buy a store or invest in a railroad.";
      } else if (player.isLocalStoreOwned() && !player.isRailroadBondOwned()) {
        return "Your storefront deed sits framed by the clerk's desk. You could still invest in a railroad bond.";
      } else if (!player.isLocalStoreOwned() && player.isRailroadBondOwned()) {
        return "The clerk tracks your railroad bond on his ledger. You could still buy a store.";
      } else if (player.isLocalStoreOwned() && player.isRailroadBondOwned()) {
        return "The clerk gestures to your deed and bond. 'Two irons in the fire.'";
      }
    }
    return this.description;
  }

  /**
   * Adds an exit to the room.
   *
   * @param direction the direction of the exit (e.g., "North")
   * @param destination the name of the destination room
   */
  public void addExit(String direction, String destination) {
    this.exits.add(direction + ":" + destination);
  }

  /**
   * Retrieves the list of exits for this room.
   *
   * @return the list of exit strings
   */
  public ArrayList<String> getExits() {
    return this.exits;
  }

  /**
   * Adds an item to the room.
   *
   * @param item the item to place in the room
   */
  public void addItem(Item item) {
    this.items.add(item);
  }

  /**
   * Retrieves the list of items in the room.
   *
   * @return the list of items
   */
  public ArrayList<Item> getItems() {
    return this.items;
  }
}
