import java.util.ArrayList;
import java.util.Scanner;

/**
 * Core engine holding global state, command routing, and world management.
 *
 * Owns all counters, booleans, and timers specified by the game logic and
 * is responsible for advancing turns and computing conditions.
 */
public class Game {
  // Global boolean flags
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

  // Internal flags for nuanced interactions
  private boolean pendingLoan = false;

  // Core counters
  private double liquidMoney = 50.0;
  private double gold = 0.0;
  private double coal = 0.0;
  private double wheat = 0.0;
  private double seeds = 3.0;
  private double plantedSeeds = 0.0;
  private double cattleCount = 0.0;
  private double cattleGoods = 0.0;
  private double bondPrincipal = 0.0;
  private int turn = 0;

  // Turn tracking counters
  private int plantTimer = 0;
  private int cattleTimer = 0;
  private int bondTimer = 0;
  private double netWorth = 0.0;
  private int loanDenyTimer = 0;
  private int debtTimer = 0;
  private int loanTurns = 0;
  private double loanBalance = 0.0;
  private double loanInterestRate = 0.0;

  private ArrayList<Room> rooms;
  private Player player;
  private boolean running;

  /**
   * Constructs the game, initializes rooms, items, and places the player.
   */
  public Game() {
    player = new Player();
    rooms = new ArrayList<Room>();
    setupRooms();
    player.setCurrentRoom(findRoom("Sutter's Fort"));
  }

  /**
   * Initializes the world architecture and links rooms together.
   */
  private void setupRooms() {
    Room suttersFort = new Room("Sutter's Fort", ""); 
    suttersFort.addExit("North:American River");
    suttersFort.addExit("East:Sacramento");
    suttersFort.addExit("South:Californio Rancho");
    suttersFort.addExit("West:Sierra Mine");
    suttersFort.addItem(new Item("Field", "A patch of tilled soil."));
    rooms.add(suttersFort);

    Room americanRiver = new Room("American River", "The American River runs cold over dark gravel. A battered tin pan sits on a rock at the bank. Specks of yellow shimmer in the riverbed — you could pan here.");
    americanRiver.addExit("South:Sutter's Fort");
    americanRiver.addItem(new Item("Pan", "A battered tin pan."));
    rooms.add(americanRiver);

    Room sierraMine = new Room("Sierra Mine", "");
    sierraMine.addExit("East:Sutter's Fort");
    sierraMine.addItem(new Coal("Coal Vein", "Coal seams streaking the walls."));
    sierraMine.addItem(new Gold("Gold Vein", "Gold gleaming from deeper stone."));
    rooms.add(sierraMine);

    Room rancho = new Room("Californio Rancho", "");
    rancho.addExit("North:Sutter's Fort");
    rancho.addItem(new Item("Pasture", "A grassy expanse."));
    rooms.add(rancho);

    Room sacramento = new Room("Sacramento", "The supply town at the confluence of two rivers. Signs point south to the store, north to the newspaper, east to the bank, and west home.");
    sacramento.addExit("North:Daily Alta");
    sacramento.addExit("East:Wells Fargo Bank");
    sacramento.addExit("South:Brannan's Store");
    sacramento.addExit("West:Sutter's Fort");
    rooms.add(sacramento);

    Room store = new Room("Brannan's Store", "");
    store.addExit("North:Sacramento");
    store.addItem(new Item("Counter", "The store counter."));
    rooms.add(store);

    Room dailyAlta = new Room("Daily Alta", "A newsboy hawks copies of the Alta California — you could read one for the date and the headline. A carriage road climbs north toward Nob Hill.");
    dailyAlta.addExit("South:Sacramento");
    dailyAlta.addExit("North:Big Four Mansion");
    dailyAlta.addItem(new Item("Newspaper", "A copy of the Alta California."));
    rooms.add(dailyAlta);

    Room bank = new Room("Wells Fargo Bank", "The offices of Wells Fargo and Co. Brass railings, heavy ledgers, strongboxes. A teller looks up. 'Need a loan? Just say the word.'");
    bank.addExit("West:Sacramento");
    bank.addExit("East:San Francisco Exchange");
    bank.addItem(new Item("Teller's Window", "The teller's window."));
    rooms.add(bank);

    Room exchange = new Room("San Francisco Exchange", "");
    exchange.addExit("West:Wells Fargo Bank");
    exchange.addItem(new Item("Chalkboard", "A chalkboard listing storefront leases and railroad bonds."));
    rooms.add(exchange);

    Room mansion = new Room("Big Four Mansion", "The marble foyer of the Big Four Mansion atop Nob Hill. A lobbyist extends a hand. The leather chair by the window is the Railroad Baron's — for $10,000, you could buy baron status and take that seat.");
    mansion.addExit("South:Daily Alta");
    mansion.addItem(new Item("Baron's Chair", "The leather chair by the window."));
    rooms.add(mansion);
  }

  /**
   * Retrieves a Room instance by its exact string name.
   *
   * @param name the name of the room to locate
   * @return the Room if found, otherwise null
   */
  public Room findRoom(String name) {
    // Manual loop required — built-in search methods are not allowed
    for (Room r : rooms) {
      if (r.getName().equalsIgnoreCase(name)) {
        return r;
      }
    }
    return null;
  }

  /**
   * Initiates the game's REPL prompt loop.
   */
  public void start() {
    running = true;
    Scanner scanner = new Scanner(System.in);
    System.out.println("Welcome to Gold Rush California.");
    System.out.println(player.getCurrentRoom().getDescription(this));

    while (running) {
      System.out.print("\n> ");
      String input = scanner.nextLine();
      processCommand(input);
    }
    scanner.close();
  }

  /**
   * Parses text input to trigger movements or specific interactions.
   *
   * @param input the raw string provided by the user
   */
  public void processCommand(String input) {
    String cmd = input.toLowerCase().trim();
    boolean commandSuccess = false;
    boolean recognizedCommand = true;

    if (cmd.equals("quit")) {
      System.out.println("Thanks for playing!");
      running = false;
      return;
    } else if (cmd.equals("help")) {
      System.out.println("Commands: go [direction], help, look, inventory, mine coal, mine gold, plant seeds, pan, harvest, collect, read, loan, accept loan, deny loan, buy [thing], sell [item], invest railroad [amount], w, s, d, a.");
      commandSuccess = true;
    } else if (cmd.equals("look")) {
      System.out.println(player.getCurrentRoom().getDescription(this));
      System.out.print("Items here: ");
      if (player.getCurrentRoom().getItems().size() == 0) {
        System.out.println("none");
      } else {
        for (int i = 0; i < player.getCurrentRoom().getItems().size(); i++) {
          System.out.print(player.getCurrentRoom().getItems().get(i).getName() + " ");
        }
        System.out.println();
      }
      commandSuccess = true;
    } else if (cmd.equals("inventory")) {
      System.out.println("Counters:");
      System.out.println("Liquid Money: $" + liquidMoney);
      System.out.println("Gold: " + gold + "g");
      System.out.println("Coal: " + coal + "g");
      System.out.println("Wheat: " + wheat);
      System.out.println("Seeds: " + seeds);
      System.out.println("Cattle Goods: " + cattleGoods);
      System.out.println("Owned Flags:");
      System.out.println("Membership: " + membershipBought);
      System.out.println("Pickaxe: " + t1PickaxeOwned);
      System.out.println("Upgrade: " + t2PickaxeOwned);
      System.out.println("Deed: " + localStoreOwned);
      System.out.println("Bond: " + railroadBondOwned);
      System.out.println("Loan: " + loanTaken);
      commandSuccess = true;
    } else if (cmd.startsWith("go ")) {
      commandSuccess = attemptMove(cmd.substring(3));
    } else if (cmd.equals("w")) {
      commandSuccess = attemptMove("north");
    } else if (cmd.equals("a")) {
      commandSuccess = attemptMove("west");
    } else if (cmd.equals("s")) {
      commandSuccess = attemptMove("south");
    } else if (cmd.equals("d")) {
      commandSuccess = attemptMove("east");
    } else if (cmd.equals("plant seeds")) {
      if (player.getCurrentRoom().getName().equals("Sutter's Fort")) {
        if (seeds > 0 && plantedSeeds == 0) {
          plantedSeeds = seeds;
          seeds = 0;
          plantTimer = 0;
          System.out.println("You press the seeds into the tilled soil. Now you wait.");
          commandSuccess = true;
        } else {
          System.out.println("You don't have seeds or already planted them.");
        }
      } else {
        System.out.println("You can't do that here.");
      }
    } else if (cmd.equals("harvest")) {
      if (player.getCurrentRoom().getName().equals("Sutter's Fort")) {
        if (cropsReady) {
          wheat += plantedSeeds * 5;
          seeds = plantedSeeds;
          plantedSeeds = 0;
          cropsReady = false;
          System.out.println("You cut the wheat. The next round of seeds is in your hand.");
          commandSuccess = true;
        } else {
          System.out.println("Crops are not ready.");
        }
      } else {
        System.out.println("You can't do that here.");
      }
    } else if (cmd.equals("pan")) {
      if (player.getCurrentRoom().getName().equals("American River")) {
        int amount = (int)(Math.random() * 5) + 1;
        gold += amount;
        System.out.println("You swirl the pan. A few specks of yellow settle at the bottom.");
        commandSuccess = true;
      } else {
        System.out.println("You can't do that here.");
      }
    } else if (cmd.equals("mine coal")) {
      if (player.getCurrentRoom().getName().equals("Sierra Mine")) {
        if (t1PickaxeOwned || t2PickaxeOwned) {
          int amount = (int)(Math.random() * 151) + 50;
          coal += amount;
          System.out.println("You swing the pickaxe. Coal cracks loose in chunks.");
          for (int i = 0; i < 5; i++) {
            turn++;
            processTurn();
          }
          return;
        } else {
          System.out.println("You have no tool to mine any of it.");
        }
      } else {
        System.out.println("You can't do that here.");
      }
    } else if (cmd.equals("mine gold")) {
      if (player.getCurrentRoom().getName().equals("Sierra Mine")) {
        if (t2PickaxeOwned) {
          int amount = (int)(Math.random() * 61) + 20;
          gold += amount;
          System.out.println("Native gold gleams where the stone shatters.");
          for (int i = 0; i < 5; i++) {
            turn++;
            processTurn();
          }
          return;
        } else {
          System.out.println("Your current pickaxe isn't strong enough.");
        }
      } else {
        System.out.println("You can't do that here.");
      }
    } else if (cmd.equals("collect")) {
      if (player.getCurrentRoom().getName().equals("Californio Rancho")) {
        if (cattleCount > 0 && resourcesAvailable) {
          cattleGoods += cattleCount * 5;
          resourcesAvailable = false;
          cattleTimer = 0;
          System.out.println("The vaqueros load the goods into your wagon.");
          commandSuccess = true;
        } else {
          System.out.println("No goods to collect right now.");
        }
      } else {
        System.out.println("You can't do that here.");
      }
    } else if (cmd.equals("loan")) {
      if (player.getCurrentRoom().getName().equals("Wells Fargo Bank")) {
        if (loanTaken) {
          System.out.println("You already have an active loan.");
        } else if (!loanAvailable) {
          System.out.println("The bank isn't offering loans right now. Come back later.");
        } else {
          loanInterestRate = ((int)(Math.random() * 11) + 10) / 100.0;
          pendingLoan = true;
          System.out.println("The teller offers a loan of $1000 at " + (int)(loanInterestRate * 100) + "% interest. Type 'accept loan' or 'deny loan'.");
        }
      } else {
        System.out.println("You can't do that here.");
      }
    } else if (cmd.equals("accept loan")) {
      if (player.getCurrentRoom().getName().equals("Wells Fargo Bank")) {
        if (pendingLoan) {
          liquidMoney += 1000;
          loanBalance = 1000;
          loanTaken = true;
          pendingLoan = false;
          System.out.println("The teller counts out a thousand dollars and stamps the ledger.");
          commandSuccess = true;
        } else {
          System.out.println("You have no pending loan offer.");
        }
      } else {
        System.out.println("You can't do that here.");
      }
    } else if (cmd.equals("deny loan")) {
      if (player.getCurrentRoom().getName().equals("Wells Fargo Bank")) {
        if (pendingLoan) {
          pendingLoan = false;
          loanAvailable = false;
          loanDenyTimer = 0;
          System.out.println("You turn down the offer.");
          commandSuccess = true;
        } else {
          System.out.println("You have no pending loan offer.");
        }
      } else {
        System.out.println("You can't do that here.");
      }
    } else if (cmd.startsWith("buy ")) {
      String thing = cmd.substring(4);
      if (player.getCurrentRoom().getName().equals("Brannan's Store")) {
        if (thing.equals("membership")) {
          if (liquidMoney >= 100 && !membershipBought) {
            liquidMoney -= 100;
            membershipBought = true;
            System.out.println("Brannan slides a brass token across the counter.");
            commandSuccess = true;
          }
        } else if (thing.equals("pickaxe")) {
          if (membershipBought && liquidMoney >= 50 && !t1PickaxeOwned) {
            liquidMoney -= 50;
            t1PickaxeOwned = true;
            System.out.println("Brannan hands you a heavy iron pickaxe.");
            commandSuccess = true;
          }
        } else if (thing.equals("upgrade")) {
          if (membershipBought && t1PickaxeOwned && liquidMoney >= 500 && !t2PickaxeOwned) {
            liquidMoney -= 500;
            t2PickaxeOwned = true;
            System.out.println("Brannan unwraps a steel-tipped beauty from oilcloth.");
            commandSuccess = true;
          }
        } else if (thing.equals("cattle")) {
          if (membershipBought && liquidMoney >= 500) {
            liquidMoney -= 500;
            cattleCount++;
            if (cattleCount == 1) cattleTimer = 0;
            System.out.println("A vaquero drives your new beast south toward the Rancho.");
            commandSuccess = true;
          }
        } else if (thing.equals("seeds")) {
          if (membershipBought && liquidMoney >= 1) {
            liquidMoney -= 1;
            seeds++;
            System.out.println("Brannan scoops the seeds into a burlap pouch.");
            commandSuccess = true;
          }
        }
      } else if (player.getCurrentRoom().getName().equals("San Francisco Exchange")) {
        if (thing.equals("store")) {
          if (liquidMoney >= 5000 && !localStoreOwned) {
            liquidMoney -= 5000;
            localStoreOwned = true;
            System.out.println("You sign the deed. A storefront on Montgomery Street is yours.");
            commandSuccess = true;
          }
        }
      } else if (player.getCurrentRoom().getName().equals("Big Four Mansion")) {
        if (thing.equals("baron")) {
          if (liquidMoney >= 10000) {
            liquidMoney -= 10000;
            railroadBaronPurchased = true;
            System.out.println("You sign the lobbyist's ledger. The chair by the window is yours. California has a new king.");
            commandSuccess = true;
          }
        }
      } else {
        System.out.println("You can't do that here.");
      }
    } else if (cmd.startsWith("sell ")) {
      if (player.getCurrentRoom().getName().equals("Brannan's Store")) {
        String item = cmd.substring(5);
        if (item.equals("gold") && gold > 0) {
          liquidMoney += gold * 2.50;
          gold = 0;
          System.out.println("Brannan counts coins onto the counter.");
          commandSuccess = true;
        } else if (item.equals("coal") && coal > 0) {
          liquidMoney += coal * 0.50;
          coal = 0;
          System.out.println("Brannan counts coins onto the counter.");
          commandSuccess = true;
        } else if (item.equals("wheat") && wheat > 0) {
          liquidMoney += wheat * 5;
          wheat = 0;
          System.out.println("Brannan counts coins onto the counter.");
          commandSuccess = true;
        } else if (item.equals("goods") && cattleGoods > 0) {
          liquidMoney += cattleGoods * 10;
          cattleGoods = 0;
          System.out.println("Brannan counts coins onto the counter.");
          commandSuccess = true;
        } else {
          System.out.println("You can't sell that, or you don't have enough.");
        }
      } else {
        System.out.println("You can't do that here.");
      }
    } else if (cmd.startsWith("invest railroad ")) {
      if (player.getCurrentRoom().getName().equals("San Francisco Exchange")) {
        try {
          double amount = Double.parseDouble(cmd.substring(16));
          if (amount > 0 && liquidMoney >= amount && !railroadBondOwned) {
            liquidMoney -= amount;
            railroadBondOwned = true;
            bondPrincipal = amount;
            bondTimer = 0;
            System.out.println("You buy a bond on the Central Pacific. The clerk seals it with red wax.");
            commandSuccess = true;
          } else {
            System.out.println("Invalid amount, not enough money, or you already own a bond.");
          }
        } catch (NumberFormatException e) {
          System.out.println("Invalid amount.");
        }
      } else {
        System.out.println("You can't do that here.");
      }
    } else if (cmd.equals("read")) {
      if (player.getCurrentRoom().getName().equals("Daily Alta")) {
        int[] monthDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        int tempDays = turn;
        int year = 1849;
        while (tempDays >= 365) {
          tempDays -= 365;
          year++;
        }
        int monthIdx = 0;
        while (tempDays >= monthDays[monthIdx]) {
          tempDays -= monthDays[monthIdx];
          monthIdx++;
        }
        int day = tempDays + 1;
        System.out.println(monthNames[monthIdx] + " " + day + ", " + year + " - NEW STRIKE DISCOVERED UP THE MOUNTAIN!");
        return; // Reading does not progress time
      } else {
        System.out.println("You can't do that here.");
      }
    } else {
      recognizedCommand = false;
    }

    if (!recognizedCommand) {
      System.out.println("You can't do that here. Type 'help' for a list of commands.");
    } else if (commandSuccess) {
      turn++;
      processTurn();
    }
  }

  /**
   * Helper method to process movement constraints and state.
   *
   * @param dir string representing the desired direction
   * @return true if the movement succeeded
   */
  private boolean attemptMove(String dir) {
    // Manual loop over ArrayList per structural requirements
    for (int i = 0; i < player.getCurrentRoom().getExits().size(); i++) {
      String exitStr = player.getCurrentRoom().getExits().get(i);
      String[] parts = exitStr.split(":");
      if (parts[0].equalsIgnoreCase(dir)) {
        String destination = parts[1];
        if (destination.equals("Big Four Mansion") && netWorth < 10000) {
          System.out.println("The doorman waves you off. 'Come back when you're somebody, friend.'");
          return false;
        }
        Room nextRoom = findRoom(destination);
        if (nextRoom != null) {
          player.setCurrentRoom(nextRoom);
          System.out.println(nextRoom.getDescription(this));
          return true;
        }
      }
    }
    System.out.println("You can't go that way.");
    return false;
  }

  /**
   * Executes scheduled background logic for timers, investments, and global updates.
   */
  public void processTurn() {
    if (plantedSeeds > 0 && !cropsReady) {
      plantTimer++;
      if (plantTimer >= 5) {
        cropsReady = true;
      }
    }
    if (cattleCount > 0 && !resourcesAvailable) {
      cattleTimer++;
      if (cattleTimer >= 10) {
        resourcesAvailable = true;
      }
    }
    if (localStoreOwned) {
      liquidMoney += (int)(Math.random() * 101) + 50;
    }
    if (railroadBondOwned) {
      bondTimer++;
      if (bondTimer >= 20) {
        double returnAmount = 0;
        int chance = (int)(Math.random() * 100) + 1;
        if (chance <= 30) {
          int percent = (int)(Math.random() * 26) + 10;
          returnAmount = bondPrincipal + (bondPrincipal * (percent / 100.0));
        } else {
          int secondRoll = (int)(Math.random() * 100) + 1;
          if (secondRoll <= 50) {
            returnAmount = bondPrincipal * 1.75;
          } else {
            returnAmount = bondPrincipal * 2.35;
          }
        }
        liquidMoney += returnAmount;
        railroadBondOwned = false;
        bondPrincipal = 0;
        bondTimer = 0;
      }
    }
    if (loanTaken) {
      loanTurns++;
      if (loanTurns % 10 == 0) {
        double interest = loanBalance * loanInterestRate;
        loanBalance += interest;
        liquidMoney -= interest;
      }
    }
    if (!loanAvailable && !loanTaken) {
      loanDenyTimer++;
      if (loanDenyTimer >= 7) {
        loanAvailable = true;
      }
    }

    netWorth = liquidMoney + (gold * 2.50) + (coal * 0.50) + (wheat * 5) + (cattleGoods * 10) + (cattleCount * 500) + bondPrincipal;

    if (railroadBaronPurchased) {
      running = false;
      return;
    }

    if (liquidMoney < 0) {
      debtTimer++;
      if (debtTimer >= 10) {
        System.out.println("You have been in debt for 10 turns. The bank forecloses on everything. You lose.");
        running = false;
      }
    } else {
      debtTimer = 0;
    }
  }

  // Getters for dynamic Room descriptions
  public boolean isCropsReady() { return cropsReady; }
  public boolean isT1PickaxeOwned() { return t1PickaxeOwned; }
  public boolean isT2PickaxeOwned() { return t2PickaxeOwned; }
  public double getCattleCount() { return cattleCount; }
  public boolean isResourcesAvailable() { return resourcesAvailable; }
  public boolean isMembershipBought() { return membershipBought; }
  public boolean isLocalStoreOwned() { return localStoreOwned; }
  public boolean isRailroadBondOwned() { return railroadBondOwned; }
}
