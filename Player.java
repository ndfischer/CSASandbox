import java.util.ArrayList;

/**
 * Tracks the player's inventory, global state flags, counters, and current location.
 *
 * Encapsulates the game state that is intrinsic to the player character, providing
 * accessors for logic updates inside Game.
 */
public class Player {
  private ArrayList<Item> inventory;
  private Room currentRoom;

  // Counters (as doubles per spec requirements)
  private double liquidMoney = 50.0;
  private double gold = 0.0;
  private double coal = 0.0;
  private double wheat = 0.0;
  private double seeds = 3.0;
  private double plantedSeeds = 0.0;
  private double cattleCount = 0.0;
  private double cattleGoods = 0.0;
  private double bondPrincipal = 0.0;
  private double netWorth = 0.0;

  // Single-instance Boolean Flags
  private boolean membershipBought = false;
  private boolean t1PickaxeOwned = false;
  private boolean t2PickaxeOwned = false;
  private boolean localStoreOwned = false;
  private boolean railroadBondOwned = false;
  private boolean loanTaken = false;
  private boolean railroadBaronPurchased = false;

  /**
   * Constructs a new Player with an empty inventory.
   */
  public Player() {
    inventory = new ArrayList<Item>();
  }

  /**
   * Sets the player's current room.
   *
   * @param room the new Room to place the player in
   */
  public void setCurrentRoom(Room room) {
    this.currentRoom = room;
  }

  /**
   * Gets the player's current room.
   *
   * @return the current Room object
   */
  public Room getCurrentRoom() {
    return currentRoom;
  }

  /**
   * Adds an item to the player's inventory list.
   *
   * @param item the Item to add
   */
  public void addItem(Item item) {
    inventory.add(item);
  }

  /**
   * Removes an item from the player's inventory by index.
   *
   * @param index the position of the item to remove
   */
  public void removeItem(int index) {
    inventory.remove(index);
  }

  /**
   * Finds an item in the inventory by its exact name.
   *
   * @param name the name of the item to search for
   * @return the Item if found, or null if not found
   */
  public Item findItem(String name) {
    // Manual loop required - built-in search methods like indexOf are not allowed
    for (Item i : inventory) {
      if (i.getName().equalsIgnoreCase(name)) {
        return i;
      }
    }
    return null;
  }

  /**
   * Recalculates the player's net worth based on current assets.
   */
  public void updateNetWorth() {
    netWorth = liquidMoney + (gold * 2.50) + (coal * 0.50) + (wheat * 5.0) + (cattleGoods * 10.0) + (cattleCount * 500.0) + bondPrincipal;
  }

  // --- Getters and Setters for Counters ---
  /** @return player's liquid money */
  public double getLiquidMoney() { return liquidMoney; }
  /** @param amt the new money amount */
  public void setLiquidMoney(double amt) { this.liquidMoney = amt; }

  /** @return player's gold amount */
  public double getGold() { return gold; }
  /** @param amt the new gold amount */
  public void setGold(double amt) { this.gold = amt; }

  /** @return player's coal amount */
  public double getCoal() { return coal; }
  /** @param amt the new coal amount */
  public void setCoal(double amt) { this.coal = amt; }

  /** @return player's wheat amount */
  public double getWheat() { return wheat; }
  /** @param amt the new wheat amount */
  public void setWheat(double amt) { this.wheat = amt; }

  /** @return player's seed amount */
  public double getSeeds() { return seeds; }
  /** @param amt the new seed amount */
  public void setSeeds(double amt) { this.seeds = amt; }

  /** @return player's planted seed amount */
  public double getPlantedSeeds() { return plantedSeeds; }
  /** @param amt the new planted seed amount */
  public void setPlantedSeeds(double amt) { this.plantedSeeds = amt; }

  /** @return player's cattle amount */
  public double getCattleCount() { return cattleCount; }
  /** @param amt the new cattle amount */
  public void setCattleCount(double amt) { this.cattleCount = amt; }

  /** @return player's cattle goods amount */
  public double getCattleGoods() { return cattleGoods; }
  /** @param amt the new cattle goods amount */
  public void setCattleGoods(double amt) { this.cattleGoods = amt; }

  /** @return player's bond principal amount */
  public double getBondPrincipal() { return bondPrincipal; }
  /** @param amt the new bond principal */
  public void setBondPrincipal(double amt) { this.bondPrincipal = amt; }

  /** @return player's calculated net worth */
  public double getNetWorth() { return netWorth; }

  // --- Getters and Setters for Flags ---
  /** @return true if membership is bought */
  public boolean isMembershipBought() { return membershipBought; }
  /** @param b sets membership status */
  public void setMembershipBought(boolean b) { this.membershipBought = b; }

  /** @return true if tier 1 pickaxe owned */
  public boolean isT1PickaxeOwned() { return t1PickaxeOwned; }
  /** @param b sets tier 1 pickaxe status */
  public void setT1PickaxeOwned(boolean b) { this.t1PickaxeOwned = b; }

  /** @return true if tier 2 pickaxe owned */
  public boolean isT2PickaxeOwned() { return t2PickaxeOwned; }
  /** @param b sets tier 2 pickaxe status */
  public void setT2PickaxeOwned(boolean b) { this.t2PickaxeOwned = b; }

  /** @return true if local store deed is owned */
  public boolean isLocalStoreOwned() { return localStoreOwned; }
  /** @param b sets local store status */
  public void setLocalStoreOwned(boolean b) { this.localStoreOwned = b; }

  /** @return true if railroad bond is owned */
  public boolean isRailroadBondOwned() { return railroadBondOwned; }
  /** @param b sets railroad bond status */
  public void setRailroadBondOwned(boolean b) { this.railroadBondOwned = b; }

  /** @return true if a loan is taken */
  public boolean isLoanTaken() { return loanTaken; }
  /** @param b sets loan taken status */
  public void setLoanTaken(boolean b) { this.loanTaken = b; }

  /** @return true if railroad baron win condition is purchased */
  public boolean isRailroadBaronPurchased() { return railroadBaronPurchased; }
  /** @param b sets baron purchase status */
  public void setRailroadBaronPurchased(boolean b) { this.railroadBaronPurchased = b; }
}
