import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

/**
 * Core simulation logic engine orchestrating game loop state, vocabulary,
 * spatial navigation, financial ledgers, and turn progression rule sets.
 */
public class Game {
    // Global Game Flags
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
    private boolean gameLost = false;

    // Financial & Resource Counters
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

    // Specialized Tracking Variables
    private double activeLoanRate = 0.0;
    private double loanAccruedInterest = 0.0;
    private double pendingLoanRate = 0.0;
    private boolean loanOffered = false;
    private int debtTurns = 0;
    private int plantTimer = 0;
    private int cattleTimer = 0;
    private int bondTimer = 0;
    private int loanDenialTimer = 0;
    private double netWorth = 50.0;

    private ArrayList<Room> rooms;
    private Player player;
    private boolean isRunning = true;
    private Random random;

    public Game() {
        this.rooms = new ArrayList<>();
        this.player = new Player();
        this.random = new Random();
        this.initializeWorld();
    }

    // Accessors needed for contextual description rendering
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
        Room Sacramento = new Room("Sacramento", "The supply town at the confluence of two rivers. Signs point south to the store, north to the newspaper, east to the bank, and west home.");
        Room brannansStore = new Room("Brannan's Store", "");
        Room dailyAlta = new Room("Daily Alta California", "A newsboy hawks copies of the Alta California — you could read one for the date and the headline. A carriage road climbs north toward Nob Hill.");
        Room wellsFargo = new Room("Wells Fargo Bank", "The offices of Wells Fargo and Co. Brass railings, heavy ledgers, strongboxes. A teller looks up. 'Need a loan? Just say the word.'");
        Room sfExchange = new Room("SF Exchange", "");
        Room bigFourMansion = new Room("Big Four Mansion", "The marble foyer of the Big Four Mansion atop Nob Hill. A lobbyist extends a hand. The leather chair by the window is the Railroad Baron's — for $10,000, you could buy baron status and take that seat.");

        suttersFort.addExit("north", "American River");
        suttersFort.addExit("east", "Sacramento");
        suttersFort.addExit("south", "Californio Rancho");
        suttersFort.addExit("west", "Sierra Mine");

        americanRiver.addExit("south", "Sutter's Fort");
        sierraMine.addExit("east", "Sutter's Fort");
        californioRancho.addExit("north", "Sutter's Fort");

        Sacramento.addExit("north", "Daily Alta California");
        Sacramento.addExit("east", "Wells Fargo Bank");
        Sacramento.addExit("south", "Brannan's Store");
        Sacramento.addExit("west", "Sutter's Fort");

        brannansStore.addExit("north", "Sacramento");
        dailyAlta.addExit("south", "Sacramento");
        dailyAlta.addExit("north", "Big Four Mansion");

        wellsFargo.addExit("west", "Sacramento");
        wellsFargo.addExit("east", "SF Exchange");
        sfExchange.addExit("west", "Wells Fargo Bank");
        bigFourMansion.addExit("south", "Daily Alta California");

        this.rooms.add(suttersFort); this.rooms.add(americanRiver);
        this.rooms.add(sierraMine); this.rooms.add(californioRancho);
        this.rooms.add(Sacramento); this.rooms.add(brannansStore);
        this.rooms.add(dailyAlta); this.rooms.add(wellsFargo);
        this.rooms.add(sfExchange); this.rooms.add(bigFourMansion);

        this.player.setCurrentRoom(suttersFort);
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Gold Rush California!");
        System.out.println(this.player.getCurrentRoom().getDescription(this));

        while (this.isRunning) {
            System.out.print("\n> ");
            if (!scanner.hasNextLine()) break;
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                this.processCommand(line);
            }
        }
        scanner.close();
    }

    private void processCommand(String rawInput) {
        String lowerInput = rawInput.toLowerCase();

        // Single Character WASD Movement Mappings
        if (lowerInput.equals("w")) { this.handleMovement("north"); return; }
        if (lowerInput.equals("s")) { this.handleMovement("south"); return; }
        if (lowerInput.equals("d")) { this.handleMovement("east"); return; }
        if (lowerInput.equals("a")) { this.handleMovement("west"); return; }

        if (lowerInput.startsWith("go ")) {
            this.handleMovement(lowerInput.substring(3).trim());
            return;
        }
        if (lowerInput.equals("help")) {
            System.out.println("Commands: go [direction], w/a/s/d, help, look, inventory, mine coal, mine gold, plant seeds, pan, harvest, collect, read, loan, accept loan, deny loan, pay loan, buy [thing], sell [item], invest railroad [amount]");
            return;
        }
        if (lowerInput.equals("look")) {
            System.out.println(this.player.getCurrentRoom().getDescription(this));
            return;
        }
        if (lowerInput.equals("inventory")) {
            System.out.printf("Cash: $%.2f\nGold: %.2fg\nCoal: %.2fg\nWheat: %.2f\nSeeds: %.2f\nCattle Goods: %.2f\n",
                    this.liquidMoney, this.gold, this.coal, this.wheat, this.seeds, this.cattleGoods);
            System.out.printf("Owned Status Flags: Membership=%b, Pickaxe=%b, Upgrade=%b, Store Owned=%b, Bond Owned=%b, Loan Taken=%b\n",
                    this.membershipBought, this.t1PickaxeOwned, this.t2PickaxeOwned, this.localStoreOwned, this.railroadBondOwned, this.loanTaken);
            return;
        }

        String currentRoom = this.player.getCurrentRoom().getName();

        // Interaction Router
        if (lowerInput.equals("plant seeds")) {
            if (!currentRoom.equals("Sutter's Fort")) { System.out.println("You can't do that here."); return; }
            if (this.seeds > 0 && this.plantedSeeds == 0) {
                this.plantedSeeds = this.seeds;
                this.seeds = 0;
                this.plantTimer = 0;
                System.out.println("You press the seeds into the tilled soil. Now you wait.");
                this.endTurn(1);
            } else {
                System.out.println("No seeds available or crop already growing.");
            }
            return;
        }
        if (lowerInput.equals("harvest")) {
            if (!currentRoom.equals("Sutter's Fort")) { System.out.println("You can't do that here."); return; }
            if (this.cropsReady) {
                this.wheat += (this.plantedSeeds * 5);
                this.seeds = this.plantedSeeds;
                this.plantedSeeds = 0;
                this.cropsReady = false;
                System.out.println("You cut the wheat. The next round of seeds is in your hand.");
                this.endTurn(1);
            } else {
                System.out.println("There is nothing ready to harvest.");
            }
            return;
        }
        if (lowerInput.equals("pan")) {
            if (!currentRoom.equals("American River")) { System.out.println("You can't do that here."); return; }
            int panned = this.random.nextInt(5) + 1;
            this.gold += panned;
            System.out.println("You swirl the pan. A few specks of yellow settle at the bottom.");
            this.endTurn(1);
            return;
        }
        if (lowerInput.equals("mine coal")) {
            if (!currentRoom.equals("Sierra Mine")) { System.out.println("You can't do that here."); return; }
            if (this.t1PickaxeOwned || this.t2PickaxeOwned) {
                int mined = this.random.nextInt(151) + 50;
                this.coal += mined;
                System.out.println("You swing the pickaxe. Coal cracks loose in chunks.");
                this.endTurn(5);
            } else {
                System.out.println("You have no tool to mine any of it.");
            }
            return;
        }
        if (lowerInput.equals("mine gold")) {
            if (!currentRoom.equals("Sierra Mine")) { System.out.println("You can't do that here."); return; }
            if (this.t2PickaxeOwned) {
                int mined = this.random.nextInt(61) + 20;
                this.gold += mined;
                System.out.println("Native gold gleams where the stone shatters.");
                this.endTurn(5);
            } else {
                System.out.println("The gold veins are still too hard to crack.");
            }
            return;
        }
        if (lowerInput.equals("collect")) {
            if (!currentRoom.equals("Californio Rancho")) { System.out.println("You can't do that here."); return; }
            if (this.cattleCount > 0 && this.resourcesAvailable) {
                this.cattleGoods += (this.cattleCount * 5);
                this.resourcesAvailable = false;
                this.cattleTimer = 0;
                System.out.println("The vaqueros load the goods into your wagon.");
                this.endTurn(1);
            } else {
                System.out.println("No resources are currently available to collect.");
            }
            return;
        }
        if (lowerInput.equals("read")) {
            if (!currentRoom.equals("Daily Alta California")) { System.out.println("You can't do that here."); return; }
            int day = ((int)this.turn) + 1;
            System.out.println("Daily Alta California — Day " + day + ", 1849");
            System.out.println("Headline: Markets boom as expansion west pushes forward across California!");
            return;
        }
        if (lowerInput.equals("loan")) {
            if (!currentRoom.equals("Wells Fargo Bank")) { System.out.println("You can't do that here."); return; }
            if (this.loanTaken) {
                System.out.println("You already have an outstanding loan balance.");
                return;
            }
            if (!this.loanAvailable) {
                System.out.println("The bank refuses to extend credit lines at this structural cycle.");
                return;
            }
            this.pendingLoanRate = 0.10 + (this.random.nextInt(11) * 0.01); // 10% to 20%
            this.loanOffered = true;
            System.out.printf("The teller offers a $1000 principal at an interest calculation rate of %.0f%%. Type 'accept loan' or 'deny loan'.\n", (this.pendingLoanRate * 100));
            return;
        }
        if (lowerInput.equals("accept loan")) {
            if (!currentRoom.equals("Wells Fargo Bank") || !this.loanOffered) { System.out.println("You can't do that here."); return; }
            this.liquidMoney += 1000.0;
            this.activeLoanRate = this.pendingLoanRate;
            this.loanTaken = true;
            this.loanOffered = false;
            this.loanAccruedInterest = 0.0;
            System.out.println("The teller counts out a thousand dollars and stamps the ledger.");
            this.endTurn(1);
            return;
        }
        if (lowerInput.equals("deny loan")) {
            if (!currentRoom.equals("Wells Fargo Bank") || !this.loanOffered) { System.out.println("You can't do that here."); return; }
            this.loanAvailable = false;
            this.loanOffered = false;
            this.loanDenialTimer = 0;
            System.out.println("You deny the offer. You will be able to request a new loan in 7 turns.");
            this.endTurn(1);
            return;
        }
        if (lowerInput.equals("pay loan")) {
            if (!currentRoom.equals("Wells Fargo Bank")) { System.out.println("You can't do that here."); return; }
            if (!this.loanTaken) {
                System.out.println("You do not currently have a loan outstanding.");
                return;
            }
            double debtRepaymentTotal = 1000.0 + this.loanAccruedInterest;
            if (this.liquidMoney >= debtRepaymentTotal) {
                this.liquidMoney -= debtRepaymentTotal;
                this.loanTaken = false;
                this.loanAccruedInterest = 0.0;
                this.activeLoanRate = 0.0;
                System.out.println("You hand the teller your dues and he thanks you for your business.");
                this.endTurn(1);
            } else {
                System.out.printf("You do not possess enough liquid asset reserves to pay back the loan total ($%.2f).\n", debtRepaymentTotal);
            }
            return;
        }
        if (lowerInput.startsWith("buy ")) {
            String choice = lowerInput.substring(4).trim();
            this.executePurchase(choice, currentRoom);
            return;
        }
        if (lowerInput.startsWith("sell ")) {
            if (!currentRoom.equals("Brannan's Store")) { System.out.println("You can't do that here."); return; }
            String choice = lowerInput.substring(5).trim();
            this.executeSale(choice);
            return;
        }
        if (lowerInput.startsWith("invest railroad ")) {
            if (!currentRoom.equals("SF Exchange")) { System.out.println("You can't do that here."); return; }
            if (this.railroadBondOwned) { System.out.println("You already hold an active railroad investment certificate."); return; }
            try {
                double amount = Double.parseDouble(lowerInput.substring(16).trim());
                if (amount > 0 && this.liquidMoney >= amount) {
                    this.liquidMoney -= amount;
                    this.bondPrincipal = amount;
                    this.railroadBondOwned = true;
                    this.bondTimer = 0;
                    System.out.println("You buy a bond on the Central Pacific. The clerk seals it with red wax.");
                    this.endTurn(1);
                } else {
                    System.out.println("Invalid allocation volume entry.");
                }
            } catch (Exception ex) {
                System.out.println("Invalid asset amount string signature.");
            }
            return;
        }

        System.out.println("You can't do that here. Type 'help' for a list of commands.");
    }

    private void handleMovement(String direction) {
        Room current = this.player.getCurrentRoom();
        String target = current.getExit(direction);
        if (target == null) {
            System.out.println("You can't go that way.");
            return;
        }
        if (target.equals("Big Four Mansion") && this.netWorth < 10000.0) {
            System.out.println("The doorman waves you off. 'Come back when you're somebody, friend.'");
            return;
        }
        for (Room r : this.rooms) {
            if (r.getName().equalsIgnoreCase(target)) {
                this.player.setCurrentRoom(r);
                System.out.println("Moved to " + r.getName() + ".");
                System.out.println(r.getDescription(this));
                this.endTurn(1);
                return;
            }
        }
    }

    private void executePurchase(String product, String currentRoom) {
        if (currentRoom.equals("Brannan's Store")) {
            if (!this.membershipBought && !product.equals("membership")) {
                System.out.println("Members only, friend. You can sell to me, but you can't buy until you've paid your dues.");
                return;
            }
            if (product.equals("membership")) {
                if (!this.membershipBought && this.liquidMoney >= 100.0) {
                    this.liquidMoney -= 100.0; this.membershipBought = true;
                    System.out.println("Brannan slides a brass token across the counter.");
                    this.endTurn(1);
                } else { System.out.println("Cannot process membership transaction requirements."); }
            } else if (product.equals("pickaxe")) {
                if (!this.t1PickaxeOwned && this.liquidMoney >= 50.0) {
                    this.liquidMoney -= 50.0; this.t1PickaxeOwned = true;
                    System.out.println("Brannan hands you a heavy iron pickaxe.");
                    this.endTurn(1);
                } else { System.out.println("Cannot execute pickaxe asset purchase."); }
            } else if (product.equals("upgrade")) {
                if (this.t1PickaxeOwned && !this.t2PickaxeOwned && this.liquidMoney >= 500.0) {
                    this.liquidMoney -= 500.0; this.t2PickaxeOwned = true;
                    System.out.println("Brannan unwraps a steel-tipped beauty from oilcloth.");
                    this.endTurn(1);
                } else { System.out.println("Upgrade requirements unfulfilled."); }
            } else if (product.equals("cattle")) {
                if (this.liquidMoney >= 500.0) {
                    this.liquidMoney -= 500.0;
                    if (this.cattleCount == 0) this.cattleTimer = 0;
                    this.cattleCount += 1.0;
                    System.out.println("A vaquero drives your new beast south toward the Rancho.");
                    this.endTurn(1);
                } else { System.out.println("Insufficient funds."); }
            } else if (product.equals("seeds")) {
                if (this.liquidMoney >= 1.0) {
                    this.liquidMoney -= 1.0; this.seeds += 1.0;
                    System.out.println("Brannan scoops the seeds into a burlap pouch.");
                    this.endTurn(1);
                } else { System.out.println("Insufficient funds."); }
            } else { System.out.println("Brannan does not sell that item."); }
        } else if (currentRoom.equals("SF Exchange") && product.equals("store")) {
            if (!this.localStoreOwned && this.liquidMoney >= 5000.0) {
                this.liquidMoney -= 5000.0; this.localStoreOwned = true;
                System.out.println("You sign the deed. A storefront on Montgomery Street is yours.");
                this.endTurn(1);
            } else { System.out.println("Store asset option unavailable or capital insufficient."); }
        } else if (currentRoom.equals("Big Four Mansion") && product.equals("baron")) {
            if (this.liquidMoney >= 10000.0) {
                this.liquidMoney -= 10000.0; this.railroadBaronPurchased = true;
                System.out.println("You sign the lobbyist's ledger. The chair by the window is yours. California has a new king.");
                this.endTurn(1);
            } else { System.out.println("You lack the required $10,000 cash execution requirement."); }
        } else {
            System.out.println("That transaction configuration cannot be completed here.");
        }
    }

    private void executeSale(String item) {
        if (item.equals("gold") && this.gold >= 1.0) {
            this.liquidMoney += (this.gold * 2.50); this.gold = 0;
            System.out.println("Brannan counts coins onto the counter."); this.endTurn(1);
        } else if (item.equals("coal") && this.coal >= 1.0) {
            this.liquidMoney += (this.coal * 0.50); this.coal = 0;
            System.out.println("Brannan counts coins onto the counter."); this.endTurn(1);
        } else if (item.equals("wheat") && this.wheat >= 1.0) {
            this.liquidMoney += (this.wheat * 5.0); this.wheat = 0;
            System.out.println("Brannan counts coins onto the counter."); this.endTurn(1);
        } else if (item.equals("goods") && this.cattleGoods >= 1.0) {
            this.liquidMoney += (this.cattleGoods * 10.0); this.cattleGoods = 0;
            System.out.println("Brannan counts coins onto the counter."); this.endTurn(1);
        } else {
            System.out.println("You do not hold inventory blocks of that asset category.");
        }
    }

    private void endTurn(int elapsed) {
        for (int i = 0; i < elapsed; i++) {
            this.turn += 1.0;

            if (!this.loanAvailable) {
                this.loanDenialTimer++;
                if (this.loanDenialTimer >= 7) { this.loanAvailable = true; this.loanDenialTimer = 0; }
            }
            if (this.loanTaken && ((int)this.turn) % 10 == 0) {
                this.loanAccruedInterest += (1000.0 * this.activeLoanRate);
            }
            if (this.liquidMoney < 0.0) {
                this.debtTurns++;
                if (this.debtTurns >= 10) { this.gameLost = true; }
            } else {
                this.debtTurns = 0;
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
            if (this.railroadBondOwned) {
                this.bondTimer++;
                if (this.bondTimer >= 20) {
                    double outputPayout = 0.0;
                    int roll = this.random.nextInt(100);
                    if (roll < 30) {
                        double variant = 0.10 + (this.random.nextInt(26) * 0.01); // 10% to 35%
                        outputPayout = this.bondPrincipal * (1.0 + variant);
                    } else {
                        if (this.random.nextBoolean()) {
                            outputPayout = this.bondPrincipal * 1.75; // Earn 75%
                        } else {
                            outputPayout = this.bondPrincipal * 2.35; // Earn 135% (multiply principal by 2.35)
                        }
                    }
                    this.liquidMoney += outputPayout;
                    System.out.printf("\n[BOND MARKET NOTIFIER] Investment contract matured yielding $%.2f cash payout.\n", outputPayout);
                    this.railroadBondOwned = false; this.bondPrincipal = 0.0; this.bondTimer = 0;
                }
            }

            this.netWorth = this.liquidMoney + (this.gold * 2.50) + (this.coal * 0.50) + (this.wheat * 5.0) + (this.cattleGoods * 10.0) + (this.cattleCount * 500.0) + this.bondPrincipal;

            if (this.gameLost) {
                System.out.println("DEFEAT BALANCE OUTCOME REACHED: You spent 10 consecutive turns in debt. The commercial courts declare you bankrupt.");
                this.isRunning = false;
                return;
            }
            if (this.railroadBaronPurchased) {
                System.out.println("VICTORY ACHIEVEMENT MET: You take your seat at the Big Four Mansion window as a Railroad Baron. California is yours!");
                this.isRunning = false;
                return;
            }
        }
    }
}
