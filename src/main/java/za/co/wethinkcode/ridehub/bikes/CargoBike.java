package za.co.wethinkcode.ridehub.bikes;

/**
 * TODO (Q3.2): implement this class as described in README.md.
 * The signatures below are complete; the bodies are placeholders.
 */
public class CargoBike extends Bike {

    public CargoBike(String serialNo, int loadCapacityKg) {
        super(serialNo, "CARGO");
    }

    public int getLoadCapacityKg() {
        return 0;
    }

    @Override
    public double unlockFee() {
        return 0;
    }

    @Override
    public double pricePerMinute() {
        return 0;
    }
}
