import java.util.*;

public class Game {
    private Player player;
    private Room currentRoom;
    private Map<String, Room> rooms;
    private boolean isRunning;
    private Random random;
    private int debtTurns; // Tracks consecutive turns spent in debt

    public Game() {
        player = new Player();
        rooms = new HashMap<>();
        random = new Random();
        isRunning = true;
        debtTurns = 0;
        initializeRooms();
    }

    private void initializeRooms() {
        Room suttersFort = new Room("Sutter's Fort", "");
        Room americanRiver = new Room("American River", "The American River runs cold over dark gravel. A battered tin pan sits on a rock at the bank. Specks of yellow shimmer in the riverbed — you could pan here.");
        Room sierraMine = new Room("Sierra Mine", "");
        Room californioRancho = new Room("Californio Rancho", "");
        Room sacramento = new Room("Sacramento", "The supply town at the confluence of two rivers. Signs point south to the store, north to the newspaper, east to the bank, and west home.");
        Room brannansStore = new Room("Brannan's Store", "");
        Room dailyAlta = new Room("Daily Alta California", "A newsboy hawks copies of the Alta California — you could read one for the date and the headline. A carriage road climbs north toward Nob Hill.");
        Room wellsFargo = new Room("Wells Fargo Bank", "The offices of Wells Fargo and Co. Brass railings, heavy ledgers, strongboxes. A teller looks up. 'Need a loan? Just say the word.'");
        Room sfExchange = new Room("San Francisco Exchange", "");
        Room bigFourMansion = new Room("Big Four Mansion", "The marble foyer of the Big Four Mansion atop Nob Hill. A lobbyist extends a hand. The leather chair by the window is the Railroad Baron's — for $10,000, you could buy baron status and take that seat.");

        rooms.put("Sutter's Fort", suttersFort);
        rooms.put("American River", americanRiver);
        rooms.put("Sierra Mine", sierraMine);
        rooms.put("Californio Rancho", californioRancho);
        rooms.put("Sacramento", sacramento);
        rooms.put("Brannan's Store", brannansStore);
        rooms.put("Daily Alta California", dailyAlta);
        rooms.put("Wells Fargo Bank", wellsFargo);
        rooms.put("San Francisco Exchange", sfExchange);
        rooms.put("Big Four Mansion", bigFourMansion);

        // Connections
        suttersFort.setExit("north", americanRiver);
        suttersFort.setExit("east", sacramento);
        suttersFort.setExit("south", californioRancho);
        suttersFort.setExit("west", sierraMine);

        americanRiver.setExit("south", suttersFort);
        sierraMine.setExit("east", suttersFort);
        californioRancho.setExit("north", suttersFort);

        sacramento.setExit("north", dailyAlta);
        sacramento.setExit("east", wellsFargo);
        sacramento.setExit("south", brannansStore);
        sacramento.setExit("west", suttersFort);

        brannansStore.setExit("north", sacramento);

        dailyAlta.setExit("south", sacramento);
        dailyAlta.setExit("north", bigFourMansion);

        wellsFargo.setExit("west", sacramento);
        wellsFargo.setExit("east", sfExchange);

        sfExchange.setExit("west", wellsFargo);
        bigFourMansion.setExit("south", dailyAlta);

        currentRoom = suttersFort;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        printRoomDescription();

        while (isRunning) {
            System.out.print("> ");
            if (!scanner.hasNextLine()) break;
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.isEmpty()) continue;
            processCommand(input);
        }
        scanner.close();
    }

    private void printRoomDescription() {
        String name = currentRoom.getName();
        if (name.equals("Sutter's Fort")) {
            if (!player.cropsReady) {
                System.out.println("Timber walls enclose a patch of tilled soil. To the north lies the American River; east, Sacramento. The year is 1849. Your fields lie quiet — if you had seeds, you could use them on the field.");
            } else {
                System.out.println("Your wheat is golden and ripe. You could harvest it now.");
            }
        } else if (name.equals("Sierra Mine")) {
            if (!player.t1PickaxeOwned && !player.t2PickaxeOwned) {
                System.out.println("Coal veins streak the walls; gold gleams from deeper stone. You have no tool to mine any of it.");
            } else if (player.t1PickaxeOwned && !player.t2PickaxeOwned) {
                System.out.println("Coal seams are soft enough for your iron pickaxe — use the pickaxe on the coal vein to mine it. The gold veins are still too hard to crack.");
            } else if (player.t2PickaxeOwned) {
                System.out.println("Coal and gold are both within reach. Your steel pickaxe bites cleanly into either — use the pickaxe to mine them.");
            }
        } else if (name.equals("Californio Rancho")) {
            if (player.cattleCount == 0) {
                System.out.println("A grassy expanse with adobe outbuildings and empty corrals. A fine place to raise cattle, if you bought some at Brannan's Store.");
            } else if (player.cattleCount > 0 && !player.resourcesAvailable) {
                System.out.println("Your cattle graze peacefully. They have not produced anything new yet.");
            } else if (player.cattleCount > 0 && player.resourcesAvailable) {
                System.out.println("Your cattle have produced fresh beef and milk. You could collect the goods now.");
            }
        } else if (name.equals("Brannan's Store")) {
            if (!player.membershipBought) {
                System.out.println("Sam Brannan's General Store. He eyes you over a ledger. 'Members only, friend. You can sell to me, but you can't buy until you've paid your dues — buy a membership for $100.'");
            } else {
                System.out.println("Brannan tips his hat. 'Welcome back. Buy seeds, a pickaxe, an upgrade, or cattle — or sell me your gold, coal, wheat, or goods.'");
            }
        } else if (name.equals("San Francisco Exchange")) {
            if (!player.localStoreOwned && !player.railroadBondOwned) {
                System.out.println("The trading floor of the SF Exchange. A chalkboard lists storefront leases and railroad bonds — you could buy a store or invest in a railroad bond.");
            } else if (player.localStoreOwned && !player.railroadBondOwned) {
                System.out.println("Your storefront deed sits framed by the clerk's desk. You could still invest in a railroad bond.");
            } else if (!player.localStoreOwned && player.railroadBondOwned) {
                System.out.println("The clerk tracks your railroad bond on his ledger. You could still buy a store.");
            } else if (player.localStoreOwned && player.railroadBondOwned) {
                System.out.println("The clerk gestures to your deed and bond. 'Two irons in the fire.'");
            }
        } else {
            System.out.println(currentRoom.getDescription());
        }
    }

    private void processCommand(String command) {
        if (command.startsWith("go ") || command.equals("w") || command.equals("s") || command.equals("d") || command.equals("a")) {
            handleMove(command);
        } else if (command.equals("help")) {
            handleHelp();
        } else if (command.equals("look")) {
            printRoomDescription();
        } else if (command.equals("inventory")) {
            handleInventory();
        } else if (command.equals("mine coal")) {
            handleMineCoal();
        } else if (command.equals("mine gold")) {
            handleMineGold();
        } else if (command.equals("plant seeds")) {
            handlePlantSeeds();
        } else if (command.equals("pan")) {
            handlePan();
        } else if (command.equals("harvest")) {
            handleHarvest();
        } else if (command.equals("collect")) {
            handleCollect();
        } else if (command.equals("read")) {
            handleRead();
        } else if (command.equals("loan")) {
            handleLoan();
        } else if (command.equals("accept loan")) {
            handleAcceptLoan();
        } else if (command.equals("deny loan")) {
            handleDenyLoan();
        } else if (command.startsWith("buy ")) {
            handleBuy(command.substring(4));
        } else if (command.startsWith("sell ")) {
            handleSell(command.substring(5));
        } else if (command.startsWith("invest railroad ")) {
            handleInvestRailroad(command.substring(16));
        } else {
            System.out.println("You can't do that here. Type 'help' for a list of commands.");
        }
    }

    private void handleMove(String command) {
        String direction = "";
        if (command.equals("w")) direction = "north";
        else if (command.equals("s")) direction = "south";
        else if (command.equals("d")) direction = "east";
        else if (command.equals("a")) direction = "west";
        else direction = command.substring(3);

        Room nextRoom = currentRoom.getExit(direction);
        if (nextRoom == null) {
            System.out.println("You can't go that way.");
            return;
        }

        if (currentRoom.getName().equals("Daily Alta California") && direction.equals("north")) {
            if (player.netWorth < 10000) {
                System.out.println("The doorman waves you off. 'Come back when you're somebody, friend.'");
                return;
            }
        }

        currentRoom = nextRoom;
        printRoomDescription();
        endTurn();
    }

    private void handleHelp() {
        System.out.println("Commands: go [direction], help, look, inventory, mine coal, mine gold, plant seeds, pan, harvest, collect, read, loan, accept loan, deny loan, buy [thing], sell [item], invest railroad [amount], w, s, d, a");
    }

    private void handleInventory() {
        System.out.printf("Counters -> Money: $%.2f, Gold: %.2fg, Coal: %.2fg, Wheat: %.2f units, Seeds: %.2f, Cattle Goods: %.2f\n",
                player.liquidMoney, player.gold, player.coal, player.wheat, player.seeds, player.cattleGoods);
    }

    private void handleMineCoal() {
        if (!currentRoom.getName().equals("Sierra Mine") || (!player.t1PickaxeOwned && !player.t2PickaxeOwned)) {
            System.out.println("You can't do that here.");
            return;
        }
        System.out.println("You swing the pickaxe. Coal cracks loose in chunks.");
        player.coal += 50 + random.nextInt(151);
        for (int i = 0; i < 5; i++) {
            endTurn();
            if (!isRunning) return;
        }
    }

    private void handleMineGold() {
        if (!currentRoom.getName().equals("Sierra Mine") || !player.t2PickaxeOwned) {
            System.out.println("You can't do that here.");
            return;
        }
        System.out.println("Native gold gleams where the stone shatters.");
        player.gold += 20 + random.nextInt(61);
        for (int i = 0; i < 5; i++) {
            endTurn();
            if (!isRunning) return;
        }
    }

    private void handlePlantSeeds() {
        if (!currentRoom.getName().equals("Sutter's Fort") || player.seeds <= 0 || player.plantedSeeds > 0) {
            System.out.println("You can't do that here.");
            return;
        }
        player.plantedSeeds = player.seeds;
        player.seeds = 0;
        player.plantTimer = 0;
        System.out.println("You press the seeds into the tilled soil. Now you wait.");
        endTurn();
    }

    private void handlePan() {
        if (!currentRoom.getName().equals("American River")) {
            System.out.println("You can't do that here.");
            return;
        }
        player.gold += 1 + random.nextInt(5);
        System.out.println("You swirl the pan. A few specks of yellow settle at the bottom.");
        endTurn();
    }

    private void handleHarvest() {
        if (!currentRoom.getName().equals("Sutter's Fort") || !player.cropsReady) {
            System.out.println("You can't do that here.");
            return;
        }
        player.wheat += player.plantedSeeds * 5;
        player.seeds = player.plantedSeeds;
        player.plantedSeeds = 0;
        player.cropsReady = false;
        System.out.println("You cut the wheat. The next round of seeds is in your hand.");
        endTurn();
    }

    private void handleCollect() {
        if (!currentRoom.getName().equals("Californio Rancho") || player.cattleCount <= 0 || !player.resourcesAvailable) {
            System.out.println("You can't do that here.");
            return;
        }
        player.cattleGoods += player.cattleCount * 5;
        player.resourcesAvailable = false;
        player.cattleTimer = 0;
        System.out.println("The vaqueros load the goods into your wagon.");
        endTurn();
    }

    private void handleRead() {
        if (!currentRoom.getName().equals("Daily Alta")) {
            System.out.println("You can't do that here.");
            return;
        }
        System.out.println("Day " + (int)player.turn + ": Fortunes expanding across California fields and veins!");
    }

    private void handleLoan() {
        if (!currentRoom.getName().equals("Wells Fargo Bank") || player.loanTaken) {
            System.out.println("You can't do that here.");
            return;
        }
        if (!player.loanAvailable) {
            System.out.println("A loan offer is currently locked.");
            return;
        }
        System.out.println("The teller details a loan document. Do you 'accept loan' or 'deny loan'?");
    }

    private void handleAcceptLoan() {
        if (!currentRoom.getName().equals("Wells Fargo Bank") || player.loanTaken) {
            System.out.println("You can't do that here.");
            return;
        }
        player.liquidMoney += 1000;
        player.loanTaken = true;
        System.out.println("The teller counts out a thousand dollars and stamps the ledger.");
        endTurn();
    }

    private void handleDenyLoan() {
        if (!currentRoom.getName().equals("Wells Fargo Bank")) {
            System.out.println("You can't do that here.");
            return;
        }
        player.loanAvailable = false;
        System.out.println("You deny the loan that Wells Fargo offers. You will be able to request a new loan in 7 turns.");
        endTurn();
    }

    private void handleBuy(String thing) {
        if (currentRoom.getName().equals("Brannan's Store")) {
            if (thing.equals("membership") && player.liquidMoney >= 100 && !player.membershipBought) {
                player.liquidMoney -= 100;
                player.membershipBought = true;
                System.out.println("Brannan slides a brass token across the counter.");
                endTurn();
            } else if (player.membershipBought) {
                if (thing.equals("pickaxe") && player.liquidMoney >= 50 && !player.t1PickaxeOwned) {
                    player.liquidMoney -= 50;
                    player.t1PickaxeOwned = true;
                    System.out.println("Brannan hands you a heavy iron pickaxe.");
                    endTurn();
                } else if (thing.equals("upgrade") && player.t1PickaxeOwned && player.liquidMoney >= 500 && !player.t2PickaxeOwned) {
                    player.liquidMoney -= 500;
                    player.t2PickaxeOwned = true;
                    System.out.println("Brannan unwraps a steel-tipped beauty from oilcloth.");
                    endTurn();
                } else if (thing.equals("cattle") && player.liquidMoney >= 500) {
                    player.liquidMoney -= 500;
                    player.cattleCount++;
                    if (player.cattleCount == 1) player.cattleTimer = 0;
                    System.out.println("A vaquero drives your new beast south toward the Rancho.");
                    endTurn();
                } else if (thing.equals("seeds") && player.liquidMoney >= 1) {
                    player.liquidMoney -= 1;
                    player.seeds++;
                    System.out.println("Brannan scoops the seeds into a burlap pouch.");
                    endTurn();
                } else {
                    System.out.println("You can't do that here.");
                }
            } else {
                System.out.println("You can't do that here.");
            }
        } else if (currentRoom.getName().equals("San Francisco Exchange") && thing.equals("store") && player.liquidMoney >= 5000 && !player.localStoreOwned) {
            player.liquidMoney -= 5000;
            player.localStoreOwned = true;
            System.out.println("You sign the deed. A storefront on Montgomery Street is yours.");
            endTurn();
        } else if (currentRoom.getName().equals("Big Four Mansion") && thing.equals("baron") && player.liquidMoney >= 10000) {
            player.liquidMoney -= 10000;
            player.railroadBaronPurchased = true;
            System.out.println("You sign the lobbyist's ledger. The chair by the window is yours. California has a new king.");
            endTurn();
        } else {
            System.out.println("You can't do that here.");
        }
    }

    private void handleSell(String item) {
        if (!currentRoom.getName().equals("Brannan's Store")) {
            System.out.println("You can't do that here.");
            return;
        }
        if (item.equals("gold") && player.gold >= 1) {
            player.liquidMoney += player.gold * 2.50;
            player.gold = 0;
        } else if (item.equals("coal") && player.coal >= 1) {
            player.liquidMoney += player.coal * 0.50;
            player.coal = 0;
        } else if (item.equals("wheat") && player.wheat >= 1) {
            player.liquidMoney += player.wheat * 5.0;
            player.wheat = 0;
        } else if (item.equals("goods") && player.cattleGoods >= 1) {
            player.liquidMoney += player.cattleGoods * 10.0;
            player.cattleGoods = 0;
        } else {
            System.out.println("You can't do that here.");
            return;
        }
        System.out.println("Brannan counts coins onto the counter.");
        endTurn();
    }

    private void handleInvestRailroad(String amountStr) {
        if (!currentRoom.getName().equals("San Francisco Exchange") || player.railroadBondOwned) {
            System.out.println("You can't do that here.");
            return;
        }
        try {
            double amount = Double.parseDouble(amountStr);
            if (amount > 0 && player.liquidMoney >= amount) {
                player.liquidMoney -= amount;
                player.railroadBondOwned = true;
                player.bondPrincipal = amount;
                player.bondTimer = 0;
                System.out.println("You buy a bond on the Central Pacific. The clerk seals it with red wax.");
                endTurn();
            }
        } catch (NumberFormatException e) {
            System.out.println("You can't do that here.");
        }
    }

    private void endTurn() {
        // 1. Increment turn
        player.turn++;

        // 2. Plant Timer logic
        if (player.plantedSeeds > 0 && !player.cropsReady) {
            player.plantTimer++;
            if (player.plantTimer >= 5) player.cropsReady = true;
        }

        // 3. Cattle Timer logic
        if (player.cattleCount > 0 && !player.resourcesAvailable) {
            player.cattleTimer++;
            if (player.cattleTimer >= 10) player.resourcesAvailable = true;
        }

        // 4. Passive store income logic
        if (player.localStoreOwned) {
            player.liquidMoney += 50 + random.nextInt(101);
        }

        // 5. Railroad bond maturity logic
        if (player.railroadBondOwned) {
            player.bondTimer++;
            if (player.bondTimer >= 20) {
                double payout = player.bondPrincipal;
                if (random.nextDouble() < 0.30) {
                    payout += player.bondPrincipal * (0.10 + (random.nextDouble() * 0.25));
                } else {
                    payout += (random.nextBoolean()) ? (player.bondPrincipal * 0.75) : (player.bondPrincipal * 1.35);
                }
                player.liquidMoney += payout;
                player.railroadBondOwned = false;
                player.bondPrincipal = 0;
                player.bondTimer = 0;
            }
        }

        // 6. Recompute Net Worth
        player.netWorth = player.liquidMoney + (player.gold * 2.50) + (player.coal * 0.50) + (player.wheat * 5.0) + (player.cattleGoods * 10.0) + (player.cattleCount * 500.0) + player.bondPrincipal;

        // 7. Win condition evaluation
        if (player.railroadBaronPurchased) {
            System.out.println("Victory! You are now a Railroad Baron.");
            isRunning = false;
            return;
        }

        // 8. CRITICAL REVISED LOSE CONDITION: Check if player has been in debt for 30 turns
        if (player.liquidMoney < 0) {
            debtTurns++;
            if (debtTurns >= 30) {
                System.out.println("LOSE EPILOGUE: Your debts have overwhelmed you. The collectors seize your remaining assets, leaving you destitute on the streets of Sacramento.");
                isRunning = false;
            }
        } else {
            debtTurns = 0;
        }
    }
}
