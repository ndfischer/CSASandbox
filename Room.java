import java.util.ArrayList;

/**
 * Represents a location in the game world.
 *
 * Holds a list of physical items, connected exits, and calculates dynamic
 * descriptions based on the global game state.
 */
public class Room {
  private String name;
  private String description;
  private ArrayList<Item> items;
  private ArrayList<String> exits;

  /**
   * Constructs a Room.
   *
   * @param name the title of the room
   * @param description the default description text
   */
  public Room(String name, String description) {
    this.name = name;
    this.description = description;
    this.items = new ArrayList<Item>();
    this.exits = new ArrayList<String>();
  }

  /**
   * Retrieves the room name.
   *
   * @return the name of the room
   */
  public String getName() {
    return name;
  }

  /**
   * Provides the narrative description of the room based on global state variables.
   *
   * @param game the Game instance used to access state flags
   * @return the precise description string for the current state
   */
  public String getDescription(Game game) {
    if (name.equals("Sutter's Fort")) {
      if (game.isCropsReady()) {
        return "Your wheat is golden and ripe. You could harvest it now.";
      } else {
        return "Timber walls enclose a patch of tilled soil. To the north lies the American River; east, Sacramento. The year is 1849. Your fields lie quiet — if you had seeds, you could use them on the field.";
      }
    } else if (name.equals("Sierra Mine")) {
      if (game.isT2PickaxeOwned()) {
        return "Coal and gold are both within reach. Your steel pickaxe bites cleanly into either — use the pickaxe to mine them.";
      } else if (game.isT1PickaxeOwned()) {
        return "Coal seams are soft enough for your iron pickaxe — use the pickaxe on the coal vein to mine it. The gold veins are still too hard to crack.";
      } else {
        return "Coal veins streak the walls; gold gleams from deeper stone. You have no tool to mine any of it.";
      }
    } else if (name.equals("Californio Rancho")) {
      if (game.getCattleCount() == 0) {
        return "A grassy expanse with adobe outbuildings and empty corrals. A fine place to raise cattle, if you bought some at Brannan's Store.";
      } else if (game.isResourcesAvailable()) {
        return "Your cattle have produced fresh beef and milk. You could collect the goods now.";
      } else {
        return "Your cattle graze peacefully. They have not produced anything new yet.";
      }
    } else if (name.equals("Brannan's Store")) {
      if (game.isMembershipBought()) {
        return "Brannan tips his hat. 'Welcome back. Buy seeds, a pickaxe, an upgrade, or cattle — or sell me your gold, coal, wheat, or goods.'";
      } else {
        return "Sam Brannan's General Store. He eyes you over a ledger. 'Members only, friend. You can sell to me, but you can't buy until you've paid your dues — buy a membership for $100.'";
      }
    } else if (name.equals("San Francisco Exchange")) {
      if (game.isLocalStoreOwned() && game.isRailroadBondOwned()) {
        return "The clerk gestures to your deed and bond. 'Two irons in the fire.'";
      } else if (game.isLocalStoreOwned() && !game.isRailroadBondOwned()) {
        return "Your storefront deed sits framed by the clerk's desk. You could still invest in a railroad bond.";
      } else if (!game.isLocalStoreOwned() && game.isRailroadBondOwned()) {
        return "The clerk tracks your railroad bond on his ledger. You could still buy a store.";
      } else {
        return "The trading floor of the SF Exchange. A chalkboard lists storefront leases and railroad bonds — you could buy a store or invest in a railroad bond.";
      }
    }
    return description;
  }

  /**
   * Adds an exit constraint to the room.
   *
   * @param exit a string formatted as "Direction:Destination"
   */
  public void addExit(String exit) {
    exits.add(exit);
  }

  /**
   * Retrieves the list of exits.
   *
   * @return the ArrayList of exit strings
   */
  public ArrayList<String> getExits() {
    return exits;
  }

  /**
   * Places an item into the room.
   *
   * @param item the Item to add to the room's inventory
   */
  public void addItem(Item item) {
    items.add(item);
  }

  /**
   * Retrieves the physical items present in the room.
   *
   * @return the ArrayList of Items
   */
  public ArrayList<Item> getItems() {
    return items;
  }
}
