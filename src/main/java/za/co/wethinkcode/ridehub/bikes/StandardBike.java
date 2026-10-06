package za.co.wethinkcode.ridehub.bikes;

public class StandardBike extends Bike {

    public static final double UNLOCK_FEE = 5.00;
    public static final double PRICE_PER_MINUTE = 0.75;

    public StandardBike(String serialNo) {
        super(serialNo, "STANDARD");
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
