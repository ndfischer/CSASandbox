import java.time.LocalDate;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private Player player;
    private Room currentRoom;
    private Random random;
    private boolean isRunning;

    public Game() {
        player = new Player();
        random = new Random();
        isRunning = true;
        initializeMap();
    }

    private void initializeMap() {
        Room suttersFort = new Room("Sutter's Fort");
        Room americanRiver = new Room("American River");
        Room sierraMine = new Room("Sierra Mine");
        Room californioRancho = new Room("Californio Rancho");
        Room sacramento = new Room("Sacramento");
        Room brannansStore = new Room("Brannan's Store");
        Room dailyAlta = new Room("Daily Alta California");
        Room wellsFargo = new Room("Wells Fargo Bank");
        Room sfExchange = new Room("San Francisco Exchange");
        Room bigFourMansion = new Room("Big Four Mansion");

        // Sutter's Fort Exits
        suttersFort.setExit("north", americanRiver);
        suttersFort.setExit("east", sacramento);
        suttersFort.setExit("south", californioRancho);
        suttersFort.setExit("west", sierraMine);

        // American River Exits
        americanRiver.setExit("south", suttersFort);

        // Sierra Mine Exits
        sierraMine.setExit("east", suttersFort);

        // Californio Rancho Exits
        californioRancho.setExit("north", suttersFort);

        // Sacramento Exits
        sacramento.setExit("north", dailyAlta);
        sacramento.setExit("east", wellsFargo);
        sacramento.setExit("south", brannansStore);
        sacramento.setExit("west", suttersFort);

        // Brannan's Store Exits
        brannansStore.setExit("north", sacramento);

        // Daily Alta Exits
        dailyAlta.setExit("south", sacramento);
        dailyAlta.setExit("north", bigFourMansion);

        // Wells Fargo Exits
        wellsFargo.setExit("west", sacramento);
        wellsFargo.setExit("east", sfExchange);

        // SF Exchange Exits
        sfExchange.setExit("west", wellsFargo);

        // Big Four Mansion Exits
        bigFourMansion.setExit("south", dailyAlta);

        currentRoom = suttersFort; // Start Room
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("--- GOLD RUSH CALIFORNIA ---");
        System.out.println(currentRoom.getDescription(player));

        while (isRunning) {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim();
            processCommand(input);
        }
        scanner.close();
    }

    private void processCommand(String input) {
        String lowerInput = input.toLowerCase();

        // 1. HELP COMMAND
        if (lowerInput.equals("help")) {
            printHelp();
            return;
        }

        // 2. LOOK COMMAND
        if (lowerInput.equals("look")) {
            System.out.println(currentRoom.getDescription(player));
            return;
        }

        // 3. INVENTORY COMMAND
        if (lowerInput.equals("inventory")) {
            printInventory();
            return;
        }

        // 4. MOVEMENT COMMANDS
        if (lowerInput.startsWith("go ")) {
            String direction = lowerInput.substring(3).trim();
            handleMovement(direction);
            return;
        }

        // 5. INTERACTION ACTIONS
        if (lowerInput.equals("pan")) {
            if (!currentRoom.getName().equals("American River")) {
                System.out.println("You can't do that here.");
                return;
            }
            int gainedGold = random.nextInt(5) + 1; // 1-5
            player.gold += gainedGold;
            System.out.println("You swirl the pan. A few specks of yellow settle at the bottom.");
            advanceTurn();
            return;
        }

        if (lowerInput.equals("mine coal")) {
            if (!currentRoom.getName().equals("Sierra Mine")) {
                System.out.println("You can't do that here.");
                return;
            }
            if (!player.t1PickaxeOwned && !player.t2PickaxeOwned) {
                System.out.println("You have no tool to mine any of it.");
                return;
            }
            int gainedCoal = random.nextInt(151) + 50; // 50-200
            player.coal += gainedCoal;
            System.out.println("You swing the pickaxe. Coal cracks loose in chunks.");
            for (int i = 0; i < 5; i++) {
                advanceTurn();
            }
            return;
        }

        if (lowerInput.equals("mine gold")) {
            if (!currentRoom.getName().equals("Sierra Mine")) {
                System.out.println("You can't do that here.");
                return;
            }
            if (!player.t2PickaxeOwned) {
                System.out.println("The gold veins are still too hard to crack.");
                return;
            }
            int gainedGold = random.nextInt(61) + 20; // 20-80
            player.gold += gainedGold;
            System.out.println("Native gold gleams where the stone shatters.");
            for (int i = 0; i < 5; i++) {
                advanceTurn();
            }
            return;
        }

        if (lowerInput.equals("plant seeds")) {
            if (!currentRoom.getName().equals("Sutter's Fort")) {
                System.out.println("You can't do that here.");
                return;
            }
            if (player.seeds <= 0) {
                System.out.println("You don't have any seeds to plant!");
                return;
            }
            if (player.plantedSeeds > 0) {
                System.out.println("A field is already growing here!");
                return;
            }
            player.plantedSeeds = player.seeds;
            player.seeds = 0;
            player.plantTimer = 0;
            System.out.println("You press the seeds into the tilled soil. Now you wait.");
            advanceTurn();
            return;
        }

        if (lowerInput.equals("harvest")) {
            if (!currentRoom.getName().equals("Sutter's Fort")) {
                System.out.println("You can't do that here.");
                return;
            }
            if (!player.cropsReady) {
                System.out.println("There is nothing ready to harvest.");
                return;
            }
            player.wheat += player.plantedSeeds * 5;
            player.seeds = player.plantedSeeds;
            player.plantedSeeds = 0;
            player.cropsReady = false;
            System.out.println("You cut the wheat. The next round of seeds is in your hand.");
            advanceTurn();
            return;
        }

        if (lowerInput.equals("collect")) {
            if (!currentRoom.getName().equals("Californio Rancho")) {
                System.out.println("You can't do that here.");
                return;
            }
            if (player.cattleCount <= 0 || !player.resourcesAvailable) {
                System.out.println("Nothing to collect right now.");
                return;
            }
            player.cattleGoods += player.cattleCount * 5;
            player.resourcesAvailable = false;
            player.cattleTimer = 0;
            System.out.println("The vaqueros load the goods into your wagon.");
            advanceTurn();
            return;
        }

        if (lowerInput.equals("read")) {
            if (!currentRoom.getName().equals("Daily Alta California")) {
                System.out.println("You can't do that here.");
                return;
            }
            LocalDate gameDate = LocalDate.of(1849, 1, 1).plusDays((long) player.turn);
            System.out.println("[" + gameDate.getMonth() + " " + gameDate.getDayOfMonth() + ", " + gameDate.getYear() + "]");
            System.out.println("Headline: GOLD RUSH INTENSIFIES AS EMIGRANTS FLOOD THE WEST VALLEY!");
            return; // Reading does not advance the turn sequence
        }

        // 6. FINANCING AND LOANS
        if (lowerInput.equals("loan")) {
            if (!currentRoom.getName().equals("Wells Fargo Bank")) {
                System.out.println("You can't do that here.");
                return;
            }
            if (player.loanTaken) {
                System.out.println("You can only take out one loan. Your account is maxed out.");
                return;
            }
            if (!player.loanAvailable) {
                System.out.println("The bank has suspended credit approvals for you right now. Try again later.");
                return;
            }
            player.offeredInterestRate = 5 + random.nextInt(11); // 5% - 15%
            player.loanOffered = true;
            System.out.println("The teller checks your balance. 'We can offer $1000 at a " + (int)player.offeredInterestRate + "% interest rate. Type \"accept loan\" or \"deny loan\".'");
            return;
        }

        if (lowerInput.equals("accept loan")) {
            if (!currentRoom.getName().equals("Wells Fargo Bank") || !player.loanOffered) {
                System.out.println("You can't do that here.");
                return;
            }
            player.liquidMoney += 1000;
            player.loanTaken = true;
            player.loanOffered = false;
            System.out.println("The teller counts out a thousand dollars and stamps the ledger.");
            advanceTurn();
            return;
        }

        if (lowerInput.equals("deny loan")) {
            if (!currentRoom.getName().equals("Wells Fargo Bank") || !player.loanOffered) {
                System.out.println("You can't do that here.");
                return;
            }
            player.loanAvailable = false;
            player.loanDenialCooldown = 7;
            player.loanOffered = false;
            System.out.println("You deny the loan. You will be able to request a new loan in 7 turns.");
            advanceTurn();
            return;
        }

        // 7. BUY ACTIONS
        if (lowerInput.startsWith("buy ")) {
            String itemType = lowerInput.substring(4).trim();
            handleBuy(itemType);
            return;
        }

        // 8. SELL ACTIONS
        if (lowerInput.startsWith("sell ")) {
            String stockType = lowerInput.substring(5).trim();
            handleSell(stockType);
            return;
        }

        // 9. INVESTING
        if (lowerInput.startsWith("invest railroad ")) {
            if (!currentRoom.getName().equals("San Francisco Exchange")) {
                System.out.println("You can't do that here.");
                return;
            }
            if (player.railroadBondOwned) {
                System.out.println("You already hold an active railroad bond.");
                return;
            }
            try {
                double amount = Double.parseDouble(lowerInput.substring(16).trim());
                if (amount <= 0 || player.liquidMoney < amount) {
                    System.out.println("Invalid investment amount or insufficient funds.");
                    return;
                }
                player.liquidMoney -= amount;
                player.railroadBondOwned = true;
                player.bondPrincipal = amount;
                player.bondTimer = 0;
                System.out.println("You buy a bond on the Central Pacific. The clerk seals it with red wax.");
                advanceTurn();
            } catch (NumberFormatException e) {
                System.out.println("You can't do that here. Type 'help' for a list of commands.");
            }
            return;
        }

        // Match no recognizable patterns
        System.out.println("You can't do that here. Type 'help' for a list of commands.");
    }

    private void handleMovement(String direction) {
        Room nextRoom = currentRoom.getExit(direction);
        if (nextRoom == null) {
            System.out.println("You can't go that way.");
            return;
        }

        // Room Transition Validation
        if (currentRoom.getName().equals("Daily Alta California") && direction.equals("north")) {
            if (player.netWorth < 10000) {
                System.out.println("The doorman waves you off. 'Come back when you're somebody, friend.'");
                return;
            }
        }

        currentRoom = nextRoom;
        System.out.println(currentRoom.getDescription(player));
    }

    private void handleBuy(String thing) {
        if (thing.equals("membership")) {
            if (!currentRoom.getName().equals("Brannan's Store")) { System.out.println("You can't do that here."); return; }
            if (player.liquidMoney < 100 || player.membershipBought) { System.out.println("Cannot fulfill purchase right now."); return; }
            player.liquidMoney -= 100;
            player.membershipBought = true;
            System.out.println("Brannan slides a brass token across the counter.");
            advanceTurn();
        } else if (thing.equals("pickaxe")) {
            if (!currentRoom.getName().equals("Brannan's Store")) { System.out.println("You can't do that here."); return; }
            if (!player.membershipBought) { System.out.println("Members only!"); return; }
            if (player.liquidMoney < 50 || player.t1PickaxeOwned) { System.out.println("Cannot fulfill purchase right now."); return; }
            player.liquidMoney -= 50;
            player.t1PickaxeOwned = true;
            System.out.println("Brannan hands you a heavy iron pickaxe.");
            advanceTurn();
        } else if (thing.equals("upgrade")) {
            if (!currentRoom.getName().equals("Brannan's Store")) { System.out.println("You can't do that here."); return; }
            if (!player.membershipBought) { System.out.println("Members only!"); return; }
            if (!player.t1PickaxeOwned) { System.out.println("You need the basic iron pickaxe first before acquiring an upgrade!"); return; }
            if (player.liquidMoney < 500 || player.t2PickaxeOwned) { System.out.println("Cannot fulfill purchase right now."); return; }
            player.liquidMoney -= 500;
            player.t2PickaxeOwned = true;
            System.out.println("Brannan unwraps a steel-tipped beauty from oilcloth.");
            advanceTurn();
        } else if (thing.equals("cattle")) {
            if (!currentRoom.getName().equals("Brannan's Store")) { System.out.println("You can't do that here."); return; }
            if (!player.membershipBought) { System.out.println("Members only!"); return; }
            if (player.liquidMoney < 500) { System.out.println("Insufficient cash setup."); return; }
            player.liquidMoney -= 500;
            player.cattleCount++;
            if (player.cattleCount == 1) player.cattleTimer = 0;
            System.out.println("A vaquero drives your new beast south toward the Rancho.");
            advanceTurn();
        } else if (thing.equals("seeds")) {
            if (!currentRoom.getName().equals("Brannan's Store")) { System.out.println("You can't do that here."); return; }
            if (!player.membershipBought) { System.out.println("Members only!"); return; }
            if (player.liquidMoney < 1) { System.out.println("Insufficient funds."); return; }
            player.liquidMoney -= 1;
            player.seeds++;
            System.out.println("Brannan scoops the seeds into a burlap pouch.");
            advanceTurn();
        } else if (thing.equals("store")) {
            if (!currentRoom.getName().equals("San Francisco Exchange")) { System.out.println("You can't do that here."); return; }
            if (player.liquidMoney < 5000 || player.localStoreOwned) { System.out.println("Cannot fulfill purchase configuration."); return; }
            player.liquidMoney -= 5000;
            player.localStoreOwned = true;
            System.out.println("You sign the deed. A storefront on Montgomery Street is yours.");
            advanceTurn();
        } else if (thing.equals("baron")) {
            if (!currentRoom.getName().equals("Big Four Mansion")) { System.out.println("You can't do that here."); return; }
            if (player.liquidMoney < 100000) { System.out.println("You cannot afford it yet."); return; }
            player.liquidMoney -= 100000;
            player.railroadBaronPurchased = true;
            advanceTurn();
        } else {
            System.out.println("You can't do that here. Type 'help' for a list of commands.");
        }
    }

    private void handleSell(String item) {
        if (!currentRoom.getName().equals("Brannan's Store")) {
            System.out.println("You can't do that here.");
            return;
        }

        if (item.equals("gold")) {
            if (player.gold < 1) { System.out.println("Nothing to sell."); return; }
            player.liquidMoney += player.gold * 0.50;
            player.gold = 0;
            System.out.println("Brannan counts coins onto the counter.");
            advanceTurn();
        } else if (item.equals("coal")) {
            if (player.coal < 1) { System.out.println("Nothing to sell."); return; }
            player.liquidMoney += player.coal * 0.05;
            player.coal = 0;
            System.out.println("Brannan counts coins onto the counter.");
            advanceTurn();
        } else if (item.equals("wheat")) {
            if (player.wheat < 1) { System.out.println("Nothing to sell."); return; }
            player.liquidMoney += player.wheat * 5.0;
            player.wheat = 0;
            System.out.println("Brannan counts coins onto the counter.");
            advanceTurn();
        } else if (item.equals("goods")) {
            if (player.cattleGoods < 1) { System.out.println("Nothing to sell."); return; }
            player.liquidMoney += player.cattleGoods * 10.0;
            player.cattleGoods = 0;
            System.out.println("Brannan counts coins onto the counter.");
            advanceTurn();
        } else {
            System.out.println("You can't do that here. Type 'help' for a list of commands.");
        }
    }

    private void advanceTurn() {
        player.turn++;

        // Crop growth tracker
        if (player.plantedSeeds > 0 && !player.cropsReady) {
            player.plantTimer++;
            if (player.plantTimer >= 5) {
                player.cropsReady = true;
            }
        }

        // Livestock assembly production tracker
        if (player.cattleCount > 0 && !player.resourcesAvailable) {
            player.cattleTimer++;
            if (player.cattleTimer >= 10) {
                player.resourcesAvailable = true;
            }
        }

        // Passive store production mechanics
        if (player.localStoreOwned) {
            int dynamicYield = random.nextInt(101) + 50; // $50-$150
            player.liquidMoney += dynamicYield;
        }

        // Bond Maturity Checks
        if (player.railroadBondOwned) {
            player.bondTimer++;
            if (player.bondTimer >= 20) {
                player.liquidMoney += 2 * player.bondPrincipal;
                player.railroadBondOwned = false;
                player.bondPrincipal = 0;
                player.bondTimer = 0;
                System.out.println("\n[NEWS] Your Railroad Bond matured! Paid out 2x principal!");
            }
        }

        // Bank Loan Denial Cooldown Tracking
        if (!player.loanAvailable && player.loanDenialCooldown > 0) {
            player.loanDenialCooldown--;
            if (player.loanDenialCooldown == 0) {
                player.loanAvailable = true;
            }
        }

        // Bank Loan interest reporting every 10 turns
        if (player.loanTaken && ((int)player.turn % 10 == 0)) {
            System.out.println("\n[WELLS FARGO] Interest processed on your active credit line.");
        }

        player.recalculateNetWorth();

        // Check Victory Trigger Conditions
        if (player.railroadBaronPurchased) {
            System.out.println("\n=======================================================");
            System.out.println("You sign the lobbyist's ledger. The chair by the window is yours. California has a new king.");
            System.out.println("Victory achieved! You became a legendary Western Railroad Baron!");
            System.out.println("=======================================================");
            isRunning = false;
        }
    }

    private void printInventory() {
        player.recalculateNetWorth();
        System.out.println("=== PLAYER PROFILE & INVENTORY ===");
        System.out.println("--- Counters ---");
        System.out.printf(" Liquid Cash: $%.2f\n", player.liquidMoney);
        System.out.printf(" Net Worth:   $%.2f\n", player.netWorth);
        System.out.println(" Gold Stock:  " + (int)player.gold + "g");
        System.out.println(" Coal Stock:  " + (int)player.coal + "g");
        System.out.println(" Wheat Yield: " + (int)player.wheat);
        System.out.println(" Crop Seeds:  " + (int)player.seeds);
        System.out.println(" Ranch Goods: " + (int)player.cattleGoods);
        System.out.println(" Cattle Herd: " + (int)player.cattleCount);
        System.out.println("--- Flags & Asset Ownership ---");
        System.out.println(" Store Membership: " + (player.membershipBought ? "YES" : "NO"));
        System.out.println(" Iron Pickaxe:      " + (player.t1PickaxeOwned ? "YES" : "NO"));
        System.out.println(" Steel Upgrade:     " + (player.t2PickaxeOwned ? "YES" : "NO"));
        System.out.println(" Store Lease Deed:  " + (player.localStoreOwned ? "YES" : "NO"));
        System.out.println(" Railroad Bond:     " + (player.railroadBondOwned ? "YES" : "NO"));
        System.out.println(" Bank Debt Taken:   " + (player.loanTaken ? "YES" : "NO"));
    }

    private void printHelp() {
        System.out.println("=== COMMAND LIST ===");
        System.out.println(" go [direction]            - Move north, south, east, or west.");
        System.out.println(" help                      - Reprint this instructions list.");
        System.out.println(" look                      - Redescribe your current room environment.");
        System.out.println(" inventory                 - Review your wallet balance, assets, and properties.");
        System.out.println(" mine coal                 - Extract charcoal vectors inside the deep quarry.");
        System.out.println(" mine gold                 - Extract native raw bullion veins.");
        System.out.println(" plant seeds               - Scatter crop bases across tilled fields.");
        System.out.println(" pan                       - Sweep down elements inside cold river waters.");
        System.out.println(" harvest                   - Reap complete wheat crops.");
        System.out.println(" collect                   - Gather pasture farm collections.");
        System.out.println(" read                      - Review today's print press edition.");
        System.out.println(" loan                      - Poll the bank teller for credit offers.");
        System.out.println(" accept loan               - Sign binding transaction notes.");
        System.out.println(" deny loan                 - Walk away from active financing offers.");
        System.out.println(" buy [thing]               - Purchase items (membership, pickaxe, upgrade, cattle, seeds, store, baron).");
        System.out.println(" sell [item]               - Liquidate supplies (gold, coal, wheat, goods).");
        System.out.println(" invest railroad [amount]  - Capitalize regional transit bonds.");
    }
}
