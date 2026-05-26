// --- FILE: Game.java ---
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The main controller class for the Gold Rush California adventure.
 *
 * Manages the global game state, the player, room initialization, user input parsing,
 * turn mechanics, and all specific interaction logic defined in the spec.
 */
public class Game {
  private ArrayList<Room> rooms;
  private Player player;
  private boolean isRunning;

  // Global Game State Flags and Counters
  private boolean cropsReady = false;
  private boolean resourcesAvailable = false;
  private boolean loanAvailable = true;

  private int turnCount = 0;
  private int plantTimer = 0;
  private int cattleTimer = 0;
  private int bondTimer = 0;
  private int debtTimer = 0;
  
  // Internal tracking for loan offers
  private boolean pendingLoan = false;
  private int loanCooldownTimer = 0;
  private double pendingLoanRate = 0;
  private double currentLoanBalance = 0;

  private double currentBondPayout = 0;

  /**
   * Constructs a new Game, initializing lists and building the world.
   */
  public Game() {
    this.rooms = new ArrayList<Room>();
    this.player = new Player();
    this.isRunning = true;
    setupWorld();
  }

  /**
   * Initializes all rooms, items, and exits as per the spec.
   */
  private void setupWorld() {
    Room suttersFort = new Room("Sutter's Fort", "");
    Room americanRiver = new Room("American River", "The American River runs cold over dark gravel. A battered tin pan sits on a rock at the bank. Specks of yellow shimmer in the riverbed — you could pan here.");
    Room sierraMine = new Room("Sierra Mine", "");
    Room rancho = new Room("Californio Rancho", "");
    Room sacramento = new Room("Sacramento", "The supply town at the confluence of two rivers. Signs point south to the store, north to the newspaper, east to the bank, and west home.");
    Room brannansStore = new Room("Brannan's Store", "");
    Room dailyAlta = new Room("Daily Alta", "A newsboy hawks copies of the Alta California — you could read one for the date and the headline. A carriage road climbs north toward Nob Hill.");
    Room bank = new Room("Wells Fargo Bank", "The offices of Wells Fargo and Co. Brass railings, heavy ledgers, strongboxes. A teller looks up. 'Need a loan? Just say the word.'");
    Room exchange = new Room("San Francisco Exchange", "");
    Room bigFour = new Room("Big Four Mansion", "The marble foyer of the Big Four Mansion atop Nob Hill. A lobbyist extends a hand. The leather chair by the window is the Railroad Baron's — for $10,000, you could buy baron status and secure your fortune.");

    // Exits for Sutter's Fort
    suttersFort.addExit("North", "American River");
    suttersFort.addExit("East", "Sacramento");
    suttersFort.addExit("South", "Californio Rancho");
    suttersFort.addExit("West", "Sierra Mine");

    // Exits for American River
    americanRiver.addExit("South", "Sutter's Fort");

    // Exits for Sierra Mine
    sierraMine.addExit("East", "Sutter's Fort");

    // Exits for Rancho
    rancho.addExit("North", "Sutter's Fort");

    // Exits for Sacramento
    sacramento.addExit("North", "Daily Alta");
    sacramento.addExit("East", "Wells Fargo Bank");
    sacramento.addExit("South", "Brannan's Store");
    sacramento.addExit("West", "Sutter's Fort");

    // Exits for Brannan's Store
    brannansStore.addExit("North", "Sacramento");

    // Exits for Daily Alta (Condition mapped in processCommand)
    dailyAlta.addExit("South", "Sacramento");
    dailyAlta.addExit("North", "Big Four Mansion");

    // Exits for Wells Fargo Bank
    bank.addExit("West", "Sacramento");
    bank.addExit("East", "San Francisco Exchange");

    // Exits for SF Exchange
    exchange.addExit("West", "Wells Fargo Bank");

    // Exits for Big Four Mansion
    bigFour.addExit("South", "Daily Alta");

    // Adding Items to Rooms per spec tables and definitions
    suttersFort.addItem(new Item("Field", "target of plant seeds"));
    suttersFort.addItem(new Wheat());
    
    americanRiver.addItem(new Item("Pan", "fixture"));
    americanRiver.addItem(new Gold());
    
    sierraMine.addItem(new Item("Coal Vein", "target of mine coal"));
    sierraMine.addItem(new Item("Gold Vein", "target of mine gold"));
    sierraMine.addItem(new Coal());
    sierraMine.addItem(new Gold());
    
    rancho.addItem(new Item("Pasture", "fixture"));
    
    brannansStore.addItem(new Item("Counter", "site of buy and sell actions"));
    brannansStore.addItem(new Cattle());
    brannansStore.addItem(new Item("Wheat Seeds", "counter"));
    
    dailyAlta.addItem(new Item("Newspaper", "target of read"));
    
    bank.addItem(new Item("Teller's Window", "site of loan"));
    
    exchange.addItem(new Item("Chalkboard", "site of buy store and invest railroad"));
    
    bigFour.addItem(new Item("Baron's Chair", "target of buy baron"));

    this.rooms.add(suttersFort);
    this.rooms.add(americanRiver);
    this.rooms.add(sierraMine);
    this.rooms.add(rancho);
    this.rooms.add(sacramento);
    this.rooms.add(brannansStore);
    this.rooms.add(dailyAlta);
    this.rooms.add(bank);
    this.rooms.add(exchange);
    this.rooms.add(bigFour);

    this.player.setCurrentRoom(suttersFort);
  }

  /**
   * Starts the main game loop, reading and processing user commands.
   */
  public void start() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Welcome to Gold Rush California.");
    System.out.println(this.player.getCurrentRoom().getDescription(this, this.player));

    while (this.isRunning) {
      System.out.print("\n> ");
      String input = scanner.nextLine().trim();
      if (input.length() > 0) {
        processCommand(input);
      }
    }
    scanner.close();
  }

  /**
   * Parses the user's input and triggers the corresponding action.
   *
   * @param input the raw string input from the user
   */
  private void processCommand(String input) {
    String lowerInput = input.toLowerCase();
    String currentRoomName = this.player.getCurrentRoom().getName();
    boolean commandSuccessful = false;
    int turnsToAdvance = 1;

    if (lowerInput.equals("help")) {
      System.out.println("Commands: go [direction], look, inventory, mine coal, mine gold, plant seeds, pan, harvest, collect, read, loan, accept loan, deny loan, pay loan, buy [thing], sell [item], invest railroad [amount], w, a, s, d, quit.");
      return; // Does not cost a turn
    } else if (lowerInput.equals("quit")) {
      this.isRunning = false;
      return;
    } else if (lowerInput.equals("look")) {
      System.out.println(this.player.getCurrentRoom().getDescription(this, this.player));
      System.out.print("Items here: ");
      ArrayList<Item> items = this.player.getCurrentRoom().getItems();
      if (items.size() == 0) {
        System.out.println("None");
      } else {
        // Manual loop required — built-in search methods are not allowed per AP CS A constraints
        for (int i = 0; i < items.size(); i++) {
          System.out.print(items.get(i).getName() + (i == items.size() - 1 ? "" : ", "));
        }
        System.out.println();
      }
      return; // Does not cost a turn
    } else if (lowerInput.equals("inventory")) {
      System.out.println("Counters: Money=$" + player.getLiquidMoney() + ", Gold=" + player.getGold() +
                         ", Coal=" + player.getCoal() + ", Wheat=" + player.getWheat() + 
                         ", Seeds=" + player.getSeeds() + ", CattleGoods=" + player.getCattleGoods());
      System.out.println("Flags: Membership=" + player.isMembershipBought() + ", T1Pickaxe=" + player.isT1PickaxeOwned() +
                         ", T2Pickaxe=" + player.isT2PickaxeOwned() + ", Store=" + player.isLocalStoreOwned() +
                         ", Bond=" + player.isRailroadBondOwned() + ", Loan=" + player.isLoanTaken());
      return; // Does not cost a turn
    } else if (lowerInput.startsWith("go ")) {
      String direction = lowerInput.substring(3).trim();
      commandSuccessful = attemptMove(direction);
    } else if (lowerInput.equals("w")) {
      commandSuccessful = attemptMove("north");
    } else if (lowerInput.equals("s")) {
      commandSuccessful = attemptMove("south");
    } else if (lowerInput.equals("d")) {
      commandSuccessful = attemptMove("east");
    } else if (lowerInput.equals("a")) {
      commandSuccessful = attemptMove("west");
    } else if (lowerInput.equals("plant seeds")) {
      if (currentRoomName.equalsIgnoreCase("Sutter's Fort")) {
        if (player.getSeeds() > 0 && player.getPlantedSeeds() == 0) {
          player.setPlantedSeeds(player.getSeeds());
          player.setSeeds(0);
          this.plantTimer = 0;
          System.out.println("You press the seeds into the tilled soil. Now you wait.");
          commandSuccessful = true;
        } else {
          System.out.println("You cannot plant seeds right now.");
        }
      } else {
        System.out.println("You can't do that here.");
      }
    } else if (lowerInput.equals("harvest")) {
      if (currentRoomName.equalsIgnoreCase("Sutter's Fort")) {
        if (this.cropsReady) {
          player.setWheat(player.getWheat() + (player.getPlantedSeeds() * 5));
          player.setSeeds(player.getPlantedSeeds());
          player.setPlantedSeeds(0);
          this.cropsReady = false;
          System.out.println("You cut the wheat. The next round of seeds is in your hand.");
          commandSuccessful = true;
        } else {
          System.out.println("The crops are not ready to harvest.");
        }
      } else {
        System.out.println("You can't do that here.");
      }
    } else if (lowerInput.equals("pan")) {
      if (currentRoomName.equalsIgnoreCase("American River")) {
        int found = (int)(Math.random() * 5) + 1;
        player.setGold(player.getGold() + found);
        System.out.println("You swirl the pan. A few specks of yellow settle at the bottom.");
        commandSuccessful = true;
      } else {
        System.out.println("You can't do that here.");
      }
    } else if (lowerInput.equals("mine coal")) {
      if (currentRoomName.equalsIgnoreCase("Sierra Mine")) {
        if (player.isT1PickaxeOwned() || player.isT2PickaxeOwned()) {
          int found = (int)(Math.random() * 151) + 50;
          player.setCoal(player.getCoal() + found);
          System.out.println("You swing the pickaxe. Coal cracks loose in chunks.");
          commandSuccessful = true;
          turnsToAdvance = 5;
        } else {
          System.out.println("You lack the proper tool.");
        }
      } else {
        System.out.println("You can't do that here.");
      }
    } else if (lowerInput.equals("mine gold")) {
      if (currentRoomName.equalsIgnoreCase("Sierra Mine")) {
        if (player.isT2PickaxeOwned()) {
          int found = (int)(Math.random() * 61) + 20;
          player.setGold(player.getGold() + found);
          System.out.println("Native gold gleams where the stone shatters.");
          commandSuccessful = true;
          turnsToAdvance = 5;
        } else {
          System.out.println("You lack the proper tool.");
        }
      } else {
        System.out.println("You can't do that here.");
      }
    } else if (lowerInput.equals("collect")) {
      if (currentRoomName.equalsIgnoreCase("Californio Rancho")) {
        if (player.getCattleCount() > 0 && this.resourcesAvailable) {
          player.setCattleGoods(player.getCattleGoods() + (player.getCattleCount() * 5));
          this.resourcesAvailable = false;
          this.cattleTimer = 0;
          System.out.println("The vaqueros load the goods into your wagon.");
          commandSuccessful = true;
        } else {
          System.out.println("There is nothing to collect.");
        }
      } else {
        System.out.println("You can't do that here.");
      }
    } else if (lowerInput.equals("read")) {
      if (currentRoomName.equalsIgnoreCase("Daily Alta")) {
        // Date formatting based on turn 0 = Jan 1, 1849
        int days = this.turnCount;
        int currentMonth = 1; // 1-12
        int currentDay = 1;
        // Simple manual date loop
        for (int i = 0; i < days; i++) {
          currentDay++;
          if (currentDay > 30) {
            currentDay = 1;
            currentMonth++;
            if (currentMonth > 12) {
              currentMonth = 1;
            }
          }
        }
        String headline = "Gold Fever Sweeps the West!";
        System.out.println("Date: Month " + currentMonth + ", Day " + currentDay + ", 1849. Headline: " + headline);
        return; // Does not advance turn
      } else {
        System.out.println("You can't do that here.");
      }
    } else if (lowerInput.equals("loan")) {
      if (currentRoomName.equalsIgnoreCase("Wells Fargo Bank")) {
        if (!player.isLoanTaken()) {
          player.setLiquidMoney(player.getLiquidMoney() + 1000);
          player.setLoanTaken(true);
          this.currentLoanBalance = 1000;
          System.out.println("The teller counts out a thousand dollars and stamps the ledger.");
          commandSuccessful = true;
        } else {
          System.out.println("You already have an outstanding loan.");
        }
      } else {
        System.out.println("You can't do that here.");
      }
    } else if (lowerInput.equals("pay loan")) {
      if (currentRoomName.equalsIgnoreCase("Wells Fargo Bank")) {
        if (player.isLoanTaken()) {
          player.setLiquidMoney(player.getLiquidMoney() - this.currentLoanBalance);
          player.setLoanTaken(false);
          this.currentLoanBalance = 0;
          System.out.println("You hand the teller your dues and he thanks you for your business.");
          commandSuccessful = true;
        } else {
          System.out.println("You have no loan to pay.");
        }
      } else {
        System.out.println("You can't do that here.");
      }
    } else if (lowerInput.startsWith("buy ")) {
      if (currentRoomName.equalsIgnoreCase("Brannan's Store")) {
        String thing = lowerInput.substring(4).trim();
        if (thing.equals("membership")) {
          if (player.getLiquidMoney() >= 100 && !player.isMembershipBought()) {
            player.setLiquidMoney(player.getLiquidMoney() - 100);
            player.setMembershipBought(true);
            System.out.println("Brannan slides a brass token across the counter.");
            commandSuccessful = true;
          }
        } else if (thing.equals("pickaxe")) {
          if (player.isMembershipBought() && player.getLiquidMoney() >= 50 && !player.isT1PickaxeOwned()) {
            player.setLiquidMoney(player.getLiquidMoney() - 50);
            player.setT1PickaxeOwned(true);
            System.out.println("Brannan hands you a heavy iron pickaxe.");
            commandSuccessful = true;
          }
        } else if (thing.equals("upgrade")) {
          if (player.isMembershipBought() && player.isT1PickaxeOwned() && player.getLiquidMoney() >= 500 && !player.isT2PickaxeOwned()) {
            player.setLiquidMoney(player.getLiquidMoney() - 500);
            player.setT2PickaxeOwned(true);
            System.out.println("Brannan unwraps a steel-tipped beauty from oilcloth.");
            commandSuccessful = true;
          }
        } else if (thing.equals("cattle")) {
          if (player.isMembershipBought() && player.getLiquidMoney() >= 500) {
            player.setLiquidMoney(player.getLiquidMoney() - 500);
            if (player.getCattleCount() == 0) {
              this.cattleTimer = 0;
            }
            player.setCattleCount(player.getCattleCount() + 1);
            System.out.println("A vaquero drives your new beast south toward the Rancho.");
            commandSuccessful = true;
          }
        } else if (thing.equals("seeds")) {
          if (player.isMembershipBought() && player.getLiquidMoney() >= 1) {
            player.setLiquidMoney(player.getLiquidMoney() - 1);
            player.setSeeds(player.getSeeds() + 1);
            System.out.println("Brannan scoops the seeds into a burlap pouch.");
            commandSuccessful = true;
          }
        } else {
          System.out.println("Invalid or unaffordable purchase.");
        }
      } else if (currentRoomName.equalsIgnoreCase("San Francisco Exchange")) {
        String thing = lowerInput.substring(4).trim();
        if (thing.equals("store")) {
          if (player.getLiquidMoney() >= 5000 && !player.isLocalStoreOwned()) {
            player.setLiquidMoney(player.getLiquidMoney() - 5000);
            player.setLocalStoreOwned(true);
            System.out.println("You sign the deed. A storefront on Montgomery Street is yours.");
            commandSuccessful = true;
          }
        }
      } else if (currentRoomName.equalsIgnoreCase("Big Four Mansion")) {
        String thing = lowerInput.substring(4).trim();
        if (thing.equals("baron")) {
          if (player.getLiquidMoney() >= 10000) {
            player.setLiquidMoney(player.getLiquidMoney() - 10000);
            player.setRailroadBaronPurchased(true);
            System.out.println("You sign the lobbyist's ledger. The chair by the window is yours. California has a new king.");
            commandSuccessful = true;
          }
        }
      } else {
        System.out.println("You can't do that here.");
      }
    } else if (lowerInput.startsWith("sell ")) {
      if (currentRoomName.equalsIgnoreCase("Brannan's Store") && player.isMembershipBought()) {
        String item = lowerInput.substring(5).trim();
        if (item.equals("gold") && player.getGold() > 0) {
          player.setLiquidMoney(player.getLiquidMoney() + (player.getGold() * 2.50));
          player.setGold(0);
          System.out.println("Brannan counts coins onto the counter.");
          commandSuccessful = true;
        } else if (item.equals("coal") && player.getCoal() > 0) {
          player.setLiquidMoney(player.getLiquidMoney() + (player.getCoal() * 0.50));
          player.setCoal(0);
          System.out.println("Brannan counts coins onto the counter.");
          commandSuccessful = true;
        } else if (item.equals("wheat") && player.getWheat() > 0) {
          player.setLiquidMoney(player.getLiquidMoney() + (player.getWheat() * 5));
          player.setWheat(0);
          System.out.println("Brannan counts coins onto the counter.");
          commandSuccessful = true;
        } else if (item.equals("goods") && player.getCattleGoods() > 0) {
          player.setLiquidMoney(player.getLiquidMoney() + (player.getCattleGoods() * 10));
          player.setCattleGoods(0);
          System.out.println("Brannan counts coins onto the counter.");
          commandSuccessful = true;
        } else {
          System.out.println("You have none of that to sell.");
        }
      } else {
        System.out.println("You can't do that here.");
      }
    } else if (lowerInput.startsWith("invest railroad ")) {
      if (currentRoomName.equalsIgnoreCase("San Francisco Exchange")) {
        try {
          double amount = Double.parseDouble(lowerInput.substring(16).trim());
          if (amount > 0 && player.getLiquidMoney() >= amount && !player.isRailroadBondOwned()) {
            player.setLiquidMoney(player.getLiquidMoney() - amount);
            player.setRailroadBondOwned(true);
            player.setBondPrincipal(amount);
            this.bondTimer = 0;
            
            // Calculate eventual payout based on spec rules
            double roll1 = Math.random();
            if (roll1 < 0.30) {
              double multiplier = 1.10 + (Math.random() * 0.25);
              this.currentBondPayout = amount * multiplier;
            } else {
              double roll2 = Math.random();
              if (roll2 < 0.50) {
                this.currentBondPayout = amount * 1.75;
              } else {
                this.currentBondPayout = amount * 2.35;
              }
            }
            System.out.println("You buy a bond on the Central Pacific. The clerk seals it in an envelope.");
            commandSuccessful = true;
          } else {
            System.out.println("Invalid investment amount or you already own a bond.");
          }
        } catch (NumberFormatException e) {
          System.out.println("Invalid amount format.");
        }
      } else {
        System.out.println("You can't do that here.");
      }
    } else {
      System.out.println("You can't do that here. Type 'help' for a list of commands.");
    }

    if (commandSuccessful) {
      // Loop processTurn based on how many turns the action cost
      for (int i = 0; i < turnsToAdvance; i++) {
        this.turnCount++;
        processTurn();
        if (!this.isRunning) break; // Break if win/loss condition triggered
      }
    }
  }

  /**
   * Attempts to move the player in the specified direction.
   *
   * @param direction the direction to move
   * @return true if successful, false otherwise
   */
  private boolean attemptMove(String direction) {
    ArrayList<String> exits = this.player.getCurrentRoom().getExits();
    // Manual loop required — built-in search methods are not allowed per AP CS A constraints
    for (int i = 0; i < exits.size(); i++) {
      String exit = exits.get(i);
      String[] parts = exit.split(":");
      if (parts[0].equalsIgnoreCase(direction)) {
        String destName = parts[1];
        
        // Handle Big Four Mansion gate
        if (destName.equalsIgnoreCase("Big Four Mansion") && player.getNetWorth() < 10000) {
          System.out.println("The doorman waves you off. 'Come back when you're somebody, friend.'");
          return false;
        }
        
        Room destRoom = findRoom(destName);
        if (destRoom != null) {
          this.player.setCurrentRoom(destRoom);
          System.out.println(destRoom.getDescription(this, this.player));
          return true;
        }
      }
    }
    System.out.println("You can't go that way.");
    return false;
  }

  /**
   * Finds a room by its name in the global room list.
   *
   * @param name the name of the room
   * @return the Room object if found, null otherwise
   */
  private Room findRoom(String name) {
    // Manual loop required — built-in search methods are not allowed per AP CS A constraints
    for (Room r : this.rooms) {
      if (r.getName().equalsIgnoreCase(name)) {
        return r;
      }
    }
    return null;
  }

  /**
   * Processes all per-turn logic defined in the spec.
   * Handles timer increments, resource generation, interest, and win/loss conditions.
   */
  private void processTurn() {
    // 2. Crop timer
    if (player.getPlantedSeeds() > 0 && !this.cropsReady) {
      this.plantTimer++;
      if (this.plantTimer >= 5) {
        this.cropsReady = true;
      }
    }

    // 3. Cattle timer
    if (player.getCattleCount() > 0 && !this.resourcesAvailable) {
      this.cattleTimer++;
      if (this.cattleTimer >= 10) {
        this.resourcesAvailable = true;
      }
    }

    // 4. Local store passive income
    if (player.isLocalStoreOwned()) {
      int passiveIncome = (int)(Math.random() * 101) + 50;
      player.setLiquidMoney(player.getLiquidMoney() + passiveIncome);
    }

    // 5. Railroad bond timer
    if (player.isRailroadBondOwned()) {
      this.bondTimer++;
      if (this.bondTimer >= 20) {
        // Spec has conflicting text regarding payout. Interaction says random 1.75x/2.35x. 
        // Sequence text says add 2 * bondPrincipal. We use sequence rule for standard payout format.
        player.setLiquidMoney(player.getLiquidMoney() + (2 * player.getBondPrincipal()));
        player.setRailroadBondOwned(false);
        player.setBondPrincipal(0);
        this.bondTimer = 0;
      }
    }
    
    // Loan interest calculation every 10 turns
    if (player.isLoanTaken()) {
      if (this.turnCount % 10 == 0) {
        double interestRate = 0.10 + (Math.random() * 0.10); // 10% to 20%
        this.currentLoanBalance += (this.currentLoanBalance * interestRate);
      }
    }
    
    // Loan cooldown
    if (!this.loanAvailable) {
      this.loanCooldownTimer--;
      if (this.loanCooldownTimer <= 0) {
        this.loanAvailable = true;
      }
    }

    // 6. Recompute netWorth (handled natively via getter, but we can call it to check)
    double netWorth = player.getNetWorth();

    // 7. Win Condition
    if (player.isRailroadBaronPurchased()) {
      // The epilogue was printed on purchase, end game.
      this.isRunning = false;
      return;
    }

    // 8. Lose Condition (Debt)
    if (player.getLiquidMoney() < 0) {
      this.debtTimer++;
      if (this.debtTimer >= 30) {
        System.out.println("You have been in debt for 30 turns. The bank forecloses on your properties. You are ruined.");
        this.isRunning = false;
      }
    } else {
      this.debtTimer = 0;
    }
  }

  // Getters for global flags requested by Room class
  /** @return true if crops are ready to harvest */
  public boolean isCropsReady() { return this.cropsReady; }
  /** @return true if cattle resources are available */
  public boolean isResourcesAvailable() { return this.resourcesAvailable; }
}
