// --- FILE: Game.java ---
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

/**
 * Primary mechanics coordinator maintaining global operational metrics and game loops.
 *
 * <p>Coordinates execution logic, state parsing rules, input token extractions, 
 * time tick mechanics, and terminal win conditions.
 */
public class Game {
  // Global State Flags
  private boolean cropsReady = false;
  private boolean resourcesAvailable = false;
  private boolean membershipBought = false;
  private boolean t1PickaxeOwned = false;
  private boolean t2PickaxeOwned = false;
  private boolean localStoreOwned = false;
  private boolean railroadBondOwned = false;
  private boolean loanTaken = false;
  private boolean railroadBaronPurchased = false;

  // Counters (All numeric game parameters stored as doubles per specification)
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

  private int plantTimer = 0;
  private int cattleTimer = 0;
  private int bondTimer = 0;
  private double netWorth = 50.0;

  private ArrayList<Room> rooms;
  private Player player;
  private boolean isRunning = true;
  private Random random;

  /**
   * Initializing structure mappings, physical maps, and engine hooks.
   */
  public Game() {
    this.rooms = new ArrayList<Room>();
    this.player = new Player();
    this.random = new Random();
    this.initializeWorld();
  }

  /**
   * Evaluates crops state flag condition criteria.
   *
   * @return True if agricultural metrics match ripeness requirements.
   */
  public boolean isCropsReady() {
    return this.cropsReady;
  }

  /**
   * Evaluates availability of livestock items.
   *
   * @return Production status flag.
   */
  public boolean isResourcesAvailable() {
    return this.resourcesAvailable;
  }

  /**
   * Evaluates store premium access clearances.
   *
   * @return Access clearance state boolean.
   */
  public boolean isMembershipBought() {
    return this.membershipBought;
  }

  /**
   * Checks tool tier parameters.
   *
   * @return Tool state level confirmation.
   */
  public boolean isT1PickaxeOwned() {
    return this.t1PickaxeOwned;
  }

  /**
   * Checks upper-tier mining equipment flags.
   *
   * @return Tool validation flag.
   */
  public boolean isT2PickaxeOwned() {
    return this.t2PickaxeOwned;
  }

  /**
   * Returns enterprise investment data status.
   *
   * @return Business ownership status flag.
   */
  public boolean isLocalStoreOwned() {
    return this.localStoreOwned;
  }

  /**
   * Returns maturity pipeline markers tracking bonds.
   *
   * @return Active security bond holding tracker flags.
   */
  public boolean isRailroadBondOwned() {
    return this.railroadBondOwned;
  }

  /**
   * Evaluates herd metrics tracking count.
   *
   * @return Size indicators mapping ranching elements.
   */
  public double getCattleCount() {
    return this.cattleCount;
  }

  /**
   * Builds the default game world configuration, entities, and exits layout.
   */
  private void initializeWorld() {
    Room suttersFort = new Room("Sutter's Fort", "Timber walls enclose a patch of tilled soil. To the north lies the American River; east, Sacramento. The year is 1849. Your fields lie quiet.");
    Room americanRiver = new Room("American River", "The American River runs cold over dark gravel. A battered tin pan sits on a rock at the bank. Specks of yellow shimmer in the riverbed.");
    Room sierraMine = new Room("Sierra Mine", "Coal veins streak the walls; gold gleams from deeper stone. You have no tool to mine any of it.");
    Room californioRancho = new Room("Californio Rancho", "A grassy expanse with adobe outbuildings and empty corrals. A fine place to raise cattle, if you owned any.");
    Room sacramento = new Room("Sacramento", "The supply town at the confluence of two rivers. Signs point south to the store, north to the newspaper, east to the bank, and west home.");
    Room brannansStore = new Room("Brannan's Store", "Sam Brannan's General Store. He eyes you over a ledger. 'Members only, friend. You can sell to me, but you can't buy until you've paid your dues.'");
    Room dailyAlta = new Room("Daily Alta California", "A newsboy hawks copies of the Alta California. A carriage road climbs north toward Nob Hill.");
    Room wellsFargo = new Room("Wells Fargo Bank", "The offices of Wells Fargo and Co. Brass railings, heavy ledgers, strongboxes. A teller looks up. 'Need a loan?'");
    Room sfExchange = new Room("SF Exchange", "The trading floor of the SF Exchange. A chalkboard lists storefront leases and railroad bonds.");
    Room bigFourMansion = new Room("Big Four Mansion", "The marble foyer of the Big Four Mansion atop Nob Hill. A lobbyist extends a hand. The leather chair by the window is the Railroad Baron's chair.");

    // Add Fixtures exactly matching requirements
    suttersFort.addItem(new Item("Field", "A patch of tilled soil ready for planting seeds."));
    americanRiver.addItem(new Item("Pan", "A battered tin pan sitting on a rock near the freezing riverbanks."));
    sierraMine.addItem(new Item("Coal Vein", "A thick dark seam of high-grade coal stretching into the rock."));
    sierraMine.addItem(new Item("Gold Vein", "A glittering quartz seam laced with deep tracks of native gold."));
    californioRancho.addItem(new Item("Pasture", "Open fields perfectly suited for grazing cattle operations."));
    brannansStore.addItem(new Item("Counter", "A sturdy wooden service desk hosting sales logs and coin vaults."));
    dailyAlta.addItem(new Item("Newspaper", "Fresh prints covering development news along California territories."));
    wellsFargo.addItem(new Item("Teller's Window", "A secure service window managed by an analytical accounts staff."));
    sfExchange.addItem(new Item("Chalkboard", "A trading dashboard tracking available city lots and modern infrastructure titles."));
    bigFourMansion.addItem(new Item("Baron's Chair", "The highly decorated target seat signifying regional rail network dominance."));

    // Inject Structural Path Connections
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

  /**
   * Runs the core driver loop processing actions and checking win conditions.
   */
  public void start() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("--- GOLD RUSH CALIFORNIA ---");
    System.out.println("Build your fortune through farming, panning, mining, and ranching to claim your throne.");
    System.out.println("Type 'look' to survey your starting surroundings.");

    while (this.isRunning) {
      System.out.print("\n> ");
      if (!scanner.hasNextLine()) {
        break;
      }
      String input = scanner.nextLine().trim();
      if (input.length() == 0) {
        continue;
      }
      this.processCommand(input);
      this.checkEndConditions();
    }
    scanner.close();
  }

  /**
   * Loops through environmental listings manually to resolve matching room nodes.
   *
   * @param roomName Text key tracking location name.
   * @return Room node tracking location if found, or null.
   */
  private Room findRoom(String roomName) {
    // Manual loop required — built-in search methods are not allowed per AP CS A constraints
    for (Room r : this.rooms) {
      if (r.getName().equalsIgnoreCase(roomName)) {
        return r;
      }
    }
    return null;
  }

  /**
   * Interprets text strings to route them to the appropriate action processors.
   *
   * @param rawInput String submitted via the system console stream.
   */
  private void processCommand(String rawInput) {
    String lowerInput = rawInput.toLowerCase();

    if (lowerInput.equals("quit")) {
      System.out.println("Exiting the frontier. Goodbye.");
      this.isRunning = false;
      return;
    }

    if (lowerInput.equals("look")) {
      Room current = this.player.getCurrentRoom();
      System.out.println(current.getDescription(this));
      System.out.print("Visible elements here: ");
      ArrayList<Item> rItems = current.getItems();
      if (rItems.size() == 0) {
        System.out.println("None");
      } else {
        for (int i = 0; i < rItems.size(); i++) {
          System.out.print(rItems.get(i).getName() + (i == rItems.size() - 1 ? "" : ", "));
        }
        System.out.println();
      }
      return;
    }

    if (lowerInput.equals("inventory")) {
      System.out.println("--- FINANCIAL PORTFOLIO & INVENTORY ---");
      System.out.printf("Liquid Money : $%.2f\n", this.liquidMoney);
      System.out.printf("Net Worth    : $%.2f\n", this.netWorth);
      System.out.printf("Gold Ore     : %.2fg\n", this.gold);
      System.out.printf("Coal Ore     : %.2fg\n", this.coal);
      System.out.printf("Wheat Harvest: %.0f units\n", this.wheat);
      System.out.printf("Wheat Seeds  : %.0f bags\n", this.seeds);
      System.out.printf("Cattle Herd  : %.0f head\n", this.cattleCount);
      System.out.printf("Ranch Goods  : %.0f crates\n", this.cattleGoods);
      if (this.t1PickaxeOwned && !this.t2PickaxeOwned) System.out.println("Equipment    : Iron Pickaxe (Tier 1)");
      if (this.t2PickaxeOwned) System.out.println("Equipment    : Steel Pickaxe (Tier 2)");
      if (this.membershipBought) System.out.println("Permits      : Store Membership Token");
      if (this.localStoreOwned) System.out.println("Assets       : Montgomery Street Storefront Deed");
      if (this.railroadBondOwned) System.out.printf("Investments  : Central Pacific Railroad Bond ($%.2f principal)\n", this.bondPrincipal);
      return;
    }

    if (lowerInput.startsWith("go ")) {
      String dir = rawInput.substring(3).trim();
      this.handleMovement(dir);
      return;
    }

    if (lowerInput.startsWith("take ")) {
      System.out.println("You cannot collect fixtures directly. Interact using environmental verbs or system tools.");
      return;
    }

    // Custom Grammar Parsing Table
    if (lowerInput.equals("use seeds on field")) {
      this.handlePlantWheat();
      return;
    }
    if (lowerInput.equals("harvest")) {
      this.handleHarvestWheat();
      return;
    }
    if (lowerInput.equals("pan")) {
      this.handlePanForGold();
      return;
    }
    if (lowerInput.equals("use pickaxe on coal vein")) {
      this.handleMineCoal();
      return;
    }
    if (lowerInput.equals("use pickaxe on gold vein")) {
      this.handleMineGold();
      return;
    }
    if (lowerInput.equals("collect")) {
      this.handleCollectCattleGoods();
      return;
    }
    if (lowerInput.equals("buy membership")) {
      this.handleBuyMembership();
      return;
    }
    if (lowerInput.equals("buy pickaxe")) {
      this.handleBuyT1Pickaxe();
      return;
    }
    if (lowerInput.equals("buy upgrade")) {
      this.handleBuyT2Pickaxe();
      return;
    }
    if (lowerInput.equals("buy cattle")) {
      this.handleBuyCattle();
      return;
    }
    if (lowerInput.equals("buy seeds")) {
      this.handleBuySeeds();
      return;
    }
    if (lowerInput.startsWith("sell ")) {
      String target = lowerInput.substring(5).trim();
      this.handleSellInventory(target);
      return;
    }
    if (lowerInput.equals("loan")) {
      this.handleTakeLoan();
      return;
    }
    if (lowerInput.equals("read")) {
      this.handleReadNewspaper();
      return;
    }
    if (lowerInput.equals("buy store")) {
      this.handleBuyLocalStoreDeed();
      return;
    }
    if (lowerInput.startsWith("invest railroad ")) {
      String amtStr = lowerInput.substring(16).trim();
      this.handleInvestRailroad(amtStr);
      return;
    }
    if (lowerInput.equals("buy baron")) {
      this.handleBuyBaronStatus();
      return;
    }

    System.out.println("Unknown action or invalid structural syntax.");
  }

  /**
   * Evaluates path connectivity rules to update spatial coordinates.
   *
   * @param direction Targeted exit direction parameter keyword.
   */
  private void handleMovement(String direction) {
    Room current = this.player.getCurrentRoom();
    ArrayList<String> exits = current.getExits();
    String targetRoomName = null;

    // Manual split logic loop over exits array string data entries
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

    // Evaluate gate restrictions verbatim matching specific requirements
    if (targetRoomName.equalsIgnoreCase("Big Four Mansion")) {
      if (this.netWorth < 10000.0) {
        System.out.println("The doorman waves you off. 'Come back when you're somebody, friend.'");
        return;
      }
    }

    Room nextRoom = this.findRoom(targetRoomName);
    if (nextRoom != null) {
      this.player.setCurrentRoom(nextRoom);
      System.out.println("Moved to " + nextRoom.getName() + ".");
      System.out.println(nextRoom.getDescription(this));
      this.executePostTurnSequence(1);
    }
  }

  /**
   * Injects seeds onto field structures inside Sutter's Fort.
   */
  private void handlePlantWheat() {
    if (!this.player.getCurrentRoom().getName().equalsIgnoreCase("Sutter's Fort")) {
      System.out.println("You cannot plant here; return to the Sutter's Fort fields.");
      return;
    }
    if (this.seeds > 0 && this.plantedSeeds == 0) {
      this.plantedSeeds = this.seeds;
      this.seeds = 0;
      this.plantTimer = 0;
      System.out.println("You press the seeds into the tilled soil. Now you wait.");
      this.executePostTurnSequence(1);
    } else {
      System.out.println("Operation rejected: Verify seed supply or clear active fields first.");
    }
  }

  /**
   * Processes wheat fields if harvesting triggers match mature state markers.
   */
  private void handleHarvestWheat() {
    if (!this.player.getCurrentRoom().getName().equalsIgnoreCase("Sutter's Fort")) {
      System.out.println("Harvest operations are limited to Sutter's Fort plots.");
      return;
    }
    if (this.cropsReady) {
      this.wheat += (this.plantedSeeds * 5);
      this.seeds = this.plantedSeeds;
      this.plantedSeeds = 0;
      this.cropsReady = false;
      System.out.println("You cut the wheat. The next round of seeds is in your hand.");
      this.executePostTurnSequence(1);
    } else {
      System.out.println("The fields are not ready yet.");
    }
  }

  /**
   * Processes gold panning actions at the American River location.
   */
  private void handlePanForGold() {
    if (!this.player.getCurrentRoom().getName().equalsIgnoreCase("American River")) {
      System.out.println("You can only pan for gold by the banks of the American River.");
      return;
    }
    int discovered = this.random.nextInt(5) + 1;
    this.gold += discovered;
    System.out.println("You swirl the pan. A few specks of yellow settle at the bottom.");
    this.executePostTurnSequence(1);
  }

  /**
   * Extracts coal ore assets by ticking time forward 5 turns.
   */
  private void handleMineCoal() {
    if (!this.player.getCurrentRoom().getName().equalsIgnoreCase("Sierra Mine")) {
      System.out.println("Mining operations require subterranean extraction features like a Mine.");
      return;
    }
    if (this.t1PickaxeOwned || this.t2PickaxeOwned) {
      int yield = this.random.nextInt(151) + 50;
      this.coal += yield;
      System.out.println("You swing the pickaxe. Coal cracks loose in chunks.");
      this.executePostTurnSequence(5);
    } else {
      System.out.println("You have no tool to mine any of it.");
    }
  }

  /**
   * Extracts premium gold fields via steel equipment tool sets.
   */
  private void handleMineGold() {
    if (!this.player.getCurrentRoom().getName().equalsIgnoreCase("Sierra Mine")) {
      System.out.println("Mining operations require subterranean extraction features like a Mine.");
      return;
    }
    if (this.t2PickaxeOwned) {
      int yield = this.random.nextInt(61) + 20;
      this.gold += yield;
      System.out.println("Native gold gleams where the stone shatters.");
      this.executePostTurnSequence(5);
    } else {
      System.out.println("The gold veins are still too hard to crack.");
    }
  }

  /**
   * Collects ranching outputs inside the Rancho range fields.
   */
  private void handleCollectCattleGoods() {
    if (!this.player.getCurrentRoom().getName().equalsIgnoreCase("Californio Rancho")) {
      System.out.println("Ranch collections are limited to the Californio Rancho.");
      return;
    }
    if (this.cattleCount > 0 && this.resourcesAvailable) {
      this.cattleGoods += (this.cattleCount * 5);
      this.resourcesAvailable = false;
      this.cattleTimer = 0;
      System.out.println("The vaqueros load the goods into your wagon.");
      this.executePostTurnSequence(1);
    } else {
      System.out.println("No ranch goods are currently ready for freight collection.");
    }
  }

  /**
   * Purchases storefront clearances at Brannan's general store desk.
   */
  private void handleBuyMembership() {
    if (!this.player.getCurrentRoom().getName().equalsIgnoreCase("Brannan's Store")) {
      System.out.println("Commerce requires you to be at Brannan's Store.");
      return;
    }
    if (this.liquidMoney >= 100.0 && !this.membershipBought) {
      this.liquidMoney -= 100.0;
      this.membershipBought = true;
      System.out.println("Brannan slides a brass token across the counter.");
      this.executePostTurnSequence(1);
    } else {
      System.out.println("Transaction rejected: Insufficient funds or membership already exists.");
    }
  }

  /**
   * Standard equipment tier 1 purchase sequence logs.
   */
  private void handleBuyT1Pickaxe() {
    if (!this.player.getCurrentRoom().getName().equalsIgnoreCase("Brannan's Store")) {
      System.out.println("Commerce requires you to be at Brannan's Store.");
      return;
    }
    if (this.membershipBought && this.liquidMoney >= 50.0 && !this.t1PickaxeOwned) {
      this.liquidMoney -= 50.0;
      this.t1PickaxeOwned = true;
      System.out.println("Brannan hands you a heavy iron pickaxe.");
      this.executePostTurnSequence(1);
    } else {
      System.out.println("Transaction rejected: Verify membership level, cash balances, or item possession.");
    }
  }

  /**
   * Upgrades mining gear to steel status thresholds.
   */
  private void handleBuyT2Pickaxe() {
    if (!this.player.getCurrentRoom().getName().equalsIgnoreCase("Brannan's Store")) {
      System.out.println("Commerce requires you to be at Brannan's Store.");
      return;
    }
    if (this.membershipBought && this.t1PickaxeOwned && this.liquidMoney >= 500.0 && !this.t2PickaxeOwned) {
      this.liquidMoney -= 500.0;
      this.t2PickaxeOwned = true;
      System.out.println("Brannan unwraps a steel-tipped beauty from oilcloth.");
      this.executePostTurnSequence(1);
    } else {
      System.out.println("Transaction rejected: Check upgrade credentials or asset liquidity.");
    }
  }

  /**
   * Increases cattle herd populations.
   */
  private void handleBuyCattle() {
    if (!this.player.getCurrentRoom().getName().equalsIgnoreCase("Brannan's Store")) {
      System.out.println("Commerce requires you to be at Brannan's Store.");
      return;
    }
    if (this.membershipBought && this.liquidMoney >= 500.0) {
      this.liquidMoney -= 500.0;
      if (this.cattleCount == 0) {
        this.cattleTimer = 0;
      }
      this.cattleCount += 1;
      System.out.println("A vaquero drives your new beast south toward the Rancho.");
      this.executePostTurnSequence(1);
    } else {
      System.out.println("Transaction rejected: Premium authorization required or asset tokens short.");
    }
  }

  /**
   * Adds agricultural seed sacks to player metrics.
   */
  private void handleBuySeeds() {
    if (!this.player.getCurrentRoom().getName().equalsIgnoreCase("Brannan's Store")) {
      System.out.println("Commerce requires you to be at Brannan's Store.");
      return;
    }
    if (this.membershipBought && this.liquidMoney >= 1.0) {
      this.liquidMoney -= 1.0;
      this.seeds += 1;
      System.out.println("Brannan scoops the seeds into a burlap pouch.");
      this.executePostTurnSequence(1);
    } else {
      System.out.println("Transaction rejected: Check funds or membership status.");
    }
  }

  /**
   * Liquidates inventory resources for raw money value matching market prices.
   *
   * @param item Target asset category selection index string.
   */
  private void handleSellInventory(String item) {
    if (!this.player.getCurrentRoom().getName().equalsIgnoreCase("Brannan's Store")) {
      System.out.println("Commerce requires you to be at Brannan's Store.");
      return;
    }
    if (item.equalsIgnoreCase("gold")) {
      if (this.gold > 0) {
        this.liquidMoney += (this.gold * 0.50);
        this.gold = 0;
        System.out.println("Brannan counts coins onto the counter.");
        this.executePostTurnSequence(1);
      } else {
        System.out.println("You don't have any gold to sell.");
      }
    } else if (item.equalsIgnoreCase("coal")) {
      if (this.coal > 0) {
        this.liquidMoney += (this.coal * 0.05);
        this.coal = 0;
        System.out.println("Brannan counts coins onto the counter.");
        this.executePostTurnSequence(1);
      } else {
        System.out.println("You don't have any coal to sell.");
      }
    } else if (item.equalsIgnoreCase("wheat")) {
      if (this.wheat > 0) {
        this.liquidMoney += (this.wheat * 5.0);
        this.wheat = 0;
        System.out.println("Brannan counts coins onto the counter.");
        this.executePostTurnSequence(1);
      } else {
        System.out.println("You don't have any wheat to sell.");
      }
    } else if (item.equalsIgnoreCase("goods")) {
      if (this.cattleGoods > 0) {
        this.liquidMoney += (this.cattleGoods * 10.0);
        this.cattleGoods = 0;
        System.out.println("Brannan counts coins onto the counter.");
        this.executePostTurnSequence(1);
      } else {
        System.out.println("You don't have any ranch goods to sell.");
      }
    } else {
      System.out.println("Brannan does not buy that item. Choose from gold, coal, wheat, or goods.");
    }
  }

  /**
   * Draws a unique banking credit allocation asset item.
   */
  private void handleTakeLoan() {
    if (!this.player.getCurrentRoom().getName().equalsIgnoreCase("Wells Fargo Bank")) {
      System.out.println("Financial processing requires you to be at Wells Fargo Bank.");
      return;
    }
    if (!this.loanTaken) {
      this.liquidMoney += 1000.0;
      this.loanTaken = true;
      System.out.println("The teller counts out a thousand dollars and stamps the ledger.");
      this.executePostTurnSequence(1);
    } else {
      System.out.println("The bank rejected your application. One-time limit exceeded.");
    }
  }

  /**
   * Formats current time into structural narrative entries without consuming turns.
   */
  private void handleReadNewspaper() {
    if (!this.player.getCurrentRoom().getName().equalsIgnoreCase("Daily Alta California")) {
      System.out.println("Newspaper distribution points are centered at the Daily Alta office.");
      return;
    }
    int dayOffset = (int) this.turn;
    int year = 1849 + (dayOffset / 365);
    int currentDay = (dayOffset % 365) + 1;
    System.out.println("Date: Day " + currentDay + ", " + year);
    System.out.println("Headline: Frontier development scales up as local net values surge past limits!");
  }

  /**
   * Purchases municipal business assets at the Exchange floors.
   */
  private void handleBuyLocalStoreDeed() {
    if (!this.player.getCurrentRoom().getName().equalsIgnoreCase("SF Exchange")) {
      System.out.println("Equity purchases are limited to the trading floor of the SF Exchange.");
      return;
    }
    if (this.liquidMoney >= 5000.0 && !this.localStoreOwned) {
      this.liquidMoney -= 5000.0;
      this.localStoreOwned = true;
      System.out.println("You sign the deed. A storefront on Montgomery Street is yours.");
      this.executePostTurnSequence(1);
    } else {
      System.out.println("Transaction rejected: Insufficient funds or asset already registered.");
    }
  }

  /**
   * Provisions security bond investments matching input parameters.
   *
   * @param amtStr Numerical amount argument string parsed from command input.
   */
  private void handleInvestRailroad(String amtStr) {
    if (!this.player.getCurrentRoom().getName().equalsIgnoreCase("SF Exchange")) {
      System.out.println("Equity purchases are limited to the trading floor of the SF Exchange.");
      return;
    }
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
        System.out.println("Transaction rejected: Check capital availability or active investments.");
      }
    } catch (NumberFormatException nfe) {
      System.out.println("Syntax failed: Specify a valid numeric format value.");
    }
  }

  /**
   * Triggers terminal game conditions if money limits pass requirements.
   */
  private void handleBuyBaronStatus() {
    if (!this.player.getCurrentRoom().getName().equalsIgnoreCase("Big Four Mansion")) {
      System.out.println("High executive assignments are restricted to the Big Four Mansion boardrooms.");
      return;
    }
    if (this.liquidMoney >= 100000.0) {
      this.liquidMoney -= 100000.0;
      this.railroadBaronPurchased = true;
      System.out.println("You sign the lobbyist's ledger. The chair by the window is yours. California has a new king.");
      this.executePostTurnSequence(1);
    } else {
      System.out.println("The board rejects your bid. You need $100,000 in liquid capital to secure the chair.");
    }
  }

  /**
   * Steps automated framework timelines forward processing recurring event systems.
   *
   * @param ticks Amount of turn steps tracking iteration updates.
   */
  private void executePostTurnSequence(int ticks) {
    for (int t = 0; t < ticks; t++) {
      this.turn += 1.0;

      // Crop ripening loop check
      if (this.plantedSeeds > 0 && !this.cropsReady) {
        this.plantTimer++;
        if (this.plantTimer >= 5) {
          this.cropsReady = true;
        }
      }

      // Cattle operations timer verification loops
      if (this.cattleCount > 0 && !this.resourcesAvailable) {
        this.cattleTimer++;
        if (this.cattleTimer >= 10) {
          this.resourcesAvailable = true;
        }
      }

      // Passive income generation formulas from owned commercial storefront assets
      if (this.localStoreOwned) {
        int profit = this.random.nextInt(101) + 50; // Random tier range between 50 and 150 dollars
        this.liquidMoney += profit;
      }

      // Security investments maturation calculations
      if (this.railroadBondOwned) {
        this.bondTimer++;
        if (this.bondTimer >= 20) {
          double payout = 2.0 * this.bondPrincipal;
          this.liquidMoney += payout;
          this.railroadBondOwned = false;
          this.bondPrincipal = 0.0;
          this.bondTimer = 0;
          System.out.printf("\n[NOTIFY] Your Central Pacific Railroad Bond has matured! Received a payout of $%.2f.\n", payout);
        }
      }

      // Recalculate net worth based on specified valuation ratios
      this.netWorth = this.liquidMoney 
          + (this.gold * 0.50) 
          + (this.coal * 0.05) 
          + (this.wheat * 5.0) 
          + (this.cattleGoods * 10.0) 
          + (this.cattleCount * 500.0) 
          + this.bondPrincipal;
    }
  }

  /**
   * Terminates loop runtime processes when win metrics pass goals.
   */
  private void checkEndConditions() {
    if (this.railroadBaronPurchased) {
      System.out.println("\n========================================================");
      System.out.println("EPILOGUE: You sit in the leather chair overlooking San Francisco.");
      System.out.println("From a penniless prospector to a railroad mogul, your victory is absolute.");
      System.out.println("========================================================");
      this.isRunning = false;
    }
  }
}


