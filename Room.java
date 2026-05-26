import java.util.ArrayList;

/**
 * Represents a discrete location within the game world.
 *
 * Stores the room's base attributes, available physical items (fixtures), and exits.
 * Dynamically resolves its description based on the state of the Game and Player.
 */
public class Room {
  private String name;
  private String description;
  private ArrayList<Item> items;
  private ArrayList<String> exits;

  /**
   * Constructs a Room with a name and base description.
   *
   * @param name the unique name of the room
   * @param description the fallback base description (if no dynamic state overrides it)
   */
  public Room(String name, String description) {
    this.name = name;
    this.description = description;
    this.items = new ArrayList<Item>();
    this.exits = new ArrayList<String>();
  }

  /**
   * Adds an exit mapping to this room.
   *
   * @param direction the command text used to travel (e.g., "North")
   * @param destination the exact name of the destination room
   */
  public void addExit(String direction, String destination) {
    exits.add(direction + ":" + destination);
  }

  /**
   * Adds a physical item or fixture to the room.
   *
   * @param item the Item to place in the room
   */
  public void addItem(Item item) {
    items.add(item);
  }

  /**
   * Gets the name of the room.
   *
   * @return the room name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the list of current items or fixtures in the room.
   *
   * @return an ArrayList of Items
   */
  public ArrayList<Item> getItems() {
    return items;
  }

  /**
   * Gets the raw string list of exits for internal navigation logic.
   *
   * @return an ArrayList of "Direction:Destination" strings
   */
  public ArrayList<String> getExits() {
    return exits;
  }

  /**
   * Resolves the room's description dynamically based on the game state.
   *
   * Uses exact text provided in the specification for every variation.
   *
   * @param game the current Game instance to check world flags
   * @param player the Player instance to check personal flags/counters
   * @return the correctly evaluated text description for this room
   */
  public String getDescription(Game game, Player player) {
    if (name.equalsIgnoreCase("Sutter's Fort")) {
      if (game.isCropsReady()) {
        return "Your wheat is golden and ripe. You could harvest it now.";
      } else {
        return "Timber walls enclose a patch of tilled soil. To the north lies the American River; east, Sacramento. The year is 1849. Your fields lie quiet — if you had seeds, you could plant them here.";
      }
    } 
    else if (name.equalsIgnoreCase("Sierra Mine")) {
      if (player.isT2PickaxeOwned()) {
        return "Coal and gold are both within reach. Your steel pickaxe bites cleanly into either — use the pickaxe to mine them.";
      } else if (player.isT1PickaxeOwned()) {
        return "Coal seams are soft enough for your iron pickaxe — use the pickaxe on the coal vein to mine it. The gold veins are still out of reach.";
      } else {
        return "Coal veins streak the walls; gold gleams from deeper stone. You have no tool to mine any of it.";
      }
    }
    else if (name.equalsIgnoreCase("Californio Rancho")) {
      if (player.getCattleCount() == 0) {
        return "A grassy expanse with adobe outbuildings and empty corrals. A fine place to raise cattle, if you bought some at Brannan's Store.";
      } else if (player.getCattleCount() > 0 && !game.isResourcesAvailable()) {
        return "Your cattle graze peacefully. They have not produced anything new yet.";
      } else {
        return "Your cattle have produced fresh beef and milk. You could collect the goods now.";
      }
    }
    else if (name.equalsIgnoreCase("Brannan's Store")) {
      if (!player.isMembershipBought()) {
        return "Sam Brannan's General Store. He eyes you over a ledger. 'Members only, friend. You can sell to me, but you can't buy until you've paid your dues — $100.'";
      } else {
        return "Brannan tips his hat. 'Welcome back. Buy seeds, a pickaxe, an upgrade, or cattle — or sell me your gold, coal, wheat, or goods.'";
      }
    }
    else if (name.equalsIgnoreCase("San Francisco Exchange")) {
      if (!player.isLocalStoreOwned() && !player.isRailroadBondOwned()) {
        return "The trading floor of the SF Exchange. A chalkboard lists storefront leases and railroad bonds — you could buy a store or invest in a railroad.";
      } else if (player.isLocalStoreOwned() && !player.isRailroadBondOwned()) {
        return "Your storefront deed sits framed by the clerk's desk. You could still invest in a railroad bond.";
      } else if (!player.isLocalStoreOwned() && player.isRailroadBondOwned()) {
        return "The clerk tracks your railroad bond on his ledger. You could still buy a store.";
      } else {
        return "The clerk gestures to your deed and bond. 'Two irons in the fire.'";
      }
    }
    
    // Return standard description for rooms with static descriptions
    return description;
  }
}
