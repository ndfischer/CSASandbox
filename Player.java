// --- FILE: Player.java ---
import java.util.ArrayList;

/**
 * Represents the player character and their state in the game.
 *
 * Tracks the player's current room, physical inventory, logical item counters,
 * and purchased status flags (single-instance items like deeds and pickaxes).
 * Used by Game to evaluate conditions and by Room for contextual descriptions.
 */
public class Player {
  private ArrayList<Item> inventory;
  private Room currentRoom;

  // Single-instance items tracked by boolean flags
  private boolean membershipBought = false;
  private boolean t1PickaxeOwned = false;
  private boolean t2PickaxeOwned = false;
  private boolean localStoreOwned = false;
  private boolean railroadBondOwned = false;
  private boolean loanTaken = false;
  private boolean railroadBaronPurchased = false;

  // Counters
  private double liquidMoney = 50.0;
  private double gold = 0;
  private double coal = 0;
  private double wheat = 0;
  private double seeds = 3;
  private double plantedSeeds = 0;
  private double cattleCount = 0;
  private double cattleGoods = 0;
  private double bondPrincipal = 0;

  /**
   * Constructs a new Player with an empty inventory.
   */
  public Player() {
    this.inventory = new ArrayList<Item>();
  }

  /**
   * Sets the room the player is currently in.
   *
   * @param room the new room for the player
   */
  public void setCurrentRoom(Room room) {
    this.currentRoom = room;
  }

  /**
   * Retrieves the player's current room.
   *
   * @return the current room
   */
  public Room getCurrentRoom() {
    return this.currentRoom;
  }

  /**
   * Adds an item to the player's physical inventory.
   *
   * @param item the item to add
   */
  public void addItem(Item item) {
    this.inventory.add(item);
  }

  /**
   * Removes an item from the player's physical inventory.
   *
   * @param item the item to remove
   */
  public void removeItem(Item item) {
    // Manual loop required — built-in search methods are not allowed per AP CS A constraints
    for (int i = 0; i < this.inventory.size(); i++) {
      if (this.inventory.get(i) == item) {
        this.inventory.remove(i);
        break;
      }
    }
  }

  /**
   * Finds an item in the player's inventory by its name.
   *
   * @param itemName the name of the item to search for
   * @return the Item object if found, or null if not found
   */
  public Item findItem(String itemName) {
    // Manual loop required — built-in search methods are not allowed per AP CS A constraints
    for (Item item : this.inventory) {
      if (item.getName().equalsIgnoreCase(itemName)) {
        return item;
      }
    }
    return null;
  }

  /**
   * Calculates and returns the player's net worth based on inventory and assets.
   *
   * @return the total calculated net worth
   */
  public double getNetWorth() {
    return liquidMoney + (gold * 2.50) + (coal * 0.50) + (wheat * 5) + (cattleGoods * 10) + (cattleCount * 500) + bondPrincipal;
  }

  // Getters and Setters for boolean flags
  /** @return true if membership is bought, false otherwise */
  public boolean isMembershipBought() { return membershipBought; }
  /** @param val the new membership status */
  public void setMembershipBought(boolean val) { this.membershipBought = val; }

  /** @return true if tier 1 pickaxe is owned, false otherwise */
  public boolean isT1PickaxeOwned() { return t1PickaxeOwned; }
  /** @param val the new t1 pickaxe status */
  public void setT1PickaxeOwned(boolean val) { this.t1PickaxeOwned = val; }

  /** @return true if tier 2 pickaxe is owned, false otherwise */
  public boolean isT2PickaxeOwned() { return t2PickaxeOwned; }
  /** @param val the new t2 pickaxe status */
  public void setT2PickaxeOwned(boolean val) { this.t2PickaxeOwned = val; }

  /** @return true if local store is owned, false otherwise */
  public boolean isLocalStoreOwned() { return localStoreOwned; }
  /** @param val the new local store status */
  public void setLocalStoreOwned(boolean val) { this.localStoreOwned = val; }

  /** @return true if a railroad bond is owned, false otherwise */
  public boolean isRailroadBondOwned() { return railroadBondOwned; }
  /** @param val the new railroad bond status */
  public void setRailroadBondOwned(boolean val) { this.railroadBondOwned = val; }

  /** @return true if a loan is currently taken, false otherwise */
  public boolean isLoanTaken() { return loanTaken; }
  /** @param val the new loan taken status */
  public void setLoanTaken(boolean val) { this.loanTaken = val; }

  /** @return true if railroad baron status is purchased, false otherwise */
  public boolean isRailroadBaronPurchased() { return railroadBaronPurchased; }
  /** @param val the new baron status */
  public void setRailroadBaronPurchased(boolean val) { this.railroadBaronPurchased = val; }

  // Getters and Setters for counters
  /** @return liquid money */
  public double getLiquidMoney() { return liquidMoney; }
  /** @param amt new liquid money */
  public void setLiquidMoney(double amt) { this.liquidMoney = amt; }

  /** @return gold count */
  public double getGold() { return gold; }
  /** @param amt new gold count */
  public void setGold(double amt) { this.gold = amt; }

  /** @return coal count */
  public double getCoal() { return coal; }
  /** @param amt new coal count */
  public void setCoal(double amt) { this.coal = amt; }

  /** @return wheat count */
  public double getWheat() { return wheat; }
  /** @param amt new wheat count */
  public void setWheat(double amt) { this.wheat = amt; }

  /** @return seed count */
  public double getSeeds() { return seeds; }
  /** @param amt new seed count */
  public void setSeeds(double amt) { this.seeds = amt; }

  /** @return planted seed count */
  public double getPlantedSeeds() { return plantedSeeds; }
  /** @param amt new planted seed count */
  public void setPlantedSeeds(double amt) { this.plantedSeeds = amt; }

  /** @return cattle count */
  public double getCattleCount() { return cattleCount; }
  /** @param amt new cattle count */
  public void setCattleCount(double amt) { this.cattleCount = amt; }

  /** @return cattle goods count */
  public double getCattleGoods() { return cattleGoods; }
  /** @param amt new cattle goods count */
  public void setCattleGoods(double amt) { this.cattleGoods = amt; }

  /** @return bond principal */
  public double getBondPrincipal() { return bondPrincipal; }
  /** @param amt new bond principal */
  public void setBondPrincipal(double amt) { this.bondPrincipal = amt; }
}
