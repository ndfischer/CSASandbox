// --- FILE: Room.java ---
import java.util.ArrayList;

/**
 * Encapsulates regional narrative zones, pathways, and contextual text adjustments.
 *
 * <p>Updated to explicitly handle strict grammar mapping references and 
 * matching narrative text structures requested by the updated specification.
 */
public class Room {
  private String name;
  private String description;
  private ArrayList<Item> items;
  private ArrayList<String> exits;

  /**
   * Instantiates an active map cell inside the coordinate infrastructure arrays.
   *
   * @param name Programmatic look-up title string.
   * @param description Fallback baseline environmental message text.
   */
  public Room(String name, String description) {
    this.name = name;
    this.description = description;
    this.items = new ArrayList<Item>();
    this.exits = new ArrayList<String>();
  }

  public String getName() {
    return this.name;
  }

  /**
   * Dynamically tracks runtime state metrics to output target room narratives.
   *
   * @param game Direct reference to system variables mapping parameters.
   * @return Contextual localized area descriptions.
   */
  public String getDescription(Game game) {
    if (this.name.equalsIgnoreCase("Sutter's Fort")) {
      if (game.isCropsReady()) {
        return "Your wheat is golden and ripe. You could harvest it now.";
      } else {
        return "Timber walls enclose a patch of tilled soil. To the north lies the American River; east, Sacramento. The year is 1849. Your fields lie quiet — if you had seeds, you could use them on the field.";
      }
    } else if (this.name.equalsIgnoreCase("Sierra Mine")) {
      if (!game.isT1PickaxeOwned() && !game.isT2PickaxeOwned()) {
        return "Coal veins streak the walls; gold gleams from deeper stone. You have no tool to mine any of it.";
      } else if (game.isT1PickaxeOwned() && !game.isT2PickaxeOwned()) {
        return "Coal seams are soft enough for your iron pickaxe — use the pickaxe on the coal vein to mine it. The gold veins are still too hard to crack.";
      } else if (game.isT2PickaxeOwned()) {
        return "Coal and gold are both within reach. Your steel pickaxe bites cleanly into either — use the pickaxe to mine them.";
      }
    } else if (this.name.equalsIgnoreCase("Californio Rancho")) {
      if (game.getCattleCount() == 0) {
        return "A grassy expanse with adobe outbuildings and empty corrals. A fine place to raise cattle, if you bought some at Brannan's Store.";
      } else if (game.getCattleCount() > 0 && !game.isResourcesAvailable()) {
        return "Your cattle graze peacefully. They have not produced anything new yet.";
      } else if (game.getCattleCount() > 0 && game.isResourcesAvailable()) {
        return "Your cattle have produced fresh beef and milk. You could collect the goods now.";
      }
    } else if (this.name.equalsIgnoreCase("Brannan's Store")) {
      if (!game.isMembershipBought()) {
        return "Sam Brannan's General Store. He eyes you over a ledger. 'Members only, friend. You can sell to me, but you can't buy until you've paid your dues — buy a membership for $100.'";
      } else {
        return "Brannan tips his hat. 'Welcome back. Buy seeds, a pickaxe, an upgrade, or cattle — or sell me your gold, coal, wheat, or goods.'";
      }
    } else if (this.name.equalsIgnoreCase("SF Exchange")) {
      if (!game.isLocalStoreOwned() && !game.isRailroadBondOwned()) {
        return "The trading floor of the SF Exchange. A chalkboard lists storefront leases and railroad bonds — you could buy a store or invest in a railroad bond.";
      } else if (game.isLocalStoreOwned() && !game.isRailroadBondOwned()) {
        return "Your storefront deed sits framed by the clerk's desk. You could still invest in a railroad bond.";
      } else if (!game.isLocalStoreOwned() && game.isRailroadBondOwned()) {
        return "The clerk tracks your railroad bond on his ledger. You could still buy a store.";
      } else if (game.isLocalStoreOwned() && game.isRailroadBondOwned()) {
        return "The clerk gestures to your deed and bond. 'Two irons in the fire.'";
      }
    }
    return this.description;
  }

  public void addItem(Item item) {
    this.items.add(item);
  }

  public ArrayList<Item> getItems() {
    return this.items;
  }

  public void addExit(String exit) {
    this.exits.add(exit);
  }

  public ArrayList<String> getExits() {
    return this.exits;
  }
}
