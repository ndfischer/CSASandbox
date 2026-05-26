# Gold Rush California
**Nico Fischer, Carlos Guo, Ivy Bock, Ved Vasudev Jayaram**

A text adventure set in 1850s California. The player starts at Sutter's Fort and builds a fortune through farming, panning, mining, and ranching to buy "Railroad Baron" status at the Big Four Mansion.

---

## Output Files

The Gem must produce exactly the following Java source files:

- `Main.java`
- `Game.java`
- `Room.java`
- `Player.java`
- `Item.java`
- `Ore.java`
- `Gold.java`
- `Coal.java`
- `Crop.java`
- `Wheat.java`
- `Livestock.java`
- `Cattle.java`

---

## Command Vocabulary

The game accepts only the verbs listed below. Any other input prints: "You can't do that here. Type 'help' for a list of commands."

- `go [direction]` — move N/S/E/W to an adjacent room
- `help` — list every command in this vocabulary.
- `look` — reprint the current room's description.
- `inventory` — list the player's counters (liquidMoney, gold, coal, wheat, seeds, cattleGoods) and owned flags (membership, pickaxe, upgrade, deed, bond, loan).
- `mine coal` — triggers the coal mining interaction
- `mine gold` — triggers the gold mining interaction
- `plant seeds` — uses seeds on field
- `pan` — pan for gold (American River only).
- `harvest` — cut ready wheat (Sutter's Fort only).
- `collect` — gather cattle goods (Californio Rancho only).
- `read` — read the newspaper (Daily Alta only).
- `loan` — request a loan (can only take one at a time) with a random (10% to 20%) interest rate. Interest calculated every 10 turns (Wells Fargo only).
- `accept loan` — accept the loan that Wells Fargo offers. (Wells Fargo only)
- `deny loan` — deny the loan that Wells Fargo offers. You will be able to request a new loan in 7 turns. (Wells Fargo only)
- `buy [thing]` — purchase. Valid things: `membership`, `pickaxe`, `upgrade`, `cattle`, `seeds`, `store`, `baron`.
- `sell [item]` — sell stock. Valid items: `gold`, `coal`, `wheat`, `goods`.
- `invest railroad [amount]` — buy a railroad bond. Railroad bonds have randomized returns (SF Exchange only).
- `w` — move north to an adjacent room.
- `s` — move south to an adjacent room.
- `d` — move east to an adjacent room.
- `a` — move west to an adjacent room.

Commands are case-insensitive. Commands valid only in certain rooms print "You can't do that here." if attempted elsewhere.

---

## I. Global Game State

**Boolean flags** (drive world changes):

- `cropsReady`: Default (False) - Becomes True when `plantTimer` reaches 5. Allows harvest and changes Sutter's Fort description.
- `resourcesAvailable`: Default (False) - Becomes True when `cattleTimer` reaches 10. Allows collect and changes Californio Rancho description.
- `membershipBought`: Default (False) - Becomes True via "buy membership". Unlocks Brannan's buy actions and changes the store description.
- `t1PickaxeOwned`: Default (False) - Becomes True via "buy pickaxe". Enables "mine coal" and changes Sierra Mine description.
- `t2PickaxeOwned`: Default (False) - Becomes True via "buy upgrade". Enables "mine gold" and changes Sierra Mine description.
- `localStoreOwned`: Default (False) - Becomes True via "buy store". Adds $50-$150 to `liquidMoney` each turn and changes SF Exchange description.
- `railroadBondOwned`: Default (False) - Becomes True via "invest railroad". Starts `bondTimer` and changes SF Exchange description.
- `loanTaken`: Default (False) - Becomes True via "loan". Prevents the one-time loan from being taken twice.
- `loanAvailable`: Default (True) - Becomes false when the player denies a loan. Becomes true again after 7 turns.
- `railroadBaronPurchased`: Default (False) - Win Condition. Becomes True via "buy baron". Ends the game in victory, requires $10k

**Counters** (driven by interaction logic; all are doubles; defaults shown):

- `liquidMoney` (50), `gold` (0), `coal` (0), `wheat` (0), `seeds` (3), `plantedSeeds` (0), `cattleCount` (0), `cattleGoods` (0), `bondPrincipal` (0), `turn` (0).
- `plantTimer` (0), `cattleTimer` (0), `bondTimer` (0) - per-turn counters that trip `cropsReady` at 5, `resourcesAvailable` at 10, and bond maturity at 20.
- `netWorth` - recomputed each turn as `liquidMoney + (gold * 2.50) + (coal * 0.50) + (wheat * 5) + (cattleGoods * 10) + (cattleCount * 500) + bondPrincipal`. Gates the Big Four Mansion at $10,000.

---

## II. Class Hierarchy

- **Base Class:** `Item` (fields: `String name`, `String description`)
- **Subclass:** `Ore` (Extra field: `double pricePerGram`)
  - `Gold`: pricePerGram = 2.50
  - `Coal`: pricePerGram = 0.50
- **Subclass:** `Crop` (Extra field: `int sellPrice`)
  - `Wheat`: sellPrice = 5; grows in 5 turns
- **Subclass:** `Livestock` (Extra fields: `int productionInterval`, `int productionAmount`, `int goodsSellPrice`)
  - `Cattle`: productionInterval = 10, productionAmount = 5, goodsSellPrice = 10

Single-instance items (pickaxes, membership, deeds, bond, baron status) are tracked by boolean variables on the `Player` class.

---

## III. Room Definitions

### Sutter's Fort (START)
- **Description (`cropsReady == False`):** "Timber walls enclose a patch of tilled soil. To the north lies the American River; east, Sacramento. The year is 1849. Your fields lie quiet — if you had seeds, you could use them on the field."
- **Description (`cropsReady == True`):** "Your wheat is golden and ripe. You could harvest it now."
- **Items:** Field (fixture; target of "plant seeds")
- **Exits:**
  - North to American River | **Condition:** None
  - East to Sacramento | **Condition:** None
  - South to Californio Rancho | **Condition:** None
  - West to Sierra Mine | **Condition:** None

### American River
- **Description:** "The American River runs cold over dark gravel. A battered tin pan sits on a rock at the bank. Specks of yellow shimmer in the riverbed — you could pan here."
- **Items:** Pan (fixture)
- **Exits:**
  - South to Sutter's Fort | **Condition:** None

### Sierra Mine
- **Description (`t1PickaxeOwned == False` and `t2PickaxeOwned == False`):** "Coal veins streak the walls; gold gleams from deeper stone. You have no tool to mine any of it."
- **Description (`t1PickaxeOwned == True` and `t2PickaxeOwned == False`):** "Coal seams are soft enough for your iron pickaxe — use the pickaxe on the coal vein to mine it. The gold veins are still too hard to crack."
- **Description (`t2PickaxeOwned == True`):** "Coal and gold are both within reach. Your steel pickaxe bites cleanly into either — use the pickaxe to mine them."
- **Items:** Coal Vein (fixture; target of "mine coal"), Gold Vein (fixture; target of "mine gold")
- **Exits:**
  - East to Sutter's Fort | **Condition:** None

### Californio Rancho
- **Description (`cattleCount == 0`):** "A grassy expanse with adobe outbuildings and empty corrals. A fine place to raise cattle, if you bought some at Brannan's Store."
- **Description (`cattleCount > 0` and `resourcesAvailable == False`):** "Your cattle graze peacefully. They have not produced anything new yet."
- **Description (`cattleCount > 0` and `resourcesAvailable == True`):** "Your cattle have produced fresh beef and milk. You could collect the goods now."
- **Items:** Pasture (fixture)
- **Exits:**
  - North to Sutter's Fort | **Condition:** None

### Sacramento
- **Description:** "The supply town at the confluence of two rivers. Signs point south to the store, north to the newspaper, east to the bank, and west home."
- **Items:** None
- **Exits:**
  - North to Daily Alta | **Condition:** None
  - East to Wells Fargo Bank | **Condition:** None
  - South to Brannan's Store | **Condition:** None
  - West to Sutter's Fort | **Condition:** None

### Brannan's Store
- **Description (`membershipBought == False`):** "Sam Brannan's General Store. He eyes you over a ledger. 'Members only, friend. You can sell to me, but you can't buy until you've paid your dues — buy a membership for $100.'"
- **Description (`membershipBought == True`):** "Brannan tips his hat. 'Welcome back. Buy seeds, a pickaxe, an upgrade, or cattle — or sell me your gold, coal, wheat, or goods.'"
- **Items:** Counter (fixture; site of all buy and sell actions)
- **Exits:**
  - North to Sacramento | **Condition:** None

### Daily Alta California
- **Description:** "A newsboy hawks copies of the Alta California — you could read one for the date and the headline. A carriage road climbs north toward Nob Hill."
- **Items:** Newspaper (fixture; target of "read")
- **Exits:**
  - South to Sacramento | **Condition:** None
  - North to Big Four Mansion | **Condition:** `netWorth >= 10000`
  - **Failure message (condition unmet):** "The doorman waves you off. 'Come back when you're somebody, friend.'"

### Wells Fargo Bank
- **Description:** "The offices of Wells Fargo and Co. Brass railings, heavy ledgers, strongboxes. A teller looks up. 'Need a loan? Just say the word.'"
- **Items:** Teller's Window (fixture; site of "loan")
- **Exits:**
  - West to Sacramento | **Condition:** None
  - East to SF Exchange | **Condition:** None

### San Francisco Exchange
- **Description (`localStoreOwned == False` and `railroadBondOwned == False`):** "The trading floor of the SF Exchange. A chalkboard lists storefront leases and railroad bonds — you could buy a store or invest in a railroad bond."
- **Description (`localStoreOwned == True` and `railroadBondOwned == False`):** "Your storefront deed sits framed by the clerk's desk. You could still invest in a railroad bond."
- **Description (`localStoreOwned == False` and `railroadBondOwned == True`):** "The clerk tracks your railroad bond on his ledger. You could still buy a store."
- **Description (`localStoreOwned == True` and `railroadBondOwned == True`):** "The clerk gestures to your deed and bond. 'Two irons in the fire.'"
- **Items:** Chalkboard (fixture; site of "buy store" and "invest railroad")
- **Exits:**
  - West to Wells Fargo Bank | **Condition:** None

### Big Four Mansion (WIN ROOM)
- **Description:** "The marble foyer of the Big Four Mansion atop Nob Hill. A lobbyist extends a hand. The leather chair by the window is the Railroad Baron's — for $10,000, you could buy baron status and take that seat."
- **Items:** Baron's Chair (fixture; target of "buy baron")
- **Exits:**
  - South to Daily Alta | **Condition:** None

---

## IV. Interaction Logic

### Plant Wheat Seeds
- **Action:** "plant seeds"
- **Location:** Sutter's Fort
- **Prerequisite:** `seeds > 0` and `plantedSeeds == 0`
- **Effect:** Sets `plantedSeeds = seeds`, `seeds = 0`, `plantTimer = 0`. Print: "You press the seeds into the tilled soil. Now you wait."

### Harvest Wheat
- **Action:** "harvest"
- **Location:** Sutter's Fort
- **Prerequisite:** `cropsReady == True`
- **Effect:** Adds `plantedSeeds * 5` to `wheat`. Sets `seeds = plantedSeeds`, `plantedSeeds = 0`, `cropsReady = False`. Print: "You cut the wheat. The next round of seeds is in your hand."

### Pan for Gold
- **Action:** "pan"
- **Location:** American River
- **Prerequisite:** None
- **Effect:** Adds a random integer (1-5) to `gold`. Takes 1 turn. Print: "You swirl the pan. A few specks of yellow settle at the bottom."

### Mine Coal
- **Action:** "mine coal"
- **Location:** Sierra Mine
- **Prerequisite:** `t1PickaxeOwned == True` or `t2PickaxeOwned == True`
- **Effect:** Adds a random integer (50-200) to `coal` over 5 turns (immediately advance the game by 5 turns). Print: "You swing the pickaxe. Coal cracks loose in chunks."

### Mine Gold
- **Action:** "mine gold"
- **Location:** Sierra Mine
- **Prerequisite:** `t2PickaxeOwned == True`
- **Effect:** Adds a random integer (20-80) to `gold` over 5 turns (immediately advance the game by 5 turns). Print: "Native gold gleams where the stone shatters."

### Collect Cattle Goods
- **Action:** "collect"
- **Location:** Californio Rancho
- **Prerequisite:** `cattleCount > 0` and `resourcesAvailable == True`
- **Effect:** Adds `cattleCount * 5` to `cattleGoods`. Sets `resourcesAvailable = False`, `cattleTimer = 0`. Print: "The vaqueros load the goods into your wagon."

### Buy Membership
- **Action:** "buy membership"
- **Location:** Brannan's Store
- **Prerequisite:** `liquidMoney >= 100` and `membershipBought == False`
- **Effect:** Subtracts 100 from `liquidMoney`. Sets `membershipBought = True`. Print: "Brannan slides a brass token across the counter."

### Buy T1 Pickaxe
- **Action:** "buy pickaxe"
- **Location:** Brannan's Store
- **Prerequisite:** `membershipBought == True`, `liquidMoney >= 50`, `t1PickaxeOwned == False`
- **Effect:** Subtracts 50 from `liquidMoney`. Sets `t1PickaxeOwned = True`. Print: "Brannan hands you a heavy iron pickaxe."

### Buy T2 Pickaxe
- **Action:** "buy upgrade"
- **Location:** Brannan's Store
- **Prerequisite:** `membershipBought == True`, `t1PickaxeOwned == True`, `liquidMoney >= 500`, `t2PickaxeOwned == False`
- **Effect:** Subtracts 500 from `liquidMoney`. Sets `t2PickaxeOwned = True`. Print: "Brannan unwraps a steel-tipped beauty from oilcloth."

### Buy Cattle
- **Action:** "buy cattle"
- **Location:** Brannan's Store
- **Prerequisite:** `membershipBought == True` and `liquidMoney >= 500`
- **Effect:** Subtracts 500 from `liquidMoney`. Adds 1 to `cattleCount`. If `cattleCount` was 0, sets `cattleTimer = 0`. Print: "A vaquero drives your new beast south toward the Rancho."

### Buy Seeds
- **Action:** "buy seeds"
- **Location:** Brannan's Store
- **Prerequisite:** `membershipBought == True` and `liquidMoney >= 1`
- **Effect:** Subtracts 1 from `liquidMoney`. Adds 1 to `seeds`. Print: "Brannan scoops the seeds into a burlap pouch."

### Sell Inventory
- **Action:** "sell [item]" where item is `gold`, `coal`, `wheat`, or `goods`
- **Location:** Brannan's Store
- **Prerequisite:** Player has at least 1 of the named item.
- **Effect:** Removes all of the named item. Adds payment at: gold $2.50/g, coal $0.50/g, wheat $5, goods $10. Print: "Brannan counts coins onto the counter."

### Take Loan
- **Action:** "loan"
- **Location:** Wells Fargo Bank
- **Prerequisite:** `loanTaken == False`
- **Effect:** Adds 1000 to `liquidMoney`. Sets `loanTaken = True`. Print: "The teller counts out a thousand dollars and stamps the ledger."

### Pay back loan
- **Action:** "pay loan"
- **Location:** Wells Fargo Bank
- **Prerequisite:** `loanTaken == True`
- **Effect:** Subtracts loan and interest accrued from `liquidMoney`. Sets `loanTaken = False`. Print: "You hand the teller your dues and he thanks you for your business."

### Read Newspaper
- **Action:** "read"
- **Location:** Daily Alta
- **Prerequisite:** None
- **Effect:** Prints `turn` formatted as a date (turn 0 = "January 1, 1849"; each turn = 1 day) plus a one-line headline. Does not advance the turn.

### Buy Local Store Deed
- **Action:** "buy store"
- **Location:** SF Exchange
- **Prerequisite:** `liquidMoney >= 5000` and `localStoreOwned == False`
- **Effect:** Subtracts 5000 from `liquidMoney`. Sets `localStoreOwned = True`. Print: "You sign the deed. A storefront on Montgomery Street is yours."

### Invest in Railroad Bond
- **Action:** "invest railroad [amount]"
- **Location:** SF Exchange
- **Prerequisite:** `amount > 0`, `liquidMoney >= amount`, `railroadBondOwned == False`
- **Effect:** Subtracts `amount` from `liquidMoney`. Sets `railroadBondOwned = True`, `bondPrincipal = amount`, `bondTimer = 0`. Print: "You buy a bond on the Central Pacific. The clerk seals it with red wax."
- **Logic:**
  - There should be a 30% chance for the player to earn a small amount from the speculation
    - This amount should be from 10% to 35%
  - There should be a 70% chance for the player to earn money
    - If the player rolled to make money, the game should roll again to check how much
    - There should be a 50% chance for the player to make 75%
    - There should be a 50% chance for the player to make 135% (multiply principal by 2.35)

### Buy Railroad Baron Status (WIN)
- **Action:** "buy baron"
- **Location:** Big Four Mansion
- **Prerequisite:** `liquidMoney >= 10000`
- **Effect:** Subtracts 10000 from `liquidMoney`. Sets `railroadBaronPurchased = True`. Print: "You sign the lobbyist's ledger. The chair by the window is yours. California has a new king."

### Per-Turn Sequence
At the end of every turn (including each of the 5 turns triggered by mining):
1. Increment `turn`.
2. If `plantedSeeds > 0` and `cropsReady == False`, increment `plantTimer`. If `>= 5`, set `cropsReady = True`.
3. If `cattleCount > 0` and `resourcesAvailable == False`, increment `cattleTimer`. If `>= 10`, set `resourcesAvailable = True`.
4. If `localStoreOwned == True`, add a random integer ($50-$150) to `liquidMoney`.
5. If `railroadBondOwned == True`, increment `bondTimer`. If `>= 20`, add `2 * bondPrincipal` to `liquidMoney`, then reset `railroadBondOwned = False`, `bondPrincipal = 0`, `bondTimer = 0`.
6. Recompute `netWorth = liquidMoney + (gold * 2.50) + (coal * 0.50) + (wheat * 5) + (cattleGoods * 10) + (cattleCount * 500) + bondPrincipal`.
7. If `railroadBaronPurchased == True`, print the winning epilogue and end the game.
8. If the player has been in debt for 30 turns, print the lose epilogue and end the game.

---

## V. Critical Path

1. **Sutter's Fort** (start) - plant seeds. (`plantedSeeds = 3`, `seeds = 0`) Go north.
2. **American River** - Pan several turns. (`gold` increases by 1-5 per pan) Go south, east, south.
3. **Brannan's Store** - Sell gold. (`gold = 0`, `liquidMoney` increases) Go north, west.
4. **Sutter's Fort** - Harvest. (`wheat += 15`, `seeds = 3`, `cropsReady = False`) Plant seeds again. (`plantedSeeds = 3`) Go east, east.
6. **Wells Fargo Bank** - Loan. (`liquidMoney += 1000`, `loanTaken = True`) Go west, south.
7. **Brannan's Store** - Sell wheat. (`wheat = 0`) Buy Membership ($100). (`membershipBought = True`) Buy Pickaxe ($50). (`t1PickaxeOwned = True`) Go north, west, west.
8. **Sierra Mine** - Use Pickaxe on Coal Vein. (`coal` increases by 50-200 over 5 turns) Go east, east, south.
9. **Brannan's Store** - Sell coal. Repeat steps 6-7 until you can afford the upgrade. Buy Upgrade ($500). (`t2PickaxeOwned = True`)
10. **Brannan's Store** - Buy Cattle (optional) ($500). (`cattleCount = 1`, `cattleTimer = 0`) Go north, east, east.
11. **SF Exchange** - Once `liquidMoney >= 5000`, Buy Store. (optional) (`localStoreOwned = True`) Invest Railroad. (`railroadBondOwned = True`) Go west, west, west, west.
12. **Sierra Mine** - Mine gold. (`gold` increases by 20-80 over 5 turns) Cycle mine-and-sell and collect-and-sell to grow `netWorth`, while investing in railroads
13. **Daily Alta** - Once `netWorth >= 10000`, go north.
14. **Big Four Mansion** - Buy Baron once `liquidMoney >= 10000`. (`railroadBaronPurchased = True`) You win.

---

## VI. Item Placement

| Item                  | Type               | Room                        | Used On                                          |
|-----------------------|--------------------|-----------------------------|--------------------------------------------------|
| Wheat Seeds           | counter (Player)   | Brannan's Store             | Field (Sutter's Fort)                            |
| Wheat                 | Crop               | Sutter's Fort               | Counter (Brannan's Store) - sell at $5 each      |
| Gold                  | Ore                | American River, Sierra Mine | Counter (Brannan's Store) - sell at $2.50/gram   |
| Coal                  | Ore                | Sierra Mine                 | Counter (Brannan's Store) - sell at $0.50/gram   |
| T1 Pickaxe            | boolean flag       | Brannan's Store             | Coal (Sierra Mine)                               |
| T2 Pickaxe            | boolean flag       | Brannan's Store             | Gold (Sierra Mine)                               |
| Membership            | boolean flag       | Brannan's Store             | Counter - unlocks buying                         |
| Cattle                | Livestock          | Brannan's Store             | Pasture (Californio Rancho)                      |
| Cattle Goods          | counter (Player)   | Californio Rancho           | Counter (Brannan's Store) - sell at $10 each     |
| Loan                  | boolean flag       | Wells Fargo Bank            | One-time $1,000 cash advance                     |
| Local Store Deed      | boolean flag       | SF Exchange                 | Chalkboard - $50-$150 per turn passive           |
| Railroad Bond         | boolean flag       | SF Exchange                 | Chalkboard - pays a random amt after 20 turns    |
| Railroad Baron Status | boolean flag (win) | Big Four Mansion            | Baron's Chair - ends the game in victory         |
