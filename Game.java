// --- FILE: Game.java ---
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

/**
 * Primary game simulation state coordinator engine.
 *
 * <p>Updated to enforce strict vocabulary boundaries, process nested conditional
 * random speculation curves for investments, and manage the bank loan process.
 */
public class Game {
  // Global Spec Flags
  private boolean cropsReady = false;
  private boolean resourcesAvailable = false;
  private boolean membershipBought = false;
  private boolean t1PickaxeOwned = false;
  private boolean t2PickaxeOwned = false;
  private boolean localStoreOwned = false;
  private boolean railroadBondOwned = false;
  private boolean loanTaken = false;
  private boolean loanAvailable = true;
  private boolean railroadBaronPurchased = false;

  // Global Resource Accumulators
  private double liquidMoney = 50.0;
  private double gold = 0.0;
  private double coal = 0.0;
  private double wheat = 0.0;
  private double seeds = 3.0;
  private double plantedSeeds = 0.0;
  private double cattleCount = 0.0;
  private double cattleGoods = 0.0;
  private double bondPrincipal = 0.0;
  private double turn = 0.0;

  // Turn-based Processing Tracking Clocks
  private int plantTimer = 0;
  private int cattleTimer = 0;
  private int bondTimer = 0;
  private int loanDenialTimer = 0;
  private double pendingLoanRate = 0.0;
  private boolean loanOffered = false;
  private double netWorth = 50.0;

  private ArrayList<Room> rooms;
  private Player player;
  private boolean isRunning = true;
  private Random random;

  /**
   * Instantiates structural mechanics and maps layouts.
   */
  public Game() {
    this.rooms = new ArrayList<Room>();
    this.player = new Player();
    this.random = new Random();
    this.initializeWorld();
  }

  public boolean isCropsReady() { return this.cropsReady; }
  public boolean isResourcesAvailable() { return this.resourcesAvailable; }
  public boolean isMembershipBought() { return this.membershipBought; }
  public boolean isT1PickaxeOwned() { return this.t1PickaxeOwned; }
  public boolean isT2PickaxeOwned() { return this.t2PickaxeOwned; }
  public boolean isLocalStoreOwned() { return this.localStoreOwned; }
  public boolean isRailroadBondOwned() { return this.railroadBondOwned; }
  public double getCattleCount() { return this.cattleCount; }

  private void initializeWorld() {
    Room suttersFort = new Room("Sutter's Fort", "");
    Room americanRiver = new Room("American River", "The American River runs cold over dark gravel. A battered tin pan sits on a rock at the bank. Specks of yellow shimmer in the riverbed — you could pan here.");
    Room sierraMine = new Room("Sierra Mine", "");
    Room californioRancho = new Room("Californio Rancho", "");
    Room sacramento = new Room("Sacramento", "The supply town at the confluence of two rivers. Signs point south to the store, north to the newspaper, east to the bank, and west home.");
    Room brannansStore = new Room("Brannan's Store", "");
    Room dailyAlta = new Room("Daily Alta California", "A newsboy hawks copies of the Alta California — you could read one for the date and the headline. A carriage road climbs north toward Nob Hill.");
    Room wellsFargo = new Room("Wells Fargo Bank", "The offices of Wells Fargo and Co. Brass railings, heavy ledgers, strongboxes. A teller looks up. 'Need a loan? Just say the word.'");
    Room sfExchange = new Room("SF Exchange", "");
    Room bigFourMansion = new Room("Big Four Mansion", "The marble foyer of the Big Four Mansion atop Nob Hill. A lobbyist extends a hand. The leather chair by the window is the Railroad Baron's — for $100,000, you could buy baron status and take that seat.");

    suttersFort.addItem(new Item("Field", "A patch of tilled soil ready for planting seeds."));
    americanRiver.addItem(new Item("Pan", "A battered tin pan sitting on a rock near the riverbank."));
    sierraMine.addItem(new Item("Coal Vein", "A thick dark seam of high-grade coal stretching into the rock."));
    sierraMine.addItem(new Item("Gold Vein", "A glittering quartz seam laced with deep tracks of native gold."));
    californioRancho.addItem(new Item("Pasture", "Open fields perfectly suited for grazing cattle operations."));
    brannansStore.addItem(new Item("Counter", "A sturdy wooden service desk hosting sales logs."));
    dailyAlta.addItem(new Item("Newspaper", "Fresh prints covering development news."));
    wellsFargo.addItem(new Item("Teller's Window", "A secure service window managed by analytical accounts staff."));
    sfExchange.addItem(new Item("Chalkboard", "A trading dashboard tracking available lots and infrastructure titles."));
    bigFourMansion.addItem(new Item("Baron's Chair", "The highly decorated seat signifying regional rail dominance."));

    suttersFort.addExit("North:American River");
    suttersFort.addExit("East:Sacramento");
    suttersFort.addExit("South:Californio Rancho");
    suttersFort.addExit("West:Sierra Mine");

    americanRiver.addExit("South:Sutter's Fort");
    sierraMine.addExit("East:Sutter's Fort");
    californioRancho.addExit("North:Sutter's Fort");

    sacramento.addExit("North:Daily Alta California");
    sacramento.addExit("East:Wells Fargo Bank");
    sacramento.addExit("South:Brannan's Store");
    sacramento.addExit("West:Sutter's Fort");

    brannansStore.addExit("North:Sacramento");
    dailyAlta.addExit("South:Sacramento");
    dailyAlta.addExit("North:Big Four Mansion");

    wellsFargo.addExit("West:Sacramento");
    wellsFargo.addExit("East:SF Exchange");
    sfExchange.addExit("West:Wells Fargo Bank");
    bigFourMansion.addExit("South:Daily Alta California");

    this.rooms.add(suttersFort);
    this.rooms.add(americanRiver);
    this.rooms.add(sierraMine);
    this.rooms.add(californioRancho);
    this.rooms.add(sacramento);
    this.rooms.add(brannansStore);
    this.rooms.add(dailyAlta);
    this.rooms.add(wellsFargo);
    this.rooms.add(sfExchange);
    this.rooms.add(bigFourMansion);

    this.player.setCurrentRoom(suttersFort);
  }

  public void start() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("--- GOLD RUSH CALIFORNIA ---");
    System.out.println("Type 'look' to survey your surroundings. Type 'help' to review structural commands.");

    while (this.isRunning) {
      System.out.print("\n> ");
      if (!scanner.hasNextLine()) break;
      String input = scanner.nextLine().trim();
      if (input.length() == 0) continue;
      this.processCommand(input);
    }
    scanner.close();
  }

  private Room findRoom(String roomName) {
    for (Room r : this.rooms) {
      if (r.getName().equalsIgnoreCase(roomName)) return r;
    }
    return null;
  }

  private void processCommand(String rawInput) {
    String lowerInput = rawInput.toLowerCase();

    // Universal Base Commands
    if (lowerInput.equals("help")) {
      System.out.println("Valid Commands: go [direction], help, look, inventory, mine coal, mine gold, plant seeds, pan, harvest, collect, read, loan, accept loan, deny loan, buy [thing], sell [item], invest railroad [amount]");
      return;
    }
    if (lowerInput.equals("look")) {
      Room current = this.player.getCurrentRoom();
      System.out.println(current.getDescription(this));
      return;
    }
    if (lowerInput.equals("inventory")) {
      System.out.println("--- INVENTORY & TRACKED PORFOLIOS ---");
      System.out.printf("liquidMoney: %.2f\ngold: %.2f\ncoal: %.2f\nwheat: %.2f\nseeds: %.2f\ncattleGoods: %.2f\n", 
          this.liquidMoney, this.gold, this.coal, this.wheat, this.seeds, this.cattleGoods);
      System.out.printf("Flags - Membership: %b | Pickaxe: %b | Upgrade: %b | Store Deed: %b | Bond: %b | Loan Account: %b\n",
          this.membershipBought, this.t1PickaxeOwned, this.t2PickaxeOwned, this.localStoreOwned, this.railroadBondOwned, this.loanTaken);
      return;
    }
    if (lowerInput.startsWith("go ")) {
      String dir = rawInput.substring(3).trim();
      this.handleMovement(dir);
      return;
    }

    // Contextually Valid Local Interactivity Route Verifications
    Room currentRoom = this.player.getCurrentRoom();
    String roomName = currentRoom.getName();

    if (lowerInput.equals("plant seeds")) {
      if (!roomName.equalsIgnoreCase("Sutter's Fort")) { System.out.println("You can't do that here."); return; }
      this.handlePlantWheat();
      return;
    }
    if (lowerInput.equals("harvest")) {
      if (!roomName.equalsIgnoreCase("Sutter's Fort")) { System.out.println("You can't do that here."); return; }
      this.handleHarvestWheat();
      return;
    }
    if (lowerInput.equals("pan")) {
      if (!roomName.equalsIgnoreCase("American River")) { System.out.println("You can't do that here."); return; }
      this.handlePanForGold();
      return;
    }
    if (lowerInput.equals("mine coal")) {
      if (!roomName.equalsIgnoreCase("Sierra Mine")) { System.out.println("You can't do that here."); return; }
      this.handleMineCoal();
      return;
    }
    if (lowerInput.equals("mine gold")) {
      if (!roomName.equalsIgnoreCase("Sierra Mine")) { System.out.println("You can't do that here."); return; }
      this.handleMineGold();
      return;
    }
    if (lowerInput.equals("collect")) {
      if (!roomName.equalsIgnoreCase("Californio Rancho")) { System.out.println("You can't do that here."); return; }
      this.handleCollectCattleGoods();
      return;
    }
    if (lowerInput.equals("read")) {
      if (!roomName.equalsIgnoreCase("Daily Alta California")) { System.out.println("You can't do that here."); return; }
      this.handleReadNewspaper();
      return;
    }

    // Banking Interactivity Trees
    if (lowerInput.equals("loan")) {
      if (!roomName.equalsIgnoreCase("Wells Fargo Bank")) { System.out.println("You can't do that here."); return; }
      this.handleRequestLoan();
      return;
    }
    if (lowerInput.equals("accept loan")) {
      if (!roomName.equalsIgnoreCase("Wells Fargo Bank")) { System.out.println("You can't do that here."); return; }
      this.handleAcceptLoan();
      return;
    }
    if (lowerInput.equals("deny loan")) {
      if (!roomName.equalsIgnoreCase("Wells Fargo Bank")) { System.out.println("You can't do that here."); return; }
      this.handleDenyLoan();
      return;
    }

    // Buying Interface Trees
    if (lowerInput.startsWith("buy ")) {
      if (!roomName.equalsIgnoreCase("Brannan's Store") && !roomName.equalsIgnoreCase("SF Exchange") && !roomName.equalsIgnoreCase("Big Four Mansion")) {
        System.out.println("You can't do that here.");
        return;
      }
      String target = lowerInput.substring(4).trim();
      this.handleBuyRouter(target, roomName);
      return;
    }

    // Selling Interface Routes
    if (lowerInput.startsWith("sell ")) {
      if (!roomName.equalsIgnoreCase("Brannan's Store")) { System.out.println("You can't do that here."); return; }
      String target = lowerInput.substring(5).trim();
      this.handleSellInventory(target);
      return;
    }

    // Capital Market Systems
    if (lowerInput.startsWith("invest railroad ")) {
      if (!roomName.equalsIgnoreCase("SF Exchange")) { System.out.println("You can't do that here."); return; }
      String amtStr = lowerInput.substring(16).trim();
      this.handleInvestRailroad(amtStr);
      return;
    }

    // Baseline Command Error Fallback Handling
    System.out.println("You can't do that here. Type 'help' for a list of commands.");
  }

  private void handleMovement(String direction) {
    Room current = this.player.getCurrentRoom();
    ArrayList<String> exits = current.getExits();
    String targetRoomName = null;

    for (String exit : exits) {
      int colonIndex = exit.indexOf(":");
      String dirPart = exit.substring(0, colonIndex);
      String destPart = exit.substring(colonIndex + 1);
      if (dirPart.equalsIgnoreCase(direction)) {
        targetRoomName = destPart;
        break;
      }
    }

    if (targetRoomName == null) {
      System.out.println("You can't go that way.");
      return;
    }

    if (targetRoomName.equalsIgnoreCase("Big Four Mansion") && this.netWorth < 10000.0) {
      System.out.println("The doorman waves you off. 'Come back when you're somebody, friend.'");
      return;
    }

    Room nextRoom = this.findRoom(targetRoomName);
    if (nextRoom != null) {
      this.player.setCurrentRoom(nextRoom);
      System.out.println("Moved to " + nextRoom.getName() + ".");
      System.out.println(nextRoom.getDescription(this));
      this.executePostTurnSequence(1);
    }
  }

  private void handlePlantWheat() {
    if (this.seeds > 0 && this.plantedSeeds == 0) {
      this.plantedSeeds = this.seeds;
      this.seeds = 0;
      this.plantTimer = 0;
      System.out.println("You press the seeds into the tilled soil. Now you wait.");
      this.executePostTurnSequence(1);
    } else {
      System.out.println("Verification failed: Empty seed reserves or active crops on plot.");
    }
  }

  private void handleHarvestWheat() {
    if (this.cropsReady) {
      this.wheat += (this.plantedSeeds * 5);
      this.seeds = this.plantedSeeds;
      this.plantedSeeds = 0;
      this.cropsReady = false;
      System.out.println("You cut the wheat. The next round of seeds is in your hand.");
      this.executePostTurnSequence(1);
    } else {
      System.out.println("The crop assets are not ready for extraction.");
    }
  }

  private void handlePanForGold() {
    int discovered = this.random.nextInt(5) + 1;
    this.gold += discovered;
    System.out.println("You swirl the pan. A few specks of yellow settle at the bottom.");
    this.executePostTurnSequence(1);
  }

  private void handleMineCoal() {
    if (this.t1PickaxeOwned || this.t2PickaxeOwned) {
      int yield = this.random.nextInt(151) + 50;
      this.coal += yield;
      System.out.println("You swing the pickaxe. Coal cracks loose in chunks.");
      this.executePostTurnSequence(5);
    } else {
      System.out.println("Mining configuration validation error: Missing pickaxe equipment.");
    }
  }

  private void handleMineGold() {
    if (this.t2PickaxeOwned) {
      int yield = this.random.nextInt(61) + 20;
      this.gold += yield;
      System.out.println("Native gold gleams where the stone shatters.");
      this.executePostTurnSequence(5);
    } else {
      System.out.println("Mining configuration validation error: Missing tier steel pickaxe upgrade.");
    }
  }

  private void handleCollectCattleGoods() {
    if (this.cattleCount > 0 && this.resourcesAvailable) {
      this.cattleGoods += (this.cattleCount * 5);
      this.resourcesAvailable = false;
      this.cattleTimer = 0;
      System.out.println("The vaqueros load the goods into your wagon.");
      this.executePostTurnSequence(1);
    } else {
      System.out.println("No processed goods are ready at the ranch layout.");
    }
  }

  private void handleRequestLoan() {
    if (this.loanTaken) {
      System.out.println("The bank has already allocated your loan quota limit.");
      return;
    }
    if (!this.loanAvailable) {
      System.out.println("Wells Fargo staff are reviewing files. Please request again later.");
      return;
    }
    this.pendingLoanRate = 0.05 + (this.random.nextInt(11) * 0.01); // Random interest calculation logic mapping 5% to 15%
    this.loanOffered = true;
    System.out.printf("The teller calculates calculations. 'We can offer $1,000 at a turn rate of %.0f%% interest.' Type 'accept loan' or 'deny loan'.\n", (this.pendingLoanRate * 100));
  }

  private void handleAcceptLoan() {
    if (!this.loanOffered) {
      System.out.println("No active institutional loan offer is currently pending.");
      return;
    }
    this.liquidMoney += 1000.0;
    this.loanTaken = true;
    this.loanOffered = false;
    System.out.println("The teller counts out a thousand dollars and stamps the ledger.");
    this.executePostTurnSequence(1);
  }

  private void handleDenyLoan() {
    if (!this.loanOffered) {
      System.out.println("No active institutional loan offer is currently pending.");
      return;
    }
    this.loanAvailable = false;
    this.loanOffered = false;
    this.loanDenialTimer = 0;
    System.out.println("You refuse the credit lines. You will be able to request a new loan in 7 turns.");
    this.executePostTurnSequence(1);
  }

  private void handleBuyRouter(String target, String location) {
    if (location.equalsIgnoreCase("Brannan's Store")) {
      if (!this.membershipBought && !target.equalsIgnoreCase("membership")) {
        System.out.println("Sam Brannan stops you. 'Members only. Buy a membership token first.'");
        return;
      }
      if (target.equalsIgnoreCase("membership")) {
        if (this.liquidMoney >= 100.0 && !this.membershipBought) {
          this.liquidMoney -= 100.0; this.membershipBought = true;
          System.out.println("Brannan slides a brass token across the counter.");
          this.executePostTurnSequence(1);
        } else { System.out.println("Transaction rejected."); }
      } else if (target.equalsIgnoreCase("pickaxe")) {
        if (this.liquidMoney >= 50.0 && !this.t1PickaxeOwned) {
          this.liquidMoney -= 50.0; this.t1PickaxeOwned = true;
          System.out.println("Brannan hands you a heavy iron pickaxe.");
          this.executePostTurnSequence(1);
        } else { System.out.println("Transaction rejected."); }
      } else if (target.equalsIgnoreCase("upgrade")) {
        if (this.t1PickaxeOwned && this.liquidMoney >= 500.0 && !this.t2PickaxeOwned) {
          this.liquidMoney -= 500.0; this.t2PickaxeOwned = true;
          System.out.println("Brannan unwraps a steel-tipped beauty from oilcloth.");
          this.executePostTurnSequence(1);
        } else { System.out.println("Transaction rejected."); }
      } else if (target.equalsIgnoreCase("cattle")) {
        if (this.liquidMoney >= 500.0) {
          this.liquidMoney -= 500.0;
          if (this.cattleCount == 0) this.cattleTimer = 0;
          this.cattleCount += 1;
          System.out.println("A vaquero drives your new beast south toward the Rancho.");
          this.executePostTurnSequence(1);
        } else { System.out.println("Transaction rejected."); }
      } else if (target.equalsIgnoreCase("seeds")) {
        if (this.liquidMoney >= 1.0) {
          this.liquidMoney -= 1.0; this.seeds += 1;
          System.out.println("Brannan scoops the seeds into a burlap pouch.");
          this.executePostTurnSequence(1);
        } else { System.out.println("Transaction rejected."); }
      } else {
        System.out.println("Brannan does not stock that item.");
      }
    } else if (location.equalsIgnoreCase("SF Exchange")) {
      if (target.equalsIgnoreCase("store")) {
        if (this.liquidMoney >= 5000.0 && !this.localStoreOwned) {
          this.liquidMoney -= 5000.0; this.localStoreOwned = true;
          System.out.println("You sign the deed. A storefront on Montgomery Street is yours.");
          this.executePostTurnSequence(1);
        } else { System.out.println("Transaction components missing or entity already owned."); }
      } else {
        System.out.println("That equity item is not listed here.");
      }
    } else if (location.equalsIgnoreCase("Big Four Mansion")) {
      if (target.equalsIgnoreCase("baron")) {
        if (this.liquidMoney >= 100000.0) {
          this.liquidMoney -= 100000.0; this.railroadBaronPurchased = true;
          System.out.println("You sign the lobbyist's ledger. The chair by the window is yours. California has a new king.");
          this.executePostTurnSequence(1);
        } else { System.out.println("The board rejects your bid. You lack required liquidation funding values."); }
      } else {
        System.out.println("Status cannot be derived from that parameter structural command.");
      }
    }
  }

  private void handleSellInventory(String target) {
    if (target.equalsIgnoreCase("gold")) {
      if (this.gold >= 1.0) { this.liquidMoney += (this.gold * 0.50); this.gold = 0; System.out.println("Brannan counts coins onto the counter."); this.executePostTurnSequence(1); }
      else System.out.println("Insufficient volume matching unit parameter guidelines.");
    } else if (target.equalsIgnoreCase("coal")) {
      if (this.coal >= 1.0) { this.liquidMoney += (this.coal * 0.05); this.coal = 0; System.out.println("Brannan counts coins onto the counter."); this.executePostTurnSequence(1); }
      else System.out.println("Insufficient volume matching unit parameter guidelines.");
    } else if (target.equalsIgnoreCase("wheat")) {
      if (this.wheat >= 1.0) { this.liquidMoney += (this.wheat * 5.0); this.wheat = 0; System.out.println("Brannan counts coins onto the counter."); this.executePostTurnSequence(1); }
      else System.out.println("Insufficient volume matching unit parameter guidelines.");
    } else if (target.equalsIgnoreCase("goods")) {
      if (this.cattleGoods >= 1.0) { this.liquidMoney += (this.cattleGoods * 10.0); this.cattleGoods = 0; System.out.println("Brannan counts coins onto the counter."); this.executePostTurnSequence(1); }
      else System.out.println("Insufficient volume matching unit parameter guidelines.");
    } else {
      System.out.println("Item mapping validation error.");
    }
  }

  private void handleReadNewspaper() {
    int dayIndex = (int) this.turn;
    int yearOffset = 1849 + (dayIndex / 365);
    int derivedDay = (dayIndex % 365) + 1;
    System.out.println("Date: Day " + derivedDay + ", " + yearOffset);
    System.out.println("Headline: Corporate integration targets infrastructure as gold values stabilize!");
  }

  private void handleInvestRailroad(String amtStr) {
    try {
      double amount = Double.parseDouble(amtStr);
      if (amount > 0 && this.liquidMoney >= amount && !this.railroadBondOwned) {
        this.liquidMoney -= amount;
        this.railroadBondOwned = true;
        this.bondPrincipal = amount;
        this.bondTimer = 0;
        System.out.println("You buy a bond on the Central Pacific. The clerk seals it with red wax.");
        this.executePostTurnSequence(1);
      } else {
        System.out.println("Transaction rejected.");
      }
    } catch (NumberFormatException nfe) {
      System.out.println("Syntax structure error: Target numerical formatting parsed invalid input parameters.");
    }
  }

  private void executePostTurnSequence(int ticks) {
    for (int t = 0; t < ticks; t++) {
      this.turn += 1.0;

      // Cooldown tracking arrays handling loan mechanics
      if (!this.loanAvailable) {
        this.loanDenialTimer++;
        if (this.loanDenialTimer >= 7) {
          this.loanAvailable = true;
          this.loanDenialTimer = 0;
        }
      }

      if (this.plantedSeeds > 0 && !this.cropsReady) {
        this.plantTimer++;
        if (this.plantTimer >= 5) this.cropsReady = true;
      }

      if (this.cattleCount > 0 && !this.resourcesAvailable) {
        this.cattleTimer++;
        if (this.cattleTimer >= 10) this.resourcesAvailable = true;
      }

      if (this.localStoreOwned) {
        this.liquidMoney += (this.random.nextInt(101) + 50);
      }

      // Updated Probability-Driven Nested Yield Engine for Maturing Speculative Bonds
      if (this.railroadBondOwned) {
        this.bondTimer++;
        if (this.bondTimer >= 20) {
          double investmentPayout = 0.0;
          int probabilityRoll = this.random.nextInt(100);

          if (probabilityRoll < 30) {
            // Risk curve route logic: Earning small yield or suffering total loss adjustments (-35% up to 10%)
            double rateModifier = -0.35 + (this.random.nextInt(46) * 0.01);
            investmentPayout = this.bondPrincipal * (1.0 + rateModifier);
          } else {
            // Success curve routes checking high yields
            int nestedYieldRoll = this.random.nextInt(100);
            if (nestedYieldRoll < 80) {
              // 80% weight tier payout index: 30% to 60% standard profit scale yields
              double yieldRate = 0.30 + (this.random.nextInt(31) * 0.01);
              investmentPayout = this.bondPrincipal * (1.0 + yieldRate);
            } else {
              // 20% weight tier payout index: 80% to 150% maximum performance scale yields
              double eliteYieldRate = 0.80 + (this.random.nextInt(71) * 0.01);
              investmentPayout = this.bondPrincipal * (1.0 + eliteYieldRate);
            }
          }

          this.liquidMoney += investmentPayout;
          System.out.printf("\n[NOTIFY] Bond matured. Received returns totaling $%.2f.\n", investmentPayout);
          this.railroadBondOwned = false;
          this.bondPrincipal = 0.0;
          this.bondTimer = 0;
        }
      }

      // Revised valuation variables matching spec updates
      this.netWorth = this.liquidMoney 
          + (this.gold * 1.50) 
          + (this.coal * 0.50) 
          + (this.wheat * 5.0) 
          + (this.cattleGoods * 10.0) 
          + (this.cattleCount * 500.0) 
          + this.bondPrincipal;

      if (this.railroadBaronPurchased) {
        System.out.println("\n========================================================");
        System.out.println("EPILOGUE: You claim your seat as a California Railroad Baron!");
        System.out.println("========================================================");
        this.isRunning = false;
        break;
      }
    }
  }
}
