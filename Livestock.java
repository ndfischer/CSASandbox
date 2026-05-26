public class Livestock extends Item {
    protected int productionInterval;
    protected int productionAmount;
    protected int goodsSellPrice;

    public Livestock(String name, String description, int productionInterval, int productionAmount, int goodsSellPrice) {
        super(name, description);
        this.productionInterval = productionInterval;
        this.productionAmount = productionAmount;
        this.goodsSellPrice = goodsSellPrice;
    }

    public int getProductionInterval() { return productionInterval; }
    public int getProductionAmount() { return productionAmount; }
    public int getGoodsSellPrice() { return goodsSellPrice; }
}
