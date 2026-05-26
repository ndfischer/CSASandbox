public class Crop extends Item {
    protected int sellPrice;

    public Crop(String name, String description, int sellPrice) {
        super(name, description);
        this.sellPrice = sellPrice;
    }

    public int getSellPrice() {
        return sellPrice;
    }
}
