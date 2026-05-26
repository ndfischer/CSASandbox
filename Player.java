public class Player {
    // Boolean flags driven by interaction logic
    public boolean cropsReady = false;
    public boolean resourcesAvailable = false;
    public boolean membershipBought = false;
    public boolean t1PickaxeOwned = false;
    public boolean t2PickaxeOwned = false;
    public boolean localStoreOwned = false;
    public boolean railroadBondOwned = false;
    public boolean loanTaken = false;
    public boolean loanAvailable = true;
    public boolean railroadBaronPurchased = false;

    // Game counters (all doubles)
    public double liquidMoney = 50.0;
    public double gold = 0.0;
    public double coal = 0.0;
    public double wheat = 0.0;
    public double seeds = 3.0;
    public double plantedSeeds = 0.0;
    public double cattleCount = 0.0;
    public double cattleGoods = 0.0;
    public double bondPrincipal = 0.0;
    public double turn = 0.0;

    // Per-turn operational timers
    public double plantTimer = 0.0;
    public double cattleTimer = 0.0;
    public double bondTimer = 0.0;
    public double netWorth = 50.0;

    // Loan processing temporary storage
    public boolean loanOffered = false;
    public double offeredInterestRate = 0.0;
    public int loanDenialCooldown = 0;

    public void recalculateNetWorth() {
        // Recalculates net worth strictly tracking the exact specification equation rules
        this.netWorth = this.liquidMoney + (this.gold * 1.50) + (this.coal * 0.50) + (this.wheat * 5.0) 
                        + (this.cattleGoods * 10.0) + (this.cattleCount * 500.0) + this.bondPrincipal;
    }
}
