import java.util.HashMap;
import java.util.ArrayList;

/**
 * Spatial map node tracker capturing inventory elements and dynamic directions.
 */
public class Room {
    private String name;
    private String staticDescription;
    private HashMap<String, String> exits;
    private ArrayList<Item> items;

    public Room(String name, String description) {
        this.name = name;
        this.staticDescription = description;
        this.exits = new HashMap<>();
        this.items = new ArrayList<>();
    }

    public String getName() { return this.name; }
    public void addExit(String direction, String targetRoom) { this.exits.put(direction.toLowerCase(), targetRoom); }
    public String getExit(String direction) { return this.exits.get(direction.toLowerCase()); }
    public void addItem(Item it) { this.items.add(it); }

    public String getDescription(Game context) {
        if (this.name.equals("Sutter's Fort")) {
            if (!context.isCropsReady()) {
                return "Timber walls enclose a patch of tilled soil. To the north lies the American River; east, Sacramento. The year is 1849. Your fields lie quiet — if you had seeds, you could use them on the field.";
            } else {
                return "Your wheat is golden and ripe. You could harvest it now.";
            }
        }
        if (this.name.equals("Sierra Mine")) {
            if (!context.isT1PickaxeOwned() && !context.isT2PickaxeOwned()) {
                return "Coal veins streak the walls; gold gleams from deeper stone. You have no tool to mine any of it.";
            } else if (context.isT1PickaxeOwned() && !context.isT2PickaxeOwned()) {
                return "Coal seams are soft enough for your iron pickaxe — use the pickaxe on the coal vein to mine it. The gold veins are still too hard to crack.";
            } else {
                return "Coal and gold are both within reach. Your steel pickaxe bites cleanly into either — use the pickaxe to mine them.";
            }
        }
        if (this.name.equals("Californio Rancho")) {
            if (context.getCattleCount() == 0) {
                return "A grassy expanse with adobe outbuildings and empty corrals. A fine place to raise cattle, if you bought some at Brannan's Store.";
            } else if (!context.isResourcesAvailable()) {
                return "Your cattle graze peacefully. They have not produced anything new yet.";
            } else {
                return "Your cattle have produced fresh beef and milk. You could collect the goods now.";
            }
        }
        if (this.name.equals("SF Exchange")) {
            if (!context.isLocalStoreOwned() && !context.isRailroadBondOwned()) {
                return "The trading floor of the SF Exchange. A chalkboard lists storefront leases and railroad bonds — you could buy a store or invest in a railroad bond.";
            } else if (context.isLocalStoreOwned() && !context.isRailroadBondOwned()) {
                return "Your storefront deed sits framed by the clerk's desk. You could still invest in a railroad bond.";
            } else if (!context.isLocalStoreOwned() && context.isRailroadBondOwned()) {
                return "The clerk tracks your railroad bond on his ledger. You could still buy a store.";
            } else {
                return "The clerk gestures to your deed and bond. 'Two irons in the fire.'";
            }
        }
        return this.staticDescription;
    }
}
