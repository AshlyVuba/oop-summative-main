package za.co.wethinkcode.ridehub.bikes;

import java.util.Locale;

public class Bike {

    public String serialNo;
    private final String type;
    private boolean available = true;

    public Bike(String serialNo, String type) {
        this.serialNo = serialNo;
        this.type = type;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public String getType() {
        return type;
    }

    public boolean isAvailable() {
        return available;
    }

    public void markUnavailable() {
        this.available = false;
    }

    public void markAvailable() {
        this.available = true;
    }

    public abstract double unlockFee();

    public abstract double pricePerMinute();

    public double tripCost(int minutes) {
        return unlockFee() + pricePerMinute() * minutes;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "%s[serialNo=%s, unlockFee=%.2f, pricePerMinute=%.2f]",
                getClass().getSimpleName(), serialNo, unlockFee(), pricePerMinute());
    }
}
