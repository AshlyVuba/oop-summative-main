package za.co.wethinkcode.ridehub.bikes;

public class ElectricBike extends Bike {

    public static final double UNLOCK_FEE = 10.00;
    public static final double PRICE_PER_MINUTE = 1.50;

    private final int batteryPercent;

    public ElectricBike(String serialNo, int batteryPercent) {
        super(serialNo, "ELECTRIC");
        if (batteryPercent < 0 || batteryPercent > 100) {
            throw new IllegalArgumentException("Battery percentage must be between 0 and 100");
        }
        this.batteryPercent = batteryPercent;
    }

    public int getBatteryPercent() {
        return batteryPercent;
    }

    @Override
    public double unlockFee() {
        return UNLOCK_FEE;
    }

    @Override
    public double pricePerMinute() {
        return PRICE_PER_MINUTE;
    }
}
