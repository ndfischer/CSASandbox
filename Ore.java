public class Ore extends Item {
    protected double pricePerGram;

    public Ore(String name, String description, double pricePerGram) {
        super(name, description);
        this.pricePerGram = pricePerGram;
    }

    public double getPricePerGram() {
        return pricePerGram;
    }
}
