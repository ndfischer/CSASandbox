import java.util.ArrayList;
import java.util.Scanner;

/**
 * Manages the main game loop, world instantiation, command processing, and turn mechanics.
 *
 * Centralizes the application state, coordinates between the Player, Rooms, and Items,
 * and maintains the execution sequence until a win or loss condition is met.
 */
public class Game {
  private ArrayList<Room> rooms;
  private Player player;
  private boolean playing;

  // Global Game State Flags & Counters
  private boolean cropsReady = false;
  private boolean resourcesAvailable = false;
  private boolean loanAvailable = true;
  
  private int turnCount = 0;
  private int plantTimer = 0;
  private int cattleTimer = 0;
  private int bondTimer = 0;
  private int loanCooldownTimer = 0;
  private int debtTimer = 0;

  // Temporary loan negotiation states
  private boolean pendingLoan = false;
  private double pendingInterestRate = 0.0;
  private double amountOwed = 0.0;

  /**
   * Constructs the Game instance, initializes the world, and places the player.
   */
  public Game() {
    rooms = new ArrayList<Room>();
    player = new Player();
    playing = true;
    setupWorld();
  }

  /**
   * Initializes all rooms, items, and connections based exactly on the spec.
   */
  private void setupWorld() {
    Room suttersFort = new Room("Sutter's Fort", "");
    suttersFort.addItem(new Item("Field", "fixture; target of 'plant seeds'"));
    
    Room americanRiver = new Room("American River", "The American River runs cold over dark gravel. A battered tin pan sits on a rock at the bank. Specks of yellow shimmer in the riverbed — you could pan here.");
    americanRiver.addItem(new Item("Pan", "fixture"));
    
    Room sierraMine = new Room("Sierra Mine", "");
    sierraMine.addItem(new Item("Coal Vein", "fixture; target of 'mine coal'"));
    sierraMine.addItem(new Item("Gold Vein", "fixture; target of 'mine gold'"));
    
    Room californioRancho = new Room("Californio Rancho", "");
    californioRancho.addItem(new Item("Pasture", "fixture"));
    
    Room sacramento = new Room("Sacramento", "The supply town at the confluence of two rivers. Signs point south to the store, north to the newspaper, east to the bank, and west home.");
    
    Room brannansStore = new Room("Brannan's Store", "");
    brannansStore.addItem(new Item("Counter", "fixture; site of all buy and sell actions"));
    
    Room dailyAlta = new Room("Daily Alta", "A newsboy hawks copies of the Alta California — you could read one for the date and the headline. A carriage road climbs north toward Nob Hill.");
    dailyAlta.addItem(new Item("Newspaper", "fixture; target of 'read'"));
    
    Room wellsFargo = new Room("Wells Fargo Bank", "The offices of Wells Fargo and Co. Brass railings, heavy ledgers, strongboxes. A teller looks up. 'Need a loan? Just say the word.'");
    wellsFargo.addItem(new Item("Teller's Window", "fixture; site of 'loan'"));
    
    Room sfExchange = new Room("San Francisco Exchange", "");
    sfExchange.addItem(new Item("Chalkboard", "fixture; site of 'buy store' and 'invest railroad'"));
    
    Room bigFourMansion = new Room("Big Four Mansion", "The marble foyer of the Big Four Mansion atop Nob Hill. A lobbyist extends a hand. The leather chair by the window is the Railroad Baron's — for $10,000, you could buy baron status and secure your fortune.");
    bigFourMansion.addItem(new Item("Baron's Chair", "fixture; target of 'buy baron'"));

    // Set up exits
    suttersFort.addExit("north", "American River");
    suttersFort.addExit("east", "Sacramento");
    suttersFort.addExit("south", "Californio Rancho");
    suttersFort.addExit("west", "Sierra Mine");

    americanRiver.addExit("south", "Sutter's Fort");

    sierraMine.addExit("east", "Sutter's Fort");

    californioRancho.addExit("north", "Sutter's Fort");

    sacramento.addExit("north", "Daily Alta");
    sacramento.addExit("east", "Wells Fargo Bank");
    sacramento.addExit("south", "Brannan's Store");
    sacramento.addExit("west", "Sutter's Fort");

    brannansStore.addExit("north", "Sacramento");

    dailyAlta.addExit("south", "Sacramento");
    dailyAlta.addExit("north", "Big Four Mansion"); // Guarded via condition in move logic

    wellsFargo.addExit("west", "Sacramento");
    wellsFargo.addExit("east", "San Francisco Exchange");

    sfExchange.addExit("west", "Wells Fargo Bank");

    bigFourMansion.addExit("south", "Daily Alta");

    // Add rooms to global list
    rooms.add(suttersFort);
    rooms.add(americanRiver);
    rooms.add(sierraMine);
    rooms.add(californioRancho);
    rooms.add(sacramento);
    rooms.add(brannansStore);
    rooms.add(dailyAlta);
    rooms.add(wellsFargo);
    rooms.add(sfExchange);
    rooms.add(bigFourMansion);

    player.setCurrentRoom(suttersFort);
  }

  /**
   * Starts the input parsing cycle and game simulation loop.
   */
  public void start() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Welcome to Gold Rush California.\n");

    while (playing) {
      player.updateNetWorth();
      System.out.println("---");
      System.out.println("[" + player.getCurrentRoom().getName() + "]");
      
      System.out.print("> ");
      String input = scanner.nextLine().trim();

      if (input.isEmpty()) continue;
      processCommand(input);
      
      // Check win condition
      if (player.isRailroadBaronPurchased()) {
        System.out.println("You sign the lobbyist's ledger. The chair by the window is yours. California has a new king.");
        playing = false;
        break;
      }
      
      // Check lose condition
      if (debtTimer >= 30) {
        System.out.println("The bank has foreclosed on your properties. You are ruined. Game Over.");
        playing = false;
        break;
      }
    }
    scanner.close();
  }

  /**
   * Finds a room by name using a manual for-each loop search.
   *
   * @param name the exact name of the target room
   * @return the Room object if found, otherwise null
   */
  private Room findRoom(String name) {
    // Manual loop required - built-in search methods are not allowed per AP CS A constraints
    for (Room r : rooms) {
      if (r.getName().equalsIgnoreCase(name)) {
        return r;
      }
    }
    return null;
  }

  /**
   * Converts shortcut keys to full compass directions.
   *
   * @param dir the raw direction string
   * @return the normalized direction string
   */
  private String normalizeDirection(String dir) {
    if (dir.equalsIgnoreCase("w")) return "north";
    if (dir.equalsIgnoreCase("s")) return "south";
    if (dir.equalsIgnoreCase("d")) return "east";
    if (dir.equalsIgnoreCase("a")) return "west";
    if (dir.equalsIgnoreCase("n")) return "north";
    if (dir.equalsIgnoreCase("e")) return "east";
    return dir;
  }

  /**
   * Parses the user command and routes it to the correct logic handler.
   *
   * @param input the raw string inputted by the user
   */
  private void processCommand(String input) {
    String lowerInput = input.toLowerCase();
    boolean validAction = false;
    int turnsToAdvance = 0;

    if (lowerInput.startsWith("go ")) {
      String direction = normalizeDirection(lowerInput.substring(3));
      validAction = attemptMove(direction);
      if (validAction) turnsToAdvance = 1;
    } 
    else if (lowerInput.equals("w") || lowerInput.equals("s") || lowerInput.equals("a") || lowerInput.equals("d")) {
      validAction = attemptMove(normalizeDirection(lowerInput));
      if (validAction) turnsToAdvance = 1;
    }
    else if (lowerInput.equals("help")) {
      printHelp();
    } 
    else if (lowerInput.equals("look")) {
      System.out.println(player.getCurrentRoom().getDescription(this, player));
      System.out.print("You see: ");
      // Manual loop for items
      ArrayList<Item> items = player.getCurrentRoom().getItems();
      if (items.size() == 0) {
        System.out.println("nothing of interest.");
      } else {
        for (int i = 0; i < items.size(); i++) {
          System.out.print(items.get(i).getName() + (i < items.size() - 1 ? ", " : ""));
        }
        System.out.println();
      }
    } 
    else if (lowerInput.equals("inventory")) {
      printInventory();
    } 
    else if (lowerInput.equals("mine coal")) {
      if (player.getCurrentRoom().getName().equalsIgnoreCase("Sierra Mine")) {
        if (player.isT1PickaxeOwned() || player.isT2PickaxeOwned()) {
          int yield = (int)(Math.random() * 151) + 50; // 50 to 200
          player.setCoal(player.getCoal() + yield);
          System.out.println("You swing the pickaxe. Coal cracks loose in chunks.");
          validAction = true;
          turnsToAdvance = 5;
        } else {
          System.out.println("You have no tool to mine any of it.");
        }
      } else {
        System.out.println("You can't do that here.");
      }
    }
    else if (lowerInput.equals("mine gold")) {
      if (player.getCurrentRoom().getName().equalsIgnoreCase("Sierra Mine")) {
        if (player.isT2PickaxeOwned()) {
          int yield = (int)(Math.random() * 61) + 20; // 20 to 80
          player.setGold(player.getGold() + yield);
          System.out.println("Native gold gleams where the stone shatters.");
          validAction = true;
          turnsToAdvance = 5;
        } else {
          System.out.println("The gold veins are still out of reach.");
        }
      } else {
        System.out.println("You can't do that here.");
      }
    }
    else if (lowerInput.equals("plant seeds")) {
      if (player.getCurrentRoom().getName().equalsIgnoreCase("Sutter's Fort")) {
        if (player.getSeeds() > 0 && player.getPlantedSeeds() == 0) {
          player.setPlantedSeeds(player.getSeeds());
          player.setSeeds(0);
          plantTimer = 0;
          System.out.println("You press the seeds into the tilled soil. Now you wait.");
          validAction = true;
          turnsToAdvance = 1;
        } else {
          System.out.println("You have no seeds or have already planted.");
        }
      } else {
        System.out.println("You can't do that here.");
      }
    }
    else if (lowerInput.equals("pan")) {
      if (player.getCurrentRoom().getName().equalsIgnoreCase("American River")) {
        int yield = (int)(Math.random() * 5) + 1; // 1 to 5
        player.setGold(player.getGold() + yield);
        System.out.println("You swirl the pan. A few specks of yellow settle at the bottom.");
        validAction = true;
        turnsToAdvance = 1;
      } else {
        System.out.println("You can't do that here.");
      }
    }
    else if (lowerInput.equals("harvest")) {
      if (player.getCurrentRoom().getName().equalsIgnoreCase("Sutter's Fort")) {
        if (cropsReady) {
          player.setWheat(player.getWheat() + (player.getPlantedSeeds() * 5));
          player.setSeeds(player.getPlantedSeeds());
          player.setPlantedSeeds(0);
          cropsReady = false;
          System.out.println("You cut the wheat. The next round of seeds is in your hand.");
          validAction = true;
          turnsToAdvance = 1;
        } else {
          System.out.println("Crops are not ready.");
        }
      } else {
        System.out.println("You can't do that here.");
      }
    }
    else if (lowerInput.equals("collect")) {
      if (player.getCurrentRoom().getName().equalsIgnoreCase("Californio Rancho")) {
        if (player.getCattleCount() > 0 && resourcesAvailable) {
          player.setCattleGoods(player.getCattleGoods() + (player.getCattleCount() * 5));
          resourcesAvailable = false;
          cattleTimer = 0;
          System.out.println("The vaqueros load the goods into your wagon.");
          validAction = true;
          turnsToAdvance = 1;
        } else {
          System.out.println("Nothing to collect right now.");
        }
      } else {
        System.out.println("You can't do that here.");
      }
    }
    else if (lowerInput.equals("read")) {
      if (player.getCurrentRoom().getName().equalsIgnoreCase("Daily Alta")) {
        // Simple manual date math mapping 1849 365-day year
        int currentDay = turnCount % 365;
        int currentYear = 1849 + (turnCount / 365);
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        int monthIdx = 0;
        int day = currentDay + 1;
        
        // Manual loop required - simulating time manually
        for (int i = 0; i < 12; i++) {
          if (day > daysInMonth[i]) {
            day -= daysInMonth[i];
            monthIdx++;
          } else {
            break;
          }
        }
        
        System.out.println(monthNames[monthIdx] + " " + day + ", " + currentYear + " - GOLD FEVER SWEEPS THE NATION!");
        // Does not advance turn
      } else {
        System.out.println("You can't do that here.");
      }
    }
    else if (lowerInput.equals("loan")) {
      if (player.getCurrentRoom().getName().equalsIgnoreCase("Wells Fargo Bank")) {
        if (!player.isLoanTaken() && loanAvailable) {
          pendingLoan = true;
          pendingInterestRate = 0.10 + (Math.random() * 0.10); // 10% to 20%
          System.out.println("The teller offers $1000 at " + (int)(pendingInterestRate * 100) + "% interest. Type 'accept loan' or 'deny loan'.");
        } else {
          System.out.println("You already have a loan or must wait before applying again.");
        }
      } else {
        System.out.println("You can't do that here.");
      }
    }
    else if (lowerInput.equals("accept loan")) {
      if (player.getCurrentRoom().getName().equalsIgnoreCase("Wells Fargo Bank") && pendingLoan) {
        player.setLiquidMoney(player.getLiquidMoney() + 1000);
        player.setLoanTaken(true);
        amountOwed = 1000;
        pendingLoan = false;
        System.out.println("The teller counts out a thousand dollars and stamps the ledger.");
        validAction = true;
        turnsToAdvance = 1;
      } else {
        System.out.println("No pending loan offer.");
      }
    }
    else if (lowerInput.equals("deny loan")) {
      if (player.getCurrentRoom().getName().equalsIgnoreCase("Wells Fargo Bank") && pendingLoan) {
        pendingLoan = false;
        loanAvailable = false;
        loanCooldownTimer = 0;
        System.out.println("You walk away from the teller's window.");
      } else {
        System.out.println("No pending loan offer.");
      }
    }
    else if (lowerInput.equals("pay loan")) {
      if (player.getCurrentRoom().getName().equalsIgnoreCase("Wells Fargo Bank")) {
        if (player.isLoanTaken()) {
          if (player.getLiquidMoney() >= amountOwed) {
            player.setLiquidMoney(player.getLiquidMoney() - amountOwed);
            player.setLoanTaken(false);
            amountOwed = 0;
            debtTimer = 0;
            System.out.println("You hand the teller your dues and he thanks you for your business.");
            validAction = true;
            turnsToAdvance = 1;
          } else {
            System.out.println("You don't have enough to pay off the balance of $" + amountOwed);
          }
        } else {
          System.out.println("You don't owe any money.");
        }
      } else {
        System.out.println("You can't do that here.");
      }
    }
    else if (lowerInput.startsWith("buy ")) {
      processBuy(lowerInput.substring(4));
      // Assume successful buys consume a turn for balance logic, though spec doesn't mandate it. We'll advance 1 turn.
      validAction = true;
      turnsToAdvance = 1;
    }
    else if (lowerInput.startsWith("sell ")) {
      processSell(lowerInput.substring(5));
      validAction = true;
      turnsToAdvance = 1;
    }
    else if (lowerInput.startsWith("invest railroad ")) {
      try {
        double amt = Double.parseDouble(lowerInput.substring(16));
        processInvest(amt);
        validAction = true;
        turnsToAdvance = 1;
      } catch (Exception e) {
        System.out.println("Invalid amount.");
      }
    }
    else if (lowerInput.equals("quit")) {
      playing = false;
    }
    else {
      System.out.println("You can't do that here. Type 'help' for a list of commands.");
    }

    // Apply per-turn mechanics based on successful actions
    if (validAction && turnsToAdvance > 0) {
      // Manual loop to process multi-turn actions correctly
      for (int i = 0; i < turnsToAdvance; i++) {
        processTurn();
      }
    }
  }

  /**
   * Processes purchases based on the given argument.
   *
   * @param item the string of the item to buy
   */
  private void processBuy(String item) {
    String roomName = player.getCurrentRoom().getName();

    if (item.equals("membership") && roomName.equalsIgnoreCase("Brannan's Store")) {
      if (player.getLiquidMoney() >= 100 && !player.isMembershipBought()) {
        player.setLiquidMoney(player.getLiquidMoney() - 100);
        player.setMembershipBought(true);
        System.out.println("Brannan slides a brass token across the counter.");
      } else {
        System.out.println("Cannot buy membership.");
      }
    }
    else if (item.equals("pickaxe") && roomName.equalsIgnoreCase("Brannan's Store")) {
      if (player.isMembershipBought() && player.getLiquidMoney() >= 50 && !player.isT1PickaxeOwned()) {
        player.setLiquidMoney(player.getLiquidMoney() - 50);
        player.setT1PickaxeOwned(true);
        System.out.println("Brannan hands you a heavy iron pickaxe.");
      } else {
        System.out.println("Cannot buy pickaxe.");
      }
    }
    else if (item.equals("upgrade") && roomName.equalsIgnoreCase("Brannan's Store")) {
      if (player.isMembershipBought() && player.isT1PickaxeOwned() && player.getLiquidMoney() >= 500 && !player.isT2PickaxeOwned()) {
        player.setLiquidMoney(player.getLiquidMoney() - 500);
        player.setT2PickaxeOwned(true);
        System.out.println("Brannan unwraps a steel-tipped beauty from oilcloth.");
      } else {
        System.out.println("Cannot buy upgrade.");
      }
    }
    else if (item.equals("cattle") && roomName.equalsIgnoreCase("Brannan's Store")) {
      if (player.isMembershipBought() && player.getLiquidMoney() >= 500) {
        player.setLiquidMoney(player.getLiquidMoney() - 500);
        if (player.getCattleCount() == 0) cattleTimer = 0;
        player.setCattleCount(player.getCattleCount() + 1);
        System.out.println("A vaquero drives your new beast south toward the Rancho.");
      } else {
        System.out.println("Cannot buy cattle.");
      }
    }
    else if (item.equals("seeds") && roomName.equalsIgnoreCase("Brannan's Store")) {
      if (player.isMembershipBought() && player.getLiquidMoney() >= 1) {
        player.setLiquidMoney(player.getLiquidMoney() - 1);
        player.setSeeds(player.getSeeds() + 1);
        System.out.println("Brannan scoops the seeds into a burlap pouch.");
      } else {
        System.out.println("Cannot buy seeds.");
      }
    }
    else if (item.equals("store") && roomName.equalsIgnoreCase("San Francisco Exchange")) {
      if (player.getLiquidMoney() >= 5000 && !player.isLocalStoreOwned()) {
        player.setLiquidMoney(player.getLiquidMoney() - 5000);
        player.setLocalStoreOwned(true);
        System.out.println("You sign the deed. A storefront on Montgomery Street is yours.");
      } else {
        System.out.println("Cannot buy store.");
      }
    }
    else if (item.equals("baron") && roomName.equalsIgnoreCase("Big Four Mansion")) {
      if (player.getLiquidMoney() >= 10000) {
        player.setLiquidMoney(player.getLiquidMoney() - 10000);
        player.setRailroadBaronPurchased(true);
      } else {
        System.out.println("You don't have enough money.");
      }
    }
    else {
      System.out.println("You can't do that here.");
    }
  }

  /**
   * Processes the selling of inventory items based on argument.
   *
   * @param item the string of the item to sell
   */
  private void processSell(String item) {
    if (!player.getCurrentRoom().getName().equalsIgnoreCase("Brannan's Store")) {
      System.out.println("You can't do that here.");
      return;
    }

    if (!player.isMembershipBought()) {
      System.out.println("You can't buy until you've paid your dues.");
    }

    boolean sold = false;
    if (item.equals("gold") && player.getGold() >= 1) {
      player.setLiquidMoney(player.getLiquidMoney() + (player.getGold() * 2.50));
      player.setGold(0);
      sold = true;
    } else if (item.equals("coal") && player.getCoal() >= 1) {
      player.setLiquidMoney(player.getLiquidMoney() + (player.getCoal() * 0.50));
      player.setCoal(0);
      sold = true;
    } else if (item.equals("wheat") && player.getWheat() >= 1) {
      player.setLiquidMoney(player.getLiquidMoney() + (player.getWheat() * 5.0));
      player.setWheat(0);
      sold = true;
    } else if (item.equals("goods") && player.getCattleGoods() >= 1) {
      player.setLiquidMoney(player.getLiquidMoney() + (player.getCattleGoods() * 10.0));
      player.setCattleGoods(0);
      sold = true;
    }

    if (sold) {
      System.out.println("Brannan counts coins onto the counter.");
    } else {
      System.out.println("You have nothing to sell of that type.");
    }
  }

  /**
   * Processes investing in a railroad bond.
   *
   * @param amount the double value to invest
   */
  private void processInvest(double amount) {
    if (!player.getCurrentRoom().getName().equalsIgnoreCase("San Francisco Exchange")) {
      System.out.println("You can't do that here.");
      return;
    }
    if (amount > 0 && player.getLiquidMoney() >= amount && !player.isRailroadBondOwned()) {
      player.setLiquidMoney(player.getLiquidMoney() - amount);
      player.setRailroadBondOwned(true);
      player.setBondPrincipal(amount);
      bondTimer = 0;
      System.out.println("You buy a bond on the Central Pacific. The clerk seals it in an envelope.");
    } else {
      System.out.println("Cannot invest that amount.");
    }
  }

  /**
   * Attempts to move the player through exits mapped to strings.
   *
   * @param direction the resolved string direction
   * @return true if movement succeeded, false otherwise
   */
  private boolean attemptMove(String direction) {
    Room current = player.getCurrentRoom();
    ArrayList<String> exits = current.getExits();
    
    // Manual loop required
    for (int i = 0; i < exits.size(); i++) {
      String mapping = exits.get(i);
      String[] parts = mapping.split(":");
      if (parts[0].equalsIgnoreCase(direction)) {
        String destName = parts[1];
        
        // Special condition check for Big Four Mansion
        if (destName.equalsIgnoreCase("Big Four Mansion")) {
          player.updateNetWorth();
          if (player.getNetWorth() < 10000) {
            System.out.println("The doorman waves you off. 'Come back when you're somebody, friend.'");
            return false;
          }
        }
        
        Room destination = findRoom(destName);
        if (destination != null) {
          player.setCurrentRoom(destination);
          System.out.println("You head " + direction + " to " + destName + ".");
          return true;
        }
      }
    }
    System.out.println("You can't go that way.");
    return false;
  }

  /**
   * Executes game world timers and triggers mechanics for one single discrete turn.
   */
  private void processTurn() {
    turnCount++;

    // 2. Crop processing
    if (player.getPlantedSeeds() > 0 && !cropsReady) {
      plantTimer++;
      if (plantTimer >= 5) {
        cropsReady = true;
      }
    }

    // 3. Cattle processing
    if (player.getCattleCount() > 0 && !resourcesAvailable) {
      cattleTimer++;
      if (cattleTimer >= 10) {
        resourcesAvailable = true;
      }
    }

    // 4. Local store passive income
    if (player.isLocalStoreOwned()) {
      int passive = (int)(Math.random() * 101) + 50; // 50 to 150
      player.setLiquidMoney(player.getLiquidMoney() + passive);
    }

    // 5. Railroad bond maturation based on spec logic rules
    if (player.isRailroadBondOwned()) {
      bondTimer++;
      if (bondTimer >= 20) {
        double p = player.getBondPrincipal();
        double earnings = 0.0;
        int roll = (int)(Math.random() * 100) + 1; // 1 to 100
        
        if (roll <= 30) {
          double multiplier = 1.10 + (Math.random() * 0.25); // 10% to 35%
          earnings = p * multiplier;
        } else {
          int coin = (int)(Math.random() * 2);
          if (coin == 0) {
            earnings = p * 1.75;
          } else {
            earnings = p * 2.35;
          }
        }
        
        player.setLiquidMoney(player.getLiquidMoney() + earnings);
        System.out.println("Your railroad bond matured! It yielded a return of $" + (int)earnings);
        player.setRailroadBondOwned(false);
        player.setBondPrincipal(0);
        bondTimer = 0;
      }
    }

    // Loan processing logic (interest & cooldown)
    if (player.isLoanTaken()) {
      debtTimer++;
      if (debtTimer % 10 == 0) {
        amountOwed += amountOwed * pendingInterestRate;
        System.out.println("Notice: Bank interest applied. You now owe $" + (int)amountOwed);
      }
    }

    if (!loanAvailable && !pendingLoan && !player.isLoanTaken()) {
      loanCooldownTimer++;
      if (loanCooldownTimer >= 7) {
        loanAvailable = true;
      }
    }
  }

  /**
   * Prints the available command list.
   */
  private void printHelp() {
    System.out.println("Available commands: go [direction], look, inventory, mine coal, mine gold, plant seeds, pan, harvest, collect, read, loan, accept loan, deny loan, pay loan, buy [thing], sell [item], invest railroad [amount], w, a, s, d, quit.");
  }

  /**
   * Prints out the player's counters and flag states.
   */
  private void printInventory() {
    player.updateNetWorth();
    System.out.println("Counters:");
    System.out.println("  Liquid Money: $" + player.getLiquidMoney());
    System.out.println("  Gold: " + player.getGold() + "g");
    System.out.println("  Coal: " + player.getCoal() + "g");
    System.out.println("  Wheat: " + player.getWheat() + " units");
    System.out.println("  Seeds: " + player.getSeeds() + " units");
    System.out.println("  Cattle Goods: " + player.getCattleGoods() + " units");
    System.out.println("  Net Worth: $" + player.getNetWorth());
    System.out.println("Owned Flags:");
    System.out.println("  Membership: " + player.isMembershipBought());
    System.out.println("  T1 Pickaxe: " + player.isT1PickaxeOwned());
    System.out.println("  T2 Pickaxe: " + player.isT2PickaxeOwned());
    System.out.println("  Local Store Deed: " + player.isLocalStoreOwned());
    System.out.println("  Railroad Bond: " + player.isRailroadBondOwned());
    System.out.println("  Loan Active: " + player.isLoanTaken());
  }

  // Allow room to get global game flags easily
  /** @return if crops are ready */
  public boolean isCropsReady() { return cropsReady; }
  /** @return if resources are available */
  public boolean isResourcesAvailable() { return resourcesAvailable; }
  /** @return debt timer counter */
  public int getDebtTimer() { return debtTimer; }
}
