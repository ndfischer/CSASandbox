// --- FILE: Room.java ---
import java.util.ArrayList;

/**
 * Encapsulates geographic zones, path connections, and environment features.
 *
 * <p>Used by Game to track world locations, handle navigation mappings, and 
 * provide dynamic narrative text based on global flags.
 */
public class Room {
  private String name;
  private String description;
  private ArrayList<Item> items;
  private ArrayList<String> exits;

  /**
   * Instantiates a distinct game world environment zone.
   *
   * @param name Immutable programmatic room identifier string.
   * @param description Static default fallback environmental narrative.
   */
  public Room(String name, String description) {
    this.name = name;
    this.description = description;
    this.items = new ArrayList<Item>();
    this.exits = new ArrayList<String>();
  }

  /**
   * Returns structural room lookup identifier.
   *
   * @return Room programmatic name string.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Resolves text narrative dynamically reflecting current boolean world states.
   *
   * @param game Reference to primary game state engine containing global toggles.
   * @return Exactly mapped room description matching criteria.
   */
  public String getDescription(Game game) {
    if (this.name.equalsIgnoreCase("Sutter's Fort")) {
      if (game.isCropsReady()) {
        return "Your wheat is golden and ripe, ready to be cut.";
      } else {
        return "Timber walls enclose a patch of tilled soil. To the north lies the American River; east, Sacramento. The year is 1849. Your fields lie quiet.";
      }
    } else if (this.name.equalsIgnoreCase("Sierra Mine")) {
      if (!game.isT1PickaxeOwned() && !game.isT2PickaxeOwned()) {
        return "Coal veins streak the walls; gold gleams from deeper stone. You have no tool to mine any of it.";
      } else if (game.isT1PickaxeOwned() && !game.isT2PickaxeOwned()) {
        return "Coal seams are soft enough for your iron pickaxe. The gold veins are still too hard to crack.";
      } else if (game.isT2PickaxeOwned()) {
        return "Coal and gold are both within reach. Your steel pickaxe bites cleanly into either.";
      }
    } else if (this.name.equalsIgnoreCase("Californio Rancho")) {
      if (game.getCattleCount() == 0) {
        return "A grassy expanse with adobe outbuildings and empty corrals. A fine place to raise cattle, if you owned any.";
      } else if (game.getCattleCount() > 0 && !game.isResourcesAvailable()) {
        return "Your cattle graze peacefully. They have not produced anything new yet.";
      } else if (game.getCattleCount() > 0 && game.isResourcesAvailable()) {
        return "Your cattle have produced fresh beef and milk. The goods are ready to be collected.";
      }
    } else if (this.name.equalsIgnoreCase("Brannan's Store")) {
      if (!game.isMembershipBought()) {
        return "Sam Brannan's General Store. He eyes you over a ledger. 'Members only, friend. You can sell to me, but you can't buy until you've paid your dues.'";
      } else {
        return "Brannan tips his hat. 'Welcome back. What'll it be?'";
      }
    } else if (this.name.equalsIgnoreCase("SF Exchange")) {
      if (!game.isLocalStoreOwned() && !game.isRailroadBondOwned()) {
        return "The trading floor of the SF Exchange. A chalkboard lists storefront leases and railroad bonds.";
      } else if (game.isLocalStoreOwned() && !game.isRailroadBondOwned()) {
        return "Your storefront deed sits framed by the clerk's desk.";
      } else if (!game.isLocalStoreOwned() && game.isRailroadBondOwned()) {
        return "The clerk tracks your railroad bond on his ledger.";
      } else if (game.isLocalStoreOwned() && game.isRailroadBondOwned()) {
        return "The clerk gestures to your deed and bond. 'Two irons in the fire.'";
      }
    }
    return this.description;
  }

  /**
   * Registers a unique item container artifact within the boundaries of the room.
   *
   * @param item Item reference being dropped or seeded.
   */
  public void addItem(Item item) {
    this.items.add(item);
  }

  /**
   * Evicts a target physical item inside this room by programmatic array index.
   *
   * @param index Array offset integer.
   */
  public void removeItem(int index) {
    this.items.remove(index);
  }

  /**
   * Fetches internal list references for environment analysis loops.
   *
   * @return Underlying room instances list tracking inventory.
   */
  public ArrayList<Item> getItems() {
    return this.items;
  }

  /**
   * Injects structural adjacency mappings formatted as "Direction:Destination".
   *
   * @param exit Connection tuple string mapping structural pathways.
   */
  public void addExit(String exit) {
    this.exits.add(exit);
  }

  /**
   * Extracts structural exit array matrices.
   *
   * @return Direction mapping string lists.
   */
  public ArrayList<String> getExits() {
    return this.exits;
  }
}



